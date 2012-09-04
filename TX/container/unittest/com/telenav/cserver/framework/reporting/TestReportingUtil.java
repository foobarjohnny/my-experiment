/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.reporting;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.telenav.cserver.framework.reporting.impl.MISReportor;
import com.telenav.cserver.framework.reporting.impl.ServerMISReportor;

/**
 * TestReportingUtil.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-5-24
 */
@RunWith(PowerMockRunner.class)
public class TestReportingUtil extends TestCase{
	
	private IReportingRequest request = PowerMock.createMock(IReportingRequest.class);
	private MISReportor misReportor = PowerMock.createMock(MISReportor.class);
	private ServerMISReportor serverMISReportor = PowerMock.createMock(ServerMISReportor.class);
	private ReportingUtil reportingUtil = new ReportingUtil();
	@Override
	protected void setUp() throws Exception {
		Whitebox.setInternalState(ReportingUtil.class, "misReportor", misReportor);
		Whitebox.setInternalState(ReportingUtil.class, "serverMISReportor", serverMISReportor);
	}
	public void testReport_serverMISReportor(){
		//prepare and replay
		EasyMock.expect(request.getType()).andReturn(ReportType.SERVER_MIS_LOG_REPORT);
		EasyMock.expect(serverMISReportor.report(request)).andReturn(null);
		PowerMock.replayAll();
		
		//invoke and verify
		ReportingUtil.report(request);
		PowerMock.verifyAll();
		//assert
		//no exception is ok
	}
	public void testReport_misReportor(){
		//prepare and replay
		EasyMock.expect(request.getType()).andReturn(new ReportType("123456"));
		EasyMock.expect(misReportor.report(request)).andReturn(null);
		PowerMock.replayAll();
		
		//invoke and verify
		ReportingUtil.report(request);
		PowerMock.verifyAll();
		
		//assert
		//no exception is ok
	}

}
