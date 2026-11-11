# veritas-processor

Document AI processing for knowvera: text extraction, cleanup, field extraction and document type
classification, driven by versioned **processing profiles**. knowvera-api decides what a document
must prove; veritas-processor works out what the document says.

| Module | Artifact | Holds |
|---|---|---|
| `common` | `veritas-processor-common` | Contracts shared with callers (no dependencies): job request/result, profiles, RabbitMQ names |
| `core` | `veritas-processor-core` | Profiles, the pipeline runner, providers, job history, S3 reading |
| `webservice` | `veritas-processor-webservice` | The Boot application: REST APIs, job listener, security |

Base package: `bw.co.roguesystems.ai`.

## Profiles

A profile is a named list of steps, run in this order (each optional):

| Step | Providers | Output |
|---|---|---|
| `TEXT_EXTRACTION` | `PDF_TEXT`, `TESSERACT`, `GEMINI` (file as input) | text |
| `TEXT_CLEANUP` | `GEMINI`, `LM_STUDIO`, `OLLAMA` | cleaned text |
| `FIELD_EXTRACTION` | language models | the requested fields, as JSON |
| `CLASSIFICATION` | language models | expected/detected type, match, score, signal scores |

A step tries its providers in order; the first result wins. When none can and one of them was
unreachable, timed out or failed itself, the step is tried again after a wait
(`VERITAS_JOB_ATTEMPTS`, `VERITAS_JOB_RETRY_DELAY`). With `VERITAS_LOCAL_ONLY=true` cloud providers
(Gemini) are skipped. Prompts are templates with `{{documentType}}`, `{{fields}}`,
`{{candidateTypes}}` and `{{text}}` (wrapped in `<document>` delimiters, with an untrusted-content
notice added to the system prompt); a step without prompts uses `DefaultPrompts`.

Step options (checked when the profile is saved):

| Option | For | Meaning |
|---|---|---|
| `temperature`, `topP`, `topK` | language models | sampling; the temperature is 0 unless set |
| `numCtx` | Ollama | context window in tokens (default `OLLAMA_NUM_CTX`, 8192). A prompt that does not fit fails the attempt: Ollama would otherwise cut it short silently |
| `maxTokens` | language models | longest answer; an answer cut off at the limit fails the attempt |
| `structuredOutput` | field extraction, classification | `true`/`false`: hold the answer to a JSON schema (Ollama `format`, LM Studio `response_format`; Gemini is asked for JSON). On for field extraction, and for classification with the default prompts |
| `ocrLanguages`, `ocrDpi` | text extraction | Tesseract languages (`eng+tsn`) and rendering resolution |

The classification's `score` and `match` are worked out by the processor from the signal scores
the model returns (match: score of 0.6 or more), not taken from the model.

Text extraction limits: documents over `VERITAS_MAX_FILE_SIZE` (25MB) and PDFs over
`VERITAS_MAX_PAGES` (50) are refused, and a PDF text layer with fewer than
`VERITAS_MIN_TEXT_CHARS_PER_PAGE` (50) characters a page does not count as its text (a scan with a
stamp), so OCR runs.

Saving a profile (`PUT /profiles/{code}`) adds a version; versions never change and every job
records the version it ran with. The `default` profile is created on first start.

## Jobs

Callers publish `ProcessingJobRequest`s to exchange `x.veritas` (routing key `veritas.jobs`) and
read `ProcessingJobResult`s from `q.veritas.results`. `POST /jobs?wait=true` runs a job
synchronously (e.g. to try a profile); `GET /jobs/{jobId}` returns a job's history. A job submitted
through the API belongs to who submitted it: nobody else can read it or reuse its id (jobs from the
queue are readable by every client with `jobs-submit`). A job that starts after text extraction
(`fromStep`) without `text` runs the whole profile on its document.

A job still running after `VERITAS_JOB_STALE_AFTER` (2h) was interrupted: it is failed and the
FAILED result published. The history holds the documents' text and fields; set
`VERITAS_JOB_RETENTION` (e.g. `90d`) to delete jobs older than that (default: kept forever).

Metrics (`/actuator/prometheus` when exposed): `veritas.step` (timer; step, provider, status) and
`veritas.jobs` (counter; status). Log lines of a job carry its id.

## Setup

1. **Database**: create a `veritas` database and user (`VERITAS_DATABASE_URL`, `VERITAS_DB_USERNAME`,
   `VERITAS_DB_PASSWORD`); Liquibase creates the tables.
2. **Storage**: read access to the knowvera bucket (`S3_*`, or `VERITAS_S3_ACCESS_KEY`/`SECRET_KEY`
   in docker-compose for a read-only key).
3. **Keycloak** (realm `knowvera`): `keycloak/generate_authz.py` writes into `knowvera-realm.json`
   (and the test realm):
   - the client `veritas-processor` (no sign-in flows) with client roles `profiles-view`,
     `profiles-admin` and `jobs-submit`;
   - a `veritas-processor` audience mapper on `knowvera-api`, whose service account gets
     `profiles-admin` (for the profile seeding).

   Keycloak imports the realm only when it does not exist yet (`--import-realm`); on a running
   Keycloak, import `knowvera-realm.json` through the admin console (Realm settings → Partial
   import, "skip" for existing resources) or create the client and roles by hand. Other callers
   need a `veritas-processor` audience mapper on their client and the roles they use.
4. **Models**: `OLLAMA_BASE_URL` / `LMSTUDIO_BASE_URL` / `LLM_MODEL` for local models; Gemini on
   Vertex AI uses `GEMINI_PROJECT_ID`, `GEMINI_LOCATION` and Google Cloud application default
   credentials.

## knowvera-api

knowvera-api sends every uploaded document to veritas-processor (`DocumentProcessingDispatcher`)
and applies the results (`VeritasResultHandler`): the text, the extracted fields, then the type
confirmation through its own KYC rules (`DocumentConfirmationService`), after which information
confirmation and verification continue in knowvera-api. A document type names its profile in
`processingProfileCode` (blank: `PROCESSING_DEFAULT_PROFILE`).

Fields are sent typed (`FieldSpec`: name, label, type, format, many, mandatory, from the document
type's expected fields) and come back normalised to their type: every requested field present
(null when not found), numbers and booleans as JSON values, ISO dates, lists for `many` fields.

**Migrating an existing installation**: the in-process pipeline that came before is gone, but its
settings are still in the database (Hibernate does not drop columns). Start knowvera-api once with
`VERITAS_SEED_PROFILES=true` (and `LLM_ID` as before): it saves the `default` profile from the old
tool selectors, a `doctype-*` profile for every document type that had its own prompts, and links
the document types. The old columns and tables (`DOCUMENT_TYPE.VALIDATION_PROMPTS`,
`TEXT_EXTRACTION_PROMPTS`, `TOOL_SELECTOR` and the `SETTINGS2*_TOOLS` tables) can be dropped after
that.

## Build

```bash
make build_veritas        # install (knowvera-api needs veritas-processor-common)
make test_veritas
make run_veritas_local    # port 8086
make build_veritas_image
```
