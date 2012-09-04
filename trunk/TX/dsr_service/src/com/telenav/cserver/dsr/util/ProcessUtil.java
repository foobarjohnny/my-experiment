package com.telenav.cserver.dsr.util;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.telenav.cserver.dsr.ds.AddressProcessedResult;
import com.telenav.cserver.dsr.ds.AddressValidation;
import com.telenav.cserver.dsr.ds.Command;
import com.telenav.cserver.dsr.ds.ProcessedResult;
import com.telenav.cserver.dsr.ds.RecContext;
import com.telenav.cserver.dsr.ds.RecResult;
import com.telenav.cserver.dsr.ds.ResultType;
import com.telenav.cserver.dsr.framework.ProcessObject;
import com.telenav.cserver.dsr.util.GeoCodeUtil;
import com.telenav.cserver.dsr.util.MatchingUtil;
import com.telenav.cserver.dsr.util.ResourceConst;
import com.telenav.cserver.dsr.util.StrUtil;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.kernel.datatype.backend.teletrip.IPersonalStop;
import com.telenav.xnav.client.XNavClient;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * 
 * @author joses
 *
 */
public class ProcessUtil {

	private static Logger logger = Logger.getLogger(ProcessUtil.class.getName()) ;
	private static final int MAX_DSR_RESULT = 5;
	
	public static Stop getStop(RecResult recResult, boolean doACE, RecContext context) {
        Stop stop = new Stop();
        int id = recResult.getId();

        stop.city = recResult.getSlot(ProcessConstants.SLOT_CITY).trim().toUpperCase();
        stop.state = recResult.getSlot(ProcessConstants.SLOT_STATE).trim().toUpperCase();
        stop.stopId = Integer.toString(id);

        if (recResult.getStop() != null && StrUtil.notBlank(recResult.getStop().getCountry())) {
            stop.country = recResult.getStop().getCountry().toUpperCase();
            //TODO crack for CN
            if (stop.state.getBytes().length > stop.state.length()) {
                stop.country = "CN";
            }
        } else {
            stop.country = "US";
        }

        if (doACE || context.needsGeocoding || id == -1 || id == 0) {
            AddressValidation validation = GeoCodeUtil.validateCity(recResult, stop.country, context.tnContext);

            if (validation.isSuccess()) {
                Stop geoStop = validation.getValidatedStop().get(0);
                if (!sameState(geoStop, stop)) {
                    logger.severe("ACE result error input:" + recResult.getSlots() + "," + " output:" + geoStop.city + ", " + geoStop.state);
                    return null;
                }
                if (!context.needsGeocoding) {
                    geoStop.stopType = Stop.STOP_CITY;
                }
                geoStop.stopId = Integer.toString(id);
                return geoStop;
            } else {
                logger.severe("validateAddress fail for " + recResult.getSlots() + "," + stop.country);
                return null;
            }
        }

        return stop;
    }
	
    public static boolean isCurrentLocation(ProcessedResult result)
    {
    	return (result.getResultType() == ResultType.TYPE_ADDRESS
    			&& ProcessConstants.CURRENT_LOCATION_HEADER.equals(
    					((AddressProcessedResult) result).getAddress()));
    }
	
    public static Stop getFavoriteStop(IPersonalStop favorite) {
        Stop stop = new Stop();
        stop.lat = ConvertUtil.convert2DM5(favorite.getAddress().getLat());
        stop.lon = ConvertUtil.convert2DM5(favorite.getAddress().getLon());
        stop.label = favorite.getLabel();
        stop.firstLine = favorite.getAddress().getFirst();
        stop.city = favorite.getAddress().getCity();
        stop.state = favorite.getAddress().getProvince();
        stop.country = favorite.getAddress().getCountry();
        return stop;
    }
    
    private static boolean sameState(Stop lStop, Stop rStop) {
        return lStop != null && rStop != null && lStop.state != null && lStop.state.equals(rStop.state);
    }

	
    public static long getTransactionId(RecResult recResult) {
		long transactionId = -1;
		try {
            transactionId = Long.parseLong(recResult.getSlot(ProcessConstants.SLOT_TRANSACTION_ID, "-1"));
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "parse transactionId", ex);
        }
		return transactionId;
	}

    public static List<ProcessedResult> exactMatchingFirst(List<ProcessedResult> results) {
        int size = results.size();
        ProcessedResult exactMatchResult = results.get(size - 1);
        results.remove(exactMatchResult);
        results.add(0, exactMatchResult);
        return results;
    }

    public static boolean isDuplicateItem(ProcessedResult itemToCompare, List<ProcessedResult> results) {
        if (itemToCompare == null)
            return false;

        for (ProcessedResult item : results) {
            if (itemToCompare.equals(item))
            	return true;
        }

        return false;
    }
    

    public static List<ProcessedResult> checkResults(ProcessObject procObj, List<ProcessedResult> procResultList){
		
    	for(ProcessedResult procResult : procResultList){
    		if (StrUtil.notBlank(procResult.getLiteral())) {
    			if (procObj.getProcessedResults().size() >= MAX_DSR_RESULT)
                    break;
    			if (!isDuplicateItem(procResult, procObj.getProcessedResults()))
        			procObj.getProcessedResults().add(procResult);
    			else
                	logger.fine("ignore duplicated item");
    		}    			
    	}
    	return procObj.getProcessedResults();
    }
    
    public static boolean isFromIphone(RecContext context) {
        String platform = context.user.getPlatform();
        return platform != null && platform.equalsIgnoreCase("iphone");
    }
}
