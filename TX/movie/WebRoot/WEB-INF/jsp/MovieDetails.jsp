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
<tml:TML outputMode="TxNode">
	<jsp:include page="/WEB-INF/jsp/common/nav/controller/DriveToController.jsp" />
	<%
	String imageBg = imageUrl + "background_telenav.png";
   	String pageURL = host + "/MovieDetails.do";
	    	
	Movie movie = (Movie) request.getAttribute("MovieDetails");
    TnPoi theater = (TnPoi) request.getAttribute("TheaterPOI");
    String bigImage = (String) request.getAttribute("bigImage");
    String date = " (" + request.getAttribute("date") + ")";
    Schedule schedule = (Schedule) request.getAttribute("schedule");;
       
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
	        detail.append("\r\n");
	        detail.append(phoneDisplay);
	    }
    }
    
  
   	Boolean onlineTickets = (Boolean)request.getAttribute("HasTickets");
   	boolean hasTickets = onlineTickets != null?onlineTickets.booleanValue():false;
   	
	Address adr = null;
	long lat = -1;
	long lon = -1;
	
	if (theater != null){
		adr = theater.getAddress();
		lat = (long)(adr.getGeoCode().getLatitude()*Constant.DEGREE_MULTIPLIER);
		lon = (long)(adr.getGeoCode().getLongitude()*Constant.DEGREE_MULTIPLIER);
	%>
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

		<tml:title id="title" fontWeight="bold|system_large" 
				align="center" fontColor="white">
			<![CDATA[<%=movie.getName()%>]]>
		</tml:title>
	<%
	    if (hasTickets)
	    {
   	%>
		<tml:menuRef name="buytickets" />
   	<%
	    }
	    if (phone != null && phone.length() > 0) {
	%>
		<tml:menuRef name="callphone" />
	<%
	    }
		if (theater != null){
	%>
		<tml:menuRef name="driveTo" />
	<%
	    }
	%>
		<tml:menuRef name="NewSearch" />
		
<tml:panel id="DetailPanel" layout="vertical" needShowScrollBar="true">
		<%--big poster--%>
		<%
			if (bigImage == null) {
		%>
			<tml:image id="bigImage" url="<%=imageUrl + "movie_image_100x150.png"%>" align="center"/>
		<%
		    } else {
		%>
			<tml:image id="bigImage" url="tempimage_bigImage" align="center"/>
			<tml:tempImage url="tempimage_bigImage" imagedata="<%=bigImage%>" />
		<%
            }
		%>
		<tml:urlLabel id="dummyLabel1" isFocusable="true">
			 
		</tml:urlLabel>
		
		<%--badge fandango--%>
		<%
		    //If theater is selected , they(badge & button) shows according theaterTicketing (in certain date)
		    //else, according availability of movie ticket at least one theater (in certain date).
		            if (hasTickets)
		            {
		%>
		<tml:urlLabel id="dummyLabel2" isFocusable="true">
			 
		</tml:urlLabel>

		<%--buy button--%>
		<tml:button id="buyButton" text="<%=msg.get("details.buynow")%>" textVisible="true"
			imageClick="<%=imageUrl + "button_large_on.png"%>" isFocusable="true"
			imageUnclick="<%=imageUrl + "button_large_off.png"%>"
			fontWeight="bold|system_median">
			<tml:menuRef name="buytickets" />
		</tml:button>
				<% }%>
		<%-------------------------- 
			Text.
		---------------------------%>
		<tml:urlLabel id="dummyLabel3" isFocusable="true">
			 
		</tml:urlLabel>
		<%--movie name--%>
		<tml:label id="movieName" fontWeight="bold|system_huge" align="left">
			<![CDATA[<%=movie.getName()%>]]>
		</tml:label>
		<%
		    if (theater != null) {
		        String showTimesDisp = "<bold>"+msg.get("movie.showtimes")+" (" + date + ")</bold>";
		        String st = Util.getShowTimes(schedule);
		%>

			<%--theater info--%>
			<tml:label id="theaterName" fontWeight="bold|system_large" align="left|bottom">
				<![CDATA[<%=theater.getBrandName()%>]]>
			</tml:label>

			<%--theater detail address & telephone--%>
			<tml:label id="theaterInfo" fontWeight="system_large" align="left|top">
				<![CDATA[<%=detail.toString()%>]]>
			</tml:label>

			<%--show times--%>
			<%
                String showTime = showTimesDisp + "\r\n" + st;
			%>
			<tml:label id="showTime" fontWeight="system_large" align="left|top">
				<%=showTime%>
			</tml:label>
			<tml:urlLabel id="dummyLabel4" isFocusable="true">
				 
			</tml:urlLabel>
		<%
		    }
		%>
		
		<%--rating&grade&runTime--%>
		<%
	    	double rating = Double.parseDouble(movie.getRating());
			String ratingImageUrl = imageUrl + "marg_line_" + ((int) (rating * 2)) + ".png";

            String s = movie.getGrade();
            if (s == null) {
                s = "";
            }
            if (movie.getRuntime() != null  && movie.getRuntime().length() > 0) {
                if (s.length() > 0) {
                    s += ", ";
                }
                s += Util.timeFormat(movie.getRuntime());
            }
		%>
		<tml:image id="ratingImage" url="<%=ratingImageUrl%>" />
		<tml:label id="showTimes" fontWeight="bold|system_large" align="left|middle">
			<![CDATA[<%=s%>]]>
		</tml:label>
		<tml:urlLabel id="dummyLabel5" isFocusable="true">
			 
		</tml:urlLabel>
		
		<%--movie genres--%>
		<%
		    String genres = (String) movie.getGenres();
            if (genres == null) {
            	genres = "";
            }
            genres = "<bold>" + msg.get("details.genres") + ":</bold> " + genres;
		%>
		<tml:label id="genres" fontWeight="system_large" align="left|top">
			<![CDATA[<%=genres%>]]>
		</tml:label>

		<tml:urlLabel id="dummyLabel6" isFocusable="true">
			 
		</tml:urlLabel>

		<%--movie cast--%>
		<%
		    String[] cast = movie.getCast();
		    if (cast == null) {
		         cast = new String[]{};
		    }
		    String castLine = "<bold>" + msg.get("details.cast") + ":</bold> " + Util.makeOneString(cast, ", "); 
		%>
		<tml:label textWrap="wrap"  id="cast" fontWeight="system_large" align="left|bottom">
			<![CDATA[<%=castLine%> ]]>
		</tml:label>

		<%--movie director--%>
		<%
		    String director = (String) movie.getDirector();
            if (director == null) {
                director = "";
            }
            director = "<bold>" + msg.get("details.dir") + ":</bold> "  + director;
		%>
		<tml:label id="director" fontWeight="system_large" align="left|top">
			<![CDATA[<%=director%>]]>
		</tml:label>

		<tml:urlLabel id="dummyLabel7" isFocusable="true">
			 
		</tml:urlLabel>

		<%--movie description--%>
		<%
		    String description = (String) movie.getDescription();
            if (description == null) {
                description = "";
            }
            description = "<bold>" + msg.get("details.descr") + ":</bold> "  + description;
		%>
		<tml:label textWrap="wrap" id="description" fontWeight="system_large" align="left | top">
			<![CDATA[<%=description%>]]>
		</tml:label>
		
		<%
            //If theater is selected , they(badge & button) shows according theaterTicketing (in certain date)
            //else, according availability of movie ticket at least one theater (in certain date).
            if (hasTickets) {
		%>
		<tml:label id="dummyLabel8">
		</tml:label>
		<%--buy button--%>
		<tml:button id="buyButton2" 
			text="<%=msg.get("details.buynow")%>" textVisible="true"
			imageClick="<%=imageUrl + "button_large_on.png"%>" isFocusable="true"
			imageUnclick="<%=imageUrl + "button_large_off.png"%>"
			fontWeight="bold|system_median">
			<tml:menuRef name="buytickets" />
		</tml:button>
		<tml:image id="fandango" url="<%=imageUrl + "fandango.png"%>" align="center"/>
		<% 
		    }
		%>
		<tml:urlLabel id="dummyLabel9" isFocusable="true">
			 
		</tml:urlLabel>
	  </tml:panel>
	</tml:page>
	
	<cserver:outputLayout />
</tml:TML>
