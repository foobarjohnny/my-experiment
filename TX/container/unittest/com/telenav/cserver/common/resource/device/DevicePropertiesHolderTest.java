/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.device;

import com.telenav.cserver.common.resource.AbstractHolderTest;
import com.telenav.cserver.common.resource.ResourceHolderManager;
import com.telenav.cserver.common.resource.constant.HolderType;
import com.telenav.cserver.framework.UserProfile;

/**
 * DevicePropertiesHolderTest.java
 *
 * jhjin@telenav.cn
 * @version 1.0 2010-6-7
 *
 */
public class DevicePropertiesHolderTest extends AbstractHolderTest
{
    
    public void testDevicePropertiesHolder()
    {
        DevicePropertiesHolder holder = ResourceHolderManager.getResourceHolder(HolderType.DEVICE_TYPE);
        UserProfile userProfile = createUserProfile();
        DeviceProperties dp = holder.getDeviceProperties(userProfile,null);
        assertEquals(10,dp.attributes.size());
    }

}
