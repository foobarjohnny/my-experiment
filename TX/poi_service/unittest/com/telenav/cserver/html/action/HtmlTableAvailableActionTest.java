package com.telenav.cserver.html.action;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.mock.MockHttpServletRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.TestUtil;
import com.telenav.cserver.html.protocol.HtmlFeedbackRetrievalRequestParser;


public class HtmlTableAvailableActionTest {
	
	private ActionMapping mapping = new ActionMapping();
	MockHttpServletRequest request = null;
	public 	HtmlTableAvailableAction htmlTableAvailableAction = new HtmlTableAvailableAction();	

	@Before
	public void setUp() throws Exception {


		/*
		 * 	action : about.do
		 * 	executor : getTableAvailable
		 * 	parser:	HtmlTablePasrser
		 * 	formatter: HtmlTableFormatter 
		 * 	
		 */
		 mapping.addForwardConfig(new ActionForward("success","/jsp/AjaxResponse.jsp", false));
		 mapping.addForwardConfig(new ActionForward("failure","/jsp/ErrorMsgPage.jsp", false));
		 
		 htmlTableAvailableAction.setRequestParser(new HtmlFeedbackRetrievalRequestParser());
		 request = new MockHttpServletRequest();
		 request.setAttribute("HTML_CLIENT_INFO", TestUtil.getHtmlClientInfo());
		 
	
	}
	

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDoActionAction() throws Exception {
		htmlTableAvailableAction.doAction(mapping, request, null);
	}
}
