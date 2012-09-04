<%@ include file="layout.jsp"%>
<%@page import="java.util.Collection"%>
<%@page
	import="com.telenav.browser.common.resource.holder.LayoutProperties"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.tnbrowser.util.Utility,java.io.*"%>
<%@page import="com.telenav.browser.movie.datatype.MovieImage"%>
<%@page import="com.telenav.browser.movie.datatype.Movie"%>
<%@page import="com.telenav.browser.movie.datatype.Theater"%>
<%@page import="com.telenav.browser.movie.util.MovieUtil"%>
<%@page import="com.telenav.j2me.datatypes.TxNode"%>
<%
	for (java.util.Iterator iterator = layoutSupported.iterator(); iterator
			.hasNext();) {
		LayoutProperties layout = (LayoutProperties) iterator.next();
		int lHeight = layout.getIntProperty("button.height");
		int lSpace = layout.getIntProperty("button..margin");
		int lTitleHeight = layout.getIntProperty("title.height");
		int lScreenWidth = layout.getIntProperty("screen.width");
		int lScreenHeight = layout.getIntProperty("screen.height");
		String lTitleFontColor = layout
				.getStringProperty("title.font.color");
		String imagePath = host + "/images/" + lScreenWidth + "x"
				+ lScreenHeight;

		int y = 0;
		int x = 0;

		int lImageHeight = layout
				.getIntProperty("movies.detail.pic.max.height");

		int lMovieNameHeight = layout
				.getIntProperty("movies.detail.movie.name.height");
		int lMargin1 = layout.getIntProperty("movies.detail.margin1");
		int lMargin2 = layout.getIntProperty("movies.detail.margin2");
		int lTheaterNameHeight = layout
				.getIntProperty("movies.detail.theater.name.height");
		int lTheaterInfoHeight = layout
				.getIntProperty("movies.detail.theater.info.height");
		int lShowtimesHeight = layout
				.getIntProperty("movies.detail.showtimes.height");
		int lGenresHeight = layout
				.getIntProperty("movies.detail.genres.height");

		int lButtonWidth = layout
				.getIntProperty("movies.buy.button.width");
		int lButtonHeight = layout
				.getIntProperty("movies.buy.button.height");
		int lButtonX = (lScreenWidth - lButtonWidth) / 2;

		int lFandangoWidth = layout
				.getIntProperty("movies.fandango.width");
		int lFandangoHeight = layout
				.getIntProperty("movies.fandango.height");
		int lFandangoX = (lScreenWidth - lFandangoWidth) / 2;

		int lRunTimesHeight = layout
				.getIntProperty("movies.info.showtimes.run.times.height");
		int lRatingImageHeight = layout
				.getIntProperty("movies.info.showtimes.rating.image.height");
		int lRatingImageWidth = layout
				.getIntProperty("movies.info.showtimes.rating.image.width");
		int lDescriptionHeight = layout
				.getIntProperty("movies.detail.description.height");

		Movie movie = (Movie) request.getAttribute("movie");
		Theater theater = (Theater) request.getAttribute("theater");
		MovieImage bigImage = (MovieImage) request
				.getAttribute("bigImage");
		boolean ticketAvailable = ((Boolean) request
				.getAttribute("ticketAvailable")).booleanValue();
		Object o = request.getAttribute("theaterTicketing");

		boolean theaterTicketing;
		if (o == null) {
			theaterTicketing = false;
		} else {
			theaterTicketing = ((Boolean) o).booleanValue();
		}

		String ratingImageUrl = imagePath + "/line_"
				+ ((int) (movie.getRating() * 2)) + ".png";
%>


<tml:layout height="<%=lScreenHeight%>" width="<%=lScreenWidth%>">
	<tml:uicontrol id="movieDetailsPage">
		<tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lScreenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=lScreenHeight%></tml:uiattribute>
		<tml:uiattribute key="background"><%=imagePath + "/background_blank.png"%></tml:uiattribute>
	</tml:uicontrol>
	<tml:uicontrol id="movieDetailsTitle">
		<tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lScreenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=lTitleHeight%></tml:uiattribute>
		<tml:uiattribute key="fontColor"><%=lTitleFontColor%></tml:uiattribute>
	</tml:uicontrol>
	<%-------------------------- 
			Images.
		---------------------------%>

	<%
		y += lTitleHeight + 5;
	%>
	<%--big poster--%>
	<%
		if (bigImage == null) {
					int width = (int) (95f / 140f * (float) lImageHeight);
					x = (lScreenWidth - width) / 2;
					y += 5;
				} else {
					int width = lImageHeight * bigImage.getWidth()
							/ bigImage.getHeight();
					if (width > lScreenWidth) {
						width = lScreenWidth;
					}
					x = (lScreenWidth - width) / 2;
	%>

	<tml:uicontrol id="bigImage">
		<tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=width%></tml:uiattribute>
		<tml:uiattribute key="height"><%=lImageHeight%></tml:uiattribute>
	</tml:uicontrol>

	<tml:uicontrol id="nullLabel1">
		<tml:uiattribute key="x"><%=0%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y + 1%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lScreenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=1%></tml:uiattribute>
	</tml:uicontrol>
	<%
		y += lImageHeight + 5;
				}
	%>
	<%
		x = 0;
	%>
	<%--badge fandango--%>
	<%
		//If theater is selected , they(badge & button) shows according theaterTicketing (in certain date)
				//else, according availability of movie ticket at least one theater (in certain date).
				if ((theater != null && theaterTicketing)
						|| (theater == null && ticketAvailable)) {
	%>

	<tml:uicontrol id="fandangoImage">
		<tml:uiattribute key="x"><%=lFandangoX%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lFandangoWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=lFandangoHeight%></tml:uiattribute>
		<tml:uiattribute key="url"><%=imagePath + "/fandango.png"%></tml:uiattribute>
	</tml:uicontrol>
	<tml:uicontrol id="nullLabel2">
		<tml:uiattribute key="x"><%=0%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y + 1%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lScreenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=1%></tml:uiattribute>
	</tml:uicontrol>
	<%
		y += lFandangoHeight + 1 + 5;
	%>
	<%--buy button--%>
	<tml:uicontrol id="buyButton1">
		<tml:uiattribute key="x"><%=lButtonX%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lButtonWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=lButtonHeight%></tml:uiattribute>
		<tml:uiattribute key="imageUnclick"><%=imagePath + "/button-a_off.png"%></tml:uiattribute>
		<tml:uiattribute key="imageClick"><%=imagePath + "/button-a_on.png"%></tml:uiattribute>
	</tml:uicontrol>
	<%
		y += lButtonHeight + 10;
				}
	%>
	<tml:uicontrol id="nullLabel3">
		<tml:uiattribute key="x"><%=0%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y + 1%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lScreenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=1%></tml:uiattribute>
	</tml:uicontrol>
	<%-------------------------- 
			Text.
		---------------------------%>
	<%--movie name--%>
	<tml:uicontrol id="nullLabel3">
		<tml:uiattribute key="x"><%=0%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y + 1%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lScreenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=1%></tml:uiattribute>
	</tml:uicontrol>
	<tml:uicontrol id="movieNameLabel">
		<tml:uiattribute key="x"><%=0%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lScreenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=lMovieNameHeight%></tml:uiattribute>
	</tml:uicontrol>
	<%
		y += lMovieNameHeight;
				y += lMargin1;
				if (theater != null) {
	%>

	<%--theater info--%>

	<%--theater name--%>
	<tml:uicontrol id="theaterNameLabel">
		<tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lScreenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=lTheaterNameHeight%></tml:uiattribute>
	</tml:uicontrol>
	<%
		y += lTheaterNameHeight;
	%>
	<%--theater detail address & telephone--%>
	<tml:uicontrol id="theaterDetailsLabel">
		<tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lScreenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=lTheaterInfoHeight%></tml:uiattribute>
	</tml:uicontrol>
	<%
		y += lTheaterInfoHeight;
					y += lMargin2;
	%>
	<%--show times--%>
	<tml:uicontrol id="showTimesLabel">
		<tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lScreenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=lShowtimesHeight * 3%></tml:uiattribute>
	</tml:uicontrol>
	<tml:uicontrol id="nullLabel4">
		<tml:uiattribute key="x"><%=0%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y + 1%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lScreenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=1%></tml:uiattribute>
	</tml:uicontrol>
	<%
		y += lShowtimesHeight * 3 + lMargin2;
				}
	%>
	<%--rating&grade&runTime--%>

	<tml:uicontrol id="ratingImage">
		<tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lRatingImageWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=lRatingImageHeight%></tml:uiattribute>
		<tml:uiattribute key="url"><%=ratingImageUrl%></tml:uiattribute>
	</tml:uicontrol>
	<%
		x += lRatingImageWidth + 8;
	%>
	<tml:uicontrol id="runTimeLabel">
		<tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lScreenWidth - x%></tml:uiattribute>
		<tml:uiattribute key="height"><%=lRunTimesHeight%></tml:uiattribute>
	</tml:uicontrol>
	<tml:uicontrol id="nullLabel5">
		<tml:uiattribute key="x"><%=0%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y + 1%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lScreenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=1%></tml:uiattribute>
	</tml:uicontrol>
	<%
		y += lRunTimesHeight;
				x = 0;
	%>
	<%--movie genres--%>
	<tml:uicontrol id="gernesLabel">
		<tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lScreenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=lGenresHeight%></tml:uiattribute>
	</tml:uicontrol>
	<tml:uicontrol id="nullLabel6">
		<tml:uiattribute key="x"><%=0%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y + 1%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lScreenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=1%></tml:uiattribute>
	</tml:uicontrol>
	<%
		y += lGenresHeight + lMargin2;
	%>
	<%--movie cast--%>
	<tml:uicontrol id="castLabel">
		<tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lScreenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=lGenresHeight%></tml:uiattribute>
	</tml:uicontrol>
	<%
		y += lGenresHeight;
	%>
	<%--movie director--%>
	<tml:uicontrol id="directorLabel">
		<tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lScreenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=lGenresHeight%></tml:uiattribute>
	</tml:uicontrol>
	<tml:uicontrol id="nullLabel7">
		<tml:uiattribute key="x"><%=0%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y + 1%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lScreenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=1%></tml:uiattribute>
	</tml:uicontrol>
	<%
		y += lGenresHeight;
	%>
	<%--movie description--%>
	<tml:uicontrol id="descriptionLabel">
		<tml:uiattribute key="x"><%=x%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lScreenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=lDescriptionHeight%></tml:uiattribute>
	</tml:uicontrol>
	<%
		y += lDescriptionHeight + lMargin2;
				//If theater is selected , they(badge & button) shows according theaterTicketing (in certain date)
				//else, according availability of movie ticket at least one theater (in certain date).
				if ((theater != null && theaterTicketing)
						|| (theater == null && ticketAvailable)) {
	%>
	<%--buy button--%>
	<tml:uicontrol id="buyButton2">
		<tml:uiattribute key="x"><%=lButtonX%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lButtonWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=lButtonHeight%></tml:uiattribute>
		<tml:uiattribute key="imageUnclick"><%=imagePath + "/button-a_off.png"%></tml:uiattribute>
		<tml:uiattribute key="imageClick"><%=imagePath + "/button-a_on.png"%></tml:uiattribute>
	</tml:uicontrol>
	<%
		y += lButtonHeight + lMargin2;
				}
	%>
	<tml:uicontrol id="nullLabel8">
		<tml:uiattribute key="x"><%=0%></tml:uiattribute>
		<tml:uiattribute key="y"><%=y + 1%></tml:uiattribute>
		<tml:uiattribute key="width"><%=lScreenWidth%></tml:uiattribute>
		<tml:uiattribute key="height"><%=1%></tml:uiattribute>
	</tml:uicontrol>

</tml:layout>
<%
	}
%>