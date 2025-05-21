<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>${pageTitle}</title>
<link rel="icon"
	href="${pageContext.request.contextPath}/assets/images/favicon.ico">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/global.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/notification-modal.css">
<c:if test="${empty sessionScope.user}">
	<link rel="stylesheet"
		href="${pageContext.request.contextPath}/assets/css/user-header.css">
</c:if>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/user-sidebar.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/contact.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/user-footer.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/${cssFile}.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
<script
	src="${pageContext.request.contextPath}/assets/js/notification-modal.js"></script>
<script
	src="${pageContext.request.contextPath}/assets/js/user-sidebar.js"></script>
<script
	src="${pageContext.request.contextPath}/assets/js/user-header.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/contact.js"></script>
</head>
<body>
	<c:choose>
		<c:when test="${empty sessionScope.user}">
			<jsp:include page="/WEB-INF/views/templates/partials/user-header.jsp" />
			<jsp:include
				page="/WEB-INF/views/templates/partials/notification-modal.jsp" />

			<div class="container">
				<jsp:include page="${contentPage}" />
			</div>
			<jsp:include page="/WEB-INF/views/templates/partials/user-footer.jsp" />
		</c:when>
		<c:otherwise>
			<c:if test="${sessionScope.user.role == 'customer'}">
				<jsp:include
					page="/WEB-INF/views/templates/partials/user-sidebar.jsp" />
				<jsp:include
					page="/WEB-INF/views/templates/partials/notification-modal.jsp" />
				<div class="customer-container">
					<jsp:include page="${contentPage}" />
				</div>
			</c:if>
		</c:otherwise>
	</c:choose>

</body>
</html>