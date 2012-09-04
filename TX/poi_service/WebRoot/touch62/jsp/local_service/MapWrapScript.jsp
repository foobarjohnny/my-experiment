<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.telenav.cserver.poi.struts.Constant"%>
<%@page import="com.telenav.cserver.poi.struts.util.PoiUtil"%>
<%@page import="com.telenav.cserver.util.TnUtil"%>
<%@page import="com.telenav.cserver.util.FeatureConstant"%>
<%@ page import="com.telenav.cserver.localservice.MapSerivce"%>
<%@page import="com.telenav.cserver.pref.struts.util.PreferenceConstants"%>
<%@page import="com.telenav.cserver.browser.util.ClientHelper"%>

<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ include file="/touch62/jsp/Header.jsp"%>
<%
    String pageUrl = getPage + "MapWrap";
	String commuteModuleName = ClientHelper.getModuleNameForCommute(handlerGloble);

	String commuteAlertUrl = "{commuteAlert.http}/" + commuteModuleName + "/goToJsp.do?pageRegion=" + region + "&amp;jsp=Startup";
	String aboutMapsUrl=getPage+"AboutMenu";
	
	
%>
<%@page import="com.telenav.cserver.stat.*"%>
    <%@ include file="/touch62/jsp/model/OneBoxModel.jsp"%>
	<tml:script language="fscript" version="1">
		<![CDATA[
		<%@ include file="../GetServerDriven.jsp"%>
		<%@ include file="../CommonScript.jsp"%>
		func onShow()
			String instance = Page.getControlProperty("page","url_flag")
			if MapWrap_M_isShowingMap(instance) == 0
				showMap()
			elsif MapWrap_M_isChangingLocation(instance) == 1
				MapWrap_M_setChangingLocation(0 ,instance)
				showMap()
			else
				resumeMap()
			endif
		endfunc
		
		func onResume()
			resumeMap()
		endfunc
		
		func resumeMap()
			# Resume Map
			# Create Map parameter
			JSONObject mapParm
		
			# Service type
			JSONObject.put(mapParm, "<%=MapSerivce.Key.SERVICE_TYPE%>", "<%=MapSerivce.ServiceType.RESUME%>")
			
			# Resume
			TxNode parmNode
			TxNode.addMsg(parmNode, "" + mapParm)
			MenuItem.setBean("showMap", "mapParm", parmNode)
			
			System.doAction("showMap")
			return FAIL		
		endfunc
		
		func showMap()
			String instance = Page.getControlProperty("page","url_flag")
			
			# Get map type
			int mapType
			mapType = MapWrap_M_getMapType(instance)
			
			# Create Map parameter
			JSONObject mapParm
			JSONObject.put(mapParm, "<%=MapSerivce.Key.SERVICE_TYPE%>", "<%=MapSerivce.ServiceType.SHOW_MAP%>")
			JSONObject.put(mapParm, "<%=MapSerivce.Key.MAP_TYPE%>", mapType)
			
			if mapType == <%=MapSerivce.MapType.SINGLE_ADDRESS%>
				if "multi" == instance
				   showMultiAddresses(mapParm)
				else
				   showSingleAddress(mapParm)
				endif
				return FAIL
			elsif mapType == <%=MapSerivce.MapType.LAST_KNOWN_LOCATION%>
				showFollowMeMap(mapParm)
				return FAIL
			elsif mapType == <%=MapSerivce.MapType.POI%>
				showPoiMap(mapParm)			
				return FAIL
			elsif mapType==<%=MapSerivce.MapType.COMMUT_ALERT%>
				# if commute alrert is turn off then it is never going to be invoked 
				showAlertMap(mapParm)
				return FAIL
			elsif mapType == <%=MapSerivce.MapType.POI_WITH_ROUTE%>   	
				showPoiMap(mapParm)
				return FAIL
			elsif mapType == <%=MapSerivce.MapType.FOLLOW_ME_WITH_TRAFFIC%>
				showFollowMeMap(mapParm)
				return FAIL
			else
				System.back()
			endif
			return FAIL
		endfunc	
		
		func showFollowMeMap(JSONObject mapParm)
			# Map menu
			if isMapUser()==0
				setSingleMapMenu(mapParm)	
			else
				setSingleATTMapMenu(mapParm)
			endif

			# Show map
			TxNode parmNode
			TxNode.addMsg(parmNode, "" + mapParm)
			MenuItem.setBean("showMap", "mapParm", parmNode)
			
			System.doAction("showMap")
			return FAIL
		endfunc
		
		func showSingleAddress(JSONObject mapParm)
			String instance = Page.getControlProperty("page","url_flag")
		
			# Load the location from the model
			JSONObject location = JSONArray.get(MapWrap_M_getPoiArray(instance), 0)
			JSONObject stop = JSONObject.get(location, "stop")
			JSONObject feature 
			if NULL == stop
				# this is a normal stop
				feature = convertStopToFeature(location)
			else
				# this is a poi
				feature = convertPoiToFeature(location,"0",1)
			endif
			JSONArray featureArray
			JSONArray.put(featureArray, feature)
			JSONObject.put(mapParm, "<%=MapSerivce.Key.SCREEN_FEATURE%>", featureArray)			

			# Map menu
			if(Account.isTnMaps()==0)
				setSingleMapMenu(mapParm)
			else
				setSingleATTMapMenu(mapParm)
			endif

			# Show map
			TxNode parmNode
			TxNode.addMsg(parmNode, "" + mapParm)
			MenuItem.setBean("showMap", "mapParm", parmNode)
			System.doAction("showMap")
			# MIS reporting for POI MapIt from change location flow
			if stop != NULL
				Logger_logPOI_JSON(<%=EventTypes.POI_VIEW_MAP%>, "3", location, <%=AttributeValues.POI_TYPE_NORMAL%>, -1)
			endif
			
			return FAIL
		endfunc
		
		func showMultiAddresses(JSONObject mapParm)
		    String instance = Page.getControlProperty("page","url_flag")
		    JSONArray MultiAddressesJA = MapWrap_M_getPoiArray(instance)
		    int length = JSONArray.length(MultiAddressesJA)
		    int index = 0
		    JSONArray featureArray
		    JSONObject location
		    JSONObject stop
		    JSONObject feature
		    while index < length
		        location = JSONArray.get(MultiAddressesJA, index)
				stop = JSONObject.get(location, "stop")
				if NULL == stop
					# this is a normal stop
					feature = convertStopToFeature(location)
				else
					# this is a poi
					feature = convertPoiToFeature(location,"0",1)
				endif
				JSONArray.put(featureArray, feature)
		        index = index + 1
		    endwhile
		    
			JSONObject.put(mapParm, "<%=MapSerivce.Key.SCREEN_FEATURE%>", featureArray)	

			# Map menu
			if(Account.isTnMaps()==0)
				setSingleMapMenu(mapParm)
			else
				setSingleATTMapMenu(mapParm)
			endif

			# Show map
			TxNode parmNode
			TxNode.addMsg(parmNode, "" + mapParm)
			MenuItem.setBean("showMap", "mapParm", parmNode)
			System.doAction("showMap")
			# MIS reporting for POI MapIt from change location flow
			if stop != NULL
				Logger_logPOI_JSON(<%=EventTypes.POI_VIEW_MAP%>, "3", location, <%=AttributeValues.POI_TYPE_NORMAL%>, -1)
			endif
			
			return FAIL
		endfunc
		
		
		func showPoiMap(JSONObject mapParm)
			String instance = Page.getControlProperty("page","url_flag")
			
			# Load the poi array from the model and convert to feature array.
			JSONArray featureArray
			JSONArray poiArray = MapWrap_M_getPoiArray(instance)
			JSONArray sponsorArray = MapWrap_M_getSponsorArray(instance)
			JSONObject anchorPoint = MapWrap_M_getAnchorPoint(instance)
			int length = JSONArray.length(poiArray)
			int i = 0
			JSONObject feature 
			
			#Add By ChengBiao. Show rating stars in map or not.
			int needShowRating = 0
			String categoryId = PoiList_C_getSearchCategory()
			int isGasByPrice = PoiList_C_isGasByPrice(categoryId)
			if 0 == isGasByPrice
			    needShowRating = 1
			endif
			
			# Convert all the features.
			while i < length
				feature = convertPoiToFeature(JSONArray.get(poiArray, i),"1",needShowRating)
				JSONArray.put(featureArray, feature)
				i = i + 1
			endwhile
			JSONObject.put(mapParm, "<%=MapSerivce.Key.SCREEN_FEATURE%>", featureArray)			
			
			#Convert sponsor pois 
			JSONArray sponsorfeatureArray
			length = JSONArray.length(sponsorArray)
			i = 0
			
			# Convert all the features.
			while i < length
				feature = convertPoiToFeature(JSONArray.get(sponsorArray, i),"1",0)
				JSONArray.put(sponsorfeatureArray, feature)
				i = i + 1
			endwhile
			
			
			# Set feature index
			int index = MapWrap_M_getPoiIndex(instance)
			JSONObject.put(mapParm, "<%=MapSerivce.Key.SCREEN_FEATURE_INDEX%>", index)			
			#Set sponsor array
			JSONObject.put(mapParm, "<%=MapSerivce.Key.KEY_A_POI_ADS_LIST%>", sponsorfeatureArray)			
			
			#Set anchor point
			if NULL != 	anchorPoint
				# No anchor point when searching along route	
				int type = JSONObject.get(anchorPoint,"type")
				if(type!=6)
					JSONObject.put(mapParm,"AnchorPoint",anchorPoint)
				endif
			endif

			#set sponsor index
			int sponsorIndex = MapWrap_M_getSponsorIndex(instance)
			JSONObject.put(mapParm,"<%=MapSerivce.Key.KEY_I_SELECTED_POI_ADS_INDEX%>",sponsorIndex)
			
			# Map menu
			int mapType = JSONObject.get(mapParm, "<%=MapSerivce.Key.MAP_TYPE%>")
			JSONArray menuArray = getPoiMapMenu(mapType)
			JSONObject.put(mapParm, "<%=MapSerivce.Key.MENU_ITEMS%>", menuArray)

			# Show map
			TxNode parmNode
			TxNode.addMsg(parmNode, "" + mapParm)
			MenuItem.setBean("showMap", "mapParm", parmNode)

			System.doAction("showMap")
			return FAIL
		endfunc

		# Get the map menu for single map.
		<%if(PoiUtil.isRimNonTouch(handlerGloble)){%>		
		func setSingleMapMenu(JSONObject mapParm)
			JSONArray menuArray
			
			<%if(TnUtil.isATTRIM63(handlerGloble)){%>
			    JSONArray.put(menuArray, menuQuickSearchMenu())
			    JSONArray.put(menuArray, menuDriveToMenu())
			    JSONArray.put(menuArray, menuSearchNearBy())
			    JSONArray.put(menuArray, menuAttExtras())
			    if checkDSRAvail()
					JSONArray.put(menuArray, menuShowDSR())
				endif
				if checkUpgrade()
					JSONArray.put(menuArray, menuUpgrade())
				endif
			    JSONArray.put(menuArray, menuSeperator())
			    
			    if isMapUser() == 0 && 1 == MapWrap_M_isTrafficSupported()
					JSONArray.put(menuArray, menuTraffic())
				endif
			    JSONArray.put(menuArray, menuZoom())
			    JSONArray.put(menuArray, menuCurrentLocation())
			    JSONArray.put(menuArray, menuChangeLocation())
			    if MapWrap_M_isTrafficCameraSupported()
					JSONArray.put(menuArray, menuShowTrafficCamera())
				endif
				JSONArray.put(menuArray, menuAboutMaps())
				
				JSONArray menuArrayForLongClick
				JSONArray.put(menuArrayForLongClick, menuDriveTo())
				if 1 == ServerDriven_CanShareAddress()
				    JSONArray.put(menuArrayForLongClick, menuShareLocation())
				endif
				JSONArray.put(menuArrayForLongClick, menuRecordLocation())
				JSONObject.put(mapParm, "<%=MapSerivce.Key.ACTION_FOR_ADDRESS%>", menuArrayForLongClick)
			<%}else{%>
				JSONArray.put(menuArray, menuCurrentLocation())
				if isMapUser() == 0 && 1 == MapWrap_M_isTrafficSupported()
					JSONArray.put(menuArray, menuTraffic())
				endif
				JSONArray.put(menuArray, menuChangeLocation())
				
				JSONArray.put(menuArray, menuSeperator())
				JSONArray.put(menuArray, menuDriveTo())
				
				JSONArray.put(menuArray, menuSearchNearBy())
				JSONArray.put(menuArray, menuRecordLocation())
				if 1 == ServerDriven_CanShareAddress()
					JSONArray.put(menuArray, menuShareLocation())
				endif
				JSONArray.put(menuArray, menuSeperator())
				
				if MapWrap_M_isTrafficCameraSupported()
					JSONArray.put(menuArray, menuShowTrafficCamera())
				endif
				
				if MapWrap_M_isSpeedTrapSupported()
					JSONArray.put(menuArray, menuShowPoliceSpeedTrap())
				endif
				
				JSONArray.put(menuArray, menuSeperator())
				JSONArray.put(menuArray, menuMapStyle())
				JSONArray.put(menuArray, menuZoom())
			<%}%>
			println("~+++~~~~~~~~~~~~~~~~~~setSingleMapMenu menuArray~~~~~~~~~~~"+menuArray)
			JSONObject.put(mapParm, "<%=MapSerivce.Key.MENU_ITEMS%>", menuArray)
		endfunc
		<%}else{%>
		# Get the map menu for single map.
		func setSingleMapMenu(JSONObject mapParm)
			JSONArray menuArray
			<%if(TnUtil.isATTRIM63(handlerGloble)){%>
			JSONArray.put(menuArray, menuQuickSearchMenu())
			<%}%>
			JSONArray.put(menuArray, menuChangeLocation())
			JSONArray.put(menuArray, menuSearchNearBy())
			<%if(TnUtil.isATTRIM63(handlerGloble)){%>
			if checkDSRAvail()
				JSONArray.put(menuArray, menuShowDSR())
			endif
			JSONArray.put(menuArray, menuDriveToMenu())
			JSONArray.put(menuArray, menuAttExtras())
			if checkUpgrade()
				JSONArray.put(menuArray, menuUpgrade())
			endif
			JSONArray.put(menuArray, menuAboutMaps())
			<%}%>
			JSONObject.put(mapParm, "<%=MapSerivce.Key.MENU_ITEMS%>", menuArray)
			
			JSONArray menuArrayForLongClick
			JSONArray.put(menuArrayForLongClick, menuDriveTo())
			if 1 == ServerDriven_CanShareAddress()
			    JSONArray.put(menuArrayForLongClick, menuShareLocation())
			endif
			JSONArray.put(menuArrayForLongClick, menuRecordLocation())
			JSONObject.put(mapParm, "<%=MapSerivce.Key.ACTION_FOR_ADDRESS%>", menuArrayForLongClick)
			
			<%if(PoiUtil.isWarrior(handlerGloble)){%>
			JSONArray.put(menuArray, menuDriveTo())
			JSONArray.put(menuArray, menuRecordLocation())
			JSONArray.put(menuArray, menuShareLocation())
			<%}%>
			println("~~~~~~~~~setSingleMapMenu menuArray~~~~~~"+menuArray)
		endfunc
		<%}%>
		# Get the map menu for single map.
		func setSingleATTMapMenu(JSONObject mapParm)
			JSONArray menuArray
			<%if(TnUtil.isATTRIM63(handlerGloble)){%>
			    JSONArray.put(menuArray, menuQuickSearchMenu())
			    JSONArray.put(menuArray, menuGetRoute())
			    JSONArray.put(menuArray, menuSearchNearBy())
			    JSONArray.put(menuArray, menuAttExtras())
			    if checkDSRAvail()
					JSONArray.put(menuArray, menuShowDSR())
				endif
				JSONArray.put(menuArray, menuSeperator())
				
				if 1 == MapWrap_M_isTrafficSupported()
					JSONArray.put(menuArray, menuTraffic())
				endif
			    <%if(PoiUtil.isRimNonTouch(handlerGloble)){%>
					JSONArray.put(menuArray, menuZoom())
				<%}%>
			    JSONArray.put(menuArray, menuCurrentLocation())
				JSONArray.put(menuArray, menuChangeLocation())
			    if MapWrap_M_isTrafficCameraSupported()
					JSONArray.put(menuArray, menuShowTrafficCamera())
				endif
				JSONArray.put(menuArray, menuAboutMaps())
			<%}else{%>
				JSONArray.put(menuArray, menuSearchNearBy())
				JSONArray.put(menuArray, menuSeperator())
				JSONArray.put(menuArray, menuGetRoute())
				JSONArray.put(menuArray, menuCurrentLocation())
				JSONArray.put(menuArray, menuChangeLocation())
				
				
				JSONArray.put(menuArray, menuSeperator())
				JSONArray.put(menuArray, menuSavFav())
				if 1 == ServerDriven_CanShareAddress()
					JSONArray.put(menuArray, menuShareLocation())
				endif
				JSONArray.put(menuArray, menuSeperator())
				
				<%if(PoiUtil.isRimNonTouch(handlerGloble)){%>
				JSONArray.put(menuArray, menuZoom())
				<%}%>
				
				JSONArray.put(menuArray, menuSeperator())
				JSONArray.put(menuArray, menuAttExtras())
				JSONArray.put(menuArray, menuAboutMaps())
			<%}%>
			
			JSONObject.put(mapParm, "<%=MapSerivce.Key.MENU_ITEMS%>", menuArray)
			
			<%if(PoiUtil.isRimTouch(handlerGloble)||PoiUtil.isRimNonTouch(handlerGloble)){%>
			JSONArray menuArrayForLongClick
			JSONArray.put(menuArrayForLongClick, menuGetRoute())
			JSONArray.put(menuArrayForLongClick, menuSavFav())
			if 1 == ServerDriven_CanShareAddress()
			    JSONArray.put(menuArrayForLongClick, menuShareLocation())
			endif
			JSONObject.put(mapParm, "<%=MapSerivce.Key.ACTION_FOR_ADDRESS%>", menuArrayForLongClick)
			<%}%>
			
			println("~+++~~~~~~~~~~~~~~~~~~setSingleATTMapMenu menuArray~~~~~~~~~~~"+menuArray)
		endfunc
		
		# Get the map menu for poi map
		func getPoiMapMenu(int mapType)
			JSONArray menuArray

			JSONArray.put(menuArray, menuListResults())
			<%if(TnUtil.isATTRIM63(handlerGloble)){%>
				JSONArray.put(menuArray, menuShowNextPOI())
				JSONArray.put(menuArray, menuShowPreviousPOI())
				JSONArray.put(menuArray, menuShowNextpage())
				JSONArray.put(menuArray, menuShowPreviousPage())
				JSONArray.put(menuArray, menuSeperator())
				
				if <%=MapSerivce.MapType.POI_WITH_ROUTE%> != mapType 
					JSONArray.put(menuArray, menuQuickSearchMenu())
				endif
				
				
				if isMapUser()
					JSONArray.put(menuArray, menuGetRoute())
				else
					JSONArray.put(menuArray, menuDriveToMenu())
				endif
				JSONArray.put(menuArray, menuSearchNearBy())
				
				JSONArray.put(menuArray, menuAttExtras())
				if checkDSRAvail()
					JSONArray.put(menuArray, menuShowDSR())
				endif
				JSONArray.put(menuArray, menuSeperator())
					
				if isMapUser() == 0  && 1 == MapWrap_M_isTrafficSupported()
					JSONArray.put(menuArray, menuTraffic())
				endif
				<%if(PoiUtil.isRimNonTouch(handlerGloble)){%>
				JSONArray.put(menuArray, menuZoom())
				<%}%>
				JSONArray.put(menuArray, menuCurrentLocation())
				JSONArray.put(menuArray, menuShowTrafficCamera())
			<%}else{%>
				JSONArray.put(menuArray, menuSeperator())
				JSONArray.put(menuArray, menuCurrentLocation())
	<%if (featureMgr.isEnabled(FeatureConstant.TRAFIC)) {%>			
				if isMapUser() == 0  && 1 == MapWrap_M_isTrafficSupported()
					JSONArray.put(menuArray, menuTraffic())
				endif
	<%}%>
	            <%if(PoiUtil.isRimNonTouch(handlerGloble)){%>
				JSONArray.put(menuArray, menuZoom())
				<%}%>
			<%}%>
			
			println("~+++~~~~~~~~~~~~~~~~~~getPoiMapMenuget menuArray~~~~~~~~~~~"+menuArray)
			return menuArray
		endfunc
		
		func isMapUser()
			return Account.isTnMaps()
		endfunc
		
		##########################################################################
		# Menus
		##########################################################################
		# Get menu for current location
		func menuCurrentLocation()
			JSONObject menu
			
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TYPE%>", <%=MapSerivce.Menu.Type.STATIC%>)
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.ID%>", "CurrentLocation")
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TEXT%>", "<%=msg.get("map.menu.curr.location")%>")
			return menu
		endfunc
		
		# Get menu for change map location
		func menuChangeLocation()
			JSONObject menu
			
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TYPE%>", <%=MapSerivce.Menu.Type.DYNAMIC%>)
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.ID%>", "<%=MapSerivce.Menu.DynamicMenu.CHANGE_LOCATION%>")
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TEXT%>", "<%=msg.get("map.menu.change.location")%>")
			return menu
		endfunc		

		# Get menu for list results
		func menuListResults()
			JSONObject menu
			
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TYPE%>", <%=MapSerivce.Menu.Type.DYNAMIC%>)
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.ID%>", "<%=MapSerivce.Menu.DynamicMenu.LIST_RESULTS%>")
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TEXT%>", "<%=msg.get("map.menu.list.results")%>")
			return menu
		endfunc	
		
		
		func menuShowNextPOI()
			JSONObject menu
			
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TYPE%>", <%=MapSerivce.Menu.Type.STATIC%>)
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.ID%>", "<%=MapSerivce.Menu.DynamicMenu.POI_SHOW_NEXT%>")
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TEXT%>", "<%=msg.get("map.menu.next.poi")%>")
			return menu
		endfunc
		
		func menuShowPreviousPOI()
			JSONObject menu
			
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TYPE%>", <%=MapSerivce.Menu.Type.STATIC%>)
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.ID%>", "<%=MapSerivce.Menu.DynamicMenu.POI_SHOW_PREVIOUS%>")
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TEXT%>", "<%=msg.get("map.menu.previous.poi")%>")
			return menu
		endfunc
		
		func menuShowNextpage()
			JSONObject menu
			
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TYPE%>", <%=MapSerivce.Menu.Type.STATIC%>)
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.ID%>", "<%=MapSerivce.Menu.DynamicMenu.POI_NEXT_PAGE%>")
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TEXT%>", "<%=msg.get("map.menu.next.page")%>")
			return menu
		endfunc
		
		func menuShowPreviousPage()
			JSONObject menu
			
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TYPE%>", <%=MapSerivce.Menu.Type.STATIC%>)
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.ID%>", "<%=MapSerivce.Menu.DynamicMenu.POI_PREVIOUS_PAGE%>")
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TEXT%>", "<%=msg.get("map.menu.previous.page")%>")
			return menu
		endfunc

<%if (featureMgr.isEnabled(FeatureConstant.TRAFIC)) {%>			
		# Get menu for show hide traffic
		func menuTraffic()
			JSONObject menu
			
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TYPE%>", <%=MapSerivce.Menu.Type.STATIC%>)
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.ID%>", "Traffic")
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TEXT%>", "")
			return menu
		endfunc	
<%}%>		

<%if (featureMgr.isEnabled(FeatureConstant.TRAFIC_CAMERA)) {%>			
		# Get menu for add traffic camera
		func menuAddTrafficCamera()
			JSONObject menu
			
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TYPE%>", <%=MapSerivce.Menu.Type.STATIC%>)
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.ID%>", "AddTrafficCamera")
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TEXT%>", "<%=msg.get("map.menu.traffic.camera")%>")
			return menu
		endfunc		
		
		#Get menu to show traffic camera
		func menuShowTrafficCamera()
			JSONObject menu
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TYPE%>", <%=MapSerivce.Menu.Type.STATIC%>)
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.ID%>", "ShowTrafficCamera")
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TEXT%>", "<%=msg.get("map.menu.traffic.showcamera")%>")
			return menu
		endfunc
<%}%>

<%if (featureMgr.isEnabled(FeatureConstant.TRAFIC_TRAPS)) {%>			
		# Get menu for report police speed trap
		func menuReportPoliceSpeedTrap()
			JSONObject menu
			
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TYPE%>", <%=MapSerivce.Menu.Type.STATIC%>)
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.ID%>", "ReportPoliceSpeedTrap")
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TEXT%>", "<%=msg.get("map.menu.speed.trap")%>")
			return menu
		endfunc
		
		# Get menu to show police speed trap
		func menuShowPoliceSpeedTrap()
			JSONObject menu
			
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TYPE%>", <%=MapSerivce.Menu.Type.STATIC%>)
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.ID%>", "ShowPoliceSpeedTrap")
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TEXT%>", "<%=msg.get("map.menu.speed.showtrap")%>")
			return menu
		endfunc
<%}%>		
		# Get menu for map style
		func menuMapStyle()
			JSONObject menu
			
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TYPE%>", <%=MapSerivce.Menu.Type.STATIC%>)
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.ID%>", "MapStyle")
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TEXT%>", "<%=msg.get("map.menu.style")%>")
			return menu
		endfunc
		
		# Get menu for zoom
		func menuZoom()
			JSONObject menu
			
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TYPE%>", <%=MapSerivce.Menu.Type.STATIC%>)
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.ID%>", "Zoom")
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TEXT%>", "<%=msg.get("map.menu.zoom")%>")
			return menu
		endfunc
		
		# Get menu for one box search
		func menuQuickSearchMenu()
			JSONObject menu
			
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TYPE%>", <%=MapSerivce.Menu.Type.DYNAMIC%>)
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.ID%>", "<%=MapSerivce.Menu.DynamicMenu.QUICK_SEARCH%>")
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TEXT%>", "<%=msg.get("map.menu.quick.search")%>")
			return menu
		endfunc	
		
		# Get menu for search near by
		func menuSearchNearBy()
			JSONObject menu
			
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TYPE%>", <%=MapSerivce.Menu.Type.DYNAMIC%>)
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.ID%>", "<%=MapSerivce.Menu.DynamicMenu.SEARCH_NEAR_BY%>")
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TEXT%>", "<%=msg.get("map.menu.search")%>")
			return menu
		endfunc	
		
		# Get menu for drive to
		func menuDriveTo()
			JSONObject menu
			
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TYPE%>", <%=MapSerivce.Menu.Type.DYNAMIC%>)
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.ID%>", "<%=MapSerivce.Menu.DynamicMenu.DRIVE_TO%>")
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TEXT%>", "<%=msg.get("map.menu.drive")%>")
			return menu
		endfunc	
		
		#Get menu for directions
		func menuGetRoute()
			JSONObject menu
			
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TYPE%>", <%=MapSerivce.Menu.Type.DYNAMIC%>)
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.ID%>", "<%=MapSerivce.Menu.DynamicMenu.GET_ROUTE%>")
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TEXT%>", "<%=msg.get("map.menu.direction")%>")
			return menu
		endfunc	
		
		func menuSavFav()
			JSONObject menu
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TYPE%>", <%=MapSerivce.Menu.Type.DYNAMIC%>)
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.ID%>", "<%=MapSerivce.Menu.DynamicMenu.SAVE_FAVORITE%>")
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TEXT%>", "<%=msg.get("map.menu.favorites")%>")
			return menu
		endfunc
		
		#ATT Extras menu
		func menuAttExtras()
			JSONObject menu
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TYPE%>", <%=MapSerivce.Menu.Type.DYNAMIC%>)
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.ID%>", "<%=MapSerivce.Menu.DynamicMenu.ATT_EXTRAS%>")
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TEXT%>", "<%=msg.get("map.menu.extras")%>")
			return menu
		endfunc
		
		# ATT About Menu
		func menuAboutMaps()
			JSONObject menu
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TYPE%>", <%=MapSerivce.Menu.Type.DYNAMIC%>)
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.ID%>", "<%=MapSerivce.Menu.DynamicMenu.ATT_ABOUT%>")
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TEXT%>", "<%=msg.get("maps.menu.about")%>")
			return menu
		endfunc

		# Get menu seperator
		func menuSeperator()
			JSONObject menu
			
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TYPE%>", <%=MapSerivce.Menu.Type.SEPERATOR%>)
			return menu
		endfunc
		
		# DSR
		func menuShowDSR()
			JSONObject menu
			
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TYPE%>", <%=MapSerivce.Menu.Type.DYNAMIC%>)
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.ID%>", "<%=MapSerivce.Menu.DynamicMenu.SHOW_DSR%>")
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TEXT%>", "<%=msg.get("maps.menu.command")%>")
			return menu
		endfunc	

		# UPGRADE
		func menuUpgrade()
			JSONObject menu
			
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TYPE%>", <%=MapSerivce.Menu.Type.DYNAMIC%>)
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.ID%>", "<%=MapSerivce.Menu.DynamicMenu.UPGRADE_VERSION%>")
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TEXT%>", "<%=msg.get("startup.menu.Upgrade")%>")
			return menu
		endfunc

		# Get menu for drive to(To ac page)
		func menuDriveToMenu()
			JSONObject menu
			
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TYPE%>", <%=MapSerivce.Menu.Type.DYNAMIC%>)
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.ID%>", "<%=MapSerivce.Menu.DynamicMenu.DRIVE_TO_MENU%>")
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TEXT%>", "<%=msg.get("map.menu.drive")%>")
			return menu
		endfunc	
		
		##########################################################################
		# Map service call back
		##########################################################################
		func mapServiceReturned()
			TxNode respNode = ParameterSet.getParam("mapResponse")
			
			# Get call back id			
			String respStr = TxNode.msgAt(respNode, 0)
			JSONObject resp = JSONObject.fromString(respStr)
			String callbackId = JSONObject.getString(resp, "<%=MapSerivce.Response.Key.ID%>")
			# Get Map status.
			String statusString = JSONObject.getString(resp, "<%=MapSerivce.Response.Key.MAP_STATUS%>")
			JSONObject status = JSONObject.fromString(statusString)
			
			# Swith each cases.
			if "<%=MapSerivce.Menu.DynamicMenu.BACK%>" == callbackId
				if isMapUser()==1
					String instance = Page.getControlProperty("page","url_flag")
					<%if(TnUtil.isATTRIM63(handlerGloble)){%>
						if "restaurant" == instance
						   System.back()
	               		   return FAIL
						endif
           		    <%}%>
					
					# Get map type
					int mapType
					mapType = MapWrap_M_getMapType(instance)
		
					if mapType == <%=MapSerivce.MapType.SINGLE_ADDRESS%>
						System.exit()
						return FAIL
					elsif mapType == <%=MapSerivce.MapType.LAST_KNOWN_LOCATION%>
						System.exit()
						return FAIL
					endif
				endif	
				
				backFromMap()
				return FAIL
			endif
			
			String instance = Page.getControlProperty("page","url_flag")
			# Process the command from map service
			MapWrap_M_setShowingMap(1, instance)
			MapWrap_M_setChangingLocation(0,instance)
			if "<%=MapSerivce.Menu.DynamicMenu.CHANGE_LOCATION%>" == callbackId
				changeLocation(status)
			elsif "<%=MapSerivce.Menu.DynamicMenu.SEARCH_NEAR_BY%>" == callbackId
				searchNearBy(status)
			elsif "<%=MapSerivce.Menu.DynamicMenu.DRIVE_TO%>" == callbackId
				driveTo(status)
			elsif "<%=MapSerivce.Menu.DynamicMenu.GET_ROUTE%>" == callbackId
				getDirections(status)
			elsif "<%=MapSerivce.Menu.DynamicMenu.SHARE_LOCATION%>" == callbackId
				shareLocation(status)
			elsif "<%=MapSerivce.Menu.DynamicMenu.COMMUTE_ALERT%>" == callbackId
				System.doAction("commuteAlert")
			elsif "<%=MapSerivce.Menu.DynamicMenu.FEATURE_SELECTED%>" == callbackId
				showPoiDetail(status)
			elsif "<%=MapSerivce.Menu.DynamicMenu.EDIT_ALERT%>" == callbackId
				editAlert(resp)
			elsif "<%=MapSerivce.Menu.DynamicMenu.DELETE_ALERT%>" == callbackId
				changeAlertStatus(callbackId)
			elsif "<%=MapSerivce.Menu.DynamicMenu.TURN_OFF_ALERT%>" == callbackId
				changeAlertStatus(callbackId)
			elsif "<%=MapSerivce.Menu.DynamicMenu.TURN_ON_ALERT%>"  == callbackId
				changeAlertStatus(callbackId)
			elsif "<%=MapSerivce.Menu.DynamicMenu.COPY_ALERT%>"  == callbackId
				copyAlert()
			elsif "<%=MapSerivce.Menu.DynamicMenu.REVERSE_ALERT%>"  == callbackId
				reverseAlert()
			elsif "<%=MapSerivce.Menu.DynamicMenu.ATT_EXTRAS%>"  == callbackId
				goToATTExtras()
			elsif "<%=MapSerivce.Menu.DynamicMenu.SAVE_FAVORITE%>"  == callbackId
				savFav(status)
			elsif "<%=MapSerivce.Menu.DynamicMenu.LIST_RESULTS%>" == callbackId
				PoiList_C_resume()			
			elsif "<%=MapSerivce.Menu.StaticMenu.RECORD_LOCATION%>" == callbackId
				savFav(status)
			elsif "<%=MapSerivce.Menu.DynamicMenu.ATT_ABOUT%>" == callbackId
				goToAboutMaps()
			elsif "<%=MapSerivce.Menu.DynamicMenu.SHOW_DSR%>" == callbackId
				goToSayCommand()
			elsif "<%=MapSerivce.Menu.DynamicMenu.UPGRADE_VERSION%>" == callbackId
				System.doAction("upgrade")
			elsif "<%=MapSerivce.Menu.DynamicMenu.DRIVE_TO_MENU%>" == callbackId
				onClickDriveTo1()
			elsif "<%=MapSerivce.Menu.DynamicMenu.UPGRADE%>" == callbackId
				upgradeToNav()
			elsif "<%=MapSerivce.Menu.DynamicMenu.QUICK_SEARCH%>" == callbackId
			    goToOnebox()
			else
				handleError(callbackId, status,instance)
			endif
			return FAIL
		endfunc
		
		func upgradeToNav()
		    int accountStatus = MapWrap_M_getUserStatusValue()
		    TxNode node
			TxNode.addValue(node, accountStatus)
			MenuItem.setBean("Purchase", "accountStatusNode", node)
			System.doAction("Purchase")
			return FAIL
		endfunc
		
		func goToOnebox()
		    OneBox_M_saveAcParam(getOneBoxParam())
		    OneBox_M_resetSearchType()
	    	System.doAction("oneBoxSearchMenu")
	    	return FAIL	
		endfunc
		
		func getOneBoxParam()
			JSONObject jo
			JSONObject.put(jo,"callbackfunction","addrCallBack")
			JSONObject.put(jo,"callbackpageurl","<%=getPageCallBack+"OneBoxWrap#Common"%>")
			JSONObject.put(jo,"from","Common")
        	return jo
     	endfunc
		
		func backFromMap()
		    String backAction = MapWrap_M_getBackAction()
            if "" != backAction
               if "<%=Constant.BACK_ACTION_MAIN_SCREEN%>" == backAction
                  MapWrap_M_deleteBackAction()
                  System.doAction("home")
                  return FAIL
               else
                  System.quit()
                  return FAIL
               endif
            else
               <%if(TnUtil.isATTRIM63(handlerGloble)){%>
					String instance = Page.getControlProperty("page","url_flag")
					if "restaurant" == instance
					   System.back()
               		   return FAIL
					endif
					
					# Get map type
					int mapType
					mapType = MapWrap_M_getMapType(instance)
					if mapType == <%=MapSerivce.MapType.SINGLE_ADDRESS%>
						System.exit()
						return FAIL
					elsif mapType == <%=MapSerivce.MapType.LAST_KNOWN_LOCATION%>
						System.exit()
						return FAIL
					endif
               <%}%>
                   System.back()
               return FAIL
            endif
            return FAIL
		endfunc
		
		func handleError(String callbackId, JSONObject status,String instance)
			int mapType = MapWrap_M_getMapType(instance)
			if mapType==<%=MapSerivce.MapType.COMMUT_ALERT%>
				String msg= "<%=msg.get("map.error.comuteAlert")%>"
				System.showGeneralMsg(NULL,msg,NULL,NULL,3,"Callback_PopopTimeOut")
				return FAIL
			else
				System.showErrorMsg("Unknow callback ID = " + callbackId + status)
			endif	
		endfunc

		func Callback_PopopTimeOut(int param)
			System.back()
			return FAIL
		endfunc

		func needDoRGC(JSONObject address)
			int needDoRGC = 0
			if !JSONObject.has(address,"city") && !JSONObject.has(address,"state")
				needDoRGC = 1
			endif 
			return needDoRGC		
		endfunc
		
		func savFav(JSONObject status)
			JSONObject poiJo = getPoiFromMapStatus(status)
			if NULL != poiJo
				JSONObject locationJo = JSONObject.get(poiJo,"stop")
				saveRGCToFav(locationJo,poiJo)	
				return FAIL
			else	
				JSONObject address
				address = getAddressFromMapStatus(status)			
				
				if needDoRGC(address)
					JSONObject jo
					JSONObject.put(jo,"lat",JSONObject.get(address, "lat"))
					JSONObject.put(jo,"lon",JSONObject.get(address, "lon"))
					JSONObject.put(jo,"type",6)
					String joStr=JSONObject.toString(jo)
					
					TxNode node
					TxNode.addMsg(node,joStr)
					TxRequest req
					String url="<%=host + "/getCurrentLocation.do"%>"
					String scriptName="savFavRGCCallback"
					TxRequest.open(req,url)
					TxRequest.setRequestData(req,node)
					TxRequest.onStateChange(req,scriptName)
					TxRequest.setProgressTitle(req,"<%=msg.get("rgc.loading")%>", "", TRUE)
					TxRequest.send(req)
				else
					saveRGCToFav(address,NULL)
				endif
			endif		
		endfunc
		
		func savFavRGCCallback(TxNode node,int status)
			if status == 0
				JSONArray bs = JSONArray.fromString("[OK]")
				JSONArray cp = JSONArray.fromString("[0]")
				String errorMsg = "<%=msg.get("error.not.available")%>"
				System.showGeneralMsgEx(NULL,errorMsg, bs, cp, 0, "RGCWaringCallback")
				return FAIL
			endif
			
			String addressStr = TxNode.msgAt(node, 1)
			if NULL == addressStr
				JSONArray bs = JSONArray.fromString("[OK]")
				JSONArray cp = JSONArray.fromString("[0]")
				String errorMsg = "<%=msg.get("rgc.error")%>"
				System.showGeneralMsgEx(NULL,errorMsg, bs, cp, 0, "RGCWaringCallback")
				return FAIL
			else
				JSONObject address
				address = JSONObject.fromString(addressStr)
				
				saveRGCToFav(address,NULL)					
			endif
		endfunc
		
		func saveRGCToFav(JSONObject locationJo,JSONObject poiJo)
			String instance = Page.getControlProperty("page","url_flag")		
			JSONObject jo
        	JSONObject.put(jo,"from","Map")
			JSONObject.put(jo,"callbackpageurl","<%=getPageCallBack + "MapWrap#"%>" + instance)
			CreateFavorites_C_onShow(locationJo,jo,poiJo)		
		endfunc
		
		func goToATTExtras()
			<%if(TnUtil.isATTRIM63(handlerGloble)){%>
				showATTExtras(!isMapUser())
			<%} else {%>
				showATTExtras(0)
			<%}%>
		endfunc
		
		func goToAboutMaps()
			System.doAction("aboutMaps")
			return FAIL
		endfunc
		func goToRecordLocation(JSONObject status)
			JSONObject address
			address = getAddressFromMapStatus(status)			
			if needDoRGC(address)
				JSONObject jo
				JSONObject.put(jo,"lat",JSONObject.get(address, "lat"))
				JSONObject.put(jo,"lon",JSONObject.get(address, "lon"))
				JSONObject.put(jo,"type",6)
				String joStr=JSONObject.toString(jo)
				
				TxNode node
				TxNode.addMsg(node,joStr)
				TxRequest req
				String url="<%=host + "/getCurrentLocation.do"%>"
				String scriptName="RecordLocationRGCCallback"
				TxRequest.open(req,url)
				TxRequest.setRequestData(req,node)
				TxRequest.onStateChange(req,scriptName)
				TxRequest.setProgressTitle(req,"<%=msg.get("rgc.loading")%>", "", TRUE)
				TxRequest.send(req)
			else
				RecordLocation_C_show(address)
			endif		
		endfunc

		func RecordLocationRGCCallback(TxNode node,int status)
			if status == 0
				JSONArray bs = JSONArray.fromString("[OK]")
				JSONArray cp = JSONArray.fromString("[0]")
				String errorMsg = "<%=msg.get("error.not.available")%>"
				System.showGeneralMsgEx(NULL,errorMsg, bs, cp, 0, "RGCWaringCallback")
				return FAIL
			endif
			
			String addressStr = TxNode.msgAt(node, 1)
			if NULL == addressStr
				JSONArray bs = JSONArray.fromString("[OK]")
				JSONArray cp = JSONArray.fromString("[0]")
				String errorMsg = "<%=msg.get("rgc.error")%>"
				System.showGeneralMsgEx(NULL,errorMsg, bs, cp, 0, "RGCWaringCallback")
				return FAIL
			else
				JSONObject address
				address = JSONObject.fromString(addressStr)
				
				RecordLocation_C_show(address)					
			endif
		endfunc
				
		func changeLocation(JSONObject mapStatus)
			# Do not save changing location flag here
			# Save it in change location callback 
			String instance = Page.getControlProperty("page","url_flag")
			
			JSONObject jo
        	JSONObject.put(jo,"title","<%=msg.get("selectaddress.title.map")%>")
        	JSONObject.put(jo,"mask","00111111111")
        	JSONObject.put(jo,"from","Map")
        	JSONObject.put(jo,"returnAsIs","1")
        	JSONObject.put(jo,"callbackfunction","changeLocationCallback")
			JSONObject.put(jo,"callbackpageurl","<%=getPageCallBack + "MapWrap#"%>" + instance)
			SelectAddress_C_SelectAddress(jo)
		endfunc
		
		func searchNearBy(JSONObject status) 
			JSONObject address
			address = getAddressFromMapStatus(status)
			JSONObject.put(address,"firstLine","<%=msg.get("map.area")%>")
			SearchPoi_C_searchNearLocation(address,"")
			return FAIL
		endfunc
		
		func driveTo(JSONObject status) 
			JSONObject poi = getPoiFromMapStatus(status)
			JSONObject address
			if NULL != poi
				address = poi
			else
				address = getAddressFromMapStatus(status)
				if needDoRGC(address)
					JSONObject jo
					JSONObject.put(jo,"lat",JSONObject.get(address, "lat"))
					JSONObject.put(jo,"lon",JSONObject.get(address, "lon"))
					JSONObject.put(jo,"type",6)
					String joStr=JSONObject.toString(jo)
					
					TxNode node
					TxNode.addMsg(node,joStr)
					TxRequest req
					String url="<%=host + "/getCurrentLocation.do"%>"
					String scriptName="DriveToRGCCallback"
					TxRequest.open(req,url)
					TxRequest.setRequestData(req,node)
					TxRequest.onStateChange(req,scriptName)
					TxRequest.setProgressTitle(req,"<%=msg.get("rgc.loading")%>", "", TRUE)
					TxRequest.send(req)
					return FAIL
				endif
			endif
			
			#If it is search along, give a string to client
			String instance = Page.getControlProperty("page","url_flag")
			int mapType = MapWrap_M_getMapType(instance)
			if mapType == <%=MapSerivce.MapType.POI_WITH_ROUTE%>		
	               DriveTo_M_saveNavType("Search Along")
			endif
				
			DriveToWrap_C_driveTo(address)
							
			if mapType == <%=MapSerivce.MapType.SINGLE_ADDRESS%>
				poi = JSONArray.get(MapWrap_M_getPoiArray(instance), 0)
				JSONObject stop = JSONObject.get(poi, "stop")
				# check if passed lat/lon is the same as poi lat/lon, otherwise 
				# it is drive to cursor, if address has no stop then it is just an address
				if NULL != stop
					int poiLat = JSONObject.get(stop, "lat")
					int poiLon = JSONObject.get(stop, "lon")
					JSONObject  mapCenter = JSONObject.get(status, "<%=MapSerivce.Response.Key.CENTER%>")
					int lat = JSONObject.get(mapCenter, "<%=MapSerivce.Response.Key.LAT%>")
					int lon = JSONObject.get(mapCenter, "<%=MapSerivce.Response.Key.LON%>")
					if poiLat == lat && poiLon == lon
						Logger_logPOI_JSON(<%=EventTypes.POI_DRIVE_TO%>, "3", poi, <%=AttributeValues.POI_TYPE_NORMAL%>, -1)
					endif
				endif
			endif
			return FAIL
		endfunc
		
		

		func DriveToRGCCallback(TxNode node,int status)
			if status == 0
				JSONArray bs = JSONArray.fromString("[OK]")
				JSONArray cp = JSONArray.fromString("[0]")
				String errorMsg = "<%=msg.get("error.not.available")%>"
				System.showGeneralMsgEx(NULL,errorMsg, bs, cp, 0, "RGCWaringCallback")
				return FAIL
			endif
			
			String addressStr = TxNode.msgAt(node, 1)
			if NULL == addressStr
				JSONArray bs = JSONArray.fromString("[OK]")
				JSONArray cp = JSONArray.fromString("[0]")
				String errorMsg = "<%=msg.get("rgc.error")%>"
				System.showGeneralMsgEx(NULL,errorMsg, bs, cp, 0, "RGCWaringCallback")
				return FAIL
			else
				JSONObject address
				address = JSONObject.fromString(addressStr)
				
				#If it is search along, give a string to client
				String instance = Page.getControlProperty("page","url_flag")
				int mapType = MapWrap_M_getMapType(instance)
				if mapType == <%=MapSerivce.MapType.POI_WITH_ROUTE%>		
	                DriveTo_M_saveNavType("Search Along")
				endif
				
				DriveToWrap_C_driveTo(address)
							
				if mapType == <%=MapSerivce.MapType.SINGLE_ADDRESS%>
					poi = JSONArray.get(MapWrap_M_getPoiArray(instance), 0)
					JSONObject stop = JSONObject.get(poi, "stop")
					# check if passed lat/lon is the same as poi lat/lon, otherwise 
					# it is drive to cursor, if address has no stop then it is just an address
					if NULL != stop
					 	int poiLat = JSONObject.get(stop, "lat")
					 	int poiLon = JSONObject.get(stop, "lon")
						JSONObject  mapCenter = JSONObject.get(status, "<%=MapSerivce.Response.Key.CENTER%>")
						int lat = JSONObject.get(mapCenter, "<%=MapSerivce.Response.Key.LAT%>")
						int lon = JSONObject.get(mapCenter, "<%=MapSerivce.Response.Key.LON%>")
						if poiLat == lat && poiLon == lon
							Logger_logPOI_JSON(<%=EventTypes.POI_DRIVE_TO%>, "3", poi, <%=AttributeValues.POI_TYPE_NORMAL%>, -1)
						endif
					endif
				endif			
			endif
		endfunc
		
		# Once before Drive To. Browser should RGC if destination has only lat lon not an address.
		# Later it changed and client will check the CLIENT_STOP_TYPE_CURSOR_ADDRESS = 8.
		# If so, it will do RGC itself. So RGCCallback function is not necessray from this page directly.
		# This function is left here in case of this fuction called from other page.
		func RGCCallback(TxNode node,int status)
			println("RGCCallback in MapWrap is called.")
			if status == 0
				JSONArray bs = JSONArray.fromString("[OK]")
				JSONArray cp = JSONArray.fromString("[0]")
				String errorMsg = "<%=msg.get("common.internal.error")%>"
				System.showGeneralMsgEx(NULL,errorMsg, bs, cp, 0, "RGCWaringCallback")
				return FAIL
			endif
			
			String addressStr = TxNode.msgAt(node, 1)
			if NULL == addressStr
				JSONArray bs = JSONArray.fromString("[OK]")
				JSONArray cp = JSONArray.fromString("[0]")
				String errorMsg = "Current location does not match any address."
				System.showGeneralMsgEx(NULL,errorMsg, bs, cp, 0, "RGCWaringCallback")
				return FAIL
			endif
			
			JSONObject address
			address = JSONObject.fromString(addressStr)
			DriveToWrap_C_driveTo(address)  
		endfunc		
		
		func RGCWaringCallback(int index)
			resumeMap()
		endfunc
		
		func getDirections(JSONObject status)
			JSONObject address = getAddressFromMapStatus(status)				
			if needDoRGC(address)
				EditRoute_C_StaticRoute(NULL)
			else
				#Call Edit Route Page, set current location as original 
				EditRoute_C_StaticRoute(address)
			endif	
		endfunc
		
		func showPoiDetail(JSONObject status)
			String instance = Page.getControlProperty("page","url_flag")
			int index = JSONObject.getInt(status, "<%=MapSerivce.Response.Key.SELECTED_INDEX%>")
			int adsIndex= JSONObject.getInt(status, "<%=MapSerivce.Key.KEY_I_SELECTED_POI_ADS_INDEX%>")
			JSONArray sponsorPoiListArray = PoiList_C_getSponsorPoiList()
			ShowDetail_C_saveForDetail(MapWrap_M_getPoiArray(instance),index,sponsorPoiListArray,adsIndex)
			ShowDetail_C_showDetail()
		endfunc		

		func shareAddressCallBack() 
		endfunc
			 
		func changeLocationCallback()
			String instance = Page.getControlProperty("page","url_flag")
			
			# Change location
			MapWrap_M_setChangingLocation(1,instance)
			
			# Get location 
        	TxNode node
        	node = ParameterSet.getParam("returnAddress")
        	if NULL != node
        		# Msg1 is POI string
        		String temp = TxNode.msgAt(node,1)
        		if NULL == temp
        			temp = TxNode.msgAt(node,0)
        		endif
        		JSONObject location = JSONObject.fromString(temp)
				# Create a new map
				MapWrap_M_setMapType(<%=MapSerivce.MapType.SINGLE_ADDRESS%>,instance)
				JSONArray array
				JSONArray.put(array, location)
				MapWrap_M_setPoiArray(array, instance)
				MapWrap_M_setPoiIndex(0, instance)
				
				MapWrap_M_setShowingMap(0, instance)
        	endif
		endfunc				

		func goToSayCommand()
			JSONObject jo
			invokeSpeakCommand(jo)
			return FAIL	
		endfunc	
		
		##########################################################################
		# Utility for Map Service
		##########################################################################
		func convertStopToFeature(JSONObject stop)
			JSONObject feature
			JSONObject.put(feature, "<%=MapSerivce.ScreenFeature.key.LAT%>",JSONObject.get(stop, "lat"))
			JSONObject.put(feature, "<%=MapSerivce.ScreenFeature.key.LON%>",JSONObject.get(stop, "lon"))
			JSONObject stopFeature
			String firstLine = JSONObject.get(stop,"firstLine")
			JSONObject.put(stopFeature,"FirstLine",firstLine)
			String lastLine = ""
			String cityName = JSONObject.get(stop,"city")
			String stateName = JSONObject.get(stop,"state")
			if  "" != cityName 
                if "" != stateName
				   lastLine = cityName + ", " + stateName
                endif
			elseif "" != stateName
				lastLine = stateName
		    endif
			String zip = JSONObject.get(stop,"zip")
			if( zip!="")
				lastLine=lastLine+" "+zip	
			endif
			String label = JSONObject.get(stop,"label")
			if label == firstLine
			    label = ""
				JSONObject.put(stop,"label",label)
			endif
			JSONObject.put(stopFeature,"SecondLine",lastLine)
			JSONObject.put(stopFeature,"FullAddress",JSONObject.toString(stop))
			
			JSONObject.put(feature, "<%=MapSerivce.ScreenFeature.key.TEXT%>", stopFeature)
			return feature
		endfunc

		func convertPoiToFeature(JSONObject poi,String needDistance,int needShowRating)
			JSONObject feature
			if UTIL_checkEmptyJSON(poi)
			    JSONObject.put(feature, "<%=MapSerivce.ScreenFeature.key.LAT%>",0)
			    JSONObject.put(feature, "<%=MapSerivce.ScreenFeature.key.LON%>",0)
			    JSONObject.put(feature, "<%=MapSerivce.ScreenFeature.key.TEXT%>","{}")
				return feature
			endif
			String stopStr = JSONObject.getString(poi, "stop")
			JSONObject stop = JSONObject.fromString(stopStr)
					
			JSONObject.put(feature, "<%=MapSerivce.ScreenFeature.key.LAT%>",JSONObject.get(stop, "lat"))
			JSONObject.put(feature, "<%=MapSerivce.ScreenFeature.key.LON%>",JSONObject.get(stop, "lon"))
			
			JSONObject text
			JSONObject.put(text, "Brand", JSONObject.get(poi, "name"))
			
            #Add By ChengBiao. Not giving "Rating" to map will hide rating icons.
			if 1 == needShowRating
			    JSONObject.put(text, "Rating", JSONObject.get(poi, "rating"))
			endif
			
			if "1" == needDistance
				JSONObject.put(text, "Distance", JSONObject.get(poi, "distance"))
			endif 
			
			JSONObject.put(text,"FirstLine",JSONObject.get(stop,"firstLine"))
			String lastLine = JSONObject.get(stop,"city")+", "+JSONObject.get(stop,"state")
			String zip = JSONObject.get(stop,"zip")
			if( zip!="")
				lastLine=lastLine+" "+zip	
			endif
			JSONObject.put(text,"SecondLine",lastLine)
			JSONObject.put(text,"FullAddress",JSONObject.toString(stop))			
			JSONObject.put(feature, "<%=MapSerivce.ScreenFeature.key.TEXT%>",text)
			
			return feature
		endfunc
		
		func getAddressFromMapStatus(JSONObject status)
			String instance = Page.getControlProperty("page","url_flag")
			# Selected address
			int index = JSONObject.getInt(status, "<%=MapSerivce.Response.Key.SELECTED_INDEX%>")
			if index >= 0
				JSONArray poiArray = MapWrap_M_getPoiArray(instance)
				if poiArray != NULL
					if JSONArray.length(poiArray) > index
						JSONObject location = JSONArray.get(poiArray, index)
						JSONObject stop = JSONObject.get(location,"stop")
						if NULL == stop
							# if there is a stop object, the location is not an address.
							# Please use getPoiFromMapStatus to get a POI
							return location
						endif
					endif				
				endif 
			endif
			
			# Cursor location
			JSONObject  mapCenter = JSONObject.get(status, "<%=MapSerivce.Response.Key.CENTER%>")
			JSONObject cursorAddress
			if JSONObject.has(mapCenter,"FullAddress")
				cursorAddress = JSONObject.fromString(JSONObject.get(mapCenter, "FullAddress"))
			else
				int lat = JSONObject.get(mapCenter, "<%=MapSerivce.Response.Key.LAT%>")
				int lon = JSONObject.get(mapCenter, "<%=MapSerivce.Response.Key.LON%>")
				
				JSONObject.put(cursorAddress, "lat", lat)
				JSONObject.put(cursorAddress, "lon", lon)
				String firstLineText = System.parseI18n("<%=msg.get("map.cursoraddress")%>")
				JSONObject.put(cursorAddress, "firstLine", firstLineText)
				# CLIENT_STOP_TYPE_CURSOR_ADDRESS = 8
				JSONObject.put(cursorAddress, "type", <%=Constant.STOP_CURSOR_ADDRESS%>)				
			endif
			DriveTo_M_saveStopType(<%=Constant.STOP_CURSOR_ADDRESS%>)
			
			return cursorAddress
		endfunc	
																			
		func getPoiFromMapStatus(JSONObject status)
			int index = JSONObject.getInt(status, "<%=MapSerivce.Response.Key.SELECTED_INDEX%>")
			String instance = Page.getControlProperty("page","url_flag")
			JSONArray poiArray
			if index >= 0 
				poiArray = MapWrap_M_getPoiArray(instance)
		    elsif index == -1 
		        index = JSONObject.getInt(status, "<%=MapSerivce.Response.Key.SELECTED_ADS_INDEX%>")
		        poiArray = MapWrap_M_getSponsorArray(instance)
		    else
		        return NULL
			endif

			if poiArray == NULL
				return NULL
			endif
			
			if index == -1
				return NULL
			endif
			
			if JSONArray.length(poiArray) > index
				JSONObject location = JSONArray.get(poiArray, index)
				if location == NULL
					return NULL
				endif
				JSONObject stop = JSONObject.get(location,"stop")
				if NULL != stop
					return location
				else
					return NULL
				endif
			else
				return NULL
			endif
		endfunc	
	]]>
	</tml:script>

	<tml:script language="fscript" version="1"
		feature="<%=FeatureConstant.COMMUTE_ALERTS%>">
		<![CDATA[
		func showAlertMap(JSONObject mapParm)
			String instance = Page.getControlProperty("page","url_flag")
			JSONObject param = MapWrap_M_getAlertId(instance)
			
			JSONObject.put(mapParm,"AlertID",JSONObject.get(param,"alertId"))
			JSONArray menuArray = getAlertMapMenu(JSONObject.get(param,"status"), JSONObject.get(param,"createNew"))
			JSONObject.put(mapParm, "<%=MapSerivce.Key.MENU_ITEMS%>", menuArray)
			#pass dest/orginal address as parameter
			if JSONObject.has(param,"origin")
				JSONObject.put(mapParm,"origin",JSONObject.get(param,"origin"))
			endif
			if JSONObject.has(param,"dest")
				JSONObject.put(mapParm,"dest",JSONObject.get(param,"dest"))
			endif			
			# Show map
			TxNode parmNode
			TxNode.addMsg(parmNode, "" + mapParm)
			MenuItem.setBean("showMap", "mapParm", parmNode)
			System.doAction("showMap")
			return FAIL
		endfunc
		
		func getAlertMapMenu(String alertStatus, int createNew)
			JSONArray menuArray
			JSONArray.put(menuArray, menuTrafficSummary())
			JSONArray.put(menuArray, menuSeperator())
			JSONArray.put(menuArray, menuEditAlert())
			if (alertStatus=="ENABLED")
				JSONArray.put(menuArray, menuTurnOffAlert())
			else
				JSONArray.put(menuArray, menuTurnOnAlert())
			endif
			JSONArray.put(menuArray, menuDeleteAlert())
			if(createNew)
				JSONArray.put(menuArray, menuCopyAlert())
				JSONArray.put(menuArray, menuReverseAlert())
			endif
			JSONArray.put(menuArray, menuSeperator())
			<%if(PoiUtil.isRimNonTouch(handlerGloble)){%>
			JSONArray.put(menuArray, menuZoom())
			<%}%>
			println("~+++~~~~~~~~~~~~~~~~~~getAlertMapMenu menuArray~~~~~~~~~~~"+menuArray)
			return menuArray
		endfunc
		
		# Get menu for Commute Alert
		func menuCommuteAlert()
			JSONObject menu
			
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TYPE%>", <%=MapSerivce.Menu.Type.DYNAMIC%>)
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.ID%>", "<%=MapSerivce.Menu.DynamicMenu.COMMUTE_ALERT%>")
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TEXT%>", "<%=msg.get("map.menu.comm.alert")%>")
			return menu
		endfunc
		
		#menu for traffic summary
		func menuTrafficSummary()
			JSONObject menu
			
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TYPE%>", <%=MapSerivce.Menu.Type.STATIC%>)
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.ID%>", "TrafficSummary")
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TEXT%>", "<%=msg.get("map.menu.traffic.summ")%>")
			return menu
		endfunc	
		
		func menuEditAlert()
			JSONObject menu
			
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TYPE%>", <%=MapSerivce.Menu.Type.DYNAMIC%>)
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.ID%>", "<%=MapSerivce.Menu.DynamicMenu.EDIT_ALERT%>")
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TEXT%>", "<%=msg.get("map.menu.edit.alert")%>")
			return menu
		endfunc
		
		func menuDeleteAlert()
			JSONObject menu
			
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TYPE%>", <%=MapSerivce.Menu.Type.DYNAMIC%>)
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.ID%>", "<%=MapSerivce.Menu.DynamicMenu.DELETE_ALERT%>")
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TEXT%>", "<%=msg.get("map.menu.delete.alert")%>")
			return menu
		endfunc
		
		func menuCopyAlert()
			JSONObject menu
			
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TYPE%>", <%=MapSerivce.Menu.Type.DYNAMIC%>)
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.ID%>", "<%=MapSerivce.Menu.DynamicMenu.COPY_ALERT%>" )
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TEXT%>", "<%=msg.get("map.menu.copy")%>")
			
			return menu
		endfunc
		
		func menuReverseAlert()
			JSONObject menu
			
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TYPE%>", <%=MapSerivce.Menu.Type.DYNAMIC%>)
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.ID%>", "<%=MapSerivce.Menu.DynamicMenu.REVERSE_ALERT%>" )
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TEXT%>", "<%=msg.get("map.menu.reverse.alert")%>")
			
			return menu
		endfunc
		
		func menuTurnOnAlert()
			JSONObject menu
			
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TYPE%>", <%=MapSerivce.Menu.Type.DYNAMIC%>)
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.ID%>", "<%=MapSerivce.Menu.DynamicMenu.TURN_ON_ALERT%>" )
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TEXT%>", "<%=msg.get("map.menu.on.alert")%>")
			return menu
		endfunc	
		
		func menuTurnOffAlert()
			JSONObject menu
			
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TYPE%>", <%=MapSerivce.Menu.Type.DYNAMIC%>)
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.ID%>", "<%=MapSerivce.Menu.DynamicMenu.TURN_OFF_ALERT%>" )
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TEXT%>", "<%=msg.get("map.menu.off.alert")%>")
			return menu
		endfunc
			
		func changeAlertStatus(String callbackId)
			Alert_C_ChangStatus(callbackId)
		endfunc
		
		func copyAlert()
			
			Alert_C_Copy()
		endfunc
		func reverseAlert()
			Alert_C_Reverse()
		endfunc
		
		func editAlert(JSONObject mapStatus)
			Alert_C_EditAlert(1)
		endfunc
		
		]]>
	</tml:script>

	<tml:script language="fscript" version="1"
		feature="<%=FeatureConstant.SHARE_ADDRESS%>">
		<![CDATA[
		# Get menu for share location
		func menuShareLocation()
			JSONObject menu
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TYPE%>", <%=MapSerivce.Menu.Type.DYNAMIC%>)
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.ID%>", "<%=MapSerivce.Menu.DynamicMenu.SHARE_LOCATION%>")
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TEXT%>", "<%=msg.get("map.menu.share")%>")
			return menu
		endfunc

		# Get menu for record location
		func menuRecordLocation()
			JSONObject menu
			
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TYPE%>", <%=MapSerivce.Menu.Type.DYNAMIC%>)
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.ID%>", "<%=MapSerivce.Menu.StaticMenu.RECORD_LOCATION%>")
			JSONObject.put(menu, "<%=MapSerivce.Menu.Key.TEXT%>", "<%=msg.get("map.menu.record")%>")
			return menu
		endfunc	
		
		func shareLocation(JSONObject status)
			String instance = Page.getControlProperty("page","url_flag")
			
			JSONObject share
        	JSONObject.put(share,"callbackfunction","shareAddressCallBack")
			JSONObject.put(share,"callbackpageurl","<%=getPageCallBack + "MapWrap#"%>" + instance)
			
			JSONObject poi = getPoiFromMapStatus(status)
			
			if NULL != poi
				JSONObject.put(share,"poi",poi)
				JSONObject.put(share,"address",JSONObject.get(poi,"stop"))
				ShareAddress_C_show(share)	
				return FAIL
			else 
				JSONObject address
				address = getAddressFromMapStatus(status)
				if needDoRGC(address)
					JSONObject jo
					JSONObject.put(jo,"lat",JSONObject.get(address, "lat"))
					JSONObject.put(jo,"lon",JSONObject.get(address, "lon"))
					JSONObject.put(jo,"type",6)
					String joStr=JSONObject.toString(jo)
					
					TxNode node
					TxNode.addMsg(node,joStr)
					TxRequest req
					String url="<%=host + "/getCurrentLocation.do"%>"
					String scriptName="shareLocationRGCCallback"
					TxRequest.open(req,url)
					TxRequest.setRequestData(req,node)
					TxRequest.onStateChange(req,scriptName)
					TxRequest.setProgressTitle(req,"<%=msg.get("rgc.loading")%>", "", TRUE)
					TxRequest.send(req)
					return FAIL
				endif
				JSONObject.put(share,"address",address)		
				ShareAddress_C_show(share)
				return FAIL			
			endif
		endfunc

		func shareLocationRGCCallback(TxNode node,int status)
			if status == 0
				JSONArray bs = JSONArray.fromString("[OK]")
				JSONArray cp = JSONArray.fromString("[0]")
				String errorMsg = "<%=msg.get("error.not.available")%>"
				System.showGeneralMsgEx(NULL,errorMsg, bs, cp, 0, "RGCWaringCallback")
				return FAIL
			endif
			
			String addressStr = TxNode.msgAt(node, 1)
			if NULL == addressStr
				JSONArray bs = JSONArray.fromString("[OK]")
				JSONArray cp = JSONArray.fromString("[0]")
				String errorMsg = "<%=msg.get("rgc.error")%>"
				System.showGeneralMsgEx(NULL,errorMsg, bs, cp, 0, "RGCWaringCallback")
				return FAIL
			else
				JSONObject address
				address = JSONObject.fromString(addressStr)
				
				String instance = Page.getControlProperty("page","url_flag")				
				JSONObject share
	        	JSONObject.put(share,"callbackfunction","shareAddressCallBack")
				JSONObject.put(share,"callbackpageurl","<%=getPageCallBack + "MapWrap#"%>" + instance)
				JSONObject.put(share,"address",address)
				ShareAddress_C_show(share)
				return FAIL			
			endif
		endfunc
		]]>
	</tml:script>	

	<tml:script language="fscript" version="1">
		<![CDATA[
			func onClickDriveTo1()
	       	 	#syn home again in case client does not finish the syn.
	        	SetUpHome_M_syncHomeFromClient()
	        	if Cell.isCoverage()
		        	goToACPage()
				else
					ignoreCheck("False")
				endif
				return FAIL
	        endfunc
			
			func goToACPage()
					JSONObject jo
		        	JSONObject.put(jo,"title","<%=msg.get("selectaddress.title.driveto")%>")
		        	JSONObject.put(jo,"mask","10111111111")
		        	JSONObject.put(jo,"from","DriveTo")
		        	JSONObject.put(jo,"returnAsIs","1")
		        	JSONObject.put(jo,"callbackfunction","CallBack_SelectAddress")
					JSONObject.put(jo,"callbackpageurl","<%=getPageCallBack + "DriveToWrap"%>")
					SelectAddress_C_SelectAddress(jo)
			endfunc
	        func checkCover()
	        	TxNode node 
       	        node = ParameterSet.getParam("checkCoverageBack")
       	        if TxNode.getStringSize(node)!=0
       	        	#check callback message
       	        	JSONObject msg = JSONObject.fromString(TxNode.msgAt(node,0))
       	        	String errorType
       	        	if JSONObject.has(msg,"Error")
 	      	       		errorType= JSONObject.get(msg,"Error")
 	      	       	else
 	      	       		errorType="back"
       	        	 endif
       	        	if errorType=="RadioIsOff"
						retry()
						return FAIL
					#go to AC page and show warning
       	        	else
       	        		SelectAddress_C_SetNoCoverage(TRUE)
       					goToACPage()	
       					return FAILWITHOUTREFRESH
    				endif
    			endif
	        endfunc
	        
	        
	        func ignoreCheck(String needCheck)
	        		JSONObject ignore
					JSONObject.put(ignore,"IgnoreRadioOff",needCheck)
					String ignoreStr = JSONObject.toString(ignore)
					TxNode node
	        		TxNode.addMsg(node,ignoreStr)
	        		MenuItem.setBean("checkCoverage","IgnoreRadioOff",node)
					System.doAction("checkCoverage")
					return FAIL
	        endfunc

	        func retry()
	        	System.showConfirm("<%=msg.get("common.radio.off")%>","<%=msg.get("common.button.retry")%>","<%=msg.get("common.button.Ignore")%>","selectRetry")
	        	return FAIL
	        endfunc
	        
	        func selectRetry(int selected)
	        	if selected==1
	        		onClickDriveTo1()
	        	elsif selected==0
	        		ignoreCheck("True")
	        	else
	        		
	        	endif
	        endfunc
	        
			func checkDSRAvail()
				if DSR_M_isDSRSupportedForDisable() == 0
					return FALSE
				else
					return TRUE
				endif
			endfunc
			
		    func checkUpgrade()
		    	int flag = StartUp_M_getUpgradeFlag()
		    	return flag
		    endfunc
		    
			func onClickUpgrade()
				string upgradeUrl = StartUp_M_getUpgradeUrl()
				if "" != upgradeUrl
					TxNode node
					TxNode.addMsg(node,upgradeUrl)
					TxNode.addMsg(node,"TRUE")
					MenuItem.setBean("callLocalBrowserMenu", "url", node)
					System.doAction("callLocalBrowserMenu")
				endif
				return FAIL
			endfunc
		]]>
	</tml:script>
	
	<tml:menuItem name="oneBoxSearchMenu" pageURL="<%=getPage + "OneBoxSearch#Common"%>">
	</tml:menuItem>
	<tml:actionItem name="doCheckCoverage" action="DriveToOffCoverageCheck">
		<tml:input name="IgnoreRadioOff"/>
		<tml:output name="checkCoverageBack" />
	</tml:actionItem>
	<tml:menuItem name="checkCoverage" actionRef="doCheckCoverage"
		onClick="checkCover" />
	<tml:menuItem name="upgrade" onClick="onClickUpgrade" />