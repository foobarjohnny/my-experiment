<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ include file="../Header.jsp"%>
<%@page import="java.util.List"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@page import="com.telenav.cserver.dsr.struts.util.AudioConstants"%>
<%@page
	import="com.telenav.cserver.pref.struts.util.PreferenceConstants"%>
<%
    String pageURL = getPage + "SpeakGettingRoute";
%>
<tml:TML outputMode="TxNode">
	<%@ include file="model/DSRCommonModel.jsp"%>
	<jsp:include page="/WEB-INF/jsp/DriveToCommonInclude.jsp" />

	<tml:script language="fscript" version="1">
		<![CDATA[
		func CallBack_SelectAddress()
			DriveTo_M_setDoingNav(0)
			DriveTo_M_setFromDSR(1)
        endfunc
        
        func preLoad()
    		TxNode audioNodeForWhole = playAudio()
    		DriveTo_M_saveAudio(audioNodeForWhole)
    		DriveTo_M_saveAddress(DSRCommon_M_getSelectedAddress())     
        endfunc
        
		func playAudio()
			TxNode audioNodeForWhole
			
			string text = DSRCommon_M_getCommand()
			if text == NULL
				text = ""
			endif

			if text == "<%=AudioConstants.DSR_COMMAND_TEXT_RESUME%>"
				TxNode nodeAudio
				TxNode.addValue(nodeAudio,<%=AudioConstants.STATIC_AUDIO_RESUME_TRIP%>)
				
				TxNode.addChild(audioNodeForWhole,nodeAudio)
			else
				int index = DSRCommon_M_getSelectedIndex()
				TxNode nodeAudioPure = DSRCommon_M_getDataAudioPure()
	        	if nodeAudioPure != NULL
	        		TxNode.addValue(audioNodeForWhole,index)
	        		TxNode.addValue(audioNodeForWhole,index)
	        		TxNode.addChild(audioNodeForWhole,nodeAudioPure) 	        		
	        	endif				
			endif
			
			return audioNodeForWhole
		endfunc		        		

		func doBack()
			Handset.stopAudio()
			
			string logText = "<%=AudioConstants.LOG_TYPE_DSR%>" + "|" + DSRCommon_M_getTrxnId() + "|-1| |1"
			if DSRCommon_M_getTrxnId() != (-1)
				System.log(logText)
			endif
			
			string dsrType = DSRCommon_M_getDSRType()
        	if dsrType == "<%=AudioConstants.DSR_RECOGNIZE_COMMAND_CONTROL%>"
        		System.doAction("home")	
        	else
        		System.back()		
        	endif
        	return FAIL		
		endfunc

        func onResume()
        	doBack()
        	return FAIL
        endfunc 
		]]>
	</tml:script>
	<tml:menuItem name="back" pageURL="" />
	<tml:menuItem name="home" pageURL="<%=host + "/startUp.do?pageRegion=" + region%>"/>
	<tml:page id="SpeakGettingRoute" url="<%=pageURL%>" type="<%=pageType%>"
		groupId="<%=GROUP_ID_DSR%>">
	</tml:page>
</tml:TML>