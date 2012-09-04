<%@page import="com.telenav.cserver.framework.html.util.HtmlFrameworkConstants"%>
<%@page import="com.telenav.browser.movie.Util"%>
<%@page import="com.telenav.cserver.framework.html.util.HtmlServiceLocator"%>
<%@page import="com.telenav.cserver.framework.html.util.HtmlClientInfoFactory"%>
<%
	HtmlClientInfo clientInfo = (HtmlClientInfo)request.getAttribute(HtmlFrameworkConstants.HTML_CLIENT_INFO);
	String manifestName = "manifest.manifest?clientInfo=" + HtmlClientInfoFactory.getInstance().buildClientInfoString(clientInfo);
	String dummyData = (String) request.getParameter("dummyData");
%>
<html manifest='<%=manifestName%>'>
<title>Movies </title>
<head>
<%@ include file="/html/jsp/Header.jsp"%>
<%
	String hostUrlWithIp = (String) request.getAttribute("Host_url");
	String  checkOutURL = HtmlServiceLocator.getInstance().getServiceUrl(hostUrlWithIp,"APPSTORE_CHECKOUT") + "?clientInfo=" + HtmlClientInfoFactory.getInstance().buildClientInfoString(clientInfoGB);
%>
<link type="text/css" href="<%=cssUrl + "buyTicket.css"%>" rel="stylesheet"/>
</head>
<body class="clsBuyTicketBody" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" >
<div id="AppStoreWrapper">
<div class="clsbuyticketPageBackground clsbuyticketPageBackgroundColor">
	<div id="titleBarDiv" class="clsTitleFrame clsTitleBg">
		<div class = "div_table">
			<div id="backButtonDiv" class="div_cell"	style="width: 19%; text-align: left" ></div>
			<div class="div_cell clsTitleContent">
				<html:msg key="buy.title" />
			</div>
			<div class="div_cell" style="width: 19%;"></div>
		</div>
	</div>
	<div id="infoBarDiv" class="bluetablestyle clsBuyTicketTopBk">
		<div id="movieInfoTable" style = "display: table; width:100%;">
			<div class="div_row">
				<div id="firstRow" class="div_cell clsFirstRow">
					<span id="date" class="clsbuyticket clsFontColorFRDate fs_middle"></span><br/>
					<span id="movieName" class="clsbuyticket fc_white fs_large"></span>
				</div>
			</div>
			<div class="div_row">
				<div class="div_cell bgImgW"></div>
			</div>
			<div class="div_row">
			    <div  id="secondRow" style="text-align: center;" class="div_cell clsSecondRow">
				  <div class = "div_table">
						<div class="div_cell" style="width:60%; height:100%;">
						  <span id="theaterName" class="clsbuyticket clsFontColorSRTN fs_large"></span><br/>
					      <span id="address" class="clsbuyticket clsFontColorSRAD fs_small"></span>
						</div>
						<div class="div_cell bgImgH"></div>
						<div class="div_cell">
							<span id="time" class="clsbuyticket fc_white fs_large"></span>
						</div>
				</div>
			  </div>
			</div>
	    </div>
	</div>
	<div id="blankBarDiv" class="ticketbarstyle ticketbarstyleBg"></div>
	<div align="center" style="width:100%;">
		<div id="bookingInfoDiv" class="clsBookingInfoDiv" style="display:none;" align="left"></div>
	</div>
	<div id="ticketQuantityGroup" style="display:none;" class="ticketquantitystyle fs_small"><html:msg key="buyticket.quantity"/>
		<div id="ticketQuantitySubDiv" style="margin-top:5px; width: 100%;">
			<div style="width: 100%; display: table; padding: 5% 0;">
				<div class="div_row">
					<div style="width:70%; text-align: left;" class="div_cell ticketquantitystyle fs_small"><html:msg key="buyticket.convenienceFee"/></div>
					<div style="width:30%; text-align: right;" id="convenienceFee" class="div_cell ticketquantitystyle fs_small"></div>
				</div>
	
				<div class="div_row">
					<div style="width:70%; text-align: left;" class="div_cell ticketquantitystyle fs_small"><html:msg key="buyticket.surcharge"/></div>
					<div style="width:30%; text-align: right;" id=surcharge class="div_cell ticketquantitystyle fs_small"></div>
				</div>
	
				<div class="div_row">
					<div style="width: 70%; text-align: left; color: black;" class="div_cell ticketquantitystyle fs_small"><html:msg key="buyticket.total" /></div>
					<div style="width: 30%; text-align: right; color: black;" id="totalMoney" class="div_cell ticketquantitystyle fs_small"></div>
				</div>
			</div>
		</div>
		<input type="hidden" id="convenienceFeeHidden" >
		<input type="hidden" id="surchargeHidden" >
		<input type="hidden" id="totalAmountHidden">
	</div>
	<div id="bottomPartDiv" class="clsBottomPart">
	<!-- confirm email box  -->
	<div id="confirmEmailDiv" class="clsConfirmEmailDiv">
		<div class="div_table" style="height:60%" >
			<div style="width: 16%; text-align: lfet" class="div_cell ticketquantitystyle fs_small"><b><html:msg key="email.label"/></b></div>
			<div style="width: 78%; text-align: lfet" class="div_cell" >
				<input id="email" type= 'text'  class="clsEmailInput fs_small" autocomplete="off"/>
			</div>
		</div>
		<div class="div_table" style="height:40%" >
			<div class="div_cell ticketquantitystyle fs_small" style="text-align: lfet; vertical-align: top">
			<p class="clsPrompt fs_verySmall"><html:msg key="buyticket.prompt"/></p>
			</div>
		</div>
	</div>
	<div id="bottom" class="clsBottomBarRelative">
		<div class="div_table">
			<div class="div_cell" style="text-align: center;">
				<input id="buyButton" type= 'button' value='<%=msg.get("buy.buy")%>' class='ticketBuyButton ticketBuyButtonDisabledBg' onclick="placeAnOrder()" disabled='disabled' />
			</div>
		</div>
	</div>
	</div>
	<!-- for popup when loading -->
	<div id="loadingPopup" class="loadingPopup clsLoadingPopupBk" >
	</div>
	<div id="backgroundPopup" class="popupBackground"></div>
	<div id="alertPopup" class="alertPopup"></div>
</div>
</div>

<input type="hidden" id="platform"  value="<%=clientInfoGB.getPlatform()%>">
<%@ include file="/html/jsp/Footer.jsp"%>
<script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/jqInterface.js"%>"></script>
<script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/movieCommon_compressed.js"%>"></script>
<script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/buyTicket_compressed.js"%>"></script>
<%-- <script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/movieCache.js"%>"></script> --%>
<%-- <script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/date.format.js"%>"></script> --%>
<%-- <script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/buyTicket.js"%>"></script> --%>
<script type="text/javascript">
var ticketList = new Array(),
	totalList = new Array(),
	GLOBAL_currentWidth,
	passThrough;

var dummyData = '<%=dummyData%>';
	
var I18NHelper = {
	"email.empty": "<%=msg.get("email.empty")%>",
	"email.invalid": "<%=msg.get("email.invalid")%>",
	"buyticket.noticket": "<%=msg.get("buyticket.noticket")%>",
	"checkout.purchaseFor": "<%=msg.get("checkout.purchaseFor")%>",
	"buyticket.noticket.schedule": "<%=msg.get("buyticket.noticket.schedule")%>",
	"buyticket.bookingFailed": "<%=msg.get("buyticket.bookingFailed")%>",
	"button.ok": "<%=msg.get("button.ok")%>"
	};
	
$(document).ready(function() {
	GLOBAL_currentWidth = document.documentElement.clientWidth;
	showMovieInfo();
	saveBuyTicketUsedHeight(0);
	changeBuyButtonCSS(false);
    fetchPriceInfo();
    
   if(CommonUtil.isIphone()){
		CommonUtil.addBackButtonForIphone("backButtonDiv","<%=msg.get("iphone.back")%>");
	}
});

$(window).resize(function(){
	if(PopupUtil.hasPopup()){
        PopupUtil.center();
	}

	if(isFlip()){
		changeBuyButtonCSS(false);
	}

});
</script>
<script type="text/javascript" charset="utf-8" src='<%=checkOutURL %>'></script>
</body>
</html>
