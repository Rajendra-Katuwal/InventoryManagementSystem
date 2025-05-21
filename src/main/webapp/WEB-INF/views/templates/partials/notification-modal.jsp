<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- JSP Notification Rendering -->
<c:if test="${not empty sessionScope.error}">
	<div id="notification-error-${System.currentTimeMillis()}"
		class="notification-modal notification-modal-error">
		<div class="notification-modal-indicator"></div>
		<div class="notification-modal-content">
			<div class="notification-modal-icon">⚠️</div>
			<div class="notification-modal-message">${sessionScope.error}</div>
		</div>
		<div class="notification-modal-actions">
			<button class="notification-modal-close"
				onclick="closeNotification('notification-error-${System.currentTimeMillis()}')">✕</button>
		</div>
		<div class="notification-modal-progress"></div>
	</div>
	<c:remove var="error" scope="session" />
</c:if>

<c:if test="${not empty sessionScope.success}">
	<div id="notification-success-${System.currentTimeMillis()}"
		class="notification-modal notification-modal-success">
		<div class="notification-modal-indicator"></div>
		<div class="notification-modal-content">
			<div class="notification-modal-icon">✅</div>
			<div class="notification-modal-message">${sessionScope.success}</div>
		</div>
		<div class="notification-modal-actions">
			<button class="notification-modal-close"
				onclick="closeNotification('notification-success-${System.currentTimeMillis()}')">✕</button>
		</div>
		<div class="notification-modal-progress"></div>
	</div>
	<c:remove var="success" scope="session" />
</c:if>

<c:if test="${not empty sessionScope.warning}">
	<div id="notification-warning-${System.currentTimeMillis()}"
		class="notification-modal notification-modal-warning">
		<div class="notification-modal-indicator"></div>
		<div class="notification-modal-content">
			<div class="notification-modal-icon">⚠️</div>
			<div class="notification-modal-message">${sessionScope.warning}</div>
		</div>
		<div class="notification-modal-actions">
			<button class="notification-modal-close"
				onclick="closeNotification('notification-warning-${System.currentTimeMillis()}')">✕</button>
		</div>
		<div class="notification-modal-progress"></div>
	</div>
	<c:remove var="warning" scope="session" />
</c:if>

<c:if test="${not empty sessionScope.info}">
	<div id="notification-info-${System.currentTimeMillis()}"
		class="notification-modal notification-modal-info">
		<div class="notification-modal-indicator"></div>
		<div class="notification-modal-content">
			<div class="notification-modal-icon">ℹ️</div>
			<div class="notification-modal-message">${sessionScope.info}</div>
		</div>
		<div class="notification-modal-actions">
			<button class="notification-modal-close"
				onclick="closeNotification('notification-info-${System.currentTimeMillis()}')">✕</button>
		</div>
		<div class="notification-modal-progress"></div>
	</div>
	<c:remove var="info" scope="session" />
</c:if>