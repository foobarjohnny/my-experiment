<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@page import="com.telenav.cserver.dsr.struts.util.AudioConstants"%>
<%@page import="com.telenav.cserver.util.FeatureConstant"%>

<%@ include file="/touch/jsp/dsr/model/SpeakNumberModel.jsp"%>
	<tml:script language="fscript" version="1" feature="<%=FeatureConstant.DSR%>">
		<![CDATA[
        func NumberDSR_Start()
        	SpeakNumber_M_setContinueFlag(1)
        	SpeakNumber_M_setSayNumberAvailable(0)
        	showSayNumberProgressBar()
        	CallBack_playNumberAudioResult()
        	SpeakNumber_M_saveTimeOutCount(0)
        endfunc
                
        func CallBack_playNumberAudioResult()
        	if SpeakNumber_M_getContinueFlag()
            	DSR.start("<%=AudioConstants.DSR_RECOGNIZE_NUMBER%>",<%=AudioConstants.TIMEOUT_FIVESCEONDS%>,<%=AudioConstants.DSR_RECOGNIZE_NUMBER_MAXSPEECHTIME%>,"CallBack_DSR_Number")
        	endif
        endfunc
		        
		func CallBack_DSR_Number(int status,TxNode node)
			if SpeakNumber_M_getContinueFlag()
				if status == <%=AudioConstants.DSR_STATUS_TIMEOUT%> || status == <%=AudioConstants.DSR_STATUS_ERROR%>
					SpeakNumber_TimeOut()
				elsif status == <%=AudioConstants.DSR_STATUS_SPEAK_START%>
					closeProgressBar()
				elsif status == <%=AudioConstants.DSR_STATUS_SPEAK_FINISH%>
					showProgressBar()
				elsif status == <%=AudioConstants.DSR_STATUS_RECOGNIZE_FINISH%>
					closeProgressBar()
					if node == NULL
						playNumberInvalidAudio()
					else
						string numberInput = getCommandText(node) 
						if isNumberValid(numberInput)
							int i = String.convertToNumber(numberInput)
							i = i-1
							Handset.stopAudio()
							SpeakNumber_M_setSayNumberAvailable(1)
							DSR_showDetail(i)
							return FAIL
						else
							playNumberInvalidAudio()
						endif
					endif	
				endif
			endif
		endfunc

		func SpeakNumber_TimeOut()
			closeProgressBar()
			SpeakNumber_M_AddTimeOutCount()
			int count = SpeakNumber_M_getTimeOutCount()
			if count<3
				playTimeOutAudio()
			else
				SpeakNumber_M_setSayNumberAvailable(1)	
			endif		
		endfunc
		
		func showSayNumberProgressBar()
			System.showDsrMsgBox("<%=msg.get("dsr.sayordernumber")%>",TRUE,"CallBack_showProgressBar")
		endfunc
		
		func showProgressBar()
			System.showProgressBar("<%=msg.get("dsr.processing")%>",TRUE,"CallBack_showProgressBar")
		endfunc
		
		func CallBack_showProgressBar()
			SpeakNumber_M_setSayNumberAvailable(1)
			SpeakNumber_M_setContinueFlag(0)
			Handset.stopAudio()
		endfunc
		
		func closeProgressBar()
			System.hideProgressBar()
			System.hidePopup()
		endfunc

		func playNumberInvalidAudio()
			showSayNumberProgressBar()
			Handset.playStaticAudio(getNumberTimeoutAudio(),"CallBack_playNumberAudioResult")
		endfunc
						
		func playTimeOutAudio()
			showSayNumberProgressBar()
			Handset.playStaticAudio(getNumberAudio(),"CallBack_playNumberAudioResult")
		endfunc
		
		func isNumberValid(string input)
			if(String.isNumberString(input))
			    int i = String.convertToNumber(input)
			    int size = SpeakNumber_M_getMaxSize()
			    
			    if i>0 && i<=size
			    	return TRUE
			    else
			    	return FALSE	
			    endif
			    
			endif
			
			return FALSE
		endfunc
		
		func getNumberAudio()
			TxNode nodeAudio
			TxNode.addValue(nodeAudio,<%=AudioConstants.STATIC_AUDIO_NUMBER_AUDIO1%>)
			return nodeAudio
		endfunc
		
		func getNumberTimeoutAudio()
			TxNode nodeAudio
			TxNode.addValue(nodeAudio,<%=AudioConstants.STATIC_AUDIO_NOMATCHFOUND%>)
			TxNode.addValue(nodeAudio,<%=AudioConstants.STATIC_AUDIO_NUMBER_AUDIO2%>)
			return nodeAudio		
		endfunc 

		
		func getCommandText(TxNode node)
			if node == NULL
				return ""
			endif
			
			TxNode nodeText = TxNode.childAt(node,0)
			if nodeText == NULL
				return ""
			endif			
		
			TxNode nodeChild = TxNode.childAt(nodeText,0)
			if nodeChild == NULL
				return ""
			endif			
					
			return TxNode.msgAt(nodeChild,0)
		endfunc

		func SpeakNumber_M_setContinueFlag(int value)
			TxNode node
			TxNode.addValue(node,value)
			Cache.saveToTempCache("<%=AudioConstants.StorageKey.AUDIO_SPEAK_NUMBER_CONTINUE_FLAG%>",node)
		endfunc
		
		func SpeakNumber_M_getContinueFlag()
			TxNode node = Cache.getFromTempCache("<%=AudioConstants.StorageKey.AUDIO_SPEAK_NUMBER_CONTINUE_FLAG%>")
			return TxNode.valueAt(node,0) 
		endfunc
		]]>
	</tml:script>
