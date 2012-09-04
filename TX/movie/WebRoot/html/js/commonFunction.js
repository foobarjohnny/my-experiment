//only upsell uses this function---start
function commonHighlight(element)
{
	if(arguments.length <= 1){
		return;
	}
	for(var i=1; i<arguments.length; i++){
		var switchClass = arguments[i];
		var originalClass = switchClass[0];
		var replacedClass = switchClass[1];
		switchHightlight(element,originalClass,replacedClass);
	}
}
//---end
var JSConstants = 
{
		DEGREE_MULTIPLIER : 100000
};

/**
 * Program Constants
 */
var ProgramConstants = {
		SCOUTPROG : "SCOUTPROG",
		SCOUTUSPROG : "SCOUTUSPROG",
		SCOUTEUPROG : "SCOUTEUPROG",
		TNNAVPROG : "TNNAVPROG",
		TNNAVPLUSPROG : "TNNAVPLUSPROG"
};

/**
 * Preference Constants
 */
var PreferenceConstants = {
		EMAIL		:	"email",
		DISUNIT		:	"disUnit",
		NICKNAME	:	"nickName",
		FIRSTNAME	:	"firstName",
		LASTNAME	:	"lastName",
		UDID		:	"udid",
		MACADDRESS	:	"macAddress",
		USERID      :	"userid",
		PTN			:	"ptn"
};

/**
 * Cache Constants
 */
var CacheKeys = {
		CLIENTINFO	:	"COMMON_API_SESSION_STORAGE_CLIENT_INFO"
};

String.prototype.format = function(){
		    var args = arguments;
		    return this.replace(/\{(\d+)\}/g,function(m,i,o,n){
		        return args[i];
		    });
};

function onErrorExitBrowser(){}

function onSuccessExitBrowser(){}

function getDummyAddressObj()
{	
	var address = {
			"lat" : 	3761386,
			"lon" :		-12239382,
			"label":	"",
			"firstLine":"SFO Airport",
			"city":		"BURLINGAME",
			"state":	"CA",
			"zip":		"",
			"country":	"US"
		};
	
	return address;		
}


function highlightBtnAll(e,originalClass,replacedClass)
{
	highlightBtn(e);
	if(arguments.length > 2)
	{
		switchHightlight(e,originalClass,replacedClass);	
	}
	else
	{
		switchHightlight(e,"fc_gray","fc_white");
	}
}

function disHighlightBtnAll(e,originalClass,replacedClass)
{
	disHighlightBtn(e);
	if(arguments.length > 2)
	{
		switchHightlight(e,originalClass,replacedClass);	
	}
	else
	{
		switchHightlight(e,"fc_white","fc_gray");
	};
}

function highlightBtn(e)
{
	switchHightlight(e,"clsButtonBgNormal","clsButtonBgHighlight");
}

function disHighlightBtn(e)
{
	switchHightlight(e,"clsButtonBgHighlight","clsButtonBgNormal");
}

function switchHightlight(e,originalClass,replacedClass)
{
	if(null != e){
		cssHelper.replaceObjCSS(e,originalClass,replacedClass);
	}
}

function highLightItem(e)
{
	switchHightlight(e,"clsListBgNormal","clsListBgHighlight");
	switchHightlight(e,"fc_gray","fc_white");
}

function dishighLightItem(e)
{
	switchHightlight(e,"clsListBgHighlight","clsListBgNormal");
	switchHightlight(e,"fc_white","fc_gray");
}

function changeCSS(e, newClassName) {
	e.className=newClassName;
}

function BackButtonBuilder() {
	
}

BackButtonBuilder.prototype = {
	backButtonDivId : "backButtonDiv",
	titleBarContainerId:"titleBarContainerDiv",
	back:"Back",
	title:"",
	exitWebView: false,
	callBack:null,

	buildBackButton : function(divId,backLable,exitWebView, callBack) {
		if (divId) {
			this.backButtonDivId = divId;
		}
		
		if(backLable){
			this.back = backLable;
		}

		if(exitWebView == true || exitWebView == "true"){
			this.exitWebView = true;
		}
		else
		{
			this.exitWebView = false;
		}
		
		if(callBack){
			this.callBack = callBack;
		}
		
		if(exitWebView=="dashboard"){
			this.exitWebView = true;
			$("#" + this.backButtonDivId).html(this.getDashboardBackButtonHtml());
		}else{
			$("#" + this.backButtonDivId).html(this.getBackButtonHtml());
		}
		
	},
	
	buildBarAndBackButton : function(titleContainerId, backLable, titleLable, exitWebView) { 
		
		if(titleContainerId){
			this.titleBarContainerId = titleContainerId;
		}
		
		if(backLable){
			this.back = backLable;
		}
		
		if(titleLable){
			this.title = titleLable;
		}
		
		if(exitWebView == true || exitWebView == "true"){
			this.exitWebView = true;
		}
		else
		{
			this.exitWebView = false;
		}
		
		$("#" + this.titleBarContainerId).html(this.getTitleAndBackButtonHtml());
	},
	
	getBackButtonHtml : function() {
		var html = '<div onTouchStart="backButtonBuilder.highlight()" onTouchEnd="backButtonBuilder.dishighlight()" onTouchMove="backButtonBuilder.dishighlight()" onClick="backButtonBuilder.doGoBack()">'
				+ '<div class = "div_cell backBtnLeftGap"></div><div id="iphoneBackBtn" class="div_cell clsIphoneBackBtn clsIphoneBackBtnBG fw_bold fc_white fs_small"  style="vertical-align: middle" >'+this.back+'</div>'
				+ '</div>';
		return html;
	},
	
	getDashboardBackButtonHtml: function(){
		var html = '<div onTouchStart="backButtonBuilder.highlight_db()" onTouchEnd="backButtonBuilder.dishighlight_db()" onTouchMove="backButtonBuilder.dishighlight_db()" onClick="backButtonBuilder.doGoBack()">'
			+ '<div class = "div_cell backBtnLeftGap"></div><div id="iphoneBackBtn" class="div_cell clsIphoneBackBtn back_button_dashboard_unfocused"><span class="fw_bold fc_white fs_small">'+this.back+'</span></div>'
			+ '</div>';
		return html;
	},
	
	getTitleAndBackButtonHtml: function(){
		var html = '<div class = "clsTitleFrame clsTitleBg">'
					+'	<div class = "div_table">'
					+'	<div class = "div_cell" style="width: 20%; height: 100%; text-align: left; vertical-align: middle;">'+this.getBackButtonHtml()+'</div>'
					+'	<div class = "div_cell clsTitleContent">'+this.title+'</div>'
					+'	<div class = "div_cell" style="width: 20%;"></div>'
					+'  </div>'
				   +'</div>';
		
		return html;
	},

	doGoBack : function() {
		if(this.callBack){
			CommonUtil.debug("callBack....."+this.callBack);
			this.callBack();
		}else{
			SDKAPI.goBack(this.exitWebView);
		}
	},

	highlight : function() {
		$("#iphoneBackBtn").attr("class", "div_cell clsIphoneBackBtn clsIphoneBackBtnHLBG fw_bold fc_white fs_small");
	},
	dishighlight : function() {
		$("#iphoneBackBtn").attr("class", "div_cell clsIphoneBackBtn clsIphoneBackBtnBG fw_bold fc_white fs_small");
	},
	highlight_db : function() {
		$("#iphoneBackBtn").attr("class", "div_cell clsIphoneBackBtn back_button_dashboard_focused");
	},
	dishighlight_db : function() {
		$("#iphoneBackBtn").attr("class", "div_cell clsIphoneBackBtn back_button_dashboard_unfocused");
	}
};

var backButtonBuilder = null;

//addrType can have values "home" or "work"
function SDK_API_getAddress(callBackFunctionName, addrType) {
	var value = "";
	try {
		function onsuc(pre) {
			if (!!pre && toString.call(pre) == "[object Object]") {
				CommonUtil.debug("pre is JSON Object during SDK_API_getAddress, stringify pre");
				pre = JSON.stringify(pre);
			}
			SDK_API_getAddress_CallBack(callBackFunctionName, pre);
		}
		function onerr() {
			CommonUtil.debug("err during SDK_API_getAddress");
			SDK_API_getAddress_CallBack(callBackFunctionName);
		}
		navigator.tnservice.getAddress(addrType, onsuc, onerr);
	} catch (e) {
		CommonUtil.debug("exception during SDK_API_getAddress:" + e);
		SDK_API_getAddress_CallBack(callBackFunctionName);
	}

	return value;
}

function SDK_API_getAddress_CallBack(callBackFunctionName,value)
{
	callBackFunctionName(value);
}

// addrType can have values "home" or "work"
function SDK_API_setAddress(callBack, addr, addrType)
{
    try
    {
       function onsuc(pre){        
         SDK_API_setAddress_CallBack(callBack);
      }
       function onerr(){
           CommonUtil.debug("err during set address");
           SDK_API_setAddress_CallBack(callBack);
       }   
       navigator.tnservice.setAddress(addrType, JSON.parse(addr), onsuc,onerr);
    }
    catch(e)
    {   
    	CommonUtil.debug("exception during SDK_API_setAddress:" + e);
    	SDK_API_setAddress_CallBack(callBack);
    }
}

function SDK_API_setAddress_CallBack(callBack)
{
	callBack();
}

function SDK_API_launchSettings()
{
	TNService.prototype.launchSettings();
}

function SDKAPI() {}

SDKAPI.prototype = {
		setWindowMode : function()
		{
			try
			{
				navigator.tnservice.setWindowMode("app", null, null);
			}
			catch(e)
			{	
				CommonUtil.debug("exception during setWindowMode:" + e);
			}
		},

		logEvent : function(logType,logValue){
			function onsuc(){
				CommonUtil.debug("Succeed in invoking log event!");
			}
			function onerr(){
				CommonUtil.debug("Failed in invoking log event!");
			}
			try
			{
				navigator.tnservice.logEvent(logType, logValue,onsuc, onerr);
			}
			catch(e)
			{	
				CommonUtil.debug("exception during logEvent:" + e);
			}
		},
		
		getPreference : function(callBack,key)
		{
			try
			{
				function onsuc(pre){			
					callBack(pre);
				}
				function onerr(){
					CommonUtil.debug("err during fetch preference");
					callBack("");
				}	
				navigator.tnservice.getPreference(key,onsuc,onerr);
			}
			catch(e)
			{	
				CommonUtil.debug("exception during getPreference:" + e);
				callBack("");
			}
		},

		setPreference : function(callBack,key,value)
		{
			try
			{
				function onsuc(pre){			
					callBack();
				}
				function onerr(){
					CommonUtil.debug("err during set preference");
					callBack();
				}	
				navigator.tnservice.setPreference(key,value,null,onsuc,onerr);
			}
			catch(e)
			{	
				CommonUtil.debug("exception during setPreference:" + e);
				callBack();
			}
		},
		
		launchLocalApp : function(url)
		{
			try
			{
				function onsuc(){}
				function onerr(){
					CommonUtil.debug("launchLocalApp onerr");
				}	
				navigator.tnservice.launchLocalApp(url,onsuc,onerr);
			}
			catch(e)
			{	
				CommonUtil.debug("exception during launchLocalApp:" + e);
				location.href = url;
			}
		},
		
		getSSOToken : function(callBack,notEncode)
		{
			var token = "";
			try
				{
					function onsuc(pre){
						if(!notEncode){
							pre = encodeURIComponent(pre);
						}
						callBack(pre);
					}
					function onerr(){
						CommonUtil.debug("getTeleNavToken error");
					}	
					navigator.tnservice.getTeleNavToken(onsuc,onerr);
				}
				catch(e)
				{	
					CommonUtil.debug("exception during getSSOToken:" + e);
					callBack(token);
				}
		},
		
		getCurrentLocation : function(callback)
		{
			try
			{
				function onsuc(position){
					if(position.coords){
						callback(position);
					}else{
						CommonUtil.getCurrentLocation(callback);
					}
				}
				function onerr(){
					CommonUtil.getCurrentLocation(callback);
				}
				navigator.tnservice.geolocation.getCurrentPosition(onsuc,onerr,CommonUtil.getGpsOptions());
			}
			catch(e)
			{	
				CommonUtil.debug("exception during getCurrentLocation:" + e);
				CommonUtil.getCurrentLocation(callback);
			}
		},
		
		goBack : function(isExit){
			try {
				//alert(history.length);
				if (isExit == true){
					navigator.tnservice.exitBrowser(false,onSuccessExitBrowser, onErrorExitBrowser);
					window.BackButton.exitWebView();
				} else {
					CommonUtil.debug("back in the webview start");
					navigator.tnWebViewUtil.goBack();
					CommonUtil.debug("back in the webview over");
				}
			} catch (e) {
				CommonUtil.debug("exception during goBack:" + e);
				history.back();
			}
		},
		
		captureAddress : function(){
			function onsuc(pre){
				if(toString.call(pre)=="[object String]"){
					pre = JSON.parse(decodeURI(pre));
				}
				var stop = pre.stop;
				var address = {
					"lat" : 	stop.lat,
					"lon" :		stop.lon,
					"label":	stop.label,
					"firstLine":stop.firstLine,
					"city":		stop.city,
					"state":	stop.province,
					"zip":		stop.zip,
					"country":	stop.country
				};
				SDK_API_captureAddressCallBack(address);	
			}
			function onerr(){}
			try {	
				navigator.tnservice.captureAddress("",onsuc,onerr);
			} catch(e) {
				CommonUtil.debug("exception during captureAddress:" + e);
				SDK_API_captureAddressCallBack(getDummyAddressObj());
			}
		},
		
		invokePrivateService : function(name, value) {
			try {
				var a = arguments;
				function onsuc(pre) {
					var onSucCallBack;
					if (a.length > 2) {
						onSucCallBack = a[2];
						if (a.length > 3) {
							var extraObj = a[3];
							onSucCallBack(pre, extraObj);
							extraObj = null;
						} else {
							onSucCallBack(pre);
						}
					}
					onSucCallBack = null;
				}

				function onerr() {
					CommonUtil.debug("doPrivateService error");
				}

				navigator.tnprivateservice.doPrivateService(name, value, onsuc, onerr);
			} catch (e) {
				CommonUtil.debug("exception during invokePrivateService:" + e);
			}
		},
		invokePrivateServiceAsync : function(name,value){
			try{
				function onsuc(pre){}
				function onerr(){
					CommonUtil.debug("doPrivateServiceAsync error");
				}
				
				var onSucCallBack = onsuc;
				if(arguments.length >2)
				{
					onSucCallBack = arguments[2];
				}
				navigator.tnprivateservice.doPrivateServiceAsync(name,value,onSucCallBack,onerr);
			}
			catch(e){
				CommonUtil.debug("exception during invokePrivateServiceAsync:" + e);
			}
		},
		getPOIDetailMap : function(searchCriteria,callBack){
			try	{
				function onsuc(pre){			
					callBack(pre);
				}
				function onerr(){
					CommonUtil.debug("err during fetch  POI map by the snapshot from OpenGL map.");
					callBack("");
				}	
				navigator.tnservice.getPOIDetailMap(searchCriteria,onsuc,onerr);
			}
			catch(e){	
				CommonUtil.debug("exception during getPOIDetailMap:" + e);
				callBack("");
			}
		},
		
		getAddressAutocompleteData : function(addressType, addressFilter, callBackFunctionName) {
			try {
				var a = arguments;
				function onsuc(pre) {
					var onSucCallBack;
					if (a.length > 2) {
						onSucCallBack = a[2];
						if (a.length > 3) {
							var extraObj = a[3];
							onSucCallBack(pre, extraObj);
							extraObj = null;
						} else {
							onSucCallBack(pre);
						}
					}
					onSucCallBack = null;
				}
				function onerr(pre) {
					CommonUtil.debug("err during getAddressAutocompleteData.");
				}
				navigator.tnservice.getAddressAutocompleteData(addressType, addressFilter, onsuc, onerr);
			} catch (e) {
				CommonUtil.debug("exception during getAddressAutocompleteData:" + e);
			}
		},

		setValidationAddress : function(addressType, address) {
			try {
				function onsuc(pre) {
					callBackFunctionName(pre);
				}
				function onerr() {
					CommonUtil.debug("err during setValidationAddress.");
				}
				navigator.tnservice.setValidationAddress(addressType, address, onsuc, onerr);
			} catch (e) {
				CommonUtil.debug("exception during setValidationAddress:" + e);
			}
		}
		
};

var SDKAPI = new SDKAPI();

function CommonUtil() {}
CommonUtil.prototype = {
		ajax :function( options ) {
			options = {
				type: options.type || "GET",
				url: options.url || "",
				onComplete: options.onComplete || function(){},
				onError: options.onError || function(){},
				onSuccess: options.onSuccess || function(){},
				loadingStyle: options.loadingStyle || 0,	
				// 0:no loading;1:normall loading:2:poi loading
				data:	0 == options.data ? 0:1,					
				// 1:return JSON object;0:return string
				isAsynchronous : false == options.isAsynchronous ? false : true,
				timeout : options.timeout || -1
			};

			if(options.loadingStyle == 1){
				PopupUtil.showLoading();
			}
			
			var xml =  this.createAjaxHttpRequest();
			var startTime = new Date().getTime();
			
			/*
			 * if ajax response within timeout(for example, 5000ms), this timeout function will be cleared in 
			 * xml.onreadystatechange function. Otherwise, will execute code in setTimeout function.
			 */
			var isTimeout = false, timerId;
	        if(options.timeout>0){
	        	timerId = setTimeout(function(){
	        		xml.abort();
	                isTimeout = true;
	                PopupUtil.hide();
	                options.onError();
	            },options.timeout);
	        }
			
			
			xml.open(options.type, options.url, options.isAsynchronous);

			xml.onreadystatechange = function(){
				CommonUtil.debug("----ajax time----" + (new Date().getTime() - startTime));
				if ( xml.readyState == 4 && !isTimeout) {
					if(options.timeout>0) clearTimeout(timerId);
					if(options.loadingStyle != 0)
					{
						try
						{
						 PopupUtil.hide();
						}catch(ex){
							CommonUtil.debug("exception during xml.onreadystatechange PopupUtil.hide():" + e);
						}
					}
					
					if ( xml.status >= 200 && xml.status < 300) {
						var returnValue = xml.responseText;
						if(options.data)
						{
							returnValue = JSON.parse(returnValue);
						}
						options.onSuccess(returnValue, options.url);
					} else {
						options.onError();
					}
					options.onComplete();
					xml = null;
				}
			};
			
			xml.send();
		},
		
		noNetworkError : function()
		{
			CommonUtil.showAlert(I18NHelper["common.network.error.title"], I18NHelper["common.network.error.text"],I18NHelper["common.button.OK"]);
		},
		
		createAjaxHttpRequest : function()
		{
			var xmlhttp;
			if(window.ActiveXObject)
			{
				xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
			}
			else
			{
				xmlhttp=new XMLHttpRequest();
			}
			return xmlhttp;
		},
		
		getClientInfo : function()
		{
			return this.getFromCache(CacheKeys.CLIENTINFO);
		},

		fetchClientInfoFromUrl : function()
		{
			var ci = {
					"programCode" :	$("#programCode").val(),
					"deviceCarrier" :	$("#deviceCarrier").val(),
					"platform":		$("#platform").val(),
					"version":		$("#version").val(),
					"productType":	$("#productType").val(),
					"device":		$("#device").val(),
					"locale":		$("#locale").val(),
					"buildNumber":	$("#buildNumber").val()
			};
			var ciStr = "clientInfo=" + JSON.stringify(ci) + "&width=" + $("#width").val() + "&height=" + $("#height").val();
			this.saveInCache(CacheKeys.CLIENTINFO, ciStr);
		},

		getValidString : function (str)
		{
			if(!str)
			{
				str = "";
			}
			
			return str;
		},

		getBoolean : function(value)
		{
			var temp = false;
			if(value)
			{
				temp = value;
			}
			return temp;
		},

		emailAddressCheck : function(s) {
			var p  = /^([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
			return p.test(s);
		},
		
		getScreenWidth : function()
		{
			return window.innerWidth;
		},

		getScreenHeight : function()
		{
			return window.innerHeight;
		},

		getContentWidth : function()
		{
			return Math.max(document.documentElement.scrollWidth,this.getScreenWidth());
		},

		getContentHeight : function()
		{
			return Math.max(document.documentElement.scrollHeight,this.getScreenHeight());
		},

		isLandscape : function()
		{
			var w = this.getScreenWidth(),
				h = this.getScreenHeight();
			
			return ((h != 0 && w > h) ? true : false);
		},
		
		debug : function(msg)
		{
			console.log("[BROWSER CSERVER]--" + msg);
		},
		
		href : function(url, needPopup)
		{
			if(needPopup){
				PopupUtil.showLoading();
			}
			
			location.href = url;
			PopupUtil.hide();
		},
		
		toHtmlString : function(s)
		{
			s = s.replace(/[&]{1}/g, "&amp;");
			s = s.replace(/[<]{1}/g, "&lt;");
			s = s.replace(/[>]{1}/g, "&gt;");
			return s;
		},
		
		getCurrentLocation : function(callback)
		{
			navigator.geolocation.getCurrentPosition(callback,this.handle_gps_error,this.getGpsOptions());
		},
		
		getGpsOptions : function()
		{
			var opt = {
					maximumAge: 60000, 
					enableHighAccuracy: true,
					timeout: 3000
				  };
			return opt;
		},
		
		handle_gps_error : function(err){},
		
		isIphone : function() {
			var platform = CommonUtil.getValidString($("#platform").val());
			return ("IPHONE" == platform || "IPAD" == platform ? true : false);
		},
		
		processGpsLocation : function(position){
			if(toString.call(position)=="[object String]"){
				position = JSON.parse(position);
			}
			var lat = position.coords.latitude, 
				lon = position.coords.longitude,
				accuracy = CommonUtil.getValidString(position.coords.accuracy);
			
			if("" == accuracy && 34.05348 == lat && -118.24532 == lon)
			{
				if(this.isUK())
				{
					lat = 51.49934;
					lon = -0.13085;
				}
				else if(this.isBrazil())
				{
					lat = -15.86740;
					lon = -47.92957;			
				}
			}
			var address = {
				"lat" : 	lat * JSConstants.DEGREE_MULTIPLIER,
				"lon" :		lon * JSConstants.DEGREE_MULTIPLIER
			};	
			return address;
		},
		
		lunchPageContainer : function(target,title,exitWebView ){
			if(!target||target == ""){
				CommonUtil.debug("target url is null, return");
				return;
			}
			
			if(!title){
				title = "";
			}	
			
			var url = GLOBAL_hostUrl+ "goToJsp.do?jsp=pageContainer" + "&" + CommonUtil.getClientInfo() 
						+ "&target="+encodeURIComponent(target) + "&exitWebView=" + exitWebView + "&title="+title;
			
			if(exitWebView == true){
				SDKAPI.launchLocalApp(url);
			}else{
				location.href = url;
			}
		},
		
		addBackButtonForIphone : function(divId,backLable,exitWebView,callBack){
			backButtonBuilder = new BackButtonBuilder();
			backButtonBuilder.buildBackButton(divId,backLable,exitWebView,callBack);
		},
		
		addBarAndBackButtonForIphone : function(titleContainerId, backLable, titleLable, exitWebView){
			backButtonBuilder = new BackButtonBuilder();
			backButtonBuilder.buildBarAndBackButton(titleContainerId, backLable, titleLable, exitWebView);
		},
		
		clickAlertOk : function()
		{
			PopupUtil.hide();
		},
		
		isBrazil : function()
		{
			return "VIVONAVPROG" == $("#programCode").val();
		},
		
		isEmptyStr : function(data){
			if(!data || "" == data){
				return true;
			}
			else
				return false;
			
		},
		
		isNumberWith : function(length, originalString) {
			var reg = new RegExp("^\\d{" + length + "}$");
			return originalString.match(reg);
		},
		
		isUK : function()
		{
			return "TMOUKNAVPROG" == $("#programCode").val();
		},
		
		retry : function(callback)
		{
			window.setTimeout(callback,100);
		},
		
		showAlert : function(title, message, buttonContent, callBackFunction)
		{
			var titleDiv = "";
			var onClickEvent = "CommonUtil.clickAlertOk";
			if(arguments.length >3)
			{
				onClickEvent = arguments[3];
			}
			if(title!=null&&title!=""){
				var suffix = '';
				if(typeof isSpecialDevice != 'undefined' && isSpecialDevice){
					suffix = '_specialDevice';
				}
				titleDiv = '<div class="popTitleDiv' + suffix + '"><div class="div_table"><div class="div_cell clsPopupTitle">'+title+'</div></div></div>';
			}
			
			this.showAlertMsg(titleDiv, message, onClickEvent, buttonContent);
		},
		
		showAlertMsg : function(titleDiv, message, callBackFunction, buttonContent)
		{
			var suffix = '';
			if(typeof isSpecialDevice != 'undefined' && isSpecialDevice){
				suffix = '_specialDevice';
			}
			var divContent = '<div class="fdpopupdiv' + suffix + '">' + titleDiv +
			'<div class="popContentDiv' + suffix + '">' +
				'<div class="div_table">' + 
					'<div class="div_cell clsPopupContent' + suffix + '"' + 'id="alertMessage">'+ message + '</div>' + 
				'</div>' + 
			'</div></div>' + 
			'<div class="align_center popbottomdivstyle' + suffix + '">' + 
				'<div class="div_table">' + 
					'<div class="div_cell">' +
						'<button  id="button"  class="clsOKButton clsMiddleRadius clsButtonColorNormal clsButtonBgNormal"'+ 
			 			' ontouchstart="highlightBtnAll(this,\'clsButtonColorNormal\',\'clsButtonColorHighlight\')" ontouchend="disHighlightBtnAll(this,\'clsButtonColorHighlight\',\'clsButtonColorNormal\')" ontouchmove="disHighlightBtnAll(this,\'clsButtonColorHighlight\',\'clsButtonColorNormal\')"' +  
			 			' onClick="' + callBackFunction + '()"><span class="fs_large">'+buttonContent+'</span></button>'+
					'</div>' + 
				'</div>' + 			 
			'</div>';

			$("#alertPopup").html(divContent);
			PopupUtil.show("alertPopup","backgroundPopup");
		},
		
		throttle : function(method, context) {
			clearTimeout(method.tId);
			method.tId= setTimeout(function(){
			method.call(context);
			}, 100);
		},
		
		toJSONString : function(s){
			var pattern1 = new RegExp("\"", "ig");
			s = s.replace(pattern1, "\\\"");
			
			var pattern2 = new RegExp("\n", "ig");
			s = s.replace(pattern2, "\\n");
			return s;
		},
		
		isScoutStyle : function(){
			return this.getBoolean(GLOBAL_FeatureHelper["SCOUTSTYLE"]);
		},
		
		saveInCache : function(key, value){
			if(value){
				sessionStorage.setItem(key, value);
			}
		},
		
		getFromCache : function(key){
			return sessionStorage.getItem(key);
		},
		
		removeCache : function(key){
			return sessionStorage.removeItem(key);
		},
		
		saveInLocalCache : function(key, value){
			if(value){
				localStorage.setItem(key, value);
			}
		},
		
		getFromLocalCache : function(key){
			return localStorage.getItem(key);
		},
		
		removeLocalCache : function(key){
			return localStorage.removeItem(key);
		},
		convertChar : function ( str ) {
			var  c = {'<':'&lt;', '>':'&gt;', '&':'&amp;', '"':'&quot;', "'":'&#039;','#':'&#035;' };
			return str.replace( /[<&>'"#]/g, function(s) { return c[s]; } );
		}
};
var CommonUtil = new CommonUtil();

/**
 * MessageFormat
 * 
 * @param originalString
 * @returns {MessageFormat}
 */

function MessageFormat(originalString) {
	this._originalStr = originalString;
}

function MessageFormat_format(argumentArray) {
	var msgPattern=/\{\}/;
	var newString=this._originalStr;
	for (var i = 0; i < argumentArray.length; i++) {
		newString=newString.replace(msgPattern,argumentArray[i]);
	}
	return newString;
}

MessageFormat.prototype.format=MessageFormat_format;

function APPSTORE_API_lockScreen()
{
	PopupUtil.show("loadingPopup","");
}

function APPSTORE_API_unLockScreen()
{
	PopupUtil.hide();
}

function CSSHelper(){}
CSSHelper.prototype = {
	replaceObjCSSById : function(objId,oldCSSName,newCSSName){
		var element = document.getElementById(objId);
		this.replaceObjCSS(element, oldCSSName, newCSSName);
	},
	replaceObjCSS : function (element,oldCSSName,newCSSName){
		if(null != element){
			var currentClassName = element.className;
			eval("var patt=/\\b"+oldCSSName+"\\b/ig;");
			var changedClassName = currentClassName.replace(patt,newCSSName);
			element.className = changedClassName;
		}
	},
	appendObjCSSById : function (objId,newCSSName){
		var element = document.getElementById(objId);
		this.appendObjCSS(element, newCSSName);
	},
	appendObjCSS : function (element,newCSSName){
		element.className = element.className + " " + newCSSName;
	}
};
var cssHelper = new CSSHelper();
