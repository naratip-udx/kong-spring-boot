# Kong + Spring Boot

This project is for learning how to use Kong + Spring Boot + Auth.

## Architecture Diagram

```mermaid
graph TD
  Client[Client]
  Kong[Kong Gateway]
  
  Auth["Auth Service<br>(Port: 8082)<br>- /auth/login<br>- /auth/refresh"]
  Order["Order Service<br>(Port: 8081)<br>- /orders"]

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
echo "JWT_SECRET=$(openssl rand -base64 64)" >> .env

echo "JWT_REFRESH_SECRET=$(openssl rand -base64 64)" >> .env
```
