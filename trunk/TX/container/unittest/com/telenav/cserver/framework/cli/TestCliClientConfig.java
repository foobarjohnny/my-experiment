/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.cli;

import org.powermock.reflect.Whitebox;

import junit.framework.TestCase;

import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.unittestutil.UnittestUtil;

/**
 * TestCliClientConfig.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-19
 */
public class TestCliClientConfig extends TestCase{
	private CliClientConfig cliClientConfig = new CliClientConfig();
	private UserProfile userProfile = UnittestUtil.createUserProfile();
	
	public void testGetCliClientConfig(){
		boolean result;
		userProfile.setCarrier("carrier0");
		result = CliClientConfig.getCliClientConfig(userProfile, "type0");
		assertFalse(result);
		
		result = CliClientConfig.getCliClientConfig(userProfile, "");
		assertTrue(result);
		
		result = CliClientConfig.getCliClientConfig(userProfile, "type2");
		assertFalse(result);
		
		result = CliClientConfig.getCliClientConfig(null, "");
		assertTrue(result);
	}
	
	//this method must be executed at last
	public void testGetCliClientHolder4fail(){
		Whitebox.setInternalState(CliClientConfig.class, "CONFIG_FILE", "cli_client.xml132");
		try {
			Whitebox.invokeMethod(CliClientConfig.class, "getCliClientHolder");
		} catch (Exception e) {
			UnittestUtil.printExceptionMsg(e);
		}
	}
	
	public void testCliClientConfigKey(){
		//prepare
		CliClientConfigKey key = new CliClientConfigKey();
		key.setCarrier("c");
		key.setType("t");
		//itself and other type object
		assertFalse(key.equals(cliClientConfig));
		assertTrue(key.equals(key));
		
		//same type
		CliClientConfigKey key_false = new CliClientConfigKey();
		key_false.setCarrier("c1");
		key_false.setType("t1");
		CliClientConfigKey key_true = new CliClientConfigKey();
		key_true.setCarrier("c");
		key_true.setType("t");
		
		assertFalse(key.equals(key_false));
		assertTrue(key.equals(key_true));
	}

}
