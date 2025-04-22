-- Create the database
CREATE DATABASE IF NOT EXISTS inventoria;
USE inventoria;

-- ----------------------------------
-- Users & Addresses
-- ----------------------------------
CREATE TABLE `User`(
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    firstname VARCHAR(100) NOT NULL,
    lastname VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20),
    is_active BOOLEAN DEFAULT TRUE,
    role ENUM('customer', 'admin', 'manager') DEFAULT 'customer',
    joined_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    profile_url VARCHAR(255)
);

CREATE TABLE UserAddress (
    address_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    address_line VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100),
    postal_code VARCHAR(20) NOT NULL,
    country VARCHAR(100) NOT NULL,
    address_type ENUM('billing', 'shipping') NOT NULL,
    is_default BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES `User`(user_id) ON DELETE CASCADE
);

-- ----------------------------------
-- Product, Supplier, Category
-- ----------------------------------
CREATE TABLE Supplier (
    supplier_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL,
    phone VARCHAR(20),
    address VARCHAR(255),
    contact_person VARCHAR(100),
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Category (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    parent_category_id INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (parent_category_id) REFERENCES Category(category_id) ON DELETE SET NULL
);

CREATE TABLE Product (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(150) NOT NULL,
    description TEXT,
    sku VARCHAR(50) UNIQUE,
    barcode VARCHAR(50),
    image_url VARCHAR(255),
    quantity_in_stock INT NOT NULL DEFAULT 0,
    reorder_level INT DEFAULT 10,
    unit_price DECIMAL(10,2) NOT NULL,
    cost_price DECIMAL(10,2),
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    supplier_id INT,
    category_id INT,
    FOREIGN KEY (supplier_id) REFERENCES Supplier(supplier_id),
    FOREIGN KEY (category_id) REFERENCES Category(category_id)
);

-- ----------------------------------
-- Inventory Transactions
-- ----------------------------------
CREATE TABLE InventoryTransaction (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    transaction_type ENUM('purchase', 'sale', 'return', 'adjustment') NOT NULL,
    reference_id INT,
    notes TEXT,
    transaction_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    user_id INT,
    FOREIGN KEY (product_id) REFERENCES Product(product_id),
    FOREIGN KEY (user_id) REFERENCES `User`(user_id)
);

-- ----------------------------------
-- Cart & Order System
-- ----------------------------------
CREATE TABLE CartItem (
    cartitem_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    added_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES `User`(user_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES Product(product_id)
);

CREATE TABLE `Order` (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    order_status ENUM('pending', 'processing', 'shipped', 'delivered', 'cancelled') DEFAULT 'pending',
    total_amount DECIMAL(10,2) NOT NULL,
    shipping_address_id INT,
    billing_address_id INT,
    notes TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES User(user_id),
    FOREIGN KEY (shipping_address_id) REFERENCES UserAddress(address_id),
    FOREIGN KEY (billing_address_id) REFERENCES UserAddress(address_id)
);

CREATE TABLE OrderItem (
    order_item_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES `Order`(order_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES Product(product_id)
);

CREATE TABLE Payment (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    payment_method ENUM('credit_card', 'paypal', 'bank_transfer', 'cash') NOT NULL,
    transaction_id VARCHAR(100),
    status ENUM('pending', 'paid', 'failed', 'refunded') DEFAULT 'pending',
    paid_at DATETIME,
    FOREIGN KEY (order_id) REFERENCES `Order`(order_id)
);

-- ----------------------------------
-- Activity Log
-- ----------------------------------
CREATE TABLE Activity_Log (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    action VARCHAR(100) NOT NULL,
    entity_type VARCHAR(100) NOT NULL,
    entity_id INT,
    details TEXT,
    ip_address VARCHAR(45),
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES `User`(user_id)
);

-- ----------------------------------
-- Purchase Order Management
-- ----------------------------------
CREATE TABLE PurchaseOrder (
    po_id INT AUTO_INCREMENT PRIMARY KEY,
    supplier_id INT NOT NULL,
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    delivery_date DATETIME,
    status ENUM('draft', 'ordered', 'received', 'cancelled') DEFAULT 'draft',
    total_amount DECIMAL(10,2) NOT NULL DEFAULT 0,
    payment_status ENUM('unpaid', 'partial', 'paid') DEFAULT 'unpaid',
    notes TEXT,
    created_by INT,
    FOREIGN KEY (supplier_id) REFERENCES Supplier(supplier_id),
    FOREIGN KEY (created_by) REFERENCES `User`(user_id)
);

CREATE TABLE PurchaseOrderItem (
    po_item_id INT AUTO_INCREMENT PRIMARY KEY,
    po_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    received_quantity INT DEFAULT 0,
    FOREIGN KEY (po_id) REFERENCES PurchaseOrder(po_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES Product(product_id)
);

-- ----------------------------------
-- Indexes for better performance
-- ----------------------------------
CREATE INDEX idx_product_name ON Product(product_name);
CREATE INDEX idx_supplier_name ON Supplier(name);
CREATE INDEX idx_category_name ON Category(name);
CREATE INDEX idx_user_email ON `User`(email);
CREATE INDEX idx_order_user ON `Order`(user_id);
CREATE INDEX idx_order_status ON `Order`(order_status);
CREATE INDEX idx_product_supplier ON Product(supplier_id);
CREATE INDEX idx_product_category ON Product(category_id);
CREATE INDEX idx_inventory_transaction_product ON InventoryTransaction(product_id);
CREATE INDEX idx_inventory_transaction_type ON InventoryTransaction(transaction_type);
CREATE INDEX idx_activity_log_user ON Activity_Log(user_id);
CREATE INDEX idx_activity_log_action ON Activity_Log(action);