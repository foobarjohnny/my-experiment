<%@ include file="header.jsp"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.tnbrowser.util.*"%>
<%@page import="com.telenav.j2me.datatypes.TxNode"%>

<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.telenav.browser.movie.Constants"%>
<%@page import="com.telenav.browser.movie.util.MovieUtil"%>
<%@page import="com.telenav.browser.movie.datatype.Movie"%>
<%@page import="com.telenav.browser.movie.datatype.Schedule"%>
<%@page import="com.telenav.browser.movie.service.MoviesManager"%>
<%
	String shortDate = " ("
			+ (String) request.getAttribute("shortDate") + ")";
	String theaterId = ((Long) request.getAttribute("theaterId"))
			.toString();
%>
<tml:TML outputMode="TxNode">
	<tml:page id="selectMoviePage" url="/SelectMovie.jsp"
		background="<%=imagePath + "/background_blank.png"%>" x="0" y="0"
		width="<%=lScreenWidth%>" height="<%=lScreenHeight%>" type="net"
		showLeftArrow="false" showRightArrow="false">
		<%-- Page menu. --%>
		<%--sort--%>
		<tml:menuRef name="sort" />
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

		<%-- Menu items and action items. --%>
		<%
			int sortBy = ((Integer) request.getAttribute("sortBy"))
							.intValue();
					if (sortBy == MoviesManager.SORT_BY_NAME) {
		%>
		<tml:menuItem name="sort" pageURL="<%=host + "/SelectMovie.do"%>"
			text="Sort by Rating" trigger="KEY_MENU">
			<tml:bean name="theaterId" valueType="int" value="<%=theaterId%>" />
			<tml:bean name="sortBy" valueType="int"
				value="<%=MoviesManager.SORT_BY_RATING + ""%>" />
		</tml:menuItem>
		<%
			} else {
		%>
		<tml:menuItem name="sort" pageURL="<%=host + "/SelectMovie.do"%>"
			text="Sort by Movie Name" trigger="KEY_MENU">
			<tml:bean name="theaterId" valueType="int" value="<%=theaterId%>" />
			<tml:bean name="sortBy" valueType="int"
				value="<%=MoviesManager.SORT_BY_NAME + ""%>" />
		</tml:menuItem>
		<%
			}
		%>
		<%-- Title --%>
		<tml:title id="selectMovieTitle" x="0" y="3" width="320" height="28"
			fontSize="18" fontWeight="bold|system_large" align="center"
			fontColor="<%=lTitleFontColor%>">
			<bean:message key="movies.title" /><%=shortDate%>
		</tml:title>
		<%--select movie--%>
		<%
			Map movieMap = (Map) request.getAttribute("movies");
					Set key = movieMap.keySet();
					int index = 0;
					int y = lTitleHeight;
					String ratingImageUrl;
					String info;
					for (Iterator i = key.iterator(); i.hasNext(); index++) {

						Movie movie = (Movie) i.next();
						List schedule = (List) movieMap.get(movie);
						String showTime = MovieUtil.composeShowTimeAm(schedule);
						String grade = movie.getGrade();

						if (grade == null) {
							grade = "";
						} else {
							grade = " (" + grade + ")";
						}

						ratingImageUrl = imagePath + "/small_"
								+ ((int) (movie.getRating() * 2)) + ".png";
						info = MovieUtil.amend(movie.getName() + grade + "\r\n"
								+ showTime);
		%>


		<tml:menuItem name="<%="selectMovie" + index%>"
			pageURL="<%=host + "/MoviesInfoTickets.do"%>"
			trigger="KEY_RIGHT|TRACKBALL_CLICK" text="Movie Info &amp; Tickets">
			<tml:bean name="movieId" valueType="int"
				value="<%=movie.getId() + ""%>" />
			<tml:bean name="theaterId" valueType="int" value="<%=theaterId%>" />
		</tml:menuItem>


		<tml:urlImageLabel id="<%="movie" + index%>" x="0" y="<%=y%>"
			width="<%=lScreenWidth%>" height="<%=lHeight%>" fontSize="16"
			fontWeight="bold|system" align="left" multLine="true"
			imageUrl="<%=ratingImageUrl%>">
			<%=info%>
			<tml:menuRef name="<%="selectMovie" + index%>" />

		</tml:urlImageLabel>
		<%
			y += lHeight + lSpace;
					}
		%>
	</tml:page>
</tml:TML>
