package com.telenav.cserver.ac.struts.action;

import junit.framework.Assert;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.util.MockHttpServletRequest;
import com.telenav.cserver.util.MockHttpServletResponse;
import com.telenav.cserver.util.helper.log2txnode.Log2TxNode;


public class SelectAddressTest {
	
	private int	ajaxChildValue = 110;
	private String actionName = "SelectAddress.do";
	private String failString = "couldn't find the TxNode in file when testing SelectAddress action";
	
	private ActionMapping mapping = new ActionMapping();
	private MockHttpServletRequest request = null;
	private MockHttpServletResponse response = new MockHttpServletResponse();;
	
	public 	SelectAddress selectAddress = new SelectAddress();
	
	@Before
	public void setUp() throws Exception {
		
		mapping.addForwardConfig(new ActionForward("SelectAddress","/jsp/ac/SelectAddress.jsp", false));
		mapping.addForwardConfig(new ActionForward("TypeAddress","/jsp/ac/TypeAddress.jsp", false));
		mapping.addForwardConfig(new ActionForward("TypeIntersection","/jsp/ac/TypeIntersection.jsp", false));
		mapping.addForwardConfig(new ActionForward("TypeCity","/jsp/ac/TypeCity.jsp", false));
		mapping.addForwardConfig(new ActionForward("TypeAirport","/jsp/ac/TypeAirport.jsp", false));
		
		request = (MockHttpServletRequest)Log2TxNode.getInstance().log2TxNode2HttpServletRequest(request, actionName, ajaxChildValue);	
		if(null != request)
		{
			request.addParameter("jsp", "SelectAddress");
		}
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
			selectAddress.doAction(mapping, request, response);
			
		} catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
