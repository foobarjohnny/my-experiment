/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.common.resource.orders;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.util.CSStringUtil;
import com.telenav.cserver.unittestutil.UTConstant;
import com.telenav.cserver.unittestutil.UnittestUtil;

/**
 * TestVersionLoadOrder.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-4-28 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({CSStringUtil.class})
public class TestVersionLoadOrder extends TestCase {
	private VersionLoadOrder versionLoadOrder = new VersionLoadOrder();
	private UserProfile userProfile = UnittestUtil.createUserProfile();
	private String result;
	
	public void testGetAttributeValue(){
		PowerMock.mockStatic(CSStringUtil.class);
		EasyMock.expect(CSStringUtil.isNotNil(userProfile.getVersion())).andReturn(true);
		PowerMock.replayAll();
		
		result = versionLoadOrder.getAttributeValue(userProfile, null);
		PowerMock.verifyAll();
		
		assertEquals(UTConstant.USERPROFILE_VERSION,result);
	}
	
	public void testGetAttributeValue1(){
		userProfile.setVersion("7.1.08");
		PowerMock.mockStatic(CSStringUtil.class);
		EasyMock.expect(CSStringUtil.isNotNil(userProfile.getVersion())).andReturn(true);
		PowerMock.replayAll();
		
		result = versionLoadOrder.getAttributeValue(userProfile, null);
		PowerMock.verifyAll();
		
		//assertEquals("7_1_0",result);
		assertEquals("7_1",result);
	}
	
	public void testGetAttributeValue2(){
        userProfile.setVersion("7_1_08");
        PowerMock.mockStatic(CSStringUtil.class);
        EasyMock.expect(CSStringUtil.isNotNil(userProfile.getVersion())).andReturn(true);
        PowerMock.replayAll();
        
        result = versionLoadOrder.getAttributeValue(userProfile, null);
        PowerMock.verifyAll();
        
        //assertEquals("7_1_0",result);
        assertEquals("7_1",result);
    }
	
	public void testGetAttributeValue_null(){
		PowerMock.mockStatic(CSStringUtil.class);
		EasyMock.expect(CSStringUtil.isNotNil(userProfile.getVersion())).andReturn(false);
		PowerMock.replayAll();
		
		result = versionLoadOrder.getAttributeValue(userProfile, null);
		PowerMock.verifyAll();
		
		assertEquals("",result);
	}
	
	public void testGetAttributeValueWithPoint1(){
		userProfile.setVersion("7_1_11");
		PowerMock.mockStatic(CSStringUtil.class);
		EasyMock.expect(CSStringUtil.isNotNil(userProfile.getVersion())).andReturn(true);
		PowerMock.replayAll();
		
		result = versionLoadOrder.getAttributeValueIgnoreSubversion(userProfile);
		PowerMock.verifyAll();
		
		//assertEquals("7.1.0",result);
		assertEquals("7.1",result);
	}
	
	public void testGetAttributeValueWithPoint2(){
		userProfile.setVersion("7.1.11");
		PowerMock.mockStatic(CSStringUtil.class);
		EasyMock.expect(CSStringUtil.isNotNil(userProfile.getVersion())).andReturn(true);
		PowerMock.replayAll();
		
		result = versionLoadOrder.getAttributeValueIgnoreSubversion(userProfile);
		PowerMock.verifyAll();
		
		//assertEquals("7.1.0",result);
		assertEquals("7.1",result);
	}
	
	public void testGetAttributeValueWithPoint_null(){
		PowerMock.mockStatic(CSStringUtil.class);
		EasyMock.expect(CSStringUtil.isNotNil(userProfile.getVersion())).andReturn(false);
		PowerMock.replayAll();
		
		result = versionLoadOrder.getAttributeValueIgnoreSubversion(userProfile);
		PowerMock.verifyAll();
		
		assertEquals("",result);
	}

}
