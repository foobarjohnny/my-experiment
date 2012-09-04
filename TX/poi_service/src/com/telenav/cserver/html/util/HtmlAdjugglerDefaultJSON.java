/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.html.util;

import java.net.URLEncoder;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.telenav.cserver.framework.html.datatype.HtmlClientInfo;
/**
 * @TODO	parse Adjuggler related data.
 * @author  
 * @version 1.0 
 */
public class HtmlAdjugglerDefaultJSON {
	/**
	 * TODO   get the default json data.
	 * @param isPremiumAccount
	 * @param clientInfo
	 * @param paramJSON
	 * @return
	 */
	public static String getDefaultJSON(String isPremiumAccount, HtmlClientInfo clientInfo, JSONObject paramJSON) {
		JSONObject adJo = null;
		if ("0".equals(isPremiumAccount)) {
			adJo = getDefaultJSONForNonPrem(clientInfo, paramJSON);
		} else {
			adJo = getDefaultJSONForPrem();
		}

		String os = adJo.toString();
		return os;
	}
	/**
	 * TODO		get Default JSON For Prem
	 * @return
	 */
	public static JSONObject getDefaultJSONForPrem() {
		JSONObject adJo = new JSONObject();
		try {
			adJo.put("campaignID", "Weather");
			adJo.put("expireTime", "3000");

			JSONObject textJO = new JSONObject();
			textJO.put("en_US", "Weather for current location");
			textJO.put("es_MX", "Weather for current location");
			adJo.put("text", textJO);

			JSONObject actionJO = new JSONObject();
			actionJO.put("type", HtmlConstants.KEY_ADJUGGLER_WEATHER);
			adJo.put("action", actionJO);

			return adJo;
		} catch (JSONException e) {

			return new JSONObject();
		}
	}
	/**
	 * TODO	get Default JSON For Non-Prem
	 * @param clientInfo
	 * @param paramJSON
	 * @return
	 */
	public static JSONObject getDefaultJSONForNonPrem(HtmlClientInfo clientInfo, JSONObject paramJSON) {
		JSONObject adJo = new JSONObject();
		try {
			adJo.put("campaignID", "PREM002");
			adJo.put("expireTime", "3000");

			JSONObject textJO = new JSONObject();
			textJO.put("en_US", "Upgrade for additional features");
			textJO.put("es_MX", "Upgrade for additional features");
			adJo.put("text", textJO);

			JSONObject actionJO = new JSONObject();
			actionJO.put("type", HtmlConstants.KEY_ADJUGGLER_BROWSER);
			
			String loginServiceLocator = paramJSON.getString("loginLocator");
			String ssoToken = URLEncoder.encode(paramJSON.getString("ssoToken"));
			String appCode = paramJSON.getString("appCode");
			String clientInfoStr = paramJSON.getString("clientInfoStr");
			String width = paramJSON.getString("width");
			String height = paramJSON.getString("height");

			String purchaseUrl = loginServiceLocator + "/html/upsell.do?appCode="+appCode+"&ssoToken="+ssoToken+"&isFirstLogin=false&clientInfo="+clientInfoStr+"&width="+width+"&height="+height;

			actionJO.put("url", purchaseUrl);
			adJo.put("action", actionJO);

			return adJo;
		} catch (JSONException e) {

			return new JSONObject();
		}
	}
}
