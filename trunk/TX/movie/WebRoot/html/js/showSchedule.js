function fetchScheduleData() {
	var movieId = JSON.parse(getCurrentMovie()).id, theaterId = JSON.parse(getCurrentTheater()).id;
	searchCriteria = {
		"movieId" : movieId,
		"theaterId" : theaterId,
		"dateIndex" : getScheduleDateText()
	};
	var jsonStr = JSON.stringify(searchCriteria);
	var ajxUrl = GLOBAL_hostUrl + "lookUpSchedule.do?jsonStr=" + jsonStr + "&" + CommonUtil.getClientInfo();
	var ajaxOptions = {
		data : 0,
		loadingStyle : 1,
		url : ajxUrl,
		onSuccess : ajaxCallBackForSchedule
	};
	CommonUtil.ajax(ajaxOptions);
}

function ajaxCallBackForSchedule(responseText) {
	setCurrentSchedule(responseText);
	if (getDefaultSchedule() == null) // for request from POIDetail.jsp
	{
		setDefaultSchedule(responseText);
	}
	displayScheduleData();
}

function displayMovieInfo() {
	var movieItem = JSON.parse(getCurrentMovie());
	document.getElementById("movieData").innerText = movieItem.name;
}

function displayTheaterInfo() {
	var theaterItem = JSON.parse(getCurrentTheater());
	var pageText = "";
	pageText += theaterItem.name + "<div class='clsScheduleTheaterAddressContent clsScheduleTheaterAddressContentColor fs_middle'>" + theaterItem.addressDisplay + "</div>";
	document.getElementById("theaterData").innerHTML = pageText;
}

function onClickBuy(showTimeText, element) {
	var currentSchedule = JSON.parse(getCurrentSchedule());
	CommonUtil.debug("[Current Schedule]" + getCurrentSchedule());
	if (currentSchedule == null || currentSchedule.ticketURI == null || currentSchedule.ticketURI == "") {
		CommonUtil.showAlert("", I18NHelper["buyticket.noticket.movie"], I18NHelper["button.ok"]);
	} else {
		setMovieTime(showTimeText);
		window.location = GLOBAL_hostUrl + "goToJsp.do?jsp=buyTicket" + "&" + CommonUtil.getClientInfo();
	}
}

function initSchedule() {
	GLOBAL_todayDate = new Date();
	GLOBAL_searchDate = getSearchDate();
	displayMovieInfo();
	displayTheaterInfo();

	if (getCurrentSchedule() == null) {
		fetchScheduleData();
	} else {
		displayScheduleData();
	}

	changeDateDisplay();

}

function datePickerOnDateChange() {
	setScheduleDate(GLOBAL_searchDate);
	setSearchDate(GLOBAL_searchDate);
	fetchScheduleData();
}

function highlightScheduleItem(element) {
	if (element) {
		switchHightlight(element, "clsListBgNormal", "clsListBgHighlight");
		switchHightlight(element, "fc_gray", "fc_white");
		switchHightlight((element.childNodes[1]).childNodes[0], "clsShowScheduleArrow", "clsShowScheduleArrowFocused");
	}
}

function dishighlightScheduleItem(element) {
	if (element) {
		switchHightlight(element, "clsListBgHighlight", "clsListBgNormal");
		switchHightlight(element, "fc_white", "fc_gray");
		switchHightlight((element.childNodes[1]).childNodes[0], "clsShowScheduleArrowFocused", "clsShowScheduleArrow");
	}
}

function formatTimeAsHtmlText(showTimeText) {
	var times = showTimeText.split(":");
	var hour = times[0] % 24;
	var displayTime = "";

	if (hour == 0) {
		displayTime = "12:" + times[1] + "am";
	} else if (hour == 12) {
		displayTime = "12:" + times[1] + "pm";
	} else if (hour < 12) {
		// make the display right indent
		if (hour < 10) {
			hour = "&nbsp;&nbsp;" + hour;
		}
		displayTime = hour + ":" + times[1] + "am";
	} else {
		hour = hour - 12;
		if (hour < 10) {
			hour = "&nbsp;&nbsp;" + hour;
		}
		displayTime = hour + ":" + times[1] + "pm";
	}
	return displayTime;
}

function isPassedTime(showTimeText) {
	var now = new Date();
	var nowTimeText = now.getHours() + ":" + now.getMinutes() + ":" + now.getSeconds();
	var passed = false;
	var dayDiff = getDaysDiff(now, getSearchDate());
	if (dayDiff == 0) {
		passed = compareTime(nowTimeText, showTimeText) > 0 ? true : false;
	} else if (dayDiff > 0) {
		passed = false;
	} else {
		passed = true;
	}
	return passed;
}

function displayScheduleData() {
	// display Schedule Data
	var scheduleItem = JSON.parse(getCurrentSchedule());
	var showTimes = scheduleItem.showTimes;
	var showTimeText = "";
	var pageText = "";

	if (showTimes != null && showTimes != "") {
		var showTimeArray = showTimes.split(";");
		for ( var i = 0; i < showTimeArray.length; i++) {
			// add by ZhangChunfeng
			var style = "";
			if (showTimeArray.length == 1) {
				style = "clsScheduleItemBg";
			} else if (i == 0) {
				style = "clsScheduleItemBgTop";
			} else if (showTimeArray.length == 1 + i) {
				style = "clsScheduleItemBgBottom";
			} else {
				style = "clsScheduleItemBgMiddle";
			}

			showTimeText = showTimeArray[i];
			var displayTime = formatTimeAsHtmlText(showTimeText);
			/* Modified by Huang Kai */
			var passed = isPassedTime(showTimeText);

			if (passed == false) {
				pageText += "<div  class=" + "\"div_table fc_gray clsListBgNormal " + style
						+ "\" ontouchstart=\"highlightScheduleItem(this)\" ontouchend=\"dishighlightScheduleItem(this)\" ontouchmove=\"dishighlightScheduleItem(this)\" onClick=\"onClickBuy('" + showTimeText + "',this)\" >"
						+ "<div class='div_cell ticketinfostyle fs_large' >" + I18NHelper["movie.buyticketsfor"] + " " + displayTime
						+ "</div><div class='div_cell'><div class='clsShowScheduleArrow'></div></div></div>";
			} else {
				pageText += "<div class=" + "\"div_table fc_gray clsListBgNormal " + style + "\">" + "<div class='div_cell ticketinfostyle fs_large'>"
						+ "<span class='clsPassedShow'>" + I18NHelper["movie.buyticketsfor"] + " " + displayTime + "</span>"
						+ "</div><div class='div_cell'><div class='clsShowScheduleArrow'></div></div></div>";
				/* end */
			}
		}
	}

	document.getElementById("scheduleList").innerHTML = pageText;
}
