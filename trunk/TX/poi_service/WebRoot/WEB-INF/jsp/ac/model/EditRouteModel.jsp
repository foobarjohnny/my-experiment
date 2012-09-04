<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.cserver.pref.struts.util.PreferenceConstants" %>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<tml:script language="fscript" version="1">
	<![CDATA[
	   func getJSONRoute()
        	String saveKey="<%=Constant.StorageKey.ATT_MAPS_STATIC_ROUTE%>"
        	return Cache.getJSONObjectFromTempCache(saveKey)
        endfunc
		
		func saveJSONRoute(JSONObject jo)
			Cache.saveToTempCache("<%=Constant.StorageKey.ATT_MAPS_STATIC_ROUTE%>",jo)
		endfunc
		
		

		##########################################################################
		#Get Style name from style id
		##########################################################################
	
		func getStyle(int id)
			String styleName="<%=msg.get("ac.pref.fastest")%>"
			if(id==<%=PreferenceConstants.VALUE_ROUTESTYLE_SHORTEST%>)
				styleName="<%=msg.get("ac.pref.shortest")%>"
			elsif(id==<%=PreferenceConstants.VALUE_ROUTESTYLE_STREET%>)
				styleName="<%=msg.get("ac.pref.streets")%>"
			elsif(id==<%=PreferenceConstants.VALUE_ROUTESTYLE_HIGHWAY%>)
				styleName="<%=msg.get("ac.pref.highways")%>"
			elsif(id==<%=PreferenceConstants.VALUE_ROUTESTYLE_PEDESTRIAN%>)
				styleName="<%=msg.get("ac.pref.pedestrian")%>"
			endif
			return styleName
		endfunc
		
		func EditRoute_M_saveBackAction(String backAction)
           TxNode backActionNode
           TxNode.addMsg(backActionNode,backAction)
           String saveKey = "<%=Constant.StorageKey.BACK_ACTION_EDITROUT%>"
		   Cache.saveToTempCache(saveKey,backActionNode)
        endfunc
        
        func EditRoute_M_getBackAction()
           String backAction = ""
           String saveKey = "<%=Constant.StorageKey.BACK_ACTION_EDITROUT%>"
           TxNode backActionNode = Cache.getFromTempCache(saveKey)
           if NULL != backActionNode
              backAction = TxNode.msgAt(backActionNode,0)
           endif
           return backAction
        endfunc
        
        func EditRoute_M_deleteBackAction()
           String saveKey = "<%=Constant.StorageKey.BACK_ACTION_EDITROUT%>"
           Cache.deleteFromTempCache(saveKey)
        endfunc
	]]>
</tml:script>
