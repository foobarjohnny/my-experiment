package com.telenav.cserver.ac.protocol.protobuf;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.util.helper.generictest.DataSource;
import com.telenav.cserver.util.helper.generictest.GenericTest;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoValidateAddressReq;


public class ValidateAddressRequestParserTest {
	private ValidateAddressRequestParser instance = new ValidateAddressRequestParser();
	private DataSource dataSource = new DataSource();
	ProtocolBuffer protoBuffer = new ProtocolBuffer();
	
	@Before
	public void setUp() throws Exception {
		
		ProtoValidateAddressReq validateAddressReq = getProtoRequest();
		if(validateAddressReq.computeSize() > 0){
			protoBuffer.setBufferData(validateAddressReq.toByteArray());
		}
		
		ExecutorRequest[] executorReq = new ExecutorRequest[2];
		dataSource.addData(Object.class.getName(), protoBuffer);		
		dataSource.addData(ExecutorRequest[].class.getName(), executorReq);
		dataSource.addData(ProtocolBuffer.class.getName(), protoBuffer);
	}
	
	@After
	public void tearDown() throws Exception {
		dataSource.clear(); 		// clear the data after testing
	}

	@Test
	public void testSuite(){
		GenericTest.getInstance().startTest(instance, dataSource);
	}
	
	public ProtoValidateAddressReq getProtoRequest(){
		
		ProtoValidateAddressReq req = ProtoValidateAddressReq.newBuilder()
									.setCity("city")
									.setCountry("country")
									.setFirstLine("firstline")
									.setLastLine("label")
									.setState("state")
									.setTransactionID("transactionId")
									.setAddrSearchID("AddrSearchId")
									.setStreet1("street1")
									.setStreet2("street2")
									.build();
		return req;
	}

}
