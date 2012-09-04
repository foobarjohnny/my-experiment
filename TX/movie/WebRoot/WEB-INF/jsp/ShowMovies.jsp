<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp"%>

<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>

<%@page import="com.telenav.datatypes.content.movie.v10.Movie"%>
<%@page import="com.telenav.datatypes.content.movie.v10.Schedule"%>
<%@page import="com.telenav.browser.movie.Util" %>
<%@page import="com.telenav.browser.movie.Constant" %>
<%@page import="java.util.Map"%>
<%@page import="com.telenav.datatypes.content.movie.v10.MovieSortTypeEnum"%>

<%
	
	// shows movies for theater choosen in POI_theaters category
    String imageBg = imageUrl + "background_telenav.png";
    String pageURL = host + "/ShowMovies.jsp";
    String twoLinesImageFocus = imageUrl + "2line_list_highlight.png";
    String twoLinesImageBlur = imageUrl + "2line_list.png";

	String theaterId = (String)request.getAttribute("theaterId");
    String theaterName = (String)request.getAttribute("theaterName");
	String dateDisp = (String)request.getAttribute("dateDisplay");
	String title = "(" + dateDisp + ")";
    
    Movie[] movie = (Movie[])request.getAttribute("Movies");
	int itemsOnScreen = movie.length;
	Map<String, Schedule> schedule = (Map<String, Schedule>) request.getAttribute("Schedules");
	
	MovieSortTypeEnum sortBy = (MovieSortTypeEnum)request.getAttribute("SortType");

%>



<tml:TML outputMode="TxNode">
	
	<jsp:include page="model/MovieModel.jsp" />
	<jsp:include page="controller/SelectDateController.jsp" />
	
	<jsp:include page="MenuItems.jsp" />
	<%@include file="DateUtil.jsp" %>
	
	<tml:script language="fscript" version="1">
		<![CDATA[
			func onLoad()
				#set flag for New Search action to return to original search
				TxNode flag
		    	TxNode.addMsg(flag, "theater")
		        String saveKey="<%=Constant.StorageKey.MOVIE_FROM_SEARCH%>"
		    	Cache.saveToTempCache(saveKey, flag)
			endfunc
			
			func chageSortType(int sortType)
				# 1 - sort by name, 0 - sort by rating
				Movie_M_saveSortType(sortType)
				TxNode dateId = Movie_M_getDateIDNode() 
				int idx = 0 
				if dateId != NULL
					idx = String.convertToNumber(TxNode.msgAt(dateId,0))
				endif
				String dateStr = getDateStr(idx)
				TxNode nodeDate
				TxNode.addMsg(nodeDate, dateStr)
				MenuItem.setBean("SortedList", "dateIndex", nodeDate)
				
				TxNode sortTypeNode
				TxNode.addValue(sortTypeNode, sortType)
				MenuItem.setBean("SortedList", "sortType", sortTypeNode)
				System.doAction("SortedList")
			endfunc
			
			func sortByName()
				chageSortType(1)
			endfunc
			
			func sortByRating()
				chageSortType(0)
			endfunc
				
			func showDetails()
			    TxNode id = ParameterSet.getParam("movieId")
				MenuItem.setBean("showDetails", "movieId", id)
			    id = ParameterSet.getParam("scheduleId")
				MenuItem.setBean("showDetails", "scheduleId", id)
				System.doAction("showDetails")
			endfunc	
			
			func OnDateChange()
				String url = "<%=host + "/ShowMovies.do"%>"
				SelectDate_C_showDateListInterface(url, NULL, <%=theaterId%>, NULL)
			endfunc
			
			func onBack()
				Movie_M_deleteDate()
			endfunc
		]]>
	</tml:script>
	
	<% if (MovieSortTypeEnum.RANK.equals(sortBy)){%>		
		<tml:menuItem name="sortByN" text="<%=msg.get("sort.name")%>"	onClick="sortByName" trigger="KEY_MENU">
		</tml:menuItem>
	<% }else{%>		
		<tml:menuItem name="sortByR" text="<%=msg.get("sort.rating")%>"	onClick="sortByRating" trigger="KEY_MENU">
		</tml:menuItem>
	<% }%>		
	
	<tml:menuItem name="SortedList" pageURL="<%=host + "/ShowMovies.do"%>">
			<tml:bean name="theaterId" valueType="String" value="<%=theaterId%>" />
			<tml:bean name="theaterName" valueType="String" value="<%=theaterName%>" />
			<tml:bean name="dateIndex" valueType="String" value="0" />
			<tml:bean name="sortType" valueType="int" value="1" />
	</tml:menuItem> 
	
	<tml:menuItem name="showDetails" pageURL="<%=host + "/MovieDetails.do"%>" trigger="KEY_MENU">
			<tml:bean name="movieId" valueType="String" value="" />
			<tml:bean name="theaterId" valueType="String" value="<%=theaterId%>" />
			<tml:bean name="scheduleId" valueType="String" value="" />
			<tml:bean name="fromTSearch" valueType="String" value="true" />
	</tml:menuItem>
	
	<tml:page id="MoviesPage" url="<%=pageURL%>" background="<%=imageBg%>" type="net" groupId="<%=MOVIE_GROUP_ID%>">

		<tml:title id="title" align="center|middle"
			fontWeight="bold|system_large" fontColor="white">
			<![CDATA[ <%=msg.get("movies")%> <%=title%>]]>
		</tml:title>
		
		<tml:listBox id="MovieListBox" isFocusable="true" hotKeyEnable="false">
<% 
			for(int i=0; i<itemsOnScreen; i++){
	    		double rating = Double.parseDouble(movie[i].getRating());
				String ratingImageUrl = imageUrl + "small_" + ((int) (rating * 2)) + ".png";
				String movieId = movie[i].getId();
				String grade = movie[i].getGrade();
				if (grade == null) grade = "NR";
				String info = movie[i].getName() + " (" + grade + ")";
				Schedule s = schedule.get(movieId);
				String sid = "-1";
				if (s!=null) sid = s.getId();
				String showTime = Util.getShowTimes(s);
%>
		<tml:menuItem name="<%="buyTickets" + i%>" pageURL="<%=host + "/BuyTickets.do"%>" trigger="TRACKBALL_CLICK">
			<tml:bean name="movieId" valueType="String" value="<%=movieId%>" />
			<tml:bean name="theaterId" valueType="String" value="<%=theaterId%>" />
			<tml:bean name="scheduleId" valueType="String" value="<%=sid%>" />
			<tml:bean name="fromTSearch" valueType="String" value="true" />
		</tml:menuItem>
		
		<tml:menuItem name="<%="showD" + i%>" onClick="showDetails" text="<%=msg.get("details.title")%>" trigger="KEY_MENU">
			<tml:bean name="movieId" valueType="String" value="<%=movieId%>" />
			<tml:bean name="scheduleId" valueType="String" value="<%=sid%>" />
		</tml:menuItem>
		
			<tml:compositeListItem id="<%="movie" + i%>" getFocus="false"
				height="45" width="480" visible="true" bgColor="#FFFFFF"
				transparent="false"
				focusBgImage="<%=twoLinesImageFocus%>"
				blurBgImage="<%=twoLinesImageBlur%>"
				isFocusable="true">
				<tml:image id="<%="starImage_" + i%>" url="<%=ratingImageUrl%>" />
				<tml:label id="<%="movie_name" + i%>" focusFontColor="white"
					fontSize="16" fontWeight="bold" textWrap="ellipsis" align="left">
					<![CDATA[<%=info%>]]>
				</tml:label>
				<tml:label id="<%="showT" + i%>" textWrap="ellipsis"
					fontSize="14" focusFontColor="white" fontColor="red" align="left">
					<![CDATA[<%=showTime%>]]>
				</tml:label>
				<tml:menuRef name="<%="buyTickets" + i%>" />
				<tml:menuRef name="<%="showD" + i%>" />
				<tml:menuRef name="sortByR" />
				<tml:menuRef name="sortByN" />
				<tml:menuRef name="ChangeDate" />
				<tml:menuRef name="NewSearch" />
			</tml:compositeListItem>
<% 
			}
%>
		</tml:listBox>
		<cserver:outputLayout />
	</tml:page>
	
</tml:TML>