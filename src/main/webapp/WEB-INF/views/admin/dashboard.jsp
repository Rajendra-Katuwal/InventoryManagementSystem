<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inventoria - Admin Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-dashboard.css">
</head>

<body>
    <!-- Sidebar -->
    <aside class="sidebar">
        <div class="sidebar-header">
            <div class="logo-icon">I</div>
            <div class="logo-text">Inventoria</div>
        </div>

        <nav class="sidebar-menu">
            <div class="menu-category">Main</div>
            <ul>
                <li class="nav-item">
                    <a href="#" class="nav-link active">
                        <span class="nav-icon">📊</span>
                        <span>Dashboard</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a href="#" class="nav-link">
                        <span class="nav-icon">📦</span>
                        <span>Inventory</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a href="#" class="nav-link">
                        <span class="nav-icon">🛒</span>
                        <span>Orders</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a href="#" class="nav-link">
                        <span class="nav-icon">🏭</span>
                        <span>Suppliers</span>
                    </a>
                </li>
            </ul>

            <div class="menu-category">Management</div>
            <ul>
                <li class="nav-item">
                    <a href="#" class="nav-link">
                        <span class="nav-icon">🏪</span>
                        <span>Warehouses</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a href="#" class="nav-link">
                        <span class="nav-icon">👥</span>
                        <span>Customers</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a href="#" class="nav-link">
                        <span class="nav-icon">📝</span>
                        <span>Reports</span>
                    </a>
                </li>
            </ul>

            <div class="menu-category">Settings</div>
            <ul>
                <li class="nav-item">
                    <a href="#" class="nav-link">
                        <span class="nav-icon">⚙️</span>
                        <span>Settings</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a href="#" class="nav-link">
                        <span class="nav-icon">👤</span>
                        <span>Profile</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a href="#" class="nav-link">
                        <span class="nav-icon">❓</span>
                        <span>Help</span>
                    </a>
                </li>
            </ul>
        </nav>

        <div class="sidebar-footer">
            <div class="user-info">
                <div class="user-avatar">JD</div>
                <div class="user-details">
                    <div class="user-name">John Doe</div>
                    <div class="user-role">Admin</div>
                </div>
            </div>
        </div>
    </aside>

    <!-- Main Content -->
    <main class="main-content">
        <div class="top-bar">
            <button class="menu-toggle">☰</button>

            <div class="search-bar">
                <span>🔍</span>
                <input type="text" placeholder="Search...">
            </div>

            <div class="top-bar-right">
                <div class="notification-icon">
                    🔔
                    <span class="notification-badge">3</span>
                </div>
                <div class="settings-icon">⚙️</div>
            </div>
        </div>

        <div class="dashboard-content">
            <h1 class="page-title">Dashboard</h1>

            <!-- Stats Section -->
            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-header">
                        <div class="stat-title">Total Products</div>
                        <div class="stat-icon">📦</div>
                    </div>
                    <div class="stat-value">2,547</div>
                    <div class="stat-change positive">
                        <span>↑</span>
                        <span>5.3% since last month</span>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-header">
                        <div class="stat-title">Low Stock Items</div>
                        <div class="stat-icon">⚠️</div>
                    </div>
                    <div class="stat-value">14</div>
                    <div class="stat-change negative">
                        <span>↑</span>
                        <span>2.8% since last week</span>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-header">
                        <div class="stat-title">Total Orders</div>
                        <div class="stat-icon">🛒</div>
                    </div>
                    <div class="stat-value">368</div>
                    <div class="stat-change positive">
                        <span>↑</span>
                        <span>12.7% since last month</span>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-header">
                        <div class="stat-title">Pending Shipments</div>
                        <div class="stat-icon">🚚</div>
                    </div>
                    <div class="stat-value">24</div>
                    <div class="stat-change positive">
                        <span>↓</span>
                        <span>3.6% since yesterday</span>
                    </div>
                </div>
            </div>

            <!-- Main Dashboard Content -->
            <div class="dashboard-grid">
                <!-- Chart Section -->
                <div class="chart-card">
                    <div class="card-header">
                        <h2 class="card-title">Inventory Overview</h2>
                        <div class="card-actions">
                            <button class="card-action">Weekly</button>
                            <button class="card-action">Monthly</button>
                            <button class="card-action">Yearly</button>
                        </div>
                    </div>
                    <div class="chart-container">
                        <div class="chart-placeholder">
                            [Inventory Chart]
                        </div>
                    </div>
                </div>

                <!-- Activity Section -->
                <div class="activity-card">
                    <div class="card-header">
                        <h2 class="card-title">Recent Activity</h2>
                        <div class="card-actions">
                            <button class="card-action">View All</button>
                        </div>
                    </div>
                    <ul class="activity-list">
                        <li class="activity-item">
                            <div class="activity-icon">📦</div>
                            <div class="activity-details">
                                <div class="activity-text">New shipment received for Office Supplies</div>
                                <div class="activity-time">30 minutes ago</div>
                            </div>
                        </li>
                        <li class="activity-item">
                            <div class="activity-icon">🛒</div>
                            <div class="activity-details">
                                <div class="activity-text">Order #38492 has been processed</div>
                                <div class="activity-time">2 hours ago</div>
                            </div>
                        </li>
                        <li class="activity-item">
                            <div class="activity-icon">⚠️</div>
                            <div class="activity-details">
                                <div class="activity-text">Low stock alert for Industrial Shelving</div>
                                <div class="activity-time">3 hours ago</div>
                            </div>
                        </li>
                        <li class="activity-item">
                            <div class="activity-icon">🔄</div>
                            <div class="activity-details">
                                <div class="activity-text">Inventory count updated for Warehouse #3</div>
                                <div class="activity-time">5 hours ago</div>
                            </div>
                        </li>
                    </ul>
                </div>

                <!-- Inventory Table -->
                <div class="inventory-card" style="grid-column: span 2;">
                    <div class="card-header">
                        <h2 class="card-title">Low Stock Items</h2>
                        <div class="card-actions">
                            <button class="card-action">View All</button>
                        </div>
                    </div>
                    <table class="inventory-table">
                        <thead>
                            <tr>
                                <th>Product Name</th>
                                <th>Category</th>
                                <th>Quantity</th>
                                <th>Status</th>
                                <th>Last Updated</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>
                                    <div class="product-info">
                                        <div class="product-image">📦</div>
                                        <div class="product-name">Industrial Shelving</div>
                                    </div>
                                </td>
                                <td>Storage</td>
                                <td>12</td>
                                <td><span class="stock-status low-stock">Low Stock</span></td>
                                <td>Today, 9:41 AM</td>
                            </tr>
                            <tr>
                                <td>
                                    <div class="product-info">
                                        <div class="product-image">📦</div>
                                        <div class="product-name">Packing Tape</div>
                                    </div>
                                </td>
                                <td>Packaging</td>
                                <td>8</td>
                                <td><span class="stock-status low-stock">Low Stock</span></td>
                                <td>Yesterday, 2:30 PM</td>
                            </tr>
                            <tr>
                                <td>
                                    <div class="product-info">
                                        <div class="product-image">📦</div>
                                        <div class="product-name">Office Chairs</div>
                                    </div>
                                </td>
                                <td>Furniture</td>
                                <td>0</td>
                                <td><span class="stock-status out-of-stock">Out of Stock</span></td>
                                <td>April 20, 2025</td>
                            </tr>
                            <tr>
                                <td>
                                    <div class="product-info">
                                        <div class="product-image">📦</div>
                                        <div class="product-name">Shipping Labels</div>
                                    </div>
                                </td>
                                <td>Packaging</td>
                                <td>15</td>
                                <td><span class="stock-status low-stock">Low Stock</span></td>
                                <td>April 21, 2025</td>
                            </tr>
                            <tr>
                                <td>
                                    <div class="product-info">
                                        <div class="product-image">📦</div>
                                        <div class="product-name">Protective Gloves</div>
                                    </div>
                                </td>
                                <td>Safety</td>
                                <td>24</td>
                                <td><span class="stock-status in-stock">In Stock</span></td>
                                <td>April 21, 2025</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </main>

    <script>
        document.addEventListener("DOMContentLoaded", function () {
            const menuToggle = document.querySelector(".menu-toggle");
            const sidebar = document.querySelector(".sidebar");

            menuToggle.addEventListener("click", () => {
                sidebar.classList.toggle("sidebar-open");
            });
        });
    </script>


    <!-- <script>
        // Toggle sidebar on mobile
        document.querySelector('.menu-toggle').addEventListener('click', function () {
            document.querySelector('.sidebar').classList.toggle('open');
        });
    </script> -->
</body>

</html>    
