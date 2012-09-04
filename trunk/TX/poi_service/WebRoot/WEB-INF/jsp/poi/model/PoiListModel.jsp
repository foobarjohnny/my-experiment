<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@page import="com.telenav.cserver.util.FeatureConstant"%>
<%@page import="com.telenav.cserver.stat.*"%>

<%@page
	import="com.telenav.cserver.pref.struts.util.PreferenceConstants"%>
<%
    String poiPageIndexKey = Constant.StorageKey.MAIN_PAGE_POI_PAGE_INDEX;
    String poiSortTypeKey = Constant.StorageKey.MAIN_PAGE_POI_SORT_TYPE;
    String poiSortTypeSpareKey = Constant.StorageKey.POI_SORT_TYPE_SPARE;
    String searchStringKey = Constant.StorageKey.SEARCH_POI_INPUTSTRING;
    String stopAudioFlagKey = Constant.StorageKey.STOP_AUDIO_FLAG;
    String temp = Constant.StorageKey.POI_LIST_CURRENT_SIZE;
    String isMostPopular = Constant.StorageKey.IS_MOST_POPULAR;
    String currentPageKey = Constant.StorageKey.CURRENT_PAGE;
    String categoryIdKey = Constant.StorageKey.SEARCH_POI_CATEGORY_ID;
    String searchFromTypeKey = Constant.StorageKey.SEARCH_FROM_TYPE;
%>
<%
    String poiListKey = Constant.StorageKeyForJSON.JSON_ARRAY_POI_LIST;
    String sponsorPoiListKey = Constant.StorageKeyForJSON.JSON_ARRAY_SPONSOR_POI_LIST;
    String audioSaveKey = Constant.StorageKey.POI_AUDIO_SAVE;
    String audioMulResIndexKey = Constant.StorageKey.AUDIO_MULTIPLE_RESULT_INDEX;
    String searchAddessKey = Constant.StorageKeyForJSON.JSON_OBJECT_SEARCH_POI_ADDESS;
%>
<%@include file="/WEB-INF/jsp/StopUtil.jsp"%>
<%@page import="com.telenav.cserver.poi.model.PoiListModel"%>
<%@ include file="../../PoiUtil.jsp"%>
<tml:script language="fscript" version="1">
	<![CDATA[
			func PoiList_M_getSearchInformation()
	           String saveKey = "<%=Constant.StorageKey.SEARCH_ALONG_INFORMATION%>"
			   TxNode searchInformationNode = Cache.getFromTempCache(saveKey)
			   return searchInformationNode
	        endfunc
	        
	        func PoiList_M_getCallBackUrl()
	           TxNode urlNode
	           String saveKey = "<%=Constant.StorageKey.SEARCH_CALL_BACK_PAGEURL%>"
	           urlNode = Cache.getFromTempCache(saveKey)
	           String url = ""
	           if NULL != urlNode
	              url = TxNode.msgAt(urlNode,0)
	           endif
	           return url
	        endfunc
	        
	        func PoiList_M_setCallBackInformation(String callBackUrl,String callBackFunction)
	           TxNode urlNode
	           TxNode.addMsg(urlNode,callBackUrl)
	           String saveKey = "<%=Constant.StorageKey.SEARCH_CALL_BACK_PAGEURL%>"
			   Cache.saveToTempCache(saveKey,urlNode)
			   
			   TxNode funcNode
	           TxNode.addMsg(funcNode,callBackFunction)
			   saveKey = "<%=Constant.StorageKey.SEARCH_CALL_BACK_FUNCTION%>"
			   Cache.saveToTempCache(saveKey,funcNode)
	        endfunc
	        
	        func PoiList_M_getCallBackFunc()
	           TxNode funcNode
	           String saveKey = "<%=Constant.StorageKey.SEARCH_CALL_BACK_FUNCTION%>"
	           funcNode = Cache.getFromTempCache(saveKey)
	           String funcName = ""
	           if NULL != funcNode
	              funcName = TxNode.msgAt(funcNode,0)
	           endif
	           return funcName
	        endfunc
            
            func PoiList_M_saveAudioFinishFlag(int audioFinishFlag)
               TxNode audioFinishFlagNode
               TxNode.addValue(audioFinishFlagNode,audioFinishFlag)
               String saveKey = "<%=Constant.StorageKey.AUDIO_ALREADY_FINISH_FLAG%>"
			   Cache.saveToTempCache(saveKey,audioFinishFlagNode)
            endfunc	
            
            func PoiList_M_getAudioFinishFlag()
               int audioFinishFlag = 0
               String saveKey = "<%=Constant.StorageKey.AUDIO_ALREADY_FINISH_FLAG%>"
               TxNode audioFinishFlagNode = Cache.getFromTempCache(saveKey)
               if NULL != audioFinishFlagNode
                  audioFinishFlag = TxNode.valueAt(audioFinishFlagNode,0)
               endif
               
			   return audioFinishFlag
            endfunc	
            
            func PoiList_M_saveSearchPoiSuccessFlag(int searchPoiSuccessFlag)
               TxNode searchPoiSuccessFlagNode
               TxNode.addValue(searchPoiSuccessFlagNode,searchPoiSuccessFlag)
               String saveKey = "<%=Constant.StorageKey.SEARCH_POI_SUCCESS_FLAG%>"
			   Cache.saveToTempCache(saveKey,searchPoiSuccessFlagNode)
            endfunc	
            
            func PoiList_M_getSearchPoiSuccessFlag()
               int searchPoiSuccessFlag = 0
               String saveKey = "<%=Constant.StorageKey.SEARCH_POI_SUCCESS_FLAG%>"
               TxNode searchPoiSuccessFlagNode = Cache.getFromTempCache(saveKey)
               if NULL != searchPoiSuccessFlagNode
                  searchPoiSuccessFlag = TxNode.valueAt(searchPoiSuccessFlagNode,0)
               endif
               
			   return searchPoiSuccessFlag
            endfunc	
            
            func PoiList_M_saveSponsorPoiList(JSONArray sponsorPoiList)
               String saveKey = "<%=sponsorPoiListKey%>"
			   Cache.saveToTempCache(saveKey, sponsorPoiList)
            endfunc
            
            func PoiList_M_getSponsorPoiList()
               String saveKey = "<%=sponsorPoiListKey%>"
			   JSONArray sponsorPoiList = Cache.getJSONArrayFromTempCache(saveKey)
			   return sponsorPoiList
            endfunc
            
            func PoiList_M_deleteSponsorPoiList()
               String saveKey = "<%=sponsorPoiListKey%>"
			   Cache.deleteFromTempCache(saveKey)
            endfunc
            
            func PoiList_M_saveSearchAlongType(int searchAlongType)
	           TxNode searchAlongTypeNode
	           TxNode.addValue(searchAlongTypeNode,searchAlongType)
	           String saveKey = "<%=Constant.StorageKey.SEARCH_ALONG_TYPE%>"
			   Cache.saveToTempCache(saveKey,searchAlongTypeNode)
	        endfunc
	        
	        func PoiList_M_getSearchAlongType()
	           int searchAlongType = <%=Constant.searchAlongType_closeAhead%>
	           String saveKey = "<%=Constant.StorageKey.SEARCH_ALONG_TYPE%>"
	           TxNode searchAlongTypeNode = Cache.getFromTempCache(saveKey)
	           if NULL != searchAlongTypeNode
	              searchAlongType = TxNode.valueAt(searchAlongTypeNode,0)
	           endif
	           return searchAlongType
	        endfunc
	        
		    func PoiList_M_getResentSearch()
		         String saveKey="<%=Constant.StorageKey.RESENT_SEARCH_STRING%>"
		         TxNode resentSearchNode
		         resentSearchNode = Cache.getCookie(saveKey)
		         return resentSearchNode
		    endfunc
		    
		    func PoiList_M_deleteCategoryNameForDsr()
		        String saveKey="<%=Constant.StorageKey.SEARCH_POI_CATEGORY_NAME%>"
	            Cache.deleteFromTempCache(saveKey)
		    endfunc
		    
		    func PoiList_M_saveCategoryNameForDsr(String name)
		        String saveKey="<%=Constant.StorageKey.SEARCH_POI_CATEGORY_NAME%>"
	            TxNode nameTxNode
	            TxNode.addMsg(nameTxNode,name)
	            Cache.saveToTempCache(saveKey,nameTxNode)
		    endfunc
		    
	        func misLogSearchRequest(int searchType, JSONObject addressJO, String searchUID, int sortType)
			    if StatLogger.isStatEnabled(<%=EventTypes.POI_SEARCH_REQUEST%>)
			    	JSONObject statEvent
					if <%=PoiListModel.SearchType.SEARCH_ALONGROUTE%> == searchType
						JSONObject.put(statEvent, "<%=AttributeID.SEARCH_AREA%>", "<%=AttributeValues.SEARCH_AREA_ALONG_ROUTE%>")
						JSONObject.put(statEvent, "<%=AttributeID.ADDRESS_TYPE%>", "<%=AttributeValues.AT_ALONG_ROUTE%>")
					else
				    	int addrType = JSONObject.getInt(addressJO,"type")
						if addrType == 6
							JSONObject.put(statEvent, "<%=AttributeID.SEARCH_AREA%>", "<%=AttributeValues.SEARCH_AREA_CURRENT_LOCATION%>")
							JSONObject.put(statEvent, "<%=AttributeID.ADDRESS_TYPE%>", "<%=AttributeValues.AT_CURRENT_LOCATION%>")
						else
							StatLogger_CM_addAddressDetails(getStopType(), statEvent)
							JSONObject.put(statEvent, "<%=AttributeID.SEARCH_AREA%>", "<%=AttributeValues.SEARCH_AREA_ADDRESS%>")
							JSONObject.put(statEvent, "<%=AttributeID.STREET%>", JSONObject.getString(addressJO, "firstLine"))
							String zip = JSONObject.get(addressJO, "zip") 
							if zip != ""
								JSONObject.put(statEvent, "<%=AttributeID.ZIP%>", zip)
							else
								JSONObject.put(statEvent, "<%=AttributeID.CITY%>", JSONObject.getString(addressJO, "city"))
								JSONObjec t.put(statEvent, "<%=AttributeID.STATE%>", JSONObject.getString(addressJO, "state"))
							endif
						endif
					endif
					JSONObject.put(statEvent, "<%=AttributeID.SEARCH_UID%>", searchUID)
					JSONObject.put(statEvent, "<%=AttributeID.POI_SORTING%>", sortType)
					StatLogger.logEvent(<%=EventTypes.POI_SEARCH_REQUEST%>, statEvent)
			    endif
			    if <%=PoiListModel.SearchType.SEARCH_ALONGROUTE%> == searchType
			    	StatLogger.addCount(<%=EventTypes.POI_SEARCH_ALONG_COUNT%>)
			    endif
	        endfunc
	        
		    func getStopType()
		        String saveKey="<%=Constant.StorageKey.STOP_TYPE%>"
		        int stopType = <%=Constant.STOP_GENERIC%>
		        TxNode stopTypeNode = Cache.getFromTempCache(saveKey)
		        if NULL != stopTypeNode
		            stopType = TxNode.valueAt(stopTypeNode,0)
		        endif
		        return stopType
		    endfunc	
		    
		    func PoiList_M_getLoggedPages()
		    	JSONObject result = Cache.getJSONObjectFromTempCache("##POI_LIST_LOGGED_PAGES")
		    	return result
		    endfunc     
            
            func PoiList_M_resetLoggedPages()
               JSONObject empty
               JSONObject.put(empty, "dummy", "y")
			   Cache.saveToTempCache("##POI_LIST_LOGGED_PAGES", empty)
            endfunc
		]]>
</tml:script>

<tml:script language="fscript" version="1">
	<![CDATA[		    
		    func PoiList_M_getPoiList()
		        String saveKey = "<%=poiListKey%>"
				JSONArray poiList = Cache.getJSONArrayFromTempCache(saveKey)
				return poiList
		    endfunc
		    
			func PoiList_M_saveAudio(TxNode audioNode)
			    String audioKey = "<%=audioSaveKey%>"
			    if NULL != audioNode
				    Cache.saveToTempCache(audioKey,audioNode)
				endif
			endfunc
		    
		    func PoiList_M_savePoiList(JSONArray resultList)
			    String poiListKey = "<%=poiListKey%>"
			    Cache.saveToTempCache(poiListKey, resultList)
			endfunc
			
			#From POI List,the address showing in one row
			func PoiList_M_getWrapAddressFromPOIList_JSON(JSONObject stop)
			    String addressStr = ""
				if NULL != stop
				    String firstLine = JSONObject.get(stop,"firstLine")
				    if NULL != firstLine
				       addressStr = addressStr + firstLine
				    endif
				    String secondLine = STOP_getSecondLine_JSON(stop)
					if "" != addressStr && "" != secondLine
						addressStr = addressStr + ", " + secondLine
					endif
				endif
				return addressStr
			endfunc
			
			func PoiList_M_getAddress()
		         String saveKey="<%=searchAddessKey%>"
				 JSONObject addressJO = Cache.getJSONObjectFromTempCache(saveKey)
				 return addressJO
		    endfunc
			
			func PoiList_M_saveLocation(JSONObject addressNode)
		         String saveKey="<%=searchAddessKey%>"
				 Cache.saveToTempCache(saveKey,addressNode) 
		    endfunc
		    
		    func PoiList_M_searchPoiWithAjax()
				String inputString = <%=PoiListModel.getKeyWord()%>
				int currentPageInt = <%=PoiListModel.getPageIndexTemp()%>
				String currentPage = currentPageInt+""
				String categoryId = <%=PoiListModel.getCategoryId()%>
				String isMostPopular = <%=PoiListModel.getMostPopular()%>
				int maxResults = <%=Constant.POI_SEARCH_MAX_SIZE%>
				int allPoiMaxResult = <%=Constant.ALL_POI_MAX_SIZE%>
				int maxSize = <%=PoiListModel.getPoiCount()%>
				int surplusSize = allPoiMaxResult - maxSize
				if surplusSize < maxResults && 0 != currentPageInt
				   maxResults = surplusSize
				endif
				
				int sortType = <%=PoiListModel.getSortTypeTemp()%>
				String sortTypeStr = sortType + ""
				JSONObject addressJO
				addressJO = PoiList_M_getAddress()
				String addressString = ""
				if NULL != addressJO
				   addressString = JSONObject.toString(addressJO)
				endif
				
				int audioType = <%=PoiListModel.getAudioType()%>
				String audioTypeStr = audioType + ""
				
				int searchType = <%=PoiListModel.getSearchType()%>
				#100 = searchType is search for Businesses,but is still search poi
                if 100 == searchType
                   searchType = <%=PoiListModel.SearchType.SEARCH_ADDRESS%>
                endif
				String searchTypeStr = searchType + ""
				
				TxNode distanceUnitNode = Preference.getPreferenceValue(<%=PreferenceConstants.KEY_GENERAL_DISTANCEUNITS%>)
				int distanceUnit = 0
				if NULL != distanceUnitNode
				   distanceUnit = TxNode.valueAt(distanceUnitNode,0)
				endif
				
				String from = <%=PoiListModel.getInputType()%>
				JSONObject jo
				   JSONObject.put(jo,"inputString",inputString)
                   JSONObject.put(jo,"currentPage",currentPage)
                   JSONObject.put(jo,"maxResults",maxResults)
                   JSONObject.put(jo,"categoryId",categoryId)
                   JSONObject.put(jo,"sortType",sortTypeStr)
                   JSONObject.put(jo,"addressString",addressString)
                   JSONObject.put(jo,"searchFromType",audioTypeStr)
                   JSONObject.put(jo,"distanceUnit",distanceUnit)
                   JSONObject.put(jo,"from",from)
                   JSONObject.put(jo,"isMostPopular",isMostPopular)
                   JSONObject.put(jo,"searchTypeStr",searchTypeStr)
                   
                   #search along
				   if 7 == searchType
				      TxNode searchInformationNode = PoiList_M_getSearchInformation()
				      if NULL != searchInformationNode
				         TxNode childNode = TxNode.childAt(searchInformationNode,0)
				         String routeID = TxNode.msgAt(childNode,0)
				         String segmentId = TxNode.msgAt(childNode,1)
				         String edgeId = TxNode.msgAt(childNode,2)
				         String shapePointId = TxNode.msgAt(childNode,3)
				         String range = TxNode.msgAt(childNode,4)
				         
				         TxNode orgNode = TxNode.childAt(searchInformationNode,1)
				         int lat = TxNode.valueAt(orgNode,1)
				         int lon = TxNode.valueAt(orgNode,2)
				         
				         String currentLat = lat + ""
				         String currentLon = lon + ""
				         JSONObject.put(jo,"routeID",routeID)
				         JSONObject.put(jo,"segmentId",segmentId)
				         JSONObject.put(jo,"edgeId",edgeId)
				         JSONObject.put(jo,"shapePointId",shapePointId)
				         JSONObject.put(jo,"range",range)
				         JSONObject.put(jo,"currentLat",currentLat)
				         JSONObject.put(jo,"currentLon",currentLon)
				         
				         int searchAlongType = PoiList_M_getSearchAlongType()
				         JSONObject.put(jo,"searchAlongType",searchAlongType)
				         
				         JSONObject locationJO
				         JSONObject.put(locationJO,"lat",lat)
				         JSONObject.put(locationJO,"lon",lon)
				         String locationString = JSONObject.toString(locationJO)
				         JSONObject.put(jo,"addressString",locationString)
				      endif
				   endif
                   
                   TxNode node
                   String strJo=JSONObject.toString(jo)
                   TxNode.addMsg(node,strJo)
				TxRequest req
				String url="<%=host + "/queryPoi.do"%>"
				String scriptName="poiCallback"
				TxRequest.open(req,url)
				TxRequest.setRequestData(req,node)
				TxRequest.onStateChange(req,scriptName)
				
				int showProgressBar = <%=PoiListModel.getShowProgressBar()%>
				if 1 == showProgressBar
				   TxRequest.setProgressTitle(req,"<%=msg.get("poi.Searching")%>")
				endif
				
				TxRequest.send(req)
				
				#log only first time at search or sorting actions
				if currentPageInt == 0
					PoiList_M_resetLoggedPages()
					# get search UID for reporting purposes
					String searchUID = <%=PoiListModel.getSearchUID()%>
					misLogSearchRequest(searchType, addressJO, searchUID, sortType)
				endif
	        endfunc
	        
	        func CallBack_PopupNotFound(int param)
				PoiList_M_backToPageWhenNoFound()
			endfunc
	        
	        func poiCallback(TxNode node,int status)
	            if status == 0
	                System.showErrorMsg("<%=msg.get("common.internal.error")%>")
	                PoiList_M_backToPageWhenNoFound()
				    return FAIL
				elseif status == 1
				    int currentPage = <%=PoiListModel.getPageIndexTemp()%>
					#poi json
		            String poiJoString = TxNode.msgAt(node,0)
		            JSONArray  poiJo=JSONArray.fromString(poiJoString)
		            int resultListSize=JSONArray.length(poiJo)
				    if NULL == poiJo || resultListSize <=0
				       System.showGeneralMsg(NULL,"<%=msg.get("poi.notfound")%>","<%=msg.get("common.button.OK")%>",NULL,NULL,"CallBack_PopupNotFound")
			 		   return FAIL
				    endif
					# Set poi sortType
					int sortType = <%=PoiListModel.getSortTypeTemp()%>
					<%=PoiListModel.setSortType("sortType")%>
					
					#audio
					TxNode audioNode = TxNode.childAt(node,0)
					PoiList_M_saveAudio(audioNode)
					
					int currentSize = 0
					if 0 == currentPage 
					    int maxPageIndex = TxNode.valueAt(node,0)
					    <%=PoiListModel.setMaxPageIndex("maxPageIndex")%>
						PoiList_M_savePoiList(poiJo)
						<%=PoiListModel.setPageIndex("0")%>
						currentSize = JSONArray.length(poiJo)
					else
						JSONArray prePoiArray = PoiList_M_getPoiList()
						int i = 0
						JSONObject newJo
						while i < resultListSize
							newJo = JSONArray.get(poiJo,i)
							JSONArray.put(prePoiArray,newJo)
							i = i + 1
						endwhile
						
						PoiList_M_savePoiList(prePoiArray)
						<%=PoiListModel.setPageIndex("currentPage")%>
						currentSize = JSONArray.length(prePoiArray)
					endif
					<%=PoiListModel.setPoiCount("currentSize")%>
					
					TxNode needSponsorNode = Preference.getPreferenceValue(<%=PreferenceConstants.KEY_GENERAL_SPONSOR%>)
					int isSponsorOn = <%=PreferenceConstants.VALUE_NEED_SPONSOR_ON%>
					if NULL != needSponsorNode && 1 <= TxNode.getValueSize(needSponsorNode)
					   isSponsorOn = TxNode.valueAt(needSponsorNode, 0)
					endif
					
					println("isSponsorOn..................."+isSponsorOn)
					if <%=PreferenceConstants.VALUE_NEED_SPONSOR_ON%> == isSponsorOn
						String sponsorPoiJoString = TxNode.msgAt(node,1)
				        JSONArray  sponsorPoiJo = JSONArray.fromString(sponsorPoiJoString)
						if 0 == currentPage 
							PoiList_M_saveSponsorPoiList(sponsorPoiJo)
					    else
					        JSONArray preSponsorPoiArray = PoiList_M_getSponsorPoiList()
					        int j = 0
					        int sponsorPoiJoSize=JSONArray.length(sponsorPoiJo)
							JSONObject newSponsorPoiJo
							while j < sponsorPoiJoSize
								newSponsorPoiJo = JSONArray.get(sponsorPoiJo,j)
								JSONArray.put(preSponsorPoiArray,newSponsorPoiJo)
								j = j + 1
							endwhile
							PoiList_M_saveSponsorPoiList(preSponsorPoiArray)
						endif
					endif
					
					int waitAudioFinishFlag = <%=PoiListModel.getWaitPromptAudioFinish()%>
					if 1 == waitAudioFinishFlag
					   PoiList_M_saveSearchPoiSuccessFlag(1)
					   PoiList_M_checkAudioFinishAndSubmit()
					else
				 	   PoiList_M_saveSearchPoiSuccessFlag(0)
				 	   PoiList_M_saveAudioFinishFlag(0)
					   System.doAction("showPoiList")
					   return FAIL
					endif
				endif
	        endfunc		

	        func PoiList_M_checkAudioFinishAndSubmit()
	            int searchFinishFlag = PoiList_M_getSearchPoiSuccessFlag()
	            int audioFinishFlag = PoiList_M_getAudioFinishFlag()
	            if 1 == searchFinishFlag && 1 == audioFinishFlag
	               PoiList_M_saveSearchPoiSuccessFlag(0)
	               PoiList_M_saveAudioFinishFlag(0)
	               System.doAction("showPoiList")
	               return FAIL
	            endif
	        endfunc
	        	            
	        func PoiList_M_backToPageWhenNoFound()
	            String backUrl = <%=PoiListModel.getBackURLWhenNoFound()%>
		        if "" != backUrl
		           MenuItem.setAttribute("backWhenNoFoundForDsr","url",backUrl)
		           System.doAction("backWhenNoFoundForDsr")
		           return FAIL
		        endif
	        endfunc	  	     

            func PoiList_M_backToBusiness(JSONObject jo)
               String url = PoiList_M_getCallBackUrl()
               String funcName = PoiList_M_getCallBackFunc()
               TxNode node
               TxNode.addMsg(node,funcName)
               
               JSONObject stopJo = JSONObject.get(jo,"stop")
               JSONObject.put(stopJo,"type",5)
               TxNode addressNode
			   TxNode.addMsg(addressNode,JSONObject.toString(stopJo))
			   JSONObject.put(jo,"stop",stopJo)
			   JSONObject.put(jo,"poiOrStop","poi")
			   TxNode.addMsg(addressNode,JSONObject.toString(jo))
			   PoiList_M_saveToRencentPlaces(jo)
               MenuItem.setAttribute("returnAddressToInvokerPage","url",url)
			   MenuItem.setBean("returnAddressToInvokerPage","callFunction",node)
			   MenuItem.setBean("returnAddressToInvokerPage","returnAddress",addressNode)
			   System.doAction("returnAddressToInvokerPage")
			   return FAIL
            endfunc	  
            
            func PoiList_M_saveToRencentPlaces(JSONObject poiJO)
			   TxNode node = PoiUtil_convertToNodeForResentSearch(poiJO)
			   RecentPlaces.saveAddress(node)
			endfunc
			
	        func PoiList_M_saveResentSearch(String inputString)
		         if "" == inputString
		             return FAIL
		         endif
		         TxNode oldResentSearchNode 
		         oldResentSearchNode = PoiList_M_getResentSearch()
	             
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
		    
		    func PoiList_M_getSearchCategory()
		        String categoryId = <%=PoiListModel.getCategoryId()%>
		        return categoryId
		    endfunc                 	            
		]]>
</tml:script>

<tml:script language="fscript" version="1"
	feature="<%=FeatureConstant.DSR%>">
	<![CDATA[
		    func PoiList_M_getAudioSave()
		        String audioKey = "<%=audioSaveKey%>"
				TxNode audioNode
				audioNode = Cache.getFromTempCache(audioKey)
				return audioNode
		    endfunc
		    
			func PoiList_M_createAudioIndex(int value)
				TxNode node
				TxNode.addValue(node,value)
				Cache.saveToTempCache("<%=audioMulResIndexKey%>",node)
			endfunc
			
			func PoiList_M_getAudioIndex()
				TxNode node = Cache.getFromTempCache("<%=audioMulResIndexKey%>")
				return TxNode.valueAt(node,0) 
			endfunc

		  	#TODO: put it to another place
			func SearchPoi_M_getLocation()
		         String saveKey="<%=Constant.StorageKeyForJSON.JSON_OBJECT_SEARCH_POI_ADDESS%>"
				 JSONObject addressJO = Cache.getJSONObjectFromTempCache(saveKey)
				 return addressJO
	    	endfunc
	    	
	    	func PoiList_M_resume()
	    	     System.doAction("showPoiList")
	    	endfunc
		]]>
</tml:script>

<tml:menuItem name="backWhenNoFoundForDsr"
	pageURL="<%=getPage + "SearchPoi"%>" />

<tml:menuItem name="showPoiList" pageURL="<%=getPage + "PoiList"%>">
</tml:menuItem>

<tml:menuItem name="returnAddressToInvokerPage"
	pageURL="<%=getPage + "PoiList"%>">
</tml:menuItem>
