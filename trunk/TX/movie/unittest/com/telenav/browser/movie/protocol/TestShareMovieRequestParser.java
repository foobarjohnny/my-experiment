package com.telenav.browser.movie.protocol;

import static org.junit.Assert.*;

import javax.servlet.http.HttpServletRequest;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.browser.movie.executor.ShareMovieRequest;
import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;


@RunWith(PowerMockRunner.class)
@PrepareForTest(ShareMovieRequestParser.class)
public class TestShareMovieRequestParser {

	private ShareMovieRequestParser requestParse;
	private HttpServletRequest request;
	
	@Before
	public void setUp() throws Exception {
		requestParse = new ShareMovieRequestParser();

		String details = "{\"tAddr\":\"{_firstLine_:_201 South Market Street_," +
				"_zip_:_95113_,_state_:_CA_,_lat_:3733180,_lon_:-12188980," +
				"_country_:_US_,_city_:_San Jose_}\"," +
				"\"tName\":\"Hackworth IMAX Dome\",\"name\":\"Born to Be Wild\"}";		
		String recepients = "{\"recipient\":\"484-848-4848\"}";	
		String ptn = "4848484848";
		String userId = "9966058";

		TxNode body = PowerMock.createMock(TxNode.class);
		DataHandler handler = PowerMock.createMock(DataHandler.class);
		request = PowerMock.createMock(HttpServletRequest.class);
		
		EasyMock.expect(request.getAttribute(BrowserFrameworkConstants.CLIENT_INFO)).andReturn(handler);
		EasyMock.expect(handler.getAJAXBody()).andReturn(body);
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_USERACCOUNT)).andReturn(ptn);
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_USERID)).andReturn(userId);
		EasyMock.expect(body.msgAt(0)).andReturn(details);
		EasyMock.expect(body.msgAt(1)).andReturn(recepients);		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetExecutorType() {
		assertEquals("shareMovie", requestParse.getExecutorType());
	}

	@Test
	public void testParseBrowserRequest() {		
		try {
			PowerMock.replayAll();
			ShareMovieRequest shareMovieRequest = (ShareMovieRequest) requestParse.parseBrowserRequest(request);
			assertEquals(shareMovieRequest.getMovieName(), "Born to Be Wild");
			PowerMock.verifyAll();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
