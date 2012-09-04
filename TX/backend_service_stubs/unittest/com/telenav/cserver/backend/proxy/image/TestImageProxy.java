package com.telenav.cserver.backend.proxy.image;
import junit.framework.TestCase;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;

/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */

/**
 * ImageProxyTest.java
 *
 * jhjin@telenav.cn
 * @version 1.0 Apr 12, 2011
 *
 */
public class TestImageProxy extends TestCase
{
    private ImageProxy imageProxy = null;
    
 
    @Override
    protected void setUp() throws Exception
    {
        imageProxy = new TestableImageProxy();
    }


    public void testGetImageTile4Success() throws Exception
    {
        String url = "http://174.35.40.12/tile/?layers=NA_NT_T&x=2638&y=6354&zoom=3&tnv=1.07&style=ts=256&alert_data_src=WESTWOOD,WESTWOODBLOCKING,ITIS,NJECTION,TIMS,ROADPILOT,CCACIR,INRIXCANADA,UGT,TELENAV&flow_data_src=INRIX,INRIXCANADA,ITIS,CCACIR";
        ImageResponse response = imageProxy.getMapTile(url); 
        assertNotNull("imagetile can't be null", response);
        assertTrue("image status should be 200 actually is "+response.getStatusCode(), response.getStatusCode() == ImageResponse.STATUS_SUCESS);
        assertNotNull("image bin data can't be null", response.getBinData());
        assertTrue("length of image bin data can't be zero", response.getBinData().length > 0);
    }
    
    public void testCreateImageUrl() throws Exception
    {
        ImageRequest request = new ImageRequest();
        request.setLayers("NA_NT_T");
        request.setXCoordinate("2638");
        request.setYCoordinate("6354");
        request.setZoom("3");
        request.setVersion("1.07");
        request.setStyle("256");
        request.setExtendParams("&alert_data_src=WESTWOOD,WESTWOODBLOCKING,ITIS,NJECTION,TIMS,ROADPILOT,CCACIR,INRIXCANADA,UGT,TELENAV&flow_data_src=INRIX,INRIXCANADA,ITIS,CCACIR");
        String url = imageProxy.createImageUrl(request);
        assertEquals("http://174.35.40.12/tile/?layers=NA_NT_T&x=2638&y=6354&zoom=3&tnv=1.07&style=ts=256&alert_data_src=WESTWOOD,WESTWOODBLOCKING,ITIS,NJECTION,TIMS,ROADPILOT,CCACIR,INRIXCANADA,UGT,TELENAV&flow_data_src=INRIX,INRIXCANADA,ITIS,CCACIR" 
        		     ,url);

    }
    
    public void testGetImageTile4Fail() throws Exception
    {
        String url = "http://xyz";
        ImageResponse response = imageProxy.getMapTile(url); 
        assertNotNull("imagetile can't be null", response);
        assertTrue("image status should not be 200", response.getStatusCode() != ImageResponse.STATUS_SUCESS);
    }
    
    
    private class TestableImageProxy extends ImageProxy
    {
        @Override
        protected HttpClient getHttpClient()
        {
           return new HttpClient();
        }

        @Override
        protected String getServiceUrl()
        {
            return "http://174.35.40.12/tile/?layers={0}&x={1}&y={2}&zoom={3}&tnv={4}&style=ts={5}";
        }
        
    }

}
