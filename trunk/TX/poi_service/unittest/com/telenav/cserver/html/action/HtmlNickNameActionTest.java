package com.telenav.cserver.html.action;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.mock.MockHttpServletRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.TestUtil;
import com.telenav.cserver.html.protocol.HtmlNickNameFormatter;
import com.telenav.cserver.html.protocol.HtmlNickNameParser;
import com.telenav.cserver.html.util.HtmlConstants;

public class HtmlNickNameActionTest {
	private ActionMapping mapping = new ActionMapping();
	private MockHttpServletRequest request = new MockHttpServletRequest();
	private HtmlNickNameParser parser = new HtmlNickNameParser();
	private HtmlNickNameFormatter formatter = new HtmlNickNameFormatter();

	public 	HtmlNickNameAction htmlNickNameAction = new HtmlNickNameAction();	

	@Before
	public void setUp() throws Exception {


		/*
		 * 	action : editNickName.do
		 * 	executor : HtmlNickNameExecutor
		 * 	parser:	HtmlNickNameParser
		 * 	formatter: HtmlNickNameFormatter 
		 * 	
		 */
		
		htmlNickNameAction.setRequestParser(parser);
		htmlNickNameAction.setResponseFormatter(formatter);
		
		mapping.addForwardConfig(new ActionForward("success","/jsp/AjaxResponse.jsp", false));
		mapping.addForwardConfig(new ActionForward("failure","/jsp/ErrorMsgPage.jsp", false));

		request.setAttribute("HTML_CLIENT_INFO", TestUtil.getHtmlClientInfo());
		request.addParameter("operateType", HtmlConstants.OPERATE_NICKNAME_CHECKANDUPDATE);
		request.addParameter("ssoToken", "AAAAAACXE0YAAAEyUSle1K6s3k7a3znYfyLGzhdNkz0");
		request.addParameter("nickName", "Jobs");
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDoActionAction() {
		try 
		{
			htmlNickNameAction.doAction(mapping, request, null);
		} catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
