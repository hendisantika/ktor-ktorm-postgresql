# Ktor Ktorm PostgreSQL

A RESTful API for book management built with Ktor, Ktorm ORM, and PostgreSQL. This project demonstrates how to create a
modern backend service using Kotlin and related technologies.

## Technologies

- **Kotlin** - Programming language
- **Ktor 3.2.3** - Asynchronous web framework
- **Ktorm** - Lightweight ORM framework for Kotlin
- **PostgreSQL** - Relational database
- **Gradle** - Build tool
- **Docker** - Containerization
- **Testcontainers** - Integration testing with real database instances

## Project Structure

- `src/main/kotlin/id/my/hendisantika/`
    - `Application.kt` - Main application entry point
    - `config/` - Application configuration
    - `model/` - Data models and entities
    - `route/` - API endpoints
    - `service/` - Business logic

## API Endpoints

The API provides the following endpoints for book management:

| Method | Endpoint   | Description       | Request Body | Response                |
|--------|------------|-------------------|--------------|-------------------------|
| GET    | /book      | Get all books     | -            | Array of BookResponse   |
| GET    | /book/{id} | Get a book by ID  | -            | BookResponse or Error   |
| POST   | /book      | Create a new book | BookRequest  | 201 Created or Error    |
| PATCH  | /book/{id} | Update a book     | BookRequest  | 204 No Content or Error |
| DELETE | /book/{id} | Delete a book     | -            | 204 No Content or Error |

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

The project uses PostgreSQL in a Docker container. To start the database:

```bash
docker-compose up -d
```

This will:

- Start PostgreSQL on port 5438
- Create a database named `ktor_postgres`
- Initialize the database with the schema and sample data

Database credentials:

- Username: yu71
- Password: 53cret

### Running the Application

To run the application:

```bash
./gradlew run
```

The server will start on http://0.0.0.0:8080

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

## License

This project is open source and available under the [MIT License](LICENSE).

## Author

Hendi Santika - [@hendisantika34](https://github.com/hendisantika)