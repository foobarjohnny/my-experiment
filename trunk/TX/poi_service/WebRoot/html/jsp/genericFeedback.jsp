<%@page import="com.telenav.cserver.framework.html.util.HtmlFrameworkConstants"%>
<%@page import="com.telenav.cserver.framework.html.util.HtmlClientInfoFactory"%>
<%@page import="com.telenav.cserver.html.util.HtmlPoiUtil"%>
<%
	HtmlClientInfo clientInfo = (HtmlClientInfo)request.getAttribute(HtmlFrameworkConstants.HTML_CLIENT_INFO);
	String manifestName = "manifest.manifest?module=feedback&clientInfo=" + HtmlClientInfoFactory.getInstance().buildClientInfoString(clientInfo);
%>
<html manifest='<%=manifestName%>'>
<head>
<title>Generic Feedback</title>
<%@ include file="/html/jsp/Header.jsp"%>
<%-- <link href="<%=cssDeviceCommonPath + "poifeedback.css"%>" rel="stylesheet"> --%>
<link href="<%=cssProgUrl  + "poifeedback_compressed.css"%>" rel="stylesheet">
</head>
<body>
<div id="loading-overlay"></div>
<div id="container_div" class="feedbackBg">
	<%if(HtmlPoiUtil.hideGeneralFeedbackTitle(clientInfo)){%>
	<div id="titleBarDiv" class="clsTitleFrame clsTitleBg" >
		<div class="div_table">
			<div id="backButtonDiv"  class="div_cell titlePaddingDiv"></div>
	 		<div class="div_cell align_center clsTitleContent fdTitle" id="title">
	 			<html:msg key="common.givefeedback"/>
	 		</div>
	 		<div class="div_cell titlePaddingDiv"></div>
		</div>
	</div>
	<%}%>
	<div class="feedbackTopDiv">
	  	<%-- comment textarea --%>
	  	<div class="clsComment">
	 		<textarea class="textArea fs_small fc_gray clsMiddleRadius" id="comment" 
	 			autocomplete="off" autocorrect="off" spellcheck="false"  placeholder='<%=msg.get("poi.feedback.yourcomment") %>'></textarea>
		</div>
	</div>
	
	<%-- 	gradientline --%>
	<div class="gradientLine"></div>
	
	<%-- feedback main options --%>
	<div id="mainOptionsDiv" class="optionsDiv"></div>
	
	<div class="yourissue fc_gray fs_middle fw_bold">	
			<html:msg key="poi.feedback.yourissue" />
	</div>
  	<%-- feedback options --%>
  	<div id="optionListDiv" class="optionsDiv"></div>
	
	<%-- email div --%>
	<div class="optionsDiv">
		<div class="div_table clsFixTable clsFeedbackBgTop clsOptionBgNormal" id="toggleDiv">
			<div class="div_cell contactMe fc_gray fs_middle"><html:msg key="poi.feedback.contactMe" /></div>
			<div class="div_cell align_right togglerButton" >
				<input class="clsCheckBox" type="checkbox" id="togglerCheckBox" name="toggler"  checked="true"/>
				<img id="togglerButton" class="toggler_button_ON"/>
			</div>
		</div>
		<div class="div_table clsFeedbackBgBottom clsOptionBgNormal" id="emailDiv">
			<input type="email" class="emailAddress fs_middle fc_gray" id="emailAddress" autocorrect="off" autocapitalize="off" 
				autocomplete="off" spellcheck="false" placeholder='<%=msg.get("poi.feedback.emailContent") %>'/>
		</div>
	</div>
	
	<%-- save button div --%>
	<div class="clsSaveButtonStyle align_center" >
		<input type="button" class="fs_large clsButton clsButtonColorNormal clsLargeRadius clsButtonBgNormal"
						id="saveButton" ontouchstart='highlightBtnAll(this,"clsButtonColorNormal","clsButtonColorHighlight")' 
						ontouchend='disHighlightBtnAll(this,"clsButtonColorHighlight","clsButtonColorNormal")' ontouchmove='disHighlightBtnAll(this,"clsButtonColorHighlight","clsButtonColorNormal")' value="<%=msg.get("common.button.Submit")%>"/>
	</div>
	
	<div class="emailCommentDiv" id="emailCommentDiv">
	 	<p class="emailComentContent fs_smallest"><html:msg key="poi.feedback.emailComment"/></p>
	</div>
</div>
<%out.flush();%>
<%-- for popup when loading --%>
<div id="loadingPopup"></div>
<div id="alertPopup" class="alertPopup"></div>
<div id="emailConfirmPopup" class="emailConfirmPopup"></div>
<div id="submitSuccessPopup" class="submitSuccessPopup"></div>
<div id="backgroundPopup" class="popupBackground"></div>	

<%@ include file="/html/jsp/Footer.jsp"%>
<%-- <script src="<%=contextPath + "js/feedback.js"%>" ></script> --%>
<script src="<%=contextPath + "js/feedback_Compressed.js"%>" ></script>
<script>
	var I18NHelper = {
			"poi.feedback.yourcomment" : "<%=msg.get("poi.feedback.yourcomment") %>",
			"poi.feedback.selectFeedback" : "<%=msg.get("poi.feedback.selectFeedback") %>",
			"poi.feedback.submitSuccess" : "<%=msg.get("poi.feedback.submitSuccess") %>",
			"poi.feedback.invalidEmail" : "<%=msg.get("poi.feedback.invalidEmail")%>",
			"poi.feedback.selectedOther" : '<%=msg.get("poi.feedback.selectedOther")%>',
			"poi.feedback.hearBack" : '<%=msg.get("poi.feedback.hearBack_1")%>'+" "+"<%=msg.get("poi.feedback.hearBack_2")%>",
			"poi.feedback.submitLog" : "<%=msg.get("poi.feedback.submitLog")%>",
			"common.button.OK" : "<%=msg.get("common.button.OK") %>",
			"common.button.Skip" : "<%=msg.get("common.button.Skip") %>",
			"common.thankyou" : "<%=msg.get("common.thankyou") %>",
			"common.Yes" : "<%=msg.get("common.Yes") %>",
			"common.No" : "<%=msg.get("common.No") %>",
			"common.network.error.title": "<%=msg.get("common.network.error.title")%>",
			"common.network.error.text": "<%=msg.get("common.network.error.text")%>"
	};
	
	
	$(document).ready(function(){
		document.addEventListener("deviceready", onDeviceReady, false);
		CommonUtil.fetchClientInfoFromUrl();
		if(CommonUtil.isIphone()){
			CommonUtil.addBackButtonForIphone("backButtonDiv", '<%=msg.get("iphone.back")%>',true);
		}
		Feedback.init();
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