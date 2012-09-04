/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.html.util;

import junit.framework.TestCase;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;

import com.telenav.cserver.framework.cli.LogDeploymentInfo;
import com.telenav.cserver.framework.html.datatype.HtmlClientInfo;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * TestAddressSharingService
 * @author pzhang
 * @date 2010-8-9
 */
public class TestHtmlCommonUtil extends TestCase
{
    private HtmlClientInfo clientInfo;
    private String ssoToken;

    @Override
    protected void setUp() throws Exception
    {
        clientInfo = new HtmlClientInfo();
        clientInfo.setProgramCode("ATTNAVPROG");
		clientInfo.setCarrier("ATT");
		clientInfo.setPlatform("ANDROID");
		clientInfo.setVersion("7.1.0");
		clientInfo.setProduct("ATT_NAV");
		clientInfo.setDevice("genericTest");
		clientInfo.setWidth("460");
		clientInfo.setHeight("760");
		clientInfo.setBuildNo("1010");
		clientInfo.setLocale("en_US");
		//set supported device
		//
		ssoToken = "AAAAAACW/uwAAAEx+YKk8n/ujtHKnFRLws+vEBTqnYY=";
    }
    
    public void testGetUserId()
    {
    	String usrId = HtmlCommonUtil.getUserId(ssoToken);
    	System.out.println("user id:" + usrId);
    	
    	Assert.assertEquals(usrId,"9895660");
    }
    
    public void testGetSsotoken()
    {
    	long userId = 1830;
    	HtmlCommonUtil.getSsoToken(userId);
    }
    
    public void testGetTnContext()
    {
    	TnContext tc = HtmlCommonUtil.getTnContext(clientInfo, ssoToken);
    	String dataSetKey = tc.getProperty(TnContext.PROP_MAP_DATASET);
    	System.out.println("dataSetKey:" + dataSetKey);
    	Assert.assertEquals(dataSetKey,"TeleAtlas");
    }
    
    public void testUseNativeBrowser()
    {
    	String url1= HtmlCommonUtil.useNativeBrowser("http://www.rottentomatoes.com/m/30_minutes_or_less/");
    	Assert.assertEquals(url1,"http://www.rottentomatoes.com/m/30_minutes_or_less/?nativebrowser=true");
    	
    	String url2= HtmlCommonUtil.useNativeBrowser("http://www.rottentomatoes.com/m/30_minutes_or_less/?key=1");
    	Assert.assertEquals(url2,"http://www.rottentomatoes.com/m/30_minutes_or_less/?key=1&nativebrowser=true");
    }
    
    public void testFilterLastPara()
    {
    	String s = "";
    	assertEquals("", HtmlCommonUtil.filterLastPara(s));
    	s = "1;2345";
    	assertEquals("1", HtmlCommonUtil.filterLastPara(s));
    	s = "12345";
    	assertEquals(s, HtmlCommonUtil.filterLastPara(s));
    }
    
    public void testConvertToInt()
    {
    	assertEquals(123, HtmlCommonUtil.convertToInt("123"));
    }
    
    public void testConvertToDM5()
    {
    	double d = 2;
    	int expected = (int)(d * 1.e5);
    	assertEquals(expected, HtmlCommonUtil.convertToDM5(d));
    }
    
    public void testGetWSContext() throws AxisFault
    {
    	ConfigurationContext configurationContext = HtmlCommonUtil.getWSContext();
    	assertNotNull(configurationContext);
    	assertTrue(Boolean.parseBoolean(configurationContext.getProperty(HTTPConstants.REUSE_HTTP_CLIENT).toString()));
    	HttpClient client = (HttpClient)configurationContext.getProperty(HTTPConstants.CACHED_HTTP_CLIENT);
    	assertNotNull(client);
    }
    
    public void testAddVersionToStaticContentUrl(){
    	String contentUrl = "login_startup_service/html/js/common.js";
    	String version = LogDeploymentInfo.getVersion();
    	String versionPathEle = StringUtils.isEmpty(version)?"":"/" + version.replace(".", "_");
    	String expectedContentUrl = versionPathEle + "/login_startup_service/html/js/common.js";
    	assertEquals(expectedContentUrl, HtmlCommonUtil.addVersionToStaticContentUrl(contentUrl));
    	
    	contentUrl = "";
    	expectedContentUrl = "";
    	assertEquals(expectedContentUrl, HtmlCommonUtil.addVersionToStaticContentUrl(contentUrl));
    	
    	contentUrl = "strangeurl";
    	expectedContentUrl = versionPathEle + "/strangeurl";
    	assertEquals(expectedContentUrl, HtmlCommonUtil.addVersionToStaticContentUrl(contentUrl));
    }
    
    public void testServiceLocatorURLs() throws Throwable{
		Assert.assertNotNull(HtmlCommonUtil.getCheckOutURL("http://s-tn60-rim-logn.telenav.com"));
		Assert.assertNotNull(HtmlCommonUtil.getFacebookUrl("http://s-tn60-rim-logn.telenav.com"));
		Assert.assertNotNull(HtmlCommonUtil.getStaticResourceServerURL("http://s-tn60-rim-logn.telenav.com"));
	}
    
    
}
