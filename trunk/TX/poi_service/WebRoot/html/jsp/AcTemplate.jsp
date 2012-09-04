<%@page import="com.telenav.cserver.framework.html.util.HtmlFrameworkConstants"%>
<%@page import="com.telenav.cserver.framework.html.util.HtmlClientInfoFactory"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.telenav.cserver.html.util.HtmlAcTemplateField"%>
<%@page import="com.telenav.cserver.html.util.HtmlAcTemplateConfig"%>
<%@page import="org.json.me.JSONArray"%>
<%@page import="org.json.me.JSONObject"%>
<%@page import="org.json.me.JSONException"%>
<%
	HtmlClientInfo clientInfo = (HtmlClientInfo) request.getAttribute(HtmlFrameworkConstants.HTML_CLIENT_INFO);

	List<List<HtmlAcTemplateField>> acTemplate = (List<List<HtmlAcTemplateField>>) request.getAttribute("acTemplate");
	JSONObject regionAutoSuggest = (JSONObject) request.getAttribute("regionAutoSuggest");
	String exitWebView = (String) request.getParameter("exitWebView");
	String alertSwitch = (String) request.getParameter("alertSwitch");
	String dummyData = (String) request.getParameter("dummyData");
	String fromPC = (String) request.getParameter("fromPC");
	String op = (String) request.getParameter("op");
	String manifestName = "manifest.manifest?module=actemplate&clientInfo=" + HtmlClientInfoFactory.getInstance().buildClientInfoString(clientInfo);
%>
<html manifest='<%=manifestName%>'>
<title>Ac Template</title>
<head>
<%@ include file="/html/jsp/Header.jsp"%>
<link type="text/css" href="<%=cssProgUrl + "actemplate_compressed.css"%>" rel="stylesheet">
<%-- <link type="text/css" href="<%=cssUrl + "actemplate.css"%>" rel="stylesheet"> --%>
<link type="text/css" href="<%=cssDeviceCommonPath + "actemplatebasic.css"%>" rel="stylesheet">
</head>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" overflow="hidden">
	<div id="container_div" class="clsPageBackground">
		<div class="clsAcTemplateDiv">
			<%-- FIELD_HOUSENUMBER_STREET --%>
			<%
				for (List<HtmlAcTemplateField> fields : acTemplate) {
			%>
			<div class="div_table_noheight">
				<div class="div_cell headBlank"></div>
				<%
					HtmlAcTemplateField field;
						for (int i = 0; i < fields.size(); i++) {
							field = (HtmlAcTemplateField) fields.get(i);
				%>
				<div class="<%="div_cell" + " " + field.getCss()%>">
					<input id="<%=field.getName()%>" name="addressInput" type="<%=field.getType()%>" maxLength="<%=field.getMaxLen()%>" minLen="<%=field.getMinLen()%>" autoSuggest="<%=field.getAutoSuggest()%>"
						class="clsInputBasic clsInput fs_large actInputBoxRadius" autocomplete="off" placeholder="<%=msg.get(field.getLabel())%>" triggerChar="<%=field.getTriggerChar()%>"
						autoSuggestFormat="<%=field.getAutoSuggestFormat()%>" optionsSize="<%=field.getOptionsSize()%>" stopField="<%=field.getStopField()%>">
				</div>
				<%
					if (i < fields.size() - 1) {
				%>
				<div class="div_cell intervalWidth"></div>
				<%
					}
				%>
				<%
					}
				%>
				<div class="div_cell tailBlank"></div>
			</div>
			<%
				}
			%>
		</div>
		<div class="div_table_noheight findAddressDivHeight">
			<div class="div_cell findAddressCell">
				<input type="button" class="clsButtonBasic clsButton fs_large clsBtnFontNormal actButtonRadius clsButtonBgNormal" ontouchstart="highlightBtnAll(this,'clsBtnFontNormal','clsBtnFontHL')"
					ontouchend="disHighlightBtnAll(this,'clsBtnFontHL','clsBtnFontNormal')" ontouchmove="disHighlightBtnAll(this,'clsBtnFontHL','clsBtnFontNormal')" value="<%=msg.get("common.button.Submit")%>" onclick="acTemplate.findAddress()">
			</div>
		</div>
		<div id="loadingPopup" class="loadingPopup"></div>
		<div id="backgroundPopup" class="popupBackground"></div>
		<div id="alertPopup" class="alertPopup"></div>
	</div>
	<%out.flush();%>
	<input type="hidden" id="programCode" value="<%=clientInfo.getProgramCode()%>">
	<input type="hidden" id="deviceCarrier" value="<%=HtmlCommonUtil.getUrlString(clientInfo.getCarrier())%>">
	<input type="hidden" id="platform" value="<%=clientInfo.getPlatform()%>">
	<input type="hidden" id="version" value="<%=clientInfo.getVersion()%>">
	<input type="hidden" id="productType" value="<%=clientInfo.getProduct()%>">
	<input type="hidden" id="device" value="<%=clientInfo.getDevice()%>">
	<input type="hidden" id="locale" value="<%=clientInfo.getLocale()%>">
	<input type="hidden" id="buildNumber" value="<%=clientInfo.getBuildNo()%>">
	<input type="hidden" id="width" value="<%=clientInfo.getWidth()%>">
	<input type="hidden" id="height" value="<%=clientInfo.getHeight()%>">
	<input type="hidden" id="region" value="<%=clientInfo.getRegion()%>">
	<%@ include file="/html/jsp/Footer.jsp"%>
	<script src="<%=contextPath + "js/acTemplate_Compressed.js"%>"></script>
<%-- 	<script src="<%=contextPath + "js/acTemplate.js"%>"></script> --%>
<%-- 	<script src="<%=contextPath + "js/autocomplete.js"%>"></script> --%>
<%-- 	<script src="<%=contextPath + "js/localdata.js"%>"></script> --%>
	<script type="text/javascript">
		var I18NHelper = {
		"common.button.OK": "<%=msg.get("common.button.OK")%>",
		"please.enter.address":"<%=msg.get("ac.template.please.enter.address")%>"
		};
		
		var alertSwitch = '<%=alertSwitch%>';
		var dummyData = '<%=dummyData%>';
		var regionSuggest = '<%=regionAutoSuggest.toString()%>';
		var Global_fromPC = '<%=fromPC%>';
		CommonUtil.debug("regionSuggest====" + regionSuggest);
		var regionSuggestObj = JSON.parse(regionSuggest);
		var op = '<%=op%>';

		$(window).resize(function() {
			if (PopupUtil.hasPopup()) {
				setTimeout(function() {
					PopupUtil.center();
				}, 50);
			}
		});

		$(document).ready(function() {
			acTemplate.clearAddressCacheList();
			//alert("	initPhoneGap();");
			initPhoneGap();
			// debug on CHROME
			if (Global_fromPC == "true") {
				onDeviceReady();
			}
		});

		function initPhoneGap() {
			document.addEventListener("deviceready", onDeviceReady, false);
		}

		function onDeviceReady() {
			if (op == "editWork") {
				acTemplate.retrieveHomeWork("workace");
			} else if (op == "editHome") {
				acTemplate.retrieveHomeWork("homeace");
			}
			acTemplate.retrieveAutoCompleteData();
		}
	</script>
</body>
</html>
