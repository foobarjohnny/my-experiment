/*
 * Copyright (c) 2011 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 */
package com.telenav.cserver.weather.executor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.backend.util.WebServiceUtils;
import com.telenav.cserver.exception.TnException;
import com.telenav.cserver.framework.executor.AbstractExecutor;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.poi.struts.util.RGCUtil;
import com.telenav.cserver.util.TnConstants;
import com.telenav.cserver.util.TnUtil;
import com.telenav.cserver.util.WebServiceConfigurator;
import com.telenav.cserver.weather.struts.datatype.WeatherInfo;
import com.telenav.cserver.weather.struts.datatype.WeatherView;
import com.telenav.datatypes.locale.v10.Country;
import com.telenav.datatypes.locale.v10.Language;
import com.telenav.datatypes.locale.v10.Locale;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.ws.datatypes.address.CityState;
import com.telenav.ws.datatypes.address.GeoCode;
import com.telenav.ws.datatypes.address.Location;
import com.telenav.ws.datatypes.common.SpeedUnit;
import com.telenav.ws.datatypes.content.weather.I18NWeatherDetails;
import com.telenav.ws.datatypes.content.weather.I18NWeatherReport;
import com.telenav.ws.datatypes.content.weather.I18NWeatherServiceRequestDTO;
import com.telenav.ws.datatypes.content.weather.I18NWeatherServiceResponseDTO;
import com.telenav.ws.datatypes.content.weather.Temperature;
import com.telenav.ws.services.weather.WeatherServiceStub;

/**
 * @author Derek Liu
 *
 * @version 1.0, 2011-3-1
 */
public class I18NWeatherExecutor extends AbstractExecutor{
    private Logger logger = Logger.getLogger(I18NWeatherExecutor.class);
    public static final int PAGE_SIZE = 50;
    /**
     * 
     */
    public void doExecute(ExecutorRequest req, ExecutorResponse resp,
            ExecutorContext context) throws ExecutorException {
        I18NWeatherRequest request = (I18NWeatherRequest) req;
        I18NWeatherResponse response = (I18NWeatherResponse) resp;
        response.setCelciusUnit(request.isCelciusUnit());
        response.setKilometerUnit(request.isKilometerUnit());
        response.setLocale(request.getLocale());
        
        TnContext tnContext = context.getTnContext();
        try {
            //get the weather information, this information include one weekly's forcast
            Stop stop = doRGC(request.getLocation(),tnContext);
            WeatherView weatherView = this.getWeatherView(stop, request);
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
    
    /**
     * get WeatherView, including current weather and week forecast
     * 
     * @param stop
     * @param locale 
     * @return
     * @throws TnException
     */
    private WeatherView getWeatherView(Stop stop, I18NWeatherRequest request) throws TnException
    {
        boolean isCelciusUnit = request.isCelciusUnit();
        boolean isKilometerUnit = request.isKilometerUnit();
        boolean isUseOriginalPicCode = request.isUseOriginalPicCode();
        String locale = request.getLocale();
        
        WeatherView weatherView = new WeatherView();
        
        //get details from WS
        I18NWeatherDetails detailsDTO = getWeatherForLocation(stop, TnConstants.NUM_OF_WEEK, isKilometerUnit, isUseOriginalPicCode, locale);
        //get current weather
        I18NWeatherReport currentReport = detailsDTO.getCurrentWeather();
        logger.info("TnManagerImpl.getWeatherView. before convertToWeatherInfo");
        WeatherInfo currentInfo = convertToWeatherInfo(currentReport,isCelciusUnit,locale);
        weatherView.setCurrentWeather(currentInfo);
        
        //get week forecast
        I18NWeatherReport[] forecast = detailsDTO.getForecast();
        
        List<WeatherInfo> v = new ArrayList<WeatherInfo>();
        for (int i = 0; i < Math.min(forecast.length, TnConstants.NUM_OF_WEEK); i++) 
        {
            I18NWeatherReport forecastReport = forecast[i];
            WeatherInfo info = convertToWeatherInfo(forecastReport,isCelciusUnit,locale);
            v.add(info);
        }
        
        weatherView.setWeekWeather(v);
        
        return weatherView;
    }
    
    

    
    /**
     * convert the webservice return object to our weather object
     * @param currentReport
     * @return
     */
    private WeatherInfo convertToWeatherInfo(I18NWeatherReport currentReport, boolean isCelciusUnit, String localeStr)
    {
        logger.info("TnManagerImpl.convertToWeatherInfo.");
        WeatherInfo info = new WeatherInfo();
        
        Temperature temp = currentReport.getCurrentTemperature();
        if(temp != null)
        {
        	////No need floating point in temperature
            ///info.setTemp(Integer.toString(((int)report.getCurrentTemperature().getFarenheit())));
        	float tempInfo = currentReport.getCurrentTemperature().getFarenheit();
        	if(isCelciusUnit){
        		tempInfo = currentReport.getCurrentTemperature().getCelcius();
        	}
            int i = Math.round(tempInfo);
            info.setTemp(Integer.toString(i));
        }
        else
        {
            info.setTemp("");
        }
 
        info.setStatus(getStatus(currentReport));
        info.setFeel(currentReport.getTempCondition());
        info.setWindDirection(currentReport.getWind().getDirection());
        info.setWindSpeed(currentReport.getWind().getSpeed());
        
        //set humidity without .0
        int humidity = (int)currentReport.getHumidity();
        info.setHumidity(String.valueOf(humidity));
        info.setTemperatureCode(currentReport.getTempCode());
        info.setWeatherCode(currentReport.getPicCode());
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentReport.getDate());
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        info.setDate(calendar.getTime());
        info.setDayOfWeek(dayOfWeek);
        
        Temperature high = currentReport.getHighTemperature();
        if(high != null)
        {
        	////No need floating point in high
            ///info.setHigh(Integer.toString((int)report.getHighTemperature().getFarenheit()));
        	float highInfo = currentReport.getHighTemperature().getFarenheit();
        	if(isCelciusUnit){
        		highInfo = currentReport.getHighTemperature().getCelcius();
        	}
            int i = Math.round(highInfo);
            info.setHigh(Integer.toString(i));
        }
        else
        {
            info.setHigh("");
        }
        
        Temperature low = currentReport.getLowTemperature();
        if(low != null)
        {
        	////No need floating point in low
            ///info.setLow(Integer.toString((int)report.getLowTemperature().getFarenheit()));
        	float lowInfo = currentReport.getLowTemperature().getFarenheit();
        	if(isCelciusUnit){
        		lowInfo = currentReport.getLowTemperature().getCelcius();
        	}
            int i = Math.round(lowInfo);
            info.setLow(Integer.toString(i));
        }
        else
        {
            info.setLow("");
        }
             
        return info;
    }
    
    private static String getStatus(I18NWeatherReport currentReport)
    {
        String status = "";
        String precipitation = currentReport.getPrecipitation();
        if(precipitation != null)
        {
            status = precipitation;
        }
        if(status == null || status.length() == 0 || status.equalsIgnoreCase("UNKNOWN"))
        {
            status = currentReport.getSkyCondition();
        }
        
        return TnUtil.getString(status);
    }
    
    
    /**
     * get weather from WS
     * 
     * @param stop
     * @param numberOfDays
     * @param locale 
     * @return
     */
    private I18NWeatherDetails getWeatherForLocation(Stop stop, int numberOfDays, boolean isKMUnit, boolean isUseOriginalPicCode, String localeStr) 
     throws TnException
    {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("getWeatherForLocation");
        if(stop == null)
        {
            cli.addData("location", "stop is null.");
            cli.complete();
            throw new TnException("stop is null",TnException.ERROR_INVALID_REQUEST);            
        }
        
        long start = System.currentTimeMillis();
        I18NWeatherServiceRequestDTO request = createRequest(isUseOriginalPicCode);
        
        //construct location
        Location location = new Location();
        
        String city = stop.city;
        String state = stop.state;
        String zip = stop.zip;
        StringBuffer cliStringBuffer = new StringBuffer();
        if(logger.isDebugEnabled())
        {
            logger.debug("Start to getWeatherForLocation of " + numberOfDays + " days....");
        }
        // use lat/lon
		GeoCode latLon = new GeoCode();
		latLon.setLatitude((double) (stop.lat / 1e5));
		latLon.setLongitude((double) (stop.lon / 1e5));

		location.setGeoCode(latLon);
		
		cliStringBuffer.append("lat=" + stop.lat + "&lon=" + stop.lon);
		
		if(logger.isDebugEnabled())
		{
			logger.debug("setGeoCode, lat:" + (double) (stop.lat / 1e5)
					+ ", lon:" + (double) (stop.lon/ 1e5));
		}
        if(isValid(city) && isValid(state))
        {
            CityState cs = new CityState();
            cs.setCity(city);
            cs.setState(state);
            location.setCityState(cs);
            
            cliStringBuffer.append("&city=" + city + "&state=" + state);
            if(logger.isDebugEnabled())
            {
                logger.debug("setCityState, city:" + city + ", state:" + state);
            }
        }
        if(isValid(zip))
        {
        	location.setPostalCode(zip);
        	cliStringBuffer.append("&postalCode=" + zip);
            if(logger.isDebugEnabled())
            {
                logger.debug("setPostalCode:postalCode=" + zip);
            }
        }
        
        request.setLocation(location);
        cli.addData("location", cliStringBuffer.toString());
        
        Locale locale = getLocale(localeStr);
		request.setLocale(locale);
		cli.addData("locale", "country=" + locale.getCountry() + "&language="
				+ locale.getLanguage());
		
        request.setNumberOfDays(numberOfDays);
        
        if(isKMUnit){
        	request.setSpeedUnit(SpeedUnit.KPH);
        }else{
        	request.setSpeedUnit(SpeedUnit.MPH);
        }

        WeatherServiceStub stub = null;
        I18NWeatherServiceResponseDTO response = null;
        try
        {
            String endpoint = WebServiceConfigurator.getUrlOfWeather();
            stub = new WeatherServiceStub(endpoint);            
        }
        catch(Throwable e)
        {
        	logger.error("can not build up WeatherService, please check your URL_WEATHER value in web_services.properties");
            e.printStackTrace();
            cli.addData(CliConstants.LABEL_ERROR, "ERROR_CANNOT_CONNECTTO_WS");
            cli.setStatus(e);
            cli.complete();
            logger.error(e, e);
            WebServiceUtils.cleanupStub(stub);
            throw new TnException(TnException.ERROR_CANNOT_CONNECTTO_WS, e);     
        }
        
        try
        {
            response = stub.getI18NCurrentNForecast(request);
        }
        catch(Throwable e)
        {
            cli.addData(CliConstants.LABEL_ERROR, "ERROR_WS_UNKNOWN");
            cli.setStatus(e);
            cli.complete();
            logger.error(e, e);
            throw new TnException(TnException.ERROR_WS_UNKNOWN, e);     
        }
		finally {
			WebServiceUtils.cleanupStub(stub);
		}
        
        
        if(response == null)
        {
            cli.addData(CliConstants.LABEL_ERROR,"ERROR_WS_NULL_RESPONSE");
            cli.complete();
            logger.error("response is NULL!");
            throw new TnException(TnException.ERROR_WS_NULL_RESPONSE);     
        }
        
        I18NWeatherDetails detailsDTO = response.getWeatherDetails();
        
        
        if(logger.isDebugEnabled())
        {
            logger.debug("End to getWeatherForLocation, cost " + (System.currentTimeMillis() - start)
                    + " ms.");
        }
        cli.complete();
        return detailsDTO;
    }
    
    
    private static boolean isValid(String s)
    {
        return s != null && s.length() > 0;
    }
    
    /**
     * create an WeatherServiceRequestDTO
     * 
     * @return
     */
    private static I18NWeatherServiceRequestDTO createRequest(boolean isUseOriginalPicCode)
    {
        I18NWeatherServiceRequestDTO request = new I18NWeatherServiceRequestDTO();
        request.setClientName("6x-cserver");
        request.setClientVersion("1.0");
        request.setTransactionId("unknown");
        request.setUseOriginalPicCode(isUseOriginalPicCode);//weatherService20
        
        return request;
    }
    
 
    /**
     *  convert locale String to Locale
     *  Till now (2011/7/1), According to xNav feedback, the languages that are currently supported by third part are English, Spanish, 
     *  Italian, French, Portuguese, Dutch, German, Swedish and traditional Chinese. 

     * @param localeStr
     * @return
     */
    private Locale getLocale(String localeStr) 
    {
    	Locale locale = new Locale();
    	if(localeStr.equals("en_US")){
            locale.setCountry(Country.US);
            locale.setLanguage(Language.ENG);
    	}else if(localeStr.equals("en_GB")){
            locale.setCountry(Country.GB);
            locale.setLanguage(Language.ENG);
    	}else if(localeStr.equals("de_DE")){
    		locale.setCountry(Country.DE);
    		locale.setLanguage(Language.GER);     		
    	}else if(localeStr.equals("es_ES")){   
    		locale.setCountry(Country.ES);
    		locale.setLanguage(Language.SPA);  
    	}else if(localeStr.equals("es_MX")){   
    		locale.setCountry(Country.MX);
    		locale.setLanguage(Language.SPA);  
    	}else if(localeStr.equals("fr_FR")){  
    		locale.setCountry(Country.FR);
    		locale.setLanguage(Language.FRE);
    	}else if(localeStr.equals("it_IT")){   
    		locale.setCountry(Country.IT);
    		locale.setLanguage(Language.ITA);
    	}else if(localeStr.equals("nl_NL")){  
    		locale.setCountry(Country.NL);
    		locale.setLanguage(Language.DUT);       		
    	}else if(localeStr.equals("pt_BR")){  
    		locale.setCountry(Country.BR);
    		locale.setLanguage(Language.POR);
    	}else if(localeStr.equals("fr_CA")){  
            locale.setCountry(Country.CA);
            locale.setLanguage(Language.FRE);
        }else if(localeStr.equals("en_CA")){  
            locale.setCountry(Country.CA);
            locale.setLanguage(Language.ENG);
        }else{
            locale.setCountry(Country.US);
            locale.setLanguage(Language.ENG);
        }              
        return locale;
    }
}