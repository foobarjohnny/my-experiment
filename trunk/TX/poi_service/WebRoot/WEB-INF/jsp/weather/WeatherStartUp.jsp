<%@page import="com.telenav.tnbrowser.util.DataHandler"%>
<%@page import="com.telenav.cserver.util.TnUtil"%>
<%@ include file="../Header.jsp"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<tml:TML outputMode="TxNode">
	<%@ include file="model/WeatherModel.jsp"%>
	<%
		boolean isCanadianCarrier = TnUtil.isCanadianCarrier(handlerGloble);
	%>
	<tml:script language="fscript" version="1">
		<![CDATA[
		func preLoad()
			fetchWeatherAjax()
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
			string isCanadianCarrier = "<%=isCanadianCarrier%>"
			if isCanadianCarrier == "true"
				string url="<%=host + "/I18NWeather.do"%>"
			else
				string url="<%=host + "/Weather.do"%>"
			endif
			string scriptName="Callback_FetchWeatherAjax"
			TxRequest.open(req,url)
			TxRequest.setRequestData(req,node)
			TxRequest.onStateChange(req,scriptName)
			TxRequest.setProgressTitle(req,"<%=msg.get("common.loading")%>","Cancel_Weather_Ajax")
			TxRequest.send(req)        
        endfunc
        
        func Cancel_Weather_Ajax()
        	string backUrl = Weather_M_getCallbackpageurl()
        	if "" == backUrl
        		System.back()
			else
				MenuItem.setAttribute("callback","url",backUrl)
	        	System.doAction("callback")			
			endif
			return FAIL	
        endfunc
        
        func Callback_FetchWeatherAjax(TxNode node,int status)
            if status == 0
                System.showGeneralMsg(NULL,"<%=msg.get("error.not.available")%>","OK",NULL,NULL,"CallBack_Popup")
			    return FAIL
			elseif status == 1
				JSONObject jo= JSONObject.fromString(TxNode.msgAt(node,0))
				JSONArray ja= JSONArray.fromString(TxNode.msgAt(node,1))
					
				Weather_M_setWeatherCurrent(jo)
				Weather_M_setWeatherList(ja)
				MenuItem.setAttribute("callback","url",Weather_M_getForwardpageurl())
	        	System.doAction("callback")
	        	return FAIL
			endif
        endfunc

		func CallBack_Popup(int param)
		endfunc	
	
		func onResume()
			Cancel_Weather_Ajax()
		endfunc
		]]>
		</tml:script>
	<tml:menuItem name="callback" pageURL="" trigger="TRACKBALL_CLICK"/>
	<tml:page id="WeatherStartUp" url="<%=getPage + "WeatherStartUp"%>" type="<%=pageType%>"  supportback="false" 
		groupId="<%=GROUP_ID_MISC%>">
	</tml:page>
</tml:TML>

