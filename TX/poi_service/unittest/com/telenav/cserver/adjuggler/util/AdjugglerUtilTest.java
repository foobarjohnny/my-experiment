package com.telenav.cserver.adjuggler.util;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.backend.cose.PoiSearchRequest;
import com.telenav.cserver.backend.datatypes.TnPoi;
import com.telenav.cserver.backend.datatypes.cose.GasPriceInfo;
import com.telenav.cserver.backend.datatypes.cose.GasPriceTypeDef;
import com.telenav.cserver.billing.BillingException;
import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.poi.struts.Constant;
import com.telenav.cserver.util.MockHttpServletRequest;
import com.telenav.cserver.util.helper.log2txnode.Log2TxNode;
import com.telenav.cserver.weather.executor.TestUtil;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.tnbrowser.util.DataHandler;
import com.telenav.ws.datatypes.content.weather.Precipitation;
import com.telenav.ws.datatypes.content.weather.SkyCondition;
import com.telenav.ws.datatypes.content.weather.Temperature;
import com.telenav.ws.datatypes.content.weather.WeatherReport;

public class AdjugglerUtilTest {

	DataHandler handler = null;
	MockHttpServletRequest request = null;
	
	
	@Before
	public void setUp() throws Exception 
	{
		if(null == request)
		{
			request = (MockHttpServletRequest)Log2TxNode.getInstance().log2TxNode2HttpServletRequest(request,"mock", 110);
		}
		handler = (DataHandler)request.getAttribute(BrowserFrameworkConstants.CLIENT_INFO);
	}

	@Test
	public void testGetPoiSearchRequest() throws JSONException {
		JSONObject locationJO = new JSONObject();
		locationJO.put("lat", 3737453);
		locationJO.put("lon", -12199983);
		long categoryId = 703;
		int sortType = 4;
		PoiSearchRequest req = AdjugglerUtil.getPoiSearchRequest(handler, locationJO, categoryId, sortType, true);
		Assert.assertNotNull(req);
	}

	@Test
	public void testGetLowestPrice() {	
		
		TnPoi tnpoi = new TnPoi();
		
		GasPriceInfo gasPriceInfo = new GasPriceInfo();
		List<GasPriceInfo> gasPriceInfos = new ArrayList<GasPriceInfo>();
		gasPriceInfos.add(gasPriceInfo);
		
		gasPriceInfo.setPrice(1.0);
		
		gasPriceInfo.setGasType(GasPriceTypeDef.TYPE_BASIC_GRADE);
		tnpoi.setGasPriceInfos(gasPriceInfos);
		AdjugglerUtil.getLowestPrice(tnpoi);
		
		gasPriceInfo.setGasType(GasPriceTypeDef.TYPE_MID_GRADE);
		tnpoi.setGasPriceInfos(gasPriceInfos);
		AdjugglerUtil.getLowestPrice(tnpoi);
		
		gasPriceInfo.setGasType(GasPriceTypeDef.TYPE_HIGH_GRADE);
		tnpoi.setGasPriceInfos(gasPriceInfos);
		AdjugglerUtil.getLowestPrice(tnpoi);

		gasPriceInfo.setGasType(GasPriceTypeDef.TYPE_DIESEL_GRADE);
		tnpoi.setGasPriceInfos(gasPriceInfos);
		AdjugglerUtil.getLowestPrice(tnpoi);
		
	}

	@Test
	public void testGetAdJugglerUrlPath() {
		
		AdjugglerUtil.getAdJugglerUrlPath(handler);
		
	}

	@Test
	public void testGetMovieSearchDate() {
		AdjugglerUtil.getMovieSearchDate("2012-03-05");
	}

	@Test
	public void testGetMovieSearchRequestDTO() throws JSONException {
		
		JSONObject locationJO = new JSONObject();
		locationJO.put("lat", 3737453);
		locationJO.put("lon", -12199983);
		
		JSONObject adJo = new JSONObject();
		JSONObject actionJson = new JSONObject();
		adJo.put("action", actionJson);
		actionJson.put("searchName", "Movie");
		actionJson.put("searchDate", "2012-03-05");
		actionJson.put("SortBy", AdjugglerUtil.MOVIE_SORT_BY_WEEKEND_GROSS);
		
		AdjugglerUtil.getMovieSearchRequestDTO(handler, locationJO, adJo);
	}

	@Test
	public void testGetWeatherString() throws Exception {
		
		WeatherReport weatherReport = new WeatherReport();
		WeatherReport weatherReportForcast = new WeatherReport();
		
		Temperature temp = new Temperature();
		temp.setFarenheit(24.0f);
		weatherReport.setCurrentTemperature(temp);
		SkyCondition skyCondition = SkyCondition.Factory.fromValue("CLEAR");
		weatherReport.setPrecipitation(null);
		weatherReport.setSkyCondition(skyCondition);
		
		weatherReportForcast.setHighTemperature(temp);
		weatherReportForcast.setLowTemperature(temp);
		
		AdjugglerUtil.getWeatherString(weatherReport, weatherReportForcast, "", handler);
	}

	@Test
	public void testSetImageUrlSuffix() throws JSONException {
		
		JSONObject backgroundImageJo = new JSONObject();
		backgroundImageJo.put("focusUrl", "focusUrl");
		backgroundImageJo.put("unfocusUrl", "unfocusUrl");
		
		JSONObject adJo = new JSONObject();
		adJo.put("backgroundImage", backgroundImageJo);
	
		Object o = AdjugglerUtil.setImageUrlSuffix(adJo, "suffix");
		Assert.assertNotNull(o);
	}

	@Test
	public void testGetAjugglerURLkey() {
	}

	@Test
	public void testIsFreeTrialEligible() throws BillingException {
		
		UserProfile profile = TestUtil.getProfile();
		TnContext context = new TnContext();
		
		String productCode = "tn_sprint_monthly_nowls";
		try
		{
			Object o = AdjugglerUtil.isFreeTrialEligible(profile, context, "SprintPCS", productCode, 100);
		}catch(Throwable e)
		{
			
		}
	}

	@Test
	public void testGetAjugglerURLkeyDefault() throws JSONException {
		JSONObject actionJO = new JSONObject();	
		actionJO.put(Constant.JSON_KEY_FROM, Constant.KEY_ADJUGGLER_FROM_MAIN);
		actionJO.put(Constant.JSON_KEY_ISPREMIUMACCOUNT, 0);
		Object o = AdjugglerUtil.getAjugglerURLkeyDefault(actionJO);
		Assert.assertNotNull(o);	
	}

	@Test
	public void testHandleAdjugglerURL() throws JSONException {
		
		JSONObject actionJO = new JSONObject();	
		actionJO.put("keyword", "");
		Object o = AdjugglerUtil.handleAdjugglerURL("http://", handler, actionJO);
		Assert.assertNotNull(o);		
	}

	@Test
	public void testDealWithExternalURL() throws JSONException {
		JSONObject action = new JSONObject();
		action.put("type", Constant.KEY_ADJUGGLER_BROWSER);
		
		JSONObject adJo = new JSONObject();
		adJo.put("action", action);
		Object o = AdjugglerUtil.dealWithExternalURL(adJo, handler);
		Assert.assertNotNull(o);
	}

	@Test
	public void testGetUserInfoMapping() {
		Object o = AdjugglerUtil.getUserInfoMapping(handler);
		Assert.assertNotNull(o);
	}

}
