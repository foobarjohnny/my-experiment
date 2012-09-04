/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.service.socket;

import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sun.grizzly.util.SelectionKeyAttachment;

/**
 * TestTelenavSelectionKeyHandler.java
 * 
 * xljiang@telenav.cn
 * 
 * @version 1.0 2011-5-27
 */
public class TestTelenavSelectionKeyHandler {

	private TelenavSelectionKeyHandler handler = null;
	private MockSelectionKey key = null;
	private MockSelectionKey key2 = null;
	private MockSelectionKey key3 = null;
	private MockSelectionKey key4 = null;

	@Before
	public void setUp() {
		key = new MockSelectionKey();
		key2 = new MockSelectionKey();
		key3 = new MockSelectionKey();
		key4 = new MockSelectionKey();
		handler = new TelenavSelectionKeyHandler();
	}

	@Test
	public void testRegister() throws ClosedChannelException {
		handler.register(key, SelectionKey.OP_ACCEPT);

		handler.register(key, System.currentTimeMillis());

		key.setValid(true);
		handler.register(key, SelectionKey.OP_ACCEPT);

		List<SelectionKey> keys = new ArrayList<SelectionKey>();
		key.setValid(true);
		key2.setValid(true);
		keys.add(key);
		keys.add(key2);
		keys.add(key3);
		keys.add(key4);
		handler.register(keys.iterator(), SelectionKey.OP_ACCEPT);
	}

	@Test
	public void testProcess() {
		Long attach = System.currentTimeMillis();
		key.attach(attach);
		handler.process(key);

		key2.attach(new MockSelectionKeyAttachment());
		handler.process(key2);
	}

	@Test
	public void testPostProcess() {
		handler.postProcess(key);

		key2.attach(new MockSelectionKeyAttachment());
		handler.postProcess(key2);
	}

	@Test
	public void testTimeout() {
		handler.setTimeout(3000);
		Assert.assertEquals(3000, handler.getTimeout());
	}

	@Test
	public void testExpire() {
		handler.expire(key, System.currentTimeMillis());

		List<SelectionKey> keys = new ArrayList<SelectionKey>();
		key2.setValid(true);
		key3.setValid(true);
		key4.setValid(true);
		key3.attach(new Long(System.currentTimeMillis()));
		key4.attach(new MockSelectionKeyAttachment());
		keys.add(key);
		keys.add(key2);
		keys.add(key3);
		keys.add(key4);
		handler.expire(keys.iterator());
	}

	@Test
	public void testClearKeyAttachment() {
		Long attach = System.currentTimeMillis();
		key.attach(attach);
		Long result = (Long) handler.clearKeyAttachment(key);
		Assert.assertEquals(attach, result);

		key2.attach(new MockSelectionKeyAttachment());
		Object result2 = handler.clearKeyAttachment(key2);
		Assert.assertTrue(result2 instanceof MockSelectionKeyAttachment);
	}

	/**
	 * Mock com.sun.grizzly.util.SelectionKeyAttachment for Unit Test use.
	 * 
	 * @author gwwang
	 * 
	 */
	class MockSelectionKeyAttachment extends SelectionKeyAttachment {
	}

	/**
	 * Mock java.nio.channels.SelectionKey for Unit Test use.
	 * 
	 * @author gwwang
	 * 
	 */
	class MockSelectionKey extends SelectionKey {

		private boolean valid = false;

		private int interestOps = 0;

		private int readyOps = 0;

		public void setValid(boolean valid) {
			this.valid = valid;
		}

		public void setInterestOps(int interestOps) {
			this.interestOps = interestOps;
		}

		public void setReadyOps(int readyOps) {
			this.readyOps = readyOps;
		}

		@Override
		public SelectableChannel channel() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Selector selector() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean isValid() {
			return valid;
		}

		@Override
		public void cancel() {
			// do nothing
		}

		@Override
		public int interestOps() {
			return interestOps;
		}

		@Override
		public SelectionKey interestOps(int ops) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int readyOps() {
			return readyOps;
		}
	}
}
