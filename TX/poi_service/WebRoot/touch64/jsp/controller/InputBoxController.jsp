<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ include file="/touch64/jsp/model/InputBoxModel.jsp"%>
<tml:script language="fscript" version="1">
	<![CDATA[
	    func InputBox_C_show(JSONObject jo)
	    	InputBox_M_saveParameter(jo)
            System.doAction("inputbox")
            return FAIL
	    endfunc
	]]>
</tml:script>

<tml:menuItem name="inputbox"
	pageURL="<%=getPage + "InputBox"%>">
</tml:menuItem>