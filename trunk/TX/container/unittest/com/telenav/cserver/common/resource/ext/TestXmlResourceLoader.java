/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.common.resource.ext;

import junit.framework.TestCase;

/**
 * TestXmlResourceLoader.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-4-28
 */
public class TestXmlResourceLoader extends TestCase{
	private XmlResourceLoader loader = new XmlResourceLoader();
	public void testLoadResource(){
		String path = "resource/testXmlResourceLoader.xml";//test.xml
		Object resource;
		resource = loader.loadResource(path, null);
		
		assertNotNull(resource);
	}
	
	public void testLoadResource_without_extensionName(){
		Object resource;
		String path = "resource/testXmlResourceLoader";//test.xml
		resource = loader.loadResource(path, null);
		
		assertNotNull(resource);
	}
	
	public void testLoadResource_noExistsFile(){
		Object resource_null;
		String path_null = "r/tes21";//test.xml
		resource_null = loader.loadResource(path_null,null);
		
		assertNull(resource_null);
	}

}
