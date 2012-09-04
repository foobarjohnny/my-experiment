package com.telenav.browser.movie.action;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.json.me.JSONObject;
import org.junit.Before;
import org.junit.Test;

import com.telenav.browser.movie.protocol.MovieSearchRequestParser;
import com.telenav.browser.movie.protocol.MovieSearchResponseFormatter;
import com.telenav.browser.movie.test.MockHttpServletRequest;
import com.telenav.browser.movie.test.MockHttpServletResponse;
import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.framework.executor.protocol.ProtocolRequestParser;
import com.telenav.cserver.framework.executor.protocol.ProtocolResponseFormatter;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;

public class TestMovieListAction {

	ProtocolRequestParser requestParser =  new MovieSearchRequestParser();
	ProtocolResponseFormatter responseFormatter = new MovieSearchResponseFormatter();
	ActionMapping mapping;
	MockHttpServletRequest httpRequest;
	MockHttpServletResponse httpResponse;
	MovieListAction movieListAction;
	
	
	@Before
	public void setUp() throws Exception {
		
		TxNode node = new TxNode();
		JSONObject jo = new JSONObject("{\"inputString\":\"\",\"batchNumber\":1,\"sortByName\":\"true\",\"batchSize\":20,\"addressString\":\"{\\\"country\\\":\\\"\\\",\\\"state\\\":\\\"\\\",\\\"city\\\":\\\"\\\",\\\"firstLine\\\":\\\"\\\",\\\"label\\\":\\\"\\\",\\\"lon\\\":-7961564,\\\"lat\\\":4364275,\\\"type\\\":6,\\\"zip\\\":\\\"\\\"}\",\"dateIndex\":\"\",\"DU\":0}");
		node.addMsg(jo.toString());
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
		
		movieListAction = new MovieListAction();
		movieListAction.setRequestParser(requestParser);
		movieListAction.setResponseFormatter(responseFormatter);
	}

	@Test
	public void testDoAction() {
		try {
			movieListAction.doAction(mapping, httpRequest, httpResponse);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
