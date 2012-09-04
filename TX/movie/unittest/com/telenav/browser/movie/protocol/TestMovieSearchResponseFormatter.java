package com.telenav.browser.movie.protocol;

import static org.junit.Assert.*;
import org.apache.struts.mock.MockHttpServletRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.telenav.browser.movie.executor.MovieSearchResponse;
import com.telenav.datatypes.content.movie.v10.Movie;
import com.telenav.datatypes.content.movie.v10.MovieListingWithDetailTheaterInfo;
import com.telenav.j2me.datatypes.TxNode;

public class TestMovieSearchResponseFormatter {
	MovieSearchResponseFormatter responseFormatter;
	MockHttpServletRequest httpRequest;
	MovieSearchResponse executorResponse;
	
	@Before
	public void setUp() throws Exception {
		responseFormatter = new MovieSearchResponseFormatter();
		httpRequest = new MockHttpServletRequest();
		executorResponse = new MovieSearchResponse();
		
		Movie movie = new Movie();
		movie.setName("A Separation");
		movie.setCast(new String[] {"Peyman Moaadi","Leila Hatami","Sareh Bayat"});
		movie.setDescription("After his wife (Leila Hatami) leaves him, an Iranian bank employee (Peyman Moaadi) hires a woman (Sareh Bayat) to take care of his ailing, elderly father.");
		movie.setDirector("Asghar Farhadi");
		movie.setGenres("Drama");
		movie.setGrade("PG-13");
		movie.setRating("3.5");
		movie.setId("173498");
		movie.setRuntime("02H03M");
		movie.setVendorId("MV003621210000");
		
		MovieListingWithDetailTheaterInfo movieWithDetailTheaterInfo = new MovieListingWithDetailTheaterInfo();
		movieWithDetailTheaterInfo.setMovie(movie);
		executorResponse.movies = new MovieListingWithDetailTheaterInfo[] {movieWithDetailTheaterInfo};
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testParseBrowserResponse() {
		try {
			responseFormatter.parseBrowserResponse(httpRequest, executorResponse);
			TxNode node = (TxNode) httpRequest.getAttribute("node");
			assertTrue(node.msgAt(0).contains("A Separation"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
