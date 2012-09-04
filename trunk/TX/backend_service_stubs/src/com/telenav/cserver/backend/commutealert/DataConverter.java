/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.commutealert;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;

import com.telenav.cserver.backend.datatypes.Address;
import com.telenav.cserver.backend.datatypes.Constants;
import com.telenav.cserver.backend.datatypes.ace.GeoCodedAddress;
import com.telenav.cserver.backend.datatypes.commutealert.CommuteAlertDetails;
import com.telenav.cserver.backend.datatypes.commutealert.TrafficSegment;
import com.telenav.cserver.backend.datatypes.commutealert.TrafficSummaryReport;
import com.telenav.cserver.backend.datatypes.commutealert.TrafficAlert;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.ws.datatypes.alerts.AlertDetails;
import com.telenav.ws.datatypes.alerts.CommuteAlert;
import com.telenav.ws.datatypes.alerts.CommuteAlert20;
import com.telenav.ws.datatypes.alerts.DayOfTheWeek;
import com.telenav.ws.datatypes.alerts.Notification;
import com.telenav.ws.datatypes.common.Property;
import com.telenav.ws.datatypes.traffic.Route;
import com.telenav.ws.datatypes.traffic.Segment;
import com.telenav.ws.datatypes.traffic.TrafficSummaryRequest;

/**
 * util class for convert the type between web service and backend
 * 
 * @author mmwang
 * @version 1.0 2010-7-15
 * 
 */
public class DataConverter
{
	private static Logger logger = Logger.getLogger(DataConverter.class);

	/**
	 * copy form {@link com.telenav.cserver.nav.util.DataConverter}
	 * convert GeoCodedAddress to Stop
	 * @param geoCodedAddr
	 * @return
	 */
	public static Stop convertGeoCodedAddressToStop(GeoCodedAddress geoCodedAddr)
	{
		if(geoCodedAddr == null)
		{
			return null;
		}
		Stop stop = new Stop();
		stop.firstLine = geoCodedAddr.getFirstLine();
		stop.city = geoCodedAddr.getCityName();
		stop.state = geoCodedAddr.getState();
		stop.country = geoCodedAddr.getCountry();
		stop.county = geoCodedAddr.getCounty();
		stop.label = geoCodedAddr.getFirstLine();
		stop.lat = convertToDM5(geoCodedAddr.getLatitude());
		stop.lon = convertToDM5(geoCodedAddr.getLongitude());
		stop.zip = geoCodedAddr.getPostalCode();
		stop.isGeocoded = true;
		return stop;
		
	}
	
	/**
	 * copy form {@link com.telenav.cserver.nav.util.DataConverter}
	 * convert stop to Address
	 * @param stop
	 * @return
	 */
	public static Address convertStopToAddress(Stop stop)
	{
		Address address = new Address();
		if(stop == null)
		{
			return address;
		}
		
    	address.setFirstLine(stop.firstLine);
    	address.setCityName(stop.city);
    	address.setState(stop.state);
    	address.setPostalCode(stop.zip);
    	address.setLatitude(convertToDegree(stop.lat));
    	address.setLongitude(convertToDegree(stop.lon));
    	address.setCountry(stop.country);
    	
    	return address;
	}

	/**
	 * @param lon
	 * @return
	 */
	public static double convertToDegree(int dm5)
	{
		return dm5 / Constants.DEGREE_MULTIPLIER;
	}
	
	/**
	 * @param latitude
	 * @return
	 */
	public static int convertToDM5(double degree)
	{
		return (int) (degree * Constants.DEGREE_MULTIPLIER);
	}

	/**
	 * @param trafficAlert
	 * @return
	 */
	private static TrafficAlert convertTrafficAlert(
			com.telenav.ws.datatypes.traffic.TrafficAlert trafficAlert)
	{
		if (trafficAlert != null)
		{
			if (logger.isDebugEnabled()) {
				logger.debug(ReflectionToStringBuilder.toString(trafficAlert));
			}
			TrafficAlert alert = new TrafficAlert();
			alert.setCrossStreet(trafficAlert.getCrossStreet());
			alert.setLatitude(trafficAlert.getLatitude());
			alert.setLocationName(trafficAlert.getLocationName());
			alert.setLongitude(trafficAlert.getLongitude());
			alert.setNumLanesClosed(trafficAlert.getNumLanesClosed());
			alert.setSeverity(trafficAlert.getSeverity());
			alert.setType(trafficAlert.getType());
			
			// if desc is not empty ,then set desc and long desc
			if (!StringUtils.isEmpty(trafficAlert.getDescription()))
			{
				alert.setDescription(trafficAlert.getDescription());
				alert.setLongDescription(trafficAlert.getLongDescription());
			}
			return alert;
		}
		return null;
	}

	private static TrafficSegment convertTrafficSegment(
			com.telenav.ws.datatypes.traffic.TrafficSegment wsSegment)
	{
		if (wsSegment != null)
		{
			if (logger.isDebugEnabled()) {
				logger.debug(ReflectionToStringBuilder.toString(wsSegment));
			}
			TrafficSegment segment = new TrafficSegment();
			segment.setAverageSpeed(wsSegment.getAverageSpeed());
			segment.setCongestionTrend(wsSegment.getCongestionTrend());
			segment.setDelay(wsSegment.getDelay());
			segment.setIncidentCount(wsSegment.getIncidentCount());
			segment.setLength(wsSegment.getLength());
			segment.setMapId(wsSegment.getMapId());
			segment.setName(wsSegment.getName());
			segment.setPostedSpeed(wsSegment.getPostedSpeed());
			segment.setSlowestSpeed(wsSegment.getSlowestSpeed());
			segment.setTmcId(wsSegment.getTmcId());
			segment.setTravelTime(wsSegment.getTravelTime());
			
			com.telenav.ws.datatypes.traffic.TrafficAlert[] wsAlerts = wsSegment.getTrafficAlert();
			if (wsAlerts != null) 
			{
				TrafficAlert[] alerts = new TrafficAlert[wsAlerts.length];
				for (int i = 0; i < alerts.length; i++)
				{
					alerts[i] = convertTrafficAlert(wsAlerts[i]);
				}
				segment.setTrafficAlert(alerts);
			}
			
			return segment;
		}

		return null;
	}

	/**
	 * convert the webservice's TrafficSummaryReport to a backend's
	 * TrafficSummaryReport
	 * 
	 * @param wsReport
	 *            will return null if it's null;
	 * @return
	 */
	public static TrafficSummaryReport convertTrafficSummaryReport(
			com.telenav.ws.datatypes.traffic.TrafficSummaryReport wsReport)
	{
		if (wsReport != null)
		{
			if (logger.isDebugEnabled()) {
				logger.debug(ReflectionToStringBuilder.toString(wsReport));
			}
			TrafficSummaryReport report = new TrafficSummaryReport();
			report.setIncidentCount(wsReport.getIncidentCount());
			com.telenav.ws.datatypes.traffic.TrafficSegment[] wsSegments = wsReport.getSegment();
			if (wsSegments != null)
			{
				// convert the ws segment to backend segment
				TrafficSegment[] segments = new TrafficSegment[wsSegments.length];
				for (int i = 0; i < wsSegments.length; i++)
				{
					segments[i] = convertTrafficSegment(wsSegments[i]);
				}
				report.setSegment(segments);
			}
			report.setTimestamp(wsReport.getTimestamp());
			report.setDelay(wsReport.getDelay());
			return report;
		}
		return null;
	}
	
	/**
	 * print the alertdetails from webservice.
	 * copy from nav_map
	 * @param alertDetails
	 */
    private static void printAlert(AlertDetails alertDetails)
    {
        if (alertDetails == null)
        {
            logger.debug("printAlert, alert is null!");
            return;
        }
        if (logger.isDebugEnabled())//check whether enable
        {
            logger.debug("========================<detail  >:" + alertDetails.getAlert().getId());
        }
        CommuteAlert alert = (CommuteAlert) alertDetails.getAlert();
        if (logger.isDebugEnabled())
        {
            logger.debug("Name:" + alert.getName() + ",Status:" + alert.getStatus());
            logger.debug("OriginLabel: " + alert.getOriginLabel() + ", DestinationLabel:" + alert.getDestinationLabel());
        }
        if (alert instanceof CommuteAlert20)
        {
            CommuteAlert20 alert20 = (CommuteAlert20) alert;
            Property[] props = alert20.getProperty();
            for (Property p : props)
            {
                // Commute Alert Properties
                logger.debug("COMMUTE ALERT PROPERTIES");
                logger.debug(p.getKey() + " = " + p.getValue());
            }
        }
        DayOfTheWeek[] days = alertDetails.getSchedule().getDaysOfTheWeek();
        logger.debug("schedule type:" + alertDetails.getSchedule().getType().getValue());
        for (int j = 0; days != null && j < days.length; j++)
        {
            DayOfTheWeek day = days[j];
            logger.debug("schedule(Weekly):" + day.getValue());
        }
        Notification[] notifications = alertDetails.getNotification();
        logger.debug("Nofication type: --- total:" + (notifications != null ? notifications.length : 0));
        for (int j = 0; notifications != null && j < notifications.length; j++)
        {
            logger.debug(notifications[j]);
        }
        TrafficSummaryRequest tsDetails = ((CommuteAlert) (alertDetails.getAlert())).getTrafficSummaryDetails();
        logger.debug("TrafficSummaryRequest:" + tsDetails);
        Route route = tsDetails.getRoute();
        logger.debug("route:" + route);
        Segment[] segments = route != null ? route.getSegment() : null;
        for (int j = 0; segments != null && j < segments.length; j++)
        {
            logger.debug("segment:" + segments[j].getName());
        }

    }

	/**
	 * convert AlertDetails To CommuteAlertDetails
	 * @param details
	 * @return
	 * @throws CommuteAlertException 
	 */
	public static CommuteAlertDetails convertAlertDetailsToCommuteAlertDetails(
			AlertDetails details, TnContext tc) 
	{
		if (logger.isDebugEnabled()) 
		{
			printAlert(details);
		}
		// return null if it's null
		if (details == null) {
			return null;
		}
		return new CommuteAlertDetails(details, tc);
	}
}
