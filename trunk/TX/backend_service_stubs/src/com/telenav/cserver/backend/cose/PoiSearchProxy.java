/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.cose;

import java.util.Map;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.log4j.Logger;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.backend.config.WebServiceConfigInterface;
import com.telenav.cserver.backend.config.WebServiceItem;
import com.telenav.cserver.backend.datatypes.ErrorCode;
import com.telenav.cserver.backend.util.WebServiceConfiguration;
import com.telenav.cserver.backend.util.WebServiceManager;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.cserver.framework.throttling.ThrottlingManager;
import com.telenav.datatypes.content.cose.v10.PoiDataSet;
import com.telenav.datatypes.content.cose.v10.PoiSearchArea;
import com.telenav.datatypes.content.cose.v10.PoiSearchAreaType;
import com.telenav.datatypes.content.cose.v10.PoiSearchService;
import com.telenav.datatypes.content.cose.v10.PoiSearchType;
import com.telenav.datatypes.content.cose.v10.ServiceAgent;
import com.telenav.datatypes.content.cose.v10.ServiceVendor;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.services.content.v10.ContentInfoServiceStub;
import com.telenav.services.content.v10.ContentSearchServiceStub;

/**
 * PoiSearchProxy.java
 * 
 * @author bhu@telenav.cn
 * @version 1.0 2009-7-10
 */
public class PoiSearchProxy
{
    private static final Logger logger = Logger.getLogger(PoiSearchProxy.class);

    public static final String POI_SEARCH_SERVICE = "POI_SEARCH";
    public static final String GET_POIDETAILS_SERVICE = "GET_POIDETAILS";
    private static final String SERVICE_POISEARCHSERVER = "POISEARCHSERVER";

    private static final String POI_YPC_MOBILE_ENDPOINT = "POI.YPC-MOBILE";
    private static final String POI_YPC_ENDPOINT = "POI.YPC";
    private static final String POI_TA_ENDPOINT = "POI.TA";
    private static final String POI_DATASET_TA = "TA";
    private static final String POI_CN_ENDPOINT = "POI.CN";
    private static ConfigurationContext configurationContext;
    private static final String REGION_CN = "CN";
    
    private TnContext tnContext;

    public PoiSearchProxy(TnContext tc)
    {
        tnContext = tc;
    }
    
    /**
     * search within city
     * 
     * @param req
     * @return
     */
    public PoiSearchResponse searchWithinCity(PoiSearchRequest req)throws ThrottlingException
    {
        return search(req, PoiSearchAreaType.CITY_STATE, PoiSearchType.FOR_GENERAL_POI);
    }

    /**
     * search with zip code
     * 
     * @param req
     * @return
     */
    public PoiSearchResponse searchWithinZip(PoiSearchRequest req)throws ThrottlingException
    {
        return search(req, PoiSearchAreaType.ZIP_AND_COUNTRY, PoiSearchType.FOR_GENERAL_POI);
    }

    /**
     * search along route
     * 
     * @param req
     * @return
     */
    public PoiSearchResponse searchAlongRoute(PoiSearchRequest req) throws ThrottlingException
    {
        return search(req, PoiSearchAreaType.ROUTE_POINTS_AND_RADIUS, PoiSearchType.FOR_GENERAL_POI);
    }

    /**
     * search within distance
     * 
     * @param req
     * @return
     */
    public PoiSearchResponse searchWithinDistance(PoiSearchRequest req) throws ThrottlingException
    {
        return search(req, PoiSearchAreaType.ANCHOR_AND_RADIUS, PoiSearchType.FOR_GENERAL_POI);
    }

    /**
     * search airport.
     * @param req
     * @return
     */
    public PoiSearchResponse searchAirport(AirportSearchRequest req)throws ThrottlingException
    {
        return searchAirport(req, PoiSearchAreaType.COUNTRYLIST);
    }

    private PoiSearchResponse searchAirport(AirportSearchRequest req, PoiSearchAreaType psaType) throws ThrottlingException
    {
        if(req!=null && logger.isDebugEnabled()) 
        {//recored the request information
            logger.debug("PoiSearchRequest=====>"+req.toString()+"\n" + "PoiSearchAreaType=" + psaType.toString());
        }
        PoiSearchResponse poiSearchResponse = new PoiSearchResponse();
        com.telenav.services.content.v10.PoiRequest request = PoiSearchConverter.airportRequesttoWSRequest(req);
        if (tnContext != null)
        {
            request.setContextString(tnContext.toContextString());
        }
        else
        {
            poiSearchResponse.setPoiSearchStatus(ErrorCode.POI_STATUS_PARAMETER_ERROR);
            return poiSearchResponse;
        }
        request.setSearchType(PoiSearchType.FOR_AIRPORT);
        PoiSearchArea psa = request.getSearchArea();
        if (psa != null)
        {
            psa.setAreaType(psaType);
        }
        com.telenav.services.content.v10.PoiSearchResponse response = search(request, req.getRegion());
        if(response != null)
        {
            poiSearchResponse = PoiSearchConverter.convertWSRespToPoiSearchResponse(response);
            if(logger.isDebugEnabled()) 
            {
                logger.debug("poiSearchResponse=====>" + poiSearchResponse.toString());
            }
        }
        else
        {
            if(logger.isDebugEnabled()) 
            {
                logger.debug("poi Search Response is null");
            }
            poiSearchResponse.setPoiSearchStatus(ErrorCode.POI_STATUS_POI_NOT_FOUND);
        }
        
        return poiSearchResponse;
    }
    
    private PoiSearchResponse search(PoiSearchRequest req, PoiSearchAreaType psaType, PoiSearchType searchType) throws ThrottlingException
    {   
        if(req!=null && logger.isDebugEnabled()) {//recored the request information
            logger.debug("PoiSearchRequest=====>"+req.toString()+"\n" + "PoiSearchAreaType=" + psaType.toString()+ "\n" +"searchType="+ searchType.toString());
        }
        PoiSearchResponse poiSearchResponse = new PoiSearchResponse();
        com.telenav.services.content.v10.PoiRequest request = PoiSearchConverter.poiRequesttoWSRequest(req);
        if(request == null)
        {
            if(logger.isDebugEnabled())
            {
                logger.debug("PoiSearchRequest is null.");
            }
            poiSearchResponse.setPoiSearchStatus(ErrorCode.POI_STATUS_PARAMETER_ERROR);
            return poiSearchResponse;
        }
        if (tnContext != null)
        {
            request.setContextString(tnContext.toContextString());
        }
        else
        {
            poiSearchResponse.setPoiSearchStatus(ErrorCode.POI_STATUS_PARAMETER_ERROR);
            return poiSearchResponse;
        }
        request.setSearchType(searchType);
        PoiSearchArea psa = request.getSearchArea();
        if (psa != null)
        {
            psa.setAreaType(psaType);
        }
        com.telenav.services.content.v10.PoiSearchResponse response = search(request, req.getRegion());
        if (response != null)
        {
            poiSearchResponse = PoiSearchConverter.convertWSRespToPoiSearchResponse(response);
            if(logger.isDebugEnabled()) {
                logger.debug("poiSearchResponse=====>" + poiSearchResponse.toString());
            }
        }
        else
        {
            if(logger.isDebugEnabled()) {
                logger.debug("poi Search Response is null");
            }
            poiSearchResponse.setPoiSearchStatus(ErrorCode.POI_STATUS_POI_NOT_FOUND);
        }
        
        return poiSearchResponse;
    }
    
    public PoiDetailsResponse getPoiDetails(PoiDetailsRequest request)  throws ThrottlingException
    {
    	if(request != null && logger.isDebugEnabled())
    	{
    		logger.debug("getPoiDetails ===> poi id : " + request.getPoiId());
    	}
    	PoiDetailsResponse response = new PoiDetailsResponse();
    	if(request == null)
    	{
    		if(logger.isDebugEnabled())
    		{
    			logger.debug("PoiDetailsRequest is null");
    		}
    		response.setStatus(ErrorCode.POI_STATUS_PARAMETER_ERROR);
    		return response;
    	}
    	com.telenav.services.content.v10.GetPoiDetailsRequest req = PoiSearchConverter.createPoiDetailsRequest(request);
    	if(tnContext != null)
    	{
    		req.setContextString(tnContext.toContextString());
    	}
    	else
    	{
    		response.setStatus(ErrorCode.POI_STATUS_PARAMETER_ERROR);
    		return response;
    	}
    	com.telenav.services.content.v10.GetPoiDetailsResponse resp = getPoiDetails(req);
    	if(resp != null)
    	{
    		response = PoiSearchConverter.createPoiDetailsResponse(resp);
    		if(logger.isDebugEnabled())
    		{
    			logger.debug("GetPoiDetailsResponse ==>" + response.toString());
    		}
    	}
    	else
    	{
    		if(logger.isDebugEnabled())
    		{
    			logger.debug("GetPoiDetails is null");
    		}
    		response.setStatus(ErrorCode.POI_STATUS_POI_NOT_FOUND);
    	}
    	return response;
    }

    private static ConfigurationContext getConfigContext(WebServiceItem serviceItem) throws AxisFault
    {
        if (configurationContext == null)
        {
            synchronized (PoiSearchProxy.class)
            {
                configurationContext = WebServiceManager.createNewContext(serviceItem);
            }
        }
        return configurationContext;
    }

    private ServiceAgent createCOSETAAgent()
    {
        ServiceAgent agent = new ServiceAgent();
        agent.setDataSet(PoiDataSet.TA);
        agent.setService(PoiSearchService.TELENAV_COSE);
        agent.setVendor(ServiceVendor.TELENAV);
        return agent;
    }

    private ServiceAgent createCOSEYPCAgent()
    {
        ServiceAgent agent = new ServiceAgent();
        agent.setDataSet(PoiDataSet.YPC);
        agent.setService(PoiSearchService.TELENAV_COSE);
        agent.setVendor(ServiceVendor.TELENAV);
        return agent;
    }

    private void setRequestInfo(com.telenav.services.content.v10.PoiRequest request)
    {
        request.setClientName("clientName");
        request.setClientVersion("clientVersion");
        //if client not set transaction id.
        if(request.getTransactionId() == null)
        {
            request.setTransactionId(String.valueOf(System.currentTimeMillis()));
        }
        ServiceAgent sa;

        if (tnContext == null)
        {
            sa = createCOSETAAgent();
        }
        else
        {
            String poidataset = tnContext.getProperty(TnContext.PROP_POI_DATASET);
            if (poidataset == null || poidataset.equals("") || poidataset.equalsIgnoreCase(POI_DATASET_TA))
            {
                sa = createCOSETAAgent();
            }
            else
            {
                sa = createCOSEYPCAgent();
            }
        }

        request.setServiceAgent(sa);
    }
    
    private com.telenav.services.content.v10.GetPoiDetailsResponse getPoiDetails(com.telenav.services.content.v10.GetPoiDetailsRequest request) throws ThrottlingException
    {
    	CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
    	cli.setFunctionName("GetPoiDetails");
    	ContentInfoServiceStub stub = null;
    	com.telenav.services.content.v10.GetPoiDetailsResponse response = null;
    	boolean startAPICall = false;
    	try
    	{
    		startAPICall = ThrottlingManager.startAPICall(SERVICE_POISEARCHSERVER, this.tnContext);
    		if(!startAPICall)
    		{
    			throw new ThrottlingException();
    		}
    		try
    		{
    			WebServiceConfigInterface wsConfig = WebServiceConfiguration.getInstance().getServiceConfig(GET_POIDETAILS_SERVICE);
    			//String endpoint = "http://contentinfo-ws.mypna.com/tnws/services/ContentInfoService";
    			String endpoint = wsConfig.getServiceUrl();
                if(logger.isDebugEnabled())
                {
                	logger.debug("Get poi details service endpoint : " + endpoint);
                }
                cli.addData("Request poi id", Long.toString(request.getPoiId()));
    			if(endpoint != null)
    			{
    				stub = new ContentInfoServiceStub(getConfigContext(wsConfig.getWebServiceItem()), endpoint);
    				response = stub.getPoiDetails(request);
    			}
    			else
    			{
    				logger.fatal("Failed to get url.");
    			}
    		}
    		catch(Exception ex)
    		{
    			logger.fatal("PoiSearchProxy::getPoiDetail", ex);
    		}
    	}
    	finally
    	{
    		cli.complete();
    		if (stub != null)
    		{
    			try
    			{
    				stub._getServiceClient().cleanupTransport();
    			}
    			catch (Exception e)
    			{
    				logger.fatal("PoiSearchProxy::getPoiDetail()", e);
    			}
    		}
    		if (startAPICall)// throttling
    		{
    			ThrottlingManager.endAPICall(SERVICE_POISEARCHSERVER, this.tnContext);
    		}
    	}
    	return response;
    }

    private com.telenav.services.content.v10.PoiSearchResponse search(com.telenav.services.content.v10.PoiRequest request, String region) throws ThrottlingException
    {  
    	CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
		cli.setFunctionName("Poi_Search");
        ContentSearchServiceStub stub = null;
        com.telenav.services.content.v10.PoiSearchResponse response = null;
        boolean startAPICall = false;
        try
        {
            startAPICall = ThrottlingManager.startAPICall(SERVICE_POISEARCHSERVER, this.tnContext);
            if (!startAPICall)
            {
                throw new ThrottlingException();
            }
            try
            {
                setRequestInfo(request);
                
                if(logger.isDebugEnabled()) {
                    logger.debug("DataSet= "+request.getServiceAgent().getDataSet().getValue() +" region= "+region);
                }
                //record poi search request
                cli.addData("Request", cliPoiSearchRequest(request)+" Region= "+ region);
                WebServiceConfigInterface wsConfig = WebServiceConfiguration.getInstance().getServiceConfig(POI_SEARCH_SERVICE);
                Map urlMap = wsConfig.getServiceUrlMapping();
                String endpoint = getEndpoint(urlMap, request, region);
                if(endpoint != null)
                {
                    stub = new ContentSearchServiceStub(getConfigContext(wsConfig.getWebServiceItem()), endpoint);
                    // response = stub.search(request);
                    response=stub.searchPoi(request);
                    cli.addData("Response", cliPoiSearchResponse(response));
                }
                else
                {
                   logger.fatal("Failed to get url.");
                }
                
            }
            catch (Exception e)
            {
                logger.fatal("PoiSearchProxy::search()", e);
            }
        }
        finally
        {   
        	cli.complete();
            if (stub != null)
            {
                try
                {
                    stub._getServiceClient().cleanupTransport();
                }
                catch (Exception e)
                {
                    logger.fatal("PoiSearchProxy::search()", e);
                }
            }
            if (startAPICall)// throttling
            {
                ThrottlingManager.endAPICall(SERVICE_POISEARCHSERVER, this.tnContext);
            }
        }
        return response;
    }
    
    /**
     * Generate cli log for poi search request
     * @param request
     * @return
     */
    private String cliPoiSearchResponse(com.telenav.services.content.v10.PoiSearchResponse response) {
	    StringBuilder sb=new StringBuilder();
	    sb.append("response==>[");
	    sb.append("code= "+response.getResponseStatus().getStatusCode());
	    sb.append(", message= "+response.getResponseStatus().getStatusMessage());
	    sb.append(", MatchCount= "+response.getTotalMatchedPoiCount());
	    if(response.getPois() != null)
		{   
	    	sb.append(", poi id=");
	    	for(int i = 0; i < response.getPois().length; i++)
			{
	    		sb.append(response.getPois()[i].getPoiId()+" ,");
			}
		}
	    if(response.getSponsorPois() != null)
		{   
	    	sb.append(",Sponsor poi id=");
	    	for(int i = 0; i < response.getSponsorPois().length; i++)
			{
	    		sb.append(response.getSponsorPois()[i].getPoiId()+" ,");
			}
		}
	    sb.append("]");
	    return sb.toString();
    }
    
    
    /**
     * Generate cli log for poi search request
     * @param request
     * @return
     */
    private String cliPoiSearchRequest(com.telenav.services.content.v10.PoiRequest request) {
	    StringBuilder sb=new StringBuilder();
	    sb.append("request==>[");
	    sb.append(", poiQuery=").append(request.getPoiQuery());
	    sb.append(", categoryid=").append(request.getCategoryParam()==null? "":request.getCategoryParam().getCategoryId()[0]);
	    sb.append(", Latitude=").append(request.getSearchArea()==null?"": (request.getSearchArea().getAnchor()==null? "" :request.getSearchArea().getAnchor().getLatitude()));
	    sb.append(", Longitude=").append(request.getSearchArea()==null?"":(request.getSearchArea().getAnchor()==null? "" :request.getSearchArea().getAnchor().getLongitude()));
	    sb.append("]");
	    return sb.toString();
    }
    
    
    private String getEndpoint(Map urlMap, com.telenav.services.content.v10.PoiRequest request, String region)
    {
        ServiceAgent sa = request.getServiceAgent();
        String endpoint;
        if(urlMap != null)
        {
            if(region != null && region.equals(REGION_CN)) //connect to CN cose
            {
                endpoint = (String)urlMap.get(POI_CN_ENDPOINT);
            }
            else //connect to US cose
            {
              //TODO: Not setting YPC_MOBILE now
//              if (sa!=null && sa.getVendor()!=null && sa.getVendor().equals(ServiceVendor.YPC))
//              {
//                  endpoint = (String)urlMap.get(POI_YPC_MOBILE_ENDPOINT);
//              } 
//              else 
              if (sa.getVendor().equals(ServiceVendor.TELENAV) && sa.getDataSet().equals(PoiDataSet.YPC))
              {
                  endpoint = (String)urlMap.get(POI_YPC_ENDPOINT);
              }
              else
              {
                  endpoint = (String)urlMap.get(POI_TA_ENDPOINT);
              }
            }
            return endpoint;
        }
        else
        {
            return null;
        }


    }
}
