package com.telenav.cserver.backend.cose;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import com.telenav.cserver.backend.datatypes.Address;
import com.telenav.cserver.backend.datatypes.ErrorCode;
import com.telenav.cserver.backend.datatypes.GeoCode;
import com.telenav.cserver.backend.datatypes.LocalAppInfo;
import com.telenav.cserver.backend.datatypes.LocalAppInfoConstants;
import com.telenav.cserver.backend.datatypes.cose.Advertisement;
import com.telenav.cserver.backend.datatypes.cose.Coupon;
import com.telenav.cserver.backend.datatypes.cose.GasPriceInfo;
import com.telenav.cserver.backend.datatypes.cose.OpenTableInfo;
import com.telenav.cserver.backend.datatypes.cose.TnPoiReviewSummary;
import com.telenav.datatypes.content.cose.v20.PoiSearchArea;
import com.telenav.datatypes.content.cose.v20.PoiSortType;
import com.telenav.datatypes.content.tnpoi.v10.TnPoiAdSchema;
import com.telenav.datatypes.content.tnpoi.v10.TnPoiSchema;
import com.telenav.datatypes.content.tnpoi.v20.Offer;
import com.telenav.datatypes.content.tnpoi.v20.PoiDetails;
import com.telenav.datatypes.content.tnpoi.v20.TnPoi;
import com.telenav.datatypes.content.tnpoi.v20.TnPriceRecord;
import com.telenav.j2me.datatypes.POI;
import com.telenav.services.content.v20.GetPoiDetailsRequest;
import com.telenav.services.content.v20.GetPoiDetailsResponse;
import com.telenav.services.content.v20.PoiRequest;
import com.telenav.services.content.v20.PoiSearchResponse;
import com.telenav.ws.datatypes.common.Property;

public class TestPoiSearchConverterv20 
{
	@Test
	public void airportRequesttoWSRequest()
	{
		AirportSearchRequest request = null;
		PoiRequest wsReq;
		//null
		wsReq = PoiSearchConverterV20.airportRequesttoWSRequest(null);
		Assert.assertNull(wsReq);
		
		//not null, country list is null
		request = new AirportSearchRequest();
		request.setAirportQuery("test");
		request.setPoiSortType(0);		
		wsReq = PoiSearchConverterV20.airportRequesttoWSRequest(request);
		Assert.assertEquals(request.getAirportQuery(), wsReq.getPoiQuery());
		
		//not null, country list isn't null
		request.setCountryList("countryList");
		wsReq = PoiSearchConverterV20.airportRequesttoWSRequest(request);
		Assert.assertEquals(request.getCountryList(), wsReq.getSearchArea().getCountryList());
	}
	
	@Test
	public void convertRoutePointArray()
	{
		PoiSearchRequest poiRequest = null;
		PoiRequest wsReq;
		//null
		wsReq = PoiSearchConverterV20.poiRequesttoWSRequest(null);
		Assert.assertNull(wsReq);
		
		//
		poiRequest = new PoiSearchRequest();
		poiRequest.setCategoryId(1000);
		poiRequest.setHierarchyId(123);
		wsReq = PoiSearchConverterV20.poiRequesttoWSRequest(poiRequest);
		Assert.assertEquals(poiRequest.getCategoryId(), wsReq.getCategoryParam().getCategoryId()[0]);
		//
		poiRequest.setPageSize(20);
		poiRequest.setPageNumber(1);
		wsReq = PoiSearchConverterV20.poiRequesttoWSRequest(poiRequest);
		Assert.assertEquals(20, wsReq.getPoiStartIndex());;
	}
	
	@Test
	public void convertToSortType()
	{
		Assert.assertTrue(PoiSortType._BY_DISTANCE.equals(PoiSearchConverterV20.convertToSortType(0).getValue()));
		Assert.assertTrue(PoiSortType._BY_RATING.equals(PoiSearchConverterV20.convertToSortType(1).getValue()));
		Assert.assertTrue(PoiSortType._BY_POPULAR.equals(PoiSearchConverterV20.convertToSortType(2).getValue()));
		Assert.assertTrue(PoiSortType._BY_RANKING.equals(PoiSearchConverterV20.convertToSortType(3).getValue()));
		Assert.assertTrue(PoiSortType._BY_GASPRICE.equals(PoiSearchConverterV20.convertToSortType(4).getValue()));
	}
	
	@Test
	public void convertSearchArea()
	{
		PoiSearchRequest poiRequest = new PoiSearchRequest();
		PoiSearchArea searchArea = null;
		//request is null
		searchArea = PoiSearchConverterV20.convertSearchArea(poiRequest);
		Assert.assertNull(searchArea);
		//request is not null && route poins is null
		Address addr = new Address();
		addr.setCityName("city");
		poiRequest.setAnchor(addr);
		searchArea = PoiSearchConverterV20.convertSearchArea(poiRequest);
		Assert.assertEquals(poiRequest.getAnchor().getCityName(), searchArea.getCity());
		
		//routepoints is not null
		List<GeoCode> list = new ArrayList<GeoCode>();
		GeoCode c1 = new GeoCode();
		c1.setLatitude(33.3333);
		c1.setLongitude(123.1234);
		list.add(c1);
		c1 = new GeoCode();
		c1.setLatitude(22.2222);
		c1.setLongitude(111.1111);
		list.add(c1);
		poiRequest.setRoutePoints(list);
		searchArea = PoiSearchConverterV20.convertSearchArea(poiRequest);
		Assert.assertTrue(searchArea.getRoutePoints().length == 2);
	}

	@Test
	public void convertWSRespToPoiSearchResponse()
	{
		PoiSearchResponse wsResp = null;
		com.telenav.cserver.backend.cose.PoiSearchResponse resp = null;
		//param is null
		resp = PoiSearchConverterV20.convertWSRespToPoiSearchResponse(null);
		Assert.assertEquals(ErrorCode.POI_STATUS_COSE_ERROR, resp.getPoiSearchStatus());
		//
		wsResp = new PoiSearchResponse();
		wsResp.setPois(new TnPoi[]{new TnPoi(), new TnPoi()});
		wsResp.setSponsorPois(new TnPoi[]{new TnPoi(), new TnPoi()});
		resp = PoiSearchConverterV20.convertWSRespToPoiSearchResponse(wsResp);
		Assert.assertTrue(resp.getPois().size() == 2);
		Assert.assertTrue(resp.getSponsorPois().size() == 2);
	}
	
	@Test
	public void convertCSPoiToWSTnPoi()
	{
		com.telenav.cserver.backend.datatypes.TnPoi poi = null;
		TnPoi wsPoi = null;
		//param is null
		wsPoi = PoiSearchConverterV20.convertCSPoiToWSTnPoi(null);
		Assert.assertNull(wsPoi);
		//param is not null
		poi = new com.telenav.cserver.backend.datatypes.TnPoi();
		poi.setPoiId(1000);
		TnPoiReviewSummary reviewSummary = new TnPoiReviewSummary();
		reviewSummary.setRatingNumber(5);
		poi.setReviewSummary(reviewSummary);
		
		wsPoi = PoiSearchConverterV20.convertCSPoiToWSTnPoi(poi);
		Assert.assertEquals(poi.getPoiId(), wsPoi.getPoiId());
		Assert.assertEquals(reviewSummary.getRatingNumber(), wsPoi.getRatingReviewSummary().getRatingNumber());
	}
	
	@Test
	public void convertPoiToCSTnPoi()
	{
		POI poi = new POI();
		poi.poiId = 1000;
		com.telenav.cserver.backend.datatypes.TnPoi csTnPoi = null;
		csTnPoi= PoiSearchConverterV20.convertPoiToCSTnPoi(poi);
		Assert.assertEquals(poi.poiId, csTnPoi.getPoiId());
	}
	
	@Test
	public void handleLocalAppRelatedInfo()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(TnPoiSchema._additionalVendors, "VendorOne,P_HA");
		List<LocalAppInfo> list = PoiSearchConverterV20.handleLocalAppRelatedInfo(map);
		Assert.assertTrue(list.size()>0);
		
		map.put(LocalAppInfoConstants.HOTEL_ALLIANCE_VENDOR_CODE, "alliance_vendor");
		Assert.assertTrue(Boolean.valueOf(list.get(0).getProps().get(LocalAppInfoConstants.LOCAL_APP_HOTEL_HASHOTELALLICANCE)));
	}
	
	@Test
	public void wsRviewSummaryToTnPoiReviewSummary()
	{
		TnPoiReviewSummary summary = null;
		com.telenav.datatypes.content.tnpoi.v20.TnPoiReviewSummary wsSummary = new com.telenav.datatypes.content.tnpoi.v20.TnPoiReviewSummary();
		//param is null
		summary = PoiSearchConverterV20.wsRviewSummaryToTnPoiReviewSummary(null);
		Assert.assertNull(summary);
		//param is not null
		wsSummary.setPoiId(1000);
		summary = PoiSearchConverterV20.wsRviewSummaryToTnPoiReviewSummary(wsSummary);
		Assert.assertEquals(wsSummary.getPoiId(), summary.getPoiId());
	}
	
	@Test
	public void convertPriceInfoList()
	{
		TnPriceRecord price = new TnPriceRecord();
		price.setPrice(12.34);
		price.setProduct("1234");
		TnPriceRecord price1 = new TnPriceRecord();
		price1.setPrice(56.78);
		price1.setProduct("aa");
		TnPriceRecord[] prices = new TnPriceRecord[]{price, price1};
		List<GasPriceInfo> list = PoiSearchConverterV20.convertPriceInfoList(prices);
		Assert.assertTrue(list.size() == 2);
		
		prices = PoiSearchConverterV20.convertGasPriceInfo(list);
		Assert.assertTrue(prices.length == 2);
	}
	
	@Test
	public void convertPropertyMap()
	{
		Property p = new Property();
		p.setKey("K");
		p.setValue("V");
		Property p1 = new Property();
		p1.setKey("K1");
		p1.setValue("V1");
				
		Map<String, String> map = PoiSearchConverterV20.convertPropertyMap(new Property[]{p, p1});
		Assert.assertEquals("V", map.get("K"));
		Assert.assertEquals("V1", map.get("K1"));
	}
	
	@Test
	public void setPoiExtraInfo()
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put(TnPoiSchema._isSponsorPoi, "isSponsor");
		map.put(TnPoiSchema._isAdsPoi, "isAdsPoi");
		map.put(TnPoiSchema._hasAdsMenu, "hasAdsMenu");
		map.put(TnPoiSchema._hasDeals, "hasDeals");
		map.put(TnPoiSchema._hasReviews, "hasReviews");
		map.put(TnPoiSchema._hasPoiMenu, "hasPoiMenu");
		map.put(TnPoiSchema._hasPoiExtraAttributes, "hasExtraAttrs");
		map.put(TnPoiSchema._hasPoiDetails, "hasPoiDetails");
		map.put(TnPoiSchema._poiLogo, "poiLogo");
		map.put(TnPoiSchema._brandLogo, "brandLogo");
		map.put(TnPoiSchema._categoryLogo, "categoryLogo");
		com.telenav.cserver.backend.datatypes.TnPoi poi = new com.telenav.cserver.backend.datatypes.TnPoi();
		PoiSearchConverterV20.setPoiExtraInfo(poi, map);
		Assert.assertEquals("isSponsor", poi.getPoiExtraInfo().get(TnPoiSchema._isSponsorPoi));
	}
	
	@Test
	public void setTnPoiProperties()
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put(TnPoiSchema._featureName, "featureName");
		map.put(TnPoiSchema._categoryLabel, "categoryLabel");
		map.put(TnPoiSchema._categoryId1, "1000");
		map.put(TnPoiSchema._distanceInMeter, "1.11");
		map.put(TnPoiSchema._priceRange, "2.22");
		map.put(TnPoiSchema._openHours, "12");
		map.put(TnPoiSchema._menus, "menus");
		map.put("distanceToUserInMeter", "3.33");
		com.telenav.cserver.backend.datatypes.TnPoi poi = new com.telenav.cserver.backend.datatypes.TnPoi();
		PoiSearchConverterV20.setTnPoiProperties(poi, map);
		Assert.assertEquals(2.22, poi.getPriceRange());
	}
	
	@Test
	public void convertAd()
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put(TnPoiAdSchema._shortMessage, "_shortMessage");
		map.put(TnPoiAdSchema._buyerName, "_buyerName");
		map.put(TnPoiAdSchema._endTime, System.currentTimeMillis()+"");
		map.put(TnPoiAdSchema._payPerCall, "false");
		map.put(TnPoiAdSchema._payPerClick, "false");
		map.put(TnPoiAdSchema._poundEnabled, "false");
		map.put(TnPoiAdSchema._sourceAdId, "1000");
		map.put(TnPoiAdSchema._starEnabled, "true");
		map.put(TnPoiSchema._ranking, "2");
		map.put("AdType", "5");
		map.put(TnPoiAdSchema._adSource, "_adSource");
		map.put(TnPoiAdSchema._message, "_message");
		map.put(TnPoiAdSchema._adpageUrl, "_adpageUrl");
		
		Advertisement ad = PoiSearchConverterV20.convertAd(map);
		Assert.assertEquals("_buyerName", ad.getBuyerName());
	}
	
	@Test
	public void convertOpenTableInfo()
	{
		OpenTableInfo info = null;
		Map<String,String> map = null;
		//param is null
		info = PoiSearchConverterV20.convertOpenTableInfo(map);
		Assert.assertNull(info.getPartner());
		
		//param is not null
		map = new HashMap<String, String>();
		map.put("partner", "partner");
		map.put("isReservable", "true");
		map.put("partnerPoiId", "123456");
		info = PoiSearchConverterV20.convertOpenTableInfo(map);
		Assert.assertEquals("partner", info.getPartner());
		
		map = PoiSearchConverterV20.convertOpenTableInfo(info);
		Assert.assertEquals("partner", map.get("partner"));
	}
	
	@Test
	public void offerToCoupon()
	{
		Offer offer = null;
		Coupon coupon = null;
		//param is null
		coupon = PoiSearchConverterV20.offerToCoupon(null);
		Assert.assertEquals(null, coupon.getId());
		
		offer = new Offer();
		offer.setId("1000");
		coupon = PoiSearchConverterV20.offerToCoupon(offer);
		Assert.assertEquals(offer.getId(), coupon.getId());
		
		offer = PoiSearchConverterV20.couponToOffer(coupon);
		Assert.assertEquals(coupon.getId(), offer.getId());
	}
	
	@Test
	public void wsOffersToCouponList()
	{
		Offer o1 = new Offer();
		o1.setId("1");
		Offer o2 = new Offer();
		o2.setId("2");
		
		List<Coupon> list = PoiSearchConverterV20.wsOffersToCouponList(new Offer[]{o1,o2});
		Assert.assertEquals(2, list.size());
		
		Offer[] os = PoiSearchConverterV20.wsOffersToCouponList(list);
		Assert.assertEquals(2, os.length);
	}
	
	@Test
	public void createPoiDetailsRequest()
	{
		PoiDetailsRequest request = new PoiDetailsRequest();
		request.setPoiId(1234);
		GetPoiDetailsRequest getRequest = null;
		//param is null
		getRequest = PoiSearchConverterV20.createPoiDetailsRequest(null);
		Assert.assertNull(getRequest);
		//param is not null
		getRequest = PoiSearchConverterV20.createPoiDetailsRequest(request);
		Assert.assertEquals(request.getPoiId(), getRequest.getPoiId());
	}
	
	@Test
	public void createPoiDetailsResponse()
	{
		GetPoiDetailsResponse response = new GetPoiDetailsResponse();
		PoiDetails detail = new PoiDetails();
		detail.setBrandLogo("BrandLogo");
		detail.setPoiLogo("PoiLogo");
		detail.setCategoryLogo("CategoryLogo");
		response.setDetails(detail);
		
		PoiDetailsResponse resp = null;
		//param is null
		resp = PoiSearchConverterV20.createPoiDetailsResponse(null);
		Assert.assertNull(resp);
		//param is not null
		resp = PoiSearchConverterV20.createPoiDetailsResponse(response);
		Assert.assertEquals(detail.getBrandLogo(), resp.getBrandLogo());
		
	}
}
