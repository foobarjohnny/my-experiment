<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String ajaxResponse = (String) request.getAttribute("ajaxResponse");
	response.getWriter().print(ajaxResponse);
%>