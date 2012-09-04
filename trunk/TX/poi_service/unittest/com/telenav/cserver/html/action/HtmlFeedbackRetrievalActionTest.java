package com.telenav.cserver.html.action;

import junit.framework.Assert;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.mock.MockHttpServletRequest;
import org.apache.struts.mock.MockHttpServletResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.html.protocol.HtmlFeedbackRetrievalRequestParser;
import com.telenav.cserver.html.protocol.HtmlFeedbackRetrievalResponseFormatter;
import com.telenav.cserver.util.helper.log2protobuf.log2Protobuf;

public class HtmlFeedbackRetrievalActionTest {

private String failString = "couldn't find the TxNode in file when testing About action";	
	
	private ActionMapping mapping = new ActionMapping();
	private MockHttpServletRequest request = null;
	private MockHttpServletResponse response = new MockHttpServletResponse();
	
	private HtmlFeedbackRetrievalRequestParser parser = new HtmlFeedbackRetrievalRequestParser();
	private HtmlFeedbackRetrievalResponseFormatter formatter = new HtmlFeedbackRetrievalResponseFormatter();

	public 	HtmlFeedbackRetrievalAction htmlFeedbackRetrievalAction = new HtmlFeedbackRetrievalAction();	

	@Before
	public void setUp() throws Exception {


		/*
		 * 	action : ajaxFeedback.do
		 * 	executor : GenericFeedbackRetrievalExecutor
		 * 	parser:	HtmlFeedbackRetrievalRequestParser
		 * 	formatter: HtmlFeedbackRetrievalResponseFormatter 
		 * 	
		 */
		
		htmlFeedbackRetrievalAction.setRequestParser(parser);
		htmlFeedbackRetrievalAction.setResponseFormatter(formatter);
		
		mapping.addForwardConfig(new ActionForward("success","/jsp/AjaxResponse.jsp", false));
		request = (MockHttpServletRequest)log2Protobuf.getInstance().getProtobuf("mock");
		request.addParameter("jsp", "");
		
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
			
			htmlFeedbackRetrievalAction.doAction(mapping, request, response);
			Assert.assertNotNull(request.getAttribute("ajaxResponse"));
			
		} catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
