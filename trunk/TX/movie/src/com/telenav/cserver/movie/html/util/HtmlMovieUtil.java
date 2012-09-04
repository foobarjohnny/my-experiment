package com.telenav.cserver.movie.html.util;

import org.apache.log4j.Logger;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.telenav.browser.movie.Constant;
import com.telenav.browser.movie.Util;
import com.telenav.cserver.framework.html.util.HtmlCommonUtil;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.kernel.sso.SsoToken;
import com.telenav.kernel.sso.SsoTokenManager;

public class HtmlMovieUtil {
	
	private static Logger logger = Logger.getLogger(HtmlMovieUtil.class);
	
    public static Stop convertJsonToStop(JSONObject jo)
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
        	logger.error("error occured during convertJsonToStop");
        }

        return address;
    }
    
    public static JSONObject convertStopToJson(Stop address)
    {
    	JSONObject jo = new JSONObject();
        try {
			jo.put("lat", address.lat);
	        jo.put("lon", address.lon);
	        
	        jo.put("label", HtmlCommonUtil.getString(address.label));
	        jo.put("firstLine", HtmlCommonUtil.getString(address.firstLine));
	        jo.put("city", HtmlCommonUtil.getString(address.city));
	        jo.put("state", HtmlCommonUtil.getString(address.state));
	        jo.put("zip", HtmlCommonUtil.getString(address.zip));
	        jo.put("country", HtmlCommonUtil.getString(address.country));
	        
		} catch (JSONException e) {
			logger.error("error occured during convertStopToJson");
		}
        return jo;
    }

    
    public static String getAddressDisplay(Stop stop)
    {
    	String text = "";
    	
    	String firstLine = HtmlCommonUtil.getString(stop.firstLine);
    	if(!"".equals(firstLine))
    	{
    		firstLine = firstLine + ", ";
    	}
    	text= firstLine + stop.city + ", " + stop.state + " " + stop.zip;
    	
    	return text;
    }
    
    
    
    public static String timeFormat(String s) {
        if (s == null || s.length() < 1)
            return "";
        String[] times = s.split("H|M");
        String hour = Integer.parseInt(times[0]) + "";
        String min = Integer.parseInt(times[1]) + "";
        return hour + " hr. " + min + " mins.";
    }
    
    public static int calDistanceInMeter(Stop stop1, Stop stop2){
    	return Util.calDistanceInMeter(stop1.lat/Constant.DEGREE_MULTIPLIER,stop1.lon/Constant.DEGREE_MULTIPLIER,stop2.lat/Constant.DEGREE_MULTIPLIER,stop2.lon/Constant.DEGREE_MULTIPLIER);
     }
    
//    public static String getClientInfoJSONString(HtmlClientInfo info){
//    	JSONObject jo = new JSONObject();
//    	try{
//    		
//    		jo.put("programCode", info.getProgramCode());
//        	jo.put("deviceCarrier", info.getCarrier());
//        	jo.put("platform", info.getPlatform());
//        	jo.put("version", info.getVersion());
//        	jo.put("productType", info.getProduct());
//        	jo.put("device", info.getDevice());
//        	jo.put("locale", info.getLocale());
//        	jo.put("buildNumber", info.getBuildNo());
//    	}catch (JSONException e) {
//    		return "{'device':'att','locale':'en_US','programCode':'ATT'}";
//		}
//    	return jo.toString();
//    }
    
	public static String getUserId(String userToken)
	{
		String userId = "3707312";	
		if(logger.isInfoEnabled()){
			logger.info("userToken:" + userToken);
			logger.info("default userId:" + userId);
		}
		
		SsoToken ssoToken = SsoTokenManager.parseToken(userToken);
		if(ssoToken != null)
		{
			userId = String.valueOf(ssoToken.getUserId());
			if(logger.isInfoEnabled()){
				logger.info("userId from sso token:" + userId);
			}
		}
		//userId = "9409039";
		
		return userId;
	}
	
	public static String getCominedImage(String imageServer,double rating,String imageSize)
	{
		String cominedImageText = "";
		String fullImage = "full_medium_star_icon_unfocused.png";
		String vacancyImage = "vacancy_medium_star_icon_unfocused.png";
		String fullImageClass = "clsStarIcon clsMediumFullStarIcon";
		String vacancyImageClass = "clsStarIcon clsMediumVacancyStarIcon";
		if("big".equals(imageSize))
		{
			fullImage = "full_big_star_icon_unfocused.png";
			vacancyImage = "vacancy_big_star_icon_unfocused.png";
			fullImageClass = "clsStarIcon clsBigFullStarIcon";
			vacancyImageClass = "clsStarIcon clsBigVacancyStarIcon";
		}
		int stars = (int)rating;
		String lightStarImage = "<img class='"+ fullImageClass + "'/>";
		String grayStarImage = "<img class='"+ vacancyImageClass + "'/>";
		StringBuffer combinedImage = new StringBuffer();
		for(int i=0; i<stars; i++){
			combinedImage.append(lightStarImage);
		} 
		int grayStars = 5 - stars;
		for(int i=0; i<grayStars; i++){
			combinedImage.append(grayStarImage);
		}
		cominedImageText = combinedImage.toString();
		return cominedImageText;
	}
	
	public static String getCheckOutURL(String hostUrl)
	{
		String url = Util.getCheckOutURL();
		if(hostUrl.startsWith("http://t-tn"))
		{
			url = Util.getCheckOutURL_test();
		}
		else if(hostUrl.startsWith("http://s-tn"))
		{
			url = Util.getCheckOutURL_stage();
		}
		else if(hostUrl.startsWith("http://hqs-"))
		{
			url = Util.getCheckOutURL_stage();
		}
		return url;
	}
}
