/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.reporting.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.telenav.cserver.framework.reporting.IReportingRequest;
import com.telenav.cserver.unittestutil.UnittestUtil;

/**
 * TestMISReportor.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-24
 */
@RunWith(PowerMockRunner.class)
public class TestMISReportor extends TestCase{
	private MISReportor misReportor = PowerMock.createPartialMock(MISReportor.class, "addBusinessSpecificData");
	private IReportingRequest request = PowerMock.createMock(IReportingRequest.class);
	private List<Map<Long, String>> misLogEvents = new ArrayList<Map<Long, String>>();
	
	@Override
	protected void setUp() throws Exception {
		Map<Long, String> map0 = new HashMap<Long, String>();
		map0.put(new Long(0), "0");
		map0.put(new Long(1), "1");
		
		Map<Long, String> map10 = new HashMap<Long, String>();
		map10.put(new Long(10), "10");
		map10.put(new Long(11), "11");
		map10.put(new Long(12), "12");
		
		misLogEvents.add(map0);
		misLogEvents.add(map10);
	}
	
	public void testReport_noFor(){
		//prepare and replay
		EasyMock.expect(request.getMisLogEvents()).andReturn(new ArrayList());
		PowerMock.replayAll();
		
		//invoke and verify
		misReportor.report(request);
		PowerMock.verifyAll();
		//assert
		//no exception is ok
	}
	/**
	 * userProfile == null for first time and not null for second time
	 * tnContext   same situation as userProfile
	 * @throws Exception 
	 */
	
	public void testReport() throws Exception{
		//prepare and replay
		EasyMock.expect(request.getMisLogEvents()).andReturn(misLogEvents);
		EasyMock.expect(request.getServerTime()).andReturn(new Long(123)).times(2);
		EasyMock.expect(request.getUserProfile()).andReturn(null);
		EasyMock.expect(request.getUserProfile()).andReturn(UnittestUtil.createUserProfile());
		EasyMock.expect(request.getTnContext()).andReturn(null);
		EasyMock.expect(request.getTnContext()).andReturn(UnittestUtil.createTnContext());
		PowerMock.expectPrivate(misReportor, "addBusinessSpecificData", misLogEvents.get(0),new StringBuilder());
		PowerMock.expectPrivate(misReportor, "addBusinessSpecificData", misLogEvents.get(1),new StringBuilder());
		PowerMock.replayAll();
		
		//invoke and verify
		misReportor.report(request);
		PowerMock.verifyAll();
		//assert
	}
	/**
	 * userProfile != null
	 * tnContext   != null
	 * @throws Exception 
	 */
	
	public void testAddBusinessSpecificData() throws Exception{
		MISReportor misr = new MISReportor();
		
		Map<Long, String> map = new HashMap<Long, String>();
		map.put(new Long(0), "0");
		map.put(new Long(1), "1");
		
		StringBuilder recordString = new StringBuilder("string");
		
		String delimiter = Character.toString((char)1);
		
		
		Whitebox.invokeMethod(misr, "addBusinessSpecificData", map, recordString);
		
		assertEquals("string0=0"+delimiter+"1=1"+delimiter,recordString.toString());
	}
}
