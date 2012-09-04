package com.telenav.cserver.html.action;

import junit.framework.Assert;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.mock.MockHttpServletRequest;
import org.apache.struts.mock.MockHttpServletResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.html.protocol.HtmlHotelFormatter;
import com.telenav.cserver.html.protocol.HtmlHotelParser;
import com.telenav.cserver.util.helper.log2protobuf.log2Protobuf;

public class HtmlHotelActionTest {

private String failString = "couldn't find the Protobuf in file when testing getHotelDetailData action";
	
	private ActionMapping mapping = new ActionMapping();
	private MockHttpServletRequest request = null;
	private MockHttpServletResponse response = new MockHttpServletResponse();
	
	private HtmlHotelParser parser = new HtmlHotelParser();
	private HtmlHotelFormatter formatter = new HtmlHotelFormatter();
	public 	HtmlHotelAction htmlHotelAction = new HtmlHotelAction();	

	@Before
	public void setUp() throws Exception {

		/*
		 * 	action : getHotelDetailData.do
		 * 	executor : HtmlHotelExecutor
		 * 	parser:	HtmlHotelParser
		 * 	formatter: responseFormatter 
		 * 	
		 */
		
		
		htmlHotelAction.setRequestParser(parser);
		htmlHotelAction.setResponseFormatter(formatter);
		
		mapping.addForwardConfig(new ActionForward("success","/jsp/AjaxResponse.jsp", false));
		mapping.addForwardConfig(new ActionForward("failure","/jsp/AjaxErrResponse.jsp", false));
		
		request = (MockHttpServletRequest)log2Protobuf.getInstance().getProtobuf("mock");	
		String JsonString = "{poiId:3413774939, isDummy:false}";
		request.addParameter("jsonStr", JsonString);
		    
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
			
			htmlHotelAction.doAction(mapping, request, response);
			Assert.assertNotNull(request.getAttribute("ajaxResponse"));
			
		} catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
