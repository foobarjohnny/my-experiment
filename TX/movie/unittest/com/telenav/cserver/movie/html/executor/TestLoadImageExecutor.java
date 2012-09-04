/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.movie.html.executor;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorException;

/**
 * TestLoadImageExecutor.java
 * @TODO
 * @author  xljiang@telenav.cn
 * @version 1.0 2011-9-27
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({MovieServiceProxy.class})
@SuppressStaticInitializationFor({"com.telenav.cserver.movie.html.executor.MovieServiceProxy"})
public class TestLoadImageExecutor extends TestCase{
	
	private LoadImageRequest executorRequest = new LoadImageRequest();
	private LoadImageResponse executorResponse = new LoadImageResponse();
	private LoadImageExecutor testedExecutor = new LoadImageExecutor();
	
	
	
	private ExecutorContext executorContext = new ExecutorContext();
	
	@Override
	protected void setUp() throws Exception {
		executorRequest.setMovieIds(new String[]{"0","1","2","3","4"});
	}
	public void testDoExecute() throws ExecutorException{
		//prepare and replay
		String[] images = new String[]{"000","001","002","003",null};
		PowerMock.mockStatic(MovieServiceProxy.class);
		EasyMock.expect(MovieServiceProxy.loadImages(executorRequest.getMovieIds(), executorRequest.getHeight(),executorRequest.getWidth())).andReturn(images);
		PowerMock.replayAll();
		
		//invoke and verify
		testedExecutor.doExecute(executorRequest, executorResponse, executorContext);
		PowerMock.verifyAll();
		
		//assert
		assertEquals(5,executorResponse.getImages().size());
		assertEquals("002",executorResponse.getImages().get("2"));
	}

}
