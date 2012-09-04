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

<%
    String imageBg = imageUrl + "background_telenav.png";
    String pageURL = host + "/MovieInfoShowTimes.jsp";
    String twoLinesImageFocus = imageUrl + "2line_list_highlight.png";
    String twoLinesImageBlur = imageUrl + "2line_list.png";

	Movie movie = (Movie) request.getAttribute("MovieDetails");
	String shortDate = "(" + (String) request.getAttribute("dateDisplay") + ")";
	Boolean onlineTickets = (Boolean)request.getAttribute("HasTickets");
	boolean hasTickets = onlineTickets != null?onlineTickets.booleanValue():false;
	double lat = ((Long)request.getAttribute("address_lat")).longValue()/Constant.DEGREE_MULTIPLIER;
	double lon = ((Long)request.getAttribute("address_lon")).longValue()/Constant.DEGREE_MULTIPLIER;
	
	String scale = (String) request.getAttribute("scale");
	boolean useMiles = "mi".equals(scale);
	String firstTicketable = null;

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
			endfunc
			
			]]>
	</tml:script> 
	
	<jsp:include page="MenuItems.jsp" />
	<jsp:include page="controller/SelectDateController.jsp" />

	<tml:page id="movieShowTimes" url="<%=pageURL%>" groupId="<%=MOVIE_GROUP_ID%>"
		background="<%=imageBg%>" type="net" helpMsg="$//$moviesinfo">

		<%--title--%>
		<tml:title id="title" fontWeight="bold|system_large" 
				   align="center" fontColor="white">
			<![CDATA[ <%=msg.get("time.title")%> <%=shortDate%> ]]>
		</tml:title>
		<tml:menuRef name="ChangeDate" />
		<tml:menuRef name="NewSearch" />
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
		<tml:label id="nameLabel" fontSize="18" align="left">
			<![CDATA[ <bold><%=movie.getName()%></bold> ]]>
		</tml:label>
		
		<%--rating--%>
		<%
		    double rating = Double.parseDouble(movie.getRating());
			String rImage = imageUrl + "line_"
		                    + ((int) (rating * 2)) + ".png";
		%>
		<tml:image id="ratingImage" url="<%=rImage%>" />
		
		<%--grade & runTime--%>
		<tml:label id="detailLabel" fontSize="18" align="left">
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
		<tml:label id="castLabel" fontSize="16" align="left" textWrap="ellipsis">
			<%
			    String cast = Util.makeOneString(movie.getCast(), ", ");
			%>
			<![CDATA[ <%=cast%> ]]>
		</tml:label>
		
		<tml:label id="mDetails" fontSize="16" align="left" textWrap="wrap">
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
		</tml:menuItem>
		
		<tml:urlLabel id="MDImageLabel" fontSize="16" align="center|top" 
				fontColor="#005AFF" getFocus="false">
			<%=msg.get("see.more")%>
			<tml:menuRef name="MovieDetails" />
		</tml:urlLabel>

		<tml:listBox id="TheatersBox" isFocusable="true" hotKeyEnable="false">
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
				int meters = Util.calDistanceInMeter(t_lat, t_lon, lat, lon);
				String distance = null;
				if (useMiles){
					scale = msg.get("time.distance.mi");
					distance = Util.distanceInMiles(meters);
				}else{
					scale = msg.get("time.distance.km");
					distance = Util.distanceInKilometers(meters);
				}
				distance = "(" + distance + " " + scale + ")";
				StringBuffer info = new StringBuffer();
				info.append("<bold>").append(theaters[i].getBrandName()).append("</bold>, ");
				info.append(Util.getDetailAddress(theaters[i])).append("\r\n");
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
			</tml:menuItem>
			<tml:compositeListItem id="<%="t" + i%>" 
				height="45" width="480" visible="true" bgColor="#FFFFFF"
				transparent="false"
				focusBgImage="<%=twoLinesImageFocus%>"
				blurBgImage="<%=twoLinesImageBlur%>"
				isFocusable="true">
				<tml:label id="<%="t_distance" + i%>" textWrap="ellipsis"
					fontSize="14" focusFontColor="#FFF000" fontColor="#005AFF" align="left">
					<%=distance%>
				</tml:label>
				<tml:label id="<%="t_info" + i%>" focusFontColor="white"
					fontSize="16" textWrap="ellipsis" align="left">
					<![CDATA[<%=info%>]]>
				</tml:label>
				<tml:label id="<%="show_t" + i%>" textWrap="ellipsis"
					fontSize="14" focusFontColor="white" fontColor="red" align="left">
					<%=showTime%>
				</tml:label>
				<% if (ticketable){%>
				<tml:image id="<%="ticket_" + i%>" url="<%=imageUrl + "ticketicon.png"%>" />
				<% }%>
				<tml:menuRef name="<%="buyTickets" + i%>" />
				<tml:menuRef name="ChangeDate" />
				<tml:menuRef name="NewSearch" />
			</tml:compositeListItem>
<% 
			}
		}else{ 
%>
		<tml:listItem id="t0" fontWeight="bold|system_large" align="left">
			<![CDATA[<%=msg.get("time.na")%>]]>
				<tml:menuRef name="ChangeDate" />
				<tml:menuRef name="NewSearch" />
		</tml:listItem>

<% 			
		}
%>
		</tml:listBox>
		
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