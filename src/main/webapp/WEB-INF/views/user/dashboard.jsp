<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Customer Dashboard - Inventoria</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/customer-dashboard.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>

<body>
    <div class="container">
        <!-- Sidebar -->
        <div class="sidebar" id="sidebar">
            <div class="sidebar-header">
                <div class="logo-indicator"></div>
                <div class="logo-text">Inventoria</div>
            </div>

            <div class="sidebar-menu">
                <div class="menu-category">CUSTOMER PORTAL</div>
                <div class="menu-item">
                    <i class="fas fa-tachometer-alt"></i>
                    <span>Dashboard</span>
                </div>
                <div class="menu-item active">
                    <i class="fas fa-shopping-cart"></i>
                    <span>My Orders</span>
                </div>
                <div class="menu-item">
                    <i class="fas fa-truck"></i>
                    <span>Shipments</span>
                </div>
                <div class="menu-item">
                    <i class="fas fa-box"></i>
                    <span>Products</span>
                </div>
                <div class="menu-item">
                    <i class="fas fa-file-invoice"></i>
                    <span>Invoices</span>
                </div>

                <div class="menu-category">ACCOUNT</div>
                <div class="menu-item">
                    <i class="fas fa-user"></i>
                    <span>Profile</span>
                </div>
                <div class="menu-item">
                    <i class="fas fa-cog"></i>
                    <span>Settings</span>
                </div>
                <div class="menu-item">
                    <i class="fas fa-headset"></i>
                    <span>Support</span>
                </div>
            </div>

            <div class="user-info">
                <div class="user-avatar">JD</div>
                <div class="user-details">
                    <div class="user-name">John Doe</div>
                    <div class="user-role">Customer</div>
                </div>
            </div>
        </div>

        <!-- Main Content -->
        <div class="main-content">
            <div class="header">
                <div class="header-left">
                    <button class="toggle-sidebar" id="toggle-sidebar">
                        <i class="fas fa-bars"></i>
                    </button>
                    <div class="search-bar">
                        <i class="fas fa-search"></i>
                        <input type="text" placeholder="Search...">
                    </div>
                </div>

                <div class="header-actions">
                    <div class="notification-icon">
                        <i class="fas fa-bell"></i>
                        <span class="notification-badge">3</span>
                    </div>
                    <div class="settings-icon">
                        <i class="fas fa-cog"></i>
                    </div>
                </div>
            </div>

            <div class="dashboard">
                <div class="dashboard-title">
                    <h1 class="dashboard-header">Customer Dashboard</h1>
                </div>

                <div class="card-container">
                    <div class="card">
                        <div class="card-header">
                            <span>Total Orders</span>
                            <i class="fas fa-shopping-cart"></i>
                        </div>
                        <div class="card-value">27</div>
                        <div class="card-trend trend-up">
                            <i class="fas fa-arrow-up"></i>
                            <span>12.7% since last month</span>
                        </div>
                    </div>

                    <div class="card">
                        <div class="card-header">
                            <span>Pending Orders</span>
                            <i class="fas fa-clock"></i>
                        </div>
                        <div class="card-value">3</div>
                        <div class="card-trend trend-down">
                            <i class="fas fa-arrow-down"></i>
                            <span>2.8% since last week</span>
                        </div>
                    </div>

                    <div class="card">
                        <div class="card-header">
                            <span>In Transit</span>
                            <i class="fas fa-truck"></i>
                        </div>
                        <div class="card-value">5</div>
                        <div class="card-trend trend-up">
                            <i class="fas fa-arrow-up"></i>
                            <span>3.6% since yesterday</span>
                        </div>
                    </div>

                    <div class="card">
                        <div class="card-header">
                            <span>Total Spent</span>
                            <i class="fas fa-dollar-sign"></i>
                        </div>
                        <div class="card-value">$12,847</div>
                        <div class="card-trend trend-up">
                            <i class="fas fa-arrow-up"></i>
                            <span>5.3% since last month</span>
                        </div>
                    </div>
                </div>

                <div class="recent-orders">
                    <div class="recent-orders-header">
                        <div class="recent-orders-title">Recent Orders</div>
                        <div class="view-all">View All</div>
                    </div>

                    <table class="orders-table">
                        <thead>
                            <tr>
                                <th>Order ID</th>
                                <th>Product</th>
                                <th>Date</th>
                                <th>Amount</th>
                                <th>Status</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>#38492</td>
                                <td>Industrial Shelving</td>
                                <td>Apr 21, 2025</td>
                                <td>$1,299.00</td>
                                <td><span class="status status-delivered">Delivered</span></td>
                                <td><button class="action-button">View</button></td>
                            </tr>
                            <tr>
                                <td>#38491</td>
                                <td>Office Supplies</td>
                                <td>Apr 20, 2025</td>
                                <td>$245.50</td>
                                <td><span class="status status-processing">Processing</span></td>
                                <td><button class="action-button">View</button></td>
                            </tr>
                            <tr>
                                <td>#38489</td>
                                <td>Storage Containers</td>
                                <td>Apr 18, 2025</td>
                                <td>$789.99</td>
                                <td><span class="status status-pending">Pending</span></td>
                                <td><button class="action-button">View</button></td>
                            </tr>
                            <tr>
                                <td>#38487</td>
                                <td>Safety Equipment</td>
                                <td>Apr 16, 2025</td>
                                <td>$432.75</td>
                                <td><span class="status status-delivered">Delivered</span></td>
                                <td><button class="action-button">View</button></td>
                            </tr>
                        </tbody>
                    </table>
                </div>

                <div class="dashboard-footer">
                    <div class="recent-activity">
                        <div class="activity-header">
                            <div class="activity-title">Recent Activity</div>
                            <div class="view-all">View All</div>
                        </div>

                        <div class="activity-item">
                            <div class="activity-icon">
                                <i class="fas fa-truck"></i>
                            </div>
                            <div class="activity-details">
                                <div class="activity-message">Your order #38492 has been delivered</div>
                                <div class="activity-time">2 hours ago</div>
                            </div>
                        </div>

                        <div class="activity-item">
                            <div class="activity-icon">
                                <i class="fas fa-box"></i>
                            </div>
                            <div class="activity-details">
                                <div class="activity-message">New shipment received for Office Supplies</div>
                                <div class="activity-time">30 minutes ago</div>
                            </div>
                        </div>

                        <div class="activity-item">
                            <div class="activity-icon">
                                <i class="fas fa-exclamation-triangle"></i>
                            </div>
                            <div class="activity-details">
                                <div class="activity-message">Low stock alert for Industrial Shelving</div>
                                <div class="activity-time">3 hours ago</div>
                            </div>
                        </div>

                        <div class="activity-item">
                            <div class="activity-icon">
                                <i class="fas fa-file-invoice"></i>
                            </div>
                            <div class="activity-details">
                                <div class="activity-message">Invoice #INV-2025-042 has been generated</div>
                                <div class="activity-time">Yesterday</div>
                            </div>
                        </div>
                    </div>

                    <div class="quick-actions">
                        <div class="action-title">Quick Actions</div>

                        <div class="action-list">
                            <div class="action-item">
                                <div class="action-icon">
                                    <i class="fas fa-plus"></i>
                                </div>
                                <div class="action-name">New Order</div>
                            </div>

                            <div class="action-item">
                                <div class="action-icon">
                                    <i class="fas fa-search"></i>
                                </div>
                                <div class="action-name">Track Order</div>
                            </div>

                            <div class="action-item">
                                <div class="action-icon">
                                    <i class="fas fa-history"></i>
                                </div>
                                <div class="action-name">Order History</div>
                            </div>

                            <div class="action-item">
                                <div class="action-icon">
                                    <i class="fas fa-headset"></i>
                                </div>
                                <div class="action-name">Support</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- <script>
        // Toggle sidebar on mobile
        document.getElementById('toggle-sidebar').addEventListener('click', function () {
            document.getElementById('sidebar').classList.toggle('active');
        });

        // Close sidebar when clicking outside on mobile
        document.addEventListener('click', function (event) {
            const sidebar = document.getElementById('sidebar');
            const toggleBtn = document.getElementById('toggle-sidebar');

            if (window.innerWidth <= 768) {
                if (!sidebar.contains(event.target) && !toggleBtn.contains(event.target) && sidebar.classList.contains('active')) {
                    sidebar.classList.remove('active');
                }
            }
        });

        // Responsive adjustments
        window.addEventListener('resize', function () {
            const sidebar = document.getElementById('sidebar');

            if (window.innerWidth > 768) {
                sidebar.classList.remove('active');
            }
        });
    </script> -->
</body>

</html>
