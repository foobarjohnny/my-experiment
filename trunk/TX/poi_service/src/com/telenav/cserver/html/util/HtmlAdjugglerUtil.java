/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.html.util;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.telenav.cserver.framework.html.datatype.HtmlClientInfo;
import com.telenav.cserver.framework.html.util.HtmlMessageHelper;
import com.telenav.cserver.framework.html.util.HtmlServiceLocator;
import com.telenav.cserver.poi.struts.Constant;
import com.telenav.cserver.util.TnUtil;
import com.telenav.ws.datatypes.content.weather.WeatherReport;
/**
 * @TODO	Tools class for adjuggler.
 * @author  
 * @version 1.0 
 */
public class HtmlAdjugglerUtil {
	public static final String USER_ID = "${USER_ID}";
	public static final String CARRIER = "${CARRIER}";
	public static final String DEVICE = "${DEVICE}";
	public static final String VERSION = "${VERSION}";
	public static final String PRODUCT = "${PRODUCT}";
	public static final String PLATFORM = "${PLATFORM}";
	public static final String BUILDNUMBER = "${BUILDNUMBER}";
	public static final String PROGRAMCODE = "${PROGRAMCODE}";
	
	public static final String LOGIN_LOCATOR = "{login.http}";
	public static final String MOVIE_LOCATOR = "{movie.http}";
	public static final String SSOTOKEN = "{SSOTOKEN}";
	public static final String APPCODE = "{APPCODE}";
	public static final String ISFIRSTLOGIN = "{ISFIRSTLOGIN}";
	public static final String WIDTH = "{WIDTH}";
	public static final String HEIGHT = "{HEIGHT}";
	public static final String CLIENT_INFO = "{CLIENT_INFO}";
	/**
	 * TODO   get weather string
	 * @param weatherReport
	 * @param weatherReportForcast
	 * @param location
	 * @param clientInfo
	 * @return
	 * @throws Exception
	 */
	public static String getWeatherString(WeatherReport weatherReport,
			WeatherReport weatherReportForcast, String location,
			HtmlClientInfo clientInfo) throws Exception {

		if (weatherReport == null || weatherReportForcast == null) {
			throw new Exception("No weather");
		}

		JSONObject weartherJson = new JSONObject();
		weartherJson.put("location", weatherReport.getLocation());
		weartherJson.put("currentTemperature", weatherReport
				.getCurrentTemperature().getFarenheit() + TnUtil.getWeatherUnit(""));
		weartherJson.put("condition",
				HtmlMessageHelper.getInstance().getMessageValue(clientInfo,
						TnUtil.getCondition(weatherReport)));
		weartherJson.put("lowTemperature", weatherReportForcast.getLowTemperature().getFarenheit()+"");
		weartherJson.put("highTemperature", weatherReportForcast.getHighTemperature().getFarenheit()+"");

		return weartherJson.toString();
	}
	/**
	 * TODO	get Ajuggler URL key
	 * @param actionJO
	 * @return
	 */
	public static String getAjugglerURLkey(JSONObject actionJO) {
		String key = Constant.KEY_ADJUGGLER_PREMIUM;
		String isPremiumAccount = getIsPremiumAccount(actionJO);
		if ("0".equals(isPremiumAccount)) {
			key = Constant.KEY_ADJUGGLER_NONPREMIUM;
		}

		return key;
	}

	public static String getIsPremiumAccount(JSONObject actionJO) {
		try {
			int needPurchase = actionJO.getInt(Constant.JSON_KEY_NEEDPURCHASE);
			if (0 == needPurchase) {
				return "1";
			}
		} catch (JSONException e) {
			return "1";
		}

		return "0";
	}
	/**
	 * TODO	handle Adjuggler URL
	 * @param url
	 * @param clientInfo
	 * @param actionJO
	 * @return
	 */
	public static String handleAdjugglerURL(String url,
			HtmlClientInfo clientInfo, JSONObject actionJO) {
//		String keyword = actionJO.optString("keyword");
//		String userID = clientInfo.getUserId();
		String programCode = clientInfo.getProgramCode();
//		String device = clientInfo.getDevice();

		StringBuilder urlBuilded = new StringBuilder();
		urlBuilded.append(url);
		urlBuilded.append("&kw=programCode_");
		urlBuilded.append(programCode);
		urlBuilded.append("&click=");

		return urlBuilded.toString();
	}
	/**
	 * TODO  deal With External URL
	 * @param adJo
	 * @param clientInfo
	 * @param paramJSON
	 * @return
	 */
	public static JSONObject dealWithExternalURL(JSONObject adJo,
			HtmlClientInfo clientInfo, JSONObject paramJSON) {
		JSONObject adJo_bak = adJo;
		try {
			JSONObject actionJO = adJo.getJSONObject("action");
			String type = actionJO.getString("type");
			if (Constant.KEY_ADJUGGLER_BROWSER.equalsIgnoreCase(type)) {
				String url = actionJO.getString("url");
				Iterator<Entry<String, String>> userInfo = getUserInfoMapping(
						clientInfo, paramJSON).entrySet().iterator();
				while (userInfo.hasNext()) {
					Entry<String, String> entry = userInfo.next();
					if (url.contains(entry.getKey())) {
						url = url.replace(entry.getKey(), entry.getValue());
					}
				}

				actionJO.put("url", url);
				adJo.put("action", actionJO);
			} 
			return adJo;
		} catch (JSONException e) {
			return adJo_bak;
		}
	}
	/**
	 * TODO		get User Info Mapping
	 * @param clientInfo
	 * @param paramJSON
	 * @return
	 * @throws JSONException
	 */
	public static Map<String, String> getUserInfoMapping(
			HtmlClientInfo clientInfo, JSONObject paramJSON) throws JSONException {
		Map<String, String> map = new HashMap<String, String>();
		map.put(USER_ID, clientInfo.getUserId());
		map.put(CARRIER, clientInfo.getCarrier());
		map.put(DEVICE, clientInfo.getDevice());
		map.put(VERSION, clientInfo.getVersion());
		map.put(PRODUCT, clientInfo.getProduct());
		map.put(PLATFORM, clientInfo.getPlatform());
		map.put(BUILDNUMBER, clientInfo.getBuildNo());
		map.put(PROGRAMCODE, clientInfo.getProgramCode());
		
		map.put(LOGIN_LOCATOR, paramJSON.getString("loginLocator"));
		map.put(MOVIE_LOCATOR, paramJSON.getString("movieLocator"));
		map.put(SSOTOKEN, URLEncoder.encode(paramJSON.getString("ssoToken")));
		map.put(APPCODE, paramJSON.getString("appCode"));
		map.put(ISFIRSTLOGIN, "false");
		map.put(CLIENT_INFO, paramJSON.getString("clientInfoStr"));
		map.put(WIDTH, paramJSON.getString("width"));
		map.put(HEIGHT, paramJSON.getString("height"));
		map.put("120", paramJSON.getString("width"));
		map.put("90", paramJSON.getString("height"));

		return map;
	}
	/**
	 * TODO		get Searvice Locator
	 * @param hostUrl
	 * @param key
	 * @return
	 */
	public static String getSearviceLocator(String hostUrl, String key){
		String urlOfTest = HtmlServiceLocator.getInstance().getServiceUrl(hostUrl, key);
		return urlOfTest;
	}
}
