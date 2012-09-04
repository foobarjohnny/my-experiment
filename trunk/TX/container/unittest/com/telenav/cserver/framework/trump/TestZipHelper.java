/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.trump;

import java.io.File;

import com.telenav.cserver.unittestutil.UnittestUtil;

import junit.framework.TestCase;

/**
 * TestZipHelper.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-25
 */
public class TestZipHelper extends TestCase{
	private ZipHelper zipHelper = new ZipHelper();
	
	public void testUnzip(){
		boolean result = zipHelper.unZip("testZipHelper/testZipHelper.zip", "testZipHelper");
		
		assertTrue(result);
	}
	public void testUnzip_not_overWrite(){
		zipHelper.setOverrider(false);
		boolean result = zipHelper.unZip("testZipHelper/testZipHelper.zip", "testZipHelper");
		
		assertTrue(result);
	}
	public void testUnzip_false(){
		boolean b1,b2;
		b1 = zipHelper.unZip("testZipHelper/testZipHelper_noexists.zip", "testZipHelper");
		b2 = zipHelper.unZip("testZipHelper/testZipHelper.zip", "testZipHelper_no$exists");
		
		assertFalse(b1);
		assertFalse(b2);
	}
	/**
	 * for coverage rate
	 */
	public void testSimple() {
		ZipHelper zipHelper = new ZipHelper();
		zipHelper.isOverrider();
		zipHelper.setOverrider(true);
		//assert

		//no Exception is OK.
	}
}
