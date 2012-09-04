/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.favoritesV40;

import com.telenav.datatypes.services.v20.ServiceRequest;
import com.telenav.cserver.backend.cose.PoiSearchConverterV20;
import com.telenav.cserver.backend.cose.PoiSearchConverter;
import com.telenav.cserver.backend.datatypes.Address;
import com.telenav.cserver.backend.datatypes.AddressDataConverter;
import com.telenav.cserver.backend.datatypes.TnPoi;
import com.telenav.cserver.backend.datatypes.favoritesV40.Favorite;
import com.telenav.cserver.backend.datatypes.favoritesV40.FavoriteCategory;
import com.telenav.cserver.backend.datatypes.favoritesV40.FavoriteMapping;
import com.telenav.cserver.backend.recentstopV40.RecentStopsServiceProxy;
import com.telenav.datatypes.favorite.v10.FavoritePoi;
import com.telenav.kernel.ws.axis2.Axis2Helper;
import com.telenav.services.favorite.v20.AllRequestDTO;
import com.telenav.services.favorite.v20.AllResponseDTO;
import com.telenav.services.favorite.v20.BasicRequestDTO;
import com.telenav.services.favorite.v20.CountResponseDTO;
import com.telenav.services.favorite.v20.FavoriteMappingResponseDTO;
import com.telenav.services.favorite.v20.FavoritesAndCategoriesRequestDTO;
import com.telenav.ws.datatypes.services.mgmt.ServiceMgmtResponseDTO;
import com.telenav.services.favorite.v20.BasicResponseDTO;


/**
 * Convert utility
 * @author zhjdou
 * 2010-2-21
 */
public class FavoritesDataConvert {
    
	
	public static void setCommonField(ServiceRequest s){
		
        s.setClientName(RecentStopsServiceProxy.clientName);
        s.setClientVersion(RecentStopsServiceProxy.clientVersion);
        s.setTransactionId(RecentStopsServiceProxy.transactionId);
	}
    /**
     * request converter, convert FavoriteConfirmRequest to FavoritesAndCategoriesRequestDTO
     * 		userId (long)
     * 		addedfavorites (favorite20:Favorite)
     * 		deletedfavorites (favorite20:Favorite)
     *      addedcategories (favorite20:FavoriteCategory)
     *      deletedcategories (favorite20:FavoriteCategory)
     *      
     * convert the confirm request to backend one
     * @param proxy
     * @return
     */
    protected static FavoritesAndCategoriesRequestDTO convertConfirmReuqestProxy2ConfirmRequest(FavoriteConfirmRequest proxy)
    {
        FavoritesAndCategoriesRequestDTO request=new FavoritesAndCategoriesRequestDTO();
       
        // set commmon request

        FavoritesDataConvert.setCommonField(request);
        request.setContextString(proxy.getContextString());
        
        request.setUserId(proxy.getUserId());        

        request.setAddedfavorites(convertArrayProxy2Fav(proxy.getAddFavorites()));
        request.setDeletedfavorites(convertArrayProxy2Fav(proxy.getDeleteFavorites()));
        request.setAddedcategories(convertArrayProxy2FavCategory(proxy.getAddCategory()));
        request.setDeletedcategories(convertArrayProxy2FavCategory(proxy.getDeleteCategory()));

        
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
     * recored response status code and message
     * @param response
     * @return
     */
    protected static FavoriteConfirmResponse convertConfirmResponse2ProxyResponse(BasicResponseDTO response)
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
		
		SyncFavoritesResponse proxy = null;
		
		if(sResponse!=null) {
			proxy=new SyncFavoritesResponse();
		    proxy.setStatus_code(sResponse.getResponseStatus().getStatusCode());
		    proxy.setStatus_message(sResponse.getResponseStatus().getStatusMessage());
		    
	    	proxy.setAddFavorites(convertArrayFavs2Proxy(sResponse.getAddedFavorites()));
		    proxy.setDeleteFavorites(convertArrayFavs2Proxy(sResponse.getDeletedFavorites()));
		    proxy.setAddCategory(convertArrayCategory2Proxy(sResponse.getAddedCategories()));
		    proxy.setDeleteCategory(convertArrayCategory2Proxy(sResponse.getDeletedCategories()));
		    proxy.setMapping(convertMapping2Proxy(sResponse.getMapping()));

		    
		}
		return proxy;
	}
	
	
	/**
	 * request converter, convert SyncFavoritesRequest to AllRequestDTO
	 * 		userId (long)
	 * 		addedFavorites (favorite20:Favorite)
	 * 		deletedFavorites (favorite20:Favorite)
	 * 		updatedFavorites (favorite20:Favorite)
	 * 		addedCategories (favorite20:FavoriteCategory)
	 * 		deletedCategories (favorite20:FavoriteCategory)
	 * 		updatedCategories  (favorite20:FavoriteCategory)
	 * 		mapping (favorite20:FavoriteMapping)
	 * 		needPoiInfo (boolean)
	 * 
	 *   
	 * Convert the query favourites request to WS type.
	 * @param proxy
	 * @return
	 */
	public static AllRequestDTO convertProxy2SyncFavoritesRequest(SyncFavoritesRequest proxy) {
		
		AllRequestDTO request = new AllRequestDTO();
		
		if(proxy!=null) {
			request = new AllRequestDTO();
			
	        FavoritesDataConvert.setCommonField(request);
			request.setUserId(proxy.getUserId());			
		    request.setMapping(convertArrayProxy2Mapping(proxy.getMapping()));	
		    
		    
			request.setAddedCategories(convertArrayProxy2FavCategory(proxy.getAddCategory()));
		 	request.setUpdatedFavorites(convertArrayProxy2Fav(proxy.getUpdateFavorites()));
		    request.setDeletedFavorites(convertArrayProxy2Fav(proxy.getDeleteFavorites()));
		    
			request.setAddedFavorites(convertArrayProxy2Fav(proxy.getAddFavorites()));
		    request.setUpdatedCategories(convertArrayProxy2FavCategory(proxy.getUpdateCategory()));
		    request.setDeletedCategories(convertArrayProxy2FavCategory(proxy.getDeleteCategory()));
		    
			request.setNeedPoiInfo(isNeedPOI(proxy));

		}
		return request;
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
	public static com.telenav.datatypes.favorite.v20.FavoriteMapping[] convertArrayProxy2Mapping(FavoriteMapping[] proxy){
		
		com.telenav.datatypes.favorite.v20.FavoriteMapping[] arrMap = null;
		
		 if (proxy != null && Axis2Helper.isNonEmptyArray(proxy)){
	            
			 	int length = proxy.length;
	            arrMap = new com.telenav.datatypes.favorite.v20.FavoriteMapping[length];
	            
	            for (int index = 0; index < length; index++){
	            	arrMap[index] = convertProxy2Mapping(proxy[index]);
	            }
	        }
	        return arrMap;
	}
	
	/**
	 * Convert mapping to c-server data type
	 * @param favorite
	 * @return
	 */
	public static com.telenav.datatypes.favorite.v20.FavoriteMapping convertProxy2Mapping(FavoriteMapping proxy){
		
		com.telenav.datatypes.favorite.v20.FavoriteMapping mapping = null;
		 
		if(proxy!=null){
			
			mapping=new com.telenav.datatypes.favorite.v20.FavoriteMapping();
			
			mapping.setFavorite(convertProxy2Favorite(proxy.getFavorite()));
			mapping.setCategories(convertArrayProxy2FavCategory(proxy.getCategories()));
		}
		
		return mapping;
	}
	
	/**
	 * Convert array Category proxy to WS format
	 * @param favorites
	 * @return
	 */
	public static com.telenav.datatypes.favorite.v20.FavoriteCategory[] convertArrayProxy2FavCategory(FavoriteCategory[] proxy){
		
		com.telenav.datatypes.favorite.v20.FavoriteCategory[] arrCates = null;
				
		 if (proxy != null && Axis2Helper.isNonEmptyArray(proxy)){
	            int length = proxy.length;
	            arrCates = new com.telenav.datatypes.favorite.v20.FavoriteCategory[length];
	            
	            for (int index = 0; index < length; index++){
	            	arrCates[index] = convertProxy2Category(proxy[index]);
	            }
	        }
	        return arrCates;
	}
	
	
	/**	 
	 * Convert Category to c-server data type
	 * @param favorite
	 * @return
	 */
	public static com.telenav.datatypes.favorite.v20.FavoriteCategory convertProxy2Category(FavoriteCategory proxy){
		
		com.telenav.datatypes.favorite.v20.FavoriteCategory category = null;
		
		if(proxy!=null){
			
			category=new com.telenav.datatypes.favorite.v20.FavoriteCategory();
		
			category.setCategoryName(proxy.getCategoryName());
			category.setParentId(proxy.getParentId());
			category.setUserId(proxy.getUserId());			
			category.setCategoryType(proxy.getCategoryType());
			category.setCategoryId(proxy.getCategoryId());

		}
		return category;
	}
	
	/**
	 * Convert array favourite proxy to WS format
	 * @param favorites
	 * @return
	 */
	public static com.telenav.datatypes.favorite.v20.Favorite[] convertArrayProxy2Fav(Favorite[] proxy){
		
		com.telenav.datatypes.favorite.v20.Favorite[] arrFavs = null;
		
		 if (proxy != null && Axis2Helper.isNonEmptyArray(proxy)){
	            int length = proxy.length;
	            arrFavs = new com.telenav.datatypes.favorite.v20.Favorite[length];
	            
	            for (int index = 0; index < length; index++){
	            	arrFavs[index] = convertProxy2Favorite(proxy[index]);
	            }

	        }
	        return arrFavs;
	}
	
	
	/**
	 * @param favorite
	 * @return
	 */
	public static com.telenav.datatypes.favorite.v20.Favorite convertProxy2Favorite(Favorite proxy){
		
		com.telenav.datatypes.favorite.v20.Favorite fav = null;
		
		if(proxy!=null){
			
			fav=new com.telenav.datatypes.favorite.v20.Favorite();
			
			fav.setFavoriteId(proxy.getFavoriteId());
			fav.setSenderName(proxy.getSenderName());
			fav.setSenderPtn(proxy.getSenderPtn());
			fav.setUserId(proxy.getUserId());
			fav.setSourceFlag(proxy.getSourceFlag());
			fav.setLifeCycle(proxy.getLifeCycle());
			fav.setEventId(proxy.getEventId());
			fav.setFavoritePoi(PoiSearchConverterV20.convertCSPoiToWSTnPoi(proxy.getFavoritePoi()));
			
			
		    com.telenav.datatypes.address.v40.Address address = new com.telenav.datatypes.address.v40.Address();
		    com.telenav.cserver.backend.datatypes.Address favoriteAddress = proxy.getFavoriteAddress();
		    AddressDataConverter.convertCSAddressToWSAddressV40(favoriteAddress, address);
		    if(address != null){
		    	fav.setFavoriteAddress(address);
		    }
		    
		    
		    /*
		     * set label,
		     * for poi_type favorite, get label from Favorite data field
		     * for address_type favorite, get label from Favorite.FavoriteAddress data field
		     */
		    
			if(proxy.isPOI()){
				fav.setLabel(proxy.getLabel());
			}else{
				if(proxy.getFavoriteAddress() != null && proxy.getFavoriteAddress().getLabel() != null)
				fav.setLabel(proxy.getFavoriteAddress().getLabel());
			}
			
		}
		return fav;
	}
	
	/**
	 * Convert the address to ws favorite address
	 * @param addr
	 * @return
	 */
/*	
	public static com.telenav.datatypes.address.v40.Address addressToWSAddress(Address addr)
	{   
		com.telenav.datatypes.address.v40.Address address = null;
		
		if(addr != null)
		{   
			address = new com.telenav.datatypes.address.v40.Address();
			
			String countryStr = addr.getCountry();
			com.telenav.datatypes.locale.v10.Country country = null;
			try{
				 country = com.telenav.datatypes.locale.v10.Country.Factory.fromValue(countryStr);
			}catch(Throwable e){
				
			}
			address.setCountry(country);
			
			address.setCity(addr.getCityName());
			address.setCounty(addr.getCounty());
			address.setAddressId(addr.getAddressId());			
	        address.setBuildingName(addr.getBuildingName());
	        
	        address.setHouseNumber("333 No.");                         // how to get house number?
	        address.setSuite(addr.getSuite());
	        
	        address.setLocality("fakeLocality");                        // how to set Locality, SubLocality
	        address.setSubLocality("fakeSubLocality");
	        
	        Locale locale = new Locale();
	        locale.setLanguage(Language.CHI);                           // how to get language
	        address.setLocale(locale);
	        
	        address.setPostalCode(addr.getPostalCode());
	        addr.setSubStreet(addr.getSubStreet());

	        Street crossStreet = new Street();                             // how to set crossStreet?
	        crossStreet.setName("Gubei Road");
	        crossStreet.setDirs(new String[] { "WestAAA", "NorthBBB" });
	        crossStreet.setType("WalkStreet");
	        crossStreet.setBody("testBody");
	        address.setCrossStreet(crossStreet);


	        Street mainStreet = new Street();                              // how to set mainStreet
	        mainStreet.setName("XianXia Road");
	        mainStreet.setDirs(new String[] { "EastAAA", "SouthBBB" });
	        mainStreet.setType("shopping street");
	        mainStreet.setBody("testBody");
	        address.setStreet(mainStreet);

	        GeoCoordinate geo = new GeoCoordinate();
	        geo.setLatitude(addr.getLatitude());
	        geo.setLongitude(addr.getLongitude());
	        address.setGeoCoordinate(geo);

			
		}
		return address;
	}
*/	
	
	/**
	 * this method also used in favourites module, if change it please very careful.
	 * @param poi
	 * @return
	 */
	public static com.telenav.datatypes.content.tnpoi.v20.TnPoi convertCSTnPoiToWSTnPoi(TnPoi poi)
	{
		com.telenav.datatypes.content.tnpoi.v20.TnPoi tnPoi = null;
		if(poi != null)
		{
			tnPoi = new com.telenav.datatypes.content.tnpoi.v20.TnPoi();
			
			tnPoi.setPoiId(poi.getPoiId());
			tnPoi.setBasePoiId(poi.getBasePoiId());
			
			com.telenav.datatypes.address.v40.Address address = new com.telenav.datatypes.address.v40.Address();
			if(address != null){
				tnPoi.setAddress(address);
			}
			
			tnPoi.setBasePoiId(poi.getBasePoiId());
			tnPoi.setBrandName(poi.getBrandName());
			tnPoi.setEditor(poi.getEditor());
			tnPoi.setVendor(poi.getVendor());
			tnPoi.setPhoneNumber(poi.getPhoneNumber());
			tnPoi.setIsNavigable(poi.isNavigable());
			tnPoi.setIsRatingEnable(poi.isRatingEnable());
			tnPoi.setPoiType(poi.getPoiType());	
		}
		
		return tnPoi;
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
		
		Address addr = poi.getAddress();
		com.telenav.ws.datatypes.address.Address targetAddr = AddressDataConverter.convertAddressToWSAddress(addr);
		if(targetAddr != null){
			tnPoi.setAddress(targetAddr);
		}
		
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
	 * request converter, convert CountReceivedAddressRequest to BasicRequestDTO
	 * 		userId (long)
	 *     
	 * Convert the query favourites request to WS type.
	 * @param proxy
	 * @return
	 */
	public static BasicRequestDTO convertProxy2FavoritesRequest(QueryFavoritesRequest proxy) {
		
		BasicRequestDTO request = null;
		
		if(proxy!=null) {
			
		    request = new BasicRequestDTO();
		    FavoritesDataConvert.setCommonField(request);
		    request.setUserId(proxy.getUserId());
		}
	    return request;
	}
	
	/**
	 * request converter, convert CountReceivedAddressRequest to BasicRequestDTO
	 * 		userId (long)
	 *                                          
     * Convert the query favourites request to WS type.
     * @param proxy
     * @return
     */
    public static BasicRequestDTO convertProxy2CountFavoritesRequest(CountReceivedAddressRequest proxy) {
    	
    	BasicRequestDTO request = null;
    	
        if(proxy!=null) {

        	request = new BasicRequestDTO();
            FavoritesDataConvert.setCommonField(request);
            request.setContextString(proxy.getContextString());
            
            request.setUserId(proxy.getUserId());
        }
        return request;
    }
	  
    
	/**
	 * Convert the response format
	 * @param fResponse
	 * @return
	 */
	public static QueryFavoritesResponse convertFavoritesResponse2Proxy(FavoriteMappingResponseDTO fResponse){
		
		QueryFavoritesResponse proxy = null;
		
		if(fResponse!=null) {
		    
			proxy=new QueryFavoritesResponse();
		    
			proxy.setStatus_code(fResponse.getResponseStatus().getStatusCode());
		    proxy.setStatus_message(fResponse.getResponseStatus().getStatusMessage());;
		    
		    proxy.setCategories(convertArrayCategory2Proxy(fResponse.getCategories()));
		    proxy.setFavorites(convertArrayFavs2Proxy(fResponse.getFavorites()));
		    proxy.setMapping(convertMapping2Proxy(fResponse.getMapping()));

		}
		return proxy;
	}
	
	/**
     * Convert the response format
     * @param fResponse
     * @return
     */
    public static CountReceivedAddressResponse convertCountFavoritesResponse2Proxy(CountResponseDTO fResponse){
    	
    	CountReceivedAddressResponse proxy = null;
    	
        if(fResponse!=null) {
        	
            proxy=new CountReceivedAddressResponse();
            proxy.setStatus_code(fResponse.getResponseStatus().getStatusCode());
            proxy.setStatus_message(fResponse.getResponseStatus().getStatusMessage());
            proxy.setCount(fResponse.getCount());
            
        }
        return proxy;
    }

	/**
	 * Convert mapping to c-server data type
	 * @param favorite
	 * @return
	 */
	public static FavoriteMapping convertMapping2Proxy(com.telenav.datatypes.favorite.v20.FavoriteMapping mapping){
		
		FavoriteMapping proxy = null;
		
		if(mapping!=null){
			
			proxy=new FavoriteMapping();
			
			proxy.setFavorite(convertFavorites2Proxy(mapping.getFavorite()));
			proxy.setCategories(convertArrayCategory2Proxy(mapping.getCategories()));
			
		}
		return proxy;
	}
	
   
	/**
	 * Convert array category to c-server data type
	 * @param favorites
	 * @return
	 */
	public static FavoriteMapping[] convertMapping2Proxy(com.telenav.datatypes.favorite.v20.FavoriteMapping[] mapppings){
		
		FavoriteMapping[] arrMap = null;
		
		 if (mapppings != null && Axis2Helper.isNonEmptyArray(mapppings)){
	            
			 	int length = mapppings.length;
	            arrMap = new FavoriteMapping[length];
	            
	            for (int index = 0; index < length; index++){
	            	arrMap[index] = convertMapping2Proxy(mapppings[index]);
	            }
	     }
		 return arrMap;
	}
	
	/**
	 * Convert array category to c-server data type
	 * @param favorites
	 * @return
	 */
/*	
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
*/
	
	/**
	 * Convert mapping to c-server data type
	 * @param favorite
	 * @return
	 */
/*	
	public static FavoriteMapping convertMapping2Proxy(com.telenav.datatypes.favorite.v10.FavoriteMapping mapping){
		if(mapping!=null){
			FavoriteMapping proxy=new FavoriteMapping();
			proxy.setFavorite(convertFavorites2Proxy(mapping.getFavorite()));
			proxy.setCategories(convertArrayCategory2Proxy(mapping.getCategories()));
			return proxy;
		}
		return null;
	}
*/	
	
	
	/**
	 * Convert array mapping to c-server data type
	 * @param favorites
	 * @return
	 */
	public static FavoriteCategory[] convertArrayCategory2Proxy(com.telenav.datatypes.favorite.v20.FavoriteCategory[] category){
		
		FavoriteCategory[] arrCate = null;
		
		if (category != null && Axis2Helper.isNonEmptyArray(category)){
	            int length = category.length;
	            arrCate = new FavoriteCategory[length];
	            
	            for (int index = 0; index < length; index++){
	            	arrCate[index] = convertCategories2Proxy(category[index]);
	            }
	        }
	        return arrCate;
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
	public static FavoriteCategory convertCategories2Proxy(com.telenav.datatypes.favorite.v20.FavoriteCategory category){
		
		FavoriteCategory proxy = null;
		
		if(null != category){
			
			proxy=new FavoriteCategory();
			
			proxy.setCategoryId(category.getCategoryId());
			proxy.setCategoryName(category.getCategoryName());
			proxy.setCategoryType(category.getCategoryType());
			proxy.setParentId(category.getParentId());
			proxy.setUserId(category.getUserId());
			
		}
		return proxy;
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
	public static Favorite[] convertArrayFavs2Proxy(com.telenav.datatypes.favorite.v20.Favorite[] favorites){
		
		Favorite[] arrFavs = null;
		
		 if (favorites != null && Axis2Helper.isNonEmptyArray(favorites)){
	    
			 	int length = favorites.length;
	            arrFavs = new Favorite[length];
	            
	            for (int index = 0; index < length; index++){
	            	arrFavs[index] = convertFavorites2Proxy(favorites[index]);
	            }      
		 }
		 return arrFavs;
	}
	
	
	/**
	 * Convert favourites to c-server data type
	 * @param favorite
	 * @return
	 */
	public static Favorite convertFavorites2Proxy(com.telenav.datatypes.favorite.v20.Favorite favorite){
		
		Favorite proxy = null;
		
		if(favorite!=null){
			
			proxy=new Favorite();
			
			proxy.setFavoriteId(favorite.getFavoriteId());
			proxy.setSenderName(favorite.getSenderName());
			proxy.setSenderPtn(favorite.getSenderPtn());
			proxy.setUserId(favorite.getUserId());
			proxy.setPOI(favorite.getFavoritePoi().getPoiId()>0 ?true : false);
		
			com.telenav.datatypes.address.v40.Address favoriteAddress = favorite.getFavoriteAddress();
			
			if(null != favoriteAddress){
				com.telenav.cserver.backend.datatypes.Address address = new com.telenav.cserver.backend.datatypes.Address();
				AddressDataConverter.convertWSAddressV40ToCSAddress(favoriteAddress, address);
				if(address != null){
					proxy.setFavoriteAddress(address);
				}
			}
			
			com.telenav.datatypes.content.tnpoi.v20.TnPoi tnPoi = favorite.getFavoritePoi();
			if(null != tnPoi){
				proxy.setFavoritePoi(PoiSearchConverterV20.convertWSPoiToCSTnPoi(favorite.getFavoritePoi()));	
				if( null != proxy.getFavoritePoi() ){
					com.telenav.cserver.backend.datatypes.Address address = new com.telenav.cserver.backend.datatypes.Address();
					if(  tnPoi.getAddress() != null )
					{
						AddressDataConverter.convertWSAddressV40ToCSAddress(tnPoi.getAddress(), address);
					}
					else
					{
						AddressDataConverter.convertWSAddressV40ToCSAddress(favorite.getFavoriteAddress(),address );
					}
					if(address != null){
						proxy.getFavoritePoi().setAddress(address);
					}
				}
			}
			
			// set label back
			if(proxy.isPOI()){
				proxy.setLabel(favorite.getLabel());
			}else{
				proxy.getFavoriteAddress().setLabel(favorite.getLabel());
			}
			
		}
		return proxy;
	}
	

/**
 * Convert favourites to c-server data type
 * @param favorite
 * @return
 */
	
/*	
public static Favorite convertFavorites2Proxy(com.telenav.datatypes.favorite.v10.Favorite favorite){
	
	Favorite proxy = null;
	
	if(favorite!=null){
		
		proxy=new Favorite();
		
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
	}
	return proxy;
}
*/

}
