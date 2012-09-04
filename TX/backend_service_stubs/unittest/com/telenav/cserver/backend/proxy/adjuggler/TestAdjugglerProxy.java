package com.telenav.cserver.backend.proxy.adjuggler;

import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.kernel.util.datatypes.TnContext;

import junit.framework.TestCase;

/**
 * 
 * @author chbzhang
 * 
 */
public class TestAdjugglerProxy extends TestCase {
	private AdjugglerProxy proxy = null;

	@Override
	protected void setUp() throws Exception {
		proxy = new TestableAdjugglerProxy();
	}

	public void testGetAdjugglerInfo4Success() throws ThrottlingException {
		AdjugglerResponse response = proxy.getAdjugglerInfo(
				createAdjugglerRequest(), new TnContext());
		assertNotNull("response shoud not be null", response);
		assertTrue("status code should be " + AdjugglerResponse.STATUS_SUCESS
				+ "but is " + response.getStatusCode(),
				AdjugglerResponse.STATUS_SUCESS == response.getStatusCode());
	}

	public void testGetAdjugglerInfo4Fail() throws ThrottlingException {
		AdjugglerResponse response = proxy.getAdjugglerInfo(
				createErrorAdjugglerRequest(), new TnContext());
		assertNotNull("response shoud not be null", response);
		assertTrue("status code should not be "
				+ AdjugglerResponse.STATUS_SUCESS + " but is "
				+ response.getStatusCode(),
				AdjugglerResponse.STATUS_SUCESS != response.getStatusCode());
	}

	public void testGetExtendHttpClientParams() {
		Map<String, Object> map = proxy.getExtendHttpClientParams();
		assertNotNull("the map object shoud not be null", map);
	}

	public AdjugglerRequest createAdjugglerRequest() {
		String url = "http://telenav.rotator.hadj7.adjuggler.net/servlet/ajrotator/81235/0/vh?z=telenav&click=";
		AdjugglerRequest request = new AdjugglerRequest();
		request.setUrl(url);

		return request;
	}

	public AdjugglerRequest createErrorAdjugglerRequest() {
		String url = "http://xyz";
		AdjugglerRequest request = new AdjugglerRequest();
		request.setUrl(url);

		return request;
	}

	class TestableAdjugglerProxy extends AdjugglerProxy {

	}
}
