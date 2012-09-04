package com.telenav.cserver.html.action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.util.MockHttpServletRequest;
import com.telenav.cserver.util.MockHttpServletResponse;

public class HtmlWeatherStartupTest {
	
	private ActionMapping mapping = new ActionMapping();
	private MockHttpServletRequest request = null;
	private MockHttpServletResponse response = null;
	
	public 	HtmlWeatherStartup htmlWeatherStartup = new HtmlWeatherStartup();
	
	@Before
	public void setUp() throws Exception {
		mapping.addForwardConfig(new ActionForward("success","/jsp/AjaxResponse.jsp", false));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDoActionAction() {
		try 
		{
			htmlWeatherStartup.doAction(mapping, request, response);
		} catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
