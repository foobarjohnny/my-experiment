package com.telenav.cserver.html.util;

import org.json.me.JSONObject;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.framework.html.datatype.HtmlClientInfo;
import com.telenav.cserver.poi.struts.Constant;
import com.telenav.cserver.util.helper.generictest.DataSource;
import com.telenav.cserver.util.helper.generictest.GenericTest;
import com.telenav.ws.datatypes.content.weather.Temperature;
import com.telenav.ws.datatypes.content.weather.WeatherReport;

public class HtmlAdjugglerUtilTest extends HtmlAdjugglerUtil {

	HtmlAdjugglerUtil htmlAdjugglerUtil = new HtmlAdjugglerUtil();
	private DataSource dataSource = new DataSource();
	
	@Before
	public void setUp() throws Exception {
		
		String str = String.valueOf("http://");
		
		JSONObject actionJson = new JSONObject();
		actionJson.put("type", Constant.KEY_ADJUGGLER_BROWSER);
		actionJson.put("url", "http://");
		
		JSONObject paramJSON = new JSONObject();
		paramJSON.put(Constant.JSON_KEY_NEEDPURCHASE, "1");
		paramJSON.put("loginLocator", "");
		paramJSON.put("movieLocator", "");
		paramJSON.put("ssoToken", "");
		paramJSON.put("appCode", "");
		paramJSON.put("clientInfoStr", "");
		paramJSON.put("width", "480-800");
		paramJSON.put("height", "800-480");
		paramJSON.put("action", actionJson);
		
		
		WeatherReport weatherReport = new WeatherReport();
		weatherReport.setLocation("");
		weatherReport.setCurrentTemperature(new Temperature());
		weatherReport.setLowTemperature(new Temperature());
		weatherReport.setHighTemperature(new Temperature());
		
		HtmlClientInfo htmlClientInfo = new HtmlClientInfo();
		htmlClientInfo.setProgramCode("TN");
		htmlClientInfo.setUserId("");
		htmlClientInfo.setCarrier("T-Mobile");
		htmlClientInfo.setDevice("");
		htmlClientInfo.setVersion("6.2.01");
		htmlClientInfo.setProduct("TMO_lite");
		htmlClientInfo.setPlatform("Android");
		htmlClientInfo.setBuildNo("");
		
		dataSource.addData(JSONObject.class.getName(), paramJSON);
		dataSource.addData(String.class.getName(), str);
		dataSource.addData(HtmlClientInfo.class.getName(), htmlClientInfo);
		dataSource.addData(WeatherReport.class.getName(), weatherReport);
		
	}

	@Test
	public void testSuite(){
		String[] filter = {"getWeatherString"};
		GenericTest.getInstance().startStaticTest(htmlAdjugglerUtil, dataSource, filter);
	}

}
