<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Inventoria IMS - Access Forbidden</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/forbidden.css">
</head>

<body>
	<div class="error-container">
		<div class="error-logo">
			<div class="logo">I</div>
			<div class="brand-name">Inventoria</div>
		</div>

		<div class="error-icon">🚫</div>

		<h1 class="error-title">Access Forbidden</h1>
		<div class="error-code">Error 403</div>

		<p class="error-message">You don't have permission to access this
			resource. Please contact your administrator if you believe this is an
			error.</p>

		<div class="action-group">
			<a href="index.html" class="action-btn">Go to Dashboard</a> <a
				href="javascript:history.back()" class="secondary-btn">Go Back</a>
		</div>
	</div>
</body>

</html>