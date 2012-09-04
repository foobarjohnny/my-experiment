package com.telenav.cserver.poi;

import com.telenav.cserver.poi.protocol.PoiBrowserProtocolResponseFormatter;

import org.junit.Test;

import junit.framework.TestCase;

public class DistanceFormatterTest extends TestCase {
	public double[] vals;
	public String[] valStrs;

	/**
	 * test for distance formatter with different cases.
	 */
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		vals = new double[] { 1.63434, 2.0757457, 9.9499999999999,
				9.949999999999999999999999999, 9.95, 10.00, 10.4, 10.5 , 42.4};
		valStrs = new String[] { "1.6", "2.1", "9.9", "10", "10", "10", "10",
				"11" ,"42"};
	}

	@Test
	public void testFormatter() {
		for (int i = 0; i < vals.length; ++i) {
			assertEquals(valStrs[i], PoiBrowserProtocolResponseFormatter
					.distanceFormat(vals[i]));
			// System.out.println(PoiBrowserProtocolResponseFormatter.distanceFormat(val));
		}
	}
}
