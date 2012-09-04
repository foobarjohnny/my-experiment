package com.telenav.cserver.html.action;

import junit.framework.Assert;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.mock.MockHttpServletRequest;
import org.apache.struts.mock.MockHttpServletResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.util.helper.log2protobuf.log2Protobuf;

public class ChoosePOIActionTest {
	
	private String failString = "couldn't find the TxNode in file when testing ChoosePOI action";	
	
	private ActionMapping mapping = new ActionMapping();
	private MockHttpServletRequest request = null;
	private MockHttpServletResponse response = new MockHttpServletResponse();
	public 	ChoosePOIAction choosePOIAction = new ChoosePOIAction();	

	@Before
	public void setUp() throws Exception {
		
		mapping.addForwardConfig(new ActionForward("success","/jsp/choosePOI.jsp", false));
		request = (MockHttpServletRequest)log2Protobuf.getInstance().getProtobuf("mock");
		
		request.addParameter("poi", "");
		request.addParameter("json", "");
		request.addParameter("width", "480");
		request.addParameter("height", "800");
		request.addParameter("clientInfo", "{}");
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDoActionAction() {
		try 
		{
			if(request == null)
			{
				Assert.fail(failString);
			}		
			choosePOIAction.doAction(mapping, request, response);
			Assert.assertNotNull(request.getParameter("poi"));
			Assert.assertNotNull(request.getParameter("json"));
			Assert.assertNotNull(request.getParameter("clientInfo"));
			
		} catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
