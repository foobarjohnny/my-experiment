package com.telenav.cserver.html.action;

import static org.junit.Assert.*;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.mock.MockHttpServletRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.TestUtil;
import com.telenav.cserver.html.protocol.HtmlFeedbackRetrievalRequestParser;

public class HtmlRestaurantActionTest extends HtmlRestaurantAction {

	
	private ActionMapping mapping = new ActionMapping();
	MockHttpServletRequest request = null;
	public 	HtmlRestaurantAction htmlRestaurantAction = new HtmlRestaurantAction();	
	
	@Before
	public void setUp() throws Exception {
		
		 mapping.addForwardConfig(new ActionForward("success","/jsp/AjaxResponse.jsp", false));
		 mapping.addForwardConfig(new ActionForward("failure","/jsp/ErrorMsgPage.jsp", false));
		 
		 htmlRestaurantAction.setRequestParser(new HtmlFeedbackRetrievalRequestParser());  //
		 request = new MockHttpServletRequest();
		 request.setAttribute("HTML_CLIENT_INFO", TestUtil.getHtmlClientInfo());
		 
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDoActionActionMappingHttpServletRequestHttpServletResponse() throws Exception {
		htmlRestaurantAction.doAction(mapping, request, null);
	}

}
