# 📦 Inventory Management System for a Warehouse

An Inventory Management System built using **JSP**, **Servlets**, and **JDBC** (non-Maven) in **Eclipse IDE**. This system helps warehouse staff manage inventory efficiently and allows customers to browse products, place orders, and make payments.

---

## 📝 Table of Contents

- [📖 Features](#-features)
- [🧑‍💼 User Roles](#-user-roles)
- [🛠️ Tech Stack](#-tech-stack)
- [📁 Project Structure](#-project-structure)
- [⚙️ Setup Instructions](#-setup-instructions)
- [🗃️ Database Schema](#️-database-schema)
- [🔑 Sample Credentials](#-sample-credentials)
- [🖼️ Screenshots](#️-screenshots)

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

### 🛠️ Admin (Warehouse Staff)
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

## 📁 Project Structure

```
InventoryManagementSystem/
│
├── WebContent/
│   ├── views/               # JSP files (UI pages)
│   ├── css/                 # Stylesheets
│   └── WEB-INF/
│       └── web.xml          # Deployment Descriptor
│
├── src/
│   ├── controller/          # Servlet classes
│   ├── dao/                 # Data Access Objects
│   ├── model/               # JavaBeans / POJOs
│   └── util/                # DB utility classes
│
├── lib/                    # JDBC driver (MySQL Connector JAR)
└── README.md               # Project Overview
```

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
-- USERS TABLE
CREATE TABLE users (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100),
  email VARCHAR(100) UNIQUE,
  password VARCHAR(100),
  role ENUM('admin', 'customer') NOT NULL
);

-- PRODUCTS TABLE
CREATE TABLE products (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100),
  description TEXT,
  price DECIMAL(10, 2),
  quantity INT,
  sku VARCHAR(50),
  category VARCHAR(50)
);

-- ORDERS TABLE
CREATE TABLE orders (
  id INT PRIMARY KEY AUTO_INCREMENT,
  user_id INT,
  order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
  status ENUM('Pending', 'Paid', 'Shipped', 'Delivered') DEFAULT 'Pending',
  FOREIGN KEY (user_id) REFERENCES users(id)
);

-- ORDER ITEMS TABLE
CREATE TABLE order_items (
  id INT PRIMARY KEY AUTO_INCREMENT,
  order_id INT,
  product_id INT,
  quantity INT,
  FOREIGN KEY (order_id) REFERENCES orders(id),
  FOREIGN KEY (product_id) REFERENCES products(id)
);

-- PAYMENTS TABLE
CREATE TABLE payments (
  id INT PRIMARY KEY AUTO_INCREMENT,
  order_id INT,
  payment_method VARCHAR(50),
  payment_status ENUM('Pending', 'Completed', 'Failed') DEFAULT 'Pending',
  payment_date DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (order_id) REFERENCES orders(id)
);
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
