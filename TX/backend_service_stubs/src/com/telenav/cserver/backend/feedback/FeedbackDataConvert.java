/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.feedback;

import com.telenav.cserver.backend.datatypes.feedback.FeedbackQuestion;
import com.telenav.ws.datatypes.feedback.Comment20;
import com.telenav.ws.datatypes.feedback.FeedbackServiceRequestDTO;
import com.telenav.ws.datatypes.feedback.Comment;
import com.telenav.ws.datatypes.feedback.Feedback;
import com.telenav.ws.datatypes.feedback.FeedbackServiceResponseDTO;
import com.telenav.ws.datatypes.feedback.FeedbackTopic;
import com.telenav.ws.datatypes.feedback.MediumType;
import com.telenav.ws.datatypes.feedback.TextAnswerType;
import com.telenav.ws.datatypes.address.GeoCode;
import com.telenav.ws.datatypes.feedback.NavigationContext;
import com.telenav.ws.datatypes.services.ResponseStatus;
import com.telenav.ws.services.feedback.GetFeedbackQuestionsRequestDTO;
import com.telenav.ws.services.feedback.GetFeedbackQuestionsResponseDTO;

/**
 * The utility of feedback data convertion
 * 
 * @author zhjdou
 * 
 */
public class FeedbackDataConvert
{

    /**
     * get response
     * 
     * @return
     */
    public static FeedbackServiceResponse convertFeedBackReponse2Proxy(FeedbackServiceResponseDTO response)
    {   
        if(response==null) {
            return null;
        }
        FeedbackServiceResponse proxy = new FeedbackServiceResponse();
        ResponseStatus status = response.getResponseStatus();
        proxy.setStatusCode(status.getStatusCode());
        proxy.setStatusMessage(status.getStatusMessage());
        proxy.setFeedbackId(response.getFeedbackId());

        return proxy;
    }

    /**
     * Convert the feedback question response to proxy type
     * 
     * @return
     */
    public static GetFeedbackQuestionsResponse convertGetFeedBackQuestionReponse2Proxy(GetFeedbackQuestionsResponseDTO response)
    {
        if (response == null)
        {
            return null;
        }
        GetFeedbackQuestionsResponse proxy = new GetFeedbackQuestionsResponse();
        proxy.setQuestion(ConvertFeedbackQuestion2Proxy(response.getQuestion()));
        return proxy;
    }

    /**
     * Convert the question array to proxy
     * 
     * @param question array
     * @return
     */
    public static FeedbackQuestion[] ConvertFeedbackQuestion2Proxy(com.telenav.ws.datatypes.feedback.FeedbackQuestion[] question)
    {
        if (question != null)
        {
            int length = question.length;
            FeedbackQuestion[] proxy = new FeedbackQuestion[length];
            for (int index = 0; index < length; index++)
            {
                proxy[index] = convertFeedbackQuestion2Proxy(question[index]);
            }
            return proxy;
        }
        return null;
    }

    /**
     * Convert the question to proxy
     * 
     * @param question
     * @return
     */
    public static FeedbackQuestion convertFeedbackQuestion2Proxy(com.telenav.ws.datatypes.feedback.FeedbackQuestion question)
    {
        if (question != null)
        {
            FeedbackQuestion proxy = new FeedbackQuestion();
            proxy.setId(question.getId());
            proxy.setApplication(question.getApplication());
            proxy.setApplicationVersion(question.getApplicationVersion());
            proxy.setCarrier(question.getCarrier());
            proxy.setDevice(question.getDevice());
            proxy.setLocale(question.getLocale());
            proxy.setName(question.getName());
            proxy.setChoice(question.getChoice());
            proxy.setPlatform(question.getPlatform());
            proxy.setquestion(question.getQuestion());
            if (question.getTopic() != null)
            {
                proxy.setTopic(new com.telenav.cserver.backend.datatypes.feedback.FeedbackTopic(question.getTopic().getValue()));
            }
            if (question.getTextAnswerType() != null)
            {
                proxy.setTextAnswerType(new com.telenav.cserver.backend.datatypes.feedback.TextAnswerType(question.getTextAnswerType().getValue()));
            }
            return proxy;
        }
        return null;
    }

    /**
     * COnvert the request to real one that can acceptable to server.
     */
    public static FeedbackServiceRequestDTO convertFeedBackRequestProxy2Request(FeedbackServiceRequest proxy)
    {
        if (proxy != null)
        {
            FeedbackServiceRequestDTO request = new FeedbackServiceRequestDTO();
            request.setClientName(FeedbackServiceProxy.clientName);
            request.setClientVersion(FeedbackServiceProxy.clientVersion);
            request.setComment(convertCommentProxyArray2Comment(proxy.getComment()));
            request.setContextString(proxy.getContextString());
            request.setFeedbackId(proxy.getFeedbackId());
            request.setTransactionId(FeedbackServiceProxy.transactionId);
            request.setFeedback(convertFeedBackProxy2FeedBack(proxy.getFeedback()));
            request.setTopic(convertFeedBackTopicProxy2Topic(proxy.getTopic()));
            request.setCarrier(proxy.getCarrier());
            request.setDevice(proxy.getDevice());
            request.setPlatform(proxy.getPlatform());
            request.setScreenName(proxy.getScreenName());
            request.setUserClient(proxy.getUserClient());
            request.setUserClientVersion(proxy.getUserClientVersion());
            return request;
        }
        return null;
    }

    /**
     * COnvert the request to real one that can acceptable to server.
     */
    public static GetFeedbackQuestionsRequestDTO convertGetFeedBackQuestionRequestProxy2Request(GetFeedbackQuestionsRequest proxy)
    {
        if (proxy != null)
        {
            GetFeedbackQuestionsRequestDTO request = new GetFeedbackQuestionsRequestDTO();
            request.setClientName(FeedbackServiceProxy.clientName);
            request.setClientVersion(FeedbackServiceProxy.clientVersion);
            request.setTransactionId(FeedbackServiceProxy.transactionId);
            request.setUserClient(proxy.getUserClient());
            request.setUserClientVersion(proxy.getUserClientVersion());
            request.setCarrier(proxy.getCarrier());
            request.setDevice(proxy.getDevice());
            request.setPlatform(proxy.getPlatform());
            request.setLocale(proxy.getLocale());
            request.setTopic(convertFeedBackTopicProxy2Topic(proxy.getTopic()));
            return request;
        }
        return null;
    }

    /**
     * Retrieve topic to backend
     * 
     * @param proxy
     * @return
     */
    public static FeedbackTopic convertFeedBackTopicProxy2Topic(com.telenav.cserver.backend.datatypes.feedback.FeedbackTopic proxy)
    {
        if (proxy != null)
        {
            String type = proxy.getFeedbackTopic();
            if (type.equals(com.telenav.cserver.backend.datatypes.feedback.FeedbackTopic._BUSINESS_SEARCH))
            {
                return FeedbackTopic.BUSINESS_SEARCH;
            }
            else if (type.equals(com.telenav.cserver.backend.datatypes.feedback.FeedbackTopic._GENERAL))
            {
                return FeedbackTopic.GENERAL;
            }
            else if (type.equals(com.telenav.cserver.backend.datatypes.feedback.FeedbackTopic._NAVIGATION))
            {
                return FeedbackTopic.NAVIGATION;
            }
            else if (type.equals(com.telenav.cserver.backend.datatypes.feedback.FeedbackTopic._ROUTE_GENERATION))
            {
                return FeedbackTopic.ROUTE_GENERATION;
            }
            else if (type.equals(com.telenav.cserver.backend.datatypes.feedback.FeedbackTopic._TRAFFIC))
            {
                return FeedbackTopic.TRAFFIC;
            } else if (type.equals(com.telenav.cserver.backend.datatypes.feedback.FeedbackTopic._CANCELLATION))
            {
                return FeedbackTopic.CANCELLATION;
            }else if (type.equals(com.telenav.cserver.backend.datatypes.feedback.FeedbackTopic._OTHER))
            {
                return FeedbackTopic.OTHER;
            }
            
        }
        return FeedbackTopic.GENERAL;
    }

    /**
     * Convert feedback proxy array to backend type
     * 
     * @return
     */
    protected static Feedback convertFeedBackProxy2FeedBack(com.telenav.cserver.backend.datatypes.feedback.Feedback proxy)
    {
        if (proxy != null)
        {
            Feedback feedback = new Feedback();
            feedback.setFeedbackTime(proxy.getFeedbackTime());
            feedback.setId(proxy.getId());
            feedback.setPtn(proxy.getPtn());
            feedback.setTitle(proxy.getTitle());
            feedback.setTopic(convertFeedBackTopicProxy2Topic(proxy.getTopic()));
            feedback.setUserId(proxy.getUserId());
            feedback.setComment(convertCommentProxyArray2Comment(proxy.getComment()));
            feedback.setNavContext(convertNavContextProxy2NavContext(proxy.getNavContext()));
            return feedback;
        }
        return null;
    }

    /**
     * 
     * @param proxy
     * @return
     */
    protected static NavigationContext convertNavContextProxy2NavContext(
            com.telenav.cserver.backend.datatypes.feedback.NavigationContext proxy)
    {
        if (proxy != null)
        {
            NavigationContext context = new NavigationContext();
            context.setCurrentLocation(covertGeoCodeProxyArray2Geo(proxy.getCurrentLocation()));
            context.setDestination(convertGeoCodeProxyGeo(proxy.getDestination()));
            context.setDeviationFix(covertGeoCodeProxyArray2Geo(proxy.getDeviationFix()));
            context.setOriginFix(covertGeoCodeProxyArray2Geo(proxy.getOriginFix()));
            return context;
        }
        return null;
    }

    /**
     * transform the GeoCode array
     * 
     * @return
     */
    protected static GeoCode[] covertGeoCodeProxyArray2Geo(com.telenav.cserver.backend.datatypes.feedback.GeoCode[] proxy)
    {
        if (proxy != null)
        {
            int length = proxy.length;
            GeoCode[] arrGeo = new GeoCode[length];
            for (int index = 0; index < length; index++)
            {
                arrGeo[index] = convertGeoCodeProxyGeo(proxy[index]);
            }
            return arrGeo;
        }
        return null;
    }

    /**
     * transform the GeoCode object
     */
    protected static GeoCode convertGeoCodeProxyGeo(com.telenav.cserver.backend.datatypes.feedback.GeoCode proxy)
    {
        if (proxy != null)
        {
            GeoCode geo = new GeoCode();
            geo.setLatitude(proxy.getLatitude());
            geo.setLongitude(proxy.getLongitude());
            return geo;
        }
        return null;
    }

    /**
     * Convert the c- comment array to backend comment array
     * 
     * @param proxy
     * @return
     */
    protected static Comment[] convertCommentProxyArray2Comment(com.telenav.cserver.backend.datatypes.feedback.Comment[] proxy)
    {
        if (proxy != null)
        {
            int length = proxy.length;
            Comment[] comment = new Comment[length];
            for (int index = 0; index < length; index++)
            {
                comment[index] = convertCommentProxy2Comment(proxy[index]);
            }
            return comment;
        }
        return null;
    }

    /**
     * Convert the c- comment array to backend comment array
     * 
     * @param proxy
     * @return
     */
    protected static Comment20 convertCommentProxy2Comment(com.telenav.cserver.backend.datatypes.feedback.Comment proxy)
    {
        if (proxy != null)
        {
            Comment20 comment = new Comment20();
            comment.setCommentator(proxy.getCommentator());
            comment.setComments(proxy.getComments());
            comment.setCommentTime(proxy.getCommentTime());
            comment.setCommentTypeId(proxy.getCommentTypeId());
            comment.setId(proxy.getId());
            comment.setAnswerType(convertAnswerType2Proxy(proxy.getAnswerType()));
            comment.setBinaryData(proxy.getBinaryData());
            comment.setChoice(proxy.getChoice());
            comment.setMediumType(convertMediumType2Proxy(proxy.getMediumType()));
            return comment;
        }
        return null;
    }
    
    /**
     * COnvert the answer type
     * @param proxy
     * @return
     */
    public static MediumType convertMediumType2Proxy(com.telenav.cserver.backend.datatypes.feedback.MediumType proxy)
    {
        if (proxy != null)
        {
            String type = proxy.getMediumType();
            if (type.equals(MediumType._AUDIO))
            {
                return MediumType.AUDIO;
            }
            else if (type.equals(MediumType.IMAGE))
            {
                return MediumType.IMAGE;
            }
            else if (type.equals(MediumType._TEXT))
            {
                return MediumType.TEXT;
            }
           
        }
        return MediumType.TEXT;
    }
    
    /**
     * COnvert the answer type
     * @param proxy
     * @return
     */
    public static TextAnswerType convertAnswerType2Proxy(com.telenav.cserver.backend.datatypes.feedback.TextAnswerType proxy)
    {
        if (proxy != null)
        {
            String type = proxy.getTextAnswerType();
            if (type.equals(TextAnswerType._MULTIPLE))
            {
                return TextAnswerType.MULTIPLE;
            }
            else if (type.equals(TextAnswerType._SINGLE))
            {
                return TextAnswerType.SINGLE;
            }
            else if (type.equals(TextAnswerType._TEXT))
            {
                return TextAnswerType.TEXT;
            }
           
        }
        return TextAnswerType.TEXT;
    }
}
