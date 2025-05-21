<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<div class="audit-logs-container">
	<h1>Audit Logs</h1>
	<div class="audit-logs-header">
		<form class="filter-form"
			action="${pageContext.request.contextPath}/admin/audit-logs"
			method="GET">
			<div class="filter-group">
				<label for="userId">Admin:</label> <select id="userId" name="userId">
					<option value="">All Admins</option>
					<c:forEach var="admin" items="${admins}">
						<option value="${admin.userId}"
							${admin.userId == userId ? 'selected' : ''}>
							${admin.name} (${admin.email})</option>
					</c:forEach>
				</select>
			</div>
			<div class="filter-group">
				<label for="action">Action:</label> <input type="text" id="action"
					name="action" value="${action}" placeholder="e.g., Order Updated">
			</div>
			<div class="filter-group">
				<label for="startDate">Start Date:</label> <input type="date"
					id="startDate" name="startDate" value="${startDate}">
			</div>
			<div class="filter-group">
				<label for="endDate">End Date:</label> <input type="date"
					id="endDate" name="endDate" value="${endDate}">
			</div>
			<button type="submit" class="btn btn-filter">Filter</button>
		</form>
	</div>

	<c:if test="${not empty error}">
		<div class="notification error">${error}</div>
		<c:remove var="error" scope="request" />
	</c:if>
	<c:if test="${not empty sessionScope.error}">
		<div class="notification error">${sessionScope.error}</div>
		<c:remove var="error" scope="session" />
	</c:if>
	<c:if test="${not empty sessionScope.success}">
		<div class="notification success">${sessionScope.success}</div>
		<c:remove var="success" scope="session" />
	</c:if>

	<div class="table-responsive">
		<table class="audit-logs-table">
			<thead>
				<tr>
					<th>Log ID</th>
					<th>Admin</th>
					<th>Action</th>
					<th>Details</th>
					<th>Timestamp</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="log" items="${auditLogs}">
					<tr>
						<td data-label="Log ID">${log.logId}</td>
						<td data-label="Admin"><c:set var="admin" value="${null}" />
							<c:forEach var="user" items="${admins}">
								<c:if test="${user.userId == log.userId}">
									<c:set var="admin" value="${user}" />
								</c:if>
							</c:forEach> <c:choose>
								<c:when test="${admin != null}">
									${admin.name} (${admin.email})
								</c:when>
								<c:otherwise>
									Unknown
								</c:otherwise>
							</c:choose></td>
						<td data-label="Action">${log.action}</td>
						<td data-label="Details">${log.details}</td>
						<td data-label="Timestamp"><fmt:formatDate
								value="${log.createdAt}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
				</c:forEach>
				<c:if test="${empty auditLogs}">
					<tr>
						<td colspan="5" class="no-data">No audit logs found.</td>
					</tr>
				</c:if>
			</tbody>
		</table>
	</div>

	<!-- Pagination -->
	<div class="pagination">
		<c:set var="startEntry" value="0" />
		<c:set var="endEntry" value="0" />
		<c:if test="${currentPage != null && totalLogs != null}">
			<c:catch var="calcError">
				<c:set var="startEntry" value="${(currentPage - 1) * 10 + 1}" />
				<c:set var="endEntry"
					value="${currentPage * 10 <= totalLogs ? currentPage * 10 : totalLogs}" />
			</c:catch>
			<c:if test="${calcError != null}">
				<c:set var="error" value="Error calculating pagination values."
					scope="request" />
			</c:if>
		</c:if>

		<div class="pagination-info">
			<c:choose>
				<c:when test="${totalLogs > 0 && calcError == null}">
					Showing ${startEntry} to ${endEntry} of ${totalLogs} entries
				</c:when>
				<c:otherwise>
					Showing 0 to 0 of 0 entries
				</c:otherwise>
			</c:choose>
		</div>

		<div class="pagination-links">
			<c:if test="${currentPage > 1}">
				<a
					href="${pageContext.request.contextPath}/admin/audit-logs?page=${currentPage - 1}&userId=${userId}&action=${action}&startDate=${startDate}&endDate=${endDate}"
					class="btn-pagination">Previous</a>
			</c:if>
			<c:forEach begin="1" end="${totalPages}" var="i">
				<a
					href="${pageContext.request.contextPath}/admin/audit-logs?page=${i}&userId=${userId}&action=${action}&startDate=${startDate}&endDate=${endDate}"
					class="btn-pagination ${i == currentPage ? 'active' : ''}">${i}</a>
			</c:forEach>
			<c:if test="${currentPage < totalPages}">
				<a
					href="${pageContext.request.contextPath}/admin/audit-logs?page=${currentPage + 1}&userId=${userId}&action=${action}&startDate=${startDate}&endDate=${endDate}"
					class="btn-pagination">Next</a>
			</c:if>
		</div>
	</div>
</div>
