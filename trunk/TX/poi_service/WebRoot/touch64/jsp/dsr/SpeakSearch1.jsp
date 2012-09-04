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

	String product = handlerGloble.getClientInfo(DataHandler.KEY_PRODUCTTYPE);
	String helpMsg = "$//$dsrbusiness";
	if(TnUtil.isTMOUser(product)){
		helpMsg = "$//$dsrsearch";
	}
%>
<tml:TML outputMode="TxNode">
	<%@ include file="Search1Common.jsp"%>
	<tml:script language="fscript" version="1">
	<![CDATA[
		func loadSpecific()
			DSRCommon_M_saveDSRType("<%=AudioConstants.DSR_RECOGNIZE_POI%>")
		endfunc
		
		func onClickTypeIn()
			releaseResource()
			int poiSearchType = DSR_M_getPoiSearchType()
			if poiSearchType == 100
				SearchPoi_C_searchForBusinessInitial(DSR_M_getCallbackpageurl(),DSR_M_getCallbackfunction())
			else
				SearchPoi_C_initial(5)
				SearchPoi_C_showSearch()
			endif
			return FAIL
		endfunc
	]]>
	</tml:script>

	<tml:menuItem name="typeInMenu" onClick="onClickTypeOnVolume" trigger="TRACKBALL_CLICK"/>
	<tml:menuItem name="search2" pageURL=""/>
			
	<tml:page id="SpeakSearch" url="<%=getDsrPage + "SpeakSearch1"%>" type="<%=pageType%>" background=""  
		 genericMenu="4" helpMsg="<%=helpMsg%>" groupId="<%=GROUP_ID_DSR%>" supportback="false" >
		<%
			handler.toXML(out);
		%>
		<tml:image	id="shadowBg" url="" align="left|top"/>
		<tml:image	id="controlBg" url="" align="left|top"/>
		
		<tml:label id="label1" align="center" fontWeight="bold" textWrap="wrap">
			<%=msg.get("dsr.search.prompt")%>
		</tml:label>

		<tml:label id="label2" fontWeight="system_large" align="center" textWrap="wrap">
			<%=PoiUtil.amend(msg.get("dsr.example") + msg.get("dsr.search.example"))%>		
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