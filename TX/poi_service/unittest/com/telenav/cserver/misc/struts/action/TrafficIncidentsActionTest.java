package com.telenav.cserver.misc.struts.action;

import javax.servlet.http.HttpServletRequest;

import junit.framework.Assert;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.util.MockHttpServletRequest;
import com.telenav.cserver.util.MockHttpServletResponse;
import com.telenav.cserver.util.helper.log2txnode.Log2TxNode;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;

class DataHandlerForUT extends DataHandler
{
	public DataHandlerForUT(HttpServletRequest request) {
		super(request, true);
	}
	

	public Object getParameter(String key)
	{
		if(key.equals("locationJO"))
		{
			TxNode txNode = new TxNode();
			JSONObject locationJo = new JSONObject();
			try {
				locationJo.put("lat", 3737392);
				locationJo.put("lon", -12199919);	
			} catch (JSONException e) {
				e.printStackTrace();
			}	
			txNode.addMsg(locationJo.toString());
			return txNode;
		}
		
		return super.getParameter(key);

	}
	
	
}

public class TrafficIncidentsActionTest {

	private int	ajaxChildValue = 110;
	private String actionName = "trafficIncidents.do";
	private String failString = "couldn't find the TxNode in file when testing trafficIncidents action";
	
	private ActionMapping mapping = new ActionMapping();
	private MockHttpServletRequest request = null;
	private MockHttpServletResponse response = new MockHttpServletResponse();;
	
	public 	TrafficIncidentsAction trafficIncidentsAction = new TrafficIncidentsAction();
	
	@Before
	public void setUp() throws Exception {
		mapping.addForwardConfig(new ActionForward("success","/jsp/AjaxResponse.jsp", false));
		mapping.addForwardConfig(new ActionForward("failure","/jsp/ErrorMsgPage.jsp", false));
		mapping.addForwardConfig(new ActionForward("Globe_Exception","/jsp/ErrorPage.jsp", false));
         
		request = (MockHttpServletRequest)Log2TxNode.getInstance().log2TxNode2HttpServletRequest(request, actionName, ajaxChildValue);
		DataHandlerForUT dataHandlerForUT = new DataHandlerForUT(request);
		request.setAttribute(BrowserFrameworkConstants.CLIENT_INFO, dataHandlerForUT);
	}

	@Test
	public void testDoActionAction() {
		try 
		{
			if(request == null)
			{
				Assert.fail(failString);
			}
			
			trafficIncidentsAction.doAction(mapping, request, response);
			JSONObject json = (JSONObject)request.getAttribute("trafficAlertsJson");
			Assert.assertNotNull(json);
			
		} catch (Exception e) 
		{
			// TODO Auto-generated catch block
			if(e.getClass().equals(java.lang.NullPointerException.class))
			{
				System.out.println(trafficIncidentsAction.getClass().getName() + " has code issue.");
			}
		}
	}

}
