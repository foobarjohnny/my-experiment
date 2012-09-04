/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.common.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.unittestutil.UnittestUtil;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * TestAbstractImplementationHolder.java
 * 
 * xljiang@telenav.cn
 * 
 * @version 1.0 2011-4-29
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ ResourceFactory.class, WildcardMatchUtil.class,
		AbstractImplementationHolder.class, ResourceContent.class,
		AbstractResourceHolder.class })
@SuppressStaticInitializationFor("com.telenav.cserver.common.resource.ResourceFactory")
public class TestAbstractImplementationHolder extends TestCase {
	private AbstractImplementationHolder holder;
	private ResourceContent content;
	private TestableClass tc = new TestableClass();
	private TestableClass1 tc1 = new TestableClass1();

	@Override
	protected void setUp() throws Exception {
		holder = PowerMock.createMock(AbstractImplementationHolder.class);
		content = PowerMock.createMock(ResourceContent.class);
	}

	public void testCreateObject() throws Exception {

		// 1. prepare
		PowerMock.mockStatic(ResourceFactory.class);
		EasyMock.expect(ResourceFactory.createResource(tc, null, null)).andReturn(new HashMap<String,String>());
		PowerMock.replayAll();

		// 2. run the test method
		ResourceContent rc = tc.createObject(null, null, null);

		// 3. verify
		PowerMock.verifyAll();
		assertNotNull(rc);
		assertNotNull(rc.getProperty(AbstractImplementationHolder.KEY_LIST));
	}
	
	public void testCreateObject_null() throws Exception {

		// 1. prepare
		PowerMock.mockStatic(ResourceFactory.class);
		EasyMock.expect(ResourceFactory.createResource(tc, null, null)).andReturn(null);
		PowerMock.replayAll();

		// 2. run the test method
		ResourceContent rc = tc.createObject(null, null, null);

		// 3. verify
		PowerMock.verifyAll();
		assertNull(rc);
	}
	/**
	 * there will be a exception when executing : ruleSize = Integer.parseInt(ruleNumber);
	 */
	public void testCreateObject_RuleNumberisNotNull_withException(){

		// 1. prepare
		Map<String,String> map = new HashMap<String,String>();
		map.put(AbstractImplementationHolder.KEY_RULE_NUMBER, "abc");
		PowerMock.mockStatic(ResourceFactory.class);
		EasyMock.expect(ResourceFactory.createResource(tc, null, null)).andReturn(map);
		PowerMock.replayAll();

		// 2. run the test method
		ResourceContent rc = tc.createObject(null, null, null);

		// 3. verify
		PowerMock.verifyAll();
		assertNotNull(rc);
	}
	
	public void testCreateObject_RuleNumberisNotNull(){

		// 1. prepare
		Map<String,String> map = new HashMap<String,String>();
		map.put(AbstractImplementationHolder.KEY_RULE_NUMBER, "5");
		PowerMock.mockStatic(ResourceFactory.class);
		EasyMock.expect(ResourceFactory.createResource(tc, null, null)).andReturn(map);
		PowerMock.replayAll();

		// 2. run the test method
		ResourceContent rc = tc.createObject(null, null, null);

		// 3. verify
		PowerMock.verifyAll();
		assertNotNull(rc);
	}

	public void testCreateObject_MoreComplex() throws Exception {
		// 1. prepare
		Map<String,String> map = new HashMap<String,String>();
		map.put("key0", "0");
		map.put("1", "1");
		map.put("2", "name_A_B_C:jim");
		map.put("3", "country_D_E:China");
		PowerMock.mockStatic(ResourceFactory.class);
		EasyMock.expect(ResourceFactory.createResource(tc, null, null)).andReturn(map);
		PowerMock.replayAll();

		// 2. run the test method
		ResourceContent rc = tc.createObject(null, null, null);

		// 3. verify
		PowerMock.verifyAll();
		//4. assert
		List<MatchObject> list = (List<MatchObject>)rc.getProperty(AbstractImplementationHolder.KEY_LIST);
		MatchObject mo0 = (MatchObject)list.get(0);
		MatchObject mo1 = (MatchObject)list.get(1);
		String[] keyStrings0 = mo0.keyStrings;
		String[] keyStrings1 = mo1.keyStrings;
		
		assertNotNull(rc);
		assertEquals(2,list.size());
		
		assertEquals("jim",mo0.value);
		assertEquals(4,keyStrings0.length);
		assertEquals("NAME",keyStrings0[0]);
		assertEquals("A",keyStrings0[1]);
		assertEquals("B",keyStrings0[2]);
		assertEquals("C",keyStrings0[3]);
		
		assertEquals("China",mo1.value);
		assertEquals(3,keyStrings1.length);
		assertEquals("COUNTRY",keyStrings1[0]);
		assertEquals("D",keyStrings1[1]);
		assertEquals("E",keyStrings1[2]);
	}

	public void testGetMatchedString() {
		PowerMock.mockStatic(WildcardMatchUtil.class);
		Map map = new HashMap();

		EasyMock.expect(content.getProps()).andReturn(map);
		EasyMock.expect(WildcardMatchUtil.match(null, null, null, null))
				.andReturn("RETURNVALUE");

		PowerMock.replayAll();
		String result = tc.getMatchedString(null, null);
		PowerMock.verifyAll();
		assertEquals("RETURNVALUE", result);
	}
	public void testGetMatchedString_returnNull() {
		TestableClass_null_resourceContent t = new TestableClass_null_resourceContent();
		assertNull(t.getMatchedString(null,null));
	}

	public void testNewInstance() {
		Object obj = tc.newInstance("com.telenav.cserver.common.resource.TestAbstractImplementationHolder");
		assertNotNull(obj);
		
		try{
			tc.newInstance("abc");
		}catch(Exception e){
			UnittestUtil.printExceptionMsg(e);
		}
	}

	public void testGetImplementationInstance() {
		Object obj = tc1.getImplementationInstance(null, null);
		assertNotNull(obj);
	}
	public void testGetImplementationInstance_returnNull() {
		TestableClass_null_resourceContent t = new TestableClass_null_resourceContent();
		Object obj = t.getImplementationInstance(null, null);
		assertNull(obj);
	}

	private class TestableClass extends AbstractImplementationHolder {
		@Override
		public ResourceContent getResourceContent(UserProfile profile,
				TnContext tc) {
			// TODO Auto-generated method stub
			return content;
		}
	}

	private class TestableClass1 extends AbstractImplementationHolder {
		@Override
		protected String getMatchedString(UserProfile profile,
				TnContext tnContext) {
			// TODO Auto-generated method stub
			return "com.telenav.cserver.common.resource.TestAbstractImplementationHolder";
		}
	}
	
	private class TestableClass_null_resourceContent extends AbstractImplementationHolder {
		@Override
		public ResourceContent getResourceContent(UserProfile profile,
				TnContext tc) {
			// TODO Auto-generated method stub
			return null;
		}
	}

}
