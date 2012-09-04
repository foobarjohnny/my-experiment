package com.telenav.cserver.adjuggler.util;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.telenav.cserver.browser.util.ClientHelper;
import com.telenav.cserver.poi.struts.Constant;
import com.telenav.tnbrowser.util.DataHandler;

public class AdjugglerDefaultJSON {
	public static String getDefaultJSON(String from, String isPremiumAccount,
			DataHandler handler) {
		JSONObject adJo = null;
		if ((Constant.KEY_ADJUGGLER_FROM_MAIN).equals(from)) {
			if ("0".equals(isPremiumAccount)) {
				adJo = getDefaultJSONForNonPrem(from, handler);
			} else {
				adJo = getDefaultJSONForPrem(from);
			}
		} else {
			adJo = getDefaultJSONForPOIBannerAd(from, handler);
		}

		String os = adJo.toString();
		return os;
	}

	public static JSONObject getDefaultJSONForPrem(String from) {
		JSONObject adJo = new JSONObject();
		try {
			adJo.put("campaignID", "Weather");
			adJo.put("expireTime", "300000");

			JSONObject textJO = new JSONObject();
			textJO.put("en_US", "Weather for current location");
			textJO.put("es_MX", "Weather for current location");
			adJo.put("text", textJO);

			JSONObject actionJO = new JSONObject();
			actionJO.put("type", "WEATHER");
			adJo.put("action", actionJO);
			adJo.put("pageName", from);

			return adJo;
		} catch (JSONException e) {

			return new JSONObject();
		}
	}

	public static JSONObject getDefaultJSONForNonPrem(String from,
			DataHandler handler) {
		JSONObject adJo = new JSONObject();
		try {
			adJo.put("campaignID", "PREM002");
			adJo.put("expireTime", "300000");

			JSONObject textJO = new JSONObject();
			textJO.put("en_US", "Upgrade for additional features");
			textJO.put("es_MX", "Upgrade for additional features");
			adJo.put("text", textJO);

			JSONObject actionJO = new JSONObject();
			actionJO.put("type", "URL");

			String purchaseUrl = Constant.SERVER_LOGIN + "/"
					+ ClientHelper.getModuleNameForLogin(handler)
					+ Constant.URL_PURCHASE;

			actionJO.put("url", purchaseUrl);
			adJo.put("action", actionJO);
			adJo.put("pageName", from);

			return adJo;
		} catch (JSONException e) {

			return new JSONObject();
		}
	}

	public static JSONObject getDefaultJSONForPOIBannerAd(String from,
			DataHandler handler) {
		JSONObject adJo = new JSONObject();
		try {
			adJo.put("campaignID", "Restaurant Reservation");
			adJo.put("expireTime", "300000");

			JSONObject textJO = new JSONObject();
			textJO
					.put("en_US",
							"New! Book a table using <blue>Restaurant Reservations</blue>");
			textJO
					.put("es_MX",
							"New! Book a table using <blue>Restaurant Reservations</blue>");
			adJo.put("text", textJO);

			JSONObject actionJO = new JSONObject();
			actionJO.put("type", "URL");

			String localAppFwkUrl = Constant.SERVER_LOCALAPPFWK + "/"
					+ ClientHelper.getModuleNameForPostLocation(handler)
					+ Constant.URL_LOCALAPPFWK;
			actionJO.put("url", localAppFwkUrl);
			adJo.put("action", actionJO);
			adJo.put("pageName", from);

			return adJo;
		} catch (JSONException e) {

			return new JSONObject();
		}
	}
}
