/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.html.util;

import java.net.URLDecoder;
import java.net.URLEncoder;

import org.junit.Assert;

import junit.framework.TestCase;

import com.telenav.cserver.framework.html.datatype.HtmlClientInfo;
import com.telenav.cserver.framework.html.datatype.HtmlDeviceConfig;
import com.telenav.cserver.framework.throttling.ThrottlingException;

/**
 * TestAddressSharingService
 * @author pzhang
 * @date 2010-8-9
 */
public class TestHtmlDeviceManager extends TestCase
{
    private HtmlClientInfo clientInfo;

    @Override
    protected void setUp() throws Exception
    {
        clientInfo = new HtmlClientInfo();
        clientInfo.setProgramCode("ATTNAVPROG");
		clientInfo.setCarrier("ATT");
		clientInfo.setPlatform("ANDROID");
		clientInfo.setVersion("7.0.01");
		clientInfo.setProduct("ATT_NAV");
		clientInfo.setDevice("genericTest");
		clientInfo.setWidth("768");
		clientInfo.setHeight("1024");
		clientInfo.setBuildNo("1010");
		clientInfo.setLocale("en_US");
		//set supported device
    }

    
    /**
     * 
     * @throws ThrottlingException
     */
    public void testResourceConfig()
    {
    	//test simple movie list
    	HtmlDeviceConfig config = HtmlDeviceManager.getInstance().getDeviceConfig(clientInfo);
    	System.out.println(config.toString());
    }
    
    
    public void testGetDeviceLevelConfig()
    {
    	String value1 = HtmlClientHelper.getDeviceLevelConfig(clientInfo, "supportFree");
    	String value2 = HtmlClientHelper.getDeviceLevelConfig(clientInfo, "isSprint");
    	System.out.println(value1 + "," + value2);
    	Assert.assertEquals("true", value1);
    	Assert.assertEquals("false", value2);
    }
    
    public void testSupportFeature()
    {
    	boolean b1 = HtmlFeatureHelper.getInstance().supportFeature(clientInfo, HtmlFrameworkConstants.FEATURE.FEATURE_MOVIE);
    	boolean b2 = HtmlFeatureHelper.getInstance().supportFeature(clientInfo, HtmlFrameworkConstants.FEATURE.FEATURE_HOTEL);
    	Assert.assertEquals(true, b1);
    	Assert.assertEquals(false, b2);
    }
    
    public void testSsoToken()
    {
    	String ssoToken = "AAAAAACW+B8AAAEyBYic7QnO5P1vID+eo8MK7+Zx3bA=";
    	
    	String test1 = URLEncoder.encode(ssoToken);
    	System.out.println("-test1:" + test1);
    	
    	String test2 = URLDecoder.decode(test1);
    	System.out.println("-test2:" + test2);
    	
    	String usrId = HtmlCommonUtil.getUserId(ssoToken);
    	System.out.println("user id:" + usrId);
    }
    
    public void testGetVesion()
    {
    	String version = HtmlClientHelper.getVersion(clientInfo.getVersion());
    	System.out.println("version no:" + version);
    	
    	Assert.assertEquals(version,"7_0");
    }
}
