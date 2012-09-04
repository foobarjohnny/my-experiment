/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.common.resource.orders;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.powermock.api.easymock.PowerMock;

import junit.framework.TestCase;

import com.telenav.cserver.common.resource.LoadOrder;

/**
 * TestOrLoadOrder.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-4-28
 */
public class TestOrLoadOrder extends TestCase{
	private OrLoadOrder orLoadOrder = new OrLoadOrder();
	LoadOrder loadOrder;
	Object result;
	private List<LoadOrder> loadOrderList = new ArrayList<LoadOrder>();
	
	@Override
	protected void setUp() throws Exception {
		loadOrder = PowerMock.createMock(LoadOrder.class);
		
		loadOrderList.add(loadOrder);
		loadOrderList.add(loadOrder);
		loadOrderList.add(loadOrder);
		
		orLoadOrder.setLoadOrderList(loadOrderList);
	}
	public void testGetAttributeValue(){
		String returnValue = "returnValue4testGetAttributeValue";
		EasyMock.expect(loadOrder.getAttributeValue(null,null)).andReturn(returnValue);
		
		PowerMock.replayAll();
		result = orLoadOrder.getAttributeValue(null,null);
		
		PowerMock.verifyAll();
		assertEquals(returnValue,result);
	}
	
	public void testGetAttributeValue_null(){
		String returnValue = "";
		EasyMock.expect(loadOrder.getAttributeValue(null,null)).andReturn(returnValue).times(loadOrderList.size());
		
		PowerMock.replayAll();
		result = orLoadOrder.getAttributeValue(null,null);
		
		PowerMock.verifyAll();
		assertEquals(returnValue,result);
	}
	public void testGetAttributeValue_null1(){
		String returnValue = null;
		EasyMock.expect(loadOrder.getAttributeValue(null,null)).andReturn(returnValue).times(loadOrderList.size());
		
		PowerMock.replayAll();
		result = orLoadOrder.getAttributeValue(null,null);
		
		PowerMock.verifyAll();
		assertEquals("",result);
	}
	
	public void testGetAttributeValueList(){
		List<String> midList = new ArrayList<String>();
		midList.add("1");
		midList.add("2");
		midList.add("3");
		EasyMock.expect(loadOrder.getAttributeValueList(null,null)).andReturn(midList).anyTimes();
		
		PowerMock.replayAll();
		result = orLoadOrder.getAttributeValueList(null, null);
		
		PowerMock.verifyAll();
		assertEquals(((List<String>)result).size(), midList.size() * loadOrderList.size());
		assertEquals("[1, 2, 3, 1, 2, 3, 1, 2, 3]",((List<String>)result).toString());
	}
	
	public void testGetResemblanceFullPath(){
		List<String> midList = new ArrayList<String>();
		midList.add("1");
		midList.add("2");
		midList.add("3");
		EasyMock.expect(loadOrder.getResemblanceFullPath(null,null,null,null)).andReturn(midList).anyTimes();
		
		PowerMock.replayAll();
		result = orLoadOrder.getResemblanceFullPath(null, null,null,null);
		
		PowerMock.verifyAll();
		assertEquals(((List<String>)result).size(), midList.size() * loadOrderList.size());
		assertEquals("[1, 2, 3, 1, 2, 3, 1, 2, 3]",((List<String>)result).toString());
	}

}
