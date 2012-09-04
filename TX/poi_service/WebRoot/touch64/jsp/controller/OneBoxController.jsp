<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ include file="../Header.jsp"%>
<jsp:include page="/touch64/jsp/ac/controller/SelectAddressController.jsp" />
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@page import="com.telenav.cserver.poi.model.PoiListModel"%>

<tml:script language="fscript" version="1">
	<%@ include file="/touch64/jsp/poi/controller/PoiListController.jsp"%>
	<%@ include file="/touch64/jsp/ac/controller/AddressCaptureController.jsp"%>
	<%@ include file="/touch64/jsp/ac/AddressAjax.jsp"%>
	<%@ include file="/touch64/jsp/model/OneBoxModel.jsp"%>
	<%@ include file="/touch64/jsp/poi/model/SearchPoiModel.jsp"%>
	<%
	int gpsValidTime = 240;
	int cellIdValidTime=1860;
	int gpsTimeout=12;
	%>
	<![CDATA[
		func clearOneBoxSearchData()
			PoiList_M_clearParameter()
			PoiList_M_setChangeLocationFlag(0)
			PoiList_M_saveSearchMethod("OneBox")
			<%=PoiListModel.deleteSortTypeTemp()%>		
		endfunc
		
		func oneBoxSearch(String inputString,String displayString)
			clearOneBoxSearchData()            
            OneBox_M_saveDispayString(displayString)
			<%=PoiListModel.setKeyWord("inputString")%>
		    <%=PoiListModel.setPageIndexTemp("0")%>
		     JSONObject addressJO = SearchPoi_M_getLocation()
			 if NULL == addressJO
				doGetGPSForOneBox()
			 else
				int lon = JSONObject.getInt(addressJO,"lon")
				if 0 == lon
					doGetGPSForOneBox()
				else
					OneBox_M_searchWithAjax()
		     	endif
		   	 endif
	    endfunc
	    
	    func OneBox_M_searchWithAjax()
				String inputString = <%=PoiListModel.getKeyWord()%>
				int currentPageInt = <%=PoiListModel.getPageIndexTemp()%>
				String currentPage = currentPageInt+""
				int maxResults = <%=Constant.POI_SEARCH_MAX_SIZE%>
				int allPoiMaxResult = <%=Constant.ALL_POI_MAX_SIZE%>
				int maxSize = <%=PoiListModel.getPoiCount()%>
				int surplusSize = allPoiMaxResult - maxSize
				if surplusSize < maxResults && 0 != currentPageInt
				   maxResults = surplusSize
				endif
				JSONObject addressJO
				addressJO = PoiList_M_getAddress()
				String addressString = ""
				if NULL != addressJO
				   addressString = JSONObject.toString(addressJO)
				endif
				
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
                JSONObject.put(jo,"addressString",addressString)
                JSONObject.put(jo,"distanceUnit",distanceUnit)
                JSONObject.put(jo,"from",from)
                JSONObject.put(jo,"searchTypeStr",searchTypeStr)
                
                int sponsorListingNumber = PoiList_M_getSponsorNumber()
				JSONObject.put(jo,"sponsorListingNumber",sponsorListingNumber)
				
				String transactionId = OneBox_M_getTransactionId()
				JSONObject.put(jo,"transactionId",transactionId)
				
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
			         
			         TxNode destNode = TxNode.childAt(searchInformationNode,2)
			         int latDest = TxNode.valueAt(destNode,1)
			         int lonDest = TxNode.valueAt(destNode,2)
			         JSONObject locationJODest
			         JSONObject.put(locationJODest,"lat",latDest)
			         JSONObject.put(locationJODest,"lon",lonDest)
			         JSONObject.put(jo,"addressStringDest",JSONObject.toString(locationJODest))
			      endif
			   endif
                TxNode node
                String strJo=JSONObject.toString(jo)
                TxNode.addMsg(node,strJo)
				TxRequest req
				String url="<%=host + "/oneBoxSearch.do"%>"
				String scriptName="oneBoxCallback"
				TxRequest.open(req,url)
				TxRequest.setRequestData(req,node)
				TxRequest.onStateChange(req,scriptName)
				String progressBarMsg = "<%=msg.get("poi.Searching.for")%>" + " <bold>" + OneBox_M_getDispayString() + "</bold>..."
				TxRequest.setProgressTitle(req,progressBarMsg)
				
				TxRequest.send(req)
	        endfunc
	        
	         func doGetGPSForOneBox()
			   		getGPSNoBlockingWithCallback(<%=Constant.CurrentLocation.LAST_KNOWN%>,<%=gpsValidTime%>,<%=cellIdValidTime%>,<%=gpsTimeout%>, "OneBoxSearch")		        
		     endfunc
				        
	        # Back from "getGPS"
	        func setCurrentLocationForOneBoxSearch(JSONObject jo)
        		SearchPoi_M_saveLocation(jo)
        		OneBox_M_searchWithAjax()
	        endfunc
	        
	        
	        func oneBoxCallback(TxNode node,int status)
				#need to add error check
				if (status ==0)
					System.showErrorMsg("<%=msg.get("common.internal.error")%>")
				endif
			
				String inputString = OneBox_M_getDispayString()
				if (TxNode.getValueSize(node)==0)
					 String noMatchFoundMsg = "<%=msg.get("onebox.notfound1")%>" + " \"" + inputString + "\"" + "<%=msg.get("onebox.notfound2")%>"
					 System.showGeneralMsg(NULL,noMatchFoundMsg,"<%=msg.get("common.button.OK")%>",NULL,NULL,"CallBack_PopupNotFound")
			 		 return FAIL					
				endif
	        	int resultType = TxNode.valueAt(node,1)
	        	if (resultType==<%=Constant.OneBox.POI_RESULT%>)
	        		if doSpecialLogicForPoiReturn()
			            OneBox_M_saveResentSearch(inputString)
	        			poiCallback(node,status)
	        		endif
	        	else
	        		if(resultType==<%=Constant.OneBox.DID_YOU_MEAN%>)
	        			String from = OneBox_M_getPageFrom()
						String pageCallBackUrl = "<%=getPageCallBack + "OneBoxWrap#"%>" + from
	        			AddressCapture_M_init(pageCallBackUrl,"addrCallBack",from)
	        		endif
	        		stateChange(node,status)
				endif
	        endfunc
	        
	        func doSpecialLogicForPoiReturn()
	        	String from = OneBox_M_getPageFrom()
	        	#poi->change locatin does not support search out poi list
	        	if "POI" == from
					System.showGeneralMsg(NULL,"<%=msg.get("ac.no.valid.city")%>","<%=msg.get("common.button.OK")%>",NULL,NULL,"CallBack_PopupNotFound")
			 		 return FALSE	
	        	endif
	        	
	        	if !needChangeSearchPoiType(from)
	        		return TRUE
	        	endif
	        	
	        	int searchType = <%=PoiListModel.getSearchType()%>
                if 5 == searchType
                	#100 = searchType is search for Businesses,but is still search poi
                   searchType = 100
                   <%=PoiListModel.setSearchType("searchType")%>
                   SearchPoi_M_setCallBackInformation(OneBox_M_getCallBackUrl(),OneBox_M_getCallBackFunction())
                endif
                return TRUE
	        endfunc
	        
	        #set the case that we should return the poi's address when click one poi in the list screen.
	        func needChangeSearchPoiType(String from)
	        	int changeFlag = FALSE
	        	if "commute" == from || "movie" == from || "CreateFavorite" == from || "MyFavorite" == from 
	        		changeFlag = TRUE
	        	endif
	        	return changeFlag
	        endfunc       
	        
	        func needChangeSearchPoiTypeForAddress(String from)
	        	int changeFlag = FALSE
	        	if "commute" == from || "movie" == from || "CreateFavorite" == from || "MyFavorite" == from || "DriveTo" == from
	        		changeFlag = TRUE
	        	endif
	        	return changeFlag
	        endfunc
	    	]]>
</tml:script>


<tml:menuItem name="autoFill" onClick="inputClick"
	trigger="TRACKBALL_CLICK|KEY_MENU" />

<tml:menuItem name="showAddress" pageURL="">
	<tml:bean name="callFunction" valueType="String" value="loadAddress">
	</tml:bean>
</tml:menuItem>

