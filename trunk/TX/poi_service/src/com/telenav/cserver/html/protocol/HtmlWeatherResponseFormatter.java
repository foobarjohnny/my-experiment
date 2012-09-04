/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.html.protocol;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.telenav.cserver.exception.TnException;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.html.datatype.HtmlClientInfo;
import com.telenav.cserver.framework.html.protocol.HtmlProtocolResponseFormatter;
import com.telenav.cserver.framework.html.util.HtmlClientHelper;
import com.telenav.cserver.framework.html.util.HtmlCommonUtil;
import com.telenav.cserver.framework.html.util.HtmlFeatureHelper;
import com.telenav.cserver.framework.html.util.HtmlFrameworkConstants;
import com.telenav.cserver.framework.html.util.HtmlMessageHelper;
import com.telenav.cserver.html.util.HtmlPoiUtil;
import com.telenav.cserver.util.TnUtil;
import com.telenav.cserver.weather.executor.I18NWeatherResponse;
import com.telenav.cserver.weather.struts.datatype.WeatherInfo;
import com.telenav.cserver.weather.struts.datatype.WeatherView;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.ws.datatypes.common.SpeedUnit;

/**
 * @TODO	format the data returned from browser server and put it into request
 * @author  panZhang@telenav.cn
 * @version 1.0 2009-5-25
 */
public class HtmlWeatherResponseFormatter extends
        HtmlProtocolResponseFormatter {
    private static Logger log = Logger
            .getLogger(HtmlWeatherResponseFormatter.class);

    public void parseBrowserResponse(HttpServletRequest httpRequest,
            ExecutorResponse executorResponse) throws Exception {
        I18NWeatherResponse response = (I18NWeatherResponse) executorResponse;
        HtmlClientInfo clientInfo = (HtmlClientInfo)httpRequest.getAttribute(HtmlFrameworkConstants.HTML_CLIENT_INFO);
        //get client version
        String version = clientInfo.getVersion(); 
        
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
        //7.x does not use this title anymore
        String title = "";//getTitle(stop,clientInfo);
        String unit = "";
        if(response.isCelciusUnit()){
        	unit = TnUtil.getWeatherUnitForCelsius();
        }
        else
        {
        	unit = TnUtil.getWeatherUnit(version);
        }
        //Map screenParameter = new HashMap();
        //screenParameter.put("title", title);
        //screenParameter.put("unit", unit);
        //screenParameter.put("WeatherInfo", currentWeatherInfo);
        
        JSONArray joList = new JSONArray();
        for (WeatherInfo info : weekdays) {
            String windDescription = getWindDescription(clientInfo, info, response);
            info.setWind(windDescription);         	
            JSONObject jo = info.toJSON();
            //jo.put("title", title);
            jo.put("unit", unit);
             doWeatherI18N(jo,clientInfo);
            
            joList.put(jo);
        }
        
        String windDescription = getWindDescription(clientInfo, currentWeatherInfo, response);
        currentWeatherInfo.setWind(windDescription);        
        JSONObject joCurrent = currentWeatherInfo.toJSON();
        //joCurrent.put("title", title);
        joCurrent.put("unit", unit);
        joCurrent.put("titleWithoutState", getTitleWithoutState(stop,clientInfo));
        doWeatherI18N(joCurrent,clientInfo);
        
        String todayDate = joCurrent.getString("longWeekDesc") + ", " + getDateDisplay(clientInfo,currentWeatherInfo.getDate());
        joCurrent.put("todayDate", todayDate);
        
        //TxNode node = new TxNode();
        //node.addMsg(joCurrent.toString());
        //node.addMsg(joList.toString());
           //handler.setParameter("weatherDataNode", node);
        //httpRequest.setAttribute("node", node); 
		
		JSONObject ajaxResponse = new JSONObject();
		
		ajaxResponse.put("today", joCurrent.toString());
		ajaxResponse.put("weekList", joList.toString());
		httpRequest.setAttribute("ajaxResponse", ajaxResponse.toString());
    }

    public String getDateDisplay(HtmlClientInfo clientInfo,Date date)
    {
    	String weatherDateFormat = HtmlFeatureHelper.getInstance().getFeature(clientInfo, HtmlFrameworkConstants.FEATURE.WEATHERDATEFORMAT);
    	weatherDateFormat = HtmlCommonUtil.getString(weatherDateFormat);
    	String displayDate;
    	if("".equals(weatherDateFormat))
    	{
    		displayDate = TnUtil.getWeatherDisplayDate(date);
    	}
    	else
    	{
    		displayDate =  TnUtil.getFormatedDate(date,weatherDateFormat);
    	}
    	return displayDate;
    }
    
    public String getWindDescription(HtmlClientInfo clientInfo, WeatherInfo weatherInfo, I18NWeatherResponse response)
    {
	    float  windSpeed = weatherInfo.getWindSpeed();
	    String windDirection = weatherInfo.getWindDirection();
	    
	    String directionStr = HtmlMessageHelper.getInstance().getMessageValue(clientInfo,windDirection);
	    if(directionStr!=null){
	    	windDirection = directionStr;
	    }
	    
	    String windDescription = "";
	    String localeStr = response.getLocale();
	    
	    if(response.isKilometerUnit()){
	    	if(localeStr.equals("fr_CA")){
	    		windDescription = Math.round(windSpeed) + " " + windDirection + " " + HtmlFrameworkConstants.DUNIT_DISPLAY_KPH;
	    	}else{
	        	windDescription = Math.round(windSpeed) + " " + HtmlFrameworkConstants.DUNIT_DISPLAY_KPH + " " + windDirection;     		
	    	}
	    	
	    }else{
	    	windDescription = Math.round(windSpeed) + " " + SpeedUnit.MPH + " " + windDirection;
	    }
	    return windDescription;
    }   
    
    /**
     * 
     * @param jo
     * @param handler
     * @return
     * @throws JSONException
     */
    private JSONObject doWeatherI18N(JSONObject jo,HtmlClientInfo clientInfo) throws JSONException
    {
    	String feel = HtmlMessageHelper.getInstance().getMessageValue(clientInfo,jo.getString("feel"));
    	if(feel != null){
    		jo.put("feel", feel);
    	}
    	
    	String status = HtmlMessageHelper.getInstance().getMessageValue(clientInfo,jo.getString("status"));
    	if(status != null){
    		jo.put("status",status);
    	}
    	
    	String longWeekDesc = HtmlMessageHelper.getInstance().getMessageValue(clientInfo,jo.getString("longWeekDesc"));
    	if(longWeekDesc != null){
        	jo.put("longWeekDesc", longWeekDesc);    		
    	}
    	
    	String shortWeekDesc = HtmlMessageHelper.getInstance().getMessageValue(clientInfo,jo.getString("shortWeekDesc"));
    	if(shortWeekDesc != null){
            jo.put("shortWeekDesc", shortWeekDesc);    		
    	}
    	
        return jo;
    }
    
    /**
     * the title is Location's address
     * @param stop
     * @return
     * @throws TnException
     */
//    private String getTitle(Stop stop,HtmlClientInfo clientInfo) throws TnException
//    {
//        //default title is current location
//        String title = HtmlMessageHelper.getInstance().getMessageValue(clientInfo, "common.currentLocation");
//        
//        if (stop == null) return title;
//        
//        int stateLen = 0;
//        int cityLen = 0;
//        
//        //null value check
//        if(stop.state != null) stateLen = stop.state.length();
//        if(stop.city != null) cityLen = stop.city.length();
//        
//        //if have city and state, return the title as "city,state"
//        if(cityLen != 0 && stateLen != 0)
//        {
//            title = stop.city + ", " + stop.state; 
//        }
//        //if only have city, return title as "city"
//        else if(cityLen != 0)
//        {
//            title = stop.city;
//        }
//        //if only have state,return title as "state"
//        else if(stateLen != 0)
//        {
//            title = stop.state;
//        }               
//        
//        return title;
//    }

    /**
     * the title is Location's address
     * @param stop
     * @return
     * @throws TnException
     */
    private String getTitleWithoutState(Stop stop,HtmlClientInfo clientInfo) throws TnException
    {
        //default title is current location
        String title = HtmlMessageHelper.getInstance().getMessageValue(clientInfo, "common.currentLocation");
        
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
