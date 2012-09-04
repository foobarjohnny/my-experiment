<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>	
<tml:script language="fscript" version="1">
	<![CDATA[
	    func SelectContact_M_saveParameter(JSONObject jo)
	    	string from = JSONObject.getString(jo,"fromPage")
	    	JSONObject joTemp
	    	JSONObject joTemp1 = Cache.getJSONObjectFromTempCache("<%=Constant.StorageKey.SELECT_CONTACT_PARAMETER%>")
			if joTemp1 != NULL
				joTemp = joTemp1
			endif
	    	JSONObject.put(joTemp,from,jo)
	    	
	        Cache.saveToTempCache("<%=Constant.StorageKey.SELECT_CONTACT_PARAMETER%>",joTemp)
	        SelectContact_M_setRunning(0)
	    endfunc

	    func SelectContact_M_getFromPage()
	        string from = Page.getControlProperty("page","url_flag")
			if from == NULL
				from = "contact"
			endif
	    	return from
	    endfunc
	    	    
	    func SelectContact_M_getParameter()
	    	JSONObject joTemp = Cache.getJSONObjectFromTempCache("<%=Constant.StorageKey.SELECT_CONTACT_PARAMETER%>")
	        JSONObject jo = JSONObject.get(joTemp,SelectContact_M_getFromPage())
	    	return jo
	    endfunc

	    func SelectContact_M_clear()
	        Cache.deleteFromTempCache("<%=Constant.StorageKey.SHARE_SELECT_CONTACT_DEFAULT%>")
	    endfunc
	    
	    func SelectContact_M_getCallbackfunction()
	        JSONObject jo = SelectContact_M_getParameter()
	    	return JSONObject.getString(jo,"callbackfunction")
	    endfunc

	    func SelectContact_M_getCallbackpageurl()
	        JSONObject jo = SelectContact_M_getParameter()
	    	return JSONObject.getString(jo,"callbackpageurl")
	    endfunc    

	    func SelectContact_M_getType()
	        JSONObject jo = SelectContact_M_getParameter()
	    	return JSONObject.getString(jo,"type")
	    endfunc

	    func SelectContact_M_getFrom()
	        JSONObject jo = SelectContact_M_getParameter()
	        string from =  JSONObject.getString(jo,"from")
	        if from == NULL
	        	from = ""
	        endif
	    	return from
	    endfunc

	    func SelectContact_M_getContactType()
	        JSONObject jo = SelectContact_M_getParameter()
	        return JSONObject.getString(jo,"contactType")
	    endfunc

	    func SelectContact_M_getTitle()
	        JSONObject jo = SelectContact_M_getParameter()
	        return JSONObject.getString(jo,"title")
	    endfunc
	    	    
		func SelectContact_M_setFromContactFlag(int flag)
			TxNode node
		    TxNode.addValue(node,flag)
		    Cache.saveToTempCache("SELECTCONTACT_FROMCONTACT",node)
		endfunc

	    func SelectContact_M_getFromContactFlag()
	    	int doing = 0
	        TxNode node = Cache.getFromTempCache("SELECTCONTACT_FROMCONTACT")
	        if NULL != node
	            doing = TxNode.valueAt(node,0)
	        endif
	        return doing
	    endfunc
	    	    	    	    
	    func SelectContact_M_getContact()
	        TxNode node = Cache.getFromTempCache("<%=Constant.StorageKey.SHARE_SELECT_CONTACT_DEFAULT%>")
			return node
	    endfunc
	    
	    func SelectContact_M_setContact(TxNode contact)
	        Cache.saveToTempCache("<%=Constant.StorageKey.SHARE_SELECT_CONTACT_DEFAULT%>",contact)
	    endfunc

		func SelectContact_M_isRunning()
			int doing = 0
	        TxNode node = Cache.getFromTempCache("<%=Constant.StorageKey.SELECTCONTACT_RUNNING%>")
	        if NULL != node
	            doing = TxNode.valueAt(node,0)
	        endif
	        return doing
		endfunc
		
		func SelectContact_M_setRunning(int showing)
			TxNode node
		    TxNode.addValue(node,showing)
		    Cache.saveToTempCache("<%=Constant.StorageKey.SELECTCONTACT_RUNNING%>",node)
		endfunc
	]]>
</tml:script>
	