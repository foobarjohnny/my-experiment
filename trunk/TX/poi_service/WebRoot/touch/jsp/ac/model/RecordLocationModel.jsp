<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>	
<tml:script language="fscript" version="1">
	<![CDATA[
	    func RecordLocation_M_saveAddress(JSONObject jo)
	        Cache.saveToTempCache("<%=Constant.StorageKey.RECORD_LOCATION_ADDRESS%>",jo)
	    endfunc
	    
	    func RecordLocation_M_getAddress()
	        JSONObject jo = Cache.getJSONObjectFromTempCache("<%=Constant.StorageKey.RECORD_LOCATION_ADDRESS%>")
	    	return jo
	    endfunc	    
	]]>
</tml:script>
	