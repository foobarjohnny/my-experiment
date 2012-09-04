var getDistanceEventHandler;
var Global_newVersion = false;
function onClickMore()
{
	event.stopPropagation();
	var detailUrl = GLOBAL_serverUrl + "/html/adsinfo.do?operateType=detail" + "&" + CommonUtil.getClientInfo()+"&isDummy="+$("#isDummy").val();
	//console.log(detailUrl);
	recordVbbMisLog_Detail_View_MoreInfo_Click();
	goToAdsDetail(detailUrl);
}

function goToAdsDetail(detailUrl)
{
	try
	{
		function onsuc(pre){
			
		}
		function onerr(){
			alert(I18NHelper["ads.displayDetailErr"]);
		}
		var data = {
					"url":detailUrl
				   };
		navigator.tnprivateservice.doPrivateService("DisplayBillboardDetail",data,onsuc,onerr);
	}
	catch(e)
	{
		//alert(e);
		location.href = detailUrl;
	}
}

function initPhoneGap()
{
	document.addEventListener("deviceready", onDeviceReady, false);
}

function onDeviceReady()
{
	initData();
}

function onClickDrive()
{
	event.stopPropagation();
	var callbackurl = "";
	var oriaddr = "";
	var desaddr = getAdsAddress();
	console.log("address:" + JSON.stringify(desaddr));
	function onsuc(){
	}
	function onerr(){
		alert(I18NHelper["common.navigationErr"]);
	}
	if(isAddressValid())
	{
		recordVbbMisLog_Detail_View_End(MisLogConstants.EXIT_REASON_DRIVE_TO,getDuration());
		recordVbbMisLog_Detail_View_DriveTo();
		navigator.tnservice.navTo(oriaddr,desaddr,callbackurl,onsuc,onerr);
	}
}

function getDuration()
{
	var currentDate = new Date();
	return currentDate.getTime() - GLOBAL_enterTime;
}

function getAdsAddress()
{
	var address = {
			"id":"0",
			"selectedIndex":0,
			"stop":{
				"lat":$("#lat").val(),
				"lon":$("#lon").val(),
				"firstLine":$("#firstLine").val(),
				"label":$("#label").val(),
				"city":$("#city").val(),
				"province":$("#state").val(),
				"zip":$("#zip").val()
				}
			};
	return address;
}

function isAddressValid()
{
	return true;
}

function onClickBackGround()
{
	var flag = PoiCacheHelper.getOpenCloseFlag();
	if("open" == flag)
	{
		PoiCacheHelper.setOpenCloseFlag("close");
		if(Global_newVersion){
			$("#topRightImage").attr("class","openPoi713");
		}else{
			$("#openCloseImage").attr("class","openPoi");
		}
		changeDisplayMode(0);
	}
	else
	{
		recordVbbMisLog_Initial_View_Click();
		PoiCacheHelper.setOpenCloseFlag("open");
		if(Global_newVersion){
			$("#topRightImage").attr("class","closePoi713");
		}else{
			$("#openCloseImage").attr("class","closePoi");
		}
		changeDisplayMode(1);
	}
}

function initData()
{
	getAdsDistance();
}

function clearInterval()
{
	window.clearInterval(getDistanceEventHandler);
}

function changeDisplayMode(mode)
{
	var data = {
			"mode":mode
	};
	SDKAPI.invokePrivateService("ChangeBillboardMode",data);
}

function setAdsDistance(d)
{
	var data = JSON.parse(d);
	var temp = ""+data.distance;
	$("#distance").html(temp);
	PoiCacheHelper.setDistanceTextCacheForAds(temp);
}

function getAdsDistance()
{
	var lat = $("#lat").val();
	var lon = $("#lon").val();
	if(!lat || lat==0 || !lon || lon==0){
		return;
	}
	
	var data = {
			"lat":lat,
			"lon":lon
		   };
	SDKAPI.invokePrivateService("GetBillboardDistance",data,setAdsDistance);
}



function displayEmptyLogo()
{
	$("#logImage").attr("src",GLOBAL_imageCommonUrl+"poi_details_default_logo_unfocused.png");
}

function highLightNavBtn(element){
	var src = $("#navImg").attr("class").replace("poi_details_driveto_icon_unfocused","poi_details_driveto_icon_focused");
	$("#navImg").attr("class",src);
	
	switchHightlight(element,"clsNavButtonBgNormal","clsNavButtonBgHL");
	switchHightlight(element,"clsNavFontColor","clsNavFontColorHL");
}

function dishighLightNavBtn(element){
	var src = $("#navImg").attr("class").replace("poi_details_driveto_icon_focused","poi_details_driveto_icon_unfocused");
	$("#navImg").attr("class",src);
	
	switchHightlight(element,"clsNavButtonBgHL","clsNavButtonBgNormal");
	switchHightlight(element,"clsNavFontColorHL","clsNavFontColor");
}

function highLightPhone(element){
	var imageId = "#phonePic";
	if(element.id=="phoneButtonLandscape")
	{
		imageId = "#phonePicLandscape";
	}
		
	var src = $(imageId).attr("class").replace("poi_details_call_icon_unfocused","poi_details_call_icon_focused");
	$(imageId).attr("class",src);
	highlightBtnAll(element,"clsPhoneNoColor","clsPhoneNoColorHL");
	disHighlightBtnAll(element,"clsPhoneBtnBgNormal","clsPhoneBtnBgHL");
	
	if(CommonUtil.isScoutStyle()){
		$("#phonePicCallForScout").attr("class",'call_icon_focused');
	}
}
function dishighLightPhone(element){
	var imageId = "#phonePic";
	if(element.id=="phoneButtonLandscape")
	{
		imageId = "#phonePicLandscape";
	}
	
	var src = $(imageId).attr("class").replace("poi_details_call_icon_focused","poi_details_call_icon_unfocused");
	$(imageId).attr("class",src);
	disHighlightBtnAll(element,"clsPhoneNoColorHL","clsPhoneNoColor");
	disHighlightBtnAll(element,"clsPhoneBtnBgHL","clsPhoneBtnBgNormal");
	
	if(CommonUtil.isScoutStyle()){
		$("#phonePicCallForScout").attr("class",'call_icon_unfocused');
	}
}

function initLayout(){
	var no = 0;
	var version = $("version").val();
	var versionArr = version.split(".");
	//version = 7.2.05 ==> no = 725
	if(versionArr){
		var b = versionArr[0] || 0,
			s = versionArr[1] || 0,
			g = versionArr[2] || 0;
		no = parseInt(b) * 100 + parseInt(s) * 10 + parseInt(g);
	}
	if(no >= 713){
		$("oldImgDiv").hide();
		$("topRightDiv").show();
		$("topTitleDiv").height(topTitleDivHeight);//set new height for title part
		$("container_div").height(containerDivHeight);//set new height for container_div part
		Global_newVersion = true;
	}
}