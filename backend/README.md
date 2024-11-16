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

| Method | Endpoint                                   | Description                                    | Example field in request body                                                                                   |
|--------|--------------------------------------------|------------------------------------------------|-----------------------------------------------------------------------------------------------------------------|
| POST   | `/api/v1/auth/login                        | Get jwt token for user                         | `username: admin`,<br/> `password: admin`                                                                       |
| POST   | `api/v1/auth/register`                     | Register a new user                            | `username: admin`,<br/> `password: admin`                                                                       |
| GET    | `/api/v1/recruiter/{id}`                   | Return details of a recruiter by ID            |                                                                                                                 |
| POST   | `/api/v1/recruiter`                        | Create a new recruiter                         | `firstName: Vladyslav`,<br/> `lastName: Bondar`,<br/> `company: ProfItSoft`                                     |
| PUT    | `/api/v1/recruiter/{id}`                   | Update a recruiter by ID                       | `company: ProfItSoft`                                                                                           |
| DELETE | `/api/v1/recruiter/{id}`                   | Delete a recruiter by ID                       |                                                                                                                 |
| GET    | `/api/v1/recruiter/{id}/applications`      | Applications for a recruiter by ID             |                                                                                                                 |
| GET    | `/api/v1/vacancy/{id}`                     | Return details of a vacancy by ID              |                                                                                                                 |
| POST   | `/api/v1/vacancy`                          | Create a new vacancy                           | `position: Java Developer`,<br/> `salary: 3000`,<br/> `technologyStack: [Java, Spring]`, <br/> `recruiterId: 1` |
| PUT    | `/api/v1/vacancy/{id}`                     | Update a vacancy by ID                         | `salary: 3500`                                                                                                  |
| DELETE | `/api/v1/vacancy/{id}`                     | Delete a vacancy by ID                         |                                                                                                                 |
| POST   | `/api/v1/vacancy/_list`                    | Return a list of vacancies by a filter         | `technologyStack: [Java, Spring]`                                                                               |
| POST   | `/api/v1/vacancy/_report`                  | Generate Excel report of vacancies by a filter | `technologyStack: [Java, Spring]`, </br> `position: Java Developer`, </br> `salary: 3000`                       |
| POST   | `/api/v1/vacancy/{id}/apply`               | Apply for a vacancy by ID (must be auth)       |                                                                                                                 |
| GET    | `/api/v1/vacancy/{id}/applications         | Get all applications for a vacancy by ID       |                                                                                                                 |
| GET    | `/api/v1/vacancy/person/{id}/applications` | Get all applications for a person by ID        |                                                                                                                 |

## Testing

### Unit tests

All unit tests require Docker Engine.

### Enhanced testing endpoints

You can easly test the application using http requests. A few files for testing the application are available in
the `src/test/resources/http-requests` directory. You can use files from this directory in your IDE.