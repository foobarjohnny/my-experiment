/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.html;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.telenav.content.bizhour.BizHour;
import com.telenav.content.bizhour.BizStatusEnum;
import com.telenav.content.bizhour.WeekDayEnum;
import com.telenav.cserver.html.datatypes.ExtraProperty;
import com.telenav.cserver.html.datatypes.GasPrice;
import com.telenav.cserver.html.datatypes.HotelItem;
import com.telenav.cserver.html.datatypes.PoiDetail;
import com.telenav.cserver.html.datatypes.TripAdvisor;
import com.telenav.cserver.html.datatypes.YelpReview;
import com.telenav.cserver.html.executor.HtmlPoiDetailRequest;
import com.telenav.cserver.html.executor.HtmlPoiDetailResponse;
import com.telenav.cserver.html.executor.HtmlPoiDetailServiceProxy;
import com.telenav.cserver.html.executor.HtmlPoiReviewRequest;
import com.telenav.cserver.html.executor.HtmlPoiReviewResponse;
import com.telenav.kernel.util.datatypes.TnContext;

public class TestContentService extends TestCase
{
	TnContext	tnContext;

	@Override
	protected void setUp() throws Exception
	{
		//
		tnContext = new TnContext();
		tnContext.addProperty(TnContext.PROP_POI_DATASET, "TA");
		//
	}

	public void testGetPoiDetailNew() throws ParseException
	{
		//3443006914
		//2040186116 menu=true
		//3000141141 menu=true
		//3000144355 menu=true
		//poiID	hasMenu	hasExtraAttributes	hasLogo	hasOpenTable (vendorCode in additional Vendors would be P_OT)	hasGasPrice(vendorCode in additional Vendors would be P_GBP)
		//3443006914	F	T	T(brand and category)	F	F
		//3000073983	T	T	T(brand and category)	F	F
		//3401785084	F	T	T	T	F

		
		HtmlPoiDetailRequest poiRequest = new HtmlPoiDetailRequest();
		long l = Long.parseLong("2040186116");
		poiRequest.setPoiId(l);
		HtmlPoiDetailResponse poiResponse = new HtmlPoiDetailResponse();
		TnContext tnContext = new TnContext();
		HtmlPoiDetailServiceProxy.getInstance().getPoiDetailsNew(poiRequest, poiResponse, tnContext);


		PoiDetail detail = poiResponse.getPoiDetail();
		if(detail != null)
		{
	    	System.out.println(detail.toString());
		}
	}

	public void testGetGasByPrice() throws ParseException
	{
		//3443006914

		//60493190 {"brand_name": "J.ANDERSON / J.EDWAR","poi_id": 60493190,"update_time": "2011-10-20 00:50:25","gas_price": [{"gas_type": "REGULAR","price": 3.939},{"gas_type": "MID_GRADE","price": 4.039},{"gas_type": "PREMIUM","price": 4.159},{"gas_type": "DIESEL_FUEL","price": 4.399}]}
		//60493189 {"brand_name": "HAMILTON","poi_id": 60493189,"update_time": "2011-10-20 00:50:25","gas_price": [{"gas_type": "REGULAR","price": 3.799},{"gas_type": "MID_GRADE","price": 3.899},{"gas_type": "PREMIUM","price": 3.999}]}
		//60523897 {"brand_name": "GAS N SHOP EUSTIS","poi_id": 60523897,"update_time": "2011-10-20 00:54:09","gas_price": [{"gas_type": "REGULAR","price": 3.429},{"gas_type": "DIESEL_FUEL","price": 3.959}]}
		//60523900 {"brand_name": "PALM BAY CITGO","poi_id": 60523900,"update_time": "2011-10-20 00:54:09","gas_price": [{"gas_type": "REGULAR","price": 3.399}]}
		//60531013 {"brand_name": "KROGER FUEL CENTER","poi_id": 60531013}
		HtmlPoiDetailRequest poiRequest = new HtmlPoiDetailRequest();
		long l = Long.parseLong("60523897");
		poiRequest.setPoiId(l);
		HtmlPoiDetailResponse poiResponse = new HtmlPoiDetailResponse();
		TnContext tnContext = new TnContext();
		HtmlPoiDetailServiceProxy.getInstance().getGasPrice(poiRequest, poiResponse, tnContext);


		List<GasPrice> list = poiResponse.getGasPriceList();
		for(int i=0;i<list.size();i++)
		{
			GasPrice price = list.get(i);
			System.out.println("item:" + price.getName() + ",price:" + price.getPrice());
		}
	}
	
	public void testGetPoiDetail() throws ParseException
	{
		HtmlPoiDetailRequest poiRequest = new HtmlPoiDetailRequest();
		long l = Long.parseLong("3000538730");
		poiRequest.setPoiId(l);
		HtmlPoiDetailResponse poiResponse = new HtmlPoiDetailResponse();
		TnContext tnContext = new TnContext();
		HtmlPoiDetailServiceProxy.getInstance().getPoiDetails(poiRequest, poiResponse, tnContext);


		PoiDetail detail = poiResponse.getPoiDetail();
		if(detail != null)
		{
			String businessHours = "";
			String fullBusinessHours = "";
			boolean isOpen = false;
	    	if(!"".equals(detail.getBusinessHours()))
	    	{
	    		BizHour bizhour = new BizHour(detail.getBusinessHours());
	    		if(BizStatusEnum.OPEN == bizhour.getBizStatus(detail.getOlsonTimezone()))
	    		{
	    			isOpen = true;
	    		}
	    		
	    		WeekDayEnum weekDay = bizhour.getWeekDay(new Date(), detail.getOlsonTimezone());
	    		businessHours = bizhour.getDayBizHourStr(weekDay);
	    		String weekDisplay = WeekDayEnum.values()[weekDay.getCode()].toString();
	    		businessHours = weekDisplay + ": " + businessHours.toLowerCase();
	    		StringBuilder sb = new StringBuilder();
				sb.append("<div class='table' style='width:100%;'>");
	    		String[] weeklyHour = bizhour.getWeekBizHourStr();
	    		for(int i=0;i<weeklyHour.length;i++){
					sb.append("<div class='tr'>");
					sb.append("<div class='td'>").append(WeekDayEnum.values()[i].toString()).append("</div>");
					sb.append("<div class='td'>").append(weeklyHour[i].toLowerCase()).append("</div>");
					sb.append("</div>");
	    		}
				sb.append("</div>");
				fullBusinessHours = sb.toString();
	    	}

	    	System.out.println(businessHours);
	    	System.out.println(isOpen);
	    	System.out.println(fullBusinessHours);
		}
	}

	/**
	 * 2208996210
	 * @throws JSONException
	 */
	public void testGetPoiExtra() throws JSONException
	{
		HtmlPoiDetailRequest poiRequest = new HtmlPoiDetailRequest();
		long l = Long.parseLong("3401785084");
		//2231599868
		poiRequest.setPoiId(l);
		HtmlPoiDetailResponse poiResponse = new HtmlPoiDetailResponse();
		TnContext tnContext = new TnContext();
		poiResponse = HtmlPoiDetailServiceProxy.getInstance().getPoiExtras(poiRequest, poiResponse, tnContext);

		JSONObject json = new JSONObject();
		List<ExtraProperty> extra = poiResponse.getExtras();
		if(extra != null)
		{
			json.put("success", true);

			JSONArray jsArray = new JSONArray();
			for(ExtraProperty extraItem:extra)
			{
				JSONObject item = new JSONObject();
				String value = extraItem.getValue();
				item.put("key", extraItem.getKey());
				//
				String strTemp = value.replaceAll("\n", "<br>");
				item.put("value", strTemp);
				jsArray.put(item);
			}
			json.put("extra", jsArray.toString());
		}
		else
		{
			json.put("success", false);
		}
	}

	//2040802937
	public void testGetPoiMenu()
	{
		HtmlPoiDetailRequest poiRequest = new HtmlPoiDetailRequest();
		long l = Long.parseLong("2040186116");
		poiRequest.setPoiId(l);
		poiRequest.setWidth("400");
		poiRequest.setHeight("400");
		HtmlPoiDetailResponse poiResponse = new HtmlPoiDetailResponse();
		TnContext tnContext = new TnContext();
		HtmlPoiDetailServiceProxy.getInstance().getPoiMenu(poiRequest, poiResponse, tnContext);
		System.out.println(poiResponse.getMenu());
	}
	
	public void testGetLogoImage()
	{
		String logoName = "/tnimages/logo/~Outback-Steakhouse.png";
		String logoImage = HtmlPoiDetailServiceProxy.getInstance().getLogoImage(logoName, "53", "53");
		System.out.println(logoImage);
	}
	
	public void testGetHotelDetail()
	{
		long poiId = Long.parseLong("2040186116");
		HotelItem hotelItem = HtmlPoiDetailServiceProxy.getInstance().getHotelDetail(poiId);
		System.out.println(hotelItem.toString());
	}

	public void testGetYelpAndTripAdvisorReview()
	{
		HtmlPoiReviewRequest poiRequest = new HtmlPoiReviewRequest();
		poiRequest.setTripAdvisorSupported(true);
		poiRequest.setIsYelpSupported(true);
		// 3411883255
		// 3411883249
		// 3000539108
		long l = Long.parseLong("3000539108");
		poiRequest.setPoiId(l);
		HtmlPoiReviewResponse poiResponse = new HtmlPoiReviewResponse();
		poiResponse = HtmlPoiDetailServiceProxy.getInstance().getYelpAndTripAdvisorReview(poiRequest, poiResponse, tnContext);
		TripAdvisor tripAdvisor = poiResponse.getTripAdvisor();
		YelpReview yelp = poiResponse.getYelpReview();
		System.out.println("yelp :" + yelp);
		System.out.println("tripAdvisor:" + tripAdvisor);
	}
}
