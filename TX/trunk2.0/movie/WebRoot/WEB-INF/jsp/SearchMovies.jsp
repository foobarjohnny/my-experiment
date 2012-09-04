<%@ include file="header.jsp"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>

<%@page import="com.telenav.browser.movie.service.MoviesManager"%>
<%@page import="java.util.List"%>
<%@page import="com.telenav.browser.movie.datatype.Movie"%>
<%@page import="com.telenav.browser.movie.util.MovieUtil"%>


<%
	int lButtonInterval = layout
			.getIntProperty("movies.button.c.interval");
	int lButtonWidth = layout.getIntProperty("movies.button.c.width");
	int lButtonHeight = layout.getIntProperty("movies.button.c.height");
	int lButtonMargin = layout.getIntProperty("movies.button.c.margin");
	int lPreviousButtonX = (lScreenWidth - lButtonWidth * 2 - lButtonInterval) / 2;
	int lNextButtonX = lPreviousButtonX + lButtonWidth
			+ lButtonInterval;
%>


<tml:TML outputMode="TxNode">
	<%
		Object o = request.getAttribute("nextPage");
			boolean nextPage = false;
			if (o != null) {
				nextPage = ((Boolean) o).booleanValue();
			}

			o = request.getAttribute("previousPage");
			boolean previousPage = false;
			if (o != null) {
				previousPage = ((Boolean) o).booleanValue();
			}

			o = request.getAttribute("currentPage");
			int currentPage = 0;
			if (o != null) {
				currentPage = ((Integer) o).intValue();
			}
			o = request.getAttribute("moviePagesAmount");
			int moviePagesAmount = 0;
			if (o != null) {
				moviePagesAmount = ((Integer) o).intValue();
			}

			String shortDate = (String) request.getAttribute("shortDate");
			int sortBy = ((Integer) request.getAttribute("sortBy"))
					.intValue();

			// Create the link.
			StringBuffer sb = new StringBuffer();
			sb.append(host).append("/ChangePage.do");
			sb.append("?timestamp=");
			sb.append(request.getAttribute("timestamp"));
			sb.append("&amp;sortBy=");
			sb.append(sortBy);
			sb.append("&amp;shortDate=");
			sb.append(shortDate);
			sb.append("&amp;pageNumber=");

			String url = sb.toString();
	%>
	<tml:page id="searchMoviesPage" url="<%=url + currentPage%>"
		background="<%=imagePath + "/background_blank.png"%>" x="0" y="0"
		width="<%=lScreenWidth%>" height="<%=lScreenHeight%>" type="prefetch">
		<%-- Page menu. --%>
		<%--sort--%>
		<tml:menuRef name="sort" />
		<%--new search--%>
		<tml:menuItem name="newsearch" pageURL="<%=host + "/Startup.do"%>"
			text="New Search" trigger="KEY_MENU">
		</tml:menuItem>
		<tml:menuRef name="newsearch" />


		<%-- Menu items and action items. --%>
		<%
			if (previousPage) {
		%>
		<tml:menuItem name="previous" pageURL="<%=url + (currentPage - 1)%>"
			text="previous" trigger="TRACKBALL_CLICK">
		</tml:menuItem>
		<%
			}
					if (nextPage) {
		%>
		<tml:menuItem name="next" pageURL="<%=url + (currentPage + 1)%>"
			text="next" trigger="TRACKBALL_CLICK">
		</tml:menuItem>
		<%
			}
		%>
		<%
			if (sortBy == MoviesManager.SORT_BY_NAME) {
		%>
		<tml:menuItem name="sort" pageURL="<%=host + "/SearchMovies.do"%>"
			text="Sort by Rating" trigger="KEY_MENU">
			<tml:bean name="sortBy" valueType="int"
				value="<%=MoviesManager.SORT_BY_RATING + ""%>" />
		</tml:menuItem>
		<%
			} else {
		%>
		<tml:menuItem name="sort" pageURL="<%=host + "/SearchMovies.do"%>"
			text="Sort by Movie Name" trigger="KEY_MENU">
			<tml:bean name="sortBy" valueType="int"
				value="<%=MoviesManager.SORT_BY_NAME + ""%>" />
		</tml:menuItem>
		<%
			}
		%>

		<%-- Title --%>
		<%
			String pagination = " (" + (currentPage + 1) + "/"
							+ moviePagesAmount + ")";
					String dateString = " (" + shortDate.replaceAll("_", " ")
							+ ")";
					String titleInfo = dateString;
		%>
		<tml:title id="searchMoviesTitle" x="0" y="3"
			width="<%=lScreenWidth%>" height="<%=lTitleHeight%>" fontSize="18"
			fontWeight="bold|system_large" align="center" fontColor="<%=lTitleFontColor%>">
			<bean:message key="movies.title" /><%=titleInfo%>
		</tml:title>

		<%
			int y = lTitleHeight;
					List list = (List) request.getAttribute("movies");
					for (int i = 0; i < list.size(); i++) {
						Movie movie = (Movie) list.get(i);

						String cast = movie.getCast();
						if (cast != null && cast.length() > 60) {
							cast = cast.substring(0, 60);
						}
						if (cast == null) {
							cast = "";
						}
						String grade = movie.getGrade();

						if (grade == null) {
							grade = "";
						} else {
							grade = " (" + grade + ")";
						}
		%>
		<%--select movie--%>
		<tml:menuItem name="<%="showTime" + i%>"
			pageURL="<%=host + "/MoviesInfoShowTimes.do"%>"
			text="Movie Info &amp; Showtimes" trigger="KEY_RIGHT|TRACKBALL_CLICK">
			<tml:bean name="movieId" valueType="int"
				value="<%=movie.getId() + ""%>" />
		</tml:menuItem>
		<%
			String ratingImageUrl = imagePath + "/small_"
								+ ((int) (movie.getRating() * 2)) + ".png";
						String info = MovieUtil.amend(movie.getName() + grade
								+ "\r\n" + cast);
		%>
		<tml:urlImageLabel id="<%="movie"+i%>" x="0" y="<%=y%>"
			width="<%=lScreenWidth%>" height="<%=lHeight%>" fontSize="16"
			fontWeight="bold|system" align="left" multLine="true"
			imageUrl="<%=ratingImageUrl%>">
			<%=info%>
			<tml:menuRef name="<%="showTime" + i%>" />
		</tml:urlImageLabel>
		<%
			y += lHeight + lSpace;
					}
		%>
		<%
			y += lButtonMargin;
		%>
		<%--previous and next button--%>
		<%
			if (previousPage) {
		%>
		<tml:button id="previousButton" x="<%=lPreviousButtonX%>" y="<%=y%>"
			width="<%=lButtonWidth%>" height="<%=lButtonHeight%>" text="Previous"
			textVisible="true"
			imageUnclick="<%=imagePath + "/button-c_off.png"%>"
			imageClick="<%=imagePath + "/button-c_on.png"%>" isFocusable="true"
			fontWeight="bold|system_median">
			<tml:menuRef name="previous" />
		</tml:button>
		<%
			}
					if (nextPage) {
		%>
		<tml:button id="nextButton" x="<%=lNextButtonX%>" y="<%=y%>"
			width="<%=lButtonWidth%>" height="<%=lButtonHeight%>" text="Next"
			textVisible="true"
			imageUnclick="<%=imagePath + "/button-c_off.png"%>"
			imageClick="<%=imagePath + "/button-c_on.png"%>" isFocusable="true"
			fontWeight="bold|system_median">
			<tml:menuRef name="next" />
		</tml:button>
		<%
			}
		%>
		<%
			if (nextPage || previousPage) {
						y += lButtonHeight * 2;
		%>
		<tml:label id="nullLabel" x="0" y="<%=y%>"
			width="<%=lScreenWidth%>" height="1" fontSize="16"
			fontWeight="bold|system_huge" align="left">
			<tml:menuRef name="null" />
		</tml:label>
		<%
			}
		%>
	</tml:page>
</tml:TML>
