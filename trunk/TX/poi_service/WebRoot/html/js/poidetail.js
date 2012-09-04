	function initPhoneGap()
	{
		document.addEventListener("deviceready", onDeviceReady, false);
	}
	
	function onDeviceReady() {
		var reviewStatus = CommonUtil.getValidString(PoiCacheHelper.getReviewStatus());
		if("" != reviewStatus){
			//clearViewParamter();
			initPoiDetailData();
		}
		else
		{
			fetchPoiKey();
			getPoiDataFromClient();
			clearPoiDetailHistory();
		}
    }
	
	function fetchPoiKey()
	{
		try
		{
			var tempCache = window.TempCache;
			if(tempCache != null)
			{
				var poikey = CommonUtil.getValidString(tempCache.getCacheValue('poikey'));
				if("" != poikey)
				{
					document.getElementById("poikey").value = poikey;
				}
			}
		}catch(e)
		{
		}
		
	}
	
	function clearPoiDetailHistory()
	{
		var data = {
						"poikey":$("#poikey").val()
					};
		SDKAPI.invokePrivateService("clearHistory",data);
	}
	
	function getPoiDataFromClient()
	{
		var data = {
				"poikey":$("#poikey").val()
			   };
		SDKAPI.invokePrivateService("RetrieveCurrentAddress",data,preparePoiDetailData);
	}
	
	function initDummyData()
	{
		var reviewStatus = CommonUtil.getValidString(PoiCacheHelper.getReviewStatus());
		if("" != reviewStatus){
			//clearViewParamter();
			initPoiDetailData();
		}
		else
		{
			preparePoiDetailData(PoiCacheHelper.getDummyPoiDetail());
		}
	}
	
	function getPoiDetailObj()
	{
		return JSON.parse(CommonUtil.getFromCache(appendPoiKey(PoiCacheKeys.POIDETAIL)));
	}
	
	function shareAddress()
	{
		var poiDetailObj = getPoiDetailObj();
		if(PoiCommonHelper.hasValidPoiAddress(poiDetailObj))
		{
			SDKAPI.invokePrivateService("ShareAddress",poiDetailObj);
		}
	}
	
	function searchNearBy()
	{
		var poiDetailObj = getPoiDetailObj();
		if(PoiCommonHelper.hasValidPoiAddress(poiDetailObj))
		{
			SDKAPI.invokePrivateService("SearchNearBy",poiDetailObj);
		}
	}
	
	function appendPoiKey (key)
	{
		return key + "_" + $("#poikey").val();
	}
	
	function preparePoiDetailData(poiDetailData)
	{
		CommonUtil.debug("poi data:" + poiDetailData);
		recordMisLog_ViewDetail();
		//PoiCacheHelper.setPoiDetailCache(unescape(poiDetailData));
		CommonUtil.saveInCache(appendPoiKey(PoiCacheKeys.POIDETAIL), unescape(poiDetailData));
		initPoiDetailData();
	}

	function initPoiDetailData()
	{
		hideAll();
		var poiDetailObj = getPoiDetailObj();
		initTopPart(poiDetailObj);
		PoiDetailSpecificHelper.initTab(poiDetailObj);
	}
	
	function clearPoiCache()
	{
		//PoiCacheHelper.removePoiReviewCache();
		CommonUtil.removeCache(appendPoiKey(PoiCacheKeys.REVIEWDATA));
		PoiCacheHelper.removePoiMainCache();
		PoiCacheHelper.removePoiExtraCache();
		PoiCacheHelper.removePoiMenuCache();
		PoiCacheHelper.removePoiDealCache();
		PoiCacheHelper.removeMapFlagCache();
		PoiCacheHelper.removeBizHourFlagCache();
		PoiCacheHelper.removeGasByPriceCache();
	}

	function initTopPart(poiDetailObj)
	{
		var poiObj = poiDetailObj.poi;
		var bizPoi = poiObj.bizPoi;
		//
		$("#name").html(CommonUtil.toHtmlString(bizPoi.brand));
		$("#distance").html(formatDisUnit(bizPoi.distanceWithUnit));
		if(PoiCommonHelper.hasValidPoiAddress(poiDetailObj))
		{
			if(poiDetailObj.existedInFavorite)
			{
				$("#favIndicator").attr("class","favorites_add_button_unfocused");
				$("#favHref").attr("onClick","");
			}
			else
			{
				$("#favIndicator").attr("class","favorites_button_unfocused");
				$("#favHref").attr("onClick","PoiCommonHelper.saveFavToClient()");
			}
		}
		else
		{
			$("#favIndicator").hide();
		}

		PoiCommonHelper.setPoiAddressDisplay(poiDetailObj.stop);
		PoiCommonHelper.setPhoneNo();
		displayReviewNumber(poiObj.rateNumber);
		diplayRatingIcon("ratingIcons",poiObj.rating,"small");
		diplayRatingIcon("ratingIconsLandscape",poiObj.rating,"small");
		
		PoiDetailSpecificHelper.getTopPartLogoImage(poiObj);
	}
	
	function formatDisUnit(rawDisUnit)
	{
		if(rawDisUnit)
		{
			var disUnitA = rawDisUnit.split(" ");
			if(disUnitA.length>=2) 
			{
				var numP = CommonUtil.getValidString(disUnitA.shift());
				if(numP.length<=4 || numP.indexOf(".") < 0) return rawDisUnit;
				// if the number is greater than 999, ignore it.
				var idx = numP.indexOf(".");
				numP = idx >= 3 ? numP.substring(0,idx):numP.substring(0,4);
				return numP + " " + disUnitA.shift();
			} else return rawDisUnit;
		} else return rawDisUnit; 
		
	}
	
	function displayReviewNumber(rateNumber)
	{
		var text = "<div class='clsRateNoFS'><b>" + rateNumber + "</b><br><div class='reviewText'>";
		if(rateNumber >1){
			text += I18NHelper["poidetail.reviews"];
		}else{
			text += I18NHelper["poidetail.review"];
		}
		text += "</div></div>";
		$("#ratingNumber").html(text);
		$("#ratingNumberLandscape").html(text);
	}
	
	function displayEmptyLogo()
	{
		var emptyLogoHtml = "<img class='clsLogoImageDiv' src='" + GLOBAL_imageCommonUrl + "poi_details_default_logo_unfocused.png'/>";
		$('#logImageDiv').html(emptyLogoHtml);
	}
	
	
	function displayLogoImage(image)
	{
		var imgFmt = '<image class="clsLogoImageDiv" id="{0}" src="{1}" />';
		$('#logImageDiv').html(imgFmt.format('logImage',"data:image/gif;base64,"+image));
	}
	
	function displayPriceRangeIcons(price)
	{
		var priceRange = parseInt(price);
		if(priceRange >0)
		{
			$("#priceRange").show();
		}
		document.getElementById("priceRange1").className = getPriceClass(priceRange);
		document.getElementById("priceRange2").className = getPriceClass(priceRange-1);
		document.getElementById("priceRange3").className = getPriceClass(priceRange-2);
		document.getElementById("priceRange4").className = getPriceClass(priceRange-3);
	}
	
	function getPriceClass(priceRange)
	{
		var cssName = "clsDollarGrey fs_middle";
		if(priceRange >0)
		{
			cssName = "clsDollarOrange fs_middle";
		}
		return cssName;
	}
	
	function showpoiopentablemain()
	{
		OpenTable.showOpenTableMain();
	}
	
	function showpoireview()
	{
		//fetch review data
		var reviewData = CommonUtil.getValidString(CommonUtil.getFromCache(appendPoiKey(PoiCacheKeys.REVIEWDATA)));
		if("" == reviewData)
		{
			PoiFetchDataHelper.fetchReviewData();
		}
		else 
		{
			displayReview();
		}		
	}
	
	function reviewNum2ReviewImg(value){
		if(value>0){
			return "<img class='evaluation good_icon_unfocused'  />";
    	}else{
    		return "<img class='evaluation bad_icon_unfocused'  />";
   	 	}
	}
	
	function displayReview()
	{
	    recordMisLog_ViewReview();
	    
		var reviewList =  JSON.parse(CommonUtil.getFromCache(appendPoiKey(PoiCacheKeys.REVIEWDATA)));
		PoiCacheHelper.setPoiReviewOptionCache(reviewList.reviewOptions);
		diplayRatingIcon("reviewRatingIcon",reviewList.rating,"big");
		
		var rateLabel = I18NHelper["poidetail.review"];
		if(reviewList.rateNumber >1)
		{
			rateLabel = I18NHelper["poidetail.reviews"];
		}
		var rateText = "(" + reviewList.rateNumber + " " + rateLabel + ")";
		$("#reviewRatingNum").html(rateText);
		//sync the rating on the top part 
		displayReviewNumber(reviewList.rateNumber);
		diplayRatingIcon("ratingIcons",reviewList.rating,"small");
		diplayRatingIcon("ratingIconsLandscape",reviewList.rating,"small");
		
		var reviewStatisticText = "<div class='div_table'><div class='div_row'>";
		var ratingPropertiesStatistic = reviewList.ratingPropertiesStatistic;
		var length = ratingPropertiesStatistic.length;
		var tdWidth = '100%';
		if(length != 0)
		{
			tdWidth = (100/length) + "%";
		}
		for(var i =0; i<length; i++){
			var everyReviewOptionStatistic = ratingPropertiesStatistic[i];
			var name = everyReviewOptionStatistic.name;
			var value = everyReviewOptionStatistic.value;
			var count = everyReviewOptionStatistic.count;
				

			reviewStatisticText += "<div class='div_cell' style='width:"+tdWidth+"'>" 
								+ "<div class='div_table clsFixTable'>" 
								+ "<div class='div_row'> <div class='div_cell fs_small fc_gray fw_bold align_center'>" 
								+ name 
								+ "</div></div>" 
								+ "<div class='div_row'> <div class='div_cell align_center' id='valueImage'>";
			reviewStatisticText += reviewNum2ReviewImg(value);
			reviewStatisticText += "</div></div>";
			reviewStatisticText += "<div class='div_row'> <div class='div_cel fs_small fc_gray align_center'>" + count + " " + I18NHelper["review.user"] + "</div></div>";
			reviewStatisticText += "</div>" + "</div>";
			if (i != length-1 && !CommonUtil.isScoutStyle()) {
				reviewStatisticText += "<div class='div_cell bgImgH'>&nbsp;</div>";
			}
		}
		reviewStatisticText += "</div></div>";
		$("#reviewOptionStatistic").html(reviewStatisticText);
		if(length > 0)
		{
			$("#reviewOptionStatistic").show();
		}
		else
		{
			$("#reviewOptionStatistic").hide();
		}
		
        //Yelp reviews
        var yelp = reviewList.yelp;
        if (!!yelp)
        {
            recordMisLog_Provider_Impression("Yelp", "yelp_button");
            
            $("#yelp").show();
            $("#yelpReviewCount").html("(" + yelp.reviewCount + " " + rateLabel + ")");
            displayYelpRating(yelp.avgRating);
            var yelpBtn = $(".yelp_button");
            yelpBtn.attr("yelpPoiId", yelp.yelpPoiId);
            yelpBtn.click(onClickYelp);
        }
        
        //trip Advisor reviews
        var trip = reviewList.tripAdvisor;
        console.log(trip);
        if (!!trip)
        {
            recordMisLog_Provider_Impression("TripAdvisor", "TripAdvisor_button");
            
            $("#trip").show();
            $("#tripReviewCount").html("(" + trip.reviewCount + " " + rateLabel + ")");
            displayTripCircle(trip.avgRating);
            var tripBtn = $(".trip_button");
            tripBtn.attr("tripId", trip.id);
            tripBtn.click(onClickTrip);
        }
        
        
		
		var reviweUserList = reviewList.reviewList;
		var reviewUserSize = reviweUserList.length;
		var reviewColumnText = "";
		var tempReview,rName,comments;
		for(var i=0;i<reviewUserSize;i++){
			rName = CommonUtil.convertChar(reviweUserList[i].reviewerName);
			if(!rName){
				rName = I18NHelper["review.anonymous"];
			}
			comments = CommonUtil.convertChar(reviweUserList[i].comments);
			if(!comments){
				comments = I18NHelper["review.no.Comment"];
			}
			reviewColumnText += "<div id='reviewItem"+i+"' class='clsReviewerDiv clsReviewerDivBg' ontouchstart=\"highLightReviewItem("+i+")\" ontouchend=\"disHighLightReviewItem("+i+")\" ontouchmove=\"disHighLightReviewItem("+i+")\" onClick='showReviewerDetail("+i+")'>"
				       +"<div class='table' style='height:100%;width:95%;'>" 
				       +"<div class='tr'>"
		               +"<div class='td' style='width:17%;height:100%'><img class='clsImgDefaultPhoto' src='"+GLOBAL_imageCommonUrl+"default_photo_icon_unfocused.png'/></div>"
			           +"<div class='td' style='width:83%;height:100%'>"
			           +"<div class='table' style='height:100%;width:100%;'>"
			           +"<div class='tr'>"
				       +"<div class='td valign_top' style='width:60%;height:50%' align='left'><div style='position:relative;height:100%;' class='clsReviewWrapper'><div class='clsNoWrap fs_middle fc_gray fw_bold' id='reviewUserName"+i+"'>"+rName+"</div></div></div>"
				       +"<div class='td clsReviewWrapper' style='width:40%' align='right' id='reviewUserRating"+i+"'>"+getRatingIcons(reviweUserList[i].rating,"medium")+"</div>" 
			           +"</div>"
			           +"<div class='tr'>"
				       +"<div class='td valign_top'>"
					   +"<div class='clsReviewBottomContentDiv'>"
					   +"<div class='clsNoWrap fs_middle fc_gray' align='left' id='reviewUserComments"+i+"'>"+comments+"</div>"
					   +"</div>"
					   +"</div>"
				       +"</div>"
			           +"</div>"
			           +"</div>"
			           +"</div>"
			           +"</div>"
			           +"</div>";
		}
		$("#reviewerListDiv").html(reviewColumnText);
	}
	
    function displayYelpRating(rating) {
        var arr = [["yelp_no_star", 0, 4], ["yelp_one_star", 5, 10], 
                   ["yelp_one_half_star", 11, 15], ["yelp_two_star", 16, 20],
                   ["yelp_two_half_star", 21, 25], ["yelp_three_star", 26, 30],
                   ["yelp_three_half_star", 31, 35], ["yelp_four_star", 36, 40],
                   ["yelp_four_half_star", 41, 45], ["yelp_five_star", 46, 1000]];
        
        for (var i = 0; i < arr.length; i ++)
        {
            var item = arr[i];
            if (rating >= item[1] && (rating <= item[2]))
            {
                $("." + item[0]).show();
            }   
            else
            {
                $("." + item[0]).hide();
            }   
        }   
    }
    
    function displayTripCircle(rating) {
        var arr = [["trip_no_circle", 0, 2], ["trip_half_circle", 2, 4],  ["trip_one_circle", 5, 10], 
                   ["trip_one_half_circle", 11, 15], ["trip_two_circle", 16, 20],
                   ["trip_two_half_circle", 21, 25], ["trip_three_circle", 26, 30],
                   ["trip_three_half_circle", 31, 35], ["trip_four_circle", 36, 40],
                   ["trip_four_half_circle", 41, 45], ["trip_five_circle", 46, 1000]];
        
        for (var i = 0; i < arr.length; i ++)
        {
            var item = arr[i];
            if (rating >= item[1] && (rating <= item[2]))
            {
                $("." + item[0]).show();
            }   
            else
            {
                $("." + item[0]).hide();
            }   
        }   
    }
    
    function onClickYelp() {
        var yelpId = $(".yelp_button").attr("yelpPoiId");
        var platform = ClientInfo.platform;
        if("IPHONE" == platform || "IPAD" == platform) //ios
        {
            var tnservice = navigator.tnservice;
            if (!tnservice) return;
            
            recordMisLog_Provider_Click("Yelp", "yelp_button");
            
            tnservice.launchNativeBrowser("yelp:///biz/" + yelpId, function() {
            }, function() {
                tnservice.launchNativeBrowser("http://www.yelp.com/biz/" + yelpId);  
            });
        }
        else //android
        {   
            recordMisLog_Provider_Click("Yelp", "yelp_button");
            
//          navigator.tnservice.launchNativeBrowser("http://www.yelp.com/biz/" + yelpId);
//          navigator.tnservice.launchLocalApp("http://www.yelp.com/biz/" + yelpId + "?nativebrowser=true");
            window.location = "http://www.yelp.com/biz/" + yelpId + "?nativebrowser=true";
        }
    }
    
    function onClickTrip() {
        var tripId = $(".trip_button").attr("tripId");
        var platform = ClientInfo.platform;
        if("IPHONE" == platform || "IPAD" == platform) //ios
        {
            var tnservice = navigator.tnservice;
            if (!tnservice) return;
            
            recordMisLog_Provider_Click("TripAdvisor", "TripAdvisor_button");
            
            tnservice.launchNativeBrowser("yelp:///biz/" + tripId, function() {
            }, function() {
                tnservice.launchNativeBrowser("http://www.yelp.com/biz/" + yelpId);  
            });
        }
        else //android
        {   
            recordMisLog_Provider_Click("TripAdvisor", "TripAdvisor_button");
            
//          navigator.tnservice.launchNativeBrowser("http://www.yelp.com/biz/" + yelpId);
//          navigator.tnservice.launchLocalApp("http://www.yelp.com/biz/" + yelpId + "?nativebrowser=true");
            window.location = "http://www.163.com/biz/" + tripId + "?nativebrowser=true";
        }
    }
    	
	function showReviewerDetail(index){
		PoiCacheHelper.setReviewStatusAsView();
		var reviewList =  JSON.parse(CommonUtil.getFromCache(appendPoiKey(PoiCacheKeys.REVIEWDATA)));
		var reviwerList = reviewList.reviewList;
		var currentReviewer = reviwerList[index];
		PoiCacheHelper.setCurrentReviewer(JSON.stringify(currentReviewer));
		
		location.href = GLOBAL_hostUrl + "goToJsp.do?jsp=viewReview" + "&" + CommonUtil.getClientInfoNormal();
	}
	


	function loadPopup_poidetail()
	{
		var screenHeight = CommonUtil.getScreenHeight();
		if(screenHeight == 0)
		{
			CommonUtil.retry(loadPopup_poidetail);
		}
		else
		{
			var top = $("#topPartDiv").height() + $("#middlePartDiv").height();
			var height = screenHeight - top;
			//loadPopupForBk("loadingPopup", "backgroundPopup",top,height);
			PopupUtil.showPoiLoading(top,height);
		}
	}
	
	function centerPopup_poidetail()
	{
		PopupUtil.center();
	}

	function getPoiAddress()
	{
		return getPoiDetailObj().stop;
	}

	function generateSimplePoiData()
	{
		var poiDetailObj = getPoiDetailObj();
		var bizPoi = poiDetailObj.poi.bizPoi;
		var simplePoiData = {
				"poiId":bizPoi.poiId,
				"brand":bizPoi.brand,
				"categoryId":bizPoi.categoryId,
				"stop":poiDetailObj.stop
				};
		//PoiCacheHelper.setSimplePoiDetailCache(JSON.stringify(simplePoiData));
		CommonUtil.saveInCache(PoiCacheKeys.SIMPLEPOI, JSON.stringify(simplePoiData));
	}
	
	function onClickWriteReview()
	{
		PoiCacheHelper.setReviewStatusAsView();
		//pass simple poi data to review
		generateSimplePoiData();
		PoiCacheHelper.setPoiReviewCacheKeyForAddReview();
		location.href = GLOBAL_hostUrl + "goToJsp.do?jsp=addReview&"+ CommonUtil.getClientInfoNormal();
	}
	
	function resizeMenuImage(){
		if($("#menuImage")){
			$("#menuImage").width(document.documentElement.clientWidth*94/100);
			$("#menuImage").height(document.documentElement.clientWidth*96/100);
		}
	}
	
	function setShownTab(backFromViewOrAddReview){
		if(backFromViewOrAddReview){
			PoiCommonHelper.onChangeTab('poidetailtab',1,hasTabNum,'poireview');
		}else{
			PoiCommonHelper.onChangeTab('poidetailtab',0,hasTabNum,'poimain');
		}
	}
	
	function isBackFromViewOrAddReview(){
		var reviewStatus = CommonUtil.getValidString(PoiCacheHelper.getReviewStatus());
		return  "VIEW" == reviewStatus;
	}
	
	function initPoiCacheByReviewStatus(backFromViewOrAddReview){
		if(backFromViewOrAddReview){
			PoiCacheHelper.setReviewStatus("");
		}else{
			clearPoiCache();//first time entering page
		}
	}
	
	function loadResourceAndSetShownTab(template,backFromViewOrAddReview){
		if(backFromViewOrAddReview){
			lazyLoadJsAndCss(template, backFromViewOrAddReview);
			PoiCommonHelper.onChangeTab('poidetailtab',1,hasTabNum,'poireview');
		}else{
			lazyLoadJsAndCss(template, backFromViewOrAddReview);
		}
	}
	
	function hideAll(){
		
		$("#poimain").hide();
		$("#poireview").hide();
		$("#poideals").hide();
		$("#poimenu").hide();
		$("#poitheater").hide();
		$("#poiextra").hide();
		$("#poigas").hide();
		//$("#poihotelreserve").hide();
		//$("#poihotelmain").hide();
		//$("#poiopentablemain").hide();
		//$("#poiopentablereserve").hide();
	}
	
	function formateMenuText(menuText)
	{
		var text = menuText;
		//replace <bold>xxx</bold> to <b>xxx</b><br>
		text = text.replace(/<bold>/ig, "<br><b>");
		text = text.replace(/<\/bold>/ig, "</b><br>");
		return text;
	}
	
    function showpoimain()
    {
    	$("#poidesc").show();
    	$("#priceRange").hide();
		$("#mapImageContainer").hide();
       var poiDetailObj = getPoiDetailObj();
       if(!poiDetailObj)
       {
    	   return false;
       }
       var data = CommonUtil.getValidString(PoiCacheHelper.getPoiMainCache());
       if(!data)
       {
    	   PoiDetailSpecificHelper.loadPoiMain(poiDetailObj); 
       }
       else
       {
           displayPoiMain();
           var resultObj = JSON.parse(data);
           PoiDetailSpecificHelper.fetchLogoWhenShowMain(resultObj.logoName);
       }
       CommonUtil.debug("SCOUT Feature: "+GLOBAL_FeatureHelper["SCOUTSTYLE"]);
       CommonUtil.debug("OPENGL Feature: "+FeatureHelper["OPENGLMAP"]);
    }
	
	function displayPoiMain()
	{
		var data = CommonUtil.getValidString(PoiCacheHelper.getPoiMainCache());
		var imgFmt = '<image class="{1}" id="{0}"/>';
		if(data)
		{
			//
			var resultObj = JSON.parse(data);
			var desc = CommonUtil.getValidString(resultObj.description);
			//desc = PoiCommonHelper.formatPoiDesc(desc);
			$("#poidesc").html("<div class='poiContentPadding'>"+desc+"</div>");
			var businessHour = resultObj.businessHours;
			if(businessHour)
			{
				PoiCacheHelper.setBizHourFlag("Y");
				$("#businessHour").html(businessHour);
				//if bizHour exists, change the first line background to gray
				//(for scout only, android prog.css doesn't have "clsFirstLineBG") add by jxl 2011.12.22
				cssHelper.appendObjCSSById("mainTabFirstLine","clsFirstLineBG");
			}
			displayPriceRangeIcons(resultObj.priceRange);
			if(resultObj.isOpen)
			{
				$("#openFlag").html(I18NHelper["poidetail.open"]);
				$("#openFlag").attr("class","td clsOpenFlag fs_large fw_bold");
			}
			else
			{
				if(businessHour)
				{
					$("#openFlag").html(I18NHelper["poidetail.close"]);
					$("#openFlag").attr("class","td clsOpenFlagGrayStyle fs_large fw_bold");
				}
			}
			$("#fullBusinessHours").html("<div class='poiContentPadding'>"+resultObj.fullBusinessHours+"</div>");
			
			var adSource = resultObj.adSource;
			if(adSource)
			{
				PoiCacheHelper.setBizHourFlag("Y");
				var adSourceIconClass = PoiCommonHelper.getAdSourceImageClass(adSource);
				$("#adSource").html(imgFmt.format('adSourceImage',adSourceIconClass)); 
			}else{
				$("#adSource").hide();
			}
			
   		  	if(!desc){
   		  		Global_openGLParam["needMap"] = true;
   		  		displayMapImage();
   		  	}else{
   		  		Global_openGLParam["needMap"] = false;
   		  		recordMisLog_ViewMerchantContent();
   		  	}
			if(PoiCacheHelper.getBizHourFlag() != "Y"){
				cssHelper.replaceObjCSSById("mainTabFirstLine","clsMainTabFirstLine","clsMainTabFirstLineEmpty");
   		  	}
		}
	}
	
	function switchBizHour()
	{
		var openFullBizHour = document.getElementById("fullBusinessHours").style.display == "none";
		if(openFullBizHour)
		{
			var data = CommonUtil.getValidString(PoiCacheHelper.getPoiMainCache());
			if(data)
			{
				//
				var resultObj = JSON.parse(data);
				var  fullBusinessHours = CommonUtil.getValidString(resultObj.fullBusinessHours);
				if(fullBusinessHours)
				{
					$("#poidetailDesc").hide();
					$("#fullBusinessHours").show();
				}
			}
		}
		else
		{
			$("#poidetailDesc").show();
			$("#fullBusinessHours").hide();
		}
	}
	

	
	function showpoiextra()
	{
		if(PoiCacheHelper.getPoiExtraCache())
		{
			displayPoiExtra();
		}
		else
		{
			PoiFetchDataHelper.fetchPoiExtra();
		}
	}
	
	function poihotelmain(){
		poiId = getPoiDetailObj().poi.bizPoi.poiId;
		
		var cache = "";//CommonUtil.getValidString(getHotelDetailCache(poiId));
		if(cache == "" ){
			fetchAndDispalyHotelDetail(poiId);
		}else{
			displayHotelDetail(cache);
		}
		
	}
	
	
	function displayPoiExtra()
	{
		var contentText = "";
		var data = CommonUtil.getValidString(PoiCacheHelper.getPoiExtraCache());
		if(data)
		{
			//
			var resultObj = JSON.parse(data);
			var extras = JSON.parse(resultObj.extra);
			var dataSize = extras.length;
			var item;
			for(var i=0;i<dataSize;i++)
			{
				contentText += getExtraDiv(extras[i].key,extras[i].value);
			}
			
			if(dataSize ==0)
			{
				noRecordFoundForExtra();
			}
			else
			{
				$("#poiextra").html(contentText);
			}
		}
	}
	
	function getExtraDiv(key,value)
	{
		var contentText = "<div class='clsExtraItemDiv'>"
			+"<div class='clsDealTitleDiv fs_large fw_bold fc_black'>"+key+"</div>"
			+"<div class='clsDealInfoDiv fs_small fc_gray'>"+value+"</div>";
			contentText += "</div>";
		return contentText;
	}
	
	function noRecordFoundForExtra()
	{
		var contentText = getExtraDiv("",I18NHelper["poidetail.dataUnavailable"]);
		$("#poiextra").html(contentText);
	}
	
	function showpoideals()
	{	
		PoiCommonHelper.showpoideals();
	}
	
	
	function showpoimenu(){
		if(PoiCacheHelper.getPoiMenuCache())
		{
			PoiCommonHelper.displayPoiMenu();
		}
		else
		{
			PoiFetchDataHelper.fetchMenuData();
		}
	}
	
	function getPoiSearchKey()
	{
		var poiDetailObj = getPoiDetailObj();
		var bizPoi = poiDetailObj.poi.bizPoi;
		var searchCriteria = {
				"poiId":		bizPoi.poiId,
				"categoryId":	bizPoi.categoryId,
				"adsId":	CommonUtil.getValidString(poiDetailObj.poi.adsId),
				"width":0,
				"height":0,
				"menuWidth":0,
				"menuHeight":0
				};
		return searchCriteria;
	}
	
	function displayGasByPrice()
	{
		var contentText = "";
		var data = CommonUtil.getValidString(PoiCacheHelper.getGasByPriceCache());
		var resultObj = JSON.parse(data);
		
		if(resultObj.success)
		{
			var gasList = JSON.parse(resultObj.data);
			var gasName = "";
			var fontColor_blue = "";
			for(var i=0;i<gasList.length;i++)
			{
				gasName = gasList[i].name;
				fontColor_blue = "fc_gasName";
				/* do not change font-color. Modified by xljiang 2012.05.03
				if("[D]" == gasName){
					fontColor_blue = "fc_gasNameDeep";
				}
				*/
				
				if(null != I18NHelper[gasName]){
					gasName = I18NHelper[gasName];
				}
				
				contentText+="<div class='gasItem'><div class='div_table'><div class='tr'>"
							+"<div class='td' style='width:10%'></div>"
							+"<div class='td' style='width:40%' align='left'><div class='clsGasPriceDiv fs_veryLarge "+fontColor_blue+" fw_bold'>"+gasName+"</div></div>"
							+"<div class='td' style='width:40%' align='right'><div class='clsGasPriceDiv fs_veryLarge fc_gasPrice fw_bold'>"+gasList[i].price+"</div></div>"							
							+"<div class='td' style='width:10%'></div>"
							+"</div></div></div>";
				if(i<gasList.length-1)
				    contentText +="<div class='clsGreyLine'></div>";
			}
			$("#poigas").html(contentText);
		}
	}
	
	
	function showpoigas()
	{
		if(PoiCacheHelper.getGasByPriceCache())
		{
			displayGasByPrice();
		}
		else
		{
			PoiDetailSpecificHelper.fetchGasByPriceData();
		}
	}
	
	function getActualSize(oraginalSize){
		if(CommonUtil.isIphone()&& window.devicePixelRatio == 2){
			return oraginalSize*2;
		}else{
			return oraginalSize;
		}
	}
	
	function showpoitheater(){
		initMovieShowTimes();
		fetchMovieListData();
	}
	
	function highLightReviewItem(id){
		var elementId = "reviewItem" + id;
		var element = document.getElementById(elementId) ;
		switchHightlight(element,"clsReviewerDivBg","clsListBgHighlight");
		$("#reviewUserName"+id).css("color","white");
		$("#reviewUserComments"+id).css("color","white");
	}
	
	function disHighLightReviewItem(id){
		var elementId = "reviewItem" + id;
		var element = document.getElementById(elementId) ;
		
		if(element)
		{
			switchHightlight(element,"clsListBgHighlight","clsReviewerDivBg");
			$("#reviewUserName"+id).css("color","#666");
			$("#reviewUserComments"+id).css("color","#666");
		}
	}
	
	function clearViewParamter()
	{
		//document.getElementById("view").value = "";
	}
	
	function applyCss()
	{
		var cssObj = {"position":"absolute","bottom":"0px","top":"0px","-webkit-border-radius":" 0px 0px 5px 5px"};
		/* remove parameter -- view
		var view = document.getElementById("view").value;
		var suffix = "";
		if("" != view){
			suffix = view;
			clearViewParamter();
		}else{
			suffix = CommonUtil.isLandscape()?"l":"p";
		}
		*/
		//GLOBAL_screenview = CommonUtil.isLandscape()?"l":"p";
		//CommonUtil.debug("---applyCss---" + suffix);
		
		if(!CommonUtil.isIphone()){
			$('#main').css(cssObj);
		}
	}
	
	function changePageCss()
	{
		applyCss();
		PoiCommonHelper.changePoiDetailCss();
		PoiCommonHelper.changePhoneNoLayout();
	}
	
	function resizeScreen()
	{
		changePageCss();
		if($("#poimain").css("display")!="none"){
			switchScreenForMapImage();
		}
		centerPopup_poidetail();
	}
	
	function initScreen()
	{
		changePageCss();
		var dummyData = document.getElementById("dummyData").value;
		if("true" == dummyData)
		{
			Global_openGLParam["dummyData"] = true;
			initDummyData();
		}
		else
		{
			initPhoneGap();
		}
		document.getElementById("poikey").focus();
	}
	
	function Poi_Util_throttle(method, context) {
		clearTimeout(GLOBAL_tmid);
		GLOBAL_tmid= setTimeout(function(){
		method.call(context);
		}, 100);
	}
	
	function lazyLoadJsAndCss( template, backFromViewOrAddReview ){
		if(template=="poitheater"){
			var jsPathArray = new Array();
			jsPathArray.push("js/movie_Compressed.js");
			// jsPathArray.push("js/movieDatePicker.js");
			// jsPathArray.push("js/movieshowtime.js");
			// jsPathArray.push("js/date.format.js");
			
			if(!backFromViewOrAddReview){
				PoiCommonHelper.onChangeTab('poidetailtab',0,hasTabNum,'poimain');
			}
		
			LazyLoader.lazyLoadResource(jsPathArray, null, function(){
				// Do nothing
			});
		}else{
			if(!backFromViewOrAddReview){
				PoiCommonHelper.onChangeTab('poidetailtab',0,hasTabNum,'poimain');
			}
		}
	}
	
	function fetchLogo(logoName)
	{
		if(logoName)
		{
			//console.log("-logoName:" + logoName);
			if(isExternalLogo(logoName))
			{
				var externalLogoHtml = "<img class='clsLogoImageDiv' src='" + logoName + "'/>";
				$('#logImageDiv').html(externalLogoHtml);
			}
			else
			{
				var width = getActualSize($("#logImageDiv").width());
				var height = getActualSize($("#logImageDiv").height());
				var searchCriteria = {
						"imageName":	logoName,
						"width":	width,
						"height":	height
						};
				PoiFetchDataHelper.fetchLogoImage(searchCriteria);
			}
		}
	}
	
	function isExternalLogo(logoName)
	{
		var isExternal = false;
		if(logoName.indexOf("http") == 0)
		{
			isExternal = true;
		}
		return isExternal;
	}
	
	function clickReviewNo()
	{
		//check if hasReview Tab
		if(PoiDetailSpecificHelper.hasReviewTab())
		{
			//console.log("---display review tab--");
			setShownTab(true);
		}
	}
	function hideTopReviewNo(){
		$("#topReviewNo").html(" ");
		$("#topReviewNoLandscape").hide();
	}
	
	function saveFavFlag()
	{
		var poiDetailObj = getPoiDetailObj();
		poiDetailObj.existedInFavorite=true;
		//PoiCacheHelper.setPoiDetailCache(JSON.stringify(poiDetailObj));
		CommonUtil.saveInCache(appendPoiKey(PoiCacheKeys.POIDETAIL), JSON.stringify(poiDetailObj));
	}
	
/**
 * =============================================================
 * ========================= for POI map=====================
 * =============================================================
 */
	function isOpenGLMap(){
		return CommonUtil.getBoolean(FeatureHelper["OPENGLMAP"])&& !Global_openGLParam["dummyData"];
	}
	function switchScreenForMapImage()
	{
		//if map exist,display it again.
		if("Y" == PoiCacheHelper.getMapFlag()){
			if(isOpenGLMap()){
				beforeFetchOpenGLMap();
			}else{
				displayMapImage();
			}
		}
	}
	function displayMapImage()
	{
		$("#poidesc").hide();
		$("#mapImageContainer").hide();
		//re-compute poimain div height to avoid map shift
		if(!CommonUtil.isLandscape()){
			var poiMainHeight = CommonUtil.getScreenHeight() - document.getElementById("blackLine").offsetTop - $("#blackLine").height();
			$("#poimain").height(poiMainHeight);
		}
		//check if biz hour exist
		if("Y" == PoiCacheHelper.getBizHourFlag()){
			cssHelper.replaceObjCSSById("mainTabFirstLine","clsMainTabFirstLineEmpty","clsMainTabFirstLine");
		}
		var poiDetailObj = getPoiDetailObj();
		var lat = poiDetailObj.stop.lat;
		var lon = poiDetailObj.stop.lon;
		if(lat != 0 && lon != 0){
			if(isOpenGLMap()){
				beforeFetchOpenGLMap(poiDetailObj);
			}else{
				lat = lat/JSConstants.DEGREE_MULTIPLIER;
				lon = lon/JSConstants.DEGREE_MULTIPLIER;
				var addressParameter = lat + ',' + lon;
				var searchCriteria = PoiDetailSpecificHelper.getSearchCriteria(addressParameter);
			    fetchMapImage(searchCriteria);
			}
		}
	}
	var Global_test = 0;
	function beforeFetchOpenGLMap(){
		$("#poidesc").hide();
		$("#mapImageContainer").hide();
		var poiDetailObj = getPoiDetailObj();
		var lat = poiDetailObj.stop.lat;
		var lon = poiDetailObj.stop.lon;
		var ret = getMapSize();
		Global_test ++;
		if(Global_test % 2 != 0){
			window.setTimeout(beforeFetchOpenGLMap,200);
		}else{
			Global_openGLParam["mapSearchCriteria"] = {
				"width":ret["width"],
				"height":ret["height"],
				"lat": lat,
				"lon" : lon,
				"zoom" : 3,
				"poiId" : poiDetailObj.poi.bizPoi.poiId,
				"orientation" : ret["orientation"]
				};
			fetchOpenGLMap();
		}
	}
	
	/**
	 * @TODO	We compute an adaptive map size on poiDetail main tab.
	 * 			For portrait mode: the map and buttons just right fill one screen;
	 * 			For landscape mode: the proportion of map height and map width is 0.3. If the computed height is less than 100px, then we assign
	 * 			100px to the map height.
	 * @author xljiang
	 * @returns Array compose of width and height
	 */
	
	function getMapSize(){
		var tdObjHeight = document.getElementById("mainInfoTd").offsetHeight;
		
		var	priceRange = document.getElementById("priceRange"),
		mapBlank2 = document.getElementById("mapBlank2"),
		ret = {"width":"0","height":"0","orientation":"portrait"},
		percent = 0.9,//proportion of map width and screen width
		percentHW = $("#openFlag").html() ? 0.31 : 0.35,//the proportion of map height and map width
		minHeight = 100;
		if(CommonUtil.isScoutStyle()){
			percent = 1;
		}
		//1. elemental result
		ret["width"] = parseInt(CommonUtil.getScreenWidth() * percent) ;
		ret["height"] = parseInt(tdObjHeight - priceRange.offsetHeight);
		//2. adjustment
		if(CommonUtil.isLandscape()){
			ret["height"] = parseInt(ret["width"] * percentHW);
			ret["orientation"] = "landscape";
		}else{
			var mapBlank2BottomPosition = mapBlank2.offsetTop + mapBlank2.offsetHeight;
			var screenHeight = CommonUtil.getScreenHeight();
			if(mapBlank2BottomPosition < screenHeight){
				ret["height"] = ret["height"] + parseInt(screenHeight - mapBlank2BottomPosition) - 2;
			}else if(mapBlank2BottomPosition > screenHeight){
				ret["height"] = ret["height"] - parseInt(mapBlank2BottomPosition - screenHeight);
			}
		}
		//3. last adjust: if the map height is less than the min height, then assign the min height to the map height.
		if(ret["height"] < minHeight){
			ret["height"] = minHeight;
		}
		return ret;
	}
	
	function fetchMapImage(searchCriteria)
	{	
		PoiCacheHelper.setMapFlag("Y");
		var staticMapUrl = PoiDetailSpecificHelper.getStaticMapUrl(searchCriteria);
		var mapDiv = $("#mapImageDiv")[0];
		var oldSrc = mapDiv.src;
		
		if(oldSrc == staticMapUrl){
			$("#mapImageContainer").show();
		}else{
			loadPopup_poidetail();
			mapDiv.src = staticMapUrl;
			mapDiv.onerror = loadStaticMapError;
			mapDiv.onload = loadStaticMapSuccess;
			PoiDetailSpecificHelper.setMapSize();
		}
	}
	
	function loadStaticMapError()
	{
		PopupUtil.hide();
	}
	
	function loadStaticMapSuccess()
	{
		PopupUtil.hide();
		$("#mapImageContainer").show();
	}
	

var Global_openGLParam = {
	"needMap" : true,
	"mapSearchCriteria" : "",
	"dummyData" : false,
	"fetchErrMsg" : "Deactived"
};

function fetchOpenGLMap(){
	PoiCacheHelper.setMapFlag("Y");
	loadPopup_poidetail();
	PoiDetailSpecificHelper.setMapSize();
	navigator.tnservice.getPOIDetailMap(Global_openGLParam["mapSearchCriteria"],fetchOpenGLMapCallBack,function(){console.log("getPOIDetailMap error");});
}

function fetchOpenGLMapCallBack (mapData){
	var mapStr,orientation="";
	//new data structure have mapdata and orientation field
	if(mapData.return_result.mapdata == undefined)
	{
		mapStr = CommonUtil.getValidString(mapData.return_result);
	}
	else
	{
		mapStr = mapData.return_result.mapdata;
		orientation = mapData.return_result.orientation;
	}
	
	if(Global_openGLParam["fetchErrMsg"] == mapStr|| "" == mapStr){
		//can't fetch map
		CommonUtil.debug("can't fetch map");
	}
	else
	{
		//compare if the returned map view is same with current screen view
		CommonUtil.debug("after fetch the map and orientation is:" + orientation);
		$('#mapImageDiv').attr("src","data:image/gif;base64,"+mapStr);
		$("#mapImageContainer").show();
	}
	PopupUtil.hide();
}