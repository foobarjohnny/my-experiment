<%@ include file="../Header.jsp"%>
<%@page import="com.telenav.cserver.util.TnUtil"%>
<%@page import="java.util.Vector"%>
<%@page import="com.telenav.tnbrowser.util.DataHandler"%>
<%@page import="com.telenav.cserver.browser.framework.BrowserFrameworkConstants"%>
<%@page import="com.telenav.cserver.poi.struts.util.PoiUtil"%>
<%@page import="com.televigation.db.feedback.*" %>
<%@page import="com.telenav.cserver.about.datatypes.FeedbackContents" %>
<%@page import="com.telenav.xnav.feedback.FeedbackTopic" %>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%
%>
<tml:TML outputMode="TxNode">
	<tml:page id="AboutFeedback" url="<%=getPage + "AboutFeedback"%>" type="<%=pageType%>" helpMsg="$//$feedbacksurvey" groupId="<%=GROUP_ID_MISC%>">
	<tml:script language="fscript" version="1">
		<![CDATA[
			func getFeedBackValue()
				TxNode nodeValue
				
				TxNode dropDownBox0 = ParameterSet.getParam("dropDownBox0")
				TxNode.addValue(nodeValue,TxNode.valueAt(dropDownBox0,0)) 
				TxNode.addMsg(nodeValue,TxNode.msgAt(dropDownBox0,0)) 

				TxNode dropDownBox1 = ParameterSet.getParam("dropDownBox1")
				TxNode.addValue(nodeValue,TxNode.valueAt(dropDownBox1,0)) 
				TxNode.addMsg(nodeValue,TxNode.msgAt(dropDownBox1,0)) 

				TxNode dropDownBox2 = ParameterSet.getParam("dropDownBox2")
				TxNode.addValue(nodeValue,TxNode.valueAt(dropDownBox2,0)) 
				TxNode.addMsg(nodeValue,TxNode.msgAt(dropDownBox2,0))
				
				TxNode dropDownBox3 = ParameterSet.getParam("dropDownBox3")
				TxNode.addValue(nodeValue,TxNode.valueAt(dropDownBox3,0)) 
				TxNode.addMsg(nodeValue,TxNode.msgAt(dropDownBox3,0))
								
				TxNode dropDownBox4 = ParameterSet.getParam("dropDownBox4")
				TxNode.addValue(nodeValue,TxNode.valueAt(dropDownBox4,0)) 
				TxNode.addMsg(nodeValue,TxNode.msgAt(dropDownBox4,0))
				
				return nodeValue			
			endfunc
		
			func submitFeedbackWithAjax()
				TxRequest req
				String url="<%=host + "/AboutFeedbackSubmit.do"%>"
				String scriptName="aboutFeedbackSubmitCallback"
				TxRequest.open(req,url)
				TxRequest.setRequestData(req,getFeedBackValue())
				TxRequest.onStateChange(req,scriptName)
				TxRequest.setProgressTitle(req,"<%=msg.get("about.feedback.submit")%>")
				TxRequest.send(req)	
			endfunc
			
			func aboutFeedbackSubmitCallback(TxNode node,int status)
			    if status == 0
			        System.showErrorMsg("<%=msg.get("common.internal.error")%>")
					return FAIL
				elseif status == 1
					int retunStatus =TxNode.valueAt(node,0)
					string msg = "<%=msg.get("about.feedback.submitSuccess")%>"
					if retunStatus != 0
						msg = "<%=msg.get("about.feedback.submitFail")%>"
					endif
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

	<!-- title --> 
	<tml:title id="title" fontWeight="bold|system_large" align="center" fontColor="white">
		<%=msg.get("about.feedback.title")%>
	</tml:title>

	<tml:menuItem name="feedback::submit" onClick="submitFeedbackWithAjax"/>
	<tml:panel id="panel">
	<%
		int index = 0;
		Vector v = FeedbackContents.getInstance().getFeedbackTopics(locale);
		
		if (v != null && v.size() > 0)
		{
			
			for (int i = 0; i < v.size(); i++)
			{
			    FeedbackTopic topic = (FeedbackTopic)v.get(i);
				String subject = topic.getSubject();
				String[] answers = topic.getAnswers();
				
				if (subject != null && answers != null && answers.length > 0)
				{ 
	%>
				<tml:dropDownBox id="<%="dropDownBox" + i%>" fontWeight="sytem_large" titleFontWeight="bold|sytem_large" 
					isFocusable="true" title="<%= subject + ": " %>" selectedIndex="<%= index %>" name="<%=subject%>" >
				<%
					for (int j = 0; j < answers.length; j++)
					{
					  String answer = answers[j];
						if(answer.equals("Directory"))
						{
							answer = msg.get("startup.Search");
						}
						if(answer.equals("Tools/Extras"))
						{
							answer = msg.get("startup.Apps");
						}
						if(answer.equals("View Maps"))
						{ 
							answer = PoiUtil.getXMLString(msg.get("startup.Maps"));
						}
				%>
						<tml:dataItem text="<%= answer%>"/>
				<%
					}
				%>	
				</tml:dropDownBox>
	<% 
				}
			}	
		}
	%>
		</tml:panel>
		<tml:button id="submitButton"
			text="<%=msg.get("common.button.Submit")%>" fontWeight="system_large"
			imageClick=""
			imageUnclick="">
		    <tml:menuRef name="feedback::submit" />
		</tml:button>
	</tml:page>
	<cserver:outputLayout/>
</tml:TML>

