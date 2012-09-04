<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%@ include file="../Header.jsp"%>
<%@page import="java.util.List"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@page import="com.telenav.cserver.dsr.struts.util.AudioConstants"%>
<% 
	String pageURL = getPage + "SpeakGettingPOI";
%>
<tml:TML outputMode="TxNode">
<%@ include file="/touch/jsp/poi/controller/PoiListController.jsp"%>
<%@ include file="model/DSRCommonModel.jsp"%>
<%@ include file="model/DSRModel.jsp"%>
<%@ include file="/touch/jsp/local_service/GetGps.jsp"%>

	<tml:script language="fscript" version="1">
		<![CDATA[
		func onLoad()
			int index = DSRCommon_M_getSelectedIndex()
			TxNode nodeAudioPure = DSRCommon_M_getDataAudioPure()
        	if nodeAudioPure != NULL
        		Handset.playSpecNode(index,index,nodeAudioPure,"CallBack_PlayAudio")	
        	else
        		CallBack_PlayAudio()
        	endif
			JSONObject joAddress = DSRCommon_M_getSelectedAddress()
			TxNode nodeDataText = getDataText(index)
        	
        	displayText(nodeDataText)
        	
        	string searchString = ""
        	if nodeDataText!= NULL
        		searchString = TxNode.msgAt(nodeDataText,0)  
        	endif
        	PoiList_M_saveResentSearch(searchString)
        	
			if isCurrentAddress(joAddress)
				getCurrentLocation(<%=Constant.CurrentLocation.CURRENT_LOCATION%>,240,480,12)
			else
				forwardToPoiList(joAddress)
			endif
		endfunc

        # Back from "getGPS"
        func setCurrentLocation(JSONObject jo)
       		forwardToPoiList(jo)
        endfunc
		        
		func isCurrentAddress(JSONObject jo)
			if jo == NULL
				return TRUE
			endif
			
			string city = JSONObject.getString(jo,"city")
        	string state = JSONObject.getString(jo,"state")
        	
			if city=="" && state==""
				return TRUE
			else
				return FALSE
			endif
		endfunc
				
		func forwardToPoiList(JSONObject joAddress)
			int index = DSRCommon_M_getSelectedIndex()
        	string text = DSRCommon_M_getCommand()
			if text == NULL
				text = ""
			endif
			
			int showInMap = 0
			if text == "<%=AudioConstants.DSR_COMMAND_TEXT_MAPIT%>"
				showInMap =1
			endif
			
        	TxNode nodeDataText = getDataText(index)
        	string searchString = ""
        	if nodeDataText!= NULL
        		searchString = TxNode.msgAt(nodeDataText,0)  
        	endif
        	
        	string noRecordBackUrl = "<%=getDsrPageCallBack + "SpeakCommand1"%>"
        	string type = DSRCommon_M_getDSRType()
        	if type == "<%=AudioConstants.DSR_RECOGNIZE_POI%>"
        		noRecordBackUrl = "<%=getDsrPageCallBack + "SpeakSearch1"%>"
        	endif
        	
			JSONObject joParameter
			JSONObject.put(joParameter,"inputString",searchString)
			JSONObject.put(joParameter,"searchType",DSR_M_getPoiSearchType())
			JSONObject.put(joParameter,"callBackUrl",DSR_M_getCallbackpageurl())
			JSONObject.put(joParameter,"callBackFunction",DSR_M_getCallbackfunction())
			JSONObject.put(joParameter,"showProgressBar",0)
			JSONObject.put(joParameter,"noRecordBackUrl",noRecordBackUrl)
			JSONObject.put(joParameter,"waitAudioFinish",1)
			JSONObject.put(joParameter,"showInMap",showInMap)
			
			PoiList_C_searchPoiInterface(joParameter,joAddress)		
		endfunc

       	func CallBack_GPS_Error(int param)
			onBack()
		endfunc
		                		
		func CallBack_PlayAudio()
			PoiList_C_checkAudioFinishAndSubmit()
		endfunc
		
		func displayText(TxNode nodeDataText)
			string text = ""
			string label = ""
			int size = TxNode.getStringSize(nodeDataText) 
			if size == 2
				text = TxNode.msgAt(nodeDataText,0) + "\n" + "<%=msg.get("dsr.around")%>" + " "
				text = text + TxNode.msgAt(nodeDataText,1)
				
				label = "<%=msg.get("dsr.poi.searching")%>"
			else
				text = TxNode.msgAt(nodeDataText,0)
				label = "<%=msg.get("dsr.poi.searchingnearby")%>"   
			endif
			
			Page.setComponentAttribute("text1","text",text)
			Page.setComponentAttribute("label1","text",label)
		endfunc
	
		func onBack()
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

		func getDataText(int index)
        	TxNode nodeText = DSRCommon_M_getDataText()
			return TxNode.childAt(nodeText,index)
        endfunc
		]]>
	</tml:script>
	
	<tml:menuItem name="back" pageURL=""/>
	<tml:menuItem name="cancel" onClick="onBack" trigger="KEY_MENU"/>
	<tml:menuItem name="home" pageURL="<%=host + "/startUp.do?pageRegion=" + region%>"/>
	<tml:page id="SpeakGettingPOI" url="<%=pageURL%>" type="<%=pageType%>" background="" supportback="false" groupId="<%=GROUP_ID_DSR%>">	
		<tml:label id="text1" fontWeight="bold" align="center" textWrap="wrap"/>
		<tml:label id="label1" fontWeight="system_median" align="center" textWrap="wrap"/>
		<tml:progressBar id="progressBar"/>
	</tml:page>
	<cserver:outputLayout/>	
</tml:TML>