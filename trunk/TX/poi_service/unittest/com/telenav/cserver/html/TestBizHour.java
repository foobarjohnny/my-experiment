/**
* (c) Copyright 2012 TeleNav.
* All Rights Reserved.
*/
package com.telenav.cserver.html;

import java.text.ParseException;

import com.telenav.content.bizhour.BizHour;
import com.telenav.content.bizhour.BizStatusEnum;

import junit.framework.TestCase;

/**
 * @TODO
 * @author  panzhang@telenav.cn
 * @version 1.0 2012-8-6
 */

public class TestBizHour extends TestCase{
    
	String businessHours = "";
	String OlsonTimezone = "";
	protected void setUp() throws Exception
    {
		OlsonTimezone = "America/New_York";
		businessHours = "0@0:00AM@1:30AM|0@10:30AM@11:59PM|1@0:00AM@1:00AM|1@10:30AM@11:59PM|2@0:00AM@1:00AM|2@10:30AM@11:59PM|3@0:00AM@1:00AM|3@10:30AM@11:59PM|4@0:00AM@1:00AM|4@10:30AM@11:59PM|5@0:00AM@1:00AM|5@10:30AM@11:59PM|6@0:00AM@1:30AM|6@10:30AM@11:59PM";
    }
	
	/**
	 * 
	 */
	public void testBizHour()
	{
		BizHour bizhour;
		try {
			bizhour = new BizHour(businessHours);
			boolean isOpen = false;
			if(BizStatusEnum.OPEN == bizhour.getBizStatus(OlsonTimezone))
			{
				isOpen = true;
			}
			businessHours = bizhour.getDayBizHourStr();
			String[] weeklyHour = bizhour.getWeekBizHourStr();
			for(int i=0;i<weeklyHour.length;i++){
				System.out.println("--" + i + weeklyHour[i]);
			}
			
			System.out.println("--isOpen" + isOpen);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

