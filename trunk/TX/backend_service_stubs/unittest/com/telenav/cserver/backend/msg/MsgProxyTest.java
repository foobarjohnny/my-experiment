package com.telenav.cserver.backend.msg;

import junit.framework.TestCase;

import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.kernel.util.datatypes.TnContext;

/*
 * Test Class for testing MsgProxy
 * @author Jianyu Zhou
 */
public class MsgProxyTest extends TestCase{

	private TnContext tc;
	
	private MsgProxy proxy;

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		proxy = MsgProxy.getInstance();
		tc = new TnContext();
	}

	/**
	 * Test method for sendEmail method under MsgProxy
	 * @throws ThrottlingException 
	 */
	public void testSendEmail() throws ThrottlingException
	{
		String fromEmail = "custService@telenav.com";
		String recipientEmail = "jianyuz@telenav.com";
		String subject = "Send Email Test";
		String emailBody = "The password of your account is 1234!";
		
		// start request
		proxy.sendEmail(tc, fromEmail, recipientEmail, subject, emailBody);
	}

}
