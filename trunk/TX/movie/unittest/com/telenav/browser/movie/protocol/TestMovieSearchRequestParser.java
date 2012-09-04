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

import com.telenav.browser.movie.Constant;
import com.telenav.browser.movie.executor.MovieSearchRequest;
import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.tnbrowser.util.DataHandler;
import com.telenav.j2me.datatypes.TxNode;

@RunWith(PowerMockRunner.class)
@PrepareForTest(MovieSearchRequestParser.class)
public class TestMovieSearchRequestParser {

	private MovieSearchRequestParser requestParse;
	private HttpServletRequest request;
	
	@Before
	public void setUp() throws Exception {
		requestParse = new MovieSearchRequestParser();
		
		String joString = "{\"DU\":1,\"batchSize\":20,\"sortByName\":\"true\",\"inputString\":\"\",\"addressString\":\"" +
				"{\\\"zip\\\":\\\"\\\",\\\"lon\\\":-12199983,\\\"state\\\":\\\"\\\",\\\"firstLine\\\":\\\"\\\"," +
				"\\\"label\\\":\\\"\\\",\\\"type\\\":6,\\\"lat\\\":3737453,\\\"country\\\":\\\"\\\",\\\"city\\\":\\\"\\\"}" +
				"\",\"batchNumber\":1,\"dateIndex\":\"2012-03-13\"}";
		
		TxNode body = PowerMock.createMock(TxNode.class);
		DataHandler handler = PowerMock.createMock(DataHandler.class);
		request = PowerMock.createMock(HttpServletRequest.class);
		
		EasyMock.expect(request.getAttribute(BrowserFrameworkConstants.CLIENT_INFO)).andReturn(handler);
		EasyMock.expect(handler.getAJAXBody()).andReturn(body);
		EasyMock.expect(body.msgAt(0)).andReturn(joString);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetExecutorType() {
		assertEquals(requestParse.getExecutorType(),"movieSearch");
	}

	@Test
	public void testParseBrowserRequest() {
		try {
			PowerMock.replayAll();
			MovieSearchRequest movieSearchRequest = (MovieSearchRequest) requestParse.parseBrowserRequest(request);
			assertEquals(movieSearchRequest.getSearchDate(), "2012-03-13");
			assertEquals(movieSearchRequest.getAddress().lat,3737453);
			assertEquals(movieSearchRequest.getAddress().lon,-12199983);
			assertEquals(movieSearchRequest.getBatchSize(),20);
			assertEquals(movieSearchRequest.getDistanceUnit(),Constant.DUNIT_MILES);
			PowerMock.verifyAll();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
