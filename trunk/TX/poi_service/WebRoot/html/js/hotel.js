/**
 * hard code on this page:
 * 1. generateImgSrc(): '"width":	"90",'+ '"height":"90"'
 * 2. fetchAndDispalyHotelDetail(id): id = 2043139220;
 */
var Global_roomQuantity = 1;
var Global_guestQuantity = 1;
var Global_nightQuantity = 0;
function addQuantity(n) {
	switch (n) {
	case 1:// add nights

		CommonUtil.debug("add night");
		if (Global_nightQuantity <= 0) {
			$("#nightSubImg").attr("src", GLOBAL_sharedImageUrl + "subtract_icon_unfocused.png");
		}
		Global_nightQuantity++;
		$("#nightQuantity").html(Global_nightQuantity);
		syncCalendar();
		break;
	case 2:// add rooms
		if (Global_roomQuantity <= 0) {
			Global_roomQuantity = 0;
			$("#roomSubImg").attr("src", GLOBAL_sharedImageUrl + "subtract_icon_unfocused.png");
		}

		Global_roomQuantity++;
		$("#roomQuantity").html(Global_roomQuantity);
		break;
	case 3:// add guest
		if (Global_guestQuantity <= 0) {
			Global_guestQuantity = 0;
			$("#guestSubImg").attr("src", GLOBAL_sharedImageUrl + "subtract_icon_unfocused.png");
		}

		Global_guestQuantity++;
		$("guestQuantity").html(Global_guestQuantity);
		break;
	default:

	}
}

function subQuantity(n) {
	switch (n) {
	case 1:// add nights
		CommonUtil.debug("sub night");
		if (Global_nightQuantity <= 0) {
			break;
		}
		Global_nightQuantity--;
		$("#nightQuantity").html(Global_nightQuantity);
		if (Global_nightQuantity <= 0) {
			Global_nightQuantity = 0;
			$("#nightSubImg").attr("src", GLOBAL_sharedImageUrl + "subtract_icon_disabled.png");
		}

		syncCalendar();
		break;
	case 2:// add rooms
		if (Global_roomQuantity <= 0) {
			break;
		}
		Global_roomQuantity--;
		$("#roomQuantity").html(Global_roomQuantity);
		if (Global_roomQuantity <= 0) {
			Global_roomQuantity = 0;
			$("#roomSubImg").attr("src", GLOBAL_sharedImageUrl + "subtract_icon_disabled.png");
		}
		break;
	case 3:// add guest
		if (Global_guestQuantity <= 0) {
			break;
		}
		Global_guestQuantity--;
		$("#guestQuantity").html(Global_guestQuantity);
		if (Global_guestQuantity <= 0) {
			Global_guestQuantity = 0;
			$("#guestSubImg").attr("src", GLOBAL_sharedImageUrl + "subtract_icon_disabled.png");
		}
		break;
	default:

	}
}

function syncCalendar() {
	CommonUtil.debug("sync calendar");
	if (!Global_calendar.getCheckInDate) {
		CommonUtil.showAlert("",I18NHelper["hotel.chooseCheckIn"],I18NHelper["common.button.OK"]);
	} else {
		if (Global_nightQuantity <= 0) {
			Global_calendar.setCheckOutDate(null);
		} else {
			Global_calendar.setCheckOutDate(Global_calendar.jumpToDate(Global_calendar.getCheckInDate(), Global_nightQuantity));

			// Global_calendar.setCheckOutDate(new Date());

		}

		Global_calendar.flushCal();
	}

}
function fetchAndDispalyHotelDetail(id) {
	if(GLOBAL_isHotelDetailLoaded){
		return;
	}
	loadPopup_poidetail();
	id = 2043139220;
	var searchCriteria = {
		poiId : id,
		isDummy : "false"
	};
	var ajxUrl = GLOBAL_hostUrl + "getHotelDetailData.do?jsonStr=" + JSON.stringify(searchCriteria) + "&"
			+ CommonUtil.getClientInfo();
	var ajaxOptions = {
			data:0,
			url:ajxUrl,
			onSuccess:ajaxCallBackHotelDetail
	};
	CommonUtil.ajax(ajaxOptions);
}

function ajaxCallBackHotelDetail(responseText) {
	var result = CommonUtil.getValidString(responseText);
	if ("" != result) {
		var resultObj = JSON.parse(result);
		if (resultObj) {
			setHotelDetailCache(result, getPoiId());
			displayHotelDetail(result);
		}
	}
}
var GLOBAL_HOTELIMAGESRC;
function displayHotelDetail(cache) {
	PopupUtil.hide();
	var hotel = JSON.parse(cache);

	$("#hotelDesc").html(hotel.desc);
	var amenitiesArr = hotel.amenities;
	$("#amenityList").html(generateAmenitiesImage(amenitiesArr));
	$("#locationDesc").html(hotel.locationDesc);
	$("#hotel_totalRooms").html(hotel.totalRooms);
	$("#hotel_totalFloors").html(hotel.totalFloors);
	$("#hotel_yearBuilt").html(hotel.yearBuilt);
	$("#hotel_lastRenovation").html(hotel.lastRenovation);
	
	var photoArr = hotel.photos;
	if(photoArr){
		generateImg(photoArr);
	}
}
function generateImg(photoArr){
	var photoStr = "";
	var imgSrc="", imgName=""; 
	var sliderNo = 0;
	for ( var i = 0; i < photoArr.length; i++) {
		GLOBAL_HOTELIMAGESRC = "";//clear this value for each loop
		imgSrc="";//clear this value for each loop
		imgName = photoArr[i];//   "/tnimages/hotelalliance/-16.jpg"
		imgSrc = CommonUtil.getValidString(PoiCacheHelper.getLogoImageCache(imgName));//iVBORw0UhEABQCAYAAACOEfKtAA...
		if(imgSrc == ""){
			generateImgSrc(imgName);
			imgSrc = GLOBAL_HOTELIMAGESRC;
			PoiCacheHelper.setLogoImageCache(imgName,imgSrc);
		}
		if(imgSrc && imgSrc != ""){
			photoStr += '<li class="sliderLI" onclick="SLIDER.onClickImage(' + sliderNo + ')">';
			photoStr += '<img class="clsSliderImg" src="data:image/gif;base64,'+imgSrc+'"/></li>';
			sliderNo ++;
		}
		
	}
	$("#sliderUL").html(photoStr);
	SLIDER.init();
}
/**
 * TODO    get image src from other Telenav Server according to imgName
 * @author xljiang
 * @Date   2011.8.18
 * @param imgName
 * @returns imgSrc		imgName:   /tnimages/hotelalliance/-16.jpg
 * 						imgSrc:	   iVBORw0UhEABQCAYAAACOEfKtAA...
 */
function generateImgSrc(imgName){
	var searchCriteria = '{"imageName":	"' + imgName + '",'+
						  '"width":	"90",'+
						  '"height":"90"'+
						 '}';
	var ajxUrl = GLOBAL_hostUrl + "getLogImage.do?jsonStr=" + searchCriteria + "&" + CommonUtil.getClientInfo();
	var ajaxOptions = {
			data:0,
			loadingStyle:1,
			url:ajxUrl,
			onSuccess:getImgSrc
	};
	CommonUtil.ajax(ajaxOptions);
}
function getImgSrc(responseText){
	var result = CommonUtil.getValidString(responseText);
	if("" != result){
	  	var resultObj =  JSON.parse(result);
	  	var image = resultObj.image;
	  	if( image != '' && image != null){
	  		GLOBAL_HOTELIMAGESRC = image;
  		}
	}
}
function generateAmenitiesImage(amenitiesArr) {
	var config = new Array();
	config[0] = new AmenityItem(1, "poi_icon_airport_shuttle", false);
	config[1] = new AmenityItem(2, "poi_icon_social_hour", false);
	config[2] = new AmenityItem(3, "poi_icon_fitness_center", false);
	config[3] = new AmenityItem(4, "poi_icon_internet_service_available", false);
	config[4] = new AmenityItem(5, "poi_icon_local_phone_service", false);
	config[5] = new AmenityItem(6, "poi_icon_breakfast", false);
	config[6] = new AmenityItem(7, "poi_icon_pet_friendly", false);
	config[7] = new AmenityItem(8, "poi_icon_pool", false);
	config[8] = new AmenityItem(9, "poi_icon_restaurant", false);
	config[9] = new AmenityItem(10, "poi_icon_kitchen_available", false);

	for ( var i in amenitiesArr) {
		for ( var j in config) {
			if (amenitiesArr[i] == config[j].id) {
				contains = true;
				config[j].hasAmenity = true;
				break;
			}
		}
	}

	var resultString = "";

	for ( var i = 0; i < config.length; i++) {
		if (i != config.length - 1) {
			resultString += config[i].toImg() + "&nbsp;&nbsp;";
		} else {
			resultString += config[i].toImg();
		}
	}

	return resultString;
}
function AmenityItem(id, iconPrefix, hasAmenity) {
	this.id = id;
	this.iconPrefix = iconPrefix;
	this.hasAmenity = false;
}

AmenityItem.prototype = {
	toImg : function() {
		var style;
		if (this.hasAmenity) {
			style = "_focusd";
		} else {
			style = "_unfocusd";
		}

		var result = "<img src='" + GLOBAL_sharedImageUrl + this.iconPrefix + style + ".png' />";
		return result;
	}
};

function findRooms() {
	if (!Global_calendar.getCheckInDate) {
		CommonUtil.showAlert("",I18NHelper["hotel.chooseCheckIn"],I18NHelper["common.button.OK"]);
	}

	if (!Global_calendar.getCheckOutDate) {
		CommonUtil.showAlert("",I18NHelper["hotel.chooseCheckOut"],I18NHelper["common.button.OK"]);
	}

	if (Global_nightQuantity <= 0) {
		CommonUtil.showAlert("", I18NHelper["hotel.chooseNightQuantity"], I18NHelper["common.button.OK"]);
		return;
	}

	if (Global_roomQuantity <= 0) {
		CommonUtil.showAlert("", I18NHelper["hotel.chooseRoomQuantity"],I18NHelper["common.button.OK"]);
		return;
	}

	if (Global_guestQuantity <= 0) {
		CommonUtil.showAlert("", I18NHelper["hotel.chooseGuestQuantity"],I18NHelper["common.button.OK"]);
		return;
	}

	var searchCriteria = new HotelSearchCriteria(null, Global_calendar.getCheckInDate(), Global_calendar.getCheckOutDate(), null, null, null, null,
			null, Global_nightQuantity, Global_roomQuantity, Global_guestQuantity);
	
	//get partnerPoiId. room searching will use this value
	var hotelDetail = getHotelDetailCache(getPoiId());
	var partnerPoiId = null;
	if(hotelDetail){
		partnerPoiId = JSON.parse(hotelDetail).partnerPoiId;
	}
	if(!partnerPoiId){
		CommonUtil.showAlert("",I18NHelper["hotel.noHotelDetail"],I18NHelper["common.button.OK"]);
		return;
	}
	setHotelInfo(partnerPoiId, JSON.stringify(searchCriteria));//modifed by jxl. avoid can't fetch data from hotelDetail Web service
	
	//forward to roomList.jsp of LocalApps project
	var roomListUrl = LocalAppsURLHelper["HOTEL"] + "&" + CommonUtil.getClientInfo();
	SDKAPI.launchLocalApp(roomListUrl);
}

/**
 * TODO		get hotel basic information such as name, telephone etc. from poi data.
 * 			Avoid the error because of null data from hotel detail web service.
 * @param	partnerPoiId	corresponding with hotelId in roomSearch operation
 * @param   searchCriteria
 */
function setHotelInfo(partnerPoiId, searchCriteria) {
	var hotelStr = '{"logoUrl":"", "name":"", "rating":"", "phoneNo":"","stop":"","id":""}';
	var hotelJson = JSON.parse(hotelStr);
	var poiData = JSON.parse(CommonUtil.getFromCache(appendPoiKey(PoiCacheKeys.POIDETAIL))).poi;
	hotelJson.logoUrl = document.getElementById("logImageDiv").getElementsByTagName("IMG")[0].src;//fetch this img src from logo div
	hotelJson.name = poiData.bizPoi.brand;
	hotelJson.rating = poiData.rating;
	hotelJson.phoneNo = poiData.bizPoi.phoneNumber;
	hotelJson.stop = poiData.bizPoi.stop;
	hotelJson.id = partnerPoiId;
	
	//save these information to client side
	navigator.tnservice.deletePersistentData("FOR_LOCALAPPS_DATA",function(){},function(){});
	var hotelInfo = '{"CURRENT_HOTEL":'+JSON.stringify(hotelJson)+', "SEARCH_CRITERIA":'+searchCriteria+'}';
	var localAppsData = '{"HOTEL":'+hotelInfo+'}';
	navigator.tnservice.setPersistentData("FOR_LOCALAPPS_DATA",localAppsData,"",function(){},function(){});
}
/** For Cache* */
function setHotelDetailCache(data, poiId) {
	sessionStorage.setItem("SS_HOTEL_CURRENT_HOTEL" + poiId, data);
}

function getHotelDetailCache(poiId) {
	return sessionStorage.getItem("SS_HOTEL_CURRENT_HOTEL" + poiId);
}

function HotelSearchCriteria(location, checkInDate, checkOutDate, maxDistance, minPrice, maxPrice, minRating,
		maxRating, nightQuantity, roomQuantity, guestQuantity) {
	this.location = location;
	this.checkInDate = checkInDate;
	this.checkOutDate = checkOutDate;
	this.maxDistance = maxDistance;
	this.minPrice = minPrice;
	this.maxPrice = maxPrice;
	this.minRating = minRating;
	this.maxRating = maxRating;
	this.nightQuantity = nightQuantity;
	this.roomQuantity = roomQuantity;
	this.guestQuantity = guestQuantity;
}
/**
 * TODO	get id of current poi cached in "PoiDetailCache"
 */
function getPoiId(){
	return JSON.parse(CommonUtil.getFromCache(appendPoiKey(PoiCacheKeys.POIDETAIL))).poi.bizPoi.poiId;
}
/**
 * generate hotel main tab and reserve tab
 */
function writeHtml(){
	var mainTabObj = document.getElementById("poihotelmain");
	var reserveTabObj = document.getElementById("poihotelreserve");
	var mainTabHtml="";
	var reserveTabHtml="";
	
	//1. generate hotel main tab html
	mainTabHtml = 
		'<table class="clsPicsTable">' +
		'	<tr>' +
		'		<td id="photoList" height="100%" width="100%" align="center" valign="middle">' +
		'			<div id="imageSliderDiv" class="imageSlider"><ul id="sliderUL"></ul></div>' +
		'		</td>' +
		'	</tr>' +
		'</table>' +
		'<table class="clsAmenitiesTable">' +
		'	<tr>' +
		'		<td align="left" valign="middle" class="clsFontColor_black bold clsAmenities fs_small">'+I18NHelper["hotel.amenities"]+'</td>' +
		'		<td align="right" id="amenityList" valign="middle" class="clsAmenitiesItem"></td>' +
		'	</tr>' +
		'</table>' +
		'<table class="clsDescriptionTable">' +
		'	<tr>' +
		'		<td valign="top" class="clsFontColor_black fs_small">' +
		'			<span class="clsFontColor_black bold" >'+I18NHelper["hotel.propertyDesc"]+':&nbsp;</span><span id="hotelDesc" class="clsFontColor_gray"></span>' +
		'		</td>' +
		'	</tr>' +
		'</table>' +
		'<table class="DescTable">' +
		'	<tr>' +
		'		<td valign="top" class="clsFontColor_black fs_small">' +
		'			<span class="clsFontColor_black bold" >'+I18NHelper["hotel.locationDesc"]+':</span>&nbsp;<span id="locationDesc" class="clsFontColor_gray"></span>' +
		'		</td>' +
		'	</tr>' +
		'</table>' +
		'<table class="DescTable">' +
		'	<tr>' +
		'		<td valign="top" >' +
		'			<div class="clsFontColor_black bold fs_small" >'+I18NHelper["hotel.propertyfacts"]+':</div>' +
		'			<div class="clsFontColor_gray fs_small" >'+I18NHelper["hotel.totalRooms"]+':&nbsp;<span id="hotel_totalRooms" class="fw_bold"></span></div>' +
		'			<div class="clsFontColor_gray fs_small" >'+I18NHelper["hotel.totalFloors"]+':&nbsp;<span id="hotel_totalFloors" class="fw_bold"></span></div>' +
		'			<div class="clsFontColor_gray fs_small" >'+I18NHelper["hotel.yearBuilt"]+':&nbsp;<span id="hotel_yearBuilt" class="fw_bold"></span></div>' +
		'			<div class="clsFontColor_gray fs_small" >'+I18NHelper["hotel.lastRenovation"]+':&nbsp;<span id="hotel_lastRenovation" class="fw_bold"></span></div>' +
		'		</td>' +
		'	</tr>' +
		'</table>';
	mainTabObj.innerHTML = mainTabHtml;


	// 2. generate hotel reserve tab html
	reserveTabHtml = 
		'<div class="clsConfigBorder">'+
		'	<table class="clsConfigRow clsTopItemRadius clsFontColor_gray">'+
		'		<tr>'+
		'			<td align="left" class="clsLeftCheckInTd" width="50%">'+
		'				<table class="clsContentWrapper" height="100%" width="100%" onclick="Global_calendar.showForCheckIn();">'+
		'					<tr>'+
		'						<td height="100%" width="75%">'+
		'							<table>'+
		'								<tr height="50%"><td align="left"><span class="clsFontColor_gray bold fs_small" >'+I18NHelper["hotel.checkIn"]+'</span></td></tr>'+
		'								<tr height="50%"><td align="left"><span class="clsHotelDate clsFontColor_blue" id="checkInDate">&nbsp; </span></td></tr>'+
		'							</table>'+
		'							</td>'+
		'					<td height="100%" width="25%" align="right" ><img src="'+GLOBAL_sharedImageUrl+'hotel_calendar_icon_unfocused.png"></td>'+
		'					</tr>'+
		'				</table>'+
		'			</td>'+
		'			<td align="left" width="50%">'+
		'				<table class="clsContentWrapper" height="100%" width="100%" onclick="Global_calendar.showForCheckOut();">'+
		'					<tr>'+
		'						<td height="100%" width="80%">'+
		'							<table>'+
		'								<tr height="50%"><td align="left"><span class="clsFontColor_gray bold fs_small" >'+I18NHelper["hotel.checkOut"]+'</span></td></tr>'+
		'								<tr height="50%"><td align="left"><span class="clsHotelDate clsFontColor_blue" id="checkOutDate">&nbsp; </span></td></tr>'+
		'								</table>'+
		'						</td>'+
		'						<td height="100%" width="20%" align="right"><img src="'+GLOBAL_sharedImageUrl+'hotel_calendar_icon_unfocused.png"></td>'+
		'					</tr>'+
		'				</table>'+
		'			</td>'+
		'		</tr>'+
		'	</table>'+
		'	<table class="clsConfigRow clsMiddleItemRadius clsFontColor_gray bold">'+
		'		<tr>'+
		'			<td width="50%" align="left" class="clsContentWrapper fs_small"><span>'+I18NHelper["hotel.nights"]+'</span></td>'+
		'			<td width="50%" align="right">'+
		'				<table class="clsAddAndSub" >'+
		'					<tr>'+
		'						<td onClick="subQuantity(1)" width="50px" height="100%" align="center" valign="middle"><img id="nightSubImg" src="'+GLOBAL_sharedImageUrl+'subtract_icon_disabled.png" class="clsAddSubSyle" /></td>'+
		'						<td id="nightQuantity" width="40px" height="100%" align="center" valign="middle" class="clsFontColor_gray fs_small bold">0</td>'+
		'						<td onClick="addQuantity(1)" width="50px" height="100%" align="center" valign="middle"><img id="nightAddImg" src="'+GLOBAL_sharedImageUrl+'plus_icon_unfocused.png" class="clsAddSubSyle" /></td>'+
		'					</tr>'+
		'				</table>'+
		'			</td>'+
		'		</tr>'+
		'	</table>'+
		'	<table class="clsConfigRow clsMiddleItemRadius  clsFontColor_gray bold">'+
		'		<tr>'+
		'			<td width="50%" align="left" class="clsContentWrapper fs_small"><span >'+I18NHelper["hotel.rooms"]+'</span></td>'+
		'			<td width="50%" align="right">'+
		'				<table class="clsAddAndSub" >'+
		'					<tr>'+
		'						<td onClick="subQuantity(2)" width="50px" height="100%" align="center" valign="middle"><img id="roomSubImg" src="'+GLOBAL_sharedImageUrl+'subtract_icon_unfocused.png" class="clsAddSubSyle" /></td>'+
		'						<td id="roomQuantity" width="40px" height="100%" align="center" valign="middle" class="fs_small bold clsFontColor_gray">1</td>'+
		'						<td onClick="addQuantity(2)" width="50px" height="100%" align="center" valign="middle"><img id="roomAddImg" src="'+GLOBAL_sharedImageUrl+'plus_icon_unfocused.png" class="clsAddSubSyle" /></td>'+
		'					</tr>'+
		'				</table>'+
		'			</td>'+
		'		</tr>'+
		'	</table>'+
		'	<table class="clsConfigRow clsBottomItemRadius fs_small clsFontColor_gray bold">'+
		'		<tr>'+
		'			<td width="50%" class="clsContentWrapper fs_small" align="left"><span>'+I18NHelper["hotel.guests"]+'</span></td>'+
		'			<td width="50%" align="right">'+
		'				<table class="clsAddAndSub" >'+
		'					<tr>'+
		'						<td onClick="subQuantity(3)" width="50px" height="100%" align="center" valign="middle"><img id="guestSubImg" src="'+GLOBAL_sharedImageUrl+'subtract_icon_unfocused.png" class="clsAddSubSyle" /></td>'+
		'						<td id="guestQuantity" width="40px" height="100%" align="center" valign="middle" class="fs_small bold clsFontColor_gray">1</td>'+
		'						<td onClick="addQuantity(3)" width="50px" height="100%" align="center" valign="middle"><img id="guestAddImg" src="'+GLOBAL_sharedImageUrl+'plus_icon_unfocused.png" class="clsAddSubSyle" /></td>'+
		'					</tr>'+
		'				</table>'+
		'			</td>'+
		'		</tr>'+
		'	</table>'+
		'</div>'+
		'<table class="clsSearchBar">'+
		'	<tr valign="middle">'+
		'		<td align="center" valign="middle" width="100%" height="100%">'+
		'			<div class="clsButtonWrapper">'+
		'				<table id="searchButton" class="clsBtnSkeleton clsLargeRadius clsbtnfontnormal fs_veryLarge clsButtonBgNormal"'+
		'					onTouchStart="changeCSS(this,\'clsBtnSkeleton clsLargeRadius clsFontColor_white fs_veryLarge clsButtonBgHighlight\')"'+
		'					onTouchEnd="changeCSS(this,\'clsBtnSkeleton clsLargeRadius clsbtnfontnormal fs_veryLarge clsButtonBgNormal\')" onclick="findRooms()">'+
		'					<tr><td align="center" class="fs_veryLarge">'+I18NHelper["hotel.search"]+'</td></tr>'+
		'				</table>'+
		'			</div>'+
		'		</td>'+
		'	</tr>'+
		'</table>';
	reserveTabObj.innerHTML = reserveTabHtml;
}
var GLOBAL_isHotelDetailLoaded = false;
writeHtml();