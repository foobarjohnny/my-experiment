<%@page import="com.telenav.cserver.framework.html.util.HtmlFrameworkConstants"%>
<%@page import="com.telenav.cserver.framework.html.util.HtmlClientInfoFactory"%>
<%@page import="com.telenav.cserver.framework.html.util.HtmlFeatureHelper" %>
<%
	HtmlClientInfo clientInfo = (HtmlClientInfo)request.getAttribute(HtmlFrameworkConstants.HTML_CLIENT_INFO);
	String manifestName = "manifest.manifest?clientInfo=" + HtmlClientInfoFactory.getInstance().buildClientInfoString(clientInfo);
	boolean backButton = HtmlCommonUtil.isIphone(clientInfo.getPlatform());
%>
<html manifest='<%=manifestName%>'>
<title>View Review</title>
<head>
<%@ include file="/html/jsp/Header.jsp"%>
<%-- <link type="text/css" href="<%=cssDeviceCommonPath + "viewreview.css"%>" rel="stylesheet"> --%>
<%-- <link type="text/css" href="<%=cssUrl + "viewreview.css"%>" rel="stylesheet"> --%>
<link type="text/css" href="<%=cssProgUrl + "viewreview_compressed.css"%>" rel="stylesheet">
<body>
<div id="container_div" class="clsBodyDivStyle">
<%-- review ower div --%>
	<div class="reviewOwer">
		<div class="div_table clsFixTable">
			<div class="div_cell reviewerPhoto align_center">
				<img class="clsImgDefaultPhoto" src="<%=imageCommonUrl + "default_photo_icon_unfocused.png"%>"/>
			</div>
			<div class="div_cell reviewerName align_left">
				<div id="reviewerName"  class="fs_large fc_gray fw_bold clsEllipsis"></div>
			</div>
			<div class="div_cell reviewerRating align_center">
				<span class="fs_small fc_gray"><html:msg key="viewreview.overallrating"/></span><br/>
				<span id="ratingImg"></span>
			</div>
		</div>
	 </div>
<%-- review options div --%>
	 <div id="reviewOptions" class="reviewOptions">
	 </div>
<%-- review content div --%>
	<div class="reviewContent">
		<div><img class="quotation_left" /></div>
		<div id="reviewComment" class="fs_middle fc_gray reviewComment"></div>
		<div class="align_right"><img class="quotation_right" /></div>
	</div>
<%-- back button div --%>
	<%if(backButton){%>
		<div class="backButton align_center">
			<input type="button" class="fs_veryLarge clsBackButton clsBtnFontNormal clsLargeRadius clsButtonBgNormal" 
						ontouchstart="highlightBtnAll(this,'clsBtnFontNormal','clsBtnFontHL')" 
						ontouchend="disHighlightBtnAll(this,'clsBtnFontHL','clsBtnFontNormal')" 
						ontouchmove="disHighlightBtnAll(this,'clsBtnFontHL','clsBtnFontNormal')" 
						value="<%=msg.get("common.button.Back")%>" onclick="SDKAPI.goBack(false)">
		</div>
	<%}%>
	</div>
	<%out.flush();%>	
<%@ include file="/html/jsp/Footer.jsp"%>
<script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/review_compressed.js"%>"></script>
<%-- <script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/poidetailcache.js"%>"></script> --%>
<%-- <script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/viewReview.js"%>"></script> --%>
<%-- <script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/rating.js"%>"></script> --%>
</head>
<script type="text/javascript">
	var I18NHelper = {
			"review.like": "<%=msg.get("review.like")%>",
			"review.dislike": "<%=msg.get("review.dislike")%>",
			"review.anonymous" : "<%=msg.get("review.anonymous")%>",
			"review.no.Comment": "<%=msg.get("review.no.Comment")%>"
	};

	$(document).ready(function() {
		 initReviewerData();
	});	
</script>	
</body>
</html>
