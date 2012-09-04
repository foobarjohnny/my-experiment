/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.movie.html.datatypes;

import junit.framework.TestCase;

/**
 * TestScheduleItem.java
 * @TODO
 * @author  xljiang@telenav.cn
 * @version 1.0 2011-9-27
 */

public class TestScheduleItem extends TestCase{
	private ScheduleItem scheduleItem = new ScheduleItem();
	
	public void testFormatShowTimes(){
		String ret = scheduleItem.formatShowTimes();
		
		assertEquals("",ret);
	}
	
	public void testGetLastestShowTime(){
		
	}
}
