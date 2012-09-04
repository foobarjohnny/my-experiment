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

public class DummyDataActionTest {
	private ActionMapping mapping = new ActionMapping();
	private MockHttpServletRequest request = new MockHttpServletRequest();
	private MockHttpServletResponse response = new MockHttpServletResponse();
	public 	DummyDataAction dummyDataAction = new DummyDataAction();	

	@Before
	public void setUp() throws Exception {
		mapping.addForwardConfig(new ActionForward("success","/jsp/AjaxResponse.jsp", false));
		mapping.addForwardConfig(new ActionForward("failure","/jsp/ErrorMsgPage.jsp", false));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDoActionAction() {
		try 
		{	
			String[] parameters  = {
					"",
					"default",
					"gas",
					"menuExtra",
					"sponsor",
					"showtime",
					"ads",
					"sponsorWithAD",
					"new4IPhone",
					"hotel",
					"openTable",
					"postOffice",
					"ATM",
					"poiDesc"};
			for(String para: parameters)
			{
				MockHttpServletRequest request = new MockHttpServletRequest();
				request.addParameter("poi", para);
				
				dummyDataAction.doAction(mapping, request, response);
				Assert.assertNotNull(request.getAttribute("ajaxResponse"));
			}
			
		} catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
