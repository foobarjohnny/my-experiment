<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%@ include file="../Header.jsp"%>
<%@page import="java.util.List"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@page import="com.telenav.cserver.dsr.struts.util.AudioConstants"%>
<%@page import="com.telenav.cserver.poi.struts.util.PoiUtil"%>
<%@page
	import="com.telenav.cserver.pref.struts.util.PreferenceConstants"%>
<%
    String pageURL = getPage + "SpeakGettingRoute";
%>
<tml:TML outputMode="TxNode">
	<%@ include file="model/DSRCommonModel.jsp"%>
	<jsp:include page="/touch64/jsp/DriveToCommonInclude.jsp" />

	<tml:script language="fscript" version="1">
		<![CDATA[
		func CallBack_SelectAddress()
			DriveTo_M_setDoingNav(3)
			DriveTo_M_setFromDSR(1)
        endfunc
        
        func preLoad()
        	playAudio()
    		TxNode audioNodeForWhole
    		DriveTo_M_saveAudio(audioNodeForWhole)
    		DriveTo_M_saveAddress(DSRCommon_M_getSelectedAddress())
    		int dataTextValue = DSRCommon_M_getSelectedTextValue()
    		if dataTextValue == <%=AudioConstants.DSR_COMMAND_VALUE_DRIVE_POI%>
    			DriveTo_M_saveStopType(<%=Constant.STOP_POI%>)
    		else
    			DriveTo_M_saveStopType(<%=Constant.STOP_GENERIC%>)	
    		endif     
        endfunc
        
        func onLoad()
			JSONObject jo = DSRCommon_M_getSelectedAddress()
	        string text = ""
        	if jo != NULL
				string label = JSONObject.getString(jo,"label")
	        	string firstLine = JSONObject.getString(jo,"firstLine")
	        	string city = JSONObject.getString(jo,"city")
	        	string state = JSONObject.getString(jo,"state")

	        	if	label != ""
	        		text = label + " \n"
	        	elsif firstLine != ""
	        		text = firstLine + " \n"
	        	endif
	        	
	        	if city != ""
	        		text = text  + city + ","
	        	endif
	
	        	if state != ""
	        		text = text + " " + state + "," 
	        	endif
	        	
	        	if text != ""
	        		text = String.at(text,0,String.getLength(text)-1)
	        	endif
         	endif       	
        	Page.setComponentAttribute("text1","text",text)        
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
				
				Handset.playStaticAudio(nodeAudio,"CallBack_PlayLocation")
				TxNode.addChild(audioNodeForWhole,nodeAudio)
			else
				TxNode nodeStaticAudio = getStaticAudio()
				
				int index = DSRCommon_M_getSelectedIndex()
				TxNode nodeAudioPure = DSRCommon_M_getDataAudioPure()
	        	if nodeAudioPure != NULL
	        		TxNode nodeAfterStaticAudio
	        		Handset.playMultiSpecNode(index,index,nodeStaticAudio,nodeAudioPure,nodeAfterStaticAudio,"CallBack_PlayLocation")
	        		
	        		TxNode.addValue(audioNodeForWhole,index)
	        		TxNode.addValue(audioNodeForWhole,index)
	        		TxNode.addChild(audioNodeForWhole,nodeStaticAudio)
	        		TxNode.addChild(audioNodeForWhole,nodeAudioPure) 	        		
	        	else
	        		Handset.playStaticAudio(nodeStaticAudio,"CallBack_PlayLocation")
	        		TxNode.addChild(audioNodeForWhole,nodeStaticAudio)
	        	endif				
			endif
			
			return audioNodeForWhole
		endfunc
		
		func CallBack_PlayLocation()
			JSONObject jo = DriveTo_M_getAddress()
      		    if NULL != JSONObject.get(jo,"poiOrStop") && "poi" == JSONObject.get(jo,"poiOrStop")
			   	DriveTo_M_saveStopType(<%=Constant.STOP_POI%>)
      		    endif				
			
			String navType = DriveTo_M_getNavType()
			DriveTo_M_saveNavType("")
			DriveToControl_driveToActionFromDSR(jo,navType)
			return FAIL 
		endfunc
		
		func getStaticAudio()
			TxNode nodeAudio
			TxNode.addValue(nodeAudio,<%=AudioConstants.STATIC_AUDIO_GETTING%>)
			TxNode.addValue(nodeAudio,getRouteStyleAudio())
			TxNode.addValue(nodeAudio,<%=AudioConstants.STATIC_AUDIO_TO%>)
			return nodeAudio
		endfunc
		
		func getRouteStyleAudio()
			int routeStyle = Pref_M_getRouteStyle()
			
			int id = <%=AudioConstants.STATIC_AUDIO_ROUTESTYLE_FASTEST%>
			if routeStyle == <%=PreferenceConstants.VALUE_ROUTESTYLE_SHORTEST%>
				id = <%=AudioConstants.STATIC_AUDIO_ROUTESTYLE_SHORTEST%>
			elsif routeStyle == <%=PreferenceConstants.VALUE_ROUTESTYLE_STREET%>
				id = <%=AudioConstants.STATIC_AUDIO_ROUTESTYLE_STREET%>
			elsif routeStyle == <%=PreferenceConstants.VALUE_ROUTESTYLE_HIGHWAY%>
				id = <%=AudioConstants.STATIC_AUDIO_ROUTESTYLE_HIGHWAY%>
			elsif routeStyle == <%=PreferenceConstants.VALUE_ROUTESTYLE_PEDESTRIAN%>
				id = <%=AudioConstants.STATIC_AUDIO_ROUTESTYLE_PEDESTRIAN%>	
			endif
			
			return id 
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
	<tml:page id="SpeakGettingRoute" url="<%=pageURL%>" type="<%=pageType%>" groupId="<%=GROUP_ID_DSR%>"　supportback="false">
		<tml:label id="label1" fontWeight="system_large" align="center" textWrap="wrap">
			<%=PoiUtil.amend(msg.get("dsr.getreouteto"))%>
		</tml:label>
		<tml:label id="text1" fontWeight="bold|system_large" align="center" textWrap="wrap"/>
	</tml:page>
	<cserver:outputLayout/>	
</tml:TML>