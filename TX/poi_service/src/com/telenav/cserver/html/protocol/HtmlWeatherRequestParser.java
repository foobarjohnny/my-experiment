/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.html.protocol;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.me.JSONObject;

import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.html.datatype.HtmlClientInfo;
import com.telenav.cserver.framework.html.protocol.HtmlProtocolRequestParser;
import com.telenav.cserver.framework.html.util.HtmlFeatureHelper;
import com.telenav.cserver.framework.html.util.HtmlFrameworkConstants;
import com.telenav.cserver.poi.struts.util.PoiUtil;
import com.telenav.cserver.weather.executor.I18NWeatherRequest;
import com.telenav.j2me.datatypes.Stop;

/**
 * @TODO	Define executor type
 * 			Parse the browser request
 * @author  panZhang@telenav.cn
 * @version 1.0 2009-5-25
 */
public class HtmlWeatherRequestParser extends HtmlProtocolRequestParser{
    private static Logger log = Logger.getLogger(HtmlWeatherRequestParser.class);
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.telenav.cserver.poi.protocol.BrowserProtocolRequestParser#getExecutorType
     * ()
     */
    @Override
    public String getExecutorType() {
        return "I18NWeather";
    }

    /*
     * (non-Javadoc)
     * 
     * @seecom.telenav.cserver.poi.protocol.BrowserProtocolRequestParser#
     * parseBrowserRequest(javax.servlet.http.HttpServletRequest)
     */
    @Override
    public ExecutorRequest parseBrowserRequest(HttpServletRequest httpRequest) 
        throws Exception {
        
    	HtmlClientInfo clientInfo = (HtmlClientInfo)httpRequest.getAttribute(HtmlFrameworkConstants.HTML_CLIENT_INFO);
    	boolean isUseOriginalPicCode = HtmlFeatureHelper.getInstance().supportFeature(clientInfo, HtmlFrameworkConstants.FEATURE.WEATHER_ORIGINAL);

        // Get the JSON request.
    	String addressString = httpRequest.getParameter("addressString");
    	String distanceUnit = httpRequest.getParameter("distanceUnit");
    	int dUnit = HtmlFrameworkConstants.DUNIT_MILES;
    	try
    	{
    		dUnit = Integer.parseInt(distanceUnit);
    	}
    	catch(NumberFormatException e)
    	{
    	}
        boolean isISU = true;
        if(HtmlFrameworkConstants.DUNIT_MILES == dUnit)
        {
        	isISU = false;
        }
        
        JSONObject addressJO =  new JSONObject(addressString);
        Stop address = PoiUtil.convertAddress(addressJO);
        

        I18NWeatherRequest request = new I18NWeatherRequest();
        request.setLocation(address);
        request.setCelciusUnit(isISU);
        request.setKilometerUnit(isISU);
        request.setLocale(clientInfo.getLocale());
        request.setUseOrinalPicCode(isUseOriginalPicCode);
        
        return request;
    } 
}
