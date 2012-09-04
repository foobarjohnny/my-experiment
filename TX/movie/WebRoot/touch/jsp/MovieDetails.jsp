<%@ include file="Header.jsp"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>

<%@page import="com.telenav.datatypes.content.movie.v10.Movie"%>
<%@page import="com.telenav.datatypes.content.movie.v10.Schedule"%>
<%@page import="com.telenav.datatypes.content.tnpoi.v10.TnPoi"%>
<%@page import="com.telenav.ws.datatypes.address.Address"%>

<%@page import="com.telenav.browser.movie.Util" %>
<%@page import="com.telenav.browser.movie.Constant"%>

<%@page import="com.telenav.datatypes.content.movie.v10.Show"%>
<%@page import="com.telenav.browser.movie.datatypes.RatingImage"%>

<%
	boolean showBuyMovie = !Util.isCanadianCarrier(handlerGloble);
%>

<tml:TML outputMode="TxNode">
	<%
   	String pageURL = host + "/MovieDetails.do";
	    	
	Movie movie = (Movie) request.getAttribute("MovieDetails");
    TnPoi theater = (TnPoi) request.getAttribute("TheaterPOI");
    String smallImage = (String) request.getAttribute("bigImage");
    String date = " (" + request.getAttribute("date") + ")";
    Schedule schedule = (Schedule) request.getAttribute("schedule");;

 	// get attributes for anchor lat, lon, distUnit and scale
    long   anchorLat = ((Long)request.getAttribute("anchorLat")).longValue();
    long   anchorLon = ((Long)request.getAttribute("anchorLon")).longValue();
    long   distUnit  = ((Long)request.getAttribute("distUnit")).longValue();
    String scale     = (String) request.getAttribute("scale");
    
    String theaterName = ""; 
    StringBuffer detail = new StringBuffer();
    String phone = null;
    String phoneDisplay = null;
    
    if (theater != null){
    	theaterName  = theater.getBrandName();
	    detail.append(Util.getDetailAddress(theater));
	    phone = theater.getPhoneNumber();
	    phoneDisplay = Util.formatPhoneNumber(phone);
	    if (phone != null && phone.length() > 0) {
	        detail.append(", ");
	        detail.append(phoneDisplay);
	    }
    }
  
   	Boolean onlineTickets = (Boolean)request.getAttribute("HasTickets");
   	boolean hasTickets = onlineTickets != null?onlineTickets.booleanValue():false;
   	
	Address adr = null;
	long lat = -1;
	long lon = -1;
	
	int driveToMenuIndex = 0;
	if (hasTickets){
		driveToMenuIndex++;
	}
	if (phone != null && phone.length() > 0){
		driveToMenuIndex++;
	}
	
	if (theater != null){
		adr = theater.getAddress();
		lat = (long)(adr.getGeoCode().getLatitude()*Constant.DEGREE_MULTIPLIER);
		lon = (long)(adr.getGeoCode().getLongitude()*Constant.DEGREE_MULTIPLIER);
	%>
	<jsp:include page="/touch/jsp/common/nav/controller/DriveToController.jsp" />
	
	<tml:menuItem name="driveTo" onClick="driveTo" text="<%=msg.get("buy.driveTo")%>" >
	</tml:menuItem>
	
	<%
		}	
	%>
	
	<%--call phone--%>
	<%
	    if (phone != null && phone.length() > 0) {
	    	String callPhone = msg.get("call.me")+ " " + phoneDisplay;
	%>
	<tml:actionItem name="makePhoneCallAction" action="makePhoneCall">
		<tml:input>
			<tml:bean name="phonenumber" valueType="string" value="<%=phone%>" />
		</tml:input>
	</tml:actionItem>
	<tml:menuItem name="callphone" actionRef="makePhoneCallAction"
		text="<%=callPhone%>">
	</tml:menuItem>
	<%
	    }
	%>
	
	<tml:script language="fscript" version="1">
		<![CDATA[
		
				func goBack()
					TempCache.saveString("<%=Constant.StorageKey.MOVIE_FROM_DP%>","Y")
					System.back()
				endfunc
		<%
		    if (theater != null) {
		%>
			func preLoad()
			   if Account.isTnMaps()
			       MenuItem.setItemText("page", <%=driveToMenuIndex%>, "<%=msg.get("poi.get.directions")%>")
			    else
			       MenuItem.setItemText("page", <%=driveToMenuIndex%>, "<%=msg.get("buy.driveTo")%>")
			    endif
			endfunc
			
			func driveTo()
				JSONObject locationJo
				JSONObject.put(locationJo,"label","<%=theater.getBrandName()%>")
				JSONObject.put(locationJo,"lat",<%=lat%>)
				JSONObject.put(locationJo,"lon",<%=lon%>)
				JSONObject.put(locationJo,"firstLine","<%=adr.getStreetName()%>")
				JSONObject.put(locationJo,"city","<%=adr.getCity()%>")
				JSONObject.put(locationJo,"state","<%=adr.getState()%>")
				JSONObject.put(locationJo,"zip","<%=adr.getPostalCode()%>")
				JSONObject.put(locationJo,"country","<%=adr.getCountry()%>")
				TxNode poiNode
				String poiStr = JSONObject.toString(locationJo)
				println(poiStr)
				TxNode.addMsg(poiNode,poiStr)
	      		# 7 - POI 
	      		DriveTo_M_saveStopType(7)
				DriveTo_C_doNav(poiNode,"Search Poi","Movie")
			endfunc
			
		<%
			}	
		%>
			]]>
	</tml:script>
	
	<jsp:include page="MenuItems.jsp" />
	
		<%--buy tickets button--%>
		<%
		    if (theater != null && hasTickets) {
		        String sid = schedule.getId();
		%>
		<tml:menuItem name="buytickets"	pageURL="<%=host + "/BuyTickets.do"%>"	text="<%=msg.get("details.buy")%>" >
			<tml:bean name="movieId" valueType="String" value="<%=movie.getId() + ""%>" />
			<tml:bean name="theaterId" valueType="String" value="<%=theater.getPoiId() + ""%>" />
			<tml:bean name="scheduleId" valueType="String" value="<%=sid%>" />
			<tml:bean name="anchorLat" valueType="int" value="<%= "" + anchorLat %>" />
			<tml:bean name="anchorLon" valueType="int" value="<%= "" + anchorLon %>" />
			<tml:bean name="distUnit" valueType="int" value="<%= "" + distUnit %>" />
		</tml:menuItem>
		<%
		    } else if (theater == null && hasTickets) {
		%>
		<tml:menuItem name="buytickets"	onClick="goBack" text="<%=msg.get("details.buy")%>">
			<tml:bean name="movieId" valueType="String" value="<%=movie.getId() + ""%>" />
		</tml:menuItem>
		<%
		    }
		%>
		
	<tml:page id="movieDetails" url="<%=pageURL%>" type="net" helpMsg="$//$moviesdetails" groupId="<%=MOVIE_GROUP_ID%>">

		<tml:title id="title" fontWeight="bold|system_large" align="center" fontColor="white">
			<![CDATA[<%=msg.get("details.title")%>]]>
		</tml:title>
	<%
	    if (hasTickets)
	    {
   	%>
		<tml:menuRef name="buytickets" />
   	<%
	    }
	    if (phone != null && phone.length() > 0) 
	    {
	%>
		<tml:menuRef name="callphone" />
	<%
	    }
		if (theater != null)
		{
	%>
		<tml:menuRef name="driveTo" />
	<%
	    }
	%>
		<tml:menuRef name="NewSearch" />

	<tml:panel id="mainPanel" layout="vertical">

	<tml:panel id="DetailPanel">
	
		<%--small poster--%>
		<%
			if (smallImage == null) {
		%>
			<tml:image id="smallImage" url="<%=imageUrl + "movie_image_80x120.png"%>" align="center"/>
		<%
		    } else {
		%>
			<tml:image id="smallImage" url="tempimage_smallImage" align="center"/>
			<tml:tempImage url="tempimage_smallImage" imagedata="<%=smallImage%>" />
		<%
            }
		%>
		
		<%--rating--%>
		<%
			RatingImage rImage = (RatingImage) request.getAttribute("rImage");
		%>
			
		<tml:image id="ratingImage1" url="<%=rImage.getStarImage1()%>" />
		<tml:image id="ratingImage2" url="<%=rImage.getStarImage2()%>" />
		<tml:image id="ratingImage3" url="<%=rImage.getStarImage3()%>" />
		<tml:image id="ratingImage4" url="<%=rImage.getStarImage4()%>" />
	
		<%--movie name--%>
		<tml:label id="nameLabel" fontSize="18" fontWeight="bold" align="left"  textWrap="wrap">
			<![CDATA[<%=movie.getName()%>]]>
		</tml:label>

		<%--grade & runTime--%>
		<tml:label id="detailLabel" fontSize="18" align="left" fontColor="#666666">
			<%
			    StringBuffer gradeRuntime = new StringBuffer();
				boolean hasGrade = false;
			    if (movie.getGrade() != null && movie.getGrade().length() > 0) 
			    {
			        gradeRuntime.append(movie.getGrade());
			    	hasGrade = true;
			    }

			    if (movie.getRuntime() != null && movie.getRuntime().length() > 0) 
			    {
			    	if (hasGrade)
			    	{
			    	    gradeRuntime.append(", ");
			    	}

			    	gradeRuntime.append(Util.timeFormat(movie.getRuntime())); 
			    }
			%>
			<![CDATA[ <%=gradeRuntime.toString()%> ]]>
		</tml:label>

		<%--movie genres--%>
		<%
		    String genres = (String) movie.getGenres();
            if (genres == null)
            {
            	genres = "";
            }
            // genres = "<bold>" + msg.get("details.genres") + ":</bold> " + genres;
		%>
		<tml:label textWrap="wrap" id="genres" fontWeight="system_medium" align="left|top">
			<![CDATA[<%=genres%>]]>
		</tml:label>

	</tml:panel> <%-- DetailPanel --%>

		<%--movie cast--%>
		<tml:urlLabel id="dummyLabel1" isFocusable="true"/>
		<tml:nullField id="nullField1"></tml:nullField>
		<%
		    String[] cast = movie.getCast();
		    if (cast == null)
		    {
		         cast = new String[]{};
		    }
		    String castLine = "<bold>" + msg.get("details.cast") + ":</bold> " + Util.makeOneString(cast, ", "); 
		%>
		<tml:multiline id="cast" fontWeight="system_medium" align="left|bottom">
			<![CDATA[<%=castLine%> ]]>
		</tml:multiline>

		<%--movie director--%>
		<%
		    String director = (String) movie.getDirector();
            if (director == null)
            {
                director = "";
            }
            director = "<bold>" + msg.get("details.dir") + ":</bold> "  + director;
		%>

		<tml:urlLabel id="dummyLabel2" isFocusable="true"/>
		<tml:nullField id="nullField2"></tml:nullField>
		<tml:multiline id="director" fontWeight="system_medium" align="left|top">
			<![CDATA[<%=director%>]]>
		</tml:multiline>

		<tml:urlLabel id="dummyLabel3" isFocusable="true"/>
		<tml:nullField id="nullField3"></tml:nullField>
		<%--movie description--%>
		<%
		    String description = (String) movie.getDescription();
            if (description == null) {
                description = "";
            }
            description = "<bold>" + msg.get("details.descr") + ":</bold> "  + description;
		%>
		<tml:multiline id="description" fontWeight="system_medium" align="left | top">
			<![CDATA[<%=description%>]]>
		</tml:multiline>

		<%
		    //If theater is selected , they(badge & button) shows according theaterTicketing (in certain date)
		    //else, according availability of movie ticket at least one theater (in certain date).
		if (hasTickets)
		    {
		%>
		<tml:urlLabel id="dummyLabel4" isFocusable="true"/>
		<tml:nullField id="nullField4"></tml:nullField>
		<tml:image id="fandango"  align="center"/>
		<tml:urlLabel id="dummyLabel5" height="1" isFocusable="true"/>
		<%
			}
		%>

	  </tml:panel> <%-- mainPanel --%>
	  
	  
	  <tml:image id="titleShadow" align="left|top"/>
	  
	  <% if(showBuyMovie) { %>	
	  <tml:image id="bottomBgImg"  align="left|top"/>
	  <tml:image id="bottomShadow" align="left|top"/>
		<% } %>
		
		<%--badge fandango--%>
		<%
		    //If theater is selected , they(badge & button) shows according theaterTicketing (in certain date)
		    //else, according availability of movie ticket at least one theater (in certain date).
		    if (hasTickets)
		    {
		%>

		<%--buy button--%>
		<% if(showBuyMovie) { %>
			<tml:button id="buyButton" text="<%=msg.get("details.buynow")%>" textVisible="true"
				 isFocusable="true" fontWeight="bold|system_median">
				<tml:menuRef name="buytickets" />
			</tml:button>
		<%} %>

		<%
			}
		    else
		    {
		%>
	  
	  		<% if(showBuyMovie) { %>
	  		<tml:label id="noTicketsLabel" textWrap="ellipsis" fontWeight="system_medium" align="center|middle">
				<![CDATA[<%=msg.get("details.notickets")%>]]>
			</tml:label>
			<% } %>

		<%
			}
		%>
		
	</tml:page>
	
	<cserver:outputLayout />
	
</tml:TML>
