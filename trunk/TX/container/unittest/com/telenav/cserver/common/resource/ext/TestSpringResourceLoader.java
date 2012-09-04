/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.ext;

import com.telenav.cserver.class4test.SampleBean;

import junit.framework.TestCase;

/**
 * SpringResourceLoaderTest.java
 *
 * jhjin@telenav.cn
 * @version 1.0 2010-6-2
 *
 */
public class TestSpringResourceLoader extends TestCase
{
    
    public void testLoadResourceForSuccessful()
    {
        String path = "device/spring_en_US";
        String objectName = "test";
        
        SpringResourceLoader loader = new SpringResourceLoader();
        Object object = loader.loadResource(path, objectName);
        
        assertNotNull(object);
        assertEquals("test string",((SampleBean)object).getValue());
        
    }
    
    public void testLoadResourceForSuccessful2() throws Exception
    {
        String path = "device/spring_es_MX";
        String objectName = "test";
        
        SpringResourceLoader loader = new SpringResourceLoader();
        Object object = loader.loadResource(path, objectName);
        assertNotNull(object);
        assertEquals("Nmero de tel\u00E9fono",((SampleBean)object).getValue());
    }
    
    public void testLoadResourceForFail()
    {
        String path = "noexist";
        String objectName = "test";
        
        SpringResourceLoader loader = new SpringResourceLoader();
        Object object = loader.loadResource(path, objectName);
        assertNull(object);
    }
    
    public void testLoadResource_obj_isnull(){
    	String path = "device/spring_es_MX";
        String objectName = "test";
        
        SpringResourceLoader loader = new SpringResourceLoader();
        Object object = loader.loadResource(path, null);
        assertNotNull(object);
        assertTrue(object instanceof SampleBean);
    }
    
    public void testLoadResource_Exception(){
    	String path = "device/spring_en_US";
        String objectName = "test";
        
        SpringResourceLoader loader = new SpringResourceLoader();
        Object object = loader.loadResource(path, null);
        assertNull(object);
    }

}

