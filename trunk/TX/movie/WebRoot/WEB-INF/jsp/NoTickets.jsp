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
	        if (theaterName == null) {
	            theaterName = "";
	        }
	        StringBuffer detail = new StringBuffer();
	        String address = Util.getDetailAddress(theater); 
	        detail.append(address);
	        address = Util.getAddressJsonStr(theater);
	        String phone = theater.getPhoneNumber();
	        String phoneDisplay = Util.formatPhoneNumber(phone);
	        if (phone != null && phone.length() > 0) {
                detail.append("\r\n");
	            detail.append(phoneDisplay);
	        }
	        
	    	Address adr = theater.getAddress();
	    	long lat = (long)(adr.getGeoCode().getLatitude()*Constant.DEGREE_MULTIPLIER);
	    	long lon = (long)(adr.getGeoCode().getLongitude()*Constant.DEGREE_MULTIPLIER);
            String date = (String) request.getAttribute("date");
	%>
	<jsp:include page="/WEB-INF/jsp/common/nav/controller/DriveToController.jsp" />
	<tml:script language="fscript" version="1">
		<![CDATA[
			<%@ include file="GetServerDriven.jsp"%>
		    func preLoad()
		        int canShareAddress = ServerDriven_CanShareAddress()
			    MenuItem.setItemValid("page",0,canShareAddress)
		    endfunc
		    
			func shareMovie()
				TxNode mName = ParameterSet.getParam("movieName")
				TxNode tName = ParameterSet.getParam("theaterName")
				TxNode tAddr = ParameterSet.getParam("theaterAddress")
				ShareMovie_C_ShareMovieInterface(mName, tName, tAddr)
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
			
			]]>
	</tml:script>
	
	<tml:menuItem name="driveTo" onClick="driveTo" text="<%=msg.get("buy.driveTo")%>" trigger="KEY_MENU">
	</tml:menuItem>
	
		<%--call phone--%>
		<%
		    if (phone != null && phone.length() > 0) {
		    	String callPhone = msg.get("call.me") + " " + phoneDisplay;
		%>
		<tml:actionItem name="makePhoneCallAction" action="makePhoneCall">
			<tml:input>
				<tml:bean name="phonenumber" valueType="string" value="<%=phone%>" />
			</tml:input>
		</tml:actionItem>
		<tml:menuItem name="callphone" actionRef="makePhoneCallAction"
			text="<%=callPhone%>" trigger="KEY_MENU">
		</tml:menuItem>
		<%
		    }
		%>
		
	<jsp:include page="MenuItems.jsp" />
	<jsp:include page="controller/ShareMovieController.jsp" />
	<jsp:include page="model/ShareMovieModel.jsp" />
		
	
	<tml:menuItem name="share" onClick="shareMovie" text="<%=msg.get("Send To Friend")%>" trigger="KEY_MENU">
		<tml:bean name="movieName" valueType="String" value="<%=Util.revise(movie.getName())%>" />
		<tml:bean name="theaterName" valueType="String" value="<%=Util.revise(theater.getBrandName())%>" />
		<tml:bean name="theaterAddress" valueType="String" value="<%=Util.revise(address)%>" />
	</tml:menuItem>
		
	<tml:page id="noTickets" url="<%=pageURL%>" groupId="<%=MOVIE_GROUP_ID%>"
		background="<%=imageBg%>" type="net">
		<%--change date--%>
		<%--title--%>
		<tml:title id="title"  fontWeight="bold|system_large" align="center" fontColor="white">
			<%=msg.get("noTicket.title")%> (<%=date%>)
		</tml:title>
		<tml:menuRef name="share" />
		<tml:menuRef name="callphone" />
		<tml:menuRef name="driveTo" />
		<tml:menuRef name="NewSearch" />

		<%
	        String smallImage = (String) request.getAttribute("smallImage");
	        if (smallImage != null) {
		%>
		<%--small poster--%>
		<tml:image id="smallImage" url="tempimage_smallImage" />
		<tml:tempImage url="tempimage_smallImage"
			imagedata="<%=smallImage%>" />
		<%
	        }else{
		%>
		<tml:image id="smallImage" url="<%=imageUrl + "movie_image_80x120.png"%>" />
		<%
	        }
		%>
		<%--movie name--%>
		<tml:label id="nameLabel" fontSize="18" fontWeight="bold" align="left">
			<![CDATA[ <%=movie.getName()%> ]]>
		</tml:label>

		<%--rating--%>
		<%
		    double rating = Double.parseDouble(movie.getRating());
			String rImage = imageUrl + "line_"
		                    + ((int) (rating * 2)) + ".png";
		%>
		<tml:image id="ratingImage" url="<%=rImage%>" />
		
		<%--grade & runTime--%>
		<tml:label id="detailLabel" fontSize="16" align="left">
			<%
			    StringBuffer gRInfo = new StringBuffer();
				boolean hasGrade = false;
			    if (movie.getGrade() != null && movie.getGrade().length() > 0) {
			    	gRInfo.append(movie.getGrade());
			    	hasGrade = true;
			    }
			    if (movie.getRuntime() != null && movie.getRuntime().length() > 0) {
			    	if (hasGrade) {
			    		gRInfo.append(", ");
			    	}
			    	gRInfo.append(Util.timeFormat(movie.getRuntime())); 
			    }
			%>
			<![CDATA[ <%=gRInfo.toString()%> ]]>
		</tml:label>
		
		<%--theater name&address&telephone--%>
		<%
		    String theaterInfo = "<bold>" + theaterName
		                    + "</bold>\r\n" + detail.toString();
		%>
		<tml:label id="theaterInfo"  fontSize="16" align="left|bottom">
			<![CDATA[<%=theaterInfo%>]]>
		</tml:label>

		<%
		// TODO add call menu to url image		    
        Schedule s = (Schedule) request.getAttribute("schedule");;
        Show[] showTimes = s.getShow();
        String showTimesDisp = "<bold>"+msg.get("movie.showtimes")+" (" + date + ")</bold>";
        String st = Util.getShowTimes(s);
            
		%>
		<tml:label id="date" fontWeight="system_huge" align="left|bottom">
			<![CDATA[<%=showTimesDisp%>]]>
		</tml:label>
		
		<tml:label id="showTimes" fontSize="18" fontColor="red" align="left|bottom">
			<![CDATA[<%=st%>]]>
		</tml:label>
		
		<tml:menuItem name="MovieDetails" pageURL="<%=host + "/MovieDetails.do"%>" text="Movie Details">
			<tml:bean name="movieId" valueType="String" value="<%=movie.getId()%>" />
			<tml:bean name="theaterId" valueType="String" value="<%=theater.getPoiId() + ""%>" />
			<tml:bean name="scheduleId" valueType="String" value="<%=s.getId() + ""%>" />
			<tml:bean name="hasTickets" valueType="String" value="false" />
		</tml:menuItem>
		
		<tml:urlImageLabel id="MDImageLabel" fontWeight="bold|system_large" align="left" 
						showArrow="true" focusFontColor="white">
			<%=msg.get("see.movie")%>
			<tml:menuRef name="MovieDetails" />
		</tml:urlImageLabel>
		<cserver:outputLayout />
	</tml:page>
</tml:TML>
