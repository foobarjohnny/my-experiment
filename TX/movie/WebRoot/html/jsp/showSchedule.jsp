<%@page import="com.telenav.cserver.framework.html.util.HtmlFrameworkConstants"%>
<%@page import="com.telenav.cserver.framework.html.util.HtmlClientInfoFactory"%>
<%
	HtmlClientInfo clientInfo = (HtmlClientInfo)request.getAttribute(HtmlFrameworkConstants.HTML_CLIENT_INFO);
	String manifestName = "manifest.manifest?clientInfo=" + HtmlClientInfoFactory.getInstance().buildClientInfoString(clientInfo);
	String exitWebView = (String)request.getParameter("exitWebView");
%>
<html manifest='<%=manifestName%>'>
<title>Movies </title>
<head>
<%@ include file="/html/jsp/Header.jsp"%>
<link type="text/css" href="<%=cssUrl + "showSchedule.css"%>" rel="stylesheet">
</head>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
	<div id="container_div" class="clsSheducePageBackground clsSheducePageBackgroundColor">
		<div id="titleBarDiv" class="clsTitleFrame clsTitleBg">
			<div class = "div_table">
				<div id="backButtonDiv" class="div_cell"	style="width: 19%; text-align: left" ></div>
				<div class="div_cell clsTitleContent">
					<html:msg key="buy.title" />
				</div>
				<div class="div_cell" style="width: 19%;"></div>
			</div>
		</div>
		<div class="bluebackground clsScheduleMovieBk">
			<div class = "div_table">
				<div id="movieData" class="div_cell clsScheduleTopContent clsMovieDateColor fs_large"></div>
			</div>
		</div>
		<div class="clsTabBottomBar"></div>
		<div class="clsScheduleTopListBg clsScheduleTopListBgComplement">
			<div class = "div_table">
				<div class="div_cell" style="width: 25%; text-align: left" >
					<a id="datePrevious" href="javascript:void(0)"><img id="datePreviousIcon" class="clsDateIcon clsDatePreviousButtonDisabled" onTouchStart="switchDatePreviousIcon(true)" onTouchEnd="switchDatePreviousIcon(false)" onTouchMove="switchDatePreviousIcon(false)"/></a>
				</div>
				<div id="searchDate" class="div_cell fs_hugest clsSearchDate clsFontColor_searchDate" style="text-align: center">
					<html:msg key="mSearch.today"/>
				</div>
				<div class="div_cell" style="width: 25%; text-align: right">
					<a id="dateNext" href="javascript:onClickDateNext()"><img id="dateNextIcon" class="clsDateIcon clsDateNextButtonUnfocused" onTouchStart="switchDateNextIcon(true)" onTouchEnd="switchDateNextIcon(false)" onTouchMove="switchDateNextIcon(false)"/></a>
				</div>
			</div>
		</div>
		<div class="deepbluebackground clsScheduleTheaterBk">
			<div class = "div_table">
				<div  id="theaterData" class="div_cell clsScheduleTopContent clsScheduleTopContentColor fs_large" style="align:center; valign:middle" >
				</div>
			</div>
		</div>
	<div class="barstyle barstyleBg"></div>
	<div class="upDiv upDivBgColor">
		<div id="scheduleList"></div>
	</div>
	
	<!-- for popup when loading -->
	<div id="loadingPopup" class="loadingPopup clsLoadingPopupBk" >
	</div>
	<div id="backgroundPopup" class="popupBackground"></div>
	<div id="alertPopup" class="alertPopup"></div>
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
<script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/jqInterface.js"%>"></script>
<script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/movieCommon_compressed.js"%>"></script>
<script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/showSchedule_compressed.js"%>"></script>
<%-- <script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/movieCache.js"%>"></script> --%>
<%-- <script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/date.format.js"%>"></script> --%>
<%-- <script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/movieDatePicker.js"%>"></script> --%>
<%-- <script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/showSchedule.js"%>"></script> --%>
<script type="text/javascript">
	var GLOBAL_searchDate, 
		GLOBAL_todayDate,
		GLOBAL_dateMaxRange = <%=HtmlConstants.MOVIE_SEARCHDATE_MAXRANGE%>;

	var I18NHelper = {
		"mSearch.today": "<%=msg.get("mSearch.today")%>",
		"movie.buyticketsfor": "<%=msg.get("movie.buyticketsfor")%>",
		"buyticket.noticket.movie": "<%=msg.get("buyticket.noticket.movie")%>",
		"button.ok": "<%=msg.get("button.ok")%>"
		};
		
	$(document).ready(function() {
		CommonUtil.fetchClientInfoFromUrl();
		initSchedule();
		
		var exitWebView = '<%=exitWebView %>';
		if(CommonUtil.isIphone()){
			CommonUtil.addBackButtonForIphone("backButtonDiv", "<%=msg.get("iphone.back")%>", exitWebView);
		}
	});

	$(window).resize(function(){ 
		if(PopupUtil.hasPopup()){
	           PopupUtil.center();
		}
	});
</script>
</body>
</html>
