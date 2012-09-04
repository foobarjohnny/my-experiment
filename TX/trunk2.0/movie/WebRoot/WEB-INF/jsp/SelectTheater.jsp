<%@ include file="header.jsp"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>

<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="com.telenav.browser.movie.Constants"%>
<%@page import="com.telenav.browser.movie.util.MovieUtil"%>
<%@page import="com.telenav.browser.movie.datatype.Movie"%>
<%@page import="com.telenav.browser.movie.datatype.Schedule"%>
<%@page import="com.telenav.browser.movie.datatype.Theater"%>



<tml:TML outputMode="TxNode">

	<tml:page id="selectTheaterPage" url="/SelectTheater.jsp"
		background="<%=imagePath + "/background_blank.png"%>" x="0" y="0"
		width="<%=lScreenWidth%>" height="<%=lScreenHeight%>" type="net">
		<%
			String shortDate = " ("
							+ (String) request.getAttribute("shortDate") + ")";
		%>
		<%-- Tile --%>
		<tml:title id="selectTheaterTitle" x="0" y="3"
			width="<%=lScreenWidth%>" height="<%=lTitleHeight%>" fontSize="18"
			fontWeight="bold|system_large" align="center" fontColor="<%=lTitleFontColor%>">
			<bean:message key="movies.selecttheater.title" /><%=shortDate%>
		</tml:title>
		<%-- Page menu. --%>
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
		<%--select theater--%>
		<%
			Map scheduleMap = (Map) request
							.getAttribute("ticketScheduleMap");
					List theaterList = (List) request
							.getAttribute("ticketTheaters");
					Movie m = (Movie) request.getAttribute("movie");
					NumberFormat nf = NumberFormat.getInstance();
					nf.setMaximumFractionDigits(1);
					int y = lTitleHeight;
					for (int i = 0; i < theaterList.size(); i++) {
						Theater t = (Theater) theaterList.get(i);

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
		<tml:menuItem name="<%="selectTheater" + i%>"
			pageURL="<%=host + "/BuyTickets.do"%>"
			trigger="KEY_RIGHT | TRACKBALL_CLICK">
			<tml:bean name="theaterId" valueType="int"
				value="<%=t.getId() + ""%>" />
			<tml:bean name="movieId" valueType="int" value="<%=m.getId() + ""%>" />
		</tml:menuItem>
		<tml:urlImageLabel id="<%="theater" + i%>" x="0" y="<%=y%>"
			width="<%=lScreenWidth%>" height="<%=lHeight%>" fontSize="16"
			fontWeight="bold|system" align="left" multLine="true">
			<%=info%>
			<tml:menuRef name="<%="selectTheater" + i%>" />
		</tml:urlImageLabel>
		<%
			y += lHeight + lSpace;
					}
		%>
	</tml:page>
</tml:TML>