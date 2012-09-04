/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.common.dispatcher;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.cserver.common.dispatcher.Dispatcher.DispatcherItem;
import com.telenav.cserver.framework.configuration.Configurator;

/**
 * TestAbstractDispatcherFactory.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-17
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Configurator.class})
public class TestAbstractDispatcherFactory extends TestCase{
	private TestableClass tc = new TestableClass();
	private Dispatcher dispatcher = new Dispatcher();
	
	
	public void testCreateDispatcher() throws Exception{
		// prepare and replay
		PowerMock.mockStatic(Configurator.class);
		EasyMock.expect(Configurator.getObject("",null)).andReturn(dispatcher);
		PowerMock.replayAll();
		
		// invoke method and verify
		Dispatcher result;
		result = tc.createDispatcher();
		
		PowerMock.verifyAll();
		
		//assert
		assertEquals(dispatcher,result);
	}
	
	public void testCreateDispatcher_fail() throws Exception{
		Dispatcher result;
		result = tc.createDispatcher();
		
		//assert
		assertNull(result);
	}
	
	public void testDispatcherSetAndGet(){
		String region = "china";
		DispatcherItem dispatcherItem = new DispatcherItem ();
		dispatcherItem.getUrl();
		dispatcherItem.setUrl("http://www.telenav.cn");
		dispatcherItem.getTimeout();
		dispatcherItem.setTimeout(8);
		
		Map<String, DispatcherItem> map = new HashMap<String, DispatcherItem>();
		map.put(region, dispatcherItem);
		dispatcher.getDispatcherSettings();
		dispatcher.setDispatcherSettings(map);
		dispatcher.getUrl(region);
		dispatcher.getTimeout(region);

	}
	private class TestableClass extends AbstractDispatcherFactory{
		@Override
		public String getConfigPath() {
			return "";
		}
		@Override
		public String getObjectName() {
			return null;
		}
	}
}
