package com.telenav.cserver.dsr.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.telenav.cserver.dsr.ds.AddressProcessedResult;
import com.telenav.cserver.dsr.ds.AddressValidation;
import com.telenav.cserver.dsr.ds.Command;
import com.telenav.cserver.dsr.ds.ProcessedResult;
import com.telenav.cserver.dsr.ds.RecContext;
import com.telenav.cserver.dsr.ds.RecResult;
import com.telenav.cserver.dsr.framework.ProcessObject;
import com.telenav.cserver.dsr.util.GeoCodeUtil;
import com.telenav.cserver.dsr.util.ProcessUtil;
import com.telenav.cserver.dsr.util.ProcessConstants;
import com.telenav.j2me.datatypes.Stop;

/**
 * 
 * @author joses
 *
 */
public class OneshotAddressHandler extends AbstractResultsHandler {

	protected static Logger logger = Logger.getLogger(OneshotAddressHandler.class.getName());
	
	int processResult(RecResult recResult) {
		
		int successFlag = STATUS_SUCCESS;
		String literal = recResult.getValue();
		
		if(recResult.getSlot(ProcessConstants.SLOT_STREET_NAME, null) == null){
			recResult.getSlots().put(ProcessConstants.SLOT_STREET_NAME, recResult.getValue());
		}
		
		AddressValidation validation = GeoCodeUtil.validateAddress(recResult, context.tnContext);
		processedResults.addAll(handleAddressResult(recResult, context, validation));
		
		if (validation.isExact()) {
            logger.info("Exact ACE address.");
            //procObj.setProcessedResults(ProcessUtil.exactMatchingFirst(procObj.getProcessedResults()));
            processedResults = (ProcessUtil.exactMatchingFirst(processedResults));
            successFlag = STATUS_BREAK;
        }
		return successFlag;
	}
	
	static List<AddressProcessedResult> handleAddressResult(RecResult recResult, RecContext context, AddressValidation validation)
	{
		return handleAddressResult(recResult, context, validation, null);
	}
	
	static List<AddressProcessedResult> handleAddressResult(RecResult recResult, RecContext context, AddressValidation validation, Command command)
	{
		List<AddressProcessedResult> results = new ArrayList<AddressProcessedResult>();
		
		for (Stop stop : validation.getValidatedStop())
		{
		    AddressProcessedResult procResult = new AddressProcessedResult(
		    						recResult.getValue(), recResult.getConfidence(), command);
			procResult.setStop(stop);
			procResult.setCity(stop.city);
			procResult.setState(stop.state);
			procResult.setAddress(stop.firstLine);
			results.add(procResult);
		}
		
		return results;
	}

	void postProcess() {
		// TODO Auto-generated method stub
		
	}

}
