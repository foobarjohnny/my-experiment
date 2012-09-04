/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.datatypes.addresssharing;
import junit.framework.Assert;
import junit.framework.TestCase;



/**
 * TestContactInfo.java
 *
 * jhjin@telenav.cn
 * @version 1.0 2011-5-6
 *
 */
public class TestContactInfo extends TestCase
{
    public void testGetFirstAndLastName4Normal()
    {
        ContactInfo contact = new ContactInfo();
        contact.setName("jin,junhang");
        Assert.assertEquals("jin", contact.getFirstName());
        Assert.assertEquals("junhang", contact.getLastName());
    }
    
    public void testGetFirstAndLastName4NoNameSplit()
    {
        ContactInfo contact = new ContactInfo();
        contact.setName("jin junhang");
        Assert.assertEquals("jin junhang", contact.getFirstName());
        Assert.assertEquals("", contact.getLastName());
    }
    
    public void testGetFirstAndLastName4NullName()
    {
        ContactInfo contact = new ContactInfo();
        contact.setName(null);
        Assert.assertEquals("", contact.getFirstName());
        Assert.assertEquals("", contact.getLastName());
    }
}
