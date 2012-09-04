/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.ext;

import com.telenav.cserver.common.resource.bar.ResourceBarCollection;

import junit.framework.TestCase;

/**
 * BarIterationResourceLoaderTest.java
 *
 * jhjin@telenav.cn
 * @version 1.0 2010-6-2
 *
 */
public class BarIterationResourceLoaderTest extends TestCase
{
    public void testLoadResourceForSuccessful()
    {
        String path= "device/ATT/RIM/6_0_01/ATT/9000/common_bar";
        
        BarIterationResourceLoader loader = new BarIterationResourceLoader();
        ResourceBarCollection resourceBarCollections = (ResourceBarCollection)loader.loadResource(path, null);
        
        assertEquals(31,resourceBarCollections.getMetaInformation().size());
        assertEquals(31,resourceBarCollections.getBarList().size());
    }

}
