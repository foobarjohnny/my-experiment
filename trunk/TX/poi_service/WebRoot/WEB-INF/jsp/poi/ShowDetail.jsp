<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ include file="../Header.jsp"%>
<%@ include file="../GetClientInfo.jsp"%>
<%@page import="java.util.PropertyResourceBundle"%>
<%@page import="com.telenav.cserver.util.TnUtil"%>
<%@page import="com.telenav.cserver.util.FeatureConstant"%>
<%@page	import="com.telenav.cserver.pref.struts.util.PreferenceConstants"%>
<%@page import="com.telenav.cserver.poi.model.PoiListModel"%>
<%@page import="com.telenav.cserver.stat.*"%>

<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%
	String ShowDetailURL = getPageCallBack + "ShowDetail";
	String imageSolid = imageUrl + "small_solid_star.png";
	String imageHalf = imageUrl + "small_half_star.png";
	String imageUnSolid = imageUrl + "small_unsolid_star.png";

	String reviews_imageSolid = imageUrl + "small_solid_star.png";
	String reviews_imageUnSolid = imageUrl + "small_unsolid_star.png";
	String citySearch = imageUrl + "Citysearch.png";
	String dollarsign_green = imageUrl + "dollarsign_green.png";
	String dollarsign_grey = imageUrl + "dollarsign_grey.png";
	String menuImage = imageUrl + "menu.png";
	String couponsImage = imageUrl + "coupon.png";

	String sponsorImage = imageUrl + "ads_pin.png";
	String flagImage = imageUrl + "big_half_star.png";
	String tmpImg = "";
	
	String menuTabID = "menuTab";
	String couponTabID = "coupons";
	String restaurantHost = "{restaurant.http}";
	String restaurantUrl = restaurantHost + "/goToJsp.do?pageRegion=NA&amp;jsp=DetailAndReservationPage";
	
%>


<tml:TML outputMode="TxNode">
	<%@ include file="model/ShowDetailModel.jsp"%>
	<%@ include file="controller/PoiListController.jsp"%>
	<%@ include file="controller/ShowReviewsController.jsp"%>
	<%@ include file="/WEB-INF/jsp/controller/DriveToController.jsp"%>
	<%@ include file="controller/EditPoiController.jsp"%>
	<%@ include file="../ac/controller/AddressCaptureController.jsp"%>
	<%@ include file="controller/RatePoiController.jsp"%>
	<%@ include file="../ac/controller/ShareAddressController.jsp"%>
	<%@ include
		file="/WEB-INF/jsp/ac/controller/CreateFavoritesController.jsp"%>
	<jsp:include page="../ac/controller/EditRouteController.jsp" />
	<%@ include file="../PoiUtil.jsp"%>
	<jsp:include
		page="/WEB-INF/jsp/local_service/controller/MapWrapController.jsp" />
	<jsp:include page="StatLogger.jsp"/>

	<tml:script language="fscript" version="1">
		<![CDATA[
		<%@ include file="../Stop.jsp"%>
		<%@ include file="../CommonScript.jsp"%>
		<%@ include file="ShowRateImageScript.jsp"%>
		<%@ include file="../GetServerDriven.jsp"%>
		
	<%if (featureMgr.isEnabled(FeatureConstant.UGC_VIEW)) {%>
		<%@ include file="ShowRateImageScriptForReviews.jsp"%>
	<%}%>
		        func preLoad()
		            int sponsor = ShowDetail_M_getIsSponsorForDetail()
					showDetail(sponsor)
					#checkServerDriven()
					
					int tabIndex = ShowDetail_M_getShowTabIndex()
		            if 1 == tabIndex
		               Page.setComponentAttribute("showDetailContainer","defaultFocus","1")
		               ShowDetail_M_deleteShowTabIndex()
		            else
		               Page.setComponentAttribute("showDetailContainer","defaultFocus","0")
		            endif
				endfunc
				
				func onShow()
				    System.setKeyEventListener("-p-n-right-left-","keypress")
				endfunc
				
				func keypress(string s)
		        	if s == "p"
		        	   showPrevious()
		        	elsif s == "n"
		        	   showNext()
		        	elsif s == "left" || s == "right"
		        	   saveMISLog()
		        	endif
				endfunc
				
				func onLoad()
			<%if (featureMgr.isEnabled(FeatureConstant.UGC_VIEW)) {%>
				    #getReviewsForPoi()
			<%}%>
				endfunc
				
				func checkServerDriven()
				    int canShareAddress = ServerDriven_CanShareAddress()
				    MenuItem.setItemValid("page",5,canShareAddress)
		    		MenuItem.commitSetItemValid("page")
				endfunc
				
				func saveMISLog()
				    int index = Page.getControlProperty("showDetailContainer","focusedIndex")
					int couponIndex = 1 
					int menuIndex = 2
					<%if (featureMgr.isEnabled(FeatureConstant.UGC_VIEW)) {%>
						couponIndex = 2 
						menuIndex = 3
					<%}%>
				    if index == couponIndex
				    	onTabChange("<%=couponTabID%>")
				    elseif index == menuIndex
				    	onTabChange("<%=menuTabID%>")
				    endif
				endfunc
		        
				func showDetailInTreeState(int sponsor)
				    if sponsor == 1 
				       Page.setComponentAttribute("sponsorImage","visible","1")
				    else
				       Page.setComponentAttribute("sponsorImage","visible","0")
				    endif
				endfunc
				
				func showDetail(int sponsor)
				    showDetailInTreeState(sponsor)
				    
				    # Address list
		            JSONArray poiJsonArray = ShowDetail_M_getAddressList()
					# Size
					int size =  JSONArray.length(poiJsonArray)
					
					# Index
					int index = ShowDetail_M_getIndex()
				
					String detailTitle = "<%=msg.get("poi.sponsor.title")%>"
					if 0 == sponsor 
						detailTitle = index + 1 + " " + "<%=msg.get("common.of")%>" + " " + size + " " + "<%=msg.get("poi.list.results")%>"
                    endif
					Page.setComponentAttribute("DetailTitle","text",detailTitle)
					
					JSONObject locationJo = JSONArray.get(poiJsonArray,index)
					
					int priceRange = JSONObject.get(locationJo,"priceRange")
					if 0 == priceRange
					   Page.setComponentAttribute("dollarsign_1","visible","0")
					   Page.setComponentAttribute("dollarsign_2","visible","0")
					   Page.setComponentAttribute("dollarsign_3","visible","0")
					   Page.setComponentAttribute("dollarsign_4","visible","0")
					else
					   Page.setComponentAttribute("dollarsign_1","visible","1")
					   Page.setComponentAttribute("dollarsign_2","visible","1")
					   Page.setComponentAttribute("dollarsign_3","visible","1")
					   Page.setComponentAttribute("dollarsign_4","visible","1")
					   showPriceRangeImage(priceRange)
					endif
					
					int rating = JSONObject.get(locationJo,"rating")
					String nameTitle
					String address
					String phone=""
					String poiName=""
					
					#Set rating star image
					String categoryId = <%=PoiListModel.getCategoryId()%>
					int isGasByPrice = PoiList_C_isGasByPrice(categoryId)
					
					int rateMenuIndex = 8
					<%if (featureMgr.isEnabled(FeatureConstant.UGC_EDIT)) {%>
						rateMenuIndex = 9
					<%}%>
					
                    if 0 == sponsor && 0==Account.isTnMaps() && 0 == isGasByPrice
                       showStarIcons(0)
					   showRateStarImage(rating,0)
					   
					   if JSONObject.has(locationJo,"ratingNumber")
						   int ratingNumber = JSONObject.get(locationJo,"ratingNumber")
						   if 0 != ratingNumber
						       String ratingNumberStr = "(" + ratingNumber + ")"
							   Page.setComponentAttribute("ratingNumber","text",ratingNumberStr)
						   else
						       Page.setComponentAttribute("ratingNumber","text","")
						   endif
					   else
						   Page.setComponentAttribute("ratingNumber","text","")
					   endif
					   Page.setComponentAttribute("ratingNumber","visible","1")
					   Page.setComponentAttribute("Reviews","visible","1")
                       MenuItem.setItemValid("page",rateMenuIndex,TRUE)
                       MenuItem.setItemValid("page",8,TRUE)
                    else
                       hideStarIcons(0)
		               Page.setComponentAttribute("ratingNumber","visible","0")
		               Page.setComponentAttribute("Reviews","visible","0")
                       MenuItem.setItemValid("page",rateMenuIndex,FALSE)
                       MenuItem.setItemValid("page",8,FALSE)
                    endif
					
					# Street, city and phone
					int latAndLon = 1
					JSONObject stop =  JSONObject.get(locationJo,"stop")
					address = getWrapAddress_JSON(stop)
					
					#handle no address
					if JSONObject.get(stop,"lat")==0 || JSONObject.get(stop,"lon")==0
						latAndLon = 0
					endif
					
					phone = JSONObject.get(locationJo,"phoneNumber")
					int hidePhoneMenu = 0
					if NULL != phone
						if "" != phone
							phone = UTIL_formatPhoneNumber(phone)
							hidePhoneMenu = 1
					        Page.setComponentAttribute("phone","text",phone)
					    else
					        Page.setComponentAttribute("phone","text","<%=msg.get("poi.nophone")%>")
						endif
					else
					    Page.setComponentAttribute("phone","text","")
					endif
					
										<%if (featureMgr.isEnabled(FeatureConstant.RESTAURANT)) {
					%>
					if ServerDriven_CanRestaurant()
					
						#set restaurant info 
						println(" locationJo " + locationJo)
						if JSONObject.has(locationJo,"restaurant") && 0==Account.isTnMaps()
							JSONObject restaurantJo = JSONObject.get(locationJo,"restaurant")
							if(JSONObject.has(restaurantJo,"isReservable"))
								Page.setComponentAttribute("reservationLink","visible","1")
								int partnerPoiId = JSONObject.getInt(restaurantJo,"partnerPoiId")
								TxNode partnerPoiIdNode
								TxNode.addValue(partnerPoiIdNode,partnerPoiId)
								MenuItem.setBean("callViewReservationScript", "partnerPoiId", partnerPoiIdNode)
								
								# Get lat&lon of anchor address 
								JSONObject addressJO = SearchPoi_M_getLocation()
								TxNode lonNode
								TxNode latNode
								int lon = 0
								int lat = 0
								if NULL != addressJO
									lon = JSONObject.getInt(addressJO,"lon")
									lat = JSONObject.getInt(addressJO,"lat")
								endif
								TxNode.addValue(lonNode,lon)
								TxNode.addValue(latNode,lat)
								MenuItem.setBean("callViewReservationScript", "anchorLon", lonNode)
								MenuItem.setBean("callViewReservationScript", "anchorLat", latNode)
								TxNode distanceUnitNode = Preference.getPreferenceValue(1)
								if NULL != distanceUnitNode
									MenuItem.setBean("callViewReservationScript", "distanceUnit", distanceUnitNode)
								endif
							else
								Page.setComponentAttribute("reservationLink","visible","0")
								Page.setComponentAttribute("reservationLink","height","1")
								
							endif						
						else
							Page.setComponentAttribute("reservationLink","visible","0")
							Page.setComponentAttribute("reservationLink","height","1")
						endif
					else
						Page.setComponentAttribute("reservationLink","visible","0")
						Page.setComponentAttribute("reservationLink","height","1")
					endif
					<%}%>
					
					
					
					#if no lat&lon,hide menu "Drive To", if no phone, hide menu "Call ****"
					hideMenuWhenNoLatLon(latAndLon,hidePhoneMenu)
					
					#Set poi name
					poiName = JSONObject.get(locationJo,"name")
					String distance = JSONObject.get(locationJo,"distance")
					if NULL == distance || "" == distance || "0" == distance
					   poiName = "<bold>" + poiName + "</bold>"
					else
					   poiName = "<bold>" + poiName + "</bold> <blue>(" + distance +")</blue>"
					endif
					Page.setComponentAttribute("poiName","text",poiName)
					
					#set ad line or gas price if there is any
					if (JSONObject.has(locationJo,"ad") || JSONObject.has(locationJo,"gasPirce")) 
						String mContent = ""
						if (JSONObject.has(locationJo,"gasPirce"))
							mContent = gasPriceDetails(JSONObject.get(locationJo,"gasPirce"))
						endif
				
						JSONObject ad = JSONObject.get(locationJo,"ad")
						if(JSONObject.has(ad,"adSource"))
							String adSourceIcon ="<%=imageUrl%>"+JSONObject.get(ad,"adSource")+"_logo.png"
							Page.setControlProperty("citySearchDetail","image",adSourceIcon)
							Page.setControlProperty("citySearchCoupon","image",adSourceIcon)
							Page.setControlProperty("citySearchMenu","image",adSourceIcon)
							println(adSourceIcon) 
							showAdsLogo("1")
						else 
							showAdsLogo("0")
						endif
						if(JSONObject.has(ad,"adLine"))
							if mContent != ""
								mContent = "\n \n" + mContent
							endif
							mContent = mContent + JSONObject.get(ad,"adLine")+"\n \n "
						endif
						
						if(mContent != "")
							Page.setComponentAttribute("sponsorInformation","text",mContent)
							showAdsComponent("1")
						else
							showAdsComponent("0")
						endif
					else
							showAdsComponent("0")
					endif
					
					#set coupon info 
					TxNode needDealNode = Preference.getPreferenceValue(<%=PreferenceConstants.KEY_GENERAL_DEALS%>)
					int isDealOn = <%=PreferenceConstants.VALUE_NEED_DEALS_ON%>
					if NULL != needDealNode && 1 <= TxNode.getValueSize(needDealNode)
					   isDealOn = TxNode.valueAt(needDealNode, 0)
					endif
					println("isDealOn..................."+isDealOn)
					if JSONObject.has(locationJo,"coupon") && <%=PreferenceConstants.VALUE_NEED_DEALS_ON%> == isDealOn
						JSONArray couponArray = JSONObject.get(locationJo,"coupon")
						int i =0
						int length = JSONArray.length(couponArray)
						Page.setComponentAttribute("<%=couponTabID%>","visible","1")
						JSONObject coupon
						while(i<length)
							 coupon= JSONArray.get(couponArray,i)
							if(JSONObject.has(coupon,"desc"))
								Page.setComponentAttribute("couponDesc"+i,"text",JSONObject.get(coupon,"desc"))
							endif
							if(JSONObject.has(coupon,"endDate"))
								Page.setComponentAttribute("expireDate"+i,"text","Offer is valid until "+JSONObject.get(coupon,"endDate"))
							endif
							if(JSONObject.has(coupon,"img"))
								String imgData = JSONObject.getString(coupon,"img")
								
								Page.setControlProperty("couponImg"+i,"imageStringFormData",imgData)
								int height=Page.getControlProperty("couponImg0","height")
							endif
							i = i+1
						endwhile
					else
						Page.setComponentAttribute("<%=couponTabID%>","visible","0")
					endif
										
					#set menu info
					TxNode needMenuNode = Preference.getPreferenceValue(<%=PreferenceConstants.KEY_GENERAL_MENU%>)
					int isMenuOn = <%=PreferenceConstants.VALUE_NEED_MENU_ON%>
					if NULL != needMenuNode && 1 <= TxNode.getValueSize(needMenuNode)
					   isMenuOn = TxNode.valueAt(needMenuNode, 0)
					endif
					println("isMenuOn..................."+isMenuOn)
					if JSONObject.has(locationJo,"menu") && <%=PreferenceConstants.VALUE_NEED_MENU_ON%> == isMenuOn
						JSONObject menuJo = JSONObject.get(locationJo,"menu")
						if JSONObject.has(menuJo,"menu") && <%=PreferenceConstants.VALUE_NEED_MENU_ON%> == isMenuOn
							Page.setComponentAttribute("menu","text",JSONObject.get(menuJo,"menu")+"\n ")
							Page.setComponentAttribute("<%=menuTabID%>","visible","1")
						else
							Page.setComponentAttribute("<%=menuTabID%>","visible","0")
						endif
					else
						Page.setComponentAttribute("<%=menuTabID%>","visible","0")
					endif
					
					#Set text
					Page.setComponentAttribute("address","text",address)
					
			<%if (featureMgr.isEnabled(FeatureConstant.UGC_VIEW)) {%>
					showReviews(locationJo)
			<%}%>
					
					String callString = "<%=msg.get("poi.call")%>" + " " + phone
					
			<%if (featureMgr.isEnabled(FeatureConstant.MOVIE)) {%>
					#check if it is Movie Theaters category
					String theaterId = "" + JSONObject.get(locationJo, "poiId")
					String poiCategoryId = JSONObject.get(locationJo, "categoryId")
					TxNode tID
					if "<%=Constant.MOVIE_THEATER_CATEGORY_ID%>" == poiCategoryId
						TxNode.addMsg(tID, theaterId)
						MenuItem.setBean("viewMovies", "theaterId", tID)
						# please do not change "page", it is reserved key word to 
						# access page associated menu
						MenuItem.setItemValid("page", 2, TRUE)
						# current date as default value
						String dateStr = Time.format("yyyy-MM-dd", -1)
						TxNode nodeDate
						TxNode.addMsg(nodeDate, dateStr)
						MenuItem.setBean("viewMovies", "dateIndex", nodeDate)
					else
						MenuItem.setItemValid("page", 2, FALSE)
					endif
					MenuItem.setItemText("page", 3, callString)
			<%} else {%>
					MenuItem.setItemText("page", 2, callString)
			<%}%>
					MenuItem.commitSetItemValid("page")
					String logText = poiName + ";" + address + ";" + Time.get()
					logPoi(logText)
					#System.repaint()
				   if StatLogger.isStatEnabled(<%=EventTypes.POI_DETAILS%>)
						logPOI_JSON(<%=EventTypes.POI_DETAILS%>)				    
				   endif
				   
				   if StatLogger.isStatEnabled(<%=EventTypes.POI_VIEW_MERCHANT%>)
						String showAdsOn = getShowMessageOn("1")
						if JSONObject.has(ad,"adLine") && "1" == showAdsOn
							logPOI_JSON(<%=EventTypes.POI_VIEW_MERCHANT%>)
						endif				    
				   endif
				endfunc
				
				func hideMenuWhenNoLatLon(int latAndLon,int hidePhoneMenu)
					if Account.isTnMaps()==1
						MenuItem.setItemValid("page",0,FALSE)
					    MenuItem.setItemValid("page", 1, latAndLon)
					else
						MenuItem.setItemValid("page",1,FALSE)
					    MenuItem.setItemValid("page", 0, latAndLon)
					endif
					
					MenuItem.setItemValid("page", 4, latAndLon)
					
					if (ServerDriven_CanShareAddress() && (1 == latAndLon))
					    MenuItem.setItemValid("page", 5, TRUE)
					else
					    MenuItem.setItemValid("page", 5, FALSE)
					endif
					
					MenuItem.setItemValid("page", 6, latAndLon)
				    MenuItem.setItemValid("page", 3, hidePhoneMenu)
				endfunc
					
				func showAdsLogo(String showLogo)
					Page.setComponentAttribute("citySearchDetail","visible",showLogo)
				endfunc
				
				func showAdsComponent(String showAds)
					showAds = getShowMessageOn(showAds)
					
					Page.setComponentAttribute("sponsorInformation","visible",showAds)
					Page.setComponentAttribute("adsDelimiter","visible",showAds)
				endfunc
				
				func getShowMessageOn(String showAds)
				    TxNode showMessageNode = Preference.getPreferenceValue(<%=PreferenceConstants.KEY_GENERAL_MERCHANT%>)
					int isShowMessageOn = <%=PreferenceConstants.VALUE_NEED_MERCHANT_ON%>
					if NULL != showMessageNode && 1 <= TxNode.getValueSize(showMessageNode)
					   isShowMessageOn = TxNode.valueAt(showMessageNode, 0)
					endif
					
					println("isShowMessageOn......................."+isShowMessageOn)
					if <%=PreferenceConstants.VALUE_NEED_MERCHANT_ON%> != isShowMessageOn
					   showAds = "0"
					endif
					
					return showAds
				endfunc
				
				func gasPriceDetails(JSONArray arr)
					String result = ""
					int i=0
					JSONObject price
					int timeL
					while i< JSONArray.length(arr)
						price = JSONArray.get(arr, i)
						result = result + "<bold>" +JSONObject.get(price, "price") + "</bold>"
						timeL = String.convertToNumber(JSONObject.getString(price, "timeL")) 
						result =  result + " (" + Time.format("MMM d, yyyy", timeL) + ")\n"
						i=i+1
					endwhile
					return result
				endfunc
									
				func showPrevious()
					int totalIndex = ShowDetail_M_getTotalIndexForDetail()
				    if totalIndex == 0
				       return FAIL
				    endif
				    		
				    int sponsorSize = ShowDetail_M_getSponsorSizeForDetail()
		            int index = ShowDetail_M_getIndex()
				    int isSponsorPoi = 0
				    index = index - 1
				    if 0 != sponsorSize
				        JSONArray poiJsonArray
				        isSponsorPoi = ShowDetail_M_getIsSponsorForDetail()
				        if 0 == isSponsorPoi
					       if 0 == (index + 1)%<%=Constant.PAGE_SIZE%>
					          int sponsorIndex = (index + 1)/<%=Constant.PAGE_SIZE%>
					          if sponsorSize > sponsorIndex
					             index = sponsorIndex
					             isSponsorPoi = 1
						         poiJsonArray = ShowDetail_M_getSponsorPoiList()
						         ShowDetail_M_saveAddressList(poiJsonArray)  
					          endif
					       endif
					    else
					       index = (index + 1) * <%=Constant.PAGE_SIZE%> - 1
				           isSponsorPoi = 0
				           poiJsonArray = ShowDetail_M_getPoiList()
				           ShowDetail_M_saveAddressList(poiJsonArray)  
					    endif
				    endif
		            
		            totalIndex = totalIndex - 1
				    ShowDetail_M_saveTotalIndexForDetail(totalIndex)
					
					TxNode newIndexNode
					TxNode.addValue(newIndexNode, index)
					ShowDetail_M_saveNewIndex(newIndexNode)
					ShowDetail_M_saveIsSponsorForDetail(isSponsorPoi)
					showDetail(isSponsorPoi)
					
			<%if (featureMgr.isEnabled(FeatureConstant.UGC_VIEW)) {%>
				    #getReviewsForPoi()
			<%}%>
			        Page.setComponentAttribute("showDetailContainer","defaultFocus","0")
				endfunc
				
				func showNext()
					int totalSize = ShowDetail_M_getTotalSize()
					int totalIndex = ShowDetail_M_getTotalIndexForDetail()
				    if totalIndex >= totalSize - 1
				       return FAIL
				    endif
				
				    int sponsorSize = ShowDetail_M_getSponsorSizeForDetail()
		            int index = ShowDetail_M_getIndex()
		            index = index + 1
		            int isSponsorPoi = 0
		            if 0 != sponsorSize
		                JSONArray poiJsonArray
		                isSponsorPoi = ShowDetail_M_getIsSponsorForDetail()
				        if 0 == isSponsorPoi
					       if 0 == index%<%=Constant.PAGE_SIZE%>
					          int sponsorIndex = index/<%=Constant.PAGE_SIZE%>
					          if sponsorSize > sponsorIndex
					             index = sponsorIndex
					             isSponsorPoi = 1
						         poiJsonArray = ShowDetail_M_getSponsorPoiList()
						         ShowDetail_M_saveAddressList(poiJsonArray)  
					          endif
					       endif
					    else
					       index = (index - 1) * <%=Constant.PAGE_SIZE%>
				           isSponsorPoi = 0
				           poiJsonArray = ShowDetail_M_getPoiList()
				           ShowDetail_M_saveAddressList(poiJsonArray)  
					    endif
		            endif
				    
		            totalIndex = totalIndex + 1
				    ShowDetail_M_saveTotalIndexForDetail(totalIndex)
		            
					TxNode newIndexNode
					TxNode.addValue(newIndexNode, index)
					ShowDetail_M_saveNewIndex(newIndexNode)
					ShowDetail_M_saveIsSponsorForDetail(isSponsorPoi)
					showDetail(isSponsorPoi)
					
			<%if (featureMgr.isEnabled(FeatureConstant.UGC_VIEW)) {%>
				    #getReviewsForPoi()
			<%}%>
			        Page.setComponentAttribute("showDetailContainer","defaultFocus","0")
				endfunc
				
				func showPriceRangeImage(int priceRange)
				    String urlForImage2 = ""
				    String urlForImage3 = ""
				    String urlForImage4 = ""
				    String urlForImage5 = ""
				    if 1 == priceRange
					    urlForImage2 = "<%=dollarsign_grey%>"
					    urlForImage3 = "<%=dollarsign_grey%>"
					    urlForImage4 = "<%=dollarsign_grey%>"
				    elsif 2 == priceRange
					    urlForImage2 = "<%=dollarsign_green%>"
					    urlForImage3 = "<%=dollarsign_grey%>"
					    urlForImage4 = "<%=dollarsign_grey%>"
				    elsif 3 == priceRange
					    urlForImage2 = "<%=dollarsign_green%>"
					    urlForImage3 = "<%=dollarsign_green%>"
					    urlForImage4 = "<%=dollarsign_grey%>"
				    else
					    urlForImage2 = "<%=dollarsign_green%>"
					    urlForImage3 = "<%=dollarsign_green%>"
					    urlForImage4 = "<%=dollarsign_green%>"
				    endif
				    
					Page.setControlProperty("dollarsign_2","image",urlForImage2)
					Page.setControlProperty("dollarsign_3","image",urlForImage3)
					Page.setControlProperty("dollarsign_4","image",urlForImage4)
				endfunc
				
				#From POI detail,the address showing in two rows
				func getWrapAddress_JSON(JSONObject stop)
					String addressStr = ""
					if NULL != stop
						String firstLine = JSONObject.get(stop,"firstLine")
					    if NULL != firstLine
					       addressStr = addressStr + firstLine
					    endif
						if "" != addressStr
							addressStr = addressStr + "\r\n"  
						endif
						addressStr = addressStr + STOP_getSecondLine_JSON(stop)
						addressStr = addressStr + " "
						String zipString = JSONObject.get(stop,"zip")
						if NULL != zipString
					       addressStr = addressStr + zipString
					    endif
					endif
					return addressStr
				endfunc
				
				func showMap()
				   JSONArray poiJsonArray = ShowDetail_M_getPoiList()
				   int index = ShowDetail_M_getIndex()
				   int adsIndex =-1
				   int totalIndex=0
				   JSONArray sponsorArray = ShowDetail_M_getSponsorPoiList()

				   JSONObject poiJO
   				   if(ShowDetail_M_getIsSponsorForDetail()==1)
				  	 index=-1
				  	 totalIndex = ShowDetail_M_getTotalIndexForDetail()
				  	 adsIndex=totalIndex/10
				  	 poiJO = JSONArray.get(sponsorArray,adsIndex)
				   else
				     poiJO = JSONArray.get(poiJsonArray,index)
				   endif
				   saveToRencentPlaces(poiJO)
				   int searchType = <%=PoiListModel.getSearchType()%>
				   JSONObject stop = SearchPoi_M_getLocation()
				   if <%=PoiListModel.SearchType.SEARCH_ALONGROUTE%> == searchType
					  MapWrap_C_showPoiAlongRoute(poiJsonArray,sponsorArray,index, adsIndex)
				   else
					  MapWrap_C_showPoi(poiJsonArray,sponsorArray,index, toMapStop(stop),adsIndex)
				   endif
				   if StatLogger.isStatEnabled(<%=EventTypes.POI_VIEW_MAP%>)
						logPOI_JSON(<%=EventTypes.POI_VIEW_MAP%>)				    
				   endif
				   return FAIL
				endfunc
				
				func saveToRencentPlaces(JSONObject poiJO)
				   TxNode node = PoiUtil_convertToNodeForResentSearch(poiJO)
				   RecentPlaces.saveAddress(node)
				endfunc
					
				func toMapStop(JSONObject stop)
					JSONObject mapStop
					if(JSONObject.get(stop,"type")!=6)
						String lastLine = JSONObject.get(stop,"city")+","+JSONObject.get(stop,"state")
						String firstLine = JSONObject.get(stop,"firstLine")
						JSONObject.put(mapStop,"FirstLine",firstLine)
						JSONObject.put(mapStop,"SecondLine",lastLine)
					endif
					JSONObject.put(mapStop,"Lat",JSONObject.get(stop,"lat"))
					JSONObject.put(mapStop,"Lon",JSONObject.get(stop,"lon"))
					JSONObject.put(mapStop,"type",JSONObject.get(stop,"type"))
					return mapStop
				endfunc	
					
					
				func DriveTo()
				   JSONArray poiJsonArray = ShowDetail_M_getAddressList()
				   int index = ShowDetail_M_getIndex()
				   JSONObject locationJo = JSONArray.get(poiJsonArray,index)
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
				
					func staticRoute()
					JSONObject poiInformationJo
				    # Address list
				    JSONArray poiJsonArray = ShowDetail_M_getAddressList()
					# Index
					int index = ShowDetail_M_getIndex()
					JSONObject locationJo = JSONArray.get(poiJsonArray,index)
					saveToRencentPlaces(locationJo)
					JSONObject stop = JSONObject.get(locationJo,"stop")
						   JSONObject.put(stop,"type",5)
					EditRoute_C_StaticRoute(stop)
					
				endfunc
				
				func rateThis()
				    # Address list
		            JSONArray poiJsonArray = ShowDetail_M_getAddressList()
					# Index
					int index=ShowDetail_M_getIndex()
					JSONObject locationJo = JSONArray.get(poiJsonArray,index)
					
					ShowDetail_M_savePoiToDo(locationJo)
					
					RatePoi_C_saveBackUrl("<%=getPageCallBack + "ShowDetail"%>")
				endfunc
				
				
				func createFavorites()
				    JSONArray poiJsonArray = ShowDetail_M_getAddressList()
				    int index = ShowDetail_M_getIndex()
				    JSONObject poiJo = JSONArray.get(poiJsonArray,index)
				    JSONObject locationJo = JSONObject.get(poiJo,"stop")
				    JSONObject jo
		        	JSONObject.put(jo,"from","POI")
					JSONObject.put(jo,"callbackpageurl","<%=ShowDetailURL%>")
					CreateFavorites_C_onShow(locationJo,jo,poiJo)
					return FAIL	
				endfunc
				
				func call()
				    JSONArray poiJsonArray = ShowDetail_M_getAddressList()
				    int index = ShowDetail_M_getIndex()
				    JSONObject locationJo = JSONArray.get(poiJsonArray,index)
				    String phone = JSONObject.get(locationJo,"phoneNumber")
				
				    TxNode ivrInput
	        		TxNode.addMsg(ivrInput,phone)
	        		MenuItem.setBean("phoneCall", "phonenumber", ivrInput)
	        		System.doAction("phoneCall")
				    if StatLogger.isStatEnabled(<%=EventTypes.POI_CALL_TO%>)
						logPOI_JSON(<%=EventTypes.POI_CALL_TO%>)				    
				    endif
				endfunc
				
				func logPOI_JSON(int eventType)
				   int index
				   int poiType = <%=AttributeValues.POI_TYPE_NORMAL%>
				   int indexOnPage = 0
				   JSONArray poiArray 
   				   if(ShowDetail_M_getIsSponsorForDetail()==1)
				  	   int totalIndex = ShowDetail_M_getTotalIndexForDetail()
				  	   index=totalIndex/10
				  	   poiArray = ShowDetail_M_getSponsorPoiList()
				       poiType = <%=AttributeValues.POI_TYPE_SPONSORED%>
				   else
					   index = ShowDetail_M_getIndex()
					   poiArray = ShowDetail_M_getPoiList()
					   indexOnPage = index/8 + 1
				   endif
				   JSONObject poi = JSONArray.get(poiArray, index)
				   Logger_logPOI_JSON(eventType, "1", poi, poiType, indexOnPage)				    
				endfunc
				
				func onTabChange(String tabID)
					# this id should be changed in sync with tml tabID
					if tabID == "<%=couponTabID%>"
					    if StatLogger.isStatEnabled(<%=EventTypes.POI_VIEW_COUPON%>)
							logPOI_JSON(<%=EventTypes.POI_VIEW_COUPON%>)				    
					    endif
					elseif tabID == "<%=menuTabID%>"
					    if StatLogger.isStatEnabled(<%=EventTypes.POI_VIEW_MENU%>)
							logPOI_JSON(<%=EventTypes.POI_VIEW_MENU%>)				    
					    endif
					endif
				endfunc
				
			]]>
	</tml:script>

	<tml:script language="fscript" version="1"
		feature="<%=FeatureConstant.SHARE_ADDRESS%>">
		<![CDATA[
				func showShareAddress()
				    JSONObject jo
		        	JSONObject.put(jo,"callbackfunction","CallBack_ShareAddress")
					JSONObject.put(jo,"callbackpageurl","<%=getPageCallBack + "ShowDetail"%>")
					
					# Address list
		            JSONArray poiJsonArray = ShowDetail_M_getAddressList()
					# Index
					int index=ShowDetail_M_getIndex()
					JSONObject locationJo = JSONArray.get(poiJsonArray,index)
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
		]]>
	</tml:script>

	<tml:script language="fscript" version="1"
		feature="<%=FeatureConstant.UGC_VIEW%>">
		<![CDATA[
		
		        func getReviewsForPoi()
		            JSONArray poiJsonArray = ShowDetail_M_getAddressList()
				    int index = ShowDetail_M_getIndex()
				    JSONObject locationJo = JSONArray.get(poiJsonArray,index)
				    
				    int poiId = JSONObject.get(locationJo,"poiId") 
				    String poiIdStr = poiId + ""
				    
				    JSONObject poiObject
				    JSONObject.put(poiObject,"poiId",poiIdStr)
				    
				    String poiString = JSONObject.toString(poiObject)
				    
				    TxNode node
				    TxNode.addMsg(node,poiString)
				    
				    TxRequest req
					String url="<%=host + "/reviewPoi.do"%>"
					String scriptName="reviewsCallback"
					TxRequest.open(req,url)
					TxRequest.setRequestData(req,node)
					TxRequest.onStateChange(req,scriptName)
					TxRequest.setProgressTitle(req,"Searching ...")
					TxRequest.send(req)
		        endfunc
		        
		        func reviewsCallback(TxNode node,int status)
		            if status == 0
		                System.showErrorMsg("<%=msg.get("common.internal.error")%>")
					    return FAIL
					elsif status == 1
					    int size = TxNode.getChildSize(node)
						if size >= 10
							size = 10
						endif
                        
                        int i = 0
                        TxNode reviewNode
                        String truncatedReview = ""
                        String reviewerName = ""
                        String reviewTime = ""
                        int rating = 0
                        
                        if 0 == size
                           Page.setComponentAttribute("noReviews","visible","1")
                        else
                           Page.setComponentAttribute("noReviews","visible","0")
                        endif
                        
                        String nameAndTime = ""
                        String firstLine_itemId = ""
                        String secondLinde_itemId = ""
                        String itemId = ""
                        while i < size
                           reviewNode = TxNode.childAt(node,i)
                           truncatedReview = TxNode.msgAt(reviewNode,0)
                           reviewerName = TxNode.msgAt(reviewNode,1)
                           reviewTime = TxNode.msgAt(reviewNode,2)
                           rating = TxNode.valueAt(reviewNode,2)
                           if "" != reviewerName
                              nameAndTime =  reviewerName + ".-" + reviewTime
                           else
                              nameAndTime =  reviewTime
                           endif
                           
                           firstLine_itemId = "reviews_info" + i
                           Page.setComponentAttribute(firstLine_itemId,"text",truncatedReview)
                           secondLinde_itemId = "name_time" + i
                           Page.setComponentAttribute(secondLinde_itemId,"text",nameAndTime)
                           itemId = "item" + i
						   Page.setComponentAttribute(itemId,"visible","1")
						   showStarsForReviews(rating,i)
                           i = i + 1
                        endwhile	
                        				    
					    while i < 10
							itemId = "item" + i
							Page.setComponentAttribute(itemId,"visible","0")
							i = i + 1
						endwhile
						ShowReviews_C_saveReviewsList(node)
					endif
		        endfunc
		        
				func showReviews(JSONObject locationJo)
				    JSONArray reviewDetailArray = JSONObject.get(locationJo,"reviewDetail")
				    if NULL != reviewDetailArray
				       int size = JSONArray.length(reviewDetailArray)
                       JSONObject reviewJo
                       String reviewText = ""
                       String reviewerName = ""
                       String reviewTime = ""
                       int rating
                       String nameAndTime = ""
                       String firstLine_itemId = ""
                       String secondLinde_itemId = ""
                       String itemId = ""
                       int i = 0
                       while i < size
                          reviewJo = JSONArray.get(reviewDetailArray,i)
                          reviewText = JSONObject.get(reviewJo,"reviewText")
                          reviewerName = JSONObject.get(reviewJo,"reviewerName")
                          reviewTime = JSONObject.get(reviewJo,"updateDate")
                          rating = JSONObject.get(reviewJo,"rating")
                          if "" != reviewerName
                             nameAndTime =  reviewerName + ".-" + reviewTime
                          else
                             nameAndTime =  reviewTime
                          endif
                          firstLine_itemId = "reviews_info" + i
                          Page.setComponentAttribute(firstLine_itemId,"text",reviewText)
                          secondLinde_itemId = "name_time" + i
                          Page.setComponentAttribute(secondLinde_itemId,"text",nameAndTime)
                          
                          itemId = "item" + i
						  Page.setComponentAttribute(itemId,"visible","1")
						  showStarsForReviews(rating,i)
                          i = i + 1
                       endwhile	
                       
                       if 0 == i 
                          Page.setComponentAttribute("noReviews","visible","1")
                       else
                          Page.setComponentAttribute("noReviews","visible","0")
                       endif
                       
					   while i < 10
						 itemId = "item" + i
						 Page.setComponentAttribute(itemId,"visible","0")
						 i = i + 1
					   endwhile
					   ShowReviews_C_saveReviewsList(reviewDetailArray)
				    else
				       Page.setComponentAttribute("noReviews","visible","1")
				    endif
				endfunc
				
				func reviewsClick()
                   TxNode indexClickNode = ParameterSet.getParam("indexClicked")
				   int indexClicked = TxNode.valueAt(indexClickNode, 0)
				   ShowReviews_C_saveIndex(indexClicked)
				endfunc
			
			
		]]>
	</tml:script>

<tml:script language="fscript" version="1"
		feature="<%=FeatureConstant.RESTAURANT%>">
		<![CDATA[
				func viewReservationScript()
					TxNode node
					node = ParameterSet.getParam("partnerPoiId")
					int partnerPoiId = -1
					if NULL != node
						partnerPoiId = TxNode.valueAt(node, 0)
					endif
					
					node = ParameterSet.getParam("anchorLat")
					int anchorLat = -1
					if NULL != node
						anchorLat = TxNode.valueAt(node, 0)
					endif
					
					node = ParameterSet.getParam("anchorLon")
					int anchorLon = -1
					if NULL != node
						anchorLon = TxNode.valueAt(node, 0)
					endif
					
					node = ParameterSet.getParam("distanceUnit")
					int distanceUnit = -1
					if NULL != node
						distanceUnit = TxNode.valueAt(node, 0)
					endif
					
					
					JSONObject restaurantJo
					JSONObject.put(restaurantJo, "lat", anchorLat)
					JSONObject.put(restaurantJo, "lon", anchorLon)
					JSONObject.put(restaurantJo, "lafObjectId", partnerPoiId)
					JSONObject.put(restaurantJo, "distanceUnit", distanceUnit)
					String joStr = JSONObject.toString(restaurantJo)
					
					TxNode restaurantNode
	       		    TxNode.addMsg(restaurantNode, joStr)
	       		    println("here5:node make reservation page:" + restaurantNode)
	       		    
	    			TxRequest req
					String url="<%=restaurantHost + "/GetRestaurantDetail.do"%>"
					String scriptName="getRestaurantDetailCallback"
					TxRequest.open(req,url)
					TxRequest.setRequestData(req,restaurantNode)
					TxRequest.onStateChange(req,scriptName)
					TxRequest.setProgressTitle(req,"<%=msg.get("restaurant.getting.detail")%>")
					TxRequest.send(req)
				endfunc
				
				func getRestaurantDetailCallback(TxNode node, int status)
					if status == 0		
						System.showErrorMsg("<%=msg.get("common.internal_error")%>")
						return FAIL
					elseif status == 1
						int returnCode = TxNode.valueAt(node,0)
						String responseStr=TxNode.msgAt(node,0)
						println("response : " + responseStr)
						if returnCode == -1
							System.showErrorMsg(responseStr)
							return FAIL
						elseif returnCode == 0
						    System.showErrorMsg(responseStr)
						    return FAIL
						else
							JSONObject restaurantJo = JSONObject.fromString(responseStr)
							String saveKey="RESTAURANT_DETAIL_POIID"
			    			Cache.saveToTempCache(saveKey,restaurantJo)
			    			System.doAction("viewReservationPage")
						endif
					endif	
					
				endfunc
		]]>
	</tml:script>

	<tml:menuItem name="DriveTo" text="<%=msg.get("poi.drive.to")%>"
		trigger="KEY_MENU|TRACKBALL_CLICK" onClick="DriveTo">
	</tml:menuItem>
	<tml:menuItem name="getDirections"
		text="<%=msg.get("poi.get.directions")%>" trigger="KEY_MENU|TRACKBALL_CLICK"
		onClick="staticRoute">
	</tml:menuItem>


	<tml:block feature="<%=FeatureConstant.MOVIE%>">
		<tml:menuItem name="viewMovies"
			text="<%=msg.get("poi.details.movie")%>"
			pageURL="{movie.http}/ShowMovies.do" trigger="KEY_MENU|TRACKBALL_CLICK">
			<tml:bean name="theaterId" valueType="String" value="" />
		</tml:menuItem>
	</tml:block>

	<tml:actionItem name="makePhoneCallAction"
		action="<%=Constant.LOCALSERVICE_MAKEPHONECALL%>">
		<tml:input name="phonenumber" />
	</tml:actionItem>
	<tml:menuItem name="phoneCall" actionRef="makePhoneCallAction" />
	<tml:menuItem name="call" text="<%=msg.get("poi.call.phone")%>"
		trigger="KEY_MENU|TRACKBALL_CLICK" onClick="call">
	</tml:menuItem>

	<tml:actionItem name="showMapAction" action="mapIt">
		<tml:input name="pSrc" />
	</tml:actionItem>

	<tml:menuItem name="doShowmap" actionRef="showMapAction">
	</tml:menuItem>
	<tml:menuItem name="showMap" text="<%=msg.get("poi.map.it")%>"
		trigger="KEY_MENU|TRACKBALL_CLICK" onClick="showMap">
	</tml:menuItem>

	<tml:block feature="<%=FeatureConstant.SHARE_ADDRESS%>">
		<tml:menuItem name="shareAddress" trigger="KEY_MENU|TRACKBALL_CLICK"
			text="<%=msg.get("poi.share.address")%>" onClick="showShareAddress">
		</tml:menuItem>
	</tml:block>

	<tml:block feature="<%=FeatureConstant.UGC_VIEW%>">
		<tml:menuItem name="viewReviews" trigger="KEY_MENU|TRACKBALL_CLICK"
			text="<%=msg.get("poi.view.history")%>" onClick="viewReviews">
		</tml:menuItem>
	</tml:block>

	<tml:menuItem name="favorites"
		text="<%=msg.get("poi.save.to.favorites")%>" trigger="KEY_MENU|TRACKBALL_CLICK"
		onClick="createFavorites">
	</tml:menuItem>

	<tml:menuItem name="rateThis" text="<%=msg.get("poi.rate.this")%>"
		trigger="KEY_MENU|TRACKBALL_CLICK" pageURL="<%=getPage + "RatePoi"%>"
		onClick="rateThis">
	</tml:menuItem>

	<tml:page id="showDetailPage" url="<%=getPage + "ShowDetail"%>" groupId="<%=GROUP_ID_POI%>"
		type="<%=pageType%>" showLeftArrow="true" showRightArrow="true"
		helpMsg="$//$poidetails">

		<tml:menuRef name="DriveTo" />
		<tml:menuRef name="getDirections" />
		<tml:menuRef name="viewMovies" />
		<tml:menuRef name="call" />
		<tml:menuRef name="showMap" />
		<tml:menuRef name="shareAddress" />
		<tml:menuRef name="favorites" />
		<tml:menuSeperator />
		<tml:menuRef name="priceRange" />
		<tml:menuRef name="rateThis" />

		<tml:title id="DetailTitle" align="center|middle" fontColor="white"
			fontWeight="bold|system_large">
			<%=msg.get("poi.list.results")%>
		</tml:title>

		<tml:tabContainer id="showDetailContainer" style="vertical"
			defaultFocus="0">
			<tml:tab id="Details" label="<%=" "+msg.get("poi.details") %>" fontSize="18">
				<tml:panel id="sponsorInformationPanel" layout="vertical">
					<tml:panel id="absolutePanel">
						<tml:panel id="horizontalPanel" layout="horizontal">
							<tml:image id="sponsorImage" align="right|bottom" url="<%=sponsorImage%>"  />
							<tml:panel id="poiInfoPanel" >
								<tml:label id="poiName" textWrap="wrap" fontSize="22"
									align="left|top">
								</tml:label>
								<tml:label id="address" align="left|bottom" textWrap="wrap"
									fontSize="20" heightAutoScale="true">
								</tml:label>
								<tml:label id="phone" fontSize="20" align="left|bottom">
								</tml:label>
									<tml:image id="starImage1_0" url="<%=imageUnSolid%>" />
									<tml:image id="starImage2_0" url="<%=imageUnSolid%>" />
									<tml:image id="starImage3_0" url="<%=imageUnSolid%>" />
									<tml:image id="starImage4_0" url="<%=imageUnSolid%>" />
									<tml:image id="starImage5_0" url="<%=imageUnSolid%>" />
									<tml:label id="ratingNumber" fontSize="18" align="left|top">
									</tml:label>
								</tml:panel>
						
						</tml:panel>
						<tml:image id="citySearchDetail" ></tml:image>
					</tml:panel>
					
					<tml:block feature="<%=FeatureConstant.RESTAURANT%>">
					<tml:menuItem name="viewReservationPage" pageURL="<%=restaurantUrl%>">
					</tml:menuItem>

					<tml:menuItem name="callViewReservationScript" onClick="viewReservationScript">
						<tml:bean name="partnerPoiId" valueType="String" value="" />
					</tml:menuItem>
					<tml:panel id = "reservationLink">
						<tml:image id="openTableLogo" url="<%=imageUrl + "opentablesmall.png" %>" />
						<tml:urlLabel id="reservation" fontWeight="system_median" align="left|bottom" fontColor="#005AFF">
						<tml:menuRef name="callViewReservationScript" /><%=msg.get("restaurant.make.reservation.link")%>
					</tml:urlLabel>
					</tml:panel>
					</tml:block>
					
					<tml:multiline id="sponsorInformation" align="left|top"
						fontSize="18" isFocusable="true" heightAutoScale="true">
					</tml:multiline>

				</tml:panel>
			</tml:tab>

			<tml:block feature="<%=FeatureConstant.UGC_VIEW%>">
				<tml:tab id="Reviews" label="<%=" "+msg.get("poi.reviews") %>" fontSize="18">

					<tml:block feature="<%=FeatureConstant.UGC_EDIT%>">
						<tml:button id="reviewButton" text="<%=msg.get("poi.write.review")%>"
							fontWeight="system"
							imageClick="<%=imageUrl + "button_small_on.png"%>"
							imageUnclick="<%=imageUrl
	                                                + "button_small_off.png"%>">
							<tml:menuRef name="rateThis" />
						</tml:button>
					</tml:block>

					<tml:label id="noReviews" align="center|bottom" visible="false"
						textWrap="ellipsis" fontWeight="system" heightAutoScale="true">
						<%=msg.get("poi.no.reviews")%>
					</tml:label>

					<tml:listBox id="reviewsList">
						<%
							for (int i = 0; i < 10; i++) {
						%>
						<tml:menuItem name="<%="item_" + i + "_clicked"%>"
							text="<%=msg.get("poi.show.reviews")%>" onClick="reviewsClick"
							pageURL="<%=getPage + "ShowReviews"%>" trigger="TRACKBALL_CLICK">
							<tml:bean name="indexClicked" valueType="int" value="<%=i + ""%>"></tml:bean>
						</tml:menuItem>

						<tml:compositeListItem id="<%="item" + i%>" getFocus="false"
							visible="true" bgColor="#FFFFFF" transparent="false"
							focusBgImage="<%=imageUrl
                                                        + "list_bg_highlight_45px.png"%>"
							blurBgImage="<%=imageUrl
                                                + "list_bg_45px.png"%>"
							isFocusable="true">
							<tml:label id="<%="reviews_info" + i%>" focusFontColor="white"
								fontSize="20" fontWeight="system_median" textWrap="ellipsis"
								align="left">
							</tml:label>

							<tml:image
								id="<%="reviews_starImage1_"
                                                + i%>"
								url="<%=reviews_imageUnSolid%>" />
							<tml:image
								id="<%="reviews_starImage2_"
                                                + i%>"
								url="<%=reviews_imageUnSolid%>" />
							<tml:image
								id="<%="reviews_starImage3_"
                                                + i%>"
								url="<%=reviews_imageUnSolid%>" />
							<tml:image
								id="<%="reviews_starImage4_"
                                                + i%>"
								url="<%=reviews_imageUnSolid%>" />
							<tml:image
								id="<%="reviews_starImage5_"
                                                + i%>"
								url="<%=reviews_imageUnSolid%>" />

							<tml:label id="<%="name_time" + i%>" focusFontColor="white"
								fontSize="18" fontWeight="system_median" textWrap="ellipsis"
								align="left">
							</tml:label>

							<tml:menuRef name="<%="item_" + i + "_clicked"%>" />

						</tml:compositeListItem>
						<%
							}
						%>
					</tml:listBox>
				</tml:tab>
			</tml:block>
			<tml:tab label="<%=" "+msg.get("poi.coupons") %>" id="<%=couponTabID%>"
				icon="<%=couponsImage%>" fontSize="18">
				<tml:image id="citySearchCoupon" 
					align="center"></tml:image>
				<tml:panel id="couponPanel" layout="vertical">
					<%
						for (int i = 0; i < 5; ++i) {
					%>
					<tml:multiline id="<%="couponDesc"+i %>" fontSize="18"
						isFocusable="true" align="left|top" />
					<tml:image id="<%="couponImg"+i %>" align="center|top"></tml:image>

					<tml:label id="<%="expireDate"+i %>" fontSize="16"
						align="center|top" />

					<%
						}
					%>
				</tml:panel>
			</tml:tab>

			<tml:tab id="<%=menuTabID%>" label="<%=" "+msg.get("poi.hours.menu") %>"
				icon="<%=menuImage%>" fontSize="18">
				<% if(TnUtil.isRogersCarrier(carrier)){ %>
					<tml:panel id="menuPanel" layout="vertical">
					<tml:panel id="citySearchMenuPanel">
						<tml:image id="citySearchMenu"></tml:image>
					</tml:panel>
					<tml:multiline id="menu" fontWeight="sytem_large" align="left"
						heightAutoScale="true" isFocusable="true" />
				</tml:panel>									
				<%} else { %>
					<tml:panel id="menuPanel">
						    <tml:image id="citySearchMenu" ></tml:image>
						<tml:multiline id="menu" fontWeight="sytem_large" align="left"
							heightAutoScale="true" isFocusable="true" />
					</tml:panel>
				<%} %>
			</tml:tab>
		</tml:tabContainer>
	</tml:page>
	<cserver:outputLayout />
</tml:TML>
