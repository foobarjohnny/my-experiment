package com.telenav.cserver.webapp.taglib;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.struts.mock.MockHttpServletRequest;
import org.apache.struts.mock.MockHttpServletResponse;

import junit.framework.TestCase;
import com.telenav.cserver.webapp.taglib.TnLayoutTag;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.tnbrowser.util.DataHandler;

import org.easymock.*;

public class TestTnLayoutTag extends TestCase {
	
	TnLayoutTag tlt = new TnLayoutTag();
    private TnContext tnContext;
    private DataHandler handler;
    HttpServletRequest request = new MockHttpServletRequest();
    HttpServletResponse response = new MockHttpServletResponse();
    
	private Map<String, String> messages;
	IMocksControl control;
    
    protected void setUp() throws Exception
    {
        tnContext = new TnContext();
        tnContext.addProperty(TnContext.PROP_CARRIER, "ATT");
        tnContext.addProperty(TnContext.PROP_DEVICE, "genericTest");
        tnContext.addProperty(TnContext.PROP_PRODUCT, "ANDROID");
        tnContext.addProperty(TnContext.PROP_VERSION, "6.2.01");
        tnContext.addProperty("application", "ATT_NAV");
        tnContext.addProperty("login", "3817799999");
        tnContext.addProperty("userid", "3707312");
        
		messages = new HashMap<String, String>();
		messages.put("common.driveto", "drive to");
		
    	handler = new DataHandler(request);
    	
        Hashtable<String, String> clientInfo = new Hashtable<String, String>();
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
        handler.setClientInfo(clientInfo);
    	
    	request.setAttribute("CLIENT_INFO", handler);
	}
    
    public void setMultipleScreen()
    {

        Hashtable clientInfo = new Hashtable();
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
        handler.setClientInfo(clientInfo);
    }
    
    public void setSingleScreen()
    {
        Hashtable clientInfo = new Hashtable();
        clientInfo.put(DataHandler.KEY_CARRIER, "ATT");
        clientInfo.put(DataHandler.KEY_PLATFORM, "ANDROID");
        clientInfo.put(DataHandler.KEY_VERSION, "6.2.01");
        clientInfo.put(DataHandler.KEY_PRODUCTTYPE, "ATT_NAV");
        clientInfo.put(DataHandler.KEY_DEVICEMODEL, "genericTest");
        
        clientInfo.put(DataHandler.KEY_WIDTH, "480");
        clientInfo.put(DataHandler.KEY_HEIGHT, "800");
        clientInfo.put(DataHandler.KEY_LOCALE, "en_US");
        clientInfo.put(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_WIDTH, "480");
        clientInfo.put(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_HEIGHT, "800");
        
        handler.setClientInfo(clientInfo);
    }
    
    public void setSupportWidth()
    {
        Hashtable clientInfo = new Hashtable();
        clientInfo.put(DataHandler.KEY_CARRIER, "ATT");
        clientInfo.put(DataHandler.KEY_PLATFORM, "ANDROID");
        clientInfo.put(DataHandler.KEY_VERSION, "6.2.01");
        clientInfo.put(DataHandler.KEY_PRODUCTTYPE, "ATT_NAV");
        clientInfo.put(DataHandler.KEY_DEVICEMODEL, "genericTest");
        
        clientInfo.put(DataHandler.KEY_WIDTH, "480");
        clientInfo.put(DataHandler.KEY_HEIGHT, "800");
        clientInfo.put(DataHandler.KEY_LOCALE, "en_US");
        clientInfo.put(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_WIDTH, "");
        clientInfo.put(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_HEIGHT, "800");
        
        handler.setClientInfo(clientInfo);
    }
	
	public void testGetImageCommonUrl()
	{
		String screenLevelImageUrl = "http://prompts.mypna.com/vnfPrompts/icons/poi_service/VMC/RIM/6_4_01/TN/480x360_360x480/360x480/image/banner_0.png";
		tlt.setScreenLevelImageUrl(screenLevelImageUrl);
		assertEquals(tlt.getScreenLevelImageUrl(), screenLevelImageUrl);
	}
	
	public void testGetSpecificLayoutWhenFileIsCommon() throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException, SecurityException, NoSuchMethodException, InvocationTargetException
	{
		String filePath = "device.Common.RIM.6_4_01.TN.320x240.320x240";
		String layoutFile = "StartUpForSN";
		Method setLocale = getMethod(tlt.getClass(), "setCurrentLocale", String.class);
		setLocale.invoke(tlt, "en_IN");
		boolean isScreenLevelImage;
		isScreenLevelImage = true;
		tlt.getSpecificLayout(filePath,layoutFile,isScreenLevelImage);
		Map<String,String> tempMap = (Map<String,String>)getValue(tlt, "tempCacheForLayouts");
		assertTrue(!tempMap.isEmpty());
	}
	
	public void testGetSpecificLayoutWhenFileIsBrowserCommon() throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException, SecurityException, NoSuchMethodException, InvocationTargetException
	{
		String filePath = "device.Common.RIM.6_4_01.TN.320x240.320x240";
		String layoutFile = "BrowserCommon1";
		Method setLocale = getMethod(tlt.getClass(), "setCurrentLocale", String.class);
		setLocale.invoke(tlt, "en_IN");
		boolean isScreenLevelImage;
		isScreenLevelImage = true;
		tlt.getSpecificLayout(filePath,layoutFile,isScreenLevelImage);
		Map<String,String> tempMap = (Map<String,String>)getValue(tlt, "tempCacheForLayouts");
		assertTrue(tempMap.isEmpty());
	}
	
	public void testGetSpecificLayoutWhenFileNonExisted() throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException, SecurityException, NoSuchMethodException, InvocationTargetException
	{
		String filePath = "device.Common.RIM.6_4_01.TN.320x240.320x240";
		String layoutFile = "StartUpForSN1";
		Method setLocale = getMethod(tlt.getClass(), "setCurrentLocale", String.class);
		setLocale.invoke(tlt, "en_IN");
		boolean isScreenLevelImage;
		isScreenLevelImage = true;
		tlt.getSpecificLayout(filePath,layoutFile,isScreenLevelImage);
		Map<String,String> tempMap = (Map<String,String>)getValue(tlt, "tempCacheForLayouts");
		assertTrue(tempMap.isEmpty());
	}
	
	public void testGetSpecificLayoutWhenLocaleIsCorrect() throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException, SecurityException, NoSuchMethodException, InvocationTargetException
	{
		String filePath = "device.MMI.RIM.6_4_01.TN.320x240.320x240";
		String layoutFile = "StartUpForSN";
		Method setLocale = getMethod(tlt.getClass(), "setCurrentLocale", String.class);
		setLocale.invoke(tlt, "en_IN");
		boolean isScreenLevelImage;
		isScreenLevelImage = false;
		tlt.getSpecificLayout(filePath,layoutFile,isScreenLevelImage);
		Map<String,String> tempMap = (Map<String,String>)getValue(tlt, "tempCacheForLayouts");
		assertTrue(!tempMap.isEmpty());
	}
	
	public void testGetSpecificLayoutWhenLocaleIsInCorrect() throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException, SecurityException, NoSuchMethodException, InvocationTargetException
	{
		String filePath = "device.MMI.RIM.6_4_01.TN.320x240.320x240";
		String layoutFile = "StartUpForSN";
		Method setLocale = getMethod(tlt.getClass(), "setCurrentLocale", String.class);
		setLocale.invoke(tlt, "en_US");
		boolean isScreenLevelImage;
		isScreenLevelImage = false;
		tlt.getSpecificLayout(filePath,layoutFile,isScreenLevelImage);
		Map<String,String> tempMap = (Map<String,String>)getValue(tlt, "tempCacheForLayouts");
		assertTrue(!tempMap.isEmpty());
	}
	
	public void testGetSpecificLayoutWhenPathIsInCorrect() throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException, SecurityException, NoSuchMethodException, InvocationTargetException
	{
		String filePath = "device.MMI.RIM.6_4_01.TN.320x240.320x2401";
		String layoutFile = "StartUpForSN";
		Method setLocale = getMethod(tlt.getClass(), "setCurrentLocale", String.class);
		setLocale.invoke(tlt, "en_US");
		boolean isScreenLevelImage;
		isScreenLevelImage = false;
		tlt.getSpecificLayout(filePath,layoutFile,isScreenLevelImage);
		Map<String,String> tempMap = (Map<String,String>)getValue(tlt, "tempCacheForLayouts");
		assertTrue(tempMap.isEmpty());
	}
	
	public void testSetImageUrl() throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException
	{
		String strURL = "http://prompts.mypna.com/vnfPrompts/icons/poi_service/VMC/RIM/6_4_01/TN/480x360_360x480/360x480/image/banner_0.png";
		
		tlt.setImageUrl(strURL);
		assertEquals(strURL, getValue(tlt,"imageUrl").toString());
	}
	
	public void testGetImageUrl() throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException
	{
		String strURL = "http://prompts.mypna.com/vnfPrompts/icons/poi_service/VMC/RIM/6_4_01/TN/480x360_360x480/360x480/image/banner_0.png";
		
		tlt.setImageUrl(strURL);
		assertEquals(getValue(tlt,"imageUrl").toString(), tlt.getImageUrl());
	}
	
	public void testSetSupportLocal() throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException
	{
		String locale = "en_US";
		tlt.setSupportLocal(locale);
		assertEquals(locale, getValue(tlt,"supportLocal").toString());
	}
	
	public void testGetSupportLocal() throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException
	{
		String locale = "en_CA";
		tlt.setSupportLocal(locale);
		assertEquals(getValue(tlt,"supportLocal").toString(), tlt.getSupportLocal());
	}
	
	public void testSetCurrentLocale() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchFieldException
	{
		String locale = "es_MX";
		Method setLocale = getMethod(tlt.getClass(), "setCurrentLocale", String.class);
		setLocale.invoke(tlt, locale);
		assertEquals(locale, getValue(tlt,"locale"));
	}
	
	public void testGetCurrentLocale() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		String locale = "es_MX";
		Method setLocale = getMethod(tlt.getClass(), "setCurrentLocale", String.class);
		setLocale.invoke(tlt, locale);
		Method getLocale = getMethod(tlt.getClass(), "getCurrentLocale", null);
		String[] localeArray = locale.split("_");
		assertEquals(localeArray[0],((String[])getLocale.invoke(tlt))[0]);
		assertEquals(localeArray[1],((String[])getLocale.invoke(tlt))[1]);
	}
	
	public void testGetCurrentLocaleWhenLocaleisIncorrect() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		String locale = "en_";
		Method setLocale = getMethod(tlt.getClass(), "setCurrentLocale", String.class);
		setLocale.invoke(tlt, locale);
		Method getLocale = getMethod(tlt.getClass(), "getCurrentLocale", null);
		String[] localeArray = "en_US".split("_");
		assertEquals(localeArray[0],((String[])getLocale.invoke(tlt))[0]);
		assertEquals(localeArray[1],((String[])getLocale.invoke(tlt))[1]);
	}
	
	
	public void testGetScreenKey() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		Method getScreenKey = getMethod(tlt.getClass(), "getScreenKey", DataHandler.class);
		setMultipleScreen();
		String[] screenKey = (String[])(getScreenKey.invoke(tlt, handler));
		assertEquals("480x800", screenKey[0]);
		assertEquals("800x480", screenKey[1]);
	}
	
	public void testGetSingleScreenKey() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		Method getScreenKey = getMethod(tlt.getClass(), "getScreenKey", DataHandler.class);
		setSingleScreen();
		String[] screenKey = (String[])(getScreenKey.invoke(tlt, handler));
		assertEquals("480x800", screenKey[0]);
	}
	
	public void testGetScreenKeyWidthNull() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		Method getScreenKey = getMethod(tlt.getClass(), "getScreenKey", DataHandler.class);
		setSingleScreen();
		String[] screenKey = (String[])(getScreenKey.invoke(tlt, handler));
		assertEquals("480x800", screenKey[0]);
	}
	
	public void testDoStartTag() throws JspException
	{
		control = EasyMock.createControl();
    	//EasyMock.expect(this.request.getServletPath()).andReturn("/touch64/jsp/mmi/InputUserInfo.jsp").anyTimes();
		
		HttpServletRequest request = control.createMock(HttpServletRequest.class);
        Hashtable<String, String> clientInfo = new Hashtable<String, String>();
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
        handler.setClientInfo(clientInfo);
    	
        EasyMock.expect(request.getAttribute("CLIENT_INFO")).andReturn(handler).anyTimes();
        EasyMock.expect(request.getServletPath()).andReturn("/touch62/jsp/PoiList.jsp").anyTimes();
        EasyMock.expect(request.getContextPath()).andReturn("/login_startup_service").anyTimes();
        StringBuffer strB = new StringBuffer("http://localhost:8080/poi_service/touch62/jsp/PoiList.jsp");
        EasyMock.expect(request.getRequestURL()).andReturn(strB);
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
		String expected = new String("<layout height=\"800\" width=\"480\">\n");
		expected += "<uicontrol id=\"oneBoxBgImg\">\n";
		expected += "<uiattribute key=\"x\">0</uiattribute>\n";
		expected += "</uicontrol>\n";
		expected += "</layout>\n";
		expected += "<layout height=\"480\" width=\"800\">\n";
		expected += "<uicontrol id=\"oneBoxBgImg\">\n";
		expected += "<uiattribute key=\"x\">0</uiattribute>\n";
		expected += "</uicontrol>\n";
		expected += "</layout>\n";
		
		assertEquals(expected, tlt.outputText.toString());
	}
	
	public void testDoStartTagWithInclude() throws JspException
	{
		control = EasyMock.createControl();
    	//EasyMock.expect(this.request.getServletPath()).andReturn("/touch64/jsp/mmi/InputUserInfo.jsp").anyTimes();
		
		HttpServletRequest request = control.createMock(HttpServletRequest.class);
        Hashtable<String, String> clientInfo = new Hashtable<String, String>();
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
        handler.setClientInfo(clientInfo);
    	
        EasyMock.expect(request.getAttribute("CLIENT_INFO")).andReturn(handler).anyTimes();
        EasyMock.expect(request.getServletPath()).andReturn("/touch62/jsp/PoiDetail.jsp").anyTimes();
        EasyMock.expect(request.getContextPath()).andReturn("/poi_service").anyTimes();
        StringBuffer strB = new StringBuffer("http://localhost:8080/poi_service/touch62/jsp/PoiDetail.jsp");
        EasyMock.expect(request.getRequestURL()).andReturn(strB);
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
		String expected = new String("<layout height=\"800\" width=\"480\">\n");
		expected += "<uicontrol id=\"oneBoxBgImg\">\n";
		expected += "<uiattribute key=\"x\">0</uiattribute>\n";
		expected += "<uiattribute key=\"y\">30</uiattribute>\n";
		expected += "</uicontrol>\n";
		expected += "</layout>\n";
		expected += "<layout height=\"480\" width=\"800\">\n";
		expected += "<uicontrol id=\"oneBoxBgImg\">\n";
		expected += "<uiattribute key=\"x\">0</uiattribute>\n";
		expected += "<uiattribute key=\"y\">30</uiattribute>\n";
		expected += "</uicontrol>\n";
		expected += "</layout>\n";
		
		assertEquals(expected, tlt.outputText.toString());
	}
	
	public void testDoStartWithSubLayout() throws JspException
	{
		control = EasyMock.createControl();
    	//EasyMock.expect(this.request.getServletPath()).andReturn("/touch64/jsp/mmi/InputUserInfo.jsp").anyTimes();
		
		HttpServletRequest request = control.createMock(HttpServletRequest.class);
		
        Hashtable<String, String> clientInfo = new Hashtable<String, String>();
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
        handler.setClientInfo(clientInfo);
    	
        EasyMock.expect(request.getAttribute("CLIENT_INFO")).andReturn(handler).anyTimes();
        EasyMock.expect(request.getServletPath()).andReturn("/touch62/jsp/about/AboutAbout.jsp").anyTimes();
        EasyMock.expect(request.getContextPath()).andReturn("/poi_service").anyTimes();
        StringBuffer strB = new StringBuffer("http://localhost:8080/poi_service/touch62/jsp/about/AboutAbout.jsp");
        EasyMock.expect(request.getRequestURL()).andReturn(strB);
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
		String expected = new String("<layout height=\"800\" width=\"480\">\n");
		expected += "<uicontrol id=\"oneBoxBgImg\">\n";
		expected += "<uiattribute key=\"x\">0</uiattribute>\n";
		expected += "</uicontrol>\n";
		expected += "</layout>\n";
		expected += "<layout height=\"480\" width=\"800\">\n";
		expected += "<uicontrol id=\"oneBoxBgImg\">\n";
		expected += "<uiattribute key=\"x\">0</uiattribute>\n";
		expected += "</uicontrol>\n";
		expected += "</layout>\n";
		
		assertEquals(expected, tlt.outputText.toString());
	}
	
	
//	
//	public void testDoStartWithSubLayoutError() throws JspException
//	{
//		IMocksControl control = EasyMock.createControl();
//    	//EasyMock.expect(this.request.getServletPath()).andReturn("/touch64/jsp/mmi/InputUserInfo.jsp").anyTimes();
//		
//		HttpServletRequest request = control.createMock(HttpServletRequest.class);
//        Hashtable<String, String> clientInfo = new Hashtable<String, String>();
//        clientInfo.put(DataHandler.KEY_CARRIER, "MMI");
//        clientInfo.put(DataHandler.KEY_PLATFORM, "RIM");
//        clientInfo.put(DataHandler.KEY_VERSION, "6.4.01");
//        clientInfo.put(DataHandler.KEY_PRODUCTTYPE, "TN");
//        clientInfo.put(DataHandler.KEY_DEVICEMODEL, "genericTest");
//        
//        clientInfo.put(DataHandler.KEY_WIDTH, "480");
//        clientInfo.put(DataHandler.KEY_HEIGHT, "320");
//        clientInfo.put(DataHandler.KEY_LOCALE, "en_IN");
//        clientInfo.put(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_WIDTH, "480-320");
//        clientInfo.put(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_HEIGHT, "320-480");   
//        handler.setClientInfo(clientInfo);
//    	
//        EasyMock.expect(request.getAttribute("CLIENT_INFO")).andReturn(handler).anyTimes();
//        EasyMock.expect(request.getServletPath()).andReturn("/touch64/jsp/about/AboutAbout.jsp").anyTimes();
//        EasyMock.expect(request.getContextPath()).andReturn("/login_startup_service").anyTimes();
//        StringBuffer strB = new StringBuffer("http://localhost:8080/poi_service/touch64/jsp/about/AboutAbout.jsp");
//        EasyMock.expect(request.getRequestURL()).andReturn(strB);
//    	EasyMock.replay(request);
//    	
//    	IMocksControl control1 = EasyMock.createControl();
//		javax.servlet.jsp.PageContext pageContext = control1.createMock(javax.servlet.jsp.PageContext.class);
//		EasyMock.expect(pageContext.getRequest()).andReturn(request).anyTimes();
//		
//		IMocksControl control2 = EasyMock.createControl();
//		JspWriter jspwriter = control2.createMock(JspWriter.class);
//		EasyMock.expect(pageContext.getOut()).andReturn(jspwriter);
//		EasyMock.replay(pageContext);
//		tlt.setPageContext(pageContext);
//		tlt.doStartTag();
//		String expected = new String("<layout height=\"480\" width=\"320\">\n");
//		expected += "</layout>\n";
//		expected += "<layout height=\"320\" width=\"480\">\n";
//		expected += "</layout>\n";
//		
//		assertEquals(expected, tlt.outputText.toString());
//	}
//	
//	public void testDoStartWithSubLayoutErrorLoop() throws JspException
//	{
//		IMocksControl control = EasyMock.createControl();
//    	//EasyMock.expect(this.request.getServletPath()).andReturn("/touch64/jsp/mmi/InputUserInfo.jsp").anyTimes();
//		
//		HttpServletRequest request = control.createMock(HttpServletRequest.class);
//        Hashtable<String, String> clientInfo = new Hashtable<String, String>();
//        clientInfo.put(DataHandler.KEY_CARRIER, "MMI");
//        clientInfo.put(DataHandler.KEY_PLATFORM, "RIM");
//        clientInfo.put(DataHandler.KEY_VERSION, "6.4.01");
//        clientInfo.put(DataHandler.KEY_PRODUCTTYPE, "TN");
//        clientInfo.put(DataHandler.KEY_DEVICEMODEL, "genericTest");
//        
//        clientInfo.put(DataHandler.KEY_WIDTH, "480");
//        clientInfo.put(DataHandler.KEY_HEIGHT, "320");
//        clientInfo.put(DataHandler.KEY_LOCALE, "en_US");
//        clientInfo.put(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_WIDTH, "480-320");
//        clientInfo.put(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_HEIGHT, "320-480");   
//        handler.setClientInfo(clientInfo);
//    	
//        EasyMock.expect(request.getAttribute("CLIENT_INFO")).andReturn(handler).anyTimes();
//        EasyMock.expect(request.getServletPath()).andReturn("/touch62/jsp/about/AboutMenu.jsp").anyTimes();
//        EasyMock.expect(request.getContextPath()).andReturn("/poi_service").anyTimes();
//        StringBuffer strB = new StringBuffer("http://localhost:8080/poi_service/touch62/jsp/about/AboutMenu.jsp");
//        EasyMock.expect(request.getRequestURL()).andReturn(strB);
//    	EasyMock.replay(request);
//    	
//    	IMocksControl control1 = EasyMock.createControl();
//		javax.servlet.jsp.PageContext pageContext = control1.createMock(javax.servlet.jsp.PageContext.class);
//		EasyMock.expect(pageContext.getRequest()).andReturn(request).anyTimes();
//		
//		IMocksControl control2 = EasyMock.createControl();
//		JspWriter jspwriter = control2.createMock(JspWriter.class);
//		EasyMock.expect(pageContext.getOut()).andReturn(jspwriter);
//		EasyMock.replay(pageContext);
//		tlt.setPageContext(pageContext);
//		tlt.doStartTag();
//		String expected = new String("<layout height=\"480\" width=\"320\">\n");
//		expected += "<uicontrol id=\"oneBoxBgImg\">\n";
//		expected += "<uiattribute key=\"x\">0</uiattribute>\n";
//		expected += "</uicontrol>\n";
//		expected += "</layout>\n";
//		expected += "<layout height=\"320\" width=\"480\">\n";
//		expected += "<uicontrol id=\"oneBoxBgImg\">\n";
//		expected += "<uiattribute key=\"x\">0</uiattribute>\n";
//		expected += "</uicontrol>\n";
//		expected += "</layout>\n";
//		
//		assertEquals(expected, tlt.outputText.toString());
//	}
//	
//	public void testDoStartWithSubLayoutNestedLoop() throws JspException
//	{
//		IMocksControl control = EasyMock.createControl();
//    	//EasyMock.expect(this.request.getServletPath()).andReturn("/touch64/jsp/mmi/InputUserInfo.jsp").anyTimes();
//		
//		HttpServletRequest request = control.createMock(HttpServletRequest.class);
//        Hashtable<String, String> clientInfo = new Hashtable<String, String>();
//        clientInfo.put(DataHandler.KEY_CARRIER, "MMI");
//        clientInfo.put(DataHandler.KEY_PLATFORM, "RIM");
//        clientInfo.put(DataHandler.KEY_VERSION, "6.4.01");
//        clientInfo.put(DataHandler.KEY_PRODUCTTYPE, "TN");
//        clientInfo.put(DataHandler.KEY_DEVICEMODEL, "genericTest");
//        
//        clientInfo.put(DataHandler.KEY_WIDTH, "480");
//        clientInfo.put(DataHandler.KEY_HEIGHT, "320");
//        clientInfo.put(DataHandler.KEY_LOCALE, "en_US");
//        clientInfo.put(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_WIDTH, "480-320");
//        clientInfo.put(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_HEIGHT, "320-480");   
//        handler.setClientInfo(clientInfo);
//    	
//        EasyMock.expect(request.getAttribute("CLIENT_INFO")).andReturn(handler).anyTimes();
//        EasyMock.expect(request.getServletPath()).andReturn("/touch62/jsp/about/AboutSupport.jsp").anyTimes();
//        EasyMock.expect(request.getContextPath()).andReturn("/poi_service").anyTimes();
//        StringBuffer strB = new StringBuffer("http://localhost:8080/poi_service/touch62/jsp/about/AboutSupport.jsp");
//        EasyMock.expect(request.getRequestURL()).andReturn(strB);
//    	EasyMock.replay(request);
//    	
//    	IMocksControl control1 = EasyMock.createControl();
//		javax.servlet.jsp.PageContext pageContext = control1.createMock(javax.servlet.jsp.PageContext.class);
//		EasyMock.expect(pageContext.getRequest()).andReturn(request).anyTimes();
//		
//		IMocksControl control2 = EasyMock.createControl();
//		JspWriter jspwriter = control2.createMock(JspWriter.class);
//		EasyMock.expect(pageContext.getOut()).andReturn(jspwriter);
//		EasyMock.replay(pageContext);
//		tlt.setPageContext(pageContext);
//		tlt.doStartTag();
//		String expected = new String("<layout height=\"480\" width=\"320\">\n");
//		expected += "<uicontrol id=\"oneBoxBgImg\">\n";
//		expected += "<uiattribute key=\"width\">88</uiattribute>\n";
//		expected += "<uiattribute key=\"x\">0</uiattribute>\n";
//		expected += "</uicontrol>\n";
//		expected += "</layout>\n";
//		expected += "<layout height=\"320\" width=\"480\">\n";
//		expected += "<uicontrol id=\"oneBoxBgImg\">\n";
//		expected += "<uiattribute key=\"width\">88</uiattribute>\n";
//		expected += "<uiattribute key=\"x\">0</uiattribute>\n";
//		expected += "</uicontrol>\n";
//		expected += "</layout>\n";
//		
//		assertEquals(expected, tlt.outputText.toString());
//	}
//	
//	public void testDoStartWithSubLayoutNestedSubLoop() throws JspException
//	{
//		IMocksControl control = EasyMock.createControl();
//    	//EasyMock.expect(this.request.getServletPath()).andReturn("/touch64/jsp/mmi/InputUserInfo.jsp").anyTimes();
//		
//		HttpServletRequest request = control.createMock(HttpServletRequest.class);
//        Hashtable<String, String> clientInfo = new Hashtable<String, String>();
//        clientInfo.put(DataHandler.KEY_CARRIER, "MMI");
//        clientInfo.put(DataHandler.KEY_PLATFORM, "RIM");
//        clientInfo.put(DataHandler.KEY_VERSION, "6.4.01");
//        clientInfo.put(DataHandler.KEY_PRODUCTTYPE, "TN");
//        clientInfo.put(DataHandler.KEY_DEVICEMODEL, "genericTest");
//        
//        clientInfo.put(DataHandler.KEY_WIDTH, "480");
//        clientInfo.put(DataHandler.KEY_HEIGHT, "320");
//        clientInfo.put(DataHandler.KEY_LOCALE, "en_US");
//        clientInfo.put(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_WIDTH, "480-320");
//        clientInfo.put(DataHandler.KEY_CLIENT_SUPPORT_SCREEN_HEIGHT, "320-480");   
//        handler.setClientInfo(clientInfo);
//    	
//        EasyMock.expect(request.getAttribute("CLIENT_INFO")).andReturn(handler).anyTimes();
//        EasyMock.expect(request.getServletPath()).andReturn("/touch62/jsp/SearchPoi.jsp").anyTimes();
//        EasyMock.expect(request.getContextPath()).andReturn("/poi_service").anyTimes();
//        StringBuffer strB = new StringBuffer("http://localhost:8080/poi_service/touch62/jsp/SearchPoi.jsp");
//        EasyMock.expect(request.getRequestURL()).andReturn(strB);
//    	EasyMock.replay(request);
//    	
//    	IMocksControl control1 = EasyMock.createControl();
//		javax.servlet.jsp.PageContext pageContext = control1.createMock(javax.servlet.jsp.PageContext.class);
//		EasyMock.expect(pageContext.getRequest()).andReturn(request).anyTimes();
//		
//		IMocksControl control2 = EasyMock.createControl();
//		JspWriter jspwriter = control2.createMock(JspWriter.class);
//		EasyMock.expect(pageContext.getOut()).andReturn(jspwriter);
//		EasyMock.replay(pageContext);
//		tlt.setPageContext(pageContext);
//		tlt.doStartTag();
//		String expected = new String("<layout height=\"480\" width=\"320\">\n");
//		expected += "<uicontrol id=\"oneBoxBgImg\">\n";
//		expected += "<uiattribute key=\"width\">88</uiattribute>\n";
//		expected += "<uiattribute key=\"y\">30</uiattribute>\n";
//		expected += "</uicontrol>\n";
//		expected += "</layout>\n";
//		expected += "<layout height=\"320\" width=\"480\">\n";
//		expected += "<uicontrol id=\"oneBoxBgImg\">\n";
//		expected += "<uiattribute key=\"width\">88</uiattribute>\n";
//		expected += "<uiattribute key=\"y\">30</uiattribute>\n";
//		expected += "</uicontrol>\n";
//		expected += "</layout>\n";
//		
//		assertEquals(expected, tlt.outputText.toString());
//	}
	
	public static Object getValue(Object instance, String fieldName) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException
	{
		Field field = getField(instance.getClass(), fieldName);
		field.setAccessible(true);
		return field.get(instance);
	}
	
	public static Field getField(Class<? extends Object> thisClass, String fieldName)throws NoSuchFieldException
	{
		return thisClass.getDeclaredField(fieldName);
	}
	
	public static Method getMethod(Class<? extends Object> thisClass, String methodName ,Class<?> parameter)throws SecurityException, NoSuchMethodException
	{
		Method method = null;
		if(null == parameter)
		{
			method = thisClass.getDeclaredMethod(methodName);
		}
		else
		{
			method = thisClass.getDeclaredMethod(methodName, parameter);
		}
		method.setAccessible(true);
		return method;
	}
}
