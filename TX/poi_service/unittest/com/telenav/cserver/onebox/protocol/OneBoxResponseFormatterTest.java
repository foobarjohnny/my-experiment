package com.telenav.cserver.onebox.protocol;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.apache.struts.mock.MockHttpServletRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.backend.contents.GetReviewResponse;
import com.telenav.cserver.backend.datatypes.contents.ReviewServicePOIReview;
import com.telenav.cserver.backend.datatypes.cose.Advertisement;
import com.telenav.cserver.backend.datatypes.cose.Coupon;
import com.telenav.cserver.backend.datatypes.cose.OpenTableInfo;
import com.telenav.cserver.onebox.executor.OneBoxResponse;
import com.telenav.cserver.poi.datatypes.BizPOI;
import com.telenav.cserver.poi.datatypes.POI;
import com.telenav.cserver.poi.datatypes.PoiConstants;
import com.telenav.cserver.poi.datatypes.PoiReviewSummary;
import com.telenav.cserver.poi.datatypes.Stop;
import com.telenav.cserver.poi.executor.POISearchResponse_WS;
import com.telenav.cserver.poi.struts.Constant;
import com.telenav.resource.data.AudioElement;
import com.telenav.resource.data.AudioMessage;
import com.telenav.resource.data.AudioRule;
import com.telenav.resource.data.PromptItem;
import com.telenav.resource.data.ResourceInfo;
import com.telenav.services.search.onebox.v10.QuerySuggestion;
import com.telenav.ws.datatypes.address.Address;
import com.telenav.ws.datatypes.address.GeoCode;

public class OneBoxResponseFormatterTest extends OneBoxResponseFormatter{

	OneBoxResponse response = new OneBoxResponse();
	MockHttpServletRequest request = new MockHttpServletRequest();
	
	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testParseBrowserResponseHttpServletRequestExecutorResponse() {
		try
		{
			int[] Types = {Constant.OneBox.ADDRESS_RESULT, Constant.OneBox.POI_RESULT, Constant.OneBox.DID_YOU_MEAN, 10};
			for(int i = 0; i < Types.length; i++)
			{
				if(Types[i] == Constant.OneBox.ADDRESS_RESULT)
				{
					response = new OneBoxResponse();
					response.setResultType(Constant.OneBox.ADDRESS_RESULT);
					Address addr = new Address();
					List<Address> addressList = new ArrayList<Address>();
					addressList.add(addr);
					response.setAddressList(addressList);
					
			        addr.setFirstLine("Semiconductor Dr at Kifer Rd");
			        addr.setCity("Santa Clara");
					addr.setState("CA");
					addr.setCountry("USA");
					GeoCode geoCode = new GeoCode();
					geoCode.setLatitude(3737456);
					geoCode.setLongitude(-12199916);
					addr.setGeoCode(geoCode);
					addr.setPostalCode("95051");
					addr.setLabel("label");

				}else if(Types[i] ==  Constant.OneBox.DID_YOU_MEAN){
					
					response = new OneBoxResponse();
					response.setResultType(Constant.OneBox.DID_YOU_MEAN);
					
					QuerySuggestion suggest = new QuerySuggestion();
					QuerySuggestion[] suggestions = {suggest};
					response.setSuggestions(suggestions);
					
					suggest.setDisplayLabel("displayLabel");
					suggest.setQuery("queryString");
					
					
				}else if(Types[i] == Constant.OneBox.POI_RESULT){
					response = new OneBoxResponse();
					response.setResultType(Constant.OneBox.POI_RESULT);
					POISearchResponse_WS poiResp = new POISearchResponse_WS();
					response.setPoiResp(poiResp);
					
					poiResp.setDistanceUnit(PoiConstants.UNIT_METER);
					POI poi = new POI();
					List<POI> poiList = new ArrayList<POI>();
					poiList.add(poi);
					poiResp.setPoiList(poiList);
					
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
					
					poiResp.setSponsorListingNumber(1);
					poiResp.setSponsorPoiList(poiList);
					
					PromptItem promptItem = new PromptItem();
					PromptItem[] promptItems = {promptItem};
					poiResp.setPromptItems(promptItems);
					
					AudioMessage aduioElement1 = new AudioMessage();
					AudioRule aduioElement2 = new AudioRule();
					
					ResourceInfo resourceInfo = new ResourceInfo();
					aduioElement1.setResourceInfo(resourceInfo);
					AudioMessage child = new AudioMessage();
					child.setChildren(null);
					child.setResourceInfo(resourceInfo);
					AudioMessage[] childs = {child};
					aduioElement1.setChildren(childs);
					
					
					aduioElement2.setRuleId(0);
					int[] intArgs = {0x1, 0x2};
					aduioElement2.setIntArgs(intArgs);
					aduioElement2.setNodeArgs(childs);

					AudioElement[] audioElements = {aduioElement1, aduioElement2};
					promptItem.setAudioElements(audioElements);
					
					
				}else
				{
					response = new OneBoxResponse();
				}
				
				this.parseBrowserResponse(request, response);
			}
		}catch(Throwable e)
		{
			Assert.fail();
		}
	}
}
