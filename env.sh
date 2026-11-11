#!/usr/bin/env bash
#
# Sample environment file for knowvera.
#
# Usage:
#   cp env.sh env.local.sh   # keep your real secrets out of git
#   source env.sh            # export the variables into your shell
#   docker compose up        # compose will pick them up
#
# Every variable mirrors a ${VAR:-default} placeholder in docker-compose.yml.
# Values below are the defaults; override the ones you need (especially secrets).

# --- Server / logging ---
export PORT=8085
export SPRING_LOG_LEVEL=INFO
export HIBERNATE_LOG_LEVEL=INFO
export KYC_LOG_LEVEL=INFO
export SHOW_SQL=false

# --- Datasource (core-application.properties) ---
export DATABASE_URL=jdbc:postgresql://postgres:5432/knowvera
export DB_USERNAME=knowvera
export DB_PASSWORD=knowvera
export HIKARI_CONNECTION_TIMEOUT=3000
export HIKARI_MAX_LIFETIME=1200000
export HIKARI_MIN_IDLE=5
export HIKARI_MAX_POOL_SIZE=20

# --- RabbitMQ ---
export RABBITMQ_HOST=rabbitmq
export RABBITMQ_PORT=5672
export RABBITMQ_USERNAME=guest
export RABBITMQ_PASSWORD=guest

# --- Security / OAuth2 (Keycloak) ---
export AUTH_URL=http://keycloak:8970
export REALM=knowvera

# --- Redis ---
export REDIS_HOST=redis
export REDIS_PORT=6379
export REDIS_PASSWORD=

# --- Observability ---
export OTLP_URL=http://localhost:4318

# --- Rate limiting ---
export RATE_LIMIT=2000

# --- Document storage (S3-compatible; shared with veritas-processor, see docs/storage.md) ---
# The older MINIO_URL / MINIO_ACCESS_KEY / MINIO_SECRET_KEY / MINIO_BUCKET names still work as fallbacks
export S3_ENDPOINT=http://minio:9000
export S3_REGION=garage
export S3_ACCESS_KEY=minio
export S3_SECRET_KEY=minio123
export S3_BUCKET=knowvera
export S3_CREATE_BUCKET=true

# --- Application roles ---
export REALM_USER_ROLE=APPLICANT
export ORGANISATION_MANAGER_ROLE=ORG_ADMIN

# --- Audit partition maintenance ---
export AUDIT_PARTITION_MONTHS_HISTORY=12
export AUDIT_PARTITION_MONTHS_FUTURE=12
export AUDIT_PARTITION_MAINTENANCE_INITIAL_DELAY_MS=30000
export AUDIT_PARTITION_MAINTENANCE_FIXED_DELAY_MS=300000

# --- Misc application settings ---
export REQUEST_TOKEN_LENGTH=32
export REGISTRATION_URL=http://localhost:4300/register
export ADMIN_WEB_URL=http://localhost:4200
export CORS_ALLOWED_ORIGINS=http://localhost:4200,http://localhost:4300
export JWT_AUDIENCES=knowvera-api
export DOCUMENT_AUTO_VERIFY=false
export MIN_PASSWORD_LENGTH=12

# --- Communication service ---
export COMM_SERVICE_URL=
export SEND_MESSAGE_URL=
export SOURCE_EMAIL=

# --- Resource-based authorization (Keycloak policy enforcer) ---
export AUTHZ_ENABLED=true
export AUTHZ_PERMISSION_AUTHORITIES=true
export API_CLIENT=knowvera-api
export API_CLIENT_SECRET=secret

# --- Document processing: knowvera-api -> veritas-processor ---
export VERITAS_URL=http://veritas-processor:8086
# The profile of document types that name none
export PROCESSING_DEFAULT_PROFILE=default

# --- veritas-processor ---
export VERITAS_IMAGE_NAME=veritas-processor
export VERITAS_PORT=8086
# Its own database (profiles and job history)
export VERITAS_DATABASE_URL=jdbc:postgresql://postgres:5432/veritas
export VERITAS_DB_USERNAME=veritas
export VERITAS_DB_PASSWORD=veritas
# Hibernate makes the tables; true also runs the Liquibase changelog, after Hibernate
export VERITAS_LIQUIBASE=false
# Keycloak client whose roles give access, and the audience its tokens must carry
export VERITAS_CLIENT_ID=veritas-processor
export VERITAS_JWT_AUDIENCES=veritas-processor
# A read-only storage key for the processor (blank: the S3 key above)
export VERITAS_S3_ACCESS_KEY=
export VERITAS_S3_SECRET_KEY=
# Keep documents in the deployment: never send them to Gemini
export VERITAS_LOCAL_ONLY=false
# Tesseract OCR (the path inside the veritas-processor image)
export TESSDATA_PREFIX=/usr/share/tesseract-ocr/5/tessdata
export TESSDATA_LANGS=eng
# Local models; LLM_MODEL is used when a step names no model
export LLM_MODEL=gemma4
export LMSTUDIO_BASE_URL=http://localhost:1234
export OLLAMA_BASE_URL=http://localhost:11434
export OLLAMA_MODEL=gemma4
# Ollama's context window in tokens: a prompt that does not fit fails instead of being cut short
export OLLAMA_NUM_CTX=8192
export LLM_READ_TIMEOUT=5m
# Gemini on Vertex AI (Google Cloud application default credentials)
export GEMINI_PROJECT_ID=central-kyc
export GEMINI_LOCATION=us-central1
export SPRING_AI_VERTEX_MODEL=gemini-2.5-flash
# Jobs processed at once (min-max consumers)
export VERITAS_JOB_CONCURRENCY=1-4
# How long the job history (document text and fields included) is kept, e.g. 90d; blank: forever
export VERITAS_JOB_RETENTION=
export VERITAS_MAX_FILE_SIZE=25MB
export VERITAS_MAX_PAGES=50
# The API documentation (Swagger) needs no sign-in: off unless asked for
export VERITAS_API_DOCS=false

# --- RabbitMQ exchanges / queues / routing keys ---
export INFORMATION_CONFIRMATION_QUEUE_EXCHANGE=x.information-confirmation-queue
export INFORMATION_CONFIRMATION_QUEUE=q.information-confirmation
export INFORMATION_CONFIRMATION_QUEUE_ROUTING_KEY=information-confirmation
export KYC_VERIFICATION_QUEUE_EXCHANGE=x.kyc-verification-queue
export KYC_VERIFICATION_QUEUE=q.kyc-verification
export KYC_VERIFICATION_QUEUE_ROUTING_KEY=kyc-verification
export ORGANISATION_VERIFICATION_QUEUE_EXCHANGE=x.organisation-verification-queue
export ORGANISATION_VERIFICATION_QUEUE=q.organisation-verification
export ORGANISATION_VERIFICATION_QUEUE_ROUTING_KEY=organisation-verification
export INDIVIDUAL_VERIFICATION_QUEUE_EXCHANGE=x.individual-verification-queue
export INDIVIDUAL_VERIFICATION_QUEUE=q.individual-verification
export INDIVIDUAL_VERIFICATION_QUEUE_ROUTING_KEY=individual-verification

# --- Novu notifications ---
export NOVU_API_KEY=
export NOVU_BASE_URL=https://api.novu.co
export NOVU_NEW_USER_NOVU_ID=novu-new-user
export NOVU_NEW_USER_QUEUE_EXCHANGE=x.novu-new-user-queue
export NOVU_NEW_USER_QUEUE=q.novu-new-user
export NOVU_NEW_USER_QUEUE_ROUTING_KEY=novu-new-user
export NOVU_NEW_ORG_USER_NOVU_ID=novu-new-org-user
export NOVU_NEW_ORG_USER_QUEUE_EXCHANGE=x.novu-new-org-user-queue
export NOVU_NEW_ORG_USER_QUEUE=q.novu-new-org-user
export NOVU_NEW_ORG_USER_QUEUE_ROUTING_KEY=novu-new-org-user
export NOVU_NEW_ORG_CLIENT_REQUEST_NOVU_ID=novu-new-org-client-request
export NOVU_NEW_ORG_CLIENT_REQUEST_QUEUE_EXCHANGE=x.novu-new-org-client-request-queue
export NOVU_NEW_ORG_CLIENT_REQUEST_QUEUE=q.novu-new-org-client-request
export NOVU_NEW_ORG_CLIENT_REQUEST_QUEUE_ROUTING_KEY=novu-new-org-client-request
export NOVU_NEW_KYC_RECORD_NOVU_ID=novu-new-kyc-record
export NOVU_NEW_KYC_RECORD_QUEUE_EXCHANGE=x.novu-new-kyc-record-queue
export NOVU_NEW_KYC_RECORD_QUEUE=q.novu-new-kyc-record
export NOVU_NEW_KYC_RECORD_QUEUE_ROUTING_KEY=novu-new-kyc-record

# --- Traefik routing ---
export WEBSERVICE_HOST=knowvera.localhost
export VERITAS_HOST=veritas.localhost
