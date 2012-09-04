package com.telenav.cserver.service.socket;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;

import com.sun.grizzly.Context;

public class TestDefaultFilter {
	@Test
	public void testExecute() throws IOException {
		DefaultFilter filter = new DefaultFilter();
		Assert.assertFalse(filter.execute(new Context()));
	}

	@Test
	public void testPostExecute() throws IOException {
		DefaultFilter filter = new DefaultFilter();
		Assert.assertTrue(filter.postExecute(new Context()));
	}
}
