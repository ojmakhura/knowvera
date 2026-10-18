#!/usr/bin/env bash
# A second API instance for the JMeter plans, using the existing services kept apart from dev:
#   - Keycloak: realm "knowvera-test" (results/test-stack/knowvera-test-realm.json, a copy of
#     webservice/src/test/resources/test-realm.json; import it once in the admin console)
#   - Postgres: database "knowvera_test" on the dev server (the API creates the schema)
#   - RabbitMQ: vhost "knowvera-test";  Redis: database 1
# Everything else comes from .env.
#
#   jmeter/test-stack.sh up      start the test API on port 18085 and wait until ready
#   jmeter/test-stack.sh seed    create the data the load journeys need (idempotent)
#   jmeter/test-stack.sh down    stop it
#
# Then: make jmeter_authz JMETER_ARGS="$(jmeter/test-stack.sh jmeter-opts)"
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
STATE="$ROOT/results/test-stack"
API_PORT=18085
TEST_REALM=knowvera-test
TEST_DATABASE=knowvera_test
TEST_VHOST=knowvera-test
TEST_REDIS_DATABASE=1
TEST_CLIENT_SECRET=test-secret  # client secrets of the test realm

auth_url() {
    # shellcheck disable=SC1091
    (. "$ROOT/.env" && echo "$AUTH_URL")
}

up() {
    mkdir -p "$STATE"
    if ! curl -sfk -o /dev/null "$(auth_url)/realms/$TEST_REALM/.well-known/openid-configuration"; then
        echo "Realm $TEST_REALM not found at $(auth_url): import results/test-stack/knowvera-test-realm.json first" >&2
        exit 1
    fi

    echo "Starting the test API on port $API_PORT..."
    (
        set -a
        # shellcheck disable=SC1091
        . "$ROOT/.env"
        PORT=$API_PORT
        REALM=$TEST_REALM
        API_CLIENT=knowvera-api
        API_CLIENT_SECRET=$TEST_CLIENT_SECRET
        DATABASE_URL="${DATABASE_URL%/*}/$TEST_DATABASE"
        SPRING_RABBITMQ_VIRTUAL_HOST=$TEST_VHOST
        SPRING_DATA_REDIS_DATABASE=$TEST_REDIS_DATABASE
        # The plans send hundreds of requests per user per minute; keep the per-IP public limit
        RATE_LIMIT=100000
        set +a
        exec java -jar "$ROOT/webservice/target/knowvera-webservice.jar"
    ) >"$STATE/api.log" 2>&1 &
    echo $! >"$STATE/api.pid"

    if ! curl -sf --retry 90 --retry-delay 2 --retry-all-errors -o /dev/null "http://localhost:$API_PORT/actuator/health"; then
        echo "The test API did not start; see $STATE/api.log" >&2
        exit 1
    fi
    echo "Test API ready on http://localhost:$API_PORT (realm $TEST_REALM)"
}

down() {
    if [ -f "$STATE/api.pid" ]; then
        kill "$(cat "$STATE/api.pid")" 2>/dev/null || true
        rm -f "$STATE/api.pid"
    fi
    echo "Test API stopped"
}

case "${1:-}" in
    up) up ;;
    down) down ;;
    jmeter-opts) echo "-Jport=$API_PORT -Jrealm=$TEST_REALM -JkeycloakUrl=$(auth_url)" ;;
    seed) python3 -I "$ROOT/jmeter/seed_test_data.py" "http://localhost:$API_PORT" "$(auth_url)" "$TEST_REALM" "Test-Passw0rd!" ;;
    *) echo "usage: $0 up|seed|down|jmeter-opts" >&2; exit 2 ;;
esac
