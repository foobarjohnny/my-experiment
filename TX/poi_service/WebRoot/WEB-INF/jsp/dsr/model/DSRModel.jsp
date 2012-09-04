<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.cserver.dsr.struts.util.AudioConstants"%>
<%@page import="com.telenav.cserver.pref.struts.util.PreferenceConstants"%>
<%@page import="com.telenav.cserver.util.FeatureConstant"%>

<%@include file="/WEB-INF/jsp/model/PrefModel.jsp"%>
<tml:script language="fscript" version="1" feature="<%=FeatureConstant.DSR%>">
		<![CDATA[
		    func DSR_M_saveParameter(JSONObject jo)
		    	clear()
		        Cache.saveToTempCache("<%=AudioConstants.StorageKey.AUDIO_FROM_PARAMEPTER%>",jo)
		    endfunc
		    
		    func clear()
		    	Cache.deleteFromTempCache("<%=AudioConstants.StorageKey.AUDIO_NORESUT_FROM%>")
		    	Cache.deleteFromTempCache("<%=AudioConstants.StorageKey.AUDIO_RESULT_DATA_DEFAULT_ADDRESS%>")
		    endfunc

		    func DSR_M_getParameter()
		        JSONObject jo = Cache.getJSONObjectFromTempCache("<%=AudioConstants.StorageKey.AUDIO_FROM_PARAMEPTER%>")
		        if jo==NULL
		        	JSONObject emptyJo
		        	DSR_M_saveParameter(emptyJo)
		        	return emptyJo
		        else
		        	return jo		        	
		        endif

		    endfunc
		    
		    func DSR_M_saveSearchAlongData(TxNode node)
		        Cache.saveToTempCache("<%=AudioConstants.StorageKey.AUDIO_POI_SEARCHALONG_DATA%>",node)
		    endfunc

		    func DSR_M_getSearchAlongData()
		        return Cache.getFromTempCache("<%=AudioConstants.StorageKey.AUDIO_POI_SEARCHALONG_DATA%>")
		    endfunc
		    
			func DSR_M_getPoiSearchType()
				JSONObject jo = DSR_M_getParameter()
				if jo == NULL
					return 5
				else
					int poiSearchType = JSONObject.getInt(jo,"poiSearchType")
					if poiSearchType == NULL
						return 5
					else
						return poiSearchType
					endif
				endif		
			endfunc
			
			func DSR_M_isSearchAlong()
				int isSearchAlong = 0
				if DSR_M_getPoiSearchType() == 7
					isSearchAlong = 1
				endif
				return isSearchAlong
			endfunc
						
			func DSR_M_getAheadAddress()
				TxNode searchInformationNode = DSR_M_getSearchAlongData()
            	TxNode orgNode = TxNode.childAt(searchInformationNode,1)
            	return orgNode
			endfunc

			func DSR_M_getDestAddress()
				TxNode searchInformationNode = DSR_M_getSearchAlongData()
            	TxNode destNode = TxNode.childAt(searchInformationNode,2)
            	return destNode
			endfunc
	
		    func DSR_M_getCallbackfunction()
		        JSONObject jo = DSR_M_getParameter()
		    	return checkNULL(JSONObject.getString(jo,"callbackfunction"))
	    	endfunc

		    func DSR_M_getCallbackpageurl()
		        JSONObject jo = DSR_M_getParameter()
		    	return checkNULL(JSONObject.getString(jo,"callbackpageurl"))
		    endfunc

		    func DSR_M_saveSearchInstFlag(int index)
		    	TxNode nodeAudio
		    	TxNode.addValue(nodeAudio,index)
		        Cache.saveCookie("<%=AudioConstants.StorageKey.AUDIO_SEARCH_INST_FLAG%>",nodeAudio)
		    endfunc
	
		    func DSR_M_getSearchInstFlag()
		        TxNode node = Cache.getCookie("<%=AudioConstants.StorageKey.AUDIO_SEARCH_INST_FLAG%>")
		        if node == NULL
		        	return 1
		        else
		        	return TxNode.valueAt(node,0) 
		        endif
		    endfunc
	    		    
		    func checkNULL(string s)
		    	if s == NULL
		    		return ""
		    	else
		    		return s
		    	endif	
		    endfunc
		    
		    	    
		    func DSR_M_isDSRSupported()
		    	int isSupport = 1
		    	TxNode node = System.getServerParam("DSR")
		    	println("DSR..................")
		    	println(node)
		    	if node != NULL
			    	if "0" == TxNode.msgAt(node,0)  
			    		isSupport = 0
			    	endif
		    	endif
		    	
		    	return isSupport
		    endfunc 
		]]>
</tml:script>