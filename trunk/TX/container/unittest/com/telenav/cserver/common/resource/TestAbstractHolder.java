/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource;

import com.telenav.cserver.framework.UserProfile;

import junit.framework.TestCase;

/**
 * AbstractHolderTest.java
 *
 * jhjin@telenav.cn
 * @version 1.0 2010-6-7
 *
 */
public class TestAbstractHolder extends TestCase
{

    @Override
    protected void setUp() throws Exception
    {
        ResourceFactory.init();
    }

    
    protected UserProfile createUserProfile()
    {
        UserProfile userProfile = new UserProfile();
        userProfile.setCarrier("ATT");
        userProfile.setPlatform("RIM");
        userProfile.setVersion("6_0_01");
        userProfile.setProduct("ATT");
        userProfile.setDevice("9000");
        return userProfile;
    }
    public void testUnknown(){
    	
    }
}
