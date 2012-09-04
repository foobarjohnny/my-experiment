<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ include file="../Header.jsp"%>
<%@page import="com.telenav.browser.movie.Constant" %>

<%@include file="../DateUtil.jsp" %>

<tml:script language="fscript" version="1">
		<![CDATA[
			func MovieList_M_saveAjaxParams(TxNode inputString, JSONObject address, TxNode dateIndex)
		        String saveKey="<%=Constant.StorageKey.MOVIE_PAGE_KEYWORD%>"
			    Cache.saveToTempCache(saveKey,inputString)
	        	saveKey="<%=Constant.StorageKeyForJSON.JSON_OBJECT_SEARCH_POI_ADDESS%>"
				Cache.saveToTempCache(saveKey,address) 
		        saveKey="<%=Constant.StorageKey.MOVIE_PAGE_DATE_ID%>"
			    Cache.saveToTempCache(saveKey,dateIndex)
			endfunc
			
			func MovieList_M_getInputString()
			    String saveKey = "<%=Constant.StorageKey.MOVIE_PAGE_KEYWORD%>"
				TxNode input = Cache.getFromTempCache(saveKey)
				String res = ""
				if NULL != input
					res = TxNode.msgAt(input,0)
				endif
				return res
			endfunc
			
		    func MovieList_M_getAddress()
		         String saveKey="<%=Constant.StorageKeyForJSON.JSON_OBJECT_SEARCH_POI_ADDESS%>"
				 JSONObject addressJO = Cache.getJSONObjectFromTempCache(saveKey)
				 return addressJO
		    endfunc
		    
		    func MovieList_M_getDateID()
		        String saveKey="<%=Constant.StorageKey.MOVIE_PAGE_DATE_ID%>"
				TxNode id = Cache.getFromTempCache(saveKey)
				String res = "0"
				if NULL != id
					res = TxNode.msgAt(id,0)
				endif
				return res
		    endfunc
		    
		    func MovieList_M_getDateIDNode()
		        String saveKey="<%=Constant.StorageKey.MOVIE_PAGE_DATE_ID%>"
				TxNode id = Cache.getFromTempCache(saveKey)
				return id
			endfunc	

			# 1 - sort by Name, 0 - sort by Rating - TODO - add more sort types
		    func MovieList_M_getNewSortBy()
		        String saveKey = "<%=Constant.StorageKey.MOVIE_NEW_SORT_BY%>"
				TxNode node = Cache.getFromTempCache(saveKey)
				String type = "<%= Constant.SORT_BY_NAME %>"
				if NULL == node
					return type
				endif
				type = TxNode.msgAt(node, 0)
				return type
		    endfunc
		    
		    func MovieList_M_deleteNewSortBy()
		        String saveKey = "<%=Constant.StorageKey.MOVIE_NEW_SORT_BY%>"
				Cache.deleteFromTempCache(saveKey)
		    endfunc
		    
		    func MovieList_M_saveNewSortBy(String newSortBy)
		        String saveKey="<%=Constant.StorageKey.MOVIE_NEW_SORT_BY%>"
			    TxNode node
			    TxNode.addMsg(node, newSortBy)
			    Cache.saveToTempCache(saveKey,node)
		    endfunc

		    func MovieList_M_setNewMovieList(JSONArray holder)
		        String saveKey="<%=Constant.StorageKeyForJSON.MOVIE_LIST_ARRAY_JSON%>"
		    	Cache.saveToTempCache(saveKey, holder)
		    endfunc
		    
		    func MovieList_M_getMovieList()
		        String saveKey="<%=Constant.StorageKeyForJSON.MOVIE_LIST_ARRAY_JSON%>"
		    	JSONArray holder = Cache.getJSONArrayFromTempCache(saveKey)
		    	return holder
		    endfunc
		    
		    func MovieList_M_getNumberOfBatches()
		        String saveKey="<%=Constant.StorageKeyForJSON.MOVIE_LIST_ARRAY_JSON%>"
		    	JSONArray holder = Cache.getJSONArrayFromTempCache(saveKey)
		    	return JSONArray.length(holder)
		    endfunc
		    
		    func MovieList_M_getMovieListByBatch(int batchNumber)
		        String saveKey="<%=Constant.StorageKeyForJSON.MOVIE_LIST_ARRAY_JSON%>"
		    	JSONArray holder = Cache.getJSONArrayFromTempCache(saveKey)
		    	int idx = batchNumber-1
		    	JSONArray batch = JSONArray.get(holder, idx)
		    	return batch
		    endfunc
		    
		    func MovieList_M_saveBatchNumber(int batchNumber)
		    	TxNode batchNode
		    	TxNode.addValue(batchNode, batchNumber)
		        String saveKey="<%=Constant.StorageKey.MOVIE_LIST_BATCH_NUMBER%>"
		    	Cache.saveToTempCache(saveKey, batchNode)
		    endfunc
		    
		    func MovieList_M_getBatchNumber()
		        String saveKey="<%=Constant.StorageKey.MOVIE_LIST_BATCH_NUMBER%>"
		    	TxNode batchNode = Cache.getFromTempCache(saveKey)
		    	int batchNumber = TxNode.valueAt(batchNode, 0)
		    	return batchNumber 
		    endfunc
		    
		    func MovieList_M_saveCurrentPage(int startIndex)
		        TxNode currentPageNode
				TxNode.addValue(currentPageNode, startIndex)
		        String saveKey="<%=Constant.StorageKey.MOVIE_LIST_CURRENT_PAGE%>"
			    Cache.saveToTempCache(saveKey,currentPageNode) 
		    endfunc
		    
		    func MovieList_M_getCurrentPage()
		        String saveKey="<%=Constant.StorageKey.MOVIE_LIST_CURRENT_PAGE%>"
		        TxNode currentPageNode = Cache.getFromTempCache(saveKey)
		    	int pageNumber = TxNode.valueAt(currentPageNode, 0)
		    	return pageNumber 
		    endfunc
		    
			func MovieList_M_searchMovieWithAjax(int batchNumber, int sortByName, String newSortBy)
				String inputString = MovieList_M_getInputString()
                
                #TODO calculate batch size from device preferences as
                # twice more then shown on screen
                int batchSize = 10*<%=Constant.BATCH_MULTIPLIER%>
                
				JSONObject addressJO
				addressJO = MovieList_M_getAddress()
				String addressString = ""
				if NULL != addressJO
				       addressString = JSONObject.toString(addressJO)
				else
					System.showErrorMsg("Address is not defined.")
					return FAIL
				endif
				
				String dateIndex = MovieList_M_getDateID()
				int idx = String.convertToNumber(dateIndex)
				dateIndex = getDateStr(idx)
				
				MovieList_M_saveBatchNumber(batchNumber)
				
				TxNode distanceUnitNode = Preference.getPreferenceValue(1)
				int distanceUnit = <%=Constant.DUNIT_MILES%>
				if NULL != distanceUnitNode
				   distanceUnit = TxNode.valueAt(distanceUnitNode,0)
				endif
	            
	            TxNode node
				JSONObject jo
				   JSONObject.put(jo,"<%= Constant.RRKey.MS_INPUT_STRING%>",inputString)
                   JSONObject.put(jo,"<%= Constant.RRKey.MS_BATCH_NUMBER%>",batchNumber)
                   if sortByName == 1
                   		JSONObject.put(jo,"<%= Constant.RRKey.MS_SORT_BY_NAME%>","true")
                   else
                   		JSONObject.put(jo,"<%= Constant.RRKey.MS_SORT_BY_NAME%>","false")
                   endif
                   if newSortBy != NULL
                   		JSONObject.put(jo,"<%= Constant.RRKey.MS_NEW_SORT_BY%>", newSortBy)
                   endif
                   JSONObject.put(jo,"<%= Constant.RRKey.MS_BATCH_SIZE%>", batchSize)
                   JSONObject.put(jo,"<%= Constant.RRKey.MS_ADDRESS%>",addressString)
                   JSONObject.put(jo,"<%= Constant.RRKey.MS_DATE_INDEX%>",dateIndex)
                   JSONObject.put(jo,"<%= Constant.RRKey.MS_DISTANCE_UNIT%>",distanceUnit)
                   String strJo=JSONObject.toString(jo)
                   TxNode.addMsg(node,strJo)
				TxRequest req
				String url="<%=host + "/queryMovieList.do"%>"
				String scriptName="movieListCallback"
				TxRequest.open(req,url)
				TxRequest.setRequestData(req,node)
				TxRequest.onStateChange(req,scriptName)
				TxRequest.setProgressTitle(req,"<%=msg.get("movie.searching")%>")
				TxRequest.send(req)
			endfunc
			
			func movieListCallback(TxNode node,int status)
				int batchNumber = MovieList_M_getBatchNumber()
	            if status == 0
	            	rollbackBatchNumber(batchNumber)
	                System.showErrorMsg("<%=msg.get("err.internal")%>")
				    return FAIL
				elseif status == 1
					#movie json
					int returnCode = TxNode.valueAt(node,0)
		            String movieJoString=TxNode.msgAt(node,0)
		            
		            if returnCode != 1
	                	System.showErrorMsg(movieJoString)
				    	return FAIL
		            endif
		            
		            JSONArray  movieJo=JSONArray.fromString(movieJoString)
		            int resultListSize=JSONArray.length(movieJo)
				    
				    String label = "<%=msg.get("err.noMovie.1")%>"
				    if batchNumber > 1
				    	label = "<%=msg.get("err.noMovie.2")%>"	
				    endif
				    
				    if NULL == movieJo
				       rollbackBatchNumber(batchNumber)
				       System.showErrorMsg(label)
				       return FAIL
				    endif
				    if resultListSize <=0
					   rollbackBatchNumber(batchNumber)				       
				       System.showErrorMsg(label)
				       return FAIL
				    endif
				    
				    #set new holder if it is first batch
					JSONArray holder 
					if batchNumber == 1
						JSONArray.put(holder, movieJo)
						MovieList_M_setNewMovieList(holder)
					else
						holder = MovieList_M_getMovieList()
						JSONArray.put(holder, movieJo)
					endif
					MovieList_M_saveCurrentPage(1)
					
					#TODO retrieve it from preferences
					int itemsOnScreen = 10
					MovieList_C_showMovieList(itemsOnScreen)
					
				endif
			endfunc
			
			func rollbackBatchNumber(int batchNumber)
				if batchNumber > 1
					MovieList_M_saveBatchNumber(batchNumber-1)
				endif
			endfunc
		]]>
</tml:script>