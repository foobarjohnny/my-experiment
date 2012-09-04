/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.common.resource.common.prompts;

import junit.framework.TestCase;

import org.apache.commons.lang.StringUtils;
import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.cserver.matcher.MatchBox;
import com.telenav.cserver.resource.common.prompts.BinaryHolder;
import com.telenav.cserver.unittestutil.autorun.GSetterCaller;

/**
 * TestBinaryHolder.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-26
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({StringUtils.class})
public class TestBinaryHolder extends TestCase{
	
	public void testIsMd5Equal_false(){
		//define variables
		String name = "myName";
		BinaryHolder binaryHolder = new BinaryHolder(name, name.getBytes());
		
		//prepare and replay
		PowerMock.mockStatic(StringUtils.class);
		EasyMock.expect(StringUtils.isNotEmpty(MatchBox.StringEquals(""))).andReturn(false);
		PowerMock.replayAll();
		
		//invoke and verify
		boolean b = binaryHolder.isMd5Equal(".123");
		PowerMock.verifyAll();
		
		//assert
		assertFalse(b);
	}
	public void testIsMd5Equal_true(){
		//define variables
		String name = "myName";
		BinaryHolder binaryHolder = new BinaryHolder(name, name.getBytes());
		
		boolean b = binaryHolder.isMd5Equal(binaryHolder.getMd5());
		
		//assert
		assertTrue(b);
		binaryHolder.toString();//for coverage
	}
	
	/**
	 * for coverage
	 * @throws Exception 
	 */
	public void testSimple(){
		String name = "myName";
		BinaryHolder binaryHolder = new BinaryHolder(name, name.getBytes());
		String clazz = "com.telenav.cserver.resource.common.prompts.BinaryHolder";
		GSetterCaller cSetterCaller = new GSetterCaller();
		
		try {
			cSetterCaller.registerClass(clazz);
			cSetterCaller.registerInstance(clazz, binaryHolder);
			cSetterCaller.registerAvoidMethod(clazz, "toString");
			cSetterCaller.call();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
//		binaryHolder.toString();
	}

}
