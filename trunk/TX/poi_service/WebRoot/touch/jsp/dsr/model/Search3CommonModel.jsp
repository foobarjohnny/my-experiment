<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.cserver.dsr.struts.util.AudioConstants"%>
<%@page import="com.telenav.cserver.util.FeatureConstant"%>

<tml:script language="fscript" version="1" feature="<%=FeatureConstant.DSR%>">
	<![CDATA[
		func Search3Common_M_createIndex(int value)
			TxNode node
			TxNode.addValue(node,value)
			Cache.saveToTempCache("<%=AudioConstants.StorageKey.AUDIO_MULTIPLE_RESULT_INDEX%>",node)
		endfunc
		
		func Search3Common_M_getIndex()
			TxNode node = Cache.getFromTempCache("<%=AudioConstants.StorageKey.AUDIO_MULTIPLE_RESULT_INDEX%>")
			return TxNode.valueAt(node,0) 
		endfunc
		
		func Search3Common_M_increaseIndex()
			int index = Search3Common_M_getIndex()
			index = index + 1
			Search3Common_M_createIndex(index)
		endfunc
		
		func Search3Common_M_SaveCancelUrl(string url)
			TxNode node
			TxNode.addMsg(node,url)
			Cache.saveToTempCache("<%=AudioConstants.StorageKey.AUDIO_FLOW3_CANCELURL%>",node)
		endfunc

		func Search3Common_M_getCancelUrl()
			TxNode node = Cache.getFromTempCache("<%=AudioConstants.StorageKey.AUDIO_FLOW3_CANCELURL%>")
			return TxNode.msgAt(node,0) 
		endfunc

		func Search3Common_M_setContinueFlag(int value)
			TxNode node
			TxNode.addValue(node,value)
			Cache.saveToTempCache("<%=AudioConstants.StorageKey.AUDIO_MULTIPLE_RESULT_CONTINUE_FLAG%>",node)
		endfunc
		
		func Search3Common_M_getContinueFlag()
			TxNode node = Cache.getFromTempCache("<%=AudioConstants.StorageKey.AUDIO_MULTIPLE_RESULT_CONTINUE_FLAG%>")
			return TxNode.valueAt(node,0) 
		endfunc
	]]>
</tml:script>