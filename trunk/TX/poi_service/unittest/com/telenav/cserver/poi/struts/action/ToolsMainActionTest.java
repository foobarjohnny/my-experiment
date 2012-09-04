package com.telenav.cserver.poi.struts.action;

import junit.framework.Assert;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.util.MockHttpServletRequest;
import com.telenav.cserver.util.MockHttpServletResponse;
import com.telenav.cserver.util.helper.log2txnode.Log2TxNode;

public class ToolsMainActionTest {

	private int	ajaxChildValue = 110;
	private String actionName = "ToolsMainAction.do";
	private String failString = "couldn't find the TxNode in file when testing ToolsMainAction action";

	private ActionMapping mapping = new ActionMapping();
	private MockHttpServletRequest request = null;
	private MockHttpServletResponse response = new MockHttpServletResponse();;
	
	public 	ToolsMainAction toolsMainAction = new ToolsMainAction();	

	@Before
	public void setUp() throws Exception {
		
		mapping.addForwardConfig(new ActionForward("success","/jsp/AjaxResponse.jsp", false));
		mapping.addForwardConfig(new ActionForward("failure","/jsp/ErrorMsgPage.jsp", false));
		
		request = (MockHttpServletRequest)Log2TxNode.getInstance().log2TxNode2HttpServletRequest(request, actionName, ajaxChildValue);
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
			
			toolsMainAction.doAction(mapping, request, response);
			
		} catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
