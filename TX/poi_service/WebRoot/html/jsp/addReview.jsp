<%@page import="com.telenav.cserver.framework.html.util.HtmlFrameworkConstants"%>
<%@page import="com.telenav.cserver.framework.html.util.HtmlClientInfoFactory"%>
<%@page import="java.util.List"%>
<%
	HtmlClientInfo clientInfo = (HtmlClientInfo)request.getAttribute(HtmlFrameworkConstants.HTML_CLIENT_INFO);
	String manifestName = "manifest.manifest?clientInfo=" + HtmlClientInfoFactory.getInstance().buildClientInfoString(clientInfo);
%>
<html manifest='<%=manifestName%>'>
<title>Reviews</title>
<head>
<%@ include file="/html/jsp/Header.jsp"%>
<link type="text/css" href="<%=cssProgUrl + "review_compressed.css"%>" rel="stylesheet">
<%-- <link type="text/css" href="<%=cssUrl + "review.css"%>" rel="stylesheet"> --%>
<%-- <link type="text/css" href="<%=cssDeviceCommonPath + "review.css"%>" rel="stylesheet"> --%>
</head>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" bottommargin="0" rightmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<div id="container_div" class="clsPageBackground">
	<div class="addTopDIV addTopDivBg"   id="addTopDIVBg">
		<div class="div_table clsFixTable">
			<div class="div_row">
				<div class="div_cell fs_veryLarge fw_bold clsFontColor_blue clsEllipsis" id="name"></div>
			</div>
			<div class="div_row">
				<div class="div_cell fs_middle fc_textAganistBG" id="address"></div>
			</div>
			<div class="div_row">
				<div class="div_cell fs_middle fc_textAganistBG clsEllipsis">
					<html:msg key="addreview.tips"/>
					<html:msg key="addreview.required"/>
					<input type="hidden" id="totalRating"  value="0">
				</div>
			</div>
			<div class="div_row">
				<div class="div_cell" style="width:90%;" id="stars">
					<div style="display: table; width:100%">
						<div class="div_row">
							<div class="div_cell" style="text-align: center"><a onclick='lightStar(1);'><img id="lightStar1" class="clsImgHugeStar vacancy_huge_star_icon_unfocused"/> </a></div>
							<div class="div_cell" style="text-align: center"><a onclick='lightStar(2);'><img id="lightStar2" class="clsImgHugeStar vacancy_huge_star_icon_unfocused"/> </a></div>
							<div class="div_cell" style="text-align: center"><a onclick='lightStar(3);'><img id="lightStar3" class="clsImgHugeStar vacancy_huge_star_icon_unfocused"/> </a></div>
							<div class="div_cell" style="text-align: center"><a onclick='lightStar(4);'><img id="lightStar4" class="clsImgHugeStar vacancy_huge_star_icon_unfocused"/> </a></div>
							<div class="div_cell" style="text-align: center"><a onclick='lightStar(5);'><img id="lightStar5" class="clsImgHugeStar vacancy_huge_star_icon_unfocused"/> </a></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="reviewMiddleDiv">
		<div style="display: table; width:100%">
			<div class="div_row">
				<div class="div_cell" >
					<div style="display: table; width:100%; border-spacing: 2px; " id="reviewOptionList"></div>
				</div>
			</div>
			<div class="div_row" style="height: 5px;">
				<div class="div_cell" >
				</div>
			</div>
			<div id="userNameInputDiv">
				<input type="text" id="userNameInput" class="fs_small reviewUserNameInputText fc_black fst_italic" autocomplete="off" autocorrect="off" spellcheck="false" maxlength="50" placeholder='<%=msg.get("addreview.username.placeholder")%>' onclick="onclickUserNameInput(this)" onblur="onBlurUserNameInput(this)">
			</div>
			<div class="div_row">
				<div class="div_cell" >
					<textarea class="reviewCommentsTextArea fs_middle" id="comment" autocomplete="off" autocorrect="off" spellcheck="false" maxlength="400" placeholder='<%=msg.get("addreview.comments")%>'></textarea>
				</div>
			</div>
			<div class="div_row">
				<div class="div_cell clsButtonsTd" style="text-align: center; padding: 3% 0px;">
					<div style="width:100%">
						<div class="addReviewButton addReviewSubmit">
							<input type="button" class="clsButton clsBtnFontNormal clsLargeRadius clsButtonBgNormal"
								ontouchstart="highlightBtnAll(this,'clsBtnFontNormal','clsBtnFontHL')" ontouchend="disHighlightBtnAll(this,'clsBtnFontHL','clsBtnFontNormal')" 
								ontouchmove="disHighlightBtnAll(this,'clsBtnFontHL','clsBtnFontNormal')" value="<%=msg.get("common.button.Submit")%>" onClick="addReview()" />
						</div>
						<div class="addReviewButton addReviewCancel">
							<input type="button" class="clsButton clsBtnFontNormal clsLargeRadius clsButtonBgNormal"
							ontouchstart="highlightBtnAll(this,'clsBtnFontNormal','clsBtnFontHL')" ontouchend="disHighlightBtnAll(this,'clsBtnFontHL','clsBtnFontNormal')" 
							ontouchmove="disHighlightBtnAll(this,'clsBtnFontHL','clsBtnFontNormal')" value="<%=msg.get("common.button.Cancel")%>" onClick="cancelReview()"/>
						</div>
				   </div>
				</div>
			</div>
		</div>
	</div>
</div>
<%out.flush();%>
<div id="loadingPopup" class="loadingPopup"></div>
<div id="backgroundPopup" class="popupBackground"></div>
<div id="alertPopup" class="alertPopup"></div>
<input type="hidden" id="platform"  value="<%=clientInfo.getPlatform()%>">
<%@ include file="/html/jsp/Footer.jsp"%>
<script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/review_compressed.js"%>"></script>
<%-- <script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/poidetailcache.js"%>"></script> --%>
<%-- <script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/addreview.js"%>"></script> --%>
<%-- <script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/viewReview.js"%>"></script> --%>
<%-- <script type="text/javascript" charset="utf-8" src="<%=contextPath + "js/rating.js"%>"></script> --%>
<script type="text/javascript">
	var I18NHelper = {
			"addreview.no.rating": "<%=msg.get("addreview.no.rating")%>",
			"addreview.optional": "<%=msg.get("addreview.optional")%>",
			"common.button.OK": "<%=msg.get("common.button.OK") %>",
			"username.placeholder": "<%=msg.get("addreview.username.placeholder")%>",
			"nickname.title": "<%=msg.get("nickname.title")%>",
			"common.network.error.title": "<%=msg.get("common.network.error.title")%>",
			"common.network.error.text": "<%=msg.get("common.network.error.text")%>"
	};
		
	$(document).ready(function() {
		 document.addEventListener("deviceready", onDeviceReady, false);
		 initAddReview();
		 onDeviceReady();
	});

	$(window).resize(function(){ 
        if(PopupUtil.hasPopup())
        {
            setTimeout(function(){
                PopupUtil.center();
            },50);
        }
	});
</script>
</body>
</html>
