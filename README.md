
# Coding Learning Platform

## Vision

Coding Learning Platform is a Java backend API designed to support learning programming through subjects, practice questions, and AI-powered assistance.

The project explores how REST APIs, databases, authentication, and external AI services can work together in a learning platform.

---

## Links

- **Deployed API:** https://learning.deployedcode.dk
- **Health check:** https://learning.deployedcode.dk/auth/healthcheck
- **Source code:** https://github.com/Schel-beck/3.semesterProjekt
- **Developer portfolio:** https://schel-beck.github.io/portfolio/

---

# Architecture

## System Overview

The application uses a layered Java backend structure:

- **Routes:** Define HTTP endpoints.
- **Controllers:** Handle requests and responses.
- **Services:** Handle functionality such as AI chat and external API interaction.
- **DAOs / persistence:** Access PostgreSQL through JPA/Hibernate.
- **Entities and DTOs:** Represent database records and data exchanged through the API.

### Technologies

- Java 17
- Maven
- Javalin
- Hibernate / JPA
- PostgreSQL
- JWT authentication
- BCrypt password hashing
- Docker
- GitHub Actions
- External AI API

## Architecture Diagram

```text
HTTP Client
     |
     v
Javalin Routes
     |
     v
Controllers
     |
     +----> Services ----> External AI API
     |
     v
DAOs
     |
     v
Hibernate / JPA
     |
     v
PostgreSQL
```

## Key Design Decisions

### Project Structure

I separated routes, controllers, services, and database access into different classes to make the project easier to understand, maintain, and test.

### Database

I chose PostgreSQL to store users, subjects, questions, and the relationships between them.

Hibernate/JPA is used to map Java entities to database tables.

### Authentication

Users can register and log in to the platform.

Passwords are hashed using BCrypt, and JWT tokens are used for authentication.

The role-assignment endpoint includes an administrator permission check.

### Validation and Error Handling

Registration validates required fields and password requirements.

Invalid registration data returns `400 Bad Request`, and incorrect login credentials return `401 Unauthorized`.

### Deployment

The backend is deployed using Docker on a DigitalOcean server.

GitHub Actions is used to build and publish the Docker image, and HTTPS is configured for the deployed API.

---

# Data Model

## ERD

<img width="1205" height="1106" alt="Untitled" src="https://github.com/user-attachments/assets/bb371d52-f254-4e00-b001-74a666c5661b" />


## Important Entities

### User

Represents a registered user of the platform.

Important fields:

- id
- username
- email
- password (hashed)
- questionsAnswered
- createdAt
- updatedAt

### Role

Represents a role assigned to users, such as USER or ADMIN.

### User_role

Connects users to their assigned roles.

### Subject

Represents a programming topic that users can study.

Important fields:

- id
- name
- numberOfQuestions
- createdAt
- updatedAt

### Question

Represents a practice question belonging to a subject.

Important fields:

- id
- name
- subject
- createdAt
- updatedAt

### User_question

Represents the relationship between a user and a question.

---

# API Documentation

## Authentication

| Method | Endpoint | Description |
|---|---|---|
| POST | `/auth/register` | Register a new user |
| POST | `/auth/login` | Log in and receive a JWT |
| POST | `/auth/user/addrole` | Assign a role |
| GET | `/auth/healthcheck` | Check API availability |

### Register a User

```http
POST /auth/register
Content-Type: application/json

{
  "username": "student1",
  "email": "student1@example.com",
  "password": "Password123!"
}
```

A successful registration returns `201 Created` with a token and username.

Invalid registration data returns `400 Bad Request`.

### Login

```http
POST /auth/login
Content-Type: application/json

{
  "username": "student1",
  "password": "Password123!"
}
```

A successful login returns `200 OK` with a JWT token.

Invalid credentials return `401 Unauthorized`.

### Using a JWT Token

For authenticated requests, include the token in the Authorization header:

```http
Authorization: Bearer <JWT_TOKEN>
```

## Subjects

| Method | Endpoint | Description |
|---|---|---|
| GET | `/subjects` | Get all subjects |
| GET | `/subjects/{id}` | Get a subject by ID |
| POST | `/subjects` | Create a subject |
| PUT | `/subjects/{id}` | Update a subject |
| DELETE | `/subjects/{id}` | Delete a subject |

## Questions

| Method | Endpoint | Description |
|---|---|---|
| GET | `/questions` | Get all questions |
| GET | `/questions/{id}` | Get a question by ID |
| POST | `/subjects/{id}/questions` | Create a question for a subject |
| PUT | `/questions/{id}` | Update a question |
| DELETE | `/questions/{id}` | Delete a question |

## User Questions

| Method | Endpoint | Description |
|---|---|---|
| GET | `/user-questions` | Get all user-question records |
| GET | `/user-questions/{id}` | Get a record by ID |
| POST | `/user-questions` | Create a record |
| PUT | `/user-questions/{id}` | Update a record |
| DELETE | `/user-questions/{id}` | Delete a record |

## AI Chat

```http
POST /chat
```

The chat endpoint communicates with an external AI service to support programming-related questions.

---

# User Stories

These user stories were written to describe the functionality and intended use of the project.

- As a visitor, I want to register an account so I can use the learning platform.
- As a registered user, I want to log in securely so I can access authenticated functionality.
- As a learner, I want to browse programming subjects so I can choose what to study.
- As a learner, I want to view questions associated with a subject so I can practice programming concepts.
- As a learner, I want to use AI chat assistance so I can ask questions about programming topics.

---

# Development Notes

This project was developed as part of my third-semester backend portfolio assignment.

During development, I worked with:

- Designing REST endpoints using Javalin.
- Creating database entities and relationships using Hibernate/JPA.
- Connecting Java to a PostgreSQL database.
- Integrating an external AI API.
- Implementing JWT authentication and password hashing.
- Writing integration tests.
- Deploying the backend using Docker and GitHub Actions.
- Improving validation, error handling, and security.

The weekly developer portfolio contains more detailed reflections about what I learned, the problems I encountered, and how I solved them.

---

# Running the Project Locally

1. Install Java 17, Maven, and PostgreSQL.
2. Configure the database and application settings in `src/main/resources/config.properties`.
3. Start the application using `app.Main` in IntelliJ.
4. The API runs locally at `http://localhost:7070`.
5. Use the HTTP requests in `src/test/dev.http` to test the endpoints.

The `config.properties` file is excluded from Git because it contains sensitive configuration values.

Do not commit database passwords, JWT secrets, or external API keys.

---

# Testing

Run the automated tests using:

```bash
mvn test
```

The project includes integration tests for backend functionality.

Additional HTTP requests can be found in `src/test/dev.http`.
