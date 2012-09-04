/*
 * Copyright (c) 2009 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 */
package com.telenav.cserver.onebox.executor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.backend.cose.PoiSearchConverter;
import com.telenav.cserver.backend.datatypes.TnPoi;
import com.telenav.cserver.backend.util.WebServiceUtils;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.executor.AbstractExecutor;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.reporting.ReportType;
import com.telenav.cserver.framework.reporting.ReportingRequest;
import com.telenav.cserver.framework.reporting.ReportingUtil;
import com.telenav.cserver.framework.reporting.impl.ServerMISReportor;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.cserver.poi.datatypes.BasePoi;
import com.telenav.cserver.poi.datatypes.POI;
import com.telenav.cserver.poi.datatypes.Stop;
import com.telenav.cserver.poi.executor.POISearchResponse_WS;
import com.telenav.cserver.poi.struts.Constant;
import com.telenav.cserver.poi.struts.util.NSPoiUtil;
import com.telenav.cserver.poi.struts.util.PoiUtil;
import com.telenav.cserver.poi.util.PoiDataConverter;
import com.telenav.cserver.resource.manager.WebPageMappingManager;
import com.telenav.cserver.util.CommonUtil;
import com.telenav.cserver.util.TnConstants;
import com.telenav.cserver.util.TnUtil;
import com.telenav.cserver.util.WebServiceConfigurator;
import com.telenav.datatypes.services.v20.ResponseStatus;
import com.telenav.datatypes.services.v20.UserInformation;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.services.search.onebox.v10.AddressResultEntry;
import com.telenav.services.search.onebox.v10.ClientNameEnum;
import com.telenav.services.search.onebox.v10.OneboxSearchRequest;
import com.telenav.services.search.onebox.v10.OneboxSearchResponse;
import com.telenav.services.search.onebox.v10.OneboxSearchServiceStub;
import com.telenav.services.search.onebox.v10.PoiResultEntry;
import com.telenav.services.search.onebox.v10.QuerySuggestion;
import com.telenav.services.search.onebox.v10.ResultEntry;
import com.telenav.ws.datatypes.address.Address;
import com.telenav.ws.datatypes.address.GeoCode;

/**
 * @author weiw
 */

public class OneBoxExecutor extends AbstractExecutor {

	public static final int SEARCH_ALONGROUTE = 7;
	/** End point for one box search server URL */
	public static final String END_POINT = WebServiceConfigurator
			.getUrlOfOneBox();

	private static Logger logger = Logger.getLogger(OneBoxExecutor.class);

	public final void doExecute(final ExecutorRequest req,
			final ExecutorResponse resp, final ExecutorContext context)
			throws ExecutorException {
		CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
		cli.setFunctionName("OneBoxExecutor");
		// Get the request and response
		OneBoxRequest oneBoxReq = (OneBoxRequest) req;
		OneBoxResponse oneBoxResponse = (OneBoxResponse) resp;
		POISearchResponse_WS poiResponse = new POISearchResponse_WS();

		TnContext tc = context.getTnContext();
		UserProfile userProfile = req.getUserProfile();
		setContext(tc, userProfile);
		ReportingRequest misLog = misReport(tc, userProfile);
		String poiDataSet = tc.getProperty(TnContext.PROP_POI_DATASET);
        if(poiDataSet == null)
        {
        	poiDataSet = "";
        }
		misLog.addServerMisLogField(ServerMISReportor.SERVLET_NAME, ServerMISReportor.POI_SERVLET_NAME);
        misLog.addServerMisLogField(ServerMISReportor.ACTION_ID, this.getClass().getSimpleName());
        misLog.addServerMisLogField(ServerMISReportor.LOGTYPE_ID, ServerMISReportor.ONE_BOX_LOGTYPE);
        misLog.addServerMisLogField(ServerMISReportor.CUSTOM15, poiDataSet);
        misLog.addServerMisLogField(ServerMISReportor.CUSTOM16, tc.getProperty(TnContext.PROP_MAP_DATASET));
		
		boolean needReviews = TnUtil.getPoiReivewFlag(userProfile);
		Stop stop = oneBoxReq.getStop();
		int pageNumber = oneBoxReq.getPageNumber();
		int maxResults = oneBoxReq.getMaxResults();
		//if search From Type is 2 or 4, it's typed from dsr.
		int searchFromType = oneBoxReq.getSearchFromType();
		
		String strSearch = oneBoxReq.getSearchString();

		OneboxSearchRequest obReq = new OneboxSearchRequest();

		// set user
		setUserInfo(req, obReq);
		// set client info
		setClientInfo(obReq);
		// set POI data set() and MAP data set
		obReq.setContextString(tc.toContextString());

		// set anchor
		setCurrentLoc(stop, obReq , oneBoxReq ,tc);

		// set query
		obReq.setQuery(strSearch);
		obReq.setStartIndex(pageNumber * maxResults);
		obReq.setResultCount(maxResults);
		//TODO: check sponsor number
		obReq.setSponsorStartIndex(pageNumber);
		obReq.setSponsorResultCount(1);
		obReq.setLocale(CommonUtil.getTnLocal(userProfile.getLocale()));
		obReq.setSponsorResultCount(oneBoxReq.getSponsorListingNumber());
		if(oneBoxReq.getTransactionId()!=null)
			obReq.setTransactionId(oneBoxReq.getTransactionId());
		 if(logger.isDebugEnabled())
        {
            logger.debug("OneBoxExecutor: &sponsorListingNumber >>>>>" + oneBoxReq.getSponsorListingNumber());
        }
		
		cli.addData("oneboxrequest", "StartIndex=" + obReq.getStartIndex() + "&Query="+obReq.getQuery()+"&ResultCount=" + obReq.getResultCount()
					+ "&SponsorStartIndex=" + obReq.getSponsorStartIndex() + "&Locale:" + obReq.getLocale() + "&Lat:" + obReq.getAnchor().getLatitude()
					+ "&Lon:" + obReq.getAnchor().getLongitude() + " & searchType:" + oneBoxReq.getSearchType() + " & searchAlongType:" + oneBoxReq.getSearchAlongType()
					+ "&sponsorListingNumber" + oneBoxReq.getSponsorListingNumber()+"&transactionId="+oneBoxReq.getTransactionId());
		
		
//      
		misLog.addServerMisLogField(ServerMISReportor.CUSTOM01, "-1");
        misLog.addServerMisLogField(ServerMISReportor.CUSTOM02, strSearch);
        misLog.addServerMisLogField(ServerMISReportor.CUSTOM03, String.valueOf(obReq.getAnchor().getLatitude()));
        misLog.addServerMisLogField(ServerMISReportor.CUSTOM04, String.valueOf(obReq.getAnchor().getLongitude()));
        misLog.addServerMisLogField(ServerMISReportor.CUSTOM05, String.valueOf(pageNumber));
        misLog.addServerMisLogField(ServerMISReportor.CUSTOM06, "Typed-in"); //because one box search always type in
        
        if(isCurrentLocation(stop))
        {
        	misLog.addServerMisLogField(ServerMISReportor.CUSTOM09, String.valueOf(obReq.getAnchor().getLatitude()));
            misLog.addServerMisLogField(ServerMISReportor.CUSTOM10, String.valueOf(obReq.getAnchor().getLongitude()));
        }
        
		OneboxSearchServiceStub stub = null;
		try {
			stub = new OneboxSearchServiceStub(END_POINT);
			OneboxSearchResponse response = stub.oneboxSearch(obReq);
			
			QuerySuggestion[] suggestions = response.getSuggestions();
			ResponseStatus responseStatus = response.getResponseStatus();
			
			String responseMsg = "ResultCount=" + response.getResultCount() + "&StatusCode" + responseStatus.getStatusCode() +
			"&StatusMessage" + responseStatus.getStatusMessage();
			cli.addData("oneboxresponse", responseMsg);
			if( logger.isDebugEnabled() )
			{
				logger.debug("OneBoxResponse: "+responseMsg);
			}
			
			
			ResultEntry[] results = response.getResults();
			misLog.addServerMisLogField(ServerMISReportor.CUSTOM07, String.valueOf(results==null?0:results.length));
			misLog.addServerMisLogField(ServerMISReportor.COMPLETED_FLAG, ServerMISReportor.COMPLETE_SUCCEED);
			misLog.addServerMisLogField(ServerMISReportor.CUSTOM08, String.valueOf(oneBoxReq.getTransactionId()));
			
			boolean hasSuggest = false, hasAddress = false, hasPoi = false;
			if (hasMutiMatch(suggestions)) {
				cli.addData("result type", "go to mutiple match screen");
				setSuggestions(oneBoxResponse, suggestions);
				hasSuggest = true;
			} else {
				if (results != null) {
					if( results.length > 1 || results.length == 0)
						oneBoxResponse.setExactMatchStatus(OneBoxResponse.STATUS_NOT_EXACT_MATCH);
					switch (SubResponseType.toType(responseStatus.getStatusMessage())) {
					case ADDRESS_MULTI:
					case ADDRESS_EXACTLY:
					case ADDRESS_LATLON:
						cli.addData("result type", "go to address resut");
						setAddressList(oneBoxResponse, results);
						hasAddress = true;
						break;
					case ADDRESS_MODIFIED:
						cli.addData("result type", "modified");
						oneBoxResponse.setExactMatchStatus(OneBoxResponse.STATUS_NOT_EXACT_MATCH);
						setAddressList(oneBoxResponse, results);
						hasAddress = true;
						break;
					case POI_BY_DEFAULT_LOC:
					case POI_BY_USER_INPUT:
					case POI_LANDMARK:
					case POI_AIRPORT:
						cli.addData("result type", "go to poi resut");	
						setPoi(oneBoxReq, oneBoxResponse, poiResponse, tc,
								maxResults, needReviews, response, results);
						hasPoi = true;
						break;
					
					default:
						cli.addData("result type", "unexpected");	
						break;
					}

				}
			}
			
			StringBuilder searchType= new StringBuilder();
			if( hasSuggest ) 
				searchType.append("DYM;");
			if( hasAddress )
				searchType.append("ADDR;");
			if( hasPoi )
			{
				searchType.append("POI;");
				oneBoxResponse.setPoiDetailUrl(WebPageMappingManager.getPoiDetailUrl(userProfile, tc));
			}
			//delete the last ';' if length > 0
			if( searchType.length() > 0 )
				searchType.deleteCharAt(searchType.length()-1);
			misLog.addServerMisLogField(ServerMISReportor.CUSTOM00, searchType.toString());

		} catch (Exception e) {
			logger.error(e, e);
			oneBoxResponse.setExactMatchStatus(OneBoxResponse.STATUS_NOT_EXACT_MATCH);
		} finally {
			cli.complete();
			ReportingUtil.report(misLog);
			WebServiceUtils.cleanupStub(stub);
		}
	}

	/**
	 * Set tn context from DSM rules
	 * 
	 * @param tc
	 * @param userProfile
	 */
	private void setContext(final TnContext tc, final UserProfile userProfile) {
		TnUtil.getDSMDataFromCServer(tc, userProfile);
		String poiDataSet = tc.getProperty(TnContext.PROP_POI_DATASET);
		if (poiDataSet == null) {
			poiDataSet = "";
		}
	}

	/**
	 * @param oneBoxResponse
	 * @param suggestions
	 */
	private void setSuggestions(final OneBoxResponse oneBoxResponse,
			final QuerySuggestion[] suggestions) {
		oneBoxResponse.setResultType(Constant.OneBox.DID_YOU_MEAN);
		oneBoxResponse.setSuggestions(suggestions);
	}

	/**
	 * @param suggestions
	 * @return
	 */
	private boolean hasMutiMatch(final QuerySuggestion[] suggestions) {
		return (suggestions != null && suggestions.length > 1);
	}

	/**
	 * @param stop
	 * @param obReq
	 */
	private void setCurrentLoc(final Stop stop, final OneboxSearchRequest obReq, OneBoxRequest oneBoxReq, TnContext tc) {
		GeoCode gcode = new GeoCode();
		int searchType = oneBoxReq.getSearchType();
		if(SEARCH_ALONGROUTE == searchType)
		{
			int searchAlongType = oneBoxReq.getSearchAlongType();
            if(searchAlongType == 0){
                try {
					List<com.telenav.cserver.backend.datatypes.GeoCode> routePoints = NSPoiUtil.getRoutePointsForOneBox(tc, 
							oneBoxReq.getRouteID(), 
							oneBoxReq.getSegmentId(), 
							oneBoxReq.getEdgeId(), 
							oneBoxReq.getShapePointId(), 
							oneBoxReq.getRange(), 
							oneBoxReq.getCurrentLat(), 
							oneBoxReq.getCurrentLon());
					
					com.telenav.cserver.backend.datatypes.GeoCode lastPoint = routePoints.get(routePoints.size()-1);
					gcode.setLatitude(lastPoint.getLatitude());
	    			gcode.setLongitude(lastPoint.getLongitude());
					
				} catch (ThrottlingException e) {
					// TODO Auto-generated catch block
					gcode.setLatitude(PoiDataConverter.DM5ToDegree(stop.lat));
					gcode.setLongitude(PoiDataConverter.DM5ToDegree(stop.lon));
					
					logger.error("error occured when set current location, lat:"+stop.lat+",lon:"+stop.lon);
					e.printStackTrace();
				}
                
            }else{
            	gcode.setLatitude(PoiDataConverter.DM5ToDegree(oneBoxReq.getStopDest().lat));
    			gcode.setLongitude(PoiDataConverter.DM5ToDegree(oneBoxReq.getStopDest().lon));
            }
		}
		else
		{
			gcode.setLatitude(PoiDataConverter.DM5ToDegree(stop.lat));
			gcode.setLongitude(PoiDataConverter.DM5ToDegree(stop.lon));
		}
		obReq.setAnchor(gcode);
	}

	/**
	 * @param tc
	 * @param userProfile
	 * @return
	 */
	private ReportingRequest misReport(final TnContext tc,
			final UserProfile userProfile) {
		ReportingRequest misLog = new ReportingRequest(
				ReportType.SERVER_MIS_LOG_REPORT, userProfile, tc);

		misLog.addServerMisLogField(ServerMISReportor.SERVLET_NAME,
				ServerMISReportor.POI_SERVLET_NAME);
		misLog.addServerMisLogField(ServerMISReportor.ACTION_ID, this
				.getClass().getSimpleName());
		misLog.addServerMisLogField(ServerMISReportor.LOGTYPE_ID,
				ServerMISReportor.ONE_BOX_LOGTYPE);
		misLog.addServerMisLogField(ServerMISReportor.CUSTOM15, tc
				.getProperty(TnContext.PROP_POI_DATASET));
		misLog.addServerMisLogField(ServerMISReportor.CUSTOM16, tc
				.getProperty(TnContext.PROP_MAP_DATASET));
		return misLog;
	}

	/**
	 * @param oneBoxResponse
	 * @param results
	 */
	private void setAddressList(final OneBoxResponse oneBoxResponse,
			final ResultEntry[] results) {
		List<Address> addressList = new ArrayList<Address>();
		for (ResultEntry res : results) {
			AddressResultEntry addressEntry = (AddressResultEntry) res;
			addressList.add(addressEntry.getAddress());
		}
		oneBoxResponse.setAddressList(addressList);
		oneBoxResponse.setResultType(Constant.OneBox.ADDRESS_RESULT);
	}

	/**
	 * @param poiRequest
	 * @param oneBoxResponse
	 * @param poiResponse
	 * @param tc
	 * @param maxResults
	 * @param needReviews
	 * @param setDistance
	 * @param response
	 * @param results
	 */
	private void setPoi(final OneBoxRequest poiRequest,
			final OneBoxResponse oneBoxResponse,
			final POISearchResponse_WS poiResponse, final TnContext tc,
			final int maxResults, final boolean needReviews,
			final OneboxSearchResponse response, final ResultEntry[] results) {

		List<TnPoi> pois = convertToTnPois(results);
		List<TnPoi> sponsorTnpois = convertToTnPois(response.getSponsors());
		
        //because the distance is from the poi to the route
        //need convert to the distance which from poi to the current position
        int searchType = poiRequest.getSearchType();
        if(SEARCH_ALONGROUTE == searchType)
        {
            calculateDistance(pois, poiRequest.getCurrentLat(), poiRequest.getCurrentLon());
            calculateDistance(sponsorTnpois, poiRequest.getCurrentLat(), poiRequest.getCurrentLon());
        }
        
        
		if(logger.isDebugEnabled())
		{
			for(int i = 0; i < pois.size(); i++)
			{
				logger.debug("poi["+i+"] : "+ pois.get(i).toString());
			}
			logger.debug("SponsorPoiResults sPoi size = " + sponsorTnpois.size());
			for(int i = 0; i < sponsorTnpois.size(); i++)
			{
				logger.debug("sponsor poi["+i+"] : "+  sponsorTnpois.get(i).toString());
			}
		}

		List<POI> sponsorList = PoiUtil.getSponsorPoiResults(sponsorTnpois,
				true, poiRequest.getDistanceUnit(), tc, needReviews);
		poiResponse.setSponsorPoiList(sponsorList);
	
		//For TN70, Sponsor poi should still return full content
		/*
		List<BasePoi> baseSponsorList = PoiUtil.getBaseSponsorPoiResults(sponsorTnpois,
				true, poiRequest.getDistanceUnit(), tc, needReviews);
		poiResponse.setBaseSponsorPoiList(baseSponsorList);
		*/

		List<POI> poiList = PoiUtil
				.getPoiResults(pois, sponsorTnpois.size(), maxResults, true,
						poiRequest.getDistanceUnit(), tc, needReviews);
		poiResponse.setPoiList(poiList);
		
		List<BasePoi> basePoiList = PoiUtil.getBasePoiResults(pois, sponsorTnpois.size(), maxResults, true,
						poiRequest.getDistanceUnit(), tc, needReviews);
		poiResponse.setBasePoiList(basePoiList);
		
		poiResponse.setTotalCount(response.getTotalResults());
        Long totalCount = poiResponse.getTotalCount();
        int pageSize = Constant.PAGE_SIZE;
        int totalMaxPageIndex = (int) (totalCount / pageSize);
        if (totalCount % pageSize == 0) {
            totalMaxPageIndex = totalMaxPageIndex - 1;
        }
        poiResponse.setTotalMaxPageIndex(totalMaxPageIndex);
		poiResponse.setDistanceUnit(poiRequest.getDistanceUnit());
		poiResponse.setSponsorListingNumber(poiRequest.getSponsorListingNumber());
		oneBoxResponse.setPoiResp(poiResponse);
		oneBoxResponse.setResultType(Constant.OneBox.POI_RESULT);
	}

	private void calculateDistance(List<TnPoi> pois, double currentLat, double currentLon)
    {
        if (pois == null)
            return;
        
        if (currentLat == 0 || currentLon == 0)
            return;
        
        double[] currPos = new double[] {currentLat/1e5, currentLon/1e5};
        for(TnPoi poi: pois)
        {
            try
            {
                double[] poiPos = new double[] {poi.getAddress().getLatitude(), poi.getAddress().getLongitude()};
                
                poi.setDistanceToUserInMeter(caculateDistance(poiPos, currPos) );
            }
            catch(NullPointerException e)
            {
                logger.fatal("calculateDistance", e);
            }
        }
    }
    
    
    private static double caculateDistance(double[] p1, double[] p2)
    {
        double lat1 = p1[0];
        double lon1 = p1[1];
        double lat2 = p2[0];
        double lon2 = p2[1];
        return 100000 * Math.sqrt((lat1 - lat2) * (lat1 - lat2) + (lon1 - lon2) * (lon1 - lon2));
    }

    /**
	 * @param results
	 * @return
	 */
	private List<TnPoi> convertToTnPois(final ResultEntry[] results) {
		List<TnPoi> pois = new ArrayList<TnPoi>();
		if (results != null) {
			for (ResultEntry res : results) {
				PoiResultEntry poi = (PoiResultEntry) res;
				com.telenav.datatypes.content.tnpoi.v10.TnPoi backendPoi = poi
						.getPoi();
				if(logger.isDebugEnabled())
				{
					com.telenav.ws.datatypes.common.Property[] properties = backendPoi.getProperties();
					if(properties != null && properties.length > 0) 
					{
						StringBuffer flagBuffer = new StringBuffer();
						for(com.telenav.ws.datatypes.common.Property property : properties)
						{
							flagBuffer.append("key:" + property.getKey() + "/value:" + property.getValue() + "/");
						}
						logger.debug("OneBox Search extra flag : " + flagBuffer.toString());
					}
				}
				TnPoi tnpoi = PoiSearchConverter
						.convertWSPoiToTnPoi(backendPoi);
				pois.add(tnpoi);
			}
		}
		return pois;
	}

	private enum SubResponseType {
		ADDRESS_MULTI, ADDRESS_EXACTLY, ADDRESS_LATLON, POI_BY_DEFAULT_LOC, POI_BY_USER_INPUT, POI_LANDMARK, POI_AIRPORT, SUGGESTION, NO_VALUE, ADDRESS_MODIFIED;

		public static SubResponseType toType(final String str) {
			try {
				return valueOf(str);
			} catch (Exception ex) {
				return NO_VALUE;
			}

		}
	}

	/**
	 * @param req
	 * @param obReq
	 */
	private void setUserInfo(final ExecutorRequest req,
			final OneboxSearchRequest obReq) {
		UserInformation user = getUserInfo(req);
		obReq.setUserInfo(user);
	}

	/**
	 * @param obReq
	 */
	private void setClientInfo(final OneboxSearchRequest obReq) {
		Date today = new Date();
		//using date only
		String trxnId = String.valueOf(today.getTime());
		obReq.setClientName(ClientNameEnum._MOBILE);
		obReq.setClientVersion("1");
		obReq.setTransactionId(trxnId);
	}

	/**
	 * @param req
	 * @return
	 */
	private UserInformation getUserInfo(final ExecutorRequest req) {
		UserInformation user = new UserInformation();
		UserProfile usrProfile = req.getUserProfile();
		user.setUserId(usrProfile.getUserId());
		user.setPtn(usrProfile.getMin());
		return user;
	}
	
	private static boolean isCurrentLocation(Stop stop)
    {
        boolean isCurrentLocation = false;
        
        if("6".equals(stop.getType()))
        {
        	isCurrentLocation = true;
        }
        
        return isCurrentLocation;
    }
}
