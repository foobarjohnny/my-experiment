package com.telenav.cserver.html.protocol;

import org.easymock.EasyMock;
import org.powermock.api.easymock.PowerMock;

import com.telenav.cserver.TestRequestParser;
import com.telenav.cserver.TestUtil;
import com.telenav.cserver.framework.html.datatype.HtmlClientInfo;
import com.telenav.cserver.framework.html.util.HtmlFeatureHelper;
import com.telenav.cserver.framework.html.util.HtmlFrameworkConstants;
import com.telenav.cserver.html.util.HtmlPoiUtil;
import com.telenav.cserver.poi.struts.util.PoiUtil;
import com.telenav.cserver.weather.executor.I18NWeatherRequest;
import com.telenav.j2me.datatypes.Stop;

public class TestHtmlWeatherRequestParser extends TestRequestParser {
	public void testParseBrowserRequest(){
		HtmlClientInfo clientInfo = TestUtil.getHtmlClientInfo(); 
		EasyMock.expect((HtmlClientInfo)httpRequest.getAttribute(HtmlFrameworkConstants.HTML_CLIENT_INFO)).andReturn(clientInfo).anyTimes();
		EasyMock.expect(httpRequest.getParameter("addressString")).andReturn(TestUtil.getLocationJSONObject().toString());
		HtmlWeatherRequestParser requestParser = new HtmlWeatherRequestParser();
		PowerMock.replayAll();
		try {
			I18NWeatherRequest weatherRequest = (I18NWeatherRequest) requestParser.parseBrowserRequest(httpRequest);
			PowerMock.verifyAll();
			assertEquals("The two Object should be same.",getI18NWeatherRequest().toString(), weatherRequest.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private I18NWeatherRequest getI18NWeatherRequest(){
		I18NWeatherRequest request = new I18NWeatherRequest();
		Stop address = PoiUtil.convertAddress(TestUtil.getLocationJSONObject());
        request.setLocation(address);
        request.setCanadianCarrier(false);
        request.setLocale(TestUtil.getHtmlClientInfo().getLocale());
        
        return request;
	}
}
