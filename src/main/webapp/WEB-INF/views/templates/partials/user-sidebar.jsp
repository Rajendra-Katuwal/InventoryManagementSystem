<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<nav class="user-sidebar" id="userSidebar"
	aria-label="Customer Navigation">
	<div class="user-sidebar-header">
		<button class="hamburger-menu" onclick="toggleSidebar()"
			aria-expanded="false" aria-label="Toggle sidebar navigation">
			<i class="fas fa-angle-left" aria-hidden="true"></i>
		</button>
		<img src="${pageContext.request.contextPath}/assets/images/logo.png"
			alt="Inventoria Logo" class="sidebar-logo">
		<h2>Inventoria</h2>
		<p>Customer Portal</p>
	</div>
	<ul class="user-sidebar-list" role="menu">
		<li class="user-sidebar-item" role="menuitem"><a
			href="${pageContext.request.contextPath}/customer/dashboard"
			class="user-sidebar-link ${fn:contains(pageContext.request.requestURI, '/dashboard') ? 'active' : ''}"
			data-tooltip="Dashboard" aria-label="Go to Dashboard"> <span
				class="user-sidebar-icon" aria-hidden="true">📊</span> <span
				class="user-sidebar-title">Dashboard</span>
		</a></li>
		<li class="user-sidebar-item" role="menuitem"><a
			href="${pageContext.request.contextPath}/customer/browse-products"
			class="user-sidebar-link" data-tooltip="Browse Products"
			aria-label="Browse Products"> <span class="user-sidebar-icon"
				aria-hidden="true">🛍️</span> <span class="user-sidebar-title">Browse
					Products</span>
		</a></li>
		<li class="user-sidebar-item" role="menuitem"><a
			href="${pageContext.request.contextPath}/customer/orders"
			class="user-sidebar-link" data-tooltip="My Orders"
			aria-label="View My Orders"> <span class="user-sidebar-icon"
				aria-hidden="true">🛒</span> <span class="user-sidebar-title">My
					Orders</span>
		</a></li>
		<li class="user-sidebar-item" role="menuitem"><a
			href="${pageContext.request.contextPath}/customer/cart"
			class="user-sidebar-link" data-tooltip="Cart" aria-label="View Cart">
				<span class="user-sidebar-icon" aria-hidden="true">🛍️</span> <span
				class="user-sidebar-title">Cart</span>
		</a></li>
		<li class="user-sidebar-item" role="menuitem"><a
			href="${pageContext.request.contextPath}/customer/profile"
			class="user-sidebar-link" data-tooltip="Profile"
			aria-label="Go to Profile"> <span class="user-sidebar-icon"
				aria-hidden="true">🔧</span> <span class="user-sidebar-title">Profile</span>
		</a></li>
		<li class="user-sidebar-item" role="menuitem"><a
			href="${pageContext.request.contextPath}/logout"
			class="user-sidebar-link" data-tooltip="Logout" aria-label="Log out">
				<span class="user-sidebar-icon" aria-hidden="true">🚪</span> <span
				class="user-sidebar-title">Logout</span>
		</a></li>
	</ul>
	<div class="user-sidebar-footer" aria-label="User Information">
		<div class="user-sidebar-user">
			<c:choose>
				<c:when
					test="${not empty sessionScope.user and not empty sessionScope.user.name}">
					<c:choose>
						<c:when
							test="${not empty sessionScope.user.profilePic and sessionScope.user.profilePic != ''}">
							<img
								src="${pageContext.request.contextPath}${sessionScope.user.profilePic}"
								alt="Profile Picture" class="user-sidebar-user-initials">
						</c:when>
						<c:otherwise>
							<span class="user-sidebar-user-initials"> <c:set
									var="nameParts"
									value="${fn:split(sessionScope.user.name, ' ')}" /> <c:out
									value="${fn:substring(nameParts[0], 0, 1)}${fn:length(nameParts) > 1 ? fn:substring(nameParts[1], 0, 1) : ''}" />
							</span>
						</c:otherwise>
					</c:choose>
					<span class="user-sidebar-user-name"><c:out
							value="${sessionScope.user.name}" /></span>
				</c:when>
				<c:otherwise>
					<span class="user-sidebar-user-initials">?</span>
					<span class="user-sidebar-user-name">Customer</span>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</nav>
<div class="sidebar-backdrop" onclick="toggleSidebar()"
	aria-hidden="true"></div>