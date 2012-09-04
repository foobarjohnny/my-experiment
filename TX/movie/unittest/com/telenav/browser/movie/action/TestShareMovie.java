package com.telenav.browser.movie.action;

import static org.junit.Assert.*;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.me.JSONObject;
import org.junit.Before;
import org.junit.Test;

import com.telenav.browser.movie.protocol.ShareMovieRequestParser;
import com.telenav.browser.movie.protocol.ShareMovieResponseFormatter;
import com.telenav.browser.movie.test.MockHttpServletRequest;
import com.telenav.browser.movie.test.MockHttpServletResponse;
import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.framework.executor.protocol.ProtocolRequestParser;
import com.telenav.cserver.framework.executor.protocol.ProtocolResponseFormatter;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;

public class TestShareMovie {
	
	ProtocolRequestParser requestParser = new ShareMovieRequestParser();
	ProtocolResponseFormatter responseFormatter = new ShareMovieResponseFormatter();
	ActionMapping mapping;
	MockHttpServletRequest httpRequest;
	MockHttpServletResponse httpResponse;
	ShareMovie shareMovie = new ShareMovie();

	@Before
	public void setUp() throws Exception {
		TxNode node = new TxNode();
		JSONObject joDetails = new JSONObject("{\"name\":\"Abraham Lincoln: Vampire Hunter 3D\",\"tName\":\"AMC Winston Churchill 24\",\"tAddr\":\"{_firstLine_:_2081 Winston Park Drive_,_zip_:_L6H 6P5_,_state_:_ON_,_lat_:4350870,_lon_:-7966990,_country_:_CA_,_city_:_Oakville_}\"}");
		JSONObject joRecepients = new JSONObject("{\"r_number\":2,\"recipient0\":\"4444444444\",\"recipient1\":\"8888888888\"}");
		node.addMsg(joDetails.toString());
		node.addMsg(joRecepients.toString());
		TxNode ajaxNode = new TxNode();
		ajaxNode.addChild(node);
		httpRequest = new MockHttpServletRequest(TxNode.toByteArray(ajaxNode));
		
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
	    
		DataHandler handler =new DataHandler(httpRequest,true);
		httpRequest.setAttribute(BrowserFrameworkConstants.CLIENT_INFO, handler);
		httpRequest.addHeader("x-up-calling-line-id", "TEST_1234567890");

		
		mapping = new ActionMapping();
		mapping.addForwardConfig(new ActionForward("success","/jsp/AjaxResponse.jsp",false));
		mapping.addForwardConfig(new ActionForward("failure","/jsp/AjaxErrResponse.jsp",false));
		
		shareMovie.setRequestParser(requestParser);
		shareMovie.setResponseFormatter(responseFormatter);
	}

	@Test
	public void test() {
		try {
			shareMovie.doAction(mapping, httpRequest, httpResponse);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
