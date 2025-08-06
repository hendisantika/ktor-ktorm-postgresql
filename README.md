# Ktor Ktorm PostgreSQL

A RESTful API for book management built with Ktor, Ktorm ORM, and PostgreSQL. This project demonstrates how to create a
modern backend service using Kotlin and related technologies.

## Technologies

- **Kotlin 2.2.0** - Modern programming language for the JVM
- **Ktor 3.2.3** - Asynchronous web framework built by JetBrains
- **Ktorm** - Lightweight ORM framework for Kotlin with SQL DSL
- **PostgreSQL 17.5** - Advanced open-source relational database
- **Gradle** - Powerful build automation tool
- **Docker & Docker Compose** - Containerization and service orchestration
- **Testcontainers 1.19.7** - Integration testing with real database instances

## Project Structure

- `src/main/kotlin/id/my/hendisantika/`
    - `Application.kt` - Main application entry point
    - `config/` - Application configuration
    - `model/` - Data models and entities
    - `route/` - API endpoints
    - `service/` - Business logic

## API Endpoints

The API provides the following endpoints for book management:

| Method | Endpoint    | Description       | Request Body | Response                |
|--------|-------------|-------------------|--------------|-------------------------|
| GET    | /books      | Get all books     | -            | Array of BookResponse   |
| GET    | /books/{id} | Get a book by ID  | -            | BookResponse or Error   |
| POST   | /books      | Create a new book | BookRequest  | 201 Created or Error    |
| PATCH  | /books/{id} | Update a book     | BookRequest  | 204 No Content or Error |
| DELETE | /books/{id} | Delete a book     | -            | 204 No Content or Error |

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

## Setup and Running

### Prerequisites

- JDK 17 or higher
- Docker and Docker Compose

### Database Setup

The project uses PostgreSQL 17.5 in a Docker container. To start the database:

```bash
docker compose up -d
```

This will:

- Start PostgreSQL on port 5438
- Create a database named `ktor_postgres`
- Initialize the database with the schema and sample data from `./sql/init-db.sql`

Database credentials:

- Username: yu71
- Password: 53cret
- Port: 5438

### Running the Application

To run the application:

```bash
./gradlew run
```

The server will start on http://0.0.0.0:8080 and expose the REST API endpoints described above.

## Building & Running

| Task                          | Description                                                          |
|-------------------------------|----------------------------------------------------------------------|
| `./gradlew test`              | Run the tests                                                        |
| `./gradlew build`             | Build everything                                                     |
| `buildFatJar`                 | Build an executable JAR of the server with all dependencies included |
| `buildImage`                  | Build the docker image to use with the fat JAR                       |
| `publishImageToLocalRegistry` | Publish the docker image locally                                     |
| `run`                         | Run the server                                                       |
| `runDocker`                   | Run using the local docker image                                     |

## Testing

The project uses Testcontainers for integration testing with a real PostgreSQL database. To run the tests:

```bash
./gradlew test
```

## CI/CD

This project uses GitHub Actions for continuous integration and delivery:

- **Build and Test**: Runs on Ubuntu with JDK 17, triggered on push/PR to main branch
- **Java CI with Gradle**: More comprehensive workflow with JDK 21, Gradle caching, and dependency submission for
  Dependabot alerts

The CI pipelines automatically build the project and run all tests to ensure code quality.

## License

This project is open source and available under the [MIT License](LICENSE).

## Author

Hendi Santika - [@hendisantika34](https://github.com/hendisantika)

## Last Updated

2025-08-06