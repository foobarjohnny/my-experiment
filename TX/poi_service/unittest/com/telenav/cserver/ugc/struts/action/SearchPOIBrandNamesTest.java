package com.telenav.cserver.ugc.struts.action;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.util.MockHttpServletRequest;
import com.telenav.cserver.util.MockHttpServletResponse;
import com.telenav.cserver.util.helper.log2txnode.Log2TxNode;
import com.telenav.j2me.datatypes.TxNode;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class SearchPOIBrandNamesTest {

	private int	ajaxChildValue = 110;
	private String actionName = "searchPOIBrandNames.do";
	private String failString = "couldn't find the TxNode in file when testing queryPoi action";
	
	private ActionMapping mapping = new ActionMapping();
	private MockHttpServletRequest request = null;
	private MockHttpServletResponse response = null;
	private SearchPOIBrandNames searchPOIBrandNames = new SearchPOIBrandNames();	


	@Before
	public void setUp() throws Exception {
		
		mapping.addForwardConfig(new ActionForward("success","/jsp/ajaxResponse.jsp", false));
		mapping.addForwardConfig(new ActionForward("Globe_Exception","/jsp/ErrorPage.jsp", false));
		
		request = (MockHttpServletRequest)Log2TxNode.getInstance().log2TxNode2HttpServletRequest(request, actionName, ajaxChildValue);
		if(null != request)
		{
			request.addParameter("jsp", "success");			
		}
	}

	@After
	public void tearDown() throws Exception {		// clear the data
	}
	
	@Test
	public void testdoAction(){
		try 
		{
			if(request == null)
			{
				Assert.fail(failString);
			}
			
			searchPOIBrandNames.doAction(mapping, request, response);
			TxNode node = (TxNode)request.getAttribute("node");
			Assert.assertNotNull(node);
			
		} catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}