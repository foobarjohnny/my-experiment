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
		
		int labelHeight = 30;
		int inputBoxHeight = 40;
		int buttonHeight = 50;
		
		int labelWidth = 40;
		int buttonWidth = 150;
		
		int y = 0;
		int x = 0;
		String imageBg = host + "/image/background_telenav.png";
%>

<tml:layout height="<%=screenHeight%>" width="<%=screenWidth%>">
	<tml:uicontrol id="page">
		<tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=screenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=screenHeight - titleHeight%></tml:uiattribute>
		<tml:uiattribute key="background"><%=imageBg%></tml:uiattribute>
	</tml:uicontrol>
    
    <tml:uicontrol id="title">
		<tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=screenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=titleHeight%></tml:uiattribute>
	</tml:uicontrol>
	<%
	     y += titleHeight;
	%>
	
	<tml:uicontrol id="mDetails">
	    <tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=screenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=labelHeight%></tml:uiattribute>
	</tml:uicontrol>
	<%
	    y += labelHeight;
	%>
	
	<tml:uicontrol id="tDetails">
	    <tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=screenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=labelHeight%></tml:uiattribute>
	</tml:uicontrol>
	<%
	    y += labelHeight;
	%>
	<tml:uicontrol id="sentTo">
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
	    y = y + 2*labelHeight;
	%>
	<tml:uicontrol id="sentButton">
	    <tml:uiattribute key="x"><%=(screenWidth - buttonWidth) / 2%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=buttonWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=buttonHeight%></tml:uiattribute>
	</tml:uicontrol>
	<%
	     y += buttonHeight; 
	%>
	<tml:uicontrol id="smsNotify">
	    <tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=screenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=labelHeight%></tml:uiattribute>
	</tml:uicontrol>
	
</tml:layout>
