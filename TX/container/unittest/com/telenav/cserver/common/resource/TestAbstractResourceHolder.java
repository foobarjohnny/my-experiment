/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.common.resource;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.unittestutil.UTConstant;
import com.telenav.cserver.unittestutil.UnittestUtil;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * TestAbstractResourceHolder.java
 * 
 * xljiang@telenav.cn
 * 
 * @version 1.0 2011-5-4
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ ResourceFactory.class, AbstractResourceHolder.class })
public class TestAbstractResourceHolder extends TestCase {
	private TestableClass tc = new TestableClass();
	private TestableClass4TestSimple tc4t = new TestableClass4TestSimple();
	private UserProfile userProfile = UnittestUtil.createUserProfile();


	public void testGet() {
		ResourceContent rc = (ResourceContent) tc.get("key", null);
		UserProfile up = (UserProfile) rc.getObject();
		assertEquals(UTConstant.USERPROFILE_CARRIER, up.getCarrier());
	}

	public void testGetKey() {
		//prepare and replay
		PowerMock.mockStatic(ResourceFactory.class);
		EasyMock.expect(
				ResourceFactory.getConfigSuffixForKey(new ArrayList(),
						userProfile, null))
				.andReturn("ccc").anyTimes();
		PowerMock.replayAll();
		
		//invoke and verify
		String result = tc.getKey(userProfile, null);
		PowerMock.verifyAll();
		
		//assert
		assertEquals("aaaccc_ccc_ccc", result);
	}
	
	public void testGetKey_Profile_null() {
		String result = tc.getKey(null, null);
		assertEquals("aaa", result);
	}
	
	public void testGetKey_set_NotNull() {
		//prepare and replay
		tc.set = new ResourceSet();
		tc.set.setName("set--Name");
		PowerMock.mockStatic(ResourceFactory.class);
		EasyMock.expect(
				ResourceFactory.getConfigSuffixForKey(new ArrayList(),userProfile, null))
				.andReturn("ccc").anyTimes();
		PowerMock.replayAll();
		
		//invoke and verify
		String result = tc.getKey(userProfile, null);
		PowerMock.verifyAll();
		
		//assert
		assertEquals("aaaccc_ccc_ccc", result);
	}
	
	public void testSimple(){
		tc4t.getResourceSet(); 
		tc4t.setResourceSet(new ResourceSet()); 
		tc4t.getTxNode(null,null); 
		tc4t.getTxNodes(null,null);
		tc4t.getVersion(null,null);
	}

	private class TestableClass extends AbstractResourceHolder {
		/**
		 * 
		 */
		public TestableClass() {
			this.name = "aaa";
		}
		@Override
		public ResourceContent createObject(String key, UserProfile profile,
				TnContext tnContext) {
			ResourceContent rc = new ResourceContent();
			rc.setObject(userProfile);
			return rc;
		}

		public LoadOrders getPrefixStructureOrders() {
			return new LoadOrders();
		}
		public LoadOrders getFloatingStructureOrders(){
			return new LoadOrders();
		}
		public LoadOrders getSuffixStructureOrders(){
			return new LoadOrders();
		}
	}
	
	private class TestableClass4TestSimple extends AbstractResourceHolder {
		/**
		 * 
		 */
		public TestableClass4TestSimple() {
			this.name = "aaa";
		}
		@Override
		public ResourceContent createObject(String key, UserProfile profile,
				TnContext tnContext) {
			ResourceContent rc = new ResourceContent();
			rc.setObject(userProfile);
			return rc;
		}

		@Override
		public ResourceContent getResourceContent(UserProfile profile,
				TnContext tc) {
			return new ResourceContent();
		}
	}

}
