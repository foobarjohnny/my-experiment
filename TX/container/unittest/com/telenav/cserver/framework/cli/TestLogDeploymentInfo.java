package com.telenav.cserver.framework.cli;

import junit.framework.Assert;

import org.junit.Test;

public class TestLogDeploymentInfo {

	@Test
	public void testStatic() {
		LogDeploymentInfo.logInfo();
		Assert.assertNotNull(LogDeploymentInfo.getVersion());
	}

}
