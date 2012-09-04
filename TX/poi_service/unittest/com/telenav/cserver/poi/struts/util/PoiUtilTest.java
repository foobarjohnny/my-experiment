package com.telenav.cserver.poi.struts.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.telenav.cserver.TestUtil;
import com.telenav.cserver.backend.contents.GetReviewResponse;
import com.telenav.cserver.backend.datatypes.contents.ReviewServicePOIReview;
import com.telenav.cserver.backend.datatypes.cose.Advertisement;
import com.telenav.cserver.backend.datatypes.cose.Coupon;
import com.telenav.cserver.backend.datatypes.cose.OpenTableInfo;
import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.cserver.poi.datatypes.BizPOI;
import com.telenav.cserver.poi.datatypes.POI;
import com.telenav.cserver.poi.datatypes.PoiReviewSummary;
import com.telenav.cserver.poi.datatypes.Stop;
import com.telenav.cserver.util.MockHttpServletRequest;
import com.telenav.cserver.util.helper.Log2TxNode;
import com.telenav.tnbrowser.util.DataHandler;

public class PoiUtilTest extends PoiUtil {

	static DataHandler handler = null;
	static MockHttpServletRequest request = null;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		if(handler == null || request == null)
		{
			request = (MockHttpServletRequest)Log2TxNode.getInstance().log2TxNode2HttpServletRequest(request, "", 110);
			handler = (DataHandler)request.getAttribute(BrowserFrameworkConstants.CLIENT_INFO);
		}
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetCountry() 
	{
		String xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><null/>";
		MockHttpServletRequest request = new MockHttpServletRequest(xmlStr.getBytes());
		request.addParameter(DataHandler.KEY_LOCALE, "pt_BR");

		DataHandler handler = new DataHandler(request);
		Assert.assertEquals("BR",getCountry(handler));
		
		
		request = new MockHttpServletRequest(xmlStr.getBytes());
		request.addParameter(DataHandler.KEY_CARRIER, "Telcel");
		request.addParameter(DataHandler.KEY_LOCALE, "en_US");
		
		handler = new DataHandler(request);
		Assert.assertEquals("MX",getCountry(handler));
		
		
		request = new MockHttpServletRequest(xmlStr.getBytes());
		request.addParameter(DataHandler.KEY_CARRIER, "NII");
		request.addParameter(DataHandler.KEY_LOCALE, "en_US");
		
		handler = new DataHandler(request);
		Assert.assertEquals("MX",getCountry(handler));
		
		
		request = new MockHttpServletRequest(xmlStr.getBytes());
		request.addParameter(DataHandler.KEY_CARRIER, "ATT");
		request.addParameter(DataHandler.KEY_LOCALE, "en_US");
		
		handler = new DataHandler(request);
		Assert.assertEquals("US",getCountry(handler));

	}
	
	@Test
	public void testGetXMLString() {
		PoiUtil.getXMLString("");
	}

	@Test
	public void testGetString() {
		PoiUtil.getString("");
	}

	@Test
	public void testAmend() {
		PoiUtil.amend("<![CDATA[ ]]>");
	}

	@Test
	public void testFilterLastPara() {
		PoiUtil.filterLastPara("jsp;jsp");
	}

	@Test
	public void testConvertAddress() throws JSONException {
		JSONObject jo = new JSONObject();
		jo.put("lat", 100000);
		jo.put("lon", 200000);
        jo.put("label", "label");
        jo.put("firstLine", "firstline");
        jo.put("city", "city");
        jo.put("state", "state");
        jo.put("zip", "zip");
        jo.put("country", "country");
		PoiUtil.convertAddress(jo);
	}

	@Test
	public void testGetTnContext() {

		PoiUtil.getTnContext(handler);
	}

	@Test
	public void testConvertToDegree() {
		PoiUtil.convertToDegree(100000);
	}

	@Test
	public void testGetUserId() {
		PoiUtil.getUserId(handler);
	}

	@Test
	public void testGetUserName() {
		PoiUtil.getUserName(handler);
	}

	@Test
	public void testIsTouchScreen() {
		PoiUtil.isTouchScreen(handler);
	}

	@Test
	public void testGetUserProfile() {
		PoiUtil.getUserProfile(handler);
	}

	@Test
	public void testIsAndroid61() {
		PoiUtil.isAndroid61(handler);
	}

	@Test
	public void testIsRimTouch() {
		PoiUtil.isRimNonTouch(handler);
	}

	@Test
	public void testIsRimNonTouch() {
		PoiUtil.isRimNonTouch(handler);
	}

	@Test
	public void testIsDual() {
		PoiUtil.isDual(handler);
	}

	@Test
	public void testShowPostLocationAndOpenTable() {
		PoiUtil.showPostLocationAndOpenTable(handler);
	}

	@Test
	public void testShowPageIndex() {
		PoiUtil.showPageIndex(handler);
	}

	@Test
	public void testIsWarrior() {
		PoiUtil.isWarrior(handler);
	}

	@Test
	public void testNeedsBrandListWhenStartUp() {
		PoiUtil.needsBrandListWhenStartUp(handler);
	}

	@Test
	public void testLoadHotBrand() throws ThrottlingException {
		PoiUtil.loadHotBrand(request);
	}

	@Test
	public void testToTxNode() {
		
		POI poi = new POI();
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
		
		coupon.setDescription("description");
		coupon.setEndDate(new Date(System.currentTimeMillis()));
		byte[] image = {0x1, 0x2};
		coupon.setImage(image);
		

		PoiUtil.toTxNode(poi, 1234);
	}

	@Test
	public void testGetLongFrom() {
		PoiUtil.getLongFrom("100");
	}

	@Test
	public void testGetNewFreemiumSupportDataHandler() {
		PoiUtil.getNewFreemiumSupport(handler);
	}

	@Test
	public void testGetNewFreemiumSupportUserProfile() {
		PoiUtil.getNewFreemiumSupport(TestUtil.getUserProfile());
	}

}
