/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.cose;

import com.telenav.cserver.backend.datatypes.Constants;
import com.telenav.cserver.backend.datatypes.TnPoi;
import com.telenav.cserver.backend.util.MockData;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.kernel.util.datatypes.TnContext;

import junit.framework.TestCase;

/**
 * PoiSearchTestCase.java
 *
 * @author bhu@telenav.cn
 * @version 1.0 2009-7-15
 */
public class TestPoiSearch extends TestCase
{
	protected void setUp() throws Exception {
	}
	protected void tearDown() throws Exception {
	}

	
	public void testSearchAirport()
	{
	    AirportSearchRequest req = new AirportSearchRequest();
	    req.setTransactionId("");
	    req.setAirportQuery("Sfo");
	    req.setCountryList("");
	    
	    TnContext tc = new TnContext();
        tc.addProperty(TnContext.PROP_POI_DATASET, "YPC");
        tc.addProperty("dataset", "Navteq");
        tc.addProperty(TnContext.PROP_AD_NEEDSPONSOR, "TRUE");
        tc.addProperty(TnContext.PROP_AD_ENGINE, "CITYSEARCH");
        tc.addProperty("adtype","SPONSORED_TEXT,MERCHANT_CONTENT,COUPON,MENU");
        
        PoiSearchProxy proxy = CoseFactory.createPoiSearchProxy(tc);
        PoiSearchResponse resp; 
        
        try
        {
            resp = proxy.searchAirport(req);
            assertNotNull(resp);
            assertSame(0,resp.getPoiSearchStatus());
            
        }
        catch (ThrottlingException e)
        {
            e.printStackTrace();
        }
	}
	public void testSearchWithinDistance()
	{
		PoiSearchRequest req = new PoiSearchRequest();
		req.setTransactionId("ddd");
		req.setAnchor(MockData.mockAddress());
		req.setCategoryId(-1);
		req.setCategoryVersion("YP50");
		
		req.setRadiusInMeter(8000);
		req.setHierarchyId(1);
		req.setNeedSponsoredPois(true);
		req.setNeedUserGeneratePois(false);
		req.setNeedUserPreviousRating(true);
		req.setPageNumber(0);
		req.setPageSize(10);
		req.setPoiQuery("ATM");
		req.setPoiSortType(Constants.SORT_BY_POPULAE);
		req.setOnlyMostPopular(true);
		req.setUserId(8849778);
		TnContext tc = new TnContext();
		tc.addProperty(TnContext.PROP_POI_DATASET, "YPC");
		tc.addProperty("dataset", "Navteq");
		tc.addProperty(TnContext.PROP_AD_NEEDSPONSOR, "TRUE");
	    tc.addProperty(TnContext.PROP_AD_ENGINE, "CITYSEARCH,TELENAV");
	    tc.addProperty("adtype","SPONSORED_TEXT,MERCHANT_CONTENT,COUPON,MENU");
	      
		PoiSearchProxy proxy = CoseFactory.createPoiSearchProxy(tc);
		PoiSearchResponse resp;
        try
        {
            resp = proxy.searchWithinDistance(req);
            assertNotNull(resp);
            assertSame(0,resp.getPoiSearchStatus());
        }
        catch (ThrottlingException e)
        {
            e.printStackTrace();
        }

        // new case
        resp = null;
        req.setPageSize(9);
        try
        {
        	resp = proxy.searchWithinDistance(req);
            assertNotNull(resp);
            assertSame(0, resp.getPoiSearchStatus());
		}
        catch (ThrottlingException e)
		{
			e.printStackTrace();
		}
	}
	
//	public void testSearchWithinDistance_CN()
//    {
//        PoiSearchRequest req = new PoiSearchRequest();
//        req.setRegion("CN");
//        req.setTransactionId("ddd");
//        req.setAnchor(MockData.mockAddress_CN());
//        req.setCategoryId(-1);
//        req.setCategoryVersion("YP50");
//        
//        req.setRadiusInMeter(8000);
//        req.setHierarchyId(1);
//        req.setNeedSponsoredPois(true);
//       // req.setNeedUserGeneratePois(true);
//        req.setNeedUserPreviousRating(true);
//        req.setPageNumber(0);
//        req.setPageSize(10);
//        req.setPoiQuery("hotel");
//        req.setPoiSortType(Constants.SORT_BY_POPULAE);
//        req.setOnlyMostPopular(false);
//        req.setUserId(8849778);
//        TnContext tc = new TnContext();
//        tc.addProperty(TnContext.PROP_POI_DATASET, "YPC");
//        tc.addProperty("dataset", "Navteq");
//        tc.addProperty(TnContext.PROP_AD_NEEDSPONSOR, "TRUE");
//        tc.addProperty(TnContext.PROP_AD_ENGINE, "CITYSEARCH,TELENAV");
//        tc.addProperty("adtype","SPONSORED_TEXT,MERCHANT_CONTENT,COUPON,MENU");
//          
//        PoiSearchProxy proxy = CoseFactory.createPoiSearchProxy(tc);
//        PoiSearchResponse resp;
//        try
//        {
//            resp = proxy.searchWithinDistance(req);
//            assertNotNull(resp);
//            assertSame(0,resp.getPoiSearchStatus());
//        }
//        catch (ThrottlingException e)
//        {
//            e.printStackTrace();
//        }
//    }
//	public void testSearchInCity()
//	{
//		PoiSearchRequest req = new PoiSearchRequest();
//		req.setRegion("CN");
//		req.setUserId(123);
//		req.setTransactionId(null);
//		req.setAnchor(MockData.mockCity());//.mockAddress());
//		req.setCategoryId(-1);
//		req.setCategoryVersion("YP50");
//		req.setRadiusInMeter(7000);
//		req.setHierarchyId(1);
//		req.setNeedSponsoredPois(true);
//		req.setNeedUserGeneratePois(false);
//		req.setNeedUserPreviousRating(true);
//		req.setPageNumber(0);
//		req.setPageSize(10);
//		req.setPoiQuery("hotel");
//		req.setPoiSortType(Constants.SORT_BY_DISTANCE);
//		//req.setUserId(userId);
//		TnContext tc = new TnContext();
//        tc.addProperty(TnContext.PROP_POI_DATASET, "YPC");
//        tc.addProperty("dataset", "Navteq");
//        tc.addProperty(TnContext.PROP_AD_NEEDSPONSOR, "TRUE");
//        tc.addProperty(TnContext.PROP_AD_ENGINE, "CITYSEARCH,TELENAV");
//        tc.addProperty("adtype","SPONSORED_TEXT,MERCHANT_CONTENT,COUPON,MENU");
//          
//		PoiSearchProxy proxy = CoseFactory.createPoiSearchProxy(tc);
//		PoiSearchResponse resp;
//        try
//        {
//            resp = proxy.searchWithinCity(req);
//            assertNotNull(resp);
//            assertSame(0,resp.getPoiSearchStatus());
//        }
//        catch (ThrottlingException e)
//        {
//            e.printStackTrace();
//        }
//	}
}
