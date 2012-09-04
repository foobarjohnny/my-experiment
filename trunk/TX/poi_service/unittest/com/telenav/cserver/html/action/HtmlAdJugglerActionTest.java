package com.telenav.cserver.html.action;

import junit.framework.Assert;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.mock.MockHttpServletRequest;
import org.apache.struts.mock.MockHttpServletResponse;
import org.json.me.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.framework.html.datatype.HtmlClientInfo;
import com.telenav.cserver.framework.html.util.HtmlCommonUtil;
import com.telenav.cserver.framework.html.util.HtmlFrameworkConstants;
import com.telenav.cserver.html.protocol.HtmlAdJugglerRequestParser;
import com.telenav.cserver.html.protocol.HtmlAdJugglerResponseFormatter;
import com.telenav.cserver.html.util.HtmlAdjugglerUtil;
import com.telenav.cserver.util.helper.log2protobuf.log2Protobuf;

public class HtmlAdJugglerActionTest {

private String failString = "couldn't find the TxNode in file when testing CheckAdJuggler action";	
	
	private ActionMapping mapping = new ActionMapping();
	private MockHttpServletRequest request = null;
	private MockHttpServletResponse response = new MockHttpServletResponse();
	private HtmlAdJugglerRequestParser parser = new HtmlAdJugglerRequestParser();
	private HtmlAdJugglerResponseFormatter formatter = new HtmlAdJugglerResponseFormatter();

	public 	HtmlAdJugglerAction htmlAdJugglerAction = new HtmlAdJugglerAction();	

	@Before
	public void setUp() throws Exception {
		/*
		 * 	action : CheckAdJuggler.do
		 * 	executor : HtmlAdJugglerExecutor
		 * 	parser:	HtmlAdJugglerRequestParser
		 * 	formatter: HtmlAdJugglerResponseFormatter 
		 * 	
		 */
		
		htmlAdJugglerAction.setRequestParser(parser);
		htmlAdJugglerAction.setResponseFormatter(formatter);
		
		mapping.addForwardConfig(new ActionForward("success","/jsp/AjaxResponse.jsp", false));
		mapping.addForwardConfig(new ActionForward("failure","/jsp/ErrorMsgPage.jsp", false));

		request = (MockHttpServletRequest)log2Protobuf.getInstance().getProtobuf("mock");
		request.addParameter("jsonStr", "{}");
		request.addParameter(BrowserFrameworkConstants.IMAGE_KEY, "");
		request.addParameter(HtmlFrameworkConstants.HTML_CLIENT_INFO, request.getAttribute("HTML_CLIENT_INFO").toString());
		request.addParameter("ssoToken", "AAAAAACXE0YAAAEyUSle1K6s3k7a3znYfyLGzhdNkz0");
		
		request.setAttribute("Host_url", "http://hqs-");
		request.addParameter("appCode", "Navigation");
		request.addParameter("clientInfo", request.getAttribute("HTML_CLIENT_INFO").toString());
		request.addParameter("width", "480");
		request.addParameter("height", "800");
			
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
			
			htmlAdJugglerAction.doAction(mapping, request, response);
//			Assert.assertNotNull(request.getAttribute("ajaxResponse"));
			
		} catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
