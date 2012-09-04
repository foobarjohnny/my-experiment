package com.telenav.cserver.html.protocol;

import com.telenav.cserver.TestResponseFormatter;
import com.telenav.cserver.html.executor.HtmlGetLogImageResponse;

public class TestHtmlGetLogImageFormatter extends TestResponseFormatter{
	public void testParseBrowserResponse() {
		HtmlGetLogImageResponse rResponse = new HtmlGetLogImageResponse();
		rResponse.setImage("");
		rResponse.setImageName("");
		HtmlGetLogImageFormatter formatter = new HtmlGetLogImageFormatter();
		try {
			formatter.parseBrowserResponse(httpRequest, rResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
