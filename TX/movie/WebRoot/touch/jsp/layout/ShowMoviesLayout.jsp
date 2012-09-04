<%@page import="java.util.Collection"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ include file="../Header.jsp"%>
<%@page import="com.telenav.datatypes.content.movie.v10.Movie"%>

<%
		int screenHeight = 320;
		int screenWidth = 480;
		
		int titleHeight = 38;
		int labelHeight = 49;
		
		int y = 0;
		int x = 0;
		String imageBg = host + "/image/background_telenav.png";
		
		Movie[] m = (Movie[])request.getAttribute("Movies");
		int itemsOnScreen = m.length;
%>


<tml:layout height="<%=screenHeight%>" width="<%=screenWidth%>">
	<tml:uicontrol id="MoviesPage">
		<tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=screenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=screenHeight-titleHeight%></tml:uiattribute>
		<tml:uiattribute key="background"><%=imageBg%></tml:uiattribute>
	</tml:uicontrol>
    
    <tml:uicontrol id="MovieListLabel">
		<tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=screenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=titleHeight%></tml:uiattribute>
	</tml:uicontrol>
	<% 
    	y += titleHeight;
	%>
    <tml:uicontrol id="MovieListBox">
		<tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=screenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=screenHeight - titleHeight - labelHeight/2%></tml:uiattribute>
	</tml:uicontrol>
	<%
		for(int i=0; i<itemsOnScreen; i++){
	%>
	
	<tml:uicontrol id="<%="movieItem" + i%>">
	    <tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=screenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=labelHeight%></tml:uiattribute>
	</tml:uicontrol>
	<%
			//y += labelHeight;
		}
	%>
	
</tml:layout>
