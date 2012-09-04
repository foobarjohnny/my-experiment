function getSearchLocation()
{
	var addressStr = sessionStorage.getItem("SESSION_STORAGE_SEARCH_LOCATION");
	return addressStr;
}

function setSearchLocation(data)
{
	sessionStorage.setItem("SESSION_STORAGE_SEARCH_LOCATION",data);
}

function getDistanceUnitCache(){
	return sessionStorage.getItem("SESSION_STORAGE_DISTANCE_UNIT");
}

function setDistanceUnitCache(unit){
	sessionStorage.setItem("SESSION_STORAGE_DISTANCE_UNIT",unit);
}

function getSearchDateText()
{
	//"2010-12-15"
	var tempDate = getSearchDate();
	var searchDateText = tempDate.getFullYear() + "-" + (tempDate.getMonth()+1) + "-" + tempDate.getDate();
	
	return searchDateText;
}

function getSearchDateTextColon(){
	//"2010:12:15"
	var tempDate = getSearchDate();
	var searchDateText = tempDate.getFullYear() + ":" + (tempDate.getMonth()+1) + ":" + tempDate.getDate();
	
	return searchDateText;
}

function setMovieTime(time)
{
	sessionStorage.setItem("SESSION_STORAGE_MOVIE_TIME",time);
}

function getMovieTime()
{
	 return sessionStorage.getItem("SESSION_STORAGE_MOVIE_TIME");
}


function setSearchDate(searchDate)
{
	var dateText = searchDate.getFullYear() + "-" + (searchDate.getMonth()+1) + "-" + searchDate.getDate();
	localStorage.setItem("LOCAL_STORAGE_SEARCH_DATE",dateText);
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

function getMovieTabStatus()
{
	var status = CommonUtil.getValidString(sessionStorage.getItem(getMovieTabCacheKey()));
	if("" == status)
	{
		status = "THEATER";
	}
	
	return status;
}

function setMovieTabStatus(status)
{
	sessionStorage.setItem(getMovieTabCacheKey(),status);
}

function clearMovieSearchData()
{
	sessionStorage.removeItem(getMovieTabCacheKey());
	sessionStorage.removeItem(getMovieListCacheKey());
	sessionStorage.removeItem(getTheaterListCacheKey());
	localStorage.removeItem(getCurrentMovieCacheKey());
	localStorage.removeItem(getCurrentTheaterCacheKey());
	localStorage.removeItem(getCurrentScheduleCacheKey());
	sessionStorage.removeItem("SESSION_STORAGE_MOVIE_TIME");
	localStorage.removeItem("LOCAL_STORAGE_DEFAULTSCHEDULE");
	localStorage.removeItem("LOCAL_STORAGE_SCHEDULE_DATE");
	//sessionStorage.removeItem("SESSION_STORAGE_SEARCH_LOCATION");
}

function setFlagBeforeGoToSchedule(){
	sessionStorage.setItem(getViewedSchedulePageFlagKey(),true);
}

function cleanViewedSchedulePageFlag(){
	sessionStorage.removeItem(getViewedSchedulePageFlagKey());
}

function getViewedSchedulePageFlagKey(){
	return "SESSION_STORAGE_VIEWED_SCHEDULE_FLAG";
}

function isBackFromSchedulePage(){
	var flag = sessionStorage.getItem(getViewedSchedulePageFlagKey());
	CommonUtil.debug(" flag = "+flag);
	if(!flag || flag == ""){
		CommonUtil.debug(" flag = false");
		return false;
	}else{	
		CommonUtil.debug(" flag = true");
		return true;
	}
}

function getMovieTabCacheKey()
{
	return "SESSION_STORAGE_MOVIETAB";
}


/**
 * check if Theater Tab is on choose
 */
function isTheaterTabOn()
{
	if("THEATER" == getMovieTabStatus())
	{
		return true;
	}
	else
	{
		return false;
	}
}

function getCurrentMovieCacheKey()
{
	return "LOCAL_STORAGE_CURRENTMOVIE";
}

function getCurrentTheaterCacheKey()
{
	return "LOCAL_STORAGE_CURRENTTHEATER";
}

function getCurrentScheduleCacheKey()
{
	return "LOCAL_STORAGE_CURRENTSCHEDULE";
}

function setCurrentMoive(movieItem)
{
	localStorage.setItem(getCurrentMovieCacheKey(),movieItem);
}

function getCurrentMovie()
{
	return localStorage.getItem(getCurrentMovieCacheKey());
}

function setCurrentTheater(theaterItem)
{
	localStorage.setItem(getCurrentTheaterCacheKey(),theaterItem);
}

function getCurrentTheater()
{
	return localStorage.getItem(getCurrentTheaterCacheKey());
}

function setCurrentSchedule(scheduleItem)
{
	localStorage.setItem(getCurrentScheduleCacheKey(),scheduleItem);
}

function getCurrentSchedule()
{
	return localStorage.getItem(getCurrentScheduleCacheKey());
}

function setDefaultSchedule(scheduleItem)
{
	localStorage.setItem("LOCAL_STORAGE_DEFAULTSCHEDULE",scheduleItem);
	//setCurrentSchedule(scheduleItem);
}

function getDefaultSchedule()
{
	return localStorage.getItem("LOCAL_STORAGE_DEFAULTSCHEDULE");
}

function setScheduleDate(scheduleDate)
{
	var dateText = scheduleDate.getFullYear() + "-" + (scheduleDate.getMonth()+1) + "-" + scheduleDate.getDate();
	localStorage.setItem("LOCAL_STORAGE_SCHEDULE_DATE",dateText);
}

function getScheduleDate()
{
	var dateText = localStorage.getItem("LOCAL_STORAGE_SCHEDULE_DATE");
	var dateArray= dateText.split("-");
	var tempDate = new Date();
	
	tempDate.setFullYear(parseInt(dateArray[0]), parseInt(dateArray[1])-1, parseInt(dateArray[2]));
	tempDate.setHours(0, 0, 1, 0);
	
	return tempDate;
}

function getScheduleDateText()
{
	var scheduleDate = getScheduleDate();
	var dateText = scheduleDate.getFullYear() + "-" + (scheduleDate.getMonth()+1) + "-" + scheduleDate.getDate();
	
	return dateText;
}

function getTheaterAddressText(theaterItem)
{
	var address = theaterItem.address;
	var addressText = address.firstLine + " " + address.city + " ," + address.state + " "  + address.zip;
	return addressText;
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
			finalText += '<SPAN class="clsPassedShowTime">' + displayFormat + "</SPAN>";
		}
		else
		{
			if(!nextAvailableFlag)
			{
				nextAvailableFlag = true;
				finalText += '<SPAN class="clsFontColor_nextAvailableShowTime">' + displayFormat + "</SPAN>";
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

function setConfirmEmailCache(confirmEmail){
	localStorage.setItem(getConfirmEmailCacheKey(),confirmEmail);
}

function getConfirmEmailCacheKey(){
	return "LOCAL_STORAGE_COMFIRM_EMAIL";
}

function getConfirmEmailCache(){
	return localStorage.getItem(getConfirmEmailCacheKey());
}

function setMovieTheaterPageCache(data){
	sessionStorage.setItem(getMovieTheaterPageCacheKey(),data);
}

function getMovieTheaterPageCache(){
	return sessionStorage.getItem(getMovieTheaterPageCacheKey());
}

function cleanMovieTheaterPageCache(){
	sessionStorage.removeItem(getMovieTheaterPageCacheKey());
}

function getMovieTheaterPageCacheKey(){
	return "SS_MOVIE_THEATER_PAGE_CACHE";
}

function setNeedToReloadWhenBack(){
	sessionStorage.setItem("SS_MOVIE_THEATER_PAGE_NEED_TO_RELOAD",true);
}

function removeNeedToReloadFlag(){
	sessionStorage.removeItem("SS_MOVIE_THEATER_PAGE_NEED_TO_RELOAD");
}

function getNeedToReloadFlagKey(){
	return "SS_MOVIE_THEATER_PAGE_NEED_TO_RELOAD";
}

function needToReloadMovieListPage(){
	var flag = sessionStorage.getItem(getNeedToReloadFlagKey());
	CommonUtil.debug("need to reload flag = "+flag);
	if(!flag || flag == ""){
		CommonUtil.debug(" flag = false");
		return false;
	}else{	
		CommonUtil.debug(" flag = true");
		return true;
	}
}




