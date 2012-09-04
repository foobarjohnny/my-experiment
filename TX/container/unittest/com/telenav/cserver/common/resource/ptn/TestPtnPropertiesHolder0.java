/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.ptn;

import com.telenav.cserver.common.resource.TestAbstractHolder;
import com.telenav.cserver.common.resource.ResourceHolderManager;
import com.telenav.cserver.common.resource.constant.HolderType;
import com.telenav.cserver.framework.UserProfile;

/**
 * DevicePropertiesHolderTest.java
 *
 * jzhu@telenav.cn
 * @version 1.0 2010-9-3
 *
 */
public class TestPtnPropertiesHolder0 extends TestAbstractHolder
{
    
    public void testDevicePropertiesHolder()
    {
        PtnPropertiesHolder holder = ResourceHolderManager.getResourceHolder(HolderType.PTN_TYPE);
        UserProfile userProfile = createUserProfile();
        PtnProperties ptn = holder.getPtnProperties(userProfile,null);
        assertNotNull(ptn);
    }

}
