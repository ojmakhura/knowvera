# knowvera-api (knowvera-api/webservice): document processing runs in veritas-processor
FROM eclipse-temurin:25-jre-jammy

RUN mkdir -p /app
WORKDIR /app

ARG WAR_FILE=knowvera-api/webservice/target/knowvera-webservice*.jar
COPY ${WAR_FILE} knowvera-webservice.jar
COPY knowvera_start.sh knowvera_start.sh

ENTRYPOINT ["sh", "/app/knowvera_start.sh"]
