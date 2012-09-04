package com.telenav.cserver.dsr.handler;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.telenav.cserver.dsr.ds.RecResult;
import com.telenav.cserver.dsr.framework.ProcessObject;
import com.telenav.cserver.dsr.util.MatchingUtil;
import com.telenav.cserver.dsr.util.ProcessConstants;

/**
 * 
 * @author joses
 *
 */
public class CCFilterHandler implements IResultsHandler {

	private static final Logger logger = Logger.getLogger(CCFilterHandler.class.getName());
	public ProcessObject process(ProcessObject procObj) {
		
		logger.log(Level.FINE, "Entering CCFilter");
		RecResult[] recItems = procObj.rawResults;
		
		
		if(recItems.length <= 1){
			logger.log(Level.FINE, "Not enough # of results for filtering");
			return procObj;
		}
			
		RecResult[] retItems = null;
        if (recItems[0].getConfidence() > recItems[1].getConfidence()) {
            //top result's confidence is the highest, select it
        	retItems = new RecResult[]{recItems[0]};
            logger.log(Level.FINE, "Selecting the result with higher confidence "+recItems[0].getValue());
        } else {
            //there are multiple results with the same confidence
            int i = 0;
            int retSize = 0;
            retItems = new RecResult[recItems.length];
            
            //scan through to find mystuff
            while (i < recItems.length && recItems[i].getConfidence() == recItems[0].getConfidence()) {
            	logger.log(Level.FINE, "Scanning through MYSTUFF SLOT "+recItems[i].getValue());
                if (recItems[i].getSlot(ProcessConstants.SLOT_COMMAND_MYSTUFF).length() > 0) {
                	retItems[retSize++] = recItems[i];
                    break;
                }
                i++;
            }
            
            if (retSize == 0) {
                //scan through to find favorite
                i = 0;
                while (i < recItems.length && recItems[i].getConfidence() == recItems[i].getConfidence()) {
                	logger.log(Level.FINE, "Scanning through FAVORITE SLOT "+recItems[i]);
                    if (recItems[i].getSlot(ProcessConstants.SLOT_COMMAND_FAVORITE).length() > 0) {
                    	retItems[retSize++] = recItems[i];
                        break;
                    }
                    i++;
                }
            }

            if (retSize == 0) {
                //return multiple RecResults when there are address results
                i = 0;
                while (i < recItems.length && recItems[i].getConfidence() == recItems[0].getConfidence()) {
                	logger.log(Level.FINE, "Returning multiple results in the case of address "+recItems[i].getValue());
                    if ((ProcessConstants.STREETS_TYPE.equalsIgnoreCase(recItems[i].getSlot(ProcessConstants.SLOT_TYPE))
                            || ProcessConstants.XSTREET_TYPE.equalsIgnoreCase(recItems[i].getSlot(ProcessConstants.SLOT_TYPE)))
                            && MatchingUtil.looksLikeAddress(recItems[i].getSlot(ProcessConstants.SLOT_ADDRESS))) {
                    	retItems[retSize++] = recItems[i];
                    }
                    i++;
                }
            }
            if (retSize == 0) {
                //since we can't find a valid address, return the top POI result
                i = 0;
                while (i < recItems.length && recItems[i].getConfidence() == recItems[0].getConfidence()) {
                	logger.log(Level.FINE, "Scanning through for POI "+recItems[i].getValue());
                    if (ProcessConstants.POI_TYPE.equalsIgnoreCase(recItems[i].getSlot(ProcessConstants.SLOT_TYPE))) {
                    	retItems[retSize++] = recItems[i];
                        break;
                    }
                    i++;
                }
            }
            if (retSize == 0) {
                //no valid address and POI, return the top
            	retItems[retSize++] = recItems[0];
            }
            
            RecResult[] trimmed = new RecResult[retSize];
            System.arraycopy(retItems, 0, trimmed, 0, retSize);
            retItems = trimmed;
        }
        
        procObj.setRawResults(retItems);
        
        return procObj;
	}

}
