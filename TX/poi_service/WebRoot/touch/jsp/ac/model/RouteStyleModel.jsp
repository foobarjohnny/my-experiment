<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>	
<tml:script language="fscript" version="1">
	<![CDATA[
	  	func getRouteStyle()
        	String saveKey="ATT_MAPS_ROUTE_STYLE"
        	return Cache.getJSONObjectFromTempCache(saveKey)
        endfunc
		
		func saveRouteStyle(JSONObject jo)
			Cache.saveToTempCache("ATT_MAPS_ROUTE_STYLE",jo)
		endfunc
	]]>
</tml:script>
	