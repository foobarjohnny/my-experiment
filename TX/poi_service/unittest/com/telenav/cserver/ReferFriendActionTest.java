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

public class ReferFriendActionTest extends TestCase {

	private MockHttpServletRequest httpRequest = null;
	private MockHttpServletResponse httpResponse = null;

	@Override
	protected void setUp() {
		TxNode actionNode = new TxNode();
		actionNode.addValue(DataConstants.ACT_SYNC_CONTACTS);
		actionNode.addValue(10L);
		actionNode.addMsg("http://127.0.0.1:8080/poi_service/touch62/ReferFriend.do");

		TxNode phoneNode = new TxNode();
		phoneNode.addValue(168L);
		phoneNode.addValue(0L);
		phoneNode.addValue(1L);
		phoneNode.addValue(0L);
		phoneNode.addValue(3L);
		phoneNode.addValue(0L);
		phoneNode.addMsg("");
		phoneNode.addMsg("1234567890"); // phone number
		phoneNode.addChild(new TxNode());

		TxNode contactsNode = new TxNode();
		TxNode wrapNode = new TxNode();
		wrapNode.addChild(phoneNode);
		contactsNode.addChild(wrapNode);
		actionNode.addChild(contactsNode);

		System.out.println(actionNode);

		httpRequest = new MockHttpServletRequest(TxNode.toByteArray(actionNode));
		DataHandler handler = new DataHandler(httpRequest, true);
		httpRequest.setAttribute(BrowserFrameworkConstants.CLIENT_INFO, handler);

		httpResponse = new MockHttpServletResponse();
	}

	@Test
	public void testDoAction() {
		ReferFriendAction action = new ReferFriendAction();

		ActionMapping mapping = new ActionMapping();
		mapping.setPath("/ReferFriend");
		mapping.setCancellable(false);
		mapping.setValidate(false);
		mapping.setScope("request");
		mapping.addForwardConfig(new ActionForward("success", "/jsp/ajaxResponse.jsp", false));
		mapping.addForwardConfig(new ActionForward("Globe_Exception", "/jsp/ErrorPage.jsp", false));

		action.doAction(mapping, httpRequest, httpResponse);
	}
}
