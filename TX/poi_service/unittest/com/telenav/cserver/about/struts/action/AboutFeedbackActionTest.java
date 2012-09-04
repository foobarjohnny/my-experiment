package com.telenav.cserver.about.struts.action;

import java.util.ArrayList;

import junit.framework.Assert;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.about.protocol.AboutFeedbackRequestParser;
import com.telenav.cserver.about.protocol.AboutFeedbackResponseFormatter;
import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.util.MockHttpServletRequest;
import com.telenav.cserver.util.MockHttpServletResponse;
import com.telenav.cserver.util.helper.log2txnode.Log2TxNode;
import com.telenav.cserver.util.helper.log2txnode.TxNode2Request;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;

public class AboutFeedbackActionTest {

    
	private AboutFeedbackAction instance = new AboutFeedbackAction();
	private AboutFeedbackRequestParser parse = new AboutFeedbackRequestParser();
	private AboutFeedbackResponseFormatter format = new AboutFeedbackResponseFormatter();		
	private MockHttpServletRequest request = null;
	private MockHttpServletResponse response = null;
	private ActionMapping mapping = new ActionMapping();
	
	
	@Before
	public void setUp() throws Exception {
		
		String TxNodePath = Log2TxNode.getTxNodePath();
		// step 1, get TxNode from log
		TxNode node = new TxNode();
		ArrayList<TxNode> nodeArray = Log2TxNode.getInstance().getTxNode("About.do", TxNodePath);
		if(nodeArray != null && nodeArray.size() > 0){
			node = nodeArray.get(0);
		}
		
		// step 2, get the ajax part from TxNode
		TxNode ajaxBody = null;
		TxNode itrNode = node.childAt(0);
		if(itrNode.childAt(0) != null){
			if(itrNode.childAt(0).valueAt(0) == 100){
				ajaxBody = itrNode.childAt(0).childAt(0);
			}
		}

		// set Datahandler using TxNode 
		request = new MockHttpServletRequest(TxNode.toByteArray(ajaxBody));
		response = new MockHttpServletResponse();
		mapping.addForwardConfig(new ActionForward("success","/jsp/ajaxResponse.jsp", false));
		mapping.addForwardConfig(new ActionForward("Globe_Exception","/jsp/ErrorPage.jsp", false));
		
		TxNode2Request.getInstance(node.childAt(0)).toMockHttpServletRequest(request);
		DataHandler handler = new DataHandler(request, true);
		request.setAttribute(BrowserFrameworkConstants.CLIENT_INFO, handler);
		
		instance.setRequestParser(parse);
		instance.setResponseFormatter(format);
		
		
	}
	
	@After
	public void tearDown() throws Exception {
		// clear the data
		
	}
	
	@Test
	public void testdoAction(){
		
		try {		
			instance.doAction(mapping, request, response);
			Assert.assertTrue(true);
			
		} catch (Exception e) {
			System.out.println("AboutFeedback Action is not completed yet, refer to getFeedbackTopics in FeedbackContents for more detail");
			System.out.println(e.getClass().getName());
		}
	}
}
