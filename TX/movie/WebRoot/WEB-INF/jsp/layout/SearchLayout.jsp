<%@page import="java.util.Collection"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ include file="../Header.jsp"%>
<%
		int screenHeight = 320;
		int screenWidth = 480;
		
		int titleHeight = 38;
		int leftMargin = 40;
		int warningHeight = 40;
		
		int labelWidth = 40;
		int labelHeight = 55;
		
		int inputBoxHeight = 40;
		
		int buttonWidth = 150;
		int buttonHeight = 50;
		
		int space = 10;
		int inSameLine = 40;
		int tabBaseWidth = 40;
		
		String labelAlign = "left";
		int inputBoxWidth = screenWidth - 2 * leftMargin - inSameLine
		        * (tabBaseWidth + labelWidth + space * 4);
		
		int y = 0;
		int x = 0;
		String imageBg = host + "/image/background_telenav.png";
%>

<tml:layout height="<%=screenHeight%>" width="<%=screenWidth%>">
	<tml:uicontrol id="searchMoviePage">
		<tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=screenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=screenHeight - titleHeight%></tml:uiattribute>
		<tml:uiattribute key="background"><%=imageBg%></tml:uiattribute>
	</tml:uicontrol>
    
    <tml:uicontrol id="searchLabel">
		<tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=screenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=titleHeight%></tml:uiattribute>
	</tml:uicontrol>
	<%
	     y += titleHeight;
	%>
	
	<tml:uicontrol id="movieName">
	    <tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=screenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=inputBoxHeight%></tml:uiattribute>
	</tml:uicontrol>
	
	<%
	     y += inputBoxHeight;
	%>
	
	<tml:uicontrol id="address">
	    <tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=screenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=labelHeight%></tml:uiattribute>
	</tml:uicontrol>
	<%
	    y += labelHeight - 15;
	%>
	<tml:uicontrol id="when">
	    <tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=screenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=labelHeight%></tml:uiattribute>
	</tml:uicontrol>
	<%
	    y += labelHeight + space * 2;
	%>
	
	<tml:uicontrol id="searchButton">
	    <tml:uiattribute key="x"><%=(screenWidth - buttonWidth) / 2%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=buttonWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=buttonHeight%></tml:uiattribute>
		<tml:uiattribute key="imageClick"><%=host + "/image/button-c_on.png"%></tml:uiattribute>
		<tml:uiattribute key="imageUnclick"><%=host + "/image/button-c_off.png"%></tml:uiattribute>
	</tml:uicontrol>
</tml:layout>
