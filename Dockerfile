# syntax=docker/dockerfile:1

# ---- build stage ----
FROM eclipse-temurin:25-jdk AS build

WORKDIR /src

# Resolve the Gradle distribution first, so it stays cached when sources change.
COPY gradlew ./
COPY gradle ./gradle
RUN ./gradlew --version --no-daemon

COPY settings.gradle.kts build.gradle.kts gradle.properties ./
COPY src ./src

# shadowJar does not run the tests: they need their own Docker daemon for Testcontainers.
RUN ./gradlew shadowJar --no-daemon

# ---- runtime stage ----
FROM eclipse-temurin:25-jre

WORKDIR /app

RUN useradd --system --uid 1001 --create-home app
USER app

COPY --from=build /src/build/libs/ktor-ktorm-postgresql-all.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
