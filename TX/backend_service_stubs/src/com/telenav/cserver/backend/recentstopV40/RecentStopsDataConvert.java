/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.recentstopV40;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.telenav.cserver.backend.cose.PoiSearchConverterV20;
import com.telenav.cserver.backend.datatypes.AddressDataConverter;
import com.telenav.datatypes.locale.v10.Country;
import com.telenav.datatypes.locale.v10.Locale;
import com.telenav.datatypes.content.poi.v10.PoiAttributes;
import com.telenav.datatypes.stop.v20.RecentStop;
import com.telenav.datatypes.content.tnpoi.v20.TnPoi;
import com.telenav.kernel.ws.axis2.Axis2Helper;
import com.telenav.services.recentstop.v30.AllRequestDTO;
import com.telenav.services.recentstop.v30.AllResponseDTO;
import com.telenav.services.recentstop.v30.DeleteRequestDTO;
import com.telenav.services.recentstop.v30.QueryRequestDTO;
import com.telenav.services.recentstop.v30.RecentStopsRequestDTO;
import com.telenav.services.recentstop.v30.RecentStopsResponseDTO;
import com.telenav.datatypes.address.v40.GeoCoordinate;
import com.telenav.datatypes.address.v40.Street;
import com.telenav.ws.datatypes.services.mgmt.ServiceMgmtResponseDTO;
import com.telenav.services.recentstop.v30.BasicResponseDTO;

/**
 * 
 * @author zhjdou
 * 
 */
public class RecentStopsDataConvert
{
    static Logger logger = Logger.getLogger(RecentStopsDataConvert.class);

    // this code for service data handling, we just hackle it at the moment
    // service side alearding on it, we will use their flag when they release new version
    private static final String RECENTSTOP_UNCHANGED = "N";

    /**
     * convertt he response to the fake type
     * 
     * @return
     */
    public static QueryResponse convertQueryResponse2Proxy(RecentStopsResponseDTO response)
    {
    	QueryResponse proxy = null;
        if (response != null)
        {
            proxy = new QueryResponse();
            proxy.setStatusCode(Integer.valueOf(response.getResponseStatus().getStatusCode()));
            proxy.setStatusMessage(response.getResponseStatus().getStatusMessage());
            proxy.setStops(convertRecentStopArray2RecentStopProxy(response.getRecentStops()));
        }
        
        return proxy;
    }

    /**
     * request converter, convert QueryRequest to QueryRequestDTO
     * 		userId (long)
     * 		recentStop (recentstop20:RecentStop)
     * 		count (int)
     * 		keyWord (string)
     * 		needPoiInfo (boolean)
     * 
     * 
     * Convert the query request to the real one
     * 
     * @param request
     * @return
     */
    
    public static QueryRequestDTO convertProxy2QueryRequest(QueryRequest proxy)
    {
    	QueryRequestDTO request = null;
        if (proxy != null)
        {
            request = new QueryRequestDTO();
            
            request.setClientName(RecentStopsServiceProxy.clientName);// can not be null
            request.setClientVersion(RecentStopsServiceProxy.clientVersion);     // xnav use 1.0 as clientVersion in their test code
            request.setTransactionId(RecentStopsServiceProxy.transactionId);
            request.setContextString(proxy.getContext());
            
            request.setCount(proxy.getMaxResult());
            request.setUserId(proxy.getUserId());
            request.setNeedPoiInfo(true);
            
        }
        return request;
    }

    /**
     * Transform the response that from backend service, to our encapsulated type<code>RecentStopsResponse</code>
     * 
     * @param rsResponse
     * @return
     */
    protected static RecentStopsResponse convertDTOResponse2Proxy(AllResponseDTO rsResponse)
    {
        if (rsResponse == null)
        { // check the parameter
            return null;
        }
        RecentStopsResponse response = new RecentStopsResponse();
        response.setStatus(Integer.valueOf(rsResponse.getResponseStatus().getStatusCode()));
        response.setStatusMessage(rsResponse.getResponseStatus().getStatusMessage());
        response.setTotalRecentStops(rsResponse.getCount());
        RecentStop[] recentInsertedStop = rsResponse.getInsertedRecentStops();
        com.telenav.cserver.backend.datatypes.recentstopV40.RecentStop[] insertStop = convertRecentStopArray2RecentStopProxy(recentInsertedStop);
        RecentStop[] recentDeletedStop = rsResponse.getDeletedRecentStops();// just contain stop id
        com.telenav.cserver.backend.datatypes.recentstopV40.RecentStop[] deletedStop = convertRecentStopArray2RecentStopProxy(recentDeletedStop);
        response.setInsertedStops(insertStop);
        response.setDeletedStopIds(deletedStop);
        return response;
    }

    /**
     * convert RecentStop To RecentStop Property
     * 
     * @param stopProperty
     * @return
     */
    protected static com.telenav.cserver.backend.datatypes.recentstopV40.RecentStop convertRecentStop2RecentStopProxy(RecentStop recentStop)
    {
        if (recentStop == null)
        { // check the parameter
            return null;
        }
        com.telenav.cserver.backend.datatypes.recentstopV40.RecentStop stopProxy = new com.telenav.cserver.backend.datatypes.recentstopV40.RecentStop();
        // You can get RecentStop.getRecentPoi() to tell whether this recentstop is a poi or not.
        // If null is returned, the recentstop is not a poi stop.
        if (recentStop.getRecentPoi() != null)
        {
            stopProxy.setPoi(true);
            com.telenav.cserver.backend.datatypes.TnPoi poiProxy = PoiSearchConverterV20.convertWSPoiToCSTnPoi(recentStop.getRecentPoi());
            stopProxy.setRecentPoi(poiProxy);
        }
        stopProxy.setOwnerUserId(recentStop.getOwnerUserId());
        stopProxy.setRecentStopId(recentStop.getRecentStopId());
        stopProxy.setLabel(recentStop.getLabel());
        
        // convert the address to proxy type
        com.telenav.cserver.backend.datatypes.recentstopV40.RecentAddress addrProxy = convertRecentAddress2AddressProxy(recentStop
                .getRecentAddress());
        if(recentStop.getLabel() != null && addrProxy != null){
        	addrProxy.setLabel(recentStop.getLabel());
        }
        
        stopProxy.setRecentAddress(addrProxy);
        if (recentStop.getRecentPoi() != null)
        {
            stopProxy.setPoi(true);
        }
        return stopProxy;
    }
    
    
    /**
     * Convert the address to PROXY type
     * 
     * @param addr
     * @return
     */
    private static com.telenav.cserver.backend.datatypes.recentstopV40.RecentAddress convertRecentAddress2AddressProxy(com.telenav.datatypes.address.v40.Address addr)
    {
    	com.telenav.cserver.backend.datatypes.recentstopV40.RecentAddress addrProxy = null;

        if (addr != null)
        { // save all attributes
        	addrProxy = new com.telenav.cserver.backend.datatypes.recentstopV40.RecentAddress();
        	AddressDataConverter.convertWSAddressV40ToCSAddress(addr,addrProxy);

        }
        return addrProxy;
    }


    
    /*
     * After ACE upgrading its system to 4.0 to support international address type, all the backend servers that involved address in the request/response will
     * upgrade their data.
     * 
     *  To support both old and new request, use override method to upgrade related methods.
     *  
     */

    protected static com.telenav.cserver.backend.datatypes.recentstopV40.RecentStop[] convertRecentStopArray2RecentStopProxy(com.telenav.datatypes.stop.v20.RecentStop[] recentStopRaw)
    { 
    	
    	com.telenav.cserver.backend.datatypes.recentstopV40.RecentStop[] recentStopProcess = null;
    	
        if (recentStopRaw != null && Axis2Helper.isNonEmptyArray(recentStopRaw))
        {
            int length = recentStopRaw.length;
            recentStopProcess = new com.telenav.cserver.backend.datatypes.recentstopV40.RecentStop[length];
            for (int index = 0; index < length; index++)
            {
            	recentStopProcess[index] = convertRecentStop2RecentStopProxy(recentStopRaw[index]);
            }
        }
        return recentStopProcess;
    }
    
    
    

    
    
    /**
     * 
     * [Code change...]
     * 		Reason: To support ACE upgrading to 4.0, override function convertRecentStopProxyArray2RecentStop to support new RecentStop transform
     * 
     * 
     * Transform the data to recentSTOP(Array)
     * 
     * @param proxy []
     * @return
     */
    protected static com.telenav.datatypes.stop.v20.RecentStop[] convertRecentStopProxyArray2RecentStop(com.telenav.cserver.backend.datatypes.recentstopV40.RecentStop[] proxy)
    {
    	com.telenav.datatypes.stop.v20.RecentStop[] stop = null;
    	
        if (proxy != null && Axis2Helper.isNonEmptyArray(proxy))
        {
            int length = proxy.length;
            stop = new com.telenav.datatypes.stop.v20.RecentStop[length];
            for (int index = 0; index < length; index++)
            {
                stop[index] = convertRecentStopProxy2RecentStop(proxy[index]);
            }
        }
        return stop;
    }

    
    
    /**
     *  [Code change...]
     * 		Reason: To support ACE upgrading to 4.0, override function convertRecentStopProxy2RecentStop to support new RecentStop transform
     * 
     * 
     * Transform the data to recentSTOP backed service can understand it
     * 
     * @param proxy
     * @return
     */
    protected static com.telenav.datatypes.stop.v20.RecentStop convertRecentStopProxy2RecentStop(com.telenav.cserver.backend.datatypes.recentstopV40.RecentStop proxy)
    {
    	com.telenav.datatypes.stop.v20.RecentStop stop = null;    	 
        if (proxy == null)
        {
            return stop;
        }
        
        stop = new com.telenav.datatypes.stop.v20.RecentStop();
        stop.setOwnerUserId(proxy.getOwnerUserId());// watch out, NULL pointer
        stop.setRecentStopId(proxy.getRecentStopId());
        stop.setSyncFlag(RECENTSTOP_UNCHANGED);
        
        
        com.telenav.datatypes.content.tnpoi.v20.TnPoi poi = convertPoiProxy2Poi(proxy.getRecentPoi());
        stop.setRecentPoi(poi);
        
        
        com.telenav.datatypes.address.v40.Address addr = new com.telenav.datatypes.address.v40.Address();
        if (proxy.isPoi())
        {
            addr = AddressDataConverter.convertCSAddressToWSAddressV40(proxy.getRecentPoi().getAddress(),addr);
        	stop.setLabel(proxy.getLabel());
        }
        else
        {
            addr = AddressDataConverter.convertCSAddressToWSAddressV40(proxy.getRecentAddress(), addr);
            
            if(proxy.getRecentAddress() != null && proxy.getRecentAddress().getLabel() != null)
            {
            	stop.setLabel(proxy.getRecentAddress().getLabel());
            }
        }
        if(addr != null){
        	stop.setRecentAddress(addr);
        }
        
        return stop;
    }

   
    /**
     *  [Code change...]
     * 		Reason: To support ACE upgrading to 4.0, override function convertPoiProxy2Poi to support new RecentPoi transform
     *  
     * Transform the data to recentPOI backed RecentStop can understand it
     * 
     * @param proxy
     * @return
     */
    protected static com.telenav.datatypes.content.tnpoi.v20.TnPoi convertPoiProxy2Poi(com.telenav.cserver.backend.datatypes.TnPoi proxy)
    {
    	com.telenav.datatypes.content.tnpoi.v20.TnPoi poi = null;
    	
        if (proxy != null)
        {
            poi = new com.telenav.datatypes.content.tnpoi.v20.TnPoi();
            poi = PoiSearchConverterV20.convertCSPoiToWSTnPoi(proxy);
          
        }
        
        return poi;
    }
    
    
    /**
     * Get all the stops ids
     * 
     * @param stops
     */
    protected static long[] retrieveStopsId(com.telenav.cserver.backend.datatypes.recentstop.RecentStop[] stops)
    {
        if (stops != null)
        {
            int length = stops.length;
            long[] deleteIds = new long[stops.length];
            for (int index = 0; index < length; index++)
            {
                deleteIds[index] = stops[index].getRecentStopId();
            }
            return deleteIds;
        }
        return null;
    }

    /**
     * Set the flag whether need poi that depend on the stops in add/update list type
     * @param request
     */
    private static void setIsNeedPOI(RecentStopsRequest request) {
        com.telenav.cserver.backend.datatypes.recentstopV40.RecentStop[] addedStops=request.getAddStops();
        com.telenav.cserver.backend.datatypes.recentstopV40.RecentStop[] updateStop=request.getUpdateStops();
        if (addedStops != null)
        {
            for (int index = 0; index < addedStops.length; index++)
            {
                if (addedStops[index].isPoi())
                {// flag
                    request.setNeedPoiInfo(true);//
                    return;// if one stop is poi we will set true
                }
            }
        }
        if (!request.isNeedPoiInfo())
        {//if above set need poi, here need not check
         //but if above false, we still check here.
            if (updateStop != null)
            {
                for (int index = 0; index < updateStop.length; index++)
                {
                    if (updateStop[index].isPoi())
                    {// flag
                        request.setNeedPoiInfo(true);//
                        return;// if one stop is poi we will set true
                    }
                }
            }
        }
    }
    
    /**
	 * request converter, convert RecentStopsRequest to AllRequestDTO
	 * 		userId (long)
	 * 		addedRecentStops (recentstop20:RecentStop)
	 * 		deletedRecentStops (recentstop20:RecentStop)
	 * 		updatedRecentStops (recentstop20:RecentStop)
	 * 		needPoiInfo (boolean)
            
     * convert the request to real one
     * 
     * @param proxy
     * @return
     */
    protected static AllRequestDTO convertProxy2RecentStopRequest(RecentStopsRequest proxy)
    {
    	AllRequestDTO request = null;
    	
        if (proxy == null)
        {
            return request;
        }
        
        
        
        //set the flag whether need poi that depend on the stops in add/update list type
        setIsNeedPOI(proxy);
        //convert the request to backend one
        request = new AllRequestDTO();
        
        request.setClientName(RecentStopsServiceProxy.clientName);
        request.setClientVersion(RecentStopsServiceProxy.clientVersion);
        request.setTransactionId(RecentStopsServiceProxy.transactionId);
        request.setContextString(proxy.getContextString());
       
        
        request.setUserId(proxy.getUserId());
        request.setNeedPoiInfo(proxy.isNeedPoiInfo());
        
        request.setAddedRecentStops(RecentStopsDataConvert.convertRecentStopProxyArray2RecentStop(proxy.getAddStops()));
        request.setUpdatedRecentStops(RecentStopsDataConvert.convertRecentStopProxyArray2RecentStop(proxy.getUpdateStops()));
        request.setDeletedRecentStops(RecentStopsDataConvert.convertRecentStopProxyArray2RecentStop(proxy.getDeleteStops()));
        

        
        
        // request.setContextString(proxy.getContextString());
        
        return request;
    }

    /**
     * convert the confirm request to backend one
     * @param proxy
     * @return
     */
    protected static RecentStopsRequestDTO convertConfirmReuqestProxy2ConfirmRequest(ConfirmRequest proxy)
    {
        RecentStopsRequestDTO request = new RecentStopsRequestDTO();
        long[] confirmInsertStops = proxy.getInsertStops();//converter
        long[] confirmDeleteStops = proxy.getDeleteStops();
        int length = 0;
        ArrayList<RecentStop> confirmStops = new ArrayList<RecentStop>();
        RecentStop stops;
        if (confirmInsertStops != null)// insert stops
        {
            length += confirmInsertStops.length;
            for (int index = 0; index < confirmInsertStops.length; index++)
            {   
                stops=new RecentStop();//add poi id
                stops.setRecentStopId(confirmInsertStops[index]);
                confirmStops.add(stops);
            }
        }
        if (confirmDeleteStops != null)// delete stops
        {
            length += confirmDeleteStops.length;
            for (int j = 0; j < confirmDeleteStops.length; j++)
            {
                stops=new RecentStop();
                stops.setRecentStopId(confirmDeleteStops[j]);
                confirmStops.add(stops);
            }
        }
        request.setRecentStops(confirmStops.toArray(new RecentStop[length]));//set stops
        request.setUserId(proxy.getUserId());
        request.setClientName(RecentStopsServiceProxy.clientName);
        request.setClientVersion(RecentStopsServiceProxy.clientVersion);
        request .setTransactionId(RecentStopsServiceProxy.transactionId);
        request.setContextString(proxy.getContextString());
        return request;
    }

    /**
     * recored response status code and message
     * @param response
     * @return
     */
    protected static ConfirmResponse convertConfirmResponse2ProxyResponse(ServiceMgmtResponseDTO response)
    {
        if (response == null)
        {
            return null;
        }
        ConfirmResponse proxy = new ConfirmResponse();
        proxy.setStatusCode(response.getResponseStatus().getStatusCode());
        proxy.setStatusMessage(response.getResponseStatus().getStatusMessage());
        return proxy;
    }

   

    /**
     * recored response status code and message
     * @param response
     * @return
     */
    protected static ConfirmResponse convertConfirmResponse2ProxyResponse(BasicResponseDTO response)
    {
        if (response == null)
        {
            return null;
        }
        ConfirmResponse proxy = new ConfirmResponse();
        proxy.setStatusCode(response.getResponseStatus().getStatusCode());
        proxy.setStatusMessage(response.getResponseStatus().getStatusMessage());
        return proxy;
    }
    
    
    
    /**
     * [2011/11/22, Larry Li refactor code], keep following code during development period
     
            DeleteRequestDTO request = new DeleteRequestDTO();
            request.setUserId(userId);
            RecentStopsResponseDTO resp = testSave();
            RecentStop[] toBeUpdated = resp.getRecentStops();
            
            request.setIsDeleteAll(false);
            request.setRecentStops(toBeUpdated);
            request.setClientName(clientName);
            request.setClientVersion(clientVersion);
            request.setTransactionId("");
     
     * convert the delete request to backend one
     * @param proxy
     * @return
     */
    protected static DeleteRequestDTO convertDeleteReuqestProxy2DeleteRequest(DeleteStopsRequest proxy)
    {  
    	DeleteRequestDTO request = null;
    	
        if(proxy!=null) {
        	
            request = new DeleteRequestDTO();
            request.setIsDeleteAll(true);               // xnav provide false as testing value
            
            request.setUserId(proxy.getUserId());
            request.setClientName(RecentStopsServiceProxy.clientName);
            request.setClientVersion(RecentStopsServiceProxy.clientVersion);
            request.setTransactionId(RecentStopsServiceProxy.transactionId);
            request.setRecentStops(convertRecentStopProxyArray2RecentStop(proxy.getDelStops()));
            
            // request.setContextString(proxy.getContext()); 
        }
        return request;
    }
    
    /**
     * save delete response status code and message
     * @param response
     * @return
     */
    protected static DeleteStopsResponse convertDeleteResponse2ProxyResponse(ServiceMgmtResponseDTO response)
    {
        if (response == null)
        {
            return null;
        }
        DeleteStopsResponse proxy = new DeleteStopsResponse();
        proxy.setStatusCode(response.getResponseStatus().getStatusCode());//code
        proxy.setStatusMessage(response.getResponseStatus().getStatusMessage());
        return proxy;
    }
    
    protected static DeleteStopsResponse convertDeleteResponse2ProxyResponse(BasicResponseDTO response)
    {
        if (response == null)
        {
            return null;
        }
        DeleteStopsResponse proxy = new DeleteStopsResponse();
        proxy.setStatusCode(response.getResponseStatus().getStatusCode());//code
        proxy.setStatusMessage(response.getResponseStatus().getStatusMessage());
        return proxy;
    }
    
}
