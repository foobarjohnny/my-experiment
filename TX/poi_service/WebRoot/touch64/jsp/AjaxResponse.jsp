<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.telenav.tnbrowser.util.DataHandler"%>
<%@page import="com.telenav.j2me.datatypes.*"%>
<%
	TxNode node = (TxNode) request.getAttribute("node");
	DataHandler.sendAJAXResponse(response, node);
%>