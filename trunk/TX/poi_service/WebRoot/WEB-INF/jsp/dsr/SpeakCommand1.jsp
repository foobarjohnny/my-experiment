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
			DSRCommon_M_saveDSRType("<%=AudioConstants.DSR_RECOGNIZE_COMMAND_CONTROL%>")
		endfunc
		
		func onClickInstruction()
			releaseResource()
		endfunc
	]]>
	</tml:script>
	<tml:menuItem name="home" pageURL="<%=host + "/startUp.do?pageRegion=" + region%>">
	</tml:menuItem>
	<tml:menuItem name="instructionMenu" onClick="onClickInstruction" pageURL="<%=getPage + "SpeakCommandInstruction"%>" text="<%=msg.get("dsr.cc.button")%>" trigger="KEY_MENU|TRACKBALL_CLICK"/>
			
	<tml:page id="SpeakCommand" url="<%=getDsrPage + "SpeakCommand1"%>" type="<%=pageType%>" background="<%=imageUrl + "backgroud_no_title.png"%>" 
		 genericMenu="4" helpMsg="$//$saycommand" groupId="<%=GROUP_ID_DSR%>" supportback="false" >
		<%
			handler.toXML(out);
		%>
		<tml:label id="label1" align="center" fontWeight="bold">
			<%=msg.get("dsr.cc.prompt")%>
		</tml:label>
		<tml:menuRef name="instructionMenu" />
		<tml:label id="label2" fontWeight="system_large" align="center" textWrap="wrap">
			<%=PoiUtil.amend(msg.get("dsr.example") + msg.get("dsr.cc.example1"))%>	
		</tml:label>
		<tml:label id="label3" fontWeight="system_large" align="center">
				<%=msg.get("dsr.cc.example2")%>
		</tml:label>
		<tml:audioVolume id="imageSpeaker" align="center"/>
		<tml:button id="viewExampleButton" text="<%=msg.get("dsr.cc.button2")%>"
			fontWeight="system_large" isFocusable="true" visible="0"
			imageClick="<%=imageUrl + "button_large_on.png"%>"
			imageUnclick="<%=imageUrl + "button_large_off.png"%>">
			<tml:menuRef name="instructionMenu" />
		</tml:button>
	</tml:page>
	<cserver:outputLayout/>
</tml:TML>