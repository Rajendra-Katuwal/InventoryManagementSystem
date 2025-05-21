<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<main class="main-content">
	<div class="dashboard-content">
		<h1 class="page-title">Dashboard</h1>

		<!-- Stats Section -->
		<div class="stats-grid">
			<div class="stat-card">
				<div class="stat-header">
					<div class="stat-title">Total Products</div>
					<div class="stat-icon">📦</div>
				</div>
				<div class="stat-value">${totalProducts}</div>
				<div
					class="stat-change ${totalProductsChange >= 0 ? 'positive' : 'negative'}">
					<span>${totalProductsChange >= 0 ? '↑' : '↓'}</span> <span><fmt:formatNumber
							value="${totalProductsChange}" pattern="#.#" />% since last
						month</span>
				</div>
			</div>
			<div class="stat-card">
				<div class="stat-header">
					<div class="stat-title">Low Stock Items</div>
					<div class="stat-icon">⚠️</div>
				</div>
				<div class="stat-value">${lowStockItems}</div>
				<div
					class="stat-change ${lowStockItemsChange >= 0 ? 'positive' : 'negative'}">
					<span>${lowStockItemsChange >= 0 ? '↑' : '↓'}</span> <span><fmt:formatNumber
							value="${lowStockItemsChange}" pattern="#.#" />% since last week</span>
				</div>
			</div>
			<div class="stat-card">
				<div class="stat-header">
					<div class="stat-title">Total Orders</div>
					<div class="stat-icon">🛒</div>
				</div>
				<div class="stat-value">${totalOrders}</div>
				<div
					class="stat-change ${totalOrdersChange >= 0 ? 'positive' : 'negative'}">
					<span>${totalOrdersChange >= 0 ? '↑' : '↓'}</span> <span><fmt:formatNumber
							value="${totalOrdersChange}" pattern="#.#" />% since last month</span>
				</div>
			</div>
			<div class="stat-card">
				<div class="stat-header">
					<div class="stat-title">Pending Shipments</div>
					<div class="stat-icon">🚚</div>
				</div>
				<div class="stat-value">${pendingShipments}</div>
				<div
					class="stat-change ${pendingShipmentsChange >= 0 ? 'positive' : 'negative'}">
					<span>${pendingShipmentsChange >= 0 ? '↑' : '↓'}</span> <span><fmt:formatNumber
							value="${pendingShipmentsChange}" pattern="#.#" />% since
						yesterday</span>
				</div>
			</div>
		</div>

		<!-- Main Dashboard Content -->
		<div class="dashboard-grid">
			<!-- Chart Section -->
			<div class="chart-card">
				<div class="card-header">
					<h2 class="card-title">Revenue Over Time</h2>
					<div class="card-actions">
						<form action="${pageContext.request.contextPath}/admin/dashboard"
							method="post" style="display: inline;">
							<input type="hidden" name="period" value="weekly">
							<button type="submit" class="card-action">Weekly</button>
						</form>
						<form action="${pageContext.request.contextPath}/admin/dashboard"
							method="post" style="display: inline;">
							<input type="hidden" name="period" value="monthly">
							<button type="submit" class="card-action">Monthly</button>
						</form>
						<form action="${pageContext.request.contextPath}/admin/dashboard"
							method="post" style="display: inline;">
							<input type="hidden" name="period" value="yearly">
							<button type="submit" class="card-action">Yearly</button>
						</form>
					</div>
				</div>
				<div class="chart-container">
					<canvas id="revenueChart"></canvas>
				</div>
			</div>

			<!-- Activity Section -->
			<div class="activity-card">
				<div class="card-header">
					<h2 class="card-title">Recent Activity</h2>
					<div class="card-actions">
						<a href="${pageContext.request.contextPath}/admin/activity"
							class="card-action">View All</a>
					</div>
				</div>
				<ul class="activity-list">
					<c:forEach var="log" items="${recentLogs}">
						<li class="activity-item">
							<div class="activity-icon">
								<c:choose>
									<c:when test="${log.action == 'SHIPMENT_RECEIVED'}">📦</c:when>
									<c:when test="${log.action == 'ORDER_PROCESSED'}">🛒</c:when>
									<c:when test="${log.action == 'LOW_STOCK_ALERT'}">⚠️</c:when>
									<c:when test="${log.action == 'INVENTORY_UPDATED'}">🔄</c:when>
									<c:otherwise>📋</c:otherwise>
								</c:choose>
							</div>
							<div class="activity-details">
								<div class="activity-text">${log.details}</div>
								<div class="activity-time">${log.createdAt}</div>
							</div>
						</li>
					</c:forEach>
				</ul>
			</div>

			<!-- Low Stock Items Table -->
			<div class="inventory-card" style="grid-column: span 2;">
				<div class="card-header">
					<h2 class="card-title">Low Stock Items</h2>
					<div class="card-actions">
						<a href="${pageContext.request.contextPath}/admin/inventory"
							class="card-action">View All</a>
					</div>
				</div>
				<table class="inventory-table">
					<thead>
						<tr>
							<th>Product Name</th>
							<th>Category</th>
							<th>Quantity</th>
							<th>Status</th>
							<th>Created At</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="product" items="${lowStockProducts}">
							<tr>
								<td>
									<div class="product-info">
										<c:choose>
											<c:when test="${not empty product.imagePath}">
												<img
													src="${pageContext.request.contextPath}/${product.imagePath}"
													alt="${product.name}" class="product-image">
											</c:when>
											<c:otherwise>
												<div class="product-image">📦</div>
											</c:otherwise>
										</c:choose>
										<div class="product-name">${product.name}</div>
									</div>
								</td>
								<td>${product.categoryName}</td>
								<td>${product.stockQuantity}</td>
								<td><span
									class="stock-status 
                                        <c:choose>
                                            <c:when test="${product.stockQuantity == 0}">out-of-stock</c:when>
                                            <c:when test="${product.stockQuantity <= 15}">low-stock</c:when>
                                            <c:otherwise>in-stock</c:otherwise>
                                        </c:choose>">
										<c:choose>
											<c:when test="${product.stockQuantity == 0}">Out of Stock</c:when>
											<c:when test="${product.stockQuantity <= 15}">Low Stock</c:when>
											<c:otherwise>In Stock</c:otherwise>
										</c:choose>
								</span></td>
								<td>${product.createdAt}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</main>

<script>
    let revenueChart;

    function initializeChart() {
        const ctx = document.getElementById('revenueChart');
        if (!ctx) {
            console.error("Canvas element 'revenueChart' not found!");
            return;
        }

        const chartContext = ctx.getContext('2d');
        if (revenueChart) {
            revenueChart.destroy();
        }

        // Prepare data directly from request attribute
        const labels = [
            <c:forEach var="entry" items="${revenueData}" varStatus="loop">
                "${fn:escapeXml(entry.periodLabel)}"${loop.last ? '' : ','}
            </c:forEach>
        ];
        const revenues = [
            <c:forEach var="entry" items="${revenueData}" varStatus="loop">
                ${entry.revenue}${loop.last ? '' : ','}
            </c:forEach>
        ];

        // Debug the data
        console.log("Labels:", labels);
        console.log("Revenues:", revenues);

        // If no data, show a placeholder
        if (labels.length === 0) {
            console.log("No revenue data available, showing placeholder.");
            labels.push("No Data");
            revenues.push(0);
        }

        revenueChart = new Chart(chartContext, {
            type: 'line',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Revenue ($)',
                    data: revenues,
                    borderColor: 'rgba(30, 58, 138, 0.8)', // --primary-blue
                    backgroundColor: 'rgba(30, 58, 138, 0.2)',
                    fill: true,
                    tension: 0.1
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    x: {
                        title: {
                            display: true,
                            text: 'Time Period'
                        }
                    },
                    y: {
                        beginAtZero: true,
                        title: {
                            display: true,
                            text: 'Revenue ($)'
                        }
                    }
                },
                plugins: {
                    legend: {
                        position: 'top'
                    }
                }
            }
        });
    }

    // Initialize chart on page load
    document.addEventListener("DOMContentLoaded", function () {
        try {
            initializeChart();
        } catch (error) {
            console.error("Error initializing chart:", error);
        }

        // Sidebar toggle
        const menuToggle = document.querySelector(".menu-toggle");
        const sidebar = document.querySelector(".admin-sidebar");

        if (menuToggle && sidebar) {
            menuToggle.addEventListener("click", () => {
                sidebar.classList.toggle("sidebar-open");
            });
        }
    });
</script>