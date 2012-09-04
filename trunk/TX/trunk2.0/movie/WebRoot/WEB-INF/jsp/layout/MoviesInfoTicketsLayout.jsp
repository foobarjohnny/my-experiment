<%@ include file="layout.jsp"%>
<%@page import="java.util.Collection"%>
<%@page
	import="com.telenav.browser.common.resource.holder.LayoutProperties"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>

<%@page import="com.telenav.browser.movie.datatype.Movie"%>
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

		int y = 0;
		int x = 0;

		int lRunTimesHeight = layout
				.getIntProperty("movies.info.showtimes.run.times.height");
		int lRatingImageHeight = layout
				.getIntProperty("movies.info.showtimes.rating.image.height");
		int lRatingImageWidth = layout
				.getIntProperty("movies.info.showtimes.rating.image.width");

		Movie m = (Movie) request.getAttribute("movie");
		List list = (List) request.getAttribute("scheduleList");

		String ratingImageUrl = imagePath + "/line_"
				+ ((int) (m.getRating() * 2)) + ".png";
		MovieImage smallImage = (MovieImage) request
				.getAttribute("smallImage");
		int scheduleCount = 0;
		if (null != list) {
			scheduleCount = list.size();
		}
%>


<tml:layout height="<%=lScreenHeight%>" width="<%=lScreenWidth%>">
	<tml:uicontrol id="moviesInfoTicketsPage">
		<tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lScreenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=lScreenHeight%></tml:uiattribute>
		<tml:uiattribute key="background"><%=imagePath + "/background_blank.png"%></tml:uiattribute>
	</tml:uicontrol>

	<%--title--%>
	<tml:uicontrol id="moviesInfoTicketsTitle">
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
		if (smallImage != null) {
					x += 5;
	%>
	<%--small poster--%>
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
	<%--rating--%>
	<%
		y += lMovieNameHeight;
	%>
	<tml:uicontrol id="ratingImage">
		<tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lRatingImageWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=lRatingImageHeight%></tml:uiattribute>
		<tml:uiattribute key="url"><%=ratingImageUrl%></tml:uiattribute>
	</tml:uicontrol>
	<%--movie grade & runTime--%>
	<tml:uicontrol id="runTimeAndGradeLabel">
		<tml:uiattribute key="x"><%=x + lRatingImageWidth + 3%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lScreenWidth - x - lRatingImageWidth
									- 3%></tml:uiattribute>
		<tml:uiattribute key="height"><%=lRunTimesHeight%></tml:uiattribute>
	</tml:uicontrol>
	<%--cast--%>
	<%
		y += lRunTimesHeight;
	%>
	<tml:uicontrol id="castLabel">
		<tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lScreenWidth - x%></tml:uiattribute>
		<tml:uiattribute key="height"><%=lPicHeight - lRunTimesHeight
									- lMovieNameHeight%></tml:uiattribute>
	</tml:uicontrol>
	<%
		y += lPicHeight - lRunTimesHeight - lMovieNameHeight + 7;
				x = 0;
	%>
	<%--movie details button--%>
	<tml:uicontrol id="movieDetailsLabel">
		<tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lScreenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=lHeight%></tml:uiattribute>
	</tml:uicontrol>
	<%--buy tickets button--%>
	<%
		for (int i = 0; i < scheduleCount; i++) {
					y += lHeight + lSpace;
	%>
	<tml:uicontrol id="<%="buyTicketsLabel" + i%>">
		<tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lScreenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=lHeight%></tml:uiattribute>
	</tml:uicontrol>
	<%
		}
				y += lHeight + lSpace;
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