package com.telenav.cserver.ac.protocol.protobuf;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.ac.executor.ShareAddressResponse;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.util.helper.generictest.DataSource;
import com.telenav.cserver.util.helper.generictest.GenericTest;
import com.telenav.j2me.datatypes.ProtocolBuffer;

public class ShareAddressResponseFormatterTest {
	
	private ShareAddressResponseFormatter instance = new ShareAddressResponseFormatter();
	private DataSource dataSource = new DataSource();
	
	@Before
	public void setUp() throws Exception {
		
		ShareAddressResponse resp = new ShareAddressResponse();
		resp.setStatus(ExecutorResponse.STATUS_OK);
		resp.setMessage("message");
		ExecutorResponse[] resps = {resp};
		
		dataSource.addData(Object.class.getName(), new ProtocolBuffer());		
		dataSource.addData(ExecutorResponse[].class.getName(), resps);
		dataSource.addData(ProtocolBuffer.class.getName(), new ProtocolBuffer());
	}
	
	@After
	public void tearDown() throws Exception {
		dataSource.clear();		// clear the data after testing
	}

	@Test
	public void testSuite(){
		GenericTest.getInstance().startTest(instance, dataSource);
	}
	
}
