package com.telenav.cserver.html.action;

import junit.framework.Assert;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.mock.MockHttpServletRequest;
import org.json.me.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.TestUtil;
import com.telenav.cserver.html.protocol.HtmlPoiDetailFormatter;
import com.telenav.cserver.html.protocol.HtmlPoiDetailParser;
import com.telenav.cserver.html.util.HtmlConstants;

public class HtmlPoiDetailActionTest {

private String failString = "couldn't find the TxNode in file when testing About action";	
	
	private ActionMapping mapping = new ActionMapping();
	private MockHttpServletRequest request = new MockHttpServletRequest();
	private HtmlPoiDetailParser parser = new HtmlPoiDetailParser();
	private HtmlPoiDetailFormatter formatter = new HtmlPoiDetailFormatter();
	public 	HtmlPoiDetailAction htmlPoiDetailAction = new HtmlPoiDetailAction();	

	@Before
	public void setUp() throws Exception {


		/*
		 * 	action : getPoiDetailData.do
		 * 	executor : HtmlPoiDetailExecutor
		 * 	parser:	HtmlPoiDetailParser
		 * 	formatter: HtmlPoiDetailFormatter 
		 * 	
		 */
		
		htmlPoiDetailAction.setRequestParser(parser);
		htmlPoiDetailAction.setResponseFormatter(formatter);
		
		mapping.addForwardConfig(new ActionForward("success","/jsp/AjaxResponse.jsp", false));
		mapping.addForwardConfig(new ActionForward("failure","/jsp/ErrorMsgPage.jsp", false));

		request.setAttribute("HTML_CLIENT_INFO", TestUtil.getHtmlClientInfo());
		
		request.addParameter("operateType",HtmlConstants.OPERATE_POIDETAIL_MAIN);
		JSONObject jo = new JSONObject();
		jo.put("poiId", "1234");
		jo.put("width", "480");
		jo.put("height", "800");
		jo.put("menuWidth", "240");
		jo.put("menuHeight", "400");
		jo.put("categoryId", "1");
		jo.put("adsId", "1");
		
		request.addParameter("jsonStr", jo.toString());
				
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
			
			htmlPoiDetailAction.doAction(mapping, request, null);
		} catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
