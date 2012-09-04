package com.telenav.cserver.adjuggler.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.axis2.databinding.types.Day;
import org.apache.axis2.databinding.types.Month;
import org.apache.axis2.databinding.types.Year;
import org.apache.log4j.Logger;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.telenav.cserver.adjuggler.executor.AdJugglerExecutor;
import com.telenav.cserver.adjuggler.executor.AdJugglerRequest;
import com.telenav.cserver.backend.cose.PoiSearchRequest;
import com.telenav.cserver.backend.datatypes.Address;
import com.telenav.cserver.backend.datatypes.TnPoi;
import com.telenav.cserver.backend.datatypes.cose.GasPriceInfo;
import com.telenav.cserver.backend.datatypes.cose.GasPriceTypeDef;
import com.telenav.cserver.billing.BillingException;
import com.telenav.cserver.billing.BillingManagerInterface;
import com.telenav.cserver.billing.holder.BillingManagerHolder;
import com.telenav.cserver.browser.datatype.DeviceConfig;
import com.telenav.cserver.browser.util.DeviceManager;
import com.telenav.cserver.browser.util.MessageHelper;
import com.telenav.cserver.common.resource.ResourceHolderManager;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.poi.struts.Constant;
import com.telenav.cserver.poi.util.PoiDataConverter;
import com.telenav.cserver.util.TnUtil;
import com.telenav.datatypes.content.movie.v10.Area;
import com.telenav.datatypes.content.movie.v10.MovieSearchDate;
import com.telenav.datatypes.content.movie.v10.MovieSortTypeEnum;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.services.content.moviesearchservice.v10.MovieSearchRequestDTO;
import com.telenav.tnbrowser.util.DataHandler;
import com.telenav.ws.datatypes.content.weather.WeatherReport;

public class AdjugglerUtil {
	
	private static Logger logger = Logger.getLogger(AdjugglerUtil.class);
	public static final String USER_ID = "${USER_ID}";
	public static final String CARRIER = "${CARRIER}";
	public static final String DEVICE = "${DEVICE}";
	public static final String VERSION = "${VERSION}";
	public static final String PRODUCT = "${PRODUCT}";
	public static final String PLATFORM = "${PLATFORM}";
	public static final String BUILDNUMBER = "${BUILDNUMBER}";

	// New Sort By string constants. This is what should be defined in adjuggler
	// also
	public static final String MOVIE_SORT_BY_NAME = "NAME";
	public static final String MOVIE_SORT_BY_RANK = "RANK";
	public static final String MOVIE_SORT_BY_RELEASE_DATE = "RELEASE_DATE";
	public static final String MOVIE_SORT_BY_WEEKEND_GROSS = "WEEKEND_GROSS";

	//billing holder
	private static BillingManagerHolder holder = (BillingManagerHolder) ResourceHolderManager
     .getResourceHolder("billing_manager");
	
	public static PoiSearchRequest getPoiSearchRequest(DataHandler handler,
			JSONObject locationJO, long categoryId, int sortType,
			boolean needSponsor) {
		Address anchor = new Address();
		anchor.setFirstLine("");
		try {
			anchor.setLatitude(PoiDataConverter.DM5ToDegree(locationJO
					.getInt("lat")));
			anchor.setLongitude(PoiDataConverter.DM5ToDegree(locationJO
					.getInt("lon")));
			anchor.setCityName("");
			anchor.setState("");
			anchor.setCountry("");
			anchor.setPostalCode("");

			PoiSearchRequest poiReq = new PoiSearchRequest();
			String region = handler.getClientInfo(DataHandler.KEY_REGION);
			String userId = handler.getClientInfo(DataHandler.KEY_USERID);

			poiReq.setRegion(region);
			poiReq.setAnchor(anchor);
			poiReq.setCategoryId(categoryId);
			poiReq.setCategoryVersion("YP50");
			poiReq.setHierarchyId(1);
			poiReq.setNeedUserPreviousRating(true);
			poiReq.setPoiQuery("");
			poiReq.setPoiSortType(sortType);
			poiReq.setRadiusInMeter(Constant.DEFAULT_RADIUS);
			poiReq.setUserId(Long.parseLong(userId));
			poiReq.setPageNumber(0);
			poiReq.setPageSize(1);
			poiReq.setMaxResult(1);
			poiReq.setOnlyMostPopular(false);
			poiReq.setAutoExpandSearchRadius(true);
			poiReq.setNeedUserGeneratePois(true);
			poiReq.setNeedSponsoredPois(needSponsor);

			return poiReq;
		} catch (JSONException e) {
			return null;
		}
	}

	public static String getLowestPrice(TnPoi tnpoi) {
		List<GasPriceInfo> priceList = tnpoi.getGasPriceInfos();
		GasPriceInfo priceInfo = priceList.get(0);
		double cheapestGas = priceInfo.getPrice();

		String cheapestGasStr = "$" + cheapestGas;
		switch (priceInfo.getGasType()) {
		case GasPriceTypeDef.TYPE_BASIC_GRADE:
			cheapestGasStr += " / [87]";
			break;
		case GasPriceTypeDef.TYPE_MID_GRADE:
			cheapestGasStr += " / [89]";
			break;
		case GasPriceTypeDef.TYPE_HIGH_GRADE:
			cheapestGasStr += " / [91]";
			break;
		case GasPriceTypeDef.TYPE_DIESEL_GRADE:
			cheapestGasStr += " / [D]";
			break;
		}
		return cheapestGasStr;
	}

	public static String getAdJugglerUrlPath(DataHandler handler) {
		DeviceConfig config = DeviceManager.getInstance().getDeviceConfig(
				handler);
		String key = "device." + config.getLayoutKeyWithDevice() + "."
				+ "AdJugglerUrl";
		return key;
	}

	// date has format dd-MM-yyyy
	private static GregorianCalendar _cal = new GregorianCalendar();

	public static MovieSearchDate getMovieSearchDate(String date) {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd", new Locale(
				"en", "US"));

		Date d = null;
		try {
			d = f.parse(date);
		} catch (Exception ex) {
		}

		if (d == null)
			d = new Date();

		GregorianCalendar cal = (GregorianCalendar) _cal.clone();
		cal.setTime(d);
		MovieSearchDate result = new MovieSearchDate();
		result.setDay(new Day(cal.get(Calendar.DAY_OF_MONTH)));
		// Month in calendar 0 based (0-January), COSE 1 based (1-January)
		result.setMonth(new Month(cal.get(Calendar.MONTH) + 1));
		result.setYear(new Year(cal.get(Calendar.YEAR)));
		return result;
	}

	public static MovieSearchRequestDTO getMovieSearchRequestDTO(
			DataHandler handler, JSONObject locationJO, JSONObject adJo)
			throws JSONException {
		JSONObject actionADJO = adJo.getJSONObject("action");

		String searchName = "";
		String searchDate = null;
		String sortBy = "";

		if (actionADJO.has("searchName")) {
			searchName = actionADJO.getString("searchName");
		}

		if (actionADJO.has("searchDate")) {
			searchDate = actionADJO.getString("searchDate");
		}

		if (actionADJO.has("sortBy")) {
			sortBy = actionADJO.getString("sortBy");
		}

		MovieSearchDate movieSearchDate = AdjugglerUtil
				.getMovieSearchDate(searchDate);
		MovieSearchRequestDTO sMovie = new MovieSearchRequestDTO();
		sMovie.setKeyword(searchName);
		sMovie.setDate(movieSearchDate);

		// get area based on lat and lon
		int lat = locationJO.getInt("lat");
		int lon = locationJO.getInt("lon");
		Area area = TnUtil.getArea(lat, lon, Constant.SEARCH_RADIUS);

		sMovie.setArea(area);
		sMovie.setPageLength(10);
		sMovie.setPageNumber(1);

		if (sortBy != null && !sortBy.equals("")) {
			if (sortBy.equals(MOVIE_SORT_BY_NAME)) {
				sMovie.setSortType(MovieSortTypeEnum.ALPHABET);
			} else if (sortBy.equals(MOVIE_SORT_BY_RANK)) {
				sMovie.setSortType(MovieSortTypeEnum.RANK);
			} else if (sortBy.equals(MOVIE_SORT_BY_RELEASE_DATE)) {
				sMovie.setSortType(MovieSortTypeEnum.RELEASE_DATE);
			} else if (sortBy.equals(MOVIE_SORT_BY_WEEKEND_GROSS)) {
				sMovie.setSortType(MovieSortTypeEnum.BOX_OFFICE_WEEKEND_GROSS);
			}
		} else {
			sMovie.setSortType(MovieSortTypeEnum.ALPHABET);
		}

		// set tncontext;
		// TODO - tidy up
		TnContext tnContext = new TnContext("poidataset=YPC");
		sMovie.setClientName("cose");
		sMovie.setClientVersion("1.0");
		sMovie.setTransactionId("1");
		sMovie.setContextString(tnContext.toContextString());

		return sMovie;
	}

	public static String getWeatherString(WeatherReport weatherReport,
			WeatherReport weatherReportForcast, String location,
			DataHandler handler) throws Exception {

		if (weatherReport == null || weatherReportForcast == null) {
			throw new Exception("No weather");
		}
		StringBuffer weatherStr = new StringBuffer();
		weatherStr.append(
				TnUtil.formatTemp(weatherReport.getCurrentTemperature()
						.getFarenheit())).append(" ").append(
				TnUtil.getTitleCase(location.toLowerCase())).append(" ")
				.append(
						TnUtil.formatTemp(weatherReportForcast
								.getHighTemperature().getFarenheit())).append(
						" / ").append(
						TnUtil.formatTemp(weatherReportForcast
								.getLowTemperature().getFarenheit())).append(
						" ");

		weatherStr.append(MessageHelper.getInstance().getMessageValue(handler,
				TnUtil.getCondition(weatherReport)));

		return weatherStr.toString();
	}

	public static JSONObject setImageUrlSuffix(JSONObject adJo,
			String imageUrlSuffix) {
		JSONObject adJo_bak = adJo;
		if (adJo.has("backgroundImage")) {
			try {
				JSONObject backgroundImageJo = adJo
						.getJSONObject("backgroundImage");
				String focusUrl = backgroundImageJo.getString("focusUrl")
						+ imageUrlSuffix;
				String unfocusUrl = backgroundImageJo.getString("unfocusUrl")
						+ imageUrlSuffix;
				backgroundImageJo.put("focusUrl", focusUrl);
				backgroundImageJo.put("unfocusUrl", unfocusUrl);
				adJo.put("backgroundImage", backgroundImageJo);
			} catch (JSONException e) {
				return adJo_bak;
			}
		}
		return adJo;
	}

	public static String getAjugglerURLkey(JSONObject actionJO, final UserProfile profile, final TnContext context, 
			final String carrier,  final String productCode, final int serviceLevel) {
		String key = Constant.KEY_STARTUP_PREMIUM;
		try {
			String from = actionJO.getString(Constant.JSON_KEY_FROM);
			String isPremiumAccount = actionJO
					.getString(Constant.JSON_KEY_ISPREMIUMACCOUNT);
			if ((Constant.KEY_ADJUGGLER_FROM_MAIN).equals(from)) {
				if ("0".equals(isPremiumAccount)) {
					key = Constant.KEY_STARTUP_NONPREMIUM;
				}
				if(actionJO.has(Constant.JSON_KEY_ISFREETRIALELIGIBLE)){
					int isFreeTrialEligible = actionJO.getInt(Constant.JSON_KEY_ISFREETRIALELIGIBLE);
					try {
						if (isFreeTrialEligible == 1) {
							key = Constant.KEY_STARTUP_ISFREETRIALELIGIBLE;
						} else if (isFreeTrialEligible(profile, context,
								carrier, productCode, serviceLevel) == 1) {
							key = Constant.KEY_STARTUP_ISFREETRIALELIGIBLE;
						}
					} catch (BillingException e) {
						logger.error("Error: retrieving free trial eligibility for Adjuggler \n" + e.getMessage());
					}
				}
			} else if ((Constant.KEY_ADJUGGLER_FROM_TRAFFIC_INCIDENT)
					.equals(from)) {
				key = Constant.KEY_TRAFFIC_PREMIUM;
			} else {
				key = Constant.KEY_POI_SEARCH_ADJUGGLER;
			}
		} catch (JSONException e) {
		}

		return key;
	}
	
	
	/**
	 * Check if the account is eligible for first 30 day free trial
	 * @param adJugglerRequest
	 * @param context
	 * @return if account is 30 day free trial eligible
	 * @throws BillingException 
	 */
	public static int isFreeTrialEligible(final UserProfile profile, final TnContext context, final String carrier,
			final String productCode, final int serviceLevel) throws BillingException{
		
		int isFreeTrialEligible = 0;
		
		// Call billing.
        BillingManagerInterface billingManager = holder.getBillingManager(
                profile, context);
        
        if (TnUtil.isSprintUser(carrier)) {
			FreeTrialChecker frChecker = new FreeTrialChecker(billingManager);
			isFreeTrialEligible = frChecker.isFreeTrialUpgradable(profile, context, 
					serviceLevel, productCode);
		}
        
        return isFreeTrialEligible;
	}
	
	
	public static String getAjugglerURLkeyDefault(JSONObject actionJO) {
		String key = Constant.KEY_STARTUP_PREMIUM;
		try {
			String from = actionJO.getString(Constant.JSON_KEY_FROM);
			String isPremiumAccount = actionJO
					.getString(Constant.JSON_KEY_ISPREMIUMACCOUNT);
			
			if ((Constant.KEY_ADJUGGLER_FROM_MAIN).equals(from)) {
				if ("0".equals(isPremiumAccount)) {
					key = Constant.KEY_STARTUP_NONPREMIUM;
				}
			} else if ((Constant.KEY_ADJUGGLER_FROM_TRAFFIC_INCIDENT)
					.equals(from)) {
				key = Constant.KEY_TRAFFIC_PREMIUM;
			} else {
				key = Constant.KEY_POI_SEARCH_ADJUGGLER;
			}
		} catch (JSONException e) {
		}

		return key;
	}

	public static String handleAdjugglerURL(String url, DataHandler handler,
			JSONObject actionJO) {
		String keyword = actionJO.optString("keyword");
		String userID = handler.getClientInfo(DataHandler.KEY_USERID);
		String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
		String device = handler.getClientInfo(DataHandler.KEY_DEVICEMODEL);

		StringBuilder urlBuilded = new StringBuilder();
		urlBuilded.append(url);
		urlBuilded.append("&kw=BARVERSION_");
		urlBuilded.append(keyword);
		urlBuilded.append(",USERID_");
		urlBuilded.append(userID);
		urlBuilded.append(",CARRIER_");
		urlBuilded.append(carrier);
		urlBuilded.append(",DEVICE_");
		urlBuilded.append(device);

		return urlBuilded.toString();
	}

	public static JSONObject dealWithExternalURL(JSONObject adJo,
			DataHandler handler) {
		JSONObject adJo_bak = adJo;
		try {
			JSONObject actionJO = adJo.getJSONObject("action");
			String type = actionJO.getString("type");
			if (Constant.KEY_ADJUGGLER_BROWSER.equalsIgnoreCase(type)) {
				String url = actionJO.getString("url");
				Iterator<Entry<String,String>> userInfo = getUserInfoMapping(handler).entrySet().iterator();
				while(userInfo.hasNext())
		        {
					Entry<String, String> entry = userInfo.next();
					if(url.contains(entry.getKey())){
						url = url.replace(entry.getKey(), entry.getValue());
					}
		        }

				actionJO.put("url", url);
				adJo.put("action", actionJO);
			}
			return adJo;
		} catch (JSONException e) {
			return adJo_bak;
		}
	}

	public static Map<String, String> getUserInfoMapping(DataHandler handler) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(USER_ID, handler.getClientInfo(DataHandler.KEY_USERID));
		map.put(CARRIER, handler.getClientInfo(DataHandler.KEY_CARRIER));
		map.put(DEVICE, handler.getClientInfo(DataHandler.KEY_DEVICEMODEL));
		map.put(VERSION, handler.getClientInfo(DataHandler.KEY_VERSION));
		map.put(PRODUCT, handler.getClientInfo(DataHandler.KEY_PRODUCTTYPE));
		map.put(PLATFORM, handler.getClientInfo(DataHandler.KEY_PLATFORM));
		map.put(BUILDNUMBER, handler.getClientInfo(DataHandler.KEY_BUILDNUMBER));

		return map;
	}
}
