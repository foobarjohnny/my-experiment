<%@page import="com.telenav.cserver.framework.html.util.HtmlFrameworkConstants"%>
<%@page import="com.telenav.cserver.framework.html.util.HtmlFeatureHelper"%>
<%@page import="com.telenav.cserver.html.util.HtmlConstants"%>
<%@page import="com.telenav.cserver.util.WebServiceConfigurator"%>
<%@page import="com.telenav.cserver.framework.html.util.HtmlServiceLocator"%>
<%@page import="com.telenav.cserver.framework.html.util.HtmlClientInfoFactory"%>
<%
	HtmlClientInfo clientInfo = (HtmlClientInfo)request.getAttribute(HtmlFrameworkConstants.HTML_CLIENT_INFO);
	String manifestName = "manifest.manifest?clientInfo=" + HtmlClientInfoFactory.getInstance().buildClientInfoString(clientInfo);

	String dummyData = (String)request.getAttribute("dummyData");
	String dummyTemplate = (String)request.getParameter("poi");
	//boolean restaurantFeature = HtmlFeatureHelper.getInstance().supportFeature(clientInfo, HtmlFrameworkConstants.FEATURE.FEATURE_RESTAURANT);
    //boolean hotelFeature = HtmlFeatureHelper.getInstance().supportFeature(clientInfo, HtmlFrameworkConstants.FEATURE.FEATURE_HOTEL);
    boolean movieFeature = HtmlFeatureHelper.getInstance().supportFeature(clientInfo, HtmlFrameworkConstants.FEATURE.FEATURE_MOVIE);
    boolean movieBuyFeature = HtmlFeatureHelper.getInstance().supportFeature(clientInfo, HtmlFrameworkConstants.FEATURE.FEATURE_MOVIE_BUY);
    boolean openGLMapFeature = HtmlFeatureHelper.getInstance().supportFeature(clientInfo, HtmlFrameworkConstants.FEATURE.OPENGLMAP);
    boolean gasFeature = !HtmlFeatureHelper.getInstance().disableFeature(clientInfo, HtmlFrameworkConstants.FEATURE.GASPRICE);
    //String hotelUrl = HtmlServiceLocator.getInstance().getServiceUrl((String) request.getAttribute("Host_url"),"HOTEL");
    //String openTableUrl = HtmlServiceLocator.getInstance().getServiceUrl((String) request.getAttribute("Host_url"),"OPENTABLE");
    
%>
<html manifest='<%=manifestName%>'>
<title>Poi Detail</title>
<head>
<%@ include file="/html/jsp/Header.jsp"%>
<%-- <link type="text/css" href="<%=cssUrl + "poidetail.css"%>" rel="stylesheet"> --%>
<%-- <link type="text/css" href="<%=cssDeviceCommonPath + "poidetail.css"%>" rel="stylesheet"> --%>
<link type="text/css" href="<%=cssProgUrl + "poidetail_compressed.css"%>" rel="stylesheet">
</head>
<body>
<div id="main" style="width:100%;height:100%;">
<%@ include file="/html/jsp/poitoppart.jsp"%>
	<%-- For Scout only, you don't need to add these classes in other PROG --%>
	<div class="tabBar tabBarBg"></div>
	<div id="blackLine" class="blackLine"></div>
	
	<%--poi main tab--%>
	<div id="poimain" class="clsMainBody" >
		<div class="table" style="height:100%;width:100%;">
			<!-- tr1 start -->
			<div class="tr">
				<div style="width:100%" id="mainTabFirstLine" class="td clsMainTabPadding clsMainTabFirstLineEmpty">
					<div style="height:100%;width:100%;" class="table clsNoWrapTable">
						<div class="tr">
							<div class="td" style="width:1%;height:100%;"></div>
							<div class="td" style="width:16%;" align="left" id="openFlag"></div>
							<div class="td clsBusinessHour" id="businessHour" onClick="switchBizHour()"><div class="clsEllipsis" id="adSource" align="right"></div></div>
							<div class="td" style="width:1%;"></div>
						</div>
					</div>
				</div>
			</div>
			<%-- tr1 end --%>
			<%-- tr2 start --%>
			<div class="tr" style="height:100%;">
				<div class="td clsMainInfoStyle" style="height:100%;width:100%;vertical-align:top" id="mainInfoTd">
					<div id="poidetailDesc">
						<div id="priceRange" style="display:none">
							<div class="table" style="float:left;width:68px;">
								<div class="tr">
									<div id="priceRange1" class="td clsDollarGrey" align="center">$</div><div class="td" style="width:2px;"></div>
									<div id="priceRange2" class="td clsDollarGrey" align="center">$</div><div class="td" style="width:2px;"></div>
									<div id="priceRange3" class="td clsDollarGrey" align="center">$</div><div class="td" style="width:2px;"></div>
									<div id="priceRange4" class="td clsDollarGrey" align="center">$</div><div class="td" style="width:2px;"></div>
								</div>
							</div>
						</div>  
						<div class="clsMainTabPadding fc_gray fw_bold fs_middle clsWordBreak" id="poidesc"></div> 
						<div  id="mapImageContainer" class="clsMapImageContainer" align="center" style="display:none;"><img id="mapImageDiv"></img></div> 
					</div>
					<div id="fullBusinessHours" class="clsMainTabPadding fc_gray fw_bold fs_middle" style="display:none;"></div>
				</div>
			</div>
	  		<%-- tr2 end --%>
	  		<%-- tr3 start --%>
			<div class="tr"><div id="mapBlank1" style="width:100%" class="td clsMapBlankBar"></div></div>
	  		<%-- tr3 end --%>
	  		<%-- tr4 start --%>
			<div class="tr">
				<div id="mainButtonsBar" class="td clsMainBottom" style="display: none;">
					<div class="table clsFixTable clsMainTabPadding" style="width:100%;height:100%;" align="center">
						<div class="tr">
							<div class="td clsBigButtonStyle clsBtnFontNormal clsLargeRadius clsButtonBgNormal" style="width:18%;height:100%;" onClick="PoiCommonHelper.showMapOfClient()" 
								ontouchstart="highlightBtnAll(this,'clsBtnFontNormal','clsBtnFontHL')" ontouchend="disHighlightBtnAll(this,'clsBtnFontHL','clsBtnFontNormal')" ontouchmove="disHighlightBtnAll(this,'clsBtnFontHL','clsBtnFontNormal')">
								<span class="fs_veryLarge4MainBtn fs_veryLarge" ><%=msg.get("poidetail.button.map")%></span>
							</div>
							<div class="td" style="width:2%;"></div>
							<div class="td clsBigButtonStyle clsBtnFontNormal clsLargeRadius clsButtonBgNormal" style="width:30%;" onClick="shareAddress()" 
								ontouchstart="highlightBtnAll(this,'clsBtnFontNormal','clsBtnFontHL')" value='<%=msg.get("poidetail.button.share")%>' ontouchend="disHighlightBtnAll(this,'clsBtnFontHL','clsBtnFontNormal')" ontouchmove="disHighlightBtnAll(this,'clsBtnFontHL','clsBtnFontNormal')">
								<span class="fs_veryLarge4MainBtn fs_veryLarge" ><%=msg.get("poidetail.button.share")%></span>
							</div>
							<div class="td" style="width:2%;"></div>
							<div class="td clsBigButtonStyle clsBtnFontNormal clsLargeRadius clsButtonBgNormal" style="width:48%;" onClick="searchNearBy()" 
								ontouchstart="highlightBtnAll(this,'clsBtnFontNormal','clsBtnFontHL')" ontouchend="disHighlightBtnAll(this,'clsBtnFontHL','clsBtnFontNormal')" ontouchmove="disHighlightBtnAll(this,'clsBtnFontHL','clsBtnFontNormal')">
								<span class="fs_veryLarge4MainBtn fs_veryLarge" ><%=msg.get("poidetail.button.searchnearby")%></span>
							</div>
						</div>	  
					</div>
				</div>
			</div>
			<%-- tr4 end --%>
	  		<%-- tr5 start --%>
			<div class="tr"><div id="mapBlank2" style="width:100%;" class="td clsMapBlankBar"></div></div>
	  		<%-- tr5 end --%>
		</div>
	</div>
	<%--poi review tab--%>
	<div class="clsReviewDiv"  align="center" id="poireview" style="display:none">
		 <div id="yelpAndTrip">
			<%-- Yelp reviews part --%>
		    <div id="yelp" class="yelpContainer" style="display:none">
		    	<div id="yelpRating" align="left">
		    		<div id="yelpStars">
		    			<div class="yelp_no_star" style="display:none"></div>
		    			<div class="yelp_one_star" style="display:none"></div>
		    			<div class="yelp_one_half_star" style="display:none"></div>
		    			<div class="yelp_two_star" style="display:none"></div>
		    			<div class="yelp_two_half_star" style="display:none"></div>
		    			<div class="yelp_three_star" style="display:none"></div>
		    			<div class="yelp_three_half_star" style="display:none"></div>
		    			<div class="yelp_four_star" style="display:none"></div>
		    			<div class="yelp_four_half_star" style="display:none"></div>
		    			<div class="yelp_five_star" style="display:none"></div>
		    		</div>
		    		<div id="yelpReviewCount"></div>
		    	</div>
		    	<div id="yelpBtnContainer">
		    		<div class="yelp_button"></div>
		    	</div>
		    </div>
		    <%-- Trip Advisor Ratings part --%>
		    <div id="trip" class="tripContainer" style="display:none">
		    	<div id="tripRating" align="left">
		    		<div id="tripCircles">
		    			<div class="trip_no_circle" style="display:none"></div>
		    			<div class="trip_half_circle" style="display:none"></div>
		    			<div class="trip_one_circle" style="display:none"></div>
		    			<div class="trip_one_half_circle" style="display:none"></div>
		    			<div class="trip_two_circle" style="display:none"></div>
		    			<div class="trip_two_half_circle" style="display:none"></div>
		    			<div class="trip_three_circle" style="display:none"></div>
		    			<div class="trip_three_half_circle" style="display:none"></div>
		    			<div class="trip_four_circle" style="display:none"></div>
		    			<div class="trip_four_half_circle" style="display:none"></div>
		    			<div class="trip_five_circle" style="display:none"></div>
		    		</div>
		    		<div id="tripReviewCount"></div>
		    	</div>
		    	<div id="tripBtnContainer">
		    		<div class="trip_button"></div>
		    	</div>
		    </div>
	    </div>
	    <%
	    String reviewTitle = msg.get("poidetail.tnreview");
	    if (scoutStyle)
	    {
	    	reviewTitle = msg.get("poidetail.scoutreview");
	    }
	    %>
		<div id="reviewTitle"><%=reviewTitle %></div>	  
		
	  <div align="center" class="clsWriteReviewDiv">
	    <div class="clsReviewTitleDiv">
			<div class="table clsFixTable" style="height:100%;width:100%;">
				<div class="tr">
					<div class="td" style="width:50%;height:100%;" align="left">
						<div align="center" id="reviewRatingIcon"></div>
						<div align="center" class="fs_middle fc_gray" id="reviewRatingNum"></div>
					</div>
					<div class="td" style="width:50%;height:100%;margin:10px 0px;padding:0.2em 0em 0.2em 0em;" align="right">
						<div class="table clsWriteReviewButton clsWriteReviewBtnColor clsLargeRadius" align="center" onClick="onClickWriteReview()" 
						    ontouchstart="highlightBtnAll(this,'clsWriteReviewBtnColor','clsWriteReviewBtnColorHL')" ontouchend="disHighlightBtnAll(this,'clsWriteReviewBtnColorHL','clsWriteReviewBtnColor')" ontouchmove="disHighlightBtnAll(this,'clsWriteReviewBtnColorHL','clsWriteReviewBtnColor')">
							<div class="tr"><div class="td fw_bold fs_middle"><%=msg.get("poidetail.button.writereview")%></div></div>	  
						</div>					    
					</div>
				</div>
			</div>
		</div>
		<div id="reviewOptionStatistic" class="clsReviewContentDiv"></div>
	  </div>
	  <div class="clsReviewBottomDiv" align="center" id="reviewerListDiv"></div>
	</div>	
	<%--poi deals tab--%>
	<div class="clsDealsBodyDiv" id="poideals" style="display:none">
	</div>
	<%--poi memu tab--%>
	<div class="clsMenuBodyDiv fs_middle" id="poimenu" style="display:none">
		<div id="menuImageDiv"></div>
	</div>
	 <%--poi Show tab--%>
	 <div class="clsShowtimeBodyDiv" id="poitheater" style="display:none">
		<div class="clsDatePicker" align="center">
			<div class="table" style="height:100%;width:100%">
				<div class="tr">
					<div class="td" style="height:100%;width:12%" align="right">
						<a id="datePrevious" href="javascript:void(0)"><img class="clsImgDatePickerImg date_previous_button_disabled" id="datePreviousIcon" ontouchstart="switchDatePreviousIcon(true)" ontouchend="switchDatePreviousIcon(false)" ontouchmove="switchDatePreviousIcon(false)"/></a>
					</div>
					<div style="height:100%" id="searchDate" class="td clsMovieDate fs_large fw_bold clsShowTimeTodayColor">
						<html:msg key="mSearch.today"/>
					</div>
					<div class="td" style="height:100%;width:12%" align="left">
						<a id="dateNext" href="javascript:onClickDateNext()"><img class="clsImgDatePickerImg date_next_button_unfocused" id="dateNextIcon" ontouchstart="switchDateNextIcon(true)" ontouchend="switchDateNextIcon(false)" ontouchmove="switchDateNextIcon(false)"/></a>
					</div>
				</div>
			</div>
		</div>
		<div class="clsDivBar"></div>
	  	<%-- movies of this theater will listed here --%>
		<div id="movieList" align="center"></div>
  	</div>
  	<%--poi extra tab--%>
	<div class=clsDealsBodyDiv id="poiextra" style="display:none"></div>
	<%--gap price tab--%>
	<div class="clsGasBodyDiv" id="poigas" style="display:none"></div>
</div>
<%out.flush();%>
<%-- for popup when loading --%>
<div id="loadingPopup"></div>
<div id="backgroundPopup" class="popupBackground"></div>	
<div id="bgDiv" class="bgDiv">
	<div class="favoriteStyle" align="center">
		<span class="fs_small"><html:msg key="poidetail.favadded"/></span>
	</div>
</div>
<div id="bgRemoveDiv" class="bgDiv">
	<div class="favoriteStyle" align="center">
		<html:msg key="poidetail.favremoved"/>
	</div>
</div>
<%-- for alert --%>
<div id="alertPopup" class="alertPopup"></div>
<div style="display: none">
<input type="text" id="poikey"  value="0">
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
<input type="hidden" id="dummyData" value="<%=dummyData%>">
<input type="hidden" id="dummyTemplate" value="<%=dummyTemplate%>">
<%@ include file="/html/jsp/Footer.jsp"%>
<script type="text/javascript" charset="utf-8" src="<%=sdkJsPath + "poiCommon_compressed.js"%>"></script>
<script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/poi_compressed.js"%>"></script>
<%-- <script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/poidetailcache.js"%>"></script> --%>
<%-- <script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/poidetail.js"%>"></script> --%>
<%-- <script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/mislog_poi.js"%>"></script> --%>
<%-- <script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/poicommon.js"%>"></script> --%>
<%-- <script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/poifetchdata.js"%>"></script> --%>
<%-- <script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/poidetailOld.js"%>"></script> --%>
<%-- <script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/LazyLoader.js"%>"></script> --%>
<%-- <script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/rating.js"%>"></script> --%>
<script type="text/javascript">
	var I18NHelper = {
			"mSearch.today": "<%=msg.get("mSearch.today")%>",
			"poidetail.open": "<%=msg.get("poidetail.open")%>",
			"poidetail.close": "<%=msg.get("poidetail.close")%>",
			"poidetail.reviews": "<%=msg.get("poidetail.reviews")%>",
			"poidetail.review": "<%=msg.get("poidetail.review")%>",
			"poidetail.tab.reviews": "<%=msg.get("poidetail.tab.reviews")%>",
			"poidetail.tab.main": "<%=msg.get("poidetail.tab.main")%>",
			"poidetail.tab.deals": "<%=msg.get("poidetail.tab.deals")%>",
			"poidetail.tab.menu": "<%=msg.get("poidetail.tab.menu")%>",
			"poidetail.tab.extra": "<%=msg.get("poidetail.tab.extra")%>",
			"poidetail.tab.showtime": "<%=msg.get("poidetail.tab.showtime")%>",
			"poidetail.tab.gasprices": "<%=msg.get("poidetail.tab.gasprices")%>",
			"poidetail.tab.reserve": "<%=msg.get("poidetail.tab.reserve")%>",
			"common.Unavailable": "<%=msg.get("common.Unavailable")%>",
			"poidetail.menuUnavailable": "<%=msg.get("poidetail.menuUnavailable")%>",
			"poidetail.dataUnavailable": "<%=msg.get("poidetail.dataUnavailable")%>",
			"common.showMapErr": "<%=msg.get("common.showMapErr")%>",
			"common.navigationErr": "<%=msg.get("common.navigationErr")%>",
			"common.button.OK": "<%=msg.get("common.button.OK")%>",
			"buy.msg": "<%=msg.get("buy.msg")%>",
			"[87]" : "<%=msg.get("poidetail.gasType.87")%>",
			"[89]" : "<%=msg.get("poidetail.gasType.89")%>",
			"[91]" : "<%=msg.get("poidetail.gasType.91")%>",
			"[D]" : "<%=msg.get("poidetail.gasType.D")%>",
			"REGULAR" : "<%=msg.get("poidetail.gasType.REGULAR")%>",
			"MID_GRADE" : "<%=msg.get("poidetail.gasType.MID_GRADE")%>",
			"PREMIUM" : "<%=msg.get("poidetail.gasType.PREMIUM")%>",
			"DIESEL_FUEL" : "<%=msg.get("poidetail.gasType.DIESEL_FUEL")%>",
			"review.user" : "<%=msg.get("review.user")%>",
			"review.anonymous" : "<%=msg.get("review.anonymous")%>",
			"review.no.Comment": "<%=msg.get("review.no.Comment")%>",
			"common.address.Unavailable" : "<%=msg.get("common.address.Unavailable")%>",			
			"common.phone.Unavailable" : "<%=msg.get("common.phone.Unavailable")%>"	
	};
		
	var GLOBAL_searchDate, 
		GLOBAL_todayDate,
	 	GLOBAL_dateMaxRange = <%=HtmlFrameworkConstants.MOVIE_SEARCHDATE_MAXRANGE%>,
		GLOBAL_tmid,
		GLOBAL_serverUrl = "<%=(String) request.getAttribute("Host_url")%>",
		GLOBAL_cssUrl = '<%=cssUrl %>',
		GLOBAL_contextPath = '<%=contextPath %>',
		GLOBAL_mapapiUrl = "<%=WebServiceConfigurator.getUrlOfStaticMap()%>",
		GLOBAL_mapapiKey = "<%=WebServiceConfigurator.getStaticMapKey()%>"
		;

	var FeatureHelper = {
		"MOVIE" : <%=movieFeature %>,
		"MOVIE_BUY" : <%=movieBuyFeature%>,
		"OPENGLMAP" : <%=openGLMapFeature%>,
		"GAS" :	<%=gasFeature%>
	};
				
	$(document).ready(function() {
		CommonUtil.fetchClientInfoFromUrl();
		GLOBAL_tmid = 0;
		initScreen();
	});
	
	$(window).resize(function(){ 
		Poi_Util_throttle(resizeScreen);
	});
</script>
</body>
</html>