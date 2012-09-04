<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.cserver.dsr.struts.util.AudioConstants"%>
<%@page import="com.telenav.cserver.util.FeatureConstant"%>

<tml:script language="fscript" version="1" feature="<%=FeatureConstant.DSR%>">
	<![CDATA[
		func SpeakNumber_M_saveMaxSize(int value)
			TxNode node
			TxNode.addValue(node,value)
			Cache.saveToTempCache("<%=AudioConstants.StorageKey.AUDIO_SAYNUMBER_MAXSIZE%>",node)
		endfunc
		
		func SpeakNumber_M_getMaxSize()
			TxNode node = Cache.getFromTempCache("<%=AudioConstants.StorageKey.AUDIO_SAYNUMBER_MAXSIZE%>")
			return TxNode.valueAt(node,0) 
		endfunc

		func SpeakNumber_M_setSayNumberAvailable(int value)
			TxNode node
			TxNode.addValue(node,value)
			Cache.saveToTempCache("<%=AudioConstants.StorageKey.AUDIO_SAYNUMBER_AVAILABLE%>",node)
		endfunc
		
		func SpeakNumber_M_isSayNumberAvailable()
			TxNode node = Cache.getFromTempCache("<%=AudioConstants.StorageKey.AUDIO_SAYNUMBER_AVAILABLE%>")
			return TxNode.valueAt(node,0) 
		endfunc

	    func SpeakNumber_M_saveTimeOutCount(int index)
	    	TxNode nodeAudio
	    	TxNode.addValue(nodeAudio,index)
	        Cache.saveToTempCache("<%=AudioConstants.StorageKey.AUDIO_TIMEOUT_COUNT%>",nodeAudio)
	    endfunc

	    func SpeakNumber_M_getTimeOutCount()
	        TxNode node = Cache.getFromTempCache("<%=AudioConstants.StorageKey.AUDIO_TIMEOUT_COUNT%>")
	        if node == NULL
	        	return 0
	        else
	        	return TxNode.valueAt(node,0) 
	        endif
	    endfunc

	    func SpeakNumber_M_AddTimeOutCount()
	        int count = SpeakNumber_M_getTimeOutCount()
	        count = count+1
	        SpeakNumber_M_saveTimeOutCount(count)
	    endfunc
	]]>
</tml:script>