function PoiCommon() {
}

PoiCommon.prototype = {
	saveFavToClient : function()
	{
		var poiDetailObj = getPoiDetailObj();
		if(this.hasValidPoiAddress(poiDetailObj))
		{
			SDKAPI.invokePrivateService("SaveToFavoriteDirectly",poiDetailObj,this.saveFavToClientCallBack);
		}
	},
	
	saveFavToClientCallBack : function()
	{
		$("#favIndicator").attr("class","favorites_add_button_unfocused");
		$("#favHref").attr("onClick","");
		saveFavFlag();
		var bgObj=document.getElementById("bgDiv");
	    bgObj.style.display = "block";
	    window.setTimeout(function(){
	    bgObj.style.display = "none";
	    },1250);
	},
	
	
	onClickDrive : function(ele)
	{
		ele.disabled = true;
		var poiDetailObj = getPoiDetailObj();
		function onsuc(){
			ele.disabled = false;
		}
		function onerr(){
			CommonUtil.debug("Failed to start navigation!");
			ele.disabled=false;
		}
		
		if(this.hasValidPoiAddress(poiDetailObj))
		{
			recordMisLog_DriveTo();
			navigator.tnservice.navTo("",poiDetailObj,"",onsuc,onerr);
		}
	},
	
	showMapOfClient : function() {
		function onsuc(res){
		}
		function onerr(){
			CommonUtil.debug(I18NHelper["Failed in mapping this place!"]);
		}
		var poiDetailObj = getPoiDetailObj();
		if(this.hasValidPoiAddress(poiDetailObj))
		{
			recordMisLog_ViewMap();
			navigator.tnservice.displayMap(poiDetailObj,"",onsuc,onerr);
		}
	},
	
	formatPhoneToDisplay : function(phone)
	{
		var phoneText = CommonUtil.getValidString(phone);
		phoneText = $.trim(phoneText);
		if("" == phoneText)
		{
			return phoneText;
		}
		var length = phoneText.length;
		
		if(length == 11)
		{
			phoneText = phoneText.substring(1);
		}
		
		if(length == 10)
		{
			var s1 = phoneText.substring(0,3);
			var s2 = phoneText.substring(3,6);
			var s3 = phoneText.substring(6);
			
			phoneText = "(" + s1 + ") " + s2 + "-" + s3;
		}

		
		return phoneText;
	},
	
	onClickPhoneNo : function()
	{
		var phoneNo = this.getPhoneNo();
		if(phoneNo)
		{
			recordMisLog_CallTo();
			location.href = "tel:" + phoneNo;
		}
	},
	
	hasValidPoiAddress : function(poiDetailObj)
	{
		return poiDetailObj && this.isPoiAddressValid(poiDetailObj.stop);
	},
	
	//poiDetailObj.stop
	isPoiAddressValid : function(stop)
	{
		return stop.lat && stop.lon;
	},
	
	changePoiDetailCss : function()
	{
		//jxl comment: replace xxLandscape css with media query 2012.02.29
//		var classAppend = "";
//		if(POI_API_isLandscape())
//		{
//			classAppend = "Landscape";
//		}
//		
//		document.getElementById("secondLineDiv").className = "clsDivSecond" + classAppend;
//		document.getElementById("topPartDiv").className = "clsMainDiv" + classAppend;
//		document.getElementById("middlePartDiv").className = "clsPageMiddleDiv" + classAppend;
//		document.getElementById("poimain").className = "clsMainBody" + classAppend;
		resizeMenuImage();
	},
	
	setPoiAddressDisplay : function(stop)
	{
		var text = "";
		if(this.isPoiAddressValid(stop)){
			
			if(stop.firstLine)
			{
				text +=  '<div style="width:100%;" class="clsAddressFS clsAddressLineSpan">'+stop.firstLine.toUpperCase() + "</div>";
			}
			
			text+= "<div class='clsCityFS'>";
			
			if(stop.city){
				text += stop.city.toUpperCase();
			}

			if(stop.province)
			{
				text += ", " + stop.province.toUpperCase();
			}
			
			text += " " + stop.zip + "</div>";
			$("#mainButtonsBar").css("display","block");
		}
		else
		{
			if(CommonUtil.isIphone()){
				text = I18NHelper["common.address.Unavailable"];
				$("#navImg").hide();
			}else{
				text = I18NHelper["common.Unavailable"];
			}
			
			this.disableButton("navButton");
		}		
		
		$("#address").html(text);
	},
	
	disableButton : function(elementId)
	{
		$("#" + elementId).attr("onClick","");
		$("#" + elementId).attr("ontouchstart","");
		$("#" + elementId).attr("ontouchend","");
		$("#" + elementId).attr("ontouchmove","");
		$("#" + elementId).css("opacity","0.6");
	},
	
	setPhoneNo : function()
	{
		if(CommonUtil.isScoutStyle()){
			var html = '<div class="td" style="width:20%;"></div>'
					  +'<div class="td clsPhonePicContainerLandscape" style="width:60%;vertical-align:middle;" align="center">'
					  +'	<img id="phonePicCallForScout" class="call_icon_unfocused" />'
					  +'</div>'
					  +'<div class="td" style="width:20%;" align="right">'
					  +'	<img id="phonePicLandscape" class="poi_details_call_icon_unfocused"/>'
					  +'</div>';
			$("#phoneDivLandscape").html(html);
		}
		
		var phoneNo = this.getPhoneNo();
		if(phoneNo){
			var formatedPhoneNo = PoiCommonHelper.formatPhoneToDisplay(phoneNo);
			$("#phone").html(formatedPhoneNo);
			if(CommonUtil.isIpad()&&ProgramConstants.ATTNAVPROG==ClientInfo.programCode){
				var newNode = "<span class='td align_center fs_small fw_bold'>"+formatedPhoneNo+"</span>";
				$("#phoneDivLandscape").html(newNode);
			}
		}else{
			if(CommonUtil.isScoutStyle()){
				$("#phone").html(I18NHelper["common.phone.Unavailable"]);
				$("#phonePic").hide();
				$("#phonePicLandscape").hide();
			}else{
				$("#phone").html(I18NHelper["common.Unavailable"]);
			}
			this.disableButton("phoneButton");
			this.disableButton("phoneButtonLandscape");
		}
	},
	
	getPhoneNo : function(){
		var poiDetailObj = getPoiDetailObj();
		if(poiDetailObj)
		{
			return CommonUtil.getValidString(poiDetailObj.poi.bizPoi.phoneNumber); 
		}
		else
		{
			return "";
		}
	},
	
	changePhoneNoLayout : function()
	{
		if(CommonUtil.isLandscape())
		{
			$("#thirdLineDivLandscape").show();
			$("#thirdLineDiv").hide();
		}
		else
		{
			$("#thirdLineDiv").show();
			$("#thirdLineDivLandscape").hide();
		}
	},
	
	highLightPhone : function(element){
		
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
		
	},
	
	dishighLightPhone : function(element){
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
	},
	
	highLightNavButton : function(element){
		var src = $("#navImg").attr("class").replace("poi_details_driveto_icon_unfocused","poi_details_driveto_icon_focused");
		$("#navImg").attr("class",src);
		
		switchHightlight(element,"clsNavButtonBgNormal","clsNavButtonBgHL");
		switchHightlight(element,"clsNavFontColor","clsNavFontColorHL");
	},
	
	dishighLightNavButton : function(element){
		var src = $("#navImg").attr("class").replace("poi_details_driveto_icon_focused","poi_details_driveto_icon_unfocused");
		$("#navImg").attr("class",src);
		
		switchHightlight(element,"clsNavButtonBgHL","clsNavButtonBgNormal");
		switchHightlight(element,"clsNavFontColorHL","clsNavFontColor");
	},
	
	generateTabs : function(configList)
	{
		hasTabNum = configList.length;// set to be global

		var tabWidth = 0;
		var tabNo = 0;
		var tabContent = "";
		for(var i=0;i<configList.length;i++){
			tabWidth = this.getTabWidth(configList[i][1],hasTabNum);
			if(i==0){
				tabContent+=this.createTab(tabWidth,configList[i][0],configList[i][1],"On",tabNo,hasTabNum);
			}else{
				tabContent+=this.createTab(tabWidth,configList[i][0],configList[i][1],"Off",tabNo,hasTabNum);
			}
			
			tabNo++;
		}
		tabContent += "<div class='td'>&nbsp;</div>";
		
		$("#tabShow").html(tabContent);
	},
	
	addTab : function(tabList,hasTab,tabName,tabId)
	{
		var tabLength = tabList.length;
		if(hasTab && tabLength <4)
		{
			tabList[tabLength] = new Array(tabName,tabId);
		}
	},
	
	createTab : function(tabWidth,value,poiDetailName,cssName,whichTab,tabNum){
		var tabLeftCssName = "td clsButtonLeft" + cssName;
		var tabMiddleCssName = "td clsButtonMiddle" + cssName;
		var tabRightCssName = "td clsButtonRignt" + cssName;
		
		var tabHtml = "";
		tabHtml += '<div class="td clsTabOutSideTable ' + tabWidth + '">';
			tabHtml += '<div style="width:100%;" class="table clsTabInSideTable clsFixTable">';
				tabHtml += '<div class="tr" onClick="PoiCommonHelper.onChangeTab(\'poidetailtab\',\'' + whichTab + "','" + tabNum + "','"+poiDetailName + '\')\">';
					tabHtml += '<div id="poidetaillefttab' + whichTab + '" height="100%" align="center" class="' + tabLeftCssName + '"> </div>';
					tabHtml += '<div id="poidetailmiddletab' + whichTab + '" height="100%" align="center" class="' + tabMiddleCssName + '"><span class="fs_middle4Tab fs_middle">' + value + "</span></div>";
					tabHtml += '<div id="poidetailrighttab' + whichTab + '" height="100%" align="center" class="' + tabRightCssName+ '"></div>';
				tabHtml += '</div>';
			tabHtml += '</div>';
		tabHtml += '</div>';
		return tabHtml;
	},
	
	getTabWidth : function(tabName,hasTabNum)
	{
		var tabWidth = "clsPoiTabNormalSmall";
		if(hasTabNum <=4)
		{
			tabWidth = "clsPoiTabNormal";
		}
		if("poireview" == tabName)
		{
			tabWidth  = "clsPoiTabReview";
		}
		else if("poigas" == tabName)
		{
			tabWidth  = "clsPoiTabGas";
		}
		else if("poitheater" == tabName)
		{
			tabWidth  = "clsPoiTabTheater";
		}
		
		if("poiextra" == tabName && hasTabNum >= 4)
		{
			tabWidth  = "clsPoiTabNormalSmall";
		}
		
		
		return tabWidth;
	},
	
	onChangeTab : function(obj,index,total,url)
	{
		PopupUtil.hide();
		for(var i=0;i<total;i++){
			if(i==index){
				document.getElementById("poidetaillefttab"+index).className = "td clsTabEdge clsTabLeftOnBk";
				document.getElementById("poidetailmiddletab"+index).className = "td clsButtonMiddleOn clsTabMiddleOnBk text_cutoff";
				document.getElementById("poidetailrighttab"+index).className = "td clsTabEdge clsTabRightOnBk";
			}else{
				if(document.getElementById("poidetaillefttab"+i)){
					document.getElementById("poidetaillefttab"+i).className = "td clsTabEdge clsTabLeftOffBk";
					document.getElementById("poidetailmiddletab"+i).className = "td clsButtonMiddleOff clsTabMiddleOffBk text_cutoff";
					document.getElementById("poidetailrighttab"+i).className = "td clsTabEdge clsTabRightOffBk";
				}
			}
		}
		
		hideAll();
		
		$("#" + url).show();
		
		var methodName = "show" + url;
		eval(methodName)();
	},
	
	showpoideals : function()
	{	
		var contentText = "";
		var data = CommonUtil.getValidString(PoiCacheHelper.getPoiDealCache());
		if("" != data)
		{
			//
			var resultObj = JSON.parse(data);
			if(resultObj.success)
			{
				var deals = JSON.parse(resultObj.deals);
				var dataSize = deals.length;
				for(var i=0;i<dataSize;i++)
				{
					contentText += this.getDealDiv(deals[i].name,deals[i].description,deals[i].dealImage);
				}
				
				if(dataSize ==0)
				{
					contentText += this.getDealDiv("",I18NHelper["poidetail.dataUnavailable"]);
				}
				else
				{
					recordMisLog_ViewCoupon();
				}
			}
		}
		
		$("#poideals").html(contentText);
	},
	
	displayPoiMenu : function(){
		var imgFmt = '<image id="{0}" src="{1}" />';
		var contentText = "";
		var data = CommonUtil.getValidString(PoiCacheHelper.getPoiMenuCache());
		if("" != data)
		{
			recordMisLog_ViewMenu();
			var resultObj = JSON.parse(data);
			var menu = resultObj.menu;
			if("" != menu)
			{
				contentText = menu;
				$("#poimenu").html(contentText);
			}
			else
			{
				var menuWidth = $("#menuImageDiv").width()-2*$("#clsPadding");
				var menuHeight = $("#menuImageDiv").width()-2*$("#clsPadding");//$("#menuImageDiv").height(); height is zero when use percentage
				$('#poimenu').html('');
				if("" != resultObj.menuImage)
				{
				 	$("#poimenu").html(imgFmt.format('menuImage',"data:image/png;base64,"+resultObj.menuImage)); 
				}
				else
				{
					$("#poimenu").html(I18NHelper["poidetail.menuUnavailable"]);
				}
			}
		}
	},
	
	getDealDiv : function(key,value,dealImage)
	{
		var contentText = "<div class='clsDealItemDiv'>"
			+"<div class='clsDealTitleDiv fs_large fw_bold fc_black'>"+key+"</div>"
			+"<div class='clsDealInfoDiv fs_small fc_gray'>"+value+"</div>";
		if(dealImage){
			contentText += "<div class='clsDealInfoDiv'><img id='dealImage' src='" + dealImage + "'/></div>";
		}
			
			contentText += "</div>";
		return contentText;
	},
	
	getAdSourceImageClass : function(adSource)
	{
		var className = "";
		if("" != adSource)
		{
			if(adSource == "TN" && $("#programCode").val() == "SCOUTPROG"){
				adSource = "Scout";
			}
			className = adSource + "_logo";
		}
		
		return className;
	},
	
	formatPoiDesc : function(str)
	{
		str = str.replace(/[&]{1}/g, "&amp;");
		return str;
	},
	
	getAdsId : function(poiDetailObj)
	{
		var adsId = "";
		if(poiDetailObj.poi){
			adsId = CommonUtil.getValidString(poiDetailObj.poi.adsId);
			var adSource = poiDetailObj.poi.ad;
			if(("" == adsId || "0" == adsId) && adSource){
				adsId = CommonUtil.getValidString(adSource.adID);
			}
		}
		return adsId;//"1122001";
	},
	
	fetchPoiMainFromServer : function()
	{
		loadPopup_poidetail();
		var searchCriteria = PoiDetailSpecificHelper.getSearchCriteriaForAds();
		var ajxUrl = GLOBAL_hostUrl + "getPoiDetailData.do?operateType=mainnew&jsonStr=" + JSON.stringify(searchCriteria) + "&" + CommonUtil.getClientInfo();
		var ajaxOptions = {
				loadingStyle:2,
				url:ajxUrl,
				onSuccess:this.ajaxCallBackPoiMainFromServer
		};
		CommonUtil.ajax(ajaxOptions);
	},
	ajaxCallBackPoiMainFromServer : function(resultObj)
	{
		PoiDetailSpecificHelper.handlePoiMainFromServer(resultObj);
	}
};
var PoiCommonHelper = new PoiCommon();
