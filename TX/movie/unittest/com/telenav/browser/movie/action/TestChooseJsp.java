package com.telenav.browser.movie.action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.junit.Before;
import org.junit.Test;
import com.telenav.browser.movie.test.MockHttpServletRequest;
import com.telenav.browser.movie.test.MockHttpServletResponse;
import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.tnbrowser.util.DataHandler;

public class TestChooseJsp {
	
	ActionMapping mapping;
	MockHttpServletRequest httpRequest;
	MockHttpServletResponse httpResponse;
	ChooseJsp chooseJsp = new ChooseJsp();

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
		DataHandler handler = new DataHandler(httpRequest,true);
		httpRequest.setAttribute(BrowserFrameworkConstants.CLIENT_INFO, handler);
		httpRequest.addParameter("jsp","MovieList");
		httpRequest.addHeader("x-up-calling-line-id", "TEST_1234567890");
		
		
		httpResponse = new MockHttpServletResponse();
		

		mapping = new ActionMapping();
		mapping.addForwardConfig(new ActionForward("StartUp","/jsp/StartUp.jsp",false));
		mapping.addForwardConfig(new ActionForward("SearchMovie","/jsp/SearchMovie.jsp",false));
		mapping.addForwardConfig(new ActionForward("SearchTheater","/jsp/SearchTheater.jsp",false));
		mapping.addForwardConfig(new ActionForward("SelectDate","/jsp/SelectDate.jsp",false));
		mapping.addForwardConfig(new ActionForward("MovieList","/jsp/MovieList.jsp",false));
		mapping.addForwardConfig(new ActionForward("MovieListRimNonTouch","/jsp/MovieListRimNonTouch.jsp",false));
		mapping.addForwardConfig(new ActionForward("MovieListAndroid","/jsp/MovieListAndroid.jsp",false));
		mapping.addForwardConfig(new ActionForward("MenuItems","/jsp/MenuItems.jsp",false));
		mapping.addForwardConfig(new ActionForward("ShareMovie","/jsp/MovieListAndroid.jsp",false));
		mapping.addForwardConfig(new ActionForward("ACInterface","/jsp/SelectAddressExternalInterface.jsp",false));
		mapping.addForwardConfig(new ActionForward("SelectContact","/jsp/SelectContact.jsp",false));
		mapping.addForwardConfig(new ActionForward("MovieController","/jsp/controller/MovieController.jsp",false));
		
	}

	@Test
	public void testDoAction() {
		try {  
			chooseJsp.doAction(mapping, httpRequest, httpResponse);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
