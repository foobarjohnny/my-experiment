package com.telenav.cserver.html.action;

import junit.framework.Assert;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.mock.MockHttpServletRequest;
import org.apache.struts.mock.MockHttpServletResponse;
import org.json.me.JSONArray;
import org.json.me.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.TestUtil;
import com.telenav.cserver.html.protocol.HtmlPoiListFeedbackSaveRequestParser;
import com.telenav.cserver.html.protocol.HtmlPoiListFeedbackSaveResponseFormatter;
import com.telenav.cserver.poi.struts.util.PoiUtil;
import com.telenav.cserver.util.helper.log2protobuf.log2Protobuf;
import com.telenav.j2me.datatypes.Stop;

public class HtmlPoiListFeedbackSaveActionTest {

private String failString = "couldn't find the TxNode in file when testing About action";	
	
	private ActionMapping mapping = new ActionMapping();
	private MockHttpServletRequest request = new MockHttpServletRequest();
	private MockHttpServletResponse response = new MockHttpServletResponse();
	private HtmlPoiListFeedbackSaveRequestParser parser = new HtmlPoiListFeedbackSaveRequestParser();
	private HtmlPoiListFeedbackSaveResponseFormatter formatter = new HtmlPoiListFeedbackSaveResponseFormatter();

	public 	HtmlPoiListFeedbackSaveAction htmlPoiListFeedbackSaveAction = new HtmlPoiListFeedbackSaveAction();	

	@Before
	public void setUp() throws Exception {


		/*
		 * 	action : poiListFeedbackSave.do
		 * 	executor : POIFeedbackSaveExecutor
		 * 	parser:	HtmlPoiListFeedbackSaveRequestParser
		 * 	formatter: HtmlPoiListFeedbackSaveResponseFormatter 
		 * 	
		 */
		
		htmlPoiListFeedbackSaveAction.setRequestParser(parser);
		htmlPoiListFeedbackSaveAction.setResponseFormatter(formatter);
		
		mapping.addForwardConfig(new ActionForward("success","/jsp/AjaxResponse.jsp", false));
		mapping.addForwardConfig(new ActionForward("failure","/jsp/ErrorMsgPage.jsp", false));

		request.setAttribute("HTML_CLIENT_INFO", TestUtil.getHtmlClientInfo());
		
		JSONObject jo = new JSONObject();
		jo.put("searchCatName", "searchCatName");
		jo.put("searchKeyword", "searchKeyword");
		JSONObject stop = new JSONObject();
		stop.put("lat", 100000);
		stop.put("lon", 100000);
		stop.put("label", "label");
		stop.put("firstLine", "1130 kifer rd");
		stop.put("city", "sunnyvale");
		stop.put("state", "CA");
		stop.put("zip", "zip");
		stop.put("country", "USA");
		
		jo.put("searchLocation", "{stop:" + stop.toString() + "}");
		jo.put("feedbackPage", "feedbackPage");
		jo.put("feedbackQuestion", "feedbackQuestion");
		JSONArray jarray = new JSONArray();
		JSONObject jos = new JSONObject();
		jo.put("feedback", "feedback");
		jarray.put(jos);
		jo.put("feedbacks", jarray.toString());
		jo.put("comment", "comment");
		
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
			
			htmlPoiListFeedbackSaveAction.doAction(mapping, request, response);
		} catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
