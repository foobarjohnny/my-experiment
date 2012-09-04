<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.cserver.util.TnUtil"%>
<%@page import="com.telenav.cserver.util.FeatureConstant"%>
<%@page import="com.telenav.cserver.poi.struts.util.PoiUtil"%>
<%@page
	import="com.telenav.cserver.pref.struts.util.PreferenceConstants"%>
<%@page import="com.telenav.cserver.poi.model.PoiListModel"%>
<%@ include file="../Header.jsp"%>

    <%@ include file="model/PoiListModel.jsp"%>
	<%@ include file="controller/ShowDetailController.jsp"%>
	<%@ include file="/touch64/jsp/model/PrefModel.jsp"%>
	<%@ include file="/touch64/jsp/dsr/SpeakNumber.jsp"%>
	<%@ include file="/touch64/jsp/model/DriveToModel.jsp"%>
	<%@ include file="controller/RatePoiController.jsp"%>
	<jsp:include
		page="/touch64/jsp/local_service/controller/MapWrapController.jsp"></jsp:include>
	<jsp:include page="/touch64/jsp/controller/DriveToController.jsp"/>
	<%@ include
		file="/touch64/jsp/ac/controller/CreateFavoritesController.jsp"%>
	<%@ include file="../ac/controller/ShareAddressController.jsp"%>
<jsp:include page="StatLogger.jsp"/>

<%
	String callBackURL = getPageCallBack + "PoiList";
	boolean isFeedbackEnabled = featureMgr.isEnabled(FeatureConstant.FEEDBACK_POI);
	
	int poisOnFirstScreen = 7; // default for warrior
	int poisOnFirstScreenWithSponsored = 9;
	
	final int SORT_CALLBACK_BASE = 100; //  to get call back id for each sort option
	final String width = handlerGloble
			.getClientInfo(DataHandler.KEY_WIDTH);
	final int screenWidth = Integer.valueOf(width).intValue();
	final String height = handlerGloble
	.getClientInfo(DataHandler.KEY_HEIGHT);
	final int screenHeight = Integer.valueOf(height).intValue();
	
	final int distanceId = Constant.SORT_BY_DISTANCE
			+ SORT_CALLBACK_BASE;
	final int ratingId = Constant.SORT_BY_RATING + SORT_CALLBACK_BASE;
	final int popularId = Constant.SORT_BY_POPULAE + SORT_CALLBACK_BASE;
	final int relevanceID = Constant.SORT_BY_RELEVANCE
			+ SORT_CALLBACK_BASE;
	final int gasPriceID = Constant.SORT_BY_GASPRICE
			+ SORT_CALLBACK_BASE;
	final int SENCOND_SPONSOR_INDEX = 1000;
%>

<tml:script language="fscript" version="1">
		<%@ include file="../PoiUtil.jsp"%>
		<![CDATA[
		<%@ include file="../Stop.jsp"%>
		<%@ include file="../CommonScript.jsp"%>
		<%@ include file="../GetServerDriven.jsp"%>
		        func preLoad()
			 	   	System.hidePopup()
		        	displaySearchAlong()
					<%if (featureMgr.isEnabled(FeatureConstant.DSR)) {%>
				        SpeakNumber_M_setSayNumberAvailable(1)
					<%}%>
		            showList(0)
		            if 1 == ServerDriven_CanDisplayAds()
                        requestBannerAds()
                    endif

					<% if(isFeedbackEnabled) { %>
						Page.removeGenericMenu(4)
					<% } %>
		        endfunc
		        
		        func onShow()
		        	System.setKeyEventListener("-a-1-2-3-4-5-6-7-8-9-dial-","onKeyPressed")
		        	changePoiList()
		        	Page.preLoadPageToCache("<%=getPageCallBack + "ShowDetail"%>")
		        endfunc
		        
		        func isSortByPrice(string categoryId)
		        	 if "50500" == categoryId || "702" == categoryId || "703" == categoryId || "704" == categoryId || "705" == categoryId
		        	 	if 0 == ServerDriven_CanGasByPrice()
					        return FALSE
					    endif
		        	 	return TRUE
		        	 else
		        	 	return FALSE
		        	 endif
		        endfunc
		        
		        func changePoiList()
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
		            String itemId = ""
		            String categoryId = <%=PoiListModel.getCategoryId()%>
		            
		            if isSortByPrice(categoryId)
		                while j < endIndex
							itemId = "item" + i
							# Rating is maybe changed, so need to update item.
							poiJo = JSONArray.get(poiJsonArray,j)
							Page.setComponentAttribute(itemId,"poiInfo",poiJo)
							Page.setComponentAttribute(itemId,"isShowRateStar","0")
							j = j + 1
							i = i + 1
			            endwhile
		            else
		                while j < endIndex
							itemId = "item" + i
							# Rating is maybe changed, so need to update item.
							poiJo = JSONArray.get(poiJsonArray,j)
							Page.setComponentAttribute(itemId,"poiInfo",poiJo)
							Page.setComponentAttribute(itemId,"isShowRateStar","1")
							j = j + 1
							i = i + 1
			            endwhile
		            endif
		        endfunc
		
				func getSortByDisplay()
					int sortType = <%=PoiListModel.getSortType()%>
					String sortName
					if <%=Constant.SORT_BY_RATING%> == sortType
					    sortName = " <%=msg.get("poi.list.Rating")%>"
					elsif <%=Constant.SORT_BY_POPULAE%> == sortType
					    sortName = " <%=msg.get("poi.list.Popular")%>"
					elsif <%=Constant.SORT_BY_RELEVANCE%> == sortType
					    sortName = " <%=msg.get("poi.list.Relevance")%>"
					elsif <%=Constant.SORT_BY_GASPRICE%> == sortType && 1 == ServerDriven_CanGasByPrice()
					    sortName = " <%=msg.get("poi.list.Price")%>"
					else
					    sortName = " <%=msg.get("poi.list.Distance")%>"
					endif
					return 	sortName			
				endfunc

				func changePrevNextButtonImg(int pageIndex,int nextPage)
					if 0 == pageIndex
					   Page.setComponentAttribute("previous","isFocusable","0")
					   Page.setComponentAttribute("previous","imageUnclick","$previousButton_disable")
					else
					   Page.setComponentAttribute("previous","isFocusable","1")
					   Page.setComponentAttribute("previous","imageUnclick","$previousButton")
					   Page.setComponentAttribute("previous","imageClick","$previousClickButton")
					endif
					
					if nextPage > 0
						Page.setComponentAttribute("next","isFocusable","1")
						Page.setComponentAttribute("next","imageUnclick","$nextButton")
						Page.setComponentAttribute("next","imageClick","$nextClickButton")
					else
						Page.setComponentAttribute("next","isFocusable","0")
						Page.setComponentAttribute("next","imageUnclick","$nextButton_disable")
					endif				
				endfunc
								
		        func showList(int playAudioFlag)
		            int pageIndex = <%=PoiListModel.getPageIndex()%>
		            JSONArray poiJsonArray = PoiList_M_getPoiList()
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
					if maxPageIndex <= pageIndex
					    nextPage = 0
					endif
					<%if(PoiUtil.showPageIndex(handlerGloble)) {%>
					String titleName = "<%=msg.get("poi.list.title")%>"
					if 0 < maxPageIndex
					   int pageIndexToShow = pageIndex + 1
					   titleName = titleName + " (" + "<%=msg.get("common.page")%>" + " " + pageIndexToShow + ")"
					endif
					Page.setComponentAttribute("title","text",titleName)
					<%}%>
					
					int sortType = <%=PoiListModel.getSortType()%>
					String sortName = getSortByDisplay()
					Page.setComponentAttribute("sortBy","text","<%=msg.get("poi.list.sortBy")%>"+": "+sortName)
					
					changePrevNextButtonImg(pageIndex,nextPage)
					
					if 0 != ServerDriven_CanSponsor()
					    showSponsorPoi(pageIndex,sortType)	
					else
						Page.setComponentAttribute("sponsorPoi_0","visible","0")
						Page.setComponentAttribute("sponsorPoi_1","visible","0")
						
						setFocusToUnSponsor()
					endif
					
					String categoryId = <%=PoiListModel.getCategoryId()%>
					int isGasByPrice = 0
					if isSortByPrice(categoryId)
						isGasByPrice = 1
					endif
					int showRateMenuItem = 1
					if 1 == isGasByPrice
						showRateMenuItem = 0
					endif
					
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
					String poiIDs = ""
					JSONArray poiWithAdd
					JSONArray poiWithAddIndex
					int poisOnScreen = getPOI_OnScreen(pageIndex)
					while j < endIndex
					    poiJo = JSONArray.get(poiJsonArray,j)
					    JSONObject.put(poiJo,"indexOfPoiList",j)
						itemId = "item" + i
						Page.setComponentAttribute(itemId,"visible","1")
						if NULL != poiJo
							
							Page.setComponentAttribute(itemId,"poiInfo",poiJo)
							
							if poisOnScreen >= i
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
							
							if <%=Constant.SORT_BY_POPULAE%> == sortType
								  Page.setComponentAttribute(itemId,"isPopular","1")
							else
								  Page.setComponentAttribute(itemId,"isPopular","0")
							endif 
							
						   	stop =  JSONObject.get(poiJo,"stop")
						   	latAndLon = 1
						   	if JSONObject.get(stop,"lat")== 0 || JSONObject.get(stop,"lon")==0
									latAndLon = 0
						   	endif
						   	handleMenuWithLatAndLon(itemId, latAndLon)
						    phone = JSONObject.get(poiJo,"phoneNumber")
						    handleMenuForPhone(itemId,phone)
						    handleMenuForRate(itemId,showRateMenuItem)
						endif
						
						j = j + 1
						i = i + 1
					endwhile
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
				    	PoiList_M_setLoggedPages("" + pageIndex)
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
					
					if 0 == ServerDriven_CanShareAddress()
					    hideShareAddressMenu()
					endif
		        endfunc
		        
		        func hideShareAddressMenu()
		            <%
				    for (int i = 1; i <= Constant.PAGE_SIZE; i++) {
				        out.println("println(\"hide item"+i+"\")");
				        out.println("MenuItem.setItemValid(\"item"+i+"\",4,0)");
				        out.println("MenuItem.setItemValid(\"item"+i+"\",11,0)");
					    out.println("MenuItem.commitSetItemValid(\"item"+i+"\")");
				    }
			        %>	        
		        endfunc
		        
		        func handleMenuForRate(String itemId, int showRateMenuItem)
					MenuItem.setItemValid(itemId, 10, showRateMenuItem)
					MenuItem.commitSetItemValid(itemId)
				endfunc
				
		        func handleMenuWithLatAndLon(String itemId, int latAndLon)
				    if Account.isTnMaps()
				       MenuItem.setItemText(itemId, 1, "<%=msg.get("poi.get.directions")%>")
				       MenuItem.setItemText(itemId, 6, "<%=msg.get("poi.get.directions")%>")
				    else
				       MenuItem.setItemText(itemId, 1, "<%=msg.get("poi.drive.to")%>")
				       MenuItem.setItemText(itemId, 6, "<%=msg.get("poi.drive.to")%>")
				    endif
				    
				    MenuItem.setItemValid(itemId, 1, latAndLon)
					MenuItem.setItemValid(itemId, 3, latAndLon)
					MenuItem.setItemValid(itemId, 4, latAndLon)
					MenuItem.setItemValid(itemId, 5, latAndLon)
					MenuItem.setItemValid(itemId, 6, latAndLon)
					MenuItem.setItemValid(itemId, 8, latAndLon)
					MenuItem.setItemValid(itemId, 9, latAndLon)
					if "sponsorPoi_0" == itemId || "sponsorPoi_1" == itemId
						MenuItem.setItemValid(itemId, 10, latAndLon)
					else
						MenuItem.setItemValid(itemId, 11, latAndLon)
					endif
					
					MenuItem.commitSetItemValid(itemId)
				endfunc
		        
		        func changeBannerAds()
                    int currentIndex = <%=PoiListModel.getPageIndex()%>
                    JSONArray array  = PoiList_M_getBannerAdsList()
                    if array==NULL
                    	Page.setComponentAttribute("bannerAds","visible","0")
                        Page.setComponentAttribute("bannerAds","focusable",0)
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
                        Page.setComponentAttribute("bannerAds","focusable",0)
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
                        	Page.setComponentAttribute("bannerAds","focusable",0)
                            int screenWidth= <%=screenWidth%>
                            int screenHeight = <%=screenHeight%>
                            int height =JSONObject.get(jo,"imageHeight")
                            
                            if height> screenHeight/4
                            	return FAIL
                            endif
                            
                            int width = JSONObject.get(jo,"imageWidth")
                            String startX = "" +(screenWidth-width)/2
                            Page.setComponentAttribute("bannerAdsImg","x",startX)
                            Page.setComponentAttribute("bannerAdsImg","y","8")
                            Page.setComponentAttribute("bannerAds","height",height+16)
                            Page.setComponentAttribute("bannerAds","width",screenWidth)
                            Page.setComponentAttribute("bannerAdsImg","image",imageUrl)

                            TxNode bannerAdsLinkNode
                            TxNode.addMsg(bannerAdsLinkNode,JSONObject.get(jo,"clickUrl"))
                            MenuItem.setBean("bannerAdsClick", "url", bannerAdsLinkNode)
                            Page.setComponentAttribute("bannerAds","visible","1")
                        	Page.setComponentAttribute("bannerAds","focusable",1)
                            Page.setComponentAttribute("bannerAdsImg","visible","1")
                            return FAIL
                        endif
                    endif
                    Page.setComponentAttribute("bannerAds","visible","0")
                    Page.setComponentAttribute("bannerAds","focusable",0)
                endfunc
				
				# get search keyword
			    func getSearchKeyword()
				  String searchKeyword = <%=PoiListModel.getKeyWord()%>
				  return searchKeyword
			    endfunc
			    
			    # get category name
				func getCategoryName()
				    String catNameString = ""
	               	TxNode catNameNode = PoiList_M_getCategoryName()
	                if 	NULL != catNameNode
	                	catNameString = TxNode.msgAt(catNameNode,0)
	                endif
	                
	                return catNameString
				endfunc
				
				<%
					if (isFeedbackEnabled)
					{
				%>
                func poiListFeedback()
                	<%if(TnUtil.isEligibleForNewFeedBack(handlerGloble)){%>
                    String searchKeyword = getSearchKeyword()
                    String catName       = getCategoryName()
                    
                    String listFeedbackUrl = "<%=host+"/GenericFeedbackRetrieval.do?feedBackTopic=SEARCHFEEDBACKV1&pageName=POIListFeedback&subKey=%25search%20keyword%25&subValue="%>"
                    if(searchKeyword != NULL && searchKeyword != "")
                    	listFeedbackUrl = listFeedbackUrl + PoiUtil_encodeURL(searchKeyword)
                    elsif (catName != NULL && catName != "")
                    	listFeedbackUrl = listFeedbackUrl + PoiUtil_encodeURL(catName)
                    endif
                    MenuItem.setAttribute("gotoPoiListFeedback", "url", listFeedbackUrl)
					<%}%>
                   
                    System.doAction("gotoPoiListFeedback")
                    return FAIL
                endfunc
				<%
					}
				%>

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

				func setFocusToSponsor()
					if isSearchAlongRoute()
						Page.setControlProperty("searchAlongUpAheadButton","focused","true")
					else
						Page.setControlProperty("sponsorPoi_0","focused","true")
					endif
				endfunc

				func setFocusToUnSponsor()
					if isSearchAlongRoute()
						Page.setControlProperty("searchAlongUpAheadButton","focused","true")
					else
						Page.setControlProperty("item1","focused","true")
					endif
				endfunc
				
		        func showSponsorPoi(int currentPageIndex,int sortType)
		            JSONArray sponsorPoiListArray = PoiList_M_getSponsorPoiList()
		            if NULL != sponsorPoiListArray
		               int sponsorPoiListLength = JSONArray.length(sponsorPoiListArray)
		               if 0 != sponsorPoiListLength
		                   int i = 0
		                   int showSponsorNumber = ServerDriven_CanSponsor()
		                   int sponsorPoiIndex = currentPageIndex
		                   String sponsorId
		                   JSONObject poiJo
		                   JSONObject stop
		                   String name 
		                   String itemInfo_address
		                   String distance
		                   String itemInfo_distance =""
		                   String sponsorInfo 
		                   String itemInfo_sponsorId
		                   int latAndLon = 1
		                   String phone
		                   String itemInfo_nameId
		                   String itemInfo_addressId
		                   String itemInfo_distanceId
		                   JSONObject ad
		                   
		                   while i < showSponsorNumber
		                      sponsorPoiIndex = currentPageIndex * showSponsorNumber + i
		                      sponsorId = "sponsorPoi_" + i
		                      println("sponsorId................"+sponsorId)
		                      if sponsorPoiIndex >= sponsorPoiListLength
				                 println("sponsorId.....b......"+sponsorId+"visible..............0")
				                 Page.setComponentAttribute(sponsorId,"visible","0")
				                 Page.setComponentAttribute("sponsorPoi_1","visible","0")

								 setFocusToSponsor()
				                 return FAIL
				              endif
				              
				              poiJo = JSONArray.get(sponsorPoiListArray,sponsorPoiIndex)
				              if UTIL_checkEmptyJSON(poiJo)
				                 println("sponsorId.....c......"+sponsorId+"visible..............0")
				                 Page.setComponentAttribute(sponsorId,"visible","0")
				                 Page.setComponentAttribute("sponsorPoi_1","visible","0")

								 setFocusToSponsor()
				                 return FAIL
				              endif
				              
				              stop =  JSONObject.get(poiJo,"stop")
						      name = JSONObject.get(poiJo,"name")
						      
						      itemInfo_address = PoiList_M_getWrapAddressFromPOIList_JSON(stop)
							  distance = JSONObject.get(poiJo,"distance")
							  if <%=Constant.SORT_BY_GASPRICE%> == sortType
							     String price = JSONObject.get(poiJo,"price")
							   	 println("~~~~~~~~~~~~~~~~price0 " + price)
						 	   	 if price != NULL && price != ""
							     	itemInfo_distance = "(" + price + ")"
							     endif
							  elsif <%=Constant.SORT_BY_POPULAE%> == sortType
							     itemInfo_distance = "(" + JSONObject.get(poiJo,"popularity") + " pt)"
							  endif
							  
							  itemInfo_sponsorId = "itemInfo_sponsor0_" + i
							  if JSONObject.has(poiJo,"ad")
							   	 ad = JSONObject.get(poiJo,"ad")
							   	 if(JSONObject.has(ad,"adTag"))
							 		sponsorInfo = JSONObject.get(ad,"adTag")
							 	 	Page.setComponentAttribute(itemInfo_sponsorId,"text",sponsorInfo)
							 	 else
							        Page.setComponentAttribute(itemInfo_sponsorId,"text","")
							 	 endif
							  else
							     Page.setComponentAttribute(itemInfo_sponsorId,"text","")
							  endif
							  
							  if StatLogger.isStatEnabled(<%=EventTypes.POI_IMPRESSION%>) && (isCurrentPageSponsorLogged() == FALSE)
								 PoiList_M_setLoggedPages("s" + currentPageIndex)
								 Logger_logPOI_JSON(<%=EventTypes.POI_IMPRESSION%>, "0", poiJo, <%=AttributeValues.POI_TYPE_SPONSORED%>, 0)
						      endif
						      
							  if JSONObject.get(stop,"lat")== 0 || JSONObject.get(stop,"lon")==0
									latAndLon = 0
							  else
							        latAndLon = 1
							  endif
							  handleMenuWithLatAndLon(sponsorId, latAndLon)
		                      
					   		  phone = JSONObject.get(poiJo,"phoneNumber")						   
							  handleMenuForPhone(sponsorId,phone)
							  
							  itemInfo_nameId = "itemInfo_name0_" + i
							  itemInfo_addressId = "itemInfo_address0_" + i
							  itemInfo_distanceId = "itemInfo_distance0_" + i
							  Page.setComponentAttribute(itemInfo_nameId,"text",name)
							  Page.setComponentAttribute(itemInfo_addressId,"text",itemInfo_address)
							  Page.setComponentAttribute(itemInfo_distanceId,"text",itemInfo_distance)
							  println("sponsorId..........."+sponsorId+"visible..............1")
							  Page.setComponentAttribute(sponsorId,"visible","1")
							  #Page.setControlProperty(sponsorId,"focused","true")
		                      i = i + 1
		                   endwhile
		                   
		                   while i < 2
		                      sponsorId = "sponsorPoi_" + i
				              println("sponsorId.....a......"+sponsorId+"visible..............0")
				              Page.setComponentAttribute(sponsorId,"visible","0")
		                      
		                      i = i + 1
		                   endwhile

						   setFocusToSponsor()
					   else
					       println("sponsor.....both..0....visible..............0")
					       Page.setComponentAttribute("sponsorPoi_0","visible","0")
					       Page.setComponentAttribute("sponsorPoi_1","visible","0")

						   setFocusToUnSponsor()
		               endif
		            else
		               println("sponsor.....both..1....visible..............0")
		               Page.setComponentAttribute("sponsorPoi_0","visible","0")
					   Page.setComponentAttribute("sponsorPoi_1","visible","0")

					   setFocusToUnSponsor()
		            endif
		        endfunc
		        
		        func handleMenuForPhone(string itemId,string phone)
				   if isEmptyString(phone)
				   		  MenuItem.setItemValid(itemId, 2, 0)
				   		  MenuItem.setItemValid(itemId, 7, 0)
				   else
				   	      phone = UTIL_formatPhoneNumber(phone)
						  phone = "<%=msg.get("poi.call")%>" + " " + phone
					      MenuItem.setItemText(itemId, 2, phone)
				   		  MenuItem.setItemValid(itemId, 2, 1)
			    		  MenuItem.setItemText(itemId, 7, phone)
				   		  MenuItem.setItemValid(itemId, 7, 1)
				   endif
				   MenuItem.commitSetItemValid(itemId)		        
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
					JSONObject.put(params, "<%=AttributeID.PAGE_NAME%>", "0")
					JSONObject.put(params, "<%=AttributeID.PAGE_NUMBER%>", <%=PoiListModel.getPageIndex()%>)
					StatLogger.logEvent(<%=EventTypes.POI_IMPRESSION%>, params)
				endfunc
				
				func isCurrentPageLogged()
					int pageIndex = <%=PoiListModel.getPageIndex()%>
					JSONObject loggedPages = PoiList_M_getLoggedPages()
					if loggedPages != NULL
						return JSONObject.has(loggedPages, "" + pageIndex)
					else
						return FALSE
					endif
				endfunc
				
				func isCurrentPageSponsorLogged()
					int pageIndex = <%=PoiListModel.getPageIndex()%>
					JSONObject loggedPages = PoiList_M_getLoggedPages()
					if loggedPages != NULL
						return JSONObject.has(loggedPages, "s" + pageIndex)
					else
						return FALSE
					endif
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
					        if hasSponsorPoi() && 0 != ServerDriven_CanSponsor()
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
			        int sponsorListNumber = ServerDriven_CanSponsor()
			        
			        if pageIndex*sponsorListNumber >= sponsorSize
			           return FALSE
			        endif
			        return TRUE
				endfunc
				
		        func detailClick()
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
		        
		        func getSelectIndex()
		            TxNode indexClickNode = ParameterSet.getParam("indexClicked")
					int indexClicked = TxNode.valueAt(indexClickNode, 0)
					if 0 == indexClicked || <%=SENCOND_SPONSOR_INDEX%> == indexClicked
					   return indexClicked
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
			        
					if 0 == indexInList || <%=SENCOND_SPONSOR_INDEX%> == indexInList
					   int sponsorPoiIndex = getSponsorIndex(pageIndex,indexInList)
					   
					   if 100 == searchType && 1 == clickType
					      JSONObject jo = JSONArray.get(sponsorPoiListArray,sponsorPoiIndex)
					      DriveTo_M_saveStopType(<%=Constant.STOP_POI%>)
					      PoiList_M_backToBusiness(jo)
					   else
					      ShowDetail_C_saveForDetail(poiList,-1,sponsorPoiListArray,sponsorPoiIndex)
				          ShowDetail_C_showDetail()
					   endif
					else
					   indexInList = indexInList - 1
					   # searchType mean for Businesses
					   if 100 == searchType && 1 == clickType
					      JSONObject jo = JSONArray.get(poiList,indexInList)
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
						int nextPageIndex = pageIndex + 1
						<%=PoiListModel.setPageIndexTemp("nextPageIndex")%>
						
						int sortType = <%=PoiListModel.getSortType()%>
						<%=PoiListModel.setSortTypeTemp("sortType")%>
						
						<%=PoiListModel.deleteShowProgressBar()%>
						<%=PoiListModel.deleteBackURLWhenNoFound()%>
						<%=PoiListModel.deleteWaitPromptAudioFinish()%>
						PoiList_M_setPagingParameter()
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
					int sponsorListNumber = ServerDriven_CanSponsor()
		            
		            <%if(!TnUtil.isATTRIM63(handlerGloble) && !TnUtil.isSprintRim62(handlerGloble)&&!TnUtil.isVNRIM62(handlerGloble)&&!TnUtil.isTMORIM62(handlerGloble)&&!TnUtil.isTMOAndroid62(handlerGloble)){ %>
		            if 2 == sponsorListNumber
		                sponsorArray = sponsorArrayFilter(sponsorArray, index, pageIndex)
		            endif
		            <%}%>
		            
					int startSponsorIndex=-1
					if index==-1
						startSponsorIndex=pageIndex*sponsorListNumber
					elsif index==999
					   index = -1
					   startSponsorIndex=pageIndex*sponsorListNumber + 1
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
		        
		        func sponsorArrayFilter(JSONArray sponsorArray, int index, int pageIndex)
		            int length = JSONArray.length(sponsorArray)
		            int i = 0
		            JSONArray newSponsorArray
		            JSONObject sponsorJo
		            while i < length
		                if 999 == index && pageIndex == i/2
		                   sponsorJo = JSONArray.get(sponsorArray,i+1)
		                else
		                   sponsorJo = JSONArray.get(sponsorArray,i)
		                endif
		                if !UTIL_checkEmptyJSON(sponsorJo)
		                   JSONArray.put(newSponsorArray,sponsorJo)
		                endif
		                i = i + 2
		            endwhile
		            
		            return newSponsorArray
		        endfunc
		        
		        func getPoi()
		        	JSONArray poiJsonArray = PoiList_M_getPoiList()
		            JSONArray sponsorArray = PoiList_M_getSponsorPoiList()
          			int index = getSelectIndex() - 1
          			JSONObject locationJo
          			if index==-1 || 999 == index
          				int pageIndex = <%=PoiListModel.getPageIndex()%>
          				int sponsorPoiIndex = getSponsorIndex(pageIndex,index)
          				locationJo= JSONArray.get(sponsorArray,sponsorPoiIndex)
          			else
          				locationJo= JSONArray.get(poiJsonArray,index)	
          			endif
				   	
				   	return locationJo
				endfunc
				
				func getSponsorIndex(int pageIndex, int index)
				   int showSponsorNumber = ServerDriven_CanSponsor()
				   int sponsorPoiIndex = pageIndex * showSponsorNumber
				   if <%=SENCOND_SPONSOR_INDEX%> == index || 999 == index
				      sponsorPoiIndex = sponsorPoiIndex + 1
				   endif
				   
				   return sponsorPoiIndex
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
					
					JSONObject.put(jo,"poi",locationJo)
					JSONObject joAddress = JSONObject.get(locationJo,"stop")
					JSONObject.put(joAddress,"type",2)
					JSONObject.put(jo,"address",joAddress)
					ShareAddress_C_show(jo)
					return FAIL
				endfunc
				
				
				func logPOI_JSON(int eventType)
				   int index = getSelectIndex()
				   int poiType = <%=AttributeValues.POI_TYPE_NORMAL%>
				   int indexOnPage = 0
				   JSONArray poiArray 
   				   if(index == 0 || <%=SENCOND_SPONSOR_INDEX%> == index)
				  	   int pageIndex = <%=PoiListModel.getPageIndex()%>
				  	   index =  getSponsorIndex(pageIndex,index)
				  	   poiArray = PoiList_M_getSponsorPoiList()
				       poiType = <%=AttributeValues.POI_TYPE_SPONSORED%>
				       
				       if <%=SENCOND_SPONSOR_INDEX%> == index
				          indexOnPage = 10
				       endif
				   else
					   index = index -1
					   poiArray = PoiList_M_getPoiList()
					   indexOnPage = index%9
					   if hasSponsorPoi() && 0 != ServerDriven_CanSponsor()
					   	   indexOnPage = indexOnPage + 1
					   endif
				   endif
				   JSONObject poi = JSONArray.get(poiArray, index)
				   Logger_logPOI_JSON(eventType, "0", poi, poiType, indexOnPage)				    
				endfunc
				
				func logPOI_JSON_All(int eventType)
				   int index
				   int indexOnPage = 0
				   JSONArray poiArray
				   JSONObject poi
				   int sponsorListNumber = ServerDriven_CanSponsor()
   				   if(hasSponsorPoi() && 0 != sponsorListNumber)
				  	   index = <%=PoiListModel.getPageIndex()%>
				  	   poiArray  = PoiList_M_getSponsorPoiList()
				       poi = JSONArray.get(poiArray, index*sponsorListNumber)
				       Logger_logPOI_JSON(eventType, "0", poi, <%=AttributeValues.POI_TYPE_SPONSORED%>, indexOnPage)
				   endif
				   
				   int pageIndex = <%=PoiListModel.getPageIndex()%>
				   int pageSize = <%=Constant.PAGE_SIZE%>
				   int startIndex = pageIndex * pageSize
				   int endIndex = startIndex + pageSize
				   
				   poiArray = PoiList_M_getPoiList()
				   int currentSize = JSONArray.length(poiArray)
				   if endIndex > currentSize
				       endIndex = currentSize
				   endif
				   
				   index = startIndex
				   if(poiArray != NULL)
				       while(index < endIndex)
						   indexOnPage = index%9
						   if hasSponsorPoi() && 0 != ServerDriven_CanSponsor()
							   indexOnPage = indexOnPage + 1
						   endif
				       	   poi = JSONArray.get(poiArray, index)
				       	   Logger_logPOI_JSON(eventType, "0", poi, <%=AttributeValues.POI_TYPE_NORMAL%>, indexOnPage)
						   index = index + 1
				       endwhile
				   endif
				endfunc

		        func showMapView()
		            cancelAudio()
		            JSONArray poiJsonArray = PoiList_M_getPoiList()
		            JSONArray sponsorArray = PoiList_M_getSponsorPoiList()
					int searchType = <%=PoiListModel.getSearchType()%>
					
					int pageIndex = <%=PoiListModel.getPageIndex()%>
					
					<%if(!TnUtil.isATTRIM63(handlerGloble) && !TnUtil.isSprintRim62(handlerGloble)&& !TnUtil.isUSCCRIM62(handlerGloble)&&!TnUtil.isVNRIM62(handlerGloble)&&!TnUtil.isTMORIM62(handlerGloble)&&!TnUtil.isTMOAndroid62(handlerGloble)){ %>
					if 2 == ServerDriven_CanSponsor()
		                sponsorArray = sponsorArrayFilter(sponsorArray,0,pageIndex)
		            endif
		            <%}%>
					
					int pageSize = <%=Constant.PAGE_SIZE%>
					int startIndex = pageIndex * pageSize
					int startSponsorIndex=-1
					if StatLogger.isStatEnabled(<%=EventTypes.POI_VIEW_MAPALL%>)
						logPOI_JSON_All(<%=EventTypes.POI_VIEW_MAPALL%>)
				   	endif
				   
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
				
				func popular()
		            int relevanceType = <%=Constant.SORT_BY_POPULAE%>
					<%=PoiListModel.setSortTypeTemp("relevanceType")%>
					sort()
				endfunc
		        func relevance()
		            int relevanceType = <%=Constant.SORT_BY_RELEVANCE%>
					<%=PoiListModel.setSortTypeTemp("relevanceType")%>
					sort()		        
		        endfunc
		        func rating()
		            int ratingType = <%=Constant.SORT_BY_RATING%>
					<%=PoiListModel.setSortTypeTemp("ratingType")%>
					sort()
		        endfunc
		        func price()
		            int priceType = <%=Constant.SORT_BY_GASPRICE%>
					<%=PoiListModel.setSortTypeTemp("priceType")%>
					sort()
		        endfunc
		        func distance()
		            int distanceType = <%=Constant.SORT_BY_DISTANCE%>
					<%=PoiListModel
											.setSortTypeTemp("distanceType")%>
					sort()
		        endfunc
		        
		        func sort()
		            cancelAudio()
				    <%=PoiListModel.setPageIndexTemp("0")%>
					<%=PoiListModel.deleteShowProgressBar()%>
					<%=PoiListModel.deleteBackURLWhenNoFound()%>
					String typeOrSpeak = "<%=Constant.StorageKey.POI_MODULE_FROM_TYPE%>"
			    	<%=PoiListModel.setInputType("typeOrSpeak")%>
			    	PoiList_M_setPagingParameter()
					PoiList_M_searchPoiWithAjax()
				endfunc
				
				func backPage()
				    System.back()
				    return FAIL
				endfunc
				
				func displaySearchAlong()
					if isSearchAlongRoute()
						Page.setComponentAttribute("searchAlongButtonPanel","visible","1")
						if PoiList_M_getSearchAlongType() == <%=Constant.searchAlongType_closeAhead%>
							focusSearchAlongUpAheadButton()
						elsif PoiList_M_getSearchAlongType() == <%=Constant.searchAlongType_nearDestination%>
							focusSearchAlongNearDestButton()
						endif	 				
					else
						Page.setComponentAttribute("searchAlongButtonPanel","visible","0")	
					endif
				endfunc
				
				func onClickSearchAlongUpAhead()
					onClickSearchAlong(<%=Constant.searchAlongType_closeAhead%>)
				endfunc
				
				func onClickSearchAlongNearDest()
					onClickSearchAlong(<%=Constant.searchAlongType_nearDestination%>)
				endfunc
				
				func onClickSearchAlong(int alongType)
					PoiList_M_saveSearchAlongType(alongType)
					cancelAudio()
				    <%=PoiListModel.setPageIndexTemp("0")%>
					<%=PoiListModel.deleteBackURLWhenNoFound()%>
					String typeOrSpeak = "<%=Constant.StorageKey.POI_MODULE_FROM_TYPE%>"
			    	<%=PoiListModel.setInputType("typeOrSpeak")%>
			    	PoiList_M_setPagingParameter()
					PoiList_M_searchPoiWithAjax()				
				endfunc				
				
				func focusSearchAlongUpAheadButton()
					Page.setComponentAttribute("searchAlongUpAheadButton","imageClick","$searchAlongUpAheadImgClickSelButton")
					Page.setComponentAttribute("searchAlongUpAheadButton","imageUnclick","$searchAlongUpAheadImgClickButton")
					
					Page.setComponentAttribute("searchAlongNearDestButton","imageClick","$searchAlongNearDestImgUnclickSelButton")
					Page.setComponentAttribute("searchAlongNearDestButton","imageUnclick","$searchAlongNearDestImgUnclickButton")
					# fontColor == blurFontColor					
					Page.setComponentAttribute("searchAlongUpAheadButton","fontColor","white")
					Page.setComponentAttribute("searchAlongUpAheadButton","focusFontColor","white")
					Page.setComponentAttribute("searchAlongNearDestButton","fontColor","black")
					Page.setComponentAttribute("searchAlongNearDestButton","focusFontColor","black")
				endfunc
				
				func focusSearchAlongNearDestButton()
					Page.setComponentAttribute("searchAlongUpAheadButton","imageClick","$searchAlongUpAheadImgUnclickSelButton")
					Page.setComponentAttribute("searchAlongUpAheadButton","imageUnclick","$searchAlongUpAheadImgUnclickButton")					
					
					Page.setComponentAttribute("searchAlongNearDestButton","imageClick","$searchAlongNearDestImgClickSelButton")
					Page.setComponentAttribute("searchAlongNearDestButton","imageUnclick","$searchAlongNearDestImgClickButton")
					# fontColor == blurFontColor	
					Page.setComponentAttribute("searchAlongUpAheadButton","fontColor","black")
					Page.setComponentAttribute("searchAlongUpAheadButton","focusFontColor","black")
					Page.setComponentAttribute("searchAlongNearDestButton","fontColor","white")
					Page.setComponentAttribute("searchAlongNearDestButton","focusFontColor","white")
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
		            elsif isSortByPrice(categoryId)
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
					else
						if 0 != ServerDriven_CanSponsor() && hasSponsorPoi() 
						    Page.setControlProperty("sponsorPoi_0","focused","true")
						else
							Page.setControlProperty("item1","focused","true")
						endif
					endif
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
