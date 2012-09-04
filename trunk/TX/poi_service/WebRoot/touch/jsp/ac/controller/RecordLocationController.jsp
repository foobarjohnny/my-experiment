<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@include file="/touch/jsp/ac/model/RecordLocationModel.jsp"%>	

<tml:script language="fscript" version="1">
	<![CDATA[
        func RecordLocation_C_show(JSONObject jo)
        	RecordLocation_M_saveAddress(jo)
            System.doAction("recordLocationActionMenu")
            return FAIL
        endfunc
	]]>
</tml:script>
<tml:menuItem name="recordLocationActionMenu" pageURL="<%=getPage + "RecordLocation"%>" trigger="TRACKBALL_CLICK" />