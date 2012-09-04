package com.telenav.browser.movie.executor;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.datatypes.content.movie.v10.MovieListingWithDetailTheaterInfo;

public class TestMovieSearchResponse {
	
	private MovieSearchResponse movieSearchResponse;
	
	private int moviesSize = 20;
	private double lat=3737453;
	private double lon=-12199983;
	private int dUnit = 1;

	@Before
	public void setUp() throws Exception {
		movieSearchResponse = new MovieSearchResponse();
		movieSearchResponse.movies = new MovieListingWithDetailTheaterInfo[moviesSize];
		movieSearchResponse.lat = lat;
		movieSearchResponse.lon = lon;
		movieSearchResponse.dUnit = dUnit;
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testToString() {
		String actual = "[moviesSize] = " + moviesSize + "; [lat] = " + lat
				+ "; [lon] = " + lon + "; [dUnit] = " + dUnit;
		assertEquals(movieSearchResponse.toString(), actual);
	}

}
