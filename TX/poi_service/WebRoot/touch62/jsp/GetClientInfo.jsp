<%@page import="com.telenav.tnbrowser.util.DataHandler"%>
<%
    DataHandler handler = (DataHandler)request.getAttribute("CLIENT_INFO");
    String carrier = "TN";
    if (null != handler){
        carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
    }
    String product = (String)request.getParameter("producttype");
%>