package com.telenav.cserver.framework.management.jmx;

import junit.framework.Assert;

import org.junit.Test;

public class TestExceptionUtil {

	@Test
	public void testCollectExceptionMsg() {
		Assert.assertNotNull(ExceptionUtil.collectExceptionMsg(new Throwable("Unit Test Message", new Throwable("Unit Test Cause"))));
		Assert.assertEquals("", ExceptionUtil.collectExceptionMsg(null));

		Assert.assertNotNull(new ExceptionUtil()); // for code Coverage
	}
}
