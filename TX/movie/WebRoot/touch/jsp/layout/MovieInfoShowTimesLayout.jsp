<%@page import="java.util.Collection"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ include file="../Header.jsp"%>
<%
		int screenHeight = 320;
		int screenWidth = 480;
		
		int titleHeight = 28;
 		int labelHeight = 49;

		// movie detail property
		int picWidth = 80;
		int picHeight = 120;
		Integer w = (Integer) request.getAttribute("imgW");
		Integer h = (Integer)request.getAttribute("imgH");
		if (w!= null) picWidth = w.intValue();
		if (h!=null) picHeight = h.intValue();
		
		int movieNameHeight = 24;
		// small picture relative number of screen
		
		
		int y = 0;
		int x = 0;
		String imageBg = host + "/image/background_telenav.png";
%>

<tml:layout height="<%=screenHeight%>" width="<%=screenWidth%>">
	<tml:uicontrol id="MovieListPage">
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
	
	<tml:uicontrol id="smallImage">
		<tml:uiattribute key="x">5</tml:uiattribute>
		<tml:uiattribute key="y">40</tml:uiattribute>
		<tml:uiattribute key="width"><%=picWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=picHeight%></tml:uiattribute>
	</tml:uicontrol>
	<%
		x = 5 + picWidth + 5;			
	%>
	
    <tml:uicontrol id="nameLabel">
		<tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y">40</tml:uiattribute>
		<tml:uiattribute key="width"><%=screenWidth - x%></tml:uiattribute>
		<tml:uiattribute key="height"><%=movieNameHeight%></tml:uiattribute>
	</tml:uicontrol>
    
    <tml:uicontrol id="ratingImage">
		<tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y">65</tml:uiattribute>
		<tml:uiattribute key="width">87</tml:uiattribute>
		<tml:uiattribute key="height">20</tml:uiattribute>
	</tml:uicontrol>
	
    <tml:uicontrol id="detailLabel">
		<tml:uiattribute key="x"><%=x + 90%></tml:uiattribute>
		<tml:uiattribute key="y">65</tml:uiattribute>
		<tml:uiattribute key="width"><%=screenWidth - x%></tml:uiattribute>
		<tml:uiattribute key="height">25</tml:uiattribute>
	</tml:uicontrol>
	
 	<tml:uicontrol id="castLabel">
		<tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y">85</tml:uiattribute>
		<tml:uiattribute key="width"><%=screenWidth - x%></tml:uiattribute>
		<tml:uiattribute key="height"><%=titleHeight + picHeight - 90%></tml:uiattribute>
	</tml:uicontrol>
		<%
		    y = 40 + picHeight + 2;
		%>

	<tml:uicontrol id="MDImageLabel">
		<tml:uiattribute key="x">0</tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=screenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=labelHeight%></tml:uiattribute>
	</tml:uicontrol>
	
		<%
		    y += labelHeight - 10;
		%>
	<tml:uicontrol id="TheatersBox">
		<tml:uiattribute key="x">0</tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=screenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=screenHeight - y - labelHeight/2%></tml:uiattribute>
	</tml:uicontrol>
<% 
			for(int i=0; i<10; i++){
%>
	<tml:uicontrol id="<%="buyTickets" + i%>">
		<tml:uiattribute key="x">0</tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=screenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=labelHeight%></tml:uiattribute>
	</tml:uicontrol>

<% 				
				y += labelHeight;
			}
%>
	
</tml:layout>
