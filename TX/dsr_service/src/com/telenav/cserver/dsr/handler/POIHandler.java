package com.telenav.cserver.dsr.handler;

import java.util.logging.Logger;

import com.telenav.cserver.dsr.ds.Command;
import com.telenav.cserver.dsr.ds.PoiQueryProcessedResult;
import com.telenav.cserver.dsr.ds.RecContext;
import com.telenav.cserver.dsr.ds.RecResult;
import com.telenav.cserver.dsr.framework.ProcessObject;

import com.telenav.cserver.dsr.util.ProcessConstants;
import com.telenav.cserver.dsr.util.ProcessUtil;
import com.telenav.cserver.dsr.util.StrUtil;
import com.telenav.j2me.datatypes.Stop;

/**
 * 
 * @author joses
 *
 */
public class POIHandler extends AbstractResultsHandler implements IResultsHandler{

	private static Logger logger = Logger.getLogger(POIHandler.class.getName());
	public ProcessObject process(ProcessObject procObj) {

		return super.process(procObj);
	}

	int processResult(RecResult recResult) {
		
		int successFlag = STATUS_SUCCESS;
		String literal = recResult.getValue();
		
		// recType, literal, location
		PoiQueryProcessedResult result = handlePoiQueryResult(recResult,context, literal);

		if (result == null)
			successFlag = STATUS_FAILED;
		else{
			processedResults.add(result);
			successFlag = STATUS_BREAK;
		}
		
		if(ProcessUtil.isFromIphone(context))
			successFlag = STATUS_BREAK;
		return successFlag;
	}
	
	
	public static PoiQueryProcessedResult handlePoiQueryResult(
												RecResult recResult,
												RecContext context,
												String poiName)
	{
		PoiQueryProcessedResult procResult = new PoiQueryProcessedResult(
								recResult.getValue(), recResult.getConfidence(), Command.SEARCH);
			
		procResult.setName(poiName);
		String city = recResult.getSlot(ProcessConstants.SLOT_CITY, null);
		
		if (StrUtil.notBlank(city)) {
			procResult.setCity(city);
			procResult.setState(recResult.getSlot(ProcessConstants.SLOT_STATE, null));
            Stop stop = ProcessUtil.getStop(recResult, true, context);

    		
            if (stop == null) {
                return null;
            }
            
            //stop.label = poiName;
            procResult.setSearchLocation(stop);
            
        } else {
        	procResult.setSearchLocation(context.location);
        }
		
		return procResult;
	}

	void postProcess() {
		// TODO Auto-generated method stub
		
	}

}
