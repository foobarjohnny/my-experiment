/*
 * Copyright (c) 2009 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 */
package com.telenav.cserver.weather.protocol;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.browser.framework.protocol.BrowserProtocolResponseFormatter;
import com.telenav.cserver.browser.util.MessageHelper;
import com.telenav.cserver.exception.TnException;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.util.TnUtil;
import com.telenav.cserver.weather.executor.WeatherResponse;
import com.telenav.cserver.weather.struts.datatype.WeatherInfo;
import com.telenav.cserver.weather.struts.datatype.WeatherView;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;

/**
 * @author pzhang
 * 
 * @version 1.0, 2009-5-25
 */
public class WeatherResponseFormatter extends
        BrowserProtocolResponseFormatter {
    private static Logger log = Logger
            .getLogger(WeatherResponseFormatter.class);

    public void parseBrowserResponse(HttpServletRequest httpRequest,
            ExecutorResponse executorResponse) throws Exception {
        WeatherResponse response = (WeatherResponse) executorResponse;
        DataHandler handler = (DataHandler) httpRequest
        .getAttribute(BrowserFrameworkConstants.CLIENT_INFO);

        //get client version
        String version = handler.getClientInfo(DataHandler.KEY_VERSION); 
        
        log.info("Weather.doAction() before getLocation()");
        //get the location that need display the weather information
        Stop stop = response.getLocation();
        if(stop == null)
        {
            //if can't get location information, throw exception with necessary error information
            throw new TnException("weather.LOCATION_ERROR");
        }
        
        log.info("Weather.doAction() after getLocation():" + stop.state + "," + 
                stop.city + "," + stop.label + "," + stop.lat + "," + stop.lon);

        WeatherView weatherView = response.getWeatherView();
        WeatherInfo currentWeatherInfo = weatherView.getCurrentWeather();
        //get weekly forcast
        List<WeatherInfo> weekdays = weatherView.getWeekWeather();
        
        log.info("Weather.doAction() after get WeatherView");
        
        //prepare the information used in view screen
        String title = getTitle(stop,handler);
        String unit = TnUtil.getWeatherUnit(version);
        if(TnUtil.isCanadianCarrier(handler)){
        	unit = TnUtil.getWeatherUnitForCelsius();
        }
        //Map screenParameter = new HashMap();
        //screenParameter.put("title", title);
        //screenParameter.put("unit", unit);
        //screenParameter.put("WeatherInfo", currentWeatherInfo);
        
        JSONArray joList = new JSONArray();
        for (WeatherInfo info : weekdays) {
            JSONObject jo = info.toJSON();
            jo.put("title", title);
            jo.put("unit", unit);
            doWeatherI18N(jo,handler);
            
            joList.put(jo);
        }
        
        JSONObject joCurrent = currentWeatherInfo.toJSON();
        joCurrent.put("title", title);
        joCurrent.put("unit", unit);
        joCurrent.put("titleWithoutState", getTitleWithoutState(stop,handler));
        doWeatherI18N(joCurrent,handler);
        
        String todayDate = joCurrent.getString("longWeekDesc") + ", " + TnUtil.getWeatherDisplayDate(currentWeatherInfo.getDate());
        joCurrent.put("todayDate", todayDate);
        
        TxNode node = new TxNode();
        node.addMsg(joCurrent.toString());
        node.addMsg(joList.toString());
        //handler.setParameter("weatherDataNode", node);
        httpRequest.setAttribute("node", node); 
    }

    /**
     * 
     * @param jo
     * @param handler
     * @return
     * @throws JSONException
     */
    private JSONObject doWeatherI18N(JSONObject jo,DataHandler handler) throws JSONException
    {
        jo.put("feel", MessageHelper.getInstance().getMessageValue(handler,jo.getString("feel")));
        jo.put("status", MessageHelper.getInstance().getMessageValue(handler,jo.getString("status")));
        jo.put("longWeekDesc", MessageHelper.getInstance().getMessageValue(handler,jo.getString("longWeekDesc")));
        jo.put("shortWeekDesc", MessageHelper.getInstance().getMessageValue(handler,jo.getString("shortWeekDesc")));
        
        return jo;
    }
    
    /**
     * the title is Location's address
     * @param stop
     * @return
     * @throws TnException
     */
    private String getTitle(Stop stop,DataHandler handler) throws TnException
    {
        //default title is current location
        String title = MessageHelper.getInstance().getMessageValue(handler, "common.currentLocation");
        
        if (stop == null) return title;
        
        int stateLen = 0;
        int cityLen = 0;
        
        //null value check
        if(stop.state != null) stateLen = stop.state.length();
        if(stop.city != null) cityLen = stop.city.length();
        
        //if have city and state, return the title as "city,state"
        if(cityLen != 0 && stateLen != 0)
        {
            title = stop.city + ", " + stop.state; 
        }
        //if only have city, return title as "city"
        else if(cityLen != 0)
        {
            title = stop.city;
        }
        //if only have state,return title as "state"
        else if(stateLen != 0)
        {
            title = stop.state;
        }               
        
        return title;
    }

    /**
     * the title is Location's address
     * @param stop
     * @return
     * @throws TnException
     */
    private String getTitleWithoutState(Stop stop,DataHandler handler) throws TnException
    {
        //default title is current location
        String title = MessageHelper.getInstance().getMessageValue(handler, "common.currentLocation");
        
        if (stop == null) return title;
        
        int stateLen = 0;
        int cityLen = 0;
        
        //null value check
        if(stop.state != null) stateLen = stop.state.length();
        if(stop.city != null) cityLen = stop.city.length();
        
        //if have city and state, return the title as "city,state"
        if(cityLen != 0 && stateLen != 0)
        {
            title = stop.city; 
        }
        //if only have city, return title as "city"
        else if(cityLen != 0)
        {
            title = stop.city;
        }
        //if only have state,return title as "state"
        else if(stateLen != 0)
        {
            title = stop.state;
        }               
        
        return title;
    }
}
