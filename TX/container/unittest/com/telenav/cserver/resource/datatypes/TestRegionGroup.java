/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.resource.datatypes;

import junit.framework.Assert;

import org.junit.Test;

/**
 * TestRegionGroup
 * 
 * @author jzhu
 * 
 */
public class TestRegionGroup
{
    protected RegionGroup regionGroup;

    @Test
    public void naGroup()
    {
        String group = RegionGroup.getRegionGroup("US");

        Assert.assertEquals(RegionGroup.REGION_GROUP_NA, group);
    }

    @Test
    public void euGroup()
    {
        String group = RegionGroup.getRegionGroup("GB");

        Assert.assertEquals(RegionGroup.REGION_GROUP_EU, group);

    }

    @Test
    public void noGroup()
    {
        String noGroup = "CN";
        String group = RegionGroup.getRegionGroup(noGroup);

        Assert.assertEquals(noGroup, group);

    }
    
    @Test
    public void saGroup()
    {
        String group = RegionGroup.getRegionGroup("BR");

        Assert.assertEquals(RegionGroup.REGION_GROUP_SA, group);

    }
}
