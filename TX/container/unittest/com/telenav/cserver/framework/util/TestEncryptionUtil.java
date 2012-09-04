/**
 * (c) Copyright 2011 TeleNav. All Rights Reserved.
 */
package com.telenav.cserver.framework.util;

import junit.framework.Assert;

import org.junit.Test;

import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.executor.ExecutorException;

/**
 * TestEncryptionUtil
 * 
 * @author kwwang
 * 
 */
public class TestEncryptionUtil {

	@Test
	public void decryptByPtnSourceWithPtnIsFromCserver()
			throws ExecutorException {
		Assert.assertEquals("3817799999", EncryptionUtil.decryptByPtnSource(
				"%A8%3Aqy%7C%60X%17%E3%DFg%DB%09%C0%FBc",
				UserProfile.PTN_SOURCE_FROM_TELENAV_CSERVER));
		Assert.assertEquals("9884995", EncryptionUtil.decryptByPtnSource(
				"%82%7D%F3%A2%AE%16%EDv",
				UserProfile.PTN_SOURCE_FROM_TELENAV_CSERVER));
	}

	@Test(expected = ExecutorException.class)
	public void decryptByPtnSourceWithPtnIsFromCserverHasException()
			throws ExecutorException {
		Assert.assertEquals("", EncryptionUtil.decryptByPtnSource("1234452",
				UserProfile.PTN_SOURCE_FROM_TELENAV_CSERVER));
		Assert.assertEquals("", EncryptionUtil.decryptByPtnSource("3457456",
				UserProfile.PTN_SOURCE_FROM_TELENAV_CSERVER));
	}

	@Test
	public void decryptByPtnSourceWithPtnIsFromCserverExcpetionalCase()
			throws ExecutorException {
		Assert.assertEquals(null, EncryptionUtil.decryptByPtnSource("",
				UserProfile.PTN_SOURCE_FROM_TELENAV_CSERVER));
		Assert.assertEquals(null, EncryptionUtil.decryptByPtnSource(null,
				UserProfile.PTN_SOURCE_FROM_TELENAV_CSERVER));
	}

	@Test
	public void decryptByPtnSourceWithPtnIsFromSimCard()
			throws ExecutorException {
		Assert.assertEquals("3817799999", EncryptionUtil.decryptByPtnSource(
				"3817799999", UserProfile.PTN_SOURCE_FROM_SIM_CARD));
		Assert.assertEquals("9884995", EncryptionUtil.decryptByPtnSource(
				"9884995", UserProfile.PTN_SOURCE_FROM_SIM_CARD));
		Assert.assertEquals("", EncryptionUtil.decryptByPtnSource("",
				UserProfile.PTN_SOURCE_FROM_SIM_CARD));
		Assert.assertEquals(null, EncryptionUtil.decryptByPtnSource(null,
				UserProfile.PTN_SOURCE_FROM_SIM_CARD));
	}

	@Test
	public void decryptByPtnSourceWithPtnIsFromUserInput()
			throws ExecutorException {
		Assert.assertEquals("3817799999", EncryptionUtil.decryptByPtnSource(
				"3817799999", UserProfile.PTN_SOURCE_FROM_USER_INPUT));
		Assert.assertEquals("9884995", EncryptionUtil.decryptByPtnSource(
				"9884995", UserProfile.PTN_SOURCE_FROM_USER_INPUT));
		Assert.assertEquals("", EncryptionUtil.decryptByPtnSource("",
				UserProfile.PTN_SOURCE_FROM_USER_INPUT));
		Assert.assertEquals(null, EncryptionUtil.decryptByPtnSource(null,
				UserProfile.PTN_SOURCE_FROM_USER_INPUT));
	}
}
