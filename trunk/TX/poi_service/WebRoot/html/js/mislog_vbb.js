	var MisLogConstants = {
			EVENT_ID_VBB_INITIAL_VIEW_CLICK:783,
			EVENT_ID_VBB_INITIAL_VIEW_END:785,
			EVENT_ID_VBB_DETAIL_VIEW_DRIVETO:788,
			EVENT_ID_VBB_DETAIL_VIEW_MOREINFO_CLICK :789,
			EVENT_ID_VBB_POI_VIEW_END :791,
			EVENT_ID_VBB_POI_VIEW_DRIVETO :792,
			EVENT_ID_VBB_POI_VIEW_CALLTO :793,
			EVENT_ID_VBB_POI_VIEW_DEAL :794,
			EVENT_ID_VBB_POI_VIEW_MENU :795,
			EVENT_ID_VBB_POI_VIEW_MAP :796,
			EXIT_REASON_DRIVE_TO: 5,
			EXIT_REASON_MAP: 6
	};

	function recordVbbMisLog_Initial_View_Click() {
		recordMisLogOfVbb(MisLogConstants.EVENT_ID_VBB_INITIAL_VIEW_CLICK);
    }

	function recordVbbMisLog_Detail_View_DriveTo() {
		recordMisLogOfVbb(MisLogConstants.EVENT_ID_VBB_DETAIL_VIEW_DRIVETO);
    }
	
	function recordVbbMisLog_Detail_View_End(reason, duration) {
		//before drive to, end the vie first
		recordMisLogOfVbb2(MisLogConstants.EVENT_ID_VBB_INITIAL_VIEW_END,reason,duration);
    }
	
	function recordVbbMisLog_Detail_View_MoreInfo_Click() {
		recordMisLogOfVbb(MisLogConstants.EVENT_ID_VBB_DETAIL_VIEW_MOREINFO_CLICK);
    }
	
	function recordMisLog_DriveTo_internal() {
		recordMisLogOfVbb(MisLogConstants.EVENT_ID_VBB_POI_VIEW_DRIVETO);
    }
	
	function recordMisLog_Poi_View_End(reason, duration) {
		recordMisLogOfVbb2(MisLogConstants.EVENT_ID_VBB_POI_VIEW_END,reason,duration);
    }
	
	function recordMisLog_ViewCoupon() {
		recordMisLogOfVbb(MisLogConstants.EVENT_ID_VBB_POI_VIEW_DEAL);
    }
	
	function recordMisLog_CallTo() {
		recordMisLogOfVbb(MisLogConstants.EVENT_ID_VBB_POI_VIEW_CALLTO);
    }

	function recordMisLog_ViewMenu() {
		recordMisLogOfVbb(MisLogConstants.EVENT_ID_VBB_POI_VIEW_MENU);
    }

	function recordMisLog_ViewMap_internal() {
		recordMisLogOfVbb(MisLogConstants.EVENT_ID_VBB_POI_VIEW_MAP);
    }
	
	function getCommonVBBFields()
	{
		var misLogObj = {
				"70002" : $("#adId").val(),
				"70003" : $("#hidAdSource").val(),
				"70001" : $("#poiId").val(),
				"70012" : $("#poiType").val()
		};
		
		return misLogObj;
	}

	function recordMisLogOfVbb(eventId)
	{
		SDKAPI.logEvent(eventId,getCommonVBBFields());
	}
	
	function recordMisLogOfVbb2(eventId,reason,duration)
	{
		var misLogObj = getCommonVBBFields();
		misLogObj["70037"] = reason;
		misLogObj["70038"] = duration;
		SDKAPI.logEvent(eventId,misLogObj);
	}