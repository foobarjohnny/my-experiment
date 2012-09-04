/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.common.resource.message;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.cserver.common.resource.ResourceContent;
import com.telenav.cserver.common.resource.ResourceFactory;

/**
 * TestMessagesHolder.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-4-28
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ResourceFactory.class,MessagesHolder.class})
public class TestMessagesHolder extends TestCase{
	private MessagesHolder messagesHolder = new MessagesHolder();
	private static String CONTENT_KEY = "messages";
	private Messages messages = new Messages(null);
	private Map result = new HashMap();
	
	public void testCreateObject() throws Exception{
		
		PowerMock.mockStatic(ResourceFactory.class);
		EasyMock.expect(ResourceFactory.createResource(messagesHolder, null, null)).andReturn(result);
		PowerMock.expectNew(Messages.class, result).andReturn(messages);

		PowerMock.replayAll();
		
		ResourceContent rc = messagesHolder.createObject(null,null , null);
		
		PowerMock.verifyAll();
		Assert.assertTrue("the two object should be the same.",messages==rc.getProperty(CONTENT_KEY));
	}

}
