/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.cli;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.unittestutil.UnittestUtil;

/**
 * TestCliFakeTransaction.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-19
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({CliThreadLocalUtil.class})
public class TestCliFakeTransaction extends TestCase{
	private String cftType = "cftType";
	private CliFakeTransaction cft = new CliFakeTransaction(cftType);
	private UserProfile userProfile = UnittestUtil.createUserProfile();
	
	//CliThreadLocalUtil.getUserProfile() == null
	public void testSetStatus(){
		//prepare and replay
		PowerMock.mockStatic(CliThreadLocalUtil.class);
		EasyMock.expect(CliThreadLocalUtil.getUserProfile()).andReturn(null);
		EasyMock.expect(CliThreadLocalUtil.getExecutorType()).andReturn(cftType);
		PowerMock.replayAll();
		//invoke and verify
		cft.setStatus(new Throwable("A fake Throwable"));
		PowerMock.verifyAll();
		//assert
	}
	//CliThreadLocalUtil.getUserProfile() != null
	public void testSetStatus1(){
		//prepare and replay
		PowerMock.mockStatic(CliThreadLocalUtil.class);
		EasyMock.expect(CliThreadLocalUtil.getUserProfile()).andReturn(userProfile);
		EasyMock.expect(CliThreadLocalUtil.getExecutorType()).andReturn(cftType);
		PowerMock.replayAll();
		//invoke and verify
		cft.setStatus(new Throwable("A fake Throwable"));
		PowerMock.verifyAll();
		//assert
	}
	public void testSimple(){
		cft.setFunctionName(""); 
		cft.addData("",""); 
		cft.complete();
		cft.setStatus("");

	}

}
