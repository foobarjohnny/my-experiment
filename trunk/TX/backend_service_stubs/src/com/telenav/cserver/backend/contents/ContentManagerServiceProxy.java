/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.contents;

import java.util.ArrayList;
import java.util.List;

import org.apache.axis2.AxisFault;
import org.apache.log4j.Logger;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.backend.StatusConstants;
import com.telenav.cserver.backend.config.WebServiceConfigInterface;
import com.telenav.cserver.backend.datatypes.contents.ReviewOption;
import com.telenav.cserver.backend.util.WebServiceConfiguration;
import com.telenav.cserver.backend.util.WebServiceUtils;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.cserver.framework.throttling.ThrottlingManager;
import com.telenav.datatypes.review.v10.Comment;
import com.telenav.datatypes.review.v10.Rating;
import com.telenav.datatypes.review.v10.Review;
import com.telenav.datatypes.review.v10.ReviewAttribute;
import com.telenav.datatypes.review.v10.ReviewableObject;
import com.telenav.datatypes.review.v10.Selectable;
import com.telenav.datatypes.review.v10.SelectableValue;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.kernel.ws.axis2.Axis2Helper;
import com.telenav.services.contentmanager.v10.ContentManagerStub;
import com.telenav.services.review.v10.AggregatedRatingRequestDTO;
import com.telenav.services.review.v10.ReviewService20Stub;
import com.telenav.services.review.v10.ReviewableObjectRequestDTO;
import com.telenav.services.review.v10.ReviewableObjectResponseDTO;
import com.telenav.services.review.v10.ReviewableObjectsRequestDTO;
import com.telenav.services.review.v10.ReviewableObjectsResponseDTO;
import com.telenav.ws.datatypes.services.mgmt.ServiceMgmtResponseDTO;

/**
 * Content_Manager service proxy
 * 
 * @author zhjdou 2009-07-13
 */
public class ContentManagerServiceProxy
{
    static Logger logger = Logger.getLogger(ContentManagerServiceProxy.class);

    private final static String SERVICE_CONTENTMANAGERSERVER = "CONTENTMANAGERSERVER";

    private static ContentManagerServiceProxy instance = new ContentManagerServiceProxy();

    // The value of folowing three parameters not important just as far as I know
    public final static String version = "v10";

    public final static String clientname = "clientName";

    public final static String transactionId = "001";
    
    private final static String WS_CONTENTMANAGER = "CONTENT_MANAGER";
    
    //private final static ConfigurationContext contentManagerServiceContext = WebServiceUtils.createConfigurationContext(WS_CONTENTMANAGER);
    

    private ContentManagerServiceProxy()
    {
    }


    /**
     * return the singleton instance
     * 
     * @return
     */
    public static ContentManagerServiceProxy getInstance()
    {
        return instance;
    }

    
    public GetReviewResponse getReviews(GetReviewRequest request, TnContext tc) throws ThrottlingException
    {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("Content_Manager");
        if (request != null && logger.isDebugEnabled())
        {// log request
            logger.debug("getReviewList======>" + request.toString());
        }
        boolean startAPICall = false;// the flag whether can start call API
        GetReviewResponse response = new GetReviewResponse();
        ReviewService20Stub stub = null;
        try
        {
            startAPICall = ThrottlingManager.startAPICall(SERVICE_CONTENTMANAGERSERVER, tc);
            if (!startAPICall)
            {
                throw new ThrottlingException();
            }
            try
            {
                stub = createStub2();
                // convert the request to real one
                ReviewableObjectsRequestDTO reviewListReview = ContentManagerDataConvert.convertProxy2ReviewableObjectsRequestDTO(request);
                cli.addData("Request", request.toString());
                // get review tags list
                ReviewableObjectsResponseDTO reResponse = stub.getReviews(reviewListReview);
                
                // convert the response to proxy
                response = ContentManagerDataConvert.converReviewableObjectsResponse2Proxy(reResponse);
                cli.addData("Reponse", "code "+response.getStatusCode()+" message "+response.getStatusMessage());
                if (logger.isDebugEnabled())
                {// log
                    logger.debug("getReviewListResponse=====>" + response.toString());
                }
            }
            catch (Exception ex)
            {
                logger.fatal("ContentManagerServiceProxy::getReviews()", ex);
                response.setStatusCode(StatusConstants.FAIL);
                response.setStatusMessage(StatusConstants.EXCEPTION_MSG+ex.getMessage());
            }
        }
        finally
        {
            cli.complete();
            if (startAPICall)// throttling
            {
                ThrottlingManager.endAPICall(SERVICE_CONTENTMANAGERSERVER, tc);
            }
            WebServiceUtils.cleanupStub(stub);
        }
        return response;
    }
    
    
    public SaveReviewsResponse saveReviews(SaveReviewsRequest request, TnContext tc) throws ThrottlingException
    {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("Content_Manager");
        if (request != null && logger.isDebugEnabled())
        {// log request
            logger.debug("SaveReviews======>" + request.toString());
        }
        boolean startAPICall = false;// the flag whether can start call API
        SaveReviewsResponse response = new SaveReviewsResponse();
        ReviewService20Stub stub = null;
        try
        {
            startAPICall = ThrottlingManager.startAPICall(SERVICE_CONTENTMANAGERSERVER, tc);
            if (!startAPICall)
            {
                throw new ThrottlingException();
            }
            try
            {
                stub = createStub2();
                // get ids first
                long[] ids = getIdsForSavingReviews(request, tc);
                if( ids == null )
                {
                    response.setStatusCode(StatusConstants.FAIL);
                    response.setStatusMessage(StatusConstants.WRONG_DATA_MSG);
                    return response;
                }
                long overallRatingRaId = ids[0];
                long summrizableCommentRaId = ids[1];

                // convert the request to real one
                ReviewableObjectRequestDTO reviewableObjectRequestDTO = ContentManagerDataConvert.convertProxy2ReviewableObjectRequestDTO(request,overallRatingRaId,summrizableCommentRaId);
                cli.addData("Request", request.toString());
                // get review tags list
                ServiceMgmtResponseDTO reResponse = stub.saveReviews(reviewableObjectRequestDTO);

                // convert the response to proxy
                response = ContentManagerDataConvert.converReviewableObjectsResponse2Proxy(reResponse);
                cli.addData("Reponse", "code " + response.getStatusCode() + " message " + response.getStatusMessage());
                if (logger.isDebugEnabled())
                {// log
                    logger.debug("SaveReviewsResponse=====>" + response.toString());
                }
            }
            catch (Exception ex)
            {
                logger.fatal("ContentManagerServiceProxy::SaveReviews()", ex);
                response.setStatusCode(StatusConstants.FAIL);
                response.setStatusMessage(StatusConstants.EXCEPTION_MSG+ex.getMessage());
            }
        }
        finally
        {
            cli.complete();
            if (startAPICall)// throttling
            {
                ThrottlingManager.endAPICall(SERVICE_CONTENTMANAGERSERVER, tc);
            }
            WebServiceUtils.cleanupStub(stub);
        }
        return response;
    }
    
    
    public GetAggregatedRatingsResponse getAggregatedRatings(GetAggregatedRatingsRequest request, TnContext tc) throws ThrottlingException
    {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("Content_Manager");
        if (request != null && logger.isDebugEnabled())
        {// log request
            logger.debug("getAggregatedRatings======>" + request.toString());
        }
        boolean startAPICall = false;// the flag whether can start call API
        GetAggregatedRatingsResponse response = new GetAggregatedRatingsResponse();
        ReviewService20Stub stub = null;
        try
        {
            startAPICall = ThrottlingManager.startAPICall(SERVICE_CONTENTMANAGERSERVER, tc);
            if (!startAPICall)
            {
                throw new ThrottlingException();
            }
            try
            {
                stub = createStub2();
                // convert the request to real one
                AggregatedRatingRequestDTO aggregatedRatingRequestDTO = ContentManagerDataConvert.convertProxy2AggregatedRatingRequestDTO(request);
                cli.addData("Request", request.toString());
                // get review tags list
                ReviewableObjectsResponseDTO reResponse = stub.getAggregatedRatings(aggregatedRatingRequestDTO);
                
                // convert the response to proxy
                response = ContentManagerDataConvert.converGetAggregatedRatingsResponse2Proxy(reResponse);
                cli.addData("Reponse", "code "+response.getStatusCode()+" message "+response.getStatusMessage());
                if (logger.isDebugEnabled())
                {// log
                    logger.debug("getAggregatedRatingsResponse=====>" + response.toString());
                }
            }
            catch (Exception ex)
            {
                logger.fatal("ContentManagerServiceProxy::getAggregatedRatings()", ex);
                response.setStatusCode(StatusConstants.FAIL);
                response.setStatusMessage(StatusConstants.EXCEPTION_MSG+ex.getMessage());
            }
        }
        finally
        {
            cli.complete();
            if (startAPICall)// throttling
            {
                ThrottlingManager.endAPICall(SERVICE_CONTENTMANAGERSERVER, tc);
            }
            WebServiceUtils.cleanupStub(stub);
        }
        return response;
    }
    
    
    private long[] getIdsForSavingReviews(SaveReviewsRequest request, TnContext tc) throws ThrottlingException
    {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("Content_Manager");
        if (request != null && logger.isDebugEnabled())
        {// log request
            logger.debug("getIdsForSavingReviews======>" + request.toString());
        }
        boolean startAPICall = false;// the flag whether can start call API
        ReviewService20Stub stub = null;
        long overallRatingRaId = 0;
        long summrizableCommentRaId = 0;
        long[] returnValues = null;
        try
        {
            startAPICall = ThrottlingManager.startAPICall(SERVICE_CONTENTMANAGERSERVER, tc);
            if (!startAPICall)
            {
                throw new ThrottlingException();
            }
            try
            {
                stub = createStub2();
                ReviewableObjectRequestDTO getRuleRequest = new ReviewableObjectRequestDTO();
                ReviewableObject reviewableObjectParam = new ReviewableObject();
                // set category id
                reviewableObjectParam.setCategory(request.getCategoryId());
                //Added by Zhang Pan on Aug-04 2010 14:50
                //Added poi id also as the input parameter
                //The reason to add poi id is that for the review webservice changes, we need pass category id as the parameter
                //but for the released client(9000/8900 etc), we can't get category id from client, so the category id will be -1
                //xnav also will do some enhancement, if the category id is -1, they will fetch the category id via poi id
                //for the new client release later, client will pass the category id as the ratepoi input parameter.
                reviewableObjectParam.setReviewableObjectId(request.getPoiId());
                //
                getRuleRequest.setReviewableObject(reviewableObjectParam);
                getRuleRequest.setClientName(clientname);
                getRuleRequest.setClientVersion(version);
                getRuleRequest.setTransactionId(transactionId);
                ReviewableObjectResponseDTO response = stub.getRules(getRuleRequest);

                ReviewableObject fetchedReviewableObject = response.getReviewableObject();
                Review[] reviews = fetchedReviewableObject.getReviews();
                if (Axis2Helper.isNonEmptyArray(reviews))
                {
                    ReviewAttribute[] reviewAttributes = reviews[0].getReviewAttributes();
                    if (Axis2Helper.isNonEmptyArray(reviewAttributes))
                    {
                        for (ReviewAttribute reviewAttribute : reviewAttributes)
                        {
                            if (reviewAttribute.getSummarizable() && (reviewAttribute instanceof Comment))
                            {
                                summrizableCommentRaId = reviewAttribute.getId();
                            }
                            else if (reviewAttribute.getSummarizable() && (reviewAttribute instanceof Rating))
                            {
                                overallRatingRaId = reviewAttribute.getId();
                            }
                        }
                        returnValues = new long[]{ overallRatingRaId, summrizableCommentRaId };
                    }
                }

                if (logger.isDebugEnabled())
                {// log
                    logger.debug("getIdsForSavingReviews=====>" + response.toString());
                }
            }
            catch (Exception ex)
            {
                logger.fatal("ContentManagerServiceProxy::getIdsForSavingReviews()", ex);
                
            }
        }
        finally
        {
            cli.complete();
            if (startAPICall)// throttling
            {
                ThrottlingManager.endAPICall(SERVICE_CONTENTMANAGERSERVER, tc);
            }
            WebServiceUtils.cleanupStub(stub);
        }
        return returnValues;
    }
    
    
    public List<ReviewOption> getReviewOptions(SaveReviewsRequest request, TnContext tc) throws ThrottlingException
    {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("Content_Manager");
        if (request != null && logger.isDebugEnabled())
        {// log request
            logger.debug("getReviewOptions======>" + request.toString());
        }
        List<ReviewOption> reviewOptions = new ArrayList<ReviewOption>();
        boolean startAPICall = false;// the flag whether can start call API
        ReviewService20Stub stub = null;
        try
        {
            startAPICall = ThrottlingManager.startAPICall(SERVICE_CONTENTMANAGERSERVER, tc);
            if (!startAPICall)
            {
                throw new ThrottlingException();
            }
            try
            {
                stub = createStub2();
                ReviewableObjectRequestDTO getRuleRequest = new ReviewableObjectRequestDTO();
                ReviewableObject reviewableObjectParam = new ReviewableObject();
                // set category id
                reviewableObjectParam.setCategory(request.getCategoryId());
                reviewableObjectParam.setReviewableObjectId(request.getPoiId());
                //
                getRuleRequest.setReviewableObject(reviewableObjectParam);
                getRuleRequest.setClientName(clientname);
                getRuleRequest.setClientVersion(version);
                getRuleRequest.setTransactionId(transactionId);
                getRuleRequest.setContextString(request.getContextString());
                getRuleRequest.setLocale(request.getLocale());
                
                ReviewableObjectResponseDTO response = stub.getRules(getRuleRequest);

                ReviewableObject fetchedReviewableObject = response.getReviewableObject();
                Review[] reviews = fetchedReviewableObject.getReviews();
                if (Axis2Helper.isNonEmptyArray(reviews))
                {
                    ReviewAttribute[] reviewAttributes = reviews[0].getReviewAttributes();
                    if (Axis2Helper.isNonEmptyArray(reviewAttributes))
                    {
                        ReviewOption option = null;
                        for (ReviewAttribute reviewAttribute : reviewAttributes)
                        {
                            if (reviewAttribute instanceof Selectable)
                            {
                                Selectable selectable = (Selectable)reviewAttribute;
                                SelectableValue[] selectableValues = selectable.getSelectableValues();
                                if( selectableValues != null && selectableValues.length == 2)
                                {
                                    if( (selectableValues[0].getId() == 1062 &&  selectableValues[1].getId() == 1063) || 
                                        (selectableValues[0].getId() == 1063 &&  selectableValues[1].getId() == 1062) )
                                    {
                                        option = new ReviewOption();
                                        option.setId(reviewAttribute.getId());
                                        option.setName(reviewAttribute.getShortDisplayName());
                                        
                                        reviewOptions.add(option);
                                    }
                                }
                            }
                        }
                    }
                }

                if (logger.isDebugEnabled())
                {// log
                    logger.debug("getIdsForSavingReviews=====>" + response.toString());
                }
            }
            catch (Exception ex)
            {
                logger.fatal("ContentManagerServiceProxy::getIdsForSavingReviews()", ex);
                
            }
        }
        finally
        {
            cli.complete();
            if (startAPICall)// throttling
            {
                ThrottlingManager.endAPICall(SERVICE_CONTENTMANAGERSERVER, tc);
            }
            WebServiceUtils.cleanupStub(stub);
        }
        return reviewOptions;
    }
    
    private ReviewService20Stub createStub2()
    {
        ReviewService20Stub stub = null;
        try
        {
            WebServiceConfigInterface ws = WebServiceConfiguration.getInstance().getServiceConfig(WS_CONTENTMANAGER);
            stub = new ReviewService20Stub(ws.getServiceUrl());
        }
        catch (AxisFault af)
        {
            logger.fatal("createStub", af);
        }
        return stub;
    }
    
    
    private ContentManagerStub createStub()
    {
        ContentManagerStub stub = null;
        try
        {
            WebServiceConfigInterface ws = WebServiceConfiguration.getInstance().getServiceConfig(WS_CONTENTMANAGER);
            stub = new ContentManagerStub(ws.getServiceUrl());
        }
        catch (AxisFault af)
        {
            logger.fatal("createStub", af);
        }
        return stub;
    }
    
}
