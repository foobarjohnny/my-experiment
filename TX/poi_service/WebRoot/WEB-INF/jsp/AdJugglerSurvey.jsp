<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%@page import="com.telenav.tnbrowser.util.DataHandler" %>
<%@page import="com.telenav.cserver.browser.framework.BrowserFrameworkConstants" %>
<%@page import="com.telenav.j2me.datatypes.TxNode" %>
<%@page import="org.json.me.JSONObject" %>
<%@page import="org.json.me.JSONException" %>
<%@page import="com.telenav.tnbrowser.util.*"%>
<%@page import="java.net.URLEncoder"%>

<%
	
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
	String pageName = request.getParameter("pageName");
	
	
	String pageURL            = getPage + pageName;
	String callBackURL        = getPageCallBack + pageName;
	
	String feedbackTitle = request.getParameter("title");
	String encFeedbackTitle = "";
	if (feedbackTitle != null) {
		feedbackTitle = feedbackTitle.split(";")[0];
		encFeedbackTitle = URLEncoder.encode(feedbackTitle, "UTF-8");
	}
	
	TxNode listNode = (TxNode) request.getAttribute("questionNode");
	int questionSize = (int) listNode.valueAt(0);
	TxNode indexNode = (TxNode) request.getAttribute("indexNode");
	int firstIndex = (int) indexNode.valueAt(0);
	int lastIndex = (int) indexNode.valueAt(1);
	
	System.out.println("page Number " + pageNumber + " page size " + pageSize + " feedbackTopic " + feedbackTopic);
	System.out.println("first index " + firstIndex + " lastIndex " + lastIndex);
	
%>

<tml:TML outputMode="TxNode">
    <%@ include file="poi/model/FeedbackModel.jsp"%>
	<%@ include file="poi/FeedbackScript.jsp"%>
	
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
            
                String continueUrl = "<%=host+"/GenericFeedbackRetrieval.do?feedBackTopic="+ feedbackTopic +"&pageName=" + pageName + "&title="+encFeedbackTitle%>"
	    		if 1 == forward
	    			continueUrl = continueUrl + "&pageNumber=" + "<%=pageNumber + 1%>" + "&pageSize=" + "<%=pageSize%>"
	    			print("continueURL: " + continueUrl)
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

			func saveFeedbackWithAjax()
			# get survey answer node
				TxNode nodeValue = saveSurveyAnswers()
				#at least one question should be answered.
				if 0 == validateAnswers(nodeValue)
					System.showErrorMsg("<%=msg.get("generic.feedback.selectFeedback")%>")
					return FAIL
				else		
					TxRequest req
					String url="<%=host + "/GenericFeedbackSave.do"%>"
					String scriptName="feedbackSaveCallback"
					TxRequest.open(req,url)
					TxRequest.setRequestData(req,nodeValue)
					TxRequest.onStateChange(req,scriptName)
					TxRequest.setProgressTitle(req,"<%=msg.get("common.saving")%>")
					TxRequest.send(req)
				endif
			endfunc
			
			func feedbackSaveCallback(TxNode node,int status)
				if status == 0
					System.showErrorMsg("<%=msg.get("common.internal.error")%>")
					return FAIL
				elseif status == 1
					Survey_M_setSurveyDone("<%=pageName%>")
					string msg = "<%=msg.get("generic.feedback.submitSuccess")%>"
					System.showGeneralMsg(NULL,msg,"Done",NULL,NULL,"Callback_PopopTimeOut")
					return FAIL	    
				endif	
			endfunc
			
			func Callback_PopopTimeOut(int param)
				System.doAction("MainPage")
				return FAIL
			endfunc

		]]>
	</tml:script>
    <tml:menuItem name="MainPage" pageURL="<%=host + "/startUp.do?pageRegion=" + region%>">
	</tml:menuItem>
	<tml:menuItem name="feedbackNext" onClick="onClickNext" />
	<tml:menuItem name="feedbackPrevious" onClick="onClickPrevious" />
	<tml:menuItem name="nextPage" pageURL="">
	</tml:menuItem>
	<tml:menuItem name="surveyComplete" pageURL="">
	</tml:menuItem>
	<tml:menuItem name="feedbackSave" onClick="saveFeedbackWithAjax"/>
	<tml:menuItem name="feedbackSaveMenu" onClick="saveFeedbackWithAjax" text="<%=msg.get("common.button.Save")%>" trigger="KEY_MENU" />
  
	<tml:page id="PurchaseExperienceSurvey" url="<%=pageURL%>" type="net" helpMsg="$//$businessdetailsfeedback" groupId="<%=GROUP_ID_POI%>">

 		<tml:bean name="questionList" valueType="TxNode"
			value="<%=Utility.TxNode2Base64(listNode)%>"></tml:bean>
			
		<tml:title id="title" fontWeight="system_large|bold" fontColor="white">
			<![CDATA[<%=feedbackTitle%>]]>
		</tml:title>
		<tml:menuRef name="feedbackSaveMenu" />
		
		<tml:panel id="feedbackPanel" layout="vertical" needShowScrollBar="true">

			<cserver:surveyQuestions pageSize="<%=pageSize%>"
				pageNumber="<%=pageNumber%>" questionsTxNodeName="questionNode"
				questionLabel="feedbackLabel" choiceLabel="feedbackOptions"
            />
		</tml:panel>
		<tml:button id="previousButton" getFocus="false" visible="true"
			fontWeight="System_large"
			isFocusable="true" text="<%=msg.get("common.button.Previous")%>"
		    imageClick="<%=imageUrl + "button_small_on.png"%>"
			imageUnclick="<%=imageUrl + "button_small_off.png"%>">
			<tml:menuRef name="feedbackPrevious" />
		</tml:button>
		<%
			
		    if (lastIndex >= (questionSize - 1)) {
		%>
		<tml:button id="saveButton" fontWeight="System_large"
			text="<%=msg.get("common.button.Save") %>"
			imageClick="<%=imageUrl + "button_small_on.png"%>"
			imageUnclick="<%=imageUrl + "button_small_off.png"%>">
			<tml:menuRef name="feedbackSave" />
		</tml:button>
		<%
			} else {
		%>
		<tml:button id="nextButton" fontWeight="System_large"
			text="<%=msg.get("common.button.Next") %>"
			imageClick="<%=imageUrl + "button_small_on.png"%>"
			imageUnclick="<%=imageUrl + "button_small_off.png"%>">
			<tml:menuRef name="feedbackNext" />
		</tml:button>
		<%
			}
		%>

	</tml:page>

	<cserver:outputLayout />

</tml:TML>