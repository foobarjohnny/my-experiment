<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%@page import="java.util.List"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@page import="com.telenav.cserver.pref.struts.util.PreferenceConstants"%>
<%@page import="com.telenav.cserver.util.TnUtil"%>
<%@ include file="../GetClientInfo.jsp"%>
<%@ include file="../Header.jsp"%>
<tml:TML outputMode="TxNode">
<%@include file="model/SelectContactModel.jsp"%>
<%@include file="../misc/controller/SendUpdateController.jsp"%>
<jsp:include page="../controller/NameAndEmailController.jsp"/>
	<tml:script language="fscript" version="1">
		<![CDATA[
		func onShow()
			if SelectContact_M_isRunning() == 1
        	endif
        	SelectContact_M_setRunning(1)
			string type = SelectContact_M_getType()
			if type != NULL && type == "1"

				System.doAction("doShowContactSelect")
				return FAIL
			else
				TxNode node
				#ShareAddress
				#ReferFriend
				TxNode.addMsg(node,SelectContact_M_getContactType())
				TxNode.addMsg(node,SelectContact_M_getTitle())
				TxNode contactNode = SelectContact_M_getContact()		
				if contactNode != NULL
					TxNode.addChild(node,contactNode) 
				endif
				MenuItem.setBean("doShowContact", "inputName", node)	
				System.doAction("doShowContact")
				return FAIL	
			endif
		endfunc
		
		func onClickShowContact()
			TxNode node
			node=ParameterSet.getParam("contact")
			if node != NULL			
			    string nextAction = TxNode.msgAt(node,0)
			    if nextAction == NULL
			    	nextAction = ""
			    endif
			    
			    if nextAction == "Back"
			    	backToPrevious()
			    else
			    	string callBackUrl = SelectContact_M_getCallbackpageurl()
			    	string callBackFunc = SelectContact_M_getCallbackfunction()
			    	string from = SelectContact_M_getFrom()
			    	SelectContact_M_setContact(node)
				    if from == "SendUpdate"
				       SendUpdate_C_saveContactNode(node)
				       TxNode firstNameNode = Preference.getPreferenceValue(<%=PreferenceConstants.KEY_GENERAL_FIRSTNAME%>)
				       
				       int sourceFlag = NameAndEmail_C_getDonotAskAgain("<%=Constant.sendUpdate%>")
				       int ATTMapFlag = Account.isTnMaps()
				       if NULL == firstNameNode  && sourceFlag == 0 && ATTMapFlag != 1
				          updateUserInfo()
				       else
				          String firstName = TxNode.msgAt(firstNameNode,0)
					      if "" == firstName  && sourceFlag == 0 && ATTMapFlag != 1
					         updateUserInfo()
					      else
					         MenuItem.setAttribute("callback","url",callBackUrl)
							 TxNode node1
				        	 TxNode.addMsg(node1,callBackFunc) 
				        	 MenuItem.setBean("callback", "callFunction", node1)
				        	 System.doAction("callback")
					      endif
				       endif
				    else
				       MenuItem.setAttribute("callback","url",callBackUrl)
					   TxNode node1
		        	   TxNode.addMsg(node1,callBackFunc) 
		        	   MenuItem.setBean("callback", "callFunction", node1)
		        	   MenuItem.setBean("callback", "contact", node)
		        	   System.doAction("callback")
				    endif			    
			    endif
	        else
				backToPrevious()
        	endif
		endfunc
		
		func backToPrevious()
		    System.back()
        	return FAIL	
		endfunc
		
		func updateUserInfo()
            String backUrl = "<%=getPageCallBack + "SendUpdate"%>"
	        String backFunc = SelectContact_M_getCallbackfunction()
	        NameAndEmail_C_initialAndGoToJsp(backUrl,backFunc,"<%=Constant.sendUpdate%>")
        endfunc
        
        func onResume()
        	onShow()
        	return FAIL
        endfunc
		]]>
	</tml:script>
	
	<tml:menuItem name="callback" pageURL="" trigger="TRACKBALL_CLICK">
			<tml:bean name="callFunction" valueType="string" value=""/>
	</tml:menuItem>	
	<tml:actionItem name="showContact" action="<%=Constant.LOCALSERVICE_CONTACT%>">
		<tml:input name="inputName" />
	    <tml:output name="contact" />
	</tml:actionItem>
	<tml:menuItem name="doShowContact" actionRef="showContact" onClick="onClickShowContact">
	</tml:menuItem>
	<tml:actionItem name="showContactSelect" action="<%=Constant.LOCALSERVICE_CONTACT_ADDRESS%>">
	    <tml:output name="contact" />
	</tml:actionItem>
	<tml:menuItem name="doShowContactSelect" actionRef="showContactSelect" onClick="onClickShowContact">
	</tml:menuItem>	
	<tml:page id="SelectContact" url="<%=getPage + "SelectContact"%>" type="<%=pageType%>"  groupId="<%=GROUP_ID_MISC%>">
	</tml:page>	
</tml:TML>
