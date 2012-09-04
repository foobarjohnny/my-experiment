<%@page import="com.telenav.cserver.framework.html.util.HtmlFrameworkConstants"%>
<%@page import="com.telenav.cserver.framework.html.util.HtmlClientInfoFactory"%>
<%@page import="com.telenav.cserver.framework.html.util.HtmlFeatureHelper"%>
<%
	HtmlClientInfo clientInfo = (HtmlClientInfo)request.getAttribute(HtmlFrameworkConstants.HTML_CLIENT_INFO);
	String manifestName = "manifest.manifest?module=weather&clientInfo=" + HtmlClientInfoFactory.getInstance().buildClientInfoString(clientInfo);
	boolean hasNightImage = HtmlFeatureHelper.getInstance().supportFeature(clientInfo, "WEATHER_NIGHT");
%>
<%if(clientInfo.getPlatform().equals("TNVIEW_LINUX")){%>
<html>
<%}else{%>
<html manifest='<%=manifestName%>'>
<%}%>
<title>Weather</title>
<head>
<%@ include file="/html/jsp/Header.jsp"%>
<%-- <link type="text/css" href="<%=cssDeviceCommonPath + "weather.css"%>" rel="stylesheet"> --%>
<link type="text/css" href="<%=cssProgUrl  + "weather_compressed.css"%>" rel="stylesheet">
</head>
<body class="weatherBg">
<div id="container_div" >
	<div id="titleBarDiv" class="clsWeatherTitleFrame weatherTitleBg" >
	  	<div class="div_table">
	  		<div class="div_cell"  id="backButtonDiv" ></div>
	  		<div class="div_cell clsTitleContent"  id="title"></div>
	  		<div class="div_cell align_right" id="changeLocation" >
	  			<img class="change_location_button_unfocused" id="changeLocationIcon" />
	  		</div>
	  	</div>
	</div>
	<div id="todayWeatherDiv" class="clsLeftPart">
		<div id="todayTop">
			<!-- 		float left -->
			<div id="tempDetail">
				<div id="temp" class="clsWeatherTemp fw_bold"></div>
				<div>
					<span class="fs_huge highLabel" id="highLabel"></span>
					<span class="clsFontColor_gray fs_huge" id="lowLabel"></span>
				</div>
				<div class="fw_bold fs_large clsStatus" id="status"></div>
			</div>
			<%--float right --%>
			<div id="todayImg" class="align_right">
				<div id="todayDate"  class="fs_large todayDate"></div>
				<div id="imageWeatherBig"></div>
			</div>
		</div>
		<%--float bottom in todayWeatherDiv --%>
		<div id="todayBottom" class="fs_large fw_bold">
			<div class="clsellipsis clsScoutGray">
				<span class="clsFeel"><html:msg key="weather.feelsLike"/></span><span id="feel"></span>
			</div>
			<div>
				<span class="clsFeel"><html:msg key="weather.humidity"/></span><span id="humidity" class="clsScoutGray"></span>
			</div>
			<div>
				<span class="clsFeel"><html:msg key="weather.wind"/></span><span id="wind" class="clsScoutGray"></span>
			</div>
		</div>
	</div>
	<div id="weeklyWeatherDiv" class="clsRightPart">
	</div>
</div>
<%out.flush();%>
<%-- for popup when loading --%>
<div id="loadingPopup"></div>
<div id="backgroundPopup" class="popupBackground"></div>
<div id="alertPopup" class="alertPopup"></div>	
<%@ include file="/html/jsp/Footer.jsp"%>
<%-- <script src="<%=contextPath + "js/weather.js"%>"></script> --%>
<script src="<%=contextPath + "js/weather_Compressed.js"%>"></script>
<%if(clientInfo.getPlatform().equals("TNVIEW_LINUX")){%>
<script src="<%=contextPath + "js/jquery.js"%>"></script>
<%}%>
<script>
var I18NHelper = {
		"common.button.OK": "<%=msg.get("common.button.OK")%>",
		"common.network.error.title": "<%=msg.get("common.network.error.title")%>",
		"common.network.error.text": "<%=msg.get("common.network.error.text")%>"
};
	var hasNightImage = <%=hasNightImage%>;
	
	$(document).ready(function() {
		CommonUtil.fetchClientInfoFromUrl();
		if(ClientInfo.platform == "TNVIEW_LINUX"){
			DeviceInfoNativeCallRequest();
		}
		document.addEventListener("deviceready", onDeviceReady, false);
		document.getElementById("temp").focus();
		Weather.init();
		
		if(CommonUtil.isIphone()){
			var exitWebview;
			if(Weather.getExitFlag()){
				exitWebview = "dashboard";
				CommonUtil.addBackButtonForIphone("backButtonDiv", '<%=msg.get("common.dashboard")%>', exitWebview);
			}else{
				CommonUtil.addBackButtonForIphone("backButtonDiv", '<%=msg.get("iphone.back")%>');
			}
		}
	});
	
	function onDeviceReady() {
		SDKAPI.setWindowMode(); 
		Weather.refreshData();
    }
	
	$(window).resize(function(){ 
		if(PopupUtil.hasPopup()){
			PopupUtil.center();
		}
	});
</script>
</body>
</html>
