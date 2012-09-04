/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.common.encryption;

import junit.framework.TestCase;

import org.powermock.reflect.Whitebox;

import com.telenav.cserver.unittestutil.UnittestUtil;

/**
 * TestCipherUtil.java
 * 
 * xljiang@telenav.cn
 * 
 * @version 1.0 2011-5-17
 */
public class TestCipherUtil extends TestCase {

	// telenav encrypted code
	private static byte resultByte[] = { 96, -122, 54, 115, 14, -12, 52, -96 };
	private static final String STRING_ENCODING = "ISO-8859-1";

	@Override
	protected void setUp() throws Exception {
		Whitebox.invokeConstructor(CipherUtil.class);
	}

	public void testDecrypt() throws Exception {
		String result;
		String original = new String(resultByte, STRING_ENCODING);
		result = CipherUtil.decrypt(original);
		assertEquals("telenav", result);
	}

	public void testEncrypt() throws Exception {
		String result;
		result = CipherUtil.encrypt("telenav");

		String expect = new String(resultByte, STRING_ENCODING);

		assertEquals(expect, result);

		try {
			CipherUtil.encrypt(null);
		} catch (Exception e) {
			UnittestUtil.printExceptionMsg(e);
		}
	}

	public void testDecryptByteArray() {
		Object obj = null;
		try {
			Whitebox.invokeMethod(CipherUtil.class, "decryptByteArray", obj);
		} catch (Exception e) {
			UnittestUtil.printExceptionMsg(e);
		}
	}
}
