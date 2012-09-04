<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.cserver.poi.model.PoiListModel"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<tml:script language="fscript" version="1">
	<![CDATA[
	    func OneBox_M_resetSearchType()
			int searchType = 5
			<%=PoiListModel.setSearchType("searchType")%>
		endfunc
		
		func OneBox_M_initialForPOI()
		    String from = OneBox_M_getPageFrom()
		    
		    if "POI" != from
			    String saveKey="<%=Constant.StorageKey.SEARCH_POI_CATEGORY_NAME%>"
		        Cache.deleteFromTempCache(saveKey)
		        
		        <%=PoiListModel.deleteMostPopular()%>
	            <%=PoiListModel.deleteCategoryId()%>
            endif
		endfunc
	
	    func OneBox_M_saveResentSearch(String inputString)
	         if "" == inputString
	             return FAIL
	         endif
	         TxNode oldResentSearchNode 
	         oldResentSearchNode = OneBox_M_getResentSearch()
             
             TxNode resentSearchNode
             TxNode.addMsg(resentSearchNode, inputString)
             
             if NULL != oldResentSearchNode
                  int size = TxNode.getStringSize(oldResentSearchNode)
				  int recentSearchSize = <%=Constant.RECENT_SEARCH_SIZE%>
				  if size > recentSearchSize
					size = recentSearchSize
				  endif   
				  int i = 0
				  String oldInput = ""
				  while i < size
				    oldInput = TxNode.msgAt(oldResentSearchNode, i)
					if inputString != oldInput
					   if recentSearchSize > TxNode.getStringSize(resentSearchNode)
						   TxNode.addMsg(resentSearchNode, oldInput)
					   endif	
					endif
					i = i + 1
				  endwhile
             endif  
             
             String saveKey = "<%=Constant.StorageKey.RESENT_SEARCH_STRING%>"
             Cache.saveCookie(saveKey,resentSearchNode)   
		endfunc
		    
	    func OneBox_M_deleteResentSearch()
	         String saveKey = "<%=Constant.StorageKey.RESENT_SEARCH_STRING%>"
             Cache.deleteCookie(saveKey)   
	    endfunc
	    
	    func OneBox_M_getResentSearch()
	         String saveKey="<%=Constant.StorageKey.RESENT_SEARCH_STRING%>"
	         TxNode resentSearchNode
	         resentSearchNode = Cache.getCookie(saveKey)
	         return resentSearchNode
	    endfunc  
	    
	    func OneBox_M_setHotBrandNode(TxNode hotBrandNode)
	        String saveKey = "<%=Constant.StorageKey.HOT_BRAND_NODE%>"
            Cache.saveCookie(saveKey,hotBrandNode)  
	    endfunc   

		func OneBox_M_getDispayString()
			TxNode node = Cache.getFromTempCache("<%=Constant.StorageKey.ONEBOX_DISPLAY_TEXT%>")
			return TxNode.msgAt(node,0) 
		endfunc
		
		func OneBox_M_saveDispayString(String displayString)
			TxNode node
			TxNode.addMsg(node,displayString)
			Cache.saveToTempCache("<%=Constant.StorageKey.ONEBOX_DISPLAY_TEXT%>",node)		
		endfunc
	    
	    func OneBox_M_saveAcParam(JSONObject jo)
    		string invokerPageURL = JSONObject.getString(jo,"callbackpageurl")
			string callbackFunction = JSONObject.getString(jo,"callbackfunction")
			string from = JSONObject.getString(jo,"from")
			OneBox_M_setCallBackInformation(invokerPageURL,callbackFunction)
			#AddressCapture_M_initAsIs(invokerPageURL,callbackFunction,from,1)
			#To avoid include AddressCapture module, copy the code here
			OneBox_AddressCapture_M_initAsIs(invokerPageURL,callbackFunction,from,1)
	    endfunc

		func OneBox_AddressCapture_M_initAsIs(String invokerPageURL, String callbackFunction, String from, int asIs)
			if from == ""
				from = "Common"
			endif
			JSONObject joTemp
			JSONObject joTemp1 = Cache.getJSONObjectFromTempCache("<%=Constant.StorageKey.ADDRESS_CAPTURE_PARAMETER%>")
			if joTemp1 != NULL
				joTemp = joTemp1
			endif

			JSONObject jo
			JSONObject.put(jo,"callbackfunction",callbackFunction)
			JSONObject.put(jo,"callbackpageurl",invokerPageURL)
			if asIs > 0
				JSONObject.put(jo,"returnAsIs","1")
			endif
	    	JSONObject.put(joTemp,from,jo)
	    	
	        Cache.saveToTempCache("<%=Constant.StorageKey.ADDRESS_CAPTURE_PARAMETER%>",joTemp)
		endfunc
				
		func OneBox_M_getPageFrom()
			string from = Page.getControlProperty("page","url_flag")
			if from == NULL || from == ""
				from = "Common"
			endif
			return from
		endfunc

        func OneBox_M_setCallBackInformation(String callBackUrl,String callBackFunction)
           TxNode urlNode
           TxNode.addMsg(urlNode,callBackUrl)
           String saveKey = "<%=Constant.StorageKey.ONEBOX_POI_CALL_BACK_PAGEURL%>"
		   Cache.saveToTempCache(saveKey,urlNode)
		   
		   TxNode funcNode
           TxNode.addMsg(funcNode,callBackFunction)
		   saveKey = "<%=Constant.StorageKey.ONEBOX_POI_CALL_BACK_FUNCTION%>"
		   Cache.saveToTempCache(saveKey,funcNode)
        endfunc
        
        func OneBox_M_getCallBackUrl()
           String saveKey = "<%=Constant.StorageKey.ONEBOX_POI_CALL_BACK_PAGEURL%>"
           TxNode urlNode = Cache.getFromTempCache(saveKey)
           String url = ""
           if NULL != urlNode
              url = TxNode.msgAt(urlNode,0)
           endif
           return url
        endfunc
        
        func OneBox_M_getCallBackFunction()
           String saveKey = "<%=Constant.StorageKey.ONEBOX_POI_CALL_BACK_FUNCTION%>"
           TxNode funcNode = Cache.getFromTempCache(saveKey)
           String funcName = ""
           if NULL != funcNode
              funcName = TxNode.msgAt(funcNode,0)
           endif
           return funcName
        endfunc
        
        func OneBox_M_saveTransactionId(String transactionId)
           TxNode node
		   TxNode.addMsg(node,transactionId)
		   Cache.saveToTempCache("<%=Constant.StorageKey.ONEBOX_TRANSACTION_ID%>",node)
        endfunc
        
        func OneBox_M_getTransactionId()
           String transactionId = ""
           TxNode node = Cache.getFromTempCache("<%=Constant.StorageKey.ONEBOX_TRANSACTION_ID%>")
           if NULL != node
              transactionId = TxNode.msgAt(node,0)
           endif
           
           return transactionId
        endfunc
	]]>
</tml:script>