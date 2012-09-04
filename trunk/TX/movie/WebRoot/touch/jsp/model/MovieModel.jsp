<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@page import="com.telenav.browser.movie.Constant" %>
<%@ include file="../Header.jsp"%>
<tml:script language="fscript" version="1">
		<![CDATA[
			# 1 - sort by Name, 0 - sort by Rating
		    func Movie_M_getSortType()
		        String saveKey = "<%=Constant.StorageKey.MOVIE_SORT_TYPE%>"
				TxNode node = Cache.getFromTempCache(saveKey)
				int type = 1
				if NULL == node
					return type
				endif
				type = TxNode.valueAt(node, 0)
				return type
		    endfunc
		    
		    func Movie_M_deleteSortType()
		        String saveKey = "<%=Constant.StorageKey.MOVIE_SORT_TYPE%>"
				Cache.deleteFromTempCache(saveKey)
		    endfunc
		    
		    func Movie_M_saveSortType(int sortType)
		        String saveKey="<%=Constant.StorageKey.MOVIE_SORT_TYPE%>"
			    TxNode node
			    TxNode.addValue(node, sortType)
			    Cache.saveToTempCache(saveKey,node)
		    endfunc
		    
		    func Movie_M_getDateIDNode()
		        String saveKey="<%=Constant.StorageKey.MOVIE_PAGE_DATE_ID%>"
				TxNode id = Cache.getFromTempCache(saveKey)
				return id
			endfunc
				
		    func Movie_M_getAddress()
		         String saveKey="<%=Constant.StorageKeyForJSON.JSON_OBJECT_SEARCH_POI_ADDESS%>"
				 JSONObject addressJO = Cache.getJSONObjectFromTempCache(saveKey)
				 return addressJO
		    endfunc
		    
		    func Movie_M_saveCurrentLocation(JSONObject addressNode) 
	        	String saveKey="<%=Constant.StorageKeyForJSON.JSON_OBJECT_SEARCH_POI_ADDESS%>"
				Cache.saveToTempCache(saveKey,addressNode) 
		    endfunc
		    
		    func Movie_M_deleteLocation()
		        String saveKey="<%=Constant.StorageKeyForJSON.JSON_OBJECT_SEARCH_POI_ADDESS%>"
		        Cache.deleteFromTempCache(saveKey)
		    endfunc
			
			func Movie_M_deleteDate()
				String saveKey="<%=Constant.StorageKey.MOVIE_PAGE_DATE_ID%>"
				Cache.deleteFromTempCache(saveKey)
			endfunc
		    
		    func Movie_M_saveBackAction(String backAction)
		        TxNode backActionNode
		        TxNode.addMsg(backActionNode,backAction)
		        String saveKey = "<%=Constant.StorageKey.BACK_ACTION_MOVIE%>"
		        Cache.saveToTempCache(saveKey,backActionNode)
		    endfunc
		     
		    func Movie_M_getBackAction()
		        String backAction = ""
		        String saveKey = "<%=Constant.StorageKey.BACK_ACTION_MOVIE%>"
		        TxNode backActionNode = Cache.getFromTempCache(saveKey)
		        if NULL != backActionNode
		           backAction = TxNode.msgAt(backActionNode,0)
		        endif
		        return backAction
		    endfunc
		     
		    func Movie_M_deleteBackAction()
		        String saveKey = "<%=Constant.StorageKey.BACK_ACTION_MOVIE%>"
		        Cache.deleteFromTempCache(saveKey)
		    endfunc
		]]>
</tml:script>