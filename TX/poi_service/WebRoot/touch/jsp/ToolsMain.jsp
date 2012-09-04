<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%@ include file="Header.jsp"%>
<%@page import="com.telenav.cserver.util.TnUtil"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@page import="com.telenav.cserver.util.FeatureConstant"%>
<%@page import="com.telenav.cserver.poi.struts.util.PoiUtil"%>
<%@page import="com.telenav.cserver.util.FeatureUtil"%>
<%
	String pageURL = getPage + "ToolsMain";
	String postLocationURL = "{postlocation.http}/" + ClientHelper.getModuleNameForPostLocation(handlerGloble) + "/goToJsp.do?pageRegion=" + region + "&amp;jsp=StartUp";
	String restaurantURL      = "{localappfwk.http}/"+ ClientHelper.getModuleNameForPostLocation(handlerGloble) +"/goToJsp.do?pageRegion="  + region + "&amp;appId=RESTAURANT&amp;jsp=StartUp";


%>
<tml:TML outputMode="TxNode">
<%@ include file="/touch/jsp/weather/controller/WeatherController.jsp"%>

	<tml:script language="fscript" version="1">
		<![CDATA[
		    <%@ include file="GetServerDriven.jsp"%>
		    func preLoad()
		       if ServerDriven_CanCommuteAlert()
		          Page.setComponentAttribute("item2","visible","1")
		       else
		          Page.setComponentAttribute("item2","visible","0")
		       endif
		       
		       if ServerDriven_CanWeather()
	        	  Page.setComponentAttribute("item1","visible","1")
	           else
	              Page.setComponentAttribute("item1","visible","0")
	           endif
	           if ServerDriven_CanLocalAppRestaurant()
		          Page.setComponentAttribute("item4","visible","1")
		       else
		          Page.setComponentAttribute("item4","visible","0")
		       endif
		    endfunc
		    
			func onClickWeather()
				Weather_C_showCurrent()
				return FAIL
			endfunc
		]]>
		</tml:script>
		
		<%
		String helpMsg="";
		if (TnUtil.isBell_VMC(handlerGloble) && "9800_Mp3".equals(handlerGloble.getClientInfo(DataHandler.KEY_DEVICEMODEL)))
			helpMsg="$//$localapps";
		 else
			helpMsg="$//$apps";
		%>
		
	<tml:page id="localApps" url="<%=pageURL%>" type="<%=pageType%>"
		showLeftArrow="true" showRightArrow="true"  helpMsg="<%=helpMsg %>" groupId="<%=GROUP_ID_MISC%>">

		<tml:title id="title" align="center|middle"
			fontWeight="bold|system_large" fontColor="white">
			<%=msg.get("apps.title")%>
		</tml:title>

		<tml:listBox id="menuListBox" name="pageListBox:settingsList"
			isFocusable="true" hotKeyEnable="false">
			<tml:block feature="<%=FeatureConstant.MOVIE%>">
				<tml:menuItem name="movieMenu" pageURL="<%=movieURL%>" />
				<tml:listItem id="item0" fontWeight="bold|system_large"
					focusIconImage=""
					blurIconImage=""
					align="left|middle" showArrow="true">

					<%=msg.get("apps.Movies")%>
					<tml:menuRef name="movieMenu" />
				</tml:listItem>
			</tml:block>

			<tml:menuItem name="weatherMenu" onClick="onClickWeather" trigger="TRACKBALL_CLICK" />
			<tml:listItem id="item1" fontWeight="bold|system_large"
				focusIconImage=""
				blurIconImage=""
				align="left|middle" showArrow="true">
				<%=msg.get("apps.Weather")%>
				<tml:menuRef name="weatherMenu" />
			</tml:listItem>

			<tml:block feature="<%=FeatureConstant.COMMUTE_ALERTS%>">
				<tml:menuItem name="commuteAlert" pageURL="<%=cAlertURL%>"></tml:menuItem>
				<tml:listItem id="item2" fontWeight="bold|system_large"
					focusIconImage=""
					blurIconImage=""
					showArrow="true">
					<%=msg.get("apps.CommuteAlerts")%>
					<tml:menuRef name="commuteAlert"></tml:menuRef>
				</tml:listItem>
			</tml:block>
			
			<tml:block feature="<%=FeatureConstant.POST_LOCATION%>">
				<tml:menuItem name="postLocation" pageURL="<%=postLocationURL%>"></tml:menuItem>
				<tml:listItem id="item3" fontWeight="bold|system_large"
					focusIconImage=""
					blurIconImage=""
				showArrow="true">
					<%=msg.get("apps.PostLocation")%>
					<tml:menuRef name="postLocation"></tml:menuRef>
				</tml:listItem>
			</tml:block>
			
			
			<tml:block feature="<%=FeatureConstant.RESTAURANT_LOCALAPPS%>">
				<tml:menuItem name="restaurantMenu" pageURL="<%=restaurantURL%>" />
				<tml:listItem id="item4" fontWeight="bold|system_large"
					blurBgImage=""
					focusBgImage=""
					focusIconImage=""
					blurIconImage=""
					showArrow="true">
					<%=msg.get("apps.Restaurant")%>
					<tml:menuRef name="restaurantMenu" />
				</tml:listItem>
			</tml:block>
		</tml:listBox>
		<tml:image id="titleShadow" url=""   visible="true" align="left|top"/>
	</tml:page>
	<cserver:outputLayout />
</tml:TML>
