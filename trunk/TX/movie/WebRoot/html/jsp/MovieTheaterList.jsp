<%@page import="com.telenav.cserver.framework.html.util.HtmlFrameworkConstants"%>
<%@page import="com.telenav.cserver.framework.html.util.HtmlClientInfoFactory"%>
<%
	HtmlClientInfo clientInfo = (HtmlClientInfo)request.getAttribute(HtmlFrameworkConstants.HTML_CLIENT_INFO);
	String manifestName = "manifest.manifest?clientInfo=" + HtmlClientInfoFactory.getInstance().buildClientInfoString(clientInfo);
	String exitWebView = (String)request.getParameter("exitWebView");
%>
<html manifest='<%=manifestName%>'>
<title>Movie List</title>

<head>
<%@ include file="/html/jsp/Header.jsp"%>
<link type="text/css" href="<%=cssUrl + "MovieList.css"%>" rel="stylesheet">
</head>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" overflow="hidden">
<div id="container_div" class="clsPageBackground align_center">
	<div id="movieHeader" align="center" class="clsTabBackground" style="position: absolute;z-index: 2;">
		<div id="titleBarContainerDiv"></div>
		<div class="clsBigTabBackground clsBackground_tab clsBigTabBgBorder">
			<div class = "div_table">
				<div class="div_cell clsMTListTabBlank">
				</div>
			
			
				<div class="div_cell" style="width: 64%; height: 100%;">
				    <div id="showTheatersButton" class="div_table" style="width: 90%; margin-left: auto; margin-right: auto;" >
					    <div class="div_cell clsTabOutSideTable" style="width: 50%;  vertical-align: bottom; " onClick="onClickTheaterList()">
						    <div class = clsTabInSideTable" style="display: table; width: 100%;">
							    <div class="div_cell clsTabEdge clsTab clsTabLeftOnBk" > </div>
								<div class="div_cell clsButtonMiddle fs_large clsFontColor_focusedTab clsTab clsTabMiddleOnBk" ><html:msg key="theaters"/></div>
								<div class="div_cell clsTabEdge clsTab clsTabRightOnBk" > </div>
							</div>					
						</div>
						<div class="div_cell clsTabOutSideTable" style="width: 50%;  vertical-align: bottom;" onClick="onClickMovieList()">
						     <div class = clsTabInSideTable" style="display: table; width: 100%;">
						     	<div class="div_cell clsTabEdge clsTab clsTabLeftOffBk" > </div>
						     	<div class="div_cell clsButtonMiddle fs_large clsFontColor_unFocusedTab clsTab clsTabMiddleOffBk" ><html:msg key="movies"/></div>
								<div class="div_cell clsTabEdge clsTab clsTabRightOffBk" > </div>
							</div>
						</div>
					</div>
					<div id="showMoviesButton" class="div_table" style="width: 90%; margin-left: auto; margin-right: auto; display:none;" >
						<div class="div_cell clsTabOutSideTable" style="width: 50%;  vertical-align: bottom;" onClick="onClickTheaterList()">
							<div class = clsTabInSideTable" style="display: table; width: 100%;">
						     	<div class="div_cell clsTabEdge clsTab clsTabLeftOffBk" > </div>
						     	<div class="div_cell clsButtonMiddle fs_large clsFontColor_unFocusedTab clsTab clsTabMiddleOffBk" ><html:msg key="theaters"/></div>
								<div class="div_cell clsTabEdge clsTab clsTabRightOffBk" > </div>
							</div>
						</div>
						<div class="div_cell clsTabOutSideTable" style="width: 50%;  vertical-align: bottom;" onClick="onClickMovieList()">
							<div class = clsTabInSideTable" style="display: table; width: 100%;">
							    <div class="div_cell clsTabEdge clsTab clsTabLeftOnBk" > </div>
								<div class="div_cell clsButtonMiddle fs_large clsFontColor_focusedTab clsTab clsTabMiddleOnBk" ><html:msg key="movies"/></div>
								<div class="div_cell clsTabEdge clsTab clsTabRightOnBk" > </div>
							</div>		
						</div>
					</div>
				</div>
			 	<div class="div_cell clsChooseLocation" style="width: 18%; height: 100%; vertical-align: middle; text-align: right;" >
					<a href="javascript:SDKAPI.captureAddress()"  onTouchStart="switchLocationIcon(true)" onTouchEnd="switchLocationIcon(false)" onTouchMove="switchLocationIcon(false)"><img id="changeLocationIcon" class="clsChangeLocationIcon clsChangeLocationButtonUnfocused"/></a>
				</div>
			</div>
		</div>
		<div class="clsTabBottomBar"></div>
		<div class="clsDatePickerBg clsDatePickerBgBorder clsDatePickerBgColor clsDatePickerBgHeight" align="center">
			<div class = "div_table clsDatePickerTableBg">
				<div class="div_cell" style="width: 25%; text-align: left" >
					<a id="datePrevious" href="javascript:void(0)" onclick="cleanPaginationInfoAndListIfNeeded()"><img id="datePreviousIcon" class="clsDateIcon clsDatePreviousButtonDisabled" onTouchStart="switchDatePreviousIcon(true)" onTouchEnd="switchDatePreviousIcon(false)" onTouchMove="switchDatePreviousIcon(false)"/></a>
				</div>
				<div id="searchDate" class="div_cell fs_large clsSearchDate clsFontColor_searchDate" style="text-align: center">
					<html:msg key="mSearch.today"/>
				</div>
				<div class="div_cell" style="width: 25%; text-align: right">
					<a id="dateNext" href="javascript:onClickDateNext()" onclick="cleanPaginationInfoAndListIfNeeded()"><img id="dateNextIcon" class="clsDateIcon clsDateNextButtonUnfocused" onTouchStart="switchDateNextIcon(true)" onTouchEnd="switchDateNextIcon(false)" onTouchMove="switchDateNextIcon(false)"/></a>
				</div>
			</div>
		</div>
		<div class="clsListTopBar clsListTopBarHeight"></div>
	</div>


	<div id="movieOrTheaterListWrapper" style="width: 100%; position: absolute; z-index: 1; top: 82px; bottom: 0px;">
		<div id="movieOrTheaterListScroller" style="width: 100%; position: absolute; z-index: 1;">
			<div id="movieOrTheaterList" class="arrowlistmenu"></div>
			<div id="smallLoading" class="smallLoadingImage smallLoadingPopupBk" style="display: none;">
				<div id="smallLoadingInside" class="clsSmallLoadingPopupInsideBk"></div>
			</div>
		</div>
	</div>

	<!-- for popup when loading -->
	<div id="loadingPopup" class="loadingPopup clsLoadingPopupBk" >
	</div>
	<div id="alertPopup" class="alertPopup"></div>
	<div id="backgroundPopup" class="popupBackground"></div>	
</div>

<input type="hidden" id="programCode"  value="<%=clientInfoGB.getProgramCode()%>">
<input type="hidden" id="deviceCarrier"  value="<%=HtmlCommonUtil.getUrlString(clientInfoGB.getCarrier())%>">
<input type="hidden" id="platform"  value="<%=clientInfoGB.getPlatform()%>">
<input type="hidden" id="version"  value="<%=clientInfoGB.getVersion()%>">
<input type="hidden" id="productType"  value="<%=clientInfoGB.getProduct()%>">
<input type="hidden" id="device"  value="<%=clientInfoGB.getDevice()%>">
<input type="hidden" id="locale"  value="<%=clientInfoGB.getLocale()%>">
<input type="hidden" id="buildNumber"  value="<%=clientInfoGB.getBuildNo()%>">
<input type="hidden" id="width"  value="<%=clientInfoGB.getWidth()%>">
<input type="hidden" id="height"  value="<%=clientInfoGB.getHeight()%>">

<%@ include file="/html/jsp/Footer.jsp"%>
<script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/movieCommon_compressed.js"%>"></script>
<script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/movieList_compressed.js"%>"></script>
<script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/jquery.js"%>"></script>
<script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/ddaccordion.js"%>"></script>
<%-- <script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/iscroll-lite.js"%>"></script> --%>
<%-- <script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/movieCache.js"%>"></script> --%>
<%-- <script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/date.format.js"%>"></script> --%>
<%-- <script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/movieDatePicker.js"%>"></script> --%>
<%-- <script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/movieList.js"%>"></script> --%>
<script type="text/javascript">
	if(isBackFromSchedulePage()&& needToReloadMovieListPage()){
		removeNeedToReloadFlag();
		var a = "<%=clientInfoGB.getPlatform()%>";
		if("IPHONE" == a || "IPAD" == a)
		{
		/*if platform is IOS, don't reload page.  */
		}else{
		/*reload since there is a rendering page bug in Android webkit core when go back to this page.
		 if invoked some ajax calls in the page, when go back, we guess that the core will simulate the ajax calls during the rendering page and then mess up json date. */
			location.reload();
		}
	}
	
    var config = {
		headerclass: "expandable", 
		contentclass: "showfilmdiv", 
		revealtype: "click", 
		mouseoverdelay: 200, 
		collapseprev: true,  
		defaultexpanded: [], 
		onemustopen: false, 
		animatedefault: false, 
		persiststate: false, 
		toggleclass: ["closeheader", "openheader"], 
		togglehtml: ["prefix", "", ""], 
		animatespeed: "fast", 
		oninit:function(headers, expandedindices){ 
		},
		onopenclose:null
	};

	ddaccordion.init(config);
	

	var myScroll;

	
	function pullUpAction() {
		//setTimeout(function() { // <-- Simulate network congestion, remove setTimeout from production!
		appendMoreItems();
		myScroll.refresh(); // Remember to refresh when contents are loaded (ie: on ajax completion)
		//}, 1000); // <-- Simulate network congestion, remove setTimeout from production!
	}

	function loaded() {
		document.getElementById("movieOrTheaterListWrapper").style.top = document.getElementById("movieHeader").offsetHeight;
		appendMore = 0;
		myScroll = new iScroll('movieOrTheaterListWrapper', {
			useTransition : true,
			topOffset : 0,
			checkDOMChanges : true,
			onScrollMove : function() {
				if (this.y < (this.maxScrollY - 5) && !GLOBAL_isLoadingMore && GLOBAL_hasMore && !appendMore) {
					appendMore = 1;
					this.maxScrollY = this.maxScrollY;
				} else if (this.y > (this.maxScrollY + 5)) {
				}
			},
			onScrollEnd : function() {
				if (appendMore == 1) {
					appendMore = 0;
					pullUpAction(); // Execute custom function (ajax call?)
				}
			}
		});
	}

	function loadImage(index){
		if(isTheaterTabOn()){
			loadImageOfTheateList(index);
		}else{
			loadImageOfMovieList(index);
		}
	}
	
	var I18NHelper = {
			"mSearch.today": "<%=msg.get("mSearch.today")%>",
			"common.button.OK": "<%=msg.get("common.button.OK")%>",
			"common.network.error": "<%=msg.get("common.network.error")%>",
			"common.network.error.title": "<%=msg.get("common.network.error.title")%>",
			"common.network.error.text": "<%=msg.get("common.network.error.text")%>"
			};

	var GLOBAL_dateMaxRange = <%=HtmlFrameworkConstants.MOVIE_SEARCHDATE_MAXRANGE%>,
		GLOBAL_todayText = "<%=msg.get("mSearch.today")%>";
	
	document.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);
	
	$(document).ready(function() {
		setTimeout(loaded, 2000);
		
		CommonUtil.fetchClientInfoFromUrl();
		
		if(isBackFromSchedulePage()){
			CommonUtil.debug("Is back from schedule page. Try to recover and clean");
			recoverPage();
			cleanCacheAndFlag();
		}else{
			CommonUtil.debug("First Time to this page");
			document.addEventListener("deviceready", onDeviceReady, false);
			initData();
		}

		logManifest();
		
		if(CommonUtil.isIphone()){
			var exitWebView = '<%=exitWebView %>';
			CommonUtil.addBarAndBackButtonForIphone("titleBarContainerDiv", "<%=msg.get("iphone.back")%>", "<%=msg.get("movies")%>",exitWebView);
			$("#emptyBlock").attr("class","clsEmptyBlockForIphone");
		}
		
	});
	
	$(window).resize(function(){
		window.setTimeout(function(){
			if(!isTheaterTabOn() && GLOBAL_currentPageNo>0){
				trunkMovieDesc();
			}
	
			if(PopupUtil.hasPopup()){
		           PopupUtil.center();
			}

		}, 100);
	});
	
	
	//the page number for pagination
	var GLOBAL_currentPageNo = 0;
	var GLOBAL_isLoadingMore = false;
	var GLOBAL_hasMore = true;
	
	
	
</script>
</body>
</html>
