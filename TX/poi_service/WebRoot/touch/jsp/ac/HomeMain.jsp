<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%@ include file="../Header.jsp"%>
<%@page import="java.util.List"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<% 
	String pageURL = getPage + "HomeMain";
%>
<tml:TML outputMode="TxNode">
<%@ include file="controller/SetUpHomeController.jsp"%>
	<tml:script language="fscript" version="1">
		<![CDATA[
		func onLoad()
			string callbackFlag = SetUpHome_M_getCallBackFlag()
			if callbackFlag != "Y"
				JSONObject jo = SetUpHome_M_getHome()
				string editFlag = SetUpHome_M_getEditFlag()
				if jo == NULL || editFlag == "Y"
					SetUpHome_C_showHowAddress()	
				else
					invokeCallBack()
				endif
			else
				SetUpHome_M_setCallBackFlag("")	
			endif
		endfunc
		
		func CallBack_SelectAddress()
			TxNode node
        	node = ParameterSet.getParam("returnAddress")
        	if NULL != node
        		JSONObject jo = JSONObject.fromString(TxNode.msgAt(node,0))
        		SetUpHome_M_setHome(jo)
        		SetUpHome_M_setCallBackFlag("Y")
        		SetUpHome_M_syncHomeToClient()
        		string msg = "<%=msg.get("home.success.msg")%>"
        		System.showGeneralMsg(NULL,msg,NULL,NULL,3,"CallBack_Popup")
        	endif
		endfunc
		
		func CallBack_Popup(int param)
			if param == 2
				invokeCallBack()
			else
        		System.back()
        		return FAIL
			endif
		endfunc
		
		func invokeCallBack()
			string callbackurl = SetUpHome_M_getCallbackpageurl()

 			MenuItem.setAttribute("callback","url",SetUpHome_M_getCallbackpageurl())
			TxNode node1
        	TxNode.addMsg(node1,SetUpHome_M_getCallbackfunction()) 
        	MenuItem.setBean("callback", "callFunction", node1)
        	
        	JSONObject jo = SetUpHome_M_getHome()
        	RecentPlace_saveAddress(jo)
        	TxNode node
        	TxNode.addMsg(node,JSONObject.toString(jo))
        	MenuItem.setBean("callback", "returnAddress", node)
        	
        	System.doAction("callback")
        	return FAIL
		endfunc

        func onResume()
        	System.back()
        	return FAIL
        endfunc 

		]]>
	</tml:script>
	<tml:menuItem name="callback" pageURL="" trigger="TRACKBALL_CLICK">
			<tml:bean name="callFunction" valueType="string" value=""/>
	</tml:menuItem>	
	<tml:page id="HomeMain" url="<%=pageURL%>" type="<%=pageType%>" supportback="false" groupId="<%=GROUP_ID_AC%>">
	</tml:page>
</tml:TML>