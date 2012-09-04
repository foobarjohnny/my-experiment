<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.cserver.browser.framework.BrowserFrameworkConstants"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%
	 final int DEFAULT_TIME_OUT=3000;
%>
<tml:script	language="fscript" version="1">

	<![CDATA[
		 func GetGPS_M_saveCallbackfunction(string text)
	    	TxNode node
	    	TxNode.addMsg(node,text) 
	        Cache.saveToTempCache("<%=Constant.StorageKey.GETGPS_CALL_BACK_FUNCTION%>",node)
	    endfunc

	    func GetGPS_M_getCallbackfunction()
	        TxNode node = Cache.getFromTempCache("<%=Constant.StorageKey.GETGPS_CALL_BACK_FUNCTION%>")
	        if node == NULL
	        	return ""
	        else
	        	return TxNode.msgAt(node,0)
	        endif  
	    endfunc
	    
		func getCurrentLocation(int type, int gpstime, int netTime, int timeout)
			GetGPS_M_saveCallbackfunction("")
			JSONObject locParm
			JSONObject.put(locParm,"LocationType",type)
			JSONObject.put(locParm,"GpsLocationValidTime",gpstime*1000)
			JSONObject.put(locParm,"NetworkLocationValidTime",netTime*1000)
			JSONObject.put(locParm,"Timeout", timeout*1000)
			TxNode locParmNode
			TxNode.addMsg(locParmNode,""+locParm)
			MenuItem.setBean("doGetGps","locParam",locParmNode)
			 System.doAction("doGetGps")
		endfunc

		func getCurrentLocationWithoutBlocking(int type, int gpstime, int netTime, int timeout)
			GetGPS_M_saveCallbackfunction("")
			JSONObject locParm
			JSONObject.put(locParm,"LocationType",type)
			JSONObject.put(locParm,"GpsLocationValidTime",gpstime*1000)
			JSONObject.put(locParm,"NetworkLocationValidTime",netTime*1000)
			JSONObject.put(locParm,"Timeout", timeout*1000)
			TxNode locParmNode
			TxNode.addMsg(locParmNode,""+locParm)
			MenuItem.setBean("doGetNonBlockingGPS","locParam",locParmNode)
			System.doAction("doGetNonBlockingGPS")
		endfunc
		
		func getGPSNoBlockingWithCallback(int type, int gpstime, int netTime, int timeout, String callbackfunction)
			GetGPS_M_saveCallbackfunction(callbackfunction)
			JSONObject locParm
			JSONObject.put(locParm,"LocationType",type)
			JSONObject.put(locParm,"GpsLocationValidTime",gpstime*1000)
			JSONObject.put(locParm,"NetworkLocationValidTime",netTime*1000)
			JSONObject.put(locParm,"Timeout", timeout*1000)
			TxNode locParmNode
			TxNode.addMsg(locParmNode,""+locParm)
			MenuItem.setBean("doGetNonBlockingGPS","locParam",locParmNode)
			System.doAction("doGetNonBlockingGPS")
		endfunc
					
		func returnCurrentLocation()
			TxNode currentLocationNode = ParameterSet.getParam("currentLocation")
        	JSONObject stop 
			 String joStr =TxNode.msgAt(currentLocationNode,0)
			 JSONObject joStop = JSONObject.fromString(joStr)
			 String callbackId = JSONObject.get(joStop,"CallbackID")
			 
			 if(callbackId!="Success")
				handleGpsError(callbackId)
			 else
				 String location = JSONObject.getString(joStop,"Location")
				 JSONObject gps= JSONObject.fromString(location)
				 
				 JSONObject.put(stop,"type",6)	
				 JSONObject.put(stop,"lat",JSONObject.get(gps,"Lat"))
				 JSONObject.put(stop,"lon",JSONObject.get(gps,"Lon"))
				 JSONObject.put(stop,"label","")
				 JSONObject.put(stop,"firstLine","")
				 JSONObject.put(stop,"city","")
				 JSONObject.put(stop,"state","")
				 JSONObject.put(stop,"country","")
				 JSONObject.put(stop,"zip","")
				 
				 String callbackflag = GetGPS_M_getCallbackfunction()
				 if "OneBoxSearch" == callbackflag
				 	setCurrentLocationForOneBoxSearch(stop)
				 else
				 	setCurrentLocation(stop)
				 endif				
			 endif
		endfunc
			
		func handleGpsError(String callbackId)
			System.showGeneralMsg(NULL,"<%=msg.get("selectaddress.gps.error")%>","<%=msg.get("common.button.OK")%>",NULL,NULL,"CallBack_GPS_Error")
			return FAIL		
		endfunc	
			
		]]>
</tml:script>

<tml:actionItem name="getGPS" action="getGPS"
	progressBarText="<%=msg.get("common.readinggps") %>">
	<tml:input name="locParam"></tml:input>
	<tml:output name="currentLocation" />
</tml:actionItem>
<tml:menuItem actionRef="getGPS" name="doGetGps"
	onClick="returnCurrentLocation">
</tml:menuItem>

<tml:actionItem name="getNonBlockingGPS" action="<%=BrowserFrameworkConstants.LOCALSERVICE_GETNONBLOCKINGGPS%>">
	<tml:input name="locParam"></tml:input>
	<tml:output name="currentLocation" />
</tml:actionItem>
<tml:menuItem actionRef="getNonBlockingGPS" name="doGetNonBlockingGPS"
	onClick="returnCurrentLocation">
</tml:menuItem>
