/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.executor.protocol.txnode;

import java.lang.reflect.Method;

import junit.framework.TestCase;

/**
 * TxNodeRequestParserTest.java
 *
 * jhjin@telenav.cn
 * @version 1.0 2010-10-19
 *
 */
public class TxNodeRequestParserTest extends TestCase
{
    
    public void testGetGuideToneName() throws Exception
    {
        TxNodeRequestParser parser = new TxNodeRequestParser();
        Method method = TxNodeRequestParser.class.getDeclaredMethod("getGuideToneName", String.class);
        method.setAccessible(true);
        assertEquals("202",method.invoke(parser, "202"));
        assertEquals(null, method.invoke(parser, "203"));
        assertEquals("male",method.invoke(parser, "204"));
        assertEquals("yes",method.invoke(parser, "1,yes"));
        assertEquals("202",method.invoke(parser, "202,"));
        assertEquals("abc,4",method.invoke(parser, "11,abc,4"));
        //assertEquals("4",method.invoke(parser, ",4")); there is no such case, just remove it.
        assertEquals(null,method.invoke(parser, "11,"));
        assertEquals(null,method.invoke(parser, ","));
        assertEquals(null,method.invoke(parser, ""));
        assertEquals(null,method.invoke(parser, new Object[]{null}));
        
    }
    
    public void testGetGuideToneNameWhenItsNotDigit() throws Exception
    {
        TxNodeRequestParser parser = new TxNodeRequestParser();
        Method method = TxNodeRequestParser.class.getDeclaredMethod("getGuideToneName", String.class);
        method.setAccessible(true);
        assertEquals("female",method.invoke(parser, "female"));
        assertEquals("female",method.invoke(parser, "-1,female"));
        assertEquals("male",method.invoke(parser, "male"));
        assertEquals(null,method.invoke(parser, ""));
        assertEquals(null,method.invoke(parser, new Object[]{null}));
        
    }

}
