package com.telenav.cserver.html.action;

import junit.framework.Assert;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.mock.MockHttpServletRequest;
import org.apache.struts.mock.MockHttpServletResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.html.protocol.HtmlAdsFormatter;
import com.telenav.cserver.html.protocol.HtmlAdsParser;
import com.telenav.cserver.util.helper.log2protobuf.log2Protobuf;

public class HtmlAdsActionTest extends HtmlAdsAction {

	private String failString = "couldn't find the Protobuf in file when testing adsinfo action";
	
	private ActionMapping mapping = new ActionMapping();
	private MockHttpServletRequest request = null;
	private MockHttpServletResponse response = new MockHttpServletResponse();
	
	private HtmlAdsParser parser = new HtmlAdsParser();
	private HtmlAdsFormatter formatter = new HtmlAdsFormatter();
	public 	HtmlAdsAction htmlAdsAction = new HtmlAdsAction();	

	@Before
	public void setUp() throws Exception {

		/*
		 * 	action : adsinfo.do
		 * 	executor : HtmlAdsExecutor
		 * 	parser:	HtmlAdsParser
		 * 	formatter: HtmlAdsFormatter 
		 * 	
		 */
		htmlAdsAction.setRequestParser(parser);
		htmlAdsAction.setResponseFormatter(formatter);
		
		mapping.addForwardConfig(new ActionForward("success","/jsp/AjaxResponse.jsp", false));
		mapping.addForwardConfig(new ActionForward("failure","/jsp/AjaxErrResponse.jsp", false));
		mapping.addForwardConfig(new ActionForward("basic","/jsp/adsbasic.jsp", false));
		mapping.addForwardConfig(new ActionForward("detail","/jsp/adsdetail.jsp", false));

		request = (MockHttpServletRequest)log2Protobuf.getInstance().getProtobuf("mock");
		
		request.addParameter("isDummy", "true");
		request.addParameter("operateType", "basic");
		request.addParameter("logoHeight", "800");
		request.addParameter("logoHeight", "480");
		request.addParameter("adsId", "0");
	        
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
			
			htmlAdsAction.doAction(mapping, request, response);
			Assert.assertNotNull(request.getAttribute("adsdetail"));
			
		} catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
