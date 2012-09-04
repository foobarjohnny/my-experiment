<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<tml:script language="fscript" version="1">
	<![CDATA[
		func showATTExtras()
			System.doAction("ATTExtras")
		endfunc
	]]>
</tml:script>

<tml:menuItem name="ATTExtras"
	pageURL="<%=getPage + "ATTExtras#ATTExtras"%>">
</tml:menuItem>