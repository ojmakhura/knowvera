#!/usr/bin/env bash
#
# Sample environment file for CentralKYC.
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
export KYC_LOG_LEVEL=DEBUG
export SHOW_SQL=false

# --- Datasource (core-application.properties) ---
export DATABASE_URL=jdbc:postgresql://postgres:5432/centralkyc
export DB_USERNAME=centralkyc
export DB_PASSWORD=centralkyc
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
export REALM=centralkyc

# --- Redis ---
export REDIS_HOST=redis
export REDIS_PORT=6379
export REDIS_PASSWORD=

# --- Observability ---
export OTLP_URL=http://localhost:4318

# --- Rate limiting ---
export RATE_LIMIT=2000

# --- MinIO ---
export MINIO_URL=http://minio:9000
export MINIO_ACCESS_KEY=minio
export MINIO_SECRET_KEY=minio123
export MINIO_BUCKET=centralkyc

# --- Application roles ---
export REALM_USER_ROLE=KYC_USER
export ADMIN_PORTAL_ROLE=ADMIN_PORTAL_USER
export USER_PORTAL_ROLE=PORTAL_USER
export API_USER_ROLE=KYC_USER
export ORGANISATION_MANAGER_ROLE=ORG_MANAGER

# --- Audit partition maintenance ---
export AUDIT_PARTITION_MONTHS_HISTORY=12
export AUDIT_PARTITION_MONTHS_FUTURE=12
export AUDIT_PARTITION_MAINTENANCE_INITIAL_DELAY_MS=30000
export AUDIT_PARTITION_MAINTENANCE_FIXED_DELAY_MS=300000

# --- Misc application settings ---
export REQUEST_TOKEN_LENGTH=32
export REGISTRATION_URL=http://localhost:4300/register
export ADMIN_WEB_URL=http://localhost:4200
export MIN_PASSWORD_LENGTH=8

# --- Tesseract OCR ---
export TESSDATA_PREFIX=/usr/share/tesseract-ocr/4/tessdata
export TESSDATA_LANGS=eng

# --- Communication service ---
export COMM_SERVICE_URL=
export SEND_MESSAGE_URL=
export SOURCE_EMAIL=

# --- Registration client ---
export REGISTRATION_CLIENT=centralkyc-registration
export REGISTRATION_CLIENT_SECRET=secret

# --- LLM / AI providers ---
export LLM_ID=ollama
export LLM_MODEL=gemma4
export LMSTUDIO_BASE_URL=http://localhost:1234
export OLLAMA_BASE_URL=http://localhost:11434
export OLLAMA_MODEL=gemma4
export GEMINI_BASE_URL=https://generativelanguage.googleapis.com
export GEMINI_API_KEY=
export GEMINI_MODEL=gemini-2.0-flash
export GEMINI_PROJECT_ID=central-kyc
export GEMINI_LOCATION=us-central1
export GCP_CREDENTIALS_FILE=/path/to/credentials.json

# --- RabbitMQ exchanges / queues / routing keys ---
export DOCUMENT_HANDLER_DEAD_LETTER_EXCHANGE=x.document-dispatch-failure
export DOCUMENT_HANDLER_DEAD_LETTER_QUEUE=q.fall-back-document-dispatch
export DOCUMENT_HANDLER_DEAD_LETTER_ROUTING_KEY=document-fall-back
export TEXT_EXTRACTION_QUEUE_EXCHANGE=x.text-extraction-queue
export TEXT_EXTRACTION_QUEUE=q.text-extraction
export TEXT_EXTRACTION_QUEUE_ROUTING_KEY=text-extraction
export GEMINI_TEXT_EXTRACTION_QUEUE_EXCHANGE=x.gemini-text-extraction-queue
export GEMINI_TEXT_EXTRACTION_QUEUE=q.gemini-text-extraction
export GEMINI_TEXT_EXTRACTION_QUEUE_ROUTING_KEY=gemini-text-extraction
export DOCUMENT_CONFIRMATION_QUEUE_EXCHANGE=x.document-confirmation-queue
export DOCUMENT_CONFIRMATION_QUEUE=q.document-confirmation
export DOCUMENT_CONFIRMATION_QUEUE_ROUTING_KEY=document-confirmation
export TEXT_PROCESSING_QUEUE_EXCHANGE=x.text-processing-queue
export TEXT_PROCESSING_QUEUE=q.text-processing
export TEXT_PROCESSING_QUEUE_ROUTING_KEY=text-processing
export INFORMATION_CONFIRMATION_QUEUE_EXCHANGE=x.information-confirmation-queue
export INFORMATION_CONFIRMATION_QUEUE=q.information-confirmation
export INFORMATION_CONFIRMATION_QUEUE_ROUTING_KEY=information-confirmation
export TEXT_CLEANUP_QUEUE_EXCHANGE=x.text-cleanup-queue
export TEXT_CLEANUP_QUEUE=q.text-cleanup
export TEXT_CLEANUP_QUEUE_ROUTING_KEY=text-cleanup
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
export WEBSERVICE_HOST=centralkyc.localhost
