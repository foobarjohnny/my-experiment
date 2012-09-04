<%@page import="com.telenav.cserver.html.util.HtmlConstants"%>
<%@page import="com.telenav.cserver.html.util.HtmlAdjugglerUtil"%>
<%@page import="com.telenav.cserver.stat.AttributeID"%>
<%@page import="com.telenav.cserver.stat.EventTypes"%>
<%
	String manifestKey = (String)request.getAttribute(HtmlFrameworkConstants.HTML_MANIFEST_KEY);
	String manifestName = manifestKey + "poi.manifest";
%>
<html manifest="<%=manifestName%>">
<head>
<%@ include file="/html/jsp/Header.jsp"%>
<link type="text/css" href="<%=cssUrl + "adjuggler.css"%>" rel="stylesheet">
<script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/adjuggler.js"%>"></script>
</head>
<%
	HtmlClientInfo clientInfo = (HtmlClientInfo)request.getAttribute(HtmlFrameworkConstants.HTML_CLIENT_INFO);
	String sharedImageUrl = imageHost + (String)request.getAttribute(HtmlFrameworkConstants.HTML_SHARED_IMAGE_KEY);
	String ssoToken = request.getParameter("ssoToken");
	String paramJSON = request.getParameter("paramJSON");
	paramJSON = paramJSON.replaceAll("'", "\"");
	System.out.println("paramJSON......."+paramJSON);
	String hostUrlWithIp = (String) request.getAttribute("Host_url") + "/html/";
	String appCode = request.getParameter("appCode");
	
	String movieLocator = HtmlAdjugglerUtil.getSearviceLocator(
			(String) request.getAttribute("Host_url"), "MOVIE");
	
	String addonLocator = HtmlAdjugglerUtil.getSearviceLocator(
			(String) request.getAttribute("Host_url"), "ADDON");
%>
<script type="text/javascript">
		var GLOBAL_SSOTOKEN = "<%=ssoToken%>",
		GLOBAL_IMAGE_URL = "<%=sharedImageUrl%>",
		GLOBAL_ADJUGGLER_URL = "<%=HtmlConstants.KEY_ADJUGGLER_BROWSER%>",
		GLOBAL_ADJUGGLER_SEARCH = "<%=HtmlConstants.KEY_ADJUGGLER_SEARCH%>",
		GLOBAL_ADJUGGLER_WEATHER = "<%=HtmlConstants.KEY_ADJUGGLER_WEATHER%>",
		GLOBAL_ADJUGGLER_MOVIE = "<%=HtmlConstants.KEY_ADJUGGLER_MOVIE%>",
		GLOBAL_ADJUGGLER_POPUP = "<%=HtmlConstants.KEY_ADJUGGLER_POPUP%>",
		GLOBAL_PARAMJSON = '<%=paramJSON%>',
		GLOBAL_HOSTURL_IP = "<%=hostUrlWithIp%>",
		GLOBAL_APPCODE = "<%=appCode%>",
		GLOBAL_MOVIE_LOCATOR = "<%=movieLocator%>",
		GLOBAL_ADDON_LOCATOR = "<%=addonLocator%>",
		
		GLOBAL_AD_BANNER_VIEW = <%=EventTypes.AD_BANNER_VIEW%>,
		GLOBAL_AD_BANNER_CLICK = <%=EventTypes.AD_BANNER_CLICK%>,
		GLOBAL_PAGE_NAME_TML = "<%=AttributeID.PAGE_NAME_TML%>",
		GLOBAL_AD_BANNER_MSG = "<%=AttributeID.AD_BANNER_MSG%>",
		GLOBAL_ACCOUNT_TYPE = "<%=AttributeID.ACCOUNT_TYPE%>",
		GLOBAL_MESSAGE_NAME = "<%=AttributeID.MESSAGE_NAME%>";
		
		$(document).ready(function() {
			CommonUtil.fetchClientInfoFromUrl();
			document.addEventListener("deviceready", onDeviceReady, false);
			//onDeviceReady();
		});

		function onDeviceReady() {
			CheckAdjuggler();
	    }
	</script>

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" overflow="hidden">

<div id="mainDiv" class="adjugglerMain itemBackground" onclick="adjugglerClick()" ontouchstart="highlightDIV(this)" ontouchend="disHighlightDIV(this)">
</div>

<input type="hidden" id="programCode"  value="<%=clientInfo.getProgramCode()%>">
<input type="hidden" id="deviceCarrier"  value="<%=HtmlCommonUtil.getUrlString(clientInfo.getCarrier())%>">
<input type="hidden" id="platform"  value="<%=clientInfo.getPlatform()%>">
<input type="hidden" id="version"  value="<%=clientInfo.getVersion()%>">
<input type="hidden" id="productType"  value="<%=clientInfo.getProduct()%>">
<input type="hidden" id="device"  value="<%=clientInfo.getDevice()%>">
<input type="hidden" id="locale"  value="<%=clientInfo.getLocale()%>">
<input type="hidden" id="buildNumber"  value="<%=clientInfo.getBuildNo()%>">
<input type="hidden" id="width"  value="<%=clientInfo.getWidth()%>">
<input type="hidden" id="height"  value="<%=clientInfo.getHeight()%>">
</body>
</html>
