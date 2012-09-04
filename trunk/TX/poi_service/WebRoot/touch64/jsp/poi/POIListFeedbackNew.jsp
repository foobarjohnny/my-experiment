<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../Header.jsp"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%@page import="com.telenav.tnbrowser.util.DataHandler"%>
<%@page
	import="com.telenav.cserver.browser.framework.BrowserFrameworkConstants"%>
<%@page import="com.telenav.j2me.datatypes.TxNode"%>
<%@page import="org.json.me.JSONObject"%>
<%@page import="org.json.me.JSONException"%>
<%@page import="com.telenav.tnbrowser.util.*"%>

<%
	String pageURL = getPage + "POIListFeedbackGeneric";
	String callBackURL = getPageCallBack + "POIListFeedbackGeneric";
	

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
	<%@ include file="model/PoiListModel.jsp"%>
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
            
			# get search keyword
			func getSearchKeyword()
				String searchKeyword = <%=PoiListModel.getKeyWord()%>
				return searchKeyword
			endfunc
			
			# get category name
			func getCategoryName()
			    String catNameString = ""
               	TxNode catNameNode = PoiList_M_getCategoryName()
                if 	NULL != catNameNode
                	catNameString = TxNode.msgAt(catNameNode,0)
                endif
                
                return catNameString
			endfunc
			
			
			#Add POI specific info to the answer node.
			#Add field name first then add field value.
			#field name should match part of the action request parser's method name.
			#so we can use java reflection API to set the values.	
			func addPOIValue(TxNode nodeValue)
				# set search keywords
				TxNode.addMsg(nodeValue, "SearchKeyword")
				TxNode.addMsg(nodeValue, getSearchKeyword())
			
			    # set category name
				TxNode.addMsg(nodeValue, "SearchCatName")
				TxNode.addMsg(nodeValue, getCategoryName())

				int pageIndex = <%=PoiListModel.getPageIndex()%>
				
				TxNode.addMsg(nodeValue, "PageIndex")
				TxNode.addMsg(nodeValue, pageIndex + "")
				
				# set category name
				TxNode.addMsg(nodeValue, "TransactionID")
				String tIDs = PoiList_M_getTransactionIdOfPage(pageIndex - 1) + ";" 
				tIDs = tIDs + PoiList_M_getTransactionIdOfPage(pageIndex) + ";" 
				tIDs = tIDs + PoiList_M_getTransactionIdOfPage(pageIndex + 1)
				
				
				TxNode.addMsg(nodeValue, tIDs)
				
				# get and set search location
				JSONObject searchLocation
           		String searchLocationStr
				#searchType=5 means search poi, searchType=7 means search along. So audio types are different 
			    if isSearchAlongRoute()
			    	int alongType = PoiList_M_getSearchAlongType()
					TxNode searchInformationNode = PoiList_M_getSearchInformation()
			      	if NULL == searchInformationNode
			      		JSONObject.put(searchLocation,"lat",0)
					    JSONObject.put(searchLocation,"lon",0)
					else
						if <%=Constant.searchAlongType_closeAhead%> == alongType
				         	TxNode orgNode = TxNode.childAt(searchInformationNode,1)
						    int lat = TxNode.valueAt(orgNode,1)
						    int lon = TxNode.valueAt(orgNode,2)
						         
						    JSONObject.put(searchLocation,"lat",lat)
						    JSONObject.put(searchLocation,"lon",lon)
						         
						elsif <%=Constant.searchAlongType_nearDestination%> == alongType
						    TxNode destNode = TxNode.childAt(searchInformationNode,2)
						    int latDest = TxNode.valueAt(destNode,1)
						    int lonDest = TxNode.valueAt(destNode,2)
						         
						    JSONObject.put(searchLocation,"lat",latDest)
						    JSONObject.put(searchLocation,"lon",lonDest)
						endif
			      	endif
			    else
			    	searchLocation = PoiList_M_getAddress()
			    endif
	           	searchLocationStr = JSONObject.toString(searchLocation)
	           	TxNode.addMsg(nodeValue, "SearchLocation")
				TxNode.addMsg(nodeValue, searchLocationStr)
				return nodeValue
			endfunc
			
		
			 
			func saveFeedbackWithAjax()
				# get survey answer node
				TxNode nodeValue = saveSurveyAnswers()
				nodeValue = addPOIValue(nodeValue)
				println("addPOIValue(nodeValue)..........\n"+nodeValue)
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
					#Callback_PopopTimeOut()
					return FAIL	    
				endif	
			endfunc
			
			func Callback_PopopTimeOut(int param)
				#String completeUrl = "<%=getPageCallBack + "FeedbackComplete"%>"
				#MenuItem.setAttribute("surveyComplete", "url", completeUrl)
				#System.doAction("surveyComplete")
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
	<tml:menuItem name="feedbackSave" onClick="saveFeedbackWithAjax" />
	<tml:menuItem name="feedbackSaveMenu" onClick="saveFeedbackWithAjax"
		text="<%=msgTemp.get("common.button.Save")%>" trigger="KEY_MENU" />

	<tml:page id="POIListFeedback" url="<%=pageURL%>"
		type="net" helpMsg="$//$businessdetailsfeedback"
		groupId="<%=GROUP_ID_POI%>">

		<tml:bean name="questionList" valueType="TxNode"
			value="<%=Utility.TxNode2Base64(listNode)%>"></tml:bean>

		<tml:title id="title" fontWeight="system_large|bold" fontColor="white">
			<%=msgTemp.get("common.givefeedback")%>
		</tml:title>
		<tml:menuRef name="feedbackSaveMenu" />

		<tml:panel id="feedbackPanel" layout="vertical"
			needShowScrollBar="true">
			<cserver:surveyQuestions pageSize="<%=pageSize%>"
				pageNumber="<%=pageNumber%>" questionsTxNodeName="questionNode"
				questionLabel="feedbackLabel" choiceLabel="feedbackOptions"
				subKey="<%=subKey%>" subValue="<%=subValue%>" comments="<%=comment%>"/>
		</tml:panel>
		<% 
			if(pageNumber != 1){
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
		    if (lastIndex >= (questionSize - 1)) {
				if(pageNumber != 1){
		%>
		<tml:button id="saveButton" fontWeight="System_large"
			text="<%=msgTemp.get("common.button.Save") %>">
			<tml:menuRef name="feedbackSave" />
		</tml:button>
		<%
				}else{
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
