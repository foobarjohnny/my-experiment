package com.telenav.cserver.misc.struts.action;

import junit.framework.Assert;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.misc.protocol.SentAddressRequestParser;
import com.telenav.cserver.misc.protocol.SentAddressResponseFormatter;
import com.telenav.cserver.util.MockHttpServletRequest;
import com.telenav.cserver.util.MockHttpServletResponse;
import com.telenav.cserver.util.helper.log2txnode.Log2TxNode;

public class SentAddressActionTest extends SentAddressAction{

	private int	ajaxChildValue = 100;
	private String actionName = "SentAddress.do";
	private String failString = "couldn't find the TxNode in file when testing SentAddress action";
	
	private ActionMapping mapping = new ActionMapping();
	private MockHttpServletRequest request = null;
	private MockHttpServletResponse response = new MockHttpServletResponse();;
	private SentAddressRequestParser parser = new SentAddressRequestParser();
	private SentAddressResponseFormatter formatter = new SentAddressResponseFormatter();
	
	public 	SentAddressAction sentAddressAction = new SentAddressAction();
	
	@Before
	public void setUp() throws Exception {
		
		sentAddressAction.setRequestParser(parser);
		sentAddressAction.setResponseFormatter(formatter);

		mapping.addForwardConfig(new ActionForward("summary","/jsp/misc/SendedAddressList.jsp", false));
		mapping.addForwardConfig(new ActionForward("success","/jsp/AjaxResponse.jsp", false));
		mapping.addForwardConfig(new ActionForward("failure","/jsp/ErrorMsgPage.jsp", false));
		mapping.addForwardConfig(new ActionForward("Globe_Exception","/jsp/ErrorPage.jsp", false));
         
		request = (MockHttpServletRequest)Log2TxNode.getInstance().log2TxNode2HttpServletRequest(request, actionName, ajaxChildValue);
		byte[] data = String.valueOf("<?xml version=\"1.0\" encoding=\"utf-8\"?> <data></data>").getBytes();		
		request.setInputStreamData(data);
		request.addParameter("action", "delete");
	}

	@Test
	public void testDoActionAction() {
		try 
		{
			if(request == null)
			{
				Assert.fail(failString);
			}
			sentAddressAction.doAction(mapping, request, response);
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

}
