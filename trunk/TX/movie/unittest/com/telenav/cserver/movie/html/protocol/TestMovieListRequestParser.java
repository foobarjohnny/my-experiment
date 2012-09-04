package com.telenav.cserver.movie.html.protocol;

import org.apache.struts.mock.MockHttpServletRequest;
import org.json.me.JSONObject;
import org.junit.Before;
import org.junit.Test;
import junit.framework.Assert;

import com.telenav.cserver.movie.html.executor.MovieListRequest;
import com.telenav.cserver.movie.html.util.HtmlConstants;
import com.telenav.cserver.movie.html.util.HtmlConstants.RRKey;

public class TestMovieListRequestParser {
	MovieListRequestParser requestParser;
	MockHttpServletRequest httpRequest;
	MovieListRequest executorRequest;

	@Before
	public void setUp() throws Exception {
		requestParser = new MovieListRequestParser();
		httpRequest = new MockHttpServletRequest();
		
		JSONObject jo = new JSONObject();
		jo.put(RRKey.MS_DATE_INDEX, "");
		jo.put(RRKey.MS_ADDRESS, "{\"country\":\"\",\"state\":\"\",\"city\":\"\",\"firstLine\":\"\",\"label\":\"\",\"lon\":-7961564,\"lat\":4364275,\"type\":6,\"zip\":\"\"}");
		jo.put(RRKey.MS_DISTANCE_UNIT, 0);
		jo.put(RRKey.MS_BATCH_NUMBER, 1);
		jo.put(RRKey.MS_START_INDEX, 0);
		jo.put(RRKey.MS_BATCH_SIZE, 20);
		jo.put(RRKey.MS_SORT_BY_NAME,true);
		
		httpRequest.addParameter("jsonStr", jo.toString());
		httpRequest.addParameter("pageType", HtmlConstants.PAGE_TYPE_SIMPLE);
	}

	@Test
	public void testParseBrowserRequest() {
		try {
			executorRequest = (MovieListRequest) requestParser.parseBrowserRequest(httpRequest);
			Assert.assertNotNull(executorRequest.getAddress());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
