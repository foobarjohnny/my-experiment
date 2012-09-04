package com.telenav.cserver.movie.html.protocol;

import java.util.ArrayList;

import junit.framework.Assert;

import org.apache.struts.mock.MockHttpServletRequest;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.movie.html.datatypes.MovieItem;
import com.telenav.cserver.movie.html.datatypes.ScheduleItem;
import com.telenav.cserver.movie.html.datatypes.TheaterItem;
import com.telenav.cserver.movie.html.executor.MovieListResponse;
import com.telenav.j2me.datatypes.Stop;

public class TestLookUpMovieResponseFormatter {

	private LookUpMovieResponseFormatter responseFormattor;
	private MockHttpServletRequest httpRequest;
	private MovieListResponse executorResponse;
	
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
		
		ScheduleItem scheduleItem = new ScheduleItem();
		scheduleItem.setId("12345");
		scheduleItem.setMovieId("12345");
		scheduleItem.setTheaterId(12345);
		scheduleItem.setVendorName("VendorName");
		scheduleItem.setTicketURI("TicketURI");
		
		MovieItem movieItem = new MovieItem();
		movieItem.setCast(new String[] {"cast1,cast2"});
		movieItem.setDescription("this is description");
		movieItem.setTheaterList(theaterList);
		movieItem.setScheduleItem(scheduleItem);
		
		ArrayList<MovieItem> movieList = new ArrayList<MovieItem>();
		movieList.add(movieItem);			
		
		executorResponse = new MovieListResponse();
		executorResponse.setMoiveList(movieList);
		executorResponse.setTheaterList(theaterList);
		
		responseFormattor = new LookUpMovieResponseFormatter();
		
		httpRequest = new MockHttpServletRequest();
	}

	@Test
	public void testParseBrowserResponse() {
		try {
			responseFormattor.parseBrowserResponse(httpRequest, executorResponse);
			Assert.assertNotNull(httpRequest.getAttribute("ajaxResponse"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
