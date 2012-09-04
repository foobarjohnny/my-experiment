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
	String menuImage = imageCommonUrl + "menu.png";
	String couponsImage = imageCommonUrl + "coupon.png";

	String tmpImg = "";
	
	String menuTabID = "menuTab";
	String couponTabID = "coupons";
	
	String restaurantHost = "{restaurant.http}";
	String restaurantUrl = restaurantHost + "/touch/goToJsp.do?pageRegion=NA&amp;jsp=DetailAndReservationPage";

	String movieModuleName = ClientHelper.getModuleNameForMovie(handlerGloble);
	String showMovieUrl = "{movie.http}/" + movieModuleName + "/ShowMovies.do";
		
	boolean isFeedbackEnabled = featureMgr.isEnabled(FeatureConstant.FEEDBACK_POI);

	String device = handlerGloble.getClientInfo(DataHandler.KEY_DEVICEMODEL);
%>


<tml:TML outputMode="TxNode">
	<jsp:include page="/touch/jsp/poi/model/ShowDetailModel.jsp"/>
	<%@ include file="controller/PoiListController.jsp"%>
	<%@ include file="controller/ShowReviewsController.jsp"%>
	<jsp:include page="/touch/jsp/controller/DriveToController.jsp"/>
	<%@ include file="controller/EditPoiController.jsp"%>
	<%@ include file="../ac/controller/AddressCaptureController.jsp"%>
	<%@ include file="controller/RatePoiController.jsp"%>
	<%@ include file="../ac/controller/ShareAddressController.jsp"%>
	<%@ include
		file="/touch/jsp/ac/controller/CreateFavoritesController.jsp"%>
	<jsp:include page="../ac/controller/EditRouteController.jsp" />
	<jsp:include
		page="/touch/jsp/local_service/controller/MapWrapController.jsp" />
	<jsp:include page="StatLogger.jsp"/>

	<tml:block feature="<%=FeatureConstant.POST_LOCATION%>">
		<jsp:include page="PostLocationIncl.jsp"/>
	</tml:block>

	<tml:script language="fscript" version="1">
		<![CDATA[
		<%@ include file="../Stop.jsp"%>
		<%@ include file="../CommonScript.jsp"%>
		<%@ include file="ShowRateImageScript.jsp"%>
		<%@ include file="../GetServerDriven.jsp"%>
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
				
				func checkServerDriven(int latAndLon)
				    String hasLatLonStr = latAndLon + ""
				    int canShareAddress = ServerDriven_CanShareAddress()
				    if 1 == canShareAddress
				       Page.setComponentAttribute("shareB","visible",hasLatLonStr)
				       
				       Page.setComponentAttribute("shadowBg1","visible","0")
				       Page.setComponentAttribute("controlBg1","visible","0")
				       Page.setComponentAttribute("driveToB1","visible","0")
				       Page.setComponentAttribute("mapB1","visible","0")
				       
				       Page.setComponentAttribute("shadowBg","visible","1")
				       Page.setComponentAttribute("controlBg","visible","1")
				       Page.setComponentAttribute("driveToB","visible",hasLatLonStr)
				       Page.setComponentAttribute("mapB","visible",hasLatLonStr)
				    else
				       Page.setComponentAttribute("shareB","visible","0")
				       
				       Page.setComponentAttribute("shadowBg1","visible","1")
				       Page.setComponentAttribute("controlBg1","visible","1")
				       Page.setComponentAttribute("driveToB1","visible",hasLatLonStr)
				       Page.setComponentAttribute("mapB1","visible",hasLatLonStr)
				       
				       Page.setComponentAttribute("shadowBg","visible","0")
				       Page.setComponentAttribute("driveToB","visible","0")
				       Page.setComponentAttribute("controlBg","visible","0")
				       Page.setComponentAttribute("mapB","visible","0")
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
				
					JSONObject locationJo = JSONArray.get(poiJsonArray,index)
					
					int rating = JSONObject.get(locationJo,"rating")
					String nameTitle
					String address
					String phone=""
					String poiName=""
					
					#Set rating star image
					String categoryId = <%=PoiListModel.getCategoryId()%>
					int isGasByPrice = PoiList_C_isGasByPrice(categoryId)
					
                    if 0 == sponsor && 0==Account.isTnMaps() && 0 == isGasByPrice
                       Page.setComponentAttribute("rating","visible","1")
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
                       MenuItem.setItemValid("page",8,TRUE)
                    else
	  				   Page.setComponentAttribute("rating","visible","0")
		               Page.setComponentAttribute("Reviews","visible","0")
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
					        Page.setComponentAttribute("phone","visible","1")
					    else
					        Page.setComponentAttribute("phone","visible","0")
						endif
					else
					    Page.setComponentAttribute("phone","visible","0")
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
					String poiName_temp
					poiName = JSONObject.get(locationJo,"name")
					poiName_temp = poiName
					String distance = JSONObject.get(locationJo,"distance")
					if NULL == distance || "" == distance || "0" == distance
					   poiName = "<bold>" + poiName + "</bold>"
					else
					   poiName = "<bold>" + poiName + "</bold> <blue>(" + distance +")</blue>"
					endif
										
					Page.setComponentAttribute("poiName","text",poiName)
					
					# fix bug ATTBBTWOZERO-2435 
					<% if(TnUtil.isATTRIM(handlerGloble) && "9810".equalsIgnoreCase(device)){ %>
						Page.setComponentAttribute("poiName","text","<bold>" + poiName_temp + "</bold>")
						Page.setComponentAttribute("distance","text","<blue>("+distance+")</blue>")
					<%}%>
					
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
							Page.setComponentAttribute("sponsorInformation","text",mContent)
							showAdsComponent("1")
						else
							showAdsComponent("0")
						endif
					else
							showAdsComponent("0")
					endif
					
					#set coupon info 
					if JSONObject.has(locationJo,"coupon")
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
					if JSONObject.has(locationJo,"menu")
						JSONObject menuJo = JSONObject.get(locationJo,"menu")
						if JSONObject.has(menuJo,"menu")
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
						MenuItem.setBean("viewMoviesMenu", "theaterId", tID)
						# please do not change "page", it is reserved key word to 
						# access page associated menu
						MenuItem.setItemValid("page", 0, TRUE)
						MenuItem.setItemValid("driveToB", 1, TRUE)
						MenuItem.setItemValid("driveToB1", 1, TRUE)
						MenuItem.setItemValid("shareB", 1, TRUE)
						MenuItem.setItemValid("mapB", 1, TRUE)
						MenuItem.setItemValid("mapB1", 1, TRUE)
						MenuItem.setItemValid("saveB", 1, TRUE)
						MenuItem.setItemValid("rating", 1, TRUE)
						MenuItem.setItemValid("postLocation", 1, TRUE)
						MenuItem.setItemValid("phone", 1, TRUE)
						
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
						MenuItem.setItemValid("page", 0, FALSE)
						MenuItem.setItemValid("driveToB", 1, FALSE)
						MenuItem.setItemValid("driveToB1", 1, FALSE)
						MenuItem.setItemValid("shareB", 1, FALSE)
						MenuItem.setItemValid("mapB", 1, FALSE)
						MenuItem.setItemValid("mapB1", 1, FALSE)
						MenuItem.setItemValid("saveB", 1, FALSE)
						MenuItem.setItemValid("rating", 1, FALSE)
						MenuItem.setItemValid("postLocation", 1, FALSE)
						MenuItem.setItemValid("phone", 1, FALSE)
					endif
					MenuItem.setItemText("page", 3, callString)
			<%} else {%>
					MenuItem.setItemText("page", 2, callString)
			<%}%>

			# if feedback is enabled, then display it
			<% if(isFeedbackEnabled) { %>
					MenuItem.setItemValid("page", 1, TRUE)
			<% } %>

					MenuItem.commitSetItemValid("page")
					MenuItem.commitSetItemValid("driveToB")
					MenuItem.commitSetItemValid("driveToB1")
					MenuItem.commitSetItemValid("shareB")
					MenuItem.commitSetItemValid("mapB")
					MenuItem.commitSetItemValid("mapB1")
					MenuItem.commitSetItemValid("saveB")
					MenuItem.commitSetItemValid("rating")
					MenuItem.commitSetItemValid("postLocation")
					MenuItem.commitSetItemValid("phone")
					
					checkServerDriven(latAndLon)
					
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
				
				func hideMenuWhenNoLatLon(int latAndLon,int hidePhoneMenu)
				    #show or hide the buttons
				    Page.setComponentAttribute("driveToB1","visible",latAndLon + "")
				    Page.setComponentAttribute("mapB1","visible",latAndLon + "")
				    Page.setComponentAttribute("driveToB","visible",latAndLon + "")
				    Page.setComponentAttribute("mapB","visible",latAndLon + "")
				    Page.setComponentAttribute("shareB","visible",latAndLon + "")
				    Page.setComponentAttribute("saveB","visible",latAndLon + "")
				endfunc
					
				func showAdsLogo(String showLogo)
					Page.setComponentAttribute("citySearchDetail","visible",showLogo)
				endfunc
				
				func showAdsComponent(String showAds)
					Page.setComponentAttribute("sponsorInformation","visible",showAds)
					Page.setComponentAttribute("adsDelimiter","visible",showAds)
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
					Page.setComponentAttribute("reservationLink","height","50")
					Page.setComponentAttribute("poiInfoPanel","resetScroll", "vertical")
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
					Page.setComponentAttribute("reservationLink","height","50")
					Page.setComponentAttribute("poiInfoPanel","resetScroll", "vertical")
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
				
				#From POI detail,the address showing in two rows
				func getWrapAddress_JSON(JSONObject stop)
					String addressStr = ""
					if NULL != stop
						String firstLine = JSONObject.get(stop,"firstLine")
					    if NULL != firstLine
					       addressStr = addressStr + firstLine
					    endif
						if "" != addressStr
							addressStr = addressStr + ", "  
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

				<%
					if (isFeedbackEnabled)
					{
				%>

                # script to forward to POI Detail Feedback page. Prepare a TxNode with
                # parameters for keyword, location, and category and forward to feedback page
                func poiDetailFeedback()

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


	<tml:menuItem name="DriveTo" 
		trigger="KEY_MENU|TRACKBALL_CLICK" onClick="DriveTo">
	</tml:menuItem>
	<tml:menuItem name="getDirections"
		trigger="KEY_MENU|TRACKBALL_CLICK" onClick="staticRoute">
	</tml:menuItem>


	<tml:block feature="<%=FeatureConstant.MOVIE%>">
		<tml:menuItem name="viewMovies"
			text="<%=msg.get("poi.details.movie")%>"
			pageURL="<%=showMovieUrl%>"
			trigger="KEY_MENU|TRACKBALL_CLICK">
			<tml:bean name="theaterId" valueType="String" value="" />
		</tml:menuItem>
		<tml:menuItem name="viewMoviesMenu"
			text="<%=msg.get("poi.details.movie")%>"
			pageURL="<%=showMovieUrl%>"
			trigger="KEY_MENU">
			<tml:bean name="theaterId" valueType="String" value="" />
		</tml:menuItem>
	</tml:block>

	<tml:actionItem name="makePhoneCallAction"
		action="<%=Constant.LOCALSERVICE_MAKEPHONECALL%>">
		<tml:input name="phonenumber" />
	</tml:actionItem>
	<tml:menuItem name="phoneCall" actionRef="makePhoneCallAction" />
	<tml:menuItem name="call"
		trigger="KEY_MENU|TRACKBALL_CLICK" onClick="call">
	</tml:menuItem>

	<tml:actionItem name="showMapAction" action="mapIt">
		<tml:input name="pSrc" />
	</tml:actionItem>

	<tml:menuItem name="doShowmap" actionRef="showMapAction">
	</tml:menuItem>
	<tml:menuItem name="showMap" 
		trigger="KEY_MENU|TRACKBALL_CLICK" onClick="showMap">
	</tml:menuItem>

	<tml:block feature="<%=FeatureConstant.SHARE_ADDRESS%>">
		<tml:menuItem name="shareAddress" trigger="KEY_MENU|TRACKBALL_CLICK"
			onClick="showShareAddress">
		</tml:menuItem>
	</tml:block>

	<tml:block feature="<%=FeatureConstant.UGC_VIEW%>">
		<tml:menuItem name="viewReviews" trigger="KEY_MENU|TRACKBALL_CLICK"
			text="<%=msg.get("poi.view.history")%>" onClick="viewReviews">
		</tml:menuItem>
	</tml:block>

	<tml:menuItem name="favorites"
		trigger="KEY_MENU|TRACKBALL_CLICK" onClick="createFavorites">
	</tml:menuItem>

	<tml:menuItem name="rateThis" 
		trigger="KEY_MENU|TRACKBALL_CLICK" pageURL="<%=getPage + "RatePoi"%>"
		onClick="rateThis">
	</tml:menuItem>

	<tml:block feature="<%=FeatureConstant.FEEDBACK_POI%>">
		<tml:menuItem name="poiDetailFeedbackMenu" onClick="poiDetailFeedback" text="<%=msg.get("common.givefeedback")%>" trigger="KEY_MENU">
		</tml:menuItem>
		<tml:menuItem name="gotoPoiDetailFeedback" pageURL="<%= getPage + "POIDetailFeedback" %>">
		      <tml:bean name="feedbackNode" valueType="TxNode" value="" />
		</tml:menuItem>
	</tml:block>

	<tml:page id="showDetailPage" url="<%=getPage + "ShowDetail"%>"
		groupId="<%=GROUP_ID_POI%>" type="<%=pageType%>" showLeftArrow="true"
		showRightArrow="true" helpMsg="$//$poidetails">

		<tml:menuRef name="viewMovies" />
		<tml:menuRef name="poiDetailFeedbackMenu" />

		<tml:title id="DetailTitle" align="center|middle" fontColor="white"
			fontWeight="bold|system_large">
			<%=msg.get("poi.list.title")%>
		</tml:title>

		<tml:tabContainer id="showDetailContainer" style="vertical"
			defaultFocus="0">
			<tml:param name="onChange" value="onTabsChange"/>
			<tml:tab id="Details" label="<%=" "+msg.get("poi.details") %>"
				fontWeight="bold|system_large">
				<tml:panel id="poiInfoPanel" layout="vertical">
					<tml:urlLabel id="dummyLabel1" isFocusable="true" height="1"/>
					<tml:panel id="adsIconPanel" height="20">
						<tml:image id="citySearchDetail"></tml:image>
					</tml:panel>
					<tml:panel id="poiNamePanel" layout="horizontal">
						<tml:image id="sponsorImage" align="left|bottom"/>
						<tml:multiline id="poiName"  fontWeight="system_large"
							align="left|top"/>
					<% if(TnUtil.isATTRIM(handlerGloble) && "9810".equalsIgnoreCase(device)){ %>
						<tml:label id="distance" focusFontColor="white"
								fontWeight="system_large" textWrap="ellipsis"
								align="center|top">
							</tml:label>						
					<% } %>
					</tml:panel>
					<tml:nullField id="nullField1" height="10"></tml:nullField>
           <% if (featureMgr.isEnabled(FeatureConstant.POST_LOCATION)){%>
					<tml:panel id="buttonsPanel" layout="horizontal">
						<tml:compositeListItem id="rating" bgColor="#FFFFFF">
							<tml:image id="starImage1_0"  />
							<tml:image id="starImage2_0"  />
							<tml:image id="starImage3_0"  />
							<tml:image id="starImage4_0"  />
							<tml:image id="starImage5_0"  />
							<tml:label id="ratingNumber" fontWeight="system_median" align="left|top" focusFontColor="white" />
							<tml:menuRef name="rateThis" />
							<tml:menuRef name="viewMoviesMenu" />
						</tml:compositeListItem>
						<tml:nullField id="nullButtField" height="45"></tml:nullField>
						<tml:compositeListItem id="postLocation" bgColor="#FFFFFF">
							<tml:label id="postLocationL" fontWeight="system_large" align="left|center" focusFontColor="white">
								<%=msg.get("apps.PostLocation")%>
							</tml:label>
							<tml:menuRef name="postLocationMI" />
							<tml:menuRef name="viewMoviesMenu" />
						</tml:compositeListItem>
					</tml:panel>
            <%}else{%>
						<tml:compositeListItem id="rating" bgColor="#FFFFFF">
							<tml:image id="starImage1_0"  />
							<tml:image id="starImage2_0"  />
							<tml:image id="starImage3_0"  />
							<tml:image id="starImage4_0"  />
							<tml:image id="starImage5_0"  />
							<tml:label id="ratingNumber" fontWeight="system_median" align="left|top" focusFontColor="white" />
							<tml:menuRef name="rateThis" />
							<tml:menuRef name="viewMoviesMenu" />
						</tml:compositeListItem>
            <%}%>
					<tml:nullField id="nullField2" height="10"></tml:nullField>
					<tml:multiline id="address" align="left" fontWeight="system_median"/>
					<tml:nullField id="nullFieldForAddress" height="1" />
					<tml:urlLabel id="phone" fontWeight="system_median" align="left|bottom"
						fontColor="#005AFF">
						<tml:menuRef name="call" />
						<tml:menuRef name="viewMoviesMenu" />
					</tml:urlLabel>
					
					
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
					
					<tml:nullField id="nullField3" height="10" />
					<tml:multiline id="sponsorInformation" align="left|top"
						fontWeight="system_median" isFocusable="true" heightAutoScale="true">
					</tml:multiline>

				</tml:panel>

			</tml:tab>

			<tml:block feature="<%=FeatureConstant.UGC_VIEW%>">
				<tml:tab id="Reviews" label="<%=" "+msg.get("poi.reviews") %>"
					fontWeight="bold|system_large">

					<tml:block feature="<%=FeatureConstant.UGC_EDIT%>">
						<tml:button id="reviewButton"
							text="<%=msg.get("poi.write.review")%>" fontWeight="system">
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
							isFocusable="true">
							<tml:label id="<%="reviews_info" + i%>" focusFontColor="white"
								fontWeight="system_median" textWrap="ellipsis"
								align="left">
							</tml:label>

							<tml:image
								id="<%="reviews_starImage1_"
                                                + i%>"/>
							<tml:image
								id="<%="reviews_starImage2_"
                                                + i%>"/>
							<tml:image
								id="<%="reviews_starImage3_"
                                                + i%>"/>
							<tml:image
								id="<%="reviews_starImage4_"
                                                + i%>"/>
							<tml:image
								id="<%="reviews_starImage5_"
                                                + i%>"/>

							<tml:label id="<%="name_time" + i%>" focusFontColor="white"
								fontWeight="system_median" textWrap="ellipsis"
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
			<tml:tab label="<%=" "+msg.get("poi.coupons") %>"
				id="<%=couponTabID%>" icon="<%=couponsImage%>" fontWeight="bold|system_large">
				<tml:panel id="couponPanel" layout="vertical">
					<tml:urlLabel id="dummyLabel2" isFocusable="true" height="1"/>
				    <tml:panel id="citySearchPanel">
						<tml:image id="citySearchCoupon" align="center"></tml:image>
					</tml:panel>
					<%
						for (int i = 0; i < 5; ++i) {
					%>
					<tml:multiline id="<%="couponDesc"+i %>" fontWeight="system_median"
						isFocusable="true" align="left|top" />
					<tml:image id="<%="couponImg"+i %>" align="center|top"></tml:image>

					<tml:label id="<%="expireDate"+i %>" fontWeight="system_small"
						align="center|top" />

					<%
						}
					%>
				</tml:panel>
			</tml:tab>

			<tml:tab id="<%=menuTabID%>"
				label="<%=" "+msg.get("poi.hours.menu") %>" icon="<%=menuImage%>"
				fontWeight="bold|system_large">
				<tml:panel id="menuPanel" layout="vertical">
					<tml:urlLabel id="dummyLabel3" isFocusable="true" height="1"/>
					<tml:panel id="citySearchMenuPanel">
						<tml:image id="citySearchMenu"></tml:image>
					</tml:panel>
					<tml:multiline id="menu" fontWeight="sytem_large" align="left"
						heightAutoScale="true" isFocusable="true" />
				</tml:panel>
			</tml:tab>
		</tml:tabContainer>
		
		<tml:image id="shadowBg1" align="left|top"/>
		<tml:image id="controlBg1" align="left|top"/>
		<tml:button id="driveToB1" text="<%=msg.get("poi.drive.to")%>"
			fontWeight="system_large">
			<tml:menuRef name="DriveTo" />
			<tml:menuRef name="viewMoviesMenu" />
		</tml:button>

		<tml:button id="mapB1" text="<%=msg.get("poi.map.button")%>"
			fontWeight="system_large">
			<tml:menuRef name="showMap" />
			<tml:menuRef name="viewMoviesMenu" />
		</tml:button>

		<tml:image id="shadowBg" align="left|top"/>
		<tml:image id="controlBg" align="left|top"/>
		<tml:button id="driveToB" text="<%=msg.get("poi.drive.to")%>"
			fontWeight="system_large">
			<tml:menuRef name="DriveTo" />
			<tml:menuRef name="viewMoviesMenu" />
		</tml:button>

		<tml:button id="mapB" text="<%=msg.get("poi.map.button")%>"
			fontWeight="system_large">
			<tml:menuRef name="showMap" />
			<tml:menuRef name="viewMoviesMenu" />
		</tml:button>
		<tml:button id="shareB" text="<%=msg.get("poi.share.addr.button")%>"
			fontWeight="system_large">
			<tml:menuRef name="shareAddress" />
			<tml:menuRef name="viewMoviesMenu" />
		</tml:button>
		<tml:button id="saveB" text="<%=msg.get("common.button.Save")%>"
			fontWeight="system_large">
			<tml:menuRef name="favorites" />
			<tml:menuRef name="viewMoviesMenu" />
		</tml:button>
	</tml:page>
	<cserver:outputLayout />
</tml:TML>
