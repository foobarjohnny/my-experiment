<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%@ include file="../Header.jsp"%>
<%@page import="java.util.List"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<% 
	String pageURL = getPage + "SendUpdate";
%>
<tml:TML outputMode="TxNode">
<jsp:include page="/touch64/jsp/ac/controller/SelectContactController.jsp"/>
<%@ include file="model/SendUpdateModel.jsp"%>
	<tml:script language="fscript" version="1">
		<![CDATA[
		func checkNULL(string s)
        	if s== NULL
        		return ""
        	else
        		return s	
        	endif
        endfunc  
        		
		func getLocationText(JSONObject jo)
			string label = checkNULL(JSONObject.getString(jo,"label"))
			string firstLine = checkNULL(JSONObject.getString(jo,"firstLine"))
        	string city = checkNULL(JSONObject.getString(jo,"city"))
        	string state = checkNULL(JSONObject.getString(jo,"state"))
        	string text = ""
        	
        	if label != ""
        		text = label
        	elsif firstLine != ""
        		text = firstLine
        	else
        		if city != ""
        		text = city + ","
	        	endif
	
	        	if state != ""
	        		text = text + state
	        	endif
        	endif

			return text
		endfunc
		
        func onShow()
        	JSONObject joAddress = SendUpdate_M_getLocation()
            string locationText = getLocationText(joAddress)
			string text = SendUpdate_M_getUserName() + " " + "<%=msg.get("sendupdate.msg1")%>" + " " + locationText
			text = text + " " + "<%=msg.get("sendupdate.msg2")%>" + " " + SendUpdate_M_getTime() + "."
        	Page.setComponentAttribute("text1","text",text)
        	Page.setControlProperty("sentButton","focused","true") 
        	
        	checkInputLength(text)
        endfunc

        func onChangeText(string id)
        	string text
			text=Page.getControlProperty(id,"text")
			if text == NULL
				text = ""
			endif        	
        	checkInputLength(text)
        endfunc
        
        func checkInputLength(string text)
			displayInputLength(String.getLength(text))
        endfunc
        
        func displayInputLength(int length)
        	string text = length + "/80"
        	Page.setComponentAttribute("text2","text",text)
        endfunc

       	func CallBack_Popup(int param)
			Page.setControlProperty("text1","focused","true")
		endfunc
		        
        func onClickDoSent()
        	TxNode node
        	TxNode messageNode = ParameterSet.getParam("text1")
			string message = TxNode.msgAt(messageNode,0)
        	if message == NULL || message == ""
        		System.showGeneralMsg(NULL,"<%=msg.get("sendupdate.check1")%>","<%=msg.get("common.button.OK")%>",NULL,NULL,"CallBack_Popup")	
        		return FAIL
        	endif
        	
        	JSONObject jo
			JSONObject.put(jo,"message",message)
            JSONObject.put(jo,"ptns",SendUpdate_M_getContact())
            String strJo=JSONObject.toString(jo)
            TxNode.addMsg(node,strJo)
			TxRequest req
			string url="<%=host + "/sendUpdate.do"%>"
			string scriptName="Callback_SendUpdateAjax"
			TxRequest.open(req,url)
			TxRequest.setRequestData(req,node)
			TxRequest.onStateChange(req,scriptName)
			TxRequest.setProgressTitle(req,"<%=msg.get("common.sending")%>")
			TxRequest.send(req)  
        endfunc        		

        func Callback_SendUpdateAjax(TxNode node,int status)
            if status == 0
                System.showErrorMsg("<%=msg.get("error.not.available")%>")
                return FAIL
			elsif status == 1
				string result = TxNode.msgAt(node,0)
				string msg = ""
				if "N" == result
					msg = "<%=msg.get("sendupdate.failmsg")%>"
				else
			    	msg = "<%=msg.get("sendupdate.successmsg")%>"					
				endif
				
				System.showGeneralMsg(NULL,msg,NULL,NULL,3,"Callback_PopopTimeOut")				
			endif
        endfunc
        
        func Callback_PopopTimeOut(int param)
			MenuItem.setAttribute("callback","url","<%=getPageCallBack + "DriveToWrap"%>")
			TxNode node1
        	TxNode.addMsg(node1,"BackToNav") 
        	MenuItem.setBean("callback", "callFunction", node1)
        	System.doAction("callback")       
        endfunc
        
        func CallBack_SelectContact()
        	TxNode node
			node = SendUpdate_M_getContactNode()
			string contacttext = ""
			if node !=NULL
				int count = TxNode.getChildSize(node)
				int i=0
				TxNode nodeContact
				if count == 1
					nodeContact = TxNode.childAt(node,0)
					contacttext = TxNode.msgAt(nodeContact,1)
				else
					while count-1>i
						nodeContact = TxNode.childAt(node,i)
						contacttext = contacttext + TxNode.msgAt(nodeContact,1) + ","
						i=i+1
					endwhile
					nodeContact = TxNode.childAt(node,count-1)
					contacttext = contacttext + TxNode.msgAt(nodeContact,1)				
				endif
			endif
			
			SendUpdate_M_setContact(contacttext)
        endfunc

		func onBack()
			JSONObject joContact
        	JSONObject.put(joContact,"callbackpageurl","<%=getPageCallBack + "SendUpdate"%>")
        	JSONObject.put(joContact,"callbackfunction","CallBack_SelectContact")
        	JSONObject.put(joContact,"from","SendUpdate")
        	JSONObject.put(joContact,"contactType","SendUpdate")
        	JSONObject.put(joContact,"title","Send ETA To")
        	TxNode contact = SendUpdate_M_getContactNode()
        	if contact == NULL
        		SelectContact_C_show(joContact)
        	else
				SelectContact_C_showWithDefault(joContact,contact)	
        	endif
			
			return FAIL
		endfunc
		]]>
	</tml:script>
	
	<tml:menuItem name="callback" pageURL="" trigger="KEY_RIGHT | TRACKBALL_CLICK">
			<tml:bean name="callFunction" valueType="string" value=""/>
	</tml:menuItem>
	<tml:menuItem name="doSentMenu" trigger="KEY_MENU" onClick="onClickDoSent" text="<%=msg.get("sendupdate.send")%>"/>
	<tml:menuItem name="doSent" trigger="KEY_RIGHT | TRACKBALL_CLICK" onClick="onClickDoSent">
	</tml:menuItem>
	<tml:page id="SendUpdate" url="<%=pageURL%>" type="<%=pageType%>" groupId="<%=GROUP_ID_MISC%>">
		<tml:menuRef name="doSentMenu" />
		<tml:title id="title" align="center|middle" fontWeight="bold|system_large" fontColor="white">
			<%=msg.get("sendupdate.title")%>
		</tml:title>
		<tml:inputBox id="text1" isFocusable="true" 
			type="textarea" prompt="" length="80" fontWeight="system_large">
			<tml:param name="onChange" value="onChangeText"></tml:param>
		</tml:inputBox>
		<tml:label id="text2" fontWeight="system_small" align="center">
				<![CDATA[0/80]]>
		</tml:label>
		<tml:button id="sentButton" text="<%=msg.get("sendupdate.send")%>"
			fontWeight="system_large" isFocusable="true"
			imageClick=""
			imageUnclick="">
			<tml:menuRef name="doSent" />
		</tml:button>
		<tml:label id="smsNotify" fontWeight="system_small" align="center">
				<%=msg.get("common.sms.prompt")%>
		</tml:label>
	</tml:page>
	<cserver:outputLayout/>		
</tml:TML>