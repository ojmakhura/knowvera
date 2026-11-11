# knowvera-api (webservice): document processing runs in veritas-processor.
# Build with `make build_api_image` (it runs the Maven build first); the build context is this directory.
FROM eclipse-temurin:25-jre-jammy

RUN mkdir -p /app
WORKDIR /app

ARG JAR_FILE=webservice/target/knowvera-webservice*.jar
COPY ${JAR_FILE} knowvera-webservice.jar
COPY knowvera_start.sh knowvera_start.sh

ENTRYPOINT ["sh", "/app/knowvera_start.sh"]
