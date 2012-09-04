/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.html.protocol;

import javax.servlet.http.HttpServletRequest;

import org.json.me.JSONObject;

import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.html.protocol.HtmlProtocolResponseFormatter;
import com.telenav.cserver.framework.html.util.HtmlCommonUtil;
import com.telenav.cserver.html.datatypes.AdsDetail;
import com.telenav.cserver.html.executor.HtmlAdsResponse;
import com.telenav.cserver.html.util.HtmlConstants;
import com.telenav.cserver.html.util.HtmlPoiUtil;
import com.telenav.j2me.datatypes.Stop;
/**
 * @TODO	format the data returned from browser server and put it into request
 * @author  panzhang@telenav.cn
 * @version 1.0 Feb 18, 2011
 */
public class HtmlAdsFormatter extends HtmlProtocolResponseFormatter{

	@Override
	public void parseBrowserResponse(HttpServletRequest httpServletRequest,
			ExecutorResponse response) throws Exception {
		
		HtmlAdsResponse adsResponse = (HtmlAdsResponse)response;
		String operateType = adsResponse.getOperateType();

	    if( HtmlConstants.OPERATE_ADSVIEW_BASIC.equals(operateType) )
	    {
	        AdsDetail detail = adsResponse.getAdsDetail();

	        formatAdsDetail(detail);
	           //format to json object
            //JSONObject json = convertToPoiFormat(detail);
            httpServletRequest.setAttribute("adsdetail",detail);
	    }
	    else if(HtmlConstants.OPERATE_ADSVIEW_FETCH_DETAIL_DATA.equals(operateType))
	    {
	        AdsDetail detail = adsResponse.getAdsDetail();

	        formatAdsDetail(detail);
	    	//format to json object
	    	JSONObject json = convertToPoiFormat(detail);
            httpServletRequest.setAttribute("ajaxResponse", json.toString());
	    }else if(HtmlConstants.OPERATE_ADSVIEW_DETAIL.equals(operateType)){
	        
	    }
	}
	
	private void formatAdsDetail(AdsDetail detail)
	{
		Stop stop = detail.getAddress();
		String text = "";
		
		if(stop==null||stop.city==null||stop.state==null||stop.zip==null){
		    detail.setAddressDisplay("");
		    return;
		}
		    
    	String firstLine = HtmlCommonUtil.getString(stop.firstLine);
    	
//    	if(!"".equals(firstLine))
//    	{
//    		firstLine = firstLine + "<br>";
//    	}
//    	
//    	text= firstLine + stop.city + ", " + stop.state + " " + stop.zip;
//    	
//    	detail.setAddressDisplay(text);
    	
    	String secondLine ="";
    	if(stop.city !=null && !stop.city.equals("")){
    		secondLine += stop.city.toUpperCase();
		}
    	/* TNCSERVER-3768: remove 'State' and Zip code from Drive To Button
		if(stop.state !=null && !stop.state.equals("")){
			secondLine += ", " + stop.state.toUpperCase();
		}
		
    	secondLine += " " + stop.zip;
    	*/
    	text = firstLine;
    	if(!secondLine.equals("")){
    		text = "<div class='clsAddressFS clsOneLine' style='width:90%;'>"+firstLine + "</div><div class='clsCityFS clsOneLine' style='width:90%;'>" + secondLine+"</div>";
    	}
    	
    	detail.setAddressDisplay(text);
	}
	
   private JSONObject convertToPoiFormat(AdsDetail detail) throws Exception{
	   
	   JSONObject total = new JSONObject();
	   //generate poiDetailBasic cache which has the same data structure with poidetail->private service
       JSONObject poiDetailObj = new JSONObject();
       
       poiDetailObj.put("id", "0");
       poiDetailObj.put("selectedIndex", "0");
       poiDetailObj.put("type", "2");
       
       JSONObject poi = new  JSONObject();
       JSONObject stop = new JSONObject();
       stop.put("zip", detail.getAddress().zip);
       stop.put("lon",detail.getAddress().lon );
       stop.put("isGeocoded", false);
       stop.put("stopId", detail.getAddress().stopId);
       stop.put("province", detail.getAddress().state);
       stop.put("label", detail.getAddress().label);
       stop.put("firstLine", detail.getAddress().firstLine);
       stop.put("streetName", "");
       stop.put("streetNumber", "");
       stop.put("type", "0");
       stop.put("lat", detail.getAddress().lat );
       stop.put("city", detail.getAddress().city);
       
       //poi.put("stop", stop);
       
       poiDetailObj.put("stop", stop);
       
       //poi.put("logoName", detail.getLogoName());
       //poi.put("adSource", detail.getAdSource());
       //poi.put("adID", detail.getAdId());
       //poi.put("logoImage", detail.getLogoImage());
       //poi.put("priceRange", "0");
       //poi.put("description", detail.getTagline());
       //poi.put("addressDisplay",detail.getAddressDisplay());
       
       JSONObject bizpoi = new JSONObject();
       bizpoi.put("brand", detail.getBrandName());
       //bizpoi.put("distanceWithUnit","");
       //bizpoi.put("price", "");
       
      // bizpoi.put("phoneNumber", "4087308117");
       bizpoi.put("phoneNumber", detail.getPhoneNo());
       
       //bizpoi.put("poiId", "");
       //bizpoi.put("categoryId", "");
       
       poi.put("bizPoi", bizpoi);
       poi.put("type", "127");
       poiDetailObj.put("poi", poi);
       poiDetailObj.put("existedInFavorite", "false");
       //poiDetailObj.put("stop", stop);
       //poiDetailObj.put("sharedFromUser", "");
       //poiDetailObj.put("phoneNumber", detail.getPhoneNo());
      // poiDetailObj.put("phoneNumber", "4087308117");
       //poiDetailObj.put("status", "1");
       //poiDetailObj.put("label", detail.getBrandName());
       //poiDetailObj.put("type", "2");
       //poiDetailObj.put("distanceUnit", "0");
       //poiDetailObj.put("sharedFromPTN", "");
       total.put("baisc", poiDetailObj.toString());
       //generate the data for Main Tab
       JSONObject jsonMain = new JSONObject();
       jsonMain.put("success", true);
       String desc = HtmlCommonUtil.getString(detail.getTagline());
       String desc1 =  HtmlPoiUtil.formatDesc(desc);;
       jsonMain.put("description", desc1);
       jsonMain.put("adSource", HtmlPoiUtil.filterAdSource(detail.getAdSource()));
       jsonMain.put("adID", detail.getAdId());
       jsonMain.put("logoName", detail.getLogoName());
       jsonMain.put("hasDeal", detail.isHasDeal());
       jsonMain.put("hasPoiMenu", detail.isHasPoiMenu());
       jsonMain.put("poiId", detail.getPoiId());
       jsonMain.put("poiType", detail.getPoiType());
       total.put("mainTab", jsonMain.toString());
       //generate the data for Deals Tab
       JSONObject jsonOffer = HtmlPoiJsonBuilder.getInstance().buildDeals(detail.getOfferList());
       total.put("dealTab", jsonOffer.toString());
       //generate the data for Menu Tab
       //menu
       String menuJson = HtmlPoiJsonBuilder.getInstance().buildMenu(detail.getMenu());
       total.put("menuTab", menuJson);
	
       return total;
   }
}
