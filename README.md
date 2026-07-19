# Kong + Spring Boot

This project is for learning how to use Kong + Spring Boot + Auth.

## Architecture Diagram

```mermaid
graph TD
  Client[Client]
  Kong[Kong Gateway]
  
  Auth["Auth Service<br>(Port: 8081)<br>- /auth/login<br>- /auth/refresh"]
  Order["Order Service<br>(Port: 8082)<br>- /orders"]

  Client -->|Port 8000| Kong
  
  Kong -->|No JWT Plugin| Auth
  Kong -->|JWT Plugin| Order

  style Client fill:#f9f9f9,stroke:#333,stroke-width:2px
  style Kong fill:#f9f9f9,stroke:#333,stroke-width:2px
  style Auth fill:#f9f9f9,stroke:#333,stroke-width:2px
  style Order fill:#f9f9f9,stroke:#333,stroke-width:2px
```

## Setup

Generate enviroment vavariable

```bash
echo "JWT_SECRET_ISSUER=http://my-auth-service" >> .env

echo "JWT_SECRET=$(openssl rand -hex 16)" >> .env

echo "JWT_REFRESH_SECRET=$(openssl rand -hex 16)" >> .env
```

## Start Server

```bash
docker compose up -d --build 
```

## Test

```curl
curl -X POST http://localhost:8000/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "password"}'
```

```curl
curl -X POST http://localhost:8000/orders \
  -H "Authorization: Bearer <ACCESS_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{"product": "Request Product", "quantity": 1}'
```

```curl
curl http://localhost:8000/orders/<orderId> \
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```

```curl
curl -X POST http://localhost:8000/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{"refreshToken": "<REFRESH_TOKEN>"}'
```
