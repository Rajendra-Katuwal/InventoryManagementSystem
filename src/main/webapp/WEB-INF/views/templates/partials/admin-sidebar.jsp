<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<nav class="admin-sidebar" id="adminSidebar"
	aria-label="Admin Navigation">
	<div class="admin-sidebar-header">
		<button class="hamburger-menu" onclick="toggleSidebar()"
			aria-expanded="false" aria-label="Toggle sidebar navigation">
			<i class="fas fa-angle-left" aria-hidden="true"></i>
		</button>
		<img src="${pageContext.request.contextPath}/assets/images/logo.png"
			alt="Inventoria Logo" class="sidebar-logo">
		<h2>Inventoria</h2>
		<p>Admin Portal</p>
	</div>
	<ul class="admin-sidebar-list" role="menu">
		<li class="admin-sidebar-item" role="menuitem"><a
			href="${pageContext.request.contextPath}/admin/dashboard"
			class="admin-sidebar-link" data-tooltip="Dashboard"
			aria-label="Go to Dashboard"> <span class="admin-sidebar-icon"
				aria-hidden="true">📊</span> <span class="admin-sidebar-title">Dashboard</span>
		</a></li>
		<li class="admin-sidebar-item" role="menuitem"><a
			href="${pageContext.request.contextPath}/admin/inventory"
			class="admin-sidebar-link" data-tooltip="Inventory"
			aria-label="Go to Inventory"> <span class="admin-sidebar-icon"
				aria-hidden="true">📦</span> <span class="admin-sidebar-title">Inventory</span>
		</a></li>
		<li class="admin-sidebar-item" role="menuitem"><a
			href="${pageContext.request.contextPath}/admin/orders"
			class="admin-sidebar-link" data-tooltip="Orders"
			aria-label="Go to Orders"> <span class="admin-sidebar-icon"
				aria-hidden="true">🛒</span> <span class="admin-sidebar-title">Orders</span>
		</a></li>
		<li class="admin-sidebar-item" role="menuitem"><a
			href="${pageContext.request.contextPath}/admin/audit-logs"
			class="admin-sidebar-link" data-tooltip="Activity Log"
			aria-label="Go to Activity Log"> <span class="admin-sidebar-icon"
				aria-hidden="true">👥</span> <span class="admin-sidebar-title">Activity
					Log</span>
		</a></li>
		<li class="admin-sidebar-item" role="menuitem"><a
			href="${pageContext.request.contextPath}/admin/sales-report"
			class="admin-sidebar-link" data-tooltip="Reports"
			aria-label="Go to Reports"> <span class="admin-sidebar-icon"
				aria-hidden="true">📈</span> <span class="admin-sidebar-title">Reports</span>
		</a></li>
		<li class="admin-sidebar-item" role="menuitem"><a
			href="${pageContext.request.contextPath}/admin/profile"
			class="admin-sidebar-link" data-tooltip="Profile"
			aria-label="Go to Profile"> <span class="admin-sidebar-icon"
				aria-hidden="true">⚙️</span> <span class="admin-sidebar-title">Profile</span>
		</a></li>
		<li class="admin-sidebar-item" role="menuitem"><a
			href="${pageContext.request.contextPath}/logout"
			class="admin-sidebar-link" data-tooltip="Logout" aria-label="Log out">
				<span class="admin-sidebar-icon" aria-hidden="true">🚪</span> <span
				class="admin-sidebar-title">Logout</span>
		</a></li>
	</ul>
	<div class="admin-sidebar-footer">
		<div class="admin-sidebar-user" aria-label="User Information">
			<span class="admin-sidebar-user-initials">${sessionScope.user.name.charAt(0)}${sessionScope.user.name.split(" ")[1].charAt(0)}</span>
			<span class="admin-sidebar-user-name">${sessionScope.user.name}</span>
		</div>
	</div>
</nav>
<div class="sidebar-backdrop" onclick="toggleSidebar()"
	aria-hidden="true"></div>