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
import com.telenav.j2me.datatypes.TxNode;


public class FindAirPortTest {

	private int	ajaxChildValue = 110;
	private String actionName = "FindAirPort.do";
	private String failString = "couldn't find the TxNode in file when testing FindAirPort action";

	private ActionMapping mapping = new ActionMapping();
	private MockHttpServletRequest request = null;
	private MockHttpServletResponse response = new MockHttpServletResponse();;

	public 	FindAirPort findAirPort = new FindAirPort();

	@Before
	public void setUp() throws Exception {

		mapping.addForwardConfig(new ActionForward("success","/jsp/ac/TypeAirport.jsp", false));

		request = (MockHttpServletRequest)Log2TxNode.getInstance().log2TxNode2HttpServletRequest(request, actionName, ajaxChildValue);
		if(null != request)
		{
			request.addParameter("from", "from");
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
			
			findAirPort.doAction(mapping, request, response);
			TxNode node = (TxNode)request.getAttribute("node");
			Assert.assertNotNull(node);
			
		} catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

