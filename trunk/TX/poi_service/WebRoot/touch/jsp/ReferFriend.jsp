<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ include file="Header.jsp"%>
<tml:TML outputMode="TxNode">
    <%@ include file="model/ReferFriendModel.jsp"%>
    <jsp:include page="controller/NameAndEmailController.jsp" />
    <tml:script language="fscript" version="1">
         <![CDATA[
             func CallBack_SelectContact()
                 TxNode contactNode = ParameterSet.getParam("contact")
                 if contactNode != NULL
                    ReferFriend_M_saveContactListNode(contactNode)
                    
                    TxNode firstNameNode = Preference.getPreferenceValue(<%=PreferenceConstants.KEY_GENERAL_FIRSTNAME%>)
			        int sourceFlag = NameAndEmail_C_getDonotAskAgain("<%=Constant.referFriend%>")
			        int ATTMapFlag = Account.isTnMaps()
			        if NULL == firstNameNode && sourceFlag == 0 && ATTMapFlag != 1
			           updateUserInfo()
			        else
			           String firstName = TxNode.msgAt(firstNameNode,0)
				       if "" == firstName && sourceFlag == 0 && ATTMapFlag != 1
				          updateUserInfo()
				       else
				          doReferFriend()
				       endif
			        endif
			     else
			        System.doAction("home")
	  			 endif
             endfunc
             
             func updateUserInfo()
		         String backUrl = "<%=getPageCallBack + "ReferFriend"%>"
		         String backFunc = "doReferFriend"
		         NameAndEmail_C_initialAndGoToJsp(backUrl,backFunc,"<%=Constant.referFriend%>")
		     endfunc
             
             func doReferFriend()
                 TxNode node
                 TxNode contactNode = ReferFriend_M_getContactListNode()
                 TxNode.addChild(node,contactNode)
                 TxRequest req
				 String url="<%=host + "/ReferFriend.do"%>"
				 String scriptName="referFriendCallback"
				 TxRequest.open(req,url)
				 TxRequest.setRequestData(req,node)
				 TxRequest.onStateChange(req,scriptName)
				 TxRequest.setProgressTitle(req,"<%=msg.get("common.refering")%>","cancelCallback", TRUE)
				 TxRequest.send(req)
             endfunc
             
             func cancelCallback()
                System.back()
				return FAIL	
             endfunc
         
             func referFriendCallback(TxNode node,int status)
		        if status == 0
				    System.showGeneralMsg(NULL,"<%=msg.get("common.internal.error")%>",NULL,NULL,3,"CallBack_Popup")
				    return FAIL
				elseif status == 1
				    System.showGeneralMsg(NULL,"<%=msg.get("common.refer.success.message")%>",NULL,NULL,3,"CallBack_Popup")
				    return FAIL
				endif
		     endfunc
		     
		     func CallBack_Popup(int param)
		        System.doAction("home")
		        return FAIL
		     endfunc
         ]]>
	</tml:script>
	<tml:menuItem name="home" pageURL="<%=host + "/startUp.do?pageRegion=" + region%>">
	</tml:menuItem>
	
    <tml:page id="ReferFriendPage" url="<%=getPage + "ReferFriend"%>"
		type="<%=pageType%>" showLeftArrow="true" showRightArrow="true" supportback="false"
		helpMsg="$//$refertnto" groupId="<%=GROUP_ID_COMMOM%>">
	</tml:page>

</tml:TML>
     
     
