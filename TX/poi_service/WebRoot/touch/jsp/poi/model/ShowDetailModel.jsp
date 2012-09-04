<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<tml:script language="fscript" version="1">
	<![CDATA[
	    func ShowDetail_M_getAddressList()
	         String saveKey = "<%=Constant.StorageKeyForJSON.JSON_ARRAY_ADDRESS_DETAIL_LIST%>"
			 JSONArray addressList = Cache.getJSONArrayFromTempCache(saveKey)
			 return addressList
	    endfunc
	    
	    func ShowDetail_M_saveAddressList(JSONArray addressList)
	         String saveKey = "<%=Constant.StorageKeyForJSON.JSON_ARRAY_ADDRESS_DETAIL_LIST%>"
			 Cache.saveToTempCache(saveKey,addressList)
	    endfunc
	    
	    func ShowDetail_M_getIndex()
	         String saveKey = "<%=Constant.StorageKey.ADDRESS_DETAIL_LIST_INDEX%>"
			 TxNode indexNode = Cache.getFromTempCache(saveKey)
			 int index = TxNode.valueAt(indexNode, 0)
			 return index
	    endfunc
	    
	    func ShowDetail_M_saveNewIndex(TxNode newIndexNode)
	        String saveKey = "<%=Constant.StorageKey.ADDRESS_DETAIL_LIST_INDEX%>"
			Cache.saveToTempCache(saveKey,newIndexNode)
	    endfunc
	    
	    func ShowDetail_M_saveForDetail(JSONArray addressList, int indexInList)
			# Save address list
			String saveKey = "<%=Constant.StorageKeyForJSON.JSON_ARRAY_ADDRESS_DETAIL_LIST%>"
			Cache.saveToTempCache(saveKey,addressList)
							
			# Save index
			TxNode indexNode
			TxNode.addValue(indexNode, indexInList)
			saveKey = "<%=Constant.StorageKey.ADDRESS_DETAIL_LIST_INDEX%>"
			Cache.saveToTempCache(saveKey,indexNode)
		endfunc
		
		func ShowDetail_M_savePoiToDo(JSONObject poiJo)
	        String saveKey = "<%=Constant.StorageKeyForJSON.JSON_OBJECT_POI_TODO%>"
			Cache.saveToTempCache(saveKey,poiJo)
	    endfunc
	    
	    func ShowDetail_M_saveIsSponsorForDetail(int isSponsor)
            TxNode isSponsorNode
            TxNode.addValue(isSponsorNode,isSponsor)
            
            String saveKey = "<%=Constant.StorageKey.IS_SPONSOR_POI%>"
			Cache.saveToTempCache(saveKey,isSponsorNode)
        endfunc
        
        func ShowDetail_M_getIsSponsorForDetail()
            int isSponsor = 0
            String saveKey = "<%=Constant.StorageKey.IS_SPONSOR_POI%>"
			TxNode isSponsorNode = Cache.getFromTempCache(saveKey)
			if NULL != isSponsorNode
			   isSponsor = TxNode.valueAt(isSponsorNode, 0)
			endif
			
			return isSponsor
        endfunc
        
	    func ShowDetail_M_saveSponsorSizeForDetail(int sponsorSize)
            TxNode sponsorSizeNode
            TxNode.addValue(sponsorSizeNode,sponsorSize)
            String saveKey = "<%=Constant.StorageKey.SPONSOR_SIZE%>"
			Cache.saveToTempCache(saveKey,sponsorSizeNode)
        endfunc
        
        func ShowDetail_M_getSponsorSizeForDetail()
            int sponsorSize = 0
            String saveKey = "<%=Constant.StorageKey.SPONSOR_SIZE%>"
			TxNode sponsorSizeNode = Cache.getFromTempCache(saveKey)
			if NULL != sponsorSizeNode
			   sponsorSize = TxNode.valueAt(sponsorSizeNode, 0)
			endif
			return sponsorSize
        endfunc
        
        func ShowDetail_M_saveTotalIndexForDetail(int totalIndexForDetail)
            TxNode totalIndexForDetailNode
            TxNode.addValue(totalIndexForDetailNode,totalIndexForDetail)
            String saveKey = "<%=Constant.StorageKey.TOTAL_INDEX_FOR_DETAIL%>"
			Cache.saveToTempCache(saveKey,totalIndexForDetailNode)
        endfunc
        
        func ShowDetail_M_getTotalIndexForDetail()
            int totalIndexForDetail = 0
            String saveKey = "<%=Constant.StorageKey.TOTAL_INDEX_FOR_DETAIL%>"
			TxNode totalIndexForDetailNode = Cache.getFromTempCache(saveKey)
			if NULL != totalIndexForDetailNode
			   totalIndexForDetail = TxNode.valueAt(totalIndexForDetailNode, 0)
			endif
			return totalIndexForDetail
        endfunc
        
        func ShowDetail_M_saveTotalSize(int totalSize)
            TxNode totalSizeNode
            TxNode.addValue(totalSizeNode,totalSize)
            String saveKey = "<%=Constant.StorageKey.TOTAL_SIZE_FOR_DETAIL%>"
			Cache.saveToTempCache(saveKey,totalSizeNode)
        endfunc
        
        func ShowDetail_M_getTotalSize()
            int totalSize = 0
            String saveKey = "<%=Constant.StorageKey.TOTAL_SIZE_FOR_DETAIL%>"
			TxNode totalSizeNode = Cache.getFromTempCache(saveKey)
			if NULL != totalSizeNode
			   totalSize = TxNode.valueAt(totalSizeNode, 0)
			endif
			return totalSize
        endfunc
        
        func ShowDetail_M_ChangeTotalIndexToIndex(int totalIndex)
            int indexInList = totalIndex
            int sponsorSize = ShowDetail_M_getSponsorSizeForDetail()
            if 0 == sponsorSize
               return indexInList
            elsif 0 == totalIndex%10
               int pageIndex = totalIndex/10
               return pageIndex%sponsorSize
            endif
            int index = indexInList - 1 - indexInList/10
            return index
        endfunc
        
        func ShowDetail_M_getPoiList()
	        String saveKey = "<%=Constant.StorageKeyForJSON.JSON_ARRAY_POI_LIST%>"
			JSONArray poiList = Cache.getJSONArrayFromTempCache(saveKey)
			return poiList
	    endfunc
	    
	    func ShowDetail_M_getSponsorPoiList()
	       String saveKey = "<%=Constant.StorageKeyForJSON.JSON_ARRAY_SPONSOR_POI_LIST%>"
		   JSONArray sponsorPoiList = Cache.getJSONArrayFromTempCache(saveKey)
		   return sponsorPoiList
	    endfunc
	    
	    func ShowDetail_M_saveShowTabIndex(int tabIndex)
            TxNode tabIndexNode
            TxNode.addValue(tabIndexNode,tabIndex)
            String saveKey = "<%=Constant.StorageKey.DETAIL_TAB_INDEX%>"
			Cache.saveToTempCache(saveKey,tabIndexNode)
        endfunc
        
        func ShowDetail_M_getShowTabIndex()
            int tabIndex = 0
            String saveKey = "<%=Constant.StorageKey.DETAIL_TAB_INDEX%>"
            TxNode tabIndexNode = Cache.getFromTempCache(saveKey)
            if NULL != tabIndexNode
               tabIndex = TxNode.valueAt(tabIndexNode,0)
            endif
            return tabIndex
        endfunc
        
        func ShowDetail_M_deleteShowTabIndex()
            String saveKey = "<%=Constant.StorageKey.DETAIL_TAB_INDEX%>"
            Cache.deleteFromTempCache(saveKey)
        endfunc
	    
		#TODO: put it to another place
		func SearchPoi_M_getLocation()
			String saveKey="<%=Constant.StorageKeyForJSON.JSON_OBJECT_SEARCH_POI_ADDESS%>"
			JSONObject addressJO = Cache.getJSONObjectFromTempCache(saveKey)
			return addressJO
		endfunc

		func ShowDetail_M_saveFromFlag(String from)
			TxNode node
			TxNode.addMsg(node,from)
			String saveKey = "<%=Constant.StorageKey.DETAIL_FROM_FLAG%>"
			Cache.saveToTempCache(saveKey,node)
		endfunc

		func ShowDetail_M_getFromFlag()
			String from = ""
			String saveKey = "<%=Constant.StorageKey.DETAIL_FROM_FLAG%>"
			TxNode node = Cache.getFromTempCache(saveKey)
			if NULL != node
				from = TxNode.msgAt(node,0)
			endif
		return from
		endfunc

		func ShowDetail_FromPoiList()
			if "PoiList" == ShowDetail_M_getFromFlag()
				return TRUE
			else
				return FALSE
			endif
		endfunc
	]]>
</tml:script>