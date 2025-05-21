package controller.common;

import dao.ContactMessageDAO;
import dao.AuditLogDAO;
import model.ContactMessage;
import model.AuditLog;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/contact")
public class ContactServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ContactMessageDAO contactMessageDAO;
	private AuditLogDAO auditLogDAO;

	@Override
	public void init() throws ServletException {
		contactMessageDAO = new ContactMessageDAO();
		auditLogDAO = new AuditLogDAO();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Forward to contact.jsp for GET requests
		request.setAttribute("pageTitle", "Contact Us - Inventoria");
		request.setAttribute("contentPage", "/WEB-INF/views/common/contact.jsp");
		request.setAttribute("cssFile", "contact");
		request.getRequestDispatcher("/WEB-INF/views/templates/user-template.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute("user");

		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String subject = request.getParameter("subject");
		String message = request.getParameter("message");
		String redirectUrl = request.getParameter("redirectUrl");

		// Default redirect to /contact
		if (redirectUrl == null || redirectUrl.trim().isEmpty()) {
			redirectUrl = request.getContextPath() + "/contact";
		}

		// Validation
		if (name == null || name.trim().isEmpty()) {
			session.setAttribute("error", "Name is required.");
			response.sendRedirect(redirectUrl);
			return;
		}
		if (email == null || email.trim().isEmpty() || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
			session.setAttribute("error", "Valid email is required.");
			response.sendRedirect(redirectUrl);
			return;
		}
		if (subject == null || subject.trim().isEmpty()) {
			session.setAttribute("error", "Subject is required.");
			response.sendRedirect(redirectUrl);
			return;
		}
		if (message == null || message.trim().isEmpty()) {
			session.setAttribute("error", "Message is required.");
			response.sendRedirect(redirectUrl);
			return;
		}

		try {
			// Create contact message
			ContactMessage contactMessage = new ContactMessage();
			contactMessage.setUserId(user != null ? user.getUserId() : null);
			contactMessage.setName(name);
			contactMessage.setEmail(email);
			contactMessage.setSubject(subject);
			contactMessage.setMessage(message);

			// Save to database
			contactMessageDAO.saveContactMessage(contactMessage);

			// Log action for authenticated users
			if (user != null) {
				AuditLog log = new AuditLog();
				log.setUserId(user.getUserId());
				log.setAction("CONTACT_MESSAGE_SUBMITTED");
				log.setDetails("Contact message submitted: name=" + name + ", email=" + email + ", subject=" + subject);
				auditLogDAO.create(log);
			}

			session.setAttribute("success", "Your message has been sent successfully!");
			response.sendRedirect(redirectUrl);
		} catch (Exception e) {
			session.setAttribute("error", "Error sending message: " + e.getMessage());
			response.sendRedirect(redirectUrl);
		}
	}
}