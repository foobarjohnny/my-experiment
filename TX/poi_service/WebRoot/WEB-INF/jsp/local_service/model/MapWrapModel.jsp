<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.cserver.util.FeatureConstant"%>

<%
    final String MAP_WRAP_M_MAP_TYPE = "MAP_WRAP_M_MAP_TYPE";
    final String MAP_WRAP_M_DEFAULT_MAP_STYLE = "MAP_WRAP_M_DEFAULT_MAP_STYLE";
    final String MAP_WRAP_M_DEFAULT_TRAFFIC = "MAP_WRAP_M_DEFAULT_TRAFFIC";
    final String MAP_WRAP_M_DEFAULT_ZOOM_LEVEL = "MAP_WRAP_M_DEFAULT_ZOOM_LEVEL";

    final String MAP_WRAP_M_SHOWING_MAP = "MAP_WRAP_M_SHOWING_MAP";
    final String MAP_WRAP_M_CHANGING_LOCATION = "MAP_WRAP_M_CHANGING_LOCATION";
	final String COMMUTE_ALERT_ID="COMMUTE_ALERT_ID";
    final String MAP_WRAP_M_LOCATION = "MAP_WRAP_M_LOCATION";
    final String MAP_WRAP_M_POI_ARRAY = "MAP_WRAP_M_POI_ARRAY";
    final String MAP_WRAP_M_SPONSOR_ARRAY="MAP_WRAP_M_SPONSOR_ARRAY";
    final String MAP_WRAP_M_POI_INDEX = "MAP_WRAP_M_POI_INDEX";
    final String MAP_WRAP_M_POI_ANCHOR_POINT= "MAP_WRAP_M_POI_ANCHOR_POINT";
    final String MAP_WRAP_M_SPONSOR_INDEX="MAP_WRAP_M_SPONSOR_INDEX";
    final String BACK_ACTION_MAPWRAP = "BACK_ACTION_MAPWRAP";
%>


<%@page import="com.telenav.cserver.localservice.MapSerivce"%><tml:script language="fscript" version="1">
	<![CDATA[
			####################################
			# Map type defination
			####################################
			func MapWrap_M_setMapType(int mapType, String instance)
				TempCache.saveInt(instance + "<%=MAP_WRAP_M_MAP_TYPE%>", mapType)
			endfunc
			
			func MapWrap_M_getMapType(String instance)
				int type = TempCache.getInt(instance + "<%=MAP_WRAP_M_MAP_TYPE%>", -1)
				if type != -1
					return type
				else
					System.showErrorMsg("Internal Error. Please restart application and try again")
					return FAIL
				endif 
			endfunc
			
			#######################################
			# Set whether it is showing the map
			#######################################
			func MapWrap_M_isShowingMap(String instance)
				return TempCache.getInt(instance + "<%=MAP_WRAP_M_SHOWING_MAP%>", 0)
			endfunc
			
			func MapWrap_M_setShowingMap(int showing, String instance)
				TempCache.saveInt(instance + "<%=MAP_WRAP_M_SHOWING_MAP%>", showing)
			endfunc
			
			#######################################
			# Set poi array
			#######################################
			func MapWrap_M_getPoiArray(String instance)
				return Cache.getJSONArrayFromTempCache(instance + "<%=MAP_WRAP_M_POI_ARRAY%>")
			endfunc
			
			func MapWrap_M_setPoiArray(JSONArray poiArray, String instance)
				Cache.saveToTempCache(instance + "<%=MAP_WRAP_M_POI_ARRAY%>", poiArray)
			endfunc		
			
			#######################################
			# Set sponsor poi array
			#######################################
			func MapWrap_M_getSponsorArray(String instance)
				return Cache.getJSONArrayFromTempCache(instance + "<%=MAP_WRAP_M_SPONSOR_ARRAY%>")
			endfunc
			
			func MapWrap_M_setSponsorArray(JSONArray poiArray, String instance)
				Cache.saveToTempCache(instance + "<%=MAP_WRAP_M_SPONSOR_ARRAY%>", poiArray)
			endfunc		
			
			#######################################
			# Set sponsor index
			#######################################
			func MapWrap_M_getSponsorIndex(String instance)
				return TempCache.getInt(instance + "<%=MAP_WRAP_M_SPONSOR_INDEX%>", -1)
			endfunc
			
			func MapWrap_M_setSponsorIndex(int index, String instance)
				TempCache.saveInt(instance + "<%=MAP_WRAP_M_SPONSOR_INDEX%>", index)
			endfunc		

			#######################################
			# Set Anchor point for POI Map
			#######################################
			func MapWrap_M_setAnchorPoint(JSONObject stop, String instance)
				Cache.saveToTempCache(instance + "<%=MAP_WRAP_M_POI_ANCHOR_POINT%>", stop)
			endfunc
			
			func MapWrap_M_getAnchorPoint(String instance)
				return Cache.getJSONObjectFromTempCache(instance + "<%=MAP_WRAP_M_POI_ANCHOR_POINT%>")
			endfunc	
			
			#######################################
			# Set poi index
			#######################################
			func MapWrap_M_setPoiIndex(int index, String instance)
				TempCache.saveInt(instance + "<%=MAP_WRAP_M_POI_INDEX%>", index)
			endfunc
			
			func MapWrap_M_getPoiIndex(String instance)
				return TempCache.getInt(instance + "<%=MAP_WRAP_M_POI_INDEX%>", -1)
			endfunc	
			
			#######################################
			# Set whether it is changing location
			#######################################
			func MapWrap_M_isChangingLocation(String instance)
				return TempCache.getInt(instance + "<%=MAP_WRAP_M_CHANGING_LOCATION%>", 0)
			endfunc
			
			func MapWrap_M_setChangingLocation(int changing,String instance)
				TempCache.saveInt(instance + "<%=MAP_WRAP_M_CHANGING_LOCATION%>", changing)
			endfunc		
			
			func MapWrap_M_saveBackAction(String backAction)
	           TxNode backActionNode
	           TxNode.addMsg(backActionNode,backAction)
	           String saveKey = "<%=BACK_ACTION_MAPWRAP%>"
			   Cache.saveToTempCache(saveKey,backActionNode)
	        endfunc
	        
	        func MapWrap_M_getBackAction()
	           String backAction = ""
	           String saveKey = "<%=BACK_ACTION_MAPWRAP%>"
	           TxNode backActionNode = Cache.getFromTempCache(saveKey)
	           if NULL != backActionNode
	              backAction = TxNode.msgAt(backActionNode,0)
	           endif
	           return backAction
	        endfunc
	        
	        func MapWrap_M_deleteBackAction()
	           String saveKey = "<%=BACK_ACTION_MAPWRAP%>"
	           Cache.deleteFromTempCache(saveKey)
	        endfunc

			func MapWrap_M_isSpeedTrapSupported()
		    	int isSupport = 1
		    	TxNode node = System.getServerParam("TRAFFIC_SPEED_TRAP")
		    	if node != NULL
			    	if "0" == TxNode.msgAt(node,0)  
			    		isSupport = 0
			    	endif
		    	endif
		    	
		    	return isSupport
		    endfunc 

			func MapWrap_M_isTrafficCameraSupported()
		    	int isSupport = 1
		    	TxNode node = System.getServerParam("TRAFFIC_CAMERA")
		    	if node != NULL
			    	if "0" == TxNode.msgAt(node,0)  
			    		isSupport = 0
			    	endif
		    	endif
		    	
		    	return isSupport
		    endfunc
		    
		    func MapWrap_M_isTrafficSupported()
			    int isSupport = 1
		    	TxNode node = System.getServerParam("TRAFFIC")
		    	if node != NULL
			    	if "0" == TxNode.msgAt(node,0)  
			    		isSupport = 0
			    	endif
		    	endif
		    	
		    	println("TRAFFIC" + ".................."+isSupport)
		    	return isSupport
			endfunc
		]]>
</tml:script>

<tml:script language="fscript" version="1" feature="<%=FeatureConstant.COMMUTE_ALERTS%>">
		<![CDATA[
			func MapWrap_M_setAlertId(int id, String status, String instance)
					JSONObject jo
					JSONObject.put(jo,"alertId",id)
					JSONObject.put(jo,"status",status)
					String saveKey= instance + "<%=COMMUTE_ALERT_ID%>"
	     			Cache.saveToTempCache(saveKey,jo)
	     	endfunc		
				
			func MapWrap_M_getAlertId(String instance)
				String saveKey= instance + "<%=COMMUTE_ALERT_ID%>"
				JSONObject jo = Cache.getJSONObjectFromTempCache(saveKey)
				return jo
			endfunc
		]]>
</tml:script>


