package com.telenav.cserver.service.chunkhandler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;

@RunWith(PowerMockRunner.class)
public class TestChunkCallbackImpl {

	@Test
	public void testSetListener() {
		ChunkCallback impl = new ChunkCallbackImpl();
		ChunkReadListener listener = PowerMock.createMock(ChunkReadListener.class);

		PowerMock.replayAll();

		impl.setListener(listener);

		PowerMock.verifyAll();
	}

	@Test
	public void testDoCallback() throws Exception {
		ChunkCallback impl = new ChunkCallbackImpl();
		ChunkReadListener listener = PowerMock.createMock(ChunkReadListener.class);

		impl.setListener(listener);
		impl.doCallback(new ExecutorRequest(), new ExecutorResponse());
	}

	@Test(expected = Exception.class)
	public void testDoCallbackWithException() throws Exception {
		ChunkCallback impl = new ChunkCallbackImpl();
		impl.doCallback(new ExecutorRequest(), new ExecutorResponse());
	}

	@Test
	public void testDoEnd() {
		ChunkCallback impl = new ChunkCallbackImpl();
		ChunkReadListener listener = PowerMock.createMock(ChunkReadListener.class);

		impl.setListener(listener);
		impl.doEnd();
		// no Exception is OK
	}

	@Test
	public void testDoEndWithNull() {
		ChunkCallback impl = new ChunkCallbackImpl();

		impl.doEnd();
		// no Exception is OK
	}
}
