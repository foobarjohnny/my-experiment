/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.common.resource.orders;

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

import com.telenav.cserver.common.resource.GeneralPtnPropertyManager;
import com.telenav.cserver.common.resource.ResourceHolderManager;
import com.telenav.cserver.common.resource.constant.HolderType;
import com.telenav.cserver.common.resource.ptn.PtnProperties;
import com.telenav.cserver.common.resource.ptn.PtnPropertiesHolder;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.unittestutil.UTConstant;
import com.telenav.cserver.unittestutil.UnittestUtil;

/**
 * TestProductLoadOrder.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-4-29
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ResourceHolderManager.class,GeneralPtnPropertyManager.class})
@SuppressStaticInitializationFor("com.telenav.cserver.common.resource.ResourceHolderManager")
public class TestPtnLoadOrder extends TestCase{
	private PtnLoadOrder ptnLoadOrder = new PtnLoadOrder();
	private UserProfile userProfile = UnittestUtil.createUserProfile();
	private PtnPropertiesHolder ptnPropertiesHolder;
	Map map4PtnProperties = new HashMap();
	String map4PtnPropertiesValue = "map4PtnPropertiesValue";
	PtnProperties ptnProperties = new PtnProperties(map4PtnProperties);
	
	@Override
	protected void setUp() throws Exception {
		ptnPropertiesHolder = PowerMock.createMock(PtnPropertiesHolder.class);
		map4PtnProperties.put(UTConstant.USERPROFILE_MIN,map4PtnPropertiesValue);
	}
	
	public void testGetAttributeValue(){
		PowerMock.mockStatic(ResourceHolderManager.class);
		EasyMock.expect(ResourceHolderManager.getResourceHolder(HolderType.PTN_TYPE)).andReturn(ptnPropertiesHolder);
		EasyMock.expect(ptnPropertiesHolder.getPtnProperties(userProfile,null)).andReturn(ptnProperties);
		PowerMock.replayAll();
		
		String result = ptnLoadOrder.getAttributeValue(userProfile, null);
		PowerMock.verifyAll();
		
		assertEquals(map4PtnPropertiesValue,result);
	}
	//ResourceHolderManager.getResourceHolder(HolderType.PTN_TYPE) == null
	public void testGetAttributeValue_null(){
		PowerMock.mockStatic(ResourceHolderManager.class);
		EasyMock.expect(ResourceHolderManager.getResourceHolder(HolderType.PTN_TYPE)).andReturn(null);
		PowerMock.replayAll();
		
		String result = ptnLoadOrder.getAttributeValue(userProfile, null);
		PowerMock.verifyAll();
		
		assertEquals("",result);
	}
	//profile.getMin() == null
	public void testGetAttributeValue_null1(){
		UserProfile up = UnittestUtil.createUserProfile();
		up.setMin(null);
		
		PowerMock.mockStatic(ResourceHolderManager.class);
		EasyMock.expect(ResourceHolderManager.getResourceHolder(HolderType.PTN_TYPE)).andReturn(ptnPropertiesHolder);
		EasyMock.expect(ptnPropertiesHolder.getPtnProperties(up,null)).andReturn(ptnProperties);
		PowerMock.replayAll();
		
		String result = ptnLoadOrder.getAttributeValue(up, null);
		PowerMock.verifyAll();
		
		assertEquals("",result);
	}
//	ptnProperties.get(ptn) == null
	public void testGetAttributeValue_null2(){
		PtnProperties ptn = new PtnProperties(new HashMap());
		
		PowerMock.mockStatic(ResourceHolderManager.class);
		EasyMock.expect(ResourceHolderManager.getResourceHolder(HolderType.PTN_TYPE)).andReturn(ptnPropertiesHolder);
		EasyMock.expect(ptnPropertiesHolder.getPtnProperties(userProfile,null)).andReturn(ptn);
		PowerMock.replayAll();
		
		String result = ptnLoadOrder.getAttributeValue(userProfile, null);
		PowerMock.verifyAll();
		
		assertEquals("",result);
	}
	
	public void testGetAttributeValueList(){
		
		PowerMock.mockStatic(ResourceHolderManager.class);
		EasyMock.expect(ResourceHolderManager.getResourceHolder(HolderType.PTN_TYPE)).andReturn(ptnPropertiesHolder);
		EasyMock.expect(ptnPropertiesHolder.getPtnProperties(userProfile,null)).andReturn(ptnProperties);
		
		PowerMock.replayAll();
		List<String> list = ptnLoadOrder.getAttributeValueList(userProfile, null);
		PowerMock.verifyAll();
		assertNotNull(list);
		assertEquals(1,list.size());
		assertEquals(map4PtnPropertiesValue,list.get(0));
	}
	
	public void testGetAttributeValueList_size0(){
		
		PowerMock.mockStatic(ResourceHolderManager.class);
		EasyMock.expect(ResourceHolderManager.getResourceHolder(HolderType.PTN_TYPE)).andReturn(null);
		
		PowerMock.replayAll();
		List<String> list = ptnLoadOrder.getAttributeValueList(userProfile, null);
		PowerMock.verifyAll();
		assertNotNull(list);
		assertEquals(0,list.size());
	}
	
	public void testGetResemblanceFullPath(){
		String configFileName = "configFileName";
		
		PowerMock.mockStatic(GeneralPtnPropertyManager.class);
		EasyMock.expect(GeneralPtnPropertyManager.getPtnResourcePath(userProfile.getMin())).andReturn("\\test\\");
		
		PowerMock.replayAll();
		List<String> list = ptnLoadOrder.getResemblanceFullPath(null, configFileName, userProfile, null);
		
		PowerMock.verifyAll();
		assertNotNull(list);
		assertEquals(1,list.size());
		assertEquals("\\test\\"+configFileName,list.get(0));
	}

}
