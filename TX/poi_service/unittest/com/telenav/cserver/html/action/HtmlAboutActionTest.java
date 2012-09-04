package com.telenav.cserver.html.action;

import junit.framework.Assert;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.mock.MockHttpServletRequest;
import org.apache.struts.mock.MockHttpServletResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.html.protocol.HtmlAboutRequestParser;
import com.telenav.cserver.html.protocol.HtmlAboutResponseFormatter;
import com.telenav.cserver.util.helper.log2protobuf.log2Protobuf;

public class HtmlAboutActionTest {

	private String failString = "couldn't find the TxNode in file when testing About action";	
	
	private ActionMapping mapping = new ActionMapping();
	private MockHttpServletRequest request = null;
	private MockHttpServletResponse response = new MockHttpServletResponse();
	private HtmlAboutRequestParser parser = new HtmlAboutRequestParser();
	private HtmlAboutResponseFormatter formatter = new HtmlAboutResponseFormatter();

	public 	HtmlAboutAction htmlAboutAction = new HtmlAboutAction();	

	@Before
	public void setUp() throws Exception {


		/*
		 * 	action : about.do
		 * 	executor : HtmlAboutExecutor
		 * 	parser:	HtmlAboutRequestParser
		 * 	formatter: HtmlAboutResponseFormatter 
		 * 	
		 */
		
		htmlAboutAction.setRequestParser(parser);
		htmlAboutAction.setResponseFormatter(formatter);
		
		mapping.addForwardConfig(new ActionForward("success","/jsp/AjaxResponse.jsp", false));
		mapping.addForwardConfig(new ActionForward("failure","/jsp/ErrorMsgPage.jsp", false));

		request = (MockHttpServletRequest)log2Protobuf.getInstance().getProtobuf("mock");
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
			
			htmlAboutAction.doAction(mapping, request, response);
		} catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
