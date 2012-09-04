package com.telenav.cserver.dsr.util;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.backend.ace.GeoCodeResponse;
import com.telenav.cserver.backend.ace.GeoCodeResponseStatus;
import com.telenav.cserver.backend.ace.GeoCodingProxy;
import com.telenav.cserver.backend.cose.AirportSearchRequest;
import com.telenav.cserver.backend.cose.CoseFactory;
import com.telenav.cserver.backend.cose.PoiSearchProxy;
import com.telenav.cserver.backend.cose.PoiSearchRequest;
import com.telenav.cserver.backend.cose.PoiSearchResponse;
import com.telenav.cserver.backend.datatypes.Address;
import com.telenav.cserver.backend.datatypes.ErrorCode;
import com.telenav.cserver.backend.datatypes.TnPoi;
import com.telenav.cserver.backend.datatypes.ace.GeoCodeSubStatus;
import com.telenav.cserver.backend.datatypes.ace.GeoCodedAddress;
import com.telenav.cserver.dsr.ds.AddressValidation;
import com.telenav.cserver.dsr.ds.RecContext;
import com.telenav.cserver.dsr.ds.RecResult;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.j2me.datatypes.BizPOI;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.kernel.util.datatypes.TnContext;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: llzhang
 * Date: 2009-9-21
 * Time: 13:20:23
 */
public class GeoCodeUtil {

    private static Logger logger = Logger.getLogger(GeoCodeUtil.class.getName());

    public static boolean isExact(GeoCodeResponse response) {
        if (response == null) {
            return false;
        }

        if (response.getStatus().getStatusCode() == GeoCodeResponseStatus.EXACT_FOUND) {
            if (response.getMatchCount() == 1) {
                GeoCodeSubStatus subStatus = response.getMatches().get(0).getSubStatus();
                return !subStatus.isSubstatus_DOOR_CHANGED();
            }
        }

        return false;
    }


    private static Address getAddress(RecResult result) {
        Address address = new Address();

        //if not city/state slot value, use city/state in stop from near by city/state 
        address.setCityName(result.getSlot(ProcessConstants.SLOT_CITY));
        if (address.getCityName().length() == 0 && result.getStop().getCity() != null) {
            address.setCityName(result.getStop().getCity());
        }

        address.setState(result.getSlot(ProcessConstants.SLOT_STATE));
        if (address.getState().length() == 0 && result.getStop().getState() != null) {
            address.setState(result.getStop().getState());
        }

        String country = "US";
        if (result.getStop() != null && StrUtil.notBlank(result.getStop().getCountry())) {
            country = result.getStop().getCountry().toUpperCase();
        }
        address.setCountry(country);

        String firstLine = result.getSlot(ProcessConstants.SLOT_ADDRESS, null);

        if (firstLine != null)
            address.setFirstLine(firstLine);

        return address;
    }

    private static Address getAddress(Stop stop) {
        Address address = new Address();
        address.setFirstLine(stop.firstLine);
        address.setCountry(stop.country);
        address.setState(stop.state);
        address.setCityName(stop.city);
        address.setPostalCode(stop.zip);
        address.setLatitude(stop.lat);
        address.setLongitude(stop.lon);
        return address;
    }

    public static AddressValidation validateAddress(RecResult recResult, TnContext tnContext) {
        GeoCodingProxy proxy = GeoCodingProxy.getInstance(tnContext);
        Address address = getAddress(recResult);
        return validateAddress(proxy, address);
    }

    public static AddressValidation validateAddress(Stop stop, TnContext tnContext) {
        GeoCodingProxy proxy = GeoCodingProxy.getInstance(tnContext);
        Address address = getAddress(stop);
        return validateAddress(proxy, address);
    }
//
//    public static AddressValidation validataAddress(String firstLine, String country, TnContext tnContext) {
//        GeoCodingProxy proxy = GeoCodingProxy.getInstance(tnContext);
//        Address address = getAddress(firstLine, country);
//        return validataAddress(proxy, address);
//    }

    public static AddressValidation validateCity(RecResult recResult, String country, TnContext tnContext) {
        GeoCodingProxy proxy = GeoCodingProxy.getInstance(tnContext);
        Address address = new Address();
        address.setCityName(recResult.getSlot(ProcessConstants.SLOT_CITY));
        address.setState(recResult.getSlot(ProcessConstants.SLOT_STATE));
        address.setCountry(country);
        return validateAddress(proxy, address);
    }
    
    public static AddressValidation validateCity(String city, String state, String country, TnContext tnContext) {
        GeoCodingProxy proxy = GeoCodingProxy.getInstance(tnContext);
        Address address = new Address();
        address.setCityName(city);
        address.setState(state);
        address.setCountry(country);
        return validateAddress(proxy, address);
    }
    
//
//    private static Address getAddress(String firstLine, String country) {
//        Address address = new Address();
//        address.setFirstLine(firstLine);
//        address.setCountry(country);
//        return address;
//    }

    private static AddressValidation validateAddress(GeoCodingProxy geoCodingProxy, Address address) {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("GeoCodeUtil.validateAddress");

        GeoCodeResponse response = null;
        try {
        	logger.fine("geoCodingProxy Coding : firstline = " + address.getFirstLine() +
                    " city = " + address.getCityName() + " state = " + address.getState());
            response = geoCodingProxy.geoCode(address);
        } catch (ThrottlingException e) {
            logger.severe("Geo Coding Failed.");
            cli.addData(CliConstants.LABEL_ERROR, "validate address error : firstline = " + address.getFirstLine());
        }

        List<Stop> stops = new ArrayList<Stop>();
        int status = AddressValidation.DEFAULT;

        if (response != null) {
            logger.fine("Geo Coding Status : status = " + response.getStatus().getStatusCode());
            if (!response.getStatus().isSuccessful()) {
                cli.addData(CliConstants.LABEL_ERROR, "validate address error:"
                        + response.getStatus().getStatusCode() + " firstline = " + address.getFirstLine());
                logger.severe("validate address error:"
                        + response.getStatus().getStatusCode() + " firstline = " + address.getFirstLine());
            }
            else if (response.getMatches() != null) {
                for (GeoCodedAddress geoCodedAddress : response.getMatches()) {
                    stops.add(ConvertUtil.convert2Stop(geoCodedAddress));
                }
                status = isExact(response) ? AddressValidation.IS_EXACT : AddressValidation.DEFAULT;
            }
        }
        cli.complete();
        return new AddressValidation(stops, status);
    }

    public static List<TnPoi> searchPoi(String poiName, Stop location, RecContext recContext, boolean searchNearby) {

    	CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("GeoCodeUtil.searchPoi");
        
        PoiSearchProxy poiSearchProxy = CoseFactory.createPoiSearchProxy(recContext.tnContext);
        PoiSearchResponse result = null;
        try {
            if (searchNearby) {
            	
            	PoiSearchRequest poiReq = constructRequest(recContext.location, recContext);
                poiReq.setPoiQuery(poiName);

                logger.fine("Do poi COSE within distance, poi = " + poiName);
                result = poiSearchProxy.searchWithinDistance(poiReq);
            } else {
                logger.fine("Do poi COSE within city, poi = " + poiName + " city = " + location);
                PoiSearchRequest poiReq = constructRequest(location, recContext);
                poiReq.setPoiQuery(poiName);
                result = poiSearchProxy.searchWithinCity(poiReq);
            }
        } catch (Exception e) {
            logger.severe("Poi COSE failed ! poi = " + poiName);
            cli.addData(CliConstants.LABEL_ERROR, "POI CoSE failed; POI = "+poiName);
        }

        
        if (result != null && result.getPoiSearchStatus() == ErrorCode.POI_STATUS_SUCCESS)
        {
        	List<TnPoi> results = new ArrayList<TnPoi>();
            for (TnPoi tnPoi : result.getPois())
            {
                Stop poi = ConvertUtil.convert2Stop(tnPoi);
                if (poi != null)
                {
                    results.add(tnPoi);
                }
            }
            cli.addData(CliConstants.LABEL_RESULT, results.toString());
            cli.complete();
            return results;
        }
        else
        {
        	logger.severe("Poi search failed with status: " + result.getPoiSearchStatus());
        	cli.addData(CliConstants.LABEL_ERROR, "POI search failed; POI = "+poiName);
        	cli.complete();
        	return null;
        }
    }

    private static PoiSearchRequest constructRequest(Stop searchLocation, RecContext recContext) {
        long nCatID = BizPOI.NO_GROUP;
        int maxResults = 10;
        int radius = 7000;
        boolean isMostPopular = false;
        int SORT_BY_RELEVANCE = 3;
        String categoryVersion = "YP50";
        long hierarchy = 1;

        Address address = ConvertUtil.convert2Address(searchLocation);
        UserProfile user = recContext.user;
        long userId = 0;
        try{
            userId = Long.parseLong(user.getUserId()); 
        }catch(Exception ignored){
        }

        PoiSearchRequest poiReq = new PoiSearchRequest();
        poiReq.setRegion(user.getRegion());
        poiReq.setAnchor(address);
        poiReq.setCategoryId(nCatID);
        poiReq.setCategoryVersion(categoryVersion);
        poiReq.setHierarchyId(hierarchy);
        poiReq.setNeedUserPreviousRating(false);
        poiReq.setPoiSortType(SORT_BY_RELEVANCE);
        poiReq.setRadiusInMeter(radius);
        poiReq.setUserId(userId);
        poiReq.setPageNumber(0);
        poiReq.setPageSize(maxResults);
        poiReq.setOnlyMostPopular(isMostPopular);
        poiReq.setAutoExpandSearchRadius(true);
        poiReq.setNeedUserGeneratePois(true);
        poiReq.setNeedSponsoredPois(false);

        return poiReq;
    }

    public static List<TnPoi> validateAirport(String airportCode, TnContext context)
    {
    	List<TnPoi> results = new ArrayList<TnPoi>() ;
    	
        AirportSearchRequest req = new AirportSearchRequest();
        req.setTransactionId("");
        req.setCountryList("");
        
        StringTokenizer st = new StringTokenizer(airportCode, ",");
        while(st.hasMoreTokens()){
        	req.setAirportQuery(st.nextToken());
        	
            CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
            cli.setFunctionName("GeoCodeUtil.validateAirport");

            try
            {
    	        PoiSearchProxy proxy = CoseFactory.createPoiSearchProxy(context);
    	        PoiSearchResponse resp = null;
    	
    	        try {
    	            logger.fine("validate Airport : code = " + airportCode);
    	            resp = proxy.searchAirport(req);
    	        } catch (Exception e) {
    	            logger.log(Level.SEVERE, "Geo Coding Failed.", e);
    	            cli.addData(CliConstants.LABEL_ERROR, "validate airport error : code = " + airportCode);
    	        }

    	        if (resp != null && resp.getPois() != null) {
    	            for (TnPoi tnPoi : resp.getPois()) {
    	            	if (tnPoi != null && tnPoi.getAddress() != null){
    	            		results.add(tnPoi);
    	            		break;
    	            	}
    	            }
    	        } else {
    	            logger.severe("Airport Geo Coding Recieve no results.");
    	        }
            }
            catch (Exception e)
            {
            	logger.severe("Exception while doing airport search: "+e);
            	return null;
            }
            finally
            {
            	cli.complete();
            }
        }
        return results;  
    }
    
    public static AddressValidation validatePoi(String poiName, RecContext recContext) {

        PoiSearchRequest poiReq = constructRequest(recContext);
        poiReq.setPoiQuery(poiName);

        PoiSearchProxy poiSearchProxy = CoseFactory.createPoiSearchProxy(recContext.tnContext);
        PoiSearchResponse result = null;
        try {
            if (searchWithLat(recContext.location)) {
                logger.fine("Do poi COSE within distance, poi = " + poiName);
                result = poiSearchProxy.searchWithinDistance(poiReq);
            } else {
                logger.fine("Do poi COSE within city, poi = " + poiName + " city = " + recContext.location.city);
                result = poiSearchProxy.searchWithinCity(poiReq);
            }
        } catch (Exception e) {
            logger.severe("Poi COSE failed ! poi = " + poiName);
        }

        AddressValidation validation = new AddressValidation();
        if (result != null && result.getPoiSearchStatus() == ErrorCode.POI_STATUS_SUCCESS) {
            for (TnPoi tnPoi : result.getPois()) {
                Stop poi = ConvertUtil.convert2Stop(tnPoi);
                if (poi != null) {
                	//poi.firstLine = tnPoi.getAddress().getFirstLine(); //DSR-19
                    validation.addStop(poi);
                    return validation;
                }
            }
        }
        return validation;
    }
    
    private static PoiSearchRequest constructRequest(RecContext recContext) {
        long nCatID = BizPOI.NO_GROUP;
        int maxResults = 10;
        int radius = 7000;
        boolean isMostPopular = false;
        int SORT_BY_RELEVANCE = 3;
        String categoryVersion = "YP50";
        long hierarchy = 1;

        Address address = ConvertUtil.convert2Address(recContext.location);
        UserProfile user = recContext.user;
        long userId = 0;
        try{
            userId = Long.parseLong(user.getUserId()); 
        }catch(Exception ignored){
        }

        PoiSearchRequest poiReq = new PoiSearchRequest();
        poiReq.setRegion(user.getRegion());
        poiReq.setAnchor(address);
        poiReq.setCategoryId(nCatID);
        poiReq.setCategoryVersion(categoryVersion);
        poiReq.setHierarchyId(hierarchy);
        poiReq.setNeedUserPreviousRating(false);
        poiReq.setPoiSortType(SORT_BY_RELEVANCE);
        poiReq.setRadiusInMeter(radius);
        poiReq.setUserId(userId);
        poiReq.setPageNumber(0);
        poiReq.setPageSize(maxResults);
        poiReq.setOnlyMostPopular(isMostPopular);
        poiReq.setAutoExpandSearchRadius(true);
        poiReq.setNeedUserGeneratePois(true);
        poiReq.setNeedSponsoredPois(false);

        return poiReq;
    }

    private static boolean searchWithLat(Stop stop) {
        return stop.lat != 0 && stop.lon != 0;
    }

}
