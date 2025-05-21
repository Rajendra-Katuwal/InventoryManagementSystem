<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>${pageTitle}</title>
<link rel="icon"
	href="${pageContext.request.contextPath}/assets/images/favicon.ico">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/global.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/notification-modal.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/admin-sidebar.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/admin-template.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/${cssFile}.css">
<!-- Font Awesome CDN -->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css">
<script src="${pageContext.request.contextPath}/assets/js/notification-modal.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/admin-sidebar.js"></script>
<!-- Include Chart.js via CDN -->
<script
	src="https://cdn.jsdelivr.net/npm/chart.js@3.9.1/dist/chart.min.js"></script>
</head>
<body>
	<jsp:include page="/WEB-INF/views/templates/partials/admin-sidebar.jsp" />
	<jsp:include
		page="/WEB-INF/views/templates/partials/notification-modal.jsp" />
	<div class="admin-container">
		<div class="top-bar">
			<button class="menu-toggle" onclick="toggleSidebar()"
				aria-expanded="false" aria-label="Toggle menu">☰</button>
			<div class="top-bar-right">
				<div class="notification-icon">
					🔔 <span class="notification-badge">${notifications}</span>
				</div>
				<div class="settings-icon">⚙️</div>
			</div>
		</div>
		<div class="dashboard-content">
			<jsp:include page="${contentPage}" />
		</div>
	</div>
</body>
</html>