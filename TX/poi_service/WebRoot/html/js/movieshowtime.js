	function fetchMovieListData()
	{
			var poiDetailObj = JSON.parse(CommonUtil.getFromCache(appendPoiKey(PoiCacheKeys.POIDETAIL)));
			var searchCriteria = {
					"theaterId":    poiDetailObj.poi.bizPoi.poiId,
					"dateIndex":	getSearchDateText()
					};
			//call ajax call to get data
			var ajxUrl = getHostUrlOfMovie() + "lookUpMovie.do?jsonStr=" + JSON.stringify(searchCriteria) + "&" + CommonUtil.getClientInfo();
			console.log(ajxUrl);
			var ajaxOptions = {
					data:0,
					url:ajxUrl,
					loadingStyle : 2,
					onSuccess:ajaxCallBackMovieList
			};
			CommonUtil.ajax(ajaxOptions);
	}
	
	function getHostUrlOfMovie()
	{
		var hostUrlOfMovie = getServerURL(GLOBAL_serverUrl)+"/movie/html/";
		return hostUrlOfMovie;
	}
	function ajaxCallBackMovieList(data)
	{
		var movies = JSON.parse(data);
		if(movies.length>0){
		    var theaterId = movies[0].theaterId;
		    
		    var poiDetailObj = JSON.parse(CommonUtil.getFromCache(appendPoiKey(PoiCacheKeys.POIDETAIL))).poi;
			var stop = poiDetailObj.stop;
			var theaterName = poiDetailObj.bizPoi.brand;
			var theaterAddress = stop.firstLine+"<br/>"+stop.city + ", " + stop.province + " " + stop.zip;
		    
		    var theaterItem = new TheaterItem(theaterId,theaterName,theaterAddress);
		    setCurrentTheater(JSON.stringify(theaterItem));
		    displayMovieList(data);
		}
		else
		{
			noRecordFoundForMovie();
		}
	}
	
	function noRecordFoundForMovie()
	{
		var pageText ="<div class='clsMovieItemBg'>"
		+"<table width='100%' height='10%' border='0' cellspacing='0' cellpadding='0' style='font-size:1em;'>"
	    +"<tr height='100%' style='cursor:pointer' align='left'>"
		+"<td class='clsMovieItemStyle fs_middle' >"+I18NHelper["poidetail.dataUnavailable"]+"</td>"
		+"</tr></table>"
//		+"<div class='clsMovieItemStyle fs_middle' style='height:10%;'>"+I18NHelper["poidetail.dataUnavailable"]+"</div>"
		+"</div>";
		document.getElementById("movieList").innerHTML = pageText;	
	}
	
	/**
	 * @return JSON format client info for large device
	 */

	function initMovieShowTimes()
	{
		GLOBAL_searchDate = new Date();
		GLOBAL_todayDate = new Date();
		setSearchDateOfShowTime(GLOBAL_searchDate);
		$("#searchDate").html(I18NHelper["mSearch.today"]);
		changeDatePreviousStyle(false);
		changeDateNextStyle(true);
		
	}
	
	function datePickerOnDateChange()
	{
		setSearchDateOfShowTime(GLOBAL_searchDate);
		loadPopup_poidetail();
		fetchMovieListData();
	}
	
	
	function onClickBuy(movieId,theaterId,showTimes,ticketURI)
	{
		var movieName = $("#movieName-"+movieId).val();
		var movieItem = new MovieItem(movieId,movieName);
		setCurrentMoive(JSON.stringify(movieItem));
//		setScheduleDate(getSearchDate());
//		setSearchDateOfShowTime(getSearchDate());
		setScheduleDate(GLOBAL_searchDate);
		setSearchDateOfShowTime(GLOBAL_searchDate);
		
		var scheduleItem = new ScheduleItem(movieId,theaterId,showTimes,ticketURI);	
		setCurrentSchedule(JSON.stringify(scheduleItem));
		
		forwardToBuyTicket();
	}
	
	function forwardToBuyTicket()
	{
		//parent.location.href
		var BuyTicketUrl = getHostUrlOfMovie() + "goToJsp.do?jsp=showSchedule" + "&" + CommonUtil.getClientInfo()+"&exitWebView=true";
		//parent.location.href = BuyTicketUrl;
		SDKAPI.launchLocalApp(BuyTicketUrl);
	}
	
	function MovieItem(id, name)       
	{   
		this.id  =  id;   
		this.name  =  name;	   
	}

	function TheaterItem(id, name , addressDisplay)       
	{   
		this.id  =  id;   
		this.name  =  name;	  
		this.addressDisplay  =  addressDisplay;
	}
	
	function ScheduleItem(movieId, theaterId , showTimes , ticketURI)       
	{   
		this.movieId  =  movieId;   
		this.theaterId  =  theaterId;	  
		this.showTimes  =  showTimes;
		this.ticketURI = ticketURI;
	}
	
	function setCurrentTheater(theaterItem)
	{
		localStorage.setItem(getCurrentTheaterCacheKey(),theaterItem);
	}
	
	function getCurrentTheater()
	{
		return localStorage.getItem(getCurrentTheaterCacheKey());
	}
	
	function setCurrentMoive(movieItem)
	{
		localStorage.setItem(getCurrentMovieCacheKey(),movieItem);
	}
	
	function setCurrentSchedule(scheduleItem)
	{
		localStorage.setItem(getCurrentScheduleCacheKey(),scheduleItem);
	}
	
	function getCurrentMovie()
	{
		return localStorage.getItem(getCurrentMovieCacheKey());
	}
	
	function getCurrentTheaterCacheKey()
	{
		return "LOCAL_STORAGE_CURRENTTHEATER";
	}
	
	function getCurrentMovieCacheKey()
	{
		return "LOCAL_STORAGE_CURRENTMOVIE";
	}
	
	function getCurrentScheduleCacheKey()
	{
		return "LOCAL_STORAGE_CURRENTSCHEDULE";
	}
	
	function setScheduleDate(scheduleDate)
	{
		var dateText = scheduleDate.getFullYear() + "-" + (scheduleDate.getMonth()+1) + "-" + scheduleDate.getDate();
		localStorage.setItem("LOCAL_STORAGE_SCHEDULE_DATE",dateText);
	}
	
	/*
	 * Return server URL
	 * etc. http://www.tnserver.com/poi_service/html/poidetail.do
	 * 
	 *  return  "http://www.tnserver.com"
	 */
	function getServerURL(requestURL){
		var indexOfSlash = requestURL.indexOf("/",7);
		var temp = requestURL.substring(0,indexOfSlash);
		return temp;
	}
	
	
	function displayMovieList(data){
		var movies = JSON.parse(data);
		var pageText = "";
		var movie;
		for(var i=0;i<movies.length;i++){
			movie = movies[i];
			
			pageText += "<div class='clsMovieItemBg clsListBgNormal'>"
				+"<div class='table clsMovieItemStyle clsFontColor_gray fs_small' style='width:100%;table-layout:fixed;'>"
			    	+"<div class='tr'><div class='td clsEllipsis'>"+movie.name+"</div></div>"
			    +"</div>";
			if(null != movie.scheduleItem){
				pageText +="<div class='table clsShowTime'>";
				pageText +="<div class='tr'><div class='td clsMovieItemStyle clsFontColor_gray fs_smallest' style='width:75%;'>"+getFormatedShowTimes(movie.scheduleItem.showTimes)+"</div>";
				if(null != movie.scheduleItem.ticketURI && CommonUtil.getBoolean(FeatureHelper["MOVIE_BUY"])){
					pageText += "<div class='td clsButtonLocation' align='center'>"
							+ "<input type='button' class='clsBuyButton fs_middle fw_bold clsBuyButtonBk' value='" +I18NHelper["buy.msg"]+ "' onClick=\"onClickBuy('"+movie.id+"','"+movie.theaterId+"','"+movie.scheduleItem.showTimes+"','"+movie.scheduleItem.ticketURI+"')\" />"							
							+ "</div>";
				}
				pageText += "</div></div>";
			}    
			pageText+="</div><input type='hidden' id='movieName-"+movie.id+"' value=\""+movie.name+"\" />";
		}
		
		document.getElementById("movieList").innerHTML = pageText;
	}
	
	function getFormatedShowTimes(showTimesValue)
	{
		//11:30:00;13:55:00;16:35:00;19:15:00;22:25:00
		if("" == showTimesValue)
		{
			return "";
		}
		var currentDate = new Date();
		var currentTime = currentDate.format("HH:MM:ss");
		var text = showTimesValue;
		var textList = text.split(";");
		var timeFlag;
		var nextAvailableFlag = false;
		var finalText = "";
		var length = textList.length;
		var displayFormat;
		
		var dayDiff = getDaysDiff(new Date(), getSearchDate());
		
		for(var i=0;i<length;i++)
		{
			timeFlag = compareTime(textList[i],currentTime);
			displayFormat = convertTimeFormatToDisplay(textList[i]);
			if(timeFlag <=0 && dayDiff == 0)
			{
				finalText += "<SPAN class='lineThroughText'>" + displayFormat + "</SPAN>";
			}
			else
			{
				if(!nextAvailableFlag)
				{
					nextAvailableFlag = true;
					finalText += "<SPAN class='clsFontColor_blue'>" + displayFormat + "</SPAN>";
				}
				else
				{
					finalText += displayFormat;
				}
			}
			if( i!= length-1)
			{
				finalText += " | ";
			}
		}
		
		return finalText;
	}
	
	function convertTimeFormatToDisplay(movieTime)
	{
		var times = movieTime.split(":");
	    var hour = times[0] % 24;
	    var displayTime="";
	    
	    if(hour == 0)
	    {
	    	displayTime = "12:" + times[1] + "am";
	    }
	    else if(hour == 12)
	    {
	    	displayTime = "12:" + times[1] + "pm";
	    }
	    else if (hour < 12){
	        displayTime = hour + ":" + times[1] + "am";
	    }else{
	     	hour = hour-12;
	        displayTime = hour + ":" + times[1] + "pm";
	    }
	    
	    return displayTime;
	}
	
	function compareTime(time1, time2)
	{
		var result = 0;
		
		var milOfTime1 = milOfTime(time1);
		var milOfTime2 = milOfTime(time2);
		if(milOfTime1>milOfTime2)
		{
			result = 1;
		}
		else if(milOfTime2>milOfTime1)
		{
			result = -1;
		}
		
		return result;
	}
	
	function milOfTime(str) {
		  var t = str.split(':');
		  var hh = parseInt(t[0],10);
		  var mm = parseInt(t[1],10);
		  var d = new Date(2007,0,1,hh,mm,00); // just a date not around daylightsaving
		  return d.getTime();
	}
	
	function getDaysDiff(d1,d2)
	{
		var s1 = new Date(d1);
	    var s2 = new Date(d2);
	    s1.setHours(0, 0, 1, 0);
	    s2.setHours(0, 0, 1, 0);

	    var time= s2.getTime() - s1.getTime(); 
	    var days = parseInt(time / (1000 * 60 * 60 * 24));

	    //alert("days diff:" + days);
	    return days;
	}
	
	function getSearchDate()
	{
		var dateText = localStorage.getItem("LOCAL_STORAGE_SEARCH_DATE");
		var dateArray= dateText.split("-");
		var tempDate = new Date();
		
		tempDate.setFullYear(parseInt(dateArray[0]), parseInt(dateArray[1])-1, parseInt(dateArray[2]));
		tempDate.setHours(0, 0, 1, 0);
		
		return tempDate;
	}
	
	function setSearchDateOfShowTime(searchDate)
	{
		var dateText = searchDate.getFullYear() + "-" + (searchDate.getMonth()+1) + "-" + searchDate.getDate();
		localStorage.setItem("LOCAL_STORAGE_SEARCH_DATE",dateText);
	}
	
	function getSearchDateText()
	{
		//"2010-12-15"
		var tempDate = getSearchDate();
		var searchDateText = tempDate.getFullYear() + "-" + (tempDate.getMonth()+1) + "-" + tempDate.getDate();
		
		return searchDateText;
	}
	
	function highLightScheduleItem(element){
		switchHightlight(element,"clsListBgNormal","clsListBgHighlight");
		switchHightlight(element,"clsFontColor_gray","clsFontColor_white");
	}
	
	function dishighLightScheduleItem(element){
		switchHightlight(element,"clsListBgHighlight","clsListBgNormal");
		switchHightlight(element,"clsFontColor_white","clsFontColor_gray");
	}
	
