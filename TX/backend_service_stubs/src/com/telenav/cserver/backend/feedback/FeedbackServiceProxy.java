/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.feedback;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.databinding.utils.ConverterUtil;
import org.apache.log4j.Logger;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.backend.config.WebServiceConfigInterface;
import com.telenav.cserver.backend.util.WebServiceConfiguration;
import com.telenav.cserver.backend.util.WebServiceUtils;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.cserver.framework.throttling.ThrottlingManager;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.ws.datatypes.feedback.FeedbackServiceRequestDTO;
import com.telenav.ws.datatypes.feedback.FeedbackServiceResponseDTO;
import com.telenav.ws.services.feedback.FeedbackFault;
import com.telenav.ws.services.feedback.FeedbackServiceStub;
import com.telenav.ws.services.feedback.GetFeedbackQuestionsRequestDTO;
import com.telenav.ws.services.feedback.GetFeedbackQuestionsResponseDTO;

/**
 * The service stub
 * 
 * @author zhjdou
 * 
 */
public class FeedbackServiceProxy
{
    final static Logger logger = Logger.getLogger(FeedbackServiceProxy.class);

    private final static String SERVICE_FEEDBACKSERVER = "FEEDBACKSERVER";

    public static final String _OK = ConverterUtil.convertToString("OK"); // status

    public static final String _SERVICE_ERROR = ConverterUtil.convertToString("SERVICE_ERROR"); // status

    private static FeedbackServiceProxy instance = new FeedbackServiceProxy();

    public final static String clientVersion = "1.0";

    public final static String clientName = "c-server";

    public final static String transactionId = "unknown";

    private final static String WS_FEEDBACK = "FEEDBACK";
    
    private static ConfigurationContext feedbackServiceContext = WebServiceUtils.createConfigurationContext(WS_FEEDBACK);

    private FeedbackServiceProxy()
    {
    }

    /**
     * return the singleton instance
     * 
     * @return
     */
    public static FeedbackServiceProxy getInstance()
    {
        return instance;
    }

    /**
     * Forward the request to backend service
     * 
     * @param request
     * @return
     * @throws FeedbackFault
     * @throws RemoteException
     */
    public GetFeedbackQuestionsResponse getFeedbackQuestions(GetFeedbackQuestionsRequest request, TnContext tc) throws ThrottlingException
    {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("Feedback");
        if (request != null && logger.isDebugEnabled())
        {
            logger.debug("GetFeedbackQuestionsRequest======>" + request.toString());
        }
        boolean startAPICall = false;// the flag whether can start call API
        GetFeedbackQuestionsResponse response = new GetFeedbackQuestionsResponse();
        GetFeedbackQuestionsResponseDTO qResponse = null;
        FeedbackServiceStub stub = null;
        try
        {
            startAPICall = ThrottlingManager.startAPICall(SERVICE_FEEDBACKSERVER, tc);
            if (!startAPICall)
            {
                throw new ThrottlingException();
            }
            try
            {
                stub = createStub();
                GetFeedbackQuestionsRequestDTO gRequest = FeedbackDataConvert.convertGetFeedBackQuestionRequestProxy2Request(request);
                cli.addData("Request", request.toString());
                qResponse = stub.getAllFeedbackQuestions(gRequest);
                cli.addData("Response", "status " + qResponse.getResponseStatus().getStatusCode() + " "
                        + qResponse.getResponseStatus().getStatusMessage());
                response = FeedbackDataConvert.convertGetFeedBackQuestionReponse2Proxy(qResponse);
                if (logger.isDebugEnabled())
                {
                    logger.debug("GetFeedbackQuestionsResponse======>" + response.toString());
                }
            }
            catch (Exception ex)
            {
                logger.fatal("FeedbackServiceProxy::getFeedbackQuestions()", ex);
            }
        }
        finally
        {
            cli.complete();
            if (startAPICall)// throttling
            {
                ThrottlingManager.endAPICall(SERVICE_FEEDBACKSERVER, tc);
            }
            WebServiceUtils.cleanupStub(stub);
        }
        return response;
    }

    /**
     * Forward the request to backend service
     * 
     * @param request
     * @return
     * @throws FeedbackFault
     * @throws RemoteException
     */
    public FeedbackServiceResponse createFeedBack(FeedbackServiceRequest request, TnContext tc) throws ThrottlingException
    {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("Feedback");
        if (request != null && logger.isDebugEnabled())
        {// log query request
            logger.debug("FeedbackServiceRequest======>" + request.toString());
        }
        boolean startAPICall = false;// the flag whether can start call API
        FeedbackServiceResponse response = new FeedbackServiceResponse();
        FeedbackServiceResponseDTO fbResponse = null;
        FeedbackServiceStub stub = null;
        try
        {
            startAPICall = ThrottlingManager.startAPICall(SERVICE_FEEDBACKSERVER, tc);
            if (!startAPICall)
            {
                throw new ThrottlingException();
            }
            try
            {
                stub = createStub();
                FeedbackServiceRequestDTO fbRequest = FeedbackDataConvert.convertFeedBackRequestProxy2Request(request);
                fbResponse = stub.createFeedback(fbRequest);// get response for services
                response = FeedbackDataConvert.convertFeedBackReponse2Proxy(fbResponse);
                cli.addData("Response", response.toString());
                if (logger.isDebugEnabled())
                {// log Feedback Service Response
                    logger.debug("FeedbackServiceResponse======>" + response.toString());
                }
            }
            catch (Exception ex)
            {
                logger.fatal("FeedbackServiceProxy::createFeedBack()", ex);
            }
        }
        finally
        {
            cli.complete();
            if (startAPICall)// throttling
            {
                ThrottlingManager.endAPICall(SERVICE_FEEDBACKSERVER, tc);
            }
            WebServiceUtils.cleanupStub(stub);
        }
        return response;
    }

    private FeedbackServiceStub createStub()
    {
        FeedbackServiceStub stub = null;
        try
        {
            WebServiceConfigInterface ws = WebServiceConfiguration.getInstance().getServiceConfig(WS_FEEDBACK);
            stub = new FeedbackServiceStub(feedbackServiceContext, ws.getServiceUrl());
            stub._getServiceClient().getOptions().setTimeOutInMilliSeconds(ws.getWebServiceItem().getWebServiceTimeout());
        }
        catch (AxisFault af)
        {
            logger.fatal("createStub()", af);
        }
        return stub;
    }


}
