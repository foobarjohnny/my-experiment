/**
 * (c) Copyright 2007 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.commutealert;

import java.util.List;

import org.apache.log4j.Logger;

import com.telenav.cserver.backend.ace.GeoCodeResponse;
import com.telenav.cserver.backend.ace.GeoCodingProxy;
import com.telenav.cserver.backend.datatypes.Address;
import com.telenav.cserver.backend.datatypes.ace.GeoCodedAddress;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * Alert Utility copy from nav_map
 * 
 * @author mmwang
 * @author yqchen
 * @version 1.0 2007-11-22 10:26:35
 */
public class AlertUtil
{
    protected static Logger logger = Logger.getLogger(AlertUtil.class);

    
    /**
     * convert lable to Stop
     * 
     * @param label
     * @return
     */
    public static Stop convertLableToStop(String label, TnContext tc)
    {
        Stop stop = new Stop();
        
        int k = label.indexOf(",");
        
        if(k > -1)
        {
            stop.firstLine = label.substring(0, k).trim();
            label = label.substring(k + 1);
            
            k = label.indexOf(",");
            if(k > -1)
            {
                stop.city = label.substring(0, k).trim();
                label = label.substring(k + 1);
            }
            
            k = label.indexOf(",");
            if(k > -1)
            {
                stop.state = label.substring(0, k).trim();
                label = label.substring(k + 1);
            }
            
            stop.country = label.trim();            
        }
        else
        {
            int k2 = label.indexOf("_");
            if(k2 > -1)
            {
                stop.lat = Integer.parseInt(label.substring(0,k2));
                label = label.substring(k2 + 1);
                
                k2 = label.indexOf("_");
                if(k2 > -1)
                {
                    stop.lon = Integer.parseInt(label.substring(0, k2));
                    label = label.substring(k2 + 1);
                    
                    stop.label = label.trim();      
                }
                else
                {
                    stop.lon = Integer.parseInt(label);
                }
                     
                
            }
        }
        
        if(stop.lat == 0 && stop.lon == 0)
        {
            Stop[] geoStops = validateAddress(stop, tc);
            if(geoStops != null && geoStops.length > 0)
            {
                stop = geoStops[0];
            }
        }
        
  //      logger.debug("convertLableToStop================firstLine:" + stop.firstLine);
        return stop;
    }
    
    public static Stop[] validateAddress(Stop inputStop, TnContext tnContext)
    {
    	Stop[] stopArray = null;
    	try
    	{
    		GeoCodingProxy proxy = GeoCodingProxy.getInstance(tnContext);
        	Address address = DataConverter.convertStopToAddress(inputStop);
        	GeoCodeResponse geoCodeResponse = proxy.geoCode(address);
        	if(geoCodeResponse != null && geoCodeResponse.getStatus().isSuccessful())
        	{
        		List<GeoCodedAddress> matchList = geoCodeResponse.getMatches();
        		if(matchList != null)
        		{
        			int matchCount = matchList.size();
        			stopArray = new Stop[matchCount];
            		for(int i = 0; i < matchCount; i++)
            		{
            			stopArray[i] = DataConverter.convertGeoCodedAddressToStop(matchList.get(i));
            			if(logger.isDebugEnabled())
            			{
            				logger.debug("validated stopArray[" + i + "]=" + stopArray[i]);
            			}
            		}
        		}
        	}
    	}
    	catch(ThrottlingException te)
    	{
    		if(logger.isDebugEnabled())
    		{
    			logger.debug("Throttling Exception in GeoCodeProxy");
    		}
    		
    	}
    	catch(Exception e)
    	{
    		if(logger.isDebugEnabled())
    		{
    			logger.debug("failed to validate address");
    		}
    	}
    	
    	return stopArray;
    }
    
    /**
     * @param firstLine - The firstline is supposed to be the firstline of an address to be geocoded; The assumption here is that Geocoding will work well even if everythuing is passed in the firstline
     * @param tnContext
     * @return
     */
    public static List<GeoCodedAddress>  validateAddress(String firstLine, TnContext  tnContext)
    {
    	if(logger.isDebugEnabled())
		{
			logger.debug("FirstLine of the address to be Geocoded:"+firstLine);
		}
    	
    	try
    	{
    		GeoCodingProxy proxy = GeoCodingProxy.getInstance(tnContext);
        	Address address = new Address();
        	address.setFirstLine(firstLine);
        	GeoCodeResponse geoCodeResponse = proxy.geoCode(address);
        	if(geoCodeResponse != null && geoCodeResponse.getStatus().isSuccessful())
        	{
        		List<GeoCodedAddress> matchList = geoCodeResponse.getMatches();
        		if(matchList != null)
        		{
        			int matchCount = matchList.size();
        			return matchList;
        		}
        	}
    	}
    	catch(ThrottlingException te)
    	{
    		if(logger.isDebugEnabled())
    		{
    			logger.debug("Throttling Exception in GeoCodeProxy");
    		}
    		
    	}
    	catch(Exception e)
    	{
    		if(logger.isDebugEnabled())
    		{
    			logger.debug("failed to validate address"+e.getMessage());
    		}
    	}
    	return null;
    }
}
