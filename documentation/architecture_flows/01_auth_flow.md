# 01. Authentication Architecture Flow

This document details the architecture and data flow for **User Signup** and **User Login**.

## 1. Signup Flow

**Scenario**: A new user registers an account.
**Endpoint**: `POST /api/auth/signup`

### Data Structures
*   **Request**: `{ "name": "John", "email": "john@example.com", "password": "pass", "role": "CUSTOMER" }`
*   **Response**: `{ "userId": 1, "token": "jwt_token_string", ... }`

```mermaid
sequenceDiagram
    autonumber
    actor User
    participant Frontend as React App
    participant Controller as AuthController
    participant Service as AuthService
    participant UserSvc as UserService
    participant DB as User Table

    User->>Frontend: Click "Sign Up"
    Frontend->>Controller: POST /signup
    Controller->>Service: signup(req)
    Service->>UserSvc: existsByEmail?
    UserSvc-->>Service: false
    Service->>UserSvc: save(User)
    UserSvc->>DB: INSERT
    DB-->>Service: User Entity
    Service->>Service: Generate JWT
    Service-->>Controller: AuthResponse
    Controller-->>Frontend: JSON Response
    Frontend->>User: Redirect
```

---

## 2. Login Flow

**Scenario**: Existing user logs in.
**Endpoint**: `POST /api/auth/login`

### Data Structures
*   **Request**: `{ "email": "john@example.com", "password": "pass" }`
*   **Response**: `{ "userId": 1, "token": "jwt_token_string", ... }`

```mermaid
sequenceDiagram
    autonumber
    actor User
    participant Frontend as React App
    participant Controller as AuthController
    participant Service as AuthService
    participant DB as User Table

    User->>Frontend: Click "Login"
    Frontend->>Controller: POST /login
    Controller->>Service: login(req)
    Service->>DB: findByEmail
    DB-->>Service: User Entity
    Service->>Service: Check Password
    Service->>Service: Generate JWT
    Service-->>Controller: AuthResponse
    Controller-->>Frontend: JSON Response
```

## Draw.io Shape Guide
*   **User**: Stickman
*   **Frontend/Controller/Service**: Rectangle
*   **Database**: Cylinder
