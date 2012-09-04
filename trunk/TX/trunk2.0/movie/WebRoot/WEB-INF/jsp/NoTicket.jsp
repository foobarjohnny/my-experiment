<%@ include file="header.jsp"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>

<%@page import="com.telenav.browser.movie.datatype.Movie"%>
<%@page import="com.telenav.browser.movie.datatype.Theater"%>
<%@page import="com.telenav.browser.movie.datatype.Schedule"%>
<%@page import="com.telenav.browser.movie.datatype.MovieImage"%>
<%@page import="com.telenav.browser.movie.util.MovieUtil"%>
<%@page import="com.telenav.tnbrowser.util.Utility,java.io.*"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Locale"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.telenav.j2me.datatypes.TxNode"%>


<%
	Movie movie = (Movie) request.getAttribute("movie");
	Theater theater = (Theater) request.getAttribute("theater");
	//Short date
	String shortDate = " ("
			+ (String) request.getAttribute("shortDate") + ")";
	String showTime = (String) request.getAttribute("showTime");
	//Theater detail info
	StringBuffer detail = new StringBuffer();
	detail.append(MovieUtil.getDetailAddress(theater));
	String theaterName = theater.getName();
	if (theaterName == null) {
		theaterName = "";
	}
	if (theater.getTelephone() != null
			&& theater.getTelephone().length() > 0) {
		if (detail.length() > 0) {
			detail.append("\r\n");
		}
		detail.append(theater.getTelephone());
	}
	String phone = theater.getTelephone();
%>
<%
	int lPicWidth = layout
			.getIntProperty("movies.no.ticket.pic.max.width");
	int lPicHeight = layout
			.getIntProperty("movies.no.ticket.pic.max.height");
	int lMovieNameHeight = layout
			.getIntProperty("movies.no.ticket.movie.name.height");
	int lShowtimesHeight = layout
			.getIntProperty("movies.no.ticket.showtimes.height");
%>

<%
	TxNode node = new TxNode();

	if (theater != null) {
		node = MovieUtil.TheaterToTxNode(theater);
	}
%>
<tml:TML outputMode="TxNode">
	<tml:page id="noTicketPage" url="/NoTicket.jsp"
		background="<%=imagePath + "/background_blank.png"%>" x="0" y="0"
		width="<%=lScreenWidth%>" height="<%=lScreenHeight%>" type="net">


		<%
			if (theater != null) {
		%>
		<tml:actionItem name="Nav" action="doNav">
			<tml:input>
				<tml:bean name="position" valueType="TxNode"
					value="<%=Utility.TxNode2Base64(node)%>"></tml:bean>
			</tml:input>
		</tml:actionItem>
		<tml:menuRef name="Nav"></tml:menuRef>
		<tml:menuItem name="Nav" text="Drive To" trigger="KEY_MENU"
			actionRef="Nav"></tml:menuItem>


		<tml:actionItem name="mapPoi" action="mapIt">
			<tml:input>
				<tml:bean name="position" valueType="TxNode"
					value="<%=Utility.TxNode2Base64(node)%>"></tml:bean>
			</tml:input>
		</tml:actionItem>
		<tml:menuRef name="mapPoi"></tml:menuRef>
		<tml:menuItem name="mapPoi" text="Map It" trigger="KEY_MENU"
			actionRef="mapPoi"></tml:menuItem>

		<%
			}
		%>

		<%--call phone--%>
		<%
			if (phone != null && phone.length() > 0) {
						String callPhone = "Call " + phone;
						phone = phone.replaceAll("-", "").trim();
		%>
		<tml:actionItem name="makePhoneCallAction" action="makePhoneCall">
			<tml:input>
				<tml:bean name="phonenumber" valueType="string" value="<%=phone%>" />
			</tml:input>
		</tml:actionItem>
		<tml:menuItem name="callphone" actionRef="makePhoneCallAction"
			text="<%=callPhone%>" trigger="KEY_MENU">
		</tml:menuItem>
		<tml:menuRef name="callphone" />
		<%
			}
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
		<tml:title id="noTicketTitle" x="0" y="3" width="<%=lScreenWidth%>"
			height="<%=lTitleHeight%>" fontSize="18"
			fontWeight="bold|system_large" align="center" fontColor="<%=lTitleFontColor%>">
			<bean:message key="movies.noticket" /><%=shortDate%>
		</tml:title>

		<%--small poster--%>
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
			<%=MovieUtil.amend(movie.getName())%>
		</tml:label>
		<%--theater name&address&telephone--%>
		<%
			String theaterInfo = MovieUtil.amend("<bold>" + theaterName
							+ "</bold>\r\n" + detail.toString());
		%>
		<tml:label id="theaterInfoLabel" x="<%=x%>"
			y="<%=45 + lMovieNameHeight%>" width="<%=lScreenWidth - x%>"
			height="<%=lPicHeight - lMovieNameHeight%>" fontSize="16"
			fontWeight="system_median" align="left|bottom">
			<%=theaterInfo%>
		</tml:label>

		<%
			int y = 45 + lPicHeight + 7;
					y += 20;
		%>
		<%--show times--%>
		<%
			String showTimes = MovieUtil.amend("Showtimes" + shortDate
							+ ":");
		%>
		<tml:label id="showTimesLabel" x="0" y="<%=y%>"
			width="<%=lScreenWidth%>" height="<%=lShowtimesHeight%>"
			fontSize="16" fontWeight="bold|system_median" align="left|bottom">
			<%=showTimes%>
		</tml:label>
		<%
			y += lShowtimesHeight;
		%>
		<tml:label id="showTimesContentLabel" x="0" y="<%=y%>"
			width="<%=lScreenWidth%>" height="<%=lShowtimesHeight * 3%>"
			fontSize="16" fontWeight="system_median" align="left|top">
			<%=MovieUtil.amend(showTime)%>
		</tml:label>
		<%--movie details button--%>
		<tml:menuItem name="movies::moviedetails"
			pageURL="<%=host + "/MovieDetails.do"%>"
			trigger="KEY_RIGHT | TRACKBALL_CLICK">
			<tml:bean name="movieId" valueType="int"
				value="<%=movie.getId() + ""%>" />
			<tml:bean name="theaterId" valueType="int"
				value="<%=theater.getId() + ""%>" />
		</tml:menuItem>
		<tml:urlImageLabel id="movieDetailsLabel" x="0"
			y="<%=lScreenHeight - lHeight - 2%>" width="<%=lScreenWidth%>"
			height="<%=lHeight%>" fontSize="16" fontWeight="bold|system"
			align="left" multLine="true">
			<bean:message key="movies.seemoviedetails" />
			<tml:menuRef name="movies::moviedetails" />
		</tml:urlImageLabel>
	</tml:page>
</tml:TML>
