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
import com.telenav.cserver.movie.html.datatypes.TheaterItem;
import com.telenav.cserver.movie.html.util.HtmlConstants;
import com.telenav.j2me.datatypes.Stop;

/**
 * TestBookTicketExecutor.java
 * @TODO
 * @author  xljiang@telenav.cn
 * @version 1.0 2011-9-27
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({MovieServiceProxy.class,MovieListExecutor.class})
@SuppressStaticInitializationFor({"com.telenav.cserver.movie.html.executor.MovieServiceProxy"})

public class TestMovieListExecutor extends TestCase{
	private MovieCommonRequest executorRequest = new MovieCommonRequest();
	private MovieListResponse executorResponse = new MovieListResponse();
	private MovieListExecutor testedExecutor = new MovieListExecutor();
	
	
	
	private ExecutorContext executorContext = new ExecutorContext();
	
	//request.getPageType() == HtmlConstants.PAGE_TYPE_SIMPLE 
	public void testDoExecute() throws ExecutorException{
		//prepare and replay
		executorRequest.setPageType(HtmlConstants.PAGE_TYPE_SIMPLE);
		PowerMock.mockStatic(MovieServiceProxy.class);
		MovieServiceProxy.searchMovies(executorRequest, executorResponse);
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
		MovieServiceProxy.searchMovies(executorRequest, executorResponse);
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
		TheaterListResponse theaterListResponse = PowerMock.createMock(TheaterListResponse.class);
		List<TheaterItem> theaterList = new ArrayList<TheaterItem>();
		theaterList.add(new TheaterItem());
		theaterList.add(new TheaterItem());
		theaterList.add(new TheaterItem());
		
		
		PowerMock.expectNew(TheaterListResponse.class).andReturn(theaterListResponse);
		PowerMock.mockStatic(MovieServiceProxy.class);
		MovieServiceProxy.lookupSubTheatreList(executorRequest, theaterListResponse);
		EasyMock.expect(theaterListResponse.getTheaterList()).andReturn(theaterList);
		PowerMock.replayAll();
		
		//invoke and verify
		testedExecutor.doExecute(executorRequest, executorResponse, executorContext);
		PowerMock.verifyAll();
		
		//assert
		assertEquals(ExecutorResponse.STATUS_OK,executorResponse.getStatus());
		assertEquals(3,executorResponse.getTheaterList().size());
	}
	//request.getPageType() == ""
	public void testDoExecute3() throws Exception{
		//prepare and replay
		executorRequest.setPageType("");
		PowerMock.mockStatic(MovieServiceProxy.class);
		MovieServiceProxy.searchMoviesWithTheaterAndSchedule(executorRequest, executorResponse);
		PowerMock.replayAll();
		
		//invoke and verify
		testedExecutor.doExecute(executorRequest, executorResponse, executorContext);
		PowerMock.verifyAll();
		
		//assert
		assertEquals(ExecutorResponse.STATUS_OK,executorResponse.getStatus());
	}
	public void testSimple(){
		MovieListRequest movieListRequest = new MovieListRequest();
		movieListRequest.setAddress(new Stop());
		movieListRequest.toString();
	}

}
