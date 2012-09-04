/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.html.executor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.telenav.cserver.html.datatypes.AdsDetail;
import com.telenav.cserver.html.datatypes.AdsOffer;
import com.telenav.cserver.html.datatypes.GasPrice;
import com.telenav.cserver.html.datatypes.HotelItem;
import com.telenav.cserver.html.datatypes.MenuItem;
import com.telenav.cserver.html.datatypes.PoiDetail;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.kernel.util.datatypes.TnContext;
/**
 * @TODO	Create dummy data for various business better testing
 * @author  
 * @version 1.0 
 */
public class HtmlServiceProxyDummy {

	private static HtmlServiceProxyDummy instance = new HtmlServiceProxyDummy();
	
    public static HtmlServiceProxyDummy getInstance()
    {
        return instance;
    }
    /**
     * TODO		Create dummy data for ad.
     * @param adsRequest
     * @param adsResponse
     * @param tc
     * @return
     */
	public  HtmlAdsResponse getDummyAds(HtmlAdsRequest adsRequest,HtmlAdsResponse adsResponse,TnContext tc)
	{
		String tagLine = "12345 678901 23456 7890 1234567 890123 45678901 234567 89012 3456789 0123 45678 9012 345678 9012 3456 7890123 456 7890";
		//String tagLine = "12345 678901 234567 890123 4567890"; 
		
		AdsDetail adsDetail = new AdsDetail();
		
		//adsDetail.setBrandName("StartBucks0123 StartBucks0123");
		adsDetail.setBrandName("StartBucks01 StartBucks01");
		adsDetail.setTagline(tagLine);
		adsDetail.setLogoName("http://www.google.com.hk/intl/zh-CN/images/logo_cn.png");
		adsDetail.setLogoImage("");
		adsDetail.setPhoneNo("4087308117");
		adsDetail.setAdSource("TN");
		adsDetail.setPoiId("1234567890");
		adsDetail.setPoiType("3");
		
		Stop address = new Stop();
		address.firstLine = "1130 KIFER ROAD";
		address.state = "CA";
		address.city = "SUNNYVALE";
		address.lat = 3737390;
		address.lon = -12199464;
		address.stopId = "0";
		adsDetail.setAddress(address);
		
		adsDetail.setHasPoiMenu(true);
		MenuItem menu = new MenuItem();
		menu.setMenuText("Here is the menu text.\nHere is the menu text.\nHere is the menu text.\nHere is the menu text.\nHere is the menu text.\nHere is the menu text.\nHere is the menu text.\n");
		adsDetail.setMenu(menu);
		
		adsDetail.setHasDeal(true);
		List<AdsOffer> offerList = new ArrayList<AdsOffer>();
		
		AdsOffer offer1 = new AdsOffer();
		offer1.setName("Offer 1");
		offer1.setDescription("Here comes the descirption No.1");
		offer1.setDealImage("http://hqt-imageads-01.telenav.com/adSmallDealImage/1323200001355/upload_b7cd1df_1340c2d0f2b__7ffd_00000490.tmp");
		offerList.add(offer1);
		
		AdsOffer offer2 = new AdsOffer();
		offer2.setName("Offer 2");
		offer2.setDescription("Here comes the descirption No.1");
		offer2.setDealImage("http://hqt-imageads-01.telenav.com/adSmallDealImage/1323200001355/upload_b7cd1df_1340c2d0f2b__7ffd_00000490.tmp");
        offerList.add(offer2);
		
		adsDetail.setOfferList(offerList);

		adsResponse.setAdsDetail(adsDetail);
		return adsResponse;
	}
	/**
	 * TODO	Create dummy data for gas price
	 * @param poiRequest
	 * @param poiResponse
	 * @param tnContext
	 * @return
	 */
	public HtmlPoiDetailResponse getDummyGasPrice(HtmlPoiDetailRequest poiRequest,HtmlPoiDetailResponse poiResponse,TnContext tnContext)
	{
		List<GasPrice> priceList = new ArrayList<GasPrice>();
		
		GasPrice price = new GasPrice();
		
		price.setName("[83]");
		price.setPrice("3.99 $");
		
		priceList.add(price);
		priceList.add(price);
		priceList.add(price);
		priceList.add(price);
		
		poiResponse.setGasPriceList(priceList);

		
		return poiResponse;		
	}
	/**
	 * TODO	Create dummy data for poi details
	 * @param poiRequest
	 * @param poiResponse
	 * @param tnContext
	 * @return
	 */
	public HtmlPoiDetailResponse getDummyPoiDetails(HtmlPoiDetailRequest poiRequest,HtmlPoiDetailResponse poiResponse,TnContext tnContext)
	{
		PoiDetail poiDetail = new PoiDetail();
    	
    	poiDetail.setDescription("test description");
    	poiDetail.setPriceRange("2");
    	poiDetail.setBusinessHours("Today:7AM - 8PM");
    	poiDetail.setLogoName("~7-ELEVEN.png");
    	poiResponse.setPoiDetail(poiDetail);
    	
		return poiResponse;		
	}
	/**
	 * TODO	Create dummy data for poi details
	 * @param poiRequest
	 * @param poiResponse
	 * @param tnContext
	 * @return
	 */
	public HtmlPoiDetailResponse getDummyPoiDetailsNew(HtmlPoiDetailRequest poiRequest,HtmlPoiDetailResponse poiResponse,TnContext tnContext)
	{
		PoiDetail poiDetail = new PoiDetail();
    	
    	poiDetail.setDescription("test description");
    	poiDetail.setPriceRange("2");
    	poiDetail.setBusinessHours("0@0:00AM@0:00AM|1@8:30AM@6:00PM|2@8:30AM@6:00PM|3@8:30AM@6:00PM|4@8:30AM@6:00PM|5@8:30AM@6:00PM|6@9:00AM@1:00PM");
    	poiDetail.setLogoName("~7-ELEVEN.png");
    	poiDetail.setOlsonTimezone("America/Los_Angeles");
    	
    	poiDetail.setHasReview(true);
    	poiDetail.setHasGasPrice(true);
    	poiDetail.setHasPoiMenu(true);
    	poiDetail.setHasPoiExtraAttributes(true);
    	
    	poiResponse.setPoiDetail(poiDetail);
    	
		return poiResponse;		
	}
	/**
	 * TODO	Create dummy data for poi menu
	 * @param poiRequest
	 * @param poiResponse
	 * @param tnContext
	 * @return
	 */
	public HtmlPoiDetailResponse getDummyPoiMenu(HtmlPoiDetailRequest poiRequest,HtmlPoiDetailResponse poiResponse,TnContext tnContext)
	{
		MenuItem menu = new MenuItem();
		menu.setMenuText("menu text");
		poiResponse.setMenu(menu);

		
		return poiResponse;		
	}
	/**
	 * TODO	Create dummy data for organic ads.
	 * @param poiRequest
	 * @param poiResponse
	 * @param tnContext
	 * @return
	 */
	public HtmlPoiDetailResponse getDummyOrganicAds(HtmlPoiDetailRequest poiRequest,HtmlPoiDetailResponse poiResponse,TnContext tnContext)
	{
		PoiDetail poiDetail = new PoiDetail();
		poiDetail.setDescription("ads message");
		poiResponse.setPoiDetail(poiDetail);
		
		MenuItem menu = new MenuItem();
		menu.setMenuText("menu text");
		poiResponse.setMenu(menu);
		
		List<AdsOffer> offerList = new ArrayList<AdsOffer>();
		for(int i=0;i<3;i++)
		{
			AdsOffer adsOffer = new AdsOffer();
			adsOffer.setDescription("Offer Desc" + i);
			adsOffer.setName("Offer Name" + i);
			adsOffer.setDealImage("http://hqt-imageads-01.telenav.com/adSmallDealImage/1323200001355/upload_b7cd1df_1340c2d0f2b__7ffd_00000490.tmp");
			offerList.add(adsOffer);
		}
		poiResponse.setOfferList(offerList);
		return poiResponse;		
	}
	/**
	 * TODO	Create dummy data for hotel
	 * @return
	 */
	public HotelItem getDummyHotel(){
		HotelItem hotel = new HotelItem();
		hotel.setPoiId(12345678);
		Set<Integer> ameniteis = new HashSet<Integer>();
		ameniteis.add(1);
		ameniteis.add(2);
		ameniteis.add(3);
		ameniteis.add(4);
		ameniteis.add(5);
		ameniteis.add(6);
		ameniteis.add(7);
		ameniteis.add(8);
		
		hotel.setAmenities(ameniteis);
		hotel.setName("Best Hotel In SunnyValle");
		hotel.setBrandName("Best Hotel");
		hotel.setPhoneNumber("4056989903");
		hotel.setLocationDesc("Business district, Expressway");
		Stop stop = new Stop();
		stop.lat = 1234567;
		stop.lon = 1234567;
		stop.city = "SunnyValle";
		stop.country = "USA";
		stop.firstLine = "506 Embaracdero Center";
		stop.state = "CA";
		stop.zip = "95500";
		
		hotel.setStop(stop);
		hotel.setDescription("The Courtyard by Marriott, Palo Alto-Los Altos is in the heart of Silicon Valley.Located on the El Camino Real, the Hotel is easily accessible to Hwy 101, I-280, Hwy 85 and the San Jose International Airport. Just minutes from Silicon Valley's prime destinations including Stanford University and Lockheed Martin, the	hotel is perfect for getting down to business or just having fun.");
		hotel.setLogo("http://static.reservetravel.com/v5/propimages/32521/5246628_70.jpg");
		Map<String,Integer> photoMap = new HashMap<String,Integer>();
		photoMap.put("http://static.reservetravel.com/v5/propimages/32521/5246629_70.jpg",1);
		photoMap.put("http://static.reservetravel.com/v5/propimages/32521/5246628_70.jpg",2);
		photoMap.put("http://static.reservetravel.com/v5/propimages/32521/5246629_70.jpg",3);
		photoMap.put("http://static.reservetravel.com/v5/propimages/32521/5246628_70.jpg",4);
		photoMap.put("http://static.reservetravel.com/v5/propimages/32521/5246630_70.jpg",5);
		photoMap.put("http://static.reservetravel.com/v5/propimages/32521/5246628_70.jpg",6);
		photoMap.put("http://static.reservetravel.com/v5/propimages/32521/5246629_70.jpg",7);
		photoMap.put("http://static.reservetravel.com/v5/propimages/32521/5246628_70.jpg",8);
		photoMap.put("http://static.reservetravel.com/v5/propimages/32521/5246629_70.jpg",9);
		photoMap.put("http://static.reservetravel.com/v5/propimages/32521/5246629_70.jpg",10);
		photoMap.put("http://static.reservetravel.com/v5/propimages/32521/5246628_70.jpg",11);
		photoMap.put("http://static.reservetravel.com/v5/propimages/32521/5246629_70.jpg",12);
		photoMap.put("http://static.reservetravel.com/v5/propimages/32521/5246628_70.jpg",13);
		photoMap.put("http://static.reservetravel.com/v5/propimages/32521/5246630_70.jpg",14);
		photoMap.put("http://static.reservetravel.com/v5/propimages/32521/5246628_70.jpg",15);
		photoMap.put("http://static.reservetravel.com/v5/propimages/32521/5246629_70.jpg",16);
		photoMap.put("http://static.reservetravel.com/v5/propimages/32521/5246628_70.jpg",17);
		photoMap.put("http://static.reservetravel.com/v5/propimages/32521/5246629_70.jpg",18);
		hotel.setPhotos(new ArrayList(photoMap.entrySet()));
		hotel.setStarRating(40);
		hotel.setTotalFloors(4);
		hotel.setYearBuilt("2001");
		hotel.setYearOfLastRenovation("2005");
		
		return hotel;
	}
}
