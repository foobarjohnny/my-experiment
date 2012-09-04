function PoiDetailSpecific() {

}

PoiDetailSpecific.prototype = {
		setMapSize:function()
		{
//			do nothing for Android
		},
		
		getSearchCriteria: function(addressParameter)
		{
			var ret = getMapSize();
			var widthParameter = ret["width"];
			var heightParameter = ret["height"];
		    //Get Image with customized center
			var searchCriteria = {
					"imageName":'',
					"width":widthParameter,
					"height":heightParameter,
					"center":addressParameter,
					"markers":'color:blue|' + addressParameter
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
			return staticMapUrl;
		},
		
		fetchLogoWhenShowMain : function(logoName)
		{
//			Do nothing for Android, only for SCOUT
		},
		
		getTopPartLogoImage : function(poiObj)
		{
			var logoName = CommonUtil.getValidString(poiObj.poiLogo);
			if("" == logoName)
			{
				logoName = CommonUtil.getValidString(poiObj.brandLogo);
			}
			if("" == logoName)
			{
				logoName = CommonUtil.getValidString(poiObj.categoryLogo);
			}
			displayEmptyLogo();
			
			//logoName = "/tnimages/logo/~Outback-Steakhouse.png";
			fetchLogo(logoName);
		},

		initTab : function(poiDetailObj){
			var template = this.getPoiTemplateName(poiDetailObj.poi.bizPoi.categoryId);
			var hasReview = this.hasReviewCheck(poiDetailObj);
			var hasDeal = this.hasDeals(poiDetailObj);
			var hasMenu = this.hasPoiMenu(poiDetailObj);
			var hasExtra = this.hasPoiExtraAttributes(poiDetailObj);
			var hasTheater = false;
			var hasGasPrice = false;
			if(!hasReview){
				hideTopReviewNo();
			}
			// Hotel
			var hasHotel = false;
			// Open Table
			var hasOpenTable = false;
			

			CommonUtil.debug("Movie Feature: "+FeatureHelper["MOVIE"]);
			//CommonUtil.debug("RESTAURANT Feature: "+FeatureHelper["RESTAURANT"]);
			//CommonUtil.debug("HOTEL Feature: "+FeatureHelper["HOTEL"]);

			if(template=="poitheater" && CommonUtil.getBoolean(FeatureHelper["MOVIE"]))
			{
				hasTheater = true;
			}
			
			//poiOpenTable
			/*
			if(template=="poiOpenTable" && CommonUtil.getBoolean(FeatureHelper["RESTAURANT"]))
			{
				hasOpenTable = true;
			}
			
			//poihotel
			if(template=="poihotel" && CommonUtil.getBoolean(FeatureHelper["HOTEL"]))
			{
				hasHotel = true;
			}
			*/
			
			if(poiDetailObj.poi.supplimentInfos && CommonUtil.getBoolean(FeatureHelper["GAS"]))
			{
				hasGasPrice = true;
			}

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
			
			initPoiCacheByReviewStatus(backFromViewOrAddReview);
			
			if(hasTheater||hasOpenTable||hasHotel){
				loadResourceAndSetShownTab(template,backFromViewOrAddReview);
			}else{
				setShownTab(backFromViewOrAddReview);
			}
		},
		
		loadPoiMain : function(poiDetailObj){
			//check if it's adsPoi
	    	   if(this.hasAdsId(poiDetailObj))
	    	   {
	    		   PoiCommonHelper.fetchPoiMainFromServer();
	    	   }
	    	   else if(this.shouldCallAdsServer(poiDetailObj))
	           {
	    		   this.fetchOrganicAds();
	           }
	           else
	           {
	              var flag = CommonUtil.getBoolean(poiDetailObj.poi.hasPoiDetails);
	              if(flag)
	              {
	            	  this.fetchPoiMain();
	              }
	              else
	              {
	            	  displayMapImage();
	              }
	           }
		},
		
		getSearchCriteriaForAds : function()
		{
			var poiDetailObj = getPoiDetailObj();
			var adsId = PoiCommonHelper.getAdsId(poiDetailObj);
			var searchCriteria = {
					"poiId":		0,
					"categoryId":	poiDetailObj.poi.bizPoi.categoryId,
					"adsId":	adsId, //"762475",
					"width":0,
					"height":0
					};
			
			return searchCriteria;
		},
		
		fetchPoiMain : function()
		{
			var searchCriteria = getPoiSearchKey();
			searchCriteria.width = $("#logImageDiv").width();
			searchCriteria.height = $("#logImageDiv").width();
			
			loadPopup_poidetail();
			var ajxUrl = GLOBAL_hostUrl + "getPoiDetailData.do?operateType=main&jsonStr=" + JSON.stringify(searchCriteria) + "&" + CommonUtil.getClientInfo();
			var ajaxOptions = {
					data:0,
					loadingStyle:2,
					url:ajxUrl,
					onSuccess:this.ajaxCallBackPoiMain
			};
			CommonUtil.ajax(ajaxOptions);
		},
		
		ajaxCallBackPoiMain : function(responseText)
		{
			var result = CommonUtil.getValidString(responseText);
			if(result)
			{
				var resultObj = JSON.parse(result);
				if(resultObj.success)
				{
					PoiCacheHelper.setPoiMainCache(result);
					displayPoiMain();
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
		
		getPoiTemplateName : function(categoryId)
		{
			var template = "";
			var movieCategoryList = [181];
			var hotelCategoryList = [595];
			var restaurantList = [2041,230,227,249,250,646,229,231,232,236,240,241,243,251,664,253,254,256,257,261];
			
			if(this.isElementExistInList(hotelCategoryList,categoryId)) return "poihotel";
			if(this.isElementExistInList(movieCategoryList,categoryId)) return "poitheater";
			if(this.isElementExistInList(restaurantList,categoryId))	return "poiOpenTable";
			
			return template;
		},
		
		isElementExistInList : function(list, element)
		{
			for(var i=0;i<list.length;i++)
			{
				if(element == list[i])
				{
					return true;
				}
			}
			return false;
		},
		
		filterAdSource : function(adSource)
		{
			var validSource = "";
			if("CS" == adSource || "TN" == adSource || "ATTi" == adSource || "Groupon" == adSource )
			{
				validSource = adSource;
			}
			
			return validSource;
		},
		
		handlePoiMainFromServer : function(resultObj)
		{
			PoiCacheHelper.setPoiMainCache(resultObj.mainTab);
			PoiCacheHelper.setPoiMenuCache(resultObj.menuTab);
			PoiCacheHelper.setPoiDealCache(resultObj.dealTab);
			showpoimain();
		},
		
		fetchGasByPriceData : function()
		{
			var poiDetailObj = getPoiDetailObj();
			var supplimentInfos = poiDetailObj.poi.supplimentInfos;
			var data = {"success":false};
			if(supplimentInfos)
			{
				data.success = true;
				var size = supplimentInfos.length;
				var priceList = new Array(size);
				
				for(var i=0;i<size;i++)
			   	{
					var item = {
						"name" : supplimentInfos[i].supportInfo,
						"price" : supplimentInfos[i].price
					};
					priceList[i] = item;
			   	}
			}
			data.data = JSON.stringify(priceList);
			PoiCacheHelper.setGasByPriceCache(JSON.stringify(data));
			displayGasByPrice();
		},
		
		fetchOrganicAds : function()
		{
			loadPopup_poidetail();
			var ajxUrl = GLOBAL_hostUrl + "getPoiDetailData.do?operateType=adsPoi&jsonStr=" + JSON.stringify(getPoiSearchKey()) + "&" + CommonUtil.getClientInfo();
			var ajaxOptions = {
					data:0,
					url:ajxUrl,
					onSuccess:this.ajaxCallBackOrganicAds
			};
			CommonUtil.ajax(ajaxOptions);
		},
		
		ajaxCallBackOrganicAds : function(responseText)
		{
			var result = CommonUtil.getValidString(responseText);
			if(result)
			{
				var resultObj = JSON.parse(result);
				if(resultObj.success)
				{
					PoiCacheHelper.setPoiMainCache(resultObj.mainTab);
					PoiCacheHelper.setPoiMenuCache(resultObj.menuTab);
					PoiCacheHelper.setPoiDealCache(resultObj.dealTab);
					displayPoiMain();
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
		
		shouldCallAdsServer : function(poiDetailObj)
		{
			var isAdsPoi = CommonUtil.getBoolean(poiDetailObj.poi.isAdsPoi);
			var isSponsorPoi = false;//CommonUtil.getBoolean(poiDetailObj.poi.isSponsorPoi);
			var poiId = poiDetailObj.poi.bizPoi.poiId;
			var callAds = false;
			if((isAdsPoi || isSponsorPoi) && "0" != poiId)
			{
				callAds = true;
			}
			return callAds;
		},
		
		hasAdsId : function(poiDetailObj)
		{
			var result = false;
			var adsId = PoiCommonHelper.getAdsId(poiDetailObj);
			if("" != adsId && "0" != adsId){
				result = true;
			}
			
			return result;
		},
		
		hasReviewCheck : function(poiDetailObj)
		{
			var flag = true;
			var poiId = poiDetailObj.poi.bizPoi.poiId;
			var isRatingEnable = poiDetailObj.poi.isRatingEnable;
			if("0" == poiId || !isRatingEnable)
			{
				flag = false;
			}
			return flag;
		},
		
		hasDeals : function(poiDetailObj)
		{
			return CommonUtil.getBoolean(poiDetailObj.poi.hasDeals);
		},
		
		hasHotelTab : function(poiDetailObj)
		{
			return CommonUtil.getBoolean(poiDetailObj.poi.hasHotelAlliance);
		},
		
		hasOpenTableTab : function(poiDetailObj)
		{
			if(poiDetailObj.poi.openTable){
				return CommonUtil.getBoolean(poiDetailObj.poi.openTable.isReservable);
			}
			return false;
		},

	    hasPoiMenu : function(poiDetailObj)
	    {
	       var flag1 = CommonUtil.getBoolean(poiDetailObj.poi.hasPoiMenu);
	       var flag2 = CommonUtil.getBoolean(poiDetailObj.poi.hasAdsMenu);
	       var flag = false;
	       if(flag1 || flag2)
	       {
	           flag = true;
	       }
	       return flag;
	    },
		
		hasPoiExtraAttributes : function(poiDetailObj)
		{
			return CommonUtil.getBoolean(poiDetailObj.poi.hasPoiExtraAttributes);
		},
	    
		hasReviewTab: function()
		{
			var poiDetailObj = getPoiDetailObj();
			var flag = true;
			var poiId = poiDetailObj.poi.bizPoi.poiId;
			var isRatingEnable = poiDetailObj.poi.isRatingEnable;
			if("0" == poiId || !isRatingEnable)
			{
				flag = false;
			}
			return flag;
		}
};

var PoiDetailSpecificHelper = new PoiDetailSpecific();
