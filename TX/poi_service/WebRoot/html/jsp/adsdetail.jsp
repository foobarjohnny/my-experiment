<%@page import="com.telenav.cserver.html.datatypes.AdsDetail"%>
<%@page import="com.telenav.cserver.framework.html.util.HtmlClientInfoFactory"%>
<%@page import="com.telenav.cserver.framework.html.util.HtmlFrameworkConstants"%>
<%
	String isDummy = (String)request.getParameter("isDummy");
	HtmlClientInfo clientInfo = (HtmlClientInfo)request.getAttribute(HtmlFrameworkConstants.HTML_CLIENT_INFO);
	String manifestName = "manifest.manifest?module=ads&clientInfo=" + HtmlClientInfoFactory.getInstance().buildClientInfoString(clientInfo);
%>
<html manifest='<%=manifestName%>'>
<head>
<title>Ads Detail</title>
<%@include file="/html/jsp/Header.jsp"%>
<%-- <link type="text/css" href="<%=cssUrl + "poidetail.css"%>" rel="stylesheet"> --%>
<%-- <link type="text/css" href="<%=cssDeviceCommonPath + "poidetail.css"%>" rel="stylesheet"> --%>
<link type="text/css" href="<%=cssProgUrl + "poidetail_compressed.css"%>" rel="stylesheet">
</head>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" overflow="hidden">
<div class="clsBodyDivStyle">
<%@ include file="/html/jsp/poitoppart.jsp"%>
	<%--ads main tab--%>
    <div class="tabBar tabBarBg"></div>
    <div class="blackLine"></div>
    <%--poi main tab--%>
	<div id="poimain" class="clsMainBody" >
		<div class="div_table">
			<div class="div_row">
				<div class="div_cell" style="width:100%;" id="mainTabFirstLine" class="clsMainTabFirstLine">
					<div class="div_table clsNoWrapTable">
						<div class="div_row">
							<div class="div_cell" style="width:2%;height:100%;"></div>
							<div class="div_cell" style="width:20%;" id="openFlag"></div>
							<div class="div_cell clsBusinessHour" align="right" id="businessHour"><div class="clsEllipsis" id="adSource" align="right"></div></div>
							<div class="div_cell" style="width:2%;"></div>
						</div>
					</div>
				</div>
			</div>
			<div class="div_row">
				<div class="div_cell clsMainInfoStyle" style="width:100%;vertical-align: top;">
					<div id="poidetailDesc"><div class="clsMainTabPadding fc_gray fw_bold fs_middle clsWordBreak" id="poidesc"></div></div>
				</div>
			</div>
			<div class="div_row">
				<div class="div_cell clsMainBottom" id="mainButtonsBar" style="display: none; padding:3% 0;">
					<div class="div_table clsFixTable align_center">
						<div class="div_row">
							<div class="div_cell" style="width:30%;"></div>
							<div class="div_cell" style="width:40%; height:100%;">
								<input type="button" class="clsBigButtonStyle fs_veryLarge4MainBtn fs_veryLarge clsBtnFontNormal clsLargeRadius clsButtonBgNormal" onClick="PoiCommonHelper.showMapOfClient()" 
								ontouchstart="highlightBtnAll(this,'clsBtnFontNormal','clsBtnFontHL')" value='<%=msg.get("poidetail.button.map")%>' ontouchend="disHighlightBtnAll(this,'clsBtnFontHL','clsBtnFontNormal')" ontouchmove="disHighlightBtnAll(this,'clsBtnFontHL','clsBtnFontNormal')"/>
							</div>
							<div class="div_cell" style="width:30%;"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
    <%--poi deals tab--%>
    <div class="clsDealsBodyDiv" id="poideals" style="display:none"></div>
    <%--poi memu tab--%>
    <div class="clsMenuBodyDiv fs_middle" id="poimenu" style="display:none"><div id="menuImageDiv"></div></div>
     <%-- for popup when loading --%>
    <div id="loadingPopup"></div>
    <div id="backgroundPopup" class="popupBackground"></div>    
    <div id="bgDiv" class="bgDiv"><div class="favoriteStyle" align="center"><html:msg key="poidetail.favadded"/></div></div>
    <div id="bgRemoveDiv" class="bgDiv"><div class="favoriteStyle" align="center"><html:msg key="poidetail.favremoved"/></div></div>
</div>
<%out.flush();%>
<input type="hidden" id="isDummy" value="<%=isDummy%>">
<input type="hidden" id="programCode"  value="<%=clientInfo.getProgramCode()%>">
<input type="hidden" id="platform"  value="<%=clientInfo.getPlatform()%>">
<input type="hidden" id="adId"  value="">
<input type="hidden" id="hidAdSource"  value="">
<input type="hidden" id="poiId"  value="">
<input type="hidden" id="poiType"  value="">
<%@ include file="/html/jsp/Footer.jsp"%>
<script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/adsdetail_Compressed.js"%>"></script>
<%-- <script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/adsdetail.js"%>"></script> --%>
<%-- <script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/adsdetailcache.js"%>"></script> --%>
<%-- <script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/poicommon.js"%>"></script> --%>
<%-- <script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/mislog_vbb.js"%>"></script> --%>
<script type="text/javascript">
	 var I18NHelper = {
			    "common.Unavailable": "<%=msg.get("common.Unavailable")%>",
		        "common.navigationErr": "<%=msg.get("common.navigationErr")%>",
                "common.showMapErr": "<%=msg.get("common.showMapErr")%>",
                "common.address.Unavailable" : "<%=msg.get("common.address.Unavailable")%>",    
                "common.phone.Unavailable" : "<%=msg.get("common.phone.Unavailable")%>",
                "poidetail.tab.main": "<%=msg.get("poidetail.tab.main")%>",
                "poidetail.tab.deals": "<%=msg.get("poidetail.tab.deals")%>",
                "poidetail.tab.menu": "<%=msg.get("poidetail.tab.menu")%>",
                "poidetail.menuUnavailable": "<%=msg.get("poidetail.menuUnavailable")%>",
                "poidetail.dataUnavailable": "<%=msg.get("poidetail.dataUnavailable")%>"
	};
	var GLOBAL_adsDetailDataReady = false,
	   GLOBAL_isdeviceReady = false,
	   GLOBAL_enterTime;

	$(document).ready(function() {
		initPhoneGap();
	    var currentDate = new Date();
	    GLOBAL_enterTime = currentDate.getTime();
		changePageCss();
		fetchAdsDetailData();
	});

	$(window).resize(function(){
		changePageCss();
	});
</script>
</body>
</html>
