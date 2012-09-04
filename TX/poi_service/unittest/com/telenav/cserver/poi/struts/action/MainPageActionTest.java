package com.telenav.cserver.poi.struts.action;


import junit.framework.Assert;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.util.MockHttpServletRequest;
import com.telenav.cserver.util.MockHttpServletResponse;


public class MainPageActionTest {

	private ActionMapping mapping = new ActionMapping();
	private MockHttpServletRequest request = null;
	private MockHttpServletResponse response = new MockHttpServletResponse();

	public 	MainPageAction mainPageAction = new MainPageAction();
	
	@Before
	public void setUp() throws Exception {
		
		request = new MockHttpServletRequest(null);			
		mapping.addForwardConfig(new ActionForward("success","/jsp/AjaxResponse.jsp", false));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDoActionAction() {
		
		try {
			mainPageAction.doAction(mapping, request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Assert.fail();
		}
		
	}

}
