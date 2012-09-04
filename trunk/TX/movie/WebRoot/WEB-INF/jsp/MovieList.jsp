<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp"%>

<%@ taglib uri="http://www.telenav.com/tnbrowser/taglib.tld" prefix="tml"%>
<%@ taglib uri="/WEB-INF/tld/cserver-taglib.tld" prefix="cserver"%>

<%@page import="com.telenav.browser.movie.Constant" %>

<%
	// shows movie list, comes from "Search Movie" page
    String imageBg = imageUrl + "background_telenav.png";
    String pageURL = getPage + "MovieList"; 
    String twoLinesImageFocus = imageUrl + "2line_list_highlight.png";
    String twoLinesImageBlur = imageUrl + "2line_list.png";

	int itemsOnScreen = 10; // default value for arm9000
	Integer iOnS = (Integer) request.getAttribute("itemsOnScreen");
	if (iOnS != null) itemsOnScreen = iOnS.intValue();

%>

<tml:TML outputMode="TxNode">
	<jsp:include page="model/MovieListModel.jsp" />
	<jsp:include page="model/MovieModel.jsp" />
	<jsp:include page="controller/MovieListController.jsp" />
	
	<%@include file="DateUtil.jsp" %>

	<tml:script language="fscript" version="1">
		<![CDATA[
				
				func onResume()
					String saveKey="<%=Constant.StorageKeyForJSON.MOVIE_LIST_ARRAY_JSON%>"
					JSONArray holder = Cache.getJSONArrayFromTempCache(saveKey)
					int arrLength = JSONArray.length(holder)
					int batchNumber = MovieList_M_getBatchNumber()
					if batchNumber > arrLength && batchNumber >0
						MovieList_M_saveBatchNumber(arrLength)
					endif
				endfunc	
				
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
						Page.setComponentAttribute("nextLabel","visible","0")
					else
						Page.setComponentAttribute("nextLabel","visible","1")
					endif 
					
					if batchNumber > 1 || pageNumber > 1
						Page.setComponentAttribute("prevLabel","visible","1")
					else
						Page.setComponentAttribute("prevLabel","visible","0")
					endif
					
					int sortType = Movie_M_getSortType()
					String newSortBy = MovieList_M_getNewSortBy()
					
					#now loop through visual components
					int i=0
				
					String star1URL
					String star2URL
					String star3URL
					String star4URL
					
					String movieStr
					String movieRuntime
					String itemId
					String movieId
					String movieNameId
					String movieRuntimeId
					String imageId
					
					String star1Id
					String star2Id
					String star3Id
					String star4Id
					
					JSONObject movie
					while i < itemsOnScreen
						itemId = "movie" + i
						if startIndx < endIndx
							#set movie
							movie = JSONArray.get(batch, startIndx)
							star1URL = makeRatingStarURL(movie,1)
							star2URL = makeRatingStarURL(movie,2)
							star3URL = makeRatingStarURL(movie,3)
							star4URL = makeRatingStarURL(movie,4)
							movieStr = makeMovieDesc(movie)
							movieRuntime = makeRuntime(movie)
							# Set items
							star1Id = "star1Image_" + i
							star2Id = "star2Image_" + i
							star3Id = "star3Image_" + i
							star4Id = "star4Image_" + i
							
							Page.setComponentAttribute(star1Id,"image", star1URL)
							Page.setComponentAttribute(star2Id,"image", star2URL)
							Page.setComponentAttribute(star3Id,"image", star3URL)
							Page.setComponentAttribute(star4Id,"image", star4URL)
							
							movieNameId = "movie_name" + i
							Page.setComponentAttribute(movieNameId,"text", movieStr)
							movieRuntimeId = "movieRuntime" + i
							Page.setComponentAttribute(movieRuntimeId,"text",movieRuntime)

							# 1 - sort by name, 0 sort by rating, show oposite
							if sortType == 1
								MenuItem.setItemValid(itemId,1,0)
								MenuItem.setItemValid(itemId,2,1)
								MenuItem.setItemValid(itemId,3,1)
								MenuItem.setItemValid(itemId,4,1)
							else
								MenuItem.setItemValid(itemId,1,1)
								MenuItem.setItemValid(itemId,2,0)
								MenuItem.setItemValid(itemId,3,1)
								MenuItem.setItemValid(itemId,4,1)
							endif
							
							if newSortBy != NULL && newSortBy != ""
								if newSortBy == "<%= Constant.SORT_BY_NAME %>"
									MenuItem.setItemValid(itemId,1,0)
									MenuItem.setItemValid(itemId,2,1)
									MenuItem.setItemValid(itemId,3,1)
									MenuItem.setItemValid(itemId,4,1)																
								elseif newSortBy == "<%= Constant.SORT_BY_RANK %>"
									MenuItem.setItemValid(itemId,1,1)
									MenuItem.setItemValid(itemId,2,0)
									MenuItem.setItemValid(itemId,3,1)
									MenuItem.setItemValid(itemId,4,1)								
								elseif newSortBy == "<%= Constant.SORT_BY_RELEASE_DATE %>"
									MenuItem.setItemValid(itemId,1,1)
									MenuItem.setItemValid(itemId,2,1)
									MenuItem.setItemValid(itemId,3,0)
									MenuItem.setItemValid(itemId,4,1)																
								elseif newSortBy == "<%= Constant.SORT_BY_WEEKEND_GROSS %>"
									MenuItem.setItemValid(itemId,1,1)
									MenuItem.setItemValid(itemId,2,1)
									MenuItem.setItemValid(itemId,3,1)
									MenuItem.setItemValid(itemId,4,0)																
								else
									MenuItem.setItemValid(itemId,1,1)
									MenuItem.setItemValid(itemId,2,1)
									MenuItem.setItemValid(itemId,3,1)
									MenuItem.setItemValid(itemId,4,1)								
								endif
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
				endfunc
				
				func setMovieId(String movieId, String itemId)
					TxNode mId 
					TxNode.addMsg(mId, movieId)
					MenuItem.setBean(itemId, "movieId", mId)
				endfunc
				
				func makeRatingStarURL(JSONObject movie,int i)
					String pref = "<%=imageUrl%>" + "star_"
					int rating = JSONObject.get(movie, "<%=Constant.RRKey.M_RATING%>") 
					int star=rating-((i-1)*2)
					if star == 1
					     pref=pref+"half"
					elseif star >= 2
					     pref=pref+"on"
					else
					     pref=pref+"off"
					endif
					
					pref = pref+".png"
					println(pref)
					return pref
				endfunc
				
				func makeMovieDesc(JSONObject movie)
					String info = JSONObject.get(movie, "<%=Constant.RRKey.M_NAME%>")
					info = "<bold>" + info + "</bold>"
					return info
				endfunc
				
				func makeRuntime(JSONObject movie)
					String grade = JSONObject.get(movie, "<%=Constant.RRKey.M_GRADE%>")
					String runTime = JSONObject.get(movie, "<%=Constant.RRKey.M_RUNTIME%>")
					if runTime!=""
					    if grade!=""
							grade=grade+", "
						endif
						grade=grade+runTime
					endif
					
					return grade
				endfunc
				

				func chageSortTypeByN()
					MovieList_M_saveNewSortBy("<%= Constant.SORT_BY_NAME %>")
					chageSortType()
				endfunc

				func chageSortTypeByR()
					MovieList_M_saveNewSortBy("<%= Constant.SORT_BY_RANK %>")
					chageSortType()
				endfunc

				func chageSortTypeByReleaseDate()
					MovieList_M_saveNewSortBy("<%= Constant.SORT_BY_RELEASE_DATE %>")
					chageSortType()
				endfunc

				func chageSortTypeByWeekendGross()
					MovieList_M_saveNewSortBy("<%= Constant.SORT_BY_WEEKEND_GROSS %>")
					chageSortType()
				endfunc
				
				func chageSortType()
					int sortType = Movie_M_getSortType()
					if sortType == 1
						sortType = 0
					else
						sortType = 1
					endif
					Movie_M_saveSortType(sortType)
					
					String newSortBy = MovieList_M_getNewSortBy()
					MovieList_C_searchMovieWithAjax(sortType, newSortBy)					
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
						String newSortBy = MovieList_M_getNewSortBy()
						MovieList_C_searchMovieNextBatch(batchNumber+1, sortByName, newSortBy)
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
					MenuItem.setBean("movieShowTimes", "lat", lat)
					MenuItem.setBean("movieShowTimes", "lon", lon)
					TxNode distanceUnitNode = Preference.getPreferenceValue(1)
					if NULL != distanceUnitNode
						MenuItem.setBean("movieShowTimes", "distUnit", distanceUnitNode)
					endif
					System.doAction("movieShowTimes")
				endfunc
				
		        func onBack()
					Movie_M_deleteSortType()
					MovieList_M_deleteNewSortBy()
				endfunc	        
				
			]]>
	</tml:script>

	
	<tml:menuItem name="sortByR" text="<%=msg.get("sort.rating")%>"	onClick="chageSortTypeByR"  trigger="KEY_MENU">
	</tml:menuItem>

	<tml:menuItem name="sortByN" text="<%=msg.get("sort.name")%>"	onClick="chageSortTypeByN"  trigger="KEY_MENU">
	</tml:menuItem>
	
	<tml:menuItem name="sortByReleaseDate" text="<%=msg.get("Sort By Release Date")%>"	onClick="chageSortTypeByReleaseDate"  trigger="KEY_MENU">
	</tml:menuItem>

	<tml:menuItem name="sortByWeekendGross" text="<%=msg.get("Sory By Box Office")%>"	onClick="chageSortTypeByWeekendGross"  trigger="KEY_MENU">
	</tml:menuItem>

	<tml:menuItem name="previous" text="<%=msg.get("mList.previous")%>"	onClick="showPrevious">
	</tml:menuItem>
	
	<tml:menuItem name="next" text="<%=msg.get("mList.next")%>" onClick="showNext">
	</tml:menuItem>
	
	<tml:menuItem name="movieShowTimes" pageURL="<%=host + "/MovieInfoShowTimes.do"%>">
			<tml:bean name="movieId" valueType="String" value="" />
			<tml:bean name="dateIndex" valueType="String" value="0" />
			<tml:bean name="lat" valueType="int" value="-1" />
			<tml:bean name="lon" valueType="int" value="-1" />
			<tml:bean name="distUnit" valueType="int" value="1" />
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
			String labelPrev = msg.get("mList.show.prev.1") + " " + itemsOnScreen + " " + msg.get("mList.show.prev.2");
%>
			<tml:listItem id="prevLabel" fontWeight="bold|system_large" 
					align="center|middle" showArrow="false">
				<%=labelPrev%>
				<tml:menuRef name="previous" />
			</tml:listItem>
<% 
			for(int i=0; i<itemsOnScreen; i++){
%>
			<tml:menuItem name="<%="showTime" + i%>" onClick="showTimes" trigger="TRACKBALL_CLICK">
				<tml:bean name="movieId" valueType="String" value="" />
			</tml:menuItem>
		
			<tml:compositeListItem id="<%="movie" + i%>" getFocus="false"
				height="45" width="480" visible="true" bgColor="#FFFFFF"
				transparent="false"
				focusBgImage="<%=twoLinesImageFocus%>"
				blurBgImage="<%=twoLinesImageBlur%>"
				isFocusable="true">
				<tml:image id="<%="star1Image_" + i%>" />
				<tml:image id="<%="star2Image_" + i%>"/>
				<tml:image id="<%="star3Image_" + i%>" />
				<tml:image id="<%="star4Image_" + i%>" />
				<tml:label id="<%="movie_name" + i%>" focusFontColor="white"
				fontWeight="system_large|bold" textWrap="ellipsis" align="left">
				</tml:label>
				<tml:label id="<%="movieRuntime" + i%>" textWrap="ellipsis"
				focusFontColor="white" fontWeight="system" align="left">
				</tml:label>
				<tml:menuRef name="<%="showTime" + i%>" />
				<tml:menuRef name="sortByN" />
				<tml:menuRef name="sortByR" />
				<tml:menuRef name="sortByReleaseDate" />
				<tml:menuRef name="sortByWeekendGross" />
			</tml:compositeListItem>
<% 
			}
			String labelNext = msg.get("mList.show.next.1") + " " + itemsOnScreen + " " + msg.get("mList.show.next.2");
%>
			<tml:listItem id="nextLabel" fontWeight="bold|system_large" 
					align="center|middle" showArrow="false">
				<%=labelNext%>
				<tml:menuRef name="next" />
			</tml:listItem>
		</tml:listBox>
		
		<cserver:outputLayout />
	</tml:page>
</tml:TML>