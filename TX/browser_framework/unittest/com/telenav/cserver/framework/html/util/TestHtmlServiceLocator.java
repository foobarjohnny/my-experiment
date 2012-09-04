package com.telenav.cserver.framework.html.util;

import junit.framework.TestCase;

import org.junit.Assert;


public class TestHtmlServiceLocator extends TestCase{
    
    private String hostUrl_TEST;
    private String hostUrl_STAGE;
    private String hostUrl_PRODUCTION;
    private String hostUrl_72DEV;
    private String hostUrl_72QA;
    private String hostUrl_SONYQA;
    
    private String key_APPSTORE;

    @Override
    protected void setUp() throws Exception
    {
    	key_APPSTORE = "APPSTORE_URL";
    	//
    	hostUrl_TEST = "t-tn60-rim-poi.telenav.com:8080";
    	hostUrl_STAGE = "http://s-tn60-rim-poi.telenav.com:8080";
    	hostUrl_PRODUCTION = "http://tn7x-poi.telenav.com";
    	hostUrl_72DEV = "http://hqd-fptn72csvr.telenav.com";
    	hostUrl_72QA = "http://hqs-fptn72csvr02.telenav.com";
    	hostUrl_SONYQA = "http://hqt-sonyvideologin.telenav.com";
    }
    
    /**
     * 
     */
    public void testGetServiceUrlOfAppstore()
    {
    	//test simple movie list
    	String urlOfTest = HtmlServiceLocator.getInstance().getServiceUrl(hostUrl_TEST, key_APPSTORE);
    	Assert.assertEquals("http://hqt-ecommerce.telenav.com/appstore/api/getjs.do", urlOfTest);
    	
    	String urlOfStage = HtmlServiceLocator.getInstance().getServiceUrl(hostUrl_STAGE, key_APPSTORE);
    	Assert.assertEquals("http://hqs-ecommerce.telenav.com/appstore/api/getjs.do", urlOfStage);
    	
    	String urlOfProduction = HtmlServiceLocator.getInstance().getServiceUrl(hostUrl_PRODUCTION, key_APPSTORE);
    	Assert.assertEquals("http://ecommerce.telenav.com/appstore/api/getjs.do", urlOfProduction);

    	String urlOf72Dev = HtmlServiceLocator.getInstance().getServiceUrl(hostUrl_72DEV, key_APPSTORE);
    	Assert.assertEquals("http://hqd-fp72ecommerce.telenav.com/appstore/api/getjs.do", urlOf72Dev);
    	
    	String urlOf72QA = HtmlServiceLocator.getInstance().getServiceUrl(hostUrl_72QA, key_APPSTORE);
    	Assert.assertEquals("http://hqs-fp72ecommerce.telenav.com/appstore/api/getjs.do", urlOf72QA);
    	
    	String urlOfSonyQA = HtmlServiceLocator.getInstance().getServiceUrl(hostUrl_SONYQA, key_APPSTORE);
    	Assert.assertEquals("http://hqt-sonyvideoappstore.telenav.com/appstore/api/getjs.do", urlOfSonyQA);
    }
}
