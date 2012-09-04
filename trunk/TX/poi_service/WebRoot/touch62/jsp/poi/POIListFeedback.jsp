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
<%@page import="com.telenav.cserver.poi.model.PoiListModel"%>


<%
	String pageURL            = getPage + "POIListFeedback";
	String callBackURL        = getPageCallBack + "POIListFeedback";
%>

<tml:TML outputMode="TxNode">
	<%@ include file="model/PoiListModel.jsp"%>
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
				else
					TxNode.addValue(resultNode, 0)	
				endif

				return resultNode
			endfunc

			# get search keyword
			func getSearchKeyword()
				String searchKeyword = <%=PoiListModel.getKeyWord()%>
				return searchKeyword
			endfunc

			func getDisplayword()
				String displayword = <%=PoiListModel.getDisplayWord()%>
				return displayword
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

			# get feedback label
			func getFeedbackLabel()
			
				String feedbackLabel = ""
				String displayword = getDisplayword()
				String catName       = getCategoryName()
				if(displayword != NULL && displayword != "")
					feedbackLabel = System.parseI18n("<%= msg.get("poi.feedback.listQuestionWithKeyword")%>") + " " + displayword + "?";
				elsif (catName != NULL && catName != "")
					feedbackLabel = System.parseI18n("<%= msg.get("poi.feedback.listQuestionWithKeyword")%>") + " " + catName + "?";
				else
					feedbackLabel = System.parseI18n("<%= msg.get("poi.feedback.listQuestion")%>")
				endif
				return feedbackLabel
			endfunc
			
			func getFeedBackValue()

				TxNode nodeValue = checkBoxToString("feedbackOptions")
				if 0 == TxNode.valueAt(nodeValue,0)
					return nodeValue
				endif 
				
				# set page name
				String pageName = "POIListFeedback"
				TxNode.addMsg(nodeValue, pageName)

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

				# set search keywords
				TxNode.addMsg(nodeValue, getSearchKeyword())

				# set category name
				TxNode.addMsg(nodeValue, getCategoryName())

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
				TxNode.addMsg(nodeValue, searchLocationStr)
				return nodeValue
			endfunc
			     
			func saveFeedbackWithAjax()
				TxNode nodeValue = getFeedBackValue()
				if 0 == TxNode.valueAt(nodeValue,0)
					System.showErrorMsg("<%= msg.get("poi.feedback.selectFeedback") %>")
					return FAIL
				else
					TxRequest req
					String url="<%=host + "/POIFeedbackSave.do"%>"
					String scriptName="poiListFeedbackSaveCallback"
					TxRequest.open(req,url)
					TxRequest.setRequestData(req,nodeValue)
					TxRequest.onStateChange(req,scriptName)
					TxRequest.setProgressTitle(req,"<%=msg.get("common.saving")%>")
					TxRequest.send(req)					
				endif 
			endfunc
			
			func poiListFeedbackSaveCallback(TxNode node,int status)
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
		
	<tml:page id="POIListFeedback" url="<%=pageURL%>" type="<%=pageType%>" helpMsg="$//$searchfeedback" groupId="<%=GROUP_ID_POI%>">

		<tml:title id="title" fontWeight="system_large|bold" fontColor="white">
			<%= msg.get("common.givefeedback") %>
		</tml:title>
		<tml:menuRef name="feedbackSaveMenu" />
		
		<tml:panel id="feedbackPanel" layout="vertical">

			<tml:nullField id="nullField1" />
			<tml:multiline id="feedbackLabel" fontWeight="bold|system_large" align="center|middle">
				<![CDATA[<%= msg.get("poi.feedback.listQuestion") %> ]]>
			</tml:multiline>
			<tml:nullField id="nullField2" />

			<tml:checkBox id="feedbackOptions" isFocusable="true" fontWeight="system_large">

				<tml:checkBoxItem value="<%=0%>" text="<%= msg.get("poi.feedback.listOption1") %>"></tml:checkBoxItem>
				<tml:checkBoxItem value="<%=1%>" text="<%= msg.get("poi.feedback.listOption2") %>"></tml:checkBoxItem>
				<tml:checkBoxItem value="<%=2%>" text="<%= msg.get("poi.feedback.listOption3") %>"></tml:checkBoxItem>
				<tml:checkBoxItem value="<%=3%>" text="<%= msg.get("poi.feedback.listOption4") %>"></tml:checkBoxItem>
				<tml:checkBoxItem value="<%=4%>" text="<%= msg.get("poi.feedback.listOption5") %>"></tml:checkBoxItem>

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
