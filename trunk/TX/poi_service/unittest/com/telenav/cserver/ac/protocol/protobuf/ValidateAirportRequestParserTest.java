package com.telenav.cserver.ac.protocol.protobuf;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.util.helper.generictest.DataSource;
import com.telenav.cserver.util.helper.generictest.GenericTest;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoValidateAirportReq;

public class ValidateAirportRequestParserTest {
	
	private ValidateAirportRequestParser instance = new ValidateAirportRequestParser();
	private DataSource dataSource = new DataSource();
	ProtocolBuffer protoBuffer = new ProtocolBuffer();
	
	@Before
	public void setUp() throws Exception {
		
		ProtoValidateAirportReq validateAirportReq = getProtoRequest();
		if(validateAirportReq.computeSize() > 0){
			protoBuffer.setBufferData(validateAirportReq.toByteArray());
		}
		dataSource.addData(Object.class.getName(), protoBuffer);		
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
	
	public ProtoValidateAirportReq getProtoRequest(){
		ProtoValidateAirportReq req = ProtoValidateAirportReq.newBuilder()
									.setAirportName("SFO")
									.setRegion("NA")
									.build();
		return req;
	}
	
}
