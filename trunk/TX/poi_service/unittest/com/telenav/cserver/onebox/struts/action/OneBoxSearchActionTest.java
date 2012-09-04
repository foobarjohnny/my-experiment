package com.telenav.cserver.onebox.struts.action;

import junit.framework.Assert;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.onebox.protocol.OneBoxRequestParser;
import com.telenav.cserver.onebox.protocol.OneBoxResponseFormatter;
import com.telenav.cserver.util.MockHttpServletRequest;
import com.telenav.cserver.util.MockHttpServletResponse;
import com.telenav.cserver.util.helper.log2txnode.Log2TxNode;
import com.telenav.j2me.datatypes.TxNode;

public class OneBoxSearchActionTest {

	private int	ajaxChildValue = 110;
	private String actionName = "oneBoxSearch.do";
	private String failString = "couldn't find the TxNode in file when testing oneBoxSearch action";
	
	private ActionMapping mapping = new ActionMapping();
	private MockHttpServletRequest request = null;
	private MockHttpServletResponse response = new MockHttpServletResponse();
	private OneBoxRequestParser parser = new OneBoxRequestParser();
	private OneBoxResponseFormatter formatter = new OneBoxResponseFormatter();
	
	public 	OneBoxSearchAction oneBoxSearchAction = new OneBoxSearchAction();
		
	@Before
	public void setUp() throws Exception {

		oneBoxSearchAction.setRequestParser(parser);
		oneBoxSearchAction.setResponseFormatter(formatter);
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
			
			oneBoxSearchAction.doAction(mapping, request, response);
			TxNode node = (TxNode)request.getAttribute("node");
			Assert.assertNotNull(node);
			
		} catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
