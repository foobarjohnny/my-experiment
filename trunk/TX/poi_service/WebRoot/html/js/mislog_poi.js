	var MisLogConstants = {
			EVENT_ID_POI_VIEW_DETAIL: 701,
			EVENT_ID_POI_VIEW_MAP: 702,
			EVENT_ID_POI_DRIVE_TO: 703,
			EVENT_ID_POI_CALL_TO: 704,
			EVENT_ID_POI_VIEW_MERCHANT: 705,
			EVENT_ID_POI_VIEW_COUPON: 706,
			EVENT_ID_POI_VIEW_MENU: 707,
			
            EVENT_ID_POI_VIEW_REVIEW:760,
            EVENT_ID_PROVIDER_REVIEW_IMPRESSION:761,
            EVENT_ID_PROVIDER_REVIEW_CLICK:762
	};

    function recordMisLog_ViewReview() {
        recordMisLogOfPoi(MisLogConstants.EVENT_ID_POI_VIEW_REVIEW);
    }
    
    function recordMisLog_Provider_Impression(provider, btnId) {
        var param = getMisLogJson();
        param["70050"] = btnId;
        param["70051"] = provider;
        
        SDKAPI.logEvent(MisLogConstants.EVENT_ID_PROVIDER_REVIEW_IMPRESSION,param);
    }
    
    function recordMisLog_Provider_Click(provider, btnId) {
        var param = getMisLogJson();
        param["70050"] = btnId;
        param["70051"] = provider;
        
        SDKAPI.logEvent(MisLogConstants.EVENT_ID_PROVIDER_REVIEW_CLICK,param);      
    }
	
	function recordMisLog_ViewDetail() {
		//recordMisLogOfPoi(MisLogConstants.EVENT_ID_POI_VIEW_DETAIL);
    }
	
	function recordMisLog_ViewMap() {
		//recordMisLogOfPoi(MisLogConstants.EVENT_ID_POI_VIEW_MAP);
    }
	
	function recordMisLog_DriveTo() {
		recordMisLogOfPoi(MisLogConstants.EVENT_ID_POI_DRIVE_TO);
    }
	
	function recordMisLog_CallTo() {
		recordMisLogOfPoi(MisLogConstants.EVENT_ID_POI_CALL_TO);
    }
	
	function recordMisLog_ViewMerchantContent() {
		if(CommonUtil.isIphone()){
			recordMisLogOfPoi(MisLogConstants.EVENT_ID_POI_VIEW_MERCHANT);
		}
    }

	function recordMisLog_ViewCoupon() {
		recordMisLogOfPoi(MisLogConstants.EVENT_ID_POI_VIEW_COUPON);
    }

	function recordMisLog_ViewMenu() {
		recordMisLogOfPoi(MisLogConstants.EVENT_ID_POI_VIEW_MENU);
    }
	
	//ATTR_EVENT_TIMESTAMP= 10003
	//INNER_ATTR_POI_INDEX = - 10001
	function recordMisLogOfPoi(eventId)
	{
		SDKAPI.logEvent(eventId,getMisLogJson());
	}
	
	function getMisLogJson()
	{
		var currentDate = new Date();
		var timeStamp = currentDate.getTime();
		var poiKey = $("#poikey").val();
		var misLogObj = {
				"10003" : timeStamp,
				"-10001" : poiKey
		};
		if(CommonUtil.isIphone()){
			var poiDetailObj = getPoiDetailObj();
			misLogObj["70001"]  = poiDetailObj.poi.bizPoi.poiId;
			misLogObj["70002"]  = CommonUtil.getValidString(poiDetailObj.poi.adsId);
		}
		//CommonUtil.debug("misLogObj:" + JSON.stringify(misLogObj));
		return misLogObj;
	}