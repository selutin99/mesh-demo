# OOO Mesh Group test task

## Build JAR

- Package the application

`$ ./mvnw clean package`

> To skip the tests use: `-DskipTests=true` 

## Run

`$ docker-compose build && docker-compose up`

## Verify the application is running

> Application listens on port 8080.

```
GET http://localhost:8080/swagger-ui.html
```

## Test data
You can authenticate via `POST /login` endpoint 
for use other endpoints.

| User | Email | Phone | Password | Initial balance
| :---: | :---: | :---: | :---: | :---: |
| 1 | test@test.ru | 79207865432 | test | 100
| 2 | test3@test.ru | 79271414102 | test | 200 