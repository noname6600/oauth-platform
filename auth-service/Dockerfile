# ============================
# Stage 1: Build (Gradle)
# ============================
FROM gradle:8.8-jdk21 AS build
WORKDIR /build

# Copy Gradle settings
COPY gradle gradle
COPY gradlew .
COPY build.gradle settings.gradle ./
RUN chmod +x gradlew

RUN ./gradlew dependencies --no-daemon || true

# Copy source code
COPY src src

RUN ./gradlew clean bootJar -x test --no-daemon

# ============================
# Stage 2: Run
# ============================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /build/build/libs/*.jar app.jar

EXPOSE 8081

# Healthcheck cho K8s / Docker
HEALTHCHECK --interval=30s --timeout=3s --start-period=8s CMD wget -qO- http://localhost:8081/actuator/health || exit 1

# JVM optimal for container
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
