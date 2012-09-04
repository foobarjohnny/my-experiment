/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.executor.impl;

import java.util.ArrayList;
import java.util.List;

import org.powermock.api.easymock.PowerMock;

import junit.framework.TestCase;

import com.telenav.cserver.framework.handler.DataHandler;

/**
 * TestDefaultResponseDataHandlerManager.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-4-27
 */
public class TestDefaultResponseDataHandlerManager extends TestCase{
	private DefaultResponseDataHandlerManager defaultResponseDataHandlerManager = new DefaultResponseDataHandlerManager();
	private List<ResponseDataHandlerItem> handlerItems = new ArrayList<ResponseDataHandlerItem> ();
	String handlerType = "handlerType";
	String anotherhandlerType = "anotherhandlerType";
	@Override
	protected void setUp() throws Exception {
		//=============================construct List<DataHandler>===========================
		//				construct first List<DataHandler>
		List<DataHandler> dataHandlerList = new ArrayList<DataHandler> ();
		for(int i=0;i<2;i++){
			dataHandlerList.add(PowerMock.createMock(DataHandler.class));
		}
		//				construct second List<DataHandler>
		List<DataHandler> anotherdataHandlerList = new ArrayList<DataHandler> ();
		for(int i=0;i<5;i++){
			anotherdataHandlerList.add(PowerMock.createMock(DataHandler.class));
		}
		//=========================end of construct List<DataHandler>===========================
		
		//construct ResponseDataHandlerItem
		ResponseDataHandlerItem item = new ResponseDataHandlerItem();
		item.setHandlerType(handlerType);
		item.setHandlers(dataHandlerList);
		
		ResponseDataHandlerItem anotheritem = new ResponseDataHandlerItem();
		anotheritem.setHandlerType(anotherhandlerType);
		anotheritem.setHandlers(anotherdataHandlerList);
		//add item to List<ResponseDataHandlerItem> handlerItems
		handlerItems.add(item);
		handlerItems.add(anotheritem);
	}
	public void testSetHandlerList()throws Exception{
		defaultResponseDataHandlerManager.setHandlerList(handlerItems);
		PowerMock.replayAll();
		List<DataHandler> results = defaultResponseDataHandlerManager.getDataHandlers(handlerType);
		List<DataHandler> anotherresults = defaultResponseDataHandlerManager.getDataHandlers(anotherhandlerType);
		PowerMock.verifyAll();
		assertEquals(2,results.size());
		assertEquals(5,anotherresults.size());
	}
	
	public void testAddDataHandlers()throws Exception{
		DataHandler dataHandler = PowerMock.createMock(DataHandler.class);
		defaultResponseDataHandlerManager.setHandlerList(handlerItems);
		PowerMock.replayAll();
		defaultResponseDataHandlerManager.addDataHandlers(handlerType, dataHandler);
		defaultResponseDataHandlerManager.addDataHandlers(anotherhandlerType, dataHandler);
		PowerMock.verifyAll();
		
		List<DataHandler> results = defaultResponseDataHandlerManager.getDataHandlers(handlerType);
		List<DataHandler> anotherresults = defaultResponseDataHandlerManager.getDataHandlers(anotherhandlerType);
		assertEquals(3,results.size());
		assertEquals(6,anotherresults.size());
		
	}

}
