<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<tml:script language="fscript" version="1">
	<%@ include file="StopUtil.jsp"%>
	<![CDATA[
	   func PoiUtil_convertToNodeForResentSearch(JSONObject jo)
            return convertToPoi(jo)
        endfunc
	]]>
</tml:script>