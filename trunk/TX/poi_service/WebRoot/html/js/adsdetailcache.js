function PoiCache() {

}

PoiCache.prototype = {
		 getAdsDetailCache : function(){
			return sessionStorage.getItem(this.getAdsDetailCacheKey());
		},

		getAdsDetailCacheKey : function (){
			return "SESSION_STORAGE_ADSDETAILDATA_" + $("#adId").val();
		},

		setAdsDetailCache : function (adsDetailObj){
			sessionStorage.setItem(this.getAdsDetailCacheKey(),adsDetailObj);
		},

		removeAdsDetailCache : function ()
		{
			return sessionStorage.removeItem(this.getAdsDetailCacheKey());
		},

		setAdsIdCache : function(data){
			localStorage.setItem(this.getAdsIdCacheKey(),data);
		},

		getAdsIdCacheKey : function(){
			return "LOCAL_STORAGE_ADS_ID";
		},

		getAdsIdCache  : function(){
			return localStorage.getItem(this.getAdsIdCacheKey());
		},

		cleanAdsIdCache  : function(){
			return localStorage.removeItem(this.getAdsIdCacheKey());
		},

		setDistanceTextCacheForAds  : function(data){
			localStorage.setItem(this.getDistanceTextCacheKeyForAds(),data);
		},

		getDistanceTextCacheForAds  : function(){
			return localStorage.getItem(this.getDistanceTextCacheKeyForAds());
		},

		getDistanceTextCacheKeyForAds  : function(){
			return "LOCAL_STORAGE_DISTANCE_TEXT_ADS";
		},

		removeDistanceTextCacheForAds  : function(){
			localStorage.removeItem(this.getDistanceTextCacheKeyForAds());
		},

		setOpenCloseFlag  : function(data)
		{
			sessionStorage.setItem(this.getOpenCloseFlagCacheKey(),data);
		},

		getOpenCloseFlag : function()
		{
			return sessionStorage.getItem(this.getOpenCloseFlagCacheKey());
		},

		getOpenCloseFlagCacheKey : function()
		{
			return "SESSIONSTORAGE_STORAGE_ADS_OPENCLOSE";
		},
		
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
			return "SESSION_STORAGE_ADSDATA_MENU";
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
			return "SESSION_STORAGE_ADSDATA_DEAL";
		}
};
var PoiCacheHelper = new PoiCache();