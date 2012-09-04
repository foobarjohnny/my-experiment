package com.telenav.cserver.adjuggler.executor;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.PropertyResourceBundle;

import org.apache.log4j.Logger;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.adjuggler.util.AdjugglerDefaultJSON;
import com.telenav.cserver.adjuggler.util.AdjugglerUtil;
import com.telenav.cserver.adjuggler.util.FreeTrialChecker;
import com.telenav.cserver.backend.cose.CoseFactory;
import com.telenav.cserver.backend.cose.PoiSearchProxy;
import com.telenav.cserver.backend.cose.PoiSearchRequest;
import com.telenav.cserver.backend.cose.PoiSearchResponse;
import com.telenav.cserver.backend.datatypes.ErrorCode;
import com.telenav.cserver.backend.datatypes.TnPoi;
import com.telenav.cserver.backend.proxy.BackendProxyManager;
import com.telenav.cserver.backend.proxy.adjuggler.AdjugglerProxy;
import com.telenav.cserver.backend.proxy.adjuggler.AdjugglerRequest;
import com.telenav.cserver.backend.proxy.adjuggler.AdjugglerResponse;
import com.telenav.cserver.browser.util.ClientHelper;
import com.telenav.cserver.browser.util.MessageHelper;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.executor.AbstractExecutor;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.cserver.misc.struts.action.TrafficIncidentsAction;
import com.telenav.cserver.poi.struts.Constant;
import com.telenav.cserver.poi.struts.util.PoiUtil;
import com.telenav.cserver.poi.struts.util.RGCUtil;
import com.telenav.cserver.util.WebServiceConfigurator;
import com.telenav.cserver.weather.executor.WeatherProxy;
import com.telenav.datatypes.content.movie.v10.MovieListingWithDetailTheaterInfo;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.services.content.moviesearchservice.v10.MovieSearchRequestDTO;
import com.telenav.services.content.moviesearchservice.v10.MovieSearchService;
import com.telenav.services.content.moviesearchservice.v10.MovieSearchServiceStub;
import com.telenav.services.content.moviesearchservice.v10.MovieWithDetailTheaterInfoServiceResponseDTO;
import com.telenav.tnbrowser.util.DataHandler;
import com.telenav.ws.datatypes.content.weather.WeatherDetails;
import com.telenav.ws.datatypes.content.weather.WeatherReport;

public class AdJugglerExecutor extends AbstractExecutor {
	public static PropertyResourceBundle serverBundle = null;
	private static Logger logger = Logger.getLogger(AdJugglerExecutor.class);
	
	public static final String GAS_PRICE = "${GAS_PRICE}";
	public static final String NEW_MOVIES = "${NEW_MOVIES}";
	public static final String POPULAR_MOVIES_THIS_WEEK = "${POPULAR_MOVIES_THIS_WEEK}";
	public static final String TRAFFIC_DATA = "${TRAFFIC_DATA}";
	public static final String WEATHER = "${WEATHER}";

	@Override
	public final void doExecute(final ExecutorRequest req,
			final ExecutorResponse resp, final ExecutorContext context)
			throws ExecutorException {
		CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
		cli.setFunctionName("AdJugglerExecutor");

		AdJugglerRequest adJugglerRequest = (AdJugglerRequest) req;
		AdJugglerResponse adJugglerResponse = (AdJugglerResponse) resp;
		try {
			DataHandler handler = adJugglerRequest.getHandler();
			String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
			
			serverBundle = (PropertyResourceBundle) PropertyResourceBundle
					.getBundle(AdjugglerUtil.getAdJugglerUrlPath(handler));

			JSONObject actionJO = adJugglerRequest.getActionJO();
			
			String productCode = "";
			if(actionJO != null && actionJO.has(Constant.JSON_KEY_PRODUCTCODE)){
				productCode = actionJO.getString(Constant.JSON_KEY_PRODUCTCODE);
			}
			
			int serviceLevel = -1;
			if(actionJO != null && actionJO.has(Constant.JSON_KEY_SERVICELEVEL)){
				serviceLevel = actionJO.getInt(Constant.JSON_KEY_SERVICELEVEL);
			}
			
			JSONObject locationJO = actionJO
					.getJSONObject(Constant.JSON_KEY_LOCATION);
			String from = actionJO.getString(Constant.JSON_KEY_FROM);
			String isPremiumAccount = actionJO
					.getString(Constant.JSON_KEY_ISPREMIUMACCOUNT);
			String key = AdjugglerUtil.getAjugglerURLkey(actionJO, adJugglerRequest.getUserProfile(), context.getTnContext()
					, carrier, productCode, serviceLevel);
			String url = getAjugglerURL(actionJO, handler, key);

			cli.addData("parameters", "from=" + from + "&isPremiumAccount="
					+ isPremiumAccount + "&locationJO" + locationJO.toString()
					+ "&url=" + url);
			logger.info("adjuggler url=" + url); // TODO - may be change to
			// MisLog later on
			try {
				AdjugglerRequest adjugglerRequest = new AdjugglerRequest();
				adjugglerRequest.setUrl(url);
				AdjugglerProxy adjugglerProxy = BackendProxyManager
						.getBackendProxyFactory().getBackendProxy(
								AdjugglerProxy.class);
				TnContext tc = context.getTnContext();
				AdjugglerResponse adjugglerResponse = adjugglerProxy
						.getAdjugglerInfo(adjugglerRequest, tc);
				String os = "";
				if (adjugglerResponse.isSuccess()) {
					String outPutString = adjugglerResponse.getJsonStr();
					JSONObject adJo = new JSONObject(outPutString);
					//set 30 day free trial flag.
					if(Constant.KEY_STARTUP_ISFREETRIALELIGIBLE.equals(key)){
						adJo.put(Constant.JSON_KEY_ISFREETRIALELIGIBLE, "1");
					}
					logger.info("adJo from adjuggler=" + outPutString);
					this.searchGasPrice(adJo, handler, locationJO);
					adJo = this
							.getMovies(adJo, handler, locationJO, NEW_MOVIES);
					adJo = this.getMovies(adJo, handler, locationJO,
							POPULAR_MOVIES_THIS_WEEK);
					TrafficIncidentsAction.getTrafficIncidents(adJo, handler,
							locationJO, TRAFFIC_DATA);

					this.getWeather(adJo, handler, locationJO);
					AdjugglerUtil.dealWithExternalURL(adJo, handler);

					logger.info("modified adJo=" + adJo);

					String imageUrlSuffix = serverBundle
							.getString("image.url.suffix");
					if (imageUrlSuffix == null) {
						imageUrlSuffix = "";
						logger
								.error("adjuggler: imageUrlSuffix should not be null");
					}
					AdjugglerUtil.setImageUrlSuffix(adJo, imageUrlSuffix);

					adJo.put("pageName", from);
					os = adJo.toString();
				} else {
					logger.info("Call adjuggler error, statusCode: "
							+ adjugglerResponse.getStatusCode());
					os = AdjugglerDefaultJSON.getDefaultJSON(from,
							isPremiumAccount, handler);
				}
				cli.addData("results", os);
				cli.complete();

				adJugglerResponse.setResult(os);
			} catch (Exception e) {
				System.err.println("Fatal protocol violation: "
						+ e.getMessage());
				logger.fatal("adjuggler Fatal protocol violation: ", e);

				String result = AdjugglerDefaultJSON.getDefaultJSON(from,
						isPremiumAccount, handler);

				cli.addData("results", result);
				cli.complete();
				adJugglerResponse.setResult(result);
			}
		} catch (JSONException e) {
			logger.fatal("adjuggler jsonexception: ", e);
		}
	}

	
	public String getAjugglerURL(final JSONObject actionJO, final DataHandler handler, String key){
		if(!serverBundle.containsKey(key))
		{
			key=AdjugglerUtil.getAjugglerURLkeyDefault(actionJO);
		}
		String url = serverBundle.getString(key);
		
		return AdjugglerUtil.handleAdjugglerURL(url, handler, actionJO);
	}

	public List<TnPoi> searchPoiList(PoiSearchRequest poiSearchRequest,
			DataHandler handler) {
		if (poiSearchRequest != null) {
			TnContext tnContext = PoiUtil.getTnContext(handler);
			PoiSearchProxy poiSearchProxy = CoseFactory
					.createPoiSearchProxy(tnContext);
			try {
				PoiSearchResponse result = poiSearchProxy
						.searchWithinDistance(poiSearchRequest);
				if (result != null
						&& result.getPoiSearchStatus() == ErrorCode.POI_STATUS_SUCCESS) {
					List<TnPoi> pois = result.getPois();
					return pois;
				}
			} catch (ThrottlingException e) {
				logger.fatal("searchPoiList: ", e);
				return null;
			}
		}
		return null;
	}

	public JSONObject searchGasPrice(JSONObject adJo, DataHandler handler,
			JSONObject locationJO) {
		JSONObject adJo_0 = adJo;
		try {
			JSONObject textADJO = adJo.getJSONObject("text");
			String enUS = textADJO.getString("en_US");
			String esMX = textADJO.getString("es_MX");
			if (enUS.contains(GAS_PRICE) || esMX.contains(GAS_PRICE)) {
				JSONObject actionADJO = adJo.getJSONObject("action");
				long categoryId = 50500L;
				if (actionADJO.has("searchCategory")) {
					categoryId = Long.parseLong(actionADJO
							.getString("searchCategory"));
				}
				PoiSearchRequest poiSearchRequest = AdjugglerUtil
						.getPoiSearchRequest(handler, locationJO, categoryId,
								Constant.SORT_BY_GASPRICE, false);
				List<TnPoi> tnpois = searchPoiList(poiSearchRequest, handler);
				if (tnpois != null && tnpois.size() > 0) {
					TnPoi tnpoi = tnpois.get(0);
					String lowestPrice = AdjugglerUtil.getLowestPrice(tnpoi);
					enUS = enUS.replace("${GAS_PRICE}", lowestPrice);
					esMX = esMX.replace("${GAS_PRICE}", lowestPrice);

					textADJO.put("en_US", enUS);
					textADJO.put("es_MX", esMX);
				} else {
					textADJO = adJo.getJSONObject("defaultText");
				}
				adJo.put("text", textADJO);
			}

			return adJo;
		} catch (JSONException e) {
			return adJo_0;
		}
	}

	// replacementToken could be ${POPULAR_MOVIES_THIS_WEEK} or ${NEW_MOVIES}.
	// Replace it with the movie name list
	public JSONObject getMovies(JSONObject adJo, DataHandler handler,
			JSONObject locationJO, String replacementToken) {
		JSONObject adJo_0 = adJo;
		try {
			JSONObject textADJO = adJo.getJSONObject("text");
			String enUS = textADJO.getString("en_US");
			String esMX = textADJO.getString("es_MX");
			if (enUS.contains(replacementToken)
					|| esMX.contains(replacementToken)) {
				MovieListingWithDetailTheaterInfo[] mList = searchMovies(
						handler, locationJO, adJo);

				String movieStr = "";
				if (mList != null) {
					for (int i = 0; i < mList.length && i < 3; i++) {
						// append , after every movie
						if (i != 0) {
							movieStr += ", ";
						}
						movieStr += mList[i].getMovie().getName();
					}
				}

				if (!movieStr.equals("")) {
					enUS = enUS.replace(replacementToken, movieStr);
					esMX = esMX.replace(replacementToken, movieStr);

					textADJO.put("en_US", enUS);
					textADJO.put("es_MX", esMX);
				} else {
					textADJO = adJo.getJSONObject("defaultText");
				}

				adJo.put("text", textADJO);
			}

			return adJo;
		} catch (JSONException e) {
			return adJo_0;
		}
	}

	private MovieListingWithDetailTheaterInfo[] searchMovies(
			DataHandler handler, JSONObject locationJO, JSONObject adJo) {
		try {
			MovieSearchRequestDTO sMovie = AdjugglerUtil
					.getMovieSearchRequestDTO(handler, locationJO, adJo);

			MovieSearchService service = new MovieSearchServiceStub(
					WebServiceConfigurator.getUrlOfMovieSearch());
			MovieWithDetailTheaterInfoServiceResponseDTO mResp = service
					.searchMoviesWithDetailTheaterInfo(sMovie);
			MovieListingWithDetailTheaterInfo[] mList = mResp.getMovieListing();
			return mList;
		} catch (Exception ex) {
			return null;
		}
	}

	public JSONObject getWeather(JSONObject adJo, DataHandler handler,
			JSONObject locationJO) {
		JSONObject adJo_bak = adJo;
		try {
			JSONObject textADJO = adJo.getJSONObject("text");
			String enUS = textADJO.getString("en_US");
			String esMX = textADJO.getString("es_MX");
			try {
				if (enUS.contains(WEATHER) || esMX.contains(WEATHER)) {

					RGCUtil rgcutil = new RGCUtil();
					Stop stop = rgcutil.getCurrentLocation(locationJO
							.getInt("lat"), locationJO.getInt("lon"),
							new TnContext());
					WeatherDetails weatherDetails = WeatherProxy.getInstance()
							.getWeatherForLocation(stop, 1, false);

					WeatherReport weatherReport = weatherDetails
							.getCurrentWeather();
					WeatherReport[] weatherReportForcast = weatherDetails
							.getForecast();

					String locationStr = MessageHelper.getInstance()
							.getMessageValue(handler, "common.currentLocation");
					if (stop != null) {
						locationStr = stop.city;
					}
					String weatherStr = AdjugglerUtil.getWeatherString(
							weatherReport, weatherReportForcast[0],
							locationStr, handler);

					enUS = enUS.replace(WEATHER, weatherStr);
					esMX = esMX.replace(WEATHER, weatherStr);

					String image = "weatherSmall_" + weatherReport.getPicCode()
							+ ".png";
					String weatherImageUrl = ClientHelper.getImageUrl(handler)
							+ image;

					try {
						URL url = new URL(weatherImageUrl);
						URLConnection connection = url.openConnection();
						connection.getInputStream();
					} catch (IOException e) {
						weatherImageUrl = ClientHelper
								.getCommonImageUrl(handler)
								+ image;
					}
					adJo.put("iconImage", weatherImageUrl);

					textADJO.put("en_US", enUS);
					textADJO.put("es_MX", esMX);
				}
			} catch (Exception e) {
				logger.fatal("getWeather: ", e);
				textADJO = adJo.getJSONObject("defaultText");
			}

			adJo.put("text", textADJO);
			return adJo;
		} catch (JSONException e) {
			return adJo_bak;
		}
	}
}
