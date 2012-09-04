<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@page import="com.telenav.cserver.util.FeatureConstant"%>

<%
    String addressDetailListKey = Constant.StorageKeyForJSON.JSON_ARRAY_ADDRESS_DETAIL_LIST;
    String addressDetailListIndexKey = Constant.StorageKey.ADDRESS_DETAIL_LIST_INDEX;
%>
<tml:script language="fscript" version="1" feature="<%=FeatureConstant.UGC_EDIT%>">
		<![CDATA[
		    func EditPoi_M_getPoiJsonArray()
		        # Address list
		        String saveKey = "<%=addressDetailListKey%>"
			    JSONArray poiJsonArray = Cache.getJSONArrayFromTempCache(saveKey)
			    return poiJsonArray
		    endfunc
		    
		    func EditPoi_M_getIndex()
		        String saveKey = "<%=addressDetailListIndexKey%>"
				TxNode indexNode = Cache.getFromTempCache(saveKey)
				int index = 0
				if NULL != indexNode
				   index = TxNode.valueAt(indexNode, 0)
				endif
				return index
		    endfunc
		    
		    func EditPoi_M_getParamValue(String component)
		    	TxNode node
				node = ParameterSet.getParam(component)
				if NULL == node 
					return NULL	
				endif	
				String value = TxNode.msgAt(node,0)
				return value
		    endfunc
		    
		    
		    func EditPoi_M_saveLocation(JSONObject addressNode)
	           String saveKey="<%=Constant.StorageKeyForJSON.JSON_OBJECT_NEW_POI_LOCATION%>"
			   Cache.saveToTempCache(saveKey,addressNode) 
	        endfunc
	    
	        func EditPoi_M_deleteLocation()
	           String saveKey="<%=Constant.StorageKeyForJSON.JSON_OBJECT_NEW_POI_LOCATION%>"
	           Cache.deleteFromTempCache(saveKey)
	        endfunc
	      
	        func EditPoi_M_getLocation()
	           String saveKey="<%=Constant.StorageKeyForJSON.JSON_OBJECT_NEW_POI_LOCATION%>"
	           JSONObject addressNode = Cache.getJSONObjectFromTempCache(saveKey)
	           return addressNode
	        endfunc
	      
	        func EditPoi_M_getBrandNames()
	           String saveKey="<%=Constant.StorageKey.NEW_POI_BRAND_NAMES%>"
	           TxNode brandNamesNode
	           brandNamesNode = Cache.getCookie(saveKey)
	           return brandNamesNode
	        endfunc
	        
	        func EditPoi_M_savePoiInformation(JSONObject poiInformationJo)
	           String saveKey="<%=Constant.StorageKeyForJSON.JSON_OBJECT_SHOW_POI_INFORMATION%>"
			   Cache.saveToTempCache(saveKey,poiInformationJo) 
	        endfunc
	        
	        func EditPoi_M_getPoiInformation()
	           String saveKey="<%=Constant.StorageKeyForJSON.JSON_OBJECT_SHOW_POI_INFORMATION%>"
			   JSONObject poiInformationJo = Cache.getJSONObjectFromTempCache(saveKey) 
			   return poiInformationJo
	        endfunc
		]]>
</tml:script>