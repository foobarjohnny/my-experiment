<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@page import="com.telenav.cserver.util.TnConstants"%>
<tml:script language="fscript" version="1">
<![CDATA[
	func Weather_M_getWeatherList()
		JSONArray ja= Cache.getJSONArrayFromTempCache("<%=TnConstants.StorageKey.WEATHER_DATA_LIST%>")
		return ja
	endfunc
	
	func Weather_M_setWeatherList(JSONArray ja)
		Cache.saveToTempCache("<%=TnConstants.StorageKey.WEATHER_DATA_LIST%>",ja)
	endfunc

	func Weather_M_getWeatherCurrent()
		JSONObject jo= Cache.getJSONObjectFromTempCache("<%=TnConstants.StorageKey.WEATHER_DATA_CURRENT%>")
		return jo
	endfunc
	
	func Weather_M_setWeatherCurrent(JSONObject jo)
		Cache.saveToTempCache("<%=TnConstants.StorageKey.WEATHER_DATA_CURRENT%>",jo)
	endfunc
			
	func Weather_M_getIndex()
		TxNode indexNode = Cache.getFromTempCache("<%=TnConstants.StorageKey.COMMON_DATA_INDEX%>")
		int indexClicked = TxNode.valueAt(indexNode,0)
		return indexClicked
	endfunc
	
	func Weather_M_setIndex(TxNode node)
		Cache.saveToTempCache("<%=TnConstants.StorageKey.COMMON_DATA_INDEX%>",node)
	endfunc
	
	func Weather_M_getSize()
		JSONArray ja = Weather_M_getWeatherList()
		int size =  JSONArray.length(ja)
		return size
	endfunc	
	
	func Weather_M_getLocation()
		JSONObject jo= Cache.getJSONObjectFromTempCache("<%=TnConstants.StorageKey.WEATHER_DATA_LOCATION%>")
		return jo
	endfunc
	
	func Weather_M_setLocation(JSONObject jo)
		Cache.saveToTempCache("<%=TnConstants.StorageKey.WEATHER_DATA_LOCATION%>",jo)
	endfunc
	
	func Weather_M_saveBackAction(String backAction)
        TxNode backActionNode
        TxNode.addMsg(backActionNode,backAction)
        String saveKey = "<%=TnConstants.StorageKey.BACK_ACTION_WEATHER%>"
        Cache.saveToTempCache(saveKey,backActionNode)
    endfunc
     
    func Weather_M_getBackAction()
        String backAction = ""
        String saveKey = "<%=TnConstants.StorageKey.BACK_ACTION_WEATHER%>"
        TxNode backActionNode = Cache.getFromTempCache(saveKey)
        if NULL != backActionNode
           backAction = TxNode.msgAt(backActionNode,0)
        endif
        return backAction
    endfunc
     
    func Weather_M_deleteBackAction()
        String saveKey = "<%=TnConstants.StorageKey.BACK_ACTION_WEATHER%>"
        Cache.deleteFromTempCache(saveKey)
    endfunc

    func Weather_M_getCallbackpageurl()
		JSONObject jo = Weather_M_getParameter()
	    return JSONObject.getString(jo,"callbackpageurl")
    endfunc

    func Weather_M_getForwardpageurl()
		JSONObject jo = Weather_M_getParameter()
	    return JSONObject.getString(jo,"forwardpageurl")
    endfunc
        
    func Weather_M_saveParameter(JSONObject jo)
        Cache.saveToTempCache("<%=TnConstants.StorageKey.WEATHER_PARAMETER%>",jo)
    endfunc
    
    func Weather_M_getParameter()
        JSONObject joParameter = Cache.getJSONObjectFromTempCache("<%=TnConstants.StorageKey.WEATHER_PARAMETER%>")
    	return joParameter
    endfunc
]]>
</tml:script>


