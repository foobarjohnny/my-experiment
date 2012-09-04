package com.telenav.cserver;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.junit.Test;

import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.util.MockHttpServletRequest;
import com.telenav.cserver.util.MockHttpServletResponse;
import com.telenav.j2me.datatypes.DataConstants;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;

import junit.framework.TestCase;

public class UpdateUserInfoActionTest extends TestCase {

	private MockHttpServletRequest httpRequest = null;
	private MockHttpServletResponse httpResponse = null;

	@Override
	protected void setUp() {
		TxNode mailNode = new TxNode();
		// {"firstName":"","email":"test@test.com","lastName":""}
		mailNode.addMsg("{\"firstName\":\"\",\"email\":\"test@test.com\",\"lastName\":\"\"}");

		TxNode actionNode = new TxNode();
		actionNode.addValue(DataConstants.ACT_SYNC_CONTACTS);
		actionNode.addValue(DataConstants.PREFERENCE_TRAFFIC_ALERT);
		actionNode.addMsg("http://127.0.0.1:8080/poi_service/touch62/updateUserInfo.do");
		actionNode.addChild(mailNode);

		TxNode clientLogNode = new TxNode();
		clientLogNode.addValue(110);
		clientLogNode.addChild(actionNode);

		System.out.println(actionNode);

		httpRequest = new MockHttpServletRequest(TxNode.toByteArray(actionNode));
		DataHandler handler = new DataHandler(httpRequest, true);
		httpRequest.setAttribute(BrowserFrameworkConstants.CLIENT_INFO, handler);

		httpResponse = new MockHttpServletResponse();
	}

	@Test
	public void testDoAction() {
		UpdateUserInfoAction action = new UpdateUserInfoAction();

		ActionMapping mapping = new ActionMapping();
		mapping.setPath("/updateUserInfo");
		mapping.setCancellable(false);
		mapping.setValidate(false);
		mapping.setScope("request");
		mapping.addForwardConfig(new ActionForward("success", "/jsp/ajaxResponse.jsp", false));
		mapping.addForwardConfig(new ActionForward("Globe_Exception", "/jsp/ErrorPage.jsp", false));

		action.doAction(mapping, httpRequest, httpResponse);
	}
}
