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
import com.telenav.cserver.html.protocol.HtmlFeedbackSaveRequestParser;
import com.telenav.cserver.html.protocol.HtmlFeedbackSaveResponseFormatter;
import com.telenav.cserver.util.helper.log2protobuf.log2Protobuf;

public class HtmlFeedbackSaveActionTest {

private String failString = "couldn't find the TxNode in file when testing About action";	
	
	private ActionMapping mapping = new ActionMapping();
	private MockHttpServletRequest request = null;
	private MockHttpServletResponse response = new MockHttpServletResponse();
	private HtmlFeedbackSaveRequestParser parser = new HtmlFeedbackSaveRequestParser();
	private HtmlFeedbackSaveResponseFormatter formatter = new HtmlFeedbackSaveResponseFormatter();

	public 	HtmlFeedbackSaveAction htmlFeedbackSaveAction = new HtmlFeedbackSaveAction();	

	@Before
	public void setUp() throws Exception {


		/*
		 * 	action : feedbackSave.do
		 * 	executor : GenericFeedbackSaveExecutor
		 * 	parser:	HtmlFeedbackSaveRequestParser
		 * 	formatter: HtmlFeedbackSaveResponseFormatter 
		 * 	
		 */
		
		htmlFeedbackSaveAction.setRequestParser(parser);
		htmlFeedbackSaveAction.setResponseFormatter(formatter);
		
		mapping.addForwardConfig(new ActionForward("success","/jsp/AjaxResponse.jsp", false));
		mapping.addForwardConfig(new ActionForward("failure","/jsp/ErrorMsgPage.jsp", false));

		request = (MockHttpServletRequest)log2Protobuf.getInstance().getProtobuf("mock");
		request.setAttribute("HTML_CLIENT_INFO", TestUtil.getHtmlClientInfo());
		
		JSONObject json = new JSONObject();
		json.put("email", "mmli@telenavsoftware.com");
		json.put("userAgent", "Mozilla 5.0");
		json.put("pageName", "poiList");
		json.put("ssoToken", "AAAAAACXE0YAAAEyUSle1K6s3k7a3znYfyLGzhdNkz0");
		
		JSONObject stop = new JSONObject();
		stop.put("lat", 100000);
		stop.put("lon", 100000);
		stop.put("label", "label");
		stop.put("firstLine", "1130 kifer rd");
		stop.put("city", "sunnyvale");
		stop.put("state", "CA");
		stop.put("zip", "zip");
		stop.put("country", "USA");
		json.put("searchLocation", "{stop:" + stop.toString() + "}");
		json.put("currentLocation", stop.toString());

		json.put("questionID", "10");
		json.put("question", "question");
		json.put("comments", "comments");
		JSONArray jarray = new JSONArray();
		JSONObject jo = new JSONObject();
		jo.put("feedback", "feedback");
		jarray.put(jo);
		json.put("feedbacks", jarray.toString());
		
		request.addParameter("jsonStr", json.toString());
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
			
			htmlFeedbackSaveAction.doAction(mapping, request, response);
		} catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
