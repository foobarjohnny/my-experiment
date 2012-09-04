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
	String pageURL = getDsrPage + "SpeakCity1";
%>
<tml:TML outputMode="TxNode">
	<%@ include file="/WEB-INF/jsp/ac/controller/AddressCaptureController.jsp"%>
	<%@ include file="Search1Common.jsp"%>
	<tml:script language="fscript" version="1">
	<![CDATA[
		func loadSpecific()
			DSRCommon_M_saveDSRType("<%=AudioConstants.DSR_RECOGNIZE_CITY_STATE%>")
		endfunc
		
		func onClickTypeIn()
			releaseResource()
			JSONObject jo = Cache.getJSONObjectFromTempCache("<%=AudioConstants.StorageKey.AUDIO_FROM_PARAMEPTER%>")
			JSONObject joDefaultAddress = DSRCommon_M_getDefaultAddress()
			if joDefaultAddress != NULL
				JSONObject.put(jo,"address",joDefaultAddress)
			endif			
			AddressCapture_C_city(jo)
			return FAIL
		endfunc
	]]>
	</tml:script>	
	<tml:menuItem name="typeInMenu" onClick="onClickTypeIn" trigger="TRACKBALL_CLICK"/>
			
	<tml:page id="SpeakCity" url="<%=pageURL%>" type="<%=pageType%>" background="<%=imageUrl + "backgroud_no_title.png"%>" 
		 genericMenu="4" helpMsg="$//$dsrcitystate" groupId="<%=GROUP_ID_DSR%>" supportback="false" >
		<%
			handler.toXML(out);
		%>
		<tml:label id="label1" align="center" fontWeight="bold">
			<%=msg.get("dsr.city.prompt")%>
		</tml:label>

		<tml:label id="label2" fontWeight="system_large" align="center" textWrap="wrap">
			<%=PoiUtil.amend(msg.get("dsr.example") + msg.get("dsr.city.example"))%>
		</tml:label>
		<tml:audioVolume id="imageSpeaker" align="center"/>
		<tml:button id="typeInButton" text="<%=msg.get("dsr.city.button.typein")%>"
			fontWeight="system_large" isFocusable="true" visible="0"
			imageClick="<%=imageUrl + "button_large_on.png"%>"
			imageUnclick="<%=imageUrl + "button_large_off.png"%>">
			<tml:menuRef name="typeInMenu" />
		</tml:button>
	</tml:page>
	<cserver:outputLayout/>
</tml:TML>