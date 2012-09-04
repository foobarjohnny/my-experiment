<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<tml:script language="fscript" version="1">
	<![CDATA[
	    func InputBox_M_saveParameter(JSONObject jo)
	        Cache.saveToTempCache("COMMON_INPUTBOX",jo)
	    endfunc 
	    
	    func InputBox_M_getParameter()
	        JSONObject jo = Cache.getJSONObjectFromTempCache("COMMON_INPUTBOX")
	        if jo==NULL
	        	JSONObject emptyJo
	        	DSR_M_saveParameter(emptyJo)
	        	return emptyJo
	        else
	        	return jo		        	
	        endif
	    endfunc	    

	    func InputBox_M_getCallbackfunction()
	        JSONObject jo = InputBox_M_getParameter()
	    	return checkNULL(JSONObject.getString(jo,"callbackfunction"))
    	endfunc

	    func InputBox_M_getCallbackpageurl()
	        JSONObject jo = InputBox_M_getParameter()
	    	return checkNULL(JSONObject.getString(jo,"callbackpageurl"))
	    endfunc
	    
	    func InputBox_M_saveComments(String comments)
			JSONObject jo = InputBox_M_getParameter()
			JSONObject.put(jo,"comments",comments)
		endfunc
		
	    func InputBox_M_getComments()
	        JSONObject jo = InputBox_M_getParameter()
	    	return checkNULL(JSONObject.getString(jo,"comments"))
	    endfunc 

	    func InputBox_M_getTitle()
	        JSONObject jo = InputBox_M_getParameter()
	    	return checkNULL(JSONObject.getString(jo,"title"))
	    endfunc 
	    		
	    func checkNULL(string s)
	    	if s == NULL
	    		return ""
	    	else
	    		return s
	    	endif	
	    endfunc
	]]>
</tml:script>

