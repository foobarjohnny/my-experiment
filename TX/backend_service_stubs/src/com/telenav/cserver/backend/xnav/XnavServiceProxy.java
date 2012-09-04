/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.xnav;

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
import com.telenav.services.xnav.v10.BrandNamesResponseDTO;
import com.telenav.services.xnav.v10.CountryRequestDTO;
import com.telenav.services.xnav.v10.PtnsAndGroupsRequestDTO;
import com.telenav.services.xnav.v10.MessageAndPtnsRequestDTO;
import com.telenav.services.xnav.v10.UserRequestDTO;
import com.telenav.services.xnav.v10.XnavServiceStub;
import com.telenav.services.xnav.v10.XnavFault;
import com.telenav.ws.datatypes.services.mgmt.ServiceMgmtResponseDTO;
/**
 * The service stub
 * 
 * @author pzhang
 * 
 */
public class XnavServiceProxy
{
    final static Logger logger = Logger.getLogger(XnavServiceProxy.class);

    private final static String SERVICE_XNAVSERVER = "XNAVSERVER";

    public static final String _OK = ConverterUtil.convertToString("OK"); // status

    public static final String _SERVICE_ERROR = ConverterUtil.convertToString("SERVICE_ERROR"); // status

    private static XnavServiceProxy instance = new XnavServiceProxy();

    public final static String clientVersion = "1.0";

    public final static String clientName = "c-server";

    public final static String transactionId = "unknown";

    private final static String WS_XNAV = "XNAV";
    
    private static ConfigurationContext xnavServiceContext = WebServiceUtils.createConfigurationContext(WS_XNAV);

    private XnavServiceProxy()
    {
    }

    /**
     * return the singleton instance
     * 
     * @return
     */
    public static XnavServiceProxy getInstance()
    {
        return instance;
    }

    /**
     * Forward the request to backend service
     * 
     * @param request
     * @return
     * @throws XnavFault
     * @throws RemoteException
     */
    public ResendPinResponse resendPin(ResendPinRequest request, TnContext tc) throws ThrottlingException
    {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("resendPin");
        if (request != null && logger.isDebugEnabled())
        {
            logger.debug("ResendPinRequest======>" + request.toString());
        }
        boolean startAPICall = false;// the flag whether can start call API
        ResendPinResponse response = new ResendPinResponse();
        ServiceMgmtResponseDTO qResponse = null;
        XnavServiceStub stub = null;
        try
        {
            startAPICall = ThrottlingManager.startAPICall(SERVICE_XNAVSERVER, tc);
            if (!startAPICall)
            {
                throw new ThrottlingException();
            }
            try
            {
                stub = createStub();
                UserRequestDTO gRequest = XnavDataConvert.convertResendPinRequestProxy2Request(request);
                cli.addData("Request", request.toString());
                qResponse = stub.resendPin(gRequest);
                cli.addData("Response", "status " + qResponse.getResponseStatus().getStatusCode() + " "
                        + qResponse.getResponseStatus().getStatusMessage());
                response = XnavDataConvert.convertResendPinResponse2Proxy(qResponse);
                if (logger.isDebugEnabled())
                {
                    logger.debug("ResendPinResponse======>" + response.toString());
                }
            }
            catch (Exception ex)
            {
                logger.fatal("XnavServiceProxy::resendPin()", ex);
            }
        }
        finally
        {
            cli.complete();
            if (startAPICall)// throttling
            {
                ThrottlingManager.endAPICall(SERVICE_XNAVSERVER, tc);
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
     * @throws XnavFault
     * @throws RemoteException
     */
    public ReferToFriendResponse refer2Friends(ReferToFriendRequest request, TnContext tc) throws ThrottlingException
    {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("refer2Friends");
        if (request != null && logger.isDebugEnabled())
        {
            logger.debug("ReferToFriendRequest======>" + request.toString());
        }
        boolean startAPICall = false;// the flag whether can start call API
        ReferToFriendResponse response = new ReferToFriendResponse();
        ServiceMgmtResponseDTO qResponse = null;
        XnavServiceStub stub = null;
        try
        {
            startAPICall = ThrottlingManager.startAPICall(SERVICE_XNAVSERVER, tc);
            if (!startAPICall)
            {
                throw new ThrottlingException();
            }
            try
            {
                stub = createStub();
                PtnsAndGroupsRequestDTO gRequest = XnavDataConvert.convertReferToFriendRequestProxy2Request(request);
                cli.addData("Request", request.toString());
                qResponse = stub.refer2Friends(gRequest);
                cli.addData("Response", "status " + qResponse.getResponseStatus().getStatusCode() + " "
                        + qResponse.getResponseStatus().getStatusMessage());
                response = XnavDataConvert.convertReferToFriendResponse2Proxy(qResponse);
                if (logger.isDebugEnabled())
                {
                    logger.debug("ReferToFriendResponse======>" + response.toString());
                }
            }
            catch (Exception ex)
            {
                logger.fatal("XnavServiceProxy::refer2Friends()", ex);
            }
        }
        finally
        {
            cli.complete();
            if (startAPICall)// throttling
            {
                ThrottlingManager.endAPICall(SERVICE_XNAVSERVER, tc);
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
     * @throws XnavFault
     * @throws RemoteException
     */
    public SendETAResponse sendEta(SendETARequest request, TnContext tc) throws ThrottlingException
    {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("sendEta");
        if (request != null && logger.isDebugEnabled())
        {
            logger.debug("SendETARequest======>" + request.toString());
        }
        boolean startAPICall = false;// the flag whether can start call API
        SendETAResponse response = new SendETAResponse();
        ServiceMgmtResponseDTO qResponse = null;
        XnavServiceStub stub = null;
        try
        {
            startAPICall = ThrottlingManager.startAPICall(SERVICE_XNAVSERVER, tc);
            if (!startAPICall)
            {
                throw new ThrottlingException();
            }
            try
            {
                stub = createStub();
                MessageAndPtnsRequestDTO gRequest = XnavDataConvert.convertSendToETARequestProxy2Request(request);
                cli.addData("Request", request.toString());
                qResponse = stub.sendEta(gRequest);
                cli.addData("Response", "status " + qResponse.getResponseStatus().getStatusCode() + " "
                        + qResponse.getResponseStatus().getStatusMessage());
                response = XnavDataConvert.convertSendToETAResponse2Proxy(qResponse);
                if (logger.isDebugEnabled())
                {
                    logger.debug("SendETAResponse======>" + response.toString());
                }
            }
            catch (Exception ex)
            {
                logger.fatal("XnavServiceProxy::sendEta()", ex);
            }
        }
        finally
        {
            cli.complete();
            if (startAPICall)// throttling
            {
                ThrottlingManager.endAPICall(SERVICE_XNAVSERVER, tc);
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
     * @throws XnavFault
     * @throws RemoteException
     */
    public FetchBrandNamesResponse fetchBrandNames(FetchBrandNamesRequest request, TnContext tc) throws ThrottlingException
    {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("fetchBrandNames");
        if (request != null && logger.isDebugEnabled())
        {
            logger.debug("FetchBrandNamesResponse======>" + request.toString());
        }
        boolean startAPICall = false;// the flag whether can start call API
        FetchBrandNamesResponse response = new FetchBrandNamesResponse();
        BrandNamesResponseDTO qResponse = null;
        XnavServiceStub stub = null;
        try
        {
            startAPICall = ThrottlingManager.startAPICall(SERVICE_XNAVSERVER, tc);
            if (!startAPICall)
            {
                throw new ThrottlingException();
            }
            try
            {
                stub = createStub();
                CountryRequestDTO gRequest = XnavDataConvert.convertFetchBrandNamesProxy2Request(request);
                cli.addData("Request", request.toString());
                qResponse = stub.fetchBrandNames(gRequest);
                cli.addData("Response", "status " + qResponse.getResponseStatus().getStatusCode() + " "
                        + qResponse.getResponseStatus().getStatusMessage());
                response = XnavDataConvert.convertFetchBrandNamesResponse2Proxy(qResponse);
                if (logger.isDebugEnabled())
                {
                    logger.debug("FetchBrandNamesResponse======>" + response.toString());
                }
            }
            catch (Exception ex)
            {
                logger.fatal("XnavServiceProxy::fetchBrandNames()", ex);
            }
        }
        finally
        {
            cli.complete();
            if (startAPICall)// throttling
            {
                ThrottlingManager.endAPICall(SERVICE_XNAVSERVER, tc);
            }
            WebServiceUtils.cleanupStub(stub);
        }
        return response;
    }
    
    /**
     * 
     * @return
     */
    private XnavServiceStub createStub()
    {
    	XnavServiceStub stub = null;
        try
        {
            WebServiceConfigInterface ws = WebServiceConfiguration.getInstance().getServiceConfig(WS_XNAV);
            stub = new XnavServiceStub(xnavServiceContext, ws.getServiceUrl());
            stub._getServiceClient().getOptions().setTimeOutInMilliSeconds(ws.getWebServiceItem().getWebServiceTimeout());
        }
        catch (AxisFault af)
        {
            logger.fatal("createStub()", af);
        }
        return stub;
    }


}
