<%@ include file="../Header.jsp"%>
<%@page import="java.util.List"%>
<%@page import="com.telenav.tnbrowser.util.DataHandler"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%
%>
<tml:TML outputMode="TxNode">
	<%@ include file="model/WeatherModel.jsp"%>
	<tml:script language="fscript" version="1">
		<![CDATA[
			func preLoad()
				display()
			endfunc

			func display()		
				JSONArray ja= Weather_M_getWeatherList()
				int index = Weather_M_getIndex()
				JSONObject jo = JSONArray.get(ja,index)
				showDetail(jo)
				
				# Size
				int size = Weather_M_getSize()
				showArrow(size,index)
			endfunc
			
			func onLoad()
				int index = Weather_M_getIndex()
				int size = Weather_M_getSize()
				showArrow(size,index)
			endfunc
						
			func showDetail(JSONObject jo)
			
				string unit = JSONObject.getString(jo,"unit")
				string highLabel = "<%=msg.get("weather.high")%>" + " " + JSONObject.getString(jo,"high") + unit
				string lowLabel = "<%=msg.get("weather.low")%>" + " " + JSONObject.getString(jo,"low") + unit
				string status = JSONObject.getString(jo,"status")
				string longWeekDesc = JSONObject.getString(jo,"longWeekDesc")				
				string imageWeatherBig = "<%=imageUrl%>" + JSONObject.getString(jo,"imageWeatherBig")
				string title = JSONObject.getString(jo,"title")
				
				Page.setComponentAttribute("title","text",title)
				Page.setComponentAttribute("high","text",highLabel)
				Page.setComponentAttribute("low","text",lowLabel)
				Page.setComponentAttribute("status","text",status)
				Page.setComponentAttribute("longWeekDesc","text",longWeekDesc)
				
				Page.setControlProperty("imageWeather","image",imageWeatherBig)
			endfunc

			func showNext()
				int size =  Weather_M_getSize()
				int index = Weather_M_getIndex()
				if 0 >= size
					return FAIL
				endif
				if index >= size - 1
					return FAIL
				endif
				
				index = index + 1
				TxNode newIndexNode
				TxNode.addValue(newIndexNode, index)
				Weather_M_setIndex(newIndexNode)
				display()
			endfunc

			func showPrevious()
	            int size =  Weather_M_getSize()
				int index = Weather_M_getIndex()
				if 0 >= size
					return FAIL
				endif
				if 0 >= index
					return FAIL
				endif
				
				index = index - 1
				TxNode newIndexNode
				TxNode.addValue(newIndexNode, index)
				Weather_M_setIndex(newIndexNode)
				
				display()
			endfunc
			
			func showArrow(int size,int index)
			    #Set previous and next
				if 0 < size
					if index < size - 1
					    Page.setShowArrow("forward","true")
					else
					    Page.setShowArrow("forward","false")
					endif
					if index > 0
					    Page.setShowArrow("back","true")
					else
					    Page.setShowArrow("back","false")
					endif
				endif
			endfunc		
			
			func returnBack()
				System.back()
				return FAIL
			endfunc		
		]]>
	</tml:script>

	<tml:page id="WeatherInfo" url="<%=getPage + "WeatherInfo"%>" type="<%=pageType%>" groupId="<%=GROUP_ID_MISC%>"  helpMsg="$//$weatherweek"
		showLeftArrow="true" showRightArrow="true" background="<%=imageUrl + "backgroud.png"%>">
		<tml:menuItem name="previous" onClick="showPrevious" trigger="KEY_LEFT"/>
		<tml:menuItem name="next" onClick="showNext" trigger="KEY_RIGHT"/>
		<tml:menuItem name="back" onClick="returnBack" trigger="KEY_MENU" text="<%=msg.get("common.button.Back")%>"/>
		<tml:menuRef name="previous" />
		<tml:menuRef name="next" />
		<tml:menuRef name="back" />
		<!-- title --> 
		<tml:title id="title" fontWeight="bold|system_large" align="center" fontColor="white">
		</tml:title>
		<tml:label id="longWeekDesc" fontWeight="bold|system_large" align="left"/>
		<tml:label id="high" fontWeight="bold|system_large" align="left"/>
		<tml:label id="low"  fontWeight="bold|system_large" align="left"/>
		<tml:label id="status" fontWeight="system_large" align="left|top"/>		
		<tml:image id="imageWeather" text="weather" />
	</tml:page>
	<cserver:outputLayout/>
</tml:TML>

