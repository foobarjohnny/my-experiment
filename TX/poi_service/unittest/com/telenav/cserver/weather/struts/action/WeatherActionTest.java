package com.telenav.cserver.weather.struts.action;

import java.util.ArrayList;
import junit.framework.Assert;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorDispatcher;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.util.MockHttpServletRequest;
import com.telenav.cserver.util.MockHttpServletResponse;
import com.telenav.cserver.util.helper.log2txnode.Log2TxNode;
import com.telenav.cserver.util.helper.log2txnode.TxNode2Request;
import com.telenav.cserver.weather.executor.WeatherResponse;
import com.telenav.cserver.weather.protocol.WeatherRequestParser;
import com.telenav.cserver.weather.protocol.WeatherResponseFormatter;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;

public class WeatherActionTest {

    ExecutorDispatcher ac = ExecutorDispatcher.getInstance();
    
	private WeatherAction instance = new WeatherAction();
	private WeatherRequestParser parse = new WeatherRequestParser();
	private WeatherResponseFormatter format = new WeatherResponseFormatter();
	private ExecutorRequest executorRequest = null; 
	private WeatherResponse executorResponse = new WeatherResponse();			
	private MockHttpServletRequest request = null;
	private MockHttpServletResponse response = null;
	private ActionMapping mapping = new ActionMapping();

	
	@Before
	public void setUp() throws Exception {

		String TxNodePath = Log2TxNode.getTxNodePath();
		
		// step 1, get TxNode from log
		TxNode node = new TxNode();
		ArrayList<TxNode> nodeArray = Log2TxNode.getInstance().getTxNode("Weather.do", TxNodePath);
		if(nodeArray != null && nodeArray.size() > 0){
			node = nodeArray.get(0);
		}
		
		// step 2, get the ajax part from TxNode
		TxNode ajaxBody = null;
		TxNode itrNode = node.childAt(0);
		if(itrNode.childAt(0) != null){
			if(itrNode.childAt(0).valueAt(0) == 110){
				ajaxBody = itrNode.childAt(0).childAt(0);
			}
		}

		// set Datahandler using TxNode 
		request = new MockHttpServletRequest(TxNode.toByteArray(ajaxBody));
		response = new MockHttpServletResponse();
		mapping.addForwardConfig(new ActionForward("success","/jsp/ajaxResponse.jsp", false));
		mapping.addForwardConfig(new ActionForward("Globe_Exception","/jsp/ErrorPage.jsp", false));
		
		TxNode2Request.getInstance(node.childAt(0)).toMockHttpServletRequest(request);
		DataHandler handler = new DataHandler(request, true);
		request.setAttribute(BrowserFrameworkConstants.CLIENT_INFO, handler);
		
		executorRequest = parse.parse(request)[0];
		instance.setRequestParser(parse);
		instance.setResponseFormatter(format);
		
	}

	@After
	public void tearDown() throws Exception {
		// clear the data
		
	}
	
	@Test
	public void testdoAction(){
		
		try {		
			instance.doAction(mapping, request, response);
			Assert.assertTrue(true);
			
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
