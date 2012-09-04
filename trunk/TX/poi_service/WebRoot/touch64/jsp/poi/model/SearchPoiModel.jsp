<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>

<%@page import="com.telenav.cserver.poi.model.PoiListModel"%><tml:script language="fscript" version="1">
	<![CDATA[
	    func SearchPoi_M_saveLocation(JSONObject addressNode)
	         String saveKey="<%=Constant.StorageKeyForJSON.JSON_OBJECT_SEARCH_POI_ADDESS%>"
			 Cache.saveToTempCache(saveKey,addressNode) 
	    endfunc
	    
	    func SearchPoi_M_getCategoryName()
	         String saveKey="<%=Constant.StorageKey.SEARCH_POI_CATEGORY_NAME%>"
			 TxNode nameNode=Cache.getFromTempCache(saveKey)
			 String categoryName = ""
			 if nameNode != NULL
			 	categoryName = TxNode.msgAt(nameNode,0)
			 endif
			 return categoryName
	    endfunc
	    
	    func SearchPoi_M_getLocation()
	         String saveKey="<%=Constant.StorageKeyForJSON.JSON_OBJECT_SEARCH_POI_ADDESS%>"
			 JSONObject addressJO = Cache.getJSONObjectFromTempCache(saveKey)
			 return addressJO
	    endfunc
	    
	    func SearchPoi_M_saveCategory(String name,String isMostPopular,TxNode idNode)
	        String saveKey="<%=Constant.StorageKey.SEARCH_POI_CATEGORY_NAME%>"
		    if NULL != name
		        if "" != name
		           TxNode nameTxNode
		           TxNode.addMsg(nameTxNode,name)
		           Cache.saveToTempCache(saveKey,nameTxNode)
		        endif
		    endif
		    
		    if NULL != isMostPopular
		       if "" != isMostPopular
					<%=PoiListModel.setMostPopular("isMostPopular")%>
		        endif
		    endif
		    
		    if NULL != idNode
		        String categoryId = TxNode.msgAt(idNode,0)
		        println("save categoryId="+categoryId)
		        if NULL == categoryId || "" == categoryId
		           categoryId = "-1"
		        endif
		        <%=PoiListModel.setCategoryId("categoryId")%>
		    endif
	    endfunc
	    
	    func SearchPoi_M_initial(int searchType)
	        <%=PoiListModel.deleteKeyWord()%>
	        
	        String saveKey="<%=Constant.StorageKey.SEARCH_POI_CATEGORY_NAME%>"
	        Cache.deleteFromTempCache(saveKey)
	        
	        <%=PoiListModel.deleteMostPopular()%>
            <%=PoiListModel.deleteCategoryId()%>
	        
	        saveKey="<%=Constant.StorageKeyForJSON.JSON_OBJECT_SEARCH_POI_ADDESS%>"
	        Cache.deleteFromTempCache(saveKey)
	        
	        <%=PoiListModel.setSearchType("searchType")%>
	    endfunc
	    
	    func SearchPoi_M_deleteLocation()
	        String saveKey="<%=Constant.StorageKeyForJSON.JSON_OBJECT_SEARCH_POI_ADDESS%>"
	        Cache.deleteFromTempCache(saveKey)
	    endfunc
	    
	    func SearchPoi_M_setLocation()
           TxNode currentLocationNode
	       currentLocationNode=ParameterSet.getParam("currentLocation")	
	       
	       if NULL != currentLocationNode
	          int lat = TxNode.valueAt(currentLocationNode,1)
	          int lon = TxNode.valueAt(currentLocationNode,2)
	          JSONObject jo
	          JSONObject.put(jo,"lat",lat)
	          JSONObject.put(jo,"lon",lon)
	          JSONObject.put(jo,"type",6)
	          SearchPoi_M_saveLocation(jo)
	       endif
        endfunc
        
        func SearchPoi_M_setSearchInformation(TxNode searchInformationNode)
           String saveKey = "<%=Constant.StorageKey.SEARCH_ALONG_INFORMATION%>"
		   Cache.saveToTempCache(saveKey,searchInformationNode)
        endfunc
        
        func SearchPoi_M_setCallBackInformation(String callBackUrl,String callBackFunction)
           TxNode urlNode
           TxNode.addMsg(urlNode,callBackUrl)
           String saveKey = "<%=Constant.StorageKey.SEARCH_CALL_BACK_PAGEURL%>"
		   Cache.saveToTempCache(saveKey,urlNode)
		   
		   TxNode funcNode
           TxNode.addMsg(funcNode,callBackFunction)
		   saveKey = "<%=Constant.StorageKey.SEARCH_CALL_BACK_FUNCTION%>"
		   Cache.saveToTempCache(saveKey,funcNode)
        endfunc
        
        func SearchPoi_M_getCallBackUrl()
           String saveKey = "<%=Constant.StorageKey.SEARCH_CALL_BACK_PAGEURL%>"
           TxNode urlNode = Cache.getFromTempCache(saveKey)
           String url = ""
           if NULL != urlNode
              url = TxNode.msgAt(urlNode,0)
           endif
           
           return url
        endfunc
        
        func SearchPoi_M_getCallBackFunction()
           String saveKey = "<%=Constant.StorageKey.SEARCH_CALL_BACK_FUNCTION%>"
           TxNode funcNode = Cache.getFromTempCache(saveKey)
           String funcName = ""
           if NULL != funcNode
              funcName = TxNode.msgAt(funcNode,0)
           endif
           
           return funcName
        endfunc
        
        func SearchPoi_M_getSearchAlongTypeNode()
           String saveKey = "<%=Constant.StorageKey.SEARCH_ALONG_TYPE%>"
           TxNode searchAlongTypeNode = Cache.getFromTempCache(saveKey)
           return searchAlongTypeNode
        endfunc
        
        func SearchPoi_M_deleteSearchAlongType()
           String saveKey = "<%=Constant.StorageKey.SEARCH_ALONG_TYPE%>"
           Cache.deleteFromTempCache(saveKey)
        endfunc
        
        func SearchPoi_M_saveBackAction(String backAction)
           TxNode backActionNode
           TxNode.addMsg(backActionNode,backAction)
           String saveKey = "<%=Constant.StorageKey.BACK_ACTION_SEARCHPOI%>"
		   Cache.saveToTempCache(saveKey,backActionNode)
        endfunc
        
        func SearchPoi_M_getBackAction()
           String backAction = ""
           String saveKey = "<%=Constant.StorageKey.BACK_ACTION_SEARCHPOI%>"
           TxNode backActionNode = Cache.getFromTempCache(saveKey)
           if NULL != backActionNode
              backAction = TxNode.msgAt(backActionNode,0)
           endif
           return backAction
        endfunc
        
        func SearchPoi_M_deleteBackAction()
           String saveKey = "<%=Constant.StorageKey.BACK_ACTION_SEARCHPOI%>"
           Cache.deleteFromTempCache(saveKey)
        endfunc
        
        func SearchPoi_M_setInput(String inputString)
           <%=PoiListModel.setKeyWord("inputString")%>
        endfunc
        
       func SearchPoi_M_saveFlowFlag(String flag)
           TxNode node
           TxNode.addMsg(node,flag)
           String saveKey = "<%=Constant.StorageKey.SEARCH_POI_FLOW_FLAG%>"
		   Cache.saveToTempCache(saveKey,node)
        endfunc
        
        func SearchPoi_M_getFlowFlag()
           String flag = ""
           String saveKey = "<%=Constant.StorageKey.SEARCH_POI_FLOW_FLAG%>"
           TxNode node = Cache.getFromTempCache(saveKey)
           if NULL != node
              flag = TxNode.msgAt(node,0)
           endif
           return flag
        endfunc

       func SearchPoi_M_saveSubCategoryName(String name)
           TxNode node
           TxNode.addMsg(node,name)
           String saveKey = "<%=Constant.StorageKey.SEARCH_POI_SUBCATEGORY_NAME%>"
		   Cache.saveToTempCache(saveKey,node)
        endfunc
        
        func SearchPoi_M_getSubCategoryName()
           String name = ""
           String saveKey = "<%=Constant.StorageKey.SEARCH_POI_SUBCATEGORY_NAME%>"
           TxNode node = Cache.getFromTempCache(saveKey)
           if NULL != node
              name = TxNode.msgAt(node,0)
           endif
           return name
        endfunc
	]]>
</tml:script>