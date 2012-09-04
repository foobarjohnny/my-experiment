<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ include file="/touch64/jsp/Header.jsp"%>

<tml:script language="fscript" version="1">

	<![CDATA[
			func DriveToWrap_C_driveTo(JSONObject address)
				TxNode addressNode
				TxNode.addMsg(addressNode,JSONObject.toString(address))
				MenuItem.setBean("DriveToWrap_C_driveto","returnAddress",addressNode)
				
				System.doAction("DriveToWrap_C_driveto")
			endfunc
		]]>
</tml:script>

<tml:menuItem name="DriveToWrap_C_driveto"
	pageURL="<%=getPage + "DriveToWrap"%>">
	<tml:bean name="callFunction" valueType="String"
		value="CallBack_SelectAddress" />
</tml:menuItem>
