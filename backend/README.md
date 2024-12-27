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

# Swagger API Documentation

![Swagger logo](https://blog.haposoft.com/content/images/2021/10/131331002-b2ef5cd3-1e57-4a96-9155-f046d493e823.png)

This project uses Swagger to document and test RESTful APIs. Swagger provides an interactive UI to explore and validate
API endpoints.

To access the Swagger UI, navigate to `http://localhost:<your_server_port>/swagger-ui/index.html`.

## Testing

### Unit tests

All unit tests require Docker Engine.

### Enhanced testing endpoints

You can easly test the application using http requests. A few files for testing the application are available in
the `src/test/resources/http-requests` directory. You can use files from this directory in your IDE.