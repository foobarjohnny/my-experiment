/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.movie.html.executor;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.movie.html.datatypes.MovieItem;
import com.telenav.cserver.movie.html.util.HtmlConstants;

/**
 * TestBookTicketExecutor.java
 * @TODO
 * @author  xljiang@telenav.cn
 * @version 1.0 2011-9-27
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({MovieServiceProxy.class,TheaterListExecutor.class})
@SuppressStaticInitializationFor({"com.telenav.cserver.movie.html.executor.MovieServiceProxy"})

public class TestTheaterListExecutor extends TestCase{
	private MovieCommonRequest executorRequest = new MovieCommonRequest();
	private TheaterListResponse executorResponse = new TheaterListResponse();
	private TheaterListExecutor testedExecutor = new TheaterListExecutor();
	
	
	
	private ExecutorContext executorContext = new ExecutorContext();
	
	//request.getPageType() == HtmlConstants.PAGE_TYPE_SIMPLE 
	public void testDoExecute() throws ExecutorException{
		//prepare and replay
		executorRequest.setPageType(HtmlConstants.PAGE_TYPE_SIMPLE);
		PowerMock.mockStatic(MovieServiceProxy.class);
		MovieServiceProxy.lookupTheatresWithDetailInfo(executorRequest, executorResponse);
		PowerMock.replayAll();
		
		//invoke and verify
		testedExecutor.doExecute(executorRequest, executorResponse, executorContext);
		PowerMock.verifyAll();
		
		//assert
		assertEquals(ExecutorResponse.STATUS_OK,executorResponse.getStatus());
	}
	//request.getPageType() == HtmlConstants.PAGE_TYPE_AJAXSIMPE
	public void testDoExecute1() throws ExecutorException{
		//prepare and replay
		executorRequest.setPageType(HtmlConstants.PAGE_TYPE_AJAXSIMPE);
		PowerMock.mockStatic(MovieServiceProxy.class);
		MovieServiceProxy.lookupTheatresWithDetailInfo(executorRequest, executorResponse);
		PowerMock.replayAll();
		
		//invoke and verify
		testedExecutor.doExecute(executorRequest, executorResponse, executorContext);
		PowerMock.verifyAll();
		
		//assert
		assertEquals(ExecutorResponse.STATUS_OK,executorResponse.getStatus());
	}
	//request.getPageType() == HtmlConstants.PAGE_TYPE_SUB
	public void testDoExecute2() throws Exception{
		//prepare and replay
		executorRequest.setPageType(HtmlConstants.PAGE_TYPE_SUB);
		MovieListResponse movieListResponse = PowerMock.createMock(MovieListResponse.class);
		List<MovieItem> movieList = new ArrayList<MovieItem>();
		movieList.add(new MovieItem());
		movieList.add(new MovieItem());
		movieList.add(new MovieItem());
		
		
		PowerMock.expectNew(MovieListResponse.class).andReturn(movieListResponse);
		PowerMock.mockStatic(MovieServiceProxy.class);
		MovieServiceProxy.lookupSubMovieList(executorRequest, movieListResponse);
		EasyMock.expect(movieListResponse.getMoiveList()).andReturn(movieList);
		PowerMock.replayAll();
		
		//invoke and verify
		testedExecutor.doExecute(executorRequest, executorResponse, executorContext);
		PowerMock.verifyAll();
		
		//assert
		assertEquals(ExecutorResponse.STATUS_OK,executorResponse.getStatus());
		assertEquals(3,executorResponse.getMoiveList().size());
	}
	//request.getPageType() == ""
	public void testDoExecute3() throws Exception{
		//prepare and replay
		executorRequest.setPageType("");
		PowerMock.mockStatic(MovieServiceProxy.class);
		MovieServiceProxy.searchTheatresWithMovieAndSchedule(executorRequest, executorResponse);
		PowerMock.replayAll();
		
		//invoke and verify
		testedExecutor.doExecute(executorRequest, executorResponse, executorContext);
		PowerMock.verifyAll();
		
		//assert
		assertEquals(ExecutorResponse.STATUS_OK,executorResponse.getStatus());
	}

}
