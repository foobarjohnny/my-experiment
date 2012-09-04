package com.telenav.cserver.dsr.handler;

import com.telenav.cserver.dsr.ds.AddressProcessedResult;
import com.telenav.cserver.dsr.ds.RecResult;
import com.telenav.cserver.dsr.framework.ProcessObject;
import com.telenav.cserver.dsr.util.ProcessUtil;
import com.telenav.j2me.datatypes.Stop;

/**
 * 
 * @author joses
 *
 */
public class CityStateHandler extends AbstractResultsHandler implements IResultsHandler {

	int processResult(RecResult recResult) {
		
		int successFlag = STATUS_SUCCESS;
		String literal = recResult.getValue();
		
		Stop stop = ProcessUtil.getStop(recResult, false, context);
		
		if(stop == null){
			successFlag = STATUS_FAILED;
			return successFlag;
		}
			
		// recType, literal, stop, stop.city, stop.state
		AddressProcessedResult procResult = new AddressProcessedResult(literal, recResult.getConfidence());
		procResult.setStop(stop);
		procResult.setCity(stop.city);
		procResult.setState(stop.state);
        processedResults.add(procResult);
		return successFlag;
	}

	void postProcess() {
		// TODO Auto-generated method stub
		
	}

}
