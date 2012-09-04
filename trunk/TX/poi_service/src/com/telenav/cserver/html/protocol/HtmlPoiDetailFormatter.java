/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.html.protocol;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.telenav.content.bizhour.BizHour;
import com.telenav.content.bizhour.BizStatusEnum;
import com.telenav.content.bizhour.WeekDayEnum;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.html.protocol.HtmlProtocolResponseFormatter;
import com.telenav.cserver.framework.html.util.HtmlCommonUtil;
import com.telenav.cserver.html.datatypes.ExtraProperty;
import com.telenav.cserver.html.datatypes.GasPrice;
import com.telenav.cserver.html.datatypes.MenuItem;
import com.telenav.cserver.html.datatypes.PoiDetail;
import com.telenav.cserver.html.executor.HtmlPoiDetailResponse;
import com.telenav.cserver.html.util.HtmlConstants;
import com.telenav.cserver.html.util.HtmlPoiUtil;

/**
 * @TODO	format the data returned from browser server and put it into request
 * @author  panzhang@telenav.cn
 * @version 1.0 Feb 18, 2011
 */
public class HtmlPoiDetailFormatter extends HtmlProtocolResponseFormatter{

	
	private void formatAdsResponse(HtmlPoiDetailResponse poiDetailResponse,JSONObject jsonTotal,boolean isNewInterface)throws JSONException, ParseException
	{
		jsonTotal.put("success", true);
	    //detail
		PoiDetail detail = poiDetailResponse.getPoiDetail();
		JSONObject jsonMain = new JSONObject();
		if(detail != null)
		{	
			if(poiDetailResponse.isPoiRequest())
			{
				formatPoiDetail(detail,jsonMain);
				jsonMain.put("logoName", detail.getLogoName());
				jsonMain.put("hasPoiMenu", detail.isHasPoiMenu());
				jsonMain.put("hasPoiExtraAttributes", detail.isHasPoiExtraAttributes());
				jsonMain.put("hasGasPrice", detail.isHasGasPrice());
				jsonMain.put("hasHotel", detail.isHasHotel());
				jsonMain.put("hasOpenTable", detail.isHasOpenTable());
				jsonMain.put("hasTheater", detail.isHasTheater());
				jsonMain.put("hasDeal", detail.isHasDeal());
				jsonMain.put("hasReview", detail.isHasReview());
				jsonMain.put("vendorId", detail.getVendorId());
			}
			else
			{
				jsonMain.put("success", true);
				jsonMain.put("isOpen", false);
				jsonMain.put("businessHours", "");
				jsonMain.put("fullBusinessHours", "");
				jsonMain.put("priceRange", "0");
				
				String desc = HtmlCommonUtil.getString(detail.getDescription());
				String desc1 =  HtmlPoiUtil.formatDesc(desc);;
				jsonMain.put("description", desc1);
				jsonMain.put("adSource", HtmlPoiUtil.filterAdSource(detail.getAdSource()));
				jsonMain.put("adID", detail.getAdID());
				jsonMain.put("logoName", detail.getLogoName());
				
				jsonMain.put("hasDeal", detail.isHasDeal());
				jsonMain.put("hasPoiMenu", detail.isHasPoiMenu());
			} 
			jsonTotal.put("mainTab", jsonMain.toString());
		}
		else
		{
			jsonTotal.put("mainTab", "");
		}
		//offer
		//List<AdsOffer> offerList = poiDetailResponse.getOfferList();
		JSONObject jsonOffer = HtmlPoiJsonBuilder.getInstance().buildDeals(poiDetailResponse.getOfferList());
		/*
		JSONObject jsonOffer = new JSONObject();
		if(offerList != null)
		{
			jsonOffer.put("success", true);
			JSONArray jsArray = new JSONArray();
			for(AdsOffer offer:offerList)
			{
				JSONObject item = new JSONObject();
				item.put("name", HtmlCommonUtil.getString(offer.getName()));
				item.put("description", HtmlCommonUtil.getString(offer.getDescription()));
				jsArray.put(item);
			}
			jsonOffer.put("deals", jsArray.toString());
		}
		else
		{
			jsonOffer.put("success", false);
		}*/
		jsonTotal.put("dealTab", jsonOffer.toString());
		
		
	    //menu
		/*
		JSONObject jsonMenu = new JSONObject();
		String menuText = HtmlCommonUtil.getString(menu.getMenuText());
		String menuImage = HtmlCommonUtil.getString(menu.getMenuImage());
		
		if(!"".equals(menuText) || !"".equals(menuImage))
		{
			jsonMenu.put("success", true);
			jsonMenu.put("menu",menuText);
			jsonMenu.put("menuImage",menuImage);
			jsonTotal.put("menuTab", jsonMenu.toString());
		}
		else
		{
			jsonTotal.put("menuTab", "");
		}*/
		
		String menuJson = HtmlPoiJsonBuilder.getInstance().buildMenu(poiDetailResponse.getMenu());
		jsonTotal.put("menuTab", menuJson);
	}
	
	private String formatWeek(String text)
	{
		String temp = text.toLowerCase();
		return temp.substring(0,1).toUpperCase() + temp.substring(1);
	}
	/**
	 * 
	 * @param detail
	 * @param json
	 * @throws JSONException
	 * @throws ParseException
	 */
	private void formatPoiDetail(PoiDetail detail,JSONObject json) throws JSONException, ParseException
	{
		json.put("success", true);
		
		String fullBusinessHours = "";
		String businessHours = "";
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
    		businessHours = formatWeek(weekDisplay) + ": " + businessHours.toLowerCase();
			//get weekly hours
			StringBuilder sb = new StringBuilder();
			sb.append("<div class='table' style='width:100%;'>");
    		String[] weeklyHour = bizhour.getWeekBizHourStr();
    		for(int i=0;i<weeklyHour.length;i++){
				sb.append("<div class='tr'>");
				sb.append("<div class='td'>").append(formatWeek(WeekDayEnum.values()[i].toString())).append("</div>");
				sb.append("<div class='td'>").append(weeklyHour[i].toLowerCase()).append("</div>");
				sb.append("</div>");
    		}
			sb.append("</div>");
			fullBusinessHours = sb.toString();
			
//    		TimeZone tz = TimeZone.getTimeZone(detail.getOlsonTimezone());
//	        Calendar calendar = Calendar.getInstance(tz);
//	        Date date = new Date();
//	        calendar.setTime(date);
//	        
//	        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)-1;
//
//			List<BizHourPeriod> bizHourList = BizHourParser.covertBizStrToBizHourPeriodList(detail.getBusinessHours());
//			BizHourPeriod todayPeriod = null;
//			if(bizHourList != null)
//			{
//				/* use a new way to fetch todayPeriod
//				 * commented by jxl 2012.04.05
//				if(bizHourList.size() >0){
//					todayPeriod = bizHourList.get(dayOfWeek);
//				}
//				*/
//				//get weekly hours
//				StringBuffer sb = new StringBuffer();
//				sb.append("<div class='table' style='width:100%;'>");
//				String periodStr;
//				int firstColon;
//				for(BizHourPeriod period:bizHourList)
//				{
//					periodStr = period.toString();
//					firstColon = periodStr.indexOf(':');
//					if(firstColon < 0){
//						continue;
//					}
//					sb.append("<div class='tr'>");
//					sb.append("<div class='td'>").append(periodStr.substring(0, firstColon)).append("</div>");
//					sb.append("<div class='td'>").append(periodStr.substring(firstColon + 1)).append("</div>");
//					sb.append("</div>");
//				}
//				sb.append("</div>");
//				fullBusinessHours = sb.toString();
//			}
//			
//			/* use a new way to judge open or closed
//			 * commented by jxl 2012.04.05
//			if(todayPeriod != null){
//				businessHours = todayPeriod.toString();
//		        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
//		        int minutes = calendar.get(Calendar.MINUTE);
//		        String time = hourOfDay + ":" + minutes;
//		        
//		        BizHour bizHour = new BizHour(time);
//		        if(bizHour.after(todayPeriod.getOpenHour())  && bizHour.before(todayPeriod.getCloseHour()))
//		        {
//		        	isOpen = true;
//		        }
//			}*/
//			
//			/*
//			 * Label A :
//			 * the new way to fetch todayPeriod and judge open or closed
//			 * commented by jxl 2012.04.05
//			 */
//			Map<WeekDayEnum,List<BizHourPeriod>> bizHourMap = BizHourParser.getBizHourMap(bizHourList);
//			List<BizHourPeriod> todayPeriodList = bizHourMap.get(WeekDayEnum.values()[dayOfWeek]);
//			if(todayPeriodList != null){
//				int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
//		        int minutes = calendar.get(Calendar.MINUTE);
//		        String time = hourOfDay + ":" + minutes;
//		        BizHour currentTime = new BizHour(time);
//		        /**
//		         * we must get the nearest period to show to the user. For example, open period are: [07:00AM - 11:00AM], [12:00AM - 06:00PM], [08:00PM - 23:00PM],
//		         * and the current time is 11:30 AM. So the nearest period is [12:00AM - 06:00PM] rather than [07:00AM - 11:00AM] and [08:00PM - 23:00PM].
//		         * If period "P" is the nearest period then, the closed time of the previous period of "P" should before current time, meanwhile, the open time of "P" should
//		         * after the current time.
//		         * commented by jxl 2012.04.05
//		         */
//		        BizHourPeriod nearestPeriod = todayPeriodList.get(0);
//		        BizHourPeriod lastPeriod = null;
//		        for(int i=0;i<todayPeriodList.size();i++){
//		        	todayPeriod = todayPeriodList.get(i);
//		        	 if(currentTime.after(todayPeriod.getOpenHour())  && currentTime.before(todayPeriod.getCloseHour())){
//			        	isOpen = true;
//			        	nearestPeriod = todayPeriod;//this open period is the nearest period
//			        }else{
//			        	if(i > 0){
//			        		lastPeriod = todayPeriodList.get(i - 1);
//			        		if(currentTime.after(lastPeriod.getCloseHour())  && currentTime.before(todayPeriod.getOpenHour())){
//			        			nearestPeriod = todayPeriod;
//			        		}
//			        	}
//			        }
//		        	if(isOpen){
//		        		break;
//		        	}
//		        }
//		        businessHours = nearestPeriod.toString();
//			}
//			//------------------ End of Label A ----------------------
    	}
    	json.put("isOpen", isOpen);
		json.put("businessHours", businessHours);
		json.put("fullBusinessHours", fullBusinessHours);
		String desc = HtmlCommonUtil.getString(detail.getDescription());
		String desc1 =  HtmlPoiUtil.formatDesc(desc);
		json.put("description", desc1);
		json.put("priceRange", detail.getPriceRange());
		//json.put("logoName", detail.getLogo());
		//json.put("logoImage", detail.getLogoImage());
		json.put("adSource", HtmlPoiUtil.filterAdSource(HtmlCommonUtil.getString(detail.getAdSource())));
		json.put("adID", "");
	}

	
	@Override
	public void parseBrowserResponse(HttpServletRequest httpServletRequest,
			ExecutorResponse response) throws Exception {
		
		HtmlPoiDetailResponse poiDetailResponse = (HtmlPoiDetailResponse)response;
		String operateType = poiDetailResponse.getOperateType();
		if(HtmlConstants.OPERATE_POIDETAIL_MAIN.equals(operateType))
		{
			JSONObject json = new JSONObject();
			PoiDetail detail = poiDetailResponse.getPoiDetail();
			if(detail != null)
			{
				formatPoiDetail(detail,json);
			}
			else
			{
				json.put("success", false);
			}
			
			httpServletRequest.setAttribute("ajaxResponse", json.toString());
		}
		else if(HtmlConstants.OPERATE_POIDETAIL_MAINNEW.equals(operateType))
		{
			JSONObject json = new JSONObject();
			
			formatAdsResponse(poiDetailResponse,json,true);
			
			httpServletRequest.setAttribute("ajaxResponse", json.toString());
		}
		else if(HtmlConstants.OPERATE_POIDETAIL_EXTRA.equals(operateType))
		{
			JSONObject json = new JSONObject();
			List<ExtraProperty> extra = poiDetailResponse.getExtras();
			if(extra != null)
			{
				json.put("success", true);
				
				JSONArray jsArray = new JSONArray();
				for(ExtraProperty extraItem:extra)
				{
					if(null != extraItem.getValue() && null != extraItem.getKey())
					{
						JSONObject item = new JSONObject();
						String value = extraItem.getValue();
						item.put("key", extraItem.getKey());
						//
						String strTemp = value.replaceAll("\n", "<br>");
						item.put("value", strTemp);
						jsArray.put(item);
					}
				}
				json.put("extra", jsArray.toString());
			}
			else
			{
				json.put("success", false);
			}
			
			httpServletRequest.setAttribute("ajaxResponse", json.toString());
		}
		else if(HtmlConstants.OPERATE_POIDETAIL_MENU.equals(operateType))
		{
			JSONObject json = new JSONObject();
			MenuItem menu = poiDetailResponse.getMenu();
			String menuText = HtmlCommonUtil.getString(menu.getMenuText());
			String menuImage = HtmlCommonUtil.getString(menu.getMenuImage());
			
		    json.put("success", true);
		    json.put("menu",menuText);
		    json.put("menuImage",menuImage);

			httpServletRequest.setAttribute("ajaxResponse", json.toString());
		}
		else if(HtmlConstants.OPERATE_POIDETAIL_ADSPOI.equals(operateType))
		{
			JSONObject jsonTotal = new JSONObject();
			formatAdsResponse(poiDetailResponse,jsonTotal,false);
			httpServletRequest.setAttribute("ajaxResponse", jsonTotal.toString());
		}		
		else if(HtmlConstants.OPERATE_POIDETAIL_GASPRICE.equals(operateType))
		{
			JSONObject json = new JSONObject();
			List<GasPrice> dataList = poiDetailResponse.getGasPriceList();
			if(dataList != null)
			{
				json.put("success", true);
				
				JSONArray jsArray = new JSONArray();
				for(GasPrice itemObj:dataList)
				{
					JSONObject item = new JSONObject();
					item.put("name", itemObj.getName());
					item.put("price", itemObj.getPrice());
					jsArray.put(item);
				}
				json.put("data", jsArray.toString());
			}
			else
			{
				json.put("success", false);
			}
			
			httpServletRequest.setAttribute("ajaxResponse", json.toString());
		}
	}
}
