package com.telenav.cserver.movie.html.protocol;

import junit.framework.Assert;

import org.apache.struts.mock.MockHttpServletRequest;
import org.json.me.JSONObject;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.movie.html.executor.LoadImageRequest;
import com.telenav.cserver.movie.html.util.HtmlConstants.RRKey;

public class TestLoadImageRequestParser {

	LoadImageRequestParser requestParser;
	MockHttpServletRequest httpRequest;
	LoadImageRequest executorRequest;
	
	@Before
	public void setUp() throws Exception {
		JSONObject jo = new JSONObject();
		jo.put(RRKey.LM_MOVIE_ID, "000001,000002,000004");
		
		httpRequest = new MockHttpServletRequest();
		httpRequest.addParameter("jsonStr", jo.toString());
		
		requestParser = new LoadImageRequestParser();
	}

	@Test
	public void testParseBrowserRequest() {
		try {
			executorRequest = (LoadImageRequest) requestParser.parseBrowserRequest(httpRequest);
			Assert.assertNotNull(executorRequest.getMovieIds());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
