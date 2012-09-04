<%@ include file="../Header.jsp"%>
<%@page import="java.util.List"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@page import="com.telenav.cserver.util.TnConstants"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<tml:TML outputMode="TxNode">
    <%@ include file="/touch/jsp/ac/controller/AddressCaptureController.jsp"%>
	<%@ include file="/touch/jsp/weather/controller/WeatherController.jsp"%>

	<tml:script language="fscript" version="1">
		<![CDATA[
			
			func onClickDetail()
				TxNode node = ParameterSet.getParam("indexClicked")
				Weather_M_setIndex(node)
				System.doAction("showDetail");
			endfunc
			
			func preLoad()
				display()				
			endfunc			

			func display()		
				int i = 0
				int size = Weather_M_getSize()
				
				string itemId
				while size>i
					showDetail(i)
					i = i + 1
				endwhile
				
				while i < <%=TnConstants.NUM_OF_WEEK%>
					itemId = "item" + i
					Page.setComponentAttribute(itemId,"visible","0")
					i = i + 1
				endwhile
			endfunc
			
			func showDetail(int index)
				JSONArray ja= Weather_M_getWeatherList()
				JSONObject jo = JSONArray.get(ja,index)
				
				string shortWeekDesc = JSONObject.getString(jo,"shortWeekDesc")
				string unit = JSONObject.getString(jo,"unit")
				string title = JSONObject.getString(jo,"title")
				string imageWeatherSmall = "<%=imageCommonUrl%>" + JSONObject.getString(jo,"imageWeatherSmall")
				
				string text = shortWeekDesc + " " + JSONObject.getString(jo,"high") + unit
				text = text + " / " + JSONObject.getString(jo,"low") + unit
				
				string itemId = "item" + index
				Page.setComponentAttribute("title","text",title)
				Page.setComponentAttribute(itemId,"text",text)
				Page.setControlProperty(itemId,"focusIcon",imageWeatherSmall)
				Page.setControlProperty(itemId,"blurIcon",imageWeatherSmall)
			endfunc
			
			func onClickChangeLocation()
	        	JSONObject jo
	        	JSONObject.put(jo,"callbackfunction","CallBack_SelectAddress")
				JSONObject.put(jo,"callbackpageurl","<%=getPageCallBack + "WeatherWeek"%>")

				AddressCapture_C_city(jo)
				return FAIL
	        endfunc
	        
	        
	        func CallBack_SelectAddress()
	        	TxNode addressNode = ParameterSet.getParam("returnAddress")
	        	String joString = TxNode.msgAt(addressNode,0)
				JSONObject joAddress = JSONObject.fromString(joString)
            	JSONObject joParameter
            	JSONObject.put(joParameter,"callbackpageurl","<%=getPageCallBack + "WeatherWeek"%>")
            	JSONObject.put(joParameter,"forwardpageurl","<%=getPageCallBack + "WeatherWeek"%>")					
				Weather_C_show(joAddress,joParameter)
        	endfunc						
		]]>
	</tml:script>

	<tml:menuItem name="showDetail" pageURL="<%=getPage + "WeatherInfo"%>"/>
	<tml:menuItem name="weatherCurrent" pageURL="<%=getPage + "WeatherCurrent"%>"/>
	<tml:menuItem name="changeMenu" onClick="onClickChangeLocation" text="<%=msg.get("common.changeLocation")%>" trigger="KEY_MENU"/>
	<tml:page id="WeatherWeek" url="<%=getPage + "WeatherWeek"%>" type="<%=pageType%>" helpMsg="$//$weatherweek" groupId="<%=GROUP_ID_MISC%>">
		<!-- title --> 
		<tml:title id="title" fontWeight="bold|system_large" align="center" fontColor="white">
		</tml:title>
		<tml:listBox id="weekListBox" name="weekList" isFocusable="true" hotKeyEnable="false">
		<%
		 for ( int i = 0; i < TnConstants.NUM_OF_WEEK; i++ )
		 {
			String menuName = "getInfo" + i;
		%>
		<tml:menuItem name="<%=menuName%>" onClick="onClickDetail" trigger="KEY_RIGHT | TRACKBALL_CLICK">
			<tml:bean name="indexClicked" valueType="int" value="<%=i + ""%>"></tml:bean>
		</tml:menuItem>
		<tml:listItem id="<%="item" + i%>" fontWeight="bold|system_large" align="left">
			<tml:menuRef name='<%=menuName%>'/>
			<tml:menuRef name="changeMenu"/>
		</tml:listItem>
		<% } %>
	</tml:listBox>
	<tml:image id="titleShadow"  visible="true"/>
	</tml:page>
	<cserver:outputLayout/>
</tml:TML>

