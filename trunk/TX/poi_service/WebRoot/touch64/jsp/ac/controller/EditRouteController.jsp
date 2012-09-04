<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ include file="../../Header.jsp"%>

<%@page import="com.telenav.cserver.browser.framework.BrowserFrameworkConstants"%>
<%@page import="com.telenav.tnbrowser.util.DataHandler"%>	
<%
	 int avoidBitLength=3;
%>	

<tml:script language="fscript" version="1">
	<%@ include file="../model/EditRouteModel.jsp"%>

	<![CDATA[
		func EditRoute_C_StaticRoute(JSONObject address)
			JSONObject route
			if(address!=NULL)
				JSONObject.put(route,"dest",address)
			endif
			JSONObject style
			
			int defaultStyleId = getPreference(<%=PreferenceConstants.KEY_NAVIGATION_ROUTESTYLE%>)
			JSONObject.put(style,"index",defaultStyleId)
			String styleName=getStyle(defaultStyleId)
			JSONObject.put(style,"value",styleName)
			JSONObject.put(route,"style",style)
			int defaultAvoidId =getPreference(<%=PreferenceConstants.KEY_NAVIGATION_ROUTEAVOID%>)
			String avoidBits= convertToBitMap(defaultAvoidId, <%=avoidBitLength%>)
			String avoidNames=bitMaptoAvoidName(avoidBits)
			JSONObject avoid
			JSONObject.put(avoid,"index",defaultAvoidId)
			JSONObject.put(avoid,"value",avoidNames)
						
			JSONObject.put(route,"avoid",avoid)
			
			saveJSONRoute(route)
			System.doAction("editRoute")
		endfunc
		
		
		
		
		##########################################################################
		# Utility for Converting int to bit map
		##########################################################################
		func convertToBitMap(int value, int length)
			String bits =""
			int index=0
			int bit=0
			int res =0
			
			while(index<length)
				bit =value%2
				value = value/2
				bits = bit+bits
				index=index+1
			endwhile
			return bits
		endfunc
		
		
		func bitMaptoAvoidName(String bitMaps)
			String names=""
			if(String.at(bitMaps,2,1)=="1")
				names = names+"<%=msg.get("ac.avoid.hov")%>"+", "
			endif
		
			if(String.at(bitMaps,1,1)=="1")
				names = names+"<%=msg.get("ac.avoid.tolls")%>" +", "
			endif
			if(String.at(bitMaps,0,1)=="1")
				names = names+"<%=msg.get("ac.avoid.traffic")%>"
			endif
			int strLen = String.getLength(names)
			if(strLen>2)
				if String.at(names,strLen-2,2)==", "	
					names = String.at(names,0,strLen-2)
				endif
			endif
			return names
		endfunc
		
		func getPreference(int preferId)
			TxNode node = Preference.getPreferenceValue(preferId)
			if node!=NULL
				int style=TxNode.valueAt(node,0)
				return style
			endif
			return -1
		endfunc
		

		
		func onScriptException(String msg)
			println(msg)
		endfunc
		
		func EditRoute_C_saveBackAction(String backAction)
           EditRoute_M_saveBackAction(backAction)
        endfunc
        
        func EditRoute_C_getBackAction()
           return EditRoute_M_getBackAction()
        endfunc
        
        func EditRoute_C_deleteBackAction()
           EditRoute_M_deleteBackAction()
        endfunc
        
        func EditRoute_C_interfaceForThirdPart(JSONObject originJSONObject, JSONObject destJSONObject)
           TxNode originNode
           String originStr = JSONObject.toString(originJSONObject)
           TxNode.addMsg(originNode, originStr)
           
           TxNode callbackFuncNode
		   TxNode.addMsg(callbackFuncNode,"CallBack_SelectAddress_original")
           MenuItem.setBean("editRoute","callFunction",callbackFuncNode)
		   MenuItem.setBean("editRoute","returnAddress",originNode)
		   EditRoute_C_StaticRoute(destJSONObject)
        endfunc
	]]>
</tml:script>
<tml:menuItem name="editRoute" pageURL="<%=getPage + "EditRoute"%>"/>