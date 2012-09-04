<%@page import="com.telenav.cserver.framework.html.util.HtmlFrameworkConstants"%>
<%@page import="com.telenav.cserver.framework.html.util.HtmlMessageHelper"%>
<%@page import="com.telenav.cserver.framework.html.util.HtmlClientHelper"%>
<%@page import="com.telenav.cserver.framework.html.datatype.HtmlMessageWrap"%>
<%@page import="com.telenav.cserver.framework.html.datatype.HtmlClientInfo"%>
<%@page import="com.telenav.cserver.movie.html.util.HtmlConstants"%>

<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="html"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%
	response.setContentType(HtmlFrameworkConstants.RESPONSE_CONTENT_TYPE);
	//response.setHeader("Pragma","no-cache");
	//response.setDateHeader ("Expires", 0);

	String projectName = "/movie";
	String contextPath = projectName + "/html/";
	String hostUrl = contextPath;
			
	String imageCommonUrl = (String)request.getAttribute(HtmlFrameworkConstants.FULLPATH_IMAGE);
	String cssUrl = (String)request.getAttribute(HtmlFrameworkConstants.FULLPATH_CSSPROG);
	String msgKey = (String)request.getAttribute(HtmlFrameworkConstants.HTML_MESSAGE_KEY); 
	HtmlMessageWrap msg = HtmlMessageHelper.getInstance().getMessageWrap(msgKey);

%>
