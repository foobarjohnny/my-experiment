	function MovieSearchCriteria(addressString, dateIndex, distanceUnit, batchNumber,startIndex)       
	{   
		this.addressString  =  addressString;   
		this.dateIndex  =  dateIndex;
		this.distanceUnit = distanceUnit;
		this.batchNumber = batchNumber;
		this.startIndex = startIndex;
	}
	
	function onClickTheaterList() {
		if(isTheaterTabOn()){
			CommonUtil.debug("already on threater tab, return and do nothing");
			return;
		}
		
		setMovieTabStatus("THEATER");
		cleanPaginationInfo();
		cleanList();
		onChangeMovieTab();
		refreshData();
		
	}

	function onClickMovieList(){
		if(!isTheaterTabOn()){
			CommonUtil.debug("already on Movie tab, return and do nothing");
			return;
		}
		
		setMovieTabStatus("MOVIE");
		cleanPaginationInfo();
		cleanList();
		onChangeMovieTab();	
		refreshData();	
	}

	function onChangeMovieTab()
	{
				
		if(isTheaterTabOn())
		{
			document.getElementById("showTheatersButton").style.display = "";
			document.getElementById("showMoviesButton").style.display = "none";
		}
		else
		{
			document.getElementById("showTheatersButton").style.display = "none";
			document.getElementById("showMoviesButton").style.display = "";			
		}
	}

	function datePickerOnDateChange()
	{
		setSearchDate(GLOBAL_searchDate);
		refreshData();
	}
	
	function refreshData()
	{
		CommonUtil.debug("refreshData()");
		PopupUtil.showLoading();
		
		var addressStr = CommonUtil.getValidString(getSearchLocation());
		if("" == addressStr)
		{
			//use current location
			CommonUtil.debug("--before get GPS--");
			SDKAPI.getCurrentLocation(getGPSBackForMovie);
			CommonUtil.debug("--after get GPS--");
		}
		else
		{
			processMovieAction();
		}
	}
	
	function processMovieAction()
	{
		SDKAPI.getPreference(afterGetDistnaceUnit,PreferenceConstants.DISUNIT);
	}
	
	function cleanPaginationInfo(){
		GLOBAL_currentPageNo = 0;
		GLOBAL_isLoadingMore = false;
		GLOBAL_hasMore = true;
		
		CommonUtil.debug("invoke cleanPaginationInfo()");
	}
	
	function cleanList(){
		$("#movieOrTheaterList").html("");
		$("#smallLoading").hide();
	}
	
	function cleanPaginationInfoAndListIfNeeded(){ 
		CommonUtil.debug("cleanPaginationInfoIfNeeded()");
		
		var daysDiffer = getDaysDiff(GLOBAL_todayDate,GLOBAL_searchDate);
		if(daysDiffer < GLOBAL_dateMaxRange && daysDiffer > 0 )
		{
			cleanList();
			cleanPaginationInfo();
		}
	}

	function afterGetDistnaceUnit(distanceUnit)
	{
		CommonUtil.debug("afterGetDistnaceUnit(distanceUnit)" + distanceUnit);
		cleanList();
		cleanPaginationInfo();
		var addressStr = getSearchLocation();
		var startIndex = $(".menuheader").length;
		var searchCriteria = new MovieSearchCriteria(addressStr,getSearchDateText(),distanceUnit,GLOBAL_currentPageNo+1,startIndex);
		var jsonStr = JSON.stringify(searchCriteria);
		var ajxUrl = GLOBAL_hostUrl + getURL() + jsonStr + "&" + CommonUtil.getClientInfo()+ getRandomTime();
		CommonUtil.debug("fetching movie:"+ajxUrl);
		var ajaxOptions = {
				data:0,
				loadingStyle:1,
				url:ajxUrl,
				onSuccess:ajaxCBDistnaceUnit
		};
		
		if (navigator.onLine) {
			CommonUtil.ajax(ajaxOptions);
		}
		else
		{
			CommonUtil.noNetworkError();
		}
	}
	
	function ajaxCBDistnaceUnit(responseText, url)
	{
		if (getCorrectResult(url) == false) {
			CommonUtil.debug("not correct result");
			return;
		}
		var headerLength = $(".menuheader",responseText).length;
		
		if(headerLength>0){
			$("#movieOrTheaterList").html($(responseText).html());
			
			//CommonUtil.debug(responseText.trim());
			GLOBAL_currentPageNo++;
			refreshPlugin();
			if(!isTheaterTabOn()){
				trunkMovieDesc();
			}
		}else{
			//do nothing
		}
		myScroll.refresh();
		if (window.innerHeight > document.getElementById("movieOrTheaterList").offsetHeight
				+ document.getElementById("movieHeader").offsetHeight) 
		{
			appendMoreItems();
		}
	}
	
	String.prototype.trim = function() 
	{ 
	return this.replace(/(^\s*)|(\s*$)/g, ""); 
	}; 
	
	function SDK_API_captureAddressCallBack(address)
	{	
		//save address
		setSearchLocation(JSON.stringify(address));
		processMovieAction();
		// cleanList();
		// cleanPaginationInfo();
	}
	
	function getGPSBackForMovie(position)
	{
		var address = CommonUtil.processGpsLocation(position);
		setSearchLocation(JSON.stringify(address));
		//
		processMovieAction();
	}
	

	function switchLocationIcon(isOn)
	{
		if(isOn)
		{
			$("#changeLocationIcon").attr("class","clsChangeLocationIcon clsChangeLocationButtonFocused");
		}
		else
		{
			$("#changeLocationIcon").attr("class","clsChangeLocationIcon clsChangeLocationButtonUnfocused");
		}
	}
	
	function onDeviceReady() {
		CommonUtil.debug("On Device Ready()");
		document.removeEventListener("deviceready", onDeviceReady, false);

		SDKAPI.setWindowMode();
		initMovieList();
    }
	
	function initData()
	{
		GLOBAL_searchDate = new Date();
		GLOBAL_todayDate = new Date();
		setSearchDate(GLOBAL_searchDate);
		onChangeMovieTab();
	}
	function initMovieList(){
		//clearMovieSearchData();
		refreshData();
	}
	
	function forwardToBuyTicket()
	{	
		storeListAndStatus();
		window.location.href = GLOBAL_hostUrl + "goToJsp.do?jsp=showSchedule" + "&" + CommonUtil.getClientInfo();
	}

	function formatShowTimes(index)
	{
		for(var i=0;i < 20 ; i++){
			var indexLabel = index+"-"+i;
			if( !$('#showTimes'+ indexLabel)||!$('#showTimes'+ indexLabel).val()){
				continue;
			}else{
				//CommonUtil.debug(getFormatedShowTimes( $('#showTimes'+ indexLabel).val()));
				
				$("#showTimesLabel"+indexLabel).html(getFormatedShowTimes( $('#showTimes'+ indexLabel).val()));
				//document.getElementById('showTimesLabel'+indexLabel).innerHTML = getFormatedShowTimes( $('#showTimes'+ indexLabel).val());
			}
			
	    }
	}
	
	function onClickBuyInMovieList(movieIndex,theaterIndex,theaterId,theaterName,theaterAddress,element)
	{
		//
		var theaterItem = new TheaterItem(theaterId,theaterName,theaterAddress);
		//
		var movieId = document.getElementById("movieId" + movieIndex).value;
		var movieName = document.getElementById("movieName" + movieIndex).value;
		var movieItem = new MovieItem(movieId,movieName);
		//
		var showTimesId = "showTimes" + movieIndex + "-" + theaterIndex;
		var showTimes = document.getElementById(showTimesId).value;	
		var ticketURI =  document.getElementById("ticketURI" + movieIndex + "-" + theaterIndex).value;	
		var scheduleItem = new ScheduleItem(movieId,theaterId,showTimes,ticketURI);	
		//
		goToScheduleScreenData(theaterItem,movieItem,scheduleItem);
	}

	function trunkMovieDesc(){
		var size = $(".menuheader").length;
	
		for(var i=0;i<size;i=i+1){
			var desc = $("#hiddenMovieDec"+i).val();
			
			if(!desc||desc == ""){
				
			}else{
				width = $("#movieDec"+i).width();
				var fontSize =  $(".theateraddress").css("font-size").replace("px","").replace("em","").replace("pt","");
				var maxLen = (Math.floor(width/fontSize))*4;
				if(desc.length>maxLen){
					desc = desc.substr(0,maxLen-3)+"...";
				}
								
				$("#movieDec"+i).html(desc);
			}
		}
	}
	
	function goToScheduleScreenData(theaterItem,movieItem,scheduleItem)
	{
		setCurrentTheater(JSON.stringify(theaterItem));
		setCurrentMoive(JSON.stringify(movieItem));
		setScheduleDate(getSearchDate());
		setCurrentSchedule(JSON.stringify(scheduleItem));
		//
		forwardToBuyTicket();
	}
	
	function onClickBuyInTheaterList(theaterIndex,movieIndex,element)
	{
		var index = theaterIndex + "-" + movieIndex;
		//
		var movieId = document.getElementById("movieId" + index).value;	
		var movieName = document.getElementById("movieName" + index).value;	
		var movieItem = new MovieItem(movieId,movieName);
		//
		var theaterId = document.getElementById("theaterId" + theaterIndex).value;
		var theaterName = document.getElementById("theaterName" + theaterIndex).value;
		var theaterAddress = document.getElementById("theaterAddress" + theaterIndex).value;
		var theaterItem = new TheaterItem(theaterId,theaterName,theaterAddress);
		//
		var showTimes = document.getElementById("showTimes" + index).value;	
		var ticketURI = document.getElementById("ticketURI" + index).value;
		var scheduleItem = new ScheduleItem(movieId,theaterId,showTimes,ticketURI);	
		//
		goToScheduleScreenData(theaterItem,movieItem,scheduleItem);
	}
	
	function loadImageOfMovieList(index){
		loadImages(index, true);
	}
	
	function getMovieIdsInMovieList(index)
	{
		var movieId = document.getElementById('movieId'+index).value;
		var movieIds = [movieId];
		return movieIds;
	}
	
	function getMovieIdsInOneTheater(index)
	{
		var movieIdes="";
		var movieIdArray = [];
		for(var i=0; ; i++){
			var movieIdNode = document.getElementById('movieId'+index+"-"+i);
			if( movieIdNode == null ){
				break;
			}
			var movieId = movieIdNode.value;
			movieIdArray[i] = movieId;	
			movieIdes += (movieId+",");
	    }
		return movieIdArray;
	}
	
	function loadImages(index,isInMovieList)
	{
		CommonUtil.debug( "loadImages()    index:[ "+index+" ]  isInMovieList: [  "+isInMovieList+"   ]");
		
		formatShowTimes(index);

		var movieIdes="";
		var movieIdArray = [];
		if(isInMovieList)
		{
			movieIdArray = getMovieIdsInMovieList(index);
		}
		else
		{
			movieIdArray = getMovieIdsInOneTheater(index);
		}
		for(var i=0;i<movieIdArray.length; i++){
			movieIdes += (movieIdArray[i]+",");
	    }
	    
		var json = {"movieIds":movieIdes};
		CommonUtil.debug( "load image: "+movieIdes);
		var ajxUrl = GLOBAL_hostUrl + "LoadImage.do?jsonStr=" + JSON.stringify(json) + "&" + CommonUtil.getClientInfo();
		
		var ajaxOptions = {
				url:ajxUrl
		};
		ajaxOptions.onSuccess = function(images){
			for(var i=0; i<movieIdArray.length; i++){			    		    	
				var image = images[movieIdArray[i]];
				var movieImageId = 'movieImageId'+index+'-'+i;
				CommonUtil.debug("movieImageId   "+movieImageId);
				if( image != 'null' ){
			    	document.getElementById(movieImageId).src = "data:image/gif;base64,"+ image;
				}
			}
		};
		if (navigator.onLine) {
			CommonUtil.ajax(ajaxOptions);
		}
		else
		{
			CommonUtil.noNetworkError();
		}
	}
	
	function loadImageOfTheateList(index){
		loadImages(index, false);
	}
	
	function refreshPlugin(){
		if(isTheaterTabOn()){
			config.contentclass = "showfilmdiv";
			config.onopenclose = function(header, index, state, isuseractivated){
				
				
			};
			
	    	ddaccordion.refreshAndInit(config);
		}else{
			config.contentclass = "placeHolder";
			config.onopenclose = function(header, index, state, isuseractivated){
				var displayid = $(header).attr("movieId");
				if("none"==state) {
					$(".movieinfor"+displayid).hide();
				} else{
					$(".movieinfor"+displayid).show();
				}
				//do nothing
			};
							
			ddaccordion.refreshAndInit(config);
		}
	}
	
	function getURL(){
		if(isTheaterTabOn()){
			return "theaterList.do?pageType=ajaxSimpleList&jsonStr=";
		}else {
			return "movieList.do?pageType=ajaxSimpleList&jsonStr=";
		}
	}
	
	function getCorrectResult(url) {
		if (!!url) {
			if (isTheaterTabOn()) {
				if (url.indexOf("theaterList.do?pageType=ajaxSimpleList&jsonStr=") != -1) {
					return true;
				}
			} else {
				if (url.indexOf("movieList.do?pageType=ajaxSimpleList&jsonStr=") != -1) {
					return true;
				}
			}
		}
		return false;
	}
	
	function loadMoreItems(distanceUnit){

		CommonUtil.debug("loadMoreItems(distanceUnit)"+distanceUnit);
		var addressStr = getSearchLocation();
		var startIndex = $(".menuheader").length;
		var searchCriteria = new MovieSearchCriteria(addressStr,getSearchDateText(),distanceUnit,GLOBAL_currentPageNo+1,startIndex);
	
		var jsonStr = JSON.stringify(searchCriteria);

		var ajxUrl = GLOBAL_hostUrl + getURL() + jsonStr + "&" + CommonUtil.getClientInfo()+getRandomTime();
		CommonUtil.debug("loading....  "+ajxUrl);
		var ajaxOptions = {
				data:0,
				url:ajxUrl,
				onSuccess:ajaxCBMoreItems
		};
		if (navigator.onLine) {
			CommonUtil.ajax(ajaxOptions);
		}
		else
		{
			CommonUtil.noNetworkError();
		}
	}
	
	function ajaxCBMoreItems(responseText, url)
	{
		$("#smallLoading").hide();
		GLOBAL_isLoadingMore = false;
		if (getCorrectResult(url) == false) {
			CommonUtil.debug("not correct result");
			return;
		}
		var headerLength = $(".menuheader",responseText).length;
		
		if(headerLength>0){
			$($(responseText).html()).appendTo($("#movieOrTheaterList"));				
			GLOBAL_currentPageNo++;
			refreshPlugin();
			if(!isTheaterTabOn()){
				trunkMovieDesc();
			}
		}else{
			CommonUtil.debug("Tried to fetch more,But No more data returned");
			GLOBAL_hasMore = false;
		}
		myScroll.refresh();
		
		jQuery(window);
		if (window.innerHeight > document.getElementById("movieOrTheaterList").offsetHeight
				+ document.getElementById("movieHeader").offsetHeight) 
		{
			appendMoreItems();
		}
	}

	function storeListAndStatus(){
		//set the flag to go to schedule page
		setFlagBeforeGoToSchedule();
		setNeedToReloadWhenBack();// there is a bug in webkit when "go back", page messed up.
		//set searchDate
		var movieTheaterPageCache = new MovieTheaterPageCache(getMovieTabStatus(),GLOBAL_searchDate.getTime(),GLOBAL_currentPageNo,GLOBAL_hasMore,ddaccordion.getOpenedContentIndex(),$("#movieOrTheaterList").html());
		setMovieTheaterPageCache(JSON.stringify(movieTheaterPageCache));
	}
	

	
	function MovieTheaterPageCache(tabStatus,searchDate,currentPageNo,hasMoreItem,whichIsOpen,listHtml){
		this.tabStatus = tabStatus;
		this.searchDate = searchDate;
		this.currentPageNo = currentPageNo;
		this.hasMoreItem = hasMoreItem;
		this.whichIsOpen = whichIsOpen;
		this.listHtml = listHtml;
	}
	
	function recoverPage(){
		var movieTheaterPageCache = JSON.parse(getMovieTheaterPageCache());
		//set the date picker
		GLOBAL_searchDate = new Date();
		GLOBAL_searchDate.setTime(movieTheaterPageCache.searchDate);
		GLOBAL_todayDate = new Date();
		setSearchDate(GLOBAL_searchDate);
		changeDateDisplay();
		//set the tab status
		setMovieTabStatus(movieTheaterPageCache.tabStatus);
		onChangeMovieTab();
		//set the list html
		GLOBAL_currentPageNo = movieTheaterPageCache.currentPageNo;
		GLOBAL_hasMore = movieTheaterPageCache.hasMoreItem;
		GLOBAL_isLoadingMore = false;
		cleanList();
		$("#movieOrTheaterList").html(movieTheaterPageCache.listHtml);
		//Do not delete the following comment

		/*
		var index = movieTheaterPageCache.whichIsOpen;
		CommonUtil.debug("Last Opened Header: " + index );
		if( index===undefined||index ===null){
			CommonUtil.debug("no header opened last time: "+ index);
		}else{
			CommonUtil.debug("set default opened header: "+ index);
			config.defaultexpanded = [index];
			ddaccordion.openedContentIndex = index;
		}
		 */
		
		refreshPlugin();
		
		config.defaultexpanded = [];//need to reset
		
		//TODO
		//open header
		//scroll to last position
	}
	
	function cleanCacheAndFlag(){
		cleanViewedSchedulePageFlag();
		cleanMovieTheaterPageCache();
	}
	
	function getRandomTime(){
		var temp = new Date();
		return "&avoidCache="+temp.getTime();
	}
	
	
	function forwardToRating(ratingUrl)
	{
		if(ratingUrl && "" != ratingUrl)
		{
			storeListAndStatus();
			if(CommonUtil.isIphone())
			{
				//CommonUtil.lunchPageContainer(ratingUrl,"",false );
				SDKAPI.launchLocalApp(ratingUrl);
			}
			else
			{
				location.href=ratingUrl;
			}
		}
	}

	function appendMoreItems() 
	{
		if (GLOBAL_isLoadingMore) 
		{
			CommonUtil.debug("is Loading More, please wait.");
			return;
		}
	
		if (!GLOBAL_hasMore) 
		{
			CommonUtil.debug("No more item to load....Do nothing");
			return;
		}
	
		if (GLOBAL_currentPageNo > 0) 
		{
			// to resolve the bug that loading icon will show when click next or
			// previous button
			GLOBAL_isLoadingMore = true;
			$("#smallLoading").show();
			myScroll.refresh();
			CommonUtil.debug("scrollToElement #smallLoading");
			myScroll.scrollToElement("#smallLoading",200);
			CommonUtil.debug("Reach Bottm, try to load more movie or theater");
			setTimeout("SDKAPI.getPreference(loadMoreItems,PreferenceConstants.DISUNIT)",1000);
		}
	}
