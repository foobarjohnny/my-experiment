<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%@page import="java.util.List"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@page import="com.telenav.cserver.pref.struts.util.PreferenceConstants"%>
<%@page import="com.telenav.cserver.util.TnUtil"%>
<%@ include file="../Header.jsp"%>
<tml:TML outputMode="TxNode">
<%@ include file="../ac/controller/AddressCaptureController.jsp"%>
<%@ include file="model/ShareAddressModel.jsp"%>
<%@ include file="/WEB-INF/jsp/StopUtil.jsp"%>
<jsp:include page="/WEB-INF/jsp/ac/controller/SelectAddressController.jsp" />
<jsp:include page="/WEB-INF/jsp/ac/controller/SelectContactController.jsp"/>
<jsp:include page="../controller/NameAndEmailController.jsp"/>
<%@ include file="../GetClientInfo.jsp"%>
<%@ include file="../PoiUtil.jsp"%>
	<tml:script language="fscript" version="1">
		<![CDATA[
        func chooseLocation()
            ShareAddress_M_clearLabel()
            
            JSONObject jo
        	JSONObject.put(jo,"title","<%=msg.get("common.chooseLocation")%>")
        	JSONObject.put(jo,"mask","01111111111")
        	JSONObject.put(jo,"from","Common")
        	JSONObject.put(jo,"callbackfunction","CallBack_SelectAddress")
			JSONObject.put(jo,"callbackpageurl","<%=getPageCallBack + "ShareAddress"%>")
			JSONObject.put(jo,"getGPS",1)
			SelectAddress_C_SelectAddress(jo)
			return FAIL
        endfunc
        
        func CallBack_SelectAddress()
			TxNode addressNode
			addressNode=ParameterSet.getParam("returnAddress")
			String joString = TxNode.msgAt(addressNode,0)
			JSONObject jo = JSONObject.fromString(joString)
			ShareAddress_M_setAddress(jo)
			ShareAddress_M_clearPoi()
			if TxNode.getStringSize(addressNode) > 1
				String poiString = TxNode.msgAt(addressNode,1)
				JSONObject joPoi = JSONObject.fromString(poiString)
				ShareAddress_M_setPoi(joPoi)
			endif
        endfunc
		        
        func getLabelFromStop(JSONObject jo)
			string firstline,city,state,label
			firstline = JSONObject.get(jo,"firstLine")
			city = JSONObject.get(jo,"city")
			label = JSONObject.get(jo,"label")        
			if firstline!=NULL && firstline!=""
				return firstline
			elsif city!=NULL && city!=""
				return city
			else
			  	return label
			endif	
        endfunc
        
       	func CallBack_Popup(int param)
		
		endfunc
        
        func onClickDoSent()
			string sentTo = ShareAddress_M_getSendToText()
			
			if sentTo == NULL || sentTo == ""
			 System.showGeneralMsg(NULL,"<%=msg.get("shareaddress.selectrecipient")%>","<%=msg.get("common.button.OK")%>",NULL,NULL,"CallBack_Popup")
			 return FAIL
			endif

			saveLabel()
						
            TxNode firstNameNode = Preference.getPreferenceValue(<%=PreferenceConstants.KEY_GENERAL_FIRSTNAME%>)
            TxNode lastNameNode = Preference.getPreferenceValue(<%=PreferenceConstants.KEY_GENERAL_LASTNAME%>)
	        
	        int sourceFlag = NameAndEmail_C_getDonotAskAgain("<%=Constant.shareAddress%>")
	        int ATTMapFlag = Account.isTnMaps()
	        <%if(TnUtil.isTMOUser(product) || TnUtil.isUsccUser(product)){
	        	%>
	        	   ATTMapFlag = 0
	        	<%
	        }%>
	        if (NULL == firstNameNode || NULL == lastNameNode) && sourceFlag == 0 && ATTMapFlag != 1
	           updateUserInfo()
	        else
	           String firstName = TxNode.msgAt(firstNameNode,0)
	           String lastName = TxNode.msgAt(lastNameNode,0)
		       if ("" == firstName || "" == lastName)  && sourceFlag == 0 && ATTMapFlag != 1
		          updateUserInfo()
		       else
					doSent()
		       endif
	        endif
        endfunc
        
        func doSent()
			JSONObject joAddress = ShareAddress_M_getAddress()
			if joAddress == NULL
				System.showGeneralMsg(NULL,"Please select one address first","<%=msg.get("common.button.OK")%>",NULL,NULL,"CallBack_Popup")
				return FAIL
			else
				doPreSendWithAjax() 
			endif      
        endfunc
        
        
        func updateUserInfo()
	        String backUrl = "<%=getPageCallBack + "ShareAddress"%>"
	        String backFunc = "updateUserBack1"
	        
	        NameAndEmail_C_initialAndGoToJsp(backUrl,backFunc,"<%=Constant.shareAddress%>")
	    endfunc
	    
	    func updateUserBack()
	        doSent()
	    endfunc
        
		func onLoad()
			display()
		endfunc
		
		func display()
			JSONObject joAddress = ShareAddress_M_getAddress()
			if	joAddress == NULL
				string addressLabel = "<%=msg.get("common.currentLocation")%>"
				Page.setComponentAttribute("addressContent","text",addressLabel)	
			else
				displayAddress(joAddress)
			endif
			displayLabel()
			displaySentTo()	
		endfunc
		
		func displaySentTo()
			string contactText = getContactDisplayText()
			string sentTo = ""
			
			if contactText == ""
				sentTo = ""
			else
				sentTo = contactText	
			endif
						
			ShareAddress_M_setSendToText(sentTo)
			Page.setComponentAttribute("sentTo","text",sentTo)

			if sentTo != ""
				Page.setControlProperty("sentButton","focused","true")
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
				
		func displayAddress(JSONObject jo)
			string value = "" 
    		if isCurrentStop(jo)
				value = "<%=msg.get("common.currentLocation")%>"
			else
	    		value = getLabelFromStop(jo)
			endif
 
			Page.setComponentAttribute("addressContent","text",value)
		endfunc
		
		func displayLabel()
			string label = ShareAddress_M_getLabel()
			if label != NULL && "" != label
				Page.setComponentAttribute("label","text",label)
				return FALSE
			endif
			
			JSONObject poiJO = ShareAddress_M_getPoi()
			JSONObject addressJO = ShareAddress_M_getAddress()
			if poiJO != NULL
				label = JSONObject.get(poiJO,"name")
			else
				if addressJO!=NULL
				    label = JSONObject.get(addressJO,"label")
				    if NULL == label || "" == label
				       label = JSONObject.get(addressJO,"firstLine")
				    endif
				    
				    if NULL == label || "" == label
				       label = JSONObject.get(addressJO,"city")
				    endif
				endif
			endif
			
			if label == NULL
				label = ""
			endif
			ShareAddress_M_setLabel(label)
			Page.setComponentAttribute("label","text",label)		
		endfunc
        
        func doPreSendWithAjax()
        	#check if need doRGC
        	JSONObject addressJO = ShareAddress_M_getAddress()
        	string state = JSONObject.get(addressJO,"state")
			string city = JSONObject.get(addressJO,"city")
			if state == NULL
				state = ""
			endif

			if city == NULL
				city = ""
			endif
			
			if city == "" && state == ""
				doRGC()
			else
			    doSendWithAjax()
			endif
        endfunc

		func doRGC()
			JSONObject address = ShareAddress_M_getAddress()			
			JSONObject jo
			JSONObject.put(jo,"lat",JSONObject.get(address, "lat"))
			JSONObject.put(jo,"lon",JSONObject.get(address, "lon"))
			JSONObject.put(jo,"type",6)
			String joStr=JSONObject.toString(jo)
			
			TxNode node
			TxNode.addMsg(node,joStr)
			TxRequest req
			String url="<%=host + "/getCurrentLocation.do"%>"
			String scriptName="ShareAddressRGCCallback"
			TxRequest.open(req,url)
			TxRequest.setRequestData(req,node)
			TxRequest.onStateChange(req,scriptName)
			TxRequest.setProgressTitle(req,"<%=msg.get("rgc.loading")%>", "", TRUE)
			TxRequest.send(req)		
		endfunc

		func ShareAddressRGCCallback(TxNode node,int status)
			if status == 0
				System.showGeneralMsg(NULL,"<%=msg.get("error.not.available")%>","<%=msg.get("common.button.OK")%>",NULL,NULL,"RGCWaringCallback")	
				return FAIL
			endif
			
			String addressStr = TxNode.msgAt(node, 1)
			if NULL == addressStr
				System.showGeneralMsg(NULL,"<%=msg.get("rgc.error")%>","<%=msg.get("common.button.OK")%>",NULL,NULL,"RGCWaringCallback")	
				return FAIL
			else
				JSONObject address
				address = JSONObject.fromString(addressStr)
				ShareAddress_M_setAddress(address)
				doSendWithAjax()			
			endif
		endfunc

		func RGCWaringCallback(int index)
			resumeMap()
		endfunc
				        
        func doSendWithAjax()
            TxNode node
			String label = ShareAddress_M_getLabel()
			JSONObject addressJO = ShareAddress_M_getAddress()
			
			String addressString = ""
			if NULL != addressJO
			       addressString = JSONObject.toString(addressJO)
			endif
			
			JSONObject poiJO = ShareAddress_M_getPoi()
			String poiString = ""
			if NULL != poiJO
			       poiString = JSONObject.toString(poiJO)
			endif
						
			JSONObject jo
			JSONObject.put(jo,"label",label)
            JSONObject.put(jo,"sendTo",getContactList())
            JSONObject.put(jo,"addressString",addressString)
            JSONObject.put(jo,"poiString",poiString)
            String strJo=JSONObject.toString(jo)
            TxNode.addMsg(node,strJo)
			TxRequest req
			string url="<%=host + "/shareAddress.do"%>"
			string scriptName=""
			TxRequest.open(req,url)
			TxRequest.setRequestData(req,node)
			TxRequest.onStateChange(req,scriptName)
			TxRequest.setProgressTitle(req,"")
			TxRequest.send(req)
			ShareAddressSuccess()        
        endfunc

        func ShareAddressSuccess()
        	saveRecentPlace()
			string msg = "<%=msg.get("shareaddress.successmsg")%>"			
		    System.showGeneralMsg(NULL,msg,NULL,NULL,3,"Callback_PopopTimeOut")
        endfunc
                
        func Callback_ShareAddressAjax(TxNode node,int status)
            if status == 0
                System.showGeneralMsg(NULL,"<%=msg.get("error.not.available")%>","OK",NULL,NULL,"CallBack_Popup")
			    return FAIL
			elseif status == 1
				if TxNode.getStringSize(node) > 1
					JSONObject addressJO = JSONObject.fromString(TxNode.msgAt(node,1))
					ShareAddress_M_setAddress(addressJO)
				endif
				
				string result = TxNode.msgAt(node,0)
				string msg = ""
				if "N" == result
					msg = "<%=msg.get("shareaddress.failmsg")%>"
				else
					saveRecentPlace()
			    	msg = "<%=msg.get("shareaddress.successmsg")%>"					
				endif
			    System.showGeneralMsg(NULL,msg,NULL,NULL,3,"Callback_PopopTimeOut")
			endif
        endfunc
        
        func saveRecentPlace()
        	JSONObject poiJO = ShareAddress_M_getPoi()
        	TxNode newPlaceNode
            if poiJO != NULL
                newPlaceNode = PoiUtil_convertToNodeForResentSearch(poiJO)
                RecentPlaces.saveAddress(newPlaceNode)
            else
                RecentPlace_saveAddress(ShareAddress_M_getAddress())
            endif
        endfunc
        
        func Callback_PopopTimeOut(int param)
         	MenuItem.setAttribute("callback","url",ShareAddress_M_getCallbackpageurl())
			
			TxNode node1
        	TxNode.addMsg(node1,ShareAddress_M_getCallbackfunction()) 
        	MenuItem.setBean("callback", "callFunction", node1)
        	System.doAction("callback")
        	return FAIL
        endfunc	
        
        func getContactDisplayText()
        	TxNode node
			node=ShareAddress_M_getContact()
			string contacttext = ""
			if node !=NULL
				int count = TxNode.getChildSize(node)
				int i=0
				TxNode nodeContact
				if count == 0
					contacttext = getContactText(TxNode.childAt(node,0))
				else
					while count-1>i
						contacttext = contacttext + getContactText(TxNode.childAt(node,i)) + ", "
						i=i+1
					endwhile
					contacttext = contacttext + getContactText(TxNode.childAt(node,count-1))				
				endif
			endif
			
			if contacttext == NULL
				contacttext = ""
			endif
			return contacttext    
        endfunc

        func getContactList()
        	TxNode node
			node=ShareAddress_M_getContact()
			string contacttext = ""
			JSONArray ja
			if node !=NULL
				int count = TxNode.getChildSize(node)
				int i=0
				while count>i
					JSONArray.put(ja,getContactJSON(TxNode.childAt(node,i)))
					i=i+1
				endwhile
			endif
			contacttext = JSONArray.toString(ja)
			return contacttext    
        endfunc
        
        func getContactJSON(TxNode nodeContact)
        	JSONObject jo
        	JSONObject.put(jo,"name",TxNode.msgAt(nodeContact,0))
			JSONObject.put(jo,"ptn",TxNode.msgAt(nodeContact,1))
			
			return jo
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
        
        func CallBack_SelectContact()
        	TxNode node
			node=ParameterSet.getParam("contact")
			if node !=NULL
				ShareAddress_M_setContact(node)	
			endif
        endfunc   		

		func setCurrentLocation()
			TxNode stop
	        stop = ParameterSet.getParam("currentLocation")	
	        if NULL != stop
	         	JSONObject jo = convertStopToJSON(stop)	
				ShareAddress_M_setAddress(jo)
				doPreSendWithAjax()
			else
				System.showGeneralMsg(NULL,"<%=msg.get("selectaddress.gps.error")%>","OK",NULL,NULL,"CallBack_Popup")	
	        endif		
		endfunc
		
		func saveLabel()
		    TxNode labelNode = ParameterSet.getParam("label")
			string label = TxNode.msgAt(labelNode,0)
			ShareAddress_M_setLabel(label)
		endfunc
		
		func onClickSentTo()
			saveLabel()
			
			JSONObject joContact
        	JSONObject.put(joContact,"callbackpageurl","<%=getPageCallBack + "ShareAddress"%>")
        	JSONObject.put(joContact,"callbackfunction","CallBack_SelectContact")
        	JSONObject.put(joContact,"contactType","ShareAddress")
        	JSONObject.put(joContact,"type","2")
        	JSONObject.put(joContact,"title","<%=msg.get("selectcontact.title.shareaddress")%>")
        	TxNode contact = ShareAddress_M_getContact()
        	if contact == NULL
        		SelectContact_C_show(joContact)
        	else
				SelectContact_C_showWithDefault(joContact,contact)	
        	endif
			
			return FAIL
		endfunc

		]]>
	</tml:script>

	<tml:menuItem name="callback" pageURL="" trigger="TRACKBALL_CLICK">
			<tml:bean name="callFunction" valueType="string" value=""/>
	</tml:menuItem>
	<tml:menuItem name="addressSelect" onClick="chooseLocation" trigger="TRACKBALL_CLICK">
	</tml:menuItem>
	<tml:menuItem name="doSent" onClick="onClickDoSent" trigger="TRACKBALL_CLICK">
	</tml:menuItem>
	<tml:menuItem name="receipientMenu" onClick="onClickSentTo" trigger="TRACKBALL_CLICK">
	</tml:menuItem>
	
	<tml:menuItem name="doSentMenu" onClick="onClickDoSent" text="<%=msg.get("shareaddress.send")%>" trigger="KEY_MENU">
	</tml:menuItem>
	<tml:menuItem name="showSentAddresses" pageURL="<%=host + "/SentAddress.do?pageRegion=" + region%>" text="<%=msg.get("shareaddress.sentaddress")%>" trigger="KEY_MENU">
	</tml:menuItem>
	<tml:page id="ShareAddress" url="<%=getPage + "ShareAddress"%>" type="<%=pageType%>"  background="<%=imageUrl + "backgroud.png"%>" groupId="<%=GROUP_ID_MISC%>">
		<tml:menuRef name="doSentMenu" />
		<tml:menuSeperator />
		<tml:menuRef name="showSentAddresses" />
		<tml:title id="title" align="center|middle" fontWeight="bold|system_large"  fontColor="white">
			<%=msg.get("shareaddress.title")%>
		</tml:title>
		<tml:listBox id="addressListBox" isFocusable="true" hotKeyEnable="false">
			<tml:compositeListItem id="item0" visible="true" bgColor="#FFFFFF" isFocusable="true"
				transparent="false" focusBgImage="<%=imageUrl + "list_bg_highlight_40px.png"%>"
				blurBgImage="<%=imageUrl + "list_bg_40px.png"%>">
				<tml:label id="address" focusFontColor="white" fontWeight="bold|system_large" align="left|middle">
					<%=msg.get("shareaddress.address")%>
				</tml:label>
				<tml:label id="addressContent" focusFontColor="white" fontWeight="system_large" align="left|middle" textWrap="ellipsis">
					<%=msg.get("common.currentLocation")%>
				</tml:label>
				<tml:menuRef name="addressSelect" />
				<tml:menuRef name="doSentMenu" />
				<tml:menuSeperator />
				<tml:menuRef name="showSentAddresses" />
			</tml:compositeListItem>
		</tml:listBox>
		<tml:inputBox id="label" title="<%=msg.get("shareaddress.label")%>" titleAbove="false"
			titleFontWeight="bold|system_large" prompt="" fontWeight="system_large">
		</tml:inputBox>
		<tml:listBox id="sentToListBox" isFocusable="true" hotKeyEnable="false">
			<tml:compositeListItem id="sentToItem0" visible="true" bgColor="#FFFFFF" isFocusable="true"
				transparent="false" focusBgImage="<%=imageUrl + "list_bg_highlight_40px.png"%>"
				blurBgImage="<%=imageUrl + "list_bg_40px.png"%>">
				<tml:label id="sentToLabel" focusFontColor="white" fontWeight="bold|system_large" align="left|middle">
					<%=msg.get("shareaddress.sendto")%>
				</tml:label>
				<tml:label id="sentTo" focusFontColor="white" fontWeight="system_large" align="left|middle" textWrap="scroll">
				</tml:label>
				<tml:menuRef name="receipientMenu" />
				<tml:menuRef name="doSentMenu" />
				<tml:menuSeperator />
				<tml:menuRef name="showSentAddresses" />
			</tml:compositeListItem>
		</tml:listBox>
		<tml:button id="sentButton" text="<%=msg.get("shareaddress.send")%>"
			fontWeight="system_large"
			imageClick="<%=imageUrl + "button_small_on.png"%>"
			imageUnclick="<%=imageUrl + "button_small_off.png"%>">
			<tml:menuRef name="doSent" />
			<tml:menuRef name="doSentMenu" />
			<tml:menuSeperator />
			<tml:menuRef name="showSentAddresses" />
		</tml:button>
		<tml:label id="smsNotify" fontWeight="system_small" align="center">
				<%=msg.get("common.sms.prompt")%>
		</tml:label>
	</tml:page>
	<cserver:outputLayout/>		
</tml:TML>