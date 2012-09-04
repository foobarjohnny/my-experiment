package com.telenav.cserver.poi.protocol.v20;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.telenav.cserver.backend.contents.GetReviewResponse;
import com.telenav.cserver.backend.datatypes.contents.ReviewServicePOIReview;
import com.telenav.cserver.backend.datatypes.cose.Coupon;
import com.telenav.cserver.browser.framework.protocol.BrowserProtocolResponseFormatter;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.poi.datatypes.POI;
import com.telenav.cserver.poi.datatypes.PoiConstants;
import com.telenav.cserver.poi.datatypes.Stop;
import com.telenav.cserver.poi.executor.v20.POISearchResponse_WS;
import com.telenav.cserver.poi.protocol.POIResponseUtil;
import com.telenav.cserver.util.TnUtil;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.kernel.ws.axis2.Axis2Helper;
import com.telenav.tnbrowser.util.Utility;

// TODO Rain too many places not good
public class PoiBrowserProtocolResponseFormatter extends
		BrowserProtocolResponseFormatter {
	public static final DateFormat dateFormat = new SimpleDateFormat(
			"MMM dd, yyyy");
	public static final int DISTANCE_UNIT_CRITICAL_POINT = 500;

	public void parseBrowserResponse(HttpServletRequest httpRequest,
			ExecutorResponse executorResponse) throws Exception {
		POISearchResponse_WS response = (POISearchResponse_WS) executorResponse;

		TxNode node = new TxNode();

		List<POI> poiList = response.getPoiList();
		JSONArray joList = new JSONArray();
		int distanceUnit = response.getDistanceUnit();
		if (response.getPoiList() != null) {
			for (POI bp : poiList) {
				JSONObject bpjo = convertPOIToJSONObject(bp, distanceUnit);
				joList.put(bpjo);
			}
		}

		// TODO sponsor poi chbzhang
		List<POI> sponsorPoiList = response.getSponsorPoiList();
		JSONArray sponsorJoList = new JSONArray();

		if (sponsorPoiList != null && sponsorPoiList.size() != 0) {
			int i = 0;
			int sponsorListingNumber = response.getSponsorListingNumber();
			for (POI bp : sponsorPoiList) {
				JSONObject bpjo = convertPOIToJSONObject(bp, distanceUnit);
				sponsorJoList.put(bpjo);
				i++;
			}
			
			while(i < sponsorListingNumber){
				JSONObject bpjo = new JSONObject();
				sponsorJoList.put(bpjo);
				i++;
			}
		}

		node.addValue(response.getTotalMaxPageIndex());

		TxNode audio = POIResponseUtil.getAudioTxNode(response.getPromptItems());
		
		node.addChild(audio);

		String poiString = joList.toString();
		node.addMsg(poiString);

		String sponsorPoiString = sponsorJoList.toString();
		node.addMsg(sponsorPoiString);

		httpRequest.setAttribute("node", node);
	}

	public static JSONObject convertPOIToJSONObject(POI bp, int distanceUnit)
			throws JSONException {
		JSONObject jo = new JSONObject();
		String distance = getUnitDistance(bp.bizPoi.distance, distanceUnit);
		jo.put("distance", distance);
		jo.put("rating", bp.avgRating);
		jo.put("phoneNumber", formatPhoneNumber(bp.bizPoi.phoneNumber));
		jo.put("price", bp.bizPoi.price);
		jo.put("name", bp.bizPoi.brand);
		jo.put("poiId", bp.poiId);
		jo.put("category", bp.bizPoi.parentCatName == null?"":bp.bizPoi.parentCatName);
		jo.put("categoryId", bp.bizPoi.categoryId);
		double priceRange = bp.priceRange;
		if (bp.priceRange == -1) {
			priceRange = 0;
		}
		jo.put("priceRange", Integer
				.parseInt(String.format("%.0f", priceRange)));
		jo.put("popularity", bp.popularity);
		jo.put("ratingNumber", bp.ratingNumber);
		jo.put("userPreviousRating", bp.userPreviousRating);
		jo.put("isRatingEnable", bp.isRatingEnable ? 1 : 0);

		Stop stop = bp.bizPoi.address;
		JSONObject stopJo = new JSONObject();
		stopJo.put("firstLine", stop.firstLine);
		stopJo.put("city", stop.city);
		stopJo.put("state", stop.state);
		stopJo.put("country", stop.country);
		stopJo.put("lon", stop.lon);
		stopJo.put("zip", getZip(stop));
		stopJo.put("lat", stop.lat);
		stopJo.put("lon", stop.lon);
		stopJo.put("label", stop.label);
		// set new field for ACE 4.0, we need assembling "first_line" form "StreetName" and "HouseNumber"
		stopJo.put("streetName", bp.bizPoi.getStreetName());
		stopJo.put("houseNumber", bp.bizPoi.getHouseNumber());
		jo.put("stop", stopJo);

		if (bp.bizPoi.supplementalInfo != null){
			JSONArray arr = new JSONArray();
			String[] gprice = bp.bizPoi.supplementalInfo;
			for (int i=0; i< gprice.length; i++) {
				if (gprice[i] != null){
					JSONObject pg = new JSONObject();
					pg.put("price", gprice[i]);
					pg.put("timeL", bp.bizPoi.supportInfo[i]);
					arr.put(pg);
				}
			}
			jo.put("gasPirce", arr);
		}

		GetReviewResponse getReviewResponse = bp.getReviewResponse;
		JSONArray reviewDetailJo = new JSONArray();
		if (getReviewResponse != null) {
			ReviewServicePOIReview[] poiReviews = getReviewResponse.getReview();
			if (poiReviews != null && Axis2Helper.isNonEmptyArray(poiReviews)) {
				int size = poiReviews.length;
				for (int i = 0; i < size; i++) {
					ReviewServicePOIReview poiReview = poiReviews[i];
					JSONObject poiReviewJo = new JSONObject();
					
					try {
						poiReviewJo.put("rating", (int)Double.parseDouble(poiReview.getRating()));
			        } catch (NumberFormatException e) {
			        	poiReviewJo.put("rating", 0);
			        }
					
					poiReviewJo.put("poiId", poiReview.getPoiId());
					poiReviewJo.put("reviewId", poiReview.getReviewId());
					poiReviewJo
							.put("reviewerName", poiReview.getReviewerName());
					poiReviewJo.put("reviewText", poiReview.getReviewText());
					Date updateDate = poiReview.getUpdateTime();
					SimpleDateFormat time = new SimpleDateFormat("MM/dd/yyyy");
					String dateStr = time.format(updateDate);
					poiReviewJo.put("updateDate", dateStr);
					reviewDetailJo.put(poiReviewJo);
				}
			}
		}
		jo.put("reviewDetail", reviewDetailJo);

		if (bp.ad != null) {
			JSONObject ad = new JSONObject();
			ad.put("adTag", bp.ad.getShortMessage());
			if (!"".equals(TnUtil.getString(bp.ad.getMessage()))) {
				ad.put("adLine", bp.ad.getMessage());
			}
			String addId = bp.ad.getSourceAdId();
			if (addId != null){ //necessary MIS reporting
				ad.put("adSource", bp.ad.getAdSource());
				ad.put("adID", addId);
			}
			jo.put("ad", ad);
		}
		
		
		if(bp.openTableInfo != null) {
			if(bp.openTableInfo.isReservable())
			//if(true) // TODO for testing
			{
				JSONObject restaurant = new JSONObject();
				restaurant.put("partner", bp.openTableInfo.getPartner());
				restaurant.put("partnerPoiId", bp.openTableInfo.getPartnerPoiId());
				restaurant.put("isReservable", bp.openTableInfo.isReservable());
				//restaurant.put("isReservable", true); // TODO for testing
				jo.put("restaurant", restaurant);
			}
		}
		
		if (bp.couponList != null && bp.couponList.size() > 0) {
			JSONArray couponArray = new JSONArray();
			for (Coupon coupon : bp.couponList) {
				JSONObject couponJSON = new JSONObject();

				couponJSON.put("desc", coupon.getDescription());
				Date date = coupon.getEndDate();
				if (date != null) {
					couponJSON.put("endDate", dateFormat.format(date));
				}
				if (coupon.getImage() != null) {
					couponJSON.put("img", Utility.byteArrayToBase64(coupon
							.getImage()));
				}
				couponArray.put(couponJSON);
			}
			jo.put("coupon", couponArray);

		}

		if (bp.menu != null && !bp.menu.equals("")) {
			JSONObject menuJo = new JSONObject();
			menuJo.put("menu", formatMenu(bp.menu));
			jo.put("menu", menuJo);
		}
		
		return jo;
		
	}
	
	
    // [weiw] Hack for TN60. Put new line under dish name
    private static String formatMenu(String menu) {
        return menu.replaceAll("</bold>", "</bold>\n");
	}
    
    
    public static String extractPhoneNumber(String phoneNumber)
    {
                    
                    if(phoneNumber==null)
                                    return null;
                    StringBuilder ans = new StringBuilder();
                    for(int i = 0; i < phoneNumber.length(); i++)
                    {
                    
                       Character ch = phoneNumber.charAt(i);
                       if(Character.isDigit(ch))
                       {
                          ans.append(ch); 
                       }
                    } 
                    return ans.toString();
    }

    private static String formatPhoneNumber(String phoneNumber){
    if(null == phoneNumber) return "";
    phoneNumber=extractPhoneNumber(phoneNumber);
    if(phoneNumber.length() > 10){
                    phoneNumber = phoneNumber.substring(phoneNumber.length() - 10);
    }
    
    return phoneNumber;
}

	public static String getUnitDistance(int distanceInMeter, int unit) {
		String distance = "";
		if (distanceInMeter < 0)
			return distance;
		double distanceInFeet = distanceInMeter * PoiConstants.METER_TO_FT;
		if (PoiConstants.UNIT_METER == unit) {// Km/Meters
			if (distanceInFeet <= DISTANCE_UNIT_CRITICAL_POINT) {
				distance = distanceInMeter + " m";
			} else {
				double distance_format = distanceInMeter
						* PoiConstants.METER_TO_KM;
				distance = distanceFormat(distance_format) + " km";
			}
		} else if (unit == PoiConstants.UNIT_MILE) {// Mi/Ft
			if (distanceInFeet <= DISTANCE_UNIT_CRITICAL_POINT) {
				distance = String.format("%.0f", distanceInFeet) + " ft";
			} else {
				double distance_format = (distanceInMeter * PoiConstants.KM_TO_MILE)
						/ PoiConstants.KM_TO_METER;
				distance = distanceFormat(distance_format) + " mi";
			}
		}
		return distance;
	}

	/**
	 * Rounding
	 * 
	 * @author chbzhang
	 * @param distance_format
	 * @return
	 */
	public static String distanceFormat(double distance_format) {
		String distance_format_str = "";
		if (10 < distance_format) {
			distance_format_str = String.format("%.0f", distance_format);
		} else {
			distance_format_str = String.format("%.1f", distance_format);
		}
		if("10.0".equals(distance_format_str)){
			distance_format_str="10";
		}
		return distance_format_str;
	}

	/**
	 * @author weiw
	 * @param stop
	 * @return 5-digit zip for USA
	 * 
	 */
	private static String getZip(Stop stop) {
		if (stop == null) {
			return "";
		}
		if ("US".equalsIgnoreCase(stop.country)) {
			if (stop.zip.length() > 5) {
				return stop.zip.substring(0, 5);
			}
		}
		return stop.zip;
	}
}
