<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.cserver.dsr.struts.util.AudioConstants"%>
<%@page import="com.telenav.cserver.util.FeatureConstant"%>

<tml:script language="fscript" version="1" feature="<%=FeatureConstant.DSR%>">
	<![CDATA[
	    func DSRCommon_M_clearAllData()
	        DSRCommon_M_clearDataText()
	        DSRCommon_M_clearDataAudio()
			DSRCommon_M_clearDataAudioPure()
			DSRCommon_M_clearSelectedAddress()
			DSRCommon_M_clearSelectedIndex()
			#DSRCommon_M_clearDSRType()
			DSRCommon_M_clearCommand()
			DSRCommon_M_clearDefaultAddress()
			Cache.deleteFromTempCache("<%=AudioConstants.StorageKey.AUDIO_RESULT_DATA_SELECTED_TEXT%>")
			#Cache.deleteFromTempCache("<%=AudioConstants.StorageKey.AUDIO_RESULT_DATA_SPEECHTIME%>")
			Cache.deleteFromTempCache("<%=AudioConstants.StorageKey.AUDIO_RESULT_DATA_SELECTED_TEXTVALUE%>")
			Cache.deleteFromTempCache("<%=AudioConstants.StorageKey.AUDIO_RESULT_DATA_AUDIO_ALONGROUTE%>")
			Cache.deleteFromTempCache("<%=AudioConstants.StorageKey.AUDIO_RESULT_DATA_TRXNID%>")
	    endfunc	

	    func DSRCommon_M_saveDataText(TxNode nodeText)
	        Cache.saveToTempCache("<%=AudioConstants.StorageKey.AUDIO_RESULT_DATA_TEXT%>",nodeText)
	    endfunc

	    func DSRCommon_M_getDataText()
	        TxNode nodeText = Cache.getFromTempCache("<%=AudioConstants.StorageKey.AUDIO_RESULT_DATA_TEXT%>")
	        return nodeText
	    endfunc

	    func DSRCommon_M_clearDataText()
	        Cache.deleteFromTempCache("<%=AudioConstants.StorageKey.AUDIO_RESULT_DATA_TEXT%>")
	    endfunc
	    	    
	    func DSRCommon_M_saveDataAudio(TxNode nodeAudio)
	        Cache.saveToTempCache("<%=AudioConstants.StorageKey.AUDIO_RESULT_DATA_AUDIO%>",nodeAudio)
	    endfunc

	    func DSRCommon_M_getDataAudio()
	        TxNode node = Cache.getFromTempCache("<%=AudioConstants.StorageKey.AUDIO_RESULT_DATA_AUDIO%>")
	        
	        return node
	    endfunc
	    
	    func DSRCommon_M_clearDataAudio()
	        Cache.deleteFromTempCache("<%=AudioConstants.StorageKey.AUDIO_RESULT_DATA_AUDIO%>")
	    endfunc

	    func DSRCommon_M_saveDataAudioPure(TxNode nodeAudio)
	        Cache.saveToTempCache("<%=AudioConstants.StorageKey.AUDIO_RESULT_DATA_AUDIOPURE%>",nodeAudio)
	    endfunc

	    func DSRCommon_M_getDataAudioPure()
	        TxNode node = Cache.getFromTempCache("<%=AudioConstants.StorageKey.AUDIO_RESULT_DATA_AUDIOPURE%>")
	        
	        return node
	    endfunc

	    func DSRCommon_M_saveDataAudioAlongRoute(TxNode nodeAudio)
	        Cache.saveToTempCache("<%=AudioConstants.StorageKey.AUDIO_RESULT_DATA_AUDIO_ALONGROUTE%>",nodeAudio)
	    endfunc

	    func DSRCommon_M_getDataAudioAlongRoute()
	        TxNode node = Cache.getFromTempCache("<%=AudioConstants.StorageKey.AUDIO_RESULT_DATA_AUDIO_ALONGROUTE%>")
	        
	        return node
	    endfunc
	    
	    func DSRCommon_M_clearDataAudioPure()
	        Cache.deleteFromTempCache("<%=AudioConstants.StorageKey.AUDIO_RESULT_DATA_AUDIOPURE%>")
	    endfunc
	    
	    func DSRCommon_M_saveSelectedIndex(int index)
	    	TxNode nodeAudio
	    	TxNode.addValue(nodeAudio,index)
	        Cache.saveToTempCache("<%=AudioConstants.StorageKey.AUDIO_RESULT_DATA_SELECTED_INDEX%>",nodeAudio)
	    endfunc

	    func DSRCommon_M_getSelectedIndex()
	        TxNode node = Cache.getFromTempCache("<%=AudioConstants.StorageKey.AUDIO_RESULT_DATA_SELECTED_INDEX%>")
	        if node == NULL
	        	return 0
	        else
	        	return TxNode.valueAt(node,0) 
	        endif
	    endfunc
	    	    
	    func DSRCommon_M_clearDSRType()
	        Cache.deleteFromTempCache("<%=AudioConstants.StorageKey.AUDIO_RESULT_DATA_TYPE%>")
	    endfunc

	    func DSRCommon_M_saveDSRType(string dsrType)
	    	TxNode node
	    	TxNode.addMsg(node,dsrType) 
	        Cache.saveToTempCache("<%=AudioConstants.StorageKey.AUDIO_RESULT_DATA_TYPE%>",node)
	    endfunc

	    func DSRCommon_M_getDSRType()
	        TxNode node = Cache.getFromTempCache("<%=AudioConstants.StorageKey.AUDIO_RESULT_DATA_TYPE%>")
	        if node == NULL
	        	return ""
	        else
	        	return TxNode.msgAt(node,0)
	        endif  
	    endfunc

	    func DSRCommon_M_clearCommand()
	        Cache.deleteFromTempCache("<%=AudioConstants.StorageKey.AUDIO_SAYCOMMAND_COMMAND%>")
	    endfunc

	    func DSRCommon_M_saveCommand(string command)
	    	TxNode node
	    	TxNode.addMsg(node,command) 
	        Cache.saveToTempCache("<%=AudioConstants.StorageKey.AUDIO_SAYCOMMAND_COMMAND%>",node)
	    endfunc

	    func DSRCommon_M_getCommand()
	        TxNode node = Cache.getFromTempCache("<%=AudioConstants.StorageKey.AUDIO_SAYCOMMAND_COMMAND%>")
	        if node == NULL
	        	return ""
	        else
	        	return TxNode.msgAt(node,0)  
	        endif
	    endfunc
	    	    	    
	    func DSRCommon_M_clearSelectedIndex()
	        Cache.deleteFromTempCache("<%=AudioConstants.StorageKey.AUDIO_RESULT_DATA_SELECTED_INDEX%>")
	    endfunc
	    	    
	    func DSRCommon_M_clearFirstFall()
	        Cache.deleteFromTempCache("<%=AudioConstants.StorageKey.AUDIO_NORESUT_FIRSTFALL%>")
	    endfunc

	    func DSRCommon_M_getFirstFall()
	        TxNode firstFallData = Cache.getFromTempCache("<%=AudioConstants.StorageKey.AUDIO_NORESUT_FIRSTFALL%>")
	        
	        return firstFallData
	    endfunc

	    func DSRCommon_M_saveFirstFall(TxNode firstFallData)
	        Cache.saveToTempCache("<%=AudioConstants.StorageKey.AUDIO_NORESUT_FIRSTFALL%>",firstFallData)
	    endfunc

	    func DSRCommon_M_saveNoResultFrom(TxNode nodeNoData)
	        Cache.saveToTempCache("<%=AudioConstants.StorageKey.AUDIO_NORESUT_FROM%>",nodeNoData)
	    endfunc
	    
	    func DSRCommon_M_getNoResultFrom()
	        TxNode nodeNoData = Cache.getFromTempCache("<%=AudioConstants.StorageKey.AUDIO_NORESUT_FROM%>")
	        
	        return nodeNoData
	    endfunc
	    
	    func DSRCommon_M_clearNoResultFrom()
	        Cache.deleteFromTempCache("<%=AudioConstants.StorageKey.AUDIO_NORESUT_FROM%>")
	    endfunc    	    	    	    	    

	    func DSRCommon_M_clearSelectedAddress()
	        Cache.deleteFromTempCache("<%=AudioConstants.StorageKey.AUDIO_RESULT_DATA_SELECTED_ADDRESS%>")
	    endfunc	
	    
	    func DSRCommon_M_saveSelectedAddress(JSONObject jo)
	        Cache.saveToTempCache("<%=AudioConstants.StorageKey.AUDIO_RESULT_DATA_SELECTED_ADDRESS%>",jo)
	    endfunc

	    func DSRCommon_M_getSelectedAddress()
	        JSONObject jo = Cache.getJSONObjectFromTempCache("<%=AudioConstants.StorageKey.AUDIO_RESULT_DATA_SELECTED_ADDRESS%>")
	        return jo
	    endfunc

	    func DSRCommon_M_clearDefaultAddress()
	        Cache.deleteFromTempCache("<%=AudioConstants.StorageKey.AUDIO_RESULT_DATA_DEFAULT_ADDRESS%>")
	    endfunc	
	    
	    func DSRCommon_M_saveDefaultAddress(JSONObject jo)
	        Cache.saveToTempCache("<%=AudioConstants.StorageKey.AUDIO_RESULT_DATA_DEFAULT_ADDRESS%>",jo)
	    endfunc

	    func DSRCommon_M_getDefaultAddress()
	        JSONObject jo = Cache.getJSONObjectFromTempCache("<%=AudioConstants.StorageKey.AUDIO_RESULT_DATA_DEFAULT_ADDRESS%>")
	        return jo
	    endfunc

	    func DSRCommon_M_saveSelectedText(string text)
	    	TxNode nodeAudio
	    	TxNode.addMsg(nodeAudio,text)
	        Cache.saveToTempCache("<%=AudioConstants.StorageKey.AUDIO_RESULT_DATA_SELECTED_TEXT%>",nodeAudio)
	    endfunc

	    func DSRCommon_M_getSelectedText()
	        TxNode node = Cache.getFromTempCache("<%=AudioConstants.StorageKey.AUDIO_RESULT_DATA_SELECTED_TEXT%>")
	        if node == NULL
	        	return ""
	        else
	        	return TxNode.msgAt(node,0) 
	        endif
	    endfunc

	    func DSRCommon_M_saveSpeechTime(int index)
	    	TxNode nodeAudio
	    	TxNode.addValue(nodeAudio,index)
	        Cache.saveToTempCache("<%=AudioConstants.StorageKey.AUDIO_RESULT_DATA_SPEECHTIME%>",nodeAudio)
	    endfunc

	    func DSRCommon_M_getSpeechTime()
	        TxNode node = Cache.getFromTempCache("<%=AudioConstants.StorageKey.AUDIO_RESULT_DATA_SPEECHTIME%>")
	        if node == NULL
	        	return 0
	        else
	        	return TxNode.valueAt(node,0) 
	        endif
	    endfunc


	    func DSRCommon_M_saveSelectedTextValue(int index)
	    	TxNode nodeAudio
	    	TxNode.addValue(nodeAudio,index)
	        Cache.saveToTempCache("<%=AudioConstants.StorageKey.AUDIO_RESULT_DATA_SELECTED_TEXTVALUE%>",nodeAudio)
	    endfunc

	    func DSRCommon_M_getSelectedTextValue()
	        TxNode node = Cache.getFromTempCache("<%=AudioConstants.StorageKey.AUDIO_RESULT_DATA_SELECTED_TEXTVALUE%>")
	        if node == NULL
	        	return 0
	        else
	        	return TxNode.valueAt(node,0) 
	        endif
	    endfunc

	    func DSRCommon_M_saveTrxnId(int index)
	    	TxNode nodeAudio
	    	TxNode.addValue(nodeAudio,index)
	        Cache.saveToTempCache("<%=AudioConstants.StorageKey.AUDIO_RESULT_DATA_TRXNID%>",nodeAudio)
	    endfunc

	    func DSRCommon_M_getTrxnId()
	        TxNode node = Cache.getFromTempCache("<%=AudioConstants.StorageKey.AUDIO_RESULT_DATA_TRXNID%>")
	        if node == NULL
	        	return 0
	        else
	        	return TxNode.valueAt(node,0) 
	        endif
	    endfunc

	    func DSRCommon_M_saveTimeOutCount(int index)
	    	TxNode nodeAudio
	    	TxNode.addValue(nodeAudio,index)
	        Cache.saveToTempCache("<%=AudioConstants.StorageKey.AUDIO_TIMEOUT_COUNT%>",nodeAudio)
	    endfunc

	    func DSRCommon_M_getTimeOutCount()
	        TxNode node = Cache.getFromTempCache("<%=AudioConstants.StorageKey.AUDIO_TIMEOUT_COUNT%>")
	        if node == NULL
	        	return 0
	        else
	        	return TxNode.valueAt(node,0) 
	        endif
	    endfunc

	    func DSRCommon_M_AddTimeOutCount()
	        int count = DSRCommon_M_getTimeOutCount()
	        count = count+1
	        DSRCommon_M_saveTimeOutCount(count)
	    endfunc
	    	    	    	    	    	    
	    func DSRCommon_M_getFirstScreen()
	    	string type = DSRCommon_M_getDSRType()
	    	String action = "<%=getDsrPageCallBack%>" + DSRCommon_M_getFirstPageName()	
			return action
	    endfunc
	    
	    func DSRCommon_M_getFirstPageName()
	    	string type = DSRCommon_M_getDSRType()
	    	String action = "Speak"
			if type == "<%=AudioConstants.DSR_RECOGNIZE_CITY_STATE%>"
				action =  action + "City1"
			elsif type == "<%=AudioConstants.DSR_RECOGNIZE_AIRPORT%>"
				action = action + "Airport1"
			elsif type == "<%=AudioConstants.DSR_RECOGNIZE_ONE_SHOT_ADDRESS%>"
				action = action + "Address1"
			elsif type == "<%=AudioConstants.DSR_RECOGNIZE_ONE_SHOT_INTERSECTION%>"
				action = action + "Intersection1"
			elsif type == "<%=AudioConstants.DSR_RECOGNIZE_POI%>"
				action = action + "Search1"			
			elsif type == "<%=AudioConstants.DSR_RECOGNIZE_SEARCHALONG%>"
				action = action + "SearchAlong1"	
			elsif type == "<%=AudioConstants.DSR_RECOGNIZE_COMMAND_CONTROL%>"
				action = action + "Command1"
			endif		
			return action
	    endfunc
	]]>
</tml:script>