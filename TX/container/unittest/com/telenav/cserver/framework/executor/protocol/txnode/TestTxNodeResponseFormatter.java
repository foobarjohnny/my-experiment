/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.executor.protocol.txnode;

import static org.easymock.EasyMock.expect;
import junit.framework.TestCase;


import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.protocol.ProtocolResponseFormatter;
import com.telenav.cserver.matcher.MatchBox;
import com.telenav.j2me.datatypes.DataConstants;
import com.telenav.j2me.datatypes.TxNode;

/**
 * TestTxNodeResponseFormatter.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-4-26
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(ExecutorDataFactory.class)
@SuppressStaticInitializationFor("com.telenav.cserver.framework.executor.protocol.txnode.ExecutorDataFactory")
public class TestTxNodeResponseFormatter extends TestCase{
	private ExecutorResponse[] responses = new ExecutorResponse[1];
	private ExecutorResponse response = new ExecutorResponse();
	private ByteArrayWrapper byteArrayWrapper = new ByteArrayWrapper();
	private TxNodeResponseFormatter txNodeResponseFormatter = new TxNodeResponseFormatter();
	private ExecutorDataFactory executorDataFactory  ;
	private ProtocolResponseFormatter protocolResponseFormatter;
	private String executorType = "0";
	private String ssoToken = "ssoToken";
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		responses[0] = response;
		response.setExecutorType(executorType);
		executorDataFactory  = PowerMock.createMock(ExecutorDataFactory.class);
		protocolResponseFormatter = PowerMock.createMock(ProtocolResponseFormatter.class);
	}
	
	public void testFormat()throws Exception{
		//mock object
		PowerMock.mockStatic(ExecutorDataFactory.class);
		expect(ExecutorDataFactory.getInstance()).andReturn(executorDataFactory);
		expect(executorDataFactory.createProtocolResponseFormatter(executorType)).andReturn(protocolResponseFormatter);
		protocolResponseFormatter.format(MatchBox.txNodeEquals(new TxNode()), MatchBox.executorResponseEquals(new ExecutorResponse[]{response}));
		PowerMock.replayAll();
		//call method
		txNodeResponseFormatter.format(byteArrayWrapper, responses);
		PowerMock.verifyAll();
		//assert
		TxNode root = TxNode.fromByteArray(byteArrayWrapper.bytes,0);
		assertNotNull(byteArrayWrapper.bytes);
		assertEquals(TxNode.VERSION_55,root.getVersion());
		assertEquals(1,root.childrenSize());
	}
	
	public void testFormatWithSsoToken()throws Exception{
		response.setSsoToken(ssoToken);
		//mock object
		PowerMock.mockStatic(ExecutorDataFactory.class);
		expect(ExecutorDataFactory.getInstance()).andReturn(executorDataFactory);
		expect(executorDataFactory.createProtocolResponseFormatter(executorType)).andReturn(protocolResponseFormatter);
		protocolResponseFormatter.format(MatchBox.txNodeEquals(new TxNode()), MatchBox.executorResponseEquals(new ExecutorResponse[]{response}));
		PowerMock.replayAll();
		//call method
		txNodeResponseFormatter.format(byteArrayWrapper, responses);
		PowerMock.verifyAll();
		//assert
		TxNode root = TxNode.fromByteArray(byteArrayWrapper.bytes,0);
		TxNode ssoTokenNode = root.childAt(0);
		
		assertNotNull(byteArrayWrapper.bytes);
		assertEquals(TxNode.VERSION_55,root.getVersion());
		assertEquals(2,root.childrenSize());
		assertEquals("SSO_TOKEN",ssoTokenNode.msgAt(0));
		assertEquals(ssoToken,ssoTokenNode.msgAt(1));
	}
	
	public void testFormatErrorNode()throws Exception{
		int maxPayloadSize = 6;
		response.setMaxPayloadSize(maxPayloadSize);
		//mock object
		PowerMock.mockStatic(ExecutorDataFactory.class);
		expect(ExecutorDataFactory.getInstance()).andReturn(executorDataFactory);
		expect(executorDataFactory.createProtocolResponseFormatter(executorType)).andReturn(protocolResponseFormatter);
		protocolResponseFormatter.format(MatchBox.txNodeEquals(new TxNode()), MatchBox.executorResponseEquals(new ExecutorResponse[]{response}));
		PowerMock.replayAll();
		//call method
		txNodeResponseFormatter.format(byteArrayWrapper, responses);
		PowerMock.verifyAll();
		//assert
		TxNode errorNode = TxNode.fromByteArray(byteArrayWrapper.bytes,0);
		TxNode childNode = errorNode.childAt(0);
		
		assertNotNull(byteArrayWrapper.bytes);
		assertEquals(TxNode.VERSION_55,errorNode.getVersion());
		assertEquals(1,errorNode.childrenSize());
		assertEquals(executorType,childNode.msgAt(0));
		assertEquals("Response size is too big, exceeded:" + maxPayloadSize,childNode.msgAt(1));
		assertEquals(DataConstants.TYPE_SERVER_RESPONSE,childNode.valueAt(0));
		assertEquals(ExecutorResponse.STATUS_EXCEPTION,childNode.valueAt(1));
	}

}
