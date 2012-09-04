/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.poi.executor;

import org.apache.log4j.Logger;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.backend.contents.ContentManagerServiceProxy;
import com.telenav.cserver.backend.contents.GetAggregatedRatingsRequest;
import com.telenav.cserver.backend.contents.GetAggregatedRatingsResponse;
import com.telenav.cserver.backend.contents.GetReviewRequest;
import com.telenav.cserver.backend.contents.SaveReviewsRequest;
import com.telenav.cserver.backend.contents.SaveReviewsResponse;
import com.telenav.cserver.backend.telepersonalize.TelepersonalizationFacade;
import com.telenav.cserver.backend.telepersonalize.UserStatus;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.executor.AbstractExecutor;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.cserver.poi.datatypes.POI;
import com.telenav.cserver.poi.struts.util.PoiUtil;
import com.telenav.kernel.util.datatypes.TnContext;


/**
 * Watch out:brandName in editPOI can not be null
 * 
 * @author zhjdou 2009-7-17
 */
public class EditPOIExecutor_WS extends AbstractExecutor
{

    static Logger logger = Logger.getLogger(EditPOIExecutor_WS.class);// log engine

    @Override
    public void doExecute(ExecutorRequest req, ExecutorResponse resp, ExecutorContext context) throws ExecutorException
    {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("EditPOI");

        EditPOIRequest editPOIRequest = (EditPOIRequest) req;
        EditPOIReponse poiResponse = (EditPOIReponse) resp;
        UserProfile userProfile = editPOIRequest.getUserProfile();
        TnContext tc = context.getTnContext();
        if (logger.isDebugEnabled())
        {
            logger.debug("TnContext: " + tc.toContextString());
        }

        ContentManagerServiceProxy server = ContentManagerServiceProxy.getInstance();

        SaveReviewsRequest request = new SaveReviewsRequest();
        request.setUserId(Long.parseLong(userProfile.getUserId()));
//        request.setContext(tc.toContextString());
        // TODO chbzhang dummy data, for TnContext is not finished.
//        request.setMapDataSet(tc.getProperty(TnContext.PROP_MAP_DATASET));
//        request.setPoiDataSet(tc.getProperty(TnContext.PROP_POI_DATASET));
        //request.setMapDataSet("TeleAtlas");
        //request.setPoiDataSet("TA");
        // /tc contain key value
        //here  set the reviewer name
        
        try {
        	UserStatus status = TelepersonalizationFacade.getUserProfile(userProfile.getUserId(), tc);
        	if(status != null)
        	{
	        	String firstName = status.getFirstName();
	            String lastName = status.getLastName();
	            String userName = PoiUtil.getString(firstName) + " " + PoiUtil.getString(lastName);
	            request.setReviewerName(userName.trim());
	            if (logger.isDebugEnabled())
	            {
	                logger.debug(firstName + " " + lastName + " is trying to edit POI");
	            }
        	}
            
		} catch (ThrottlingException e1) {
			// TODO Auto-generated catch block
			logger.error("error occured when get usrprofile, the userid is "+userProfile.getUserId());
			e1.printStackTrace();
			cli.addData(CliConstants.LABEL_ERROR, "fetchUser failure");
            logger.warn("Can't get user name from xnav.");
		}
        
        int categoryId = -1;
        
        try{
        	categoryId = Integer.parseInt(editPOIRequest.getCategoryId());
        }catch (NumberFormatException e) {
		}
           
        
        request.setCategoryId(categoryId);
        request.setComment(editPOIRequest.poiToEdit.getReview());
        request.setPoiId(editPOIRequest.poiToEdit.getPoiId());
        request.setRating((int)editPOIRequest.poiToEdit.getRating() + "");
        request.setReadonly(true);
        request.setReviewSourceId("0");
//        request.setEditPOI(editPOIRequest.poiToEdit);
        if(editPOIRequest.poiToEdit != null)
        {
            cli.addData("editPoi", "id=" + editPOIRequest.poiToEdit.getPoiId());
        }
        else
        {
            cli.addData("editPoi", "poiToEdit is null.");
        }
        try
        { // edit poi
        	SaveReviewsResponse response = server.saveReviews(request, tc);
            String statusCode = response.getStatusCode();// list the status of response
            String statusMsg = response.getStatusMessage();
            if (logger.isDebugEnabled())
            {
                logger.debug("statusCode=" + statusCode + "statusMsg=" + statusMsg);// log4j
            }
            cli.addData("status", "statusCode=" + statusCode + "statusMsg=" + statusMsg);// cli log

            if (statusCode != null && statusCode.equalsIgnoreCase("OK"))// the status is ok
            {
                poiResponse.setStatus(ExecutorResponse.STATUS_OK);
                
                GetAggregatedRatingsRequest getAggregatedRatingsRequest = new GetAggregatedRatingsRequest();
                getAggregatedRatingsRequest.setCategoryId(categoryId);
                getAggregatedRatingsRequest.setPoiId(editPOIRequest.poiToEdit.getPoiId());
                GetAggregatedRatingsResponse getAggregatedRatingsResponse =  server.getAggregatedRatings(getAggregatedRatingsRequest, tc);
                
                String getAggregatedRatingsStatusCode = getAggregatedRatingsResponse.getStatusCode();// list the status of response
                String getAggregatedRatingsStatusMsg = getAggregatedRatingsResponse.getStatusMessage();
                if (logger.isDebugEnabled())
                {
                    logger.debug("getAggregatedRatingsStatusCode=" + getAggregatedRatingsStatusCode + "getAggregatedRatingsStatusMsg=" + getAggregatedRatingsStatusMsg);// log4j
                }
                cli.addData("getAggregatedRatings status", "getAggregatedRatingsStatusCode=" + getAggregatedRatingsStatusCode + "getAggregatedRatingsStatusMsg=" + getAggregatedRatingsStatusMsg);// cli log
                
                POI poi = convertTnPoiToPOI(getAggregatedRatingsResponse,tc);
                if(null != poi){
                   poiResponse.setPoi(poi);	
                }
            }
            else
            {
                poiResponse.setStatus(ExecutorResponse.STATUS_FAIL);
                cli.addData("warning", "Edit POI EXCEPTION or Failed");
                cli.setState(CliConstants.STATUS_FAIL);
            }

            poiResponse.setMessage("Status Code =" + response.getStatusCode() + " Message=" + response.getStatusMessage());
        }
        catch (ThrottlingException ex)
        {
            logger.error(ex);
            poiResponse.setStatus(ExecutorResponse.STATUS_FAIL);
            cli.addData("Error", "Throttling Exception");
            cli.setState(CliConstants.STATUS_FAIL);
        }
        finally
        {
            cli.complete();
        }
    }
    
    public static POI convertTnPoiToPOI(GetAggregatedRatingsResponse getAggregatedRatingsResponse, TnContext tc)
	{
    	if(getAggregatedRatingsResponse == null)
		{
			return null;
		}
		POI clientPOI = new POI();
		
//		TnPoiReviewSummary reviewSmy = tnPoi.getReviewSummary();
//		if(reviewSmy != null)
//		{
		    clientPOI.ratingNumber = getAggregatedRatingsResponse.getNumberOfRatings();
		    clientPOI.avgRating = (int)(getAggregatedRatingsResponse.getRatingValue()*10);
            clientPOI.popularity = (int)Math.round(getAggregatedRatingsResponse.getPopularity());
//			clientPOI.reviewSummary = PoiDataConverter.convertReviewSummary(reviewSmy);
//			clientPOI.priceRange = reviewSmy.getReviewAveragePrice()!=null?Double.parseDouble(reviewSmy.getReviewAveragePrice()):0;
//		}
//		ContentManagerServiceProxy contentManagerServiceProxy = ContentManagerServiceProxy.getInstance();
//		ReviewDetailListRequest request = new ReviewDetailListRequest();
		GetReviewRequest request = new GetReviewRequest();
		request.setOnlySummarizableAttributes(true);
//		request.setContext(tc.toContextString());
		request.setPoiId(getAggregatedRatingsResponse.getPoiId());
		request.setStartIndex(0);
		request.setEndIndex(9);
//		try {
//			GetReviewResponse getReviewResponse = contentManagerServiceProxy.getReviews(request, tc);
//            clientPOI.getReviewResponse = getReviewResponse;
//        } catch (ThrottlingException e) {
//        }
		
		return clientPOI;
	}
}