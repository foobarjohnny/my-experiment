<%@ include file="Header.jsp"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>

<%@page import="java.text.SimpleDateFormat"%>

<%@page import="com.telenav.datatypes.content.movie.v10.Movie"%>
<%@page import="com.telenav.datatypes.content.movie.v10.Schedule"%>
<%@page import="com.telenav.datatypes.content.tnpoi.v10.TnPoi"%>
<%@page import="com.telenav.ws.datatypes.address.Address"%>

<%@page import="com.telenav.browser.movie.Util" %>
<%@page import="com.telenav.browser.movie.Constant"%>

<%@page import="com.telenav.datatypes.content.movie.v10.Show"%>

<tml:TML outputMode="TxNode">
	<%
			String imageBg = imageUrl + "background_telenav.png";
   			String pageURL = host + "/NoTickets.jsp";
   	
	    	Movie movie = (Movie) request.getAttribute("MovieDetails");
	        TnPoi theater = (TnPoi) request.getAttribute("TheaterPOI");
	        String theaterName = theater.getBrandName();
	        if (theaterName == null) 
	        {
	            theaterName = "";
	        }

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
	        
	        StringBuffer detail = new StringBuffer();
	        String address = Util.getDetailAddress(theater); 
	        detail.append(address);
	        address = Util.getAddressJsonStr(theater);
	        String phone = theater.getPhoneNumber();
	        String phoneDisplay = Util.formatPhoneNumber(phone);
	        
	    	Address adr = theater.getAddress();
	    	long lat = (long)(adr.getGeoCode().getLatitude()*Constant.DEGREE_MULTIPLIER);
	    	long lon = (long)(adr.getGeoCode().getLongitude()*Constant.DEGREE_MULTIPLIER);

			double t_lat = theater.getAddress().getGeoCode().getLatitude();
			double t_lon = theater.getAddress().getGeoCode().getLongitude();

			// get distance in meters between anchor location and address location
			int meters = Util.calDistanceInMeter(t_lat, t_lon, anchorLat/Constant.DEGREE_MULTIPLIER, anchorLon/Constant.DEGREE_MULTIPLIER);
			String distance = null;
			if (useMiles){
				distance = Util.distanceInMiles(meters);
			}else{
				distance = Util.distanceInKilometers(meters);
			}

			distance = "(" + distance + " " + scale + ")";
	
            String date = (String) request.getAttribute("date");

           	String showTimesDisp = "<bold>Showtimes </bold>";
			if (request.getAttribute("LOCALE_KEY").equals("es_MX")){
				showTimesDisp = "<bold>Horarios de funciones </bold>";
			}
			else if (request.getAttribute("LOCALE_KEY").equals("fr_CA")){
				showTimesDisp = "<bold>Horaires des s\u00e9ances</bold>";
			}
            showTimesDisp = showTimesDisp + "<bold> (" + date + ")</bold>";
			
            // get show times details
        	Schedule s = (Schedule) request.getAttribute("schedule");
        	StringBuffer buff = new StringBuffer(); 
        	SimpleDateFormat neededFormat = new SimpleDateFormat("h:mma");
        	if (s == null) buff.append("Show times are unavailable.");
        	else
        	{
        		Show[] showTimes = s.getShow();
            	if (showTimes.length == 0) buff.append("Show times are unavailable.");        			
        		else
        		{
        		    buff.append(neededFormat.format(new java.util.Date(showTimes[0].getTime().getAsCalendar().getTimeInMillis())));
                    for (int i = 1; i < showTimes.length; i++)
                    {
                        buff.append(", " + neededFormat.format(new java.util.Date(showTimes[i].getTime().getAsCalendar().getTimeInMillis())));
                    }
        		}
        	}
             
        	String st =buff.toString();
			
			
			String send_to_friend = msg.get("buy.sendTo");
			if (send_to_friend.equals("buy.sendTo")) {
				send_to_friend = "Send To Friend";
			}
			
     %>
	<jsp:include page="/touch/jsp/common/nav/controller/DriveToController.jsp" />
	
	<tml:script language="fscript" version="1">
		<![CDATA[
		    <%@ include file="GetServerDriven.jsp"%>
		    func preLoad()
		        int canShareAddress = ServerDriven_CanShareAddress()
			    MenuItem.setItemValid("page",0,canShareAddress)
			    
			    if Account.isTnMaps()
			       MenuItem.setItemText("page", 2, "<%=msg.get("poi.get.directions")%>")
			    else
			       MenuItem.setItemText("page", 2, "<%=msg.get("buy.driveTo")%>")
			    endif
		    endfunc 
			
			func shareMovie()
				TxNode mName = ParameterSet.getParam("movieName")
				TxNode tName = ParameterSet.getParam("theaterName")
				TxNode tAddr = ParameterSet.getParam("theaterAddress")
				ShareMovie_C_ShareMovieInterface(mName, tName, tAddr)
				return FAIL
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
				TxNode.addMsg(poiNode,poiStr)
	      		# 7 - POI 
	      		DriveTo_M_saveStopType(7)
				DriveTo_C_doNav(poiNode,"Search Poi","Movie")
			endfunc

			func call()

			    String lphone = "<%= phone %>"
			    TxNode ivrInput
        		    TxNode.addMsg(ivrInput, lphone)
        		    MenuItem.setBean("callphone", "phonenumber", ivrInput)
        		    System.doAction("callphone")

			endfunc
			
			]]>
	</tml:script>
	
	<tml:menuItem name="driveTo" onClick="driveTo" text="<%=msg.get("buy.driveTo")%>" trigger="KEY_MENU">
	</tml:menuItem>
	
		<%--call phone--%>
		<%
		    if (phone != null && phone.length() > 0) 
		    {
		    	String callPhone = msg.get("call.me") + " " + phoneDisplay;
		%>
		<tml:actionItem name="makePhoneCallAction" action="makePhoneCall">
			<tml:input>
				<tml:bean name="phonenumber" valueType="string" value="<%=phone%>" />
			</tml:input>
		</tml:actionItem>
		<tml:menuItem name="callphone" actionRef="makePhoneCallAction"	text="<%=callPhone%>" trigger="KEY_MENU">
		</tml:menuItem>
		<tml:menuItem name="call" onClick="call">
		</tml:menuItem>
		<%
		    }
		%>
		
	<jsp:include page="MenuItems.jsp" />
	<jsp:include page="controller/ShareMovieController.jsp" />
	<jsp:include page="model/ShareMovieModel.jsp" />

	<tml:menuItem name="share" onClick="shareMovie" text="<%=send_to_friend%>" trigger="KEY_MENU">
		<tml:bean name="movieName"      valueType="String" value="<%=Util.revise(movie.getName())%>" />
		<tml:bean name="theaterName"    valueType="String" value="<%=Util.revise(theater.getBrandName())%>" />
		<tml:bean name="theaterAddress" valueType="String" value="<%=Util.revise(address)%>" />
	</tml:menuItem>
		
	<tml:page id="noTickets" url="<%=pageURL%>" groupId="<%=MOVIE_GROUP_ID%>"
		background="<%=imageBg%>" type="net" helpMsg="$//$moviestickets">

		<%--change date--%>
		<%--title--%>
		<tml:title id="title"  fontWeight="bold|system_large" align="center" fontColor="white">
			<%=msg.get("noTicket.title")%>
		</tml:title>
		<tml:menuRef name="share" />
		<tml:menuRef name="callphone" />
		<tml:menuRef name="driveTo" />
		<tml:menuRef name="NewSearch" />

		
		<tml:panel id="DetailPanel" layout="vertical">
			<tml:urlLabel id="dummyLabel1" isFocusable="true" height="1"/>
		<tml:panel id="infoPanel">
		<%--movie name--%>
			<tml:label id="nameLabel" fontSize="18" fontWeight="bold" align="left">
				<![CDATA[ <%=movie.getName()%> ]]>
			</tml:label>
		
			<%--theater name&address&telephone--%>
			<tml:label id="distance" fontSize="16" textWrap="ellipsis" align="left|bottom" fontColor="#005AFF">
				<![CDATA[<%=distance%>]]>
			</tml:label>

	
		
			<tml:label id="theaterName" fontSize="16" align="left|bottom" fontWeight="bold" textWrap="wrap">
				<![CDATA[<%=theaterName%>]]>
			</tml:label>

		
			<tml:label id="theaterInfo" fontSize="16" align="left|bottom" textWrap="wrap">
				<![CDATA[<%=detail.toString()%>]]>
			</tml:label>

			<%
			if (phoneDisplay != null && phoneDisplay.length() > 0) {
			%>
				<tml:urlLabel id="theaterPhoneInfo" fontSize="16" align="left|middle" textWrap="ellipsis">
					<![CDATA[<%=phoneDisplay%>]]>
					<tml:menuRef name="call" />
				</tml:urlLabel>
			<%
			}
			%>

			</tml:panel>
			<tml:multiline id="date" fontSize="14" fontWeight="bold" align="left|bottom">
				<![CDATA[<%=showTimesDisp%>]]>
			</tml:multiline>
			
			<tml:multiline id="showTimes" fontSize="14" align="left">
				<![CDATA[<%=st%>]]>
			</tml:multiline>
			<tml:urlLabel id="dummyLabel2" isFocusable="true" height="1"/>
		
		</tml:panel>
		
		<tml:menuItem name="MovieDetails" pageURL="<%=host + "/MovieDetails.do"%>" text="Movie Details">
			<tml:bean name="movieId" valueType="String" value="<%=movie.getId()%>" />
			<tml:bean name="theaterId" valueType="String" value="<%=theater.getPoiId() + ""%>" />
			<tml:bean name="scheduleId" valueType="String" value="<%=s.getId() + ""%>" />
			<tml:bean name="hasTickets" valueType="String" value="false" />
		</tml:menuItem>

	</tml:page>
	<cserver:outputLayout />
</tml:TML>
