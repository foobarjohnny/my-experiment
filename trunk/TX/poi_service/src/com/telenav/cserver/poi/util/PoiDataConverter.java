/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.poi.util;

import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import com.telenav.cserver.backend.contents.ContentManagerServiceProxy;
import com.telenav.cserver.backend.contents.GetReviewRequest;
import com.telenav.cserver.backend.contents.GetReviewResponse;
import com.telenav.cserver.backend.datatypes.Address;
import com.telenav.cserver.backend.datatypes.AddressFormatConstants;
import com.telenav.cserver.backend.datatypes.TnPoi;
import com.telenav.cserver.backend.datatypes.cose.GasPriceInfo;
import com.telenav.cserver.backend.datatypes.cose.GasPriceTypeDef;
import com.telenav.cserver.backend.datatypes.cose.TnPoiReviewSummary;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.cserver.poi.datatypes.BasePoi;
import com.telenav.cserver.poi.datatypes.BasePoiExtraInfo;
import com.telenav.cserver.poi.datatypes.BizPOI;
import com.telenav.cserver.poi.datatypes.POI;
import com.telenav.cserver.poi.datatypes.PoiConstants;
import com.telenav.cserver.poi.datatypes.PoiReviewSummary;
import com.telenav.cserver.poi.datatypes.Stop;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * PoiDataConverter.java
 *
 * @author bhu@telenav.cn
 * @version 1.0 2009-7-20
 */
public class PoiDataConverter
{
	static Logger logger = Logger.getLogger(PoiDataConverter.class);
	public static final double DEGREE_MULTIPLIER = 1.e5; // 1e-5 deg units
	
	public static BasePoi convertTnPoiToBasePoi(TnPoi tnPoi, boolean setDistance,int distanceUnit,TnContext tc, boolean needReviews)
	{
		if(tnPoi == null)
		{
			return null;
		}
		BasePoiExtraInfo basePoiExtraInfo = new BasePoiExtraInfo();
		BasePoi basePoi = new BasePoi();
		basePoi.setLocalAppInfos(tnPoi.getLocalAppInfos());
		basePoi.isRatingEnable = tnPoi.isRatingEnable();
		basePoi.poiId = tnPoi.getPoiId();
		basePoi.basePoiExtraInfo = basePoiExtraInfo;
		TnPoiReviewSummary reviewSmy = tnPoi.getReviewSummary();
		if(reviewSmy != null)
		{
			basePoi.avgRating = (int)(reviewSmy.getAverageRating()*10);
			basePoi.popularity = (int)Math.round(reviewSmy.getPopularity());
			basePoiExtraInfo.ratingNumber = reviewSmy.getRatingNumber();
			basePoiExtraInfo.userPreviousRating = reviewSmy.getUserPreviousRating();
			if(basePoiExtraInfo.userPreviousRating < 0 || basePoiExtraInfo.userPreviousRating > 5)
			{
			    basePoiExtraInfo.userPreviousRating = 0;
			}
		}
		
		//BizPOI
		String version = tc.getProperty("version");
		BizPOI bizPoi = convertTnPoiToBizPOI(tnPoi, setDistance,distanceUnit, version);
		if(bizPoi == null)
		{
			return null;
		}
		basePoi.bizPOI = bizPoi;
		
		//ads
		if (tnPoi.getAd() != null) {
			basePoiExtraInfo.shortMessage = tnPoi.getAd().getShortMessage();
			basePoiExtraInfo.message = tnPoi.getAd().getMessage();
			basePoiExtraInfo.adSource = tnPoi.getAd().getAdSource();
			basePoiExtraInfo.sourceAdId = tnPoi.getAd().getSourceAdId();
		}
		
		//open table info
		if(tnPoi.getOpenTableInfo() != null)
		{
		    basePoiExtraInfo.isReservable = tnPoi.getOpenTableInfo().isReservable();
		    basePoiExtraInfo.partnerPoiId = tnPoi.getOpenTableInfo().getPartnerPoiId();
		    basePoiExtraInfo.partner = tnPoi.getOpenTableInfo().getPartner();
		}
		basePoi.basePoiExtraInfo.extraProperties = tnPoi.getPoiExtraInfo();

		return basePoi;
	}
	
	public static POI convertTnPoiToPOI(TnPoi tnPoi, boolean setDistance,int distanceUnit,TnContext tc, boolean needReviews)
	{
		if(tnPoi == null)
		{
			return null;
		}
		POI clientPOI = new POI();
		clientPOI.poiId = tnPoi.getPoiId();
		clientPOI.basePoiId = tnPoi.getBasePoiId();
		clientPOI.isRatingEnable = tnPoi.isRatingEnable();
		clientPOI.hasUserRatedThisPoi = false;
//		clientPOI.priceRange = tnPoi.getPriceRange();
		clientPOI.setLocalAppInfos(tnPoi.getLocalAppInfos());
		//Poi review summary
		TnPoiReviewSummary reviewSmy = tnPoi.getReviewSummary();
		if(reviewSmy != null)
		{
//			clientPOI.avgRating = convertToClientRating(reviewSmy.getAverageRating());
//			clientPOI.popularity = convertToClientRating(reviewSmy.getPopularity());
		    clientPOI.ratingNumber = reviewSmy.getRatingNumber();
		    clientPOI.avgRating = (int)(reviewSmy.getAverageRating()*10);
            clientPOI.popularity = (int)Math.round(reviewSmy.getPopularity());
			clientPOI.reviewSummary = convertReviewSummary(reviewSmy);
			clientPOI.priceRange = reviewSmy.getReviewAveragePrice()!=null?Double.parseDouble(reviewSmy.getReviewAveragePrice()):0;
			clientPOI.userPreviousRating = reviewSmy.getUserPreviousRating();
			if(clientPOI.userPreviousRating < 0 || clientPOI.userPreviousRating > 5){
				clientPOI.userPreviousRating = 0;
			}
		}
		//Poi Ad.
		if(tnPoi.getAd() != null)
		{
			clientPOI.ad = tnPoi.getAd();
		}
		if(tnPoi.getCouponList() != null)
		{
			clientPOI.couponList = tnPoi.getCouponList();
		}
		
		if(tnPoi.getOpenTableInfo() != null)
		{
			clientPOI.openTableInfo = tnPoi.getOpenTableInfo();
		}
		
		//TODO:[HB] set menu
		if(tnPoi.getMenu() != null)
		{
			clientPOI.menu = tnPoi.getMenu();
		}
		//TODO:[HB] business hour
		if(tnPoi.getBusinessHour() != null)
		{
			clientPOI.businessHour = tnPoi.getBusinessHour();
		}
		
		//BizPOI
		String version = tc.getProperty("version");
		BizPOI bizPoi = convertTnPoiToBizPOI(tnPoi, setDistance,distanceUnit, version);
		if(bizPoi == null)
		{
			return null;
		}
		clientPOI.bizPoi = bizPoi;
		
		//Add by chbzhang, for poi reviews
		if(needReviews)
		{
			ContentManagerServiceProxy contentManagerServiceProxy = ContentManagerServiceProxy.getInstance();
//			ReviewDetailListRequest request = new ReviewDetailListRequest();
			GetReviewRequest request = new GetReviewRequest();
			request.setOnlySummarizableAttributes(true);
//			request.setContext(tc.toContextString());
			//TODO chbzhang dummy data for reviews
			request.setPoiId(tnPoi.getPoiId());
			try {
				request.setCategoryId(Integer.parseInt(tnPoi.getCategoryId()));
	        } catch (NumberFormatException e) {
	        	request.setCategoryId(-1);
	        }
			request.setStartIndex(0);
			request.setEndIndex(9);
//			request.setExcludeReviewsOfEmptyComments(true);
			try {
				GetReviewResponse getReviewResponse = contentManagerServiceProxy.getReviews(request, tc);
	            clientPOI.getReviewResponse = getReviewResponse;
	        } catch (ThrottlingException e) {
	        }
		}
		
		if (tnPoi.poiExtraInfo != null)
		{
			clientPOI.extraProperties.putAll(tnPoi.poiExtraInfo);
		}

		// set new field for ACE 4.0, we need assembling "first_line" form "StreetName" and "HouseNumber"
		clientPOI.bizPoi.setStreetName(tnPoi.getAddress().getStreetName());
		clientPOI.bizPoi.setHouseNumber(tnPoi.getAddress().getDoor());

		return clientPOI;
	}
	
	public static BizPOI convertTnPoiToBizPOI(TnPoi poi, boolean setDistance,int distanceUnit, String version)
	{
		if(poi == null)
		{
			return null;
		}
		
		BizPOI bizPoi = new BizPOI();
		bizPoi.navigable = poi.isNavigable();
		bizPoi.groupID = BizPOI.NO_GROUP;
		bizPoi.vendorCode = poi.getVendor();
		if(logger.isDebugEnabled())
		{
			logger.debug("vendorCode>>" + poi.getVendor() + ",id>>" + poi.getPoiId());
		}
		//TODO:[HB] distance unit?
//		double distance = getUnitDistance((int) poi.getDistanceInMeter(), distanceUnit);
		//bizPoi.distance = setDistance ? (int)poi.getDistanceInMeter() : -1;
		int distance = 0;
		if(setDistance)
		{
			distance = (int)poi.getDistanceToUserInMeter();
			if(distance == 0)
			{
				distance = (int)poi.getDistanceInMeter();
			}
			bizPoi.distance = distance;
		}
		else
		{
			bizPoi.distance =  -1; 
		}
		
		// XXX because client will filt distance 0, set 3 is 10 ft(3 m),
		// maybe seem reasonable
		if (bizPoi.distance == 0)
		{
			bizPoi.distance = 3;
		}   
		Address addr = poi.getAddress();
		if(addr != null)
		{
			bizPoi.address = new Stop();
			if(!"6.4.01".equals(version))
			{
				bizPoi.address.firstLine = addr.getStreetName();
			}
			else
			{
				bizPoi.address.firstLine = addr.getLines().get(AddressFormatConstants.FIRST_LINE);
			}
			

			bizPoi.address.city = addr.getCityName();
			bizPoi.address.state = addr.getState();
			bizPoi.address.zip = addr.getPostalCode();
			bizPoi.address.country = addr.getCountry();
			bizPoi.address.lat = degreeToDM5(addr.getLatitude());
			bizPoi.address.lon = degreeToDM5(addr.getLongitude());
			
			bizPoi.brand = poi.getBrandName();
			String phoneCode = poi.getPhoneNumber();
			//TODO:[HB] format phone number 
			bizPoi.phoneNumber = phoneCode;
			
			if(poi.getGasPriceInfos() != null)
			{
				GasPriceCollection gpc = parseGasPriceData(poi.getGasPriceInfos());
				String[] priceResult = gpc.priceInfo;
				
				if (null != priceResult[0])
				{
					bizPoi.price = priceResult[0];
				}
				
				bizPoi.supplementalInfo = new String[4];
				for (int i = 1; i < priceResult.length; ++i)
				{
					if (null != priceResult[i])
					{
						bizPoi.supplementalInfo[i-1] = priceResult[i];
					}
				} // for
				
				String[] lastUpdateTime = gpc.lastUpdateTime;
				bizPoi.supportInfo = new String[4];
				for (int i = 0; i < lastUpdateTime.length; i ++)
				{
					if (null != lastUpdateTime[i])
					{
						bizPoi.supportInfo[i] = lastUpdateTime[i];
					}
				} // for
				
				if(logger.isDebugEnabled())
				{
					logger.debug("bizPoi.supportInfo:" + bizPoi.supportInfo);
				}
			}
			
			if(poi.getCategoryLabel() != null)
			{
				bizPoi.parentCatName = poi.getCategoryLabel();
			}
			
			String categoryId = poi.getCategoryId();
			if(categoryId != null && !"".equals(categoryId)){
				bizPoi.categoryId = categoryId;
			}else{
				bizPoi.categoryId = "";
			}
			
			return bizPoi;
		}
		
		
		
		return bizPoi;
	}
	public static int convertToClientRating(double rating) {
        // TODO Remove this.
        Random r = new Random();
        if (rating == 0) {
            rating = r.nextDouble() * 5;
        }
        return (int) (rating * 10);
    }
	
	public static double DM5ToDegree(int dm5)
	{
		 return dm5 / DEGREE_MULTIPLIER;
	}
	
	public static int degreeToDM5(double degree) {
        return (int) (degree * DEGREE_MULTIPLIER);
    }
	
	/**
	 * parses gas price
	 * 
	 * @return [0] price of 87 without label "$1.99" [1] price of 87 with label
	 *         "[87] $1.99" [2] price of 89 with label "[89] $2.01" [3] price of
	 *         91 with label "[91] $2.11" [4] price of deisel with label "[D]
	 *         $2.11" any entry can be null, in which case there is no pricing
	 *         for that particular grade of fuel
	 * @return
	 */
	public static GasPriceCollection parseGasPriceData(List<GasPriceInfo> priceList)
	{
		GasPriceCollection gpc = new GasPriceCollection();
		String[] priceInfo = gpc.priceInfo;
		String[] lastUpdateTime = gpc.lastUpdateTime;
		double cheapestGas = 100000d;
		for(int i = 0; priceList != null && i < priceList.size(); i++)
		{
			GasPriceInfo p = (GasPriceInfo)priceList.get(i);
			if(i == 0)
			{
				cheapestGas = p.getPrice();
			}
			switch(p.getGasType())
			{
			case GasPriceTypeDef.TYPE_BASIC_GRADE:
				priceInfo[1] = "[87] $" + p.getPrice();
				lastUpdateTime[0] = getTime(p);
				break;
			case GasPriceTypeDef.TYPE_MID_GRADE:
				priceInfo[2] = "[89] $" + p.getPrice();
				lastUpdateTime[1] = getTime(p);
				break;
			case GasPriceTypeDef.TYPE_HIGH_GRADE:
				priceInfo[3] = "[91] $" + p.getPrice();
				lastUpdateTime[2] = getTime(p);
				break;
			case GasPriceTypeDef.TYPE_DIESEL_GRADE:
				priceInfo[4] = "[D] $" + p.getPrice();
				lastUpdateTime[3] = getTime(p);
				break;
			}
		}
		priceInfo[0] = "$" + cheapestGas;
		logger.debug("cheapestGas price = " + priceInfo[0]);
		return gpc;
	}
	
	private static String getTime(GasPriceInfo p)
	{
		long time = 0;
		try{
			time = p.getUpdateTime().getTime();
		}
		catch(Exception e){
		}
		return Long.toString(time);
	}
	public static class GasPriceCollection
	{
		/**
		 * [0] price of 87 without label "$1.99" [1] price of 87 with label
		 * "[87] $1.99" [2] price of 89 with label "[89] $2.01" [3] price of 91
		 * with label "[91] $2.11" [4] price of deisel with label "[D] $2.11"
		 * any entry can be null, in which case there is no pricing for that
		 * particular grade of fuel
		 */
		public String[] priceInfo = new String[5];

		/**
		 *  [0] lastUpdateTime of 87 with label
		 * "[87] 1187683851177" [1] lastUpdateTime of 89 with label "[89] 1187683851177" [2] lastUpdateTime of 91
		 * with label "[91] 1187683851177" [3] lastUpdateTime of deisel with label "[D] 1187683851177"
		 * any entry can be null, in which case there is no pricing for that
		 * particular grade of fuel
		 */
		public String[] lastUpdateTime = new String[4];
	}
	
	public static PoiReviewSummary convertReviewSummary(TnPoiReviewSummary tnPoiReviewSummary)
	{
		if(tnPoiReviewSummary == null)
		{
			return null;
		}
		PoiReviewSummary reviewSum = new PoiReviewSummary();
		reviewSum.setPoiId(tnPoiReviewSummary.getPoiId());
		reviewSum.setBasePoiId(tnPoiReviewSummary.getBasePoiId());
		reviewSum.setAverageRating(tnPoiReviewSummary.getAverageRating());
		reviewSum.setPopularity(tnPoiReviewSummary.getPopularity());
		reviewSum.setRatingNumber(tnPoiReviewSummary.getRatingNumber());
		reviewSum.setReviewAveragePrice(tnPoiReviewSummary.getReviewAveragePrice());
		reviewSum.setReviewCategoryPath(tnPoiReviewSummary.getReviewCategoryPath());
		reviewSum.setReviewNumber(tnPoiReviewSummary.getReviewNumber());
		reviewSum.setReviewText(tnPoiReviewSummary.getReviewText());
		reviewSum.setReviewType(tnPoiReviewSummary.getReviewType());
		return reviewSum;
	}
	
	public static double getUnitDistance(int distanceInMeter, int unit)
	{
		double unitDist = distanceInMeter;
		if(unit == PoiConstants.UNIT_METER)
		{
			return unitDist;
		}
		else if(unit == PoiConstants.UNIT_MILE)
		{
			unitDist = (distanceInMeter * PoiConstants.KM_TO_MILE) / PoiConstants.KM_TO_METER;
		}
		return unitDist;
	}
}


