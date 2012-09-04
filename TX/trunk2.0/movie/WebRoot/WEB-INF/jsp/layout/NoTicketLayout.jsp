<%@ include file="layout.jsp"%>
<%@page import="java.util.Collection"%>
<%@page
	import="com.telenav.browser.common.resource.holder.LayoutProperties"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>

<%@page import="com.telenav.browser.movie.datatype.MovieImage"%>
<%@page import="com.telenav.tnbrowser.util.Utility,java.io.*"%>

<%
	for (java.util.Iterator iterator = layoutSupported.iterator(); iterator
			.hasNext();) {
		LayoutProperties layout = (LayoutProperties) iterator.next();
		int lHeight = layout.getIntProperty("button.height");
		int lSpace = layout.getIntProperty("button..margin");
		int lTitleHeight = layout.getIntProperty("title.height");
		int lScreenWidth = layout.getIntProperty("screen.width");
		int lScreenHeight = layout.getIntProperty("screen.height");
		String lTitleFontColor = layout.getStringProperty("title.font.color");
		String imagePath = host + "/images/" + lScreenWidth + "x"
				+ lScreenHeight;

		int lPicWidth = layout
				.getIntProperty("movies.no.ticket.pic.max.width");
		int lPicHeight = layout
				.getIntProperty("movies.no.ticket.pic.max.height");
		int lMovieNameHeight = layout
				.getIntProperty("movies.no.ticket.movie.name.height");
		int lShowtimesHeight = layout
				.getIntProperty("movies.no.ticket.showtimes.height");

		int y = 0;
		int x = 0;
%>



<tml:layout height="<%=lScreenHeight%>" width="<%=lScreenWidth%>">
	<tml:uicontrol id="noTicketPage">
		<tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lScreenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=lScreenHeight%></tml:uiattribute>
		<tml:uiattribute key="background"><%=imagePath + "/background_blank.png"%></tml:uiattribute>
	</tml:uicontrol>
	<tml:uicontrol id="noTicketTitle">
		<tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lScreenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=lTitleHeight%></tml:uiattribute>
		<tml:uiattribute key="fontColor"><%=lTitleFontColor%></tml:uiattribute>
	</tml:uicontrol>
	<%
		y += lTitleHeight + 10;
	%>
	<%--small poster--%>
	<%
		MovieImage smallImage = (MovieImage) request
						.getAttribute("smallImage");
				if (smallImage != null) {
					x += 5;
	%>
	<tml:uicontrol id="smallImage">
		<tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lPicWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=lPicHeight%></tml:uiattribute>
	</tml:uicontrol>
	<%
		x += lPicWidth + 5;
				}
	%>

	<%--movie name--%>
	<tml:uicontrol id="movieNameLabel">
		<tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lScreenWidth - x%></tml:uiattribute>
		<tml:uiattribute key="height"><%=lMovieNameHeight%></tml:uiattribute>
	</tml:uicontrol>
	<%--theater name&address&telephone--%>
	<tml:uicontrol id="theaterInfoLabel">
		<tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y + lMovieNameHeight%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lScreenWidth - x%></tml:uiattribute>
		<tml:uiattribute key="height"><%=lPicHeight - lMovieNameHeight%></tml:uiattribute>
	</tml:uicontrol>
	<%
		y += lPicHeight + 7;
				y += 20;
				x = 0;
	%>
	<%--show times--%>
	<tml:uicontrol id="showTimesLabel">
		<tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lScreenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=lShowtimesHeight%></tml:uiattribute>
	</tml:uicontrol>
	<%
		y += lShowtimesHeight;
	%>
	<tml:uicontrol id="showTimesContentLabel">
		<tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y + lMovieNameHeight%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lScreenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=lShowtimesHeight * 3%></tml:uiattribute>
	</tml:uicontrol>
	<tml:uicontrol id="movieDetailsLabel">
		<tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=lScreenHeight - lHeight - 2%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lScreenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=lHeight%></tml:uiattribute>
		<tml:uiattribute key="imageUnclick"><%=imagePath + "/button-b_off.png"%></tml:uiattribute>
		<tml:uiattribute key="imageClick"><%=imagePath + "/button-b_on.png"%></tml:uiattribute>
	</tml:uicontrol>
</tml:layout>
<%
	}
%>




