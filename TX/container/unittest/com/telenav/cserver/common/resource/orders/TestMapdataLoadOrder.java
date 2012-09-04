/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.common.resource.orders;

import junit.framework.TestCase;

import com.telenav.cserver.unittestutil.UTConstant;
import com.telenav.cserver.unittestutil.UnittestUtil;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * TestMapdataLoadOrder.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-4-28
 */
public class TestMapdataLoadOrder extends TestCase{
	
	private MapdataLoadOrder mapdataLoadOrder = new MapdataLoadOrder();
	private String result;
	public void testGetAttributeValue() {
		TnContext tnContext = UnittestUtil.createTnContext();
		
		result = mapdataLoadOrder.getAttributeValue(null,tnContext);
		assertEquals(UTConstant.TnContext_PROP_MAP_DATASET, result);
		
	}

}
