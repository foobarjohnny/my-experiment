<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@page import="com.telenav.cserver.dsr.struts.util.AudioConstants"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@page import="com.telenav.cserver.util.FeatureConstant"%>
<%@page import="com.telenav.cserver.stat.*"%>

<%@ include file="DSRCommon.jsp"%>
<%@ include file="SpeakShowDetail.jsp"%>
<%@ include file="/touch64/jsp/dsr/controller/Search3CommonController.jsp"%>
<%@ include file="/touch64/jsp/poi/controller/SearchPoiController.jsp"%>
<jsp:include page="/touch64/jsp/ac/controller/SelectAddressController.jsp" />
<jsp:include page="/touch64/jsp/local_service/controller/MapWrapController.jsp" />
	<tml:script language="fscript" version="1" feature="<%=FeatureConstant.DSR%>">
		<![CDATA[
		
        func preLoad()
        	Page.setControlProperty("typeInButton","focused","true")
        endfunc
        
        func onShow()
        	DSR.stop()
        	DSRCommon_M_saveTimeOutCount(0)
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
        	Page.setComponentAttribute("typeInButton","isFocusable","0")
			releaseResource()
			string dsrType = DSRCommon_M_getDSRType()
        	if dsrType == "<%=AudioConstants.DSR_RECOGNIZE_COMMAND_CONTROL%>"
        		<%if(PoiUtil.isAndroid61(handler) || TnUtil.isATTRIM63(handler)){%>
        		   System.back()
        		<%}else{%>
        		   System.doAction("home")
        		<%}%>
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
        	string audioName = ""
        	if from=="search3"
        		audioNode = ParameterSet.getParam("audio1")
        		audioName = "audio1"
        	else
	       		if firstFall == 1
	       			audioNode = ParameterSet.getParam("audio3")
	       			String buttonType = "0"
					if NULL != Handset.getRimTrackButtonType()
					   buttonType = Handset.getRimTrackButtonType() + ""
					endif
					println("~~~~~~buttonType::::::::\n"+buttonType)
					if "trackpad" == buttonType && isCommandType()
					   audioNode = ParameterSet.getParam("audio6")
					endif
					println("audioNode.................")
					println(audioNode)
	       			audioName = "audio3"
	       		else
	       			audioNode = ParameterSet.getParam("audio4")
	       			audioName = "audio4"
	       		endif
			endif
			println("~~~~~~audioName::::::::\n"+audioName)
			System.showGeneralMsg(NULL,getAudioMessage(audioName),"<%=msg.get("common.button.OK")%>",NULL,NULL,"CallBack_Popup_DoNothing")
			Handset.playStaticAudio(audioNode,"CallBack_playStaticAudio")
        endfunc
                
        func  CallBack_playStaticAudio()
        	closeProgressBar()
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
				if( Page.getControlProperty("typeInButton","isFocused") == TRUE )
					nodeAudio2 = ParameterSet.getParam("audio1")
				endif
				println("~~~~~~buttonType::::::::\n"+buttonType)
				println("nodeAudio2.................")
				println(nodeAudio2)
				
				Handset.playStaticAudio(nodeAudio2,"CallBack_playStaticAudio")
				System.showGeneralMsg(NULL,getAudioMessage("audio2"),"<%=msg.get("common.button.OK")%>",NULL,NULL,"CallBack_Popup_DoNothing")
			endif		
		endfunc
		
		func CallBack_Popup_DoNothing(int param)
			if param == 3
				println("---cancel--")
			else
				println("---ok--")
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
			reachMaxTime = 0
			return reachMaxTime
		endfunc
		
		func preShowProgressBar()
			TxNode nodeAudio
			TxNode.addValue(nodeAudio,<%=AudioConstants.STATIC_AUDIO_ENDING_BEEP%>)
			
			Handset.playStaticAudio(nodeAudio,"showProgressBar")
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
			System.hidePopup()
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
        	DSRCommon_M_AddTimeOutCount()
			int count = DSRCommon_M_getTimeOutCount()
			if count>=3
				cancelAction()
			else	
	        	if reachMaxSpeechTime()
					TxNode nodeAudioMaxSpeech = ParameterSet.getParam("audioMaxSpeech")
					Handset.playStaticAudio(nodeAudioMaxSpeech,"CallBack_playStaticAudio")
				else
					TxNode nodeNoData
					TxNode.addMsg(nodeNoData,"search2") 
					DSRCommon_M_saveNoResultFrom(nodeNoData)
					loadRetry()
				endif
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
            
            string commandText = getDataCommandText()
        	DSRCommon_M_saveCommand(commandText)
        	if isShortCutCommand(commandText)
        		handleShortCutCommand()
        	else
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
        	endif
        endfunc

		func isShortCutCommand(string text)
			int isShortCut = FALSE
			
			if text == "<%=AudioConstants.DSR_COMMAND_TEXT_SHOW%>"	
				isShortCut = TRUE
			endif
			return isShortCut
		endfunc

		func handleShortCutCommand()
			string text = getOneDataText(0)
			
			if text == "<%=AudioConstants.DSR_COMMAND_TEXT_WEATHER%>"
				Weather_C_showCurrent()
			elsif text == "<%=AudioConstants.DSR_COMMAND_TEXT_TRAFFIC%>"
				MapWrap_C_showTrafficMap()
			elsif text == "<%=AudioConstants.DSR_COMMAND_TEXT_COMMUTE%>"
				System.doAction("commuteAlert")
			elsif text == "<%=AudioConstants.DSR_COMMAND_TEXT_MOVIE%>"
				TxNode nodeExternalFlag
				TxNode.addMsg(nodeExternalFlag,"searchMovie")
				Cache.saveToTempCache("<%=AudioConstants.StorageKey.EXTERNAL_FROM_SAYCOMMAND%>",nodeExternalFlag)
				System.doAction("movieMenu")
			elsif text == "<%=AudioConstants.DSR_COMMAND_TEXT_THEATER%>"
				doGetGPSForSearchTheater()
			else
				noRecordFound()	
			endif
		endfunc

		func doGetGPSForSearchTheater()
	   		getCurrentLocationOfTheater(<%=Constant.CurrentLocation.CURRENT_LOCATION%>,240,480,12)		        
        endfunc
        
        # Back from "getGPS"
        func setCurrentLocationOfTheater(JSONObject jo)

			JSONObject joParameter
			JSONObject.put(joParameter,"inputString","searchTheater")
			JSONObject.put(joParameter,"searchType",5)
			JSONObject.put(joParameter,"callBackUrl","")
			JSONObject.put(joParameter,"callBackFunction","")
			JSONObject.put(joParameter,"showProgressBar",1)
			JSONObject.put(joParameter,"noRecordBackUrl","<%=getDsrPageCallBack + "SpeakCommand1"%>")
			JSONObject.put(joParameter,"waitAudioFinish",0)
			JSONObject.put(joParameter,"showInMap",0)

			PoiList_C_searchPoiInterface(joParameter,jo)
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
        	JSONObject.put(jo,"returnAsIs","1")
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
		
		func onClickTypeOnVolume()
			println("----in onClickTypeOnVolume")
        	if getVoiceInputStatus()
	        	DSR.manualRecordEnd()
        	endif
		endfunc
		
		func getAudioMessage(string audioNodeName)
			string message = DSRCommon_M_getFirstPageName() + "." + audioNodeName
			string message1 = "$(" + message + ")"
			println("------------message:" + message1)
			return message1
		endfunc
		]]>
	</tml:script>
	
	<tml:menuItem name="gettingMap" pageURL="<%=getPage + "SpeakGettingMap"%>"/>
	<tml:menuItem name="search1" pageURL=""/>
	<tml:actionItem name="switchAudioPath" action="<%=Constant.LOCALSERVICE_SWITCHAUDIOPATH%>">
	</tml:actionItem>
	<tml:menuItem name="switchAudioPathMenu" actionRef="switchAudioPath" trigger="TRACKBALL_CLICK" />	
