function log(msg)
{
	console.log(msg);
}

function highlightDIV(element)
{
	switchHightlight(element,"itemBackground","itemBackgroundHilight");
	
	switchHightlight(document.getElementById("messageMiddle"),"adjuggler_fontColor_gray","clsFontColor_white");
	switchHightlight(document.getElementById("messageLarge"),"adjuggler_fontColor_gray","clsFontColor_white");
	switchHightlight(document.getElementById("firstLine"),"adjuggler_fontColor_blue","clsFontColor_white");
	switchHightlight(document.getElementById("secondLine"),"adjuggler_fontColor_gray","clsFontColor_white");
	switchHightlight(document.getElementById("weartherLine1"),"adjuggler_fontColor_blue","clsFontColor_white");
	switchHightlight(document.getElementById("weartherLine2"),"adjuggler_fontColor_gray","clsFontColor_white");
	switchHightlight(document.getElementById("temperatureForcast"),"adjuggler_fontColor_gray","clsFontColor_white");
	
	if(null != document.getElementById("clickIcon")){
		document.getElementById("clickIcon").src = GLOBAL_IMAGE_URL + "arrowIcon_focused.png";
	}
}

function disHighlightDIV(element)
{
	switchHightlight(element,"itemBackgroundHilight","itemBackground");
	
	switchHightlight(document.getElementById("messageMiddle"),"clsFontColor_white","adjuggler_fontColor_gray");
	switchHightlight(document.getElementById("messageLarge"),"clsFontColor_white","adjuggler_fontColor_gray");
	switchHightlight(document.getElementById("firstLine"),"clsFontColor_white","adjuggler_fontColor_blue");
	switchHightlight(document.getElementById("secondLine"),"clsFontColor_white","adjuggler_fontColor_gray");
	switchHightlight(document.getElementById("weartherLine1"),"clsFontColor_white","adjuggler_fontColor_blue");
	switchHightlight(document.getElementById("weartherLine2"),"clsFontColor_white","adjuggler_fontColor_gray");
	switchHightlight(document.getElementById("temperatureForcast"),"clsFontColor_white","adjuggler_fontColor_gray");
	
	if(null != document.getElementById("clickIcon")){
		document.getElementById("clickIcon").src = GLOBAL_IMAGE_URL + "arrowIcon_unfocused.png";
	}
}

function CheckAdjuggler()
{
	startRefreshAd();
	
	var lastNeedPurchase = getNeedPurchase();
	var paramJson = JSON.parse(GLOBAL_PARAMJSON);
	var needPurchase = paramJson.needPurchase;
	if(null == lastNeedPurchase){
		saveNeedPurchase(needPurchase);
	}else if(lastNeedPurchase != needPurchase){
		removeAdjugglerJSON();
		saveNeedPurchase(needPurchase);
	}
	var adJSON = getAdjugglerJSON();
	if(null == adJSON){
		getAdjugglerFromWebSite();
	}else{
		showAdjugglerInfo(JSON.parse(adJSON));
		if(checkExpire()){
			getAdjugglerFromWebSite();
		}
	}
}

function getAdjugglerFromWebSite()
{
	var ajxUrl = GLOBAL_ADDON_LOCATOR + "/html/" + "CheckAdJuggler.do?" + CommonUtil.getClientInfo() + "&appCode=" + GLOBAL_APPCODE+ "&ssoToken=" + encodeURIComponent(GLOBAL_SSOTOKEN) +"&jsonStr=" + GLOBAL_PARAMJSON;
	var ajaxOptions = {
			data:0,
			url:ajxUrl,
			onSuccess:ajaxCallBackAdjuggler
	};
	CommonUtil.ajax(ajaxOptions);
}

function ajaxCallBackAdjuggler(data)
{
	var adJSON = JSON.parse(data);
	showAdjugglerInfo(adJSON);
	saveCurrentTime();
	saveAdjugglerJSON(data);
	refreshAd();
}

function refreshAd()
{
	var refreshState = getRefreshFlag();
	log("refreshAd.............."+refreshState);
	if("YES" == refreshState){
		var time = getTimeForRefreshInterval();
	    if(time > 0){
	 		setTimeout("refreshAd()",time);
	 	}else{
	 		getAdjugglerFromWebSite();
	 	}
	}
}


function startRefreshAd()
{
	saveRefreshFlag("YES")
	var stopRefreshTime = 120;
	setTimeout("stopRefreshAd()",stopRefreshTime*1000);
}

function stopRefreshAd()
{
	saveRefreshFlag("NO");
}


function saveCurrentTime()
{
	var date = new Date();
	saveLastTime(date.getTime());
}

function getTimeForRefreshInterval()
{
	var adJSON = getAdjugglerJSON();
	if(null == adJSON){
		return 0;
	}
	adJSON = JSON.parse(adJSON);
	if(null == adJSON.expireTime){
		return 0;
	}
	var lastTime = getLastTime();
	var expireTime = adJSON.expireTime;
	var date = new Date();
    var currentTime = date.getTime();
    var time = currentTime - lastTime;
    var remainingTime = expireTime - time;
    return remainingTime;
}

function checkExpire()
{
	var adJSON = getAdjugglerJSON();
	if (null == adJSON){
		return true;
	}
	
	adJSON = JSON.parse(adJSON);
	if (null == adJSON.expireTime){
		return true;
	}
	
	var lastTime = getLastTime();
	if (0 == lastTime){
		return true;
	}
	
	var expireTime = adJSON.expireTime;
	var date = new Date();
	var currentTime = date.getTime();
	var time = currentTime - lastTime
	
	if (time >= expireTime){
		return true;
	}
	
	return false;
}

function showAdjugglerInfo(adJSON)
{
	var htmlText = "<table width='100%' height='100%' border='0' cellpadding='0' cellspacing='0'><tr>";
	var textJSON = adJSON.text;
	var locale = $("#locale").val();
	var message = textJSON[locale];
	if(null == message){
		message = textJSON.en_US;
	}
	if(null != adJSON.iconImage){
		var iconUrl = GLOBAL_IMAGE_URL + adJSON.iconImage;
		try{
			var weartherJson = JSON.parse(message);
			var actionJSON = adJSON.action;
			var key = actionJSON.type;
			if(GLOBAL_ADJUGGLER_WEATHER == key){
				htmlText = showWeatherHtml(weartherJson, htmlText, iconUrl);
			}
		}catch(e){
			htmlText += "<td class='adIcon'><img src='"+ iconUrl +"'/></td>" +
			"<td id='messageMiddle' class='messageMiddle adjuggler_fontColor_gray'>" + message  + "</td>";
		}
	}else{
		try{
			var weartherJson = message;
			if(null != weartherJson.firstLine && null != weartherJson.secondLine){
				var firstLine = weartherJson.firstLine;
				var secondLine = weartherJson.secondLine;
				htmlText += "<td><table width='100%' height='100%' border='0' cellpadding='0' cellspacing='0'>" +
						"<tr><td id='firstLine' class='firstLine adjuggler_fontColor_blue'>" + firstLine + "</td></tr>" +
						"<tr><td id='secondLine' class='secondLine adjuggler_fontColor_gray'>" + secondLine + "</td></tr>" +
						"</table></td>";
			}else{
				htmlText += "<td id='messageLarge' class='messageLarge adjuggler_fontColor_gray'>" + message + "</td>";
			}
		}catch(e){
			htmlText += "<td id='messageLarge' class='messageLarge adjuggler_fontColor_gray'>" + message + "</td>";
		}
	}
	htmlText += "<td class='clickIcon'><image id='clickIcon' src='" + GLOBAL_IMAGE_URL + "arrowIcon_unfocused.png' /></td></tr></table>";
	log(htmlText);
	
	$("#mainDiv").html(htmlText);
	
	recordMISLog(GLOBAL_AD_BANNER_VIEW,adJSON);
}

function showWeatherHtml(weartherJson, htmlText, iconUrl)
{
	var location = weartherJson.location;
	var currentTemperature = weartherJson.currentTemperature;
	var lowTemperature = weartherJson.lowTemperature;
	var highTemperature = weartherJson.highTemperature;
	
	htmlText += "<td class='adIcon'><img src='"+ iconUrl +"'/></td>" +
			"<td><table width='100%' height='100%' border='0' cellpadding='0' cellspacing='0'>" +
			"	<tr><td id='weartherLine1' colspan='2' class='weartherLine1 adjuggler_fontColor_blue'>" + location + "</td></tr>" +
			"	<tr><td id='weartherLine2' class='weartherLine2 adjuggler_fontColor_gray'>" + currentTemperature +
					"<td id='temperatureForcast' class='temperatureForcast adjuggler_fontColor_gray'>H " + highTemperature + "   L " + lowTemperature + "</td></tr>" +
			"</table></td>";
	
	return htmlText;
}

function adjugglerClick()
{
	var adJSON = getAdjugglerJSON();
	if(null == adJSON){
	}
	adJSON = JSON.parse(adJSON);
	recordMISLog(GLOBAL_AD_BANNER_CLICK,adJSON);
	var actionJSON = adJSON.action;
	var key = actionJSON.type;
	if (null != key && "" != key){
		if(GLOBAL_ADJUGGLER_WEATHER == key)
		{
			var weatherUrl = GLOBAL_HOSTURL_IP + "weather.do?" + CommonUtil.getClientInfo();
			SDKAPI.launchLocalApp(weatherUrl);
		}else if(GLOBAL_ADJUGGLER_URL == key){
			if(null != actionJSON.url){
				var url = actionJSON.url;
				SDKAPI.launchLocalApp(url);
			}
		}
	}
}

/********************mislog for adjuggler*************************/
function recordMISLog(eventId, adJSON)
{
	var pageName = "HomeMain";
	var campaignID = CommonUtil.getValidString(adJSON.campaignID);
	var accountType = $("#productType").val();
	var textJSON = adJSON.text;
	var locale = $("#locale").val();
	var message = textJSON[locale];
	
	var misLogObj = getMISLogObj(pageName, campaignID, accountType, message);
	recordMisLogOfVbb(eventId, misLogObj);
}

function getMISLogObj(pageName, campaignID, accountType, message)       
{   
	var misLogObj = {};
	misLogObj[GLOBAL_PAGE_NAME_TML] = pageName;
	misLogObj[GLOBAL_AD_BANNER_MSG] = campaignID;
	misLogObj[GLOBAL_ACCOUNT_TYPE] = accountType;
	misLogObj[GLOBAL_MESSAGE_NAME] = message;
	
	return misLogObj;
}

function recordMisLogOfVbb(eventId, misLogObj)
{
	log("record vbb mis log eventId:" + eventId + ",value:" + JSON.stringify(misLogObj));
	SDKAPI.logEvent(eventId,misLogObj);
}

/********************cache of adjuggler*************************/
function saveAdjugglerJSON(adJSON)
{
	localStorage.setItem(getAdJSONCacheKey(),adJSON);
}

function getAdjugglerJSON()
{
	return localStorage.getItem(getAdJSONCacheKey());
}

function removeAdjugglerJSON()
{
	return localStorage.removeItem(getAdJSONCacheKey());
}

function saveLastTime(lastTime)
{
	localStorage.setItem(getLastTimeKey(),lastTime);
}

function getLastTime()
{
	return localStorage.getItem(getLastTimeKey());
}

function saveRefreshFlag(refreshFlag)
{
	localStorage.setItem(getRefreshFlagKey(),refreshFlag);
}

function getRefreshFlag()
{
	return localStorage.getItem(getRefreshFlagKey());
}

function saveNeedPurchase(needPurchase)
{
	localStorage.setItem(getNeedPurchaseKey(),needPurchase);
}

function getNeedPurchase()
{
	return localStorage.getItem(getNeedPurchaseKey());
}


function getAdJSONCacheKey()
{
	return "LOCAL_STORAGE_ADJUGGLER_JSON";
}

function getLastTimeKey()
{
	return "LOCAL_STORAGE_LAST_TIME";
}

function getRefreshFlagKey()
{
	return "LOCAL_STORAGE_REFRESH_FLAG";
}

function getNeedPurchaseKey()
{
	return "LOCAL_STORAGE_NEED_PURCHASE";
}