package com.telenav.cserver.framework.management.jmx;

import junit.framework.Assert;

import org.junit.Test;

public class TestDetectResult {

	@Test
	public void testToJSON() {
		DetectResult detectResult = new DetectResult();
		detectResult.setName("UnitTest");
		Assert.assertNotNull(detectResult.toJSON());

		DetectResult detectResult2 = new DetectResult("UnitTest");
		Assert.assertNotNull(detectResult2);
	}
}
