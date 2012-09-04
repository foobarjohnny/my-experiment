<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp"%>
<%@page import="com.telenav.tnbrowser.util.DataHandler"%>
<%@page import="com.telenav.j2me.datatypes.*"%>
<%
	TxNode node = (TxNode) request.getAttribute("node");
	String errMsg = (String) request.getAttribute("errorMsg");
	Long errCode = (Long)request.getAttribute("errorCode");
	if (node == null){
		node = new TxNode();
		if (errCode != null) node.addValue(errCode);
		else node.addValue(-1);
		if (errMsg != null) {
			String errStr = msg.get(errMsg);
			node.addMsg(errStr);
		}
		else node.addMsg("N/A");
	}
	DataHandler.sendAJAXResponse(response, node);
%>