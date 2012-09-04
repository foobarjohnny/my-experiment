<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>	
<tml:script language="fscript" version="1">
	<![CDATA[
	    func SendUpdate_M_setParameter(JSONObject jo)
	        Cache.saveToTempCache("<%=Constant.StorageKey.SEND_UPDATE_PARAMETER%>",jo)
	    endfunc
	    
	    func SendUpdate_M_getParameter()
	        JSONObject joParameter = Cache.getJSONObjectFromTempCache("<%=Constant.StorageKey.SEND_UPDATE_PARAMETER%>")
	    	return joParameter
	    endfunc

	    func SendUpdate_M_clearParameter()
	        Cache.deleteFromTempCache("<%=Constant.StorageKey.SEND_UPDATE_PARAMETER%>")
	    endfunc
	    
	    func SendUpdate_M_getUserName()
	        JSONObject jo = SendUpdate_M_getParameter()
	    	return JSONObject.getString(jo,"username")
	    endfunc

	    func SendUpdate_M_getTime()
	        JSONObject jo = SendUpdate_M_getParameter()
	    	return JSONObject.getString(jo,"time")
	    endfunc

	    func SendUpdate_M_getLocation()
	        JSONObject jo = SendUpdate_M_getParameter()
	    	return JSONObject.get(jo,"location")
	    endfunc

	    func SendUpdate_M_setContact(string contact)
	    	JSONObject jo = SendUpdate_M_getParameter()
	    	JSONObject.put(jo,"contact",contact)
	    	SendUpdate_M_setParameter(jo)
	    endfunc
	    	    
	    func SendUpdate_M_getContact()
	    	JSONObject jo = SendUpdate_M_getParameter()
	    	return JSONObject.getString(jo,"contact")
	    endfunc
	    
	    func SendUpdate_M_saveContactNode(TxNode contactNode)
	        String saveKey = "<%=Constant.StorageKey.CONTACT_NODE_FOR_SENDUPDATE%>"
	        Cache.saveToTempCache(saveKey,contactNode)
	    endfunc
	    
	    func SendUpdate_M_getContactNode()
	        String saveKey = "<%=Constant.StorageKey.CONTACT_NODE_FOR_SENDUPDATE%>"
	        TxNode contactNode = Cache.getFromTempCache(saveKey)
	        return contactNode
	    endfunc
	]]>
</tml:script>
	