package com.telenav.cserver.ac.protocol.protobuf;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.ac.executor.ValidateAirportResponse;
import com.telenav.cserver.backend.datatypes.TnPoi;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.util.helper.generictest.DataSource;
import com.telenav.cserver.util.helper.generictest.GenericTest;
import com.telenav.j2me.datatypes.ProtocolBuffer;

public class ValidateAirportResponseFormatterTest {

	private ValidateAirportResponseFormatter instance = new ValidateAirportResponseFormatter();
	private DataSource dataSource = new DataSource();
		
	@Before
	public void setUp() throws Exception {
		
		List<TnPoi> airportPoi = new ArrayList();
		airportPoi.add(getTnPoi());
		
		ValidateAirportResponse resp = new ValidateAirportResponse();
		resp.setStatus(ExecutorResponse.STATUS_OK);
		resp.setMessage("just for testing");
		resp.setAirportList(airportPoi);
		
		ExecutorResponse[] resps = {resp};
		
		dataSource.addData(Object.class.getName(), new ProtocolBuffer());		
		dataSource.addData(ExecutorResponse[].class.getName(), resps);
		dataSource.addData(ValidateAirportResponse.class.getName(), resp);
	}
	
	@After
	public void tearDown() throws Exception {
		dataSource.clear();		// clear the data after testing
	}

	@Test
	public void testSuite(){
		GenericTest.getInstance().startTest(instance, dataSource);
	}
	
	public TnPoi getTnPoi(){
		TnPoi poi = new TnPoi();
		poi.setFeatureName("feature name");
		poi.setBrandName("brand name");
		return poi;
	}
}
