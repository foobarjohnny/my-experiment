/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.contents;
import java.util.ArrayList;
import java.util.List;

import com.telenav.cserver.backend.StatusConstants;
import com.telenav.cserver.backend.datatypes.contents.ReviewOption;
import com.telenav.cserver.backend.datatypes.contents.ReviewServicePOIReview;
import com.telenav.datatypes.review.v10.AggregatedRating;
import com.telenav.datatypes.review.v10.Comment;
import com.telenav.datatypes.review.v10.IndexPagination;
import com.telenav.datatypes.review.v10.Rating;
import com.telenav.datatypes.review.v10.Review;
import com.telenav.datatypes.review.v10.ReviewAttribute;
import com.telenav.datatypes.review.v10.ReviewableObject;
import com.telenav.datatypes.review.v10.Selectable;
import com.telenav.datatypes.review.v10.SelectableValue;
import com.telenav.datatypes.review.v10.StrategyEnum;
import com.telenav.kernel.ws.axis2.Axis2Helper;
import com.telenav.services.review.v10.AggregatedRatingRequestDTO;
import com.telenav.services.review.v10.ReviewableObjectRequestDTO;
import com.telenav.services.review.v10.ReviewableObjectsRequestDTO;
import com.telenav.services.review.v10.ReviewableObjectsResponseDTO;
import com.telenav.ws.datatypes.services.mgmt.ServiceMgmtResponseDTO;

/**
 * Data convert utility
 * 
 * @author zhjdou 2009-07-14
 */
public class ContentManagerDataConvert
{

   
    
    public static ReviewableObjectsRequestDTO convertProxy2ReviewableObjectsRequestDTO(GetReviewRequest proxy)
    {
        ReviewableObjectsRequestDTO request = new ReviewableObjectsRequestDTO();
        if (proxy != null)
        {
            request.setClientName(ContentManagerServiceProxy.clientname);
            request.setClientVersion(ContentManagerServiceProxy.version);
            request.setTransactionId(ContentManagerServiceProxy.transactionId);
            ReviewableObject reviewableObject = new ReviewableObject();
            // set category id
            reviewableObject.setCategory(proxy.getCategoryId());
            // set poi id
            reviewableObject.setReviewableObjectId(proxy.getPoiId());

            //only support one poi id
            request.setReviewableObjects(new ReviewableObject[] { reviewableObject });
            IndexPagination pagination = new IndexPagination();
            if( proxy.getRevewId() != proxy.NOEXIST_REVIEW_ID )
            {
                // set start index
                pagination.setStartIndex(proxy.getStartIndex());
                // set end index
                pagination.setEndIndex(proxy.getEndIndex());
                request.setPaginationInfo(pagination);
            }
            else{
                Review review = new Review();
                // set review id
                review.setReviewId(proxy.getRevewId());
                reviewableObject.addReviews(review);
            }
            
            if( proxy.isOnlySummarizableAttributes() )
            {
                request.setIsOnlySummarizableAttributes(true);
            }
            
            if( proxy.isExcludeReviewsOfEmptyComments() )
            {
                request.setExcludeReviewsOfEmptyComments(true);
            }
        }
        return request;
    }
    
    public static AggregatedRatingRequestDTO convertProxy2AggregatedRatingRequestDTO(GetAggregatedRatingsRequest proxy)
    {
        AggregatedRatingRequestDTO request = new AggregatedRatingRequestDTO();
        if (proxy != null)
        {
            request.setClientName(ContentManagerServiceProxy.clientname);
            request.setClientVersion(ContentManagerServiceProxy.version);
            request.setTransactionId(ContentManagerServiceProxy.transactionId);
            request.setIncludePopularity(true);
            request.setStrategy(StrategyEnum.AVERAGE);
            ReviewableObject reviewableObject = new ReviewableObject();
            // set category id
            reviewableObject.setCategory(proxy.getCategoryId());
            // set poi id
            reviewableObject.setReviewableObjectId(proxy.getPoiId());

            //only support one poi id
            request.setReviewableObjects(new ReviewableObject[] { reviewableObject });
        }
        return request;
    }
    
    public static ReviewableObjectRequestDTO convertProxy2ReviewableObjectRequestDTO(SaveReviewsRequest proxy, long overallRatingRaId, long summrizableCommentRaId)
    {
        ReviewableObjectRequestDTO request = new ReviewableObjectRequestDTO();
        
        if (proxy != null)
        {
            request.setClientName(ContentManagerServiceProxy.clientname);
            request.setClientVersion(ContentManagerServiceProxy.version);
            request.setTransactionId(ContentManagerServiceProxy.transactionId);
            //request.setContextString(proxy.getContext());
            ReviewableObject reviewableObject = new ReviewableObject();
            // set category id
            reviewableObject.setCategory(proxy.getCategoryId());
            // set poi id
            reviewableObject.setReviewableObjectId(proxy.getPoiId());
            
            Review review = new Review();
            review.setUserId(proxy.getUserId());
            review.setReviewerName(proxy.getReviewerName());
            review.setReviewSourceId(proxy.getReviewSourceId());
            review.setReadonly(proxy.isReadonly());
            
            Comment comment = new Comment();
            // set comment text
            comment.setCommentText(proxy.getComment());
            // set summrizable raId
            comment.setId(summrizableCommentRaId);
            
            
            Rating rating = new Rating();
            SelectableValue value = new SelectableValue();
            // set rating value
            value.setValue(proxy.getRating());
            rating.setRatingValue(value);
            // set overall raId
            rating.setId(overallRatingRaId);
            
            List<ReviewOption> reviewOptions = proxy.getReviewOptions();
            if (reviewOptions != null && reviewOptions.size() != 0)
            {

                List<Selectable> options = new ArrayList<Selectable>(reviewOptions.size());
                for (ReviewOption reviewOption : reviewOptions)
                {
                    if (reviewOption != null)
                    {
                        Selectable option = new Selectable();
                        option.setId(reviewOption.getId());
                        SelectableValue selectableValue = new SelectableValue();
                        selectableValue.setValue(reviewOption.getValue());
                        option.addSelectedValues(selectableValue);
                        options.add(option);
                    }
                }

                List<ReviewAttribute> attributes = new ArrayList<ReviewAttribute>();
                attributes.add(comment);
                attributes.add(rating);
                attributes.addAll(options);
                ReviewAttribute[] attrsArray = new ReviewAttribute[attributes.size()];
                attributes.toArray(attrsArray);
                review.setReviewAttributes(attrsArray);

            }
            else
            {
                review.setReviewAttributes(new ReviewAttribute[]{ comment, rating });
            }

            reviewableObject.setReviews(new Review[] { review });
            request.setReviewableObject(reviewableObject);
        }
        return request;
    }

 
    
    public static GetReviewResponse converReviewableObjectsResponse2Proxy(ReviewableObjectsResponseDTO response)
    {
        GetReviewResponse proxy = new GetReviewResponse();
        if (response != null)
        {
            ReviewableObject[] reviewableObjects = response.getReviewableObjects();
            if( reviewableObjects != null &&  reviewableObjects.length > 0 )
            {
                proxy.setReview(convertPOIReviewArray2proxy(reviewableObjects[0]));
                proxy.setStatusCode(StatusConstants.SUCCESS);
                proxy.setStatusMessage(response.getResponseStatus().getStatusMessage());
            }
            else{
                proxy.setStatusCode(StatusConstants.FAIL);
                proxy.setStatusMessage(StatusConstants.WRONG_DATA_MSG);
            }
        }
        else{
            proxy.setStatusCode(StatusConstants.FAIL);
            proxy.setStatusMessage(StatusConstants.NULL_RESPONSE_MSG);
        }
        return proxy;
    }
    
    public static SaveReviewsResponse converReviewableObjectsResponse2Proxy(ServiceMgmtResponseDTO response)
    {
        SaveReviewsResponse proxy = new SaveReviewsResponse();
        if (response != null)
        {
            proxy.setStatusCode(StatusConstants.SUCCESS);
            proxy.setStatusMessage(response.getResponseStatus().getStatusMessage());
        }
        else
        {
            proxy.setStatusCode(StatusConstants.FAIL);
            proxy.setStatusCode(StatusConstants.NULL_RESPONSE_MSG);
        }
        return proxy;
    }
    
    public static GetAggregatedRatingsResponse converGetAggregatedRatingsResponse2Proxy(ReviewableObjectsResponseDTO response)
    {
        GetAggregatedRatingsResponse proxy = new GetAggregatedRatingsResponse();
        proxy.setStatusCode(StatusConstants.FAIL);
        proxy.setStatusMessage(StatusConstants.WRONG_DATA_MSG);
        if (response != null)
        {
            if ( Axis2Helper.isNonEmptyArray(response.getReviewableObjects()) )
            {
                ReviewableObject object = response.getReviewableObjects()[0];

                AggregatedRating aggregatedRating = object.getAggregatedRating();
                if (aggregatedRating != null)
                {
                    proxy.setPoiId(object.getReviewableObjectId());
                    proxy.setNumberOfRatings(aggregatedRating.getNumberOfRatings());
                    proxy.setRatingValue(aggregatedRating.getRatingValue());
                    proxy.setNumberOfComments(aggregatedRating.getNumberOfComments());
                    proxy.setLastUpdatedTime(aggregatedRating.getLastUpdatedTime().getTime());
                    proxy.setPopularity(aggregatedRating.getPopularity());

                    proxy.setStatusCode(StatusConstants.SUCCESS);
                    proxy.setStatusMessage(response.getResponseStatus().getStatusMessage());
                }

            }
        }
        
        return proxy;
    }

    
    
    private static ReviewServicePOIReview[] convertPOIReviewArray2proxy(ReviewableObject reviewableObject)
    {
        if (reviewableObject != null)
        {
            Review[] reviews = reviewableObject.getReviews();
            if (Axis2Helper.isNonEmptyArray(reviews))
            {
                int length = reviews.length;
                ReviewServicePOIReview[] proxy = new ReviewServicePOIReview[length];
                for (int index = 0; index < length; index++)
                {
                    proxy[index] = convertPOIReview2proxy(reviews[index]);
                    // set poi id
                    if (proxy[index] != null)
                    {
                        proxy[index].setPoiId(reviewableObject.getId());
                    }
                }
                return proxy;
            }
        }
        return null;
    }
    
    private static ReviewServicePOIReview convertPOIReview2proxy(Review poiReview)
    {
        if (poiReview != null)// check parameter
        {
            ReviewServicePOIReview proxy = new ReviewServicePOIReview();// object
            proxy.setCreateTime(poiReview.getLifeCycle().getCreated().getTime());
            //proxy.setInstanceId(poiReview.getInstanceId());
            //proxy.setPopularity(poiReview.getPopularity());
            
            
            //proxy.setRelevanceIndicator(poiReview.getRelevanceIndicator());
            proxy.setReviewerName(poiReview.getReviewerName());
            proxy.setReviewId(poiReview.getReviewId());
            proxy.setReviewSource(poiReview.getReviewSourceId());
            proxy.setUserId(poiReview.getUserId());
            proxy.setUpdateTime(poiReview.getLifeCycle().getUpdated().getTime());
            //proxy.setReviewType(poiReview.getReviewType());
            

            ReviewAttribute[] reviewAttributes = poiReview.getReviewAttributes();
            //TODO ratingCount is the count of rating ???
            int ratingCount = 0;  
            if (Axis2Helper.isNonEmptyArray(reviewAttributes))
            {
                for (ReviewAttribute reviewAttribute : reviewAttributes)
                {
                    if (reviewAttribute instanceof Comment)
                    {
                        Comment comment = (Comment) reviewAttribute;
                        proxy.setReviewText(comment.getCommentText());
                    }
                    else if (reviewAttribute instanceof Rating)
                    {
                        Rating rating = (Rating) reviewAttribute;
                        proxy.setRating(rating.getRatingValue().getValue());
                        ratingCount++;
                    }
                    else if(reviewAttribute instanceof Selectable)
                    {
                        Selectable option = (Selectable)reviewAttribute;
                        ReviewOption reviewOption = new ReviewOption();
                        reviewOption.setId(option.getId());
                        reviewOption.setName(option.getShortDisplayName());
                        SelectableValue[] selectedValue = option.getSelectedValues();
                        if( selectedValue != null && selectedValue.length > 0 )
                        {
                            reviewOption.setValue(selectedValue[0].getValue());
                        }
                        proxy.addReviewOption(reviewOption);
                    }
                }
            }
            proxy.setRatingCount(ratingCount);
            return proxy;
        }
        return null;
    }


}
