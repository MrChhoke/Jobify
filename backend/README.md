# Spring Boot and REST API

## Description

This Spring Boot application serves as a backend service for storing and managing data related to recruiters and job
vacancies. Leveraging a PostgreSQL database, it offers REST API endpoints for efficient interaction with the entities.

![Entity Relationship Diagram](./docs-photos/entity-relationship-diagram.png)

## Technologies

- Java 17
- Spring Boot 3.2.5 (with Spring Web, Spring Data JPA, Spring Validation)
- PostgreSQL 16
- Maven 4.0.0
- Docker
- Liquibase
- Apache POI

## Usage

The application provides the following REST API endpoints:

| Method | Endpoint                                   | Description                                              | Example field in request body                                                                                                    | Roles (Permissions)                                      |
|--------|--------------------------------------------|----------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------|
| POST   | `/api/v1/auth/login`                       | Get JWT token for user                                   | `username: admin`,<br/> `password: admin`                                                                                        | Guest (access allowed for all)                           |
| POST   | `api/v1/auth/register`                     | Register a new user                                      | `username: admin`,<br/> `password: admin`                                                                                        | Guest (access allowed for all)                           |
| POST   | `/api/v1/recruiter/register`               | Create a new recruiter                                   | `first_name: Vladyslav`,<br/> `last_name: Bondar`,<br/> `company: ProfItSoft`, <br/> `username: vlad`, <br/> `password:password` | Admin                                                    |
| GET    | `/api/v1/recruiter/applications`           | Get all applications for authed recruiter                |                                                                                                                                  | Recruiter                                                |
| GET    | `/api/v1/recruiter/vacancies`              | Get all vacancies for authed recruiter                   |                                                                                                                                  | Recruiter                                                |
| GET    | `/api/v1/vacancy/{id}`                     | Return details of a vacancy by ID                        |                                                                                                                                  | User, Recruiter, Admin                                   |
| POST   | `/api/v1/vacancy`                          | Create a new vacancy                                     | `position: Java Developer`,<br/> `salary: 3000`,<br/> `technology_stack: [Java, Spring]`, <br/> `recruiter_id: 1`                | Recruiter, Admin                                         |
| PUT    | `/api/v1/vacancy/{id}`                     | Update a vacancy by ID                                   | `salary: 3500`                                                                                                                   | Recruiter, Admin                                         |
| DELETE | `/api/v1/vacancy/{id}`                     | Delete a vacancy by ID                                   |                                                                                                                                  | Recruiter, Admin                                         |
| POST   | `/api/v1/vacancy/_list`                    | Return a list of vacancies by a filter                   | `technology_stack: [Java, Spring]`                                                                                               | Guest (access allowed for all)                           |
| POST   | `/api/v1/vacancy/_report`                  | Generate Excel report of vacancies by a filter           | `technology_stack: [Java, Spring]`, </br> `position: Java Developer`, </br> `salary: 3000`                                       | Admin                                                    |
| POST   | `/api/v1/vacancy/{id}/apply`               | Apply for a vacancy by ID (must be auth)                 |                                                                                                                                  | User, Admin                                              |
| GET    | `/api/v1/vacancy/{id}/applications`        | Get all applications of vacancy the for authed recruiter |                                                                                                                                  | Recruiter                                                |
| GET    | `/api/v1/vacancy/person/{id}/applications` | Get all applications for a person by ID                  |                                                                                                                                  | Admin                                                    |
| GET    | `/api/v1/vacancy/applied`                  | Get all vacancies for which the authed user has applied  |                                                                                                                                  | User                                                     |
| GET    | `api/v1/profile`                           | Get profile of the authenticated user                    |                                                                                                                                  | User, Recruiter, Admin                                   |
| PUT    | `api/v1/profile/update`                    | Update profile of the authenticated user                 | `first_name: Vladyslav`,<br/> `last_name: Bondar`                                                                                | User, Recruiter, Admin                                   |
| PUT    | `/api/v1/developers/update-to-admin`       | Update user to Admin role                                |                                                                                                                                  | Guest (access allowed for all, if dev profile is active) |
| PUT    | `/api/v1/developers/update-to-recruiter`   | Update user to Recruiter role                            |                                                                                                                                  | Guest (access allowed for all, if dev profile is active) |                                                  |
| PUT    | `/api/v1/developers/update-to-user`        | Update user to User role                                 |                                                                                                                                  | Guest (access allowed for all, if dev profile is active) |                                                 |

## Testing

### Unit tests

All unit tests require Docker Engine.

### Enhanced testing endpoints

You can easly test the application using http requests. A few files for testing the application are available in
the `src/test/resources/http-requests` directory. You can use files from this directory in your IDE.