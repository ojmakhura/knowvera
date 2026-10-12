#!/usr/bin/env bash
# Throwaway stack for the JMeter plans: Keycloak with the test realm, fresh Postgres, RabbitMQ
# and Redis containers, and a second API instance (built jar) pointed at them. The dev stack is
# not touched, except MinIO, which is shared (the plans only read from it).
#
#   jmeter/test-stack.sh up      start everything, wait until ready
#   jmeter/test-stack.sh down    stop and remove everything
#
# Then: make jmeter_authz JMETER_OPTS="-Jport=18085 -JkeycloakUrl=http://localhost:18970"
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
STATE="$ROOT/results/test-stack"
API_PORT=18085
KC_PORT=18970
PG_PORT=15432
RABBIT_PORT=15673
REDIS_PORT=16379
KC_IMAGE="${KC_IMAGE:-quay.io/keycloak/keycloak:26.5}"
PREFIX=knowvera-test

up() {
    mkdir -p "$STATE"
    # The import file must be named after the realm
    docker run -d --rm --name $PREFIX-keycloak -p $KC_PORT:8080 \
        -v "$ROOT/webservice/src/test/resources/test-realm.json:/opt/keycloak/data/import/knowvera-realm.json:ro" \
        "$KC_IMAGE" start-dev --import-realm >/dev/null
    docker run -d --rm --name $PREFIX-postgres -p $PG_PORT:5432 \
        -e POSTGRES_DB=knowvera -e POSTGRES_USER=knowvera -e POSTGRES_PASSWORD=knowvera-test postgres:18 >/dev/null
    docker run -d --rm --name $PREFIX-rabbit -p $RABBIT_PORT:5672 rabbitmq:4.2-management >/dev/null
    docker run -d --rm --name $PREFIX-redis -p $REDIS_PORT:6379 redis:8-alpine >/dev/null

    echo "Waiting for Keycloak..."
    curl -sf --retry 90 --retry-delay 2 --retry-all-errors -o /dev/null \
        "http://localhost:$KC_PORT/realms/knowvera/.well-known/openid-configuration"

    echo "Starting the API on port $API_PORT..."
    (
        set -a
        # shellcheck disable=SC1091
        . "$ROOT/.env"
        PORT=$API_PORT
        AUTH_URL=http://localhost:$KC_PORT
        REALM=knowvera
        API_CLIENT=knowvera-api
        API_CLIENT_SECRET=test-secret
        REGISTRATION_CLIENT=knowvera-registration
        REGISTRATION_CLIENT_SECRET=test-secret
        DATABASE_URL=jdbc:postgresql://localhost:$PG_PORT/knowvera
        DB_USERNAME=knowvera
        DB_PASSWORD=knowvera-test
        RABBITMQ_HOST=localhost
        RABBITMQ_PORT=$RABBIT_PORT
        RABBITMQ_USERNAME=guest
        RABBITMQ_PASSWORD=guest
        REDIS_HOST=localhost
        REDIS_PORT=$REDIS_PORT
        REDIS_PASSWORD=
        set +a
        exec java -jar "$ROOT/webservice/target/knowvera-webservice.jar"
    ) >"$STATE/api.log" 2>&1 &
    echo $! >"$STATE/api.pid"

    echo "Waiting for the API..."
    if ! curl -sf --retry 90 --retry-delay 2 --retry-all-errors -o /dev/null "http://localhost:$API_PORT/actuator/health"; then
        echo "The API did not start; see $STATE/api.log" >&2
        exit 1
    fi
    echo "Test stack ready: API http://localhost:$API_PORT, Keycloak http://localhost:$KC_PORT"
}

down() {
    if [ -f "$STATE/api.pid" ]; then
        kill "$(cat "$STATE/api.pid")" 2>/dev/null || true
        rm -f "$STATE/api.pid"
    fi
    docker rm -f $PREFIX-keycloak $PREFIX-postgres $PREFIX-rabbit $PREFIX-redis >/dev/null 2>&1 || true
    echo "Test stack stopped"
}

case "${1:-}" in
    up) up ;;
    down) down ;;
    *) echo "usage: $0 up|down" >&2; exit 2 ;;
esac
