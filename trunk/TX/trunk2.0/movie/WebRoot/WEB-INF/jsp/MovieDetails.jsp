<%@ include file="header.jsp"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld"
	prefix="tml"%>
<%@page import="com.telenav.tnbrowser.util.Utility,java.io.*"%>
<%@page import="com.telenav.browser.movie.datatype.MovieImage"%>
<%@page import="com.telenav.browser.movie.datatype.Movie"%>
<%@page import="com.telenav.browser.movie.datatype.Theater"%>
<%@page import="com.telenav.browser.movie.util.MovieUtil"%>
<%@page import="com.telenav.j2me.datatypes.TxNode"%>

<tml:TML outputMode="TxNode">
	<%
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
	%>
	<%
		Movie movie = (Movie) request.getAttribute("movie");
			Theater theater = (Theater) request.getAttribute("theater");
			MovieImage bigImage = (MovieImage) request
					.getAttribute("bigImage");
			String shortDate = " (" + request.getAttribute("shortDate")
					+ ")";
			boolean ticketAvailable = ((Boolean) request
					.getAttribute("ticketAvailable")).booleanValue();
			Object o = request.getAttribute("theaterTicketing");

			boolean theaterTicketing;
			if (o == null) {
				theaterTicketing = false;
			} else {
				theaterTicketing = ((Boolean) o).booleanValue();
			}
	%>


	<%
		TxNode node = new TxNode();

			if (theater != null) {
				node = MovieUtil.TheaterToTxNode(theater);

			}
	%>

	<tml:page id="movieDetailsPage" url="/MovieDetails.jsp"
		background="<%=imagePath + "/background_blank.png"%>" x="0" y="0"
		width="<%=lScreenWidth%>" height="<%=lScreenHeight%>" type="net">
		<%----------------------
			Menu and action
		------------------------%>


		<%--buy tickets menu--%>
		<%
			if ((theater != null && theaterTicketing)
							|| (theater == null && ticketAvailable)) {
		%>

		<%--buy tickets menu--%>
		<tml:menuRef name="buyticketsmenu" />
		<%
			}
		%>
		<%
			if (theater != null) {
						String phone = theater.getTelephone();
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
		<%--buy tickets button--%>
		<%
			if (theater != null) {
		%>
		<tml:menuItem name="buytickets" pageURL="<%=host + "/BuyTickets.do"%>"
			trigger="TRACKBALL_CLICK">
			<tml:bean name="movieId" valueType="int"
				value="<%=movie.getId() + ""%>" />
			<tml:bean name="theaterId" valueType="int"
				value="<%=theater.getId() + ""%>" />
		</tml:menuItem>
		<tml:menuItem name="buyticketsmenu"
			pageURL="<%=host + "/BuyTickets.do"%>" text="Buy Tickets"
			trigger="KEY_MENU">
			<tml:bean name="movieId" valueType="int"
				value="<%=movie.getId() + ""%>" />
			<tml:bean name="theaterId" valueType="int"
				value="<%=theater.getId() + ""%>" />
		</tml:menuItem>
		<%
			} else if (theater == null && ticketAvailable) {
		%>
		<tml:menuItem name="buytickets"
			pageURL="<%=host + "/SelectTheater.do"%>" trigger="TRACKBALL_CLICK">
			<tml:bean name="movieId" valueType="int"
				value="<%=movie.getId() + ""%>" />
		</tml:menuItem>
		<tml:menuItem name="buyticketsmenu"
			pageURL="<%=host + "/SelectTheater.do"%>" text="Buy Tickets"
			trigger="KEY_MENU">
			<tml:bean name="movieId" valueType="int"
				value="<%=movie.getId() + ""%>" />
		</tml:menuItem>
		<%
			}
		%>
		<tml:menuItem name="null" trigger="KEY_RIGHT | TRACKBALL_CLICK">
		</tml:menuItem>

		<%-------------------------- 
			Images.
		---------------------------%>
		<tml:title id="movieDetailsTitle" x="0" y="0"
			width="<%=lScreenWidth%>" height="<%=lTitleHeight%>" fontSize="18"
			fontWeight="bold|system_large" align="center"
			fontColor="<%=lTitleFontColor%>">
			<%=MovieUtil.amend(movie.getName())%>
		</tml:title>

		<%
			int y = lTitleHeight + 5;
		%>
		<%--big poster--%>
		<%
			if (bigImage == null) {
						int width = (int) (95f / 140f * (float) lImageHeight);
						int x = (lScreenWidth - width) / 2;
						y += 5;
					} else {
						String bigImgBase64 = Utility
								.byteArrayToBase64(bigImage.getData());
						int width = lImageHeight * bigImage.getWidth()
								/ bigImage.getHeight();
						if (width > lScreenWidth) {
							width = lScreenWidth;
						}
						int x = (lScreenWidth - width) / 2;
		%>

		<tml:image id="bigImage" x="<%=x%>" y="<%=y%>" width="<%=width%>"
			height="<%=lImageHeight%>" url="tempimage_bigImage" />
		<tml:tempImage url="tempimage_bigImage" imagedata="<%=bigImgBase64%>" />


		<tml:urlLabel id="nullLabel1" x="0" y="<%=y%>"
			width="<%=lScreenWidth%>" height="1" fontSize="16"
			fontWeight="bold|system_huge" align="left" isFocusable="true">
			<tml:menuRef name="null" />
		</tml:urlLabel>
		<%
			y += lImageHeight + 5;
					}
		%>
		<%--badge fandango--%>
		<%
			//If theater is selected , they(badge & button) shows according theaterTicketing (in certain date)
					//else, according availability of movie ticket at least one theater (in certain date).
					if ((theater != null && theaterTicketing)
							|| (theater == null && ticketAvailable)) {
		%>
		<tml:image id="fandangoImage" x="<%=lFandangoX%>" y="<%=y%>"
			width="<%=lFandangoWidth%>" height="<%=lFandangoHeight%>"
			url="<%=imagePath + "/fandango.png"%>" />
		<tml:urlLabel id="nullLabel2" x="0" y="<%=y%>"
			width="<%=lScreenWidth%>" height="1" fontSize="16"
			fontWeight="bold|system_huge" align="left" isFocusable="true">
			<tml:menuRef name="null" />
		</tml:urlLabel>

		<%
			y += 60 + 5;
		%>
		<%--buy button--%>
		<tml:button id="buyButton1" x="<%=lButtonX%>" y="<%=y%>"
			width="<%=lButtonWidth%>" height="<%=lButtonHeight%>"
			text="Buy Tickets Now" textVisible="true"
			imageUnclick="<%=imagePath + "/button-a_off.png"%>"
			imageClick="<%=imagePath + "/button-a_on.png"%>" isFocusable="true"
			fontWeight="bold|system_median">
			<tml:menuRef name="buytickets" />
		</tml:button>

		<%
			y += lButtonHeight + 10;
					}
		%>

		<%-------------------------- 
			Text.
		---------------------------%>
		<tml:urlLabel id="nullLabel3" x="0" y="<%=y%>"
			width="<%=lScreenWidth%>" height="1" fontSize="16"
			fontWeight="bold|system_huge" align="left" isFocusable="true">
			<tml:menuRef name="null" />
		</tml:urlLabel>
		<%--movie name--%>
		<tml:label id="movieNameLabel" x="0" y="<%=y%>"
			width="<%=lScreenWidth%>" height="<%=lMovieNameHeight%>"
			fontSize="16" fontWeight="bold|system_huge" align="left|middle">
			<%=MovieUtil.amend(movie.getName())%>
		</tml:label>
		<%
			y += lMovieNameHeight;
					y += lMargin1;
					if (theater != null) {
						StringBuffer detail = new StringBuffer();
						detail.append(MovieUtil.getDetailAddress(theater));
						if (theater.getTelephone() != null
								&& theater.getTelephone().length() > 0) {
							if (detail.length() > 0) {
								detail.append("\r\n");
							}
							detail.append(theater.getTelephone());
						}
		%>

		<%--theater info--%>

		<%--theater name--%>
		<tml:label id="theaterNameLabel" x="0" y="<%=y%>"
			width="<%=lScreenWidth%>" height="<%=lTheaterNameHeight%>"
			fontSize="16" fontWeight="bold|system_median" align="left|bottom">
			<%=MovieUtil.amend(theater.getName())%>
		</tml:label>
		<%
			y += lTheaterNameHeight;
		%>
		<%--theater detail address & telephone--%>
		<tml:label id="theaterDetailsLabel" x="0" y="<%=y%>"
			width="<%=lScreenWidth%>" height="<%=lTheaterInfoHeight%>"
			fontSize="16" fontWeight="system_median" align="left|top">
			<%=MovieUtil.amend(detail.toString())%>
		</tml:label>
		<%
			y += lTheaterInfoHeight;
						y += lMargin2;
		%>
		<%--show times--%>
		<%
			String showTime = (String) request
								.getAttribute("showTime");
						if (showTime == null) {
							showTime = "";
						}
						showTime = MovieUtil.amend("<bold>Showtimes"
								+ shortDate + ":</bold> " + showTime);
		%>
		<tml:label id="showTimesLabel" x="0" y="<%=y%>"
			width="<%=lScreenWidth%>" height="<%=lShowtimesHeight * 3%>"
			fontSize="16" fontWeight="system_median" align="left|top">
			<%=showTime%>
		</tml:label>
		<tml:urlLabel id="nullLabel4" x="0" y="<%=y%>"
			width="<%=lScreenWidth%>" height="1" fontSize="16"
			fontWeight="bold|system_huge" align="left" isFocusable="true">
			<tml:menuRef name="null" />
		</tml:urlLabel>

		<%
			y += lShowtimesHeight * 3 + lMargin2;
					}
		%>
		<%--rating&grade&runTime--%>
		<%
			String ratingImageUrl = imagePath + "/line_"
							+ ((int) (movie.getRating() * 2)) + ".png";

					String s = movie.getGrade();
					if (s == null) {
						s = "";
					}
					if (movie.getRunTime() != null
							&& movie.getRunTime().length() > 0) {
						if (s.length() > 0) {
							s += ", ";
						}
						s += MovieUtil.timeFormat(movie.getRunTime());
					}
					s = MovieUtil.amend(s);
		%>
		<tml:image id="ratingImage" x="0" y="<%=y%>" width="87" height="20"
			url="<%=ratingImageUrl%>" />
		<tml:label id="runTimeLabel" x="95" y="<%=y%>"
			width="<%=lScreenWidth%>" height="25" fontSize="16"
			fontWeight="bold|system_median" align="left|middle">
			<%=s%>
		</tml:label>
		<tml:urlLabel id="nullLabel5" x="90" y="<%=y%>"
			width="<%=lScreenWidth%>" height="1" fontSize="16"
			fontWeight="bold|system_huge" align="left" isFocusable="true">
			<tml:menuRef name="null" />
		</tml:urlLabel>
		<%
			y += 25;
		%>
		<%--movie genres--%>
		<%
			String gernes = (String) movie.getGenres();
					if (gernes == null) {
						gernes = "";
					}
					gernes = MovieUtil.amend("<bold>Genres:</bold> " + gernes);
		%>
		<tml:label id="gernesLabel" x="0" y="<%=y%>" width="<%=lScreenWidth%>"
			height="<%=lGenresHeight%>" fontSize="16" fontWeight="system_median"
			align="left|top">
			<%=gernes%>
		</tml:label>

		<tml:urlLabel id="nullLabel6" x="0" y="<%=y%>"
			width="<%=lScreenWidth%>" height="1" fontSize="16"
			fontWeight="bold|system_huge" align="left" isFocusable="true">
			<tml:menuRef name="null" />
		</tml:urlLabel>
		<%
			y += lGenresHeight + lMargin2;
		%>
		<%--movie cast--%>
		<%
			String cast = (String) movie.getCast();
					if (cast == null) {
						cast = "";
					}
					cast = MovieUtil.amend("<bold>Cast:</bold> " + cast);
		%>
		<tml:label id="castLabel" x="0" y="<%=y%>" width="<%=lScreenWidth%>"
			height="<%=lGenresHeight%>" fontSize="16" fontWeight="system_median"
			align="left|bottom">
			<%=cast%>
		</tml:label>
		<%
			y += lGenresHeight;
		%>
		<%--movie director--%>
		<%
			String director = (String) movie.getDirector();
					if (director == null) {
						director = "";
					}
					director = MovieUtil.amend("<bold>Director:</bold> "
							+ director);
		%>
		<tml:label id="directorLabel" x="0" y="<%=y%>"
			width="<%=lScreenWidth%>" height="<%=lGenresHeight%>" fontSize="16"
			fontWeight="system_median" align="left|top">
			<%=director%>
		</tml:label>

		<tml:urlLabel id="nullLabel7" x="0" y="<%=y%>"
			width="<%=lScreenWidth%>" height="1" fontSize="16"
			fontWeight="bold|system_huge" align="left|top" isFocusable="true">
			<tml:menuRef name="null" />
		</tml:urlLabel>
		<%
			y += lGenresHeight;
		%>
		<%--movie description--%>
		<%
			String description = (String) movie.getDescription();
					if (description == null) {
						description = "";
					}
					description = MovieUtil.amend("<bold>Description:</bold> "
							+ description);
		%>
		<tml:label id="descriptionLabel" x="0" y="<%=y%>"
			width="<%=lScreenWidth%>" height="<%=lGenresHeight * 3%>"
			fontSize="16" fontWeight="system_median" align="left|middle"
			textWrap="wrap">
			<%=description%>
		</tml:label>
		<%
			y += lGenresHeight * 3 + lMargin2;
					//If theater is selected , they(badge & button) shows according theaterTicketing (in certain date)
					//else, according availability of movie ticket at least one theater (in certain date).
					if ((theater != null && theaterTicketing)
							|| (theater == null && ticketAvailable)) {
		%>
		<%--buy button--%>
		<tml:button id="buyButton2" x="<%=lButtonX%>" y="<%=y%>"
			width="<%=lButtonWidth%>" height="<%=lButtonHeight%>"
			text="Buy Tickets Now" textVisible="true"
			imageUnclick="<%=imagePath + "/button-a_off.png"%>"
			imageClick="<%=imagePath + "/button-a_on.png"%>" isFocusable="true"
			fontWeight="bold|system_median">
			<tml:menuRef name="buytickets" />
		</tml:button>
		<%
			y += lButtonHeight + lMargin2;
					}
		%>
		<tml:label id="nullLabel8" x="0" y="<%=y%>" width="<%=lScreenWidth%>"
			height="1" fontSize="16" fontWeight="bold|system_huge" align="left">
			<tml:menuRef name="null" />
		</tml:label>
	</tml:page>
</tml:TML>
