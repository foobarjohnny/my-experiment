<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>	
<%@page import="com.telenav.cserver.util.FeatureConstant"%>

<tml:script language="fscript" version="1" feature="<%=FeatureConstant.SHARE_ADDRESS%>">
	<![CDATA[
	    func ShareAddress_M_saveParameter(JSONObject jo)
	        Cache.saveToTempCache("<%=Constant.StorageKey.SHARE_ADDRESS_PARAMETER%>",jo)
	    endfunc
	    
	    func ShareAddress_M_getParameter()
	        JSONObject joParameter = Cache.getJSONObjectFromTempCache("<%=Constant.StorageKey.SHARE_ADDRESS_PARAMETER%>")
	    	return joParameter
	    endfunc
	    
	     func ShareAddress_M_clearLabel()
	        JSONObject jo = ShareAddress_M_getParameter()
	        JSONObject.put(jo,"label","")
	        ShareAddress_M_saveParameter(jo)
	    endfunc

	    func ShareAddress_M_clear()
	        Cache.deleteFromTempCache("<%=Constant.StorageKey.SHARE_ADDRESS_PARAMETER%>")
	        Cache.deleteFromTempCache("<%=Constant.StorageKey.SHARE_ADDRESS_RECIPIENT%>")
	        Cache.deleteFromTempCache("<%=Constant.StorageKey.SHARE_ADDRESS_CONTACT%>")
	    endfunc
	    
	    func ShareAddress_M_getCallbackfunction()
	        JSONObject jo = ShareAddress_M_getParameter()
	    	return JSONObject.getString(jo,"callbackfunction")
	    endfunc

	    func ShareAddress_M_getCallbackpageurl()
	        JSONObject jo = ShareAddress_M_getParameter()
	    	return JSONObject.getString(jo,"callbackpageurl")
	    endfunc

	    func ShareAddress_M_getLabel()
	        JSONObject jo = ShareAddress_M_getParameter()
	    	return JSONObject.getString(jo,"label")
	    endfunc

	    func ShareAddress_M_setLabel(String label)
	        JSONObject jo = ShareAddress_M_getParameter()
	        JSONObject.put(jo,"label",label)
	    	ShareAddress_M_saveParameter(jo)
	    endfunc
	    	    
	    func ShareAddress_M_getRecipient()
	        TxNode node = Cache.getFromTempCache("<%=Constant.StorageKey.SHARE_ADDRESS_RECIPIENT%>")
	        return node
	    endfunc
	    
	    func ShareAddress_M_getRecipientSize()
	    	TxNode node = ShareAddress_M_getRecipient()
	    	int size = 0
			if node != NULL
				size = TxNode.getStringSize(node)
			endif
			return size
	    endfunc
	    
	    func ShareAddress_M_setRecipient(TxNode sentTo)
	        Cache.saveToTempCache("<%=Constant.StorageKey.SHARE_ADDRESS_RECIPIENT%>",sentTo)
	    endfunc

	    func ShareAddress_M_getContact()
	        TxNode node = Cache.getFromTempCache("<%=Constant.StorageKey.SHARE_ADDRESS_CONTACT%>")
	        return node
	    endfunc
	    
	    func ShareAddress_M_setContact(TxNode contact)
	        Cache.saveToTempCache("<%=Constant.StorageKey.SHARE_ADDRESS_CONTACT%>",contact)
	    endfunc
	    	    
	    func ShareAddress_M_getSendToText()
	        JSONObject jo = ShareAddress_M_getParameter()
	    	return JSONObject.getString(jo,"sentTo")
	    endfunc
	    
	    func ShareAddress_M_setSendToText(String sentTo)
	        JSONObject jo = ShareAddress_M_getParameter()
	        JSONObject.put(jo,"sentTo",sentTo)
	    	ShareAddress_M_saveParameter(jo)
	    endfunc

	    func ShareAddress_M_getContactNoOkFlag()
	        JSONObject jo = ShareAddress_M_getParameter()
	        string flag = JSONObject.getString(jo,"ContactNoOkFlag")
	        if flag == NULL
	        	flag = ""
	        endif
	    	return flag
	    endfunc

	    func ShareAddress_M_setContactNoOkFlag(String flag)
	        JSONObject jo = ShareAddress_M_getParameter()
	        JSONObject.put(jo,"ContactNoOkFlag",flag)
	    	ShareAddress_M_saveParameter(jo)
	    endfunc
	    	        
		func ShareAddress_M_getAddress()
	        JSONObject jo = ShareAddress_M_getParameter()
	    	return JSONObject.get(jo,"address")
	    endfunc

		func ShareAddress_M_setAddress(JSONObject joAddress)
	        JSONObject jo = ShareAddress_M_getParameter()
	        JSONObject.put(jo,"address",joAddress)
	    	ShareAddress_M_saveParameter(jo)
	    endfunc
	    
		func ShareAddress_M_getPoi()
	        JSONObject jo = ShareAddress_M_getParameter()
	    	return JSONObject.get(jo,"poi")
	    endfunc
	    
		func ShareAddress_M_setPoi(JSONObject joPoi)
	        JSONObject jo = ShareAddress_M_getParameter()
	    	JSONObject.put(jo,"poi",joPoi)
	    endfunc	
	    
		func ShareAddress_M_clearPoi()
			JSONObject jo = ShareAddress_M_getParameter()
			JSONObject.put(jo,"poi",NULL)
			ShareAddress_M_saveParameter(jo)
	    endfunc	        	    
	]]>
</tml:script>
	