package com.telenav.cserver.html.protocol;

import com.telenav.cserver.TestResponseFormatter;
import com.telenav.cserver.html.executor.HtmlAboutResponse;

public class TestHtmlAboutResponseFormatter extends TestResponseFormatter{
	public void testParseBrowserResponse() {
		HtmlAboutResponse rResponse = new HtmlAboutResponse();
		rResponse.setDataSet("");
		HtmlAboutResponseFormatter formatter = new HtmlAboutResponseFormatter();
		try {
			formatter.parseBrowserResponse(httpRequest, rResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
