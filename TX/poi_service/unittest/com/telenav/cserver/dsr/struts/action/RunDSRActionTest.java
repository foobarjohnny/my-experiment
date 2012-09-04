package com.telenav.cserver.dsr.struts.action;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.util.MockHttpServletRequest;
import com.telenav.cserver.util.MockHttpServletResponse;
import com.telenav.cserver.util.helper.log2txnode.Log2TxNode;


public class RunDSRActionTest {

	private RunDSRAction instance = new RunDSRAction();
	private MockHttpServletRequest request = null;
	private MockHttpServletResponse response = null;
	private ActionMapping mapping = new ActionMapping();
	
	@Before
	public void setUp() throws Exception {
		
		mapping.addForwardConfig(new ActionForward("success","/jsp/ajaxResponse.jsp", false));
		mapping.addForwardConfig(new ActionForward("Globe_Exception","/jsp/ErrorPage.jsp", false));
		mapping.addForwardConfig(new ActionForward("SpeakSearch1","/jsp/dsr/SpeakSearch1.jsp", false));
		mapping.addForwardConfig(new ActionForward("SpeakSearchAlong1","/jsp/dsr/SpeakSearchAlong1.jsp", false));
		mapping.addForwardConfig(new ActionForward("SpeakCity1","/jsp/dsr/SpeakCity1.jsp", false));
		mapping.addForwardConfig(new ActionForward("SpeakAirport1","/jsp/dsr/SpeakAirport1.jsp", false));
		mapping.addForwardConfig(new ActionForward("SpeakAddress1","/jsp/dsr/SpeakAddress1.jsp", false));
		mapping.addForwardConfig(new ActionForward("SpeakIntersection1","/jsp/dsr/SpeakIntersection1.jsp", false));
		mapping.addForwardConfig(new ActionForward("SpeakCommand1","/jsp/dsr/SpeakCommand1.jsp", false));
		mapping.addForwardConfig(new ActionForward("SpeakFlow3","/jsp/dsr/SpeakFlow3.jsp", false));
	}
	
	@After
	public void tearDown() throws Exception {
		// release the resource
		instance = null;
		request = null;
		response = null;
		mapping = null;
	}

	@Test
	public void testDoAction(){
		try {
			String[][] actions = {
					{"SpeakSearch1","/jsp/dsr/SpeakSearch1.jsp"},
					{"SpeakSearchAlong1","/jsp/dsr/SpeakSearchAlong1.jsp"},
					{"SpeakCity1","/jsp/dsr/SpeakCity1.jsp"},
					{"SpeakAirport1","/jsp/dsr/SpeakAirport1.jsp"},
					{"SpeakAddress1","/jsp/dsr/SpeakAddress1.jsp"},
					{"SpeakIntersection1","/jsp/dsr/SpeakIntersection1.jsp"},
					{"SpeakCommand1","/jsp/dsr/SpeakCommand1.jsp"},
					{"SpeakFlow3","/jsp/dsr/SpeakFlow3.jsp"}
			};
			for(int i = 0; i < actions.length; i++)
			{			
				request = (MockHttpServletRequest)Log2TxNode.getInstance().log2TxNode2HttpServletRequest(request, "dsr", 110);
				byte[] data = String.valueOf("<?xml version=\"1.0\" encoding=\"utf-8\"?> <shareData> <bean name=\"beanName\" valueType=\"TxNode\" value=\"\" /> </shareData>").getBytes();		
				request.setInputStreamData(data);
						
				request.addParameter("action", actions[i][0]);
				ActionForward forward = instance.doAction(mapping, request, response);
				Assert.assertEquals(forward.getPath(), actions[i][1]);
			}
		} catch (Exception e) {
			Assert.fail("RunDSRAction test failed");
			e.printStackTrace();
		}
	}
	
}
