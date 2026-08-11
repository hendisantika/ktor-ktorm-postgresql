# Ktor Ktorm PostgreSQL

[![Build and Test](https://github.com/hendisantika/ktor-ktorm-postgresql/actions/workflows/build.yml/badge.svg)](https://github.com/hendisantika/ktor-ktorm-postgresql/actions/workflows/build.yml)

A RESTful API for book management built with Ktor, Ktorm ORM, and PostgreSQL. This project demonstrates how to create a
modern backend service using Kotlin and related technologies.

## Technologies

| Technology         | Version | Purpose                                            |
|--------------------|---------|----------------------------------------------------|
| **Kotlin**         | 2.4.10  | Modern programming language for the JVM            |
| **Ktor**           | 3.5.2   | Asynchronous web framework built by JetBrains      |
| **Ktorm**          | 4.2.1   | Lightweight ORM framework for Kotlin with SQL DSL  |
| **PostgreSQL**     | 17.5    | Advanced open-source relational database           |
| **PostgreSQL JDBC**| 42.7.13 | JDBC driver                                        |
| **Logback**        | 1.5.38  | Logging backend                                    |
| **Gradle**         | 9.3.1   | Build automation (via the Gradle wrapper)          |
| **JDK**            | 25      | Build + runtime target (pinned Gradle toolchain)   |
| **JUnit**          | 5.14.4  | Test framework (JUnit Jupiter)                     |
| **Testcontainers** | 2.0.5   | Integration testing against a real database        |
| **Docker Compose** | -       | Local PostgreSQL for development                   |

## Project Structure

- `src/main/kotlin/id/my/hendisantika/`
    - `Application.kt` - Main application entry point
    - `Routing.kt` - Sample root route
    - `config/`
        - `DatabaseFactory.kt` - Ktorm database connection (environment configurable)
        - `Serialization.kt` - JSON content negotiation
    - `model/` - Data models and entities (`Book`, `Books`, request/response DTOs)
    - `route/BookRoutes.kt` - API endpoints
    - `service/BookService.kt` - Business logic
- `src/main/resources/` - `application.yaml`, `logback.xml`
- `src/test/kotlin/id/my/hendisantika/`
    - `PostgresTestContainer.kt` - Starts a PostgreSQL container for tests and resets its schema
    - `route/BookRoutesTest.kt` - HTTP-level tests for the endpoints and their status codes
    - `service/BookServiceTest.kt` - Integration tests for the book service
- `sql/init-db.sql` - Schema + sample data loaded on first container start
- `Dockerfile` - Multi-stage build of the application image (JDK 25 build, JRE 25 runtime)
- `compose.yml` - PostgreSQL and the application service

## API Endpoints

The API provides the following endpoints for book management:

| Method | Endpoint    | Description       | Request Body | Success Response      | Error Response              |
|--------|-------------|-------------------|--------------|-----------------------|-----------------------------|
| GET    | /books      | Get all books     | -            | 200 + BookResponse[]  | -                           |
| GET    | /books/{id} | Get a book by ID  | -            | 200 + BookResponse    | 400 invalid id, 404 missing |
| POST   | /books      | Create a new book | BookRequest  | 201 Created           | 400 + ErrorResponse         |
| PATCH  | /books/{id} | Update a book     | BookRequest  | 204 No Content        | 400 invalid id, 404 missing |
| DELETE | /books/{id} | Delete a book     | -            | 204 No Content        | 400 invalid id, 404 missing |

> An id that is not a number is a `400 Bad Request`; a well-formed id with no matching row is a `404 Not Found`. Both
> carry an `ErrorResponse` body.

### Data Models

**BookRequest**

```json
{
  "name": "Book Title"
}
```

**BookResponse**

```json
{
  "id": 1,
  "name": "Book Title"
}
```

**ErrorResponse**

```json
{
  "message": "Book with id [999] not found"
}
```

## Setup and Running

### Prerequisites

- JDK 25 (the Gradle build pins a Java 25 toolchain)
- Docker and Docker Compose (for the database and for the Testcontainers-based tests)

### Running Everything with Docker Compose

`compose.yml` defines both the database and the application, so the whole stack starts with one command:

```bash
docker compose up -d --build
```

The `app` service builds from the `Dockerfile`, waits for the database healthcheck before starting, reaches
PostgreSQL over the Compose network (`postgres-sandbox:5432`), and serves the API on http://localhost:8080. It runs
as a non-root user. Rebuild it after changing the code with `docker compose up -d --build app`.

### Database Only

For local development against `./gradlew run`, start just the database:

```bash
docker compose up -d postgres-sandbox
```

This will:

- Start PostgreSQL on port 5438
- Create a database named `ktor_postgres`
- Initialize the schema and sample data from `./sql/init-db.sql` (only on first start, when the volume is empty)
- Expose a healthcheck so `docker compose ps` reports when the database is ready

Database credentials:

- Database: ktor_postgres
- Username: yu71
- Password: 53cret
- Port: 5438

Data lives in the named Docker volume `postgres-data`. To start over from a clean database:

```bash
docker compose down -v && docker compose up -d postgres-sandbox
```

### Configuration

The connection details default to the values in `compose.yml`, and can be overridden with environment variables (or
JVM system properties of the same name):

| Variable      | Default                                            |
|---------------|----------------------------------------------------|
| `DB_URL`      | `jdbc:postgresql://localhost:5438/ktor_postgres`   |
| `DB_USER`     | `yu71`                                             |
| `DB_PASSWORD` | `53cret`                                           |

```bash
DB_URL="jdbc:postgresql://localhost:5438/ktor_postgres" ./gradlew run
```

### Running the Application

To run the application:

```bash
./gradlew run
```

The server will start on http://0.0.0.0:8080 and expose the REST API endpoints described above.

### Trying the API

```bash
# List every book
curl -s http://localhost:8080/books
# [{"id":1,"name":"The Great Gatsby"},{"id":2,"name":"To Kill a Mockingbird"},{"id":3,"name":"1984"}]

# Fetch one
curl -s http://localhost:8080/books/1
# {"id":1,"name":"The Great Gatsby"}

# Create one (201, empty body)
curl -i -X POST http://localhost:8080/books \
  -H 'Content-Type: application/json' \
  -d '{"name":"Dune"}'

# The new id comes from the sequence, so it is only 4 on a freshly seeded database.
# Read it back from the list, then update and delete it:
ID=$(curl -s http://localhost:8080/books |
  python3 -c 'import json,sys; print([b["id"] for b in json.load(sys.stdin) if b["name"] == "Dune"][-1])')

curl -i -X PATCH http://localhost:8080/books/$ID \
  -H 'Content-Type: application/json' \
  -d '{"name":"Dune Messiah"}'
# HTTP/1.1 204 No Content

curl -i -X DELETE http://localhost:8080/books/$ID
# HTTP/1.1 204 No Content

# Error cases
curl -s -w ' -> %{http_code}\n' http://localhost:8080/books/999
# {"message":"Book with id [999] not found"} -> 404
curl -s -w ' -> %{http_code}\n' http://localhost:8080/books/abc
# {"message":"Invalid id"} -> 400
```

The same requests are available as [`app.http`](app.http), runnable straight from IntelliJ IDEA's HTTP client.

## Building & Running

| Task                                    | Description                                                          |
|-----------------------------------------|----------------------------------------------------------------------|
| `./gradlew test`                        | Run the tests                                                        |
| `./gradlew build`                       | Build everything (compile + test)                                    |
| `./gradlew buildFatJar`                 | Build an executable JAR of the server with all dependencies included |
| `./gradlew buildImage`                  | Build the docker image to use with the fat JAR                       |
| `./gradlew publishImageToLocalRegistry` | Publish the docker image locally                                     |
| `./gradlew run`                         | Run the server                                                       |
| `./gradlew runDocker`                   | Run using the local docker image                                     |

The fat JAR is written to `build/libs/ktor-ktorm-postgresql-all.jar` and can be started directly. It is Java 25
bytecode, so it needs a Java 25 runtime:

```bash
java -jar build/libs/ktor-ktorm-postgresql-all.jar
```

`buildImage` produces `build/jib-image.tar` on top of a JRE 25 base image. It is an alternative to the `Dockerfile`
that the Compose `app` service builds from — use whichever suits you. To run the JIB image against the Compose
database:

```bash
docker load -i build/jib-image.tar
docker run --rm -p 8080:8080 \
  -e DB_URL="jdbc:postgresql://host.docker.internal:5438/ktor_postgres" \
  -e DB_USER=yu71 -e DB_PASSWORD=53cret \
  ktor-docker-image:latest
```

## Testing

The project uses Testcontainers for integration testing with a real PostgreSQL database, so Docker must be running.
The container is started once per test JVM, and each test re-creates the `book` table from
`src/test/resources/init-test-db.sql`, so tests are independent of each other and of your local database.

- `BookServiceTest` covers the Ktorm data access
- `BookRoutesTest` drives the routes over HTTP with `testApplication`, pinning the response codes (including 404 for a
  missing book)

```bash
./gradlew test
```

The HTML test report is written to `build/reports/tests/test/index.html`.

## CI/CD

A single workflow, **Build and Test** (`.github/workflows/build.yml`), runs on push and pull request to `main`:

- `build`: JDK 25 on Ubuntu with Gradle caching, runs `./gradlew build` — including the Testcontainers integration
  tests, since Docker is preinstalled on the GitHub-hosted Ubuntu runners — and uploads the test report as an artifact
- `dependency-submission`: submits the dependency graph that feeds Dependabot alerts (push events only)

## License

This project is open source and available under the [MIT License](LICENSE).

## Author

Hendi Santika - [@hendisantika34](https://github.com/hendisantika)

## Last Updated

[![Last commit](https://img.shields.io/github/last-commit/hendisantika/ktor-ktorm-postgresql?label=last%20commit&style=flat-square)](https://github.com/hendisantika/ktor-ktorm-postgresql/commits/main)

The badge above is rendered from the repository's commit history, so this section never needs editing.
