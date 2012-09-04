package com.telenav.cserver.weather.struts.action;


import junit.framework.Assert;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorDispatcher;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.util.MockHttpServletRequest;
import com.telenav.cserver.util.MockHttpServletResponse;
import com.telenav.cserver.util.helper.log2txnode.Log2TxNode;
import com.telenav.cserver.weather.executor.I18NWeatherResponse;
import com.telenav.cserver.weather.protocol.I18NWeatherRequestParser;
import com.telenav.cserver.weather.protocol.I18NWeatherResponseFormatter;
import com.telenav.j2me.datatypes.TxNode;;

public class I18NWeatherActionTest {

	
	private int	ajaxChildValue = 110;
	private String actionName = "Weather.do";
	private String failString = "couldn't find the TxNode in file when testing Weather action";
	
    ExecutorDispatcher ac = ExecutorDispatcher.getInstance();
	private ExecutorRequest executorRequest = null; 
	private I18NWeatherResponse executorResponse = new I18NWeatherResponse();			
	
	private ActionMapping mapping = new ActionMapping();
	private MockHttpServletRequest request = null;
	private MockHttpServletResponse response = null;
	private I18NWeatherRequestParser parser = new I18NWeatherRequestParser();
	private I18NWeatherResponseFormatter formatter = new I18NWeatherResponseFormatter();

	private I18NWeatherAction i18NWeatherAction = new I18NWeatherAction();
	

	
	@Before
	public void setUp() throws Exception {
		
		i18NWeatherAction.setRequestParser(parser);
		i18NWeatherAction.setResponseFormatter(formatter);
		mapping.addForwardConfig(new ActionForward("success","/jsp/ajaxResponse.jsp", false));
		mapping.addForwardConfig(new ActionForward("Globe_Exception","/jsp/ErrorPage.jsp", false));
		
		request = (MockHttpServletRequest)Log2TxNode.getInstance().log2TxNode2HttpServletRequest(request, actionName, ajaxChildValue);
		response = new MockHttpServletResponse();
		executorRequest = parser.parse(request)[0];

	}

	@After
	public void tearDown() throws Exception {  // clear the data
	}
	
	@Test
	public void testdoAction(){
		
		try {	
			
			if(request == null)
			{
				Assert.fail(failString);
			}
			
			i18NWeatherAction.doAction(mapping, request, response);
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
			Assert.assertTrue(false);
			e.printStackTrace();
		}
	}
}
