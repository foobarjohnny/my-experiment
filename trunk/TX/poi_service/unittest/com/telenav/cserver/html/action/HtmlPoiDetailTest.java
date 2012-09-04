package com.telenav.cserver.html.action;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.mock.MockHttpServletRequest;
import org.apache.struts.mock.MockHttpServletResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.html.protocol.HtmlPoiDetailFormatter;
import com.telenav.cserver.html.protocol.HtmlPoiDetailParser;
import com.telenav.cserver.util.helper.log2protobuf.log2Protobuf;

public class HtmlPoiDetailTest {

	private ActionMapping mapping = new ActionMapping();
	private MockHttpServletRequest request = null;
	private MockHttpServletResponse response = null;
	
	private HtmlPoiDetailParser parser = new HtmlPoiDetailParser();
	private HtmlPoiDetailFormatter formatter = new HtmlPoiDetailFormatter();
	
	public 	HtmlPoiDetail htmlPoiDetail = new HtmlPoiDetail();
	
	@Before
	public void setUp() throws Exception {
		
		/*
		 * 	action : getPoiDetailData.do
		 * 	executor : HtmlPoiDetailExecutor
		 * 	parser:	HtmlPoiDetailParser
		 * 	formatter: HtmlPoiDetailFormatter 
		 * 	
		 */
		htmlPoiDetail.setRequestParser(parser);
		htmlPoiDetail.setResponseFormatter(formatter);
		
		mapping.addForwardConfig(new ActionForward("success","/jsp/AjaxResponse.jsp", false));
		mapping.addForwardConfig(new ActionForward("failure","/jsp/AjaxErrResponse.jsp", false));

		request = (MockHttpServletRequest)log2Protobuf.getInstance().getProtobuf("mock");
		request.addParameter("dummyData", "dummyData");
		mapping.addForwardConfig(new ActionForward("success","/jsp/AjaxResponse.jsp", false));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDoActionAction() {
		try 
		{
			htmlPoiDetail.doAction(mapping, request, response);
		} catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
