<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%@ include file="Header.jsp"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@page import="com.telenav.cserver.util.FeatureConstant"%>
<%@page import="com.telenav.cserver.poi.struts.util.PoiUtil"%>
<%@page import="com.telenav.cserver.util.FeatureUtil"%>
<%
	String pageURL = getPage + "ATTExtras";
	String pageURLCallBack = getPageCallBack + "ATTExtras";
	boolean isSupportWeather = FeatureUtil
			.isSupportWeather(handlerGloble);
%>
<tml:TML outputMode="TxNode">
	<%@include file="ac/model/AddressCaptureModel.jsp"%>
	<%@include file="ac/model/SelectAddressModel.jsp"%>
	<%@ include file="/touch/jsp/weather/controller/WeatherController.jsp"%>
	<tml:script language="fscript" version="1">
		<![CDATA[
			func goFav()
				#Add by ChengBiao, For bug47197. When back from favorite, will use this url and function
				JSONObject jo
	        	JSONObject.put(jo,"callbackfunction","CallBack_MyFav")
				JSONObject.put(jo,"callbackpageurl","<%=pageURLCallBack%>")
				JSONObject.put(jo,"from","ATTExtras")
				SelectAddress_M_saveMaskForFavorite(jo)
				AddressCapture_M_favorites(jo)
				return FAIL
			endfunc
			
			func CallBack_MyFav()
				
			endfunc

			func onClickWeather()
				Weather_C_showCurrent()
				return FAIL
			endfunc
			]]>
	</tml:script>

	<tml:page id="attExtras" url="<%=pageURL%>" type="<%=pageType%>"
		showLeftArrow="true" showRightArrow="true" helpMsg="" groupId="<%=GROUP_ID_COMMOM%>">

		<tml:menuItem name="testMenu" pageURL="" onClick="goFav" />
		<tml:title id="title" align="center|middle"
			fontWeight="bold|system_large" fontColor="white">
			<%=msg.get("Extras.title")%>
		</tml:title>

		<tml:listBox id="menuListBox" name="pageListBox:settingsList"
			isFocusable="true" hotKeyEnable="false">

			<tml:listItem id="item0" fontWeight="bold|system" align="left|middle"
				focusIconImage=""
				blurIconImage=""
				showArrow="true">
				<%=msg.get("selectaddress.favorite") %>
				<tml:menuRef name="testMenu" />
			</tml:listItem>

			<%
				if (isSupportWeather) {
			%>
			<tml:menuItem name="weatherMenu" onClick="onClickWeather" trigger="TRACKBALL_CLICK"/>
			<tml:listItem id="item1" fontWeight="bold|system"
				focusIconImage=""
				blurIconImage=""
				align="left|middle" showArrow="true">
				<%=msg.get("apps.Weather")%>
				<tml:menuRef name="weatherMenu" />
			</tml:listItem>
			<%
				}
			%>
			<tml:block feature="<%=FeatureConstant.MOVIE%>">
				<tml:menuItem name="movieMenu"	pageURL="<%=movieURL%>" />
				<tml:listItem id="item2" fontWeight="bold|system" align="left|middle"
					focusIconImage=""
					blurIconImage=""
					showArrow="true">
					<%=msg.get("apps.Movies")%>
					<tml:menuRef name="movieMenu" />
				</tml:listItem>
			</tml:block>


		</tml:listBox>
		<tml:image id="titleShadow" url=""   visible="true" align="left|top"></tml:image>
	</tml:page>
	<cserver:outputLayout />
</tml:TML>