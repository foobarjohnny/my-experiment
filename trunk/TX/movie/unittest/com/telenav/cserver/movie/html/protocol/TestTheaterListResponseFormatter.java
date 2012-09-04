package com.telenav.cserver.movie.html.protocol;

import java.util.ArrayList;

import org.apache.struts.mock.MockHttpServletRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.Assert;

import com.telenav.cserver.movie.html.datatypes.MovieItem;
import com.telenav.cserver.movie.html.datatypes.TheaterItem;
import com.telenav.cserver.movie.html.executor.TheaterListResponse;
import com.telenav.cserver.movie.html.util.HtmlConstants;
import com.telenav.j2me.datatypes.Stop;

public class TestTheaterListResponseFormatter {
	
	TheaterListResponseFormatter responseFormattor;
	MockHttpServletRequest httpRequest;
	TheaterListResponse executorResponse;

	@Before
	public void setUp() throws Exception {
		Stop stop = new Stop();
		stop.lat = 12345;
		stop.lon = 12345;
		TheaterItem theaterItem = new TheaterItem();
		theaterItem.setDistance("11 mile");
		theaterItem.setDistanceUnit("");
		theaterItem.setId(11223344);
		theaterItem.setPhoneNo("1234567890");
		theaterItem.setName("test theater");
		theaterItem.setAddress(stop);
		ArrayList<TheaterItem> theaterList = new ArrayList<TheaterItem>();
		theaterList.add(theaterItem);
		
		MovieItem movieItem = new MovieItem();
		movieItem.setCast(new String[] {"cast1,cast2"});
		movieItem.setDescription("this is description");
		movieItem.setTheaterList(theaterList);
		ArrayList<MovieItem> movieList = new ArrayList<MovieItem>();
		movieList.add(movieItem);
		
		executorResponse = new TheaterListResponse();
		executorResponse.setDistanceUnit(0);
		executorResponse.setAddress(stop);
		executorResponse.setMoiveList(movieList);
		executorResponse.setTheaterList(theaterList);
		executorResponse.setPageType(HtmlConstants.PAGE_TYPE_SIMPLE);
		
		responseFormattor = new TheaterListResponseFormatter();
		
		httpRequest = new MockHttpServletRequest();
		
	}

	@Test
	public void testParseBrowserResponse() {
		try {
			responseFormattor.parseBrowserResponse(httpRequest, executorResponse);
			Assert.notNull(httpRequest.getAttribute("theaterList"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
