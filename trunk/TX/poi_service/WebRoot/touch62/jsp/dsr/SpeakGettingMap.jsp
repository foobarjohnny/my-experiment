<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ include file="../Header.jsp"%>
<%@page import="java.util.List"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@page import="com.telenav.cserver.dsr.struts.util.AudioConstants"%>
<% 
	String pageURL = getPage + "SpeakGettingMap";
%>
<tml:TML outputMode="TxNode">
<%@ include file="model/DSRCommonModel.jsp"%>
<jsp:include page="/touch62/jsp/local_service/controller/MapWrapController.jsp" />

	<tml:script language="fscript" version="1">
		<![CDATA[
		func preLoad()
			string callbackfunction = "CallBack_PlayStaticAudio"
			int dataTextValue = DSRCommon_M_getSelectedTextValue()
			TxNode nodeStaticAudio = getStaticAudio()
			int isCurrent = 0
			if dataTextValue == <%=AudioConstants.DSR_COMMAND_VALUE_CURRENTLOCATION%>
				callbackfunction = "CallBack_PlayLocation"
				isCurrent = 1
				TxNode.addValue(nodeStaticAudio,<%=AudioConstants.STATIC_AUDIO_CURRENT_LOCATION%>)
				Handset.playStaticAudio(nodeStaticAudio,callbackfunction)
			else
				int index = DSRCommon_M_getSelectedIndex()
				TxNode nodeAudioPure = DSRCommon_M_getDataAudioPure()
	        	if nodeAudioPure != NULL
	        		TxNode nodeAfterStaticAudio
	        		Handset.playMultiSpecNode(index,index,nodeStaticAudio,nodeAudioPure,nodeAfterStaticAudio,"CallBack_PlayLocation")
	        	else
	        		Handset.playStaticAudio(nodeStaticAudio,"CallBack_PlayLocation")
	        	endif				
			endif
			
			if isCurrent
				
				MapWrap_C_showCurrent()
			else
				JSONObject jo = DSRCommon_M_getSelectedAddress()
				MapWrap_C_showSingleAddress(jo)			
			endif
		endfunc
        		
		func CallBack_SelectAddress()
        endfunc	
		
		func getStaticAudio()
			TxNode nodeAudio
			TxNode.addValue(nodeAudio,<%=AudioConstants.STATIC_AUDIO_GETTING_MAP%>)
			return nodeAudio
		endfunc
	
		func onBack()
			Handset.stopAudio()
			string dsrType = DSRCommon_M_getDSRType()
        	if dsrType == "<%=AudioConstants.DSR_RECOGNIZE_COMMAND_CONTROL%>"
        		System.doAction("home")	
        	else
        		System.back()		
        	endif
        	return FAIL	
		endfunc
		
		func CallBack_PlayLocation()
		
		endfunc			        		

        func onResume()
        	onBack()
        	return FAIL
        endfunc 
		]]>
	</tml:script>
	
	<tml:menuItem name="back" pageURL=""/>
	<tml:menuItem name="home" pageURL="<%=host + "/startUp.do?pageRegion=" + region%>"/>
	<tml:page id="SpeakGettingMap" url="<%=pageURL%>" type="<%=pageType%>" supportback="false" groupId="<%=GROUP_ID_DSR%>">
	</tml:page>	
</tml:TML>