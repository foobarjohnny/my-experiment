/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.common.resource;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.telenav.cserver.unittestutil.UnittestUtil;

/**
 * TestResourceUtil.java
 * 
 * xljiang@telenav.cn
 * 
 * @version 1.0 2011-4-29
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ResourceBundle.class,ResourceUtil.class,PropertyResourceBundle.class})
public class TestResourceUtil extends TestCase {
	private Properties properties = PowerMock.createMock(Properties.class);
	private ResourceBundle resourceBundle = PowerMock.createMock(ResourceBundle.class);
	private Enumeration enumeration;
	private Locale locale = new Locale("en");
	private String resourceName = "resourceName";
	private PropertyResourceBundle propertyResourceBundle = PowerMock.createMock(PropertyResourceBundle.class);

	@Override
	protected void setUp() throws Exception {
		Hashtable<String,String> hash = new Hashtable<String,String>();
		hash.put("a", "1");
		hash.put("b", "2");
		hash.put("c", "3");
		enumeration = hash.keys();
	}

	public void testGetResource() {
		EasyMock.expect(properties.keys()).andReturn(enumeration);
		EasyMock.expect(properties.getProperty("a")).andReturn("97");
		EasyMock.expect(properties.getProperty("b")).andReturn("98");
		EasyMock.expect(properties.getProperty("c")).andReturn("99");
		PowerMock.replayAll();
		Map map = ResourceUtil.getResource(properties);
		PowerMock.verifyAll();
		assertEquals("97",map.get("a"));
		assertEquals("98",map.get("b"));
		assertEquals("99",map.get("c"));
	}
	
	public void testGetResource1() {
		EasyMock.expect(resourceBundle.getKeys()).andReturn(enumeration);
		EasyMock.expect(resourceBundle.getString("a")).andReturn("97");
		EasyMock.expect(resourceBundle.getString("b")).andReturn("98");
		EasyMock.expect(resourceBundle.getString("c")).andReturn("99");
		PowerMock.replayAll();
		
		Map map = ResourceUtil.getResource(resourceBundle);
		PowerMock.verifyAll();
		
		assertEquals("97",map.get("a"));
		assertEquals("98",map.get("b"));
		assertEquals("99",map.get("c"));
	}
	public void testGetResourceBundle(){
		PowerMock.mockStatic(PropertyResourceBundle.class);
		EasyMock.expect(PropertyResourceBundle.getBundle(resourceName, locale)).andReturn(propertyResourceBundle);
		PowerMock.replayAll();
		
		PropertyResourceBundle bundle = ResourceUtil.getResourceBundle(resourceName, locale);
		PowerMock.verifyAll();
		
		assertNotNull(bundle);
	}
	public void testSplit(){
		PowerMock.replayAll();
		String[] result = ResourceUtil.split("1!22!333!4444!55555", "!");
		PowerMock.verifyAll();
		assertEquals("[1, 22, 333, 4444, 55555]",Arrays.asList(result).toString());
	}
	
	public void testSimple()throws Exception{
		try{
			ResourceUtil.getResourceBundle(resourceName);
		}catch(Exception e){
			UnittestUtil.printExceptionMsg(e);
		}
		//com.telenav.cserver.common.resource.ResourceUtil
		Whitebox.invokeConstructor(ResourceUtil.class);
	}

}
