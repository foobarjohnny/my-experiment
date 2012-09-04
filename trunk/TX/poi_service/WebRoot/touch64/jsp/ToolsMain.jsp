<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>
<%@ include file="Header.jsp"%>
<%@page import="com.telenav.cserver.poi.struts.Constant"%>
<%@page import="com.telenav.cserver.poi.struts.util.PoiUtil"%>
<%@page import="com.telenav.cserver.util.TnUtil"%>
<%
	String pageURL = getPage + "ToolsMain";
	String postLocationURL = "{postlocation.http}/" + ClientHelper.getModuleNameForPostLocation(handlerGloble) + "/goToJsp.do?pageRegion=" + region + "&amp;jsp=StartUp";
	//FIXME getModuleNameForPostLocation TO getModuleNameForRestaurant
	String restaurantURL = "{localappfwk.http}/touch/goToJsp.do?pageRegion=" + region + "&amp;appId=RESTAURANT&amp;jsp=StartUp";
	String MyMileageURL     = "{localappfwk.http}/touch/goToJspGlobal.do?pageRegion="  + region + "&amp;jsp=MyMileageAboutPhase2";
	String MyMileageHomeURL     = "{localappfwk.http}/touch/goToJspGlobal.do?pageRegion="  + region + "&amp;jsp=MyMileageHome";

	String appTitle = msg.get("apps.title");
	if(TnUtil.isATTRIM63(handlerGloble)) {
	    appTitle = msg.get("Extras.title");
	}
%>
<tml:TML outputMode="TxNode">
<%@ include file="/touch64/jsp/weather/controller/WeatherController.jsp"%>

	<tml:script language="fscript" version="1">
		<![CDATA[
		    <%@ include file="GetServerDriven.jsp"%>
		    <%@ include file="BadgeScripts.jsp"%>
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
	           
			   if ServerDriven_CanPostLocation()
		          Page.setComponentAttribute("item3","visible","1")
		       else
		          Page.setComponentAttribute("item3","visible","0")
		       endif
		       
		       if ServerDriven_CanShowMovie()
	        	  Page.setComponentAttribute("item0","visible","1")
	           else
	              Page.setComponentAttribute("item0","visible","0")
	           endif
	           
	           if ServerDriven_CanRestaurant()
	        	  Page.setComponentAttribute("item4","visible","1")
	           else
	              Page.setComponentAttribute("item4","visible","0")
	           endif
	           
	           String label = ""
		       label = "<%=msg.get("apps.MyMileage")%>"
		       if "TRUE" == IsNewApp("MyMileage")
		       	  label = label + " <red>$(startup.New)</red>" 
		       endif
		       Page.setComponentAttribute("item5","text",label)
		       
	           if ServerDriven_CanMyMileage()
	        	  Page.setComponentAttribute("item5","visible","1")
	           else
	              Page.setComponentAttribute("item5","visible","0")
	           endif
		    endfunc
		    
			func onClickWeather()
				Weather_C_showCurrent()
				return FAIL
			endfunc

   			func onMyMileageClick()
   				makeNotNew("MyMileage")
				if !isPremium()
					System.doAction("MyMileageMenuURL")
					return
				endif
				if !checkTermsConditionAccept()
					System.doAction("MyMileageMenuURL")
					return					
				endif
				System.doAction("MyMileageHomeMenuURL")
		    endfunc
		    
		   func checkTermsConditionAccept()
			TxNode node = Cache.getCookie("ACCEPTED_MYMILEAGE_TERMS")
		   	string flag
	        	if NULL != node
	          	 	flag = TxNode.msgAt(node,0)
	          	 	if flag != NULL
	          	 		if flag == "YES"
	          	 			return TRUE
	          	 		endif
	          	 	endif
	        	endif
	        	return FALSE
		    endfunc

		    func AccountTypeForSprint_GetServiceLevel()
			    TxNode serviceLevelNode = UserInfo.getAccountInfo()
			    println("get user info from...........")
			    println(serviceLevelNode)
		        int value = 0
		        if NULL != serviceLevelNode && TxNode.getValueSize(serviceLevelNode) > 1
		           value = TxNode.valueAt(serviceLevelNode, 1)
		        endif
		       
		        println("service level................"+value)
		        return value
		    endfunc
			func isPremium()
				int serviceLevel =  AccountTypeForSprint_GetServiceLevel()
				if serviceLevel >= <%=Constant.SERVICE_LEVEL_PREMIUM_FREE%>
					return TRUE
				endif
				return FALSE
			endfunc


		]]>
		</tml:script>
		
	<tml:page id="localApps" url="<%=pageURL%>" type="<%=pageType%>"
		showLeftArrow="true" showRightArrow="true"  helpMsg="$//$apps" groupId="<%=GROUP_ID_MISC%>">

		<tml:title id="title" align="center|middle"
			fontWeight="bold|system_large" fontColor="white">
			<%=appTitle%>
		</tml:title>

		<tml:listBox id="menuListBox" name="pageListBox:settingsList"
			isFocusable="true" hotKeyEnable="false">
			<tml:menuItem name="movieMenu" pageURL="<%=movieURL%>" />
			<tml:listItem id="item0" fontWeight="bold|system_large"
				focusIconImage=""
				blurIconImage=""
				align="left|middle" showArrow="true">

				<%=msg.get("apps.Movies")%>
				<tml:menuRef name="movieMenu" />
			</tml:listItem>

			<tml:menuItem name="weatherMenu" onClick="onClickWeather" trigger="TRACKBALL_CLICK" />
			<tml:listItem id="item1" fontWeight="bold|system_large"
				focusIconImage=""
				blurIconImage=""
				align="left|middle" showArrow="true">
				<%=msg.get("apps.Weather")%>
				<tml:menuRef name="weatherMenu" />
			</tml:listItem>

			<tml:menuItem name="commuteAlert" pageURL="<%=cAlertURL%>"></tml:menuItem>
			<tml:listItem id="item2" fontWeight="bold|system_large"
				focusIconImage=""
				blurIconImage=""
				showArrow="true">
				<%=msg.get("apps.CommuteAlerts")%>
				<tml:menuRef name="commuteAlert"></tml:menuRef>
			</tml:listItem>
			
			<tml:menuItem name="postLocation" pageURL="<%=postLocationURL%>"></tml:menuItem>
			<tml:listItem id="item3" fontWeight="bold|system_large"
				focusIconImage=""
				blurIconImage=""
				showArrow="true">
				<%=msg.get("apps.PostLocation")%>
				<tml:menuRef name="postLocation"></tml:menuRef>
			</tml:listItem>
			
			
			<tml:menuItem name="restaurantMenu" pageURL="<%=restaurantURL%>" />
			<tml:listItem id="item4" fontWeight="bold|system_large"
				focusIconImage=""
				blurIconImage=""
				showArrow="true">
				<%=msg.get("apps.Restaurant")%>
				<tml:menuRef name="restaurantMenu" />
			</tml:listItem>
			
			<tml:menuItem name="MyMileageMenu" onClick="onMyMileageClick" />
			<tml:menuItem name="MyMileageMenuURL" pageURL="<%=MyMileageURL%>" />
			<tml:menuItem name="MyMileageHomeMenuURL" pageURL="<%=MyMileageHomeURL%>" />

			<tml:listItem id="item5" fontWeight="bold|system_large"
				focusIconImage=""
				blurIconImage=""
				showArrow="true">
				<%=msg.get("apps.MyMileage")%>
				<tml:menuRef name="MyMileageMenu" />
			</tml:listItem>
		</tml:listBox>
		<tml:image id="titleShadow" url=""   visible="true" align="left|top"/>
	</tml:page>
	<cserver:outputLayout />
</tml:TML>
