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
import org.powermock.reflect.Whitebox;

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

public class TestGetTicketQuantityExecutor extends TestCase{
	private GetTicketQuantityRequest executorRequest = new GetTicketQuantityRequest();
	private GetTicketQuantityResponse executorResponse = new GetTicketQuantityResponse();
	private GetTicketQuantityExecutor testedExecutor = new GetTicketQuantityExecutor();
	
	
	
	private ExecutorContext executorContext = new ExecutorContext();
	
	public void testDoExecute() throws ExecutorException{
		//prepare and replay
		PowerMock.mockStatic(MovieServiceProxy.class);
		MovieServiceProxy.getTicketPrices(executorRequest, executorResponse);
		PowerMock.replayAll();
		
		//invoke and verify
		testedExecutor.doExecute(executorRequest, executorResponse, executorContext);
		PowerMock.verifyAll();
		
		//assert
		assertEquals(ExecutorResponse.STATUS_OK,executorResponse.getStatus());
	}
	
	public void testDebug() throws ExecutorException{
		Whitebox.setInternalState(GetTicketQuantityExecutor.class, "isDebug", true);
		testedExecutor.doExecute(executorRequest, executorResponse, executorContext);
		
		//assert
		assertEquals(ExecutorResponse.STATUS_OK,executorResponse.getStatus());
		assertNotNull(executorResponse.getTicketList());
		assertEquals(3,executorResponse.getTicketList().size());
		assertEquals("Adult",executorResponse.getTicketList().get(0).getType());
		assertEquals("222",executorResponse.getTicketList().get(1).getTicketId());
		assertEquals(9.0,executorResponse.getTicketList().get(2).getPrice());
	}

}
