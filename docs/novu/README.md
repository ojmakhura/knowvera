# Novu notifications

Knowvera sends user-facing notifications through [Novu](https://novu.co). This folder holds the
drafts for the four Novu workflows and the JSON contracts between the API, RabbitMQ and Novu.

```
publisher (API / core)                 RabbitMQ                          webservice                Novu
──────────────────────   ── direct exchange ─► durable queue ──►  NovuQueueProcessor  ── trigger ──►  workflow
convertAndSend(payload)     x.novu-…-queue        q.novu-…        @RabbitListener          (workflowId) ├ in-app
                                                                  Map<String,String>                    └ email
portals ── GET /novu/config ──► { subscriberId, subscriberHash } ── @novu/js Inbox (HMAC-secured)
```

| Workflow id (`app.novu.queue.*NovuId`) | Queue | Sent by | Channels | State |
|---|---|---|---|---|
| `novu-new-user` | `q.novu-new-user` | `KeycloakUserService.registerUser`, `IndividualApiImpl` | in-app, email | works once the template exists |
| `novu-new-org-user` | `q.novu-new-org-user` | `KeycloakUserService.registerUser` | in-app, email | works once the template exists |
| `novu-new-org-client-request` | `q.novu-new-org-client-request` | `ClientRequestNotification` (core) | email only | **broken**: no recipient (issue 1) |
| `novu-new-kyc-record` | `q.novu-new-kyc-record` | nobody | in-app, email | proposed; listener does not trigger |

Files:

- `workflows/<id>.json`: per workflow, the queue, publishers, the payload JSON Schema (paste into
  the workflow's *Payload schema* in the Novu dashboard), the steps with subjects and in-app text,
  and an example payload.
- `templates/<id>.email.html`: the email step bodies (Liquid, `{{ payload.x }}`).

Fields marked *(to add)* in the schemas are not sent today; the templates fall back when they are
missing (`| default:`), so the workflows can go live before the code changes.

## Setting up Novu

1. In the Novu dashboard (one environment per Knowvera environment), create the four workflows
   with exactly the ids above (or set `NOVU_*_NOVU_ID` to the ids you choose).
2. For each workflow, paste the payload schema from `workflows/<id>.json`, add the steps listed
   there, and paste `templates/<id>.email.html` as the email body. Mark `novu-new-user`,
   `novu-new-org-user` and `novu-new-org-client-request` *critical*, so subscriber preferences cannot
   switch them off. Leave payload schema *validation* off until the code sends every required field
   (issues 1, 2 and 11): a rejected trigger is redelivered forever today (issue 5).
3. Connect an email provider to the environment and an in-app (Inbox) channel.
4. Enable **HMAC encryption** for the Inbox (the API already signs subscriber ids, see below).
5. Configure the API: `NOVU_API_KEY` (the environment's secret key), `NOVU_BASE_URL`
   (`https://api.novu.co`, or `https://eu.api.novu.co` for the EU region). Configure the portals:
   `novuApplicationIdentifier`, `novuApi`, `novuWebSocket` in `env.json`.
6. Test each workflow from the dashboard with the `examplePayload` from its JSON, or with the
   trigger request below.

Account emails depend on two systems: Novu sends the welcome message, and **Keycloak** sends the
"Update your account" email with the link to set a password and verify the address
(`executeActionsEmail`). That needs SMTP configured on the realm (Realm settings → Email); without
it, creating a user without a password fails with 503 and the account is rolled back.

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
- Recommended envelope fields for idempotency and tracing (*to add*): `"transactionId"` (a UUID per
  notification; Novu ignores a repeated trigger with the same id) and `"locale"` (`"en"`).

### 2. Novu trigger (NovuQueueProcessor → `POST /v1/events/trigger`)

What the listener should send. Passing the subscriber as an object creates or updates the
subscriber in the same call, which replaces `checkSubscriberExists` (two extra API calls per
message today):

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

Ordered by impact. None are fixed yet.

1. **Client request invitations are never delivered.** `ClientRequestNotification`
   (`core/.../messaging/ClientRequestNotification.java:95`) builds the payload without `email`.
   `NovuQueueProcessor` then looks up subscriber `null`, the call fails, and the listener throws.
   With no dead-letter queue and Spring's default requeue, the message is redelivered in a loop.
   Fix: add the recipient's email (the individual's `emailAddress`; for an organisation target, its
   `contactEmailAddress`) and skip, with a warning, requests that have none.
2. **`recipientName` is the target's UUID** (`request.getTargetId()`), so the email would greet
   "Dear 0192f3c4-…". Use the individual's full name or the organisation's name.
3. **The invitation token is logged** at INFO (`ClientRequestNotification.java:92`: "Token: …").
   Anyone with log access can open the client's KYC link. Remove the token from the log line.
4. **Bulk uploads crash before any invitation:** `ClientRequestServiceImpl.java:449` calls
   `c.getId().toString()` on requests not yet saved (id is null until `saveAll`), so it throws a
   NullPointerException. Use `c.getId() == null`.
5. **No failure handling on the listeners.** Any Novu error (outage, bad key, rate limit) makes the
   message redeliver immediately and forever. Add a dead-letter exchange to the four queues and a
   retry interceptor with back-off (or catch, log and drop after N attempts).
6. **`novu-new-kyc-record` does nothing**: no publisher, and the listener only upserts the subscriber.
   Either implement it (draft included) or remove the queue and listener.
7. **The client request message is `Map<String, Object>`** while the listener expects
   `Map<String, String>`; harmless today (all values are strings) but it should be typed the same.
8. **New individuals can get two welcome messages**: `KeycloakUserService.registerUser` sends one,
   and `IndividualApiImpl.save` sends another (`newUserMessage`) to the same queue, with a different
   `loginUrl` (`platformUrl` instead of `kycPortalLink`) and an extra `organisationName` the
   `novu-new-user` template ignores. Keep the one in `registerUser`.
9. **`firstName` carries the middle name and a trailing space** ("Ann Mary "). Send `firstName`,
   `middleName` and `surname` separately (the templates use `| strip` meanwhile).
10. **Subscriber id is the email address**, in both the triggers and `/novu/config`. When a user
    changes their email, the Inbox loses their history and they get a second subscriber. The
    Keycloak user id (`sub`) is stable; switching needs both sides changed together. Also,
    `/novu/config` throws a NullPointerException for tokens without an `email` claim.
11. **`loginUrl` can be empty**: `KeycloakUserService` sends `""` when no Settings row exists, which
    fails the schema's `uri` format once validation is on and leaves the button without a link.
12. **`/novu/config` returns 500 when `NOVU_API_KEY` is not set** (seen on the test stack). Return
    503, or let the portals hide the Inbox when Novu is not configured.
13. **The portals load the Inbox configuration but render no Inbox.** `@novu/js` is installed in
    both portals, and the admin portal stores the config in `AppEnvState`, but no component mounts
    the Inbox, and the customer portal never calls `/novu/config`. In-app steps are therefore
    invisible until an Inbox (bell) is added to the shells.
