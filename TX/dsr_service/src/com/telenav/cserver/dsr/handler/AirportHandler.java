package com.telenav.cserver.dsr.handler;

import java.util.ArrayList;
import java.util.List;

import com.telenav.cserver.backend.datatypes.TnPoi;
import com.telenav.cserver.dsr.ds.AirportProcessedResult;
import com.telenav.cserver.dsr.ds.Command;
import com.telenav.cserver.dsr.ds.RecContext;
import com.telenav.cserver.dsr.ds.RecResult;
import com.telenav.cserver.dsr.util.GeoCodeUtil;
import com.telenav.cserver.dsr.util.ProcessConstants;

/**
 * 
 * @author joses
 *
 */
public class AirportHandler extends AbstractResultsHandler {

	int processResult(RecResult recResult) {
		
		int successFlag = STATUS_SUCCESS;
		processedResults.addAll(handleAirportResult(recResult, context));
		return successFlag;
	}
	
	public static List<AirportProcessedResult> handleAirportResult(RecResult recResult, RecContext context)
	{
		return handleAirportResult(recResult, context, null);
	}
	
	public static List<AirportProcessedResult> handleAirportResult(RecResult recResult, RecContext context, Command command)
	{
		List<AirportProcessedResult> resultList =
							new ArrayList<AirportProcessedResult>();
		
		String literal = recResult.getValue();
		String airportCode = recResult.getSlot(ProcessConstants.SLOT_CODE,"");
		if(airportCode.equals("")){
			airportCode = recResult.getSlot(ProcessConstants.SLOT_AIRPORT,"");
			if(airportCode.equals(""))
				airportCode = literal;
		}
		
		List<TnPoi> airports = GeoCodeUtil.validateAirport(airportCode, context.tnContext);
		if (airports != null && airports.size() > 0) {
			 for (TnPoi airport : airports) {
				 
				 if (airport == null)
                     continue;
				 
				 AirportProcessedResult procResult = new AirportProcessedResult(literal,
						 														recResult.getConfidence(),
						 														command);
				 procResult.setAirportCode(airport.getFeatureName());
				 procResult.setAirportName(airport.getBrandName()); 
				 procResult.setAirport(airport) ;
                 resultList.add(procResult);
			 }
		}
		
		return resultList;
	}


	void postProcess() {
		// TODO Auto-generated method stub
		
	}

}
