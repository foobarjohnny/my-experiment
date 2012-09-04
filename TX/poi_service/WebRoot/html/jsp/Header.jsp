<%@page import="com.telenav.cserver.framework.html.util.HtmlFrameworkConstants"%>
<%@page import="com.telenav.cserver.framework.html.util.HtmlMessageHelper"%>
<%@page import="com.telenav.cserver.framework.html.util.HtmlClientHelper"%>
<%@page import="com.telenav.cserver.framework.html.util.HtmlCommonUtil"%>
<%@page import="com.telenav.cserver.framework.html.datatype.HtmlMessageWrap"%>
<%@page import="com.telenav.cserver.framework.html.datatype.HtmlClientInfo"%>
<%@page import="com.telenav.cserver.framework.html.util.HtmlFeatureHelper"%>
<meta http-equiv="Content-Type" content="<%=HtmlFrameworkConstants.META_CONTENT_TYPE%>">
<meta name="viewport" content="<%=HtmlFrameworkConstants.VIEW_POINT%>"/>
<meta name="format-detection" content="telephone=no" />
<meta name="format-detection" content="address=no" />
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="html"%>
<%@ page language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%
	response.setContentType(HtmlFrameworkConstants.RESPONSE_CONTENT_TYPE);
	String projectName = "/poi_service";
	String contextPath = projectName + "/html/";
	String hostUrl = contextPath;
	//
	String msgKey = (String)request.getAttribute(HtmlFrameworkConstants.HTML_MESSAGE_KEY); 
	HtmlMessageWrap msg = HtmlMessageHelper.getInstance().getMessageWrap(msgKey);
	//
	String cssUrl = (String)request.getAttribute(HtmlFrameworkConstants.FULLPATH_CSS);
	String cssProgUrl = (String)request.getAttribute(HtmlFrameworkConstants.FULLPATH_CSSPROG);
	String imageCommonUrl = (String)request.getAttribute(HtmlFrameworkConstants.FULLPATH_IMAGE);
	String sharedImageUrl = (String)request.getAttribute(HtmlFrameworkConstants.FULLPATH_SHARED_IMAGE);
	String commonJsUrl = (String)request.getAttribute(HtmlFrameworkConstants.FULLPATH_JS_COMMON);
	//
	HtmlClientInfo clientInfoGB = (HtmlClientInfo)request.getAttribute(HtmlFrameworkConstants.HTML_CLIENT_INFO);
	String sdkJsPath = contextPath + "js/"  + clientInfoGB.getPlatform() + "/";
	String cssDeviceCommonPath = (String)request.getAttribute(HtmlFrameworkConstants.FULLPATH_CSS_DEVCIECOMMON_KEY);
	String cssProgDeviceCommonPath = (String)request.getAttribute(HtmlFrameworkConstants.FULLPATH_SHARED_CSS_DEVCIECOMMON_KEY);
	boolean scoutStyle = HtmlFeatureHelper.getInstance().supportFeature(clientInfoGB, HtmlFrameworkConstants.FEATURE.SCOUTSTYLE);
%>
<%-- <link type="text/css" href="<%=cssUrl + "style.css"%>" rel="stylesheet"> --%>
<%-- <link type="text/css" href="<%=cssDeviceCommonPath + "style.css"%>" rel="stylesheet"> --%>
<%-- <link type="text/css" href="<%=cssProgDeviceCommonPath + "prog.css"%>" rel="stylesheet"> --%>
<%-- <link type="text/css" href="<%=cssProgUrl + "prog.css"%>" rel="stylesheet"> --%>
<link type="text/css" href="<%=cssProgUrl + "common_compressed.css"%>" rel="stylesheet">
<%out.flush();%>
