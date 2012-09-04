package com.telenav.cserver.dsr.handler;

import java.util.List;

import com.telenav.cserver.dsr.ds.AddressProcessedResult;
import com.telenav.cserver.dsr.ds.ProcessedResult;
import com.telenav.cserver.dsr.framework.ProcessObject;
import com.telenav.cserver.dsr.util.MatchingUtil;

/**
 * 
 * @author joses
 *
 */
public class ValidateCitiesHandler implements IResultsHandler {

	public ProcessObject process(ProcessObject procObj) {

		String currentState = procObj.getContext().location.state;
		List<ProcessedResult> processedResults = procObj.getProcessedResults();
		
		for (int i = 0; i < processedResults.size(); i++) {
			for (int j = 0; j < i; j++) {
				AddressProcessedResult resi = (AddressProcessedResult) processedResults.get(i);
				AddressProcessedResult resj = (AddressProcessedResult) processedResults.get(j);
				
                if (MatchingUtil.equalStr(resi.getCity(), resj.getCity())) {
                    
                	if (MatchingUtil.equalStr(resi.getState(), currentState)) {
                    	processedResults.remove(j);
                        i--;
                        j--;
                    } else if (MatchingUtil.equalStr(resj.getState(), currentState)) {
                    	processedResults.remove(i);
                        i--;
                        break;
                    }
                }
            }
        }
		procObj.setProcessedResults(processedResults);
		return procObj;
	}

}
