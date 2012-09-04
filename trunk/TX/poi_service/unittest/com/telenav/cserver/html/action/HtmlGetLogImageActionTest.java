package com.telenav.cserver.html.action;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.mock.MockHttpServletRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.TestUtil;
import com.telenav.cserver.framework.html.datatype.HtmlClientInfo;
import com.telenav.cserver.html.protocol.HtmlGetLogImageParser;
import com.telenav.cserver.html.util.HtmlConstants;
import com.telenav.cserver.util.helper.Log2TxNode;

public class HtmlGetLogImageActionTest {
	
	private ActionMapping mapping = new ActionMapping();
	private MockHttpServletRequest request = null;
	public 	HtmlGetLogImageAction htmlGetLogImageAction = new HtmlGetLogImageAction();	

	@Before
	public void setUp() throws Exception {
		
        String jsonString = "{\"width\":480, \"height\":800, \"center\":\"center\", \"markers\": \"markers\", \"imageName\": \"imageName\"}";
		htmlGetLogImageAction.setRequestParser(new HtmlGetLogImageParser());
		request = (MockHttpServletRequest)Log2TxNode.getInstance().log2TxNode2HttpServletRequest(request, "", 110);
		
		HtmlClientInfo htmlClientInfo = TestUtil.getHtmlClientInfo();
		request.setAttribute("HTML_CLIENT_INFO", htmlClientInfo);
		
		request.addParameter("jsonStr", jsonString);
		request.addParameter("operateType", HtmlConstants.OPERATE_FETCH_IMAGE);
		request.addParameter("imageName", "");
		
		mapping.addForwardConfig(new ActionForward("success","/jsp/AjaxResponse.jsp", false));
		mapping.addForwardConfig(new ActionForward("failure","/jsp/AjaxErrResponse.jsp", false));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDoActionAction() throws Exception {
		htmlGetLogImageAction.doAction(mapping, request, null);
	}

}
