/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.matcher;

import org.apache.commons.lang.StringUtils;
import org.easymock.IArgumentMatcher;
import org.powermock.reflect.Whitebox;

/**
 * AbstractMatcher.java
 *
 * jhjin@telenav.cn
 * @version 1.0 2011-6-13
 *
 */
public abstract class AbstractMatcher<T> implements IArgumentMatcher
{
    protected T expected = null;
    public AbstractMatcher(T expected)
    {
        this.expected = expected;
    }
    
    public T getExpected()
    {
        return expected;
    }

    @Override
    public void appendTo(StringBuffer arg0)
    {

    }

    @Override
    public boolean matches(Object object)
    {
        if( expected == null && object == null )
            return true;
        if( expected == null || object == null )
            return false;
        if( object.getClass() != expected.getClass() )
            return false;
        return doMatch((T)object);
    }
    
    public static boolean equals(Object obj1, Object obj2, String[] fieldNames)
    {
        if( obj1.getClass() != obj2.getClass() )
            return false;
        
        Class clazz = obj1.getClass();
        
        try
        {
            for (String fieldName : fieldNames)
            {
                if( !StringUtils.equals((String) (Whitebox.getField(clazz, fieldName).get(obj1)), (String) (Whitebox.getField(clazz, fieldName).get(obj2))) )
                        return false;
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
        
        return true;
    }
    
    abstract boolean doMatch(T actual);

}
