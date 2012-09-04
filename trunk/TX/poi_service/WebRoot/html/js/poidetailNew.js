function PoiDetailSpecific() {

}

PoiDetailSpecific.prototype = {
		setMapSize:function()
		{
			if(window.devicePixelRatio == 2)
			{
				var ret = getMapSize();
				var widthParameter = ret["width"];
				var heightParameter = ret["height"];
				$("#mapImageDiv").width(widthParameter);
				$("#mapImageDiv").height(heightParameter);
			}
		},
		
		getSearchCriteria: function(addressParameter)
		{
			var ret = getMapSize();
			var widthParameter = getActualSize(ret["width"]);
			var heightParameter = getActualSize(ret["height"]);
		    //Get Image with customized center
			var searchCriteria = {
					"imageName":'',
					"width":widthParameter,
					"height":heightParameter,
					"center":addressParameter,
					"markers":'color:scoutA|' + addressParameter
					};
			return searchCriteria;
		},
		
		getStaticMapUrl: function(searchCriteria)
		{
			var poiDetailObj = getPoiDetailObj();
			var poiID = poiDetailObj.poi.bizPoi.poiId;
		
			var staticMapUrl = GLOBAL_mapapiUrl;
			staticMapUrl += "?width=" + searchCriteria.width + "&height=" + searchCriteria.height + "&zoom=1&center=";
			staticMapUrl += searchCriteria.center + "&markers=" + encodeURIComponent(searchCriteria.markers) + "&apiKey=" + encodeURIComponent(GLOBAL_mapapiKey)+"&poiID="+poiID;
			if(window.devicePixelRatio == 2)
			{
				staticMapUrl += "&reserved=layers=NA_TA_LARGE";
			}
			return staticMapUrl;
		},
		
		fetchLogoWhenShowMain : function(logoName)
		{
			fetchLogo(logoName);
		},
		
		getTopPartLogoImage : function(poiObj)
		{
			displayEmptyLogo();
		},

		initTab : function(poiDetailObj){
			var backFromViewOrAddReview = isBackFromViewOrAddReview();
			if(!backFromViewOrAddReview){
				clearPoiCache();
			}
			
			var data = CommonUtil.getValidString(PoiCacheHelper.getPoiMainCache());
		    if("" == data)
		    {
				var configList = new Array();
				PoiCommonHelper.addTab(configList,true,I18NHelper["poidetail.tab.main"],"poimain");
				PoiCommonHelper.generateTabs(configList);
				setShownTab(false);
		    }else{
		    	this.generateTabsBaseOnFlag(data);
		    }
		    
		    if(backFromViewOrAddReview){
		    	PoiCacheHelper.setReviewStatus("");
			}
		},
		
		loadPoiMain : function(poiDetailObj){
			PoiCommonHelper.fetchPoiMainFromServer();
		},

		getSearchCriteriaForAds : function()
		{
			var poiDetailObj = getPoiDetailObj();
			var adsId = PoiCommonHelper.getAdsId(poiDetailObj);
			var searchCriteria = {
					"poiId":		poiDetailObj.poi.bizPoi.poiId,
					"categoryId":	poiDetailObj.poi.bizPoi.categoryId,
					"adsId":	adsId, //"762475",
					"width":0,
					"height":0,
					"menuWidth":0,
					"menuHeight":0
					};
			
			return searchCriteria;
		},
		
		handlePoiMainFromServer : function(resultObj)
		{
			if(resultObj.success)
			{
				var mainText = resultObj.mainTab;
				//
				PoiCacheHelper.setPoiMainCache(mainText);
				PoiCacheHelper.setPoiMenuCache(resultObj.menuTab);
				PoiCacheHelper.setPoiDealCache(resultObj.dealTab);
				
				if(mainText != "")
				{
					//
					var mainTabObj = JSON.parse(resultObj.mainTab);
					//
					PoiDetailSpecificHelper.generateTabsBaseOnFlag(resultObj.mainTab);
					//fetch logo
					fetchLogo(mainTabObj.logoName);					
				}
				else
				{
					displayMapImage();
				}
			}
			else
			{
				displayMapImage();
			} 	
		},
		
		generateTabsBaseOnFlag : function(data)
		{
			var resultObj = JSON.parse(data);
			var hasReview = resultObj.hasReview;
			var hasDeal = resultObj.hasDeal;
			var hasMenu = resultObj.hasPoiMenu;
			var hasExtra = resultObj.hasPoiExtraAttributes;
			var hasGasPrice = resultObj.hasGasPrice;
			if(!hasReview){
				hideTopReviewNo();
			}
			//Theater
			var hasTheater = resultObj.hasTheater && CommonUtil.getBoolean(FeatureHelper["MOVIE"]);
			// Hotel
			var hasHotel = false;//resultObj.hasHotel && CommonUtil.getBoolean(FeatureHelper["HOTEL"]);
			// Open Table
			var hasOpenTable = false;//resultObj.hasOpenTable && CommonUtil.getBoolean(FeatureHelper["RESTAURANT"]);
			
			var configList = new Array();
			// if the poi is a hotel then we show hotel main tab
			/*
			if(hasHotel){
				PoiCommonHelper.addTab(configList,true,I18NHelper["poidetail.tab.main"],"poihotelmain");
			}else if(hasOpenTable){
				PoiCommonHelper.addTab(configList,true,I18NHelper["poidetail.tab.main"],"poiopentablemain");
			}else{
				PoiCommonHelper.addTab(configList,true,I18NHelper["poidetail.tab.main"],"poimain");
			}*/
			PoiCommonHelper.addTab(configList,true,I18NHelper["poidetail.tab.main"],"poimain");

			PoiCommonHelper.addTab(configList,hasReview,I18NHelper["poidetail.tab.reviews"],"poireview");
			PoiCommonHelper.addTab(configList,hasDeal,I18NHelper["poidetail.tab.deals"],"poideals");
			PoiCommonHelper.addTab(configList,hasMenu,I18NHelper["poidetail.tab.menu"],"poimenu");
			PoiCommonHelper.addTab(configList,hasTheater,I18NHelper["poidetail.tab.showtime"],"poitheater");
			PoiCommonHelper.addTab(configList,hasGasPrice,I18NHelper["poidetail.tab.gasprices"],"poigas");
			PoiCommonHelper.addTab(configList,hasExtra,I18NHelper["poidetail.tab.extra"],"poiextra");
			
			//PoiCommonHelper.addTab(configList,hasHotel,I18NHelper["poidetail.tab.reserve"],"poihotelreserve");
			//PoiCommonHelper.addTab(configList,hasOpenTable,I18NHelper["poidetail.tab.reserve"],"poiopentablereserve");

			PoiCommonHelper.generateTabs(configList);
			
			var backFromViewOrAddReview = isBackFromViewOrAddReview();
			var template = "";
			if(hasTheater)
			{
				template = "poitheater";
			}
			else if(hasOpenTable)
			{
				template = "poiOpenTable";
			}
			else if(hasHotel)
			{
				template = "poihotel";
			}
			if(hasTheater||hasOpenTable||hasHotel){
				loadResourceAndSetShownTab(template,backFromViewOrAddReview);
			}else{
				setShownTab(backFromViewOrAddReview);
			}
		},
		
		fetchGasByPriceData : function()
		{
			var searchCriteria = getPoiSearchKey();
			loadPopup_poidetail();
			var ajxUrl = GLOBAL_hostUrl + "getPoiDetailData.do?operateType=gasprice&jsonStr=" + JSON.stringify(searchCriteria) + "&" + CommonUtil.getClientInfo();
			var ajaxOptions = {
					loadingStyle:2,
					url:ajxUrl,
					onSuccess:this.ajaxCallBackGasByPrice
			};
			CommonUtil.ajax(ajaxOptions);
		},
		
		ajaxCallBackGasByPrice : function(resultObj)
		{
			if(resultObj.success)
			{
				PoiCacheHelper.setGasByPriceCache(JSON.stringify(resultObj));
				displayGasByPrice();
			}   	
		},
		
		hasReviewTab: function()
		{
			var flag = false;
			var data = CommonUtil.getValidString(PoiCacheHelper.getPoiMainCache());
			if("" != data)
			{
				var resultObj = JSON.parse(data);
				flag = resultObj.hasReview;
			}
			return flag;
		}
};

var PoiDetailSpecificHelper = new PoiDetailSpecific();
