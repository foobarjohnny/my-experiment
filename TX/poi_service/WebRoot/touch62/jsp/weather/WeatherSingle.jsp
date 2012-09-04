<%@ include file="../Header.jsp"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.telenav.cserver.util.TnUtil"%>
<%@page import="com.telenav.tnbrowser.util.DataHandler"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@page import="com.telenav.cserver.weather.struts.datatype.WeatherInfo"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%
	DataHandler handler = (DataHandler) request.getAttribute("DataHandler");
%>
<tml:TML outputMode="TxNode">
    <%@ include file="/touch62/jsp/ac/controller/AddressCaptureController.jsp"%>
	<%@ include file="/touch62/jsp/weather/controller/WeatherController.jsp"%>
	
	<tml:script language="fscript" version="1">
		<![CDATA[
			func onClickChangeLocation()
	        	JSONObject jo
	        	JSONObject.put(jo,"callbackfunction","CallBack_SelectAddress")
				JSONObject.put(jo,"callbackpageurl","<%=getPageCallBack + "WeatherSingle"%>")
				
				AddressCapture_C_city(jo)
				return FAIL
	        endfunc
	        
	        func CallBack_SelectAddress()
	        	TxNode addressNode = ParameterSet.getParam("returnAddress")
	        	String joString = TxNode.msgAt(addressNode,0)
				JSONObject joAddress = JSONObject.fromString(joString)
            	JSONObject joParameter
            	JSONObject.put(joParameter,"callbackpageurl","<%=getPageCallBack + "WeatherSingle"%>")
            	JSONObject.put(joParameter,"forwardpageurl","<%=getPageCallBack + "WeatherSingle"%>")				
				Weather_C_show(joAddress,joParameter)
        	endfunc	
        	
        	func onShow()
				int offset = displayCurrentDay()
				displayWeekly(offset)
        	endfunc
        	
        	func displayCurrentDay()
        		JSONObject jo= Weather_M_getWeatherCurrent()
        		
        		string title = JSONObject.getString(jo,"titleWithoutState")
        		string todayDate = JSONObject.getString(jo,"todayDate")
        		string status = JSONObject.getString(jo,"status")
        		string temp = JSONObject.getString(jo,"temp")
        		string unit = JSONObject.getString(jo,"unit")
        		string feel = JSONObject.getString(jo,"feel")
        		string feelLabel = "<%=msg.get("weather.feelsLike")%>" + " <bold>" + feel + "</bold>"
        		string wind = JSONObject.getString(jo,"wind")
        		string windLabel = "<%=msg.get("weather.wind")%>" + " <bold>" + wind + "</bold>"
        		string humidity = JSONObject.getString(jo,"humidity")
        		string humidityLabel = "<%=msg.get("weather.humidity")%>" + " <bold>" + humidity + "%</bold>"
        		
        		string imageWeatherBig = "<%=imageCommonUrl%>" + JSONObject.getString(jo,"imageWeatherBig")
        		string tempCodeImage = "<%=imageCommonUrl%>" + JSONObject.getString(jo,"tempCodeImage")

        		Page.setComponentAttribute("titleLabel","text",title)
        		Page.setComponentAttribute("todayDate","text",todayDate)
        		Page.setComponentAttribute("status","text",status)
        		Page.setComponentAttribute("temp","text",temp + " " + unit)
        		Page.setComponentAttribute("feelsLike","text",feelLabel)
        		Page.setComponentAttribute("wind","text",windLabel)
        		Page.setComponentAttribute("humidity","text",humidityLabel)
        		
        		Page.setControlProperty("imageWeather","image",imageWeatherBig)
        		# Page.setControlProperty("tempImage","image",tempCodeImage)        	
 
 
         		
        		JSONArray ja= Weather_M_getWeatherList()
				JSONObject joFirstDay = JSONArray.get(ja,0)
        		String today = JSONObject.getString(jo,"shortWeekDesc")
				String firstDay= JSONObject.getString(joFirstDay,"shortWeekDesc")	
				if today == firstDay
	        		string highandlowLabel = JSONObject.getString(joFirstDay,"high") + unit + "  " + JSONObject.getString(joFirstDay,"low") + unit
	        		Page.setComponentAttribute("highandlow","text",highandlowLabel)
	        		return 0
	        	else
	        		return 1
	        	endif		
        				
        	endfunc

			func displayWeekly(int offset)		
				int i = 1
				int size = Weather_M_getSize()
				
				while size>i
					showDetail(i, offset)
					i = i + 1
				endwhile
			endfunc
			
			#use offset to define the start date of weekly forecaset because:
			#When requesting from daytime, the forecast list starts from today
			#When requesting from night, the forecast list starts from next day
			
			func showDetail(int index, int offset)
				JSONArray ja= Weather_M_getWeatherList()
				JSONObject jo = JSONArray.get(ja,index-offset)
				
				string unit = JSONObject.getString(jo,"unit")
				string highLabel =  JSONObject.getString(jo,"high") + unit
				string lowLabel =  JSONObject.getString(jo,"low") + unit
				string longWeekDesc = JSONObject.getString(jo,"shortWeekDesc")				
				string imageWeatherSmall = "<%=imageCommonUrl%>" + JSONObject.getString(jo,"imageWeatherSmall")
				

				Page.setComponentAttribute("itemDate" + index,"text",longWeekDesc)
				Page.setComponentAttribute("itemHigh" + index,"text",highLabel)
				Page.setComponentAttribute("itemLow" + index,"text",lowLabel)
								
				Page.setControlProperty("itemImage" + index,"image",imageWeatherSmall)
			endfunc

        	func onBack()
			   String backAction = Weather_M_getBackAction()
               if "" != backAction
                  if "<%=Constant.BACK_ACTION_MAIN_SCREEN%>" == backAction
                     Weather_M_deleteBackAction()
                     System.doAction("home")
                  else
                     System.quit()
                  endif
               else
		          System.back()
               endif
               return FAIL
			endfunc
		]]>
		</tml:script>
	<tml:menuItem name="home" pageURL="<%=host + "/startUp.do?pageRegion=" + region%>">
	</tml:menuItem>
	<tml:page id="WeatherSingle" url="<%=getPage + "WeatherSingle"%>" type="<%=pageType%>"  helpMsg="$//$weather" groupId="<%=GROUP_ID_MISC%>">
		<tml:menuItem name="changeMenu" onClick="onClickChangeLocation" trigger="KEY_MENU"  text="<%=msg.get("common.changeLocation")%>"/>

		<!-- title --> 
		<tml:title id="titleLabel" fontWeight="bold|system_large" align="center" fontColor="white"/>

		<!-- background image -->
		<tml:image id="listBg"  align="left|top"/>

		<tml:label id="todayDate" fontWeight="system_large" align="left" />

		<tml:image id="imageWeather" url="" align="left|top"/>

		<!-- new line -->		
	 	<%-- tml:image id="tempImage" url="" text="" / --%>
	 	<tml:label id="temp" fontWeight="bold" align="left"/>
	 	<!-- new line -->
	 	<tml:label id="highandlow" fontWeight="bold|system_large" align="left"/>
		<!-- new line -->
		<tml:label id="status" fontWeight="bold|system_large" align="left"/>	
		<!-- new line -->
		<tml:label id="feelsLike" fontWeight="system_large" align="left" textWrap="wrap"/>
		<!-- new line -->
		<tml:label id="humidity" fontWeight="system_large" align="left"/>	
		<!-- new line -->
		<tml:label id="wind" fontWeight="system_large" align="left"/>	
		<!-- new line -->
		<tml:menuRef name="changeMenu"/>

				
		<%
		 for ( int i = 1; i < TnConstants.NUM_OF_WEEK; i++ )
		 {
		%>
				
				<tml:label id="<%="itemDate" + i%>"   fontWeight="bold|system_large" textWrap="ellipsis" align="left|middle" fontColor="white"></tml:label>
				<tml:label id="<%="itemHigh" + i%>"   fontWeight="system_large" textWrap="ellipsis" align="left|middle" fontColor="white"></tml:label>
				<tml:label id="<%="itemLow" + i%>"    fontWeight="system_large" textWrap="ellipsis" align="left|middle" fontColor="white"></tml:label>
				<tml:image id="<%="itemImage" + i%>" url="" align="left|middle"/>
			
		<% } %>
		<tml:image id="titleShadow" url="" visible="true" align="left|top"/>

	</tml:page>
	<cserver:outputLayout/>
</tml:TML>

