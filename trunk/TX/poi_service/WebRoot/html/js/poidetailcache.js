var PoiCacheKeys = {
		POIDETAIL	:	"SESSION_STORAGE_POIDETAILDATA",
		SIMPLEPOI	:	"SESSION_STORAGE_SIMPLE_POIDETAILDATA",
		REVIEWDATA	:	"SESSION_STORAGE_POIREVIEWDATA",
		REVIEWOPTIONS : "SESSION_STORAGE_POIREVIEWOPTIONS"
};
function PoiCache() {
}

PoiCache.prototype = {  
	/*
	setPoiDetailCache : function(data)
	{
		sessionStorage.setItem(this.getPoiDetailCacheKey(),data);
	},
	
	getPoiDetailCache : function()
	{
		return sessionStorage.getItem(this.getPoiDetailCacheKey());
	},

	getPoiDetailCacheKey : function()
	{
		return "SESSION_STORAGE_POIDETAILDATA_" + $("#poikey").val();
	},
	
	setSimplePoiDetailCache : function(data)
	{
		sessionStorage.setItem(this.getSimplePoiDetailCacheKey(),data);
	},

	getSimplePoiDetailCache : function()
	{
		return sessionStorage.getItem(this.getSimplePoiDetailCacheKey());
	},

	getSimplePoiDetailCacheKey : function()
	{
		return "SESSION_STORAGE_SIMPLE_POIDETAILDATA";
	},*/
	
	setLogoImageCache : function(logoImageName,data)
	{
		localStorage.setItem(this.getLogoImageCacheKey(logoImageName),data);
	},

	getLogoImageCache : function(logoImageName)
	{
		return localStorage.getItem(this.getLogoImageCacheKey(logoImageName));
	},

	getLogoImageCacheKey : function(logoImageName)
	{
		return "LOCAL_STORAGE_LOGOIMAGE_" + logoImageName;
	},
	
	/*
	setPoiReviewCache : function(data)
	{
		sessionStorage.setItem(this.getPoiReviewCacheKey(),data);
	},
	
	getPoiReviewCache : function()
	{
		return sessionStorage.getItem(this.getPoiReviewCacheKey());
	},
	
	removePoiReviewCache : function()
	{
		return sessionStorage.removeItem(this.getPoiReviewCacheKey());
	},


	getPoiReviewCacheKey : function()
	{
		return "SESSION_STORAGE_POIREVIEWDATA_" + $("#poikey").val();
	},*/
	
	setPoiMainCache : function(data)
	{
		sessionStorage.setItem(this.getPoiMainCacheKey(),data);
	},
	
	getPoiMainCache : function()
	{
		return sessionStorage.getItem(this.getPoiMainCacheKey());
	},
	
	removePoiMainCache : function()
	{
		return sessionStorage.removeItem(this.getPoiMainCacheKey());
	},

	getPoiMainCacheKey : function()
	{
		return "SESSION_STORAGE_POIDATA_MAIN_" + $("#poikey").val();
	},

	setPoiMenuCache : function(data)
	{
		sessionStorage.setItem(this.getPoiMenuCacheKey(),data);
	},
	
	getPoiMenuCache : function()
	{
		return sessionStorage.getItem(this.getPoiMenuCacheKey());
	},
	
	removePoiMenuCache : function()
	{
		return sessionStorage.removeItem(this.getPoiMenuCacheKey());
	},

	getPoiMenuCacheKey : function()
	{
		return "SESSION_STORAGE_POIDATA_MENU_" + $("#poikey").val();
	},

	setGasByPriceCache : function(data)
	{
		sessionStorage.setItem(this.getGasByPriceCacheKey(),data);
	},
	
	getGasByPriceCache : function()
	{
		return sessionStorage.getItem(this.getGasByPriceCacheKey());
	},
	
	removeGasByPriceCache : function()
	{
		return sessionStorage.removeItem(this.getGasByPriceCacheKey());
	},

	getGasByPriceCacheKey : function()
	{
		return "SESSION_STORAGE_POIDATA_GASBYPRICE_" + $("#poikey").val();
	},
	
	setPoiDealCache : function(data){
		sessionStorage.setItem(this.getPoiDealCacheKey(),data);
	},
	
	getPoiDealCache : function(){
		return sessionStorage.getItem(this.getPoiDealCacheKey());
	},
	
	removePoiDealCache : function()
	{
		return sessionStorage.removeItem(this.getPoiDealCacheKey());
	},

	getPoiDealCacheKey : function(){
		return "SESSION_STORAGE_POIDATA_DEAL_" + $("#poikey").val();
	},
	
	setPoiExtraCache : function(data)
	{
		sessionStorage.setItem(this.getPoiExtraCacheKey(),data);
	},
	
	getPoiExtraCache : function()
	{
		return sessionStorage.getItem(this.getPoiExtraCacheKey());
	},
	
	removePoiExtraCache : function()
	{
		return sessionStorage.removeItem(this.getPoiExtraCacheKey());
	},
	
	getPoiExtraCacheKey : function()
	{
		return "SESSION_STORAGE_POIDATA_EXTRA_" + $("#poikey").val();
	},
	
	setCurrentReviewer : function(data)
	{
		sessionStorage.setItem(this.getCurrentReviewerCacheKey(),data);
	},
	
	removeCurrentReviewerCache : function()
	{
		return sessionStorage.removeItem(this.getCurrentReviewerCacheKey());
	},

	getCurrentReviewer : function()
	{
		return sessionStorage.getItem(this.getCurrentReviewerCacheKey());
	},

	getCurrentReviewerCacheKey : function()
	{
		return "SESSION_STORAGE_REVIEWE_CURRENTREVIEWER";
	},
	
	setNickName : function(data)
	{
		sessionStorage.setItem(this.getNickNameCacheKey(),data);
	},
	
	getNickName : function()
	{
		return sessionStorage.getItem(this.getNickNameCacheKey());
	},

	getNickNameCacheKey : function()
	{
		return "SESSIONSTORAGE_STORAGE_NICKNAME";
	},
	
	getDummyPoiDetail : function()
	{
		//For Poi Dummy     [Reviews]  [menus] [extra]
		var dummyPoiDetail="";
		var poi = document.getElementById("dummyTemplate").value;
		var ajxUrl = GLOBAL_hostUrl + "dummyDataAction.do?poi=" + poi;
		
		var ajaxOptions = {
				data:0,
				url:ajxUrl,
				isAsynchronous:false
		};
		ajaxOptions.onSuccess = function(responseText){
			dummyPoiDetail = responseText;
		};
		CommonUtil.ajax(ajaxOptions);
		
		return dummyPoiDetail;
	},
	
	getReviewStatusCacheKey : function()
	{
		return "SESSIONSTORAGE_STORAGE_REVIEWSTATUS_" + $("#poikey").val();
	},
	
	getReviewStatus : function(){
	    return sessionStorage.getItem(this.getReviewStatusCacheKey());
	},
	
	setReviewStatusAsView : function(){
		this.setReviewStatus("VIEW");
	},
	
	setReviewStatus : function(status){
		sessionStorage.setItem(this.getReviewStatusCacheKey(),status);
	},
	
	setPoiReviewCacheKeyForAddReview : function(){
		sessionStorage.setItem("SESSIONSTORAGE_STORAGE_POI_REVIEW_CACHEKEY_FOR_ADDREVIEW",appendPoiKey(PoiCacheKeys.REVIEWDATA));
	},
	
	getPoiReviewCacheKeyForAddReview : function(){
		return sessionStorage.getItem("SESSIONSTORAGE_STORAGE_POI_REVIEW_CACHEKEY_FOR_ADDREVIEW");
	},
	
	setPoiReviewOptionCache : function(data){
		sessionStorage.setItem(PoiCacheKeys.REVIEWOPTIONS,data);
	},
	
	getPoiReviewOptionCache : function(){
		return sessionStorage.getItem(PoiCacheKeys.REVIEWOPTIONS);
	},
	
	removePoiReviewCacheKeyForAddReview : function(){
		sessionStorage.removeItem("SESSIONSTORAGE_STORAGE_POI_REVIEW_CACHEKEY_FOR_ADDREVIEW");
	},
	
	setPoiReviewCacheForAddReview : function(data){
		sessionStorage.setItem(this.getPoiReviewCacheKeyForAddReview(),data);
	},
	
	setMapFlag : function(data)
	{	
		sessionStorage.setItem(this.getMapFlagCacheKey(),data);
	},
	
	removeMapFlagCache : function()
	{
		return sessionStorage.removeItem(this.getMapFlagCacheKey());
	},
	
	getMapFlag : function()
	{
		return sessionStorage.getItem(this.getMapFlagCacheKey());
	},

	getMapFlagCacheKey : function()
	{
		return "SESSIONSTORAGE_STORAGE_MAPFLAG_" + $("#poikey").val();
	},
	
	setBizHourFlag : function(data)
	{	
		sessionStorage.setItem(this.getBizHourFlagCacheKey(),data);
	},
	
	removeBizHourFlagCache : function()
	{
		return sessionStorage.removeItem(this.getBizHourFlagCacheKey());
	},
	
	getBizHourFlag : function()
	{
		return sessionStorage.getItem(this.getBizHourFlagCacheKey());
	},

	getBizHourFlagCacheKey : function()
	{
		return "SESSIONSTORAGE_STORAGE_BIZHOURFLAG_" + $("#poikey").val();
	}	
};

var PoiCacheHelper = new PoiCache();


