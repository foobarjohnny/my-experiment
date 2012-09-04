package com.telenav.cserver.misc.protocol.protobuf;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoSentAddressReq;

public class SentAddressProtoRequestParserTest extends SentAddressProtoRequestParser{

	ProtocolBuffer protoBuf = new ProtocolBuffer();
	@Before
	public void setUp() throws Exception {
	
		ProtoSentAddressReq req = ProtoSentAddressReq.newBuilder()
									.setAction("SentAddress")
									.setId("addressId")
									.build();							
		protoBuf.setBufferData(req.toByteArray());
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testParse() throws ExecutorException {
		this.parse(protoBuf);
	}

}
