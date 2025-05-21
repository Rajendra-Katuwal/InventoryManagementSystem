<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Inventoria IMS - Server Error</title>
<link rel="stylesheet" href="../css/servererror.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/servererror.css">
</head>

<body>
	<div class="error-container">
		<div class="error-logo">
			<div class="logo">I</div>
			<div class="brand-name">Inventoria</div>
		</div>

		<div class="warning-icon">⚠️</div>

		<h1 class="error-title">Server Error</h1>
		<div class="error-code">Error 500</div>

		<p class="error-message">Something went wrong on our servers. This
			is not your fault! Our technical team has been notified and is
			working to fix the issue.</p>

		<div class="action-group">
			<a href="javascript:location.reload()" class="action-btn">Try
				Again</a> <a href="index.html" class="secondary-btn">Go to Dashboard</a>
		</div>
	</div>
</body>

</html>