/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.recentstop;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.telenav.datatypes.content.poi.v10.PoiAttributes;
import com.telenav.datatypes.stop.v10.RecentAddress;
import com.telenav.datatypes.stop.v10.RecentPoi;
import com.telenav.datatypes.stop.v10.RecentStop;
import com.telenav.kernel.ws.axis2.Axis2Helper;
import com.telenav.services.recentstop.v20.AllRequestDTO;
import com.telenav.services.recentstop.v20.AllResponseDTO;
import com.telenav.services.recentstop.v20.DeleteRequestDTO;
import com.telenav.services.recentstop.v20.QueryRequestDTO;
import com.telenav.services.recentstop.v20.RecentStopsRequestDTO;
import com.telenav.services.recentstop.v20.RecentStopsResponseDTO;
import com.telenav.ws.datatypes.address.Address;
import com.telenav.ws.datatypes.address.GeoCode;
import com.telenav.ws.datatypes.services.mgmt.ServiceMgmtResponseDTO;

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
        if (response != null)
        {
            QueryResponse proxy = new QueryResponse();
            proxy.setStatusCode(Integer.valueOf(response.getResponseStatus().getStatusCode()));
            proxy.setStatusMessage(response.getResponseStatus().getStatusMessage());
            proxy.setStops(convertRecentStopArray2RecentStopProxy(response.getRecentStops()));
            return proxy;
        }
        return null;
    }

    /**
     * Convert the query request to the real one
     * 
     * @param request
     * @return
     */
    public static QueryRequestDTO convertProxy2QueryRequest(QueryRequest proxy)
    {
        if (proxy != null)
        {
            QueryRequestDTO request = new QueryRequestDTO();
            request.setUserId(proxy.getUserId());
            request.setCount(proxy.getMaxResult());
            request.setContextString(proxy.getContext());
            request.setClientName(RecentStopsServiceProxy.clientName);// can not be null
            request.setClientVersion(RecentStopsServiceProxy.clientVersion);
            request.setTransactionId(RecentStopsServiceProxy.transactionId);
            //QueryRequestDTO is used to fetch places contain all stops and poi.
            //so this flag should be always true.
            request.setNeedPoiInfo(true);
            return request;
        }
        return null;
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
        com.telenav.cserver.backend.datatypes.recentstop.RecentStop[] insertStop = convertRecentStopArray2RecentStopProxy(recentInsertedStop);
        RecentStop[] recentDeletedStop = rsResponse.getDeletedRecentStops();// just contain stop id
        com.telenav.cserver.backend.datatypes.recentstop.RecentStop[] deletedStop = convertRecentStopArray2RecentStopProxy(recentDeletedStop);
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
    protected static com.telenav.cserver.backend.datatypes.recentstop.RecentStop convertRecentStop2RecentStopProxy(RecentStop recentStop)
    {
        if (recentStop == null)
        { // check the parameter
            return null;
        }
        com.telenav.cserver.backend.datatypes.recentstop.RecentStop stopProxy = new com.telenav.cserver.backend.datatypes.recentstop.RecentStop();
        // You can get RecentStop.getRecentPoi() to tell whether this recentstop is a poi or not.
        // If null is returned, the recentstop is not a poi stop.
        if (recentStop.getRecentPoi() != null)
        {
            stopProxy.setPoi(true);
            com.telenav.cserver.backend.datatypes.recentstop.RecentPoi poiProxy = convertPoi2PoiProxy(recentStop.getRecentPoi());
            stopProxy.setRecentPoi(poiProxy);
        }
        stopProxy.setOwnerUserId(recentStop.getOwnerUserId());
        stopProxy.setRecentStopId(recentStop.getRecentStopId());
        // convert the address to proxy type
        com.telenav.cserver.backend.datatypes.recentstop.RecentAddress addrProxy = convertRecentAddress2AddressProxy(recentStop
                .getRecentAddress());
        stopProxy.setRecentAddress(addrProxy);
        if (recentStop.getRecentPoi() != null)
        {
            stopProxy.setPoi(true);
        }
        return stopProxy;
    }

    /**
     * Convert poi to PROCY type we almost concern all the element
     * 
     * @param poi
     * @return
     */
    private static com.telenav.cserver.backend.datatypes.recentstop.RecentPoi convertPoi2PoiProxy(RecentPoi poi)
    {
        com.telenav.cserver.backend.datatypes.recentstop.RecentPoi poiProxy = new com.telenav.cserver.backend.datatypes.recentstop.RecentPoi();
        RecentAddress recentAddr = (RecentAddress) poi.getAddress();// recentAddress extends address
        poiProxy.setAddress(convertRecentAddress2AddressProxy(recentAddr));
        poiProxy.setId(Long.valueOf(poi.getId()));
        poiProxy.setName(poi.getName());
        poiProxy.setPoiRating(poi.getPoiRating());
        if (poi.getAttributes() != null)
        {
            poiProxy.setLabel(poi.getAttributes().getLabel());
            poiProxy.setOtherDescription(poi.getAttributes().getOtherDescription());
            poiProxy.setPhoneNumber(poi.getAttributes().getPhoneNumber());
            poiProxy.setPriceRange(poi.getAttributes().getPriceRange());
            poiProxy.setImageUrl(poi.getAttributes().getImageUrl());
            poiProxy.setEmail(poi.getAttributes().getEmail());
            poiProxy.setFaxNumber(poi.getAttributes().getFaxNumber());
            poiProxy.setHours(poi.getAttributes().getHours());
            poiProxy.setLogo(poi.getAttributes().getLogo());
            poiProxy.setMessage(poi.getAttributes().getMessage());
            poiProxy.setWapUrl(poi.getAttributes().getWapUrl());
            poiProxy.setWebUrl(poi.getAttributes().getWebUrl());
            poiProxy.setAcceptedPaymentMethods(poi.getAttributes().getAcceptedPaymentMethods());
            poiProxy.setBrandName(poi.getAttributes().getBrandName());
        }
        poiProxy.setPoiRatingOfCurrentUser(poi.getPoiRatingOfCurrentUser());
        return poiProxy;
    }

    /**
     * Convert the address to PROXY type
     * 
     * @param addr
     * @return
     */
    private static com.telenav.cserver.backend.datatypes.recentstop.RecentAddress convertRecentAddress2AddressProxy(RecentAddress addr)
    {
        if (addr != null)
        { // save all attributes
            com.telenav.cserver.backend.datatypes.recentstop.RecentAddress addrProxy = new com.telenav.cserver.backend.datatypes.recentstop.RecentAddress();
            addrProxy.setAddressId(addr.getAddressId());
            addrProxy.setCity(addr.getCity());
            addrProxy.setCountry(addr.getCountry());
            addrProxy.setCounty(addr.getCounty());
            addrProxy.setFirstLine(addr.getFirstLine());
            addrProxy.setLabel(addr.getLabel());
            addrProxy.setLatitude(addr.getGeoCode().getLatitude());
            addrProxy.setLongitude(addr.getGeoCode().getLongitude());
            addrProxy.setPostalCode(addr.getPostalCode());
            addrProxy.setState(addr.getState());
            addrProxy.setCrossStreetName(addr.getCrossStreetName());
            addrProxy.setIntersection(addr.getIsIntersection());
            addrProxy.setLandmark(addr.getLandmark());
            addrProxy.setLastLine(addr.getLastLine());
            addrProxy.setPoBox(addr.getPoBox());
            addrProxy.setPostalCodePlus4(addr.getPostalCodePlus4());
            addrProxy.setStreetName(addr.getStreetName());
            addrProxy.setStreetNumber(addr.getStreetNumber());
            addrProxy.setStreetNumberHigh(addr.getStreetNumberHigh());
            addrProxy.setStreetNumberLow(addr.getStreetNumberLow());
            addrProxy.setSuite(addr.getSuite());
            return addrProxy;
        }
        return null;
    }

    /**
     * Convert RecentStop array To RecentStop Property array
     * 
     * @param stopProperty
     * @return
     */
    protected static com.telenav.cserver.backend.datatypes.recentstop.RecentStop[] convertRecentStopArray2RecentStopProxy(
            RecentStop[] recentStop)
    { // webservice enclose the null object as array, the first index will refer to null ,xis2Helper.isNonEmptyArray
        // check it
        if (recentStop != null && Axis2Helper.isNonEmptyArray(recentStop))
        {
            int length = recentStop.length;
            com.telenav.cserver.backend.datatypes.recentstop.RecentStop[] arrRecentStop = new com.telenav.cserver.backend.datatypes.recentstop.RecentStop[length];
            for (int index = 0; index < length; index++)
            {
                arrRecentStop[index] = convertRecentStop2RecentStopProxy(recentStop[index]);
            }
            return arrRecentStop;
        }
        return null;
    }

    /**
     * Transform the data to recentSTOP(Array)
     * 
     * @param proxy []
     * @return
     */
    protected static RecentStop[] convertRecentStopProxyArray2RecentStop(com.telenav.cserver.backend.datatypes.recentstop.RecentStop[] proxy)
    {
        if (proxy != null && Axis2Helper.isNonEmptyArray(proxy))
        {
            int length = proxy.length;
            RecentStop[] stop = new RecentStop[length];
            for (int index = 0; index < length; index++)
            {
                stop[index] = convertRecentStopProxy2RecentStop(proxy[index]);
            }
            return stop;
        }
        return null;
    }

    /**
     * Transform the data to recentSTOP backed service can understand it
     * 
     * @param proxy
     * @return
     */
    protected static RecentStop convertRecentStopProxy2RecentStop(com.telenav.cserver.backend.datatypes.recentstop.RecentStop proxy)
    {
        if (proxy == null)
        {
            return null;
        }
        RecentStop stop = new RecentStop();
        stop.setOwnerUserId(proxy.getOwnerUserId());// watch out, NULL pointer
        stop.setRecentStopId(proxy.getRecentStopId());
        RecentPoi poi = convertPoiProxy2Poi(proxy.getRecentPoi());
        stop.setRecentPoi(poi);
        RecentAddress addr;
        if (proxy.isPoi())
        {
            addr = convertAddrProxy2Addr(proxy.getRecentPoi().getAddress());
            addr.setLabel(proxy.getRecentPoi().getLabel());
        }
        else
        {
            addr = convertAddrProxy2Addr(proxy.getRecentAddress());
        }
        stop.setSyncFlag(RECENTSTOP_UNCHANGED);
        stop.setRecentAddress(addr);
        return stop;
    }

    /**
     * Transform the addr proxy to address
     * 
     * @param proxy
     * @return
     */
    private static RecentAddress convertAddrProxy2Addr(com.telenav.cserver.backend.datatypes.recentstop.RecentAddress proxy)
    {
        if (proxy != null)
        {
            RecentAddress addr = new RecentAddress();
            addr.setPostalCode(proxy.getPostalCode());
            addr.setCity(proxy.getCity());
            addr.setState(proxy.getState());
            addr.setFirstLine(proxy.getFirstLine());
            addr.setCountry(proxy.getCountry());
            addr.setCounty(proxy.getCounty());
            addr.setLabel(proxy.getLabel());
            GeoCode geo = new GeoCode();
            geo.setLatitude(proxy.getLatitude());
            geo.setLongitude(proxy.getLongitude());
            addr.setGeoCode(geo);
            return addr;
        }
        return null;
    }

    /**
     * Transform the data to recentPOI backed RecentStop can understand it
     * 
     * @param proxy
     * @return
     */
    protected static RecentPoi convertPoiProxy2Poi(com.telenav.cserver.backend.datatypes.recentstop.RecentPoi proxy)
    {
        if (proxy != null)
        {
            RecentPoi poi = new RecentPoi();
            poi.setId(String.valueOf(proxy.getId()));// to STRING
            poi.setName(proxy.getName());
            poi.setPoiRating(proxy.getPoiRating());
            poi.setPoiRatingOfCurrentUser(proxy.getPoiRatingOfCurrentUser());
            Address addr = convertAddressProxy2Addrss(proxy.getAddress());
            poi.setAddress(addr);
            PoiAttributes attr = newPoiAttributes(proxy);
            poi.setAttributes(attr);
            return poi;
        }
        return null;
    }

    /**
     * Build up new poiAttribute object Please check the attribute of the poi, we trying to save all infos
     * 
     * @param proxy
     * @return
     */
    private static PoiAttributes newPoiAttributes(com.telenav.cserver.backend.datatypes.recentstop.RecentPoi proxy)
    {
        if (proxy == null)
        {
            return null;
        }
        PoiAttributes atti = new PoiAttributes();
        atti.setAcceptedPaymentMethods(proxy.getAcceptedPaymentMethods());
        atti.setBrandName(proxy.getName());
        atti.setEmail(proxy.getEmail());
        atti.setFaxNumber(proxy.getFaxNumber());
        atti.setHours(proxy.getHours());
        atti.setImageUrl(proxy.getImageUrl());
        atti.setLogo(proxy.getLogo());
        atti.setMessage(proxy.getMessage());
        atti.setWapUrl(proxy.getWapUrl());
        atti.setWebUrl(proxy.getWebUrl());
        atti.setOtherDescription(proxy.getOtherDescription());
        atti.setPhoneNumber(proxy.getPhoneNumber());
        atti.setPriceRange(proxy.getPriceRange());
        atti.setLabel(proxy.getLabel());
        return atti;

    }

    /**
     * Transform the data to recentAddress backed Address can understand it
     * 
     * @param addrPro
     * @return
     */
    private static Address convertAddressProxy2Addrss(com.telenav.cserver.backend.datatypes.recentstop.RecentAddress addrPro)
    {
        if (addrPro != null)
        {
            Address addr = new Address(); // On address start
            addr.setAddressId(addrPro.getAddressId());
            addr.setCity(addrPro.getCity());
            addr.setCountry(addrPro.getCountry());
            addr.setCounty(addrPro.getCounty());
            addr.setFirstLine(addrPro.getFirstLine());
            addr.setLabel(addrPro.getLabel());
            addr.setPostalCode(addrPro.getPostalCode());
            addr.setState(addrPro.getState());
            addr.setCrossStreetName(addrPro.getCrossStreetName());
            GeoCode geo = new GeoCode();
            geo.setLatitude(addrPro.getLatitude());
            geo.setLongitudeError(addrPro.getLongitude());
            addr.setGeoCode(geo);
            addr.setIsIntersection(addrPro.isIntersection());
            addr.setLandmark(addrPro.getLandmark());
            addr.setLastLine(addrPro.getLastLine());
            addr.setPoBox(addrPro.getPoBox());
            addr.setPostalCode(addrPro.getPostalCode());
            addr.setStreetName(addrPro.getStreetName());
            addr.setStreetNumber(addrPro.getStreetNumber());
            addr.setStreetNumberHigh(addrPro.getStreetNumberHigh());//
            addr.setStreetNumberLow(addrPro.getStreetNumberLow());//
            addr.setSuite(addrPro.getSuite());//
            return addr;
        }
        return null;
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
        com.telenav.cserver.backend.datatypes.recentstop.RecentStop[] addedStops=request.getAddStops();
        com.telenav.cserver.backend.datatypes.recentstop.RecentStop[] updateStop=request.getUpdateStops();
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
     * convert the request to real one
     * 
     * @param proxy
     * @return
     */
    protected static AllRequestDTO convertProxy2RecentStopRequest(RecentStopsRequest proxy)
    {
        if (proxy == null)
        {// check the parameter
            return null;
        }
        //set the flag whether need poi that depend on the stops in add/update list type
        setIsNeedPOI(proxy);
        //convert the request to backend one
        AllRequestDTO request = new AllRequestDTO();
        request.setAddedRecentStops(RecentStopsDataConvert.convertRecentStopProxyArray2RecentStop(proxy.getAddStops()));
        request.setUpdatedRecentStops(RecentStopsDataConvert.convertRecentStopProxyArray2RecentStop(proxy.getUpdateStops()));
        request.setDeletedRecentStops(RecentStopsDataConvert.convertRecentStopProxyArray2RecentStop(proxy.getDeleteStops()));
        request.setUserId(proxy.getUserId());
        request.setContextString(proxy.getContextString());
        request.setNeedPoiInfo(proxy.isNeedPoiInfo());
        request.setClientName(RecentStopsServiceProxy.clientName);
        request.setClientVersion(RecentStopsServiceProxy.clientVersion);
        request.setTransactionId(RecentStopsServiceProxy.transactionId);
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
     * convert the delete request to backend one
     * @param proxy
     * @return
     */
    protected static DeleteRequestDTO convertDeleteReuqestProxy2DeleteRequest(DeleteStopsRequest proxy)
    {  
        if(proxy!=null) {
            DeleteRequestDTO request = new DeleteRequestDTO();
            request.setUserId(proxy.getUserId());
            request.setContextString(proxy.getContext());
            request.setClientName(RecentStopsServiceProxy.clientName);
            request.setClientVersion(RecentStopsServiceProxy.clientVersion);
            request.setTransactionId(RecentStopsServiceProxy.transactionId);
            request.setRecentStops(convertRecentStopProxyArray2RecentStop(proxy.getDelStops()));
            return request;
        }
        return null;
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
}
