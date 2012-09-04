package com.telenav.cserver.html.util;

import org.json.me.JSONObject;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.framework.html.datatype.HtmlClientInfo;
import com.telenav.cserver.util.helper.generictest.DataSource;
import com.telenav.cserver.util.helper.generictest.GenericTest;

public class HtmlAdjugglerDefaultJSONTest {
	
	HtmlAdjugglerDefaultJSON htmlAdjugglerDefaultJSON = new HtmlAdjugglerDefaultJSON();
	private DataSource dataSource = new DataSource();
	
	@Before
	public void setUp() throws Exception {
		
		JSONObject paramJSON = HtmlAdjugglerDefaultJSON.getDefaultJSONForPrem();
		paramJSON.put("loginLocator", "");
		paramJSON.put("ssoToken", "");
		paramJSON.put("appCode", "");
		paramJSON.put("clientInfoStr", "");
		paramJSON.put("width", "480-800");
		paramJSON.put("height", "800-480");
		
		HtmlClientInfo htmlClientInfo = new HtmlClientInfo();
		
		dataSource.addData(JSONObject.class.getName(), paramJSON);
		dataSource.addData(String.class.getName(), String.valueOf("1"));
		dataSource.addData(HtmlClientInfo.class.getName(), htmlClientInfo);
	}

	@Test
	public void testSuite(){
		GenericTest.getInstance().startStaticTest(htmlAdjugglerDefaultJSON, dataSource);
	}
}
