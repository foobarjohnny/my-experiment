/**
 * (c) Copyright 2012 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy.traffic;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import junit.framework.TestCase;

import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.datatypes.traffic.tims.v10.IncidentSource;
import com.telenav.datatypes.traffic.tims.v10.IncidentType;
import com.telenav.datatypes.traffic.tims.v10.SeverityType;
import com.telenav.datatypes.traffic.tims.v10.SourceType;
import com.telenav.datatypes.traffic.tims.v10.TrafficIncident;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.services.traffic.tims.v10.CreateResponseDTO;
import com.telenav.services.traffic.tims.v10.IncidentReport;
import com.telenav.ws.datatypes.services.ResponseCodeEnum;

/**
 * TestTrafficProxy.java
 * @author njiang
 * @version 1.0 2012-7-25
 */
public class TestTrafficProxy extends TestCase {
	
	static TrafficProxy proxy = new TrafficProxy();
	
	public void testTrafficProxy(){
		UserProfile userProfile = TestTrafficProxy.initUserProfile();
		
		IncidentSource source = new IncidentSource();
        source.setReporterID(userProfile.getUserId());
        source.setSourceReportID("");
        StringBuffer sourceName = new StringBuffer();
        sourceName.append(userProfile.getCarrier());
        sourceName.append("-");
        sourceName.append(userProfile.getDevice());
        sourceName.append("-");
        sourceName.append(userProfile.getVersion());
        source.setSourceName(sourceName.toString());
        source.setType(SourceType.TELENAV_USER);
        
        TrafficIncident incident = new TrafficIncident();
        incident.setSeverity(SeverityType.LOW_IMPACT);
        
        incident.setReportTime(Calendar.getInstance());
        incident.setStartTime(Calendar.getInstance());
        incident.setLatitude(3737890);
        incident.setLongitude(-12201072);
        incident.setStreetName("Santa Ana Ct at E Arques Ave");
        incident.setCrossStreet(null);
        incident.setMarketID("UNKNOWN");
        incident.setType(IncidentType.SPEED_TRAP);
        incident.setDescription("Speed Trap");
        
        List<IncidentReport> reports = new ArrayList<IncidentReport>();
        IncidentReport report = new IncidentReport();
        report.setSource(source);
        report.setIncident(incident);
        reports.add(report);
		
        
        
		CreateResponseDTO resp = null;
		
        TnContext tc = new TnContext();
		
		try {
			resp = proxy.createIncident(reports, tc);
		} catch (ThrottlingException e) {
			e.printStackTrace();
		}
		
		assertEquals(ResponseCodeEnum._OK, resp.getResponseStatus().getStatusCode());
	}
	
	private static UserProfile initUserProfile(){
		UserProfile userProfile = new UserProfile();
        userProfile.setUserId("9985413");
        userProfile.setProgramCode("ATTNAVPROG");
        userProfile.setPlatform("ANDROID");
        userProfile.setVersion("7.2.0");
        userProfile.setDeviceID("asdfasdfasdfasdf72983487192836");
        
        return userProfile;
	}
	

}
