<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@page import="com.telenav.cserver.dsr.struts.util.AudioConstants"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@page import="com.telenav.cserver.util.FeatureConstant"%>
<%@page import="com.telenav.cserver.stat.*"%>

<%@ include file="DSRCommon.jsp"%>
<%@ include file="SpeakShowDetail.jsp"%>
<%@ include file="/WEB-INF/jsp/dsr/controller/Search3CommonController.jsp"%>
<%@ include file="/WEB-INF/jsp/poi/controller/SearchPoiController.jsp"%>
<jsp:include page="/WEB-INF/jsp/ac/controller/SelectAddressController.jsp" />
<jsp:include page="/WEB-INF/jsp/local_service/controller/MapWrapController.jsp" />
	<tml:script language="fscript" version="1" feature="<%=FeatureConstant.DSR%>">
		<![CDATA[
		
        func preLoad()
        	Page.setComponentAttribute("typeInButton","visible","0")
        	MenuItem.setItemValid("typeInButton",0,0)
        	MenuItem.commitSetItemValid("typeInButton")
        	DSRCommon_M_saveTimeOutCount(0)
        endfunc
        
        func onShow()
        	DSR.stop()
			Navigation.setAutoBackFlagForNavAudio(1)
        	System.setKeyEventListener("-dial-$-","onKeyPressed")
        	loadSpecific()
        	
        	TxNode nodeNoData = DSRCommon_M_getNoResultFrom()
			if nodeNoData == NULL
				loadNormal()
			else
				loadRetry()
			endif       	
        endfunc
                				
		func checkError(string msg)
			System.showGeneralMsg(NULL,msg,"<%=msg.get("common.button.OK")%>",NULL,NULL,"CallBack_Popup_Error")
			return FAIL			
		endfunc
		
		func CallBack_Popup_Error(int param)
			cancelAction()
		endfunc	
		      
		func onBack()
			cancelAction()
			return FAIL		
		endfunc
		                
        func cancelAction()
			releaseResource()
			string dsrType = DSRCommon_M_getDSRType()
        	if dsrType == "<%=AudioConstants.DSR_RECOGNIZE_COMMAND_CONTROL%>"
        		System.doAction("home")
				return FAIL		
        	else
        		System.back()
				return FAIL			
        	endif
        endfunc
        		
		func onKeyPressed(string s)
        	if s == "dial"
        		if getVoiceInputStatus()
	        		DSR.manualRecordEnd()
        		endif
        		return TRUE	
        	elsif s == "$"
        		System.doAction("switchAudioPathMenu")
        		return FAIL
        	endif
        endfunc

        func setVoiceInputStatus(int flag)
        	TxNode node
	    	TxNode.addValue(node,flag)
	        Cache.saveToTempCache("VoiceInputStatus",node)
        endfunc

        func getVoiceInputStatus()
        	TxNode node = Cache.getFromTempCache("VoiceInputStatus")
	        if node == NULL
	        	return 0
	        else
	        	return TxNode.valueAt(node,0) 
	        endif
        endfunc
                        
        func loadNormal()
        	DSR.prefetch(DSRCommon_M_getDSRType(),DSR_getTimeout(),DSR_getMaxSpeechTime(),"CallBack_DSR_FIRST")
            DSRCommon_M_clearFirstFall()
			#TxNode nodeAudio1 = ParameterSet.getParam("audio1")
			#Handset.playStaticAudio(nodeAudio1,"CallBack_playStaticAudio")
			CallBack_playStaticAudio()
        endfunc
		
        func loadRetry()
        	DSR.prefetch(DSRCommon_M_getDSRType(),DSR_getTimeout(),DSR_getMaxSpeechTime(),"CallBack_DSR_FIRST")
        	Page.setComponentAttribute("typeInButton","visible","1")
        	MenuItem.setItemValid("typeInButton",0,1)
        	MenuItem.commitSetItemValid("typeInButton")
        	
			int firstFall = 0
			TxNode firstFallData = DSRCommon_M_getFirstFall()
			if firstFallData == NULL
				firstFall = 1
				TxNode data
				TxNode.addMsg(data,"firstfall") 
				DSRCommon_M_saveFirstFall(data)
			endif
			
			TxNode audioNode
        	TxNode nodeNoData = DSRCommon_M_getNoResultFrom()
        	string from = TxNode.msgAt(nodeNoData,0)
        	DSRCommon_M_clearNoResultFrom()
        	if from=="search3"
        		audioNode = ParameterSet.getParam("audio1")
        	else
	       		if firstFall == 1
	       			audioNode = ParameterSet.getParam("audio3")
	       			String buttonType = "0"
					if NULL != Handset.getRimTrackButtonType()
					   buttonType = Handset.getRimTrackButtonType() + ""
					endif
					if "trackpad" == buttonType && isCommandType()
					   audioNode = ParameterSet.getParam("audio6")
					endif
					println("audioNode.................")
					println(audioNode)
	       		else
	       			audioNode = ParameterSet.getParam("audio4")
	       		endif
			endif
			Handset.playStaticAudio(audioNode,"CallBack_playStaticAudio")
        endfunc
                
        func  CallBack_playStaticAudio()
			DSR.start(DSRCommon_M_getDSRType(),DSR_getTimeout(),DSR_getMaxSpeechTime(),"CallBack_DSR_FIRST")
		endfunc
		
        func  CallBack_DSR_FIRST(int status,TxNode node)
        	setVoiceInputStatus(0)
			if status == <%=AudioConstants.DSR_STATUS_SPEAK_START%>
				setVoiceInputStatus(1)
			elsif status == <%=AudioConstants.DSR_STATUS_SPEAK_FINISH%>
				showProgressBar()
				saveSpeechTime(node)
			elsif status == <%=AudioConstants.DSR_STATUS_RECOGNIZE_FINISH%>	
				closeProgressBar()
				if saveData(node)
					checkResult()
				else
					noRecordFound()	
				endif			
			elsif status == <%=AudioConstants.DSR_STATUS_TIMEOUT%>
				DSR_TimeOut()
			elsif status == <%=AudioConstants.DSR_STATUS_ERROR%>
				Handset.stopAudio()
				closeProgressBar()
				string errorMsg = ""
				if node != NULL
					errorMsg = TxNode.msgAt(node,0)  
					if errorMsg == NULL || errorMsg == ""
						errorMsg = "DSR error!"
					endif
				endif
				checkError(errorMsg)
			endif						
		endfunc
		
		func DSR_TimeOut()
			DSRCommon_M_AddTimeOutCount()
			int count = DSRCommon_M_getTimeOutCount()
			if count>=3
				cancelAction()
			else
				TxNode nodeAudio2 = ParameterSet.getParam("audio2")
				String buttonType = "0"
				if NULL != Handset.getRimTrackButtonType()
				   buttonType = Handset.getRimTrackButtonType() + ""
				endif
				if "trackpad" == buttonType && isCommandType()
				   nodeAudio2 = ParameterSet.getParam("audio5")
				endif
				println("nodeAudio2.................")
				println(nodeAudio2)
				Handset.playStaticAudio(nodeAudio2,"CallBack_playStaticAudio")
			endif		
		endfunc
		
		func saveSpeechTime(TxNode node)
			int speechTime = 0
			if node != NULL
				if TxNode.getValueSize(node) >0
					speechTime = TxNode.valueAt(node,0) 
				endif
			endif
			
			DSRCommon_M_saveSpeechTime(speechTime)
		endfunc
		
		func reachMaxSpeechTime()
			int reachMaxTime = 0		
			if DSRCommon_M_getSpeechTime() >= DSR_getMaxSpeechTime()
				reachMaxTime = 1
			endif
			return reachMaxTime
		endfunc

		func showProgressBar()
			System.showProgressBar(getProgressBarText(),TRUE,"CallBack_showProgressBar")
		endfunc
		
		func getProgressBarText()
			string msg = "<%=msg.get("dsr.processing")%>"
        	return msg			
		endfunc
		
		func CallBack_showProgressBar()
			cancelAction()
		endfunc
		
		func closeProgressBar()
			System.hideProgressBar()
		endfunc
		
        func checkResult()	
        	releaseResource()
        	
			if isCommandType()
				checkResultForCommand()
			else
	        	TxNode node = getDataText()
				int inputcount = TxNode.getStringSize(node)
				
				if inputcount == 0
					noRecordFound()
				elsif inputcount == 1
					DSR_showDetail(0)
				else
					
					Search3Common_C_show(DSRCommon_M_getFirstScreen())
				endif
			endif	
        endfunc
		
        func noRecordFound()
        	if reachMaxSpeechTime()
				TxNode nodeAudioMaxSpeech = ParameterSet.getParam("audioMaxSpeech")
				Handset.playStaticAudio(nodeAudioMaxSpeech,"CallBack_playStaticAudio")
			else
				TxNode nodeNoData
				TxNode.addMsg(nodeNoData,"search2") 
				DSRCommon_M_saveNoResultFrom(nodeNoData)
				loadRetry()
			endif 
        endfunc
        
        func isCommandType()
        	string dsrType = DSRCommon_M_getDSRType()
        	if dsrType == "<%=AudioConstants.DSR_RECOGNIZE_COMMAND_CONTROL%>"
        		return TRUE
        	else
        		return FALSE
        	endif
        endfunc	

        func checkResultForCommand()
            
        	DSRCommon_M_saveCommand(getDataCommandText())
        	int commandStatus = getRecognitionCommandStatus()
        	#This is No Recognition
        	if commandStatus == 0
        		noRecordFound()
        	elsif commandStatus == 2
        		resumeTrip()
        	else
	         	int dataStatus = getRecognitionDataStatus()
	        	#This is Partial Recognition
	        	if dataStatus == 0
	        		checkPartialRecognition()
	        	else
	        		checkFullRecognition()	
	        	endif       			
        	endif
        endfunc
        
        func resumeTrip()
        	JSONObject jo = SelectAddress_M_getResumeAddress()
        	if jo == NULL
        		goToDriveToMain()
        	else
        		DSRCommon_M_saveSelectedAddress(jo)
        		TxNode node
	        	TxNode.addMsg(node,JSONObject.toString(jo))
				TxNode node1
	        	TxNode.addMsg(node1,"CallBack_SelectAddress")
	        	MenuItem.setBean("callback", "callFunction", node1)
	        	MenuItem.setBean("callback", "returnAddress", node)
	        	MenuItem.setAttribute("callback","url","<%=getPageCallBack + "SpeakGettingRoute"%>")
	        	System.doAction("callback")
	        	return FAIL
        	endif
        endfunc
        
        func checkPartialRecognition()
        	string text = DSRCommon_M_getCommand()
        	
        	if text == "<%=AudioConstants.DSR_COMMAND_TEXT_DRIVETO%>"
				goToDriveToMain()
			endif 

			if text == "<%=AudioConstants.DSR_COMMAND_TEXT_MAPIT%>"
				goToMapItMain()
			endif 
			
			if text == "<%=AudioConstants.DSR_COMMAND_TEXT_SEARCH%>"
				goToSearchMain()
			endif 
			
        endfunc
        

        func checkFullRecognition()
        	TxNode node = getDataText()
			int inputcount = TxNode.getStringSize(node)
			
			if inputcount == 1
				DSR_showDetail(0)
			else
				Search3Common_C_show(DSRCommon_M_getFirstScreen())
			endif
        endfunc
        
        func getRecognitionCommandStatus()
        	int status = 0
        	
        	string text = DSRCommon_M_getCommand()
			String cmd = "<%=AttributeValues.ENTRY_POINT_DRIVE_TO%>"			
			if text == "<%=AudioConstants.DSR_COMMAND_TEXT_DRIVETO%>"
				status = 1
			endif 

			if text == "<%=AudioConstants.DSR_COMMAND_TEXT_MAPIT%>"
				status = 1
				cmd = "<%=AttributeValues.ENTRY_POINT_MAP_IT%>"
			endif 
			
			if text == "<%=AudioConstants.DSR_COMMAND_TEXT_SEARCH%>"
				status = 1
				cmd = "<%=AttributeValues.ENTRY_POINT_SEARCH%>"
			endif 

			if text == "<%=AudioConstants.DSR_COMMAND_TEXT_RESUME%>"
				status = 2
			endif 
									        	
		    if status != 0 && StatLogger.isStatEnabled(<%=EventTypes.STARTUP_INFO%>)
		    	JSONObject statEvent
				JSONObject.put(statEvent, "<%=AttributeID.STARTED_BY%>", "<%=AttributeValues.FROM_CONVENIENCE_KEY%>")
				JSONObject.put(statEvent, "<%=AttributeID.STARTUP_ENTRY_POINT%>", cmd)
				StatLogger.logEvent(<%=EventTypes.STARTUP_INFO%>, statEvent)
		    endif
				    
        	return status
        endfunc
                
        func getRecognitionDataStatus()
        	int status = 0
        	
        	TxNode node = getDataText()
			int inputcount = TxNode.getStringSize(node)
			if inputcount > 0
				status = 1
			endif 
        	
        	return status
        endfunc
        
        func goToDriveToMain()
			JSONObject jo
        	JSONObject.put(jo,"title","<%=msg.get("selectaddress.title.driveto")%>")
        	JSONObject.put(jo,"mask","10111111111")
        	JSONObject.put(jo,"from","DriveTo")
        	JSONObject.put(jo,"callbackfunction","CallBack_SelectAddress")
			JSONObject.put(jo,"callbackpageurl","<%=getPageCallBack + "DriveToWrap"%>")
			SelectAddress_C_SelectAddress(jo)        
        endfunc    
           	
        func goToSearchMain()
			SearchPoi_C_initial(5)
			SearchPoi_C_showSearch()        
        endfunc

        func goToMapItMain()
        	DSRCommon_M_saveSelectedTextValue(<%=AudioConstants.DSR_COMMAND_VALUE_CURRENTLOCATION%>)
			System.doAction("gettingMap")
        	return FAIL     
        endfunc
		]]>
	</tml:script>
	
	<tml:menuItem name="gettingMap" pageURL="<%=getPage + "SpeakGettingMap"%>"/>
	<tml:menuItem name="search1" pageURL=""/>
	<tml:actionItem name="switchAudioPath" action="<%=Constant.LOCALSERVICE_SWITCHAUDIOPATH%>">
	</tml:actionItem>
	<tml:menuItem name="switchAudioPathMenu" actionRef="switchAudioPath" trigger="TRACKBALL_CLICK" />	
