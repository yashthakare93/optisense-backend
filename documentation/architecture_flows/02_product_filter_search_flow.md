# 02. Product Filter & Search Flow

This document details the architecture for filtering products (by Category, Brand, Price, Frame Colour) and searching.

## 1. Filter Flow

**Scenario**: User filters by "Frame Colour: Black".
**Endpoint**: `GET /api/products?frameColours=Black`

### Data Structures
*   **Request Params**: `frameColours=Black`, `page=0`, `size=12`
*   **Response**: `Page<Product>` JSON object.

```mermaid
sequenceDiagram
    autonumber
    actor User
    participant Sidebar as FilterSidebar (UI)
    participant Marketplace as Marketplace (UI)
    participant Controller as ProductController
    participant Service as ProductService
    participant Spec as ProductSpecifications
    participant DB as Database

    User->>Sidebar: Select "Black"
    Sidebar->>Marketplace: Update State
    Marketplace->>Controller: GET /api/products?frameColours=Black
    Controller->>Service: getAllProducts
    Service->>Spec: Build Dynamic Query
    Spec-->>Service: JPA Specification
    Service->>DB: Execute Query
    Note right of Service: SQL: ... JOIN specs s WHERE s.val IN ('Black')
    DB-->>Service: List<Product>
    Service-->>Marketplace: JSON Response
    Marketplace->>User: Show Grid
```

## Draw.io Shape Guide
*   **User**: Stickman
*   **UI Components**: Blue Rectangle
*   **Backend Classes**: Green Rectangle
*   **Database**: Cylinder
*   **Query Logic**: Diamond or Parallelogram
