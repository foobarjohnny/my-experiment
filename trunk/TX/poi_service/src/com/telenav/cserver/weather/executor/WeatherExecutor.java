/*
 * Copyright (c) 2009 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 */
package com.telenav.cserver.weather.executor;

import com.telenav.cserver.framework.executor.AbstractExecutor;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.poi.struts.util.RGCUtil;
import com.telenav.cserver.util.TnConstants;
import com.telenav.cserver.util.TnUtil;
import com.telenav.cserver.weather.struts.datatype.WeatherView;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * @author pzhang
 *
 * @version 1.0, 2009-5-25
 */
public class WeatherExecutor extends AbstractExecutor{
    public static final int PAGE_SIZE = 50;
    /**
     * 
     */
    public void doExecute(ExecutorRequest req, ExecutorResponse resp,
            ExecutorContext context) throws ExecutorException {
        WeatherRequest request = (WeatherRequest) req;
        WeatherResponse response = (WeatherResponse) resp;
        TnContext tnContext = context.getTnContext();
        try {
            //get the weather information, this information include one weekly's forcast
            Stop stop = doRGC(request.getLocation(),tnContext);
            WeatherView weatherView = WeatherProxy.getInstance().getWeatherView(stop, request.isCanadianCarrier());
            response.setWeatherView(weatherView);
            response.setLocation(stop);
        } catch (Exception e) {
            throw new ExecutorException(e);
        }
    }
    
    private Stop doRGC(Stop stop,TnContext tnContext)
    {
        RGCUtil rgcutil = new RGCUtil();
        //if not state/city, get the full location information
        if(stop!= null && (TnUtil.getString(stop.state).equals("") && (TnUtil.getString(stop.city).equals(""))))
        {
            //do RGC
            stop = rgcutil.getCurrentLocation(stop.lat, stop.lon, tnContext); 
        }
    
        //if no location, use a default location
        if(stop == null)
        {
            //use default city
            stop = rgcutil.getCurrentLocation(TnConstants.DEFAULT_CITY_LAT,TnConstants.DEFAULT_CITY_LON,tnContext);
        }
        
        return stop;
    }
}
