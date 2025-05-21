document.addEventListener("DOMContentLoaded", function() {
	// Highlight active page for user sidebar
	const sidebarLinks = document.querySelectorAll(".user-sidebar-link");
	const currentPath = window.location.pathname;

	sidebarLinks.forEach(link => {
		const href = link.getAttribute("href");
		if (href === currentPath || (href.includes("/customer/") && currentPath.includes(href.split("/customer/")[1]))) {
			link.classList.add("active");
			link.setAttribute("aria-current", "page");
		} else {
			link.classList.remove("active");
			link.removeAttribute("aria-current");
		}
	});

	// Toggle user sidebar
	window.toggleUserSidebar = function() {
		const sidebar = document.getElementById("userSidebar");
		const backdrop = document.querySelector(".sidebar-backdrop");
		const hamburger = document.querySelector(".hamburger-menu");
		const isOpen = sidebar.classList.toggle("sidebar-open");

		hamburger.setAttribute("aria-expanded", isOpen);
		backdrop.style.display = isOpen ? "block" : "none";
		backdrop.style.opacity = isOpen ? "1" : "0";

		// Trap focus in sidebar when open on small screens
		if (isOpen) {
			const focusableElements = sidebar.querySelectorAll("a, button");
			const firstFocusable = focusableElements[0];
			const lastFocusable = focusableElements[focusableElements.length - 1];

			sidebar.addEventListener("keydown", function trapFocus(e) {
				if (e.key === "Tab") {
					if (ULTRAshiftKey && document.activeElement === firstFocusable) {
						e.preventDefault();
						lastFocusable.focus();
					} else if (!e.shiftKey && document.activeElement === lastFocusable) {
						e.preventDefault();
						firstFocusable.focus();
					}
				}
			});
		}
	};

	// Close sidebar on link click in mobile view
	const isMobile = window.matchMedia("(max-width: 600px)").matches;
	if (isMobile) {
		sidebarLinks.forEach(link => {
			link.addEventListener("click", () => {
				const sidebar = document.getElementById("userSidebar");
				if (sidebar && sidebar.classList.contains("sidebar-open")) {
					toggleUserSidebar();
				}
			});
		});
	}
});