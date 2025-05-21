<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%
request.setAttribute("pageTitle", "Home");
request.setAttribute("contentPage", "/WEB-INF/views/common/home.jsp");
request.setAttribute("cssFile", "home");
%>
<jsp:include page="/WEB-INF/views/templates/user-template.jsp" />