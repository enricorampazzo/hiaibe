# Simple backend with authentication to test BE-FE integration issues

## Purpose
This is a _very_ basic spring boot application that includes spring-security.

It publishes 4 endpoints:

- /api/v1/auth/signup to register a new user
- /api/v1/auth/login to login with an existing user
- /api/v1/user to show the users currently registered
- /api/v1/user/me to show the details of the logged-in user

Authentication is implemented using the `Bearer` token, i.e. when you successfully login
the server returns a JSON with a `token` field containing the token
Subsequent calls to protected endpoints (under /api/v1/user) need to include the 
`Authorization` header with the value `Bearer <token>`

Passwords are returned in the JSON responses so you can see that they are hashed

## Running the application

### Installing Docker

This application is dockerized, ie it requires you to install Docker.

Once you do that, run `docker-compose up -d` in a terminal and wait for 
the hiaibe and mysql images to be running. You can easily check the status from 
Docker Desktop

### Sending HTTP requests to the endpoints with cURL
Once the application is up and running it is reachable at 127.0.0.1:8005

Here there are a few examples on how to register a user, login, and show the currently 
logged in user. It's quite simple and you can use these as a basis to send your 
own requests

#### Creating a new user (sign-up)

##### Request
```bash
curl --location 'http://localhost:8005/api/v1/auth/signup' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "some_user@some_domain.com",
    "password": "very safe password",
    "fullName": "itsMe" 
}'
```
##### Response
```json
{
    "id": 1,
    "fullName": "itsMe",
    "email": "some_user@some_domain.com",
    "password": "$2a$10$EE8rf3t53v1FSmVMWDgNAuBCGmAcOw8cGXFg5SStKUN2ZpQSju6he",
    "createdAt": "2024-08-28T14:00:31.963+00:00",
    "updatedAt": "2024-08-28T14:00:31.963+00:00",
    "authorities": [],
    "username": "some_user@some_domain.com",
    "enabled": true,
    "accountNonExpired": true,
    "accountNonLocked": true,
    "credentialsNonExpired": true
}

```

#### User login

##### Request
```bash
curl --location 'http://localhost:8005/api/v1/auth/login' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "some_user@some_domain.com",
    "password": "very safe password"
}'
```

##### Response
Notice the `token` field, duration is 1h in milliseconds
```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzb21lX3VzZXJAc29tZV9kb21haW4uY29tIiwiaWF0IjoxNzI0ODUzNjM5LCJleHAiOjE3MjQ4NTcyMzl9.M5ZNC50G_1qAAeDxL6p6FIc1kVpRo9vvXkQBhoZxRKE",
    "expiresIn": 3600000
}
```

#### Show the currently logged-in user
##### Request
Notice that the value returned by the login call is passed in the `Authorization`
header
```bash
curl --location 'http://localhost:8005/api/v1/user/me' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzb21lX3VzZXJAc29tZV9kb21haW4uY29tIiwiaWF0IjoxNzI0ODUzNjM5LCJleHAiOjE3MjQ4NTcyMzl9.M5ZNC50G_1qAAeDxL6p6FIc1kVpRo9vvXkQBhoZxRKE'
```
####
##### Response
```json
{
    "id": 1,
    "fullName": "itsMe",
    "email": "some_user@some_domain.com",
    "password": "$2a$10$LzrcvjMZViNz9j1mpahqo.qNPMpuLoa4I0mT37euZrlvtb.2pLEqi",
    "createdAt": "2024-08-28T14:42:40.205+00:00",
    "updatedAt": "2024-08-28T14:42:40.205+00:00",
    "authorities": [],
    "username": "some_user@some_domain.com",
    "enabled": true,
    "accountNonExpired": true,
    "accountNonLocked": true,
    "credentialsNonExpired": true
}
```

