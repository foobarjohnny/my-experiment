/*
 * Copyright (c) 2009 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 */
package com.telenav.cserver.poi.struts.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.telenav.client.dsm.ContextMgrService;
import com.telenav.client.dsm.ContextMgrStatus;
import com.telenav.client.dsm.Error;
import com.telenav.cserver.backend.datatypes.TnPoi;
import com.telenav.cserver.backend.datatypes.contents.ReviewServicePOIReview;
import com.telenav.cserver.backend.datatypes.cose.Coupon;
import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.common.resource.ResourceHolderManager;
import com.telenav.cserver.common.resource.holder.impl.DsmRuleHolder;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.cserver.poi.datatypes.BasePoi;
import com.telenav.cserver.poi.datatypes.POI;
import com.telenav.cserver.poi.struts.Constant;
import com.telenav.cserver.poi.util.PoiDataConverter;
import com.telenav.cserver.ugc.struts.action.PoiFinderManager;
import com.telenav.cserver.util.TnConstants;
import com.telenav.cserver.util.TnUtil;
import com.telenav.datatypes.locale.v10.Country;
import com.telenav.j2me.datatypes.DataConstants;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.protocol.constants.NodeTypeDefinitions;
import com.telenav.tnbrowser.util.DataHandler;


/**
 * @author pzhang
 *
 * @version 1.0, 2009-4-24
 */
public class PoiUtil {
	
    private static DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
    
    private static SimpleDateFormat time = new SimpleDateFormat("MM/dd/yyyy");
	/** max number for sponsor pois*/
    public final static int MAX_SPONSORED_NUM = 2;	
    
    public static HashMap<String, String> mappedCountryTableforAC = new HashMap();
    static
    {	
    	mappedCountryTableforAC.put(Country.US.getValue(), Country.MX.getValue());     // add coupled countries here    	
    }

    
	/**
	 *  
	 &      & amp;   
     '      & apos;   
     "      & quot;   
     >      & gt;   
     <      & lt;
	 * @param s
	 * @return
	 */
	public static String getXMLString(String s)
	{
		String temp =s;
		temp = temp.replaceAll("&", "&amp;");
		temp = temp.replaceAll("'", "&apos;");
		temp = temp.replaceAll("\"", "&quot;");
		temp = temp.replaceAll(">", "&gt;");
		temp = temp.replaceAll("<", "&lt;");
		
		return temp;
	}
	
	/**
	 * 
	 * @param string
	 * @return
	 */
	public static String getString(String string)
	{
		if(string == null) return "";
		
		return string.trim();
	}
	
	/**
	 * 
	 * @param s
	 * @return
	 */
    public static String amend(String s) {

        if (s == null)
            return "";
        if (s.indexOf(">") > 0 || s.indexOf("<") > 0 || s.indexOf("&") > 0
                || s.indexOf(",") > 0 || s.indexOf("\"") > 0) {
            s = "<![CDATA[" + s + "]]>";
        }
        return s;
    }
    
    /**
     * 
     * @param s
     * @return
     */
    public static String filterLastPara(String s)
    {
    	if("".equals(getString(s))) return "";
    	
    	String[] list = s.split(";");
    	if(list != null)
    	{
    		return list[0];
    	}
    	else
    	{
    		return "";
    	}
    }
    
    /**
     * 
     * @param jo
     * @return
     */
    public static Stop convertAddress(JSONObject jo)
    {
        Stop address = new Stop();
        
        try {
            address.lat = jo.getInt("lat");
            address.lon = jo.getInt("lon");
            
            address.label = jo.getString("label");
            address.firstLine = jo.getString("firstLine");
            address.city = jo.getString("city");
            address.state = jo.getString("state");
            address.zip = jo.getString("zip");
            address.country = jo.getString("country");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        }

        return address;
    }
    
    /**
     * 
     * @param handler
     * @return
     */
    public static TnContext getTnContext(DataHandler handler)
    {
        TnContext tc = new TnContext();
        String mapDataSet = "Navteq";   
        String account = handler.getClientInfo(DataHandler.KEY_USERACCOUNT);
        String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
        String platform = handler.getClientInfo(DataHandler.KEY_PLATFORM);
        String version = handler.getClientInfo(DataHandler.KEY_VERSION);
        String device = handler.getClientInfo(DataHandler.KEY_DEVICEMODEL);
        
        
        long lUserID = getUserId(handler);

        tc.addProperty(TnContext.PROP_LOGIN_NAME , account);
        tc.addProperty(TnContext.PROP_CARRIER , carrier);
        tc.addProperty(TnContext.PROP_DEVICE , device);
        tc.addProperty(TnContext.PROP_PRODUCT , platform);
        tc.addProperty(TnContext.PROP_VERSION , version);

        ContextMgrService cms = new ContextMgrService();
        ContextMgrStatus myStatus = cms.registerContext(lUserID, "BROWSER-SERVER", tc);

        if(myStatus == null || myStatus.getStatusCode() != Error.NO_ERROR)
        {
            tc.addProperty(TnContext.PROP_MAP_DATASET, mapDataSet);
        }
        
        return tc;
    }
    
    public static double convertToDegree(int dm5) {
        return dm5 / Constant.DEGREE_MULTIPLIER;
    }
    
    /**
     * 
     * @param handler
     * @return
     */
    public static String getCountry(DataHandler handler)
    {
    	String country = "";
    	String locale = getString(handler.getClientInfo(DataHandler.KEY_LOCALE));  // en_US, pt_BR, ...
    	if(locale != null && locale.contains("_") && locale.split("_").length > 1)
        {
        	country = locale.split("_")[1];
        } 
       	
    	
    	String carrier = getString(handler.getClientInfo(DataHandler.KEY_CARRIER));  // en_US, pt_BR, ..... 
        boolean isMappedRequired = false;
    	if(carrier.equalsIgnoreCase(ResourceConst.CARRIER_TELCEL) && locale.equalsIgnoreCase(TnConstants.CLIENT_LOCALE_en_US))
    	{
    		isMappedRequired = true;
    	}
    	else if(carrier.equalsIgnoreCase(ResourceConst.CARRIER_NII) && locale.equalsIgnoreCase(TnConstants.CLIENT_LOCALE_en_US))
    	{
    		isMappedRequired = true;
    	}
    	
    	if(isMappedRequired)
    	{
    		String mappedCountry = mappedCountryTableforAC.get(country);
    		if(mappedCountry != null)
    		{
    			return mappedCountry;
    		}
    	}
    	
        return country;
    }
    
    /**
     * 
     * @param handler
     * @return
     */
    public static long getUserId(DataHandler handler)
    {
        String userID = getString(handler.getClientInfo(DataHandler.KEY_USERID));
        long lUserID = -1;
		if (userID.length() > 0) {
			lUserID = Long.parseLong(userID);
		}
        return lUserID;
    }
    
    /**
     * 
     * @param handler
     * @return
     */
    public static String getUserName(DataHandler handler)
    {
        String userName = handler.getClientInfo(DataHandler.KEY_USERACCOUNT);
        return userName;
    }
    
    /**
     * check if the device is touch screen, if it's touch screen, some of  dsr audio will different
     * @param handler
     * @return
     */
    public static boolean isTouchScreen(DataHandler handler)
    {
    	boolean isTouch = false;
    	// Set the user profile.
        
        isTouch = TnUtil.getTouchFlag(getUserProfile(handler));
        return isTouch;
    }
    
    /**
     * 
     * @param handler
     * @return
     */
    public static UserProfile getUserProfile(DataHandler handler)
    {
    	UserProfile userProfile = new UserProfile();

        userProfile.setVersion(handler.getClientInfo(DataHandler.KEY_VERSION));
        userProfile.setPlatform(handler.getClientInfo(DataHandler.KEY_PLATFORM));
        userProfile.setDevice(handler.getClientInfo(DataHandler.KEY_DEVICEMODEL));
        userProfile.setLocale(handler.getClientInfo(DataHandler.KEY_LOCALE));
        userProfile.setCarrier(handler.getClientInfo(DataHandler.KEY_CARRIER));
        userProfile.setProduct(handler.getClientInfo(DataHandler.KEY_PRODUCTTYPE));
        userProfile.setRegion(handler.getClientInfo(DataHandler.KEY_REGION));
        userProfile.setScreenWidth(handler.getClientInfo(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_WIDTH));
        userProfile.setScreenHeight(handler.getClientInfo(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_HEIGHT));
        userProfile.setProgramCode(userProfile.getCarrier());
    	
        return userProfile;
    }
    /**
     * check if this is android platform
     * @param handler
     * @return
     */
    public static boolean isAndroid61(DataHandler handler)
    {
    	UserProfile userProfile = getUserProfile(handler);
    	boolean isAndroid61 = TnUtil.isAndroid(userProfile);
        return isAndroid61;
    }
    
    /**
     * check if this is rim platform & touch
     * @param handler
     * @return
     */
	public static boolean isRimTouch(DataHandler handler) {
		UserProfile userProfile = getUserProfile(handler);
		boolean isRimTouch = TnUtil.isRim(userProfile)
				&& TnUtil.getTouchFlag(userProfile);
		return isRimTouch;
	}
	
	 /**
     * check if this is rim platform & Non-touch
     * @param handler
     * @return
     */
	public static boolean isRimNonTouch(DataHandler handler) {
		UserProfile userProfile = getUserProfile(handler);
		boolean isRimTouch = TnUtil.isRim(userProfile)
				&& (!TnUtil.getTouchFlag(userProfile));
		return isRimTouch;
	}
	
    /**
     * check if this is dual screen
     * @param handler
     * @return
     */
    public static boolean isDual(DataHandler handler) {
        UserProfile userProfile = getUserProfile(handler);
        return TnUtil.isDual(userProfile);
    }
	
	/**
     * check if this is show Post Location  & Open Table
     * @param handler
     * @return
     */
	public static boolean showPostLocationAndOpenTable(DataHandler handler) {
		boolean showPostLocationAndOpenTable = PoiUtil.isRimNonTouch(handler)||PoiUtil.isRimTouch(handler);
		return showPostLocationAndOpenTable;
	}
	
	/**
	 * check whether show page index
	 * 
	 * @param handler
	 * @return
	 */
	public static boolean showPageIndex(DataHandler handler) {
		boolean showPageIndex = PoiUtil.isRimTouch(handler);
		return showPageIndex;
	}
	
	
	
    
    /**
     * 
     * @param handler
     * @return
     */
    public static boolean isWarrior(DataHandler handler)
    {
    	boolean isWarrior = false;
    	String device = handler.getClientInfo(DataHandler.KEY_DEVICEMODEL);
        if("warrior".equalsIgnoreCase(device) || "9800_Mp3".equalsIgnoreCase(device))
        {
        	isWarrior = true;
        }
        return isWarrior;
    }
    
    public static boolean needsBrandListWhenStartUp(DataHandler handler) {
		String versionNo = handler.getClientInfo(DataHandler.KEY_VERSION);
		
		boolean need = false;
		if ("6.2.01".equals(versionNo) || "6.3.01".equals(versionNo) || "6.4.01".equals(versionNo))
		{
			need = true;
		}
		return need;
	}
    
    /**
	 * @param request
	 * @throws ThrottlingException
	 */
	public static  void loadHotBrand(HttpServletRequest request)
			throws ThrottlingException {
		DataHandler handler = (DataHandler) request
		.getAttribute(BrowserFrameworkConstants.CLIENT_INFO);
		TnContext tncontext = PoiUtil.getTnContext(handler);
		// Hot brand for poi search...cbzhang
		PoiFinderManager manager = new PoiFinderManager();
		List<String>  brandNames = manager.searchPOIBrandNames("USA", 100,tncontext);
		TxNode childNode = new TxNode();
		if ((brandNames != null) && (brandNames.size() > 0)) {
			for (int i = 0; i < brandNames.size(); i++) {
				String brand = (String) brandNames.get(i);
				if (brand != null) {
					TxNode node = new TxNode();
					node.addMsg(brand.toUpperCase());
					childNode.addMsg(brand.toUpperCase());
				}
			}
		}
		request.setAttribute("node", childNode);
	}
	
	/**Convert sponsor pois from TN POI */
	public static List<POI> getSponsorPoiResults(List<TnPoi> sPois, boolean setDistance,int distanceUnit,TnContext tc, boolean needReviews)
	{
		if(sPois == null || sPois.size() == 0)
		{
			return null;
		}
		List<POI> sponsorPois = new ArrayList<POI>();
		for(int i = 0; i < sPois.size() && i < MAX_SPONSORED_NUM; i++)
		{
			POI poi = PoiDataConverter.convertTnPoiToPOI(sPois.get(i), setDistance,distanceUnit,tc, needReviews);
			if(poi != null)
			{
				sponsorPois.add(poi);
			}
		}
		
		return sponsorPois;
	}
	
    public static List<BasePoi> getBaseSponsorPoiResults(List<TnPoi> sPois, boolean setDistance,int distanceUnit,TnContext tc, boolean needReviews)
    {
    	if(sPois == null || sPois.size() == 0)
		{
			return null;
		}
    	List<BasePoi> sponsorPois = new ArrayList<BasePoi>();
    	for(int i = 0; i < sPois.size() && i < MAX_SPONSORED_NUM; i++)
    	{
    	    BasePoi poi = PoiDataConverter.convertTnPoiToBasePoi(sPois.get(i), setDistance,distanceUnit,tc, needReviews);
    		if(poi != null)
    		{
    			sponsorPois.add(poi);
    		}
    	}
    	return sponsorPois;
    }
	
	
	/**Convert nature pois from TN POI */
	public static List<POI> getPoiResults(List<TnPoi> pois, int sponsorSize, int maxResults, boolean setDistance,int distanceUnit,TnContext tc, boolean needReviews)
	{
		if(pois == null || pois.size() == 0)
		{
			return null;
		}
		int poiSize = pois.size();
		List<POI> poiResults = new ArrayList<POI>(poiSize);
		for(int i = 0; i < poiSize; i++)
		{
			POI poi = PoiDataConverter.convertTnPoiToPOI(pois.get(i), setDistance,distanceUnit,tc, needReviews);
			if(poi != null)
			{
				poiResults.add(poi);
			}
		}
		return poiResults;
	}
	
	public static List<BasePoi> getBasePoiResults(List<TnPoi> pois, int sponsorSize, int maxResults, boolean setDistance,int distanceUnit,TnContext tc, boolean needReviews)
	{
		if(pois == null || pois.size() == 0)
		{
			return null;
		}
		int poiSize = pois.size();
		List<BasePoi> poiResults = new ArrayList<BasePoi>(poiSize);
		for(int i = 0; i < poiSize; i++)
		{
		    BasePoi poi = PoiDataConverter.convertTnPoiToBasePoi(pois.get(i), setDistance,distanceUnit,tc, needReviews);
			if(poi != null)
			{
				poiResults.add(poi);
			}
		}
		return poiResults;
	}
	
	public static TxNode toTxNode(POI poi, int poiType)
    {
        if (poi == null)
            return null;
        
        
        // main poi node
        TxNode poiTxNode = new TxNode();
        poiTxNode.addValue(poiType);
        poiTxNode.addValue(poi.avgRating);
        poiTxNode.addValue(poi.popularity);
        poiTxNode.addValue(poi.ratingNumber);
        poiTxNode.addValue(poi.userPreviousRating);
        poiTxNode.addValue(poi.isRatingEnable ? 1 : 0);
        poiTxNode.addMsg(String.format("%.0f", poi.priceRange));
        poiTxNode.addMsg(poi.menu);

        // bizpoi
        if (poi.bizPoi != null)
        {
            TxNode bizPoiNode = new TxNode();
            bizPoiNode.addValue(DataConstants.TYPE_BUSINESS_DETAIL);
            bizPoiNode.addMsg(String.valueOf(poi.bizPoi.distance));
            bizPoiNode.addMsg(poi.bizPoi.phoneNumber);
            bizPoiNode.addMsg(poi.bizPoi.price);
            bizPoiNode.addMsg(poi.bizPoi.brand);
            bizPoiNode.addMsg(String.valueOf(poi.poiId));
            bizPoiNode.addMsg(poi.bizPoi.categoryId);
            bizPoiNode.addMsg(poi.bizPoi.parentCatName == null ? "" : poi.bizPoi.parentCatName);
            poiTxNode.addChild(bizPoiNode);
        }

        // poi address
        if (poi.bizPoi != null && poi.bizPoi.address != null)
        {
            TxNode addrNode = new TxNode();
            addrNode.addValue(DataConstants.TYPE_STOP);
            addrNode.addValue(poi.bizPoi.address.lat);
            addrNode.addValue(poi.bizPoi.address.lon);
            addrNode.addMsg(poi.bizPoi.address.label);
            addrNode.addMsg(poi.bizPoi.address.firstLine);
            addrNode.addMsg(poi.bizPoi.address.city);
            addrNode.addMsg(poi.bizPoi.address.state);
            addrNode.addMsg(poi.bizPoi.address.zip);
            addrNode.addMsg(poi.bizPoi.address.country);
            poiTxNode.addChild(addrNode);
        }

        // supplement info
        if (poi.bizPoi != null && poi.bizPoi.supplementalInfo != null && poi.bizPoi.supplementalInfo.length > 0)
        {
            for (int i = 0; i < poi.bizPoi.supplementalInfo.length; i++)
            {
                String price = poi.bizPoi.supplementalInfo[i];
                if (price != null)
                {
                    TxNode supplementInfoNode = new TxNode();
                    supplementInfoNode.addValue(NodeTypeDefinitions.TYPE_SUPPLEMENTINFO_NODE);
                    supplementInfoNode.addMsg(price);
                    supplementInfoNode.addMsg(poi.bizPoi.supportInfo[i]);
                    poiTxNode.addChild(supplementInfoNode);
                }
            }
        }

        // review details
        if (poi.getReviewResponse != null && poi.getReviewResponse.getReview() != null
                && poi.getReviewResponse.getReview().length > 0)
        {
            ReviewServicePOIReview[] poiReviews = poi.getReviewResponse.getReview();
            for (ReviewServicePOIReview review : poiReviews)
            {
                TxNode reviewNode = new TxNode();
                reviewNode.addValue(NodeTypeDefinitions.TYPE_REVIEWEDETAILS_NODE);
                reviewNode.addValue(review.getPoiId());
                reviewNode.addValue(review.getReviewId());
                reviewNode.addMsg(review.getRating());
                reviewNode.addMsg(review.getReviewerName());
                reviewNode.addMsg(review.getReviewText());
                reviewNode.addMsg(format(time, review.getUpdateTime()));
                poiTxNode.addChild(reviewNode);
            }
        }

        // poi ad
        if (poi.ad != null)
        {
            TxNode adNode = new TxNode();
            adNode.addValue(NodeTypeDefinitions.TYPE_POI_AD_NODE);
            adNode.addMsg(poi.ad.getShortMessage());
            adNode.addMsg(poi.ad.getMessage());
            adNode.addMsg(poi.ad.getAdSource());
            adNode.addMsg(poi.ad.getSourceAdId());
            poiTxNode.addChild(adNode);
        }

        // opentalbe info
        if (poi.openTableInfo != null)
        {
            TxNode openTableInfoNode = new TxNode();
            openTableInfoNode.addValue(NodeTypeDefinitions.TYPE_OPENTABLE_INFO_NODE);
            openTableInfoNode.addValue(poi.openTableInfo.isReservable() ? 1 : 0);
            openTableInfoNode.addMsg(poi.openTableInfo.getPartnerPoiId());
            openTableInfoNode.addMsg(poi.openTableInfo.getPartner());

            poiTxNode.addChild(openTableInfoNode);
        }

        // coupon
        if (poi.couponList != null && poi.couponList.size() > 0)
        {
            for (Coupon coupon : poi.couponList)
            {
                TxNode couponNode = new TxNode();
                couponNode.addValue(NodeTypeDefinitions.TYPE_COUPON_NODE);
                couponNode.addMsg(coupon.getDescription());
                couponNode.addMsg(format(dateFormat, coupon.getEndDate()));
                poiTxNode.addChild(couponNode);
            }
        }
        
        return poiTxNode;
      
    }
	
    private static String format(DateFormat df, Date date)
    {
        if (date == null)
            return "";
        else
            return df.format(date);
    }
    
    public static long getLongFrom(String str)
    {
    	long value=-1;
    	if(str!=null&&str.trim().length()>0)
    	{
    		value=Long.valueOf(str);
    	}
    	return value;
    }
    /**
     * Copy from login_startup_service BrowserUtil.java
     * Some of the new freemium flows are supported for only certain device. For eg., the
     * following flows are being supported only for Sprint devices - EVO and EPIC (both 480x800)
     * 
     * a. Free Trial support for lite users
     * b. Bundle users get a one-time popup to upgrade to premium
     * c. New purchase detail page
     * 
     * @param profile
     * @return
     */
	public static boolean getNewFreemiumSupport(DataHandler handler) {
	    UserProfile profile = getUserProfile(handler);
	    return getNewFreemiumSupport(profile);
	}
	
    public static boolean getNewFreemiumSupport(UserProfile profile) {
        boolean supportFreemiumFlow = false;
        DsmRuleHolder dsmHolder = ResourceHolderManager.getResourceHolder("dsm");
        if (dsmHolder == null) {
            return false;
        }
        String flag = getString(dsmHolder.getDsmResponseBy(profile, "supportNewFreemiumFlow", null));
        if ("TRUE".equalsIgnoreCase(flag)) {
            supportFreemiumFlow = true;
        }
        return supportFreemiumFlow;
    }
}
