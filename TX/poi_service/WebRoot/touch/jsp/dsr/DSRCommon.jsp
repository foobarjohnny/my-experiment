<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@page import="com.telenav.cserver.dsr.struts.util.AudioConstants"%>
<%@page import="com.telenav.cserver.util.FeatureConstant"%>

<%@ include file="model/DSRCommonModel.jsp"%>
<%@ include file="/touch/jsp/StopUtil.jsp"%>
	<tml:script language="fscript" version="1" feature="<%=FeatureConstant.DSR%>">
		<![CDATA[
		func saveData(TxNode node)
			DSRCommon_M_clearAllData()
			if node == NULL
				return FALSE
			endif
			
			int childSize = TxNode.getChildSize(node)
			
			if childSize > 0
				TxNode nodeText = TxNode.childAt(node,0)
				if nodeText == NULL
					return FALSE
				endif
				DSRCommon_M_saveDataText(nodeText)
				
				if TxNode.getValueSize(node) >2
					int trxnId = TxNode.valueAt(node,2)
					DSRCommon_M_saveTrxnId(trxnId) 
				endif
			else
				return FALSE	
			endif
			
			if childSize > 1
				TxNode nodeAudio = TxNode.childAt(node,1)
				if nodeAudio != NULL
					Handset.prefetchAudio(node)
					DSRCommon_M_saveDataAudio(nodeAudio)
				endif
			endif
			
			if childSize > 2
				TxNode nodeAudioPure = TxNode.childAt(node,2)
				if nodeAudioPure != NULL
					DSRCommon_M_saveDataAudioPure(nodeAudioPure)
				endif
			endif

			if childSize > 3
				TxNode nodeAudioAlongRoute = TxNode.childAt(node,3)
				if nodeAudioAlongRoute != NULL
					DSRCommon_M_saveDataAudioAlongRoute(nodeAudioAlongRoute)
				endif
			endif
									
			return TRUE	
		endfunc
		
		func getDataText()
        	TxNode nodeText = DSRCommon_M_getDataText()
        	TxNode nodeDataText,nodeChild
			int count = TxNode.getChildSize(nodeText)
			string text
			int i =0
			int msgSize
			while count>i
				nodeChild = TxNode.childAt(nodeText,i)
				msgSize = TxNode.getStringSize(nodeChild)
				if msgSize>0 
					text = TxNode.msgAt(nodeChild,0)
				endif

				if msgSize>1 
					text = text + " " + "<%=msg.get("dsr.around")%>" + " "+ TxNode.msgAt(nodeChild,1)
				endif				
				
				if text != NULL && text !=""
					TxNode.addMsg(nodeDataText,text)
				endif 
				i = i+1
			endwhile
			
        	return nodeDataText
        endfunc

		
		func getOneDataText(int index)
        	TxNode nodeText = DSRCommon_M_getDataText()
        	if nodeText == NULL
        		return ""
        	else
				TxNode oneNode = TxNode.childAt(nodeText,index)
				if oneNode ==NULL
					return ""
				else
					return TxNode.msgAt(oneNode,0) 
				endif
			endif
        endfunc
        
        func getDataTextSize()
        	TxNode node = getDataText()
        	return TxNode.getStringSize(node)
        endfunc
        
        func getDataStop(int i)
        	TxNode nodeText = DSRCommon_M_getDataText()
        	TxNode childNode = TxNode.childAt(nodeText,i)
        	if TxNode.getChildSize(childNode) > 0
        		return TxNode.childAt(childNode,0)
        	else
        		return NULL	
        	endif
        	
        endfunc

        func getDataTextValue(int i)
        	TxNode nodeText = DSRCommon_M_getDataText()
        	TxNode childNode = TxNode.childAt(nodeText,i)
        	
        	int value = 0
        	if childNode != NULL
        		value = TxNode.valueAt(childNode,0) 
        	endif
        	
        	return value
        endfunc

        func getDataCommandText()
        	string text = ""
        	TxNode nodeText = DSRCommon_M_getDataText()
        	if nodeText != NULL
        		text = TxNode.msgAt(nodeText,0)
        	endif
        	return text
        endfunc
                        
        func getDataAudio()
        	return DSRCommon_M_getDataAudio()
        endfunc

		func releaseResource()
			Handset.stopAudio()
			DSR.stop()
		endfunc

		func onScriptException(String msg)
	
			return FAIL
        endfunc		             

		func DSR_getTimeout()
			return <%=AudioConstants.TIMEOUT_FIVESCEONDS%>
		endfunc
		
		func DSR_getMaxSpeechTime()
			string type = DSRCommon_M_getDSRType()
	    	int maxSpeechTime = <%=AudioConstants.DSR_RECOGNIZE_CITY_STATE_MAXSPEECHTIME%>
			if type == "<%=AudioConstants.DSR_RECOGNIZE_CITY_STATE%>"
				maxSpeechTime = <%=AudioConstants.DSR_RECOGNIZE_CITY_STATE_MAXSPEECHTIME%>
			elsif type == "<%=AudioConstants.DSR_RECOGNIZE_AIRPORT%>"
				maxSpeechTime = <%=AudioConstants.DSR_RECOGNIZE_AIRPORT_MAXSPEECHTIME%>
			elsif type == "<%=AudioConstants.DSR_RECOGNIZE_ONE_SHOT_ADDRESS%>"
				maxSpeechTime = <%=AudioConstants.DSR_RECOGNIZE_ONE_SHOT_ADDRESS_MAXSPEECHTIME%>
			elsif type == "<%=AudioConstants.DSR_RECOGNIZE_ONE_SHOT_INTERSECTION%>"
				maxSpeechTime = <%=AudioConstants.DSR_RECOGNIZE_ONE_SHOT_INTERSECTION_MAXSPEECHTIME%>
			elsif type == "<%=AudioConstants.DSR_RECOGNIZE_POI%>"
				maxSpeechTime = <%=AudioConstants.DSR_RECOGNIZE_POI_MAXSPEECHTIME%>			
			elsif type == "<%=AudioConstants.DSR_RECOGNIZE_SEARCHALONG%>"
				maxSpeechTime = <%=AudioConstants.DSR_RECOGNIZE_POI_MAXSPEECHTIME%>	
			elsif type == "<%=AudioConstants.DSR_RECOGNIZE_COMMAND_CONTROL%>"
				maxSpeechTime = <%=AudioConstants.DSR_RECOGNIZE_COMMAND_CONTROL_MAXSPEECHTIME%>
			endif
			
			return maxSpeechTime		
		endfunc		
		]]>
	</tml:script>
