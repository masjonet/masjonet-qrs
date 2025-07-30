# Stage 1: Build the app
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy source code and pom.xml
COPY . .

# Build the project (skip tests to save time)
RUN mvn clean package -DskipTests

# Stage 2: Run the app
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy the JAR from the previous stage
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
