<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="dashboard">
	<div class="dashboard-title">
		<h1 class="dashboard-header">Customer Dashboard</h1>
	</div>

	<div class="card-container">
		<div class="card">
			<div class="card-header">
				<span>Total Orders</span> <i class="fas fa-shopping-cart"></i>
			</div>
			<div class="card-value">${totalOrders}</div>
			<div
				class="card-trend ${totalOrdersChange >= 0 ? 'trend-up' : 'trend-down'}">
				<i class="fas fa-arrow-${totalOrdersChange >= 0 ? 'up' : 'down'}"></i>
				<span><fmt:formatNumber value="${totalOrdersChange}"
						pattern="#.#" />% since last month</span>
			</div>
		</div>

		<div class="card">
			<div class="card-header">
				<span>Pending Orders</span> <i class="fas fa-clock"></i>
			</div>
			<div class="card-value">${pendingOrders}</div>
			<div
				class="card-trend ${pendingOrdersChange >= 0 ? 'trend-up' : 'trend-down'}">
				<i class="fas fa-arrow-${pendingOrdersChange >= 0 ? 'up' : 'down'}"></i>
				<span><fmt:formatNumber value="${pendingOrdersChange}"
						pattern="#.#" />% since last week</span>
			</div>
		</div>

		<div class="card">
			<div class="card-header">
				<span>In Transit</span> <i class="fas fa-truck"></i>
			</div>
			<div class="card-value">${inTransitShipments}</div>
			<div
				class="card-trend ${inTransitChange >= 0 ? 'trend-up' : 'trend-down'}">
				<i class="fas fa-arrow-${inTransitChange >= 0 ? 'up' : 'down'}"></i>
				<span><fmt:formatNumber value="${inTransitChange}"
						pattern="#.#" />% since yesterday</span>
			</div>
		</div>

		<div class="card">
			<div class="card-header">
				<span>Total Spent</span> <i class="fas fa-dollar-sign"></i>
			</div>
			<div class="card-value">
				$
				<fmt:formatNumber value="${totalSpent}" pattern="#,###" />
			</div>
			<div
				class="card-trend ${totalSpentChange >= 0 ? 'trend-up' : 'trend-down'}">
				<i class="fas fa-arrow-${totalSpentChange >= 0 ? 'up' : 'down'}"></i>
				<span><fmt:formatNumber value="${totalSpentChange}"
						pattern="#.#" />% since last month</span>
			</div>
		</div>
	</div>

	<div class="recent-orders">
		<div class="recent-orders-header">
			<div class="recent-orders-title">Recent Orders</div>
			<div class="view-all">
				<a href="${pageContext.request.contextPath}/customer/orders">View
					All</a>
			</div>
		</div>

		<table class="orders-table">
			<thead>
				<tr>
					<th>Order ID</th>
					<th>Product</th>
					<th>Date</th>
					<th>Amount</th>
					<th>Status</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="order" items="${recentOrders}">
					<tr>
						<td>#${order.orderId}</td>
						<td>${order.productName}</td>
						<td>${order.createdAt}</td>
						<td>$<fmt:formatNumber value="${order.totalAmount}"
								pattern="#,##0.00" /></td>
						<td><span
							class="status status-${order.orderStatus.toLowerCase()}">
								${order.orderStatus} </span></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>

	<div class="dashboard-footer">
		<div class="recent-activity">
			<div class="activity-header">
				<div class="activity-title">Recent Activity</div>
			</div>

			<c:forEach var="activity" items="${recentActivity}" begin="0" end="4">
				<div class="activity-item">
					<div class="activity-icon">
						<i class="fas fa-${activity.iconClass}"></i>
					</div>
					<div class="activity-details">
						<div class="activity-message">${activity.message}</div>
						<div class="activity-time">${activity.time}</div>
					</div>
				</div>
			</c:forEach>
		</div>

		<div class="quick-actions">
			<div class="action-title">Quick Actions</div>
			<div class="action-list">
				<div class="action-item">
					<div class="action-icon">
						<i class="fas fa-plus"></i>
					</div>
					<div class="action-name">
						<a href="${pageContext.request.contextPath}/customer/new-order">New
							Order</a>
					</div>
				</div>
				<div class="action-item">
					<div class="action-icon">
						<i class="fas fa-search"></i>
					</div>
					<div class="action-name">
						<a href="${pageContext.request.contextPath}/customer/track-order">Track
							Order</a>
					</div>
				</div>
				<div class="action-item">
					<div class="action-icon">
						<i class="fas fa-history"></i>
					</div>
					<div class="action-name">
						<a href="${pageContext.request.contextPath}/customer/orders">Order
							History</a>
					</div>
				</div>
				<div class="action-item">
					<div class="action-icon">
						<i class="fas fa-headset"></i>
					</div>
					<div class="action-name">
						<a href="${pageContext.request.contextPath}/customer/support">Support</a>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>