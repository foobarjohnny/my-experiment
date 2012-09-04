<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp"%>
<%@page import="com.telenav.browser.movie.Constant" %>

<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.telenav.browser.movie.Util"%>

<%
    String imageBg = imageUrl + "background_telenav.png";
    String pageURL = getPage + "ShareMovie";
    // get attributes for anchor lat, lon, distUnit and scale
    long   anchorLat = ((Long)request.getAttribute("anchorLat")).longValue();
    long   anchorLon = ((Long)request.getAttribute("anchorLon")).longValue();
    long   distUnit  = ((Long)request.getAttribute("distUnit")).longValue();
    String scale     = (String) request.getAttribute("scale");
%>

<tml:TML outputMode="TxNode">
	
	<jsp:include page="controller/ShareMovieController.jsp" />
	<jsp:include page="model/ShareMovieModel.jsp" />
	<jsp:include page="/touch/jsp/common/profile/controller/NameAndEmailController.jsp" />
	<jsp:include page="/touch/jsp/common/contact/controller/SelectContactController.jsp" />
		
	<tml:script language="fscript" version="1">
		<![CDATA[
		        func onLoad()
		        	JSONObject params = ShareMovie_M_getParams()
		        	String mName = JSONObject.getString(params, "<%= Constant.RRKey.M_NAME%>")
		        	String tName = JSONObject.getString(params, "<%= Constant.RRKey.M_THEATER_NAME%>")
		        	String tAddr = JSONObject.getString(params, "<%= Constant.RRKey.M_THEATER_ADDRESS%>")
		        	mName = mName
		        	tName = tName
			        Page.setComponentAttribute("mDetails","text",mName)
			        Page.setComponentAttribute("tDetails","text",tName)
					String tmp = Page.getControlProperty("sentToDisplay", "text")
					if tmp != ""
						Page.setComponentAttribute("sentToLabel","visible", "1")
						Page.setComponentAttribute("sentToDisplay","visible", "1")
						Page.setComponentAttribute("sentTo","visible", "0")
					else					
						Page.setComponentAttribute("sentTo","visible", "1")
						Page.setComponentAttribute("sentToDisplay","visible", "0")
						Page.setComponentAttribute("sentToLabel","visible", "0")
					endif
		        endfunc
		        
		        func CallBack_SelectContact()
		        	TxNode node = ParameterSet.getParam("contact")
					String contacttext = ""
					JSONObject jRec
					
					int count
					if node !=NULL
						ShareMovie_M_setContact(node)
						count = TxNode.getChildSize(node)
						JSONObject.put(jRec, "<%=Constant.RRKey.SM_RECIPIENT_NUMBER%>", count)
						int i=0
						TxNode nodeContact
						if count == 0
							nodeContact = TxNode.childAt(node,0) 
							contacttext = getContactText(nodeContact)
							addPhone(jRec, nodeContact, count)
						else
							while count-1>i
								nodeContact = TxNode.childAt(node,i) 
								addPhone(jRec, nodeContact, i)
								contacttext = contacttext + getContactText(nodeContact) + ", "
								i=i+1
							endwhile
							nodeContact = TxNode.childAt(node,count-1) 
							addPhone(jRec, nodeContact, count-1)
							contacttext = contacttext + getContactText(nodeContact)				
						endif
					endif
					
		        	if jRec != NULL
		        		ShareMovie_M_saveRecipients(jRec)
		        	endif
		        	
					Page.setComponentAttribute("sentToDisplay","text", contacttext)
		
					if contacttext != ""
						Page.setControlProperty("sentButton","focused","true")
					endif
		        endfunc
		        
		        func getPhoneNoAsNum(string text)
					string newPhoneNo = String.at(text,0,3) + String.at(text,4,3) + String.at(text,8,4) 
	        		return newPhoneNo		        
		        endfunc
		        
		        func addPhone(JSONObject jRec, TxNode contact, int num)
		        	String key = "<%=Constant.RRKey.SM_RECIPIENT%>" + num
		        	String phone = TxNode.msgAt(contact,1)
					JSONObject.put(jRec, key, phone)
		        endfunc
		        
		        func getContactText(TxNode nodeContact)
		        	if nodeContact == NULL
		        		return ""
		        	endif
		        	String contactMsg = TxNode.msgAt(nodeContact,0)
		        	if "" == contactMsg
		        	   return getPhoneNoDisplayText(TxNode.msgAt(nodeContact,1))
		        	else
		        	   return TxNode.msgAt(nodeContact,0) + " " + getPhoneNoDisplayText(TxNode.msgAt(nodeContact,1))
		        	endif
		        endfunc
		        
				func getPhoneNoDisplayText(string phoneNo)
					if phoneNo == NULL
						return ""
					endif
					
					if String.getLength(phoneNo) != 10
						return phoneNo
					endif
					
					string newPhoneNo = "(" + String.at(phoneNo,0,3) + ") " + String.at(phoneNo,3,3) + "-" + String.at(phoneNo,6,4)
					return newPhoneNo
				endfunc
				
				#because client don't provide api to create TxNode for Contact, so c-server just create it as bellow.
				func createContact(String phone)
					# change phone number XXX_XXX_XXXX to XXXXXXXXXX
					String firstPhone = String.at(phone,0,3)
					String secondPhone = String.at(phone,4,3)
					String lastPhone = String.at(phone,8,4)
					String phoneNumber = firstPhone + secondPhone + lastPhone
					String contactName = ""
					int TYPE_CONTACT = 168
					int contactType = 0
					int contactStatus = 1
					int contactId = 0
					int phoneNumberType = 3
					int groupNamesSize = 0
					# create TxNode for contact
					TxNode node
					#create child node about contact info, will add into contact node
					TxNode contactNode
					# create a empty child for contact info,  it's a reference to client  
					TxNode child
					TxNode.addValue(contactNode,TYPE_CONTACT)
					TxNode.addValue(contactNode,contactType)
					TxNode.addValue(contactNode,contactStatus)
					TxNode.addValue(contactNode,contactId)
					TxNode.addValue(contactNode,phoneNumberType)
					TxNode.addValue(contactNode,groupNamesSize)
					TxNode.addMsg(contactNode,contactName)
					TxNode.addMsg(contactNode,phoneNumber)
					TxNode.addChild(contactNode,child)
					TxNode.addChild(node,contactNode)
					return node				
				endfunc
		        
		        func savePhone()
		        	TxNode pNode = ParameterSet.getParam("sentTo")
		        	String phone = TxNode.msgAt(pNode, 0)
		        	if phone != ""
		        		int length = String.getLength(phone)
		        		TxNode node = ShareMovie_M_getContact()
		        		if  length == 12 && node == NULL
			        		TxNode cNode = createContact(phone)
			        		ShareMovie_M_setContact(cNode)
			        	endif
		        	endif
		        	#ShareMovie_M_removeRecipients()
		        	
		        	JSONObject joContact
		        	JSONObject.put(joContact,"callbackpageurl","<%=getPageCallBack + "ShareMovie"%>")
				    JSONObject.put(joContact,"callbackfunction","CallBack_SelectContact")
		        	JSONObject.put(joContact,"contactType","ShareAddress")
		        	JSONObject.put(joContact,"title","<%=msg.get("share.title")%>")
		        	
		        	TxNode contact = ShareMovie_M_getContact()
		        	if contact == NULL
		        		SelectContact_C_show(joContact)
		        	else
						SelectContact_C_showWithDefault(joContact,contact)	
        			endif
		            return FAIL
		        endfunc
		        
		        func doSent()
		        	TxNode firstNameNode = Preference.getPreferenceValue(25)
			        int ATTMapFlag = Account.isTnMaps()
			        if NULL == firstNameNode && ATTMapFlag != 1
			           updateUserInfo()
			           return FAIL
			        else
			           String firstName = TxNode.msgAt(firstNameNode,0)
				       if "" == firstName  && ATTMapFlag != 1
				          updateUserInfo()
				          return FAIL
				       endif
			        endif
		        	
		        	JSONObject jRec = ShareMovie_M_getRecipients()
		        	if jRec != NULL
		        		ShareMovie_M_sendSms("")
		        		return CONTINUE
		        	endif
		        	TxNode pNode = ParameterSet.getParam("sentTo")
		        	String phone = TxNode.msgAt(pNode, 0)
		        	if phone == ""
						System.showErrorMsg("<%=msg.get("share.err")%>")
						return FAIL
		        	endif
	        		int length = String.getLength(phone)
	        		if  length < 12
						System.showErrorMsg("<%=msg.get("share.err.short")%>")
						return FAIL
	        		endif
		        	ShareMovie_M_sendSms(phone)
		        endfunc
		        
		        func updateUserInfo()
			        String backUrl = "<%=getPageCallBack + "ShareMovie"%>"
			        String backFunc = "goToSent1"
			        
			        NameAndEmail_C_initialAndGoToJsp(backUrl,backFunc,"<%=Constant.shareMovie%>")
			    endfunc
		        

			func onChangeText(string id)
	        	string text
				text=Page.getControlProperty(id,"text")
				if text != NULL
					string newText = formatPhoneNo(text)
					Page.setComponentAttribute(id,"text",newText)
				endif        	
        	endfunc
        	
        	func formatPhoneNo(string text)
        		string newPhoneNo = ""
        		int length = String.getLength(text)
        		if  length == 4 || length == 8
        			if String.At(text,length-1,1) == "-"
        				newPhoneNo = text
        			else
        			    String inputNumber = String.At(text,length-1,1)
        			    if !String.isNumberString(inputNumber)
	        			   newPhoneNo = String.At(text,0,length-1)
	        			   return newPhoneNo
	        			endif
        				newPhoneNo = String.At(text,0,length-1) + "-" + String.At(text,length-1,1)
        			endif
        		else
        			 String inputNumber = String.At(text,length-1,1)
        			 if !String.isNumberString(inputNumber)
        			    newPhoneNo = String.At(text,0,length-1)
	        			return newPhoneNo
        			 endif
        			newPhoneNo = text
        		endif
        		return newPhoneNo
        	endfunc
			]]>
	</tml:script>
	
	<tml:menuItem name="selectRecipient" onClick="savePhone">
	</tml:menuItem>
	<tml:menuItem name="doSent" onClick="doSent">
	</tml:menuItem>
	
	<tml:page id="shareMovie" url="<%=pageURL%>" background="<%=imageBg%>" type="<%=pageType%>" groupId="<%=MOVIE_GROUP_ID%>"
		helpMsg="$//$sharemovie">
		<tml:title id="title" align="center|middle" fontWeight="bold|system_large" fontColor="white">
			<%=msg.get("share.title")%> 
		</tml:title>
		<tml:label id="mDetailsLabel" fontWeight="system_large|bold"	align="left|middle">
			<%=msg.get("movie")+":"%> 
		</tml:label>>
		<tml:label id="mDetails" fontWeight="system_large"	align="left|middle"/>
		
		<tml:label id="tDetailsLabel" fontWeight="system_large|bold"	align="left|middle">
			<%=msg.get("theater")+":"%>
		</tml:label>
		<tml:label id="tDetails" fontWeight="system_large"	align="left|middle"/>
		<tml:inputBox id="sentTo" title="<%=msg.get("share.input") + ":"%>" titleAbove="false"
			titleFontWeight="system_large|bold"	fontWeight="system_large"  prompt="<%=msg.get("share.input.prompt")%>" 
			length="12" style="phonenumber">
			<tml:param name="onChange" value="onChangeText"></tml:param>
		</tml:inputBox>
		
		<tml:label id="sentToLabel" fontWeight="system_large|bold"	align="left|middle">
			<%=msg.get("share.input") + ":"%> 
		</tml:label>
		<tml:label id="sentToDisplay" fontWeight="system_large"	align="left|middle"/>
		
		<tml:urlImageLabel id="recipients" fontWeight="system_large|bold"
			align="left|middle" showArrow="true"  focusFontColor="white">
			<tml:menuRef name="selectRecipient" />
			<%=msg.get("share.more")%>
		</tml:urlImageLabel>
		<tml:button id="sentButton" text="<%=msg.get("shareaddress.send")%>"
			fontWeight="system_large">
			<tml:menuRef name="doSent" />
		</tml:button>
		<tml:label id="smsNotify" fontWeight="system_small" align="center">
				(<%=msg.get("share.rate")%>)
		</tml:label>

		<cserver:outputLayout />
	</tml:page>
</tml:TML>
