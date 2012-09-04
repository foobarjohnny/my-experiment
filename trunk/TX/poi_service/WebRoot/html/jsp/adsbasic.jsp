<%@page import="com.telenav.cserver.html.datatypes.AdsDetail"%>
<%@page import="com.telenav.cserver.framework.html.util.HtmlClientInfoFactory"%>
<%@page import="com.telenav.cserver.framework.html.util.HtmlFeatureHelper" %>
<%

	AdsDetail ads = (AdsDetail)request.getAttribute("adsdetail");
	String descrption = ads.getTagline();
	String image = ads.getLogoImage();
	HtmlClientInfo clientInfo = (HtmlClientInfo)request.getAttribute(HtmlFrameworkConstants.HTML_CLIENT_INFO);
	String isDummy = (String)request.getParameter("isDummy");
	String manifestName = "manifest.manifest?module=ads&clientInfo=" + HtmlClientInfoFactory.getInstance().buildClientInfoString(clientInfo);
	String audioKey = (String)request.getAttribute(HtmlFrameworkConstants.FULLPATH_AUDIO);
	boolean scoutstyle = HtmlFeatureHelper.getInstance().supportFeature(clientInfo,"SCOUTSTYLE");
%>
<html>
<head>
	<title>Ads Basic</title>
<%@ include file="/html/jsp/Header.jsp"%>
<%-- <link type="text/css" href="<%=cssUrl + "adsbasic.css"%>" rel="stylesheet"> --%>
<%-- <link type="text/css" href="<%=cssDeviceCommonPath + "adsbasic.css"%>" rel="stylesheet"> --%>
<link type="text/css" href="<%=cssProgUrl + "adsbasic_compressed.css"%>" rel="stylesheet">
</head>
<body class="vbbBody">
<%-- <audio id="media" autoplay="autoplay" src="<%=audioKey%>vbb_friendly_beep.wav"></audio> --%>
	<%-- =======================0. top Right image div======================== --%>
<div id="topRightDiv" class="topRightDiv div_table">
	<div class="div_row">
		<div class="div_cell topRightFirstCell">&nbsp;</div>
		<div class="div_cell" onClick="onClickBackGround()">
			<img id="topRightImage" class="openPoi713"/>
		</div>
	</div>
</div>
<div id="container_div" class="clsTotal" onClick="onClickBackGround()">
	<%-- =======================1. BrandName======================== --%>
	<div class="div_table clsTopPart" id="topTitleDiv">
		<div class="div_row">
			<div class="div_cell adWord fw_bold fc_gray firstCell" align="center">AD</div>
			<div class="div_cell logImageDiv" align="center"><img class="clsLogoImage" id='logImage' src=""/></div>
			<div class="div_cell fs_large fw_bold clsFontColor_blue clsOneLine"><%=ads.getBrandName()%></div>
			<div class="div_cell fs_large fw_bold fc_gray" id="distance" align="right" style="width:23%;"></div>
			<div class="div_cell" align="center" style="width:10%;" id="oldImgDiv"><img id="openCloseImage" class="openPoi"/></div>
		</div>
	</div>
	<%-- =======================2. description========================= --%>
	<div class="div_table descDiv clsFixTable">
		<div class="div_row">
			<div class="div_cell firstCell" align="center"></div>
			<div class="div_cell"><div class="clsAdsDescription fs_verySmall"><%=descrption%></div></div>
		</div>
	</div>
	<%-- =======================3. button========================= --%>
	<div class="div_table buttonDiv">
		<div class="div_row">
			<div class="div_cell firstCell" align="center"></div>
			<div class="div_cell" style="width:70%;">
				<div id="navButton" style="table-layout:fixed;" class="div_table clsNavButtonStyle clsNavFontColor clsLargeRadius clsNavButtonBgNormal"  
					onClick="onClickDrive()" ontouchstart="highLightNavBtn(this)" ontouchend="dishighLightNavBtn(this)" ontouchmove="dishighLightNavBtn(this)">
					<div class="div_row">
						<div class="div_cell clsBtnPicDiv" width="17%" align="left" valign="middle"><img id="navImg" class="clsImgDriveTo poi_details_driveto_icon_unfocused"/></div>
						<div class="div_cell clsNavTextDiv adbNavTextDiv fw_bold clsOneLine" align="left" valign="middle" id="address"><%=ads.getAddressDisplay()%></div>
					</div>
				</div>
			</div>
			<div class="div_cell" style="width:3%;">&nbsp;</div>
			<div class="div_cell">
				<%if(scoutstyle){%>
					<div id="phoneButton" ontouchstart="highLightPhone(this)" ontouchend="dishighLightPhone(this)" ontouchmove="dishighLightPhone(this)" class="table clsBigButtonStyle clsPhoneNoColor" onclick="onClickMore()">
						<div class="tr">
							<div class="td clsBtnPicDiv"><img id="phonePic" class="poi_details_call_icon_unfocused"></div>
							<div id="phoneContainer" align="center" class="td fs_middle fw_bold"><html:msg key="ads.more"/></div>
						</div>
					</div>
				<%}else{ %>
					<div class="div_table fs_verySmall fc_black fw_bold clsLargeRadius clsButtonBgNormal" align="center"  onClick="onClickMore()" 
			   	   		 ontouchstart="highlightBtnAll(this,'fc_black','fc_white')" ontouchend="disHighlightBtnAll(this,'fc_white','fc_black')" ontouchmove="disHighlightBtnAll(this,'fc_white','fc_black')">
			   	   		 <div class="div_row">
			   	   			 <div class="div_cell" align="center"><html:msg key="ads.more"/></div>
			   	   		 </div>
					</div>
				<% }%>
			</div>
			<div class="div_cell" style="width:1.4%;">&nbsp;</div>
		</div>
	</div>
	<%-- =======================4. blank========================= --%>
	<div class="blankDiv"></div>
</div>
<%out.flush();%>
<input type="hidden" id="isDummy" value="<%=isDummy%>">
<input type="hidden" id="adId"  value="<%=ads.getAdId()%>">
<input type="hidden" id="hidAdSource"  value="<%=ads.getAdSource()%>">
<input type="hidden" id="poiId"  value="<%=ads.getPoiId()%>">
<input type="hidden" id="poiType"  value="<%=ads.getPoiType()%>">

<input type="hidden" id="lat"  value=<%=ads.getAddress().lat%>>
<input type="hidden" id="lon"  value=<%=ads.getAddress().lon%>>
<input type="hidden" id="label"  value="<%=ads.getAddress().label%>">
<input type="hidden" id="firstLine"  value="<%=ads.getAddress().firstLine%>">
<input type="hidden" id="city"  value="<%=ads.getAddress().city%>">
<input type="hidden" id="state"  value="<%=ads.getAddress().state%>">
<input type="hidden" id="zip"  value="<%=ads.getAddress().zip%>">
<input type="hidden" id="country"  value="<%=ads.getAddress().country%>">

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
<%@ include file="/html/jsp/Footer.jsp"%>
<script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/adsbasic_Compressed.js"%>"></script>
<%-- <script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/adsbasic.js"%>"></script> --%>
<%-- <script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/adsdetailcache.js"%>"></script> --%>
<%-- <script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/mislog_vbb.js"%>"></script> --%>
<script type="text/javascript">
    var I18NHelper = {
        "ads.displayDetailErr": "<%=msg.get("ads.displayDetailErr")%>",
        "common.navigationErr": "<%=msg.get("common.navigationErr")%>"
        };
       
	var GLOBAL_serverUrl = "<%=(String) request.getAttribute("Host_url")%>",
	GLOBAL_enterTime,
	topTitleDivHeight = "<%=(String)request.getParameter("initHeight")%>",
	containerDivHeight = "<%=(String)request.getParameter("contentHeight")%>";
	
	$(document).ready(function() {
		initPhoneGap();
		initLayout();
		CommonUtil.fetchClientInfoFromUrl();
		var currentDate = new Date();
		GLOBAL_enterTime = currentDate.getTime();
		    
		var imageString = "<%=ads.getLogoName() %>";
		if(imageString==null||imageString== ""  ){
			displayEmptyLogo();
		}else{
			$("#logImage").attr("src",imageString);
		}
		
		PoiCacheHelper.setAdsIdCache($("#adId").val());
		if(<%=isDummy%>){
			$("distance").html("1.25 mi");
		}
	});
</script>

</body>
</html>
