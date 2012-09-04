/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.ext;

import java.io.File;
import java.net.URL;

import org.powermock.reflect.Whitebox;

import junit.framework.TestCase;

import com.telenav.cserver.common.resource.bar.ResourceBarCollection;
import com.telenav.cserver.common.resource.ext.BarIterationResourceLoader.BarFilter;
import com.telenav.cserver.common.resource.ext.BarIterationResourceLoader.MetaFilter;
import com.telenav.cserver.common.resource.ext.BarIterationResourceLoader.MetaString;
import com.telenav.cserver.unittestutil.UnittestUtil;

/**
 * BarIterationResourceLoaderTest.java
 *
 * jhjin@telenav.cn
 * @version 1.0 2010-6-2
 *
 */
public class TestBarIterationResourceLoader extends TestCase
{
	BarIterationResourceLoader loader = new BarIterationResourceLoader();
    public void testLoadResourceForSuccessful()
    {
        String path= "device/ATT/RIM/6_0_01/ATT/9000/common_bar";
        
        
        ResourceBarCollection resourceBarCollections = (ResourceBarCollection)loader.loadResource(path, null);
        
        assertEquals(31,resourceBarCollections.getMetaInformation().size());
        assertEquals(31,resourceBarCollections.getBarList().size());
    }
    
    public void testOthers(){
    	//test BarFilter
    	BarFilter barFilter = new BarFilter();
    	assertFalse(barFilter.accept(null,null));
    	assertFalse(barFilter.accept(null,"abc"));
    	assertTrue(barFilter.accept(null,"abc.node"));
    	
    	//test MetaString
    	int compareTo;
    	MetaString metaString = loader.new MetaString();
    	MetaString anotherMetaString = loader.new MetaString();
    	metaString.getMetaFile();
    	metaString.getPriority();
    	metaString.setPriority(1);
    	anotherMetaString.setPriority(5);
    	try{
    		metaString.compareTo(new String("string"));
    	}catch(Exception e){
    		UnittestUtil.printExceptionMsg(e);
    	}
    	
    	compareTo = metaString.compareTo(anotherMetaString);
    	assertEquals(-4,compareTo);
    }
    
    public void testResortFileName() throws Exception{
    	String path = "resource/meta";
    	URL url = Thread.currentThread().getContextClassLoader().getResource(path);
    	String absoluteFilePath = url.getPath();
		File dir = new File(absoluteFilePath);
		File[] fileArray = dir.listFiles(new MetaFilter());
    	fileArray = Whitebox.invokeMethod(loader, "resortFileName", fileArray,dir.getAbsolutePath());
    	assertEquals(3,fileArray.length);
    	assertEquals("res_core_ri.client.meta",fileArray[0].getName());
    	assertEquals("browser.page.meta",fileArray[1].getName());
    	assertEquals("map_color.meta",fileArray[2].getName());
    }

}
