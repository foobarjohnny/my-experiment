<%@page import="com.telenav.cserver.framework.html.util.HtmlFrameworkConstants"%>
<%@page import="com.telenav.cserver.html.util.HtmlConstants"%>
<%@page import="com.telenav.cserver.framework.html.util.HtmlCommonUtil" %>
<html>
<title>PoiList Feedback</title>

<head>
<%@ include file="/html/jsp/Header.jsp"%>
<%
	String[] options = { msg.get("poi.feedback.listOption1"), msg.get("poi.feedback.listOption2"), msg.get("poi.feedback.listOption3"),msg.get("poi.feedback.listOption4")};
%>
<link href="<%=cssUrl + "feedback.css"%>" rel="stylesheet">
</head>
<body>
<div id="container_div" class="feedbackBg">
	<div id="titleBarDiv" class="clsTitleFrame clsTitleBg" >
		<div class="div_table">
			<div id="backButtonDiv"  class="div_cell titlePaddingDiv"></div>
	 		<div class="div_cell align_center clsTitleContent fdTitle" >
	 			<html:msg key="common.givefeedback"/>
	 		</div>
	 		<div class="div_cell titlePaddingDiv"></div>
		</div>
	</div>
	
  	<%-- feedback question --%>
  	<div id="questionBarDiv" class="align_center fc_gray fs_large">
	  	<div class="clsQuestionContent" id="questionContent"></div>
	  	<div class="clsQuestionKey" id="questionKey"></div>
  	</div>
  	
  	<%-- feedback options --%>
  	<div class="optionsDiv" >
 	<%
		for (int i = 0; i < options.length; i++) {
	%>
   		<div id="<%="optionDiv"+i %>"  class="div_table clsListBgNormal clsFontColor_gray clsFeedbackBg" onclick="changeImg('<%=i %>')" ontouchstart="highLightOptionItem('<%=i %>')" ontouchend="dishighLightOptionItem('<%=i %>')" ontouchmove="dishighLightOptionItem('<%=i %>')">
   			<div class="div_cell checkboxContent clsEllipsis" >
   				<label class="fs_middle" id="<%="op" + i%>" ><%= options[i]%></label>
   			</div>
   			<div class="div_cell align_right checkboxImg">
				<input class="clsCheckBox" type="checkbox" id="<%="option" + i%>" name="feedbacks" value="<%=i%>" />
				<img id="<%="image" + i %>" class="check_box_unfocused" />
   			</div>   			
		</div>
	<%
		}
	%>
	</div>
	
	
	<div class="clsCommentTitle fc_gray fs_middle align_left">
		<html:msg key="poi.feedback.listOption5"/>
	</div>
	
	<div class="clsComment">
		<textarea class="textArea fs_small fc_gray clsMiddleRadius" id="comment" autocomplete="off" autocorrect="off" spellcheck="false"></textarea>
	</div>		
	
	<div class="bar"></div>
	
	<div class="clsSaveButtonStyle align_center" >
		<input type="button" class="fs_large clsButton clsFontColor_gray clsSmallRadius clsButtonBgNormal"
						id="saveButton" ontouchstart='highlightBtnAll(this)' 
						ontouchend='disHighlightBtnAll(this)' value="<%=msg.get("common.button.Save")%>" ontouchmove='disHighlightBtnAll(this)' value="<%=msg.get("common.button.Save")%>"/>
	</div>
</div>
<%out.flush();%>
<div id="loadingPopup"></div>
<div id="backgroundPopup" class="popupBackground"></div>
<div id="alertPopup" class="alertPopup"></div>
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
<script src="<%=contextPath + "js/poilistfeedback.js"%>"></script>
<script>
	var I18NHelper = {
			"poi.feedback.listQuestion" : "<%=msg.get("poi.feedback.listQuestion") %>",
			"poi.feedback.listQuestionWithKeyword" : "<%=msg.get("poi.feedback.listQuestionWithKeyword") %>",
			"poi.feedback.selectFeedback" : "<%=msg.get("poi.feedback.selectFeedback") %>",
			"poi.feedback.submitSuccess" : "<%=msg.get("poi.feedback.submitSuccess") %>",
			"common.button.OK" : "<%=msg.get("common.button.OK") %>",
			"common.thankyou" : "<%=msg.get("common.thankyou") %>",
			"common.network.error.title": "<%=msg.get("common.network.error.title")%>",
			"common.network.error.text": "<%=msg.get("common.network.error.text")%>"
	};
	
	var GLOBAL_parameter = {
			"searchCatName" : '<%=HtmlCommonUtil.getString(request.getParameter("searchCatName"))%>',
			"searchKeyword" : '<%=HtmlCommonUtil.getString(request.getParameter("searchKeyword")) %>',
			"searchLocation" : '<%=HtmlCommonUtil.getString(request.getParameter("searchLocation")) %>',
			"feedbackPage" : 'POIListFeedback'
	};
	
	$(document).ready(function(){
		init();
		if(CommonUtil.isIphone()){
			CommonUtil.addBackButtonForIphone("backButtonDiv", '<%=msg.get("iphone.back")%>', true);
		}
	});
	
	$(window).resize(function(){ 
		setQuestionContent();
		if(PopupUtil.hasPopup())
		{
			PopupUtil.center();
		}
	});
      	
</script>
</body>
</html>