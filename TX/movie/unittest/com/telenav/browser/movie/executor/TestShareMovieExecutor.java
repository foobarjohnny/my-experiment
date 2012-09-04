package com.telenav.browser.movie.executor;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.j2me.datatypes.Stop;

public class TestShareMovieExecutor {

	private ShareMovieExecutor shareMovieExecutor;
	private ShareMovieRequest request;
	private ShareMovieResponse response;
	private ExecutorContext executorContext;
	
	@Before
	public void setUp() throws Exception {
		shareMovieExecutor = new ShareMovieExecutor();
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
		
		response  = new ShareMovieResponse();
		executorContext = new ExecutorContext();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testExecute() {
		try {
			shareMovieExecutor.execute(request, response, executorContext);
			assertEquals(response.getStatus(), ExecutorResponse.STATUS_OK);
		} catch (ExecutorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
