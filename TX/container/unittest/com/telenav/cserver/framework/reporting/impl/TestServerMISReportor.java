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
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.reporting.IReportingRequest;
import com.telenav.cserver.unittestutil.UTConstant;
import com.telenav.cserver.unittestutil.UnittestUtil;
import com.telenav.j2me.datatypes.GpsData;

/**
 * TestServerMISReportor.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-24
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ServerMISReportor.class})
public class TestServerMISReportor extends TestCase{
	private IReportingRequest request = PowerMock.createMock(IReportingRequest.class);
	private ServerMISReportor serverMISReportor_global = new ServerMISReportor();
	/**
	 * request.isWriten() == true
	 */
	public void testReport(){
		//prepare and replay
		ServerMISReportor serverMISReportor = PowerMock.createPartialMock(ServerMISReportor.class, "joinFields");
		EasyMock.expect(request.isWriten()).andReturn(true);
		PowerMock.replayAll();
		
		//invoke and verify
		serverMISReportor.report(request);
		PowerMock.verifyAll();
		
		//assert
		//no Exception is OK.
	}
	/**
	 * request.isWriten() == false
	 * @throws Exception 
	 */
	public void testReport1() throws Exception {
		//define variables
		ServerMISReportor serverMISReportor = PowerMock.createPartialMock(ServerMISReportor.class, "joinFields");
		//prepare and replay
		
		EasyMock.expect(request.isWriten()).andReturn(false);
		PowerMock.expectPrivate(serverMISReportor, "joinFields", request).andReturn("");
		request.setWriten(true);
		PowerMock.replayAll();
		
		//invoke and verify
		serverMISReportor.report(request);
		PowerMock.verifyAll();

		//assert

		//no Exception is OK.
	}
	public void testFilterSpecialChars() {
		//define variables
		String str = "t\"e,l\ne\rn'av";

		//assert
		assertEquals("t e l e n av", serverMISReportor_global.filterSpecialChars(str));
	}
	/**
	 * userProfile == null
	 * gpsList == null
	 * @throws Exception 
	 */
	public void testInitialAttrArray() throws Exception {
		//define variables
		String[] result;
		//prepare and replay
		EasyMock.expect(request.getUserProfile()).andReturn(null);
		EasyMock.expect(request.getGpsList()).andReturn(null);
		PowerMock.replayAll();

		//invoke and verify
		result = (String[])Whitebox.invokeMethod(serverMISReportor_global, "initialAttrArray", request);
		PowerMock.verifyAll();

		//assert
		assertEquals("0",result[3]);
	}
	
	/**
	 * userProfile == null
	 * gpsList.size == 0
	 * @throws Exception 
	 */
	public void testInitialAttrArray1() throws Exception {
		//define variables
		String[] result;
		//prepare and replay
		EasyMock.expect(request.getUserProfile()).andReturn(null);
		EasyMock.expect(request.getGpsList()).andReturn(new ArrayList());
		PowerMock.replayAll();

		//invoke and verify
		result = (String[])Whitebox.invokeMethod(serverMISReportor_global, "initialAttrArray", request);
		PowerMock.verifyAll();

		//assert
		assertEquals("0",result[(int)ServerMISReportor.COMPLETED_FLAG]);
	}
	/**
	 * userProfile != null
	 * gpsList.size != 0
	 * 		
	 * @throws Exception 
	 */
	public void testInitialAttrArray2() throws Exception {
		//define variables
		String[] attrs;
		UserProfile userProfile = UnittestUtil.createUserProfile();
		List<GpsData> gpsList = new ArrayList<GpsData>();
		GpsData gps = new GpsData();
		gps.lat = 11;
		gps.lon = 22;
		
		gpsList.add(null);
		gpsList.add(gps);
		//prepare and replay
		EasyMock.expect(request.getUserProfile()).andReturn(userProfile);
		EasyMock.expect(request.getGpsList()).andReturn(gpsList);
		PowerMock.replayAll();

		//invoke and verify
		attrs = (String[])Whitebox.invokeMethod(serverMISReportor_global, "initialAttrArray", request);
		PowerMock.verifyAll();

		//assert
		assertEquals("0",attrs[(int)ServerMISReportor.COMPLETED_FLAG]);
		assertEquals(attrs[(int)ServerMISReportor.PTN],UTConstant.USERPROFILE_MIN);
         
		assertEquals(attrs[(int)ServerMISReportor.CUSTOM12], UTConstant.USERPROFILE_PRODUCT);
		assertEquals(attrs[(int)ServerMISReportor.CUSTOM13], UTConstant.USERPROFILE_DEVICE);
		assertEquals(attrs[(int)ServerMISReportor.CUSTOM14], UTConstant.USERPROFILE_PLATFORM);
		assertEquals(attrs[(int)ServerMISReportor.CUSTOM15], UTConstant.USERPROFILE_LOCALE);
         
		assertEquals(attrs[(int)ServerMISReportor.CUSTOM17], UTConstant.USERPROFILE_CARRIER);
		assertEquals(attrs[(int)ServerMISReportor.CUSTOM18],UTConstant.USERPROFILE_VERSION);
		assertEquals(attrs[(int)ServerMISReportor.CUSTOM19], UTConstant.USERPROFILE_USERID);
		
		assertEquals(attrs[(int)ServerMISReportor.CUSTOM09],"11");
		assertEquals(attrs[(int)ServerMISReportor.CUSTOM10],"22");
	}
	
	public void testJoinFields() throws Exception {
		//define variables
		ServerMISReportor serverMISReportor = PowerMock.createPartialMock(ServerMISReportor.class, "initialAttrArray");
		String result;
		String[] attrs = new String[5];
		List<Map<Long, String>> list = new ArrayList<Map<Long, String>>(); 
		
		Map<Long, String> map = new HashMap<Long, String>();
		map.put(new Long(3), "33");
		map.put(new Long(4), "44");
		
		list.add(map);
		//prepare and replay
		PowerMock.expectPrivate(serverMISReportor, "initialAttrArray",request).andReturn(attrs);
		EasyMock.expect(request.getMisLogEvents()).andReturn(list);
		EasyMock.expect(request.getServerTime()).andReturn(new Long(10));
		PowerMock.replayAll();

		//invoke and verify
		result = Whitebox.invokeMethod(serverMISReportor, "joinFields", request);
		PowerMock.verifyAll();

		//assert
		System.out.println(result);
		assertTrue(result.indexOf(",33,") > -1);
		assertTrue(result.indexOf(",44") > -1);
	}
}
