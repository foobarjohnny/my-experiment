<%@ include file="header.jsp"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.tnbrowser.util.*"%>
<%@page import="com.telenav.j2me.datatypes.TxNode"%>

<%@page import="java.text.NumberFormat"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.telenav.browser.movie.Constants"%>
<%@page import="com.telenav.browser.movie.datatype.Movie"%>
<%@page import="com.telenav.browser.movie.datatype.Theater"%>
<%@page import="com.telenav.browser.movie.datatype.Schedule"%>
<%@page import="com.telenav.browser.movie.datatype.MovieImage"%>
<%@page import="com.telenav.browser.movie.util.MovieUtil"%>
<%@page import="com.telenav.tnbrowser.util.Utility,java.io.*"%>
<%
	int lPicWidth = layout
			.getIntProperty("movies.info.showtimes.pic.max.width");
	int lPicHeight = layout
			.getIntProperty("movies.info.showtimes.pic.max.height");
	int lMovieNameHeight = layout
			.getIntProperty("movies.info.showtimes.movie.name.height");
%>


<tml:TML outputMode="TxNode">
	<tml:page id="moviesInfoShowTimesPage" url="/MoviesInfoShowTimes.jsp"
		background="<%=imagePath + "/background_blank.png"%>" x="0" y="0"
		width="<%=lScreenWidth%>" height="<%=lScreenHeight%>" type="net">
		<%
			Movie m = (Movie) request.getAttribute("movie");
					String shortDate = " ("
							+ (String) request.getAttribute("shortDate") + ")";
		%>


		<%--change date--%>
		<tml:menuItem name="changedate" pageURL="<%=host + "/SelectDate.do"%>"
			text="Change Date" trigger="KEY_MENU">
		</tml:menuItem>
		<tml:menuRef name="changedate" />
		<%--new search--%>
		<tml:menuItem name="newsearch" pageURL="<%=host + "/Startup.do"%>"
			text="New Search" trigger="KEY_MENU">
		</tml:menuItem>
		<tml:menuRef name="newsearch" />
		<%--title--%>
		<tml:title id="moviesInfoShowTimesTitle" x="0" y="3"
			width="<%=lScreenWidth%>" height="<%=lTitleHeight%>" fontSize="18"
			fontWeight="bold|system_large" align="center" fontColor="<%=lTitleFontColor%>">
			<bean:message key="movies.movieinfoshowtimes" /><%=shortDate%>
		</tml:title>
		<%
			int x = 0;
					MovieImage smallImage = (MovieImage) request
							.getAttribute("smallImage");
					if (smallImage == null) {
						x = 5;
					} else {
						String smallImgBase64 = Utility
								.byteArrayToBase64(smallImage.getData());
		%>

		<%--small poster--%>
		<tml:image id="smallImage" x="5" y="45" width="<%=lPicWidth%>"
			height="<%=lPicHeight%>" url="tempimage_smallImage" />

		<tml:tempImage url="tempimage_smallImage"
			imagedata="<%=smallImgBase64%>" />
		<%
			x = 5 + lPicWidth + 5;
					}
		%>
		<%--movie name--%>
		<tml:label id="movieNameLabel" x="<%=x%>" y="45"
			width="<%=lScreenWidth - x%>" height="<%=lMovieNameHeight%>"
			fontSize="16" fontWeight="bold|system" align="left">
			<%=MovieUtil.amend(m.getName())%>
		</tml:label>
		<%--rating--%>
		<%
			String imageUrl = imagePath + "/line_"
							+ ((int) (m.getRating() * 2)) + ".png";
		%>
		<tml:image id="ratingImage" x="<%=x%>" y="70" width="87" height="20"
			url="<%=imageUrl%>" />
		<%--grade & runTime--%>
		<tml:label id="runTimeAndGradeLabel" x="<%=x + 90%>" y="70"
			width="<%=lScreenWidth - x%>" height="25" fontSize="16"
			fontWeight="system" align="left">
			<%
				StringBuffer detail = new StringBuffer();
							if (m.getGrade() != null && m.getGrade().length() > 0) {
								detail.append(m.getGrade());
							}
							if (m.getRunTime() != null
									&& m.getRunTime().length() > 0) {
								if (m.getGrade() != null
										&& m.getGrade().length() > 0) {
									detail.append(", ");
								}
								detail.append(MovieUtil.timeFormat(m.getRunTime()));
							}
			%>
			<%=MovieUtil.amend(detail.toString())%>
		</tml:label>
		<%--cast--%>
		<tml:label id="castLabel" x="<%=x%>" y="90"
			width="<%=lScreenWidth - x%>" height="<%=45 + lPicHeight - 90%>"
			fontSize="16" fontWeight="system" align="left">
			<%
				StringBuffer cast = new StringBuffer();

							cast.append(m.getCast() == null ? "" : m.getCast());
			%>
			<%=MovieUtil.amend(cast.toString())%>
		</tml:label>
		<%
			int y = 45 + lPicHeight + 7;
		%>
		<%--movie details button--%>
		<tml:menuItem name="movies::moviedetails"
			pageURL="<%=host + "/MovieDetails.do"%>"
			trigger="KEY_RIGHT | TRACKBALL_CLICK">
			<tml:bean name="movieId" valueType="int" value="<%=m.getId() + ""%>" />
		</tml:menuItem>
		<tml:urlImageLabel id="movieDetailsLabel" x="0" y="<%=y%>"
			width="<%=lScreenWidth%>" height="<%=lHeight%>" fontSize="16"
			fontWeight="bold|system" align="left" multLine="true">
			<bean:message key="movies.seemoviedetails" />
			<tml:menuRef name="movies::moviedetails" />
		</tml:urlImageLabel>


		<%
			Map scheduleMap = (Map) request.getAttribute("scheduleMap");
					List theaterList = (List) request
							.getAttribute("theaterList");
					NumberFormat nf = NumberFormat.getInstance();
					nf.setMaximumFractionDigits(1);
					for (int i = 0; i < theaterList.size(); i++) {
						Theater t = (Theater) theaterList.get(i);
						y += lHeight + lSpace;
						// Distance.
						String distance = nf.format(t.getDistance()
								/ Constants.MILE_METER);

						// Schedule
						List list = (List) scheduleMap.get(t);
						//detailAddress
						String detailAddress = MovieUtil.getDetailAddress(t);
						//show time
						String showTime = MovieUtil.composeShowTimeAm(list);
						String info = MovieUtil.amend("[" + distance + " mi] "
								+ t.getName() + ", " + detailAddress + "\r\n"
								+ showTime);
		%>
		<%--buy tickets button--%>
		<tml:menuItem name="<%="buyTickets" + i%>"
			pageURL="<%=host + "/BuyTickets.do"%>"
			trigger="KEY_RIGHT | TRACKBALL_CLICK">
			<tml:bean name="theaterId" valueType="int"
				value="<%=t.getId() + ""%>" />
			<tml:bean name="movieId" valueType="int" value="<%=m.getId() + ""%>" />
		</tml:menuItem>
		<tml:urlImageLabel id="<%="buyTicketsLabel" + i%>" x="0" y="<%=y%>"
			width="<%=lScreenWidth%>" height="<%=lHeight%>" fontSize="16"
			fontWeight="bold|system" align="left" multLine="true">
			<%=info%>
			<tml:menuRef name="<%="buyTickets" + i%>" />
		</tml:urlImageLabel>
		<%
			}
		%>
	</tml:page>
</tml:TML>