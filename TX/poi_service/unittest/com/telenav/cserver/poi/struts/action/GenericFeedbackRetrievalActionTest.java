package com.telenav.cserver.poi.struts.action;

import junit.framework.Assert;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.poi.protocol.GenericFeedbackRetrievalRequestParser;
import com.telenav.cserver.poi.protocol.GenericFeedbackRetrievalResponseFormatter;
import com.telenav.cserver.util.MockHttpServletRequest;
import com.telenav.cserver.util.MockHttpServletResponse;
import com.telenav.cserver.util.helper.log2txnode.Log2TxNode;

public class GenericFeedbackRetrievalActionTest {
	
	private int	ajaxChildValue = 100;
	private String actionName = "GenericFeedbackRetrieval.do";
	private String failString = "couldn't find the TxNode in file when testing GenericFeedbackRetrieval action";

	private ActionMapping mapping = new ActionMapping();
	private MockHttpServletRequest request = null;
	private MockHttpServletResponse response = new MockHttpServletResponse();;
	private GenericFeedbackRetrievalRequestParser parser = new GenericFeedbackRetrievalRequestParser();
	private GenericFeedbackRetrievalResponseFormatter formatter = new GenericFeedbackRetrievalResponseFormatter();
	
	public 	GenericFeedbackRetrievalAction genericFeedbackRetrievalAction = new GenericFeedbackRetrievalAction();	

	@Before
	public void setUp() throws Exception {
		
		genericFeedbackRetrievalAction.setRequestParser(parser);
		genericFeedbackRetrievalAction.setResponseFormatter(formatter);
		mapping.addForwardConfig(new ActionForward("successForPOIDetailFeedback","/jsp/poi/POIListFeedbackNew.jsp", false));
		mapping.addForwardConfig(new ActionForward("successForPurchaseAbandonSurvey","/jsp/poi/successForPurchaseAbandonSurvey.jsp", false));
		mapping.addForwardConfig(new ActionForward("successForAdJugglerSurvey","/jsp/poi/successForAdJugglerSurvey.jsp", false));
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
			
			genericFeedbackRetrievalAction.doAction(mapping, request, response);
			
		} catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
