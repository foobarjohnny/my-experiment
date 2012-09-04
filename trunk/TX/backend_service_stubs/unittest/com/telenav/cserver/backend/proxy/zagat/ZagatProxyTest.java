package com.telenav.cserver.backend.proxy.zagat;

import junit.framework.TestCase;

import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.kernel.util.datatypes.TnContext;


/**
 * 
 * @author chandrak
 * 
 */
public class ZagatProxyTest  extends TestCase  {
	
	private ZagatProxy proxy = null;
	
	@Override
	protected void setUp() throws Exception {
		proxy = new TestableZagatProxy();
	}
	
	public void testZagatSearch() throws ThrottlingException {
		
		ZagatResponse response = proxy.getZagatResponse(createZagatRequest(), new TnContext());
		assertNotNull("response shoud not be null", response);
		assertTrue("status code should be " + ZagatResponse.STATUS_SUCESS
				+ "but is " + response.getStatusCode(),
				ZagatResponse.STATUS_SUCESS == response.getStatusCode());
	}
	private ZagatRequest createZagatRequest() {
		// TODO Auto-generated method stub
		ZagatRequest request = new ZagatRequest();
		request.setUrl("http://spt-appserver-2028474069.us-east-1.elb.amazonaws.com:8080/tribster/lsapi/searchByLocation?country_code=US&locale_code=en_US&where=-12199983;3737453&radius=20000&sort_by=relevance&items=10&page=1");
		return request;
	}
	class TestableZagatProxy extends ZagatProxy {

	}
}
