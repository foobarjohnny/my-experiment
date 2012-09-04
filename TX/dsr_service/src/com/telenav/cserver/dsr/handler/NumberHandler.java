package com.telenav.cserver.dsr.handler;

import java.util.logging.Logger;

import com.telenav.cserver.dsr.ds.ListNumberProcessedResult;
import com.telenav.cserver.dsr.ds.ProcessedResult;
import com.telenav.cserver.dsr.ds.RecResult;
import com.telenav.cserver.dsr.framework.ProcessObject;
import com.telenav.cserver.dsr.util.ProcessConstants;

/**
 * 
 * @author joses
 *
 */
public class NumberHandler extends AbstractResultsHandler {

	private static Logger logger = Logger.getLogger(NumberHandler.class.getName());
	
	int processResult(RecResult recResult) {
		
		int successFlag = STATUS_BREAK;
		String literal = recResult.getValue();
		String index = recResult.getSlot(RecResult.SLOT_INDEX);
		// recType, literal
		ListNumberProcessedResult result = new
						ListNumberProcessedResult(index, recResult.getConfidence());
		result.setNumber(literal);
		processedResults.add(result);
 		return successFlag;
	}

	void postProcess() {
		// TODO Auto-generated method stub
		
	}

}
