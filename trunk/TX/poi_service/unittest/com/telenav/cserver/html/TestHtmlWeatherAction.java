package com.telenav.cserver.html;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.easymock.EasyMock;
import org.json.me.JSONObject;
import org.powermock.api.easymock.PowerMock;
import org.powermock.api.support.membermodification.MemberModifier;
import org.powermock.reflect.Whitebox;

import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorDispatcher;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.protocol.ProtocolRequestParser;
import com.telenav.cserver.framework.executor.protocol.ProtocolResponseFormatter;
import com.telenav.cserver.html.action.HtmlWeatherAction;
import com.telenav.cserver.weather.executor.I18NWeatherResponse;

/**
 * 
 * @author chbzhang
 * 
 */
public class TestHtmlWeatherAction extends TestCase {
	private HttpServletRequest httpRequest = PowerMock
			.createMock(HttpServletRequest.class);
	private ActionMapping mapping = null;
	private HttpServletResponse httpResponse = PowerMock
			.createMock(HttpServletResponse.class);
	private ExecutorRequest[] executorRequests = new ExecutorRequest[1];
	private ExecutorRequest executorRequest = PowerMock
			.createMock(ExecutorRequest.class);
	private ExecutorContext executorContext = new ExecutorContext();
	private ProtocolRequestParser requestParser = PowerMock
			.createMock(ProtocolRequestParser.class);
	private ProtocolResponseFormatter responseFormatter = PowerMock
			.createMock(ProtocolResponseFormatter.class);
	private ExecutorDispatcher executorDispatcher = PowerMock
			.createMock(ExecutorDispatcher.class);
	private ActionForward actionForward = PowerMock
			.createMock(ActionForward.class);
	
	//---- modify me 2----------
	private Class<HtmlWeatherAction> actionClass = HtmlWeatherAction.class;
	private HtmlWeatherAction testedAction = Whitebox.newInstance(actionClass);
	private HtmlWeatherAction testedAction4Ex = PowerMock.createPartialMock(actionClass,"addErrors");
	
	//---- modify me 3, over----------
	private Class<I18NWeatherResponse> responseClass = I18NWeatherResponse.class;
	private I18NWeatherResponse executorResponse = PowerMock.createMock(responseClass);
	//----

	protected void setUp() throws Exception {

		MemberModifier.suppress(ActionMapping.class.getConstructors());
		mapping = PowerMock.createMock(ActionMapping.class);

		Whitebox.setInternalState(testedAction, "requestParser", requestParser);
		Whitebox.setInternalState(testedAction, "responseFormatter",
				responseFormatter);
	}
	
	public void testSimple() throws Exception{
		Object o = actionClass.newInstance();
	}

	
	
	// the test code is not completed, has error
	
/*
	public void testWeatherAction() throws Exception {
//		JSONObject addressinfo = new JSONObject();
//		addressinfo.put("city", "Sunnyvale");
//		addressinfo.put("state", "Ca");
//		addressinfo.put("country", "US");
//		addressinfo.put("lat", 3737391);
//		addressinfo.put("lon", -12199926);

		//prepare and replay
		executorRequests[0] = executorRequest;
		EasyMock.expect(requestParser.parse(httpRequest)).andReturn(executorRequests);
		
		PowerMock.mockStatic(ExecutorDispatcher.class);
		EasyMock.expect(ExecutorDispatcher.getInstance()).andReturn(executorDispatcher);
		PowerMock.expectNew(responseClass).andReturn(executorResponse);
		PowerMock.expectNew(ExecutorContext.class).andReturn(executorContext);
		
		executorDispatcher.execute(executorRequest, executorResponse, executorContext);
		EasyMock.expect(executorResponse.getStatus()).andReturn(ExecutorResponse.STATUS_OK);
		
		responseFormatter.format(EasyMock.anyObject(HttpServletRequest.class), EasyMock.anyObject(ExecutorResponse[].class));
		EasyMock.expect(mapping.findForward("success")).andReturn(actionForward);
		PowerMock.replayAll();

		//invoke and verify
		ActionForward result;
//		result = testedAction.doAction(mapping, httpRequest, httpResponse);
		PowerMock.verifyAll();
		//assert
//		assertEquals("The two Object should be same.",result, actionForward);
	}
*/
	
}
