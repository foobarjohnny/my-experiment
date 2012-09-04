/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.handler;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.protocol.ProtocolHandler;
import com.telenav.cserver.framework.executor.protocol.ProtocolResponseFormatter;
import com.telenav.cserver.framework.executor.protocol.txnode.ByteArrayWrapper;
import com.telenav.cserver.framework.transportation.StepWriteTransportor;
import com.telenav.cserver.framework.transportation.http.ServletTransportor;
import com.telenav.cserver.matcher.MatchBox;
import com.telenav.cserver.unittestutil.UnittestUtil;
import com.telenav.j2me.datatypes.TxNode;

/**
 * TestChunkedDataHandler.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-23
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ChunkedDataHandler.class,TxNode.class})
public class TestChunkedDataHandler extends TestCase{
	
	private CliTransaction cli = PowerMock.createMock(CliTransaction.class);
	private ServletTransportor trans = PowerMock.createMock(ServletTransportor.class);
	private ExecutorContext context = PowerMock.createMock(ExecutorContext.class);
	private StepWriteTransportor transpor = PowerMock.createMock(StepWriteTransportor.class);
	private ProtocolHandler protocolHandler = PowerMock.createMock(ProtocolHandler.class);
	private ProtocolResponseFormatter protocolResponseFormatter = PowerMock.createMock(ProtocolResponseFormatter.class);
	private ExecutorRequest req = new ExecutorRequest();
	private ExecutorResponse resp = new ExecutorResponse();
	public void testParser_return_isContinue(){
		//prepare and replay
		TestablClass chunkedDataHandler = PowerMock.createPartialMock(TestablClass.class, "sendEndChunkResponse","sendChunkResponse");
		Whitebox.setInternalState(chunkedDataHandler, "handlerType", "EndChunk");
		chunkedDataHandler.sendEndChunkResponse(null, null,null, null);
		PowerMock.replayAll();
		
		//invoke and verify
		boolean b = chunkedDataHandler.parse(null, null, null, true, null);
		
		Whitebox.setInternalState(chunkedDataHandler, "handlerType", "");
		boolean b1 = chunkedDataHandler.parse(null, null, null, false, null);
		PowerMock.verifyAll();
		
		//assert
		assertTrue(b);
		assertFalse(b1);
	}
	
	public void testParser_return_isSuccess(){
		//prepare and replay
		TestablClass chunkedDataHandler = PowerMock.createPartialMock(TestablClass.class, "sendEndChunkResponse","sendChunkResponse");
		Whitebox.setInternalState(chunkedDataHandler, "handlerType", "");
		chunkedDataHandler.sendChunkResponse(null, null,null, null);
		PowerMock.replayAll();
		
		//invoke and verify
		boolean b = chunkedDataHandler.parse(null, null, null, true, null);
		PowerMock.verifyAll();
		
		//assert
		assertFalse(b);
	}
	
	public void testSendEndChunkResponse() throws Exception{
		//define variable
		TestablClass chunkedDataHandler = new TestablClass();
		//prepare and replay
		
		EasyMock.expect(context.getTransportor()).andReturn(trans);
		PowerMock.expectNew(StepWriteTransportor.class, trans).andReturn(transpor);
		transpor.flushBuffer();
		transpor.finish();
		transpor.flush();
		cli.addData("Sending End Chunk", "End");
		PowerMock.replayAll();
		
		//invoke and verify
		chunkedDataHandler.sendEndChunkResponse(null, null, context, cli);
		PowerMock.verifyAll();
	}
	/**
	 * wrapper.isProtocolBuffer == true, (result != null && result.length > 0) == false
	 * @throws Exception 
	 */
	public void testSendChunkResponse_0() throws Exception{
		//define variable
		TestablClass chunkedDataHandler = new TestablClass();
		ByteArrayWrapper wrapper = PowerMock.createMock(ByteArrayWrapper.class);
		wrapper.isProtocolBuffer = true;
		wrapper.bytes = null;
		
		//prepare and replay
		EasyMock.expect(context.getTransportor()).andReturn(trans);
		PowerMock.expectNew(StepWriteTransportor.class, trans).andReturn(transpor);
		transpor.flushBuffer();
		PowerMock.expectNew(ByteArrayWrapper.class).andReturn(wrapper);
		EasyMock.expect( context.getProtocolHandler()).andReturn(protocolHandler);
		EasyMock.expect(protocolHandler.getResponseFormatter()).andReturn(protocolResponseFormatter);
		protocolResponseFormatter.format(MatchBox.ByteArrayWrapperEquals(wrapper), EasyMock.aryEq(new ExecutorResponse[]{resp}));
		cli.addData("Sending Chunk", "null");
		PowerMock.replayAll();
		
		//invoke and verify
		chunkedDataHandler.sendChunkResponse(null, resp, context, cli);
		PowerMock.verifyAll();
	}
	/**
	 * wrapper.isProtocolBuffer == false, 
	 * 		subNode.childrenSize() > 1 
	 * (result != null && result.length > 0) == true
	 * dataProcessType == null
	 * dataProcessor == null
	 * @throws Exception 
	 */
	public void testSendChunkResponse_1() throws Exception{
		//define variable
		TestablClass chunkedDataHandler = new TestablClass();
		
		ByteArrayWrapper wrapper = PowerMock.createMock(ByteArrayWrapper.class);
		wrapper.isProtocolBuffer = false;
		wrapper.bytes = UnittestUtil.getTxNodeBytes(true);
		
		//prepare and replay
		EasyMock.expect(context.getTransportor()).andReturn(trans);
		PowerMock.expectNew(StepWriteTransportor.class, trans).andReturn(transpor);
		transpor.flushBuffer();
		PowerMock.expectNew(ByteArrayWrapper.class).andReturn(wrapper);
		EasyMock.expect( context.getProtocolHandler()).andReturn(protocolHandler);
		EasyMock.expect(protocolHandler.getResponseFormatter()).andReturn(protocolResponseFormatter);
		protocolResponseFormatter.format(MatchBox.ByteArrayWrapperEquals(wrapper), EasyMock.aryEq(new ExecutorResponse[]{resp}));
		EasyMock.expect(transpor.write(MatchBox.byteArrEquals(new byte[]{}))).andReturn(0);
		transpor.flush();
		cli.addData("Sended Chunk"," result size : 41");
		PowerMock.replayAll();
		
		//invoke and verify
		chunkedDataHandler.sendChunkResponse(req, resp, context, cli);
		PowerMock.verifyAll();
		//don't throw exception is just ok.
	}
	/**
	 * wrapper.isProtocolBuffer == false, 
	 * 		subNode.childrenSize() <= 1 
	 * (result != null && result.length > 0) == true
	 * dataProcessType != null
	 * dataProcessor != null && dataProcessType.equalsIgnoreCase("gzip") == true
	 * @throws Exception 
	 */
	public void testSendChunkResponse_2() throws Exception{
		//define variable
		TestablClass chunkedDataHandler = new TestablClass();
		
		ByteArrayWrapper wrapper = PowerMock.createMock(ByteArrayWrapper.class);
		wrapper.isProtocolBuffer = false;
		wrapper.bytes = UnittestUtil.getTxNodeBytes(false);
		
		UserProfile userProfile = new UserProfile();
		userProfile.setDataProcessType("gzip");
		req.setUserProfile(userProfile);
		//prepare and replay
		EasyMock.expect(context.getTransportor()).andReturn(trans);
		PowerMock.expectNew(StepWriteTransportor.class, trans).andReturn(transpor);
		transpor.flushBuffer();
		PowerMock.expectNew(ByteArrayWrapper.class).andReturn(wrapper);
		EasyMock.expect( context.getProtocolHandler()).andReturn(protocolHandler);
		EasyMock.expect(protocolHandler.getResponseFormatter()).andReturn(protocolResponseFormatter);
		protocolResponseFormatter.format(MatchBox.ByteArrayWrapperEquals(wrapper), EasyMock.aryEq(new ExecutorResponse[]{resp}));
		EasyMock.expect(transpor.write(MatchBox.byteArrEquals(new byte[]{}))).andReturn(0);
		
		
		
		transpor.flush();
		cli.addData("Sended Chunk"," result size : 69");
		PowerMock.replayAll();
		
		//invoke and verify
		chunkedDataHandler.sendChunkResponse(req, resp, context, cli);
		PowerMock.verifyAll();
	}
	/**
	 * wrapper.isProtocolBuffer == false, 
	 * 		subNode.childrenSize() <= 1 
	 * (result != null && result.length > 0) == true
	 * dataProcessType != null
	 * dataProcessor != null && dataProcessType.equalsIgnoreCase("gzip") != true
	 * @throws Exception 
	 */
	public void testSendChunkResponse_4() throws Exception{
		//define variable
		TestablClass chunkedDataHandler = new TestablClass();
		
		ByteArrayWrapper wrapper = PowerMock.createMock(ByteArrayWrapper.class);
		wrapper.isProtocolBuffer = false;
		wrapper.bytes = UnittestUtil.getTxNodeBytes(false);
		
		UserProfile userProfile = new UserProfile();
		userProfile.setDataProcessType("gzip11111111111111");
		req.setUserProfile(userProfile);
		//prepare and replay
		EasyMock.expect(context.getTransportor()).andReturn(trans);
		PowerMock.expectNew(StepWriteTransportor.class, trans).andReturn(transpor);
		transpor.flushBuffer();
		PowerMock.expectNew(ByteArrayWrapper.class).andReturn(wrapper);
		EasyMock.expect( context.getProtocolHandler()).andReturn(protocolHandler);
		EasyMock.expect(protocolHandler.getResponseFormatter()).andReturn(protocolResponseFormatter);
		protocolResponseFormatter.format(MatchBox.ByteArrayWrapperEquals(wrapper), EasyMock.aryEq(new ExecutorResponse[]{resp}));
		EasyMock.expect(transpor.write(MatchBox.byteArrEquals(new byte[]{}))).andReturn(0);
		
		
		
		transpor.flush();
		cli.addData("Sended Chunk"," result size : 41");
		PowerMock.replayAll();
		
		//invoke and verify
		chunkedDataHandler.sendChunkResponse(req, resp, context, cli);
		PowerMock.verifyAll();
	}
	public void testSimple(){
		TestablClass chunkedDataHandler = new TestablClass();
		chunkedDataHandler.getHandlerType();
		chunkedDataHandler.setHandlerType("");
		chunkedDataHandler.doParse(null, null, null);
	}
	
	private class TestablClass extends ChunkedDataHandler{

        @Override
        public boolean doParse(ExecutorRequest req, ExecutorResponse resp, ExecutorContext context)
        {
            // TODO Auto-generated method stub
            return false;
        }
		
	}

}
