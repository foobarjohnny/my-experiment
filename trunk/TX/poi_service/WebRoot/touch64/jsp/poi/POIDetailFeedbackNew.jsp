<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../Header.jsp"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%@page import="com.telenav.tnbrowser.util.DataHandler" %>
<%@page import="com.telenav.cserver.browser.framework.BrowserFrameworkConstants" %>
<%@page import="com.telenav.j2me.datatypes.TxNode" %>
<%@page import="org.json.me.JSONObject" %>
<%@page import="org.json.me.JSONException" %>
<%@page import="com.telenav.tnbrowser.util.*"%>

<%
	String pageURL            = getPage + "POIDetailFeedback";
	String callBackURL        = getPageCallBack + "POIDetailFeedback";
	 
	String pNumStr = request.getParameter("pageNumber");
	if (pNumStr == null) {
		pNumStr = "1";
	}
	int pageNumber = Integer.parseInt(pNumStr.toString());

	String pSizeStr = request.getParameter("pageSize");
	if (pSizeStr == null) {
		pSizeStr = "1";
	}else{
		pSizeStr = pSizeStr.split(";")[0];
	}
	int pageSize = Integer.parseInt(pSizeStr.toString());
	
	String feedbackTopic = request.getParameter("feedBackTopic");
	
	TxNode listNode = (TxNode) request.getAttribute("questionNode");
	int questionSize = (int) listNode.valueAt(0);
	TxNode indexNode = (TxNode) request.getAttribute("indexNode");
	int firstIndex = (int) indexNode.valueAt(0);
	int lastIndex = (int) indexNode.valueAt(1);
	
	String subKey = request.getParameter("subKey");
	String subValue = request.getParameter("subValue");
	if(subKey == null) subKey = "";
	else subKey = subKey.split(";")[0];
	if(subValue == null) subValue = "";
	else subValue = subValue.split(";")[0];
	
	System.out.println("page Number " + pageNumber + " page size " + pageSize + " feedbackTopic " + feedbackTopic);
	System.out.println("first index " + firstIndex + " lastIndex " + lastIndex);
	System.out.println("subKey [" + subKey + "] subValue [" + subValue + "]");
	
	MessageWrap msgTemp = MessageHelper.getInstance(true).getMessageWrap(msgKey);
	String comment = msgTemp.get("poi.feedback.other.comment");
%>

<tml:TML outputMode="TxNode">
    <%@ include file="model/FeedbackModel.jsp"%>
	<jsp:include page="/touch64/jsp/poi/model/ShowDetailModel.jsp"/>
	<%@ include file="FeedbackScript.jsp"%>
	
	<tml:script language="fscript" version="1">

		<![CDATA[
			func onLoad()
				TxNode questionListNode
				questionListNode=ParameterSet.getParam("questionList")
				if NULL != questionListNode
				  Survey_M_saveQuestionListNode(questionListNode)
				  return FAIL
				endif
			endfunc

			func onClickNext()
               nextSurveyPage(1)
            endfunc
            
            func onClickPrevious()
               onBack()
            endfunc
            
            func nextSurveyPage(int forward)
            
                String continueUrl = "<%=host+"/GenericFeedbackRetrieval.do?feedBackTopic="+ feedbackTopic +"&pageName=POIListFeedback"%>"
	    		if 1 == forward
	    			continueUrl = continueUrl + "&pageNumber=" + "<%=pageNumber + 1%>" + "&pageSize=" + "<%=pageSize%>"
					saveSurveyAnswers()
				else
					continueUrl = continueUrl + "&pageNumber=" + "<%=pageNumber - 1%>" + "&pageSize=" + "<%=pageSize%>"
				endif
				MenuItem.setAttribute("nextPage", "url", continueUrl)
				System.doAction("nextPage")
				return FAIL
			endfunc
			
			func onBack()
			   if "<%=pageNumber -1%>" == "0"
			       		System.back()
			   else
			       		nextSurveyPage(0)
			       		return FAIL
			   endif
		    endfunc	 


			func getPoiJo()
			    JSONArray poiJsonArray = ShowDetail_M_getAddressList()
			    int index              = ShowDetail_M_getIndex()
			    JSONObject poiJo       = JSONArray.get(poiJsonArray,index)
				return poiJo
			endfunc
			
			func getPoiName()
				JSONObject poiJo = getPoiJo()
				String poiName   = JSONObject.get(poiJo, "name")
				return poiName
			endfunc
			
			func getPoiPhoneNumber()
				JSONObject poiJo      = getPoiJo()
				String poiPhoneNumber = JSONObject.get(poiJo, "phoneNumber")
				return poiPhoneNumber			
			endfunc

			func getPoiLocationJo()
				JSONObject poiJo      = getPoiJo()
				JSONObject locationJo = JSONObject.get(poiJo,"stop")
				return locationJo			
			endfunc

			func getPoiID()
				JSONObject poiJo      = getPoiJo()
				String poiID = JSONObject.getString(poiJo, "poiId")
				return poiID			
			endfunc
				
			#Add POI specific info to the answer node.
			#Add field name first then add field value.
			#field name should match part of the action request parser's method name.
			#so we can use java reflection API to set the values.	
			func addPOIValue(TxNode nodeValue)
				# set search keywords
				TxNode.addMsg(nodeValue, "PoiName")
				TxNode.addMsg(nodeValue, getPoiName())
			
			    # set category name
				TxNode.addMsg(nodeValue, "PoiPhoneNumber")
				TxNode.addMsg(nodeValue, getPoiPhoneNumber())
				
				# set poi ID
				TxNode.addMsg(nodeValue, "PoiID")
				TxNode.addMsg(nodeValue, getPoiID())
	           	
	           	JSONObject locationJo = getPoiLocationJo()
				String joStr = JSONObject.toString(locationJo) 
	           	TxNode.addMsg(nodeValue, "PoiLocation")
				TxNode.addMsg(nodeValue, joStr)
				return nodeValue
			endfunc
			
			func saveFeedbackWithAjax()
			# get survey answer node
				TxNode nodeValue = saveSurveyAnswers()
				nodeValue = addPOIValue(nodeValue)
				#at least one question should be answered.
				<%if(TnUtil.isEligibleForNewFeedBackWithoutComments(handlerGloble)){%>
					if 0 == validateAnswersWithoutComments(nodeValue)
						System.showErrorMsg("<%=msgTemp.get("poi.feedback.selectFeedback")%>")
						return FAIL
					else		
						TxRequest req
						String url="<%=host + "/GenericFeedbackSave.do"%>"
						String scriptName="poiDetailFeedbackSaveCallback"
						TxRequest.open(req,url)
						TxRequest.setRequestData(req,nodeValue)
						TxRequest.onStateChange(req,scriptName)
						TxRequest.setProgressTitle(req,"<%=msgTemp.get("common.saving")%>")
						TxRequest.send(req)
					endif	
				<%}else{%>	
					if 0 == validateAnswers(nodeValue)
						System.showErrorMsg("<%=msgTemp.get("poi.feedback.selectFeedback")%>")
						return FAIL
					else		
						TxRequest req
						String url="<%=host + "/GenericFeedbackSave.do"%>"
						String scriptName="poiDetailFeedbackSaveCallback"
						TxRequest.open(req,url)
						TxRequest.setRequestData(req,nodeValue)
						TxRequest.onStateChange(req,scriptName)
						TxRequest.setProgressTitle(req,"<%=msgTemp.get("common.saving")%>")
						TxRequest.send(req)
					endif
				<%}%>	
			endfunc
			
			func poiDetailFeedbackSaveCallback(TxNode node,int status)
				if status == 0
					System.showErrorMsg("<%=msgTemp.get("common.internal.error")%>")
					return FAIL
				elseif status == 1
					string msg = "<%=msgTemp.get("poi.feedback.submitSuccess")%>"
					System.showGeneralMsg(NULL,msg,NULL,NULL,3,"Callback_PopopTimeOut")
					return FAIL	    
				endif	
			endfunc
			
			func Callback_PopopTimeOut(int param)
				System.back()
				return FAIL
			endfunc

		]]>
	</tml:script>

	<tml:menuItem name="feedbackNext" onClick="onClickNext" />
	<tml:menuItem name="feedbackPrevious" onClick="onClickPrevious" />
	<tml:menuItem name="nextPage" pageURL="">
	</tml:menuItem>
	<tml:menuItem name="surveyComplete" pageURL="">
	</tml:menuItem>
	<tml:menuItem name="feedbackSave" onClick="saveFeedbackWithAjax"/>
	<tml:menuItem name="feedbackSaveMenu" onClick="saveFeedbackWithAjax" text="<%=msgTemp.get("common.button.Save")%>" trigger="KEY_MENU" />
  
	<tml:page id="POIDetailFeedback" url="<%=pageURL%>" type="net" helpMsg="$//$businessdetailsfeedback" groupId="<%=GROUP_ID_POI%>">

 		<tml:bean name="questionList" valueType="TxNode"
			value="<%=Utility.TxNode2Base64(listNode)%>"></tml:bean>
			
		<tml:title id="title" fontWeight="system_large|bold" fontColor="white">
			<%= msgTemp.get("common.givefeedback") %>
		</tml:title>
		<tml:menuRef name="feedbackSaveMenu" />
		
		<tml:panel id="feedbackPanel" layout="vertical" needShowScrollBar="true">

			<cserver:surveyQuestions pageSize="<%=pageSize%>"
				pageNumber="<%=pageNumber%>" questionsTxNodeName="questionNode"
				questionLabel="feedbackLabel" choiceLabel="feedbackOptions"
				subKey="<%=subKey%>" subValue="<%=subValue%>" comments="<%=comment%>"/>
		</tml:panel>
		<%
			if (pageNumber != 1){
		%>
		<tml:button id="previousButton" getFocus="false" visible="true"
			fontWeight="System_large"
			isFocusable="true" text="<%=msgTemp.get("common.button.Previous")%>">
			<tml:menuRef name="feedbackPrevious" />
		</tml:button>
		<%
			}
		%>
		<%
		    if (lastIndex >= (questionSize - 1)){
				if (pageNumber != 1){
		%>
		<tml:button id="saveButton" fontWeight="System_large"
			text="<%=msgTemp.get("common.button.Save") %>">
			<tml:menuRef name="feedbackSave" />
		</tml:button>
		<%
			} else {
		%>
		<tml:button id="saveButton1" fontWeight="System_large"
			text="<%=msgTemp.get("common.button.Save") %>">
			<tml:menuRef name="feedbackSave" />
		</tml:button>
		<%
				}
			} else {
		%>
		<tml:button id="nextButton" fontWeight="System_large"
			text="<%=msgTemp.get("common.button.Next") %>">
			<tml:menuRef name="feedbackNext" />
		</tml:button>
		<%
			} 
		%>
	</tml:page>

	<cserver:outputLayout />

</tml:TML>
