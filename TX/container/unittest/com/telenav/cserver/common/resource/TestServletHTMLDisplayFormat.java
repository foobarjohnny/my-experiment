/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.common.resource;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.powermock.reflect.Whitebox;

import com.telenav.cserver.common.resource.holder.impl.BillingConfHolder;

/**
 * TestServletHTMLDisplayFormat.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-13
 */
public class TestServletHTMLDisplayFormat extends TestCase{
	private ResourceCacheManagement cacheManagement = ResourceCacheManagement.getInstance();
	private ServletHTMLDisplayFormat servletHTMLDisplayFormat = new ServletHTMLDisplayFormat(cacheManagement);
	private BillingConfHolder bholder = new BillingConfHolder();
	
	private final static String holderName = "BillingConfHolder";
	private final static String holderType = "Bill";
	
	private void initHolder(){
		cacheManagement.clear();
		bholder.setName(holderName);
		bholder.setType(holderType);
		cacheManagement.addHolder(bholder);
	}
	public void testGetHttpMethod(){
		assertEquals("GET",servletHTMLDisplayFormat.getHttpMethod());
	}
	public void testGetContentsAction(){
		assertEquals("resource-management",servletHTMLDisplayFormat.getContentsAction());
	}
	public void testGetForm(){
		Map<String, String> args = new HashMap<String,String>();
		StringBuffer result;
		result = servletHTMLDisplayFormat.getForm("/testAction","This is a test action", args);
		System.out.println(result.toString());
		assertEquals("<table><form name=\"invokeBean\" action=\"/testAction\" method=\"GET\"/><tr><td align=right><input type=\"hidden\" name=\"operation\" value=\"contents\"></input><input  type=\"submit\" name=\"op_submit\" value=\"This is a test action\"></input></td></tr></form></table>"
                     ,result.toString());
	}
	public void testGetInputHiddenItem(){
		String result;
		result = servletHTMLDisplayFormat.getInputHiddenItem("a-key", "value of a-key");
		assertEquals("<input type=\"hidden\" name=\"a-key\" value=\"value of a-key\"></input>",result);
	}
	public void testContents(){
		String result;
		result = servletHTMLDisplayFormat.contents("123", null);
		//assertEquals("<html><body>123 is not found!</body></html>",result);
		assertEquals("<html><body>can't find null = [null]!</body></html>",result);
	}
	public void testDetails(){
		initHolder();
		String result;
		result = servletHTMLDisplayFormat.details();
		assertEquals("<html><body><table border=1 align=center><tr><td align=right>Statistic</td></tr><tr><td align=right>Holder Type:</td><td align=right>1</td></tr><tr><td align=right>Holders:</td><td align=right>1</td></tr><tr><td align=right>Counter Of Object</td><td align=right>0</td></tr><tr><td align=right>Memory:</td><td align=right>0Byte</td></tr><tr></tr><tr><td align=right>Holder</td></tr><tr><td align=right>Holder Name</td><td align=right>Holder Type</td><td align=right>Counter Of Object</td><td align=right>Memory</td><td align=right>Details</td></tr><tr><td align=right>BillingConfHolder</td><td align=right>BillingConfHolder</td><td align=right>0</td><td align=right>0Byte</td><td align=right><table></table></td></tr></table></body></html>"
				,result);
	}
	public void testHtmlPage() throws Exception{
		String result;
		result = Whitebox.invokeMethod(servletHTMLDisplayFormat, "htmlPage", "abc");
		assertEquals("<html><body>abc</body></html>",result);
	}
}
