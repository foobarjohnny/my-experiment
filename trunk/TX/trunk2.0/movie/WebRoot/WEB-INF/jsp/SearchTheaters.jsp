<%@ include file="header.jsp"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.tnbrowser.util.*"%>
<%@page import="com.telenav.j2me.datatypes.TxNode"%>

<%@page import="com.telenav.browser.movie.service.MoviesManager"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.telenav.browser.movie.datatype.Movie"%>
<%@page import="com.telenav.browser.movie.datatype.Theater"%>
<%@page import="com.telenav.browser.movie.util.MovieUtil"%>
<%@page import="com.telenav.browser.movie.Constants"%>
<%@page import="java.text.NumberFormat"%>


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
			o = request.getAttribute("theaterPagesAmount");
			int theaterPagesAmount = 0;
			if (o != null) {
				theaterPagesAmount = ((Integer) o).intValue();
			}
			// Create the link.
			StringBuffer sb = new StringBuffer();
			sb.append(host).append("/ChangeTheaterPage.do");
			sb.append("?theaterTimestamp=");
			sb.append(request.getAttribute("theaterTimestamp"));
			sb.append("&amp;lon=");
			sb.append(request.getAttribute("lon"));
			sb.append("&amp;lat=");
			sb.append(request.getAttribute("lat"));
			sb.append("&amp;radius=");
			sb.append(request.getAttribute("radius"));
			sb.append("&amp;theaterPageNumber=");

			String url = sb.toString();
	%>


	<tml:page id="searchTheatersPage" url="<%=url + currentPage%>"
		background="<%=imagePath + "/background_blank.png"%>" x="0" y="0"
		width="<%=lScreenWidth%>" height="<%=lScreenHeight%>" type="prefetch">
		<%-- Page menu. --%>

		<%--change date--%>
		<tml:menuItem name="changedate" pageURL="<%=host + "/SelectDate.do"%>"
			text="Change Date" trigger="KEY_MENU">
		</tml:menuItem>
		<!-- [DH] Per PM request, no change date should be in menu list
		<tml:menuRef name="changedate" />
		-->
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
		<%-- Title --%>
		<%
			String pagination = " (" + (currentPage + 1) + "/"
							+ theaterPagesAmount + ")";
		%>
		<tml:title id="searchTheatersTitle" x="0" y="3"
			width="<%=lScreenWidth%>" height="<%=lTitleHeight%>" fontSize="18"
			fontWeight="bold|system_large" align="center" fontColor="<%=lTitleFontColor%>">
			<bean:message key="movies.theaters" />
		</tml:title>


		<%
			int y = lTitleHeight;
					List pageTheaters = (List) request
							.getAttribute("pageTheaters");

					NumberFormat nf = NumberFormat.getInstance();
					nf.setMaximumFractionDigits(1);
					for (int i = 0; i < pageTheaters.size(); i++) {
						Theater t = (Theater) pageTheaters.get(i);
						String distance = nf.format(t.getDistance()
								/ Constants.MILE_METER);
		%>
		<%--select theater--%>
		<tml:menuItem name="<%="selectTheater" + i%>"
			pageURL="<%=host + "/SelectMovie.do"%>"
			trigger="KEY_RIGHT|TRACKBALL_CLICK">
			<tml:bean name="theaterId" valueType="int"
				value="<%=t.getId() + ""%>" />
		</tml:menuItem>



		<%
			StringBuffer detail = new StringBuffer();
						if (t != null) {
							detail.append(MovieUtil.getDetailAddress(t));
							detail.insert(0, "[" + distance + " mi] "
									+ t.getName() + "\r\n");
						}
		%>
		<tml:urlImageLabel id="<%="theater" + i%>" x="0" y="<%=y%>"
			width="<%=lScreenWidth%>" height="<%=lHeight%>" fontSize="16"
			fontWeight="bold|system" align="left" multLine="true">
			<%=MovieUtil.amend(detail.toString())%>
			<tml:menuRef name="<%="selectTheater" + i%>" />
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
		<tml:label id="nullLabel" x="0" y="<%=y%>" width="<%=lScreenWidth%>"
			height="1" fontSize="16" fontWeight="bold|system_huge" align="left">
			<tml:menuRef name="null" />
		</tml:label>
		<%
			}
		%>
	</tml:page>
</tml:TML>
