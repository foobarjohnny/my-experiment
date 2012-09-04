package com.telenav.cserver.common.resource;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class TestMonitorObject {

	@Test
	public void testName() {
		MonitorObject object = new MonitorObject();

		object.setName("UnitTest");
		Assert.assertEquals("UnitTest", object.getName());
	}

	@Test
	public void testData() {
		MonitorObject object = new MonitorObject();

		Map<String, String> data = new HashMap<String, String>();
		data.put("UnitTestKey", "UnitTestValue");

		object.setData(data);
		Assert.assertEquals(data, object.getData());
	}
}
