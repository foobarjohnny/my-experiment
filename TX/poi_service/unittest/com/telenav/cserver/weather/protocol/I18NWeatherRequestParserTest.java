package com.telenav.cserver.weather.protocol;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.util.MockHttpServletRequest;
import com.telenav.cserver.util.helper.DataSource;
import com.telenav.cserver.util.helper.GenericTest;
import com.telenav.cserver.util.helper.Log2TxNode;
import com.telenav.cserver.util.helper.TxNode2Request;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;

public class I18NWeatherRequestParserTest {

	private DataSource dataSource = new DataSource();
	private I18NWeatherRequestParser instance = new I18NWeatherRequestParser();

	@Before
	public void setUp() throws Exception {

		String TxNodePath = Log2TxNode.getTxNodePath();
		// step 1, get TxNode from log
		TxNode node = new TxNode();
		ArrayList<TxNode> nodeArray = Log2TxNode.getInstance().getTxNode("Weather.do", TxNodePath);
		if(nodeArray != null && nodeArray.size() > 0){
			node = nodeArray.get(0);
		}
		
		// step 2, get the ajax part from TxNode
		TxNode ajaxBody = null;
		TxNode itrNode = node.childAt(0);
		if(itrNode.childAt(0) != null){
			if(itrNode.childAt(0).valueAt(0) == 110){
				ajaxBody = itrNode.childAt(0).childAt(0);
			}
		}

		// set Datahandler using TxNode 
		MockHttpServletRequest request = new MockHttpServletRequest(TxNode.toByteArray(ajaxBody));
		TxNode2Request.getInstance(node.childAt(0)).toMockHttpServletRequest(request);
		DataHandler handler = new DataHandler(request, true);
		request.setAttribute(BrowserFrameworkConstants.CLIENT_INFO, handler);

		
		dataSource.addData(String.class.getName(), "I18NWeather");		
		dataSource.addData(DataHandler.class.getName(), handler);
		dataSource.addData(HttpServletRequest.class.getName(), request);
	}
	
	@After
	public void tearDown() throws Exception {
		// clear the data after testing	
		dataSource.clear();
	}

	@Test
	public void testI18NWeatherRequestParser(){		
		GenericTest.getInstance().startTest(instance, dataSource);
	}

}
