<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%@ include file="../Header.jsp"%>	
<%@page import="com.telenav.tnbrowser.util.DataHandler"%>
<%@page import="com.telenav.cserver.dsr.struts.util.AudioConstants"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<% 
	DataHandler handler = (DataHandler)request.getAttribute("DataHandler");
	String pageURL = getDsrPage + "SpeakSearchAlong2";
	String backgroundUrl = imageUrl + "backgroud_no_title.png";
%>
<tml:TML outputMode="TxNode">
<%@ include file="../poi/controller/PoiListController.jsp"%>
<%@ include file="model/DSRCommonModel.jsp"%>
<%@ include file="model/DSRModel.jsp"%>
<%@ include file="/WEB-INF/jsp/StopUtil.jsp"%>
	
	<tml:script language="fscript" version="1">
		<![CDATA[
		func preLoad()
			Navigation.setAutoBackFlagForNavAudio(0)
		    Page.setControlProperty("changeLocationButton","focused","true")
		endfunc
		
        func onLoad()
        	setAheadAddresFlag(1)
        	Page.setComponentAttribute("text1","text",getPoiName())
        	startSearchAlong()
        endfunc
        
        func startSearchAlong()
        	displayLabel()
       		playAudio()       
        endfunc
        
        func playAudio()
        	int index = DSRCommon_M_getSelectedIndex()
        	if getAheadAddresFlag()
				TxNode nodeAudioPure = DSRCommon_M_getDataAudioPure()
	        	if nodeAudioPure != NULL
	        		Handset.playSpecNode(index,index,nodeAudioPure,"showDetail")	
	        	else
	        		showDetail()
	        	endif
        	else
				TxNode nodeAudioAlongRoute = DSRCommon_M_getDataAudioAlongRoute()
	        	if nodeAudioAlongRoute != NULL
	        		Handset.playSpecNode(index,index,nodeAudioAlongRoute,"showDetail")	
	        	else
	        		showDetail()
	        	endif
        	endif
        endfunc
        
		func onBack()
			releaseResource()
			System.back()
			return FAIL
		endfunc
		
		func showDetail()
			JSONObject joParameter
			JSONObject.put(joParameter,"inputString",getPoiName())
			JSONObject.put(joParameter,"searchType",7)
			JSONObject.put(joParameter,"callBackUrl","")
			JSONObject.put(joParameter,"callBackFunction","")
			JSONObject.put(joParameter,"showProgressBar",0)
			JSONObject.put(joParameter,"noRecordBackUrl","<%=getDsrPageCallBack + "SpeakSearchAlong1"%>")
			JSONObject.put(joParameter,"waitAudioFinish",0)
			JSONObject.put(joParameter,"showInMap",1)
			JSONObject.put(joParameter,"searchAlongType",getSearchAlongType())
			PoiList_C_searchPoiInterface(joParameter,getSearchAlongAddress())
		endfunc
		
		func getPoiName()
			string poiName = DSRCommon_M_getSelectedText()
			return poiName
		endfunc
		
		func displayLabel()
			Page.setComponentAttribute("label1","text",getSearchAlongLabel())
			Page.setComponentAttribute("changeLocationButton","text",getButtonText())
		endfunc
		
		func getSearchAlongLabel()
			string label = "<%=msg.get("dsr.searchneardest")%>"
			
			if getAheadAddresFlag()
				label = "<%=msg.get("dsr.searchbizalong")%>"
			endif
			return label
		endfunc
		
		func getSearchAlongAddress()
			TxNode addressNode = DSR_M_getDestAddress()
			if getAheadAddresFlag()
				addressNode = DSR_M_getAheadAddress()
			endif
			JSONObject jo = convertStopToJSON(addressNode)
			
			return jo
		endfunc
		
		func getSearchAlongType()
			int type = <%=Constant.searchAlongType_nearDestination%>
			if getAheadAddresFlag()
				type = <%=Constant.searchAlongType_closeAhead%>
			endif
			return type
		endfunc
		
		func getButtonText()
			string label = "<%=msg.get("dsr.searchalong.button.along")%>"
			
			if getAheadAddresFlag()
				label = "<%=msg.get("dsr.searchalong.button.neardest")%>"
			endif
			return label		
		endfunc
		
		func onClickChangeLocation()
			releaseResource()
			changeAddress()
			startSearchAlong()
		endfunc
        
        func changeAddress()
        	setAheadAddresFlag(!getAheadAddresFlag())
        endfunc		
	
        func setAheadAddresFlag(int flag)
        	TxNode node
	    	TxNode.addValue(node,flag)
	        Cache.saveToTempCache("<%=AudioConstants.StorageKey.AUDIO_SEARCHALONG_AHEAD_FLAG%>",node)
        endfunc

        func getAheadAddresFlag()
        	TxNode node = Cache.getFromTempCache("<%=AudioConstants.StorageKey.AUDIO_SEARCHALONG_AHEAD_FLAG%>")
	        if node == NULL
	        	return 0
	        else
	        	return TxNode.valueAt(node,0) 
	        endif
        endfunc

		func releaseResource()
			Handset.stopAudio()
		endfunc

        func onResume()
			System.doAction("cancel")
			return FAIL
        endfunc		        	
		]]>
	</tml:script>
	<tml:menuItem name="cancel"  onClick="cancelAction"  text="<%=msg.get("common.button.Cancel")%>" trigger="KEY_MENU"/>
	<tml:menuItem name="search1" pageURL="<%=getDsrPage + "SpeakSearch1"%>"/>
	<tml:menuItem name="changeLocation" onClick="onClickChangeLocation" trigger="TRACKBALL_CLICK"/>
			
	<tml:page id="SpeakSearchAlong2" url="<%=pageURL%>" type="<%=pageType%>" background="<%=backgroundUrl%>" supportback="false" groupId="<%=GROUP_ID_DSR%>">
		<%
			handler.toXML(out);
		%>
		<tml:label id="text1" fontWeight="bold|system_large" align="center"/>
		<tml:label id="label1" fontWeight="system_medium" align="center"/>
		<tml:progressBar id="progressBar"/>
		
		<tml:button id="changeLocationButton" text=""
			fontWeight="system_large" isFocusable="true" 
			imageClick="<%=imageUrl + "button_huge_on.png"%>"
			imageUnclick="<%=imageUrl + "button_huge_off.png"%>">
			<tml:menuRef name="changeLocation" />
		</tml:button>
	</tml:page>	
	<cserver:outputLayout/>
</tml:TML>
