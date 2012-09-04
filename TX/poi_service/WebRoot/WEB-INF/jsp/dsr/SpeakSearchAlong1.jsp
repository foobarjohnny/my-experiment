<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%@ include file="../Header.jsp"%>	
<%@page import="com.telenav.tnbrowser.util.DataHandler"%>
<%@page import="com.telenav.cserver.dsr.struts.util.AudioConstants"%>
<%@page import="com.telenav.cserver.poi.struts.util.PoiUtil"%>
<% 
	DataHandler handler = (DataHandler)request.getAttribute("DataHandler");
%>
<tml:TML outputMode="TxNode">
	<%@ include file="Search1Common.jsp"%>
	<tml:script language="fscript" version="1">
	<![CDATA[
		func loadSpecific()
			DSRCommon_M_saveDSRType("<%=AudioConstants.DSR_RECOGNIZE_SEARCHALONG%>")
			DSR.setAddress(DSR_M_getAheadAddress())
		endfunc
		
		func onClickTypeIn()
			releaseResource()
			SearchPoi_C_searchAlongInitial(DSR_M_getSearchAlongData())
			return FAIL
		endfunc
	]]>
	</tml:script>

	<tml:menuItem name="typeInMenu" onClick="onClickTypeIn" trigger="TRACKBALL_CLICK"/>
	<tml:menuItem name="search2" pageURL=""/>
			
	<tml:page id="SpeakSearchAlong1" url="<%=getDsrPage + "SpeakSearchAlong1"%>" type="<%=pageType%>" background="<%=imageUrl + "backgroud_no_title.png"%>"  
		 genericMenu="4" helpMsg="$//$dsrbusiness" groupId="<%=GROUP_ID_DSR%>" supportback="false" >
		<%
			handler.toXML(out);
		%>
		<tml:label id="label1" align="center" fontWeight="bold" textWrap="wrap">
			<%=msg.get("dsr.search.prompt")%>
		</tml:label>

		<tml:label id="label2" fontWeight="system_large" align="center" textWrap="wrap">
			<%=PoiUtil.amend(msg.get("dsr.example") + msg.get("dsr.searchalong.example"))%>	
		</tml:label>
		<tml:audioVolume id="imageSpeaker" align="center"/>
		
		<tml:button id="typeInButton" text="<%=msg.get("dsr.search.button.typein")%>"
			fontWeight="system_large" isFocusable="true" visible="0"
			imageClick="<%=imageUrl + "button_large_on.png"%>"
			imageUnclick="<%=imageUrl + "button_large_off.png"%>">
			<tml:menuRef name="typeInMenu" />
		</tml:button>
	</tml:page>
	<cserver:outputLayout/>
</tml:TML>