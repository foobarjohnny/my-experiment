/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource;

import java.lang.reflect.Method;

import com.telenav.cserver.unittestutil.UnittestUtil;

import junit.framework.TestCase;

/**
 * ResourceLoaderConfigTest.java
 *
 * jhjin@telenav.cn
 * @version 1.0 2010-6-2
 *
 */
public class ResourceLoaderConfigTest extends TestCase
{
	public void testParseResourceHolderConfig()
	{
		// load resource holders and JMX beans
		ResourceLoaderConfigTest.resourceLoaderConfigInit();
		// assertEquals(3, ResourceHolderManager.getAllResourceHolder().size());
		// 5 Holder, see the resource_loader.xml
		// device, ptn, messages, service_locator, dsm
		assertEquals(5, ResourceHolderManager.getAllResourceHolder().size());
	}

    private static void resourceLoaderConfigInit(){
    	try{
    		Class[] cArray = ResourceFactory.class.getDeclaredClasses();
        	for(Class c: cArray){
        		if(c.getSimpleName().equals("ResourceLoaderConfig")){
        			Method m = c.getDeclaredMethod("init", new Class[]{});
        			m.setAccessible(true);
        			m.invoke(ResourceFactory.getInstance(), new Object[]{});
        			break;
        		}
        	}
    	}catch(Exception ex){
    		UnittestUtil.printExceptionMsg(ex);
    	}
    	
    }

}
