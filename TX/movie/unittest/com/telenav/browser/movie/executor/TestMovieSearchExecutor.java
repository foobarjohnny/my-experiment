package com.telenav.browser.movie.executor;

import static org.junit.Assert.*;

import org.json.me.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.browser.movie.Constant;
import com.telenav.browser.movie.datatypes.Address;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorResponse;

public class TestMovieSearchExecutor {
	
	private MovieSearchExecutor movieSearchExecutor;
	private MovieSearchRequest request;
	private MovieSearchResponse response;
	private ExecutorContext executorContext;
	
	@Before
	public void setUp() throws Exception {
		movieSearchExecutor = new MovieSearchExecutor();
		
		executorContext = new ExecutorContext();
		request = new MovieSearchRequest();
		JSONObject addressJson = new JSONObject("{\"zip\":\"\",\"lon\":-12199983," +
				"\"state\":\"\",\"firstLine\":\"\",\"label\":\"\",\"type\":6,\"lat\":3737453,\"country\":\"\",\"city\":\"\"}");
		Address address = new Address();
		address.makeFrom(addressJson);
		request.setBatchNumber(1);
		request.setBatchSize(20);
		request.setDistanceUnit(Constant.DUNIT_MILES);
		request.setSearchString("");
		request.setSortByName(true);
		request.setNewSortBy(Constant.SORT_BY_NAME);
		request.setAddress(address);
		response = new MovieSearchResponse();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testExecute() {
		try {
			movieSearchExecutor.doExecute(request, response, executorContext);
			assertEquals(response.getStatus(), ExecutorResponse.STATUS_OK);
		} catch (ExecutorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
