# Novu notifications

Knowvera sends user-facing notifications through [Novu](https://novu.co). This folder holds the
drafts for the four Novu workflows and the JSON contracts between the API, RabbitMQ and Novu.

```
publisher (API / core)                 RabbitMQ                          webservice                Novu
──────────────────────   ── direct exchange ─► durable queue ──►  NovuQueueProcessor  ── trigger ──►  workflow
ConfirmedPublisher.publish  x.novu-…-queue        q.novu-…        @RabbitListener          (workflowId) ├ in-app
                                                                  Map<String,String>                    └ email
portals ── GET /novu/config ──► { subscriberId, subscriberHash } ── @novu/js Inbox (HMAC-secured)
```

| Workflow id (`app.novu.queue.*NovuId`) | Queue | Sent by | Channels | State |
|---|---|---|---|---|
| `novu-new-user` | `q.novu-new-user` | `KeycloakUserService.registerUser` (individual without an organisation of their own) | in-app, email | works once the template exists |
| `novu-new-org-user` | `q.novu-new-org-user` | `KeycloakUserService.registerUser` (member of an organisation) | in-app, email | works once the template exists |
| `novu-new-org-client-request` | `q.novu-new-org-client-request` | `ClientRequestNotification` (core), after a single client request is saved | email only | works once the template exists; bulk upload sends nothing yet |
| `novu-new-kyc-record` | `q.novu-new-kyc-record` | nobody | in-app, email | proposed; listener ignores messages |

Files:

- `workflows/<id>.json`: per workflow, the queue, publishers, the payload JSON Schema (paste into
  the workflow's *Payload schema* in the Novu dashboard), the steps with subjects and in-app text,
  and an example payload.
- `templates/<id>.email.html`: the email step bodies (Liquid, `{{ payload.x }}`).

Fields marked *(to add)* in the schemas are not sent yet (today only `roleName`, and everything in
`novu-new-kyc-record`). Blank values are left out of the payload rather than sent as `""`. The
templates print plain variables (no Liquid `| default:` filters), so the publishers send the
fallbacks themselves: `platformName` ("Knowvera"), `supportContact` ("our support team") and
`setupLinkValidFor` ("a limited time"). Only `loginUrl` and `platformUrl` can be missing, when no
Settings row exists.

## Setting up Novu

1. In the Novu dashboard (one environment per Knowvera environment), create the four workflows
   with exactly the ids above, or point the API at the ids you have. Novu derives a workflow's id
   from its name when it is created ("New User" → `new-user`) and it cannot be renamed later, so
   it is usually easier to set the API's ids:
   `NOVU_NEW_USER_NOVU_ID`, `NOVU_NEW_ORG_USER_NOVU_ID`, `NOVU_NEW_ORG_CLIENT_REQUEST_NOVU_ID`,
   `NOVU_NEW_KYC_RECORD_NOVU_ID`. They must exist in the environment `NOVU_API_KEY` belongs to.
2. For each workflow, paste the payload schema from `workflows/<id>.json`, add the steps listed
   there, and paste `templates/<id>.email.html` as the email body. Mark `novu-new-user`,
   `novu-new-org-user` and `novu-new-org-client-request` *critical*, so subscriber preferences cannot
   switch them off. Payload schema *validation* can be turned on: every required field is sent,
   except `loginUrl` when no Settings row exists. A trigger Novu rejects is retried, then moved to
   the dead-letter queue (see *Message flow*).
3. Connect an email provider to the environment and an in-app (Inbox) channel.
4. Enable **HMAC encryption** for the Inbox (the API already signs subscriber ids, see below).
5. Configure the API: `NOVU_API_KEY` (the environment's secret key), `NOVU_BASE_URL`
   (`https://api.novu.co`, `https://eu.api.novu.co` for the EU region, or a self-hosted Novu's API
   URL). Keep `.env` and `env.sh` on the same Novu: a key sent to the wrong server is refused.
   Configure the portals: `novuApplicationIdentifier`, `novuApi`, `novuWebSocket` in `env.json`.

   A self-hosted Novu behind a self-signed certificate: the certificate's *subject alternative
   names* must include the Novu host names (Java ignores the common name, so `CN=*.example.com`
   alone is not enough), and the API's JVM must trust it (e.g.
   `keytool -importcert -cacerts -alias … -file cert.crt`, then restart the API).
6. Test each workflow from the dashboard with the `examplePayload` from its JSON, or with the
   trigger request below.

Account emails depend on two systems: Novu sends the welcome message, and **Keycloak** sends the
"Update your account" email with the link to set a password and verify the address
(`executeActionsEmail`). That needs SMTP configured on the realm (Realm settings → Email); without
it, creating a user without a password fails with 503 and the account is rolled back.

## Message flow

How each notification gets from an HTTP request to Novu (class.method).

**Common tail.**

- *Publishing.* Publishers call `ConfirmedPublisher.publish(exchange, routingKey, Map<String,String>)`
  (core). It gives the message one id, used as its AMQP `message_id`, its publisher-confirm
  correlation id and later Novu's `transactionId`, and publishes through the shared `RabbitTemplate`
  (`RabbitMQConfig`: JSON converter, `mandatory`). The broker confirms every publish
  (`spring.rabbitmq.publisher-confirm-type: correlated`) and returns unroutable ones
  (`publisher-returns`); a rejected, unconfirmed or unroutable message is logged with its id. The
  publish does not wait for the broker: the HTTP request has already succeeded.
- *Consuming.* `novuListenerContainerFactory` (`NovuQueueConfig`) runs 3 consumers per queue (up to
  10, `NOVU_CONSUMERS` / `NOVU_MAX_CONSUMERS`), each holding one message (prefetch 1), so a message
  waiting between retries does not hold up the rest of its queue. Order is not kept.
  `NovuQueueProcessor.process…Queue(payload, messageId)` → `trigger(workflowId, payload, messageId)`:
  a message without `email` is dropped with a warning; otherwise `triggerRequest(...)` builds the
  trigger (subscriber inline, `transactionId` = message id) and `novuSdk.trigger().body(…).call()`
  sends it.
- *Failing.* Each failed attempt logs `Novu trigger of <workflow> (message <id>) at <base-url>
  failed: <Novu's reply>`. The message is retried 3 more times after 2 s, 6 s and 18 s
  (`app.novu.retry.*`), then `RepublishMessageRecoverer` moves it to `<queue>.dlq` on
  `x.novu-dead-letter`, with the error in its headers. See *Failed notifications* below.

**1. Client request invitation (`novu-new-org-client-request`)**

```
POST /client-requests                                   ClientRequestApiImpl.save
 └ ClientRequestServiceImpl.handleSave
    ├ new request: RequestTokens.issue() → BCrypt hash stored → clientRequestRepository.save
    └ ClientRequestNotification.queueEmailNotificationsForRequests(requests, {targetId → token}, organisation)
       ├ on the caller's thread: only the request ids and tokens are read; requests without a new
       │ token are skipped
       ├ after the caller's transaction commits (at once when there is none):
       │ virtualThreadExecutor → read-only transaction → send(tokens, organisation)
       │   └ per id: clientRequestRepository.findById (deleted → skipped) → buildPayload
       │      → recipientOf: the individual's emailAddress and full name, or the organisation's
       │        contactEmailAddress and name (none → skipped with a warning)
       └ ConfirmedPublisher.publish(x.novu-new-org-client-request-queue, …) → processNewOrgClientRequestQueue → trigger
```

The requests are reloaded in their own transaction because the caller's entities belong to a
closed persistence context (lazy associations such as `request.getOrganisation()` would fail on
another thread), and before the commit the new requests are not visible.
`ClientRequestServiceImpl.handleUploadRequests` (bulk upload) calls the same method, but
`ClientRequestApiImpl.uploadRequests` throws `UnsupportedOperationException`, so uploaded requests
get no invitation yet.

**2. Welcome when a client accepts a request (`novu-new-user` or `novu-new-org-user`)**

```
GET /client-requests/{id}/confirm?confirm=true&registrationToken=…   ClientRequestApiImpl.confirmRegistration
 ├ individualService.findById, keycloakUserService.getUserByIdentityNo (existing user → error)
 ├ organisationService.findById(request.getOrganisationId())          the requesting organisation
 └ KeycloakUserService.registerUser(individual, requestingOrganisation)
    ├ role: ORG_ADMIN if the individual belongs to an organisation, else APPLICANT
    ├ createRegistrationUser → create(): Keycloak user, organisation membership, realm roles,
    │   executeActionsEmail(UPDATE_PASSWORD, VERIFY_EMAIL)   ← the password e-mail comes from Keycloak
    └ newUserMessage(individual, user, memberOrganisation(individual, requestingOrganisation), settings)
       ├ member of an organisation → organisationName set → x.novu-new-org-user-queue → processNewOrgUserQueue
       └ otherwise (a client)      →                        x.novu-new-user-queue     → processNewUserQueue
```

The workflow follows the same test as the role: `memberOrganisation` returns the individual's own
organisation, or null. The requesting organisation only fills in a missing name when it is the
same organisation, so an invited client is an applicant and gets `novu-new-user`.

**3. Welcome when staff create an individual with an account (same workflows)**

```
POST /individuals (new individual, hasUser = true, no existing user)  IndividualApiImpl.save
 └ KeycloakUserService.registerUser(individual, organisation of the individual's branch or organisation)
```

**Not sending:** `novu-new-kyc-record` has no publisher, and `processNewKycRecordQueue` only logs.
`KeycloakUserService.registerUser(String individualId)` has no callers.

**Inbox:** `GET /novu/config` → `NovuAuthController.getNovuConfig` →
`NovuAuthService.generateSubscriberHash(email)` → `{ subscriberId, subscriberHash }` → the
portals' `@novu/js` Inbox (not mounted yet, issue 13).

## Failed notifications: dead letters and re-drive

A notification that fails every attempt waits in its dead-letter queue until it is moved back:

| Queue | Dead-letter queue (on `x.novu-dead-letter`, same routing key) |
|---|---|
| `q.novu-new-user` | `q.novu-new-user.dlq` |
| `q.novu-new-org-user` | `q.novu-new-org-user.dlq` |
| `q.novu-new-org-client-request` | `q.novu-new-org-client-request.dlq` |
| `q.novu-new-kyc-record` | `q.novu-new-kyc-record.dlq` |

**1. Find the cause.** The API log has one warning per attempt with Novu's reply:

```
WARN … NovuQueueProcessor : Novu trigger of new-user (message bf545219-…) at https://novu-api.example.com failed: …
```

The dead letter carries the last error in `x-exception-message` (RabbitMQ management UI → the
`.dlq` queue → *Get messages*), with `x-original-exchange` and `x-original-routingKey`.

| Error | Cause | Fix |
|---|---|---|
| `422 … "message":"workflow_not_found"` | No workflow with that id in the key's environment | Create it, or set `NOVU_*_NOVU_ID` (Setting up Novu, step 1) |
| `401` / `403` | Wrong `NOVU_API_KEY`, or a key sent to another Novu (`NOVU_BASE_URL`) | Use the secret key of the environment at `NOVU_BASE_URL` |
| `SSLHandshakeException: No subject alternative DNS name matching …` | The certificate does not name the Novu host | Reissue it with the host (or `*.example.com`) as a SAN, trust it (step 5) |
| `SSLHandshakeException: PKIX path building failed` | The JVM does not trust the certificate | Import it into the JVM's trust store, restart the API |
| `ConnectException`, `UnknownHostException`, timeouts | Novu is down or unreachable | Start it / fix `NOVU_BASE_URL` |

A trigger Novu accepts is not retried, even when the workflow then sends nothing (no steps, no
email provider): check the workflow's activity feed in the Novu dashboard for those.

**2. Fix it, then re-drive.** Nothing moves dead letters back by itself. With the API:

```bash
curl -s -H "Authorization: Bearer $TOKEN" http://localhost:8085/novu/dead-letters
curl -s -X POST -H "Authorization: Bearer $TOKEN" "http://localhost:8085/novu/dead-letters/q.novu-new-user/redrive?max=100"
```

| Endpoint | Permission | Answer |
|---|---|---|
| `GET /novu/dead-letters` | `novu:view` | `{"q.novu-new-user": 1, "q.novu-new-org-user": 3, …}`: messages in each `.dlq`, by queue |
| `POST /novu/dead-letters/{queue}/redrive?max=100` | `novu:edit` | `{"queue": "q.novu-new-user", "redriven": 1, "remaining": 0}` |

- `{queue}` is the main queue's name (`q.novu-new-user`, …); an unknown one is 404. `max` is
  1–1000 (default 100); call again for more.
- `novu:view` and `novu:edit` belong to PLATFORM_ADMIN (and SUPER_ADMIN, DEVELOPER); `novu:view`
  also to AUDITOR and the roles above it (`keycloak/ROLE_MATRIX.md`). After adding them, re-import
  the realm and sign in again, or the calls are 403.
- Each re-drive is audited (`@Audit`, entity `NOTIFICATION`).

How a re-drive works (`NovuDeadLetterService`):

```
POST /novu/dead-letters/{queue}/redrive         NovuDeadLetterController.redriveDeadLetters
 └ NovuDeadLetterService.redrive(queue, max)
    └ rabbitTemplate.execute(channel →
       repeat up to max:
         channel.basicGet(<queue>.dlq, autoAck = false)        nothing left → stop
         copy = same body, properties and message_id, without the x-exception-* / x-original-* headers
         ConfirmedPublisher.publishAndWait(<queue's exchange>, <routing key>, copy, 10 s)
           ├ confirmed and routed → channel.basicAck   (the dead letter is removed)
           └ otherwise            → channel.basicNack(requeue) and stop (it stays dead-lettered))
```

- At-least-once: a dead letter is removed only after its copy is safely on the main queue; if the
  API stops halfway, the unacknowledged message returns to the `.dlq`.
- The copy keeps its `message_id`, which is Novu's `transactionId`, so a notification Novu had
  in fact accepted before the failure is not sent twice.
- Re-driven messages go through the same retries: if the cause is not fixed they end up back in
  the `.dlq`.

Without the API, the RabbitMQ management UI can move them: open the `.dlq` queue and use *Move
messages* to its main queue (needs the `rabbitmq_shovel` and `rabbitmq_shovel_management` plugins). That keeps the
failure headers on the message, which is harmless.

Settings:

| Property (environment variable) | Default | Meaning |
|---|---|---|
| `app.novu.retry.max-retries` (`NOVU_RETRY_MAX_RETRIES`) | 3 | Attempts after the first |
| `app.novu.retry.initial-interval` (`NOVU_RETRY_INITIAL_INTERVAL`) | 2s | First wait |
| `app.novu.retry.multiplier` (`NOVU_RETRY_MULTIPLIER`) | 3 | Each wait is this times the previous |
| `app.novu.retry.max-interval` (`NOVU_RETRY_MAX_INTERVAL`) | 30s | Longest wait |
| `app.novu.consumers.concurrency` (`NOVU_CONSUMERS`) | 3 | Consumers per queue |
| `app.novu.consumers.max-concurrency` (`NOVU_MAX_CONSUMERS`) | 10 | Most consumers per queue under load |
| `app.novu.queue.deadLetterExchange` (`NOVU_DEAD_LETTER_EXCHANGE`) | `x.novu-dead-letter` | Exchange of the `.dlq` queues |

## JSON contracts

### 1. RabbitMQ message (publisher → queue)

A flat JSON object of strings (the listeners take `Map<String, String>`), serialized by the shared
`JacksonJsonMessageConverter`. One message per recipient. `email` is required in every message: it is
the recipient and the Novu subscriber id.

```json
{
  "email": "ann.applicant@example.com",
  "firstName": "Ann",
  "surname": "Applicant",
  "username": "ann.applicant@example.com",
  "loginUrl": "https://portal.knowvera.example",
  "platformName": "Knowvera",
  "supportContact": "support@knowvera.example",
  "setupLinkValidFor": "72 hours",
  "currentYear": "2026"
}
```

The other workflows add their own fields; see `payloadSchema` in each workflow file. Rules:

- Strings only; dates already formatted for reading (`"19 October 2026"`), enums as readable labels.
- Never: passwords, identity numbers, screening/risk results, document contents. The client request
  link token is the one secret allowed, and only in the email step of `novu-new-org-client-request`.
- Idempotency: `NovuQueueProcessor` sends the AMQP message id (set by `ConfirmedPublisher`) as the
  trigger's `transactionId`, so a retried or re-driven trigger Novu already accepted is not
  delivered twice.
  `"locale"` (`"en"`) is still *to add*.

### 2. Novu trigger (NovuQueueProcessor → `POST /v1/events/trigger`)

What `NovuQueueProcessor.triggerRequest` sends. The subscriber is passed inline, which creates or
updates it in the same call. `firstName` is the payload's `firstName` (or `recipientName` for client
requests), `lastName` its `surname`:

```json
{
  "name": "novu-new-user",
  "to": {
    "subscriberId": "ann.applicant@example.com",
    "email": "ann.applicant@example.com",
    "firstName": "Ann",
    "lastName": "Applicant"
  },
  "payload": {
    "email": "ann.applicant@example.com",
    "firstName": "Ann",
    "surname": "Applicant",
    "username": "ann.applicant@example.com",
    "loginUrl": "https://portal.knowvera.example",
    "platformName": "Knowvera",
    "supportContact": "support@knowvera.example",
    "setupLinkValidFor": "72 hours",
    "currentYear": "2026"
  },
  "transactionId": "0192f3c4-6b1e-7c2a-9d3f-4a5b6c7d8e9f"
}
```

To try a workflow by hand (secret key from the Novu dashboard, never committed):

```bash
curl -sS -X POST "$NOVU_BASE_URL/v1/events/trigger" -H "Authorization: ApiKey $NOVU_API_KEY" -H "Content-Type: application/json" -d @trigger.json
```

### 3. Inbox configuration (`GET /novu/config`, signed-in users)

```json
{
  "subscriberId": "ann.applicant@example.com",
  "subscriberHash": "5f0c…(hex HMAC-SHA256 of subscriberId with the Novu secret key)"
}
```

The portals pass both, with `applicationIdentifier` from `env.json`, to the `@novu/js` Inbox:

```json
{
  "novuApplicationIdentifier": "<Novu application identifier>",
  "novuApi": "https://api.novu.co",
  "novuWebSocket": "https://ws.novu.co"
}
```

The subscriber id must be the same value the API triggers with (today: the email address), or the
Inbox shows nothing.

## Problems found in the current integration

Ordered by impact. Fixed items are marked **Fixed** with what changed.

1. **Client request invitations are never delivered.** `ClientRequestNotification`
   (`core/.../messaging/ClientRequestNotification.java:95`) builds the payload without `email`.
   `NovuQueueProcessor` then looks up subscriber `null`, the call fails, and the listener throws.
   With no dead-letter queue and Spring's default requeue, the message is redelivered in a loop.
   Fix: add the recipient's email (the individual's `emailAddress`; for an organisation target, its
   `contactEmailAddress`) and skip, with a warning, requests that have none.
   **Fixed:** the payload carries `email` (individual's `emailAddress`, or the organisation's `contactEmailAddress`); requests without one are skipped with a warning, and so are re-uploaded requests that got no new token.
2. **`recipientName` is the target's UUID** (`request.getTargetId()`), so the email would greet
   "Dear 0192f3c4-…". Use the individual's full name or the organisation's name.
   **Fixed:** full name (first, middle, surname) or organisation name. `reference`, `linkExpiresAt` (from `app.request-token-link-ttl`) and `currentYear` are sent too.
3. **The invitation token is logged** at INFO (`ClientRequestNotification.java:92`: "Token: …").
   Anyone with log access can open the client's KYC link. Remove the token from the log line.
   **Fixed:** the log line has only the request id.
4. **Bulk uploads crash before any invitation:** `ClientRequestServiceImpl.java:449` calls
   `c.getId().toString()` on requests not yet saved (id is null until `saveAll`), so it throws a
   NullPointerException. Use `c.getId() == null`.
   **Fixed:** `ClientRequestServiceImpl.issueTokensForNewRequests` checks `c.getId() == null`.
5. **No failure handling on the listeners.** Any Novu error (outage, bad key, rate limit) makes the
   message redeliver immediately and forever. Add a dead-letter exchange to the four queues and a
   retry interceptor with back-off (or catch, log and drop after N attempts).
   **Fixed:** the Novu listeners use `novuListenerContainerFactory` (`NovuQueueConfig`): `app.novu.retry.*` retries with back-off (default 3 retries, 2 s ×3 up to 30 s), then the message, with the exception in its headers, goes to `<queue>.dlq` on `x.novu-dead-letter` (`NOVU_DEAD_LETTER_EXCHANGE`). The main queues keep their arguments, so existing brokers need no changes. Messages without `email` are dropped with a warning. The trigger passes the subscriber inline (`to` object) instead of the `checkSubscriberExists` lookups.
6. **`novu-new-kyc-record` does nothing**: no publisher, and the listener only upserts the subscriber.
   Either implement it (draft included) or remove the queue and listener.
   *Open:* the listener now ignores messages (debug log) instead of upserting a subscriber; the workflow still has no publisher.
7. **The client request message is `Map<String, Object>`** while the listener expects
   `Map<String, String>`; harmless today (all values are strings) but it should be typed the same.
   **Fixed:** `Map<String, String>`.
8. **New individuals can get two welcome messages**: `KeycloakUserService.registerUser` sends one,
   and `IndividualApiImpl.save` sends another (`newUserMessage`) to the same queue, with a different
   `loginUrl` (`platformUrl` instead of `kycPortalLink`) and an extra `organisationName` the
   `novu-new-user` template ignores. Keep the one in `registerUser`.
   **Fixed:** `IndividualApiImpl` no longer sends one.
9. **`firstName` carries the middle name and a trailing space** ("Ann Mary "). Send `firstName`,
   `middleName` and `surname` separately (the templates use `| strip` meanwhile).
   **Fixed:** `firstName`, `middleName` and `surname` are separate; `platformName`, `supportContact` and `setupLinkValidFor` (e.g. "72 hours") are sent too.
10. **Subscriber id is the email address**, in both the triggers and `/novu/config`. When a user
    changes their email, the Inbox loses their history and they get a second subscriber. The
    Keycloak user id (`sub`) is stable; switching needs both sides changed together. Also,
    `/novu/config` throws a NullPointerException for tokens without an `email` claim.
    *Partly fixed:* `/novu/config` returns 400 for tokens without an `email` claim. The subscriber id is still the email address.
11. **`loginUrl` can be empty**: `KeycloakUserService` sends `""` when no Settings row exists, which
    fails the schema's `uri` format once validation is on and leaves the button without a link.
    **Fixed:** blank values are left out, so with no Settings row there is no `loginUrl` (a warning is logged) instead of `""`. Templates must cope with it missing.
12. **`/novu/config` returns 500 when `NOVU_API_KEY` is not set** (seen on the test stack). Return
    503, or let the portals hide the Inbox when Novu is not configured.
    **Fixed:** 503 when `NOVU_API_KEY` is blank.
13. **The portals load the Inbox configuration but render no Inbox.** `@novu/js` is installed in
    both portals, and the admin portal stores the config in `AppEnvState`, but no component mounts
    the Inbox, and the customer portal never calls `/novu/config`. In-app steps are therefore
    invisible until an Inbox (bell) is added to the shells.
14. **An accepted client got the organisation-manager welcome.** `confirmRegistration` passes the
    *requesting* organisation to `registerUser`, and the payload added `organisationName` whenever
    that was set, so the client received `novu-new-org-user` ("You now manage …") while their role
    (decided by `individual.getOrganisation()`) was APPLICANT.
    **Fixed:** `KeycloakUserService.memberOrganisation` picks the workflow by the same test as the
    role; organisation users are named after their own organisation.
15. **Invitations were built from detached entities on another thread.** `@Async`
    `queueEmailNotificationsForRequests` received the caller's `ClientRequest` entities, so lazy
    associations could fail outside the request's persistence context, and the thread could run
    before the new requests were committed.
    **Fixed:** only ids and tokens leave the caller; after the commit the requests are reloaded in a
    read-only transaction on `virtualThreadExecutor` (see *Message flow*, path 1).
16. **Publishes were not confirmed.** No publisher confirms, `mandatory` flag or returns callback: a
    message lost on the way, or not routable to any queue, vanished without a log.
    **Fixed:** confirms and returns are on, all Novu publishers use `ConfirmedPublisher`, and
    rejected, unconfirmed or unroutable messages are logged with their message id
    (`RabbitMQConfig` covers the other publishers too).
17. **One failing message stalled its queue.** One consumer per queue, retries waiting in the
    consumer thread: messages behind a failing one waited about 26 s each.
    **Fixed:** 3–10 consumers per queue, prefetch 1.
18. **Nothing drained the dead-letter queues.**
    **Fixed:** `GET /novu/dead-letters` and `POST /novu/dead-letters/{queue}/redrive` (see *Failed
    notifications*). Each failed attempt now logs Novu's reply, which the retry and dead-letter
    steps did not show.
