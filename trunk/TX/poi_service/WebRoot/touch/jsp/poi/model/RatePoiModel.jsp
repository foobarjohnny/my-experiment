<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<tml:script language="fscript" version="1">
		<![CDATA[
		    func RatePoi_M_savePoiToDo(JSONObject poiJo)
		        String saveKey = "<%=Constant.StorageKeyForJSON.JSON_OBJECT_POI_TODO%>"
				Cache.saveToTempCache(saveKey,poiJo)
		    endfunc
		    
		    func RatePoi_M_getPoiToDo()
		        String saveKey = "<%=Constant.StorageKeyForJSON.JSON_OBJECT_POI_TODO%>"
				JSONObject poiJo = Cache.getJSONObjectFromTempCache(saveKey)
				return poiJo
		    endfunc
		    
		    func RatePoi_M_saveRateIndex(int rateIndex)
		        TxNode rateIndexNode
		        TxNode.addValue(rateIndexNode,rateIndex)
		        
		        String saveKey = "<%=Constant.StorageKey.RATE_POI_INDEX%>"
		        Cache.saveToTempCache(saveKey,rateIndexNode)
		    endfunc
		    
		    func RatePoi_M_getRateIndex()
		        int rateIndex = 0
		        
		        String saveKey = "<%=Constant.StorageKey.RATE_POI_INDEX%>"
		        TxNode rateIndexNode = Cache.getFromTempCache(saveKey)
		        if NULL != rateIndexNode
		           rateIndex = TxNode.valueAt(rateIndexNode,0)
		        endif
		        return rateIndex
		    endfunc
		    
		    func RatePoi_M_saveBackUrl(String url)
		        TxNode urlNode
		        TxNode.addMsg(urlNode,url)
		        String saveKey = "<%=Constant.StorageKey.RATE_POI_BACK_URL%>"
		        Cache.saveToTempCache(saveKey,urlNode)
		    endfunc
		    
		    func RatePoi_M_getBackUrl()
		        String url = ""
		        String saveKey = "<%=Constant.StorageKey.RATE_POI_BACK_URL%>"
		        TxNode urlNode = Cache.getFromTempCache(saveKey)
		        if NULL != urlNode
		           url = TxNode.msgAt(urlNode,0)
		        endif
		        
		        return url
		    endfunc
		]]>
</tml:script>