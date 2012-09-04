/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.cose;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.activation.DataHandler;

import org.apache.commons.lang.StringUtils;

import com.telenav.cserver.backend.datatypes.Address;
import com.telenav.cserver.backend.datatypes.Constants;
import com.telenav.cserver.backend.datatypes.ErrorCode;
import com.telenav.cserver.backend.datatypes.GeoCode;
import com.telenav.cserver.backend.datatypes.LocalAppInfo;
import com.telenav.cserver.backend.datatypes.LocalAppInfoConstants;
import com.telenav.cserver.backend.datatypes.TnPoi;
import com.telenav.cserver.backend.datatypes.cose.Advertisement;
import com.telenav.cserver.backend.datatypes.cose.Coupon;
import com.telenav.cserver.backend.datatypes.cose.GasPriceInfo;
import com.telenav.cserver.backend.datatypes.cose.OpenTableInfo;
import com.telenav.cserver.backend.datatypes.cose.TnPoiReviewSummary;
import com.telenav.datatypes.content.cose.v10.CategoryParam;
import com.telenav.datatypes.content.cose.v10.PoiSearchArea;
import com.telenav.datatypes.content.cose.v10.PoiSearchType;
import com.telenav.datatypes.content.cose.v10.PoiSortType;
import com.telenav.datatypes.content.cose.v10.UGCPreference;
import com.telenav.datatypes.content.tnpoi.v10.TnPoiAdSchema;
import com.telenav.datatypes.content.tnpoi.v10.TnPoiSchema;
import com.telenav.datatypes.services.v20.UserInformation;
import com.telenav.kernel.ws.axis2.Axis2Helper;
import com.telenav.services.search.onebox.v10.ClientNameEnum;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * ContentSearchConverter.java
 *
 * @author bhu@telenav.cn
 * @version 1.0 2009-7-13
 */
public class PoiSearchConverter implements Constants
{
	public static com.telenav.services.content.v10.PoiRequest poiRequesttoWSRequest(PoiSearchRequest request)
	{
		com.telenav.services.content.v10.PoiRequest res = null;
		if(request == null)
		{
			return res;
		}
		
		res = new com.telenav.services.content.v10.PoiRequest();
		res.setAutoExpandSearchRadius(request.isAutoExpandSearchRadius());
		
		CategoryParam categoryParam = new CategoryParam();
		categoryParam.setCategoryHierarchyId(request.getHierarchyId());
		categoryParam.setCategoryId(new long[]{request.getCategoryId()});
		categoryParam.setCategoryVersion(request.getCategoryVersion());
		res.setCategoryParam(categoryParam);
		
		//res.setContextString(request.getContextString());
		res.setMinReturnedItems(request.getMinReturnedItems());
		res.setNeedHighlyRelevantPois(request.isNeedHighlyRelevantPois());
		res.setNeedSponsoredPois(request.isNeedSponsoredPois());
		res.setNeedUserGeneratePois(request.isNeedUserGeneratePois());
		res.setNeedUserPreviousRating(request.isNeedUserPreviousRating());
		
		res.setOnlyMostPopular(request.isOnlyMostPopular());
		// Done at the request of ChunFeng to improve pagination
//		res.setPageNumber(request.getPageNumber());
//		res.setPageSize(request.getPageSize());
		res.setPoiNumber(request.getMaxResult());
		
		res.setPoiStartIndex(request.getPageNumber() * request.getPageSize());
		
		res.setSponsorListingNumber(request.getSponsorListingNumber());
		res.setSponsorListingStartIndex(request.getPageNumber() * request.getSponsorListingNumber());
		res.setPoiQuery(request.getPoiQuery());
		
		res.setSearchArea(convertSearchArea(request));
		
		//TODO: search type
		//res.setSearchType();
		res.setSortType(convertToSortType(request.getPoiSortType()));
		res.setUgcPreference(convertToUGCPerference(request.getUgcPreferenceType()));
		res.setUserId(request.getUserId());
		res.setTransactionId(request.getTransactionId());
		
		return res;
	}
	
	public static com.telenav.services.content.v10.PoiRequest airportRequesttoWSRequest(AirportSearchRequest request)
	{
	    com.telenav.services.content.v10.PoiRequest req = null;
        if(request == null)
        {
            return req;
        }
        req = new com.telenav.services.content.v10.PoiRequest();
        req.setPoiQuery(request.getAirportQuery());
        req.setSearchType(PoiSearchType.FOR_AIRPORT);
        req.setSortType(convertToSortType(request.getPoiSortType()));
        PoiSearchArea psa = new PoiSearchArea();
        if(request.getCountryList() != null && !request.getCountryList().equals(""))
        {
            psa.setCountryList(request.getCountryList());
        }
        req.setSearchArea(psa);
        
        return req;
	}
	
	public static UGCPreference convertToUGCPerference(int ugcPreference)
	{
		switch(ugcPreference)
		{
		case GENERATE_BY_ALL_USERS:
			return UGCPreference.GENERATE_BY_ALL_USERS;
		case GENERATE_BY_ME:
			return UGCPreference.GENERATE_BY_ME;
		case GENERATE_BY_HIGHLY_RATED_USERS:
			return UGCPreference.GENERATE_BY_HIGHLY_RATED_USERS;
		case GENERATE_BY_TRUSTED_USERS:
			return UGCPreference.GENERATE_BY_TRUSTED_USERS;
		}
		return UGCPreference.GENERATE_BY_ALL_USERS;
	}
	
	public static PoiSortType convertToSortType(int sortType)
	{
		switch(sortType)
		{
		case SORT_BY_DISTANCE:
			return PoiSortType.BY_DISTANCE;
		case SORT_BY_RATING:
			return PoiSortType.BY_RATING;
		case SORT_BY_POPULAE:
			return PoiSortType.BY_POPULAR;
		case SORT_BY_RELEVANCE:
			return PoiSortType.BY_RANKING;
		case SORT_BY_GASPRICE:
			return PoiSortType.BY_GASPRICE;
		}
		return PoiSortType.BY_RANKING;
	}
	public static PoiSearchArea convertSearchArea(PoiSearchRequest request)
	{
		Address addr = request.getAnchor();
		if(addr == null)
		{
			return null;
		}
		PoiSearchArea searchArea = new PoiSearchArea();
		searchArea.setRadiusInMeter(request.getRadiusInMeter());
		searchArea.setCity(addr.getCityName());
		searchArea.setProvince(addr.getState());
		searchArea.setCountryCode(addr.getCountry());
		searchArea.setPostalCode(addr.getPostalCode());
		//com.telenav.ws.datatypes.address.Address address = addressToWSAddress(addr);
		//searchArea.setGeoCodedAddress(address);
		com.telenav.ws.datatypes.address.GeoCode geoCode = new com.telenav.ws.datatypes.address.GeoCode();
		geoCode.setLatitude(addr.getLatitude());
		geoCode.setLongitude(addr.getLongitude());
		searchArea.setAnchor(geoCode);
		
		//set route points
		List<GeoCode> routePoints = request.getRoutePoints();
		if(routePoints != null)
		{
			searchArea.setRoutePoints(convertRoutePointArray(routePoints));
		}
		return searchArea;
	}
	
	public static com.telenav.ws.datatypes.address.GeoCode[] convertRoutePointArray(List<GeoCode> routePoints)
	{
		com.telenav.ws.datatypes.address.GeoCode[] geoCodes = new com.telenav.ws.datatypes.address.GeoCode[routePoints.size()];
		for(int i = 0; i < routePoints.size(); i++)
		{
			geoCodes[i] = new com.telenav.ws.datatypes.address.GeoCode();
			geoCodes[i].setLatitude(routePoints.get(i).getLatitude());
			geoCodes[i].setLongitude(routePoints.get(i).getLongitude());
		}
		return geoCodes;
	}
	public static com.telenav.ws.datatypes.address.Address addressToWSAddress(Address addr)
	{
		com.telenav.ws.datatypes.address.Address address = new com.telenav.ws.datatypes.address.Address();
		if(addr != null)
		{
			address.setCity(addr.getCityName());
			address.setCountry(addr.getCountry());
			address.setCounty(addr.getCounty());
			address.setFirstLine(addr.getFirstLine());
			address.setCrossStreetName(addr.getCrossStreetName());
			
			com.telenav.ws.datatypes.address.GeoCode geoCode = new com.telenav.ws.datatypes.address.GeoCode();
			geoCode.setLatitude(addr.getLatitude());
			geoCode.setLongitude(addr.getLongitude());
			address.setGeoCode(geoCode);
			
			address.setLastLine(addr.getLastLine());
			address.setPostalCode(addr.getPostalCode());
			address.setState(addr.getState());
			address.setStreetName(addr.getStreetName());
			//street number
			//address.setStreetNumber(addr.getstreetNumber);
		}
		return address;
	}
	
	public static PoiSearchResponse convertWSRespToPoiSearchResponse(com.telenav.services.content.v10.PoiSearchResponse resp)
	{
		PoiSearchResponse response = new PoiSearchResponse();
		if(resp == null)
		{
			response.setPoiSearchStatus(ErrorCode.POI_STATUS_COSE_ERROR);
			return response;
		}
		response.setTotalMatchedPoiCount(resp.getTotalMatchedPoiCount());
		
		List<TnPoi> poiList = new ArrayList<TnPoi>();
		if(resp.getPois() != null)
		{
			for(int i = 0; i < resp.getPois().length; i++)
			{
				TnPoi poi = convertWSPoiToTnPoi(resp.getPois()[i]);
				poiList.add(poi);
			}
			response.setPois(poiList);
		}
		else
		{
			response.setPoiSearchStatus(ErrorCode.POI_STATUS_POI_NOT_FOUND);
		}
		List<TnPoi> sponsorList = new ArrayList<TnPoi>();
		if(resp.getSponsorPois() != null)
		{
			for(int i = 0; i < resp.getSponsorPois().length; i++)
			{
				TnPoi sPoi = convertWSPoiToTnPoi(resp.getSponsorPois()[i]);
				sponsorList.add(sPoi);
			}
			response.setSponsorPois(sponsorList);
		}

		return response;
	}
	
	/**
	 * this method also used in favorites module, if change it please very careful.
	 * @param poi
	 * @return
	 */
	public static TnPoi convertWSPoiToTnPoi(com.telenav.datatypes.content.tnpoi.v10.TnPoi poi)
	{
		if(poi == null)
		{
			return null;
		}
		TnPoi tnPoi = new TnPoi();
		tnPoi.setPoiId(poi.getPoiId());
		tnPoi.setBasePoiId(poi.getBasePoiId());
		tnPoi.setAddress(wsAddressToAddress(poi.getAddress()));
		tnPoi.setBasePoiId(poi.getBasePoiId());
		tnPoi.setBrandName(poi.getBrandName());
		tnPoi.setEditor(poi.getEditor());
		tnPoi.setFlagInfo(poi.getFlagInfo());
		tnPoi.setVendor(poi.getVendor());
		tnPoi.setPhoneNumber(poi.getPhoneNumber());
		tnPoi.setNavigable(poi.getIsNavigable());
		tnPoi.setRatingEnable(poi.getIsRatingEnable());
	
		if(poi.getRatingReviewSummary() != null)
		{
			tnPoi.setReviewSummary(wsRviewSummaryToTnPoiReviewSummary(poi.getRatingReviewSummary()));
		}
		Map<String, String> propertyMap = convertPropertyMap(poi.getProperties());
		tnPoi.setAd(convertAd(propertyMap));
		
		setTnPoiProperties(tnPoi, propertyMap);
		tnPoi.setLocalAppInfos(handleLocalAppRelatedInfo(propertyMap));
		tnPoi.setOpenTableInfo(convertOpenTableInfo(propertyMap));
		
		tnPoi.setCouponList(wsOffersToCouponList(poi.getOffers()));
		tnPoi.setGasPriceInfos(convertPriceInfoList(poi.getPriceInfos()));
		
		//extra info flag
		setPoiExtraInfo(tnPoi, propertyMap);
		return tnPoi;
	}
	
	public static List<LocalAppInfo> handleLocalAppRelatedInfo(Map<String, String> propertyMap)
	{
		List<LocalAppInfo> localAppInfos=new ArrayList<LocalAppInfo>();
		
		if(StringUtils.isNotBlank(propertyMap.get(TnPoiSchema._additionalVendors)))
		{
			//hotel
			LocalAppInfo hotelLocalApp=new LocalAppInfo(LocalAppInfoConstants.LOCAL_APP_HOTEL);
			String[] vendorCodes = propertyMap.get(TnPoiSchema._additionalVendors).split(",");
			List<String> verdorCodeList=Arrays.asList(vendorCodes);
			if(verdorCodeList.contains(LocalAppInfoConstants.HOTEL_ALLIANCE_VENDOR_CODE))
				hotelLocalApp.addProp(LocalAppInfoConstants.LOCAL_APP_HOTEL_HASHOTELALLICANCE, String.valueOf(true));
			else
				hotelLocalApp.addProp(LocalAppInfoConstants.LOCAL_APP_HOTEL_HASHOTELALLICANCE, String.valueOf(false));
			localAppInfos.add(hotelLocalApp);
		}
		
		return localAppInfos;
	}
	
	public static TnPoiReviewSummary wsRviewSummaryToTnPoiReviewSummary(com.telenav.datatypes.content.tnpoi.v10.TnPoiReviewSummary summary)
	{
		if(summary == null)
		{
			return null;
		}
		
		TnPoiReviewSummary reviewSum = new TnPoiReviewSummary();
		reviewSum.setAverageRating(summary.getAverageRating());
		reviewSum.setBasePoiId(summary.getBasePoiId());
		reviewSum.setPoiId(summary.getPoiId());
		reviewSum.setPopularity(summary.getPopularity());
		reviewSum.setRatingNumber(summary.getRatingNumber());
		reviewSum.setReviewAveragePrice(summary.getReviewAveragePrice());
		reviewSum.setReviewCategoryPath(summary.getReviewCategoryPath());
		reviewSum.setReviewNumber(summary.getReviewNumber());
		reviewSum.setReviewText(summary.getReviewText());
		reviewSum.setReviewType(summary.getReviewType());
		reviewSum.setUserPreviousRating(summary.getUserPreviousRating());
		
		return reviewSum;
	}
	public static List<GasPriceInfo> convertPriceInfoList(com.telenav.datatypes.content.tnpoi.v10.TnPriceRecord[] prices)
	{
		List<GasPriceInfo> priceList= null;
		if(prices != null && prices.length > 0)
		{	
			priceList = new ArrayList<GasPriceInfo>();
			for(int i = 0; i < prices.length; i++)
			{
				GasPriceInfo gasPriceInfo = new GasPriceInfo();
				gasPriceInfo.setPrice(prices[i].getPrice());
				try
				{
					gasPriceInfo.setGasType(Integer.valueOf(prices[i].getProduct()).intValue());
				}
				catch(Exception e)
				{
					gasPriceInfo.setGasType(i);
				}
				gasPriceInfo.setUpdateTime(prices[i].getUpdateTime());
				priceList.add(gasPriceInfo);
			}
		}
		return priceList;
	}
	/**
	 * convert property array into a map
	 * @param properties
	 * @return
	 */
	public static Map<String, String> convertPropertyMap(com.telenav.ws.datatypes.common.Property[] properties)
	{  
		Map<String, String> propertyMap = new HashMap<String, String>();
		if(properties!=null&& Axis2Helper.isNonEmptyArray(properties)) {
		    for(int i = 0; i < properties.length; i++)
	        {
	            com.telenav.ws.datatypes.common.Property property = properties[i];
	            propertyMap.put(property.getKey(), property.getValue());
	        }
		}
	
		return propertyMap;
	}
	
	public static void setPoiExtraInfo(TnPoi p, Map<String, String> props)
	{
		Object o = props.get(TnPoiSchema._isSponsorPoi);
		if(o != null)
			p.getPoiExtraInfo().put(TnPoiSchema._isSponsorPoi, (String)o);
		o = props.get(TnPoiSchema._isAdsPoi);
		if(o != null)
			p.getPoiExtraInfo().put(TnPoiSchema._isAdsPoi, (String)o);
		o = props.get(TnPoiSchema._hasAdsMenu);
		if(o != null)
			p.getPoiExtraInfo().put(TnPoiSchema._hasAdsMenu, (String)o);
		o = props.get(TnPoiSchema._hasDeals);
		if(o != null)
			p.getPoiExtraInfo().put(TnPoiSchema._hasDeals, (String)o);
		o = props.get(TnPoiSchema._hasReviews);
		if(o != null)
			p.getPoiExtraInfo().put(TnPoiSchema._hasReviews, (String)o);
		o = props.get(TnPoiSchema._hasPoiMenu);
		if(o != null)
			p.getPoiExtraInfo().put(TnPoiSchema._hasPoiMenu, (String)o);
		o = props.get(TnPoiSchema._hasPoiExtraAttributes);
		if(o != null)
			p.getPoiExtraInfo().put(TnPoiSchema._hasPoiExtraAttributes, (String)o);
		o = props.get(TnPoiSchema._hasPoiDetails);
		if(o != null)
			p.getPoiExtraInfo().put(TnPoiSchema._hasPoiDetails, (String)o);
		o = props.get(TnPoiSchema._poiLogo);
		if(o != null)
		    p.getPoiExtraInfo().put(TnPoiSchema._poiLogo, (String)o);
		o = props.get(TnPoiSchema._brandLogo);
        if(o != null)
            p.getPoiExtraInfo().put(TnPoiSchema._brandLogo, (String)o);
        o = props.get(TnPoiSchema._categoryLogo);
        if(o != null)
            p.getPoiExtraInfo().put(TnPoiSchema._categoryLogo, (String)o);
	}
	
	public static void setTnPoiProperties(TnPoi p, Map<String, String> props)
	{
		Object o ;
		o= props.get(TnPoiSchema._featureName);
		if (o != null)
			p.setFeatureName((String)o);		
//		o = props.get("categoryName");
//		if (o != null)
//			p.setCategoryName((String)o);
//		
		o = props.get(TnPoiSchema._categoryLabel);
		if (o!=null)
			p.setCategoryLabel((String)o);
		
		o = props.get(TnPoiSchema._categoryId1);
		if (o != null)
			p.setCategoryId((String)o);
//		
//		o = props.get(TnPoiSchema._categoryId2);
//		if (o != null)
//			p.setCategoryId2(Long.valueOf((String)o).longValue());
//		
//		o = props.get(TnPoiSchema._categoryId3);
//		if (o!=null)
//			p.setCategoryId3(Long.valueOf((String)o).longValue());
		
		// TODO unit need to unify to meter.
		o = props.get(TnPoiSchema._distanceInMeter);
		if (o != null)
			p.setDistanceInMeter(Double.valueOf((String)o).doubleValue());
		
//		o = props.get(TnPoiSchema._faxNumber);
//		if (o != null)
//			p.setFax((String)o);
//		
//		o = props.get(TnPoiSchema._webUrl);
//		if (o != null)
//			p.setUrl((String)o);
//		
//		o = props.get(TnPoiSchema._priority);
//		if (o != null)
//			p.setDisplayPriorityId(Long.valueOf((String)o).longValue());
//		
//		o = props.get(TnPoiSchema._audioId);
//		if (o != null)
//			p.setAudioId((String)o);
//		
//		o = props.get(TnPoiSchema._layoutType);
//		if (o != null)
//			p.setLayoutId((String)o);
//		
//		o = props.get(TnPoiSchema._marketCity);
//		if (o != null)
//			p.setMarketCity((String)o);
//		
//		o = props.get(TnPoiSchema._marketState);
//		if (o != null)
//			p.setMarketState((String)o);
//		
//		o = props.get(TnPoiSchema._marketName);
//		if (o != null)
//			p.setMarketName((String)o);
//		
//		o = props.get(TnPoiSchema._promotionTypeId);
//		if (o != null)
//			p.setPromotionTypeId(Long.valueOf((String)o).longValue());
		o = props.get(TnPoiSchema._priceRange);
		if(o != null)
		{
			p.setPriceRange(Double.valueOf((String)o).doubleValue());
		}
		o = props.get(TnPoiSchema._openHours);
		if(o != null)
		{
			p.setBusinessHour((String)o);
		}
		
		o = props.get(TnPoiSchema._menus);
		if(o != null)
		{
			p.setMenu((String)o);
		}
		
		//added by Pan Zhang on 2010-10-12 to get distanceToUserInMeter from oneboxsearch
		o = props.get("distanceToUserInMeter");
		if (o != null)
		{
			p.setDistanceToUserInMeter(Double.valueOf((String)o).doubleValue());;
		}
	}
	
	public static Advertisement convertAd(Map<String, String> props) 
	{
		Advertisement ad = new Advertisement();
		if(props == null)
		{
			return ad;
		}
		
		Object o = props.get(TnPoiAdSchema._shortMessage);
		if (o != null)
		{
			ad.setShortMessage((String)o);
		}
			
		o = props.get(TnPoiAdSchema._buyerName);
		if (o != null)
		{
			ad.setBuyerName((String)o);
		}
		
		o = props.get(TnPoiAdSchema._endTime);
		if (o != null)
		{
			ad.setEndTime(new Date(Long.valueOf((String)o).longValue()));
		}
		
		o = props.get(TnPoiAdSchema._payPerCall);
		if (o != null)
		{
			ad.setPayPerCall(Boolean.valueOf((String)o).booleanValue());
		}
		
		o = props.get(TnPoiAdSchema._payPerClick);
		if (o != null)
		{
			ad.setPayPerClick(Boolean.valueOf((String)o).booleanValue());
		}
		
		o = props.get(TnPoiAdSchema._poundEnabled);
		if (o != null)
		{
			ad.setPoundEnabled(Boolean.valueOf((String)o).booleanValue());
		}
		
		o = props.get(TnPoiAdSchema._sourceAdId);
		if (o!=null)
		{
			ad.setSourceAdId((String)o);
		}
			
		o = props.get(TnPoiAdSchema._starEnabled);
		if (o!=null)
		{
			ad.setStarEnabled(Boolean.valueOf((String)o).booleanValue());
		}
			
		o = props.get(TnPoiSchema._ranking);
		if (o != null)
		{
			ad.setRanking(Integer.valueOf((String)o).intValue());
		}
		//TODO:[HB] cose will add it in schema later
		o = props.get("AdType");
		if (o != null)
		{
			ad.setAdType(Integer.valueOf((String)o).intValue());
		}
			
		o = props.get(TnPoiAdSchema._adSource);
		if (o != null)
		{
			ad.setAdSource((String)o);
		}
			
		o = props.get(TnPoiAdSchema._message);
		if (o != null)
		{
			ad.setMessage((String)o);
		}
			
		o = props.get(TnPoiAdSchema._adpageUrl);
		if(o != null)
		{
			ad.setAdPageUrl((String)o);
		}
		
		return ad;
	}
	
	public static OpenTableInfo convertOpenTableInfo(Map<String, String> props)
	{
		OpenTableInfo openTableInfo = new OpenTableInfo();
		if(props == null)
		{
			return openTableInfo;
		}
		Object o = props.get("partner");
		if (o != null)
		{
			openTableInfo.setPartner((String)o);
		}
		o = props.get("isReservable");
		if(o != null)
		{
			openTableInfo.setReservable(((String)o).equalsIgnoreCase("true") ? true : false);
		}
		o = props.get("partnerPoiId");
		if(o != null)
		{
			openTableInfo.setPartnerPoiId((String)o); 
		}
		return openTableInfo;
	}
	
	public static Coupon offerToCoupon(com.telenav.datatypes.content.tnpoi.v10.Offer offer)
	{
		Coupon coupon = new Coupon();
		if(offer == null)
		{
			return coupon;
		}
		coupon.setDescription(offer.getDescription());
		coupon.setEndDate(offer.getEndDate());
		coupon.setId(offer.getId());
		
		coupon.setImage(dataHandlerToByteArray(offer.getImage()));
		coupon.setName(offer.getName());
		coupon.setStartDate(offer.getStartDate());
		coupon.setText(offer.getText());
		coupon.setUrl(offer.getUrl());
		coupon.setUrlPPE(offer.getUrlPPE());
		coupon.setImage(dataHandlerToByteArray(offer.getImage()));
		return coupon;
		
	}
	public static List<Coupon> wsOffersToCouponList(com.telenav.datatypes.content.tnpoi.v10.Offer[] wsOffers)
	{
		List couponList = new ArrayList();
		if(wsOffers == null || wsOffers.length == 0)
		{
			return couponList;
		}
		for(int i = 0; i < wsOffers.length; i++)
		{
			if(wsOffers[i] != null)
			{
				couponList.add(offerToCoupon(wsOffers[i]));
			}
		}
		
		return couponList;
	}
	
	public static byte[] dataHandlerToByteArray(DataHandler dh)
	{
		byte[] img = null;
		try {
			if (dh != null && dh.getInputStream() != null) {
				InputStream is = dh.getInputStream();
				byte buf[] = new byte[1024];
				Vector vBuf = new Vector();
				int len = 0;
				int totalLen = 0;
				while ((len = is.read(buf)) > 0) {
					vBuf.add(buf);
					totalLen += len;
					buf = new byte[1024];
				}
				is.close();
				int left = totalLen;
				if (totalLen < 1024 * 1024 * 16) {
					img = new byte[totalLen];
					for (int i = 0; i < vBuf.size(); i++) {
						byte[] b = (byte[]) vBuf.get(i);
						System.arraycopy(b, 0, img, 1024 * i,
								(left > 1024 ? 1024 : left));
						left -= 1024;
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return img;
	}
	
	/**
	 * this method also used in the favorite module
	 * @param wsAddr
	 * @return
	 */
	public static Address wsAddressToAddress(com.telenav.ws.datatypes.address.Address wsAddr)
	{
		if(wsAddr == null)
		{
			return null;
		}
		Address address = new Address();
		address.setCityName(wsAddr.getCity());
		address.setCountry(wsAddr.getCountry());
		address.setCounty(wsAddr.getCounty());
		address.setCrossStreetName(wsAddr.getCrossStreetName());
		address.setDoor(wsAddr.getStreetNumber());
		address.setFirstLine(wsAddr.getFirstLine());
		address.setLastLine(wsAddr.getLastLine());
		if(wsAddr.getGeoCode() != null)
		{
			address.setLatitude(wsAddr.getGeoCode().getLatitude());
			address.setLongitude(wsAddr.getGeoCode().getLongitude());
		}
		address.setPostalCode(wsAddr.getPostalCode());
		address.setState(wsAddr.getState());
		address.setStreetName(wsAddr.getStreetName());
		address.setLabel(wsAddr.getLabel());
		
		return address;
	}

	public static com.telenav.services.content.v10.GetPoiDetailsRequest createPoiDetailsRequest(PoiDetailsRequest request)
	{
		if(request == null)
			return null;
		com.telenav.services.content.v10.GetPoiDetailsRequest req = new com.telenav.services.content.v10.GetPoiDetailsRequest();
		req.setPoiId(request.getPoiId());
		
		UserInformation user = new UserInformation();
		user.setUserId(request.getUserId());
		user.setPtn(request.getPtn());
		req.setUserInfo(user);
		
		req.setClientName(ClientNameEnum._MOBILE);
		req.setClientVersion("1");
		//req.setTransactionId(request.getPtn());//TODO : transactionId ??	
		return req;
	}
	
	public static PoiDetailsResponse createPoiDetailsResponse(com.telenav.services.content.v10.GetPoiDetailsResponse response)
	{
		if(response == null || response.getDetails() == null)
			return null;
		PoiDetailsResponse resp = new PoiDetailsResponse();
		resp.setBusinessHours(response.getDetails().getBusinessHours());
		resp.setBusinessHoursNote(response.getDetails().getBusinessHoursNote());
		resp.setDescription(response.getDetails().getDescription());
		if(response.getDetails().getLogo() != null)
		{
			resp.setLogoId(response.getDetails().getLogo().getLogoId());
			resp.setMediaServerKey(response.getDetails().getLogo().getMediaServerKey());
		}
		resp.setOlsonTimezone(response.getDetails().getOlsonTimezone());
		resp.setPoiId(response.getDetails().getPoiId());
		resp.setPriceRange(response.getDetails().getPriceRange());
		return resp;
	}
}
