package com.telenav.cserver.util;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.telenav.cserver.common.resource.ResourceHolderManager;
import com.telenav.cserver.common.resource.holder.impl.DsmRuleHolder;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.poi.struts.Constant;
import com.telenav.datatypes.content.movie.v10.Area;
import com.telenav.datatypes.locale.v10.Country;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.tnbrowser.util.DataHandler;
import com.telenav.ws.datatypes.address.GeoCode;
import com.telenav.ws.datatypes.content.weather.Precipitation;
import com.telenav.ws.datatypes.content.weather.WeatherReport;

public class TnUtil implements TnConstants{
	public static  String[] DAY_OF_WEEK = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
	
	public static final String COUNTRY_SPLIT_CHARACTER = ",";
	public static final String key_GeoCodingCountryList = "GeoCodingCountryList";
	public static final String key_defaultCountry = "defaultCountry";
	public static final String key_supportCountrySelect = "isSupportCountrySelect";
	/**
	 * 
	 * @param string
	 * @return
	 */
	public static int convertToInt(String string)
	{
		int i = 0;
		if(!"".equals(TnUtil.getString(string)))
		{
			i = Integer.parseInt(string);
		}
		return i;
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
	
    public static String getFormatedDate(Date input,String format)
    {
    	SimpleDateFormat dateFormat = new SimpleDateFormat(format,Locale.US);
        return dateFormat.format(input);
    }
  
    public static String getWeatherDisplayDate(Date input)
    {
        return getFormatedDate(input,"MM/dd");
    }
    
    public static String getShotWeekDesc(int day)
    {
    	return DAY_OF_WEEK[day-1];
    }
    
    public static String getLongWeekDesc(int day)
    {
    	return "week." + DAY_OF_WEEK[day-1];
    }
    
    public static String getWeatherUnit(String version)
    {
		String unit = "";
		
	    try {
		        byte[] btt = new byte[]{-62, -80, 70};
		        unit = new String(btt, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
	    return unit;
    	
    	//return "F";
    }
    
    public static String getWeatherUnitForCelsius()
    {
    	String unit = "";
		
	    try {
		        byte[] btt = new byte[]{-62, -80, 67};
		        unit = new String(btt, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
	    return unit;

    	//return "C";
    }
    
    /*
	public static TnContext getTnContext(String[] userInfo)
	{
		TnContext tc = new TnContext();
		String mapDataSet = "Navteq";	
		
		if (userInfo == null)
		{
			tc.addProperty(TnContext.PROP_MAP_DATASET, mapDataSet);
			return tc;
		}
		
		String loginName = userInfo[INDEX_PARAMETER_USER_MIN];
		String carrier = userInfo[INDEX_PARAMETER_CLIENT_CARRIER];
		String device = userInfo[INDEX_PARAMETER_CLIENT_DEVICE];
		String product = userInfo[INDEX_PARAMETER_CLIENT_PLATFORM];
		String version = userInfo[INDEX_PARAMETER_CLIENT_VERSION];
		String userID = userInfo[INDEX_PARAMETER_USER_ID]; 	
		
		long lUserID = -1;
		if (userID != null && userID.length() > 0) {
			lUserID = Long.parseLong(userID);
		}
		tc.addProperty(TnContext.PROP_LOGIN_NAME , loginName);
		tc.addProperty(TnContext.PROP_CARRIER , carrier);
		tc.addProperty(TnContext.PROP_DEVICE , device);
		tc.addProperty(TnContext.PROP_PRODUCT , product);
		tc.addProperty(TnContext.PROP_VERSION , version);

		ContextMgrService cms = new ContextMgrService();
        ContextMgrStatus myStatus = cms.registerContext(lUserID, CALLER_BROWSERSERVER, tc);

        if(myStatus == null || myStatus.getStatusCode() != Error.NO_ERROR)
        {
        	tc.addProperty(TnContext.PROP_MAP_DATASET, mapDataSet);
        }
        
		return tc;
	}*/
	
	public static void getDSMDataFromCServer(TnContext tc, UserProfile profile){
		DsmRuleHolder dsmHolder = ResourceHolderManager.getResourceHolder("dsm");
		
		if(null == tc.getProperty(TnContext.PROP_AD_NEEDSPONSOR) || "".equals(tc.getProperty(TnContext.PROP_AD_NEEDSPONSOR))){
			tc.addProperty(TnContext.PROP_AD_NEEDSPONSOR, dsmHolder.getDsmResponseBy(profile, TnContext.PROP_AD_NEEDSPONSOR, tc));
		}
        
		if(null == tc.getProperty(TnContext.PROP_AD_ENGINE) || "".equals(tc.getProperty(TnContext.PROP_AD_ENGINE))){
			tc.addProperty(TnContext.PROP_AD_ENGINE, dsmHolder.getDsmResponseBy(profile, TnContext.PROP_AD_ENGINE, tc));
		}
        
        if(null == tc.getProperty("adtype") || "".equals(tc.getProperty("adtype"))){
        	tc.addProperty("adtype", dsmHolder.getDsmResponseBy(profile, "adtype", tc));
        }
	}
	
	public static String getDefaultCountry(TnContext tc, UserProfile profile){
		DsmRuleHolder dsmHolder = ResourceHolderManager.getResourceHolder("dsm");
		String defaultCountry = dsmHolder.getDsmResponseBy(profile, key_defaultCountry, tc);
		if(null == defaultCountry){
			defaultCountry = "US";
		}
		
		return defaultCountry;
	}
	
    public static boolean isSupportCountrySelect(DataHandler handler)
    {
    	boolean isSupportCountrySelect = false;
	    UserProfile profile = getUserProfile(handler);
	    if(profile != null)
	    {
	        DsmRuleHolder dsmHolder = ResourceHolderManager.getResourceHolder("dsm");
	        if(dsmHolder != null)
	        {
	        	String flag = getString(dsmHolder.getDsmResponseBy(profile, key_supportCountrySelect, null));
	        	if (flag != null && "true".equalsIgnoreCase(flag)) 
	        	{
	        		isSupportCountrySelect = true;
	        	}
	        }
	    }
    	return isSupportCountrySelect;
    }
	
	public static ArrayList<Country> getGeoCodingCountryList(TnContext tc, UserProfile profile){
		
		DsmRuleHolder dsmHolder = ResourceHolderManager.getResourceHolder("dsm");
		String GeoCodingCountryList = dsmHolder.getDsmResponseBy(profile, key_GeoCodingCountryList, tc);
		ArrayList<Country> countryList = new ArrayList<Country>();
		
		if(null != GeoCodingCountryList)
		{
			String[] CountryArray =  GeoCodingCountryList.split(COUNTRY_SPLIT_CHARACTER);
			for(String ca : CountryArray)
			{
				try
				{	
					Country country = Country.Factory.fromValue(ca.trim().toUpperCase());
					countryList.add(country);
				}
				catch(IllegalArgumentException e)
				{
					e.printStackTrace();
				}
			}
		}
		return countryList;
	}
	

   

    /**
     * 
     * @param handler
     * @return
     */
    public static UserProfile getUserProfile(DataHandler handler) {
        UserProfile userProfile = new UserProfile();

        userProfile.setVersion(handler.getClientInfo(DataHandler.KEY_VERSION));
        userProfile
                .setPlatform(handler.getClientInfo(DataHandler.KEY_PLATFORM));
        userProfile.setDevice(handler
                .getClientInfo(DataHandler.KEY_DEVICEMODEL));
        userProfile.setLocale(handler.getClientInfo(DataHandler.KEY_LOCALE));
        userProfile.setCarrier(handler.getClientInfo(DataHandler.KEY_CARRIER));
        userProfile.setProduct(handler
                .getClientInfo(DataHandler.KEY_PRODUCTTYPE));
        userProfile.setRegion(handler.getClientInfo(DataHandler.KEY_REGION));
        userProfile.setScreenWidth(handler
                .getClientInfo(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_WIDTH));
        userProfile.setScreenHeight(handler
                .getClientInfo(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_HEIGHT));
        userProfile.setProgramCode(userProfile.getCarrier());
        return userProfile;
    }
   
	/**
	 * 
	 * @param profile
	 * @return
	 */
	public static boolean getPoiReivewFlag(UserProfile profile){
		boolean needReview = true;
		DsmRuleHolder dsmHolder = ResourceHolderManager.getResourceHolder("dsm");
		String flag = getString(dsmHolder.getDsmResponseBy(profile,"needreview", null));
		if("FALSE".equalsIgnoreCase(flag))
		{
			needReview = false;
		}
		return needReview;
	}

	/**
	 * 
	 * @param profile
	 * @return
	 */
	public static boolean getTouchFlag(UserProfile profile){
		boolean supportTouch = false;
		DsmRuleHolder dsmHolder = ResourceHolderManager.getResourceHolder("dsm");
		String flag = getString(dsmHolder.getDsmResponseBy(profile,"supportTouch", null));
		if("TRUE".equalsIgnoreCase(flag))
		{
			supportTouch = true;
		}
		return supportTouch;
	}
	
	/**
	 * 
	 * @param profile
	 * @return
	 */
	public static String getPoiFinderVersion(UserProfile profile){
		DsmRuleHolder dsmHolder = ResourceHolderManager.getResourceHolder("dsm");
		String poiFinderVersion = getString(dsmHolder.getDsmResponseBy(profile,"poiFinderVersion", null));
		return poiFinderVersion;
	}
	
	
	/**
	 * 
	 * @param profile
	 * @return
	 */
	public static boolean isAndroid(UserProfile profile){
		boolean isAndroid = false;
		DsmRuleHolder dsmHolder = ResourceHolderManager.getResourceHolder("dsm");
		String flag = getString(dsmHolder.getDsmResponseBy(profile,"isAndroid", null));
		if("TRUE".equalsIgnoreCase(flag))
		{
			isAndroid = true;
		}
		return isAndroid;
	}
	
	public static boolean isRim(UserProfile profile) {
		boolean isRim = false;
		DsmRuleHolder dsmHolder = ResourceHolderManager
				.getResourceHolder("dsm");
		String flag = getString(dsmHolder.getDsmResponseBy(profile, "isRim",
				null));
		if ("TRUE".equalsIgnoreCase(flag)) {
			isRim = true;
		}
		return isRim;
	}
	
    public static boolean isDual(UserProfile profile) {
        boolean isDual = false;
        DsmRuleHolder dsmHolder = ResourceHolderManager
                .getResourceHolder("dsm");
        String flag = getString(dsmHolder.getDsmResponseBy(profile, "isDual",
                null));
        if ("TRUE".equalsIgnoreCase(flag)) {
            isDual = true;
        }
        return isDual;
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
    public static String flagAsBold(String s)
    {
    	if("".equals(TnUtil.getString(s)))
    	{
    		return "";
    	}
    	
    	return "<bold>" + s + "</bold>";
    }
    
    /**
     * 
     * @param s
     * @return
     */
    public static String filterLastPara(String s)
    {
    	if("".equals(TnUtil.getString(s))) return "";
    	
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
     * @param handler
     * @return
     */
    public static String[] getOptionParam(DataHandler handler)
    {
        String account = handler.getClientInfo(DataHandler.KEY_USERACCOUNT);
        String pin = handler.getClientInfo(DataHandler.KEY_USERPIN);
        String userID = handler.getClientInfo(DataHandler.KEY_USERID);

        String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
        String platform = handler.getClientInfo(DataHandler.KEY_PLATFORM);
        String version = handler.getClientInfo(DataHandler.KEY_VERSION);
        String device = handler.getClientInfo(DataHandler.KEY_DEVICEMODEL);

        String[] optionParam = { account, pin, userID, carrier, platform,
                version, device };
        
        return optionParam;
    }
    
    /**
     * 
     * @param jo
     * @return
     */
    public static Stop convertToStop(JSONObject jo)
    {
        Stop stop = new Stop();
        
        try {
            stop.lat = jo.getInt("lat");
            stop.lon = jo.getInt("lon");
            stop.city = jo.getString("city");
            stop.label = jo.getString("label");
            stop.state = jo.getString("state");
            stop.firstLine = jo.getString("firstLine");
            stop.country = jo.getString("country");
            stop.zip = jo.getString("zip");
            
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        
        return stop;
    }
    
    public static boolean isMaps(String product){
    	if(product.equals("ATT_MAPS"))
    		return  true;
    	else
    		return false;
    	
    }
    
    public static boolean isTN(String product){
        if("TN".equals(product)){
          return true; 
        }else{
          return false;  
        }
    }
    
    /**
     * ATT Navigator
     * @param product
     * @return
     */
    public static boolean isATT(String product){
        if("ATT_MAPS".equals(product) || "ATT_NAV".equals(product)){
          return true; 
        }else{
          return false;  
        }
    }
	
    public static boolean isRogersANDROID(DataHandler handler) {
        boolean isRogersANDROID = false;
        String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
        String platform = handler.getClientInfo(DataHandler.KEY_PLATFORM);
        if ("Rogers".equalsIgnoreCase(carrier) && "ANDROID".equalsIgnoreCase(platform)) {
        	isRogersANDROID = true;
        }
        return isRogersANDROID;
    }
    
	public static boolean isRogersANDROID62(DataHandler handler) {
        boolean isRogersANDROID62 = false;
        String version = handler.getClientInfo(DataHandler.KEY_VERSION);
        if (isRogersANDROID(handler)) {
            if (version != null && version.startsWith("6.2")) {
            	isRogersANDROID62 = true;
            }
        }

        return isRogersANDROID62;
    }
	
	public static boolean isATTANDROID(DataHandler handler) {
        boolean isATTANDROID = false;
        String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
        String platform = handler.getClientInfo(DataHandler.KEY_PLATFORM);
        if ("ATT".equalsIgnoreCase(carrier) && "ANDROID".equalsIgnoreCase(platform)) {
            isATTANDROID = true;
        }
        return isATTANDROID;
    }
	
	public static boolean isATTANDROID62(DataHandler handler) {
        boolean isATTANDROID62 = false;
        String version = handler.getClientInfo(DataHandler.KEY_VERSION);
        if (isATTANDROID(handler)) {
            if (version != null && version.startsWith("6.2")) {
                isATTANDROID62 = true;
            }
        }

        return isATTANDROID62;
    }
   
    public static boolean isATTRIM(DataHandler handler) {
        boolean isATTRIM = false;
        String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
        String platform = handler.getClientInfo(DataHandler.KEY_PLATFORM);
        if ("ATT".equalsIgnoreCase(carrier) && "RIM".equalsIgnoreCase(platform)) {
            isATTRIM = true;
        }
        return isATTRIM;
    }

    public static boolean isATTRIM62(DataHandler handler) {
        boolean isATTRIM62 = false;
        String version = handler.getClientInfo(DataHandler.KEY_VERSION);
        if (isATTRIM(handler)) {
            if (version != null && version.startsWith("6.2")) {
                isATTRIM62 = true;
            }
        }

        return isATTRIM62;
    }

    public static boolean isATTRIM63(DataHandler handler) {
        boolean isATTRIM63 = false;
        String version = handler.getClientInfo(DataHandler.KEY_VERSION);
        if (isATTRIM(handler)) {
            if (version != null && version.startsWith("6.3")) {
                isATTRIM63 = true;
            }
        }

        return isATTRIM63;
    }
    
    public static boolean isATTRIM623(DataHandler handler) {
        return isATTRIM62(handler) || isATTRIM63(handler);
    }
    
    /**
     * Sprint Navigation
     * @param product
     * @return
     */
    public static boolean isSprintUser(String carrier){
		boolean isSprint = false;
		if("SprintPCS".equalsIgnoreCase(carrier))
		{
			isSprint = true;
		}

		return isSprint;
    }
	
	/**
     * MMI Demo
     * @param product
     * @return
     */
	public static boolean isMMICarrier(DataHandler handler)
	{
		if(handler == null) return false;
		boolean isMMI = false;
		String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
		if("MMI".equals(carrier))
		{
			isMMI = true;
		}

		return isMMI;
	}

    /**
     * BellMob Virgin Navigation
     * @param product
     * @return
     */
    public static boolean isBell_VMCUser(String carrier){
		boolean isBell_VMC = false;
		if("BellMOb".equalsIgnoreCase(carrier) || "VMC".equalsIgnoreCase(carrier))
		{
			isBell_VMC = true;
		}

		return isBell_VMC;
    }
    
    public static boolean isBoostRIM6001User(DataHandler handler) {
        if (handler == null) {
            return false;
        }
        boolean isBoostRIM6001User = false;
        String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
        String platform = handler.getClientInfo(DataHandler.KEY_PLATFORM);
        String version = handler.getClientInfo(DataHandler.KEY_VERSION);
        if ("RIM".equalsIgnoreCase(platform)
                && "Boost".equalsIgnoreCase(carrier) && "6.0.01".equalsIgnoreCase(version)) {
        	isBoostRIM6001User = true;
        }

        return isBoostRIM6001User;
    } 
    
    public static boolean isSprintAndroid62(DataHandler handler){
    	boolean isSprintAndroid62 = false;
    	String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
        String version = handler.getClientInfo(DataHandler.KEY_VERSION);
        String platform = handler.getClientInfo(DataHandler.KEY_PLATFORM);
        if ("SprintPCS".equalsIgnoreCase(carrier) && "ANDROID".equalsIgnoreCase(platform)) {
            if (version != null && version.startsWith("6.2")) {
            	isSprintAndroid62 = true;
            }
        }

        return isSprintAndroid62;
    }
    
    public static boolean isBoost62(DataHandler handler){
        boolean isBoost62 = false;
        String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
        String version = handler.getClientInfo(DataHandler.KEY_VERSION);
        if ("Boost".equalsIgnoreCase(carrier)) {
            if (version != null && version.startsWith("6.2")) {
                isBoost62 = true;
            }
        }
        return isBoost62;
    }    
      
    public static boolean isBoostAndroid62(DataHandler handler){
        boolean isBoostAndroid62 = false;
        String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
        String version = handler.getClientInfo(DataHandler.KEY_VERSION);
        String platform = handler.getClientInfo(DataHandler.KEY_PLATFORM);
        if ("Boost".equalsIgnoreCase(carrier) && "ANDROID".equalsIgnoreCase(platform)) {
            if (version != null && version.startsWith("6.2")) {
                isBoostAndroid62 = true;
            }
        }

        return isBoostAndroid62;
    }
    
    public static boolean isBoostRIM62(DataHandler handler){
        boolean isBoostRIM62 = false;
        String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
        String version = handler.getClientInfo(DataHandler.KEY_VERSION);
        String platform = handler.getClientInfo(DataHandler.KEY_PLATFORM);
        if ("Boost".equalsIgnoreCase(carrier) && "RIM".equalsIgnoreCase(platform)) {
            if (version != null && version.startsWith("6.2")) {
                isBoostRIM62 = true;
            }
        }

        return isBoostRIM62;
    }
    
    public static boolean isSprint62(DataHandler handler) {
        boolean isSprint62 = false;
        String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
        String version = handler.getClientInfo(DataHandler.KEY_VERSION);
        if ("SprintPCS".equalsIgnoreCase(carrier)) {
            if (version != null && version.startsWith("6.2")) {
                isSprint62 = true;
            }
        }

        return isSprint62;
    }
    
   public static boolean isUSCCRIM62(DataHandler handler) {
        boolean isUSCCRIM62 = false;
        String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
        String version = handler.getClientInfo(DataHandler.KEY_VERSION);
        String platform = handler.getClientInfo(DataHandler.KEY_PLATFORM);
        if ("USCC".equalsIgnoreCase(carrier) && "RIM".equalsIgnoreCase(platform)) {
            if (version != null && version.startsWith("6.2")) {
            	isUSCCRIM62 = true;
            }
        }

        return isUSCCRIM62;
    }
    
   /**
    * Used in touch62 startup.jsp to fix bug of changing Local App "new" image 
    * @param handler
    * @return
    */
    public static boolean isUSCC62(DataHandler handler) {
        boolean isUSCC62 = false;
        String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
        String version = handler.getClientInfo(DataHandler.KEY_VERSION);
        if ("USCC".equalsIgnoreCase(carrier)) {
            if (version != null && version.startsWith("6.2")) {
            	isUSCC62 = true;
            }
        }

        return isUSCC62;
    }
    
    public static boolean isSprintRim62(DataHandler handler) {
        return isSprint62(handler) && isRim(handler);
    }

    public static boolean isRim(DataHandler handler) {
        String platform = handler.getClientInfo(DataHandler.KEY_PLATFORM);
        if ("RIM".equalsIgnoreCase(platform)) {
            return true;
        }

        return false;
    }
    
    public static boolean isBoostNotTN60(String carrier, String versionNo){
		boolean isBoostUser = false;
		if(!"6.0.01".equalsIgnoreCase(versionNo) && "Boost".equalsIgnoreCase(carrier))
		{
			isBoostUser = true;
		}

		return isBoostUser;
    }
    
    public static boolean isTMOUser(String product){
		boolean isTMO = false;
		if(product.contains("TMO_"))
		{
			isTMO = true;
		}

		return isTMO;
    }
    
    public static boolean isTMOUser(DataHandler handler) {
        return isTMOUser(handler.getClientInfo(DataHandler.KEY_PRODUCTTYPE));
    }
    
    public static boolean isUsccUser(String product) {
        if ("UN".equalsIgnoreCase(product)
                || "UN_MAPS".equalsIgnoreCase(product)) {
            return true;
        }
        return false;
    }
    
	public static boolean isUsccAndroid62(DataHandler handler) {
    	boolean isUsccAndroid62 = false;
    	String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
        String version = handler.getClientInfo(DataHandler.KEY_VERSION);
        String platform = handler.getClientInfo(DataHandler.KEY_PLATFORM);
        if ("USCC".equalsIgnoreCase(carrier) && "ANDROID".equalsIgnoreCase(platform)) {
            if (version != null && version.startsWith("6.2")) {
            	isUsccAndroid62 = true;
            }
        }

        return isUsccAndroid62;
   }
    

    public static boolean isRogersCarrier(String carrier){
		boolean isRogersCarrier = false;
		if(("Rogers").equalsIgnoreCase(carrier))
		{
			isRogersCarrier = true;
		}
		//Rogers and Fido use the same flow
		if(("Fido").equalsIgnoreCase(carrier))
		{
			isRogersCarrier = true;
		}

		return isRogersCarrier;
    }
    
    public static boolean isVNUser(String product){
		boolean isVN = false;
		if(product.contains("VN_") || product.contains("BAW_"))
		{
			isVN = true;
		}

		return isVN;
    }
    
    public static boolean isVNUser(DataHandler handler){
        return isVNUser(handler.getClientInfo(DataHandler.KEY_PRODUCTTYPE));
    }
    
    public static boolean isTMOAndroidUser(DataHandler handler) {
        if (handler == null) {
            return false;
        }
        boolean isTMOAndroidUser = false;
        String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
        String platform = handler.getClientInfo(DataHandler.KEY_PLATFORM);
        
        if ("ANDROID".equalsIgnoreCase(platform)
                && "T-Mobile".equalsIgnoreCase(carrier)) {
            isTMOAndroidUser = true;
        }

        return isTMOAndroidUser;
    }
    
    public static boolean isTMORIM62(DataHandler handler) {
        if (handler == null) {
            return false;
        }
        boolean isTMORIM62 = false;
        String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
        String platform = handler.getClientInfo(DataHandler.KEY_PLATFORM);
        String version = handler.getClientInfo(DataHandler.KEY_VERSION);
        if ("T-Mobile".equalsIgnoreCase(carrier) && "RIM".equals(platform)) {
            if (version != null && version.startsWith("6.2")) {
            	isTMORIM62 = true;
            }
        }

        return isTMORIM62;
    }
    
    public static boolean isTMOAndroid62(DataHandler handler) {
        if (handler == null) {
            return false;
        }
        boolean isTMOAndroid62 = false;
        String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
        String platform = handler.getClientInfo(DataHandler.KEY_PLATFORM);
        String version = handler.getClientInfo(DataHandler.KEY_VERSION);
        if ("T-Mobile".equalsIgnoreCase(carrier) && "ANDROID".equals(platform)) {
            if (version != null && version.startsWith("6.2")) {
            	isTMOAndroid62 = true;
            }
        }

        return isTMOAndroid62;
    }

    public static boolean isTMOAndroidLiteUser(DataHandler handler) {
        if (handler == null) {
            return false;
        }
        boolean isTMOAndroidLiteUser = false;
        String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
        String platform = handler.getClientInfo(DataHandler.KEY_PLATFORM);
        String producttype = handler.getClientInfo(DataHandler.KEY_PRODUCTTYPE);
       
        if ("ANDROID".equalsIgnoreCase(platform)
                && "T-Mobile".equalsIgnoreCase(carrier)
                && "TMO_lite".equalsIgnoreCase(producttype)) {
            isTMOAndroidLiteUser = true;
        }

        return isTMOAndroidLiteUser;
    }

    public static boolean isVNAndroidUser(DataHandler handler) {
        if (handler == null) {
            return false;
        }
        boolean isVNAndroidUser = false;
        String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
        String platform = handler.getClientInfo(DataHandler.KEY_PLATFORM);
        
        if ("ANDROID".equalsIgnoreCase(platform)
                && "Verizon".equalsIgnoreCase(carrier)) {
        	isVNAndroidUser = true;
        }

        return isVNAndroidUser;
    }
    
    public static boolean isVNRIM62(DataHandler handler) {
        if (handler == null) {
            return false;
        }
        boolean isVNRIM62 = false;
        String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
        String platform = handler.getClientInfo(DataHandler.KEY_PLATFORM);
        String version = handler.getClientInfo(DataHandler.KEY_VERSION);

        if ("RIM".equalsIgnoreCase(platform)
                && "Verizon".equalsIgnoreCase(carrier) && version != null
                && version.startsWith("6.2")) {
            isVNRIM62 = true;
        }

        return isVNRIM62;
    }

    public static boolean isVNAndroidFreeUser(DataHandler handler) {
        if (handler == null) {
            return false;
        }
        boolean isVNAndroidFreeUser = false;
        String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
        String platform = handler.getClientInfo(DataHandler.KEY_PLATFORM);
        String producttype = handler.getClientInfo(DataHandler.KEY_PRODUCTTYPE);
      
        if ("ANDROID".equalsIgnoreCase(platform)
                && "Verizon".equalsIgnoreCase(carrier)
                && "VN_FREE".equalsIgnoreCase(producttype)) {
        	isVNAndroidFreeUser = true;
        }

        return isVNAndroidFreeUser;
    }
    
    public static boolean isBAWPAIDUser(DataHandler handler) {
        if (handler == null) return false;
        if (!isVNRIM62(handler)) return false;
        boolean isBAWPAIDUser = false;
        String prudctType = handler.getClientInfo(DataHandler.KEY_PRODUCTTYPE);
        if ( prudctType.startsWith("BAW_PAID")) {
        	isBAWPAIDUser = true;
        }
        return isBAWPAIDUser;
    }
    
    public static boolean isBAWUser(DataHandler handler) {
        if (handler == null) return false;
        if (!isVNRIM62(handler)) return false;
        boolean isBAWUser = false;
        String prudctType = handler.getClientInfo(DataHandler.KEY_PRODUCTTYPE);
        if ( prudctType.startsWith("BAW")) {
        	isBAWUser = true;
        }
        return isBAWUser;
    }
    
    public static boolean isCanadianCarrier(DataHandler handler)
	{
		if(handler == null) return false;
		boolean isCanadianCarrier = false;
		String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
		if("Rogers".equals(carrier) || "Fido".equals(carrier) || "BellMob".equals(carrier) || "VMC".equals(carrier))
		{
			isCanadianCarrier = true;
		}

		return isCanadianCarrier;
	}
    
    public static boolean isBell_VMC(DataHandler handler)
	{
		if(handler == null) return false;
		boolean isBell_VMC = false;
		String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
		if("BellMob".equals(carrier) || "VMC".equals(carrier))
		{
			isBell_VMC = true;
		}

		return isBell_VMC;
	}
    
    public static boolean isBellCarrier(DataHandler handler)
	{
		if(handler == null) return false;
		boolean isBellCarrier = false;
		String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
		if("BellMob".equals(carrier))
		{
			isBellCarrier = true;
		}

		return isBellCarrier;
	}
    
    public static boolean isVMCCarrier(DataHandler handler)
    {
		if(handler == null) return false;
		boolean isVMCCarrier = false;
		String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
		if("VMC".equals(carrier))
		{
			isVMCCarrier = true;
		}

		return isVMCCarrier;
    }
    
    public static boolean isTelcelRIM64(DataHandler handler) 
    {
        if (handler == null) 
        {
            return false;
        }
        boolean isTelcelRIM64 = false;
        String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
        String platform = handler.getClientInfo(DataHandler.KEY_PLATFORM);
        String version = handler.getClientInfo(DataHandler.KEY_VERSION);

        if ("RIM".equalsIgnoreCase(platform)&& "Telcel".equalsIgnoreCase(carrier) && version != null&& version.startsWith("6.4"))
        {
        	isTelcelRIM64 = true;
        }

        return isTelcelRIM64;
    }
    
    
    /**
     * Get login folder for purchase from poi
     * @param handler
     * @return String
     */    
    public static String getLoginFolder(DataHandler handler) {
        String loginFolder = "";
        if (handler == null) {
            return "";
        }

        String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
        if ("ATT".equalsIgnoreCase(carrier)) {
            loginFolder = "/att";
        } else if ("BellMob".equalsIgnoreCase(carrier)) {
            loginFolder = "/bell_virgin";
        } else if ("Rogers".equalsIgnoreCase(carrier)) {
            loginFolder = "/rogers";
        } else if ("SprintPCS".equalsIgnoreCase(carrier)) {
            loginFolder = "/sprint";
        } else if ("T-Mobile".equalsIgnoreCase(carrier)) {
            loginFolder = "/t_mobile";
        } else if ("Verizon".equalsIgnoreCase(carrier)) {
            loginFolder = "/verizon";    
        } else if ("USCC".equalsIgnoreCase(carrier)) {
            loginFolder = "/uscc";
        } else if ("Boost".equalsIgnoreCase(carrier)) {
            loginFolder = "/boost";
        }

        return loginFolder;
    }
    
    
    /**
     * convert TN version no to carrier version no
     * @param handler
     * @return
     */
    public static String getVersionNo(DataHandler handler)
    {
        String versionNo = handler.getClientInfo(DataHandler.KEY_VERSION);
        String product = handler.getClientInfo(DataHandler.KEY_PRODUCTTYPE);
        String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
        
        /**
         * version no for ATT
         */
        if(isATT(product))
        {
            if(versionNo.endsWith("6.0.01"))
            {
            	versionNo = "2.0";
            }
            else if(versionNo.endsWith("6.1.01"))
            {
                versionNo = "2.1";
            }
        }
        /**
         * version no for Sprint
         */
        else if(isSprintUser(carrier))
        {
            if(versionNo.endsWith("6.0.01"))
            {
            	versionNo = "3.0";
            }
            else if(versionNo.endsWith("6.1.01"))
            {
                versionNo = "3.1";
            }       	
        }
        return versionNo;
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
	
	public static String getCarrierForBannerAds(String carrier){
		if("SprintPCS".equalsIgnoreCase(carrier)){
        	carrier = "SPRINT";
        } else if("T-Mobile".equalsIgnoreCase(carrier)) {
            carrier = "TMOBILE";
        } else if("USCC".equalsIgnoreCase(carrier)) {
            carrier = "US_CELLULAR";
        } else if ("BellMob".equalsIgnoreCase(carrier)){
        	carrier = "BELL_CANADA";
        } else if ("Boost".equalsIgnoreCase(carrier)){
        	carrier = "BOOST";
        } else if ("Rogers".equalsIgnoreCase(carrier)){
        	carrier = "ROGERS";
        } else if ("Verizon".equalsIgnoreCase(carrier)){
        	carrier = "VERIZON";
        }
		return carrier;
	}
	
	public static Area getArea(long lat, long lon, int radiusInMeters) {
		Area area = new Area();
		GeoCode gc = new GeoCode();
		gc.setLatitude(lat / Constant.DEGREE_MULTIPLIER);
		gc.setLongitude(lon / Constant.DEGREE_MULTIPLIER);
		area.setGeoCode(gc);
		area.setRadiusInMeter(radiusInMeters);
		return area;
	}
	
	public static String getCondition(WeatherReport report) {
		String status = "";
		Precipitation precipitation = report.getPrecipitation();
		if (precipitation != null) {
			status = precipitation.getValue();
		}
		if (status == null || status.length() == 0
				|| status.equalsIgnoreCase("UNKNOWN")) {
			status = report.getSkyCondition().getValue();
		}

		return TnUtil.getString(status);
	}

	public static String formatTemp(float temp) {

		String result = "";
		if (temp > 10d)
			result = String.format("%.0f\u00B0F", temp);
		else
			result = String.format("%.1f\u00B0F", temp);
		return result;
	}

	public static boolean isValid(String s) {
		return s != null && s.length() > 0;
	}
	
	public static String getTitleCase(String s) {
		final StringBuffer result = new StringBuffer(s.length());
		String[] words = s.split("\\s");
		for (int i = 0, l = words.length; i < l; ++i) {
			if (words[i].length() > 0) {
				if (i > 0)
					result.append(" ");
				result.append(Character.toUpperCase(words[i].charAt(0)))
						.append(words[i].substring(1));

			}
		}
		return result.toString();
	}

	public static boolean isEligibleForNewFeedBack(DataHandler handler) {
		return isSprintRim62(handler)||isUSCCRIM62(handler)||isATTRIM63(handler)|| isTMORIM62(handler)||isVNRIM62(handler);
	}
	
	public static boolean isBellANDROID(DataHandler handler) {
        boolean isBellANDROID = false;
        String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
        String platform = handler.getClientInfo(DataHandler.KEY_PLATFORM);
        if ("BellMob".equalsIgnoreCase(carrier) && "ANDROID".equalsIgnoreCase(platform)) {
        	isBellANDROID = true;
        }
        return isBellANDROID;
    }
    
	public static boolean isBellANDROID62(DataHandler handler) {
        boolean isBelllANDROID62 = false;
        String version = handler.getClientInfo(DataHandler.KEY_VERSION);
        if (isBellANDROID(handler)) {
            if (version != null && version.startsWith("6.2")) {
            	isBelllANDROID62 = true;
            }
        }

        return isBelllANDROID62;
    }
	
	public static boolean isVMCANDROID(DataHandler handler) {
        boolean isVMCANDROID = false;
        String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
        String platform = handler.getClientInfo(DataHandler.KEY_PLATFORM);
        if ("VMC".equalsIgnoreCase(carrier) && "ANDROID".equalsIgnoreCase(platform)) {
        	isVMCANDROID = true;
        }
        return isVMCANDROID;
    }
    
	public static boolean isVMCANDROID62(DataHandler handler) {
        boolean isVMCANDROID62 = false;
        String version = handler.getClientInfo(DataHandler.KEY_VERSION);
        if (isVMCANDROID(handler)) {
            if (version != null && version.startsWith("6.2")) {
            	isVMCANDROID62 = true;
            }
        }

        return isVMCANDROID62;
    }
	
	public static boolean isRIM64(DataHandler handler)
	{
		String version = handler.getClientInfo(DataHandler.KEY_VERSION);
		{
			if (version != null && version.startsWith("6.4"))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean isKilometerUnit(DataHandler handler){
		return isUseMetricSystem(handler);
	}
	
	public static boolean isCelciusUnit(DataHandler handler){
		return isUseMetricSystem(handler);
	}	
	
	public static boolean isUseMetricSystem(DataHandler handler){
		boolean useMetricSystem = true;
		String locale = handler.getClientInfo(DataHandler.KEY_LOCALE);
		String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
		String country = locale.split("_")[1];
		
		// US and MX carrier set wrong locales, so we have to add hard code logic
		if(country.equals("US") || country.equals("MX")) {
			if(!carrier.equals("Telcel") && !carrier.equals("NII")) {
				useMetricSystem = false;
			}
		}
		
		return useMetricSystem;
	}
}