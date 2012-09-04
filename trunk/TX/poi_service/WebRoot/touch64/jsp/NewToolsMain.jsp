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
	           
		       if "TRUE" == IsNewApp("MyMileage")
		       	   Page.setComponentAttribute("itemImageNew5","visible","1")
		       endif		      
		       
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

		<tml:listBox id="menuListBox">
			<tml:menuItem name="movieMenu" pageURL="<%=movieURL%>" />
			<tml:compositeListItem id="item0" visible="true" bgColor="#FFFFFF" isFocusable="true"
				transparent="false" focusBgImage=""
				blurBgImage="">
				<tml:image id="itemImage0" url="" align="left|top"/>
				<tml:label id="itemlabel0" focusFontColor="white" fontWeight="bold|system_large"
					textWrap="ellipsis" align="left|middle">
					<%=msg.get("apps.Movies")%>
				</tml:label>
				<tml:menuRef name="movieMenu" />
			</tml:compositeListItem>
	
			<tml:menuItem name="weatherMenu" onClick="onClickWeather" trigger="TRACKBALL_CLICK" />
			<tml:compositeListItem id="item1" visible="true" bgColor="#FFFFFF" isFocusable="true"
				transparent="false" focusBgImage=""
				blurBgImage="">
				<tml:image id="itemImage1" url="" align="left|top"/>
				<tml:label id="itemlabel1" focusFontColor="white" fontWeight="bold|system_large"
					textWrap="ellipsis" align="left|middle">
					<%=msg.get("apps.Weather")%>
				</tml:label>
				<tml:menuRef name="weatherMenu" />
			</tml:compositeListItem>		
	
			<tml:menuItem name="commuteAlert" pageURL="<%=cAlertURL%>"></tml:menuItem>
			<tml:compositeListItem id="item2" visible="true" bgColor="#FFFFFF" isFocusable="true"
				transparent="false" focusBgImage=""
				blurBgImage="">
				<tml:image id="itemImage2" url="" align="left|top"/>
				<tml:label id="itemlabel2" focusFontColor="white" fontWeight="bold|system_large"
					textWrap="ellipsis" align="left|middle">
					<%=msg.get("apps.CommuteAlerts")%>
				</tml:label>
				<tml:menuRef name="commuteAlert" />
			</tml:compositeListItem>
	
			<tml:menuItem name="postLocation" pageURL="<%=postLocationURL%>"></tml:menuItem>
			<tml:compositeListItem id="item3" visible="true" bgColor="#FFFFFF" isFocusable="true"
				transparent="false" focusBgImage=""
				blurBgImage="">
				<tml:image id="itemImage3" url="" align="left|top"/>
				<tml:label id="itemlabel3" focusFontColor="white" fontWeight="bold|system_large"
					textWrap="ellipsis" align="left|middle">
					<%=msg.get("apps.PostLocation")%>
				</tml:label>
				<tml:menuRef name="postLocation" />
			</tml:compositeListItem>
	
			<tml:menuItem name="restaurantMenu" pageURL="<%=restaurantURL%>" />
			<tml:compositeListItem id="item4" visible="true" bgColor="#FFFFFF" isFocusable="true"
				transparent="false" focusBgImage=""
				blurBgImage="">
				<tml:image id="itemImage4" url="" align="left|top"/>
				<tml:label id="itemlabel4" focusFontColor="white" fontWeight="bold|system_large"
					textWrap="ellipsis" align="left|middle">
					<%=msg.get("apps.Restaurant")%>
				</tml:label>
				<tml:menuRef name="restaurantMenu" />
			</tml:compositeListItem>
			
			<tml:menuItem name="MyMileageMenu" onClick="onMyMileageClick" />
			<tml:menuItem name="MyMileageMenuURL" pageURL="<%=MyMileageURL%>" />
			<tml:menuItem name="MyMileageHomeMenuURL" pageURL="<%=MyMileageHomeURL%>" />
			<tml:compositeListItem id="item5" visible="true" bgColor="#FFFFFF" isFocusable="true"
				transparent="false" focusBgImage=""
				blurBgImage="">
				<tml:image id="itemImage5" url="" align="left|top"/>
				<tml:label id="itemlabel5" focusFontColor="white" fontWeight="bold|system_large"
					textWrap="ellipsis" align="left|middle">
					<%=msg.get("apps.MyMileage")%>
				</tml:label>
				<tml:image id="itemImageNew5" url="" align="left|top" visible="false"/>
				<tml:menuRef name="MyMileageMenu" />
			</tml:compositeListItem>
		</tml:listBox>
		<tml:image id="titleShadow" url=""   visible="true" align="left|top"/>
	</tml:page>
	<cserver:outputLayout />
</tml:TML>
