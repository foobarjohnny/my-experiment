/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.traffic;

import com.telenav.cserver.backend.datatypes.traffic.TrafficIncidentReport;
import com.telenav.cserver.backend.datatypes.traffic.TrafficIncidentSource;
import com.telenav.datatypes.traffic.tims.v10.IncidentSource;
import com.telenav.datatypes.traffic.tims.v10.IncidentType;
import com.telenav.datatypes.traffic.tims.v10.SeverityType;
import com.telenav.datatypes.traffic.tims.v10.SourceType;
import com.telenav.datatypes.traffic.tims.v10.TrafficIncident;
import com.telenav.services.traffic.tims.v10.IncidentReport;

/**
 * 
 *
 * @author mmwang
 * @version 1.0 2010-7-19
 * 
 */
public class DataConverter
{

	static IncidentReport convertReport(TrafficIncidentReport report)
	{
		IncidentReport wsReport = null;
		if (report != null)
		{
			wsReport = new IncidentReport();
			
			wsReport.setIncident(convertIncident(report.getIncident()));
			wsReport.setSource(convertSource(report.getSource()));
		}
		
		return wsReport;
	}

	/**
	 * @param source
	 * @return
	 */
	private static IncidentSource convertSource(TrafficIncidentSource source)
	{
		IncidentSource wsSource = null;
		if (source != null)
		{
			wsSource = new IncidentSource();
			wsSource.setReporterID(source.getReporterId());
			wsSource.setSourceReportID(source.getSourceReportId());
			StringBuilder sourceName = new StringBuilder();
			sourceName.append(source.getCarrier());
			sourceName.append("-");
			sourceName.append(source.getDevice());
			sourceName.append("-");
			sourceName.append(source.getVersion());
			wsSource.setReporterName(sourceName.toString());
			wsSource.setType(SourceType.Factory.fromValue(source.getType()));
		}
		return wsSource;
	}

	/**
	 * @param incident
	 * @return
	 */
	private static TrafficIncident convertIncident(
			com.telenav.cserver.backend.datatypes.traffic.TrafficIncident incident)
	{
		TrafficIncident wsIncident = null;
		if (incident != null)
		{
			wsIncident = new TrafficIncident();
			wsIncident.setSeverity(SeverityType.Factory.fromValue(incident.getSeverity()));
			wsIncident.setLatitude(incident.getLatitude());
			wsIncident.setLongitude(incident.getLongitude());
			wsIncident.setReportTime(incident.getReportTime());
			wsIncident.setStartTime(incident.getStartTime());
			wsIncident.setMarketID(incident.getMarketID());
			wsIncident.setStreetName(incident.getStreetName());
			wsIncident.setCrossStreet(incident.getCrossStreet());
			wsIncident.setType(IncidentType.Factory.fromValue(incident.getType()));
			wsIncident.setDescription(incident.getDescription());
		}
		return wsIncident;
	}
	
	
}
