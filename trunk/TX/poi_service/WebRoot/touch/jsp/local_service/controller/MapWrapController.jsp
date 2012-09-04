<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.cserver.util.FeatureConstant"%>
<%@page import="com.telenav.cserver.localservice.MapSerivce"%>

<%@ include file="../../Header.jsp"%>

<tml:script	language="fscript" version="1">
	<%@ include file="/touch/jsp/local_service/model/MapWrapModel.jsp"%>

	<![CDATA[
			func MapWrap_C_showTrafficMap()
				MapWrap_M_setMapType(<%=MapSerivce.MapType.FOLLOW_ME_WITH_TRAFFIC%>, "single")

				MapWrap_M_setShowingMap(0, "single")
				System.doAction("MapWrap_C_MenuShowMapSingle")
				return FAIL
			endfunc
			
			func MapWrap_C_showCurrent()
				MapWrap_M_setMapType(<%=MapSerivce.MapType.LAST_KNOWN_LOCATION%>, "single")

				MapWrap_M_setShowingMap(0, "single")
				System.doAction("MapWrap_C_MenuShowMapSingle")
				return FAIL
			endfunc

			func MapWrap_C_showSingleAddress(JSONObject location)
				MapWrap_M_setMapType(<%=MapSerivce.MapType.SINGLE_ADDRESS%>, "single")
				JSONArray array
				JSONArray.put(array, location)
				MapWrap_M_setPoiArray(array, "single")
				MapWrap_M_setPoiIndex(0, "single")
				
				MapWrap_M_setShowingMap(0, "single")
				System.doAction("MapWrap_C_MenuShowMapSingle")
				return FAIL
			endfunc	
			
			func MapWrap_C_showPoi(JSONArray poiArray, JSONArray sponsorArray, int index, JSONObject stop, int sponsorIndex)
				MapWrap_M_setMapType(<%=MapSerivce.MapType.POI%>, "poi")
				MapWrap_M_setPoiArray(poiArray, "poi")
				MapWrap_M_setPoiIndex(index, "poi")
				MapWrap_M_setAnchorPoint(stop, "poi")
				MapWrap_M_setSponsorArray(sponsorArray,"poi")
				MapWrap_M_setSponsorIndex(sponsorIndex,"poi")
				MapWrap_M_setShowingMap(0, "poi")
				System.doAction("MapWrap_C_MenuShowMapPOI")
				return FAIL
			endfunc
			
			func MapWrap_C_showPoiAlongRoute(JSONArray poiArray, JSONArray sponsorArray, int index, int sponsorIndex)		
				MapWrap_M_setMapType(<%=MapSerivce.MapType.POI_WITH_ROUTE%>, "poi")
				MapWrap_M_setPoiArray(poiArray, "poi")
				MapWrap_M_setSponsorArray(sponsorArray,"poi")
				MapWrap_M_setPoiIndex(index, "poi")
				MapWrap_M_setSponsorIndex(sponsorIndex,"poi")
				MapWrap_M_setShowingMap(0, "poi")
				System.doAction("MapWrap_C_MenuShowMapPOI")
				return FAIL
			endfunc						
			
			func MapWrap_C_saveBackAction(String backAction)
	           MapWrap_M_saveBackAction(backAction)
	        endfunc
	        
	        func MapWrap_C_getBackAction()
	           return MapWrap_M_getBackAction()
	        endfunc
	        
	        func MapWrap_C_deleteBackAction()
	           MapWrap_M_deleteBackAction()
	        endfunc
		]]>
</tml:script>

<tml:script language="fscript" version="1" feature="<%=FeatureConstant.COMMUTE_ALERTS%>">
		<![CDATA[
			func MapWrap_C_showCommuteAlert(int id, String status)
					MapWrap_M_setMapType(<%=MapSerivce.MapType.COMMUT_ALERT%> , "alert")
					MapWrap_M_setAlertId(id, status, "alert")

					MapWrap_M_setShowingMap(0, "alert")
					System.doAction("MapWrap_C_MenuShowMapAlert")
					return FAIL
			endfunc
		]]>
</tml:script>

<tml:menuItem name="MapWrap_C_MenuShowMapSingle"
	pageURL="<%=getPage+"MapWrap#single"%>" />
<tml:menuItem name="MapWrap_C_MenuShowMapPOI"
	pageURL="<%=getPage+"MapWrap#poi"%>" />
<tml:menuItem name="MapWrap_C_MenuShowMapAlert"
	pageURL="<%=getPage+"MapWrap#alert"%>" />		
