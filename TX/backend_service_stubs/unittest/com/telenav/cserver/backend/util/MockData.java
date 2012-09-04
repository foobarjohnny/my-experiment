/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.util;

import com.telenav.cserver.backend.datatypes.Address;

/**
 * MockData.java
 *
 * @author bhu@telenav.cn
 * @version 1.0 2009-7-15
 */
public class MockData
{
    public static Address mockAddress_CN()
    {
        Address addr = new Address();
        addr.setLatitude(31.20367);
        addr.setLongitude(121.48893);
        return addr;
    }
	public static Address mockAddress()
	{
		Address addr = new Address();
		//addr.setLatitude(37.37392);
		//addr.setLongitude(-121.99934);
//		addr.setFirstLine("1130 kifer rd");
		
		addr.setLatitude(37.352245);
		addr.setLongitude(-122.014565);
		
		//addr.setLatitude(37.373919);
		//addr.setLongitude(-121.999303);
		//addr.setCityName("Sunnyvale");
		//addr.setState("CA");
		//addr.setCountry("US");
		//addr.setCountry("GB");
		return addr;
	}
	
	public static Address mockCity()
	{
		Address addr = new Address();
		addr.setCityName("beijing");
		addr.setState("beijing");
		addr.setCountry("CN");
		return addr;
	}
}
