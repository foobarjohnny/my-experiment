<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>	
<%@ include file="../model/WeatherModel.jsp"%>
<%@ include file="/WEB-INF/jsp/StopUtil.jsp"%>
<%@ include file="/WEB-INF/jsp/local_service/GetGps.jsp"%>
		
<tml:script language="fscript" version="1">
	<![CDATA[
         func Weather_C_showCurrent()
            TxNode node = Gps.getLastKnownLocation()
            if node == NULL || TxNode.getValueSize(node) ==0 
            	getCurrentLocation(<%=Constant.CurrentLocation.CURRENT_LOCATION%>,60,180,5)
            else
	            JSONObject joParameter
	            JSONObject.put(joParameter,"callbackpageurl","")
	            JSONObject.put(joParameter,"forwardpageurl","")
	            
	            JSONObject jo = convertStopToJSON(node)
	            Weather_C_show(jo,joParameter)
         	endif
         endfunc
         
         func Weather_C_show(JSONObject jo,JSONObject joParameter)
			if !Cell.isCoverage()
				System.showErrorMsg("<%=msg.get("common.nocell.error")%>")
	            return FAIL
			endif
            Weather_M_setLocation(jo)
			Weather_M_saveParameter(joParameter)
            fetchWeatherAjax()
         endfunc
                
        func setCurrentLocation(JSONObject jo)
	        JSONObject joParameter
	        JSONObject.put(joParameter,"callbackpageurl","")
	        JSONObject.put(joParameter,"forwardpageurl","")			
			Weather_C_show(jo,joParameter)
			return FAIL
        endfunc
 
         func fetchWeatherAjax()
            TxNode node
			JSONObject addressJO = Weather_M_getLocation()
			
			String addressString = ""
			if NULL != addressJO
			       addressString = JSONObject.toString(addressJO)
			endif
						
			JSONObject jo
            JSONObject.put(jo,"addressString",addressString)
            String strJo=JSONObject.toString(jo)
            TxNode.addMsg(node,strJo)
			TxRequest req
			string url="<%=host + "/Weather.do"%>"
			string scriptName="Callback_FetchWeatherAjax"
			TxRequest.open(req,url)
			TxRequest.setRequestData(req,node)
			TxRequest.onStateChange(req,scriptName)
			TxRequest.send(req)        
        endfunc
        
        func Callback_FetchWeatherAjax(TxNode node,int status)
            if status == 0
                System.showGeneralMsg(NULL,"<%=msg.get("error.not.available")%>","OK",NULL,NULL,"CallBack_GPS_Error")
			    return FAIL
			elseif status == 1
				JSONObject jo= JSONObject.fromString(TxNode.msgAt(node,0))
				JSONArray ja= JSONArray.fromString(TxNode.msgAt(node,1))
					
				Weather_M_setWeatherCurrent(jo)
				Weather_M_setWeatherList(ja)
				
				displayMiniWeather()
	        	return FAIL
			endif
        endfunc
    ]]>
</tml:script>		