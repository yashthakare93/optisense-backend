# 04. Admin User Management Flow

This document details the architecture for Admin operations on Users.

## 1. Get All Users

**Scenario**: Admin views list of all registered users.
**Endpoint**: `GET /api/admin/users`

### Data Structures
*   **Request**: None (JWT Token required in Header)
*   **Response**: `List<User>`

```mermaid
sequenceDiagram
    autonumber
    actor Admin
    participant Dashboard as Admin UI
    participant Controller as AdminController
    participant Service as UserService
    participant DB as User Table

    Admin->>Dashboard: Open User List
    Dashboard->>Controller: GET /admin/users
    Controller->>Service: findAll()
    Service->>DB: SELECT * FROM users
    DB-->>Service: List<User>
    Service-->>Controller: Return List
    Controller-->>Dashboard: JSON Response
    Dashboard->>Admin: Display Table
```

## 2. Delete User

**Scenario**: Admin bans/deletes a user.
**Endpoint**: `DELETE /api/admin/users/{id}`

```mermaid
sequenceDiagram
    autonumber
    actor Admin
    participant Dashboard as Admin UI
    participant Controller as AdminController
    participant Service as UserService
    participant DB as User Table

    Admin->>Dashboard: Click "Delete"
    Dashboard->>Controller: DELETE /admin/users/5
    Controller->>Service: deleteById(5)
    Service->>DB: DELETE FROM users WHERE id=5
    DB-->>Service: Void
    Service-->>Controller: Success
    Controller-->>Dashboard: 200 OK
    Dashboard->>Admin: Remove Row
```

## Draw.io Shape Guide
*   **Admin**: Stickman
*   **Dashboard/Controller/Service**: Rectangle
*   **Database**: Cylinder
