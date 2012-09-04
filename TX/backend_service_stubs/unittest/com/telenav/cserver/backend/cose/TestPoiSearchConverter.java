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
import com.telenav.datatypes.content.cose.v10.PoiSearchArea;
import com.telenav.datatypes.content.cose.v10.PoiSortType;
import com.telenav.datatypes.content.tnpoi.v10.Offer;
import com.telenav.datatypes.content.tnpoi.v10.PoiDetails;
import com.telenav.datatypes.content.tnpoi.v10.TnPoi;
import com.telenav.datatypes.content.tnpoi.v10.TnPoiAdSchema;
import com.telenav.datatypes.content.tnpoi.v10.TnPoiSchema;
import com.telenav.datatypes.content.tnpoi.v10.TnPriceRecord;
import com.telenav.services.content.v10.GetPoiDetailsRequest;
import com.telenav.services.content.v10.GetPoiDetailsResponse;
import com.telenav.services.content.v10.PoiRequest;
import com.telenav.services.content.v10.PoiSearchResponse;
import com.telenav.ws.datatypes.common.Property;

public class TestPoiSearchConverter 
{
	@Test
	public void airportRequesttoWSRequest()
	{
		AirportSearchRequest request = null;
		PoiRequest wsReq;
		//null
		wsReq = PoiSearchConverter.airportRequesttoWSRequest(null);
		Assert.assertNull(wsReq);
		
		//not null, country list is null
		request = new AirportSearchRequest();
		request.setAirportQuery("test");
		request.setPoiSortType(0);		
		wsReq = PoiSearchConverter.airportRequesttoWSRequest(request);
		Assert.assertEquals(request.getAirportQuery(), wsReq.getPoiQuery());
		
		//not null, country list isn't null
		request.setCountryList("countryList");
		wsReq = PoiSearchConverter.airportRequesttoWSRequest(request);
		Assert.assertEquals(request.getCountryList(), wsReq.getSearchArea().getCountryList());
	}
	
	@Test
	public void convertRoutePointArray()
	{
		PoiSearchRequest poiRequest = null;
		PoiRequest wsReq;
		//null
		wsReq = PoiSearchConverter.poiRequesttoWSRequest(null);
		Assert.assertNull(wsReq);
		
		//
		poiRequest = new PoiSearchRequest();
		poiRequest.setCategoryId(1000);
		poiRequest.setHierarchyId(123);
		wsReq = PoiSearchConverter.poiRequesttoWSRequest(poiRequest);
		Assert.assertEquals(poiRequest.getCategoryId(), wsReq.getCategoryParam().getCategoryId()[0]);
		//
		poiRequest.setPageSize(20);
		poiRequest.setPageNumber(1);
		wsReq = PoiSearchConverter.poiRequesttoWSRequest(poiRequest);
		Assert.assertEquals(20, wsReq.getPoiStartIndex());;
	}
	
	@Test
	public void convertToSortType()
	{
		Assert.assertTrue(PoiSortType._BY_DISTANCE.equals(PoiSearchConverter.convertToSortType(0).getValue()));
		Assert.assertTrue(PoiSortType._BY_RATING.equals(PoiSearchConverter.convertToSortType(1).getValue()));
		Assert.assertTrue(PoiSortType._BY_POPULAR.equals(PoiSearchConverter.convertToSortType(2).getValue()));
		Assert.assertTrue(PoiSortType._BY_RANKING.equals(PoiSearchConverter.convertToSortType(3).getValue()));
		Assert.assertTrue(PoiSortType._BY_GASPRICE.equals(PoiSearchConverter.convertToSortType(4).getValue()));
	}
	
	@Test
	public void convertSearchArea()
	{
		PoiSearchRequest poiRequest = new PoiSearchRequest();
		PoiSearchArea searchArea = null;
		//request is null
		searchArea = PoiSearchConverter.convertSearchArea(poiRequest);
		Assert.assertNull(searchArea);
		//request is not null && route poins is null
		Address addr = new Address();
		addr.setCityName("city");
		poiRequest.setAnchor(addr);
		searchArea = PoiSearchConverter.convertSearchArea(poiRequest);
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
		searchArea = PoiSearchConverter.convertSearchArea(poiRequest);
		Assert.assertTrue(searchArea.getRoutePoints().length == 2);
	}

	@Test
	public void convertWSRespToPoiSearchResponse()
	{
		PoiSearchResponse wsResp = null;
		com.telenav.cserver.backend.cose.PoiSearchResponse resp = null;
		//param is null
		resp = PoiSearchConverter.convertWSRespToPoiSearchResponse(null);
		Assert.assertEquals(ErrorCode.POI_STATUS_COSE_ERROR, resp.getPoiSearchStatus());
		//
		wsResp = new PoiSearchResponse();
		wsResp.setPois(new TnPoi[]{new TnPoi(), new TnPoi()});
		wsResp.setSponsorPois(new TnPoi[]{new TnPoi(), new TnPoi()});
		resp = PoiSearchConverter.convertWSRespToPoiSearchResponse(wsResp);
		Assert.assertTrue(resp.getPois().size() == 2);
		Assert.assertTrue(resp.getSponsorPois().size() == 2);
	}
	
	@Test
	public void handleLocalAppRelatedInfo()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(TnPoiSchema._additionalVendors, "VendorOne,P_HA");
		List<LocalAppInfo> list = PoiSearchConverter.handleLocalAppRelatedInfo(map);
		Assert.assertTrue(list.size()>0);
		
		map.put(LocalAppInfoConstants.HOTEL_ALLIANCE_VENDOR_CODE, "alliance_vendor");
		Assert.assertTrue(Boolean.valueOf(list.get(0).getProps().get(LocalAppInfoConstants.LOCAL_APP_HOTEL_HASHOTELALLICANCE)));
	}
	
	@Test
	public void wsRviewSummaryToTnPoiReviewSummary()
	{
		TnPoiReviewSummary summary = null;
		com.telenav.datatypes.content.tnpoi.v10.TnPoiReviewSummary wsSummary = new com.telenav.datatypes.content.tnpoi.v10.TnPoiReviewSummary();
		//param is null
		summary = PoiSearchConverter.wsRviewSummaryToTnPoiReviewSummary(null);
		Assert.assertNull(summary);
		//param is not null
		wsSummary.setPoiId(1000);
		summary = PoiSearchConverter.wsRviewSummaryToTnPoiReviewSummary(wsSummary);
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
		List<GasPriceInfo> list = PoiSearchConverter.convertPriceInfoList(prices);
		Assert.assertTrue(list.size() == 2);
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
				
		Map<String, String> map = PoiSearchConverter.convertPropertyMap(new Property[]{p, p1});
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
		PoiSearchConverter.setPoiExtraInfo(poi, map);
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
		PoiSearchConverter.setTnPoiProperties(poi, map);
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
		
		Advertisement ad = PoiSearchConverter.convertAd(map);
		Assert.assertEquals("_buyerName", ad.getBuyerName());
	}
	
	@Test
	public void convertOpenTableInfo()
	{
		OpenTableInfo info = null;
		Map<String,String> map = null;
		//param is null
		info = PoiSearchConverter.convertOpenTableInfo(map);
		Assert.assertNull(info.getPartner());
		
		//param is not null
		map = new HashMap<String, String>();
		map.put("partner", "partner");
		map.put("isReservable", "true");
		map.put("partnerPoiId", "123456");
		info = PoiSearchConverter.convertOpenTableInfo(map);
		Assert.assertEquals("partner", info.getPartner());
	}
	
	@Test
	public void offerToCoupon()
	{
		Offer offer = null;
		Coupon coupon = null;
		//param is null
		coupon = PoiSearchConverter.offerToCoupon(null);
		Assert.assertEquals(null, coupon.getId());
		
		offer = new Offer();
		offer.setId("1000");
		coupon = PoiSearchConverter.offerToCoupon(offer);
		Assert.assertEquals(offer.getId(), coupon.getId());
	}
	
	@Test
	public void wsOffersToCouponList()
	{
		Offer o1 = new Offer();
		o1.setId("1");
		Offer o2 = new Offer();
		o2.setId("2");
		
		List<Coupon> list = PoiSearchConverter.wsOffersToCouponList(new Offer[]{o1,o2});
		Assert.assertEquals(2, list.size());
	}
	
	@Test
	public void createPoiDetailsRequest()
	{
		PoiDetailsRequest request = new PoiDetailsRequest();
		request.setPoiId(1234);
		GetPoiDetailsRequest getRequest = null;
		//param is null
		getRequest = PoiSearchConverter.createPoiDetailsRequest(null);
		Assert.assertNull(getRequest);
		//param is not null
		getRequest = PoiSearchConverter.createPoiDetailsRequest(request);
		Assert.assertEquals(request.getPoiId(), getRequest.getPoiId());
	}
	
	@Test
	public void createPoiDetailsResponse()
	{
		GetPoiDetailsResponse response = new GetPoiDetailsResponse();
		PoiDetails detail = new PoiDetails();
		detail.setPoiId(1234);
		response.setDetails(detail);
		
		PoiDetailsResponse resp = null;
		//param is null
		resp = PoiSearchConverter.createPoiDetailsResponse(null);
		Assert.assertNull(resp);
		//param is not null
		resp = PoiSearchConverter.createPoiDetailsResponse(response);
		Assert.assertEquals(detail.getPoiId(), resp.getPoiId());
		
	}
}
