# Dockerfile for Native Image
FROM eclipse-temurin:21-jre-jammy

# Install Tesseract OCR
RUN apt-get update && \
    apt-get install -y tesseract-ocr tesseract-ocr-eng libtesseract-dev libleptonica-dev && \
    rm -rf /var/lib/apt/lists/*

# Set Tesseract data path
ENV TESSDATA_PREFIX=/usr/share/tesseract-ocr/4/tessdata

RUN mkdir -p /app
WORKDIR /app

ARG WAR_FILE=webservice/target/knowvera-webservice*.jar
COPY ${WAR_FILE} knowvera-webservice.jar
COPY kyc_start.sh kyc_start.sh

ENTRYPOINT ["sh", "/app/kyc_start.sh"]