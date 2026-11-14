# knowvera-api (webservice), document processing included.
# Build with `make build_api_image` (it runs the Maven build first); the build context is this directory.
# Ubuntu 24.04 (noble) ships Tesseract 5, which tess4j 5.x needs for the OCR step.
FROM eclipse-temurin:25-jre-noble

RUN apt-get update && \
    apt-get install -y --no-install-recommends tesseract-ocr tesseract-ocr-eng && \
    rm -rf /var/lib/apt/lists/* && \
    tesseract --list-langs
ENV TESSDATA_PREFIX=/usr/share/tesseract-ocr/5/tessdata

RUN useradd --system --no-create-home --home-dir /app knowvera && mkdir -p /app/logs && chown knowvera /app/logs
WORKDIR /app

ARG JAR_FILE=webservice/target/knowvera-webservice*.jar
COPY ${JAR_FILE} knowvera-webservice.jar
COPY knowvera_start.sh knowvera_start.sh

USER knowvera
EXPOSE 8085
ENTRYPOINT ["sh", "/app/knowvera_start.sh"]
