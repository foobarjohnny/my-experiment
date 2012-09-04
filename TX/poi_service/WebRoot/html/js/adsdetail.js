	function getPoiDetailObj()
	{
		return JSON.parse(PoiCacheHelper.getAdsDetailCache());
	}
	
	function hideAll(){
		
		$("#poimain").hide();
		$("#poideals").hide();
		$("#poimenu").hide();
	}
	
	function changePageCss()
	{
		PoiCommonHelper.changePoiDetailCss();
		PoiCommonHelper.changePhoneNoLayout();
	}
	
	function resizeMenuImage()
	{
		
	}
	
	function POI_API_isLandscape()
	{
		return CommonUtil.isLandscape();
	}
	
	function fetchAdsDetailData(){
		cleanAdsCache();
		var adsId = PoiCacheHelper.getAdsIdCache();
		$("#adId").val(adsId);
		var ajxUrl = GLOBAL_hostUrl + "adsinfo.do?operateType=fetchDetailData&adsId=" + adsId + "&" + CommonUtil.getClientInfo()+"&isDummy="+$("#isDummy").val();
		var ajaxOptions = {
				loadingStyle:1,
				url:ajxUrl,
				onSuccess:ajaxCallBackFetchAdsDetail
		};
		CommonUtil.ajax(ajaxOptions);
	}
	
	function ajaxCallBackFetchAdsDetail(resultObj){
		GLOBAL_adsDetailDataReady = true;
	    checkFavStatus();
		//put it into cache and display the data
		PoiCacheHelper.setAdsDetailCache(resultObj.baisc);
		PoiCacheHelper.setPoiMainCache(resultObj.mainTab);
		PoiCacheHelper.setPoiMenuCache(resultObj.menuTab);
		PoiCacheHelper.setPoiDealCache(resultObj.dealTab);
		//
		initTopPart();
		//
		initTabs(resultObj.mainTab);
		//
	}
	
	function initTopPart(){

		var poiDetailObj = getPoiDetailObj();
		var poiObj = poiDetailObj.poi;
		//display the top part;
		$("#name").html(poiObj.bizPoi.brand);
		$("#distance").html(PoiCacheHelper.getDistanceTextCacheForAds());
		PoiCommonHelper.setPoiAddressDisplay(poiDetailObj.stop);
		PoiCommonHelper.setPhoneNo();
		PoiCommonHelper.changePhoneNoLayout();
		
		$("#favIndicator").attr("class","favorites_button_unfocused");
		$("#favHref").attr("onClick","javascript:PoiCommonHelper.saveFavToClient()");
		//showMain();
		//showDeals();
		//showMenu();
	    //change the css
	}
	
	function cleanAdsCache(){
		PoiCacheHelper.removeAdsDetailCache();
		PoiCacheHelper.removePoiMainCache();
		PoiCacheHelper.removePoiMenuCache();
		PoiCacheHelper.removePoiDealCache();
	}
	
	function showpoimain(){
		
		var data = CommonUtil.getValidString(PoiCacheHelper.getPoiMainCache());
		var imgFmt = '<image class="{1}" id="{0}"/>';
		if("" != data)
		{
			//
			var resultObj = JSON.parse(data);
			var desc = CommonUtil.getValidString(resultObj.description);
			desc = PoiCommonHelper.formatPoiDesc(desc);
			$("#poidesc").html(desc);

			var adSource = resultObj.adSource;
			if(adSource && "" !=adSource)
			{
				var adSourceIconClass = PoiCommonHelper.getAdSourceImageClass(adSource);
				$("#adSource").html(imgFmt.format('adSourceImage',adSourceIconClass)); 
			}else{
				$("#adSource").hide();
			}
			$("#hidAdSource").val(adSource);
			$("#poiId").val(resultObj.poiId);
			$("#poiType").val(resultObj.poiType);
			fetchLogo(resultObj.logoName);
		}
	}
	
	function showpoideals()
	{	
		PoiCommonHelper.showpoideals();
	}
	
	function showpoimenu(){
		PoiCommonHelper.displayPoiMenu();
		
	}
	
	function displayEmptyLogo()
	{
		var emptyLogoHtml = "<img class='clsLogoImageDiv' src='" + GLOBAL_imageCommonUrl + "poi_details_default_logo_unfocused.png'/>";
		$('#logImageDiv').html(emptyLogoHtml);
	}
	
	
	function displayLogoImage(logoName)
	{
		var externalLogoHtml = "<img class='clsLogoImageDiv' src='" + logoName + "'/>";
		$('#logImageDiv').html(externalLogoHtml);
	}
	
	function fetchLogo(image)
	{
		if(image==""||image==undefined){
			displayEmptyLogo();
		}
		else
		{
			displayLogoImage(image);
		}
	}
	
	function initTabs(data){
		var resultObj = JSON.parse(data);
		var hasDeal = resultObj.hasDeal;
		var hasMenu = resultObj.hasPoiMenu;
		
		//var hasTabNum = 1; //main tab will always be there
		var configList = new Array();
		PoiCommonHelper.addTab(configList,true,I18NHelper["poidetail.tab.main"],"poimain");
		PoiCommonHelper.addTab(configList,hasDeal,I18NHelper["poidetail.tab.deals"],"poideals");
		PoiCommonHelper.addTab(configList,hasMenu,I18NHelper["poidetail.tab.menu"],"poimenu");
		PoiCommonHelper.generateTabs(configList);
		PoiCommonHelper.onChangeTab('poidetailtab',0,configList.length,'poimain');	
	}
	
	function initPhoneGap()
	{
		document.addEventListener("deviceready", onDeviceReady, false);
	}

	function onDeviceReady()
	{
		GLOBAL_isdeviceReady = true;
		checkFavStatus();
		//displayEmptyLogo();
	}
	
	function checkFavStatus()
	{
		if(isFavReady()){
			initFav();
		}
	}
	
	function initFav()
	{
		var poiDetailObj = getPoiDetailObj();
		if(PoiCommonHelper.isPoiAddressValid(poiDetailObj.stop))
		{
			SDKAPI.invokePrivateService("IsFavoriteAddress",poiDetailObj,setFavFlag);
		}
	}
	
	function setFavFlag(isFavJSONStr){
		var isFav = JSON.parse(isFavJSONStr).return_result;
		//CommonUtil.debug("is Favorite:"+isFav);
		if(isFav == true){
			$("#favIndicator").attr("class","favorites_add_button_unfocused");
			$("#favHref").attr("onClick","");
		}else{
			
		}
	}
	
	function isFavReady(){
		if(GLOBAL_adsDetailDataReady&&GLOBAL_isdeviceReady){
			return true;
		}
		return false;
	}
	
	function getDuration()
	{
		var currentDate = new Date();
		return currentDate.getTime() - GLOBAL_enterTime;
	}
	
	function recordMisLog_DriveTo()
	{
		recordMisLog_Poi_View_End(MisLogConstants.EXIT_REASON_DRIVE_TO,getDuration());
		recordMisLog_DriveTo_internal();
	}
	
	function recordMisLog_ViewMap()
	{
		recordMisLog_Poi_View_End(MisLogConstants.EXIT_REASON_MAP,getDuration());
		recordMisLog_ViewMap_internal();
	}
	
	function saveFavFlag()
	{
		
	}
