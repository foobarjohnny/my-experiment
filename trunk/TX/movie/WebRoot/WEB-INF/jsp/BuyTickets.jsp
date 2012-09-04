<%@ include file="Header.jsp"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>

<%@page import="java.text.SimpleDateFormat"%>

<%@page import="com.telenav.datatypes.content.movie.v10.Movie"%>
<%@page import="com.telenav.datatypes.content.movie.v10.Schedule"%>
<%@page import="com.telenav.datatypes.content.movie.v10.MovieSearchDate"%>
<%@page import="com.telenav.datatypes.content.tnpoi.v10.TnPoi"%>
<%@page import="com.telenav.ws.datatypes.address.Address"%>

<%@page import="com.telenav.browser.movie.Util" %>
<%@page import="com.telenav.browser.movie.Constant"%>

<%@page import="com.telenav.datatypes.content.movie.v10.Show"%>

<tml:TML outputMode="TxNode">
	<%
	String imageBg = imageUrl + "background_telenav.png";
   	String pageURL = host + "/BuyTickets.do";
   	
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
	
    Boolean fromTheaters = (Boolean)request.getAttribute("fromTSearch");
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
			
			func showPopup()
				JSONArray buttonStrings = JSONArray.fromString("[]")
				JSONArray callbackParams = JSONArray.fromString("[0]")
				System.showGeneralMsgEx( NULL, "<%=msg.get("buy.popup")%>", buttonStrings, callbackParams, 3 , "showPopupCallback" )
			endfunc
			
			func showPopupCallback(int index)
			    TxNode theaterNode = bt_convertTNPoiToNode()
			    Favorite.saveAddress(theaterNode)
				TxNode node = ParameterSet.getParam("callMe")
				String actionId = TxNode.msgAt(node, 0)
				System.doAction(actionId)
			endfunc
			
			func bt_convertTNPoiToNode()
	            TxNode node
	            TxNode.addMsg(node,"supportInfo")
	            TxNode.addMsg(node,"<%=theater.getBrandName()%>")
	            String phoneNumber = "<%=phoneDisplay%>"
	            if NULL == phoneNumber
	               phoneNumber = ""
	            endif
	            TxNode.addMsg(node,phoneNumber)
	            #distance
	            TxNode.addMsg(node,"")
	            TxNode.addMsg(node,"movie")
	            TxNode.addMsg(node,"price")
	            TxNode.addMsg(node,"vendorCode")
	
	            TxNode.addValue(node,196)
	            TxNode.addValue(node,0)
	            
	            TxNode stopNode = bt_convertToStop()
	            TxNode.addChild(node,stopNode)
	             
	            TxNode newPlaceNode
	            TxNode.addValue(newPlaceNode,196)
	            TxNode.addValue(newPlaceNode,0)
	            TxNode.addValue(newPlaceNode,1)
	            TxNode.addValue(newPlaceNode,0)
	     
	            int poiId = <%=theater.getPoiId()%>
	            TxNode.addValue(newPlaceNode,poiId)
	            TxNode.addValue(newPlaceNode,0)
	            TxNode.addMsg(newPlaceNode,"movie")
	            TxNode.addChild(newPlaceNode,node)
	            return newPlaceNode
	        endfunc
	        
	        func bt_convertToStop()
	        	TxNode node
	        	TxNode.addValue(node,0)	
	        	TxNode.addValue(node,<%=lat%>)	
	        	TxNode.addValue(node,<%=lon%>)
	        	TxNode.addValue(node,1)
	        	TxNode.addValue(node,0)
	        	TxNode.addValue(node,0)
	        	TxNode.addValue(node,0)
	        	
	        	TxNode.addMsg(node,"<%=theater.getBrandName()%>")
	        	TxNode.addMsg(node,"<%=adr.getStreetName()%>")
	        	TxNode.addMsg(node,"<%=adr.getCity()%>")      	
	        	TxNode.addMsg(node,"<%=adr.getState()%>")
	            TxNode.addMsg(node,"")
	        	TxNode.addMsg(node,"<%=adr.getPostalCode()%>")
	        	TxNode.addMsg(node,"<%=adr.getCountry()%>")
	        	return node
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
			
			]]>
	</tml:script>
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
		<tml:menuItem name="callphone" actionRef="makePhoneCallAction" trigger="KEY_MENU"
			text="<%=callPhone%>">
		</tml:menuItem>
		<%
		    }
		%>
		
	<jsp:include page="MenuItems.jsp" />
	<jsp:include page="controller/ShareMovieController.jsp" />
	<jsp:include page="model/ShareMovieModel.jsp" />
		
	
	<tml:menuItem name="share" onClick="shareMovie" text="<%=msg.get("buy.sendTo")%>"  trigger="KEY_MENU">
		<tml:bean name="movieName" valueType="String" value="<%=Util.revise(movie.getName())%>" />
		<tml:bean name="theaterName" valueType="String" value="<%=Util.revise(theater.getBrandName())%>" />
		<tml:bean name="theaterAddress" valueType="String" value="<%=Util.revise(address)%>" />
	</tml:menuItem>
	
	<tml:menuItem name="driveTo" onClick="driveTo" text="<%=msg.get("buy.driveTo")%>"  trigger="KEY_MENU">
	</tml:menuItem>
		
	<tml:page id="buyTickets" url="<%=pageURL%>" groupId="<%=MOVIE_GROUP_ID%>"
		background="<%=imageBg%>" type="net" helpMsg="$//$moviestickets">
		<%--change date--%>
		<%--title--%>
		<tml:title id="title" fontWeight="bold|system_large" align="center" fontColor="white">
			<%=msg.get("buy.title")%>
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
				<tml:tempImage url="tempimage_smallImage" imagedata="<%=smallImage%>" />
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
		    String theaterInfo = detail.toString();
		%>
		<tml:label id="theaterName" fontWeight="bold|system_small" align="left|bottom" textWrap="ellipsis">
			<![CDATA[<%=theaterName%>]]>
		</tml:label>
		
		<tml:label id="theaterInfo" fontSize="16" align="left|bottom" textWrap="ellipsis">
			<![CDATA[<%=theaterInfo%>]]>
		</tml:label>
		
		<tml:image id="fandango" align="center"	url="<%=imageUrl + "fandango.png"%>"/> 

		<%--buy tickets--%>
<tml:panel id="DetailPanel" layout="vertical">
		
		<%
        	Schedule s = (Schedule) request.getAttribute("schedule");;
		    if(fromTheaters){
		%>
		<tml:menuItem name="MovieDetails" pageURL="<%=host + "/MovieDetails.do"%>" text="Movie Details">
			<tml:bean name="movieId" valueType="String" value="<%=movie.getId()%>" />
			<tml:bean name="theaterId" valueType="String" value="<%=theater.getPoiId() + ""%>" />
			<tml:bean name="scheduleId" valueType="String" value="<%=s.getId() + ""%>" />
			<tml:bean name="hasTickets" valueType="String" value="true" />
		</tml:menuItem>
		<tml:urlImageLabel id="details"	fontWeight="bold|system_large" align="left" 
						multLine="true" focusFontColor="white">
			<![CDATA[ <%=msg.get("see.movie")%> ]]>
			<tml:menuRef name="MovieDetails" />
		</tml:urlImageLabel>
		
		<%	
			}
		    
		            String wapLink;
		            String buyText;
		            String date = (String) request.getAttribute("date");
		            String trueDateString;
		            String numberDateString;
		            Show[] showTimes = s.getShow();
		            String trueShowTime;
		            int marginDay;
	                SimpleDateFormat sdf = (SimpleDateFormat)Constant.DATE_FORMAT.clone();
	                SimpleDateFormat ndf = (SimpleDateFormat)Constant.NUMBER_DATE_FORMAT.clone();
		            for (int i = 0; i < showTimes.length; i++) {
		                String showTime = showTimes[i].getTime().toString();
		                String quals = "";
		                String[] qualifiers = showTimes[i].getQualifier();
		                if (qualifiers != null && qualifiers.length > 0) quals = " (" + Util.makeOneString(qualifiers, " | ") + ")"; 
		                
		                if (s.getTicketURI() == null
		                        || s.getTicketURI().length() < 1) {
		                    continue;
		                }
		                marginDay = Util.getMarginDay(showTime);
		                if (marginDay == 0) {
		                    trueDateString = date;
		                } else {
		                    trueDateString = sdf.format(Util.getTrueDate(s.getDate(), showTime));
		                }
		                trueShowTime = Util.formatShowTime(showTime);
		                numberDateString = ndf.format(Util.getTrueDate(s.getDate(), trueShowTime));
		                wapLink = Util.revise(Util.getWapLink(s.getVendorName(), trueShowTime, 
		                        s.getTicketURI(), numberDateString, s.getVendorTheaterId()));
		                buyText = msg.get("buy.msg") + " - "
		                        + Util.formatShowTimeAll(showTime) + ", "
		                        + trueDateString + quals;
		%>
		<tml:actionItem name="<%="goFandango" + i%>" action="LocalService_invokePhoneBrowser">
			<tml:input>
				<tml:bean name="url" valueType="string" value="<%=wapLink%>" />
			</tml:input>
		</tml:actionItem>
		<tml:menuItem name="<%="movies::buytickets" + i%>" actionRef="<%="goFandango" + i%>" />
		<tml:menuItem name="<%="movies::popup" + i%>" onClick="showPopup">
			<tml:bean name="callMe" valueType="String" value="<%="movies::buytickets" + i%>" />
		</tml:menuItem>
		
		<tml:urlImageLabel id="buyTicket" fontWeight="bold|system_large" align="left" 
					multLine="true" focusFontColor="white">
			<![CDATA[<%=buyText%>]]>
			<tml:menuRef name="<%="movies::popup" + i%>" />
		</tml:urlImageLabel>
				<% }%>
		
	  </tml:panel>
	</tml:page>
	<cserver:outputLayout />
</tml:TML>
