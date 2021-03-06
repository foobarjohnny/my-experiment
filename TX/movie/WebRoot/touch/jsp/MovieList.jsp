<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp"%>

<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>

<%@page import="com.telenav.browser.movie.Constant" %>

<%
	// shows movie list, comes from "Search Movie" page
    String imageBg = imageUrl + "background_telenav.png";
    String pageURL = getPage + "MovieList"; 

	int itemsOnScreen = 10; // default value for arm9000
	Integer iOnS = (Integer) request.getAttribute("itemsOnScreen");
	if (iOnS != null) itemsOnScreen = iOnS.intValue();

	// get attributes for anchor lat, lon, distUnit and scale
	long   anchorLat = ((Long)request.getAttribute("anchorLat")).longValue();
	long   anchorLon = ((Long)request.getAttribute("anchorLon")).longValue();
	long   distUnit  = ((Long)request.getAttribute("distUnit")).longValue();
	String scale     = (String) request.getAttribute("scale");

%>

<tml:TML outputMode="TxNode">
	<jsp:include page="model/MovieListModel.jsp" />
	<jsp:include page="model/MovieModel.jsp" />
	<jsp:include page="controller/MovieListController.jsp" />
	
	<%@include file="DateUtil.jsp" %>

	<tml:script language="fscript" version="1">
		<![CDATA[
				
				func showMovieList()
					setInformation(1)
				endfunc
				
				func setInformation(int focusFirst)
					int batchNumber = MovieList_M_getBatchNumber()
					int pageNumber = MovieList_M_getCurrentPage()
					int itemsOnScreen = <%=itemsOnScreen%>
					JSONArray batch = MovieList_M_getMovieListByBatch(batchNumber)
					int sizeOfBatch = JSONArray.length(batch)
					if sizeOfBatch == 0
					endif
					#calc starting and boundary index inside batch
					int startIndx = itemsOnScreen*(pageNumber - 1)
					int endIndx = pageNumber*itemsOnScreen
					if sizeOfBatch < endIndx 
						#size of the list is less then number of listItems on screen
						#set next button invisiable
						endIndx = sizeOfBatch

						Page.setComponentAttribute("next","isFocusable","0")
						Page.setComponentAttribute("next","imageUnclick","$nextImageDisable")

					else
						Page.setComponentAttribute("next","isFocusable","1")
						Page.setComponentAttribute("next","imageClick","$nextImageClick")
						Page.setComponentAttribute("next","imageUnclick","$nextImageUnclick")
					endif 
					
					if batchNumber > 1 || pageNumber > 1
						Page.setComponentAttribute("previous","isFocusable","1")
						Page.setComponentAttribute("previous","imageClick","$previousImageClick")
						Page.setComponentAttribute("previous","imageUnclick","$previousImageUnclick")
					else
						Page.setComponentAttribute("previous","isFocusable","0")
						Page.setComponentAttribute("previous","imageUnclick","$previousImageDisable")
					endif
					
					int sortType = Movie_M_getSortType()
					
					#now loop through visual components
					int i=0
					String ratingURL
					String movieStr
					String t_address
					String t_distance
					String itemId
					String movieId
					String movieNameId
					String t_addressId
					String distanceId
					String imageId
					JSONObject movie
					while i < itemsOnScreen
						itemId = "movie" + i
						if startIndx < endIndx
							#set movie
							movie = JSONArray.get(batch, startIndx)
							ratingURL = makeRatingURL(movie)
							movieStr = makeMovieDesc(movie)
							t_distance = makeDistance(movie)
							t_address = makeAddress(movie) 
							# Set items
							imageId = "starImage_" + i
							Page.setComponentAttribute(imageId,"image", ratingURL)
							movieNameId = "movie_name" + i
							Page.setComponentAttribute(movieNameId,"text", movieStr)
							distanceId = "t_distance" + i
							Page.setComponentAttribute(distanceId,"text",t_distance)
							t_addressId = "t_address" + i
							Page.setComponentAttribute(t_addressId,"text",t_address)

							# 1 - sort by name, 0 sort by rating, show oposite
							if sortType == 1
								MenuItem.setItemValid(itemId,1,1)
								MenuItem.setItemValid(itemId,2,0)
							else
								MenuItem.setItemValid(itemId,1,0)
								MenuItem.setItemValid(itemId,2,1)
							endif
							MenuItem.commitSetItemValid(itemId)
							Page.setComponentAttribute(itemId,"visible","1")

							#set movie id
							itemId = "showTime" + i
							movieId = JSONObject.get(movie, "<%=Constant.RRKey.M_ID%>")
							setMovieId(movieId, itemId)
						else
							#set invsible
							Page.setComponentAttribute(itemId,"visible","0")
						endif
						i = i + 1
						startIndx = startIndx + 1
					endwhile
					if focusFirst == 1
						Page.setControlProperty("movie0","focused","true")
					else
						int tmp = itemsOnScreen - 1
						Page.setControlProperty("movie" + tmp, "focused","true")
					endif

					# set sort name					
					String sortName
					if sortType == 1
					    sortName = " <%=msg.get("sort.byName")%>"
					else
						sortName = " <%=msg.get("sort.byRating")%>"
					endif
					Page.setComponentAttribute("sortBy","text","<%=msg.get("sort.sortBy") %>"+": "+sortName)
					
				endfunc
				
				func setMovieId(String movieId, String itemId)
					TxNode mId 
					TxNode.addMsg(mId, movieId)
					MenuItem.setBean(itemId, "movieId", mId)
				endfunc
				
				func makeRatingURL(JSONObject movie)
					String pref = "$star"
					int imgNum = JSONObject.get(movie, "<%=Constant.RRKey.M_RATING%>") 
					pref = pref + imgNum
					return pref
				endfunc
				
				func makeMovieDesc(JSONObject movie)
					String info = JSONObject.get(movie, "<%=Constant.RRKey.M_NAME%>")
					String grade = JSONObject.get(movie, "<%=Constant.RRKey.M_GRADE%>")
					info = "<bold>" + info + "</bold> (" + grade + ")"
					return info
				endfunc
				
				func makeDistance(JSONObject movie)
					String info = JSONObject.get(movie, "<%=Constant.RRKey.M_THEATER_DISTANCE%>")
					info = "(" + info + ")"
					return info
				endfunc
				
				func makeAddress(JSONObject movie)
					String info = JSONObject.get(movie, "<%=Constant.RRKey.M_THEATER_ADDRESS%>")
					if info == "NA"
						info = JSONObject.get(movie, "<%=Constant.RRKey.M_CAST%>")
					endif
					return info
				endfunc
				
				func changeSortType(int index)

					int newSortType     = index
					int currentSortType = Movie_M_getSortType()

					println("currentSortType"+currentSortType)
					println("newSortType"+newSortType)

					if newSortType == 1
						newSortType = 0
					elsif newSortType == 0
						newSortType = 1
					else
						return FAIL
					endif
					
					if currentSortType == newSortType
						return FAIL
					endif

					Movie_M_saveSortType(newSortType)					
					MovieList_C_searchMovieWithAjax(newSortType)					

				endfunc
			
				func showNext()
					int batchNumber = MovieList_M_getBatchNumber()
					int pageNumber = MovieList_M_getCurrentPage()
					int itemsOnScreen = <%=itemsOnScreen%>
					JSONArray batch = MovieList_M_getMovieListByBatch(batchNumber)
					int sizeOfBatch = JSONArray.length(batch)
					int last = pageNumber*itemsOnScreen
					if sizeOfBatch > last
						#we still have items in cache
						MovieList_M_saveCurrentPage(pageNumber + 1)
						setInformation(1)
						return CONTINUE
					endif
					# batch size == number of items then probably we have items on server
					# or in local cache
					if last == sizeOfBatch
						int batchMax = MovieList_M_getNumberOfBatches()
						if batchNumber < batchMax
							#have batch in cache
							MovieList_M_saveBatchNumber(batchNumber+1)
							MovieList_M_saveCurrentPage(1)
							setInformation(1)
							return CONTINUE
						endif
						int sortByName = Movie_M_getSortType()
						MovieList_C_searchMovieNextBatch(batchNumber+1, sortByName)
						return CONTINUE
					endif
					System.showErrorMsg("<%=msg.get("mList.noMovies.msg")%>")
					return FAIL						
				endfunc
				
				func showPrevious()
					int batchNumber = MovieList_M_getBatchNumber()
					int pageNumber = MovieList_M_getCurrentPage()
					int itemsOnScreen = <%=itemsOnScreen%>
					JSONArray batch = MovieList_M_getMovieListByBatch(batchNumber)
					int sizeOfBatch = JSONArray.length(batch)
					#if there is previous page
					if pageNumber-1 > 0
						MovieList_M_saveCurrentPage(pageNumber - 1)
						setInformation(0)
						return CONTINUE
					endif
					#if there is previous batch
					if batchNumber-1 > 0
						MovieList_M_saveBatchNumber(batchNumber-1)
						MovieList_M_saveCurrentPage(<%=Constant.BATCH_MULTIPLIER%>)
						setInformation(0)
						return CONTINUE
					endif 
					System.showErrorMsg("<%=msg.get("mList.beginnig.msg")%>")
					return FAIL						
				endfunc
				
				func showTimes()
				    TxNode id = ParameterSet.getParam("movieId")
					MenuItem.setBean("movieShowTimes", "movieId", id)
					TxNode dateId = MovieList_M_getDateIDNode()
					int idx = 0 
					if dateId != NULL
						idx = String.convertToNumber(TxNode.msgAt(dateId,0))
					endif
					String dateStr = getDateStr(idx)
					TxNode nodeDate
					TxNode.addMsg(nodeDate, dateStr)
					MenuItem.setBean("movieShowTimes", "dateIndex", nodeDate)
					
					JSONObject addr = Movie_M_getAddress()
					String addrStr = JSONObject.toString(addr)
					int latInt = JSONObject.getInt(addr, "lat")
					int lonInt = JSONObject.getInt(addr, "lon")
					TxNode lat
					TxNode.addValue(lat, latInt)
					TxNode lon
					TxNode.addValue(lon, lonInt)
					MenuItem.setBean("movieShowTimes", "anchorLat", lat)
					MenuItem.setBean("movieShowTimes", "anchorLon", lon)
					TxNode distanceUnitNode = Preference.getPreferenceValue(1)
					if NULL != distanceUnitNode
						MenuItem.setBean("movieShowTimes", "distUnit", distanceUnitNode)
					endif
					System.doAction("movieShowTimes")
				endfunc
				
		        func onBack()
					Movie_M_deleteSortType()
				endfunc	        
				
			func sortTypePop()

				int sortType = Movie_M_getSortType()

				JSONArray options
				JSONArray callBackParams

	  			JSONArray.put(options,"<%=msg.get("sort.rating")%>")
				JSONArray.put(callBackParams, <%= 1 %>)
	
				JSONArray.put(options,"<%=msg.get("sort.name")%>")
				JSONArray.put(callBackParams, <%= 0 %>)

				System.showGeneralMsgEx(NULL,"<%=msg.get("sort.sortTitle")%> ", options, callBackParams, -1, "changeSortType")
						
			endfunc

			]]>
	</tml:script>

	<tml:menuItem name="setSortType" onClick="sortTypePop"/>

	<tml:menuItem name="previous" text="<%=msg.get("mList.previous")%>"	onClick="showPrevious">
	</tml:menuItem>
	
	<tml:menuItem name="next" text="<%=msg.get("mList.next")%>" onClick="showNext">
	</tml:menuItem>
	
	<tml:menuItem name="movieShowTimes" pageURL="<%=host + "/MovieInfoShowTimes.do"%>">
			<tml:bean name="movieId" valueType="String" value="" />
			<tml:bean name="dateIndex" valueType="String" value="0" />
			<tml:bean name="anchorLat" valueType="int" value="<%= "" + anchorLat %>" />
			<tml:bean name="anchorLon" valueType="int" value="<%= "" + anchorLon %>" />
			<tml:bean name="distUnit" valueType="int" value="<%= "" + distUnit %>" />
	</tml:menuItem>
	
	<tml:page id="MovieListPage" url="<%=pageURL%>" groupId="<%=MOVIE_GROUP_ID%>"
		background="<%=imageBg%>" type="<%=pageType%>" showLeftArrow="true" 
		showRightArrow="true" helpMsg="$//$moviesresults">

		<tml:title id="title" align="center|middle"
			fontWeight="bold|system_large" fontColor="white">
			<%=msg.get("movies")%> 
		</tml:title>
		
		<tml:listBox id="MovieListBox" isFocusable="true" hotKeyEnable="false">
			
<% 
			for(int i=0; i<itemsOnScreen; i++)
			{
%>
			<tml:menuItem name="<%="showTime" + i%>" onClick="showTimes" trigger="TRACKBALL_CLICK">
				<tml:bean name="movieId" valueType="String" value="" />
			</tml:menuItem>
		
			<tml:compositeListItem id="<%="movie" + i%>" getFocus="false"
				height="45" width="480" visible="true" bgColor="#FFFFFF"
				transparent="false"
				isFocusable="true">
				<tml:image id="<%="starImage_" + i%>" url="$star0"/>
				<tml:label id="<%="movie_name" + i%>" focusFontColor="white"
				fontWeight="system_large|bold" textWrap="ellipsis" align="left">
				</tml:label>
				<tml:label id="<%="t_distance" + i%>" textWrap="ellipsis"
				 fontWeight="system_large" focusFontColor="#FFF000" fontColor="#005AFF"
					align="left">
				</tml:label>
				<tml:label id="<%="t_address" + i%>" textWrap="ellipsis"
					 fontWeight="system_large" focusFontColor="white" align="left">
				</tml:label>
				<tml:menuRef name="<%="showTime" + i%>" />
			</tml:compositeListItem>

<% 
			}
%>

		</tml:listBox>
		
		<tml:image id="titleShadow" align="left|top"/>
		<tml:image id="bottomShadow" align="left|top"/>
		<tml:image id="bottomBgImg"  align="left|top"/>
		
		<tml:button id="sortBy" getFocus="false" visible="true"
			fontWeight="system"
			isFocusable="true" text="<%=msg.get("sort.sortBy") %>">
			<tml:menuRef name="setSortType" />
			<param name="labelImageBound" value="bottom" />
			<param name="addText" value="" />
		</tml:button>
		
		<tml:button id="previous" getFocus="false" visible="true"
			fontWeight="system"
			isFocusable="true" text=" ">
			<tml:menuRef name="previous" />
			<param name="labelImageBound" value="bottom" />
			<param name="addText" value="" />
		</tml:button>
		<tml:button id="next" getFocus="false" visible="true"
			fontWeight="system"
			isFocusable="false" text=" ">
			<tml:menuRef name="next" />
			<param name="labelImageBound" value="bottom" />
			<param name="addText" value="" /> 
		</tml:button>

		<cserver:outputLayout />
	</tml:page>
</tml:TML>
