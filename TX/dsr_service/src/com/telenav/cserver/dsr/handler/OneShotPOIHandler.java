package com.telenav.cserver.dsr.handler;

import com.telenav.cserver.dsr.ds.Command;
import com.telenav.cserver.dsr.ds.PoiQueryProcessedResult;
import com.telenav.cserver.dsr.ds.PoiSearchResultsProcessedResult;
import com.telenav.cserver.dsr.ds.ProcessedResult;
import com.telenav.cserver.dsr.ds.RecContext;
import com.telenav.cserver.dsr.ds.RecResult;
import com.telenav.cserver.dsr.framework.ProcessObject;
import com.telenav.cserver.dsr.util.StrUtil;
import com.telenav.cserver.dsr.util.ProcessUtil;
import com.telenav.cserver.dsr.util.ProcessConstants;
import com.telenav.j2me.datatypes.Stop;

/**
 * 
 * @author joses
 *
 */
public class OneShotPOIHandler extends AbstractResultsHandler {

	int processResult(RecResult recResult) {
		
		int successFlag = STATUS_SUCCESS;
		String literal = recResult.getValue();
		
		String poiName = recResult.getSlot(ProcessConstants.SLOT_POI, null);
		if (poiName == null) {
            poiName = literal;
        }
		
		PoiQueryProcessedResult result = handlePoiQueryResult(
												recResult,context,poiName);
		
		if (result == null)
			successFlag = STATUS_FAILED;
		else
			processedResults.add(result);

		if(ProcessUtil.isFromIphone(context))
			successFlag = STATUS_BREAK;
		return successFlag;
	}
	
	public static PoiQueryProcessedResult handlePoiQueryResult(
											RecResult recResult,
											RecContext context,
											String poiName)
	{
		return handlePoiQueryResult(recResult, context, poiName, null);
	}
	
	
	public static PoiQueryProcessedResult handlePoiQueryResult(
												RecResult recResult,
												RecContext context,
												String poiName,
												Command command)
	{
		PoiQueryProcessedResult procResult = new PoiQueryProcessedResult(
								recResult.getValue(), recResult.getConfidence(),command);
		
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
        	procResult.setCity(context.location.city);
        	procResult.setState(context.location.state);
        	procResult.setSearchLocation(context.location);
        }
		
		return procResult;
	}

	void postProcess() {
		// TODO Auto-generated method stub
		
	}

}
