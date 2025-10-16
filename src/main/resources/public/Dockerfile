# Use a lightweight OpenJDK 17 image
FROM eclipse-temurin:17-jdk-jammy AS build

# Set work directory
WORKDIR /app

# Copy Maven files
COPY pom.xml .
COPY src ./src

# Build the project
RUN ./mvnw clean package -DskipTests

# Second stage: run the application
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Copy the jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Default command
ENTRYPOINT ["java","-jar","app.jar"]