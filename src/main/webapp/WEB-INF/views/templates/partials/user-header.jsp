<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<header aria-label="Main Navigation">
	<div class="header-container">
		<div class="logo-container">
			<div class="logo-icon">I</div>
			<div class="logo-text">Inventoria</div>
		</div>

		<nav role="navigation">
			<ul>
				<li><a href="${pageContext.request.contextPath}/"
					aria-current="page">Home</a></li>
				<li><a href="${pageContext.request.contextPath}/features">Features</a></li>
				<li><a href="${pageContext.request.contextPath}/pricing">Pricing</a></li>
				<li><a href="${pageContext.request.contextPath}/resources">Resources</a></li>
				<li><a href="${pageContext.request.contextPath}/contact">Contact</a></li>
			</ul>
		</nav>

		<div class="auth-buttons">
			<a href="${pageContext.request.contextPath}/login" class="btn-login">Log
				In</a> <a href="${pageContext.request.contextPath}/register"
				class="btn-signup">Sign Up</a>
		</div>

		<button class="mobile-menu-btn" aria-expanded="false"
			aria-label="Toggle mobile menu">☰</button>
	</div>
</header>