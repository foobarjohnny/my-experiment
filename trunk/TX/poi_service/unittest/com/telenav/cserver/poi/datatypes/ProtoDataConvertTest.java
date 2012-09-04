package com.telenav.cserver.poi.datatypes;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.backend.contents.GetReviewResponse;
import com.telenav.cserver.backend.datatypes.contents.ReviewServicePOIReview;
import com.telenav.cserver.backend.datatypes.cose.Advertisement;
import com.telenav.cserver.backend.datatypes.cose.Coupon;
import com.telenav.cserver.backend.datatypes.cose.OpenTableInfo;


public class ProtoDataConvertTest extends ProtoDataConvert {
	
	List<POI> poiList = new ArrayList<POI>();
	List<BasePoi> basePoiList = new ArrayList<BasePoi>();
	
	@Before
	public void setUp() throws Exception {
		
		POI poi = new POI();
		poiList.add(poi);
		
		poi.avgRating = 1;
		BizPOI bizPoi = new BizPOI();
		poi.bizPoi = bizPoi;
		bizPoi.phoneNumber = "5555218135";
		bizPoi.price = "1.4";
		bizPoi.brand = "brand";
		bizPoi.parentCatName = "Food";
		poi.priceRange = -1;
		poi.popularity = 2;
		poi.userPreviousRating = 1;
		Stop address = new Stop();
		bizPoi.address = address;
		
        address.firstLine = "Semiconductor Dr at Kifer Rd";
        address.city = "Santa Clara";
		address.state = "CA";
		address.country = "USA";
		address.lon = -12199916;
		address.lat = 3737456;
		address.zip = "95051";
		address.label = "";
		
		bizPoi.supplementalInfo = new String[]{"0.2", "0.3"};
		bizPoi.supportInfo = new String[]{"info1", "info2"};
		
		PoiReviewSummary reviewSummary = new PoiReviewSummary();
		poi.reviewSummary = reviewSummary; 
		
		GetReviewResponse getReviewResponse = new GetReviewResponse();
		ReviewServicePOIReview review = new ReviewServicePOIReview();
		ReviewServicePOIReview[] reviews = {review};
		getReviewResponse.setReview(reviews);
		poi.getReviewResponse = getReviewResponse;
		
		review.setRating("10");
		review.setPoiId(1234);
		review.setReviewId(1);
		review.setReviewerName("review name");
		review.setReviewText("reviewtext");
		review.setUpdateTime(new Date(System.currentTimeMillis()));
		
		Advertisement ad = new Advertisement();
		poi.ad = ad;
		ad.setMessage("message");
		ad.setSourceAdId("123");
		
		OpenTableInfo openTableInfo = new OpenTableInfo();
		poi.openTableInfo = openTableInfo;
		
		openTableInfo.setPartner("parent");
		openTableInfo.setPartnerPoiId("parentId");
		openTableInfo.setReservable(true);
		
		poi.menu = "menu";
		
		Coupon coupon = new Coupon();
		List<Coupon> couponList = new ArrayList<Coupon>();
		couponList.add(coupon);
		poi.couponList = couponList;
		
		Review theReview = new Review();
		List<Review> reviewList = new ArrayList<Review>();
		reviewList.add(theReview);
		theReview.reviewString = "";
		poi.reviews = reviewList;
		
		
        
        BasePoi basePoi = new BasePoi();
		basePoiList.add(basePoi);
		basePoi.poiId = 1;
		basePoi.popularity = 3;
		basePoi.isRatingEnable = true;
		basePoi.avgRating = 1;
		basePoi.hasUserRatedThisPoi = false;
		basePoi.bizPOI = bizPoi;
		BasePoiExtraInfo basePoiExtraInfo = new BasePoiExtraInfo();

		basePoiExtraInfo.userPreviousRating = 3;
		basePoiExtraInfo.ratingNumber = 1;
		basePoiExtraInfo.shortMessage = "SMS";
		basePoiExtraInfo.message = "MSG";
		basePoiExtraInfo.sourceAdId = "1234";
		basePoiExtraInfo.isReservable = false;
		basePoiExtraInfo.partnerPoiId = "1000";
		basePoiExtraInfo.partner = "YelP";
		basePoiExtraInfo.poiNameAudio = new byte[]{0x1, 0x2};
		
		HashMap<String, String> extraProperties = new HashMap<String, String>();
		extraProperties.put("purpose", "UnitTest");
		basePoiExtraInfo.extraProperties = extraProperties;
		basePoi.basePoiExtraInfo = basePoiExtraInfo;
	    
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testHandlePOI() {
		
		ProtoDataConvert.handlePOI(poiList);
	}

	@Test
	public void testHandleBasePoi() {
		ProtoDataConvert.handleBasePoi(basePoiList);
	}

}
