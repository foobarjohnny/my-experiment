<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%@ include file="../Header.jsp"%>	
<%@page import="com.telenav.tnbrowser.util.DataHandler"%>
<%@page import="com.telenav.cserver.poi.struts.util.PoiUtil"%>
<%@page import="com.telenav.cserver.dsr.struts.util.AudioConstants"%>
<% 
	DataHandler handler = (DataHandler)request.getAttribute("DataHandler");
%>
<tml:TML outputMode="TxNode">
	<%@ include file="/touch/jsp/ac/controller/AddressCaptureController.jsp"%>
	<%@ include file="Search1Common.jsp"%>
	<tml:script language="fscript" version="1">
	<![CDATA[
		func loadSpecific()
			DSRCommon_M_saveDSRType("<%=AudioConstants.DSR_RECOGNIZE_ONE_SHOT_ADDRESS%>")
		endfunc
		
		func onClickTypeIn()
			releaseResource()
			JSONObject jo = Cache.getJSONObjectFromTempCache("<%=AudioConstants.StorageKey.AUDIO_FROM_PARAMEPTER%>")
			
			JSONObject joDefaultAddress = DSRCommon_M_getDefaultAddress()
			if joDefaultAddress != NULL
				JSONObject.put(jo,"address",joDefaultAddress)
			endif
			
			AddressCapture_C_address(jo)
			return FAIL
		endfunc
	]]>
	</tml:script>
	<tml:menuItem name="typeInMenu" onClick="onClickTypeOnVolume" trigger="TRACKBALL_CLICK"/>
			
	<tml:page id="SpeakAddress" url="<%=getDsrPage + "SpeakAddress1"%>" type="<%=pageType%>" background="" 
		 genericMenu="4" helpMsg="$//$dsraddres" groupId="<%=GROUP_ID_DSR%>" supportback="false" >
		<%
			handler.toXML(out);
		%>
		<tml:image	id="shadowBg" url="" align="left|top"/>
		<tml:image	id="controlBg" url="" align="left|top"/>	
		
		<tml:label id="label1" fontWeight="bold" align="center">
			<%=msg.get("dsr.address.prompt")%>
		</tml:label>

		<tml:label id="label2" fontWeight="system_large" align="center" textWrap="wrap">
			<%=PoiUtil.amend(msg.get("dsr.example") + msg.get("dsr.address.example"))%>
		</tml:label>
		<tml:audioVolume id="imageSpeaker" align="center"/>
		<tml:button id="typeInButton" text="<%=msg.get("dsr.finish")%>"
			fontWeight="system_median" isFocusable="true" visible="0"
			imageClick=""
			imageUnclick="">
			<tml:menuRef name="typeInMenu" />
		</tml:button>	
	</tml:page>
	<cserver:outputLayout/>
</tml:TML>