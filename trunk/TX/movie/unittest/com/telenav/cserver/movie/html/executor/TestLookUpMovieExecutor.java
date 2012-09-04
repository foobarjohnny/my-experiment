/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.movie.html.executor;

import junit.framework.TestCase;

import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorResponse;

/**
 * TestBookTicketExecutor.java
 * @TODO
 * @author  xljiang@telenav.cn
 * @version 1.0 2011-9-27
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({MovieServiceProxy.class})
@SuppressStaticInitializationFor({"com.telenav.cserver.movie.html.executor.MovieServiceProxy"})

public class TestLookUpMovieExecutor extends TestCase{
	private MovieCommonRequest executorRequest = new MovieCommonRequest();
	private MovieListResponse executorResponse = new MovieListResponse();
	private LookUpMovieExecutor testedExecutor = new LookUpMovieExecutor();
	
	
	
	private ExecutorContext executorContext = new ExecutorContext();
	
	public void testDoExecute() throws ExecutorException{
		//prepare and replay
		PowerMock.mockStatic(MovieServiceProxy.class);
		MovieServiceProxy.lookupSubMovieList(executorRequest, executorResponse);
		PowerMock.replayAll();
		
		//invoke and verify
		testedExecutor.doExecute(executorRequest, executorResponse, executorContext);
		PowerMock.verifyAll();
		
		//assert
		assertEquals(ExecutorResponse.STATUS_OK,executorResponse.getStatus());
	}

}
