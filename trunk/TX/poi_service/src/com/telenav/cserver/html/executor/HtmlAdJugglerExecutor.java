/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.html.executor;

import org.apache.log4j.Logger;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.backend.proxy.BackendProxyManager;
import com.telenav.cserver.backend.proxy.adjuggler.AdjugglerProxy;
import com.telenav.cserver.backend.proxy.adjuggler.AdjugglerRequest;
import com.telenav.cserver.backend.proxy.adjuggler.AdjugglerResponse;
import com.telenav.cserver.framework.executor.AbstractExecutor;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.html.datatype.HtmlClientInfo;
import com.telenav.cserver.framework.html.util.HtmlMessageHelper;
import com.telenav.cserver.html.util.HtmlAdjugglerDefaultJSON;
import com.telenav.cserver.html.util.HtmlAdjugglerUtil;
import com.telenav.cserver.poi.struts.Constant;
import com.telenav.cserver.poi.struts.util.RGCUtil;
import com.telenav.cserver.weather.executor.WeatherProxy;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.ws.datatypes.content.weather.WeatherDetails;
import com.telenav.ws.datatypes.content.weather.WeatherReport;
/**
 * @TODO	Call the executor to implement business logic
 * @author  
 * @version 1.0 
 */
public class HtmlAdJugglerExecutor extends AbstractExecutor {
//	public static PropertyResourceBundle serverBundle = null;
	private static Logger logger = Logger
			.getLogger(HtmlAdJugglerExecutor.class);

	public static final String GAS_PRICE = "${GAS_PRICE}";
	public static final String NEW_MOVIES = "${NEW_MOVIES}";
	public static final String POPULAR_MOVIES_THIS_WEEK = "${POPULAR_MOVIES_THIS_WEEK}";
	public static final String TRAFFIC_DATA = "${TRAFFIC_DATA}";
	public static final String WEATHER = "${WEATHER}";

	@Override
	public final void doExecute(final ExecutorRequest req,
			final ExecutorResponse resp, final ExecutorContext context)
			throws ExecutorException {
//		CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
//		cli.setFunctionName("AdJugglerExecutor");
//
//		HtmlAdJugglerRequest adJugglerRequest = (HtmlAdJugglerRequest) req;
//		HtmlAdJugglerResponse adJugglerResponse = (HtmlAdJugglerResponse) resp;
//		try {
//			HtmlClientInfo clientInfo = adJugglerRequest.getClientInfo();
//			clientInfo.setUserId(adJugglerRequest.getUserId());
//
////			serverBundle = (PropertyResourceBundle) PropertyResourceBundle
////					.getBundle(HtmlAdjugglerUtil
////							.getAdJugglerUrlPath(clientInfo));
//
//			JSONObject actionJO = adJugglerRequest.getActionJO();
//			JSONObject locationJO = actionJO
//					.getJSONObject(Constant.JSON_KEY_LOCATION);
//			String isPremiumAccount = HtmlAdjugglerUtil.getIsPremiumAccount(actionJO);
//
//			String url = getAjugglerURL(actionJO, clientInfo, adJugglerRequest.getHostUrl());
//
//			cli.addData("parameters", "isPremiumAccount="
//					+ isPremiumAccount + "&locationJO" + locationJO.toString()
//					+ "&url=" + url);
//			logger.error("adjuggler url=" + url);
//			// MisLog later on
//			try {
//				AdjugglerRequest adjugglerRequest = new AdjugglerRequest();
//				adjugglerRequest.setUrl(url);
//				AdjugglerProxy adjugglerProxy = BackendProxyManager
//						.getBackendProxyFactory().getBackendProxy(
//								AdjugglerProxy.class);
//				TnContext tc = context.getTnContext();
//				AdjugglerResponse adjugglerResponse = adjugglerProxy
//						.getAdjugglerInfo(adjugglerRequest, tc);
//				String os = "";
//				if (adjugglerResponse.isSuccess()) {
//					String outPutString = adjugglerResponse.getJsonStr();
//					JSONObject adJo = new JSONObject(outPutString);
//					logger.info("adJo from adjuggler=" + outPutString);
//
//					this.getWeather(adJo, clientInfo, locationJO);
//					HtmlAdjugglerUtil.dealWithExternalURL(adJo, clientInfo, adJugglerRequest.getParamJSON());
//
//					logger.info("modified adJo=" + adJo);
//					os = adJo.toString();
//				} else {
//					logger.info("Call adjuggler error, statusCode: "
//							+ adjugglerResponse.getStatusCode());
//					os = HtmlAdjugglerDefaultJSON.getDefaultJSON(isPremiumAccount, clientInfo, adJugglerRequest.getParamJSON());
//				}
//				cli.addData("results", os);
//				cli.complete();
//
//				adJugglerResponse.setResult(os);
//			} catch (Exception e) {
//				System.err.println("Fatal protocol violation: "
//						+ e.getMessage());
//				logger.fatal("adjuggler Fatal protocol violation: ", e);
//
//				String result = HtmlAdjugglerDefaultJSON.getDefaultJSON(isPremiumAccount, clientInfo, adJugglerRequest.getParamJSON());
//
//				cli.addData("results", result);
//				cli.complete();
//				adJugglerResponse.setResult(result);
//			}
//		} catch (JSONException e) {
//			logger.fatal("adjuggler jsonexception: ", e);
//		}
	}

//	public String getAjugglerURL(JSONObject actionJO, HtmlClientInfo clientInfo, String hostUrl) {
//		String key = HtmlAdjugglerUtil.getAjugglerURLkey(actionJO);
//		String url = HtmlAdjugglerUtil.getSearviceLocator(hostUrl, key);
//
//		return HtmlAdjugglerUtil.handleAdjugglerURL(url, clientInfo, actionJO);
//	}
//
//	public JSONObject getWeather(JSONObject adJo, HtmlClientInfo clientInfo,
//			JSONObject locationJO) {
//		JSONObject adJo_bak = adJo;
//		try {
//			JSONObject textADJO = adJo.getJSONObject("text");
//			String enUS = textADJO.getString("en_US");
//			String esMX = textADJO.getString("es_MX");
//			try {
//				if (enUS.contains(WEATHER) || esMX.contains(WEATHER)) {
//
//					RGCUtil rgcutil = new RGCUtil();
//					Stop stop = rgcutil.getCurrentLocation(locationJO
//							.getInt("lat"), locationJO.getInt("lon"),
//							new TnContext());
//					WeatherDetails weatherDetails = WeatherProxy.getInstance()
//							.getWeatherForLocation(stop, 1, false);
//
//					WeatherReport weatherReport = weatherDetails
//							.getCurrentWeather();
//					WeatherReport[] weatherReportForcast = weatherDetails
//							.getForecast();
//
//					String locationStr = HtmlMessageHelper.getInstance()
//							.getMessageValue(clientInfo,
//									"common.currentLocation");
//					if (stop != null) {
//						locationStr = stop.city;
//					}
//					String weatherStr = HtmlAdjugglerUtil.getWeatherString(
//							weatherReport, weatherReportForcast[0],
//							locationStr, clientInfo);
//
//					enUS = enUS.replace(WEATHER, weatherStr);
//					esMX = esMX.replace(WEATHER, weatherStr);
//
//					String image = "weatherSmall_" + weatherReport.getPicCode()
//							+ ".png";
//					adJo.put("iconImage", image);
//
//					textADJO.put("en_US", enUS);
//					textADJO.put("es_MX", esMX);
//				}
//			} catch (Exception e) {
//				logger.fatal("getWeather: ", e);
//				textADJO = adJo.getJSONObject("defaultText");
//			}
//
//			adJo.put("text", textADJO);
//			return adJo;
//		} catch (JSONException e) {
//			return adJo_bak;
//		}
//	}
}
