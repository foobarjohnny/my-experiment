/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource;

import java.util.Properties;
import java.util.ResourceBundle;

import junit.framework.TestCase;

/**
 * ResourceUtilTest.java
 *
 * jhjin@telenav.cn
 * @version 1.0 2010-6-7
 *
 */
public class ResourceUtilTest extends TestCase
{
     
    
     public void testGetResourceFromResourceBundle2()
     {
        try{
           ResourceBundle rb = null;
           ResourceUtil.getResource(rb);
        }catch(NullPointerException npe)
        {
            return;
        }
        fail();
     }
    
     public void testGetResourceFromProperties2()
     {
         try{
           Properties properties = null;
           ResourceUtil.getResource(properties);
         }catch(NullPointerException npe)
         {
             return;
         }
         fail();
     }
     
     public void testSplit()
     {
         String str = "1;2;3;4;;5";
         String token =";";
         String[] result = ResourceUtil.split(str, token);
         assertEquals(5,result.length);
         
     }
}
