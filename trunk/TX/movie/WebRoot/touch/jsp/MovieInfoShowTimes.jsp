<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp"%>

<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>

<%@page import="com.telenav.browser.movie.Constant" %>
<%@page import="com.telenav.datatypes.content.movie.v10.Movie" %>
<%@page import="com.telenav.datatypes.content.movie.v10.Schedule" %>
<%@page import="com.telenav.datatypes.content.tnpoi.v10.TnPoi" %>
<%@page import="com.telenav.browser.movie.Util" %>
<%@page import="java.util.Map" %>
<%@page import="com.telenav.browser.movie.datatypes.RatingImage"%>

<%
    String imageBg = imageUrl + "background_telenav.png";
    String pageURL = host + "/MovieInfoShowTimes.jsp";

	Movie movie = (Movie) request.getAttribute("MovieDetails");
	String shortDate = "(" + (String) request.getAttribute("dateDisplay") + ")";
	Boolean onlineTickets = (Boolean)request.getAttribute("HasTickets");
	boolean hasTickets = onlineTickets != null?onlineTickets.booleanValue():false;
	
	// get attributes for anchor lat, lon, distUnit and scale
	long   anchorLat = ((Long)request.getAttribute("anchorLat")).longValue();
	long   anchorLon = ((Long)request.getAttribute("anchorLon")).longValue();
	long   distUnit  = ((Long)request.getAttribute("distUnit")).longValue();
	String scale     = (String) request.getAttribute("scale");

	// set useMiles and scale values
	boolean useMiles = "mi".equals(scale);
	if (useMiles){
		scale = msg.get("time.distance.mi");
	}else{
		scale = msg.get("time.distance.km");
	}

	String firstTicketable = null;
	
	boolean buyTicket = !Util.isCanadianCarrier(handlerGloble);

%>

<tml:TML outputMode="TxNode">

	<tml:script language="fscript" version="1">
		<![CDATA[
		
			func onShow()
				String key = "<%=Constant.StorageKey.MOVIE_FROM_DP%>"
				String fromDP = TempCache.getString(key, "N")
				if fromDP == "Y"
					TempCache.deleteString(key)
					System.showMsgWithTimeout("<%=msg.get("time.msg.choose")%>", 3)
					String id = firstTicketable()
					Page.setControlProperty(id,"focused","true")
				endif
			endfunc
			
			func preLoad()
				Page.setControlProperty("t0","focused","true")
			endfunc
			
			func OnDateChange()
				String url = "<%=host + "/MovieInfoShowTimes.do"%>" 
				SelectDate_C_showDateListInterface(url, NULL, -1, <%=movie.getId()%>)
				return FAIL
			endfunc
			
			]]>
	</tml:script> 
	
	<jsp:include page="MenuItems.jsp" />
	<jsp:include page="controller/SelectDateController.jsp" />

	<tml:page id="movieShowTimes" url="<%=pageURL%>" groupId="<%=MOVIE_GROUP_ID%>"
		background="<%=imageBg%>" type="net" helpMsg="$//$moviesshowtime">


		<%--title--%>
		<tml:title id="title" fontWeight="bold|system_large" 
				   align="center" fontColor="white">
			<![CDATA[ <%=msg.get("time.title")%> <%=shortDate%> ]]>
		</tml:title>
		<tml:menuRef name="ChangeDate" />
		<tml:menuRef name="NewSearch" />

	<tml:panel id="infoPanel">

		<%
		    int x = 0;
            String smallImgBase64 = (String) request.getAttribute("smallImage");
            if (smallImgBase64 != null) {
		%>
		
		<%--small poster--%>
		<tml:image id="smallImage" url="tempimage_smallImage" />
		<tml:tempImage url="tempimage_smallImage"
			imagedata="<%=smallImgBase64%>" />
		<%
            }else{
		%>
		<tml:image id="smallImage" url="<%=imageUrl + "movie_image_80x120.png"%>" />
		<%  
            }
		%>
		<%--movie name--%>
		<tml:label id="nameLabel" fontSize="18" align="left" textWrap="wrap">
			<![CDATA[ <bold><%=movie.getName()%></bold> ]]>
		</tml:label>
		
		<%--rating--%>
		<%
			RatingImage rImage = (RatingImage) request.getAttribute("rImage");
		%>
			
		<tml:image id="ratingImage1" url="<%=rImage.getStarImage1()%>" />
		<tml:image id="ratingImage2" url="<%=rImage.getStarImage2()%>" />
		<tml:image id="ratingImage3" url="<%=rImage.getStarImage3()%>" />
		<tml:image id="ratingImage4" url="<%=rImage.getStarImage4()%>" />

		<%--grade & runTime--%>
		<tml:label id="detailLabel" fontSize="18" align="left" fontColor="#666666">
			<%
			    StringBuffer detail = new StringBuffer();
				boolean hasGrade = false;
			    if (movie.getGrade() != null && movie.getGrade().length() > 0) {
			    	detail.append(movie.getGrade());
			    	hasGrade = true;
			    }
			    if (movie.getRuntime() != null && movie.getRuntime().length() > 0) {
			    	if (hasGrade) {
			    		detail.append(", ");
			    	}
			    	detail.append(Util.timeFormat(movie.getRuntime())); 
			    }
			%>
			<![CDATA[ <%=detail.toString()%> ]]>
		</tml:label>

		<%--cast--%>
		<tml:label id="castLabel" fontSize="16" align="left" fontColor="#666666" textWrap="wrap">
		<!--  tml:multiline id="castLabel"  fontSize="16" align="left|middle"  -->
			<%
			    String cast = Util.makeOneString(movie.getCast(), ", ");
			%>
			<![CDATA[ <%=cast%> ]]>
			<!--  /tml:multiline -->
		</tml:label>
		
		<tml:label id="mDetails" fontSize="14" align="left" textWrap="wrap" fontColor="#666666">
			<%
			    String mD = movie.getDescription();
				if (mD == null) mD = "";
			%>
			<![CDATA[ <%=mD%> ]]>
		</tml:label>
		
		<%--movie details button--%>
		<tml:menuItem name="MovieDetails" pageURL="<%=host + "/MovieDetails.do"%>" text="Movie Details">
			<tml:bean name="movieId" valueType="String" value="<%=movie.getId()%>" />
			<tml:bean name="hasTickets" valueType="String" value="<%="" + hasTickets%>" />
			<tml:bean name="anchorLat" valueType="int" value="<%= "" + anchorLat %>" />
			<tml:bean name="anchorLon" valueType="int" value="<%= "" + anchorLon %>" />
			<tml:bean name="distUnit"  valueType="int" value="<%= "" + distUnit %>" />
		</tml:menuItem>
		
		<tml:urlLabel id="MDImageLabel" fontSize="16" align="center|top" 
				fontColor="#005AFF" getFocus="false">
			<%=msg.get("see.more")%>
			<tml:menuRef name="MovieDetails" />
		</tml:urlLabel>

		<%-- tml:listBox id="TheatersBox" isFocusable="true" hotKeyEnable="false" --%>
<% 
			
		TnPoi[] theaters = (TnPoi[]) request.getAttribute("Theaters");
		if (theaters != null) {
			Map<String,Schedule> schedules = (Map<String,Schedule>) request.getAttribute("Schedules");
			Schedule sched;
			int counter = theaters.length >10? 10 : theaters.length;
			for(int i=0; i<counter; i++){
				String tid="";
				String sid="";
				double t_lat = theaters[i].getAddress().getGeoCode().getLatitude();
				double t_lon = theaters[i].getAddress().getGeoCode().getLongitude();
				
				// get distance in meters between anchor location and address location
				int meters = Util.calDistanceInMeter(t_lat, t_lon, anchorLat/Constant.DEGREE_MULTIPLIER, anchorLon/Constant.DEGREE_MULTIPLIER);
				String distance = null;
				if (useMiles){
					distance = Util.distanceInMiles(meters);
				}else{
					distance = Util.distanceInKilometers(meters);
				}

				distance = "(" + distance + " " + scale + ")";

				StringBuffer info = new StringBuffer();
				info.append("<bold>").append(theaters[i].getBrandName()).append("</bold>");

				StringBuffer detailAddress = new StringBuffer();
				detailAddress.append(Util.getDetailAddress(theaters[i])).append("\r\n");

				tid = "" + theaters[i].getPoiId();
				sched = schedules.get(tid);
				String showTime = null;
				boolean ticketable = false;
				if (sched != null){
					showTime = Util.getShowTimes(sched);
					sid = sched.getId();
					ticketable = sched.getTicketURI() != null && sched.getTicketURI().length() > 0 && !Util.isCanadianCarrier(handlerGloble);
					if (firstTicketable == null && ticketable) 
						firstTicketable = "t" + i;
				}else{
					showTime = msg.get("time.na");
				}
%>
			<%--buy tickets button--%>
			<tml:menuItem name="<%="buyTickets" + i%>" pageURL="<%=host + "/BuyTickets.do"%>"  trigger="TRACKBALL_CLICK">
				<tml:bean name="theaterId" valueType="String" value="<%="" + tid%>" />
				<tml:bean name="movieId" valueType="String" value="<%=movie.getId() + ""%>" />
				<tml:bean name="scheduleId" valueType="String" value="<%=sid%>" />
				<tml:bean name="anchorLat" valueType="int" value="<%= "" + anchorLat %>" />
				<tml:bean name="anchorLon" valueType="int" value="<%= "" + anchorLon %>" />
				<tml:bean name="distUnit" valueType="int" value="<%= "" + distUnit %>" />
			</tml:menuItem>
			<tml:compositeListItem id="<%="t" + i%>" 
				height="45" width="480" visible="true" bgColor="#FFFFFF"
				transparent="false"
				isFocusable="true">
				<tml:label id="<%="t_distance" + i%>" textWrap="ellipsis"
					fontSize="16" focusFontColor="#FFF000" fontColor="#005AFF" align="left">
					<%=distance%>
				</tml:label>
				<tml:label id="<%="t_detailAddress" + i%>" focusFontColor="white" fontColor="#666666"
					fontSize="16" textWrap="ellipsis" align="left">
					<![CDATA[<%=detailAddress%>]]>
				</tml:label>
				<tml:label id="<%="show_t" + i%>" textWrap="ellipsis"
					fontSize="16" focusFontColor="white" fontColor="#004480" align="left">
					<%=showTime%>
				</tml:label>
				<% if (ticketable){%>
				<tml:label id="<%="t_info" + i%>" focusFontColor="white"
					fontSize="18" textWrap="ellipsis" align="left">
					<![CDATA[<%=info%>]]>
				</tml:label>
				<% if (buyTicket) { %>
					<tml:image id="<%="ticket_" + i%>" url="" />
				<% } %>
				
				<% }else{%>
				<tml:label id="<%="t_info_b" + i%>" focusFontColor="white"
					fontSize="18" textWrap="ellipsis" align="left">
					<![CDATA[<%=info%>]]>
				</tml:label>
				<%}%>
				<tml:menuRef name="<%="buyTickets" + i%>" />
				<tml:menuRef name="ChangeDate" />
				<tml:menuRef name="NewSearch" />
			</tml:compositeListItem>
<% 
			}
		}else{ 
%>
		<tml:urlImageLabel id="t0" fontWeight="bold|system_large" align="left">
			<![CDATA[<%=msg.get("time.na")%>]]>
				<tml:menuRef name="ChangeDate" />
				<tml:menuRef name="NewSearch" />
		</tml:urlImageLabel>

<% 			
		}
%>
		<%-- /tml:listBox --%>
		
	</tml:panel>
	<tml:image id="titleShadow" align="left|top"/>
	</tml:page>
	<%  if (firstTicketable != null){%>
	<tml:script language="fscript" version="1">
		<![CDATA[
		
			func firstTicketable()
				return "<%=firstTicketable%>"
			endfunc
			
			]]>
	</tml:script> 
	<% 
		}
	%>
	<cserver:outputLayout />
</tml:TML>		
