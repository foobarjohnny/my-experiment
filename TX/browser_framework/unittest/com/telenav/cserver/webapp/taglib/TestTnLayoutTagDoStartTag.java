package com.telenav.cserver.webapp.taglib;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.struts.mock.MockHttpServletRequest;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.telenav.tnbrowser.util.DataHandler;

import edu.emory.mathcs.backport.java.util.Arrays;

@RunWith(value = Parameterized.class)
public class TestTnLayoutTagDoStartTag{
	
	private String expected;
	private DataHandler handler;
	private String servletPath;
	private String contentPath;
	private StringBuffer requestURL;
	private static final int TEST_CASE_COUNT = 9;
	private static final int PARAMETER_COUNT = 5;
	private static HttpServletRequest request = new MockHttpServletRequest();

//	@BeforeClass
//	public static void beforeClass()
//	{
//		
//	}
//	
//	@AfterClass
//	public static void afterClass()
//	{
//		
//	}
	
	public TestTnLayoutTagDoStartTag(String expected, DataHandler handler, String servletPath, String contentPath, StringBuffer requestURL)
	{
		this.expected = expected;
		this.handler = handler;
		this.servletPath = servletPath;
		this.contentPath = contentPath;
		this.requestURL = requestURL;
	}
	
	@SuppressWarnings("unchecked")
	@Parameters
	public static Collection<Object[]> prepareData() 
	{
		DataHandler handler[] = new DataHandler[TEST_CASE_COUNT];
		String[] servletPath = new String[TEST_CASE_COUNT];
		String[] contentPath = new String[TEST_CASE_COUNT];
		StringBuffer[] requestURL = new StringBuffer[TEST_CASE_COUNT];
		String[] expected = new String[TEST_CASE_COUNT];
		
		//Case 1 Test when BrowserCommon doesn't exist in both Common and Device folder
		Hashtable<String,String> clientInfo = new Hashtable<String,String>();
		clientInfo.put(DataHandler.KEY_CARRIER, "ATT");
		clientInfo.put(DataHandler.KEY_PLATFORM, "ANDROID");
		clientInfo.put(DataHandler.KEY_VERSION, "6.2.01");
		clientInfo.put(DataHandler.KEY_PRODUCTTYPE, "ATT_NAV");
		clientInfo.put(DataHandler.KEY_DEVICEMODEL, "genericTest");
		clientInfo.put(DataHandler.KEY_WIDTH, "480");
		clientInfo.put(DataHandler.KEY_HEIGHT, "800");
		clientInfo.put(DataHandler.KEY_LOCALE, "en_US");
		clientInfo.put(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_WIDTH, "480-800");
		clientInfo.put(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_HEIGHT, "800-480");
		handler[0] = new DataHandler(request);
		handler[0].setClientInfo(clientInfo);		
		servletPath[0] = "/touch62/jsp/PoiList.jsp";
		contentPath[0] = "/poi_service";
		requestURL[0] = new StringBuffer("http://localhost:8080/poi_service/touch62/jsp/PoiList.jsp");
		expected[0] = new String("<layout height=\"800\" width=\"480\">\n");
		expected[0] += "<uicontrol id=\"oneBoxBgImg\">\n";
		expected[0] += "<uiattribute key=\"x\">0</uiattribute>\n";
		expected[0] += "</uicontrol>\n";
		expected[0] += "</layout>\n";
		expected[0] += "<layout height=\"480\" width=\"800\">\n";
		expected[0] += "<uicontrol id=\"oneBoxBgImg\">\n";
		expected[0] += "<uiattribute key=\"x\">0</uiattribute>\n";
		expected[0] += "</uicontrol>\n";
		expected[0] += "</layout>\n";
		
		//Case 2 Test when BrowserCommon exists in Common folder
		Hashtable<String,String> clientInfo_480x854 = new Hashtable<String,String>();
		clientInfo_480x854.put(DataHandler.KEY_CARRIER, "ATT");
		clientInfo_480x854.put(DataHandler.KEY_PLATFORM, "ANDROID");
		clientInfo_480x854.put(DataHandler.KEY_VERSION, "6.2.01");
		clientInfo_480x854.put(DataHandler.KEY_PRODUCTTYPE, "ATT_NAV");
		clientInfo_480x854.put(DataHandler.KEY_DEVICEMODEL, "genericTest");
		clientInfo_480x854.put(DataHandler.KEY_WIDTH, "480");
		clientInfo_480x854.put(DataHandler.KEY_HEIGHT, "854");
		clientInfo_480x854.put(DataHandler.KEY_LOCALE, "en_US");
		clientInfo_480x854.put(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_WIDTH, "854-480");
		clientInfo_480x854.put(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_HEIGHT, "480-854");
		handler[1] = new DataHandler(request);
		handler[1].setClientInfo(clientInfo_480x854);		
		servletPath[1] = "/touch62/jsp/PoiList.jsp";
		contentPath[1] = "/poi_service";
		requestURL[1] = new StringBuffer("http://localhost:8080/poi_service/touch62/jsp/PoiList.jsp");
		expected[1] = new String("<layout height=\"854\" width=\"480\">\n");
		expected[1] += "<uicontrol id=\"oneBoxBgImg\">\n";
		expected[1] += "<uiattribute key=\"x\">0</uiattribute>\n";
		expected[1] += "</uicontrol>\n";
		expected[1] += "<uicontrol id=\"title\">\n";
		expected[1] += "<uiattribute key=\"x\">0</uiattribute>\n";
		expected[1] += "</uicontrol>\n";
		expected[1] += "</layout>\n";
		expected[1] += "<layout height=\"480\" width=\"854\">\n";
		expected[1] += "<uicontrol id=\"oneBoxBgImg\">\n";
		expected[1] += "<uiattribute key=\"x\">0</uiattribute>\n";
		expected[1] += "</uicontrol>\n";
		expected[1] += "<uicontrol id=\"title\">\n";
		expected[1] += "<uiattribute key=\"x\">0</uiattribute>\n";
		expected[1] += "</uicontrol>\n";
		expected[1] += "</layout>\n";
		
		//Case 3 Test when BrowserCommon exists in device folder
		Hashtable<String,String> clientInfo_640x480 = new Hashtable<String,String>();
		clientInfo_640x480.put(DataHandler.KEY_CARRIER, "ATT");
		clientInfo_640x480.put(DataHandler.KEY_PLATFORM, "ANDROID");
		clientInfo_640x480.put(DataHandler.KEY_VERSION, "6.2.01");
		clientInfo_640x480.put(DataHandler.KEY_PRODUCTTYPE, "ATT_NAV");
		clientInfo_640x480.put(DataHandler.KEY_DEVICEMODEL, "genericTest");
		clientInfo_640x480.put(DataHandler.KEY_WIDTH, "480");
		clientInfo_640x480.put(DataHandler.KEY_HEIGHT, "640");
		clientInfo_640x480.put(DataHandler.KEY_LOCALE, "en_US");
		clientInfo_640x480.put(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_WIDTH, "640-480");
		clientInfo_640x480.put(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_HEIGHT, "480-640");
		handler[2] = new DataHandler(request);
		handler[2].setClientInfo(clientInfo_640x480);		
		servletPath[2] = "/touch62/jsp/PoiList.jsp";
		contentPath[2] = "/poi_service";
		requestURL[2] = new StringBuffer("http://localhost:8080/poi_service/touch62/jsp/PoiList.jsp");
		expected[2] = new String("<layout height=\"640\" width=\"480\">\n");
		expected[2] += "<uicontrol id=\"oneBoxBgImg\">\n";
		expected[2] += "<uiattribute key=\"x\">0</uiattribute>\n";
		expected[2] += "</uicontrol>\n";
		expected[2] += "<uicontrol id=\"title\">\n";
		expected[2] += "<uiattribute key=\"x\">1</uiattribute>\n";
		expected[2] += "</uicontrol>\n";
		expected[2] += "</layout>\n";
		expected[2] += "<layout height=\"480\" width=\"640\">\n";
		expected[2] += "<uicontrol id=\"oneBoxBgImg\">\n";
		expected[2] += "<uiattribute key=\"x\">0</uiattribute>\n";
		expected[2] += "</uicontrol>\n";
		expected[2] += "<uicontrol id=\"title\">\n";
		expected[2] += "<uiattribute key=\"x\">1</uiattribute>\n";
		expected[2] += "</uicontrol>\n";
		expected[2] += "</layout>\n";
		
		
		//Case 4 Test when BrowserCommon exists in both common and device folder
		Hashtable<String,String> clientInfo_320x480 = new Hashtable<String,String>();
		clientInfo_320x480.put(DataHandler.KEY_CARRIER, "ATT");
		clientInfo_320x480.put(DataHandler.KEY_PLATFORM, "ANDROID");
		clientInfo_320x480.put(DataHandler.KEY_VERSION, "6.2.01");
		clientInfo_320x480.put(DataHandler.KEY_PRODUCTTYPE, "ATT_NAV");
		clientInfo_320x480.put(DataHandler.KEY_DEVICEMODEL, "genericTest");
		clientInfo_320x480.put(DataHandler.KEY_WIDTH, "320");
		clientInfo_320x480.put(DataHandler.KEY_HEIGHT, "480");
		clientInfo_320x480.put(DataHandler.KEY_LOCALE, "en_US");
		clientInfo_320x480.put(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_WIDTH, "480-320");
		clientInfo_320x480.put(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_HEIGHT, "320-480");
		handler[3] = new DataHandler(request);
		handler[3].setClientInfo(clientInfo_320x480);		
		servletPath[3] = "/touch62/jsp/PoiList.jsp";
		contentPath[3] = "/poi_service";
		requestURL[3] = new StringBuffer("http://localhost:8080/poi_service/touch62/jsp/PoiList.jsp");
		expected[3] = new String("<layout height=\"480\" width=\"320\">\n");
		expected[3] += "<uicontrol id=\"oneBoxBgImg\">\n";
		expected[3] += "<uiattribute key=\"x\">0</uiattribute>\n";
		expected[3] += "</uicontrol>\n";
		expected[3] += "<uicontrol id=\"title\">\n";
		expected[3] += "<uiattribute key=\"x\">3</uiattribute>\n";
		expected[3] += "</uicontrol>\n";
		expected[3] += "</layout>\n";
		expected[3] += "<layout height=\"320\" width=\"480\">\n";
		expected[3] += "<uicontrol id=\"oneBoxBgImg\">\n";
		expected[3] += "<uiattribute key=\"x\">0</uiattribute>\n";
		expected[3] += "</uicontrol>\n";
		expected[3] += "<uicontrol id=\"title\">\n";
		expected[3] += "<uiattribute key=\"x\">3</uiattribute>\n";
		expected[3] += "</uicontrol>\n";
		expected[3] += "</layout>\n";
		
		//Case 5 Test when layout properties file exists in Common folder
		handler[4] = new DataHandler(request);
		handler[4].setClientInfo(clientInfo_640x480);		
		servletPath[4] = "/touch62/jsp/PoiDetail.jsp";
		contentPath[4] = "/poi_service";
		requestURL[4] = new StringBuffer("http://localhost:8080/poi_service/touch62/jsp/PoDetail.jsp");
		expected[4] = new String("<layout height=\"640\" width=\"480\">\n");
		expected[4] += "<uicontrol id=\"oneBoxBgImg\">\n";
		expected[4] += "<uiattribute key=\"y\">30</uiattribute>\n";
		expected[4] += "</uicontrol>\n";
		expected[4] += "<uicontrol id=\"title\">\n";
		expected[4] += "<uiattribute key=\"x\">1</uiattribute>\n";
		expected[4] += "</uicontrol>\n";
		expected[4] += "</layout>\n";
		expected[4] += "<layout height=\"480\" width=\"640\">\n";
		expected[4] += "<uicontrol id=\"oneBoxBgImg\">\n";
		expected[4] += "<uiattribute key=\"y\">30</uiattribute>\n";
		expected[4] += "</uicontrol>\n";
		expected[4] += "<uicontrol id=\"title\">\n";
		expected[4] += "<uiattribute key=\"x\">1</uiattribute>\n";
		expected[4] += "</uicontrol>\n";
		expected[4] += "</layout>\n";
		
		//Case 6 Test when layout properties file exists in device folder
		handler[5] = new DataHandler(request);
		handler[5].setClientInfo(clientInfo_640x480);		
		servletPath[5] = "/touch62/jsp/StartUp.jsp";
		contentPath[5] = "/poi_service";
		requestURL[5] = new StringBuffer("http://localhost:8080/poi_service/touch62/jsp/StartUp.jsp");
		expected[5] = new String("<layout height=\"640\" width=\"480\">\n");
		expected[5] += "<uicontrol id=\"oneBoxBgImg\">\n";
		expected[5] += "<uiattribute key=\"x\">11</uiattribute>\n";
		expected[5] += "</uicontrol>\n";
		expected[5] += "<uicontrol id=\"title\">\n";
		expected[5] += "<uiattribute key=\"x\">1</uiattribute>\n";
		expected[5] += "</uicontrol>\n";
		expected[5] += "</layout>\n";
		expected[5] += "<layout height=\"480\" width=\"640\">\n";
		expected[5] += "<uicontrol id=\"oneBoxBgImg\">\n";
		expected[5] += "<uiattribute key=\"x\">11</uiattribute>\n";
		expected[5] += "</uicontrol>\n";
		expected[5] += "<uicontrol id=\"title\">\n";
		expected[5] += "<uiattribute key=\"x\">1</uiattribute>\n";
		expected[5] += "</uicontrol>\n";
		expected[5] += "</layout>\n";
		
		//Case 7 Test when layout properties file exists in both Common and device folder
		handler[6] = new DataHandler(request);
		handler[6].setClientInfo(clientInfo_640x480);		
		servletPath[6] = "/touch62/jsp/SearchPoi.jsp";
		contentPath[6] = "/poi_service";
		requestURL[6] = new StringBuffer("http://localhost:8080/poi_service/touch62/jsp/SearchPoi.jsp");
		expected[6] = new String("<layout height=\"640\" width=\"480\">\n");
		expected[6] += "<uicontrol id=\"oneBoxBgImg\">\n";
		expected[6] += "<uiattribute key=\"y\">18</uiattribute>\n";
		expected[6] += "</uicontrol>\n";
		expected[6] += "<uicontrol id=\"title\">\n";
		expected[6] += "<uiattribute key=\"x\">1</uiattribute>\n";
		expected[6] += "</uicontrol>\n";
		expected[6] += "</layout>\n";
		expected[6] += "<layout height=\"480\" width=\"640\">\n";
		expected[6] += "<uicontrol id=\"oneBoxBgImg\">\n";
		expected[6] += "<uiattribute key=\"y\">18</uiattribute>\n";
		expected[6] += "</uicontrol>\n";
		expected[6] += "<uicontrol id=\"title\">\n";
		expected[6] += "<uiattribute key=\"x\">1</uiattribute>\n";
		expected[6] += "</uicontrol>\n";
		expected[6] += "</layout>\n";
		
		//Case 8 Test when layout properties file doesn't exist in both Common and device folder
		handler[7] = new DataHandler(request);
		handler[7].setClientInfo(clientInfo_640x480);		
		servletPath[7] = "/touch62/jsp/AboutAbout.jsp";
		contentPath[7] = "/poi_service";
		requestURL[7] = new StringBuffer("http://localhost:8080/poi_service/touch62/jsp/AboutAbout.jsp");
		expected[7] = new String("<layout height=\"640\" width=\"480\">\n");
		expected[7] += "<uicontrol id=\"title\">\n";
		expected[7] += "<uiattribute key=\"x\">1</uiattribute>\n";
		expected[7] += "</uicontrol>\n";
		expected[7] += "</layout>\n";
		expected[7] += "<layout height=\"480\" width=\"640\">\n";
		expected[7] += "<uicontrol id=\"title\">\n";
		expected[7] += "<uiattribute key=\"x\">1</uiattribute>\n";
		expected[7] += "</uicontrol>\n";
		expected[7] += "</layout>\n";
		
		//Case 9 Test when layout properties file doesn't exist in both Common and device folder
		handler[8] = new DataHandler(request);
		handler[8].setClientInfo(clientInfo_640x480);		
		servletPath[8] = "/touch62/jsp/about/AboutAbout.jsp";
		contentPath[8] = "/poi_service";
		requestURL[8] = new StringBuffer("http://localhost:8080/poi_service/touch62/jsp/about/AboutAbout.jsp");
		expected[8] = new String("<layout height=\"640\" width=\"480\">\n");
		expected[8] += "<uicontrol id=\"title\">\n";
		expected[8] += "<uiattribute key=\"x\">10</uiattribute>\n";
		expected[8] += "</uicontrol>\n";
		expected[8] += "</layout>\n";
		expected[8] += "<layout height=\"480\" width=\"640\">\n";
		expected[8] += "<uicontrol id=\"title\">\n";
		expected[8] += "<uiattribute key=\"x\">10</uiattribute>\n";
		expected[8] += "</uicontrol>\n";
		expected[8] += "</layout>\n";
		
		Object[][] object = new Object[TEST_CASE_COUNT][PARAMETER_COUNT];
		
		
		
		for(int i = 0; i < TEST_CASE_COUNT; i ++)
		{
			object[i] = new Object[]{expected[i],handler[i], servletPath[i], contentPath[i], requestURL[i]};
		}
		return Arrays.asList(object);
	}
	
	@Test
	public void testDoStartTag() throws JspException
	{
		TnLayoutTag tlt = new TnLayoutTag();
		IMocksControl control = EasyMock.createControl();
		HttpServletRequest request = control.createMock(HttpServletRequest.class);

        EasyMock.expect(request.getAttribute("CLIENT_INFO")).andReturn(handler).anyTimes();
        EasyMock.expect(request.getServletPath()).andReturn(servletPath).anyTimes();
        EasyMock.expect(request.getContextPath()).andReturn(contentPath).anyTimes();
        EasyMock.expect(request.getRequestURL()).andReturn(requestURL);
    	EasyMock.replay(request);
    	control = EasyMock.createControl();
		javax.servlet.jsp.PageContext pageContext = control.createMock(javax.servlet.jsp.PageContext.class);
		EasyMock.expect(pageContext.getRequest()).andReturn(request).anyTimes();
		
		control = EasyMock.createControl();
		JspWriter jspwriter = control.createMock(JspWriter.class);
		EasyMock.expect(pageContext.getOut()).andReturn(jspwriter);
		EasyMock.replay(pageContext);
		tlt.setPageContext(pageContext);
		tlt.doStartTag();
		
		assertEquals(this.expected, tlt.outputText.toString());
	}
	
}
