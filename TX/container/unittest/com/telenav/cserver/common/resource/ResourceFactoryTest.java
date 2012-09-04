/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource;

import java.util.ArrayList;
import java.util.List;

import com.telenav.cserver.common.resource.constant.HolderType;
import com.telenav.cserver.common.resource.device.DevicePropertiesHolder;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.kernel.util.datatypes.TnContext;

import junit.framework.TestCase;

/**
 * ResourceFactoryTest.java
 * 
 * jhjin@telenav.cn
 * 
 * @version 1.0 2010-6-7
 * 
 */
public class ResourceFactoryTest extends TestCase
{

    public void testGetConfigSuffixForFail()
    {
        try
        {
            // throw NullPointerException
            ResourceFactory.getConfigSuffix(null, null, null);
        }
        catch (NullPointerException npe)
        {
            return;
        }
        fail();

    }
    
    public void testGetConfigSuffixForSuccessful()
    {
        List<LoadOrder> orders = new ArrayList<LoadOrder>();
        LoadOrder loadOrder1 = new LoadOrder1();
        LoadOrder loadOrder2 = new LoadOrder2();
        orders.add(loadOrder1);
        orders.add(loadOrder2);
        String result = ResourceFactory.getConfigSuffix(orders,orders.size()-1,null, null);
        assertEquals(ResourceFactory.SEPARATOR+loadOrder1.getType()+ResourceFactory.SEPARATOR+loadOrder2.getType(), result);
    }
 
    
    public void testCreateResourceForSuccessful2()
    {
        DevicePropertiesHolder dpHolder = ResourceHolderManager.getResourceHolder(HolderType.DEVICE_TYPE);
        Object result = ResourceFactory.createResource(dpHolder, createUserProfile(), null);
        assertNotNull(result);
    }
    
    public void testCreateResourceForSuccessfulResemb()
    {
        DevicePropertiesHolder dpHolder = ResourceHolderManager.getResourceHolder(HolderType.DEVICE_TYPE);
        Object result = ResourceFactory.createResource(dpHolder, createUserProfileForResem(), null);
        assertNotNull(result);
    }
    
    public void testCreateResourceForSuccessfulResembPtn()
    {
        DevicePropertiesHolder dpHolder = ResourceHolderManager.getResourceHolder(HolderType.DEVICE_TYPE);
        Object result = ResourceFactory.createResource(dpHolder, createUserProfileForResem(), null);
        assertNotNull(result);
    }
    
    private UserProfile createUserProfile()
    {
        UserProfile userProfile = new UserProfile();
        userProfile.setCarrier("ATT");
        userProfile.setPlatform("RIM");
        userProfile.setVersion("6_0_01");
        userProfile.setProduct("ATT_NAV");
        userProfile.setDevice("9000");
        userProfile.setScreenWidth("420-800");
        userProfile.setScreenHeight("800-420");
        return userProfile;
    }
    
    private UserProfile createUserProfileForResem()
    {
        UserProfile userProfile = new UserProfile();
        userProfile.setCarrier("ATT");
        userProfile.setPlatform("RIM");
        userProfile.setVersion("6_0_01");
        userProfile.setProduct("ATT_NAV");
        userProfile.setDevice("9999");
        userProfile.setScreenWidth("420-800");
        userProfile.setScreenHeight("800-420");
        userProfile.setMin("40844458578");
        return userProfile;
    }
    

    class LoadOrder1 extends LoadOrder
    {
        public LoadOrder1()
        {
            setType("loadorder1");
        }

        @Override
        public String getAttributeValue(UserProfile profile, TnContext tnContext)
        {
            return getType();
        }

    }

    class LoadOrder2 extends LoadOrder
    {
        public LoadOrder2()
        {
            setType("loadorder2");
        }

        @Override
        public String getAttributeValue(UserProfile profile, TnContext tnContext)
        {
            return getType();
        }

    }
    
   
    class TestResourceHolder extends AbstractResourceHolder
    {

        @Override
        public ResourceContent createObject(String key, UserProfile profile, TnContext tnContext)
        {
            return new ResourceContent();
        }
        
    }
    
   

}
