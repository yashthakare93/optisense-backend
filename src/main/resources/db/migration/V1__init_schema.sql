-- 1. Create Users Table
CREATE TABLE users (
    user_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL
);

-- 2. Create Shops Table
CREATE TABLE shops (
    shop_id BIGSERIAL PRIMARY KEY,
    seller_id BIGINT NOT NULL,
    shop_name VARCHAR(255) NOT NULL,
    business_type VARCHAR(100),
    phone VARCHAR(15) NOT NULL,
    email VARCHAR(255),
    shop_address VARCHAR(500) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    pin_code VARCHAR(6) NOT NULL,
    country VARCHAR(100),
    gst_number VARCHAR(15),
    aadhaar_number VARCHAR(12),
    pan_number VARCHAR(10),
    description VARCHAR(1000),
    status VARCHAR(50) NOT NULL,
    rejection_reason VARCHAR(500),
    approved_by BIGINT,
    approved_at TIMESTAMP,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- 3. Create Products Table
CREATE TABLE products_tbl (
    product_id BIGSERIAL PRIMARY KEY,
    shop_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    brand VARCHAR(255),
    description VARCHAR(2000),
    price DOUBLE PRECISION NOT NULL,
    stock_quantity INTEGER,
    category VARCHAR(50),
    discount INTEGER,
    discounted_price DOUBLE PRECISION,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- 4. Create Product Images (ElementCollection)
CREATE TABLE product_images_tbl (
    product_id BIGINT NOT NULL,
    image_url VARCHAR(255),
    FOREIGN KEY (product_id) REFERENCES products_tbl(product_id)
);

-- 5. Create Product Specifications (ElementCollection Map)
CREATE TABLE product_specifications_tbl (
    product_id BIGINT NOT NULL,
    spec_key VARCHAR(255) NOT NULL,
    spec_value VARCHAR(255),
    FOREIGN KEY (product_id) REFERENCES products_tbl(product_id)
);