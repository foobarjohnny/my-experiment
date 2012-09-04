<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.cserver.util.FeatureConstant"%>
<%@page import="com.telenav.cserver.util.TnConstants"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@page import="com.telenav.cserver.util.TnUtil"%>
<%@ include file="../Header.jsp"%>
<%@page
	import="com.telenav.cserver.pref.struts.util.PreferenceConstants"%>
	
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>

<%@page import="com.telenav.cserver.poi.model.PoiListModel"%>
<%@page import="com.telenav.cserver.stat.*"%>
<%@page import="com.telenav.cserver.poi.struts.util.PoiUtil"%>

<%
	String ShowDetailURL = getPageCallBack + "ShowDetail";
	
	String menuTabID = "menuTab";
	String couponTabID = "coupons";

	 
	boolean isFeedbackEnabled = featureMgr.isEnabled(FeatureConstant.FEEDBACK_POI);
	
	String restaurantHost = "{restaurant.http}/touch";
	String restaurantUrl = restaurantHost +"/goToJsp.do?pageRegion=" + region + "&amp;jsp=DetailAndReservationPage";
	 
	final int popularMenuIndex = 3; //3
	final int priceMenuIndex = popularMenuIndex + 1; //4
	final int relevanceMenuIndex = popularMenuIndex + 2;//5
	final int ratingMenuIndex = popularMenuIndex + 3;//6
	final int distanceMenuIndex = popularMenuIndex + 4;//7
	
	final int viewMoviesMenuIndex = 0;//0
	final int driveToMenuIndex = viewMoviesMenuIndex + 1;//1
	final int getDirectionsMenuIndex = viewMoviesMenuIndex + 2;//2
	final int callMenuIndex = viewMoviesMenuIndex + 3;//3
	final int mapMenuIndex = viewMoviesMenuIndex + 4;//4
	final int shareMenuIndex = viewMoviesMenuIndex + 5;//5
	final int savMenuIndex = viewMoviesMenuIndex + 6;//6
	
	final int rateMenuIndex = viewMoviesMenuIndex + 8;//8
	final int postLocationMenuIndex = viewMoviesMenuIndex + 9;//9
	final int feedBackMenuIndex = viewMoviesMenuIndex + 10;//10
	
%>

<%@ include file="controller/PoiListController.jsp"%>
<%@ include file="controller/ShowReviewsController.jsp"%>
<%@ include file="controller/EditPoiController.jsp"%>
<%@ include file="../ac/controller/AddressCaptureController.jsp"%>
<%@ include file="controller/RatePoiController.jsp"%>
<%@ include file="../ac/controller/ShareAddressController.jsp"%>
<%@ include
	file="/touch64/jsp/ac/controller/CreateFavoritesController.jsp"%>

<tml:script language="fscript" version="1">
	<%@ include file="../PoiUtil.jsp"%>
		<![CDATA[
		<%@ include file="../Stop.jsp"%>
		<%@ include file="../CommonScript.jsp"%>
		<%@ include file="ShowRateImageScript.jsp"%>
		        func preLoad()
		            Page.setControlProperty("driveToB","focused","true")
		            int sponsor = ShowDetail_M_getIsSponsorForDetail()
					showDetail(sponsor)
					
					int tabIndex = ShowDetail_M_getShowTabIndex()
		            if 1 == tabIndex
		               Page.setComponentAttribute("showDetailContainer","defaultFocus","1")
		               ShowDetail_M_deleteShowTabIndex()
		            else
		               Page.setComponentAttribute("showDetailContainer","defaultFocus","0")
		            endif

					<% if(isFeedbackEnabled) { %>
						Page.removeGenericMenu(4)
					<% } %>

				endfunc
				
				func onShow()
				    System.setKeyEventListener("-p-n-right-left-","keypress")
				endfunc
				
				func keypress(string s)
		        	if s == "p"
		        	   showPrevious()
		        	elsif s == "n"
		        	   showNext()
		        	endif
				endfunc
				
				func onTabsChange(String controlName)
				    saveMISLog()
				endfunc
				
				func onLoad()
			<%if (featureMgr.isEnabled(FeatureConstant.UGC_VIEW)) {%>
				    #getReviewsForPoi()
			<%}%>
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
					Page.setComponentAttribute("title","text",detailTitle)
					
					JSONObject locationJo = JSONArray.get(poiJsonArray,index)
					
					int rating = JSONObject.get(locationJo,"rating")
					String nameTitle
					String address
					String phone=""
					String poiName=""
					
					#Set rating star image
					String categoryId = <%=PoiListModel.getCategoryId()%>
					int isGasByPrice = PoiList_C_isGasByPrice(categoryId)
					int isRatingEnable = JSONObject.get(locationJo,"isRatingEnable")
                    if 0 == sponsor && 0 == isGasByPrice && 1 == isRatingEnable
                       Page.setComponentAttribute("rating","visible","1")
	  				   Page.setComponentAttribute("ratingPanel","visible","1")
					   showRateStarImage(rating,0)
					   
					   if JSONObject.has(locationJo,"ratingNumber")
						   int ratingNumber = JSONObject.get(locationJo,"ratingNumber")
						   if 0 != ratingNumber
						       String ratingNumberStr = "(" + ratingNumber + ")"
						       if ratingNumber == 1
							        String onerating = System.parseI18n("<%= msg.get("poi.detail.onerating")%>")
									if onerating == "poi.detail.onerating"
									   onerating = "<%= msg.get("poi.detail.rating")%>"
                                    endif
						       		ratingNumberStr = ratingNumber + " " + onerating
						       else
						       		ratingNumberStr = ratingNumber + " " +"<%= msg.get("poi.detail.rating")%>"
						       endif
							   Page.setComponentAttribute("ratingNumber","text",ratingNumberStr)
						   else
						       Page.setComponentAttribute("ratingNumber","text","")
						   endif
					   else
						   Page.setComponentAttribute("ratingNumber","text","")
					   endif
					   Page.setComponentAttribute("ratingNumber","visible","1")
					   Page.setComponentAttribute("Reviews","visible","1")
                       MenuItem.setItemValid("page",<%=rateMenuIndex%>,TRUE)
                    else
	  				   Page.setComponentAttribute("rating","visible","0")
	  				   Page.setComponentAttribute("ratingPanel","visible","0")
		 			   Page.setComponentAttribute("Reviews","visible","0")
                       MenuItem.setItemValid("page",<%=rateMenuIndex%>,FALSE)
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
					<%if(PoiUtil.showPostLocationAndOpenTable(handlerGloble)) {%>
					if NULL != phone
						if "" != phone
							phone = UTIL_formatPhoneNumber(phone)
							hidePhoneMenu = 1
					        Page.setComponentAttribute("phone","text",phone)
					        Page.setComponentAttribute("phone","visible","1")
					        Page.setComponentAttribute("callIcon","visible","1")
					    else
					        Page.setComponentAttribute("phone","visible","0")
					        Page.setComponentAttribute("callIcon","visible","0")
						endif
					else
					    Page.setComponentAttribute("phone","visible","0")
					    Page.setComponentAttribute("callIcon","visible","0")
					endif
					<%}else{%>
					if NULL != phone
						if "" != phone
							phone = UTIL_formatPhoneNumber(phone)
							hidePhoneMenu = 1
					        Page.setComponentAttribute("phone","text",phone)
					        Page.setComponentAttribute("phonePanel","visible","1")
					    else
					        Page.setComponentAttribute("phonePanel","visible","0")
						endif
					else
					    Page.setComponentAttribute("phonePanel","visible","0")
					endif		
					<%} %>
					
					#if no lat&lon,hide menu "Drive To", if no phone, hide menu "Call ****"
					hideMenuWhenNoLatLon(latAndLon,hidePhoneMenu)
					
					#Set poi name
					poiName = JSONObject.get(locationJo,"name")
					String distance = JSONObject.get(locationJo,"distance")
					poiName = "<bold>" + poiName + "</bold>"
					Page.setComponentAttribute("poiName","text",poiName)
					if NULL == distance || "" == distance || "0" == distance
					   distance =""
					else
					   Page.setComponentAttribute("distance","text",distance)
					endif
					
					
					#set ad line or gas price if there is any
					if (JSONObject.has(locationJo,"ad") || JSONObject.has(locationJo,"gasPirce")) 
						String mContent = ""
						if (JSONObject.has(locationJo,"gasPirce"))
							mContent = gasPriceDetails(JSONObject.get(locationJo,"gasPirce"))
						endif
						
						JSONObject ad = JSONObject.get(locationJo,"ad")
						if(JSONObject.has(ad,"adSource"))
							String adSourceIcon ="<%=imageCommonUrl%>"+JSONObject.get(ad,"adSource")+"_logo.png"
							Page.setControlProperty("citySearchDetail","image",adSourceIcon)
							Page.setControlProperty("citySearchCoupon","image",adSourceIcon)
							Page.setControlProperty("citySearchMenu","image",adSourceIcon)
							
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
							<% if( TnUtil.isUSCCRIM62(handlerGloble) ) { %>
							mContent = mContent + "\n"
							<% } %>
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
					MenuItem.setItemText("page", <%=callMenuIndex%>, callString)
					
					#check if it is Movie Theaters category
					String theaterId = "" + JSONObject.get(locationJo, "poiId")
					String poiCategoryId = JSONObject.get(locationJo, "categoryId")
					TxNode tID
					if "<%=Constant.MOVIE_THEATER_CATEGORY_ID%>" == poiCategoryId
						TxNode.addMsg(tID, theaterId)
						MenuItem.setBean("viewMovies", "theaterId", tID)
						MenuItem.setBean("viewMoviesMenu", "theaterId", tID)
						# please do not change "page", it is reserved key word to 
						# access page associated menu
						<%if (featureMgr.isEnabled(FeatureConstant.MOVIE)) {%>
							MenuItem.setItemValid("page", <%=viewMoviesMenuIndex%>, TRUE)
						<%}else{%>
							MenuItem.setItemValid("page", <%=viewMoviesMenuIndex%>, FALSE)
						<%}%>
						
						# current date as default value
						String dateStr = Time.format("yyyy-MM-dd", -1)
						TxNode nodeDate
						TxNode.addMsg(nodeDate, dateStr)
						MenuItem.setBean("viewMovies", "dateIndex", nodeDate)
						MenuItem.setBean("viewMoviesMenu", "dateIndex", nodeDate)
						# Get lat&lon of anchor address 
						JSONObject addressJO = SearchPoi_M_getLocation()
						TxNode lonNode
						TxNode latNode
						int lon = 0
						int lat = 0
						if NULL != addressJO
							lon = JSONObject.getInt(addressJO,"lon")
							lat = JSONObject.getInt(addressJO,"lat")
							
							#save movie address used in SelectDate.jsp
							Cache.saveToTempCache("<%=Constant.StorageKeyForJSON.JSON_OBJECT_MOVIE_ADDESS%>",addressJO) 
						endif
						TxNode.addValue(lonNode,lon)
						TxNode.addValue(latNode,lat)
						MenuItem.setBean("viewMovies", "anchorLon", lonNode)
						MenuItem.setBean("viewMovies", "anchorLat", latNode)
						MenuItem.setBean("viewMoviesMenu", "anchorLon", lonNode)
						MenuItem.setBean("viewMoviesMenu", "anchorLat", latNode)
						println("111$$$$$$$"+lonNode)
						println("$$$$$$$"+latNode)
						# Get distance unit 
						TxNode distanceUnitNode = Preference.getPreferenceValue(1)
						if NULL != distanceUnitNode
							MenuItem.setBean("viewMovies", "distUnit", distanceUnitNode)
							MenuItem.setBean("viewMoviesMenu", "distUnit", distanceUnitNode)
						endif
						println("$$$$$$$"+distanceUnitNode)
					else
						MenuItem.setItemValid("page", <%=viewMoviesMenuIndex%>, FALSE)
					endif
					int displayRestaurant = displayRestaurant(locationJo)
					int displayPostLocation = ServerDriven_CanPostLocation() && latAndLon
					MenuItem.setItemValid("page", <%=postLocationMenuIndex%>, displayPostLocation)
					if displayRestaurant || displayPostLocation 
						Page.setComponentAttribute("PostAndReservePanel","visible","1")
					else
						Page.setComponentAttribute("PostAndReservePanel","visible","0")
					endif
					
					Page.setComponentAttribute("reservation","visible",displayRestaurant + "")
					Page.setComponentAttribute("postLocation","visible",displayPostLocation + "")
			# if feedback is enabled, then display it
			<%if (isFeedbackEnabled) {%>
					MenuItem.setItemValid("page", <%=feedBackMenuIndex%>, TRUE)
			<%}else{%>
					MenuItem.setItemValid("page", <%=feedBackMenuIndex%>, FALSE)
			<%}%>
					MenuItem.commitSetItemValid("page")
					
					#check if come from PoiList
		            if ShowDetail_FromPoiList()
		            	ShowDetail_M_saveFromFlag("")
						String logText = poiName + ";" + address + ";" + Time.get()
						logPoi(logText)
					   if StatLogger.isStatEnabled(<%=EventTypes.POI_DETAILS%>)
							logPOI_JSON(<%=EventTypes.POI_DETAILS%>)				    
					   endif
					   
					   if StatLogger.isStatEnabled(<%=EventTypes.POI_VIEW_MERCHANT%>)
							String showAdsOn = getShowMessageOn("1")
							if JSONObject.has(ad,"adLine") && "1" == showAdsOn
								logPOI_JSON(<%=EventTypes.POI_VIEW_MERCHANT%>)
							endif				    
					   endif
				   endif
				endfunc
				
				
				func displayRestaurant(JSONObject locationJo)
					if ServerDriven_CanRestaurant()
						#set restaurant info 
						println(" locationJo " + locationJo)
						if JSONObject.has(locationJo,"restaurant") && 0==Account.isTnMaps()
							JSONObject restaurantJo = JSONObject.get(locationJo,"restaurant")
							if(JSONObject.has(restaurantJo,"isReservable"))
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
								return 1
							else
								return 0
							endif						
						else
							return 0
						endif
					else
						return 0
					endif
				endfunc
				
				func hideMenuWhenNoLatLon(int latAndLon,int hidePhoneMenu)
					if Account.isTnMaps()==1
						MenuItem.setItemValid("page",<%=driveToMenuIndex%>,FALSE)
					    MenuItem.setItemValid("page",<%=getDirectionsMenuIndex%>, latAndLon)
					else
						MenuItem.setItemValid("page",<%=getDirectionsMenuIndex%>,FALSE)
					    MenuItem.setItemValid("page",<%=driveToMenuIndex%>, latAndLon)
					endif
					
					MenuItem.setItemValid("page", <%=mapMenuIndex%>, latAndLon)
					MenuItem.setItemValid("page", <%=shareMenuIndex%>, latAndLon)
					MenuItem.setItemValid("page", <%=savMenuIndex%>, latAndLon)
				    MenuItem.setItemValid("page", <%=callMenuIndex%>, hidePhoneMenu)
				endfunc
				
				func showAdsLogo(String showLogo)
					Page.setComponentAttribute("citySearchDetail","visible",showLogo)
					Page.setComponentAttribute("citySearchDetailPanel","visible",showLogo)
				endfunc
				
				func showAdsComponent(String showAds)
					showAds = getShowMessageOn(showAds)
					
					Page.setComponentAttribute("sponsorInformation","visible",showAds)
					Page.setComponentAttribute("dashLinePanel","visible",showAds)
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
					Page.setComponentAttribute("poiInfoPanel","resetScroll", "vertical")
					int totalIndex = ShowDetail_M_getTotalIndexForDetail()
				    if totalIndex == 0
				       return FAIL
				    endif
				    		
				    int sponsorSize = ShowDetail_M_getSponsorSizeForDetail()
		            int index = ShowDetail_M_getIndex()
				    int isSponsorPoi = ShowDetail_M_getIsSponsorForDetail()
				    int sponsorListNumber = ServerDriven_CanSponsor()
				    index = index - 1
				    if 0 != sponsorSize
				        JSONArray poiJsonArray
				        if 0 == isSponsorPoi
					       if 0 == (index + 1)%<%=Constant.PAGE_SIZE%>
				              int sponsorIndex = ((index + 1)/<%=Constant.PAGE_SIZE%>)*sponsorListNumber
					          if sponsorSize > sponsorIndex
					             index = sponsorIndex
					             isSponsorPoi = 1
						         poiJsonArray = ShowDetail_M_getSponsorPoiList()
						         ShowDetail_M_saveAddressList(poiJsonArray)
					          endif
					       endif
					    else
				           if 0 == index%sponsorListNumber
				              index = (index/sponsorListNumber + 1) * <%=Constant.PAGE_SIZE%> - 1
				              int totalSize = ShowDetail_M_getTotalSize()
				              int normalSize = totalSize - sponsorSize
				              if index >= normalSize
				                 index = normalSize - 1
				              endif
				              isSponsorPoi = 0
				           	  poiJsonArray = ShowDetail_M_getPoiList()
				           else
				              isSponsorPoi = 1
				              poiJsonArray = ShowDetail_M_getSponsorPoiList()
				           endif
				           ShowDetail_M_saveAddressList(poiJsonArray)  
					    endif
				    endif
		            
		            totalIndex = totalIndex - 1
				    ShowDetail_M_saveTotalIndexForDetail(totalIndex)
					
					TxNode newIndexNode
					TxNode.addValue(newIndexNode, index)
					ShowDetail_M_saveNewIndex(newIndexNode)
					ShowDetail_M_saveIsSponsorForDetail(isSponsorPoi)
					
					if 1 == isSponsorPoi
						JSONObject sponsorJo = JSONArray.get(poiJsonArray,index)
						if UTIL_checkEmptyJSON(sponsorJo)
						   showPrevious()
						   return FAIL
						endif
					endif
					
					showDetail(isSponsorPoi)
					
			<%if (featureMgr.isEnabled(FeatureConstant.UGC_VIEW)) {%>
				    #getReviewsForPoi()
			<%}%>
			        Page.setComponentAttribute("showDetailContainer","defaultFocus","0")
				endfunc
				
				func showNext()
					Page.setComponentAttribute("poiInfoPanel","resetScroll", "vertical")
					int totalSize = ShowDetail_M_getTotalSize()
					int totalIndex = ShowDetail_M_getTotalIndexForDetail()
				    if totalIndex >= totalSize - 1
				       return FAIL
				    endif
				
				    int sponsorSize = ShowDetail_M_getSponsorSizeForDetail()
		            int index = ShowDetail_M_getIndex()
		            int sponsorListNumber = ServerDriven_CanSponsor()
		            index = index + 1
		            int isSponsorPoi = ShowDetail_M_getIsSponsorForDetail()
		            if 0 != sponsorSize
		                JSONArray poiJsonArray
				        if 0 == isSponsorPoi
					       int normalSize = totalSize - sponsorSize
					       if (0 == index%<%=Constant.PAGE_SIZE%>) || (normalSize == index)
					          int sponsorIndex = index/<%=Constant.PAGE_SIZE%>
					          if 2 == sponsorListNumber
					             if 0 != index%<%=Constant.PAGE_SIZE%>
						            sponsorIndex = sponsorIndex + 1
						         endif
					             sponsorIndex = sponsorIndex*2 - 1
					          endif
					          if sponsorSize > sponsorIndex
					             index = sponsorIndex
					             isSponsorPoi = 1
						         poiJsonArray = ShowDetail_M_getSponsorPoiList()
						         ShowDetail_M_saveAddressList(poiJsonArray)  
					          endif
					       endif
					    else
					       if 1 == sponsorListNumber || 1 == index%sponsorListNumber
					           index = ((index - 1)/sponsorListNumber) * <%=Constant.PAGE_SIZE%>
					           isSponsorPoi = 0
					           poiJsonArray = ShowDetail_M_getPoiList()
					           ShowDetail_M_saveAddressList(poiJsonArray) 
					       else
			              	   poiJsonArray = ShowDetail_M_getSponsorPoiList()
					       endif
					    endif
		            endif
				    
		            totalIndex = totalIndex + 1
				    ShowDetail_M_saveTotalIndexForDetail(totalIndex)
		            
					TxNode newIndexNode
					TxNode.addValue(newIndexNode, index)
					ShowDetail_M_saveNewIndex(newIndexNode)
					ShowDetail_M_saveIsSponsorForDetail(isSponsorPoi)
					
					if 1 == isSponsorPoi
						JSONObject sponsorJo = JSONArray.get(poiJsonArray,index)
						if UTIL_checkEmptyJSON(sponsorJo)
						   if totalIndex == (totalSize - 1)
						      showPrevious()
						   else
						      showNext()
						   endif
						   return FAIL
						endif
						
					endif
					
					showDetail(isSponsorPoi)
					
			<%if (featureMgr.isEnabled(FeatureConstant.UGC_VIEW)) {%>
				    #getReviewsForPoi()
			<%}%>
			        Page.setComponentAttribute("showDetailContainer","defaultFocus","0")
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
							addressStr = addressStr + "\n"  
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
				   int sponsorListNumber = ServerDriven_CanSponsor()
				   <%if(!TnUtil.isATTRIM63(handlerGloble) && !TnUtil.isSprintRim62(handlerGloble) &&!TnUtil.isUSCCRIM62(handlerGloble)&&!TnUtil.isVNRIM62(handlerGloble)&& !TnUtil.isTMORIM62(handlerGloble)){ %>
					   if 2 == sponsorListNumber
			                sponsorArray = sponsorArrayFilter(sponsorArray, index)
			           endif
		           <%}%>

				   #JSONObject poiJO
   				   if(ShowDetail_M_getIsSponsorForDetail()==1)
				  	 index=-1
				  	 totalIndex = ShowDetail_M_getTotalIndexForDetail()
				  	 adsIndex=ShowDetail_M_getIndex()
				  	 #poiJO = JSONArray.get(sponsorArray,adsIndex)
				   else
				     #poiJO = JSONArray.get(poiJsonArray,index)
				   endif
				   #saveToRencentPlaces(poiJO)
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
				
				func sponsorArrayFilter(JSONArray sponsorArray, int index)
		            int length = JSONArray.length(sponsorArray)
		            int i = 0
		            JSONArray newSponsorArray
		            JSONObject sponsorJo
		            while i < length
		                if 1 == index%2 && i == index-1
		                   sponsorJo = JSONArray.get(sponsorArray,index)
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
				
				func saveToRencentPlaces(JSONObject poiJO)
				   TxNode node = convertToPoi(poiJO)
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
					   indexOnPage = index%9
					   JSONArray sponsorArray = ShowDetail_M_getSponsorPoiList()
					   if sponsorArray != NULL
					   	   int sponsorSize = JSONArray.length(sponsorArray)
					   	   int pageIndex = <%=PoiListModel.getPageIndex()%>
					   	   if pageIndex < sponsorSize
					   	   	   indexOnPage = indexOnPage + 1
					   	   endif
					   endif
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

				<%
					if (isFeedbackEnabled)
					{
				%>
				func getPoiJo()
				    JSONArray poiJsonArray = ShowDetail_M_getAddressList()
				    int index              = ShowDetail_M_getIndex()
				    JSONObject poiJo       = JSONArray.get(poiJsonArray,index)
					return poiJo
				endfunc
			
                func getPoiName()
					JSONObject poiJo = getPoiJo()
					String poiName   = JSONObject.get(poiJo, "name")
					return poiName
				endfunc
			
                # script to forward to POI Detail Feedback page. Prepare a TxNode with
                # parameters for keyword, location, and category and forward to feedback page
                func poiDetailFeedback()
					<%if(TnUtil.isEligibleForNewFeedBack(handlerGloble)){%>
					String poiName = getPoiName()
					println("poi name " + poiName)
                    String detailFeedbackUrl = "<%=host+"/GenericFeedbackRetrieval.do?feedBackTopic=POIFEEDBACKV1&pageName=POIDetailFeedback&subKey=%25place%20name%25&subValue="%>"
                    detailFeedbackUrl = detailFeedbackUrl + PoiUtil_encodeURL(poiName)
                    println("detail feedback url: " + detailFeedbackUrl) 
                    MenuItem.setAttribute("gotoPoiDetailFeedback", "url", detailFeedbackUrl)
                   	<%}%>
                    System.doAction("gotoPoiDetailFeedback")
                    return FAIL

                endfunc

				<%
					}
				%>


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

	<tml:script language="fscript" version="1">
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
	
	
