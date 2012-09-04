<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.cserver.util.FeatureConstant"%>
<%@page import="com.telenav.cserver.util.TnConstants"%>

<%@page
	import="com.telenav.cserver.pref.struts.util.PreferenceConstants"%>
<%@ include file="../Header.jsp"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%
    String callBackURL = getPageCallBack + "PoiList";
	String previousLabel = msg.get("poi.list.prev");
	String nextLabel = msg.get("poi.list.next");
	boolean showUgcIcon = featureMgr
			.isEnabled(FeatureConstant.UGC_EDIT)
			|| featureMgr.isEnabled(FeatureConstant.UGC_VIEW);

	boolean isFeedbackEnabled = featureMgr.isEnabled(FeatureConstant.FEEDBACK_POI);

	String deviceModel = handlerGloble
			.getClientInfo(DataHandler.KEY_DEVICEMODEL);
	//how many items are in the list view including both sponsored listings and natural POIs
	// Applicable only for the list view
	int poisOnFirstScreen = 7; // default for warrior
	int poisOnFirstScreenWithSponsored = 6;
	if ("Backflip".equals(deviceModel)) {
		poisOnFirstScreen = 6; 
		poisOnFirstScreenWithSponsored = 5;
	}

	//               Non sponsored     Sponsored (for now we don't support landscape mode)
	//             --------------------------------
	//             | 1page | Npage | 1page | Npage |
	//    warrior  |   7   |   NA  |   6   |   NA  |
	//    Backflip |   6   |   NA  |   5   |   NA  |
	//   next supported model should be here

	final int SORT_CALLBACK_BASE = 100; //  to get call back id for each sort option
	final String width = handlerGloble
			.getClientInfo(DataHandler.KEY_WIDTH);
	final int screenWidth = Integer.valueOf(width);
	final String height = handlerGloble
	.getClientInfo(DataHandler.KEY_HEIGHT);
	final int screenHeight = Integer.valueOf(height);

	final int showBannerAds = featureMgr
			.isEnabled(FeatureConstant.BANNER_ADS) ? 1 : 0;

	final int distanceId = Constant.SORT_BY_DISTANCE
			+ SORT_CALLBACK_BASE;
	final int ratingId = Constant.SORT_BY_RATING + SORT_CALLBACK_BASE;
	final int popularId = Constant.SORT_BY_POPULAE + SORT_CALLBACK_BASE;
	final int relevanceID = Constant.SORT_BY_RELEVANCE
			+ SORT_CALLBACK_BASE;
	final int gasPriceID = Constant.SORT_BY_GASPRICE
			+ SORT_CALLBACK_BASE;
%>

<%@page import="com.telenav.cserver.poi.model.PoiListModel"%>
<%@page import="com.telenav.cserver.stat.*"%>

<tml:TML outputMode="TxNode">
	<%@ include file="model/PoiListModel.jsp"%>
	<%@ include file="controller/ShowDetailController.jsp"%>
	<%@ include file="/touch/jsp/model/PrefModel.jsp"%>
	<%@ include file="/touch/jsp/dsr/SpeakNumber.jsp"%>
	<%@ include file="/touch/jsp/model/DriveToModel.jsp"%>
	<%@ include file="controller/RatePoiController.jsp"%>
	<jsp:include
		page="/touch/jsp/local_service/controller/MapWrapController.jsp"></jsp:include>
	<jsp:include page="/touch/jsp/controller/DriveToController.jsp" />
	<%@ include
		file="/touch/jsp/ac/controller/CreateFavoritesController.jsp"%>
	<%@ include file="../ac/controller/ShareAddressController.jsp"%>

	<jsp:include page="StatLogger.jsp" />

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
		            if <%=showBannerAds%>
                        requestBannerAds()
                    endif

					<% if(isFeedbackEnabled) { %>
						Page.removeGenericMenu(4)
					<% } %>
		        endfunc
		        
		        func onShow()
		            #Page.setComponentAttribute("mapViewButton","id",1122334455)
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
					String sortName
					if <%=Constant.SORT_BY_RATING%> == sortType
					    sortName = " <%=msg.get("poi.list.byRating")%>"
					elsif <%=Constant.SORT_BY_POPULAE%> == sortType
					    sortName = " <%=msg.get("poi.list.byPopular")%>"
					elsif <%=Constant.SORT_BY_RELEVANCE%> == sortType
					    sortName = " <%=msg.get("poi.list.byRelevance")%>"
					elsif <%=Constant.SORT_BY_GASPRICE%> == sortType
					    sortName = " <%=msg.get("poi.list.byPrice")%>"
					else
					    sortName = " <%=msg.get("poi.list.byDistance")%>"
					endif
					Page.setComponentAttribute("sortBy","text","<%=msg.get("poi.list.sortBy")%>"+": "+sortName)
					
					String categoryId = <%=PoiListModel.getCategoryId()%>
					
					# Previous button
					if 0 == pageIndex
					   Page.setComponentAttribute("previous","isFocusable","0")
					   Page.setComponentAttribute("previous","imageUnclick","$previousButton_disable")
					else
					   Page.setComponentAttribute("previous","isFocusable","1")
					   Page.setComponentAttribute("previous","imageUnclick","$previousButton")
					   Page.setComponentAttribute("previous","imageClick","$previousClickButton")
					endif
					
					# Next button
					if nextPage > 0
						Page.setComponentAttribute("next","isFocusable","1")
						Page.setComponentAttribute("next","imageUnclick","$nextButton")
						Page.setComponentAttribute("next","imageClick","$nextClickButton")
					else
						Page.setComponentAttribute("next","isFocusable","0")
						Page.setComponentAttribute("next","imageUnclick","$nextButton_disable")
					endif
					
					Page.setControlProperty("item1","focused","true")
					#show information of sponsor poi
					showSponsorPoi(pageIndex,sortType)
					
					# Items
					int i = 1
					int latAndLon = 1
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
					String adInfo 
					String phone
					String callString
					String itemName
					String price=""
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
							#itemInfo_name = indexNumber+". " + name
							itemInfo_name = name
							
								price = JSONObject.get(poiJo,"price")
								if price!=NULL && price !=""
									Page.setComponentAttribute("itemInfo_price"+i,"text",price)
								   	Page.setComponentAttribute("itemInfo_price"+i,"visible","1")
								else
									Page.setComponentAttribute("itemInfo_price"+i,"visible","0")
								endif
								if <%=Constant.SORT_BY_POPULAE%> == sortType
								   itemInfo_distance = JSONObject.get(poiJo,"popularity") + " pt"
								else
								   itemInfo_distance = JSONObject.get(poiJo,"distance")
								endif 
							
							itemInfo_address = PoiList_M_getWrapAddressFromPOIList_JSON(stop)
							phone = JSONObject.get(poiJo,"phoneNumber")
							
						   	#if no lat&lon,hide menu "Drive To","Map", "Share", "Save Fav"
						   	latAndLon = 1
						   	# handle no address
						   	if JSONObject.get(stop,"lat")== 0 || JSONObject.get(stop,"lon")==0
									latAndLon = 0
						   	endif
						   	handleMenuWithLatAndLon(itemId, latAndLon)
						   
						   # Set "Call" available or not according to phone number
						   if isEmptyString(phone)
						   		  MenuItem.setItemValid(itemId, 2, 0)
						   		  # CONTEXT_MENU FOR ANDROID 6.1
					    		  MenuItem.setItemValid(itemId, 7, 0)
								  MenuItem.commitSetItemValid(itemId)
						   else
						   	      phone = UTIL_formatPhoneNumber(phone)
								  phone = "<%=msg.get("poi.call")%>" + " " + phone
							      MenuItem.setItemText(itemId, 2, phone)
						   		  MenuItem.setItemValid(itemId, 2, 1)
						   		  # CONTEXT_MENU FOR ANDROID 6.1
						   		  MenuItem.setItemText(itemId, 7, phone)
					    		  MenuItem.setItemValid(itemId, 7, 1)
								  MenuItem.commitSetItemValid(itemId)
						   endif
									
							
							itemInfo_sponsorId = "itemInfo_sponsor" + i
							if JSONObject.has(poiJo,"ad")
							   ad = JSONObject.get(poiJo,"ad")
							   if(JSONObject.has(ad,"adTag"))
							   	  adInfo = JSONObject.get(ad,"adTag")
							   	  Page.setComponentAttribute(itemId,"focusBgImage","$threeLinesImageFocus")
							   	  Page.setComponentAttribute(itemId,"blurBgImage","$threeLinesImageBlur")
								  itemInfo_address= adInfo
								
							   else	 
							      Page.setComponentAttribute(itemId,"focusBgImage","$twoLinesImageFocus")
							   	  Page.setComponentAttribute(itemId,"blurBgImage","$twoLinesImageBlur")
							   	  Page.setComponentAttribute(itemInfo_sponsorId,"visible","0")
							   endif
							else
							     Page.setComponentAttribute(itemId,"focusBgImage","$twoLinesImageFocus")
							   	 Page.setComponentAttribute(itemId,"blurBgImage","$twoLinesImageBlur")
							   	 Page.setComponentAttribute(itemInfo_sponsorId,"visible","0")
							endif
						endif
						
						# Set item text
						itemInfoId = "itemInfo_name" + i
						Page.setComponentAttribute(itemInfoId,"text",itemInfo_name)
						itemInfo_addressId = "itemInfo_address" + i
						Page.setComponentAttribute(itemInfo_addressId,"text",itemInfo_address)
						distanceId = "itemInfo_distance" + i
						if "()" == itemInfo_distance
						   itemInfo_distance = ""
						endif
						Page.setComponentAttribute(distanceId,"text",itemInfo_distance)
						
						itemLeftImageId = "itemLeftImage" + i
						itemLeftImageMenuId = "itemLeftImageMenu" + i
					    
					    if JSONObject.has(poiJo,"menu")
						    Page.setControlProperty(itemId,itemLeftImageId+"$image","$menuImage")
						    Page.setComponentAttribute(itemLeftImageId,"visible","1")
						    if JSONObject.has(poiJo,"coupon")
						       Page.setComponentAttribute(itemLeftImageMenuId,"visible","1")
						    else
						       Page.setComponentAttribute(itemLeftImageMenuId,"visible","0")
						    endif
						    
					    elsif JSONObject.has(poiJo,"coupon")
						    Page.setControlProperty(itemId,itemLeftImageId+"$image","$couponsImage")
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
					
					changeBannerAds()
					
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
		        
		        
		        func handleMenuWithLatAndLon(String itemId, int latAndLon)
				    #show or hide the menu items
				    
				    # Drive to, Map , Share, Save
				    MenuItem.setItemValid(itemId, 1, latAndLon)
					MenuItem.setItemValid(itemId, 3, latAndLon)
					MenuItem.setItemValid(itemId, 4, latAndLon)
					MenuItem.setItemValid(itemId, 5, latAndLon)
					# CONTEXT_MENU FOR ANDROID 6.1
					MenuItem.setItemValid(itemId, 6, latAndLon)
					MenuItem.setItemValid(itemId, 8, latAndLon)
					MenuItem.setItemValid(itemId, 9, latAndLon)
					MenuItem.setItemValid(itemId, 10, latAndLon)
					
					MenuItem.commitSetItemValid(itemId)
				endfunc
		        
		        func changeBannerAds()
                    int currentIndex = <%=PoiListModel.getPageIndex()%>
                    JSONArray array  = PoiList_M_getBannerAdsList()
                    if array==NULL
                        return FAIL
                    endif
                    int arrayLen = JSONArray.length(array)
                    if currentIndex<=arrayLen-1
                        JSONObject jo = JSONArray.get(array,currentIndex)
                        displayBannerAds(jo)
                    endif
                endfunc
		        
		        func requestBannerAds()
                    TxNode node
                    JSONObject searchLocation=SearchPoi_M_getLocation()
                    JSONObject jo

                    JSONObject.put(jo,"searchLocation",searchLocation)
                    String pageId = "POIList"
                    JSONObject.put(jo,"pageId",pageId)
                    JSONObject.put(jo,"pageIndex",<%=PoiListModel.getPageIndex()%>)
                    JSONObject.put(jo,"searchUID",<%=PoiListModel.getSearchUID()%>)
                    JSONObject.put(jo,"catId",<%=PoiListModel.getCategoryId()%>)
                    JSONObject.put(jo,"keyWord",<%=PoiListModel.getKeyWord()%>)
                    String joStr = JSONObject.toString(jo)
                    TxNode.addMsg(node,joStr)
                    String url="<%=host + "/getBannerAds.do"%>"
                    TxRequest req
                    TxRequest.open(req,url)

                    TxRequest.setRequestData(req,node)
                    TxRequest.onStateChange(req,"bannerAdsCallBack")
                    TxRequest.send(req)
                endfunc

                func bannerAdsCallBack(TxNode node, int status)
                    if status == 0
                        Page.setComponentAttribute("bannerAds","visible","0")
                        return FAIL
                    endif

                    if node != NULL
                        String bannerStr = TxNode.msgAt(node,0)
                        JSONObject jo = JSONObject.fromString(bannerStr)
                        int pageIndex = <%=PoiListModel.getPageIndex()%>
                        JSONArray adsArray
                        if pageIndex != 0
                            adsArray = PoiList_M_getBannerAdsList()
                        endif
                        JSONArray.put(adsArray,jo)
                        PoiList_M_saveBannerAdsList(adsArray)
                        displayBannerAds(jo)
                        return FAIL
                    endif
                endfunc

                func displayBannerAds(JSONObject jo)
                    if jo !=NULL
                        String imageUrl = JSONObject.get(jo,"imageUrl")
                        if imageUrl!=NULL
                            Page.setComponentAttribute("bannerAds","visible","0")
                            int screenWidth= <%=screenWidth%>
                            int screenHeight = <%=screenHeight%>
                            int height =JSONObject.get(jo,"imageHeight")
                            
                            #do not show the image it too large
                            if height> screenHeight/4
                            	return FAIL
                            endif
                            
                            
                            int width = JSONObject.get(jo,"imageWidth")
                            String startX = "" +(screenWidth-width)/2
                            Page.setComponentAttribute("bannerAdsImg","x",startX)
                            Page.setComponentAttribute("bannerAdsImg","y","8")
                            Page.setComponentAttribute("bannerAds","height",height+16)
                            Page.setComponentAttribute("bannerAdsImg","image",imageUrl)

                            TxNode bannerAdsLinkNode
                            TxNode.addMsg(bannerAdsLinkNode,JSONObject.get(jo,"clickUrl"))
                            MenuItem.setBean("bannerAdsClick", "url", bannerAdsLinkNode)
                            Page.setComponentAttribute("bannerAds","visible","1")
                            Page.setComponentAttribute("bannerAdsImg","visible","1")
                            return FAIL
                        endif
                    endif
                    Page.setComponentAttribute("bannerAds","visible","0")
                endfunc
                

				<%
					if (isFeedbackEnabled)
					{
				%>

                # script to forward to POI List Feedback page. Prepare a TxNode with
                # parameters for keyword, location, and category and forward to feedback page
                
                func poiListFeedback()

                    System.doAction("gotoPoiListFeedback")
                    return FAIL

                endfunc

				<%
					}
				%>

                
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
		    		play = 0
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
						   String distance = JSONObject.get(poiJo,"distance") 
						   String itemInfo_distance =""
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
						   
						   #if no lat&lon,hide menu "Drive To","Map", "Share", "Save Fav"
						   int latAndLon = 1
						   # handle no address
						   if JSONObject.get(stop,"lat")== 0 || JSONObject.get(stop,"lon")==0
								latAndLon = 0
						   endif
						   handleMenuWithLatAndLon("sponsorPoi", latAndLon)
						   
						   # Set "Call" available or not according to phone number
						   String phone = JSONObject.get(poiJo,"phoneNumber")
						   if isEmptyString(phone)
						   		  MenuItem.setItemValid("sponsorPoi", 2, 0)
						   		   # CONTEXT_MENU FOR ANDROID 6.1
						   		  MenuItem.setItemValid("sponsorPoi", 7, 0)
								  MenuItem.commitSetItemValid("sponsorPoi")
						   else
						   	      phone = UTIL_formatPhoneNumber(phone)
								  phone = "<%=msg.get("poi.call")%>" + " " + phone
							      MenuItem.setItemText("sponsorPoi", 2, phone)
						   		  MenuItem.setItemValid("sponsorPoi", 2, 1)
						   		  # CONTEXT_MENU FOR ANDROID 6.1
					    		  MenuItem.setItemText("sponsorPoi", 7, phone)
						   		  MenuItem.setItemValid("sponsorPoi", 7, 1)
								  MenuItem.commitSetItemValid("sponsorPoi")
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
		        
		        func isEmptyString(String s)
		        	if NULL != s 
		        		if "" != String.trim(s)
		        			return FALSE
		        		endif
		        	endif
		        	return TRUE
		        endfunc
		        
                func getPOI_OnScreen(int currentPageIndex)
		            JSONArray sponsorPoiListArray = PoiList_M_getSponsorPoiList()
		            if NULL != sponsorPoiListArray
		               int sponsorPoiListLength = JSONArray.length(sponsorPoiListArray)
		               if 0 != sponsorPoiListLength && currentPageIndex < sponsorPoiListLength
		                  	return <%=poisOnFirstScreenWithSponsored%>
			           endif
					endif
                  	return <%=poisOnFirstScreen%>
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
		        
		        func ratePoi()
					JSONArray poiJsonArray = PoiList_M_getPoiList()
					int index = getSelectIndex() - 1
					ShowDetail_C_saveAddressList(poiJsonArray)
					ShowDetail_C_saveIndex(index)
					JSONObject locationJo = JSONArray.get(poiJsonArray,index)
					RatePoi_C_savePoiToDo(locationJo)
					RatePoi_C_saveBackUrl("<%=callBackURL%>")
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
					      #add by ChengBiao, when poi has no address, do nothing
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
					       #add by ChengBiao, when poi has no address, do nothing
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
		        
		        func onSwipe()
					TxNode paraNode = ShareData.get("SWIPE_DATA")
					string direction = TxNode.msgAt(paraNode,0)
					if("right" == direction)
						showPrevious()
					elsif("left" == direction)
						showNext()
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
					endif
					if StatLogger.isStatEnabled(<%=EventTypes.POI_VIEW_MAP%>)
						logPOI_JSON(<%=EventTypes.POI_VIEW_MAP%>)
					endif
		        endfunc
		        
		        func getPoi()
		        	JSONArray poiJsonArray = PoiList_M_getPoiList()
		            JSONArray sponsorArray = PoiList_M_getSponsorPoiList()
          			int index = getSelectIndex() - 1
          			JSONObject locationJo
          			if index==-1
          				int pageIndex = <%=PoiListModel.getPageIndex()%>
          				locationJo= JSONArray.get(sponsorArray,pageIndex)
          			else
          				locationJo= JSONArray.get(poiJsonArray,index)	
          			endif
				   	
				   	return locationJo
				endfunc
		        
		        
		        func DriveTo()
				   JSONObject locationJo = getPoi()	
				   JSONObject.put(locationJo,"poiOrStop","poi")
				   TxNode poiNode
				   String poiStr = JSONObject.toString(locationJo)
				   TxNode.addMsg(poiNode,poiStr)
				   
				   int searchType = <%=PoiListModel.getSearchType()%>
		      	   String navType = "Search Poi"
		      	   if <%=PoiListModel.SearchType.SEARCH_ALONGROUTE%> == searchType
		      		  navType = "Search Along"
		      	   endif
		      	   DriveTo_M_saveStopType(<%=Constant.STOP_POI%>)
				   DriveTo_C_doNav(poiNode,navType,"POI")
				   if StatLogger.isStatEnabled(<%=EventTypes.POI_DRIVE_TO%>)
						logPOI_JSON(<%=EventTypes.POI_DRIVE_TO%>)				    
				   endif
				   return FAIL
				endfunc
		        
     			func createFavorites()
				    JSONObject poiJo = getPoi()
				    JSONObject locationJo = JSONObject.get(poiJo,"stop")
				    JSONObject jo
		        	JSONObject.put(jo,"from","POI")
					JSONObject.put(jo,"callbackpageurl","<%=callBackURL%>")
					CreateFavorites_C_onShow(locationJo,jo,poiJo)
					return FAIL	
				endfunc
				
				
				func call()
				    JSONObject locationJo = getPoi()
				    String phone = JSONObject.get(locationJo,"phoneNumber")
				
				    TxNode ivrInput
	        		TxNode.addMsg(ivrInput,phone)
	        		MenuItem.setBean("phoneCall", "phonenumber", ivrInput)
	        		System.doAction("phoneCall")
				    if StatLogger.isStatEnabled(<%=EventTypes.POI_CALL_TO%>)
						logPOI_JSON(<%=EventTypes.POI_CALL_TO%>)				    
				    endif
				endfunc
				
				func showShareAddress()
				    JSONObject jo
		        	JSONObject.put(jo,"callbackfunction","CallBack_ShareAddress")
					JSONObject.put(jo,"callbackpageurl","<%=callBackURL%>")
					
					JSONObject locationJo = getPoi()
					String name = JSONObject.get(locationJo,"name")
					int poiId = JSONObject.get(locationJo,"poiId")
					
					#JSONObject joPoi
					JSONObject.put(jo,"poi",locationJo)
					
					JSONObject joAddress = JSONObject.get(locationJo,"stop")
					JSONObject.put(joAddress,"type",2)
					JSONObject.put(jo,"address",joAddress)
					ShareAddress_C_show(jo)
					return FAIL
				endfunc
				
				
				func logPOI_JSON(int eventType)
		            JSONObject poi = getPoi()
					int poiType = <%=AttributeValues.POI_TYPE_NORMAL%>
		            TxNode indexClickNode = ParameterSet.getParam("indexClicked")
					int indexClicked = TxNode.valueAt(indexClickNode, 0)
					if 0 == indexClicked
						poiType = <%=AttributeValues.POI_TYPE_SPONSORED%>
					endif
					Logger_logPOI_JSON(eventType, "0", poi, poiType, indexClicked)				    
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
					if <%=PoiListModel.SearchType.SEARCH_ALONGROUTE%> == searchType
					   MapWrap_C_showPoiAlongRoute(poiJsonArray, sponsorArray,startIndex,startSponsorIndex)
					else
					   JSONObject mapStop = toMapStop(SearchPoi_M_getLocation())
					   MapWrap_C_showPoi(poiJsonArray, sponsorArray,startIndex,mapStop,startSponsorIndex)
					endif
					return FAIL
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
				
				func sortTypePop()
					int sortType = <%=PoiListModel.getSortType()%>
					JSONArray options
					JSONArray callBackParams
					String categoryId = <%=PoiListModel.getCategoryId()%>
					
					String isMostPopular = <%=PoiListModel.getMostPopular()%>
		            if "1" == isMostPopular
		            	JSONArray.put(options,"<%=msg.get("poi.list.sortByPopularity")%>")
						JSONArray.put(callBackParams, <%=popularId%>)

						JSONArray.put(options,"<%=msg.get("poi.list.sortByDistance")%>")
						JSONArray.put(callBackParams, <%=distanceId%>)
		            elsif "50500" == categoryId || "702" == categoryId || "703" == categoryId || "704" == categoryId || "705" == categoryId
						JSONArray.put(options,"<%=msg.get("poi.list.sortByPrice")%>")
						JSONArray.put(callBackParams, <%=gasPriceID%>)
						
						JSONArray.put(options,"<%=msg.get("poi.list.sortByDistance")%>")
						JSONArray.put(callBackParams, <%=distanceId%>)
					else
					  	JSONArray.put(options,"<%=msg.get("poi.list.sortByRelevance")%>")
						JSONArray.put(callBackParams, <%=relevanceID%>)
	
						JSONArray.put(options,"<%=msg.get("poi.list.sortByDistance")%>")
						JSONArray.put(callBackParams, <%=distanceId%>)
	
						JSONArray.put(options,"<%=msg.get("poi.list.sortByRating")%>")
						JSONArray.put(callBackParams, <%=ratingId%>)
					endif
					
				
								
					System.showGeneralMsgEx(NULL,"<%=msg.get("poi.list.sortTitle")%> ", options, callBackParams, -1, "setType")
						
				endfunc	
				
				func setType(int index)
					int sortyType=index-<%=SORT_CALLBACK_BASE%>
					int currentSortType = <%=PoiListModel.getSortType()%>
					if currentSortType!=sortyType
						if sortyType==<%=Constant.SORT_BY_RELEVANCE%>
							relevance()
						elseif sortyType==<%=Constant.SORT_BY_DISTANCE%>
							distance()
						elseif sortyType==<%=Constant.SORT_BY_RATING%>
							rating()
						elseif sortyType==<%=Constant.SORT_BY_POPULAE%>
							popular()
						elseif sortyType==<%=Constant.SORT_BY_GASPRICE%>
							price()
						endif	
					endif
				endfunc
				
				func getSortTypes()
					JSONArray types
					JSONArray.put(types,"<%=msg.get("poi.list.sortByRelevance")%>")
					return types 
				endfunc
				
				
		]]>
	</tml:script>
	<tml:menuItem name="showMapViewClick" onClick="showMapView" text="<%=msg.get("poi.map.view")%>" trigger="KEY_MENU|TRACKBALL_CLICK"/>

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

	<tml:actionItem name="makePhoneCallAction"
		action="<%=Constant.LOCALSERVICE_MAKEPHONECALL%>">
		<tml:input name="phonenumber" />
	</tml:actionItem>
	<tml:menuItem name="phoneCall" actionRef="makePhoneCallAction" />

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

	<tml:menuItem name="item_0_map_clicked" trigger="KEY_MENU"
		text="<%=msg.get("poi.list.mapResults")%>" onClick="mapResultOnclick">
		<tml:bean name="indexClicked" valueType="int" value="0"></tml:bean>
	</tml:menuItem>
	<tml:menuItem name="<%="item_0_driveTo_clicked"%>" trigger="KEY_MENU"
		text="<%=msg.get("poi.drive.to")%>" onClick="DriveTo">
		<tml:bean name="indexClicked" valueType="int" value="0"></tml:bean>
	</tml:menuItem>

	<tml:menuItem name="<%="item_0_call_clicked"%>" trigger="KEY_MENU"
		text="<%=msg.get("poi.call")%>" onClick="call">
		<tml:bean name="indexClicked" valueType="int" value="0"></tml:bean>
	</tml:menuItem>

	<tml:menuItem name="<%="item_0_map_clicked"%>" trigger="KEY_MENU"
		text="<%=msg.get("poi.map.it")%>" onClick="mapResultOnclick">
		<tml:bean name="indexClicked" valueType="int" value="0"></tml:bean>
	</tml:menuItem>

	<tml:menuItem name="<%="item_0_share_clicked"%>" trigger="KEY_MENU"
		text="<%=msg.get("poi.share.address")%>" onClick="showShareAddress">
		<tml:bean name="indexClicked" valueType="int" value="0"></tml:bean>
	</tml:menuItem>

	<tml:menuItem name="<%="item_0_saveFav_clicked"%>" trigger="KEY_MENU"
		text="<%=msg.get("poi.save.to.favorites")%>" onClick="createFavorites">
		<tml:bean name="indexClicked" valueType="int" value="0"></tml:bean>
	</tml:menuItem>

	<tml:menuItem name="<%="item_0_driveTo_contextMenu"%>"
		trigger="KEY_CONTEXT_MENU" text="<%=msg.get("poi.drive.to")%>"
		onClick="DriveTo">
		<tml:bean name="indexClicked" valueType="int" value="0"></tml:bean>
	</tml:menuItem>
	<tml:menuItem name="<%="item_0_call_contextMenu"%>"
		trigger="KEY_CONTEXT_MENU" text="<%=msg.get("poi.call")%>"
		onClick="call">
		<tml:bean name="indexClicked" valueType="int" value="0"></tml:bean>
	</tml:menuItem>
	<tml:menuItem name="<%="item_0_map_contextMenu"%>"
		trigger="KEY_CONTEXT_MENU" text="<%=msg.get("poi.map.it")%>"
		onClick="mapResultOnclick">
		<tml:bean name="indexClicked" valueType="int" value="0"></tml:bean>
	</tml:menuItem>
	<tml:menuItem name="<%="item_0_saveFav_contextMenu"%>"
		trigger="KEY_CONTEXT_MENU"
		text="<%=msg.get("poi.save.to.favorites")%>" onClick="createFavorites">
		<tml:bean name="indexClicked" valueType="int" value="0"></tml:bean>
	</tml:menuItem>
	<tml:menuItem name="<%="item_0_share_contextMenu"%>"
		trigger="KEY_CONTEXT_MENU" text="<%=msg.get("poi.share.address")%>"
		onClick="showShareAddress">
		<tml:bean name="indexClicked" valueType="int" value="0"></tml:bean>
	</tml:menuItem>


	<tml:menuItem name="setSortType" onClick="sortTypePop" />
	<tml:menuItem name="next" onClick="showNext">
	</tml:menuItem>
	<tml:menuItem name="previous" onClick="showPrevious">
	</tml:menuItem>
	<tml:actionItem name="invokeBrowser"
		action="<%=TnConstants.LOCALSERVICE_INVOKEPHONEBROWSER%>">
		<tml:input name="url" />
	</tml:actionItem>

    <tml:menuItem name="bannerAdsClick" actionRef="invokeBrowser" text="<%=msg.get("about.visit")%>" trigger="KEY_MENU|TRACKBALL_CLICK"/>


	<tml:block feature="<%=FeatureConstant.FEEDBACK_POI%>">
		<tml:menuItem name="poiListFeedbackMenu" onClick="poiListFeedback" text="<%=msg.get("common.givefeedback.menu")%>" trigger="KEY_MENU">
		</tml:menuItem>
		<tml:menuItem name="gotoPoiListFeedback" pageURL="<%= getPage + "POIListFeedback" %>">
		      <tml:bean name="feedbackNode" valueType="TxNode" value="" />
		</tml:menuItem>
	</tml:block>

	<tml:page id="PoiListPage" url="<%=getPage + "PoiList"%>"
		groupId="<%=GROUP_ID_POI%>" type="<%=pageType%>" showLeftArrow="true"
		showRightArrow="true" helpMsg="$//$searchresults">
		<tml:title id="title" align="center|middle" fontColor="white"
			fontWeight="bold|system_large">
			<%=msg.get("poi.list.title")%>
		</tml:title>

		<tml:menuRef name="poiListFeedbackMenu" />

		
		<tml:compositeListItem id="mapViewComposite" getFocus="false"
			visible="true" bgColor="#FFFFFF" transparent="false"
			isFocusable="true">
			<tml:label id="mapViewLabel" focusFontColor="white">
			</tml:label>
			<tml:menuRef name="showMapViewClick" />
		</tml:compositeListItem>


		<tml:listBox id="detailPanel">

			<tml:compositeListItem id="sponsorPoi" getFocus="false"
				visible="true" transparent="false"
				isFocusable="true">
				<tml:label id="itemInfo_name0" fontWeight="bold" textWrap="ellipsis" align="left"/>
				<tml:label id="itemInfo_address0" textWrap="ellipsis"align="left"/>
				<tml:label id="itemInfo_sponsor0" textWrap="ellipsis"align="left"/>

				<tml:menuRef name="item_0_clicked" />
				<tml:menuRef name="<%="item_0_driveTo_clicked"%>" />
				<tml:menuRef name="<%="item_0_call_clicked"%>" />
				<tml:menuRef name="<%="item_0_map_clicked"%>" />
				<tml:menuRef name="<%="item_0_share_clicked"%>" />
				<tml:menuRef name="<%="item_0_saveFav_clicked"%>" />

				<tml:menuRef name="<%="item_0_driveTo_contextMenu"%>" />
				<tml:menuRef name="<%="item_0_call_contextMenu"%>" />
				<tml:menuRef name="<%="item_0_map_contextMenu"%>" />
				<tml:menuRef name="<%="item_0_saveFav_contextMenu"%>" />
				<tml:menuRef name="<%="item_0_share_contextMenu"%>" />
				<tml:menuRef name="poiListFeedbackMenu" />
			</tml:compositeListItem>



			<%
				for (int i = 1; i <= Constant.PAGE_SIZE; i++) {
			%>
			<tml:menuItem name="<%="item_" + i + "_clicked"%>"
				onClick="detailClick" trigger="TRACKBALL_CLICK">
				<tml:bean name="indexClicked" valueType="int" value="<%=i + ""%>"></tml:bean>
			</tml:menuItem>

			<tml:menuItem
				name="<%="item_" + i
												+ "_driveTo_clicked"%>"
				trigger="KEY_MENU" text="<%=msg.get("poi.drive.to")%>"
				onClick="DriveTo">
				<tml:bean name="indexClicked" valueType="int" value="<%=i + ""%>"></tml:bean>
			</tml:menuItem>

			<tml:menuItem name="<%="item_" + i + "_call_clicked"%>"
				trigger="KEY_MENU" text="<%=msg.get("poi.call")%>" onClick="call">
				<tml:bean name="indexClicked" valueType="int" value="<%=i + ""%>"></tml:bean>
			</tml:menuItem>

			<tml:menuItem name="<%="item_" + i + "_map_clicked"%>"
				trigger="KEY_MENU" text="<%=msg.get("poi.map.it")%>"
				onClick="mapResultOnclick">
				<tml:bean name="indexClicked" valueType="int" value="<%=i + ""%>"></tml:bean>
			</tml:menuItem>

			<tml:menuItem name="<%="item_" + i + "_share_clicked"%>"
				trigger="KEY_MENU" text="<%=msg.get("poi.share.address")%>"
				onClick="showShareAddress">
				<tml:bean name="indexClicked" valueType="int" value="<%=i + ""%>"></tml:bean>
			</tml:menuItem>

			<tml:menuItem
				name="<%="item_" + i
												+ "_saveFav_clicked"%>"
				trigger="KEY_MENU"
				text="<%=msg
														.get("poi.save.to.favorites")%>"
				onClick="createFavorites">
				<tml:bean name="indexClicked" valueType="int" value="<%=i + ""%>"></tml:bean>
			</tml:menuItem>


			<tml:menuItem
				name="<%="item_" + i
										+ "_driveTo_contextMenu"%>"
				trigger="KEY_CONTEXT_MENU" text="<%=msg.get("poi.drive.to")%>"
				onClick="DriveTo">
				<tml:bean name="indexClicked" valueType="int" value="<%=i + ""%>"></tml:bean>
			</tml:menuItem>

			<tml:menuItem
				name="<%="item_" + i
										+ "_call_contextMenu"%>"
				trigger="KEY_CONTEXT_MENU" text="<%=msg.get("poi.call")%>"
				onClick="call">
				<tml:bean name="indexClicked" valueType="int" value="<%=i + ""%>"></tml:bean>
			</tml:menuItem>

			<tml:menuItem
				name="<%="item_" + i
												+ "_map_contextMenu"%>"
				trigger="KEY_CONTEXT_MENU" text="<%=msg.get("poi.map.it")%>"
				onClick="mapResultOnclick">
				<tml:bean name="indexClicked" valueType="int" value="<%=i + ""%>"></tml:bean>
			</tml:menuItem>

			<tml:menuItem
				name="<%="item_" + i
										+ "_saveFav_contextMenu"%>"
				trigger="KEY_CONTEXT_MENU"
				text="<%=msg
														.get("poi.save.to.favorites")%>"
				onClick="createFavorites">
				<tml:bean name="indexClicked" valueType="int" value="<%=i + ""%>"></tml:bean>
			</tml:menuItem>

			<tml:menuItem
				name="<%="item_" + i
										+ "_rate_contextMenu"%>"
				trigger="KEY_CONTEXT_MENU" text="<%=msg.get("poi.rate.it")%>"
				onClick="ratePoi" pageURL="<%=getPage + "RatePoi"%>">
				<tml:bean name="indexClicked" valueType="int" value="<%=i + ""%>"></tml:bean>
			</tml:menuItem>

			<tml:menuItem
				name="<%="item_" + i
										+ "_share_contextMenu"%>"
				trigger="KEY_CONTEXT_MENU" text="<%=msg.get("poi.share.address")%>"
				onClick="showShareAddress">
				<tml:bean name="indexClicked" valueType="int" value="<%=i + ""%>"></tml:bean>
			</tml:menuItem>

			<tml:compositeListItem id="<%="item" + i%>" getFocus="false"
				visible="true" transparent="false"
				focusBgImage="$twoLinesImageFocus" blurBgImage="$twoLinesImageBlur"
				isFocusable="true">

				<tml:image id="<%="itemLeftImage" + i%>" url="$menuImage" />
				<tml:image id="<%="itemLeftImageMenu" + i%>" />

				<tml:label id="<%="itemInfo_name" + i%>" fontWeight="bold" textWrap="ellipsis" align="left">
				</tml:label>
				<tml:label id="<%="itemInfo_distance" + i%>" textWrap="ellipsis"
					align="right|top">
				</tml:label>
				<tml:label id="<%="itemInfo_price" + i%>" textWrap="ellipsis"
					align="left">
				</tml:label>

				<tml:label id="<%="itemInfo_address" + i%>" textWrap="ellipsis"
					align="left">
				</tml:label>
				<tml:image id="<%="starImage1_" + i%>" />
				<tml:image id="<%="starImage2_" + i%>" />
				<tml:image id="<%="starImage3_" + i%>" />
				<tml:image id="<%="starImage4_" + i%>" />
				<tml:image id="<%="starImage5_" + i%>" />

				<tml:label id="<%="itemInfo_sponsor" + i%>" textWrap="ellipsis"
					visible="false"
					align="left">
				</tml:label>

				<tml:menuRef name="<%="item_" + i + "_clicked"%>" />
				<tml:menuRef
					name="<%="item_" + i
												+ "_driveTo_clicked"%>" />
				<tml:menuRef name="<%="item_" + i + "_call_clicked"%>" />
				<tml:menuRef name="<%="item_" + i + "_map_clicked"%>" />
				<tml:menuRef name="<%="item_" + i + "_share_clicked"%>" />
				<tml:menuRef
					name="<%="item_" + i
												+ "_saveFav_clicked"%>" />
				<tml:menuSeperator />

				<tml:menuRef
					name="<%="item_" + i
										+ "_driveTo_contextMenu"%>" />
				<tml:menuRef
					name="<%="item_" + i
										+ "_call_contextMenu"%>" />
				<tml:menuRef
					name="<%="item_" + i
												+ "_map_contextMenu"%>" />
				<tml:menuRef
					name="<%="item_" + i
										+ "_saveFav_contextMenu"%>" />
				<tml:menuRef
					name="<%="item_" + i
										+ "_rate_contextMenu"%>" />
				<tml:menuRef
					name="<%="item_" + i
										+ "_share_contextMenu"%>" />
				<tml:menuRef name="poiListFeedbackMenu" />
			</tml:compositeListItem>
			<%
				}
			%>
			<tml:block feature="<%=FeatureConstant.BANNER_ADS%>">
				<tml:compositeListItem id="bannerAds">
					<tml:image id="bannerAdsImg" align="center" />
					<tml:menuRef name="bannerAdsClick" />
					<tml:menuRef name="poiListFeedbackMenu" />
				</tml:compositeListItem>
			</tml:block>
		</tml:listBox>


		<tml:image id="bottomShadow" align="left|top" />
		<tml:image id="bottomBgImg" align="left|top" />

		<tml:button id="sortBy" getFocus="false" visible="true"
			fontWeight="system" isFocusable="true"
			text="<%=msg.get("poi.list.sortBy")%>">
			<tml:menuRef name="setSortType" />
			<param name="labelIma
				geBound" value="bottom" />
			<param name="addText" value="" />
		</tml:button>

		<tml:button id="previous" getFocus="false" visible="true"
			fontWeight="system" isFocusable="true" text=" ">
			<tml:menuRef name="previous" />
			<param name="labelImageBound" value="bottom" />
			<param name="addText" value="" />
		</tml:button>
		<tml:button id="next" getFocus="false" visible="true"
			fontWeight="system" isFocusable="false" text=" ">
			<tml:menuRef name="next" />
			<param name="labelIma
				geBound" value="bottom" />
			<param name="addText" value="" />
		</tml:button>


		<tml:image id="titleShadow" visible="true" align="left|top" />
	</tml:page>
	<cserver:outputLayout />
</tml:TML>
