<%@ include file="layout.jsp"%>
<%@page import="java.util.Collection"%>
<%@page
	import="com.telenav.browser.common.resource.holder.LayoutProperties"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>

<%@page import="com.telenav.browser.movie.datatype.MovieImage"%>
<%@page import="java.util.List"%>

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

		int y = 0;
		int x = 0;

		int lPicWidth = layout
				.getIntProperty("movies.info.showtimes.pic.max.width");
		int lPicHeight = layout
				.getIntProperty("movies.info.showtimes.pic.max.height");
		int lMovieNameHeight = layout
				.getIntProperty("movies.info.showtimes.movie.name.height");
		int lFandangoWidth = layout
				.getIntProperty("movies.fandango.width");
		int lFandangoHeight = layout
				.getIntProperty("movies.fandango.height");
		int lFandangoX = (lScreenWidth - lFandangoWidth) / 2;

		MovieImage smallImage = (MovieImage) request
				.getAttribute("smallImage");
		List list = (List) request.getAttribute("scheduleList");
		int schedulesCount = 0;
		if (null != list) {
			schedulesCount = list.size();
		}
%>
<tml:layout height="<%=lScreenHeight%>" width="<%=lScreenWidth%>">
	<tml:uicontrol id="buyTicketsPage">
		<tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lScreenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=lScreenHeight%></tml:uiattribute>
		<tml:uiattribute key="background"><%=imagePath + "/background_blank.png"%></tml:uiattribute>
	</tml:uicontrol>
	<%--title--%>
	<tml:uicontrol id="buyTicketsTitle">
		<tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lScreenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=lTitleHeight%></tml:uiattribute>
		<tml:uiattribute key="fontColor"><%=lTitleFontColor%></tml:uiattribute>
	</tml:uicontrol>
	<%
		y += lTitleHeight + 10;
	%>
	<%
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
				x = 0;
	%>
	<%
		for (int i = 0; i < schedulesCount; i++) {
	%>
	<tml:uicontrol id="<%="buyTicketsLabel" + i%>">
		<tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lScreenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=lHeight%></tml:uiattribute>
	</tml:uicontrol>
	<%
		y += lHeight + lSpace;
				}
	%>
	<%
		y += 15;
	%>
	<%--badge fandango--%>
	<tml:uicontrol id="fandango">
		<tml:uiattribute key="x"><%=lFandangoX%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lFandangoWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=lFandangoHeight%></tml:uiattribute>
		<tml:uiattribute key="url"><%=imagePath + "/fandango.png"%></tml:uiattribute>
	</tml:uicontrol>
	<%
		y += lFandangoHeight + 5;
	%>
	<tml:uicontrol id="nullLabel">
		<tml:uiattribute key="x"><%=0%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y + 1%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lScreenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=1%></tml:uiattribute>
	</tml:uicontrol>
</tml:layout>
<%
	}
%>