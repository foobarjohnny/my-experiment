package com.telenav.cserver.backend.poi;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


import com.telenav.datatypes.content.cose.v10.CategoryParam;
import com.telenav.datatypes.content.cose.v10.PoiDataSet;
import com.telenav.datatypes.content.cose.v10.PoiSearchArea;
import com.telenav.datatypes.content.cose.v10.PoiSearchAreaType;
import com.telenav.datatypes.content.cose.v10.PoiSearchResponseStatus;
import com.telenav.datatypes.content.cose.v10.PoiSearchService;
import com.telenav.datatypes.content.cose.v10.PoiSearchType;
import com.telenav.datatypes.content.cose.v10.PoiSortType;
import com.telenav.datatypes.content.cose.v10.ServiceAgent;
import com.telenav.datatypes.content.cose.v10.ServiceVendor;
import com.telenav.datatypes.content.tnpoi.v10.PoiRichContent;
import com.telenav.datatypes.content.tnpoi.v10.TnPoi;
import com.telenav.datatypes.content.tnpoi.v10.TnPoiSchema;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.services.content.v10.ContentInfoServiceStub;
import com.telenav.services.content.v10.ContentSearchServiceStub;
import com.telenav.services.content.v10.GetLogoRequest;
import com.telenav.services.content.v10.GetLogoResponse;
import com.telenav.services.content.v10.GetPoiDetailsRequest;
import com.telenav.services.content.v10.GetPoiDetailsResponse;
import com.telenav.services.content.v10.GetPoiExtraAttributesRequest;
import com.telenav.services.content.v10.GetPoiExtraAttributesResponse;
import com.telenav.services.content.v10.GetPoiMenuRequest;
import com.telenav.services.content.v10.GetPoiMenuResponse;
import com.telenav.services.content.v10.GetPoiRichContentRequest;
import com.telenav.services.content.v10.GetPoiRichContentResponse;
import com.telenav.services.content.v10.PoiRequest;
import com.telenav.services.content.v10.PoiSearchResponse;
import com.telenav.ws.datatypes.address.GeoCode;
import com.telenav.ws.datatypes.common.Property;
import com.telenav.ws.datatypes.services.ResponseCodeEnum;


public class ContentSearchServiceStubTest {

	private static final Object HOTEL_ALLIANCE_VENDOR_CODE = "P_HA";
	
	private static long[] additionalVendorsPoiIds = new long[]{3000538456L,3427839578L,3428745431L,3000539646L,
		3432637363L,3426845496L,3411883913L,3421715226L,3000622556L,3421670233L};
	private Set<Long> additionalVendorsPoiIdSet = new HashSet<Long>(additionalVendorsPoiIds.length);
	
	private ContentSearchServiceStub stub = null;
	private static final String CONTENT_SEARCH_SERVICE_URL = "http://f6-3:8080/tnws/services/ContentSearchService";
	
	
	@Before
	public void setUp() throws Exception {
		stub = new ContentSearchServiceStub(null, CONTENT_SEARCH_SERVICE_URL);
		for(long poiId : additionalVendorsPoiIds) {
			additionalVendorsPoiIdSet.add(poiId);
		}
	}

	@Test
	public void testAddtionalVendors() throws Exception {
		PoiRequest request = getHotelsRequest();	

		PoiSearchResponse response = stub.searchPoi(request);
		assertNotNull(response);
		String statusCode = response.getResponseStatus().getStatusCode();
		assertNotNull(statusCode, PoiSearchResponseStatus._FOUND);
		TnPoi[] pois = response.getPois();
		assertNotNull(pois);
		assertTrue(pois.length > 0);
		
		for(TnPoi poi : pois) {
			if(poi == null || !additionalVendorsPoiIdSet.contains(poi.getPoiId())) {
				continue;
			}
			
			String[] additionalVendorCodes = getAdditionalVendorCodes(poi);
			
			assertNotNull(additionalVendorCodes);
			
			boolean hasHotelAlliance = false;
			for(String vendorCode : additionalVendorCodes) {
				if(vendorCode.equals(HOTEL_ALLIANCE_VENDOR_CODE)){
					hasHotelAlliance = true;
				}
			}
			assertTrue(hasHotelAlliance);
		}

	}

	private String[] getAdditionalVendorCodes(TnPoi poi) {
		Property[] properties = poi.getProperties();
		assertNotNull(properties);			
		for(Property property : properties) {
			if(property == null) {
				continue;
			}
			
			if(property.getKey().equals(TnPoiSchema._additionalVendors)) {
				String  strVendorCodes = property.getValue();
				assertNotNull(strVendorCodes);
				String[] vendorCodes = strVendorCodes.split(",");
				return vendorCodes;
			}
		}
		return null;
	}

	private PoiRequest getHotelsRequest() {

		String dataset = "TA";
		int page = 0;

		boolean needSponsor = true;
		boolean mostPopular = false;
		boolean rating = false;

		long hierarchyId = 1L;
		String versionId = "YP50";
		long[] categoryId = new long[1];
		categoryId[0] = 595L;

		int PAGE_SIZE = 20;

		TnContext context = new TnContext();
		context.addProperty("poidataset", dataset);
		context.addProperty("searchunit","KM");
		context.addProperty("needsponsor","TRUE");
		context.addProperty("adengine","CITYSEARCH,TELENAV");
		context.addProperty("adtype","MERCHANT_CONTENT,COUPON,MENU,SPONSORED_TEXT");

		PoiRequest request = new PoiRequest();
		request.setAutoExpandSearchRadius(true);
		request.setClientName("clientname");
		request.setClientVersion("clientversion");
		//request.setIsDebugRequest(true);
		request.setContextString(context.toString());
		request.setUserId(88888888L);
		request.setTransactionId(String.valueOf(System.currentTimeMillis()));
		request.setSearchType(PoiSearchType.FOR_GENERAL_POI);

		request.setNeedSponsoredPois(needSponsor);
		request.setOnlyMostPopular(mostPopular);
		request.setNeedUserPreviousRating(rating);
		request.setSortType(PoiSortType.BY_RANKING);

		request.setPoiNumber(PAGE_SIZE);
		request.setPoiStartIndex(page * PAGE_SIZE);
		request.setSponsorListingNumber(1);
		request.setSponsorListingStartIndex(0);

		if (categoryId[0] != -1L) {
			CategoryParam category = new CategoryParam();
			category.setCategoryHierarchyId(hierarchyId);
			category.setCategoryVersion(versionId);
			category.setCategoryId(categoryId);
			request.setCategoryParam(category);
		}

		request.setPoiQuery("");
		PoiSearchArea area = new PoiSearchArea();
		area.setAreaType(PoiSearchAreaType.ANCHOR_AND_RADIUS);
		GeoCode geo = new GeoCode();
		geo.setLatitude(37.36995d);
		geo.setLongitude(-121.9991d);
		area.setAnchor(geo);
		area.setRadiusInMeter(8045);
		request.setSearchArea(area);

		ServiceAgent agent = new ServiceAgent();
		try {
			agent.setDataSet((PoiDataSet) PoiDataSet.class.getField(dataset).get(null));
		} catch (Exception e) {
			// will never happen, ignore
		}
		agent.setService(PoiSearchService.TELENAV_COSE);
		agent.setVendor(ServiceVendor.TELENAV);
		request.setServiceAgent(agent);

		return request;
	}	
}
