<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>	
<tml:script language="fscript" version="1">
<%@include file="/touch62/jsp/StopUtil.jsp"%>
	<![CDATA[
	    func SetUpHome_M_setParameter(JSONObject jo)
	        Cache.saveToTempCache("<%=Constant.StorageKey.ADDRESS_HOME_PARAMETER%>",jo)
	    endfunc
	    
	    func SetUpHome_M_getParameter()
	        JSONObject joParameter = Cache.getJSONObjectFromTempCache("<%=Constant.StorageKey.ADDRESS_HOME_PARAMETER%>")
	    	return joParameter
	    endfunc

	    func SetUpHome_M_clearParameter()
	        Cache.deleteFromTempCache("<%=Constant.StorageKey.ADDRESS_HOME_PARAMETER%>")
	    endfunc
	    
	    func SetUpHome_M_getCallbackfunction()
	        JSONObject jo = SetUpHome_M_getParameter()
	    	return JSONObject.getString(jo,"callbackfunction")
	    endfunc

	    func SetUpHome_M_getCallbackpageurl()
	        JSONObject jo = SetUpHome_M_getParameter()
	    	return JSONObject.getString(jo,"callbackpageurl")
	    endfunc
	    
	    func SetUpHome_M_syncHomeToClient()
	        TxNode node
	        TxNode.addValue(node,SetUpHome_M_getHomeLabelIndex()) 
	        TxNode.addMsg(node,SetUpHome_M_getHomeLabel())
	        TxNode stop = convertToStop(SetUpHome_M_getHome())
	        TxNode.addChild(node,stop)
	        AddressManager.setHomeAndOffice(node)
	    endfunc
	    
	    func SetUpHome_M_syncHomeFromClient()
	        TxNode node = AddressManager.getHomeAndOffice()
	        if TxNode.getChildSize(node) > 0
	        	TxNode stop = TxNode.childAt(node,0)
	        	SetUpHome_M_setHome(convertStopToJSON(stop))
	        endif
	    endfunc
	    	    
	    func SetUpHome_M_getHome()
	    	return Cache.getJSONObjectFromCookie("<%=Constant.StorageKey.ADDRESS_HOME_DATA%>")
	    endfunc

	    func SetUpHome_M_setHome(JSONObject jo)
	    	JSONObject.put(jo,"label",SetUpHome_M_getHomeLabel())
	        Cache.saveCookie("<%=Constant.StorageKey.ADDRESS_HOME_DATA%>",jo)
	    endfunc
	    
	    func SetUpHome_M_clearHome()
	        TxNode node
	        TxNode.addValue(node,2)
	        AddressManager.setHomeAndOffice(node)
	        Cache.deleteCookie("<%=Constant.StorageKey.ADDRESS_HOME_DATA%>") 
	    endfunc
	    
	    func SetUpHome_M_getHomeLabel()
			 String selectaddressText = System.parseI18n("<%=msg.get("selectaddress.home")%>")
			 return selectaddressText  
	    endfunc
 
	    func SetUpHome_M_getHomeLabelIndex()
			return 0
	    endfunc
	    	    
	    func SetUpHome_M_isHome()
	    	int type = SetUpHome_M_getHomeLabelIndex()
	    	if type == 0
	    		return TRUE
	    	else
	    		return FALSE
	    	endif
	    endfunc
	    
	    func SetUpHome_M_setCallBackFlag(string flag)
	        TxNode node
	        TxNode.addMsg(node,flag)
	        Cache.saveToTempCache("ADDRESS_HOME_CALLBACK_FLAG",node)
	    endfunc
	    
	    func SetUpHome_M_getCallBackFlag()
	        TxNode node = Cache.getFromTempCache("ADDRESS_HOME_CALLBACK_FLAG")
			if node == NULL
				return ""
			else
				return TxNode.msgAt(node,0)  
			endif
	    endfunc
	    	    
		func SetUpHome_M_saveAddressListCallBack(String invokerPageURL,String callbackFunction)
			JSONObject joTemp
			JSONObject joTemp1 = Cache.getJSONObjectFromTempCache("<%=Constant.StorageKey.ADDRESS_CAPTURE_PARAMETER%>")
			if joTemp1 != NULL
				joTemp = joTemp1
			endif
			
			string from = Page.getControlProperty("page","url_flag")
			JSONObject jo
			JSONObject.put(jo,"callbackfunction",callbackFunction)
			JSONObject.put(jo,"callbackpageurl",invokerPageURL)
	    	JSONObject.put(joTemp,"Home",jo)
	    	
	        Cache.saveToTempCache("<%=Constant.StorageKey.ADDRESS_CAPTURE_PARAMETER%>",joTemp)
		endfunc
		
	    func SetUpHome_M_getEditFlag()
	        JSONObject jo = SetUpHome_M_getParameter()
	        string flag = JSONObject.getString(jo,"edit")
	        if flag == NULL
	        	flag = ""
	        endif
	    	return flag
	    endfunc		

		func SetUpHome_M_fromHome(int value)
			TxNode node
			TxNode.addValue(node,value)
			Cache.saveToTempCache("SetUpHome_M_fromHome",node)
		endfunc
		
		func SetUpHome_M_isFromHome()
			TxNode node = Cache.getFromTempCache("SetUpHome_M_fromHome")
			int home = 1
			if node != NULL
				home = TxNode.valueAt(node,0) 
			endif
			return home 
		endfunc		
	]]>
</tml:script>
	