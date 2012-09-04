<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<tml:script language="fscript" version="1">
	<![CDATA[
		func showATTExtras(int flag)
			if flag == 1
				 System.doAction("ATTExtrasForRIM")
			else
				System.doAction("ATTExtras")
			endif			
		endfunc
	]]>
</tml:script>

<tml:menuItem name="ATTExtras"
	pageURL="<%=getPage + "ATTExtras#ATTExtras"%>">
</tml:menuItem>

<tml:menuItem name="ATTExtrasForRIM"
	pageURL="<%=getPage + "ToolsMain"%>">
</tml:menuItem>