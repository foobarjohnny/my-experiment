package com.telenav.cserver.poidetail.executor.v20;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.backend.cose.CoseFactory;
import com.telenav.cserver.backend.cose.PoiDetailsRequest;
import com.telenav.cserver.backend.cose.PoiDetailsResponse;
import com.telenav.cserver.backend.cose.PoiSearchProxyV20;
import com.telenav.cserver.backend.datatypes.ErrorCode;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.executor.AbstractExecutor;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.util.TnUtil;
import com.telenav.kernel.util.datatypes.TnContext;
import com.televigation.log.TVCategory;

public class POIDetailsExecutor extends AbstractExecutor 
{
	protected static TVCategory logger = (TVCategory) TVCategory.getInstance(POIDetailsExecutor.class);
	@Override
	public void doExecute(ExecutorRequest request, ExecutorResponse response, ExecutorContext context) throws ExecutorException 
	{
		CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
		cli.setFunctionName("POIDetailsExecutor");
		POIDetailsRequest detailRequest = (POIDetailsRequest)request;
		POIDetailsResponse detailResponse = (POIDetailsResponse)response;
		
		TnContext tc = context.getTnContext();
		UserProfile userProfile = request.getUserProfile();
		TnUtil.getDSMDataFromCServer(tc, userProfile);
		
		//TODO : MISLog
		PoiDetailsRequest req = new PoiDetailsRequest();
		req.setPoiId(detailRequest.getPoiId());
		req.setPtn(detailRequest.getUserProfile().getMin());
		req.setUserId(detailRequest.getUserProfile().getUserId());
		PoiDetailsResponse result = null;
		try
		{
			PoiSearchProxyV20 poiSearchProxy = CoseFactory.createPoiSearch20Proxy(tc);
			result = poiSearchProxy.getPoiDetails(req);
		}
		catch(Exception e)
		{
			logger.error(e, e);
		}
		if(result != null)
		{
			if(logger.isDebugEnabled())
			{
                logger.debug("search status : " + result.getStatus());
			}
			cli.addData("search status", result.toString());
		}
		else
		{
			if(logger.isDebugEnabled())
			{
				logger.debug("result is null");	
			}
			cli.addData("warning", "result is null");
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("search status : " + result != null ? result.getStatus() : "result is null");
		}
		if(result != null && result.getStatus() == ErrorCode.POI_STATUS_SUCCESS)
		{
			detailResponse.setPoiId(result.getPoiId());
			detailResponse.setBusinessHours(result.getBusinessHours());
			detailResponse.setBusinessHoursNote(result.getBusinessHoursNote());
			detailResponse.setDescription(result.getDescription());
			detailResponse.setPriceRange(result.getPriceRange());
			detailResponse.setOlsonTimezone(result.getOlsonTimezone());
			detailResponse.setLogoId(result.getLogoId());
			detailResponse.setMediaServerKey(result.getMediaServerKey());
			detailResponse.setStatus(ExecutorResponse.STATUS_OK);
		}
		else
		{
			detailResponse.setStatus(ExecutorResponse.STATUS_FAIL);
			detailResponse.setErrorMessage("Failed to get POI Details");
		}
		cli.complete();
		//TODO : MISLog
	}

}
