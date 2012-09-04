package com.telenav.browser.movie.action;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.browser.movie.test.MockHttpServletRequest;
import com.telenav.browser.movie.test.MockHttpServletResponse;
import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;


@RunWith(PowerMockRunner.class)
@PrepareForTest(DataHandler.class)
public class TestMovieInfoShowTimes {

	ActionMapping mapping;
	MockHttpServletRequest httpRequest;
	MockHttpServletResponse httpResponse;
	MovieInfoShowTimes movieInfoShowTimes = new MovieInfoShowTimes();
	
	@Before
	public void setUp() throws Exception {
		httpRequest = new MockHttpServletRequest(new byte[]{});
		httpRequest.addParameter("clientInfo","{\"platform\":\"ANDROID\"," +
	    		"\"buildNumber\":\"7011109\"," +
	    		"\"deviceCarrier\":\"ATT\"," +
	    		"\"locale\":\"en_US\"," +
	    		"\"device\":\"generic\"," +
	    		"\"programCode\":\"ATTNAVPROG\"," +
	    		"\"gpsType\":\"AGPS\"," +
	    		"\"productType\":\"ATT_NAV\"," +
	    		"\"version\":\"7.0.01\"}");
		httpRequest.addParameter("deviceid","test123");
		httpRequest.addParameter("guidetone","test123");
		httpRequest.addParameter("locale","en_US");
		httpRequest.addParameter("region","NA");
		httpRequest.addParameter("audioformat","test123");
		httpRequest.addParameter("carrier","ATT");
		httpRequest.addParameter("platform","ANDROID");
		httpRequest.addParameter("devicemodel","sdk");
		httpRequest.addParameter("buildnumber","6.2.01");
		httpRequest.addParameter("width","480-320");
		httpRequest.addParameter("height","320-480");
		httpRequest.addParameter("browserversion","6.2.01");
		httpRequest.addParameter("useraccount","TEST_1234567890");
		httpRequest.addParameter("userpin","1234");
		httpRequest.addParameter("userid","12345678");
		httpRequest.addParameter("subid2", "test sub id2");
		httpRequest.addParameter("producttype","ATT");
		httpRequest.addParameter("version","6_2_01");
		httpRequest.addParameter("client_support_screen_width","480-320");
		httpRequest.addParameter("client_support_screen_height","320-480");
		
		TxNode latNode= new TxNode();
		latNode.addValue(4364275);
		TxNode lonNode= new TxNode();
		lonNode.addValue(-7961564);
		TxNode movieIdNode= new TxNode();
		movieIdNode.addMsg("190367");
		TxNode unitNode= new TxNode();
		unitNode.addValue(0);
		DataHandler handler = PowerMock.createMock(DataHandler.class, httpRequest,true);
		EasyMock.expect(handler.getParameter("movieId")).andReturn(movieIdNode);
		EasyMock.expect(handler.getParameter("dateIndex")).andReturn(null);
		EasyMock.expect(handler.getParameter("anchorLat")).andReturn(latNode);
		EasyMock.expect(handler.getParameter("anchorLon")).andReturn(lonNode);
		EasyMock.expect(handler.getParameter("distUnit")).andReturn(unitNode);
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_LOCALE)).andReturn("en_US");
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_WIDTH)).andReturn("480");
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_HEIGHT)).andReturn("360");
		EasyMock.expect(handler.getClientInfo(DataHandler.KEY_CARRIER)).andReturn("ATT");
		
		httpRequest.setAttribute(BrowserFrameworkConstants.CLIENT_INFO, handler);
		httpRequest.addParameter("jsp","MovieList");
		httpRequest.addHeader("x-up-calling-line-id", "TEST_1234567890");
		
		
		httpResponse = new MockHttpServletResponse();

		mapping = new ActionMapping();
		mapping.addForwardConfig(new ActionForward("success","/jsp/MovieInfoShowTimes.jsp",false));
		mapping.addForwardConfig(new ActionForward("failure","/jsp/ErrorMsgPage.jsp",false));
	}

	@Test
	public void testDoAction() {
		try {
			PowerMock.replayAll();
			movieInfoShowTimes.doAction(mapping, httpRequest, httpResponse);
			PowerMock.verifyAll();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
