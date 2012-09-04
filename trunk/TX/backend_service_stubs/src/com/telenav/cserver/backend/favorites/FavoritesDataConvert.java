/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.favorites;



import com.telenav.cserver.backend.cose.PoiSearchConverter;
import com.telenav.cserver.backend.datatypes.Address;
import com.telenav.cserver.backend.datatypes.TnPoi;
import com.telenav.cserver.backend.datatypes.favorites.Favorite;
import com.telenav.cserver.backend.datatypes.favorites.FavoriteCategory;
import com.telenav.cserver.backend.datatypes.favorites.FavoriteMapping;
import com.telenav.cserver.backend.recentstop.RecentStopsServiceProxy;
import com.telenav.datatypes.favorite.v10.FavoriteAddress;
import com.telenav.datatypes.favorite.v10.FavoritePoi;
import com.telenav.kernel.ws.axis2.Axis2Helper;
import com.telenav.services.favorite.v10.AllRequestDTO;
import com.telenav.services.favorite.v10.AllResponseDTO;
import com.telenav.services.favorite.v10.BasicRequestDTO;
import com.telenav.services.favorite.v10.CountResponseDTO;
import com.telenav.services.favorite.v10.FavoriteMappingResponseDTO;
import com.telenav.services.favorite.v10.FavoritesAndCategoriesRequestDTO;
import com.telenav.ws.datatypes.services.mgmt.ServiceMgmtResponseDTO;

/**
 * Convert utility
 * @author zhjdou
 * 2010-2-21
 */
public class FavoritesDataConvert {
    
	
    /**
     * convert the confirm request to backend one
     * @param proxy
     * @return
     */
    protected static FavoritesAndCategoriesRequestDTO convertConfirmReuqestProxy2ConfirmRequest(FavoriteConfirmRequest proxy)
    {
        FavoritesAndCategoriesRequestDTO request=new FavoritesAndCategoriesRequestDTO();
        request.setAddedfavorites(convertArrayProxy2Fav(proxy.getAddFavorites()));
        request.setDeletedfavorites(convertArrayProxy2Fav(proxy.getDeleteFavorites()));
        request.setAddedcategories(convertArrayProxy2FavCategory(proxy.getAddCategory()));
        request.setDeletedcategories(convertArrayProxy2FavCategory(proxy.getDeleteCategory()));
        request.setUserId(proxy.getUserId());
        request.setClientName(RecentStopsServiceProxy.clientName);
        request.setClientVersion(RecentStopsServiceProxy.clientVersion);
        request .setTransactionId(RecentStopsServiceProxy.transactionId);
        request.setContextString(proxy.getContextString());
        return request;
    }
    
    
    /**
     * recored response status code and message
     * @param response
     * @return
     */
    protected static FavoriteConfirmResponse convertConfirmResponse2ProxyResponse(ServiceMgmtResponseDTO response)
    {
        if (response == null)
        {
            return null;
        }
        FavoriteConfirmResponse proxy = new FavoriteConfirmResponse();
        proxy.setStatus_code(response.getResponseStatus().getStatusCode());
        proxy.setStatus_message(response.getResponseStatus().getStatusMessage());
        return proxy;
    }
    
	/**
	 * Convert the response format
	 * @param fResponse
	 * @return
	 */
	public static SyncFavoritesResponse convertSyncFavoritesResponse2Proxy(AllResponseDTO sResponse){
		if(sResponse!=null) {
			SyncFavoritesResponse proxy=new SyncFavoritesResponse();
		    proxy.setStatus_code(sResponse.getResponseStatus().getStatusCode());
		    proxy.setStatus_message(sResponse.getResponseStatus().getStatusMessage());
		    proxy.setAddFavorites(convertArrayFavs2Proxy(sResponse.getAddedFavorites()));
		    proxy.setDeleteFavorites(convertArrayFavs2Proxy(sResponse.getDeletedFavorites()));
		    proxy.setAddCategory(convertArrayCategory2Proxy(sResponse.getAddedCategories()));
		    proxy.setDeleteCategory(convertArrayCategory2Proxy(sResponse.getDeletedCategories()));
		    proxy.setMapping(convertMapping2Proxy(sResponse.getMapping()));
		    return proxy;
		}
		return null;
	}
	
	
	/**
	 * Convert the query favourites request to WS type.
	 * @param proxy
	 * @return
	 */
	public static AllRequestDTO convertProxy2SyncFavoritesRequest(SyncFavoritesRequest proxy) {
		if(proxy!=null) {
			AllRequestDTO req=new AllRequestDTO();
		    req.setAddedFavorites(convertArrayProxy2Fav(proxy.getAddFavorites()));
		    req.setUpdatedFavorites(convertArrayProxy2Fav(proxy.getUpdateFavorites()));
		    req.setDeletedFavorites(convertArrayProxy2Fav(proxy.getDeleteFavorites()));
		    req.setAddedCategories(convertArrayProxy2FavCategory(proxy.getAddCategory()));
		    req.setUpdatedCategories(convertArrayProxy2FavCategory(proxy.getUpdateCategory()));
		    req.setDeletedCategories(convertArrayProxy2FavCategory(proxy.getDeleteCategory()));
		    req.setMapping(convertArrayProxy2Mapping(proxy.getMapping()));
		    req.setClientName(FavoritesServiceProxy.clientName);
		    req.setClientVersion(FavoritesServiceProxy.clientVersion);
		    req.setTransactionId(FavoritesServiceProxy.transactionId);
		    req.setUserId(proxy.getUserId());
		    req.setNeedPoiInfo(isNeedPOI(proxy));
		    return req;
		}
		return null;
	}
	
	 /**
     * Set the flag whether need poi that depend on the stops in add/update list type
     * @param request
     */
    private static boolean isNeedPOI(SyncFavoritesRequest request) {
        Favorite[] addedStops=request.getAddFavorites();
        Favorite[] updateStop=request.getUpdateFavorites();
        if (addedStops != null)
        {
            for (int index = 0; index < addedStops.length; index++)
            {
                if (addedStops[index].getFavoritePoi()!=null && addedStops[index].getFavoritePoi().getPoiId()>0)
                {// flag
                    return true;
                }
            }
        }
 
        if (updateStop != null)
        {
           for (int index = 0; index < updateStop.length; index++)
           {
               if (updateStop[index].getFavoritePoi()!=null && updateStop[index].getFavoritePoi().getPoiId()>0)
               {
                  return true;
               }
          }
       }
        return false;
    }
	
	/**
	 * Convert array category to c-server data type
	 * @param favorites
	 * @return
	 */
	public static com.telenav.datatypes.favorite.v10.FavoriteMapping[] convertArrayProxy2Mapping(FavoriteMapping[] proxy){
		 if (proxy != null && Axis2Helper.isNonEmptyArray(proxy)){
	            int length = proxy.length;
	            com.telenav.datatypes.favorite.v10.FavoriteMapping[] arrMap = new com.telenav.datatypes.favorite.v10.FavoriteMapping[length];
	            for (int index = 0; index < length; index++){
	            	arrMap[index] = convertProxy2Mapping(proxy[index]);
	            }
	            return arrMap;
	        }
	        return null;
	}
	
	/**
	 * Convert mapping to c-server data type
	 * @param favorite
	 * @return
	 */
	public static com.telenav.datatypes.favorite.v10.FavoriteMapping convertProxy2Mapping(FavoriteMapping proxy){
		if(proxy!=null){
			com.telenav.datatypes.favorite.v10.FavoriteMapping mapping=new com.telenav.datatypes.favorite.v10.FavoriteMapping();
			mapping.setFavorite(convertProxy2Favorite(proxy.getFavorite()));
			mapping.setCategories(convertArrayProxy2FavCategory(proxy.getCategories()));
			return mapping;
		}
		return null;
	}
	
	/**
	 * Convert array Category proxy to WS format
	 * @param favorites
	 * @return
	 */
	public static com.telenav.datatypes.favorite.v10.FavoriteCategory[] convertArrayProxy2FavCategory(FavoriteCategory[] proxy){
		 if (proxy != null && Axis2Helper.isNonEmptyArray(proxy)){
	            int length = proxy.length;
	            com.telenav.datatypes.favorite.v10.FavoriteCategory[] arrCates = new com.telenav.datatypes.favorite.v10.FavoriteCategory[length];
	            for (int index = 0; index < length; index++){
	            	arrCates[index] = convertProxy2Category(proxy[index]);
	            }
	            return arrCates;
	        }
	        return null;
	}
	
	
	/**
	 * Convert Category to c-server data type
	 * @param favorite
	 * @return
	 */
	public static com.telenav.datatypes.favorite.v10.FavoriteCategory convertProxy2Category(FavoriteCategory proxy){
		if(proxy!=null){
			com.telenav.datatypes.favorite.v10.FavoriteCategory category=new com.telenav.datatypes.favorite.v10.FavoriteCategory();
			category.setCategoryId(proxy.getCategoryId());
			category.setCategoryName(proxy.getCategoryName());
			category.setCategoryType(proxy.getCategoryType());
			category.setUserId(proxy.getUserId());
			return category;
		}
		return null;
	}
	
	/**
	 * Convert array favourite proxy to WS format
	 * @param favorites
	 * @return
	 */
	public static com.telenav.datatypes.favorite.v10.Favorite[] convertArrayProxy2Fav(Favorite[] proxy){
		 if (proxy != null && Axis2Helper.isNonEmptyArray(proxy)){
	            int length = proxy.length;
	            com.telenav.datatypes.favorite.v10.Favorite[] arrFavs = new com.telenav.datatypes.favorite.v10.Favorite[length];
	            for (int index = 0; index < length; index++){
	            	arrFavs[index] = convertProxy2Favorite(proxy[index]);
	            }
	            return arrFavs;
	        }
	        return null;
	}
	
	
	/**
	 * Convert favourites to c-server data type
	 * @param favorite
	 * @return
	 */
	public static com.telenav.datatypes.favorite.v10.Favorite convertProxy2Favorite(Favorite proxy){
		if(proxy!=null){
			com.telenav.datatypes.favorite.v10.Favorite fav=new com.telenav.datatypes.favorite.v10.Favorite();
			fav.setFavoriteId(proxy.getFavoriteId());
			fav.setSenderName(proxy.getSenderName());
			fav.setSenderPtn(proxy.getSenderPtn());
			fav.setUserId(proxy.getUserId());
			fav.setFavoriteAddress(addressToWSAddress(proxy.getFavoriteAddress()));
			fav.setFavoritePoi(convertTnPoiToWSPoi(proxy.getFavoritePoi()));
			return fav;
		}
		return null;
	}
	
	/**
	 * Convert the address to ws favorite address
	 * @param addr
	 * @return
	 */
	public static FavoriteAddress addressToWSAddress(Address addr)
	{   
		
		if(addr != null)
		{   
			FavoriteAddress address = new FavoriteAddress();
			address.setCity(addr.getCityName());
			address.setCountry(addr.getCountry());
			address.setCounty(addr.getCounty());
			address.setFirstLine(addr.getFirstLine());
			address.setCrossStreetName(addr.getCrossStreetName());
			//the label of address is the key in favourite module
			address.setLabel(addr.getLabel());
			com.telenav.ws.datatypes.address.GeoCode geoCode = new com.telenav.ws.datatypes.address.GeoCode();
			geoCode.setLatitude(addr.getLatitude());
			geoCode.setLongitude(addr.getLongitude());
			address.setGeoCode(geoCode);
			
			address.setLastLine(addr.getLastLine());
			address.setPostalCode(addr.getPostalCode());
			address.setState(addr.getState());
			address.setStreetName(addr.getStreetName());
			return address;
		}
		return null;
	}
	
	/**
	 * this method also used in favourites module, if change it please very careful.
	 * @param poi
	 * @return
	 */
	public static FavoritePoi convertTnPoiToWSPoi(TnPoi poi)
	{
		if(poi == null)
		{
			return null;
		}
		FavoritePoi tnPoi = new FavoritePoi();
		tnPoi.setPoiId(poi.getPoiId());
		tnPoi.setBasePoiId(poi.getBasePoiId());
		tnPoi.setAddress(PoiSearchConverter.addressToWSAddress(poi.getAddress()));
		tnPoi.setBasePoiId(poi.getBasePoiId());
		tnPoi.setBrandName(poi.getBrandName());
		tnPoi.setEditor(poi.getEditor());
		tnPoi.setFlagInfo(poi.getFlagInfo());
		tnPoi.setVendor(poi.getVendor());
		tnPoi.setPhoneNumber(poi.getPhoneNumber());
		tnPoi.setIsNavigable(poi.isNavigable());
		tnPoi.setIsRatingEnable(poi.isRatingEnable());
		tnPoi.setPoiType(poi.getPoiType());
		return tnPoi;
	}
	
	
	/**
	 * Convert the query favourites request to WS type.
	 * @param proxy
	 * @return
	 */
	public static BasicRequestDTO convertProxy2FavoritesRequest(QueryFavoritesRequest proxy) {
		if(proxy!=null) {
		    BasicRequestDTO req=new BasicRequestDTO();
		    req.setUserId(proxy.getUserId());
		    req.setClientName(FavoritesServiceProxy.clientName);
		    req.setClientVersion(FavoritesServiceProxy.clientVersion);
		    req.setTransactionId(FavoritesServiceProxy.transactionId);
		    return req;
		}
		return null;
	}
	
	/**
     * Convert the query favourites request to WS type.
     * @param proxy
     * @return
     */
    public static BasicRequestDTO convertProxy2CountFavoritesRequest(CountReceivedAddressRequest proxy) {
        if(proxy!=null) {
            BasicRequestDTO req=new BasicRequestDTO();
            req.setUserId(proxy.getUserId());
            req.setClientName(FavoritesServiceProxy.clientName);
            req.setClientVersion(FavoritesServiceProxy.clientVersion);
            req.setTransactionId(FavoritesServiceProxy.transactionId);
            return req;
        }
        return null;
    }
	
	/**
	 * Convert the response format
	 * @param fResponse
	 * @return
	 */
	public static QueryFavoritesResponse convertFavoritesResponse2Proxy(FavoriteMappingResponseDTO fResponse){
		if(fResponse!=null) {
		    QueryFavoritesResponse proxy=new QueryFavoritesResponse();
		    proxy.setStatus_code(fResponse.getResponseStatus().getStatusCode());
		    proxy.setStatus_message(fResponse.getResponseStatus().getStatusMessage());
		    proxy.setFavorites(convertArrayFavs2Proxy(fResponse.getFavorites()));
		    proxy.setCategories(convertArrayCategory2Proxy(fResponse.getCategories()));
		    proxy.setMapping(convertMapping2Proxy(fResponse.getMapping()));
		    return proxy;
		}
		return null;
	}
	
	/**
     * Convert the response format
     * @param fResponse
     * @return
     */
    public static CountReceivedAddressResponse convertCountFavoritesResponse2Proxy(CountResponseDTO fResponse){
        if(fResponse!=null) {
            CountReceivedAddressResponse proxy=new CountReceivedAddressResponse();
            proxy.setStatus_code(fResponse.getResponseStatus().getStatusCode());
            proxy.setStatus_message(fResponse.getResponseStatus().getStatusMessage());
            proxy.setCount(fResponse.getCount());
            return proxy;
        }
        return null;
    }
	
	/**
	 * Convert array category to c-server data type
	 * @param favorites
	 * @return
	 */
	public static FavoriteMapping[] convertMapping2Proxy(com.telenav.datatypes.favorite.v10.FavoriteMapping[] mapppings){
		 if (mapppings != null && Axis2Helper.isNonEmptyArray(mapppings)){
	            int length = mapppings.length;
	            FavoriteMapping[] arrMap = new FavoriteMapping[length];
	            for (int index = 0; index < length; index++){
	            	arrMap[index] = convertMapping2Proxy(mapppings[index]);
	            }
	            return arrMap;
	        }
	        return null;
	}
	
	/**
	 * Convert mapping to c-server data type
	 * @param favorite
	 * @return
	 */
	public static FavoriteMapping convertMapping2Proxy(com.telenav.datatypes.favorite.v10.FavoriteMapping mapping){
		if(mapping!=null){
			FavoriteMapping proxy=new FavoriteMapping();
			proxy.setFavorite(convertFavorites2Proxy(mapping.getFavorite()));
			proxy.setCategories(convertArrayCategory2Proxy(mapping.getCategories()));
			return proxy;
		}
		return null;
	}
	
	
	
	/**
	 * Convert array mapping to c-server data type
	 * @param favorites
	 * @return
	 */
	public static FavoriteCategory[] convertArrayCategory2Proxy(com.telenav.datatypes.favorite.v10.FavoriteCategory[] category){
		 if (category != null && Axis2Helper.isNonEmptyArray(category)){
	            int length = category.length;
	            FavoriteCategory[] arrCate = new FavoriteCategory[length];
	            for (int index = 0; index < length; index++){
	            	arrCate[index] = convertCategories2Proxy(category[index]);
	            }
	            return arrCate;
	        }
	        return null;
	}
	
	/**
	 * Convert category to c-server data type
	 * @param favorite
	 * @return
	 */
	public static FavoriteCategory convertCategories2Proxy(com.telenav.datatypes.favorite.v10.FavoriteCategory category){
		if(category!=null){
			FavoriteCategory proxy=new FavoriteCategory();
			proxy.setCategoryId(category.getCategoryId());
			proxy.setCategoryName(category.getCategoryName());
			proxy.setCategoryType(category.getCategoryType());
			proxy.setParentId(category.getParentId());
			proxy.setUserId(category.getUserId());
			return proxy;
		}
		return null;
	}
	
	
	
	/**
	 * Convert array favourites to c-server data type
	 * @param favorites
	 * @return
	 */
	public static Favorite[] convertArrayFavs2Proxy(com.telenav.datatypes.favorite.v10.Favorite[] favorites){
		 if (favorites != null && Axis2Helper.isNonEmptyArray(favorites)){
	            int length = favorites.length;
	            Favorite[] arrFavs = new Favorite[length];
	            for (int index = 0; index < length; index++){
	            	arrFavs[index] = convertFavorites2Proxy(favorites[index]);
	            }
	            return arrFavs;
	        }
	        return null;
	}
	
	
	/**
	 * Convert favourites to c-server data type
	 * @param favorite
	 * @return
	 */
	public static Favorite convertFavorites2Proxy(com.telenav.datatypes.favorite.v10.Favorite favorite){
		if(favorite!=null){
			Favorite proxy=new Favorite();
			proxy.setFavoriteId(favorite.getFavoriteId());
			proxy.setSenderName(favorite.getSenderName());
			proxy.setSenderPtn(favorite.getSenderPtn());
			proxy.setUserId(favorite.getUserId());
			proxy.setPOI(favorite.getFavoritePoi().getPoiId()>0 ?true : false);
			proxy.setFavoriteAddress(PoiSearchConverter.wsAddressToAddress(favorite.getFavoriteAddress()));
			proxy.setFavoritePoi(PoiSearchConverter.convertWSPoiToTnPoi(favorite.getFavoritePoi()));
			if(proxy.getFavoritePoi()!=null) {
			    proxy.getFavoritePoi().setAddress(PoiSearchConverter.wsAddressToAddress(favorite.getFavoriteAddress()));
			}
			return proxy;
		}
		return null;
	}
	
}
