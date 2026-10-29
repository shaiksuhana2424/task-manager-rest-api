# Task Manager REST API

A Java REST API built with Spring Boot, Maven, Spring Data JPA, an H2 in-memory database, Bean Validation, JUnit 5, Mockito, MockMvc, and Swagger/OpenAPI.

## Requirements
- JDK 17 or later
- Maven 3.6+ (or use an IDE with Maven support)

## Run the application
```bash
mvn spring-boot:run
```
The API runs at `http://localhost:8080`.

## Run automated tests
```bash
mvn test
```

## API endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/tasks` | Create a task |
| GET | `/api/tasks` | List all tasks |
| GET | `/api/tasks/{id}` | Get a task by ID |
| PUT | `/api/tasks/{id}` | Update a task |
| DELETE | `/api/tasks/{id}` | Delete a task |

### Example request body
```json
{
  "title": "Prepare assignment",
  "description": "Build a CRUD REST API",
  "status": "PENDING"
}
```
`title` is required and must not exceed 120 characters. `description` can be up to 1000 characters. Allowed statuses are `PENDING`, `IN_PROGRESS`, and `COMPLETED`. If status is omitted on creation, it defaults to `PENDING`.

## H2 database
The application uses an in-memory H2 database. Data is reset when the application stops.
- H2 console: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:taskdb`
- Username: `sa`
- Password: (blank)

## Swagger/OpenAPI
Open `http://localhost:8080/swagger-ui.html` while the application is running.

## Error responses
Validation errors return HTTP 400 and include a `validationErrors` map. Requests for missing task IDs return HTTP 404. Successful creation returns HTTP 201; successful deletion returns HTTP 204.

## Project structure
- `controller` — REST endpoints
- `service` — application/business logic
- `repository` — Spring Data JPA persistence
- `model` — JPA entity and status enum
- `dto` — validated request object
- `exception` — consistent API error responses
- `src/test` — service unit tests and REST integration tests

## Database schema file
`postgres-schema.sql` provides a PostgreSQL-compatible schema as requested in the assignment's submission notes. The running application uses H2 by default, as required in the core objectives.

## GitHub submission
1. Create a public GitHub repository named `task-manager-rest-api`.
2. Extract this ZIP and open a terminal in the extracted project folder.
3. Run `mvn test` and confirm tests pass before submitting.
4. Commit the project and push it to GitHub.
5. Invite GitHub user `iks-hiring` to the repository if GitHub is used for submission.
