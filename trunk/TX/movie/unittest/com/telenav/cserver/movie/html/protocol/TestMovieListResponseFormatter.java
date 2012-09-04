package com.telenav.cserver.movie.html.protocol;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.apache.struts.mock.MockHttpServletRequest;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.movie.html.datatypes.MovieItem;
import com.telenav.cserver.movie.html.datatypes.TheaterItem;
import com.telenav.cserver.movie.html.executor.MovieListResponse;
import com.telenav.cserver.movie.html.util.HtmlConstants;
import com.telenav.j2me.datatypes.Stop;

public class TestMovieListResponseFormatter {

	MovieListResponseFormatter responseFormattor;
	MockHttpServletRequest httpRequest;
	MovieListResponse movieListResponse;
	
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
		
		responseFormattor = new MovieListResponseFormatter();
		httpRequest = new MockHttpServletRequest();
		movieListResponse = new MovieListResponse();
		movieListResponse.setMoiveList(movieList);
		movieListResponse.setTheaterList(theaterList);
		movieListResponse.setAddress(stop);
	}

	@Test
	public void testParseBrowserResponseSimpleList() {
		movieListResponse.setPageType(HtmlConstants.PAGE_TYPE_SIMPLE);
		try {
			responseFormattor.parseBrowserResponse(httpRequest, movieListResponse);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testParseBrowserResponseSubList() {
		movieListResponse.setPageType(HtmlConstants.PAGE_TYPE_SUB);
		try {
			responseFormattor.parseBrowserResponse(httpRequest, movieListResponse);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testParseBrowserResponseAjaxSimpleList() {
		movieListResponse.setPageType(HtmlConstants.PAGE_TYPE_AJAXSIMPE);
		movieListResponse.setStartIndex(0);
		try {
			responseFormattor.parseBrowserResponse(httpRequest, movieListResponse);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testParseBrowserResponseFullList() {
		movieListResponse.setPageType(HtmlConstants.PAGE_TYPE_FULL);
		try {
			responseFormattor.parseBrowserResponse(httpRequest, movieListResponse);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
