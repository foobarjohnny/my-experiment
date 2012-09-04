/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.class4test;

import com.telenav.cserver.framework.UserProfile;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * TestBean.java
 *
 * jhjin@telenav.cn
 * @version 1.0 2010-6-23
 *
 */
public class SampleBean{
	public static UserProfile userProfile;
	public final TnContext tnContext = null;
    private String value;

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }
    
    public void methodWithException() throws Exception{
    	throw new Exception("");
    }

    @Override
    public String toString()
    {
        return value;
    }
    
    
}