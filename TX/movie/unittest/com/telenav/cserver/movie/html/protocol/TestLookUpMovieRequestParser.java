package com.telenav.cserver.movie.html.protocol;

import junit.framework.Assert;

import org.apache.struts.mock.MockHttpServletRequest;
import org.json.me.JSONObject;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.movie.html.executor.MovieCommonRequest;
import com.telenav.cserver.movie.html.util.HtmlConstants.RRKey;

public class TestLookUpMovieRequestParser {
	
	LookUpMovieRequestParser requestParser;
	MockHttpServletRequest httpRequest;
	MovieCommonRequest executorRequest;

	@Before
	public void setUp() throws Exception {
		JSONObject jo = new JSONObject();
		jo.put(RRKey.MS_DATE_INDEX, "2012-12-12");
		jo.put(RRKey.MS_THEATER_ID, "123456");
		
		httpRequest = new MockHttpServletRequest();
		httpRequest.addParameter("jsonStr", jo.toString());
		
		requestParser = new LookUpMovieRequestParser();
	}

	@Test
	public void test() {
		try {
			executorRequest = (MovieCommonRequest) requestParser.parseBrowserRequest(httpRequest);
			Assert.assertNotNull(executorRequest.getTheaterId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
