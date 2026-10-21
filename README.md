# HMCTS Task Management — Backend

A Java Spring Boot REST API for managing caseworker tasks, built as part of the HMCTS Developer Technical Test. The service stores tasks in PostgreSQL and exposes endpoints to create, retrieve, list, update task status, and delete tasks.

This project extends the [HMCTS backend starter](https://github.com/hmcts/hmcts-dev-test-backend) and can be used with the [task management frontend](https://github.com/lokytech5/hmcts-task-frontend).

## Technology stack

- Java 21 and Spring Boot
- Spring Web for REST endpoints
- Spring Data JPA / Hibernate for persistence
- PostgreSQL 17
- Flyway for database schema migrations
- Gradle Wrapper for builds and tests
- Docker Compose for the local database

## Implemented functionality

- Create a task with a title, optional description, and due date/time
- Retrieve one task by ID or list all tasks
- Change a task's status (`PENDING`, `IN_PROGRESS`, or `COMPLETED`)
- Delete a task
- Persist data in PostgreSQL using Spring Data JPA
- Initialise the database schema using Flyway
- Return a not-found response when a requested task does not exist

**Current scope:** The update endpoint changes **status only**. Editing other task fields and more comprehensive automated testing are potential extensions; they are not described here as completed functionality.

## Prerequisites

- JDK 21
- Docker and Docker Compose
- A terminal (macOS, Linux, or Windows with an appropriate shell)

Gradle does not need to be installed separately: the repository includes the Gradle Wrapper.

## Run locally

From the backend repository directory:

### 1. Start PostgreSQL

```bash
docker compose up -d postgres
```

The supplied `compose.yaml` starts PostgreSQL 17 on `localhost:5432`, with a database named `hmcts_tasks`.

Check the container if needed:

```bash
docker compose ps
docker compose logs postgres
```

### 2. Start the Spring Boot API

```bash
./gradlew bootRun
```

The API is configured to run at **http://localhost:4000**. Spring Boot connects to the database and Flyway applies the migration in `src/main/resources/db/migration/`.

### 3. Check that the API responds

In a second terminal:

```bash
curl -i http://localhost:4000/api/tasks
```

A fresh database should have no tasks until you create one.

To stop the database later:

```bash
docker compose down
```

`docker compose down` preserves the named PostgreSQL data volume unless you explicitly remove volumes.

## Database configuration

The application reads these environment variables, with local development defaults:

| Environment variable | Local default | Purpose |
| --- | --- | --- |
| `DB_URL` | `jdbc:postgresql://localhost:5432/hmcts_tasks` | JDBC connection URL |
| `DB_USERNAME` | `task_user` | Database username |
| `DB_PASSWORD` | `task_password` | Database password |

These credentials are **examples for local development only**. Supply secure credentials through environment variables or a secret manager in any deployed environment.

Hibernate is configured with `ddl-auto: validate`, so the schema is managed by **Flyway**, rather than generated automatically by Hibernate. The initial migration is `V1__create_tasks_table.sql`.

## REST API

Base URL: `http://localhost:4000`

| Method | Path | Description |
| --- | --- | --- |
| `POST` | `/api/tasks` | Create a task |
| `GET` | `/api/tasks` | List tasks |
| `GET` | `/api/tasks/{id}` | Retrieve a task by ID |
| `PATCH` | `/api/tasks/{id}/status` | Update the task status |
| `DELETE` | `/api/tasks/{id}` | Delete a task |

### Task representation

| Field | Type | Description |
| --- | --- | --- |
| `id` | Number | Database-generated task ID |
| `title` | String | Task title |
| `description` | String or `null` | Optional details |
| `status` | String | `PENDING`, `IN_PROGRESS`, or `COMPLETED` |
| `dueDateTime` | ISO 8601 date/time | Due date/time with an offset, for example `2026-10-25T16:30:00Z` |

### Create a task

```bash
curl -i -X POST http://localhost:4000/api/tasks \
  -H 'Content-Type: application/json' \
  -d '{
    "title": "Review case documents",
    "description": "Check the submitted evidence",
    "dueDateTime": "2026-10-25T16:30:00Z"
  }'
```

New tasks start with the default status configured by the application.

### List all tasks

```bash
curl -i http://localhost:4000/api/tasks
```

### Retrieve one task

```bash
curl -i http://localhost:4000/api/tasks/1
```

Replace `1` with an ID returned when creating a task.

### Update task status

```bash
curl -i -X PATCH http://localhost:4000/api/tasks/1/status \
  -H 'Content-Type: application/json' \
  -d '{"status":"IN_PROGRESS"}'
```

Supported status values are `PENDING`, `IN_PROGRESS`, and `COMPLETED`.

### Delete a task

```bash
curl -i -X DELETE http://localhost:4000/api/tasks/1
```

A successful deletion returns HTTP `204 No Content`.

Requests for a task that does not exist are handled with a `404 Not Found` response.

## Build and test

Run the Gradle tests:

```bash
./gradlew clean test
```

Build the application:

```bash
./gradlew build
```

The backend's Gradle test command has been verified locally. This does **not** imply that all task-management behaviour is covered by automated tests; additional controller, service, repository, and integration tests would strengthen coverage.

## Project structure

```text
src/main/
├── java/uk/gov/hmcts/reform/dev/tasks/
│   ├── Task.java                     # JPA entity
│   ├── TaskStatus.java               # Status enum
│   ├── TaskRepository.java           # JPA repository
│   ├── TaskService.java              # Application logic
│   ├── TaskController.java           # REST endpoints
│   ├── TaskNotFoundException.java    # Not-found handling
│   └── dto/
│       ├── CreateTaskRequest.java
│       ├── UpdateTaskStatusRequest.java
│       └── TaskResponse.java
└── resources/
    ├── application.yaml
    └── db/migration/V1__create_tasks_table.sql

compose.yaml                         # Local PostgreSQL database
build.gradle                         # Dependencies and build configuration
```

The controller handles HTTP requests, the service coordinates task operations, the repository provides database access, and DTOs separate API payloads from persistence concerns.

## Frontend integration

The separate [frontend application](https://github.com/lokytech5/hmcts-task-frontend) consumes these endpoints. Its API client defaults to `http://localhost:4000` and can be configured using `TASK_API_URL`.

For local development, start PostgreSQL and the backend before starting the frontend. The frontend is served separately at `https://localhost:3100`.

