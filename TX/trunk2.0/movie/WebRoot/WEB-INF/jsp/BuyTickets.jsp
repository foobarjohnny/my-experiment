<%@ include file="header.jsp"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>

<%@page import="com.telenav.browser.movie.datatype.Movie"%>
<%@page import="com.telenav.browser.movie.datatype.Theater"%>
<%@page import="com.telenav.browser.movie.datatype.Schedule"%>
<%@page import="com.telenav.browser.movie.datatype.MovieImage"%>
<%@page import="com.telenav.browser.movie.util.MovieUtil"%>
<%@page import="com.telenav.browser.movie.Constants"%>
<%@page import="com.telenav.tnbrowser.util.Utility,java.io.*"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Locale"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.telenav.j2me.datatypes.TxNode"%>

<tml:TML outputMode="TxNode">
	<%
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
	%>
	<%
		Movie movie = (Movie) request.getAttribute("movie");
			Theater theater = (Theater) request.getAttribute("theater");
			String theaterName = theater.getName();
			if (theaterName == null) {
				theaterName = "";
			}
			StringBuffer detail = new StringBuffer();
			detail.append(MovieUtil.getDetailAddress(theater));
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
		TxNode node = new TxNode();

			if (theater != null) {
				node = MovieUtil.TheaterToTxNode(theater);

			}
	%>

	<tml:page id="buyTicketsPage" url="/BuyTickets.jsp"
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
		<tml:title id="buyTicketsTitle" x="0" y="3" width="<%=lScreenWidth%>"
			height="<%=lTitleHeight%>" fontSize="18"
			fontWeight="bold|system_large" align="center" fontColor="<%=lTitleFontColor%>">
			<bean:message key="movies.buymovietickets" />
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
		<%
			int y = 45;
		%>
		<%--movie name--%>
		<tml:label id="movieNameLabel" x="<%=x%>" y="<%=y%>"
			width="<%=lScreenWidth - x%>" height="<%=lMovieNameHeight%>"
			fontSize="25" fontWeight="bold|system" align="left">
			<%=MovieUtil.amend(movie.getName())%>
		</tml:label>
		<%
			y += lMovieNameHeight;
		%>
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
			y = 45 + lPicHeight + 7;
		%>
		<%--buy tickets--%>
		<%
			List list = (List) request.getAttribute("scheduleList");
					Schedule s;
					String wapLink;
					String buyText;
					String date = (String) request.getAttribute("date");
					String quals;
					String trueDateString;
					String numberDateString;
					String showTime;
					String trueShowTime;
					int marginDay;
					for (int i = 0; i < list.size(); i++) {
						s = (Schedule) list.get(i);
						if (s.getTicketURI() == null
								|| s.getTicketURI().length() < 1) {
							continue;
						}
						quals = s.getQuals();
						if (quals == null || quals.length() < 1) {
							quals = "";
						} else {
							quals = " \r\n(" + quals + ")";
						}
						showTime = s.getShowTime();
						marginDay = MovieUtil.getMarginDay(showTime);
						if (marginDay == 0) {
							trueDateString = date;
						} else {
							trueDateString = Constants.DATE_FORMAT
									.format(MovieUtil.getTrueDate(s.getDate(),
											showTime));
						}
						trueShowTime = MovieUtil.formatShowTime(showTime);
						numberDateString = Constants.NUMBER_DATE_FORMAT
								.format(MovieUtil.getTrueDate(s.getDate(),
										trueShowTime));
						wapLink = MovieUtil.revise(MovieUtil.getWapLink(s
								.getVendorName(), trueShowTime, s
								.getTicketURI(), numberDateString, theater
								.getVendorId()));
						buyText = MovieUtil.amend("BUY - "
								+ MovieUtil.formatShowTimeAll(showTime) + " "
								+ trueDateString + quals);
		%>
		<tml:actionItem name="<%="goFandango" + i%>"
			action="LocalService_invokePhoneBrowser">

			<tml:input>

				<tml:bean name="url" valueType="string" value="<%=wapLink%>" />

			</tml:input>

		</tml:actionItem>
		<tml:menuItem name="<%="movies::buytickets" + i%>"
			trigger="KEY_RIGHT | TRACKBALL_CLICK"
			actionRef="<%="goFandango" + i%>" />
		<tml:urlImageLabel id="<%="buyTicketsLabel"+i %>" x="0" y="<%=y%>"
			width="<%=lScreenWidth%>" height="<%=lHeight%>" fontSize="16"
			fontWeight="bold|system" align="left" multLine="true">
			<%=buyText%>
			<tml:menuRef name="<%="movies::buytickets" + i%>" />
		</tml:urlImageLabel>
		<%
			y += lHeight + lSpace;
					}
		%>


		<%
			y += 15;
		%>
		<%--badge fandango--%>
		<tml:image id="fandango" x="<%=lFandangoX%>" y="<%=y%>"
			width="<%=lFandangoWidth%>" height="<%=lFandangoHeight%>"
			url="<%=imagePath + "/fandango.png"%>" />
		<%
			y += 50 + 15;
		%>

		<tml:label id="nullLabel" x="60" y="<%=y%>" width="<%=lScreenWidth%>"
			height="1" fontSize="16" fontWeight="system" align="left">
		</tml:label>
	</tml:page>
</tml:TML>
