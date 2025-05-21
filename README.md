# 📦 Inventory Management System for a Warehouse

An Inventory Management System built using **JSP**, **Servlets**, and **JDBC** (non-Maven) in **Eclipse IDE**. This system helps warehouse staff manage inventory efficiently and allows customers to browse products, place orders, and make payments.

---

## 📖 Features

### ✅ Product Management (Admin)
- Add, update, or remove products.
- Product details include: Name, Description, Price, Quantity, SKU, and Category.

### ✅ Order Management
- Customers can place orders and track order status.
- Admin can view and update order statuses.
- Order Status Flow: `Pending → Paid → Shipped → Delivered`.

### ✅ Payment Management
- Customers can pay via Credit Card or PayPal (mock implementation).
- Payment statuses: `Pending`, `Completed`, `Failed`.
- Admin can view and manage all payments.

---

## 🧑‍💼 User Roles

### 👤 Customer
- Register/Login.
- Browse available products.
- Add products to cart/order.
- Checkout and make payments.
- View order history and track statuses.

### 🛠️ Admin
- Manage product inventory.
- View and manage customer orders.
- Update stock quantities.
- Handle payments and fulfillment processes.

---

## 🛠️ Tech Stack

| Technology         | Description                       |
|--------------------|-----------------------------------|
| JSP                | Frontend UI Pages                 |
| Servlets           | Backend Logic                     |
| JDBC               | Database Connectivity             |
| MySQL              | Relational Database               |
| Apache Tomcat      | Web Server                        |
| Eclipse IDE        | Development Environment           |
| HTML, CSS, JS      | UI Design and Interactivity       |

---


## ⚙️ Setup Instructions

1. **Clone or Download the Repository**
   ```bash
   git clone https://github.com/yourusername/inventory-management-jsp.git
   ```

2. **Open the Project in Eclipse**
   - `File` > `Import` > `Existing Projects into Workspace`
   - Select the project directory.
   - Choose `Dynamic Web Project`.

3. **Add MySQL JDBC Driver**
   - Download MySQL Connector JAR (`mysql-connector-java-<version>.jar`).
   - Place it inside the `lib/` folder.
   - Right-click JAR > Build Path > Add to Build Path.

4. **Configure the Database**
   - Create a MySQL database:
     ```sql
     CREATE DATABASE inventory_db;
     ```
   - Import the schema below or run the SQL manually.
   - Update your DB connection code in `DBConnection.java`:
     ```java
     private static final String URL = "jdbc:mysql://localhost:3306/inventory_db";
     private static final String USER = "your_username";
     private static final String PASSWORD = "your_password";
     ```

5. **Run the Project**
   - Right-click project > `Run on Server` > Choose Apache Tomcat.
   - Visit: `http://localhost:8080/InventoryManagementSystem`

---

## 🗃️ Database Schema

```sql
-- Database: inventory_db
CREATE DATABASE inventory_db;
USE inventory_db;

-- Categories
CREATE TABLE categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,
    INDEX idx_name (name)
);

-- Users
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(80) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('customer', 'admin') NOT NULL,
    contact_phone VARCHAR(15),
    profile_pic VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,
    INDEX idx_email (email),
    INDEX idx_role (role)
);

-- Products
CREATE TABLE products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    sku VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    stock_quantity INT NOT NULL DEFAULT 0,
    category_id INT NOT NULL,
    image_path VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idx_sku (sku),
    INDEX idx_category_id (category_id)
);

-- Cart
CREATE TABLE cart (
    cart_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_product_id (product_id)
);

-- Orders
CREATE TABLE orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    order_status ENUM('pending', 'paid', 'shipped', 'delivered', 'cancelled') NOT NULL DEFAULT 'pending',
    total_amount DECIMAL(10, 2) NOT NULL,
    shipping_address TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_order_status (order_status),
    INDEX idx_created_at (created_at)
);

-- Order Items
CREATE TABLE order_items (
    order_item_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE RESTRICT ON UPDATE CASCADE
);

-- Audit Logs
CREATE TABLE audit_logs (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    action VARCHAR(100) NOT NULL,
    details TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_created_at (created_at)
);
-- Contact Messages

CREATE TABLE contact_messages (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NULL, -- Nullable for non-authenticated users
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    subject VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL
);


INSERT INTO users (user_id, name, email, password, role)
VALUES (0, 'System', 'system@example.com', 'dummy-password', 'admin');

```

---

## 🔑 Sample Credentials

> **Admin**
- Email: `admin@example.com`
- Password: `admin123`

> **Customer**
- Email: `customer@example.com`
- Password: `cust123`

> *(You can register new users via the Registration Page as well.)*

---

## 🖼️ Screenshots

> _Add screenshots of key pages like:_
- Admin Dashboard
- Product Listing
- Order History
- Payment Checkout
