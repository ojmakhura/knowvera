# veritas-processor: build with `make build_veritas` first (veritas-processor/webservice/target/veritas-processor.jar)
# Ubuntu 24.04 (noble): Tesseract 5, which tess4j 5.x binds to
FROM eclipse-temurin:25-jre-noble

# Tesseract OCR (the TESSERACT provider); add tesseract-ocr-<lang> packages for other languages
RUN apt-get update && \
    apt-get install -y --no-install-recommends tesseract-ocr tesseract-ocr-eng && \
    rm -rf /var/lib/apt/lists/* && \
    tesseract --list-langs

ENV TESSDATA_PREFIX=/usr/share/tesseract-ocr/5/tessdata

RUN useradd --system --no-create-home --home-dir /app veritas && mkdir -p /app
WORKDIR /app

ARG JAR_FILE=webservice/target/veritas-processor.jar
COPY ${JAR_FILE} veritas-processor.jar

# Nothing is written at run time: no need for root
USER veritas

EXPOSE 8086
# The heap follows the container's memory limit (override with JAVA_TOOL_OPTIONS)
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75"
ENTRYPOINT ["java", "-jar", "/app/veritas-processor.jar"]
