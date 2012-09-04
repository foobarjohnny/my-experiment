/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.message;

import java.lang.reflect.Field;
import java.util.Map;

import com.telenav.cserver.common.resource.TestAbstractHolder;
import com.telenav.cserver.common.resource.ResourceHolderManager;
import com.telenav.cserver.common.resource.constant.HolderType;

import junit.framework.TestCase;

/**
 * MessagesHolderTest.java
 * 
 * jhjin@telenav.cn
 * 
 * @version 1.0 2010-6-7
 * 
 */
public class MessagesHolderTest extends TestAbstractHolder
{

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        System.out.println(System.getProperties());
    }

    public void testMessagesHolder()
    {
        MessagesHolder holder = ResourceHolderManager.getResourceHolder(HolderType.MESSAGE_TYPE);
        Messages messages = holder.getMessages(createUserProfile(), null);
        assertEquals(3, getMapFrom(messages).size());
    }

    private Map getMapFrom(Messages messages)
    {
        try
        {
            Field field = Messages.class.getDeclaredField("map");
            field.setAccessible(true);
            return (Map) field.get(messages);
        }
        catch (NoSuchFieldException e)
        {
            return null;
        }
        catch (IllegalAccessException e)
        {
            return null;
        }
    }

}
