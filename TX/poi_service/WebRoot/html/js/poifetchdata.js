function PoiFetchData() {
}

PoiFetchData.prototype = {
		fetchLogoImage : function(searchCriteria)
		{
			//check if cache exist
			var logoImage = CommonUtil.getValidString(PoiCacheHelper.getLogoImageCache(searchCriteria.logoName));
			if(logoImage != "")
			{
				displayLogoImage(logoImage);
			}
			else
			{
				var ajxUrl = GLOBAL_hostUrl + "getLogImage.do?jsonStr=" + JSON.stringify(searchCriteria) + "&" + CommonUtil.getClientInfo();
				var ajaxOptions = {
						url:ajxUrl,
						onSuccess:this.ajaxCallFetchLogoImage
				};
				CommonUtil.ajax(ajaxOptions);
			}
		},
		
		ajaxCallFetchLogoImage : function(resultObj){
   		  	var image = resultObj.image;
   		  	if( image != '' && image != null){
   		  		displayLogoImage(image);
       		  	PoiCacheHelper.setLogoImageCache(resultObj.imageName,image);
	  		}    	
		},
		
		fetchPoiExtra : function()
		{
			loadPopup_poidetail();
			var ajxUrl = GLOBAL_hostUrl + "getPoiDetailData.do?operateType=extra&jsonStr=" + JSON.stringify(getPoiSearchKey()) + "&" + CommonUtil.getClientInfo();
			var ajaxOptions = {
					loadingStyle:2,
					url:ajxUrl,
					onSuccess:this.ajaxCallBackPoiExtra
			};
			CommonUtil.ajax(ajaxOptions);
		},
		
		ajaxCallBackPoiExtra : function(resultObj)
		{
			if(resultObj.success)
			{
				PoiCacheHelper.setPoiExtraCache(JSON.stringify(resultObj));
				displayPoiExtra();
			}
			else
			{
				noRecordFoundForExtra();
			}	
		},
		
		fetchMenuData : function(){
			var searchCriteria = getPoiSearchKey();
			searchCriteria.menuWidth = $("#menuImageDiv").width();
			searchCriteria.menuHeight = $("#menuImageDiv").width();//$("#menuImageDiv").height(); height is zero when use percentage
			
			loadPopup_poidetail();
			var url = GLOBAL_hostUrl + "getPoiDetailData.do?operateType=menu&jsonStr=" + JSON.stringify(searchCriteria) + "&" + CommonUtil.getClientInfo();
			var ajaxOptions = {
					url:url,
					onSuccess:this.ajaxCallBackPoiMenu,
					loadingStyle:2
			};
			
			CommonUtil.ajax(ajaxOptions);
		},
		
		ajaxCallBackPoiMenu : function(resultObj){
			if(resultObj.success)
			{	
				PoiCacheHelper.setPoiMenuCache(JSON.stringify(resultObj));
				PoiCommonHelper.displayPoiMenu();
			} 	
		},
		
		fetchReviewData : function()
		{
			var poiDetailObj = getPoiDetailObj();
			var reviewSearchCriteria = {
					"poiId":		poiDetailObj.poi.bizPoi.poiId,
					"categoryId":	poiDetailObj.poi.bizPoi.categoryId
					};
			loadPopup_poidetail();
			var ajxUrl = GLOBAL_hostUrl + "poireview.do?operateType=view&jsonStr=" + JSON.stringify(reviewSearchCriteria) + "&" + CommonUtil.getClientInfo();
			var ajaxOptions = {
					loadingStyle:2,
					url:ajxUrl,
					onSuccess:this.ajaxCallBackReview
			};
			CommonUtil.ajax(ajaxOptions);
		},
		
		ajaxCallBackReview : function(resultObj)
		{
			//PoiCacheHelper.setPoiReviewCache(JSON.stringify(resultObj));
			CommonUtil.saveInCache(appendPoiKey(PoiCacheKeys.REVIEWDATA), JSON.stringify(resultObj));
	    	displayReview();	
		}
};
var PoiFetchDataHelper = new PoiFetchData();