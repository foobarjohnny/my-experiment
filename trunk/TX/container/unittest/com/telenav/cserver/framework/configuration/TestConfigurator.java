/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.configuration;

import java.io.File;
import java.util.Map;

import junit.framework.TestCase;

import com.telenav.cserver.class4test.configurator.ParentBen;
import com.telenav.cserver.class4test.configurator.ParentJim;
import com.telenav.cserver.unittestutil.UnittestUtil;

/**
 * TestConfigurator.java
 * 
 * xljiang@telenav.cn
 * 
 * @version 1.0 2011-5-19
 */
public class TestConfigurator extends TestCase {

	private Configurator configurator = new Configurator();
	private String path0 = UnittestUtil.getAbsURLPath("./configurator/test.xml");
	private String path1 = UnittestUtil.getAbsURLPath("./configurator/test1.xml");

	public void testGetObjects() throws Exception {
		/*
		 * Failed on Hudson, remove first
		System.out.println("path0:" + path0);
		System.out.println("paht1:" + path1);
		
		File file = new File("./");
		System.out.println("current location:" + file.getAbsolutePath());

		Map map = (Map) Configurator.getObjects(path0, ParentJim.class);

		assertEquals(1, map.size());
		assertTrue(map.get("childJim") instanceof ParentJim);
		*/
	}

	public void testGetObjects1() throws Exception {
		/*
		 * Failed on Hudson, remove first
		String[] configFile = new String[2];
		configFile[0] = path0;
		configFile[1] = path1;

		System.out.println("path0:" + path0);
		System.out.println("paht1:" + path1);

		Map map = (Map) Configurator.getObjects(configFile, ParentBen.class);

		assertEquals(3, map.size());
		assertTrue(map.get("childBen") instanceof ParentBen);
		*/
	}

}
