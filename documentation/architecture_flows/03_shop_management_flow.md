# 03. Shop Management Flow

This document details the architecture for **Creating**, **Approving**, and **Rejecting** shops.

## 1. Create Shop (Seller)

**Scenario**: A Seller requests to open a new shop.
**Endpoint**: `POST /api/shop/create`

### Data Structures
*   **Request**: `{ "name": "BestSpecs", "description": "Quality glasses", ... }`
*   **Response**: Shop Object with `status: "PENDING"`.

```mermaid
sequenceDiagram
    autonumber
    actor Seller
    participant Frontend as ShopForm (UI)
    participant Controller as ShopController
    participant Service as ShopService
    participant DB as Shop Table

    Seller->>Frontend: Submit Shop Details
    Frontend->>Controller: POST /shop/create
    Controller->>Service: createShop(shop)
    Service->>Service: Set Status = PENDING
    Service->>DB: INSERT INTO shop
    DB-->>Service: Saved Shop
    Service-->>Frontend: JSON Response
    Frontend->>Seller: "Shop Pending Approval"
```

---

## 2. Approve Shop (Admin)

**Scenario**: Admin approves a pending shop.
**Endpoint**: `PUT /api/admin/shops/{id}/approve`

```mermaid
sequenceDiagram
    autonumber
    actor Admin
    participant Dashboard as Admin Dashboard
    participant Controller as ShopController
    participant Service as ShopService
    participant DB as Shop Table

    Admin->>Dashboard: Click "Approve"
    Dashboard->>Controller: PUT /shops/1/approve
    Controller->>Service: approveShop(1)
    Service->>DB: UPDATE shop SET status='APPROVED'
    DB-->>Service: Updated Shop
    Service-->>Dashboard: Success
    Dashboard->>Admin: Show "Active"
```

## Draw.io Shape Guide
*   **Actors**: Stickman (Seller / Admin)
*   **Code Components**: Rectangle
*   **Database**: Cylinder
