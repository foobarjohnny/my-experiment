package com.telenav.cserver.html.action;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.mock.MockHttpServletRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class HtmlChooseJspTest extends HtmlChooseJsp {

	HtmlChooseJsp instance = new HtmlChooseJsp();
	private ActionMapping mapping = new ActionMapping();
	MockHttpServletRequest request = new MockHttpServletRequest();
	@Before
	public void setUp() throws Exception {
		
		mapping.addForwardConfig(new ActionForward("test","/jsp/AjaxResponse.jsp", false));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDoActionActionMappingHttpServletRequestHttpServletResponse() throws Exception {
		request.addParameter("jsp", "test");
		instance.doAction(mapping, request, null);
	}

}
