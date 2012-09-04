/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.common.resource.orders;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.unittestutil.UTConstant;
import com.telenav.cserver.unittestutil.UnittestUtil;

/**
 * TestResolutionLoadOrder.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-4-29
 */
public class TestResolutionLoadOrder extends TestCase {
	private ResolutionLoadOrder resolutionLoadOrder = new ResolutionLoadOrder();
	private UserProfile userProfile = UnittestUtil.createUserProfile();
	public void testGetAttributeValue(){
		String result = resolutionLoadOrder.getAttributeValue(userProfile, null);
		assertEquals(UTConstant.USERPROFILE_RESOLUTION,result);
	}
	
	public void testGetAttributeValueList(){
		List<String> list = resolutionLoadOrder.getAttributeValueList(userProfile, null);
		assertNotNull(list);
		assertEquals(2,list.size());
		assertEquals(UTConstant.USERPROFILE_RESOLUTION,list.get(0));
		assertEquals(UTConstant.DEFAULT,list.get(1));
	}
	
	public void testGetParaentFolder() throws Exception{
		String attr = "test.xml";
		String parent = "\\unittest\\config\\";
		String fileName = parent + attr;
		Method method = ResolutionLoadOrder.class.getDeclaredMethod("getParaentFolder", String.class,String.class);
        method.setAccessible(true);
        assertEquals(parent,method.invoke(resolutionLoadOrder, fileName,attr));
	}
	public void testGetResolutionResem()throws Exception{
		String attr = "test.xml";
		String parent = "device/";
		String fileName = parent + attr;
		Method method = ResolutionLoadOrder.class.getDeclaredMethod("getResolutionResem", String.class,String.class);
        method.setAccessible(true);
        List<String> list = (List<String>)method.invoke(resolutionLoadOrder, fileName,attr);
        assertEquals(2,list.size());
	}
	public void testGetResemblance(){
		String resolution = "400x480_480x400";
		List<String> list = new ArrayList<String>();
		list.add("1300x1480_1480x1300");
		list.add("0x0_0x0");
		list.add("320x480_480x320");
		list.add("800x600_600x300");
		list.add("800x600_600x800");
		list.add("1x2_2x1");
		list.add("500x400_400x500");
		list.add("300x500_500x300");
		
		assertEquals("320x480_480x320",ResolutionLoadOrder.getResemblance(list, resolution));
		
		assertNull(ResolutionLoadOrder.getResemblance(null,""));
		assertNull(ResolutionLoadOrder.getResemblance(new ArrayList<String>(),""));
	}
	public void testGetResemblanceFullPath(){
		UserProfile userProfile1 = new UserProfile();
		userProfile1.setAttribute(UserProfile.ARRT_RESOLUTION, "400x480_480x400");
		String attr = "400x480_480x400";
		String parent = "device/";
		String fileName = parent + attr;
		
		List<String> list = resolutionLoadOrder.getResemblanceFullPath(fileName, null, userProfile1, null);
		
		assertNotNull(list);
		assertEquals(1,list.size());
		assertEquals("device/320x480_480x320",list.get(0));
	}


}
