/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.util;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.telenav.livestats.client.LiveStatsPacket;
import com.telenav.livestats.client.LiveStatsPacketFactory;
import com.televigation.log.TVCategory;

/**
 * TestLiveStatsLogger.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-25
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({LiveStatsLogger.class})
@SuppressStaticInitializationFor({"com.telenav.cserver.framework.util.LiveStatsLogger"})
public class TestLiveStatsLogger extends TestCase{
	/* 
	 *
	 */
	@Override
	protected void setUp() throws Exception {
		Whitebox.setInternalState(LiveStatsLogger.class, "logger", TVCategory.getInstance(LiveStatsLogger.class));
	}
	/**
	 * factory == null
	 */
	public void testSend_fail1(){
		LiveStatsLogger liveStatsLogger = new LiveStatsLogger();
		Object nullObj = null;
		Whitebox.setInternalState(LiveStatsLogger.class, "factory", nullObj);
		liveStatsLogger.send();
	}
	/**
	 * app == null
	 */
	public void testSend_fail2(){
		LiveStatsLogger liveStatsLogger = new LiveStatsLogger();
		LiveStatsPacketFactory factory = PowerMock.createMock(LiveStatsPacketFactory.class);
		liveStatsLogger.setApp("");
		Whitebox.setInternalState(LiveStatsLogger.class, "factory", factory);
		liveStatsLogger.send();
	}
	/**
	 * ex1 != null
	 * @throws UnknownHostException 
	 */
	public void testSend1() throws UnknownHostException{
		//define variables
		LiveStatsLogger liveStatsLogger = new LiveStatsLogger();
		LiveStatsPacketFactory factory = PowerMock.createMock(LiveStatsPacketFactory.class);
		liveStatsLogger.setApp("app");
		liveStatsLogger.setEx1("ex1");
		Whitebox.setInternalState(LiveStatsLogger.class, "factory", factory);
		LiveStatsPacket packet = new ImplLiveStatsPacket();
		//prepare and replay
		EasyMock.expect(factory.createPacket()).andReturn(packet);
		PowerMock.replayAll();

		//invoke and verify

		liveStatsLogger.send();
		PowerMock.verifyAll();

		//assert

		//no Exception is OK.
	}
	/**
	 * ex1 == null
	 * @throws UnknownHostException 
	 */
	public void testSend2() throws UnknownHostException{
		//define variables
		LiveStatsLogger liveStatsLogger = new LiveStatsLogger();
		LiveStatsPacketFactory factory = PowerMock.createMock(LiveStatsPacketFactory.class);
		liveStatsLogger.setApp("app");
		liveStatsLogger.setEx1(null);
		Whitebox.setInternalState(LiveStatsLogger.class, "factory", factory);
		LiveStatsPacket packet = new ImplLiveStatsPacket();
		//prepare and replay
		EasyMock.expect(factory.createPacket()).andReturn(packet);
		PowerMock.replayAll();

		//invoke and verify

		liveStatsLogger.send();
		PowerMock.verifyAll();

		//assert

		//no Exception is OK.
	}
	/**
	 * for coverage rate
	 * @throws Exception
	 */
	public void testSimple(){
		LiveStatsLogger liveStatsLogger = new LiveStatsLogger();
		liveStatsLogger.getLiveStatsHost();
		liveStatsLogger.getLiveStatsPort();
		liveStatsLogger.getRequestCount();
		liveStatsLogger.getSuccessCount();
		liveStatsLogger.setSuccessCount("");
		liveStatsLogger.getDefaultCount();
		liveStatsLogger.setApp("");
		liveStatsLogger.setEx1("");
	}
	
	private class ImplLiveStatsPacket implements LiveStatsPacket{
		@Override
		public LiveStatsPacket add(String arg0, String arg1) {
			// TODO Auto-generated method stub
			return new ImplLiveStatsPacket();
		}
		@Override
		public List getEntries() {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public void send() throws IOException {
			// TODO Auto-generated method stub
			
		}
	}
}
