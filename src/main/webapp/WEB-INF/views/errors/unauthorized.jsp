<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Inventoria IMS - Unauthorized Access</title>
<link rel="stylesheet" href="../css/unauthorized.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/unauthorized.css">

</head>

<body>
	<div class="error-container">
		<div class="error-logo">
			<div class="logo">I</div>
			<div class="brand-name">Inventoria</div>
		</div>

		<div class="error-icon">🔒</div>

		<h1 class="error-title">Unauthorized Access</h1>
		<div class="error-code">Error 401</div>

		<p class="error-message">You don't have permission to access this
			page. Please log in with appropriate credentials or contact your
			system administrator for assistance.</p>

		<div class="action-group">
			<a href="login.html" class="action-btn">Log In</a> <a
				href="javascript:history.back()" class="secondary-btn">Go Back</a>
		</div>
	</div>
</body>

</html>