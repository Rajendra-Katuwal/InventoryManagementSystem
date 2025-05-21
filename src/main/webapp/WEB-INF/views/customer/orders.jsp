<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<div class="customer-orders-container">
	<div class="customer-orders-content">
		<h1>My Orders</h1>

		<!-- Orders List -->
		<div class="customer-orders-list">
			<c:choose>
				<c:when test="${empty orders}">
					<p class="customer-orders-no-data">You have no orders yet.</p>
				</c:when>
				<c:otherwise>
					<!-- Table View (Shown on Larger Screens) -->
					<div class="customer-orders-table-responsive">
						<table class="customer-orders-table">
							<thead>
								<tr>
									<th>Order ID</th>
									<th>Date</th>
									<th>Total</th>
									<th>Status</th>
									<th>Actions</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="order" items="${orders}">
									<tr data-order-id="${order.orderId}">
										<td data-label="Order ID">#${fn:escapeXml(order.orderId)}</td>
										<td data-label="Date"><fmt:formatDate
												value="${order.createdAt}" pattern="MMM dd, yyyy" /></td>
										<td data-label="Total"><fmt:formatNumber
												value="${order.totalAmount}" type="currency"
												currencySymbol="$" /></td>
										<td data-label="Status"><span
											class="customer-orders-status customer-orders-status-${fn:toLowerCase(order.orderStatus)}">
												${fn:escapeXml(order.orderStatus)} </span></td>
										<td data-label="Actions"
											class="customer-orders-action-buttons">
											<button class="customer-orders-btn customer-orders-btn-view"
												onclick="openModal('customer-order-modal-${order.orderId}')"
												aria-label="View order ${order.orderId}">
												<i class="fas fa-eye"></i> View
											</button> <c:if
												test="${order.orderStatus == 'pending' || order.orderStatus == 'paid'}">
												<form
													action="${pageContext.request.contextPath}/customer/orders"
													method="post" style="display: inline;">
													<input type="hidden" name="action" value="cancelOrder">
													<input type="hidden" name="orderId"
														value="${order.orderId}">
													<button type="submit"
														class="customer-orders-btn customer-orders-btn-cancel"
														onclick="return confirm('Are you sure you want to cancel this order?')"
														aria-label="Cancel order ${order.orderId}">
														<i class="fas fa-times"></i> Cancel
													</button>
												</form>
											</c:if>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>

					<!-- Card View (Shown on Mobile) -->
					<c:forEach var="order" items="${orders}">
						<div class="customer-orders-card" data-order-id="${order.orderId}">
							<div class="customer-orders-card-header">
								<div class="customer-orders-card-id">#${fn:escapeXml(order.orderId)}</div>
								<div class="customer-orders-card-date">
									<fmt:formatDate value="${order.createdAt}"
										pattern="MMM dd, yyyy" />
								</div>
							</div>
							<div class="customer-orders-card-body">
								<div class="customer-orders-card-total">
									<fmt:formatNumber value="${order.totalAmount}" type="currency"
										currencySymbol="$" />
								</div>
								<span
									class="customer-orders-status customer-orders-status-${fn:toLowerCase(order.orderStatus)}">
									${fn:escapeXml(order.orderStatus)} </span>
							</div>
							<div class="customer-orders-card-actions">
								<button class="customer-orders-btn customer-orders-btn-view"
									onclick="openModal('customer-order-modal-${order.orderId}')"
									aria-label="View order ${order.orderId}">
									<i class="fas fa-eye"></i> View
								</button>
								<c:if
									test="${order.orderStatus == 'pending' || order.orderStatus == 'paid'}">
									<form
										action="${pageContext.request.contextPath}/customer/orders"
										method="post">
										<input type="hidden" name="action" value="cancelOrder">
										<input type="hidden" name="orderId" value="${order.orderId}">
										<button type="submit"
											class="customer-orders-btn customer-orders-btn-cancel"
											onclick="return confirm('Are you sure you want to cancel this order?')"
											aria-label="Cancel order ${order.orderId}">
											<i class="fas fa-times"></i> Cancel
										</button>
									</form>
								</c:if>
							</div>
						</div>
					</c:forEach>

					<!-- Order Modals -->
					<c:forEach var="order" items="${orders}">
						<div id="customer-order-modal-${order.orderId}"
							class="customer-orders-modal" role="dialog"
							aria-labelledby="modal-title-${order.orderId}">
							<div class="customer-orders-modal-content">
								<span class="customer-orders-modal-close"
									onclick="closeModal('customer-order-modal-${order.orderId}')"
									aria-label="Close modal">&times;</span>
								<h2 id="modal-title-${order.orderId}">Order Details</h2>
								<div class="customer-orders-details">
									<div class="customer-orders-detail-item">
										<span class="customer-orders-detail-label">Order ID:</span> <span
											class="customer-orders-detail-value">#${fn:escapeXml(order.orderId)}</span>
									</div>
									<div class="customer-orders-detail-item">
										<span class="customer-orders-detail-label">User Email:</span>
										<span class="customer-orders-detail-value">${fn:escapeXml(userEmail)}</span>
									</div>
									<div class="customer-orders-detail-item">
										<span class="customer-orders-detail-label">Total
											Amount:</span> <span class="customer-orders-detail-value"> <fmt:formatNumber
												value="${order.totalAmount}" type="currency"
												currencySymbol="$" />
										</span>
									</div>
									<div class="customer-orders-detail-item">
										<span class="customer-orders-detail-label">Status:</span> <span
											class="customer-orders-detail-value"> <span
											class="customer-orders-status customer-orders-status-${fn:toLowerCase(order.orderStatus)}">
												${fn:escapeXml(order.orderStatus)} </span>
										</span>
									</div>
									<div class="customer-orders-detail-item">
										<span class="customer-orders-detail-label">Created At:</span>
										<span class="customer-orders-detail-value"> <fmt:formatDate
												value="${order.createdAt}" pattern="MMM dd, yyyy HH:mm" />
										</span>
									</div>
									<div class="customer-orders-detail-item">
										<span class="customer-orders-detail-label">Shipping
											Address:</span> <span class="customer-orders-detail-value">${fn:escapeXml(order.shippingAddress)}</span>
									</div>
									<div class="customer-orders-detail-item">
										<span class="customer-orders-detail-label">Order Items:</span>
										<div class="customer-orders-detail-value">
											<table class="customer-orders-items-table">
												<thead>
													<tr>
														<th>Product</th>
														<th>Quantity</th>
														<th>Unit Price</th>
														<th>Total</th>
													</tr>
												</thead>
												<tbody>
													<c:choose>
														<c:when test="${empty orderItemsMap[order.orderId]}">
															<tr>
																<td colspan="4">No items found</td>
															</tr>
														</c:when>
														<c:otherwise>
															<c:forEach var="item"
																items="${orderItemsMap[order.orderId]}">
																<tr>
																	<td>${fn:escapeXml(productNamesMap[item.orderItemId])}</td>
																	<td>${item.quantity}</td>
																	<td><fmt:formatNumber value="${item.unitPrice}"
																			type="currency" currencySymbol="$" /></td>
																	<td><fmt:formatNumber
																			value="${item.quantity * item.unitPrice}"
																			type="currency" currencySymbol="$" /></td>
																</tr>
															</c:forEach>
														</c:otherwise>
													</c:choose>
												</tbody>
											</table>
										</div>
									</div>
								</div>
								<div class="customer-orders-form-actions">
									<button class="customer-orders-btn customer-orders-btn-close"
										onclick="closeModal('customer-order-modal-${order.orderId}')"
										aria-label="Close modal">Close</button>
								</div>
							</div>
						</div>
					</c:forEach>

					<!-- Pagination -->
					<div class="customer-orders-pagination">
						<c:if test="${currentPage > 1}">
							<a href="?page=${currentPage - 1}"
								class="customer-orders-pagination-link"
								aria-label="Previous page">Previous</a>
						</c:if>
						<c:forEach var="i" begin="1" end="${totalPages}">
							<a href="?page=${i}"
								class="customer-orders-pagination-link ${i == currentPage ? 'active' : ''}"
								aria-label="Page ${i}"
								aria-current="${i == currentPage ? 'page' : ''}">${i}</a>
						</c:forEach>
						<c:if test="${currentPage < totalPages}">
							<a href="?page=${currentPage + 1}"
								class="customer-orders-pagination-link" aria-label="Next page">Next</a>
						</c:if>
					</div>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</div>

<!-- Modal JavaScript -->
<script>
    document.addEventListener('DOMContentLoaded', function() {
        // Modal functions
        window.openModal = function(modalId) {
            const modal = document.getElementById(modalId);
            if (modal) {
                document.body.style.overflow = 'hidden';
                modal.style.display = 'flex';
                requestAnimationFrame(() => {
                    modal.classList.add('show');
                });
            }
        };

        window.closeModal = function(modalId) {
            const modal = document.getElementById(modalId);
            if (modal) {
                modal.classList.remove('show');
                setTimeout(() => {
                    modal.style.display = 'none';
                    document.body.style.overflow = '';
                }, 300);
            }
        };

        // Event Listeners
        document.querySelectorAll('.customer-orders-modal').forEach(modal => {
            // Close on outside click
            modal.addEventListener('click', (e) => {
                if (e.target === modal) {
                    closeModal(modal.id);
                }
            });

            // Prevent modal content clicks from closing
            const content = modal.querySelector('.customer-orders-modal-content');
            if (content) {
                content.addEventListener('click', (e) => {
                    e.stopPropagation();
                });
            }

            // Close button functionality
            const closeBtn = modal.querySelector('.customer-orders-modal-close');
            if (closeBtn) {
                closeBtn.addEventListener('click', () => {
                    closeModal(modal.id);
                });
            }
        });

        // Close on Escape key
        document.addEventListener('keydown', (e) => {
            if (e.key === 'Escape') {
                const visibleModal = document.querySelector('.customer-orders-modal.show');
                if (visibleModal) {
                    closeModal(visibleModal.id);
                }
            }
        });

        // Button hover and focus effects
        document.querySelectorAll('.customer-orders-btn').forEach(btn => {
            btn.addEventListener('mouseenter', () => {
                btn.style.transform = 'scale(1.05)';
                btn.style.boxShadow = '0 2px 8px var(--shadow-light)';
            });
            
            btn.addEventListener('mouseleave', () => {
                btn.style.transform = '';
                btn.style.boxShadow = '';
            });

            btn.addEventListener('focus', () => {
                btn.style.boxShadow = '0 0 0 3px var(--focus-ring)';
            });

            btn.addEventListener('blur', () => {
                btn.style.boxShadow = '';
            });
        });
    });
</script>