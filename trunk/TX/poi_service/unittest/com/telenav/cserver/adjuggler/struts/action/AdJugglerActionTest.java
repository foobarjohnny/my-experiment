package com.telenav.cserver.adjuggler.struts.action;

import junit.framework.Assert;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.adjuggler.executor.AdJugglerResponse;
import com.telenav.cserver.adjuggler.protocol.AdJugglerRequestParser;
import com.telenav.cserver.adjuggler.protocol.AdJugglerResponseFormatter;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorDispatcher;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.util.MockHttpServletRequest;
import com.telenav.cserver.util.MockHttpServletResponse;
import com.telenav.cserver.util.helper.log2txnode.Log2TxNode;
import com.telenav.j2me.datatypes.TxNode;

public class AdJugglerActionTest {

	private int	ajaxChildValue = 110;
	private String actionName = "CheckAdJuggler.do";
	private String failString = "couldn't find the TxNode in file when testing CheckAdJuggler action";
	
    ExecutorDispatcher ac = ExecutorDispatcher.getInstance();
	private ExecutorRequest executorRequest = null; 
	private AdJugglerResponse executorResponse = new AdJugglerResponse();
	
	private ActionMapping mapping = new ActionMapping();	
	private MockHttpServletRequest request = null;
	private MockHttpServletResponse response = new MockHttpServletResponse();;
	private AdJugglerRequestParser parser = new AdJugglerRequestParser();
	private AdJugglerResponseFormatter formatter = new AdJugglerResponseFormatter();
	
	private AdJugglerAction adJugglerAction = new AdJugglerAction();
	
	@Before
	public void setUp() throws Exception {
		
		adJugglerAction.setRequestParser(parser);
		adJugglerAction.setResponseFormatter(formatter);
		mapping.addForwardConfig(new ActionForward("success","/jsp/ajaxResponse.jsp", false));
		mapping.addForwardConfig(new ActionForward("failure","/jsp/ErrorMsgPage.jsp", false));

		request = (MockHttpServletRequest)Log2TxNode.getInstance().log2TxNode2HttpServletRequest(request, actionName, ajaxChildValue);
		if(null != request)
		{
			executorRequest = parser.parse(request)[0];
		}
	}

	@After
	public void tearDown() throws Exception {
		// clear the data
		
	}
	
	@Test
	public void testdoAction(){
		
		if(request == null)
		{
			Assert.fail(failString);
		}
		
		try {		
			adJugglerAction.doAction(mapping, request, response);
			TxNode node = (TxNode)request.getAttribute("node");
			Assert.assertNotNull(node);
			
		} catch (Exception e) {
			Assert.assertTrue(false);
			e.printStackTrace();
		}
	}
	
	@Test
	public void testExecutor(){
		try {		
			ac.execute(executorRequest, executorResponse, new ExecutorContext());
			Assert.assertEquals(ExecutorResponse.STATUS_OK, executorResponse.getStatus());
			
		} catch (Exception e) {
		//	Assert.assertTrue(false);
			e.printStackTrace();
		}
	}

}