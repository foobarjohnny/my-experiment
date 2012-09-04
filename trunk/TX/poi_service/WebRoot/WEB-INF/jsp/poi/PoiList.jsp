<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.cserver.util.FeatureConstant"%>
<%@page
	import="com.telenav.cserver.pref.struts.util.PreferenceConstants"%>
<%@ include file="../Header.jsp"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%
    String ShowDetailURL = getPage + "ShowDetail";
    String imageSolid = imageUrl + "small_solid_star.png";
    String imageHalf = imageUrl + "small_half_star.png";
    String imageUnSolid = imageUrl + "small_unsolid_star.png";
    String ads_pin = imageUrl + "ads_pin.png";
    String threeLinesImageFocus = imageUrl + "3line_list_highlight.png";
    String threeLinesImageBlur = imageUrl + "3line_list.png";
    String twoLinesImageFocus = imageUrl + "2line_list_highlight.png";
    String twoLinesImageBlur = imageUrl + "2line_list.png";
    String menuImage = imageUrl + "menu.png";
    String couponsImage = imageUrl + "coupon.png";
    boolean showUgcIcon = featureMgr
            .isEnabled(FeatureConstant.UGC_EDIT)
            || featureMgr.isEnabled(FeatureConstant.UGC_VIEW);
    
	String deviceModel = handlerGloble.getClientInfo(DataHandler.KEY_DEVICEMODEL);
	String carrier = handlerGloble.getClientInfo(DataHandler.KEY_CARRIER);
	//how many items are in the list view including both sponsored listings and natural POIs
	// Applicable only for the list view
	int poisOnFirstScreen = 6; // default for 9000
	int poisOnFirstScreenWithSponsored = 5;
	if("8900".equals(deviceModel)){
		// poisOnFirstScreen same for Javelin
		poisOnFirstScreenWithSponsored = 4;
	}
	if("SprintPCS".equals(carrier)&&"9670".equals(deviceModel)){
		poisOnFirstScreen = 10;
		poisOnFirstScreenWithSponsored = 10;
	}
	
	//           Non sponsored     Sponsored
	//         --------------------------------
	//         | 1page | Npage | 1page | Npage |
	//    9000 |   6   |   5   |   5   |   4   |
	//    8900 |   6   |   5   |   4   |   3   |
	//   next supported model should be here
%>

<%@page import="com.telenav.cserver.poi.model.PoiListModel"%>
<%@page import="com.telenav.cserver.stat.*"%>

<tml:TML outputMode="TxNode">
	<%@ include file="model/PoiListModel.jsp"%>
	<%@ include file="controller/ShowDetailController.jsp"%>
	<%@ include file="/WEB-INF/jsp/model/PrefModel.jsp"%>
	<%@ include file="/WEB-INF/jsp/dsr/SpeakNumber.jsp"%>
	<%@ include file="/WEB-INF/jsp/model/DriveToModel.jsp"%>
	<jsp:include
		page="/WEB-INF/jsp/local_service/controller/MapWrapController.jsp"></jsp:include>
	
	<jsp:include page="StatLogger.jsp"/>
	
	<tml:script language="fscript" version="1">
		<![CDATA[
		<%@ include file="../Stop.jsp"%>
		<%@ include file="../CommonScript.jsp"%>
		<%@ include file="ShowRateImageScript.jsp"%>
		        func preLoad()
			<%if (featureMgr.isEnabled(FeatureConstant.DSR)) {%>
		        	SpeakNumber_M_setSayNumberAvailable(1)
			<%}%>
		            showList(0)
		        endfunc
		        
		        func onShow()
		            Page.setComponentAttribute("mapViewButton","id",1122334455)
		        	System.setKeyEventListener("-a-1-2-3-4-5-6-7-8-9-dial-","onKeyPressed")
		        	#checkChangeFocus()
		        	#set rating image(When back from detail page after rating poi, change rating image)
		        	changePoiList()
		        	setCurrentPageLogged()
		        endfunc
		        
		        func changePoiList()
		            #nomal poi
		            JSONArray poiJsonArray = PoiList_M_getPoiList()
		            int pageIndex = <%=PoiListModel.getPageIndex()%>
					int pageSize = <%=Constant.PAGE_SIZE%>
					int startIndex = pageIndex * pageSize
					int endIndex = startIndex + pageSize
					int currentSize = JSONArray.length(poiJsonArray)
					if endIndex > currentSize
						endIndex = currentSize
					endif
		            int j = startIndex
		            JSONObject poiJo
		            int rating = 0
		            int i = 1
		            String categoryId = <%=PoiListModel.getCategoryId()%>
		            if "50500" == categoryId || "702" == categoryId || "703" == categoryId || "704" == categoryId || "705" == categoryId
		                while j < endIndex
						   hideStarIcons(i)
						   j = j + 1
						   i = i + 1
			            endwhile
		            else
		                while j < endIndex
			               poiJo = JSONArray.get(poiJsonArray,j)
			               rating = JSONObject.get(poiJo,"rating")
						   showRateStarImage(rating,i)
						   j = j + 1
						   i = i + 1
			            endwhile
		            endif
		        endfunc
		
		        func showList(int playAudioFlag)
		            # Load from local storage.
		            int pageIndex = <%=PoiListModel.getPageIndex()%>
		            JSONArray poiJsonArray = PoiList_M_getPoiList()
					# Start index and previous page
					int pageSize = <%=Constant.PAGE_SIZE%>
					int startIndex = pageIndex * pageSize
					int endIndex = startIndex + pageSize
					int currentSize = JSONArray.length(poiJsonArray)
					
					int nextPage = 1
					if endIndex > currentSize
						endIndex = currentSize
						nextPage = 0
					endif
					int maxPageIndex = <%=PoiListModel.getMaxPageIndex()%>
					if maxPageIndex == pageIndex
					    nextPage = 0
					endif
					
					# Sort
					int sortType = <%=PoiListModel.getSortType()%>
					String titleName = ""
					if <%=Constant.SORT_BY_RATING%> == sortType
					    titleName = titleName + " <%=msg.get("poi.list.byRating")%>"
					elsif <%=Constant.SORT_BY_POPULAE%> == sortType
					    titleName = titleName + " <%=msg.get("poi.list.byPopular")%>"
					elsif <%=Constant.SORT_BY_RELEVANCE%> == sortType
					    titleName = titleName + " <%=msg.get("poi.list.byRelevance")%>"
					elsif <%=Constant.SORT_BY_GASPRICE%> == sortType
					    titleName = titleName + " <%=msg.get("poi.list.byPrice")%>"
					else
					    titleName = titleName + " <%=msg.get("poi.list.byDistance")%>"
					endif
					if 0 != maxPageIndex
					   int pageIndexToShow = pageIndex + 1
					   titleName = titleName + " (" + "<%=msg.get("common.page")%>" + " " + pageIndexToShow + ")"
					endif
					Page.setComponentAttribute("searchLabel","text",titleName)
					
					String categoryId = <%=PoiListModel.getCategoryId()%>
					setMenuVisible("sponsorPoi",categoryId,sortType)
					
					# Previous button
					if 0 == pageIndex
					   Page.setComponentAttribute("previousLabel","visible","0")
					else
					   Page.setComponentAttribute("previousLabel","visible","1")
					endif
					
					# Next button
					if nextPage > 0
						Page.setComponentAttribute("nextLabel","visible","1")
					else
						Page.setComponentAttribute("nextLabel","visible","0")
					endif
					
					Page.setControlProperty("item1","focused","true")
					#show information of sponsor poi
					showSponsorPoi(pageIndex,sortType)
					
					# Items
					int i = 1
					String itemId = ""
					int j = startIndex
					String itemInfo_name = ""
					String itemInfo_address = ""
					String name = ""
					JSONObject stop
					int indexNumber = 0
					JSONObject poiJo
					String itemInfoId = ""
					String itemInfo_addressId = ""
					String itemLeftImageId = ""
					String itemLeftImageMenuId = ""
					String itemInfo_distance = ""
					String distanceId = ""
					String price = ""
					String adInfo 
					JSONObject ad
					String itemInfo_sponsorId=""
					# for MIS reporting
					String poiIDs = ""
					JSONArray poiWithAdd
					JSONArray poiWithAddIndex
					# sponsored poi impression logs separatelly
					int poisOnScreen = getPOI_OnScreen(pageIndex)
					while j < endIndex
					    poiJo = JSONArray.get(poiJsonArray,j)
						itemId = "item" + i
						Page.setComponentAttribute(itemId,"visible","1")
						if NULL != poiJo
							stop =  JSONObject.get(poiJo,"stop")
							name = JSONObject.get(poiJo,"name")
							
							if poisOnScreen >= i
								# if POI with adds add to array
								if poiHasAdd(poiJo)
									JSONArray.put(poiWithAdd, poiJo)
									JSONArray.put(poiWithAddIndex, i)
								else
									if poiIDs == ""
										poiIDs = JSONObject.getString(poiJo, "poiId")
									else
										poiIDs = poiIDs + "#" + JSONObject.getString(poiJo, "poiId")
									endif
								endif
							endif
							
							indexNumber = j + 1
							itemInfo_name = indexNumber+". " + name
							
							if <%=Constant.SORT_BY_GASPRICE%> == sortType
							   price = JSONObject.get(poiJo,"price")
							   println("~~~~~~~~~~~~~~~~price1 " + price)
						 	   if price != NULL && price != ""
							   		itemInfo_distance = "(" + price + ")"
							   endif
							elsif <%=Constant.SORT_BY_POPULAE%> == sortType
							   itemInfo_distance = "(" + JSONObject.get(poiJo,"popularity") + " pt)"
							else
							   if NULL != JSONObject.get(poiJo,"distance") && "" != JSONObject.get(poiJo,"distance")
							      itemInfo_distance = "(" + JSONObject.get(poiJo,"distance") + ")"
							   endif
							endif 
							
							itemInfo_address = PoiList_M_getWrapAddressFromPOIList_JSON(stop)
							itemInfo_sponsorId = "itemInfo_sponsor" + i
							if JSONObject.has(poiJo,"ad")
							   ad = JSONObject.get(poiJo,"ad")
							   if(JSONObject.has(ad,"adTag"))
							   	  adInfo = JSONObject.get(ad,"adTag")
							   	  Page.setComponentAttribute(itemId,"focusBgImage","<%=threeLinesImageFocus%>")
							   	  Page.setComponentAttribute(itemId,"blurBgImage","<%=threeLinesImageBlur%>")
							   	  Page.setComponentAttribute(itemInfo_sponsorId,"visible","1")
							 	  Page.setComponentAttribute(itemInfo_sponsorId,"text",adInfo)
							   else	 
							      Page.setComponentAttribute(itemId,"focusBgImage","<%=twoLinesImageFocus%>")
							   	  Page.setComponentAttribute(itemId,"blurBgImage","<%=twoLinesImageBlur%>")
							   	  Page.setComponentAttribute(itemInfo_sponsorId,"visible","0")
							   endif
							else
							     Page.setComponentAttribute(itemId,"focusBgImage","<%=twoLinesImageFocus%>")
							   	 Page.setComponentAttribute(itemId,"blurBgImage","<%=twoLinesImageBlur%>")
							   	 Page.setComponentAttribute(itemInfo_sponsorId,"visible","0")
							endif
						endif
						
						# Set item text
						itemInfoId = "itemInfo_name" + i
						Page.setComponentAttribute(itemInfoId,"text",itemInfo_name)
						itemInfo_addressId = "itemInfo_address" + i
						Page.setComponentAttribute(itemInfo_addressId,"text",itemInfo_address)
						distanceId = "itemInfo_distance" + i
						Page.setComponentAttribute(distanceId,"text",itemInfo_distance)
						setMenuVisible(itemId,categoryId,sortType)
						
						itemLeftImageId = "itemLeftImage" + i
						itemLeftImageMenuId = "itemLeftImageMenu" + i
					    
					    if JSONObject.has(poiJo,"menu")
						    Page.setControlProperty(itemId,itemLeftImageId+"$image","<%=menuImage%>")
						    Page.setComponentAttribute(itemLeftImageId,"visible","1")
						    if JSONObject.has(poiJo,"coupon")
						       Page.setComponentAttribute(itemLeftImageMenuId,"visible","1")
						    else
						       Page.setComponentAttribute(itemLeftImageMenuId,"visible","0")
						    endif
					    elsif JSONObject.has(poiJo,"coupon")
						    Page.setControlProperty(itemId,itemLeftImageId+"$image","<%=couponsImage%>")
						    Page.setComponentAttribute(itemLeftImageId,"visible","1")
						    Page.setComponentAttribute(itemLeftImageMenuId,"visible","0")
					    else
						    Page.setComponentAttribute(itemLeftImageId,"visible","0")
						    Page.setComponentAttribute(itemLeftImageMenuId,"visible","0")
					    endif
						j = j + 1
						i = i + 1
					endwhile
					# Hide the others
					while i < 10
						itemId = "item" + i
						Page.setComponentAttribute(itemId,"visible","0")
						i = i + 1
					endwhile
					
					if 0 == pageIndex && 0 == playAudioFlag
					    if needPlayAudio()
					    	PlayAudio()
					    endif	
					endif
					
				    if StatLogger.isStatEnabled(<%=EventTypes.POI_IMPRESSION%>) && (isCurrentPageLogged() == FALSE)
						logPOI_Impression(poiIDs)
						int arrSize = JSONArray.length(poiWithAdd)
						if arrSize > 0
							i = 0
							int addIndex
							while i < arrSize
								poiJo = JSONArray.get(poiWithAdd, i)
								addIndex = JSONArray.get(poiWithAddIndex, i)
								if poiJo != NULL
									Logger_logPOI_JSON(<%=EventTypes.POI_IMPRESSION%>, "0", poiJo, <%=AttributeValues.POI_TYPE_WITH_ADD%>, addIndex)
								endif
								i = i + 1 
							endwhile
						endif				    
					endif
		        endfunc
		        
		        func setMenuVisible(String itemId,String categoryId,int sortType)
		            MenuItem.setItemValid(itemId, 4, 1)
		            MenuItem.setItemValid(itemId, 5, 1)
		            MenuItem.setItemValid(itemId, 6, 1)
		            MenuItem.setItemValid(itemId, 7, 1)
		            MenuItem.setItemValid(itemId, 8, 1)
		            if <%=Constant.SORT_BY_RATING%> == sortType
					    MenuItem.setItemValid(itemId, 7, 0)
					elsif <%=Constant.SORT_BY_POPULAE%> == sortType
					    MenuItem.setItemValid(itemId, 4, 0)
					elsif <%=Constant.SORT_BY_RELEVANCE%> == sortType
					    MenuItem.setItemValid(itemId, 5, 0)
					elsif <%=Constant.SORT_BY_GASPRICE%> == sortType
					    MenuItem.setItemValid(itemId, 8, 0)
					else
					    MenuItem.setItemValid(itemId, 6, 0)
					endif
					
					String isMostPopular = <%=PoiListModel.getMostPopular()%>
		            if "1" == isMostPopular
		                MenuItem.setItemValid(itemId, 8, 0)
					    MenuItem.setItemValid(itemId, 7, 0)
					    MenuItem.setItemValid(itemId, 5, 0)
		            elsif "50500" == categoryId || "702" == categoryId || "703" == categoryId || "704" == categoryId || "705" == categoryId
					    MenuItem.setItemValid(itemId, 7, 0)
					    MenuItem.setItemValid(itemId, 5, 0)
					    MenuItem.setItemValid(itemId, 4, 0)
					else
					    MenuItem.setItemValid(itemId, 8, 0)
					    MenuItem.setItemValid(itemId, 4, 0)
					endif
					
					MenuItem.commitSetItemValid(itemId)
		        endfunc
		        
		        # the following three functions are left here because it is used everywhere on this page
		        # least common denominator to get rid of DSR
		        func needPlayAudio()
		        	int play = 0
			<%if (featureMgr.isEnabled(FeatureConstant.DSR)) {%>
		        	string from = <%=PoiListModel.getInputType()%>
		        	if "<%=Constant.StorageKey.POI_MODULE_FROM_SPEAK%>" == from
		        		if Pref_M_getAnounceSearchResult() == <%=PreferenceConstants.VALUE_ANOUNCESEARCHRESULT_YES%>
		        			play = 1
		        		endif
		        	endif
		    <%}%>
		        	return play
		        endfunc
		        
				func releaseResource()
					cancelAudio()
				endfunc
								
				func cancelAudio()
	               <%=PoiListModel.getAudioStopped()%>
	               if needPlayAudio()	           
				   		Handset.stopAudio()
				   endif
				endfunc
				
		        func showSponsorPoi(int currentPageIndex,int sortType)
		            JSONArray sponsorPoiListArray = PoiList_M_getSponsorPoiList()
		            if NULL != sponsorPoiListArray
		               int sponsorPoiListLength = JSONArray.length(sponsorPoiListArray)
		               if 0 != sponsorPoiListLength
		                   int sponsorPoiIndex = currentPageIndex
			               if currentPageIndex >= sponsorPoiListLength
			                  Page.setComponentAttribute("sponsorPoi","visible","0")
			                  return FAIL
			               endif
		                   JSONObject poiJo = JSONArray.get(sponsorPoiListArray,sponsorPoiIndex)
						   JSONObject stop =  JSONObject.get(poiJo,"stop")
						   String name = JSONObject.get(poiJo,"name")
						   
						   String itemInfo_address = PoiList_M_getWrapAddressFromPOIList_JSON(stop)
						   if "" == itemInfo_address
						      String phone = JSONObject.get(poiJo,"phoneNumber")
						      itemInfo_address = UTIL_formatPhoneNumber(phone)
						   endif
						   String distance = JSONObject.get(poiJo,"distance") 
						   String itemInfo_distance =""
						   if(distance!= NULL&&distance!=""&& distance!="0")
						  		itemInfo_distance= "(" + JSONObject.get(poiJo,"distance") + ")"
						   endif
						   if <%=Constant.SORT_BY_GASPRICE%> == sortType
						       String price = JSONObject.get(poiJo,"price")
							   println("~~~~~~~~~~~~~~~~price0 " + price)
						 	   if price != NULL && price != ""
						 	   	itemInfo_distance = "(" + price + ")"
						 	   endif
						   elsif <%=Constant.SORT_BY_POPULAE%> == sortType
							   itemInfo_distance = "(" + JSONObject.get(poiJo,"popularity") + " pt)"
						   endif
						   
						   String sponsorInfo 
						   if JSONObject.has(poiJo,"ad")
						   	 JSONObject ad = JSONObject.get(poiJo,"ad")
						   	 if(JSONObject.has(ad,"adTag"))
						 		sponsorInfo = JSONObject.get(ad,"adTag")
						 	 	Page.setComponentAttribute("itemInfo_sponsor0","text",sponsorInfo)
						 	 else
						        Page.setComponentAttribute("itemInfo_sponsor0","text","")
						 	 endif
						   else
						     Page.setComponentAttribute("itemInfo_sponsor0","text","")
						   endif
						   
						   if StatLogger.isStatEnabled(<%=EventTypes.POI_IMPRESSION%>) && (isCurrentPageLogged() == FALSE)
								Logger_logPOI_JSON(<%=EventTypes.POI_IMPRESSION%>, "0", poiJo, <%=AttributeValues.POI_TYPE_SPONSORED%>, 0)				    
						   endif
						   
						   # Set item text
						   Page.setComponentAttribute("itemInfo_name0","text",name)
						   Page.setComponentAttribute("itemInfo_address0","text",itemInfo_address)
						   Page.setComponentAttribute("itemInfo_distance0","text",itemInfo_distance)
						   Page.setComponentAttribute("sponsorPoi","visible","1")
						   Page.setControlProperty("sponsorPoi","focused","true")
					   else
					       Page.setComponentAttribute("sponsorPoi","visible","0")
		               endif
		            else
		               Page.setComponentAttribute("sponsorPoi","visible","0")
		            endif
		        endfunc
                
                func getPOI_OnScreen(int currentPageIndex)
		            JSONArray sponsorPoiListArray = PoiList_M_getSponsorPoiList()
		            if NULL != sponsorPoiListArray
		               int sponsorPoiListLength = JSONArray.length(sponsorPoiListArray)
		               if 0 != sponsorPoiListLength && currentPageIndex < sponsorPoiListLength
		                  if currentPageIndex == 0
		                  	return <%=poisOnFirstScreenWithSponsored%>
		                  else
							# because of prev button I need to minus 1 for consiquent pages
		                  	return <%=poisOnFirstScreenWithSponsored%> -1
		                  endif
			           endif
					endif
                    if currentPageIndex == 0
	                  	return <%=poisOnFirstScreen%>
		            else
		               	return <%=poisOnFirstScreen%> -1
		            endif
                endfunc
                
				func logPOI_Impression(String poiIDs)
					if poiIDs == ""
						return TRUE
					endif
					JSONObject params
					JSONObject.put(params, "<%=AttributeID.POI_ID%>", poiIDs)
					JSONObject.put(params, "<%=AttributeID.POI_TYPE%>",  <%=AttributeValues.POI_TYPE_NORMAL%>)
					JSONObject.put(params, "<%=AttributeID.SEARCH_UID%>", <%=PoiListModel.getSearchUID()%>)
					# it is always from POIList
					JSONObject.put(params, "<%=AttributeID.PAGE_NAME%>", "0")
					JSONObject.put(params, "<%=AttributeID.PAGE_NUMBER%>", <%=PoiListModel.getPageIndex()%>)
					StatLogger.logEvent(<%=EventTypes.POI_IMPRESSION%>, params)
				endfunc
				
				func isCurrentPageLogged()
					int pageIndex = <%=PoiListModel.getPageIndex()%>
					JSONObject loggedPages = PoiList_M_getLoggedPages()
					return JSONObject.has(loggedPages, "" + pageIndex)
				endfunc
				
				func setCurrentPageLogged()
					int pageIndex = <%=PoiListModel.getPageIndex()%>
					JSONObject loggedPages = PoiList_M_getLoggedPages()
					JSONObject.put(loggedPages, "" + pageIndex, "y")
				endfunc
				
				func poiHasAdd(JSONObject poi)
					JSONObject ad = JSONObject.get(poi, "ad")
					if ad != NULL
						if JSONObject.has(ad, "adID")
							return TRUE
						endif
					endif
					return FALSE
				endfunc
				
                #press 1-9 to show detail
				func onKeyPressed(string s)
					int pageIndex = <%=PoiListModel.getPageIndex()%>
		        	if 0 == pageIndex
		        	    int maxSize = <%=PoiListModel.getPoiCount()%>
						SpeakNumber_M_saveMaxSize(maxSize)
					    if s =="1" || s =="2" || s =="3" || s =="4" || s =="5" || s =="6" || s =="7" || s =="8" || s =="9"
							if isNumberValid(s)
								int i = String.convertToNumber(s)
								releaseResource()
								showDetail(i,1)
					        	return TRUE
					        endif
					    elsif s == "a"
					        if hasSponsorPoi()
					           showDetail(0,1)
					        endif
					    elsif s =="dial"
					    	if needPlayAudio() 
					    		if SpeakNumber_M_isSayNumberAvailable()
							    	releaseResource()
							    	<%=PoiListModel.getAudioStopped()%>
								  	NumberDSR_Start()
							  	endif
						  	endif
						  	return TRUE      
					    endif 
		        	endif
				endfunc
				
				func hasSponsorPoi()
				    int sponsorSize = 0
					JSONArray sponsorPoiListArray = PoiList_M_getSponsorPoiList()
					if NULL == sponsorPoiListArray
			           return FALSE
			        endif
			        sponsorSize = JSONArray.length(sponsorPoiListArray)
			        int pageIndex = <%=PoiListModel.getPageIndex()%>
			        if pageIndex >= sponsorSize
			           return FALSE
			        endif
			        return TRUE
				endfunc
				
		        func detailClick()
					# Get action based on selected index.
					int indexClicked = getSelectIndex()
					showDetail(indexClicked,1)
					cancelAudio()
					return FAIL
		        endfunc
		        
		        func detailMenu()
		            # Get action based on selected index.
					int indexClicked = getSelectIndex()
					showDetail(indexClicked,0)
					cancelAudio()
					return FAIL
		        endfunc
		        
		        func getSelectIndex()
		            TxNode indexClickNode = ParameterSet.getParam("indexClicked")
					int indexClicked = TxNode.valueAt(indexClickNode, 0)
					if 0 == indexClicked
					   return 0
					endif
					int pageIndex = <%=PoiListModel.getPageIndex()%>
					int pageSize = <%=Constant.PAGE_SIZE%>
					int startIndex = pageIndex * pageSize
					indexClicked = indexClicked + startIndex
					
					return indexClicked
		        endfunc
		        
		        func showDetail(int indexInList, int clickType)
					# 0 == indexInList means sponsor poi; 1 == clickType click trackball, 0 == clickType click menu
					int searchType = <%=PoiListModel.getSearchType()%>
					int pageIndex = <%=PoiListModel.getPageIndex()%>
					int sponsorSize = 0
					JSONArray poiList = PoiList_M_getPoiList()
					JSONArray sponsorPoiListArray = PoiList_M_getSponsorPoiList()
					if NULL != sponsorPoiListArray
			           sponsorSize = JSONArray.length(sponsorPoiListArray)
			        endif
			        
					if 0 == indexInList
					   int sponsorPoiIndex = pageIndex%sponsorSize
					   if 100 == searchType && 1 == clickType
					      JSONObject jo = JSONArray.get(sponsorPoiListArray,sponsorPoiIndex)
					      #add by ChengBiao, bug 75233 when poi has no address, do nothing
					      JSONObject stop =  JSONObject.get(jo,"stop")
					      if JSONObject.get(stop,"lat")==0 || JSONObject.get(stop,"lon")==0
							 return FAIL
						  endif
					      
					      DriveTo_M_saveStopType(<%=Constant.STOP_POI%>)
					      PoiList_M_backToBusiness(jo)
					   else
					      ShowDetail_C_saveForDetail(poiList,-1,sponsorPoiListArray,pageIndex)
				          ShowDetail_C_showDetail()
					   endif
					else
					   indexInList = indexInList - 1
					   # searchType mean for Businesses
					   if 100 == searchType && 1 == clickType
					      JSONObject jo = JSONArray.get(poiList,indexInList)
					      #add by ChengBiao, bug 75233 when poi has no address, do nothing
					      JSONObject stop =  JSONObject.get(jo,"stop")
					      if JSONObject.get(stop,"lat")==0 || JSONObject.get(stop,"lon")==0
							 return FAIL
						  endif
					      
					      PoiList_M_backToBusiness(jo)
					   else
					      ShowDetail_C_saveForDetail(poiList,indexInList,sponsorPoiListArray,-1)
				          ShowDetail_C_showDetail()
					   endif
					endif
		        endfunc
		        
		        func showPrevious()
					int pageIndex = <%=PoiListModel.getPageIndex()%>
					if 0 != pageIndex
					   pageIndex = pageIndex - 1	
					   <%=PoiListModel.setPageIndex("pageIndex")%>
					   changePoiList()
					   showList(1)
					endif
		        endfunc
		        
		        func showNext()
		            # Load from local storage.
					int pageIndex = <%=PoiListModel.getPageIndex()%>
					JSONArray poiList = PoiList_M_getPoiList()
					int pageSize = <%=Constant.PAGE_SIZE%>
					int nextStartIndex = pageIndex * pageSize + pageSize
					int currentSize = JSONArray.length(poiList)
					int maxPageIndex = pageIndex + 1
					if currentSize > nextStartIndex
						int newPageIndex = pageIndex + 1
						<%=PoiListModel.setPageIndex("newPageIndex")%>
						changePoiList()
						showList(1)
						cancelAudio()
						return FAIL
					else
						#currentPage
						int nextPageIndex = pageIndex + 1
						<%=PoiListModel.setPageIndexTemp("nextPageIndex")%>
						
						int sortType = <%=PoiListModel.getSortType()%>
						<%=PoiListModel.setSortTypeTemp("sortType")%>
						
						<%=PoiListModel.deleteShowProgressBar()%>
						<%=PoiListModel.deleteBackURLWhenNoFound()%>
						<%=PoiListModel.deleteWaitPromptAudioFinish()%>
						PoiList_M_searchPoiWithAjax()
						cancelAudio()
					endif
		        endfunc
		        
		        func mapResultOnclick()
					cancelAudio()
		            JSONArray poiJsonArray = PoiList_M_getPoiList()
		            JSONArray sponsorArray = PoiList_M_getSponsorPoiList()
		            
					int index = getSelectIndex() - 1
					int pageIndex = <%=PoiListModel.getPageIndex()%>
					int startSponsorIndex=-1
					if (index==-1)
						startSponsorIndex=pageIndex
					endif
					int searchType = <%=PoiListModel.getSearchType()%>
					if <%=PoiListModel.SearchType.SEARCH_ALONGROUTE%> == searchType
					   MapWrap_C_showPoiAlongRoute(poiJsonArray, sponsorArray,index,startSponsorIndex)
					else
					    JSONObject mapStop = toMapStop(SearchPoi_M_getLocation())
					    MapWrap_C_showPoi(poiJsonArray, sponsorArray,index,mapStop,startSponsorIndex)
					    if StatLogger.isStatEnabled(<%=EventTypes.POI_VIEW_MAPALL%>)
							int poiType = <%=AttributeValues.POI_TYPE_NORMAL%>
				            TxNode indexClickNode = ParameterSet.getParam("indexClicked")
							int indexClicked = TxNode.valueAt(indexClickNode, 0)
							if 0 == indexClicked
							   poiType = <%=AttributeValues.POI_TYPE_SPONSORED%>
							endif
					   		JSONObject poi
					   		if (index != -1)
					   			poi = JSONArray.get(poiJsonArray, index)
					   		else
					   			poi = JSONArray.get(sponsorArray, startSponsorIndex)
					   		endif
					   		Logger_logPOI_JSON(<%=EventTypes.POI_VIEW_MAPALL%>, "0", poi, poiType, indexClicked)
					   endif
					endif
		        endfunc
		        
		        func showMapView()
		            cancelAudio()
		            JSONArray poiJsonArray = PoiList_M_getPoiList()
		            JSONArray sponsorArray = PoiList_M_getSponsorPoiList()
					int searchType = <%=PoiListModel.getSearchType()%>
					
					int pageIndex = <%=PoiListModel.getPageIndex()%>
					int pageSize = <%=Constant.PAGE_SIZE%>
					int startIndex = pageIndex * pageSize
					int startSponsorIndex=-1
					
					if StatLogger.isStatEnabled(<%=EventTypes.POI_VIEW_MAPALL%>)
						logPOI_JSON(<%=EventTypes.POI_VIEW_MAPALL%>, startIndex, poiJsonArray)				    
				   	endif
					
					if <%=PoiListModel.SearchType.SEARCH_ALONGROUTE%> == searchType
					   MapWrap_C_showPoiAlongRoute(poiJsonArray, sponsorArray,startIndex,startSponsorIndex)
					else
					   JSONObject mapStop = toMapStop(SearchPoi_M_getLocation())
					   MapWrap_C_showPoi(poiJsonArray, sponsorArray,startIndex,mapStop,startSponsorIndex)
					endif
					return FAIL
		        endfunc
		        
		        func logPOI_JSON(int eventType, int startIndex, JSONArray poiArray)
				   int poiType = <%=AttributeValues.POI_TYPE_NORMAL%>
				   JSONObject poi = JSONArray.get(poiArray, startIndex)
				   Logger_logPOI_JSON(eventType, "0", poi, poiType, 0)				    
				endfunc
			
				func toMapStop(JSONObject stop)
					JSONObject mapStop
					if(JSONObject.get(stop,"type")!=6)
						
						String lastLine = JSONObject.get(stop,"city")+", "+JSONObject.get(stop,"state")
						String zip = JSONObject.get(stop,"zip")
						if(zip!="")
							lastLine=lastLine+", " + zip
						endif
						String firstLine = JSONObject.get(stop,"firstLine")
						JSONObject.put(mapStop,"FirstLine",firstLine)
						JSONObject.put(mapStop,"SecondLine",lastLine)
					endif
					JSONObject.put(mapStop,"Lat",JSONObject.get(stop,"lat"))
					JSONObject.put(mapStop,"Lon",JSONObject.get(stop,"lon"))
					JSONObject.put(mapStop,"type",JSONObject.get(stop,"type"))
					return mapStop
				endfunc	
				
				# sort by popular
				func popular()
		            int relevanceType = <%=Constant.SORT_BY_POPULAE%>
					<%=PoiListModel.setSortTypeTemp("relevanceType")%>
					sort()
				endfunc
				
		        # sort by relevance
		        func relevance()
		            int relevanceType = <%=Constant.SORT_BY_RELEVANCE%>
					<%=PoiListModel.setSortTypeTemp("relevanceType")%>
					sort()		        
		        endfunc
		        
		        # sort by rating
		        func rating()
		            int ratingType = <%=Constant.SORT_BY_RATING%>
					<%=PoiListModel.setSortTypeTemp("ratingType")%>
					sort()
		        endfunc
		        
		        # sort by price
		        func price()
		            int priceType = <%=Constant.SORT_BY_GASPRICE%>
					<%=PoiListModel.setSortTypeTemp("priceType")%>
					sort()
		        endfunc
		        
		        # sort by distance
		        func distance()
		            int distanceType = <%=Constant.SORT_BY_DISTANCE%>
					<%=PoiListModel
                                            .setSortTypeTemp("distanceType")%>
					sort()
		        endfunc
		        
		        func sort()
		            cancelAudio()
				    #Set new page index
				    <%=PoiListModel.setPageIndexTemp("0")%>
					<%=PoiListModel.deleteShowProgressBar()%>
					<%=PoiListModel.deleteBackURLWhenNoFound()%>
					String typeOrSpeak = "<%=Constant.StorageKey.POI_MODULE_FROM_TYPE%>"
			    	<%=PoiListModel.setInputType("typeOrSpeak")%>
					PoiList_M_searchPoiWithAjax()
				endfunc
				
				#Cancel Audio before back
				func onBack()
				    cancelAudio()
				    System.doAction("cancelAudioAndBack")
				    return FAIL
				endfunc
				
				func backPage()
				    System.back()
				    return FAIL
				endfunc
			]]>
	</tml:script>

	<tml:script language="fscript" version="1"
		feature="<%=FeatureConstant.DSR%>">
		<![CDATA[
				func DSR_showDetail(int i)
					showDetail(i+1,1)
				endfunc
				
		        func playAudioBack()
		        
		        endfunc
		        
		        func PlayAudio()
			        #Delete stop audio flag,so that the audio can be play
			        <%=PoiListModel.deleteAudioStopped()%>
			        TxNode audioNode = PoiList_M_getAudioSave()			        
				    if NULL != audioNode
				       Page.setControlProperty("item1","focused","true")
					   PoiList_M_createAudioIndex(0)
					   CallBack_playAudio()
				    endif
			    endfunc
			     
			    func increaseAudioIndex()
					int index = PoiList_M_getAudioIndex()
					index = index + 1
					PoiList_M_createAudioIndex(index)
				endfunc
				
				func CallBack_playAudio()
		            #1 == stopAudio means can play audio
		            int stopAudio = <%=PoiListModel.getAudioStopped()%>
	                
	                if 1 == stopAudio	        
			            String audioKey = "<%=Constant.StorageKey.POI_AUDIO_SAVE%>"
						TxNode audioNode
						audioNode = Cache.getFromTempCache(audioKey)
			            int audioListSize = TxNode.getChildSize(audioNode) 
			       		int index = PoiList_M_getAudioIndex()
			        	
			        	if index >= audioListSize
			        		playAudioBack()
			        	else
			        		Handset.playSpecNode(index,index,audioNode,"playNextAudio_POI")
			        	endif
			        endif
		        endfunc
		        
		        func getAudioSize()
	        		String audioKey = "<%=Constant.StorageKey.POI_AUDIO_SAVE%>"
					TxNode audioNode
					audioNode = Cache.getFromTempCache(audioKey)
		            int audioListSize = TxNode.getChildSize(audioNode)
		            
		            return audioListSize
		        endfunc
				
				func playNextAudio_POI()
					if SpeakNumber_M_isSayNumberAvailable()
			        	increaseAudioIndex()
			        	int index = PoiList_M_getAudioIndex() + 1
			        	Page.setControlProperty("item" + index,"focused","true")
			        	CallBack_playAudio()
		        	endif		
				endfunc
		]]>
	</tml:script>
	<tml:menuItem name="showMapViewClick" onClick="showMapView" />

	<tml:menuItem name="cancelAudioAndBack" onClick="backPage">
	</tml:menuItem>
	<tml:menuItem name="cancelAudio" text="Cancel Audio" trigger="KEY_MENU"
		onClick="cancelAudio">
	</tml:menuItem>

	<tml:menuItem name="addPoi" text="<%=msg.get("poi.list.addPoi")%>"
		trigger="KEY_MENU" onClick="addPoi">
	</tml:menuItem>

	<tml:menuItem name="hideContent"
		text="<%=msg.get("poi.list.hideUserContent")%>" trigger="KEY_MENU"
		onClick="hideUseContent">
	</tml:menuItem>

	<tml:menuItem name="previous" onClick="showPrevious">
	</tml:menuItem>
	<tml:menuItem name="popular"
		text="<%=msg.get("poi.list.sortByPopularity")%>" onClick="popular"
		trigger="KEY_MENU">
	</tml:menuItem>
	<tml:menuItem name="relevance"
		text="<%=msg.get("poi.list.sortByRelevance")%>" onClick="relevance"
		trigger="KEY_MENU">
	</tml:menuItem>
	<tml:menuItem name="distance"
		text="<%=msg.get("poi.list.sortByDistance")%>" onClick="distance"
		trigger="KEY_MENU">
	</tml:menuItem>
	<tml:menuItem name="rating"
		text="<%=msg.get("poi.list.sortByRating")%>" onClick="rating"
		trigger="KEY_MENU">
	</tml:menuItem>

	<tml:menuItem name="price" text="<%=msg.get("poi.list.sortByPrice")%>"
		onClick="price" trigger="KEY_MENU">
	</tml:menuItem>

	<tml:menuItem name="item_0_clicked" onClick="detailClick"
		trigger="TRACKBALL_CLICK">
		<tml:bean name="indexClicked" valueType="int" value="0"></tml:bean>
	</tml:menuItem>
	<tml:menuItem name="item_0_menu" 
		onClick="detailMenu" trigger="KEY_MENU">
		<tml:bean name="indexClicked" valueType="int" value="0"></tml:bean>
	</tml:menuItem>

	<tml:menuItem name="item_0_map_clicked" trigger="KEY_MENU"
		text="<%=msg.get("poi.list.mapResults")%>" onClick="mapResultOnclick">
		<tml:bean name="indexClicked" valueType="int" value="0"></tml:bean>
	</tml:menuItem>

	<tml:menuItem name="next" onClick="showNext">
	</tml:menuItem>
	<tml:menuItem name="previous" onClick="showPrevious">
	</tml:menuItem>
	<tml:page id="PoiListPage" url="<%=getPage + "PoiList"%>" groupId="<%=GROUP_ID_POI%>"
		type="<%=pageType%>" showLeftArrow="true" showRightArrow="true"
		helpMsg="$//$searchresults">
		<tml:title id="searchLabel" align="center|middle" fontColor="white"
			fontWeight="bold|system_large">
			<%=msg.get("poi.list.results")%>
		</tml:title>
		<tml:button id="mapViewButton" text="<%=msg.get("poi.map.view")%>"
			textVisible="true" isFocusable="true" fontSize="20"
	imageClick="<%=imageUrl + "titlebutton2_2.png"%>"
			imageUnclick="<%=imageUrl + "titlebutton2_1.png"%>">

			<tml:menuRef name="showMapViewClick" />
		</tml:button>

		<tml:listBox id="detailPanel">
			<tml:compositeListItem id="previousLabel" getFocus="false"
				visible="true" bgColor="#FFFFFF" transparent="false"
				focusBgImage="<%=twoLinesImageFocus%>"
				blurBgImage="<%=twoLinesImageBlur%>" isFocusable="true">
				<tml:label id="previous" textWrap="ellipsis"
					fontWeight="system_large|bold" focusFontColor="white" align="left">
					<%=msg.get("poi.list.showPreviousMore")%>
				</tml:label>
				<tml:menuRef name="previous" />
			</tml:compositeListItem>

			<tml:compositeListItem id="sponsorPoi" getFocus="false" visible="true" bgColor="#FFFFFF" transparent="false"
				focusBgImage="<%=threeLinesImageFocus%>"
				blurBgImage="<%=threeLinesImageBlur%>" isFocusable="true">
				<tml:image id="itemLeftImage0" url="<%=ads_pin%>" />
				<tml:label id="itemInfo_name0" focusFontColor="white"
					fontWeight="bold|system_large" textWrap="ellipsis" align="left">
				</tml:label>
				<tml:label id="itemInfo_distance0" textWrap="ellipsis" fontWeight="system"
					focusFontColor="#FFF000" fontColor="#005AFF" align="left">
				</tml:label>
				<tml:label id="itemInfo_address0" textWrap="ellipsis" fontWeight="system"
					focusFontColor="white" align="left">
				</tml:label>

				<tml:label id="itemInfo_sponsor0" textWrap="ellipsis" fontWeight="system_small"
					focusFontColor="#FDF017" fontColor="#005AFF" align="left">
				</tml:label>

				<tml:menuRef name="item_0_clicked" />
				<tml:menuRef name="item_0_menu" />
				<tml:menuRef name="item_0_map_clicked" />
				<tml:menuSeperator />
				<tml:menuRef name="popular" />
				<tml:menuRef name="relevance" />
				<tml:menuRef name="distance" />
				<tml:menuRef name="rating" />
				<tml:menuRef name="price" />
			</tml:compositeListItem>

			<%
			    for (int i = 1; i <= Constant.PAGE_SIZE; i++) {
			%>
			<tml:menuItem name="<%="item_" + i + "_clicked"%>"
				onClick="detailClick" trigger="TRACKBALL_CLICK">
				<tml:bean name="indexClicked" valueType="int" value="<%=i + ""%>"></tml:bean>
			</tml:menuItem>

			<tml:menuItem name="<%="item_" + i + "_menu"%>" 
				onClick="detailMenu" trigger="KEY_MENU">
				<tml:bean name="indexClicked" valueType="int" value="<%=i + ""%>"></tml:bean>
			</tml:menuItem>

			<tml:menuItem name="<%="item_" + i + "_map_clicked"%>"
				trigger="KEY_MENU" text="<%=msg.get("poi.list.mapResults")%>" onClick="mapResultOnclick">
				<tml:bean name="indexClicked" valueType="int" value="<%=i + ""%>"></tml:bean>
			</tml:menuItem>

			<tml:compositeListItem id="<%="item" + i%>" getFocus="false"
				height="70" width="480" visible="true" bgColor="#FFFFFF"
				transparent="false" focusBgImage="<%=twoLinesImageFocus%>"
				blurBgImage="<%=twoLinesImageBlur%>" isFocusable="true">

				<tml:image id="<%="itemLeftImage" + i%>" url="<%=menuImage%>" />
				<tml:image id="<%="itemLeftImageMenu" + i%>" url="<%=couponsImage%>" />

				<tml:label id="<%="itemInfo_name" + i%>" focusFontColor="white"
					fontWeight="system_large|bold" textWrap="ellipsis" align="left">
				</tml:label>
				<tml:label id="<%="itemInfo_distance" + i%>" textWrap="ellipsis"
					fontWeight="system" focusFontColor="#FFF000" fontColor="#005AFF"
					align="left">
				</tml:label>
				<tml:label id="<%="itemInfo_address" + i%>" textWrap="ellipsis"
					fontWeight="system" focusFontColor="white" align="left">
				</tml:label>
				<tml:image id="<%="starImage1_" + i%>" url="<%=imageUnSolid%>" />
				<tml:image id="<%="starImage2_" + i%>" url="<%=imageUnSolid%>" />
				<tml:image id="<%="starImage3_" + i%>" url="<%=imageUnSolid%>" />
				<tml:image id="<%="starImage4_" + i%>" url="<%=imageUnSolid%>" />
				<tml:image id="<%="starImage5_" + i%>" url="<%=imageUnSolid%>" />

				<tml:label id="<%="itemInfo_sponsor" + i%>" textWrap="ellipsis"
					fontSize="16" visible="false" focusFontColor="#FDF017"
					fontColor="#005AFF" align="left">
				</tml:label>

				<tml:menuRef name="<%="item_" + i + "_clicked"%>" />
				<tml:menuRef name="<%="item_" + i + "_menu"%>" />
				<tml:menuRef name="<%="item_" + i + "_map_clicked"%>" />
				<tml:menuSeperator />
				<tml:menuRef name="popular" />
				<tml:menuRef name="relevance" />
				<tml:menuRef name="distance" />
				<tml:menuRef name="rating" />
				<tml:menuRef name="price" />
				<tml:menuSeperator />
			</tml:compositeListItem>
			<%
			    }
			%>

			<tml:compositeListItem id="nextLabel" getFocus="false" visible="true"
				bgColor="#FFFFFF" transparent="false"
				focusBgImage="<%=twoLinesImageFocus%>"
				blurBgImage="<%=twoLinesImageBlur%>" isFocusable="true">
				<tml:label id="next" textWrap="ellipsis" 
					fontWeight="system_large|bold" focusFontColor="white" align="left">
					<%=msg.get("poi.list.showMore")%>
				</tml:label>
				<tml:menuRef name="next" />
				<param name="labelImageBound" value="bottom" />
				<param name="addText" value="" />
			</tml:compositeListItem>
		</tml:listBox>
	</tml:page>
	<cserver:outputLayout />
</tml:TML>
