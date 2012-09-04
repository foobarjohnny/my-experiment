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

<%
	String pageURL            = getPage + "POIDetailFeedback";
	String callBackURL        = getPageCallBack + "POIDetailFeedback";
%>

<tml:TML outputMode="TxNode">
	<jsp:include page="/touch64/jsp/poi/model/ShowDetailModel.jsp"/>

	<tml:script language="fscript" version="1">

		<![CDATA[

			func preLoad()
				Page.setComponentAttribute("feedbackLabel","text",getFeedbackLabel())
			endfunc

			func checkBoxToString(String checkBoxId)
				TxNode node
				TxNode resultNode

				node=ParameterSet.getParam(checkBoxId)
				if(node!=NULL)
				
					int len =TxNode.getValueSize(node)
					TxNode.addValue(resultNode, len)
					int i =0
					while(i<len)
						TxNode.addMsg(resultNode, TxNode.msgAt(node, i))
						i=i+1
					endwhile
				endif

				return resultNode
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

			func getFeedbackLabel()
				string feedbackLabel = System.parseI18n("<%= msg.get("poi.feedback.detailQuestionWithName")%>") + " " + getPoiName() + "?"
				return feedbackLabel
			endfunc
				
			func getFeedBackValue()

				TxNode nodeValue = checkBoxToString("feedbackOptions")
				
				String tmpstr
				tmpstr = "POIDetailFeedback"
				TxNode.addMsg(nodeValue, tmpstr)

				# set feedback label
				TxNode.addMsg(nodeValue, getFeedbackLabel())

		        # get and set comments
		      	# This code is not used as we have removed the comments UI field
		        String comments    = ""
		        TxNode commentNode = ParameterSet.getParam("comments")
		        if 	NULL != commentNode
					comments = TxNode.msgAt(commentNode, 0)
		        endif
				TxNode.addMsg(nodeValue, comments)

				# set poi name
				TxNode.addMsg(nodeValue, getPoiName())

				# set poi phone number
				TxNode.addMsg(nodeValue, getPoiPhoneNumber())

				# set poi location
				JSONObject locationJo = getPoiLocationJo()
				String joStr = JSONObject.toString(locationJo)
				TxNode.addMsg(nodeValue, joStr)
				
				return nodeValue

			endfunc
			
			func checkOtherReason()
		       TxNode otherReasonNode = ParameterSet.getParam("comments")
	           if NULL != otherReasonNode
	              String otherReason = TxNode.msgAt(otherReasonNode,0)
	              if "" != otherReason
	                 return 1
	              else
	                 return 0
	              endif
	           else
	              return 0
	           endif
		    endfunc

			func saveFeedbackWithAjax()
				TxNode nodeValue = getFeedBackValue()
				if 0 == TxNode.valueAt(nodeValue,0) && 0 == checkOtherReason()
					System.showErrorMsg("<%= msg.get("poi.feedback.selectFeedback") %>")
					return FAIL
				else		
					TxRequest req
					String url="<%=host + "/POIFeedbackSave.do"%>"
					String scriptName="poiDetailFeedbackSaveCallback"
					TxRequest.open(req,url)
					TxRequest.setRequestData(req,nodeValue)
					TxRequest.onStateChange(req,scriptName)
					TxRequest.setProgressTitle(req,"<%=msg.get("common.saving")%>")
					TxRequest.send(req)
				endif
			endfunc
			
			func poiDetailFeedbackSaveCallback(TxNode node,int status)
				if status == 0
					System.showErrorMsg("<%=msg.get("common.internal.error")%>")
					return FAIL
				elseif status == 1
					string msg = "<%=msg.get("poi.feedback.submitSuccess")%>"
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

	<tml:menuItem name="feedbackSave" onClick="saveFeedbackWithAjax"/>
	<tml:menuItem name="feedbackSaveMenu" onClick="saveFeedbackWithAjax" text="<%=msg.get("common.button.Save")%>" trigger="KEY_MENU" />
		
	<tml:page id="POIDetailFeedback" url="<%=pageURL%>" type="<%=pageType%>" helpMsg="$//$businessdetailsfeedback" groupId="<%=GROUP_ID_POI%>">

		<tml:title id="title" fontWeight="system_large|bold" fontColor="white">
			<%= msg.get("common.givefeedback") %>
		</tml:title>
		<tml:menuRef name="feedbackSaveMenu" />
		
		<tml:panel id="feedbackPanel" layout="vertical">

			<tml:nullField id="nullField1" />
			<tml:multiline id="feedbackLabel" fontWeight="bold|system_large" align="center|middle">
			</tml:multiline>
			<tml:nullField id="nullField2" />
			<tml:inputBox id="comments" fontWeight="system_large"
				titleFontWeight="system_large|bold" isAlwaysShowPrompt="true"
				title="<%=msg.get("poi.feedback.other.comment")%>">
			</tml:inputBox>
			<tml:checkBox id="feedbackOptions" isFocusable="true" fontWeight="system_large">

				<tml:checkBoxItem value="<%=0%>" text="<%= msg.get("poi.feedback.detailOption1") %>"></tml:checkBoxItem>
				<tml:checkBoxItem value="<%=1%>" text="<%= msg.get("poi.feedback.detailOption2") %>"></tml:checkBoxItem>
				<tml:checkBoxItem value="<%=2%>" text="<%= msg.get("poi.feedback.detailOption3") %>"></tml:checkBoxItem>
				<tml:checkBoxItem value="<%=3%>" text="<%= msg.get("poi.feedback.detailOption4") %>"></tml:checkBoxItem>

			</tml:checkBox>

			<tml:nullField id="nullField3" />
			<tml:panel id="buttonPanel">
				<tml:button id="saveButton" fontWeight="System_large" text="<%=msg.get("common.button.Save") %>">
		    		<tml:menuRef name="feedbackSave" />
				</tml:button>
			</tml:panel>
			
		</tml:panel>

	</tml:page>

	<cserver:outputLayout />

</tml:TML>
