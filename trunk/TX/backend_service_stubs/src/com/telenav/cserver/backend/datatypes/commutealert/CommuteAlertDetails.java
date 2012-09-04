/**
 * (c) Copyright 2007 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.datatypes.commutealert;

import java.util.List;

import org.apache.log4j.Logger;

import com.telenav.cserver.backend.commutealert.AlertUtil;
import com.telenav.cserver.backend.datatypes.ace.GeoCodedAddress;
import com.telenav.j2me.datatypes.Stop;

import com.telenav.ws.datatypes.alerts.AlertDetails;
import com.telenav.ws.datatypes.alerts.CommuteAlert;
import com.telenav.ws.datatypes.alerts.CommuteAlert20;
import com.telenav.ws.datatypes.common.Property;
import com.telenav.kernel.util.datatypes.TnContext;
/**
 * CommuteAlert Details
 * 
 * @author yqchen
 * @version 1.0 2007-11-22 10:16:00
 */
public class CommuteAlertDetails
{
    private static Logger logger = Logger.getLogger(CommuteAlertDetails.class);
    private long alertId = -1;
    private String name = "";
    private Stop origin = null;
    private Stop destination = null;
    
 // It seems in TN6.0 we also need to take routeStyle and avoidsetting while henerating Traffic Summary for commute Alerts.
	private String routeStyle="";
	private String avoidSetting="";
	
	private String originFirstLine="";
	private String destinationFirstLine="";
	
	private GeoCodedAddress originGeoCodedAddress= null;
	private GeoCodedAddress destGeoCodedAddress=null;
	
	// Is there some other place where we can define these constants
	public static String PROP_ROUTE_STYLE_KEY="routeStyle";
	public static String PROP_AVOID_SETTINGS_KEY="avoid";
    
    public CommuteAlertDetails(AlertDetails detail, TnContext tc)
    {
       
        CommuteAlert alert = (CommuteAlert)detail.getAlert();
        this.name = alert.getName();
        this.alertId = alert.getId();
        
        String originLabel = alert.getOriginLabel();
        this.origin = AlertUtil.convertLableToStop(originLabel, tc);
        String destinationLabel = alert.getDestinationLabel();
        this.destination = AlertUtil.convertLableToStop(destinationLabel, tc);
        
        this.originFirstLine=originLabel;
        this.destinationFirstLine=destinationLabel;
        List<GeoCodedAddress> addresses = AlertUtil.validateAddress(this.originFirstLine, tc);
        this.originGeoCodedAddress=  (addresses != null && addresses.size() > 0) ?  addresses.get(0):null;
        addresses = AlertUtil.validateAddress(this.destinationFirstLine, tc);
        this.destGeoCodedAddress= (addresses != null && addresses.size() > 0) ?  addresses.get(0):null;
        this.destinationFirstLine=destinationLabel;
        if(alert instanceof CommuteAlert20)
        {
        	CommuteAlert20 alert20 = (CommuteAlert20) alert;
            Property[] props = alert20.getProperty();
            for (Property p : props) 
            {
                 if( logger.isDebugEnabled() )
                 {
                    logger.debug(p.getKey() + " = " + p.getValue());
                 }
                 if(PROP_ROUTE_STYLE_KEY.equalsIgnoreCase(p.getKey()) )
                		 this.routeStyle=p.getValue();
                 if(PROP_AVOID_SETTINGS_KEY.equalsIgnoreCase(p.getKey()) )
            		 this.avoidSetting=p.getValue();
            }
          
        }
    }

    public long getAlertId()
    {
        return alertId;
    }

    public void setAlertId(long alertId)
    {
        this.alertId = alertId;
    }

//    public Stop getDestination()
//    {
//        return destination;
//    }

    public void setDestination(Stop destination)
    {
        this.destination = destination;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

//    public Stop getOrigin()
//    {
//        return origin;
//    }

    public void setOrigin(Stop origin)
    {
        this.origin = origin;
    }

	public String getRouteStyle() {
		return routeStyle;
	}

	public void setRouteStyle(String routeStyle) {
		this.routeStyle = routeStyle;
	}

	public String getAvoidSetting() {
		return avoidSetting;
	}

	public void setAvoidSetting(String avoidSetting) {
		this.avoidSetting = avoidSetting;
	}

	public GeoCodedAddress getOriginGeoCodedAddress() {
		return originGeoCodedAddress;
	}

	public GeoCodedAddress getDestGeoCodedAddress() {
		return destGeoCodedAddress;
	}

	  
}
