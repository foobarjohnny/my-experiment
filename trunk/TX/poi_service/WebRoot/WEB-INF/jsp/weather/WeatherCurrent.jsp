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
    <%@ include file="/WEB-INF/jsp/ac/controller/AddressCaptureController.jsp"%>
	<%@ include file="/WEB-INF/jsp/weather/controller/WeatherController.jsp"%>
	
	<tml:script language="fscript" version="1">
		<![CDATA[
			func onClickChangeLocation()
	        	JSONObject jo
	        	JSONObject.put(jo,"callbackfunction","CallBack_SelectAddress")
				JSONObject.put(jo,"callbackpageurl","<%=getPageCallBack + "WeatherCurrent"%>")
				
				AddressCapture_C_city(jo)
				return FAIL
	        endfunc
	        
	        func CallBack_SelectAddress()
	        	TxNode addressNode = ParameterSet.getParam("returnAddress")
	        	String joString = TxNode.msgAt(addressNode,0)
				JSONObject joAddress = JSONObject.fromString(joString)
            	JSONObject joParameter
            	JSONObject.put(joParameter,"callbackpageurl","<%=getPageCallBack + "WeatherCurrent"%>")
            	JSONObject.put(joParameter,"forwardpageurl","<%=getPageCallBack + "WeatherCurrent"%>")				
				Weather_C_show(joAddress,joParameter)
        	endfunc	
        	
        	func onShow()
        		JSONObject jo= Weather_M_getWeatherCurrent()
        		
        		string title = JSONObject.getString(jo,"title")
        		string status = JSONObject.getString(jo,"status")
        		string temp = JSONObject.getString(jo,"temp")
        		string unit = JSONObject.getString(jo,"unit")
        		string feel = JSONObject.getString(jo,"feel")
        		string feelLabel = "<%=msg.get("weather.feelsLike")%>" + " <bold>" + feel + "</bold>"
        		string wind = JSONObject.getString(jo,"wind")
        		string windLabel = "<%=msg.get("weather.wind")%>" + " <bold>" + wind + "</bold>"
        		string humidity = JSONObject.getString(jo,"humidity")
        		string humidityLabel = "<%=msg.get("weather.humidity")%>" + " <bold>" + humidity + "%</bold>"
        		
        		string imageWeatherBig = "<%=imageUrl%>" + JSONObject.getString(jo,"imageWeatherBig")
        		string tempCodeImage = "<%=imageUrl%>" + JSONObject.getString(jo,"tempCodeImage")
        		
        		Page.setComponentAttribute("title","text",title)
        		Page.setComponentAttribute("status","text",status)
        		Page.setComponentAttribute("temp","text",temp + " " + unit)
        		Page.setComponentAttribute("feelsLike","text",feelLabel)
        		Page.setComponentAttribute("wind","text",windLabel)
        		Page.setComponentAttribute("humidity","text",humidityLabel)
        		
        		Page.setControlProperty("imageWeather","image",imageWeatherBig)
        		Page.setControlProperty("tempImage","image",tempCodeImage)
        	endfunc
        	
        	func onClickWeekForecast()
        		System.doAction("weekForecastAction")
        		return FAIL
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
	<tml:menuItem name="weekForecastAction" pageURL="<%=getPage + "WeatherWeek"%>" trigger="TRACKBALL_CLICK"/>
	
	<tml:page id="WeatherCurrent" url="<%=getPage + "WeatherCurrent"%>" type="<%=pageType%>"   background="<%=imageUrl + "backgroud.png"%>" helpMsg="$//$weather" groupId="<%=GROUP_ID_MISC%>">
		<tml:menuItem name="change" onClick="onClickChangeLocation" trigger="TRACKBALL_CLICK"/>
		<tml:menuItem name="changeMenu" onClick="onClickChangeLocation" trigger="KEY_MENU"  text="<%=msg.get("common.changeLocation")%>"/>
		
		<tml:menuItem name="weekForecast" onClick="onClickWeekForecast" trigger="TRACKBALL_CLICK"/>	
		<tml:menuItem name="weekForecastMenu" pageURL="<%=getPage + "WeatherWeek"%>" trigger="KEY_MENU" text="<%=msg.get("weather.weekForecast")%>"/>
		<!-- title --> 
		<tml:title id="title" fontWeight="bold|system_large" align="center" fontColor="white">
		</tml:title>
		<tml:image id="imageWeather" url="" />
		<!-- new line -->
	 	<tml:label id="temp" fontWeight="bold" align="left">
		</tml:label>
	 	<tml:image id="tempImage" url=""
			text="" />
		<!-- new line -->
		<tml:label id="status" fontWeight="bold|system_large" align="left">
		</tml:label>	
		<!-- new line -->
		<tml:label id="feelsLike" fontWeight="system_large" align="left" textWrap="wrap">
		</tml:label>
		<!-- new line -->
		<tml:label id="wind" fontWeight="system_large" align="left">
		</tml:label>
		<!-- new line -->
		<tml:label id="humidity" fontWeight="system_large" align="left">
		</tml:label>			
		<!-- new line -->
		
		<tml:listBox id="menuListBox" name="pageListBox:settingsList"
			isFocusable="true" hotKeyEnable="false">
			<tml:compositeListItem id="item0" visible="true" bgColor="#FFFFFF" isFocusable="true"
				transparent="false" focusBgImage="<%=imageUrl + "list_bg_highlight_45px.png"%>"
				blurBgImage="<%=imageUrl + "list_bg_45px.png"%>">
				<tml:label id="itemlabel0" focusFontColor="white" 
					fontWeight="bold|system_large" textWrap="ellipsis" align="left|middle">
					<%=msg.get("weather.weekForecast")%>
				</tml:label>
				<tml:menuRef name="weekForecast"/>
				<tml:menuRef name="weekForecastMenu"/>
				<tml:menuRef name="changeMenu"/>
			</tml:compositeListItem>
			<tml:compositeListItem id="item1" visible="true" bgColor="#FFFFFF" isFocusable="true"
				transparent="false" focusBgImage="<%=imageUrl + "list_bg_highlight_45px.png"%>"
				blurBgImage="<%=imageUrl + "list_bg_45px.png"%>">
				<tml:label id="itemlabel1" focusFontColor="white" 
					fontWeight="bold|system_large" textWrap="ellipsis" align="left|middle">
					<%=msg.get("common.changeLocation")%>
				</tml:label>
				<tml:menuRef name="change"/>
				<tml:menuRef name="weekForecastMenu"/>
				<tml:menuRef name="changeMenu"/>
			</tml:compositeListItem>
		</tml:listBox>
		
		
	</tml:page>
	<cserver:outputLayout/>
</tml:TML>

