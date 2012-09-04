package com.telenav.cserver.framework.management.jmx;

import org.junit.Test;

public class TestLogLevelModifier {

	@Test
	public void testModify() {
		LogLevelModifier logLevelModifier = new LogLevelModifier();
		logLevelModifier.modify("debug", "unittest");

		// no exception is OK
	}
}
