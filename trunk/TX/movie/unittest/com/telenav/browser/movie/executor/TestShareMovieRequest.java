package com.telenav.browser.movie.executor;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.j2me.datatypes.Stop;

public class TestShareMovieRequest {
	
	private ShareMovieRequest request;

	@Before
	public void setUp() throws Exception {
		request = new ShareMovieRequest();
		
		Stop stop = new Stop();
		stop.firstLine = "3111 Mission College Blvd";
		stop.country = "US";
		stop.zip = "95054";
		stop.lat = 3738810;
		stop.lon = -12198360;
		
		request.setAddress(stop);
		request.setPtn("5555215554");
		request.setMovieName("Act of Valor");
		request.setTheaterName("AMC Mercado 20");
		request.setRecipients(new String[] {"4848484848,5555218135"});
		request.setUserId(9966058);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		assertEquals(request.getPtn(), "5555215554");
		assertEquals(request.getMovieName(),"Act of Valor");
		assertEquals(request.getTheaterName(),"AMC Mercado 20");
		assertEquals(request.getUserId(),9966058);
	}

}
