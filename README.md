# Restaurant Reservation Platform

Demo project to learn how to use **Spring Boot** to build a **microservice** running with its **own database** 

Microservice for a restaurant reservation platform.

## Technologies used
Spring Boot Web, Test, JPA and Security is used. An in-memory database H2 is used a persistent storage.

Swagger/Open API for API documentation

Testing:
* Unit Test Data layer: JPA Test (repository tests e.g. `ReservationRepositoryTest`)
* Unit Test Service layer: JUnit 5 and Mockito (service unit tests e.g. `ReservationServiceTest`)
* Unit Test Controllers: `@WebMvcTest` and `@MockBean` and `MockMvc`
* Integration Tests with `@SpringBootTest` and `Rest Assured`

## Project Requirements

* Spring/Spring Boot
* Java 11
* Lombok

## Build

to run the service

`./mvnw spring-boot:run`

## Build with Docker

to build the image

`mvn clean install`

`docker build -t restaurant-service .`

to run the image

`docker run -p 8090:8080 restaurant-service`   

## Documentation

The service API is documented using Open API Spring Doc 

SpringDoc is a tool that simplifies the generation and maintenance of API docs, based on the OpenAPI 3 specification.

JSON api format
`http://localhost:8080/api-docs/`

Swagger UI
`http://localhost:8080/swagger-ui.html`

You can also find a Postman collection in the documentation folder

`documentation/restaurant_reservation_service.postman_collection.json`

# Architecture of the service

* Api layer
* Service layer
* Database layer

## API Layer

The availabilities, reservations and restaurants API are defined in the `controller package`

## Service Layer

The business logic is implemented in the `service package`

## Database Layer

The database layer is defined in the `data package`

An embedded in memory databases - H2 has been used as database layer and JPA for CRUD operations.

The schema and initial data are defined in `resources/schema.sql` and  `resources/data.sql`

![ER Diagram](documentation/er-diagram.png?raw=true "Reservation ER Diagram")

## Security

The microservice uses Spring security with an in-memory authentication.

Only the POST method to create a restaurant is secured with only users having role ADMIN allowed to create a
restaurant.

The service uses a basic HTTP auth. It is not the most secure way to authenticate a user as username and password are
not encrypted (only decoded).

In a real production environment a different method from basic auth should be used.

## Trade-offs

Some features of the application haven't been considered for demo purposes, 
but these features should be implemented for the microservice to be production ready 

* HTTPS for the API
* a mechanism to handle concurrency in the booking. This could be handled by the atomicity and transactional nature of
the relational database, using composite primary key and attributes to indicate the status of a reservation

## AWS Component Architecture Diagram

Below a diagram about how the reservation microservice could be deployed on AWS.

![AWS Diagram](documentation/aws_diagram.png?raw=true "AWS Diagram")

## API

### Restaurants API

Return all restaurants

GET `http://localhost:8080/api/restaurants`

```json
[
     {
         "id": 1,
         "name": "Tacos",
         "location": "London",
         "tablesWithCapacity": {
             "1": 2,
             "2": 6,
             "3": 3,
             "4": 3,
             "5": 3
         }
     }
 ]
```

Return restaurant by id

GET `http://localhost:8080/api/restaurants/2`

```json
{
    "id": 2,
    "name": "CrazyPizza",
    "location": "Paris",
    "tablesWithCapacity": {
        "6": 3
    }
}
```

Create a restaurant

POST `http://localhost:8080/api/restaurants`

request body

```json
{
    "restaurantName": "Pizzerium",
    "location": "Epsom",
    "tableCapacities": [
        2,
        3,
        4
    ]
}
```
This is the only endpoint that require basic HTTP auth

`curl -X POST localhost:8080/api/restaurants -H "Content-type:application/json" 
     -d {\"restaurantName\": \"Pizzerium\", \"location\": \"Epsom\",\"tableCapacities\": [2,3,4]} -u admin:admin123`

### Availabilities API

Return availabilities by restaurant id, capacity and date

GET `http://localhost:8080/api/availabilities?restaurant=1&capacity=3&date=24-12-2020`

```json
{
    "restaurantId": 1,
    "restaurantName": "Tacos",
    "capacity": 3,
    "availabilityDates": {
        "2020-12-24": [
            5,
            4
        ]
    }
}
```
Return availabilities by restaurant id, capacity and date range

GET `http://localhost:8080/api/availabilities?restaurant=1&capacity=3&dateFrom=24-12-2020&dateUntil=26-12-2020`

```json
{
    "restaurantId": 1,
    "restaurantName": "Tacos",
    "capacity": 3,
    "availabilityDates": {
        "2020-12-26": [
            5,
            3,
            4
        ],
        "2020-12-25": [
            5,
            3,
            4
        ],
        "2020-12-24": [
            5,
            4
        ]
    }
}
```

### Reservations API

Create a reservation

POST `http://localhost:8080/api/reservations`

Request body

```json
{
    "restaurantId": 2,
    "customerId": 1,
    "tableId": 3,
    "partySize": 4
}
```

Amend a reservation

PUT `http://localhost:8080/api/reservations/3`

Request body

```json
{
    "partySize": 4,
    "tableId": 2
}
```

Delete a reservation

DELETE `http://localhost:8080/api/reservations/3`