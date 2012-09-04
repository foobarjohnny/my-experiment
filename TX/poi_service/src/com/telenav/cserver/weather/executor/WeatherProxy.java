package com.telenav.cserver.weather.executor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.backend.util.WebServiceUtils;
import com.telenav.cserver.exception.TnException;
import com.telenav.cserver.util.TnConstants;
import com.telenav.cserver.util.TnUtil;
import com.telenav.cserver.util.WebServiceConfigurator;
import com.telenav.cserver.weather.struts.datatype.WeatherInfo;
import com.telenav.cserver.weather.struts.datatype.WeatherView;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.ws.datatypes.address.CityState;
import com.telenav.ws.datatypes.address.GeoCode;
import com.telenav.ws.datatypes.address.Location;
import com.telenav.ws.datatypes.common.SpeedUnit;
import com.telenav.ws.datatypes.content.weather.Precipitation;
import com.telenav.ws.datatypes.content.weather.Temperature;
import com.telenav.ws.datatypes.content.weather.WeatherDetails;
import com.telenav.ws.datatypes.content.weather.WeatherReport;
import com.telenav.ws.datatypes.content.weather.WeatherServiceRequestDTO;
import com.telenav.ws.datatypes.content.weather.WeatherServiceResponseDTO;
import com.telenav.ws.services.weather.WeatherServiceStub;

public class WeatherProxy {
	public static WeatherProxy instance = new WeatherProxy();

	private static final Logger logger = Logger.getLogger(WeatherProxy.class);

	public static WeatherProxy getInstance() {
		return instance;
	}

	/**
	 * get WeatherView, including current weather and week forecast
	 * 
	 * @param stop
	 * @return
	 * @throws TnException
	 */
	public WeatherView getWeatherView(Stop stop, boolean isCanadianCarrier)
			throws TnException {

		WeatherView weatherView = new WeatherView();

		// get details from WS
		WeatherDetails detailsDTO = getWeatherForLocation(stop,
				TnConstants.NUM_OF_WEEK, isCanadianCarrier);
		// get current weather
		WeatherReport currentReport = detailsDTO.getCurrentWeather();
		logger
				.info("TnManagerImpl.getWeatherView. before convertToWeatherInfo");
		WeatherInfo currentInfo = convertToWeatherInfo(currentReport,
				isCanadianCarrier);
		weatherView.setCurrentWeather(currentInfo);

		// get week forecast
		WeatherReport[] forecast = detailsDTO.getForecast();

		List<WeatherInfo> v = new ArrayList<WeatherInfo>();
		for (int i = 0; i < Math.min(forecast.length, TnConstants.NUM_OF_WEEK); i++) {
			WeatherReport forecastReport = forecast[i];
			WeatherInfo info = convertToWeatherInfo(forecastReport,
					isCanadianCarrier);
			v.add(info);
		}

		weatherView.setWeekWeather(v);

		return weatherView;
	}

	/**
	 * convert the webservice return object to our weather object
	 * 
	 * @param report
	 * @return
	 */
	private WeatherInfo convertToWeatherInfo(WeatherReport report,
			boolean isCanadianCarrier) {
		logger.info("TnManagerImpl.convertToWeatherInfo.");
		WeatherInfo info = new WeatherInfo();

		Temperature temp = report.getCurrentTemperature();
		if (temp != null) {
			// //No need floating point in temperature
			// /info.setTemp(Integer.toString(((int)report.getCurrentTemperature().getFarenheit())));
			float tempInfo = report.getCurrentTemperature().getFarenheit();
			if (isCanadianCarrier) {
				tempInfo = report.getCurrentTemperature().getCelcius();
			}
			int i = Math.round(tempInfo);
			info.setTemp(Integer.toString(i));
		} else {
			info.setTemp("");
		}

		info.setStatus(getStatus(report));
		info.setFeel(report.getTempCondition().getValue());

		// //No need floating point in wind
		// //info.setWind(TnUtil.getString(report.getWind()));

		// //We have to deal with the follow situation
		// //10.34 mph N

		String windDescription = TnUtil.getString(report.getWind());
		if (windDescription.indexOf(".") > -1) {
			int i = windDescription.indexOf(".") + 1;
			for (; i < windDescription.length(); i++) {
				char c = windDescription.charAt(i);
				if (c < '0' || c > '9') {
					break;
				}
			}

			String numberString = windDescription.substring(0, i);

			float f = 0;

			try {
				f = Float.parseFloat(numberString);
			} catch (NumberFormatException e) {
				logger.error("Error parsing wind description", e);
			}

			int num = Math.round(f);

			windDescription = num + windDescription.substring(i);
		}
		String[] tempWind = windDescription.split(" ");
		if (tempWind != null && tempWind.length == 3) {
			windDescription = tempWind[2] + " " + tempWind[0] + " "
					+ tempWind[1];
		}

		info.setWind(windDescription);

		// set humidity without .0
		int humidity = (int) report.getHumidity();
		info.setHumidity(String.valueOf(humidity));
		info.setTemperatureCode(report.getTempCode());
		info.setWeatherCode(report.getPicCode());

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(report.getDate());
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		info.setDate(calendar.getTime());
		info.setDayOfWeek(dayOfWeek);

		Temperature high = report.getHighTemperature();
		if (high != null) {
			// //No need floating point in high
			// /info.setHigh(Integer.toString((int)report.getHighTemperature().getFarenheit()));
			float highInfo = report.getHighTemperature().getFarenheit();
			if (isCanadianCarrier) {
				highInfo = report.getHighTemperature().getCelcius();
			}
			int i = Math.round(highInfo);
			info.setHigh(Integer.toString(i));
		} else {
			info.setHigh("");
		}

		Temperature low = report.getLowTemperature();
		if (low != null) {
			// //No need floating point in low
			// /info.setLow(Integer.toString((int)report.getLowTemperature().getFarenheit()));
			float lowInfo = report.getLowTemperature().getFarenheit();
			if (isCanadianCarrier) {
				lowInfo = report.getLowTemperature().getCelcius();
			}
			int i = Math.round(lowInfo);
			info.setLow(Integer.toString(i));
		} else {
			info.setLow("");
		}

		return info;
	}

	private static String getStatus(WeatherReport report) {
		String status = "";
		Precipitation precipitation = report.getPrecipitation();
		if (precipitation != null) {
			status = precipitation.getValue();
		}
		if (status == null || status.length() == 0
				|| status.equalsIgnoreCase("UNKNOWN")) {
			status = report.getSkyCondition().getValue();
		}

		return TnUtil.getString(status);
	}

	/**
	 * get weather from WS
	 * 
	 * @param stop
	 * @param numberOfDays
	 * @return
	 */
	public WeatherDetails getWeatherForLocation(Stop stop, int numberOfDays,
			boolean isCanadianCarrier) throws TnException {
		CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
		cli.setFunctionName("getWeatherForLocation");
		if (stop == null) {
			cli.addData("location", "stop is null.");
			cli.complete();
			throw new TnException("stop is null",
					TnException.ERROR_INVALID_REQUEST);
		}

		long start = System.currentTimeMillis();
		WeatherServiceRequestDTO request = createRequest();

		// construct location
		Location location = new Location();

		String city = stop.city;
		String state = stop.state;
		String zip = stop.zip;
		StringBuffer cliStringBuffer = new StringBuffer();
		if (logger.isDebugEnabled()) {
			logger.debug("Start to getWeatherForLocation of " + numberOfDays
					+ " days....");
		}
		// use lat/lon
		GeoCode latLon = new GeoCode();
		latLon.setLatitude((double) (stop.lat / 1e5));
		latLon.setLongitude((double) (stop.lon / 1e5));

		location.setGeoCode(latLon);

		cliStringBuffer.append("lat=" + stop.lat + "&lon=" + stop.lon);

		if (logger.isDebugEnabled()) {
			logger.debug("setGeoCode, lat:" + (double) (stop.lat / 1e5)
					+ ", lon:" + (double) (stop.lon / 1e5));
		}
		if (isValid(city) && isValid(state)) {
			CityState cs = new CityState();
			cs.setCity(city);
			cs.setState(state);
			location.setCityState(cs);

			cliStringBuffer.append("&city=" + city + "&state=" + state);
			if (logger.isDebugEnabled()) {
				logger.debug("setCityState, city:" + city + ", state:" + state);
			}
		}
		if (isValid(zip)) {
			location.setPostalCode(zip);
			cliStringBuffer.append("&postalCode=" + zip);
			if (logger.isDebugEnabled()) {
				logger.debug("setPostalCode:postalCode=" + zip);
			}
		}

		request.setLocation(location);
		cli.addData("location", cliStringBuffer.toString());

		request.setNumberOfDays(numberOfDays);

		if (isCanadianCarrier) {
			request.setSpeedUnit(SpeedUnit.KPH);
		} else {
			request.setSpeedUnit(SpeedUnit.MPH);
		}

		WeatherServiceStub stub = null;
		WeatherServiceResponseDTO response = null;
		try {
			String endpoint = WebServiceConfigurator.getUrlOfWeather();
			stub = new WeatherServiceStub(endpoint);
		} catch (Throwable e) {
			logger.error("can not build up WeatherService, please check your URL_WEATHER value in web_services.properties");
			e.printStackTrace();
			cli.addData(CliConstants.LABEL_ERROR, "ERROR_CANNOT_CONNECTTO_WS");
			cli.setStatus(e);
			cli.complete();
			logger.error(e, e);
			WebServiceUtils.cleanupStub(stub);
			throw new TnException(TnException.ERROR_CANNOT_CONNECTTO_WS, e);
		}

		try {
			response = stub.getCurrentNForecast(request);
		} catch (Throwable e) {
			cli.addData(CliConstants.LABEL_ERROR, "ERROR_WS_UNKNOWN");
			cli.setStatus(e);
			cli.complete();
			logger.error(e, e);
			throw new TnException(TnException.ERROR_WS_UNKNOWN, e);
		}
		finally {
			WebServiceUtils.cleanupStub(stub);
		}

		if (response == null) {
			cli.addData(CliConstants.LABEL_ERROR, "ERROR_WS_NULL_RESPONSE");
			cli.complete();
			logger.error("response is NULL!");
			throw new TnException(TnException.ERROR_WS_NULL_RESPONSE);
		}

		WeatherDetails detailsDTO = response.getWeatherDetails();

		if (logger.isDebugEnabled()) {
			logger.debug("End to getWeatherForLocation, cost "
					+ (System.currentTimeMillis() - start) + " ms.");
		}
		cli.complete();
		return detailsDTO;
	}

	private static boolean isValid(String s) {
		return s != null && s.length() > 0;
	}

	/**
	 * create an WeatherServiceRequestDTO
	 * 
	 * @return
	 */
	private static WeatherServiceRequestDTO createRequest() {
		WeatherServiceRequestDTO request = new WeatherServiceRequestDTO();
		request.setClientName("6x-cserver");
		request.setClientVersion("1.0");
		request.setTransactionId("" + Calendar.getInstance().getTimeInMillis());

		return request;
	}
}
