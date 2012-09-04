<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.telenav.browser.movie.Util" %>

<% 
    String host = "http://sergeyz-laptop.telenav.com:8080/movie";
	String poi_service_ac = "http://sergeyz-laptop.telenav.com:8080/poi_service/goToJsp.do?jsp=SelectAddress";
	String poi_service_selectR = "http://63.237.220.107:8080/poi_service/goToJsp.do?jsp=Receipient";
    String pageType = "static";
    String getPage = host + "/goToJsp.do?jsp=";
    //String pageURL = host + "/index.jsp";
    String pageURL = host + "/startUp.do";
    String menu="Test";
%>

<tml:TML outputMode="TxNode">
	<tml:script language="fscript" version="1">
		<![CDATA[
			func checkPoiLog()
				return TRUE
	        endfunc
		]]>
	</tml:script>
	
	
	<tml:page id="MovieTmpStartUp" url="<%=pageURL%>" type="<%=pageType%>"
		x="0" y="0" width="480" height="320" showLeftArrow="true"
		showRightArrow="true" helpMsg="" supportback="true">

		<tml:title id="title" align="center|middle" x="0" y="0" width="480"
			height="35" fontWeight="bold|system_huge">
		</tml:title>
		<tml:listBox id="menuListBox" name="pageListBox:settingsList" x="0"
			y="35" width="480" height="320" isFocusable="true"
			hotKeyEnable="false">

			<tml:menuItem name="searchMovie" pageURL='<%=getPage + "SearchMovie" %>'>
			</tml:menuItem>
			<tml:listItem id="searchMovieL" fontWeight="bold|system_huge" x="0"
				y="35" width="480" height="45" align="left|middle"
				showArrow="true">
				<tml:menuRef name="searchMovie" />
				<tml:menuRef name="NewSearch" />
				Search Movies
			</tml:listItem>

			<tml:menuItem name="searchTheater"	pageURL="<%=host + "/ShowMovies.do"%>">
				<tml:bean name="theaterId" valueType="String" value="2800001413" />
			</tml:menuItem>
			<tml:listItem id="searchTheaterL" fontWeight="bold|system_huge" x="0"
				y="80" width="480" height="45" align="left|middle"
				showArrow="true">
				<tml:menuRef name="searchTheater" />
				Search Movies in Theater
			</tml:listItem>
			
			<tml:menuItem name="selectDate"	pageURL="<%=poi_service_selectR%>">
			</tml:menuItem>
			<tml:listItem id="selectDateL" fontWeight="bold|system_huge" x="0"
				y="125" width="480" height="45" align="left|middle"
				showArrow="true">
				<tml:menuRef name="selectDate" />
				Select Recepients
			</tml:listItem>
			
			<tml:menuItem name="selectAddress"	pageURL="<%=poi_service_ac%>">
			</tml:menuItem>
			<tml:listItem id="selectAddressL" fontWeight="bold|system_huge" x="0"
				y="170" width="480" height="45" align="left|middle"
				showArrow="true">
				<tml:menuRef name="selectAddress" />
				Select Address
			</tml:listItem>
			
		</tml:listBox>
	</tml:page>
</tml:TML>
