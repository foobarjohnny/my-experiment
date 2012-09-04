package com.telenav.browser.movie.executor;

import static org.junit.Assert.*;
import org.json.me.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.telenav.browser.movie.Constant;
import com.telenav.browser.movie.datatypes.Address;

public class TestMovieSearchRequest {
	
	private MovieSearchRequest request;

	@Before
	public void setUp() throws Exception {
		request = new MovieSearchRequest();
		
		JSONObject addressJson = new JSONObject("{\"zip\":\"\",\"lon\":-12199983," +
				"\"state\":\"\",\"firstLine\":\"\",\"label\":\"\",\"type\":6,\"lat\":3737453,\"country\":\"\",\"city\":\"\"}");
		Address address = new Address();
		address.makeFrom(addressJson);
		
		request.setBatchNumber(1);
		request.setBatchSize(20);
		request.setDistanceUnit(Constant.DUNIT_MILES);
		request.setSearchString("Forest Gunmp");
		request.setSearchDate("2012-02-29");
		request.setSortByName(true);
		request.setNewSortBy(Constant.SORT_BY_NAME);
		request.setAddress(address);
		
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		assertEquals(request.getBatchNumber(),1);
		assertEquals(request.getBatchSize(),20);
		assertEquals(request.getDistanceUnit(),Constant.DUNIT_MILES);
		assertEquals(request.getSearchString(),"Forest Gunmp");
		assertEquals(request.getSearchDate(),"2012-02-29");
		assertTrue(request.isSortByName());
		assertEquals(request.getNewSortBy(),Constant.SORT_BY_NAME);
		assertEquals(request.getAddress().lat,3737453);
		assertEquals(request.getAddress().lon,-12199983);
	}
}
