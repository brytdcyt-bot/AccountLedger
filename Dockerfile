# ============================================
#  Account Ledger – Full Stack Dockerfile
#  Backend: Java 17 Spring Boot
#  Frontend: Static (HTML/CSS/JS) served by NGINX
#  Author: Bryan Barie
# ============================================

# ---------- Stage 1: Build Backend ----------
FROM maven:3.9.6-eclipse-temurin-17 AS builder
LABEL stage="builder"

# Set working directory
WORKDIR /build

# Copy project files
COPY pom.xml .
COPY src ./src

# Build the JAR file
RUN mvn clean package -DskipTests

# ---------- Stage 2: Build Frontend ----------
FROM node:20-alpine AS frontend
LABEL stage="frontend"

WORKDIR /app

# Copy static web files
COPY resources/public ./public

# Install any dependencies (if needed)
COPY resources/public/JSON/package.json ./package.json
RUN npm install --silent || true

# Build frontend (if you had a build step, e.g. React/Vue)
RUN mkdir -p /dist && cp -r public/* /dist/

# ---------- Stage 3: Final Runtime Image ----------
FROM ubuntu:24.04
LABEL authors="Bryan Barie"
LABEL project="Account Ledger Dashboard"
LABEL version="1.0"

# Install runtime dependencies (Java + NGINX)
RUN apt-get update && \
    apt-get install -y openjdk-17-jre nginx && \
    rm -rf /var/lib/apt/lists/*

# Copy backend JAR from builder stage
COPY --from=builder /build/target/*.jar /opt/account-ledger/account-ledger.jar

# Copy frontend build from frontend stage
COPY --from=frontend /dist/ /var/www/html/

# Configure NGINX
RUN echo 'server { \
    listen 80; \
    server_name localhost; \
    root /var/www/html; \
    index index.html; \
    location /api/ { \
        proxy_pass http://127.0.0.1:8080/; \
    } \
}' > /etc/nginx/sites-available/default

# Expose ports
EXPOSE 80 8080

# Start both backend and frontend services
CMD service nginx start && java -jar /opt/account-ledger/account-ledger.jar
