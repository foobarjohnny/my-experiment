/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.ext;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.powermock.reflect.Whitebox;

import junit.framework.TestCase;

/**
 * TelenavResourceBundlerTest.java
 * 
 * jhjin@telenav.cn
 * 
 * @version 1.0 2010-6-2
 * 
 */
public class TestTelenavResourceBundler extends TestCase {

	public void testTelenavResourceBundlerForLoadSuccessful() {
		String path = "device/ATT/RIM/6_0_01/ATT/9000/normal/en_US/server_driven_parameter";
		Locale locale = null;
		String mapDataSet = null;
		TelenavResourceBundler resourceBundle = new TelenavResourceBundler(
				path, locale, mapDataSet);
		// assertEquals(10, resourceBundle.getProperties().size());

		path = "device/ATT/RIM/6_0_01/ATT/8900/normal/es_MX/server_driven_parameter";
		locale = null;
		mapDataSet = null;
		resourceBundle = new TelenavResourceBundler(path, locale, mapDataSet);
		// assertEquals(10, resourceBundle.getProperties().size());
		System.out.println(resourceBundle.getProperties());
	}

	public void testTelenavResourceBundlerForLoadFail() {
		String path = "device/noexist";
		Locale locale = null;
		String mapDataSet = null;
		TelenavResourceBundler loader = new TelenavResourceBundler(path,
				locale, mapDataSet);
		assertNull(loader.getProperties());
	}

	public void testTelenavResourceBundler_args() {
		String path = "device/ATT/RIM/6_0_01/ATT/9000/normal/en_US/server_driven_parameter";
		Locale locale = Locale.CHINA;
		String mapDataSet = "mapDataSet";
		TelenavResourceBundler resourceBundle = new TelenavResourceBundler(
				path, locale, mapDataSet);

		mapDataSet = null;
		resourceBundle = new TelenavResourceBundler(path, locale, mapDataSet);

		locale = null;
		mapDataSet = "mapDataSet";
		resourceBundle = new TelenavResourceBundler(path, locale, mapDataSet);
	}
	
	public void testTelenavResourceBundler_getHttp_fail(){
    	String path = "http";
        Locale locale = Locale.CHINA;
        String mapDataSet = "mapDataSet";
        TelenavResourceBundler resourceBundle = new TelenavResourceBundler(path,locale,mapDataSet);
        
    }
	
	public void testSimple(){
		TelenavResourceBundler resourceBundle = new TelenavResourceBundler("");
		resourceBundle.get("");
		
		Map map = new HashMap();
		map.put("1","one");
		Whitebox.setInternalState(resourceBundle, "map",map);
		assertEquals("one",resourceBundle.get("1"));
	}
}
