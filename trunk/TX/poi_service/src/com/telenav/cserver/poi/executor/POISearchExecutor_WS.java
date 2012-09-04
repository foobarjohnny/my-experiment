/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.poi.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;

import com.telenav.audio.client.ResourceServiceClient;
import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.backend.cose.PoiSearchRequest;
import com.telenav.cserver.backend.cose.PoiSearchResponse;
import com.telenav.cserver.backend.datatypes.Address;
import com.telenav.cserver.backend.datatypes.ErrorCode;
import com.telenav.cserver.backend.datatypes.TnPoi;
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
import com.telenav.cserver.poi.datatypes.BasePoi;
import com.telenav.cserver.poi.datatypes.BizPOI;
import com.telenav.cserver.poi.datatypes.POI;
import com.telenav.cserver.poi.datatypes.PoiConstants;
import com.telenav.cserver.poi.datatypes.Stop;
import com.telenav.cserver.poi.struts.Constant;
import com.telenav.cserver.poi.struts.util.PoiUtil;
import com.telenav.cserver.poi.util.PoiDataConverter;
import com.telenav.cserver.resource.manager.WebPageMappingManager;
import com.telenav.cserver.util.CommonUtil;
import com.telenav.cserver.util.TnConstants;
import com.telenav.cserver.util.TnUtil;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.resource.ResourceConstants;
import com.telenav.resource.data.NameInfo;
import com.telenav.resource.data.POIResultInfo;
import com.telenav.resource.data.PromptItem;
import com.telenav.resource.protocol.POIAudioRequest;
import com.telenav.resource.protocol.POIAudioResponse;

/**
 * POISearchExecutor_WS.java
 *
 * @author bhu@telenav.cn
 * @version 1.0 2009-7-17
 */
public class POISearchExecutor_WS extends AbstractExecutor implements PoiConstants
{
    //Define max sponsored pois returned to client
    public final static int MAX_SPONSORED_NUM = 2;	
	
    public final static int MAX_AUDIO_NUMBER = 9;
    public final static int INCLUDE_AUDIO_NUMBER = 3;
	private static int SEARCH_REDIUS = 7000;
	
	//poi type
	public static final int POI_TYPE_SPONSOR = 1;
	public static final int POI_TYPE_NORMAL = 2;
   
	//TODO: category version should be configurable
    public static final String CATEGORY_VERSION = "YP50";
    public static final long HIERARCHY_FULL = 1;
    protected static Logger logger = Logger.getLogger(POISearchExecutor_WS.class);
	public void doExecute(ExecutorRequest req, ExecutorResponse resp,
			ExecutorContext context) throws ExecutorException
	{
		CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
		cli.setFunctionName("POISearchExecutor_WS");
		// Get the request and response
        POISearchRequest_WS poiRequest = (POISearchRequest_WS) req;
        POISearchResponse_WS poiResponse = (POISearchResponse_WS) resp;
		
        TnContext tc = context.getTnContext();
        //TODO:temp add context, need set in dsm rule, this should be got from dms rule
//        tc.addProperty(TnContext.PROP_AD_NEEDSPONSOR, "TRUE");
//        tc.addProperty(TnContext.PROP_AD_ENGINE, "CITYSEARCH");
        //TODO: temp add for getting ad, if this can be setting in client side, should in request.
//        tc.addProperty("adtype","SPONSORED_TEXT,MERCHANT_CONTENT,COUPON,MENU");
//        tc.addProperty(TnContext.PROP_LOGIN_NAME, poiRequest.getLogInName());
//        tc.addProperty(TnContext.PROP_MAP_DATASET, "Navteq");
        UserProfile userProfile = req.getUserProfile();
        TnUtil.getDSMDataFromCServer(tc, userProfile);

        String poiDataSet = tc.getProperty(TnContext.PROP_POI_DATASET);
        if(poiDataSet == null)
        {
        	poiDataSet = "";
        }
        ReportingRequest misLog = new ReportingRequest(ReportType.SERVER_MIS_LOG_REPORT, userProfile, tc);
        
        misLog.addServerMisLogField(ServerMISReportor.SERVLET_NAME, ServerMISReportor.POI_SERVLET_NAME);
        misLog.addServerMisLogField(ServerMISReportor.ACTION_ID, this.getClass().getSimpleName());
        misLog.addServerMisLogField(ServerMISReportor.LOGTYPE_ID, ServerMISReportor.GET_POI_LOGTYPE);
        misLog.addServerMisLogField(ServerMISReportor.CUSTOM15, poiDataSet);
        misLog.addServerMisLogField(ServerMISReportor.CUSTOM16, tc.getProperty(TnContext.PROP_MAP_DATASET));
        
        
        String audioFormat = userProfile.getAudioFormat();;
        if (audioFormat == null)
        {
            audioFormat = poiRequest.getAudioFormat();
            userProfile.setAudioFormat(audioFormat);
        }
        
        // Validate request.
        if (poiRequest.getMaxResults() <= 0) {
            return;
        }
        
        long nCatID = poiRequest.getCategoryId();
        String strSearch = poiRequest.getSearchString();
        int radius = poiRequest.getRadiusInMeter();
        
        if(radius == 0)
        {
        	radius = SEARCH_REDIUS;
        }
        
        int searchType = poiRequest.getSearchType();
        int maxResults = poiRequest.getMaxResults();
        int pageSize = poiRequest.getPageSize();
        
        int sortType = poiRequest.getSortType();
        int pageNumber = poiRequest.getPageNumber();
        boolean isMostPopular = poiRequest.isMostPopular();
        //boolean needReviews = poiRequest.isNeedReviews();
        boolean needReviews = TnUtil.getPoiReivewFlag(userProfile);
        int searchFromType = poiRequest.getSearchFromType();
        //if search From Type is 2 or 4, it's typed from dsr.
        boolean isTypeFromDsr = searchFromType == TnConstants.TYPE_SEARCH_FROM_SPEAKIN || searchFromType == TnConstants.TYPE_SEARCH_FROM_SPEAKIN_ALONG;
        //TODO: search redius change
//        if(isMostPopular)
//        {
//            radius = MOST_POPULAR_SEARCH_RADIUS;
//        }
        Stop stop = poiRequest.getStop();
        Address anchor = new Address();
        anchor.setFirstLine(stop.firstLine);
        anchor.setLatitude(PoiDataConverter.DM5ToDegree(stop.lat));
        anchor.setLongitude(PoiDataConverter.DM5ToDegree(stop.lon));
        anchor.setCityName(stop.city);
        anchor.setState(stop.state);
        anchor.setCountry(stop.country);
        anchor.setPostalCode(stop.zip);
        
        PoiSearchRequest poiReq = new PoiSearchRequest();
        poiReq.setRegion(userProfile.getRegion());
        poiReq.setAnchor(anchor);
        poiReq.setCategoryId(nCatID);
        poiReq.setCategoryVersion(CATEGORY_VERSION);
        poiReq.setHierarchyId(HIERARCHY_FULL);
        poiReq.setNeedUserPreviousRating(true);
        poiReq.setPoiQuery(strSearch);
        poiReq.setPoiSortType(sortType);
        poiReq.setRadiusInMeter(radius);
        poiReq.setUserId(Long.parseLong(userProfile.getUserId()));
        poiReq.setPageNumber(pageNumber);
        poiReq.setPageSize(pageSize);
        poiReq.setMaxResult(maxResults);
        poiReq.setOnlyMostPopular(isMostPopular);
        poiReq.setAutoExpandSearchRadius(true);
        poiReq.setSponsorListingNumber(poiRequest.getSponsorListingNumber());
        poiReq.setNeedUserGeneratePois(true);
        poiReq.setNeedSponsoredPois(true);
        
		if (poiRequest.getTransactionID() != null) {
			poiReq.setTransactionId(poiRequest.getTransactionID());
		}
        
        cli.addData("searchParm", "catID=" + nCatID + "&query=" + strSearch + "&searchType=" + searchType 
                + "&sortType=" + sortType + "&isMostPopular=" + isMostPopular + "&lat=" + anchor.getLatitude()
                + "&lon=" + anchor.getLongitude() + "&pageNum=" + pageNumber + "&pageSize=" + maxResults
                + "&sponsorListingNumber" + poiRequest.getSponsorListingNumber()+"&transactionId="+poiRequest.getTransactionID() 
                + "&poiDataSet=" + poiDataSet);
        if(logger.isDebugEnabled())
        {
            logger.debug("searchParm catID=" + nCatID + "&query=" + strSearch + "&searchType=" + searchType 
                + "&sortType=" + sortType + "&isMostPopular=" + isMostPopular + "&lat=" + anchor.getLatitude()
                + "&lon=" + anchor.getLongitude() + "&pageNum=" + pageNumber + "&pageSize=" + maxResults 
                + "&sponsorListingNumber" + poiRequest.getSponsorListingNumber()+"&transactionId="+poiRequest.getTransactionID() 
                + "&poiDataSet=" + poiDataSet);
        }
        
        misLog.addServerMisLogField(ServerMISReportor.CUSTOM00, ""); //this field can be blank. Confirmed by vlad
        misLog.addServerMisLogField(ServerMISReportor.CUSTOM01, String.valueOf(nCatID));
        misLog.addServerMisLogField(ServerMISReportor.CUSTOM02, strSearch);
        misLog.addServerMisLogField(ServerMISReportor.CUSTOM03, String.valueOf(anchor.getLatitude()));
        misLog.addServerMisLogField(ServerMISReportor.CUSTOM04, String.valueOf(anchor.getLongitude()));
        misLog.addServerMisLogField(ServerMISReportor.CUSTOM05, String.valueOf(poiReq.getPageNumber()));
        misLog.addServerMisLogField(ServerMISReportor.CUSTOM06, isTypeFromDsr ? "DSR " : "Typed-in");
        
        if(isCurrentLocation(stop))
        {
        	misLog.addServerMisLogField(ServerMISReportor.CUSTOM09, String.valueOf(anchor.getLatitude()));
            misLog.addServerMisLogField(ServerMISReportor.CUSTOM10, String.valueOf(anchor.getLongitude()));
        }
        
		boolean setDistance = true;
		PoiSearchResponse result = null;
		try {
			result = PoiSearchHandlerFactory.getPoiSearchHandlerBy(
					userProfile.getProgramCode()).handle(poiReq, poiRequest,
					context);
		}
        catch(Exception e)
        {
        	logger.error(e, e);
        }

        if(logger.isDebugEnabled())
        {
        	logger.debug("search status:" + result != null ? result.getPoiSearchStatus() : "result is null");
        }
        if(result != null && result.getPoiSearchStatus() == ErrorCode.POI_STATUS_SUCCESS)
        {
        	//1. set sponsor pois
//            String productType = poiRequest.getProductType();
            int sponsorSize = 0;
//            if(!"ATT_MAPS".equals(productType) && !"ATT_NAV".equals(productType)){
        	cli.addData("sponsorPoiResult: need reviews ", Boolean.toString(needReviews));
                    
        	List<POI> sponsorList = getSponsorPoiResults(result.getSponsorPois(), setDistance,poiRequest.getDistanceUnit(),tc, needReviews);
        	if(sponsorList != null && sponsorList.size() > 0)
        	{
        		sponsorSize = sponsorList.size();
        	}
        	poiResponse.setSponsorPoiList(sponsorList);
        	//For TN70, Sponsor poi should still return full content
        	/*
        	int baseSponsorSize = 0;
        	List<BasePoi> baseSponsorList = getBaseSponsorPoiResults(result.getSponsorPois(), setDistance, poiRequest.getDistanceUnit(), tc, needReviews);
        	if(baseSponsorList != null && baseSponsorList.size() > 0)
        	{
        		baseSponsorSize = baseSponsorList.size();
        	}
        	poiResponse.setBaseSponsorPoiList(baseSponsorList);
        	*/
//            }
            //2. set pois
            cli.addData("poiResult: need reviews ", Boolean.toString(needReviews));
            List<POI> poiList = getPoiResults(result.getPois(), sponsorSize, maxResults, setDistance,poiRequest.getDistanceUnit(),tc, needReviews);
        	poiResponse.setPoiList(poiList);
        	List<BasePoi> basePoiList = getBasePoiResults(result.getPois(), sponsorSize, maxResults, setDistance,poiRequest.getDistanceUnit(),tc, needReviews);
        	poiResponse.setBasePoiList(basePoiList);
        	
        	poiResponse.setTotalCount(result.getTotalMatchedPoiCount());
        	poiResponse.setSponsorListingNumber(poiRequest.getSponsorListingNumber());
        	
        	Long totalCount = poiResponse.getTotalCount();
    		int totalMaxPageIndex = (int) (totalCount / Constant.PAGE_SIZE);
    		if (totalCount % Constant.PAGE_SIZE == 0) {
    			totalMaxPageIndex = totalMaxPageIndex - 1;
    		}
    		poiResponse.setTotalMaxPageIndex(totalMaxPageIndex);
    		
        	poiResponse.setDistanceUnit(poiRequest.getDistanceUnit());
        	
        	if(pageNumber == 0 && poiRequest.isNeedAudio())
        	{
        		//convert to AllTypePOI list
        		List<AllTypePOI> allTypePOIList = new ArrayList<AllTypePOI>();
        		if(poiList != null && poiList.size() > 0)
        		{
        			for(POI poi : poiList)
        			{
        				AllTypePOI aPoi = new AllTypePOI();
        				aPoi.poiType = POI_TYPE_NORMAL;
        				aPoi.poi = poi;
        				allTypePOIList.add(aPoi);
        			}
        		}
        		PromptItem[] promptItems = getAudio(strSearch, allTypePOIList, searchFromType, poiRequest.getDistanceUnit(), MAX_AUDIO_NUMBER, INCLUDE_AUDIO_NUMBER, userProfile);
        		poiResponse.setPromptItems(promptItems);
        	}
        	
        	poiResponse.setPoiDetailUrl(WebPageMappingManager.getPoiDetailUrl(userProfile, tc));
        	//poiResponse.setPoiDetailUrl("http://172.16.10.178:8080/poi_service/html/poidetail.do?width=480&height=800&clientInfo={\"programCode\":\"SCOUTUSPROG\",\"platform\":\"ANDROID\",\"version\":\"7.2\",\"productType\":\"SCOUTUS\",\"device\":\"generic\",\"locale\":\"en_US\"}");
 
        	int NumberOfResults = sponsorSize;
        	if(poiList!=null)
        	{
        	    NumberOfResults = NumberOfResults + poiList.size();
        	}
            misLog.addServerMisLogField(ServerMISReportor.CUSTOM07, String.valueOf(NumberOfResults));
        	misLog.addServerMisLogField(ServerMISReportor.COMPLETED_FLAG, ServerMISReportor.COMPLETE_SUCCEED);
        }
        else
        {
            misLog.addServerMisLogField(ServerMISReportor.CUSTOM07, "0");
            cli.addData("SearchStatus", "poi not found.");
        	if(logger.isDebugEnabled())
        	{
        		logger.debug("poi not found.");
        	}
        }
        misLog.addServerMisLogField(ServerMISReportor.CUSTOM08, poiReq.getTransactionId());
        cli.complete();
        ReportingUtil.report(misLog);
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
    
	private List<POI> getSponsorPoiResults(List<TnPoi> sPois, boolean setDistance,int distanceUnit,TnContext tc, boolean needReviews)
	{
		if(sPois == null || sPois.size() == 0)
		{
			return null;
		}
		List<POI> sponsorPois = new ArrayList<POI>();
		
		if(logger.isDebugEnabled())
		{
			logger.debug("SponsorPoiResults sPoi size = " + sPois.size());
		}
		
		for(int i = 0; i < sPois.size() && i < MAX_SPONSORED_NUM; i++)
		{
			if(logger.isDebugEnabled())
			{
				logger.debug("SponsorPoiResults sPoi ID = " + sPois.get(i) + ", brandName = " + sPois.get(i).getBrandName());
			}
			POI poi = PoiDataConverter.convertTnPoiToPOI(sPois.get(i), setDistance,distanceUnit,tc, needReviews);
			if(poi != null)
			{
				sponsorPois.add(poi);
			}
		}
		
		return sponsorPois;
	}
	
	private List<BasePoi> getBasePoiResults(List<TnPoi> pois, int sponsorSize, int maxResults, boolean setDistance,int distanceUnit,TnContext tc, boolean needReviews)
	{
		return PoiUtil.getBasePoiResults(pois, sponsorSize, maxResults, setDistance, distanceUnit, tc, needReviews);
	}
	
	private List<POI> getPoiResults(List<TnPoi> pois, int sponsorSize, int maxResults, boolean setDistance,int distanceUnit,TnContext tc, boolean needReviews)
	{
		if(pois == null || pois.size() == 0)
		{
			return null;
		}
		int poiSize = pois.size();
		List<POI> poiResults = new ArrayList<POI>(poiSize);
		for(int i = 0; i < poiSize; i++)
		{
			POI poi = PoiDataConverter.convertTnPoiToPOI(pois.get(i), setDistance,distanceUnit,tc, needReviews);
			if(poi != null)
			{
				poiResults.add(poi);
			}
		}
		return poiResults;
	}

	
	/**
     * Get the audio according to the POI list.
     * 
     * @param poiList POI List.
     * @return Audio for client.
     */
    private PromptItem[] getAudio(String searchString, List<AllTypePOI> poiList, int searchFromType, int distanceUnit, int maxAudioNumber, int includeAudioNumber, UserProfile userProfile) {
        if (poiList == null || poiList.isEmpty()) {
            return null;
        }
        
        int infosSize =  poiList.size();
        if(infosSize > maxAudioNumber){
            infosSize = maxAudioNumber;
        }
        POIResultInfo[] infos = new POIResultInfo[infosSize];
        Locale locale = CommonUtil.getLocale(userProfile.getLocale());
        String language = locale.getLanguage();
        String country = locale.getCountry();
        
        for(int i = 0; i < infosSize; i++)
        {
        	AllTypePOI aPoi = poiList.get(i);
            BizPOI bizPOI = aPoi.poi.bizPoi;
            // POI name
            NameInfo poiName = new NameInfo(bizPOI.brand, language, country);

            // street name 1
            String street = bizPOI.address.street;
            if(street == null || street.equals(""))
            {
            	street = bizPOI.address.firstLine;
                if (street != null && !"".equals(street)
                        && Character.isDigit(street.charAt(0))) {
                    int first = street.indexOf(' ');
                    street = street.substring(first + 1,street.length()).trim();
                }
            }
            
            NameInfo nameInfo1 = new NameInfo(street, language, country);

            // street name 2
            NameInfo nameInfo2 = null;
            String street2Str = bizPOI.address.street2;
            if (street2Str != null && !"".equals(street2Str)) {
            	nameInfo2 = new NameInfo(street2Str, language, country);
            }
            if(searchFromType == 3){
                int distanceInMeter = bizPOI.distance;
                //String unit = POIAudioRequest.UNIT_MILE;
                double distance = PoiDataConverter.getUnitDistance(distanceInMeter, distanceUnit);
                
                String strUnitDistance = String.valueOf(distance);
                String unit = getUnit(distanceUnit, distance);
                nameInfo1 = new NameInfo(strUnitDistance, language, country);
                nameInfo2 = new NameInfo(unit, language, country);
            }

            NameInfo city = new NameInfo(bizPOI.address.city, language, country);

            // Sponsor POI
            boolean isSponsor = false;
            if(aPoi.poiType == POI_TYPE_SPONSOR)
            {
            	isSponsor = true;
            }

            // Include audio.
            boolean includeAudio = false;
            if (i < includeAudioNumber) 
            {
                includeAudio = true;
            }

            // New info.
            POIResultInfo info = new POIResultInfo(poiName, nameInfo1,
            		nameInfo2, city, isSponsor, includeAudio);

            infos[i] = info;
        }
       
        // userProfile.getGuideTone() e.g."203,xxxx"
        // When id is 203, please set the value as null which is the
        // default value
        //
        // When id is 204, please set the value as "male", please check
        // this value with Lele
        //
        // For other id, set the value as the value after ",", e,g 205
        // is "aaaa"
        // XXX it's to fix bug 84129 temporarily.
		String guideTone = null;
		if (null != userProfile) {
			String originalGuideTone = userProfile.getGuideTone();
			if (null != originalGuideTone) {
				String[] s = originalGuideTone.split(",");
				String guideToneId = s[0];
				if ("203".equals(guideToneId)) {
					guideTone = null;
				} else if ("204".equals(guideToneId)) {
					guideTone = "male";
				} else if (s.length >= 2) {
					guideTone = s[1];
				}
			}
		}
		// Call backend.
        POIAudioRequest par = new POIAudioRequest(
                ResourceConstants.DATA_PACKET_TYPE, userProfile.getAudioFormat(), locale.toString(), guideTone,
                infos, searchFromType, searchString);
        ResourceServiceClient rsc = new ResourceServiceClient();
        POIAudioResponse resp = rsc.getPoiAudios(par);
        // ResourceResponse resp = rsc.makeResourceRequest(par);

        // Return response.
        if (resp.wasSuccessful()) {
            // TxNode node = TxNode.fromByteArray(resp.getData(), 0);
            PromptItem[] items = resp.getPromptSequence();
            return items;
        } else {
            // TODO remove
            System.out.println("Request was not succesful: status="
                    + resp.getErrorCode() + " - error="
                    + resp.getErrorMessage());
            return null;
        }
    }
    
    private static String getUnit(int iUnit, double distance)
    {
    	String unit = POIAudioRequest.UNIT_METERS;
    	if(distance <= 1) //singular
    	{
    		switch(iUnit)
    		{
    		case PoiConstants.UNIT_METER:
    			unit = POIAudioRequest.UNIT_METER;
    			break;
    		case PoiConstants.UNIT_MILE:
    			unit = POIAudioRequest.UNIT_MILE;
    			break;
    		case PoiConstants.UNIT_KM:
    			unit = POIAudioRequest.UNIT_KM;
    			break;
    		case PoiConstants.UNIT_FOOT:
    			unit = POIAudioRequest.UNIT_FOOT;
    			break;
    		}
    	}
    	else //plural
    	{
    		switch(iUnit)
    		{
    		case PoiConstants.UNIT_METER:
    			unit = POIAudioRequest.UNIT_METERS;
    			break;
    		case PoiConstants.UNIT_MILE:
    			unit = POIAudioRequest.UNIT_MILES;
    			break;
    		case PoiConstants.UNIT_KM:
    			unit = POIAudioRequest.UNIT_KMS;
    			break;
    		case PoiConstants.UNIT_FOOT:
    			unit = POIAudioRequest.UNIT_FEET;
    			break;
    		}
    	}
    	return unit;
    }
}
class AllTypePOI
{
	public int poiType;
	public POI poi;
}
