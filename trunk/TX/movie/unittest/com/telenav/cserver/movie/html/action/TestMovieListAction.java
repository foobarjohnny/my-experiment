/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.movie.html.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.api.support.membermodification.MemberModifier;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorDispatcher;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.protocol.ProtocolRequestParser;
import com.telenav.cserver.framework.executor.protocol.ProtocolResponseFormatter;
import com.telenav.cserver.movie.html.executor.MovieListResponse;
import com.telenav.cserver.movie.html.util.HtmlConstants;

/**
 * TestMovieListAction.java
 * @TODO
 * @author  xljiang@telenav.cn
 * @version 1.0 2011-9-27
 */
@RunWith(PowerMockRunner.class)
//<!------- modify me 1---------
@PrepareForTest({ExecutorDispatcher.class,MovieListAction.class,ActionMapping.class})
@SuppressStaticInitializationFor({"com.telenav.cserver.movie.html.action.MovieListAction","com.telenav.cserver.framework.executor.ExecutorDispatcher"})
//<!-------
public class TestMovieListAction extends TestCase{
	
	private HttpServletRequest httpRequest = PowerMock.createMock(HttpServletRequest.class);
	private ActionMapping mapping = null;
	private HttpServletResponse httpResponse = PowerMock.createMock(HttpServletResponse.class);
	private ExecutorRequest[] executorRequests = new ExecutorRequest[1];
	private ExecutorRequest executorRequest = PowerMock.createMock(ExecutorRequest.class);
	private ExecutorContext executorContext = new ExecutorContext();
	private ProtocolRequestParser requestParser = PowerMock.createMock(ProtocolRequestParser.class);
	private ProtocolResponseFormatter responseFormatter = PowerMock.createMock(ProtocolResponseFormatter.class);
	private ExecutorDispatcher executorDispatcher = PowerMock.createMock(ExecutorDispatcher.class);
	private ActionForward actionForward = PowerMock.createMock(ActionForward.class);
	
	//---- modify me 2----------
	private Class<MovieListAction> actionClass = MovieListAction.class;
	private MovieListAction testedAction = Whitebox.newInstance(actionClass);
	private MovieListAction testedAction4Ex = PowerMock.createPartialMock(actionClass,"addErrors");
	private Logger logger = Logger.getLogger(actionClass);
	
	//---- modify me 3, over----------
	private Class<MovieListResponse> responseClass = MovieListResponse.class;
	private MovieListResponse executorResponse = PowerMock.createMock(responseClass);
	//----
	
	
	@Override
	protected void setUp() throws Exception {
		MemberModifier.suppress(ActionMapping.class.getConstructors());
		mapping = PowerMock.createMock(ActionMapping.class);
		
		Whitebox.setInternalState(testedAction, "requestParser", requestParser);
		Whitebox.setInternalState(testedAction, "responseFormatter", responseFormatter);
		Whitebox.setInternalState(actionClass, "logger", logger);
	}
	public void testSimple() throws Exception{
		Object o = actionClass.newInstance();
	}
	
	
	//executorResponse.getPageType() == HtmlConstants.PAGE_TYPE_SIMPLE
	public void testDoActionSuc() throws Exception {
		//prepare and replay
		executorRequests[0] = executorRequest;
		EasyMock.expect(requestParser.parse(httpRequest)).andReturn(executorRequests);
		
		PowerMock.mockStatic(ExecutorDispatcher.class);
		EasyMock.expect(ExecutorDispatcher.getInstance()).andReturn(executorDispatcher);
		PowerMock.expectNew(responseClass).andReturn(executorResponse);
		PowerMock.expectNew(ExecutorContext.class).andReturn(executorContext);
		
		executorDispatcher.execute(executorRequest, executorResponse, executorContext);
		EasyMock.expect(executorResponse.getPageType()).andReturn(HtmlConstants.PAGE_TYPE_SIMPLE);
		
		responseFormatter.format(EasyMock.anyObject(HttpServletRequest.class), EasyMock.anyObject(ExecutorResponse[].class));
		EasyMock.expect(mapping.findForward("simpleList")).andReturn(actionForward);
		PowerMock.replayAll();

		//invoke and verify
		ActionForward result;
		result = testedAction.doAction(mapping, httpRequest, httpResponse);
		PowerMock.verifyAll();
		//assert
		assertEquals("The two Object should be same.",result, actionForward);
	}
	//executorResponse.getPageType() == ""
	public void testDoActionSuc1() throws Exception {
		//prepare and replay
		executorRequests[0] = executorRequest;
		EasyMock.expect(requestParser.parse(httpRequest)).andReturn(executorRequests);
		
		PowerMock.mockStatic(ExecutorDispatcher.class);
		EasyMock.expect(ExecutorDispatcher.getInstance()).andReturn(executorDispatcher);
		PowerMock.expectNew(responseClass).andReturn(executorResponse);
		PowerMock.expectNew(ExecutorContext.class).andReturn(executorContext);
		
		executorDispatcher.execute(executorRequest, executorResponse, executorContext);
		EasyMock.expect(executorResponse.getPageType()).andReturn("").anyTimes();
		
		responseFormatter.format(EasyMock.anyObject(HttpServletRequest.class), EasyMock.anyObject(ExecutorResponse[].class));
		EasyMock.expect(mapping.findForward("success")).andReturn(actionForward);
		PowerMock.replayAll();

		//invoke and verify
		ActionForward result;
		result = testedAction.doAction(mapping, httpRequest, httpResponse);
		PowerMock.verifyAll();
		//assert
		assertEquals("The two Object should be same.",result, actionForward);
	}
	
	
	//executorRequests == null
	// and executorResponse.getPageType() == HtmlConstants.PAGE_TYPE_SUB
	public void testDoActionErr1() throws Exception {
		//prepare and replay
		executorRequests[0] = executorRequest;
		EasyMock.expect(requestParser.parse(httpRequest)).andReturn(null);
		PowerMock.expectNew(responseClass).andReturn(executorResponse);
		EasyMock.expect(executorResponse.getPageType()).andReturn(HtmlConstants.PAGE_TYPE_SUB).anyTimes();
		
		responseFormatter.format(EasyMock.anyObject(HttpServletRequest.class), EasyMock.anyObject(ExecutorResponse[].class));
		EasyMock.expect(mapping.findForward("subList")).andReturn(actionForward);
		PowerMock.replayAll();

		//invoke and verify
		ActionForward result;
		result = testedAction.doAction(mapping, httpRequest, httpResponse);
		PowerMock.verifyAll();
		//assert
		assertEquals("The two Object should be same.",result, actionForward);
	}
	
	
	//executorRequests != null, executorRequests.length == 0
	// and executorResponse.getPageType() == HtmlConstants.PAGE_TYPE_AJAXSIMPE
	public void testDoActionErr2() throws Exception {
		//prepare and replay
		EasyMock.expect(requestParser.parse(httpRequest)).andReturn(new ExecutorRequest[0]);
		PowerMock.expectNew(responseClass).andReturn(executorResponse);
		EasyMock.expect(executorResponse.getPageType()).andReturn(HtmlConstants.PAGE_TYPE_AJAXSIMPE).anyTimes();
		
		responseFormatter.format(EasyMock.anyObject(HttpServletRequest.class), EasyMock.anyObject(ExecutorResponse[].class));
		EasyMock.expect(mapping.findForward("ajaxSimpleList")).andReturn(actionForward);
		PowerMock.replayAll();

		//invoke and verify
		ActionForward result;
		result = testedAction.doAction(mapping, httpRequest, httpResponse);
		PowerMock.verifyAll();
		//assert
		assertEquals("The two Object should be same.",result, actionForward);
	}
	
	
	public void testDoActionException() throws Exception{
		//prepare and replay
		//EasyMock.expect(requestParser.parse(null)).andThrow(new ExecutorException());//andReturn(null);
		EasyMock.expect(mapping.findForward("success")).andReturn(actionForward);
		PowerMock.replayAll();

		//invoke and verify
		ActionForward result;
		result = testedAction4Ex.doAction(mapping, null, null);
		PowerMock.verifyAll();
		//assert
		assertEquals("The two Object should be same.",result, actionForward);
	}

}
