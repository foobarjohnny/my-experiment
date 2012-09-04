/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.addresssharing;

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
import com.telenav.services.addresssharing.v10.AddressSharingInfoResponseDTO;
import com.telenav.services.addresssharing.v10.AddressSharingRequestDTO;
import com.telenav.services.addresssharing.v10.AddressSharingServiceStub;
import com.telenav.services.addresssharing.v10.LocationSharingRequestDTO;
import com.telenav.services.addresssharing.v10.PaginationRequestDTO;
import com.telenav.ws.datatypes.services.mgmt.ServiceMgmtResponseDTO;
/**
 * The service stub
 * 
 * @author pzhang
 * 
 */
public class AddressSharingServiceProxy
{
    final static Logger logger = Logger.getLogger(AddressSharingServiceProxy.class);

    private final static String SERVICE_ADDRESSSHARINGSERVER = "ADDRESSSHARINGSERVER";

    public static final String _OK = ConverterUtil.convertToString("OK"); // status

    public static final String _SERVICE_ERROR = ConverterUtil.convertToString("SERVICE_ERROR"); // status

    private static AddressSharingServiceProxy instance = new AddressSharingServiceProxy();

    public final static String clientVersion = "1.0";

    public final static String clientName = "c-server";

    public final static String transactionId = "unknown";

    private final static String WS_ADDRESSSHARING = "ADDRESSSHARING";
    
    private static ConfigurationContext serviceContext = WebServiceUtils.createConfigurationContext(WS_ADDRESSSHARING);

    private AddressSharingServiceProxy()
    {
    }

    /**
     * return the singleton instance
     * 
     * @return
     */
    public static AddressSharingServiceProxy getInstance()
    {
        return instance;
    }

    /**
     * Forward the request to backend service
     * 
     * @param request
     * @return
     * @throws RemoteException
     */
    public AddressSharingResponse shareAddress(AddressSharingRequest request, TnContext tc) throws ThrottlingException
    {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("AddressSharing");
        if (request != null && logger.isDebugEnabled())
        {
            logger.debug("AddressSharingRequest======>" + request.toString());
        }
        boolean startAPICall = false;// the flag whether can start call API
        AddressSharingResponse response = new AddressSharingResponse();
        ServiceMgmtResponseDTO qResponse = null;
        AddressSharingServiceStub stub = null;
        try
        {
            startAPICall = ThrottlingManager.startAPICall(SERVICE_ADDRESSSHARINGSERVER, tc);
            if (!startAPICall)
            {
                throw new ThrottlingException();
            }
            try
            {
                stub = createStub();
                AddressSharingRequestDTO gRequest = AddressSharingDataConvert.convertShareAddressRequestProxy2Request(request);
                cli.addData("Request", request.toString());
                qResponse = stub.shareAddress(gRequest);
                cli.addData("Response", "status " + qResponse.getResponseStatus().getStatusCode() + " "
                        + qResponse.getResponseStatus().getStatusMessage());
                response = AddressSharingDataConvert.convertShareAddressResponse2Proxy(qResponse);
                if (logger.isDebugEnabled())
                {
                    logger.debug("AddressSharingResponse======>" + response.toString());
                }
            }
            catch (Exception ex)
            {
                logger.fatal("AddressSharingServiceProxy::shareAddress()", ex);
            }
        }
        finally
        {
            cli.complete();
            if (startAPICall)// throttling
            {
                ThrottlingManager.endAPICall(SERVICE_ADDRESSSHARINGSERVER, tc);
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
     * @throws RemoteException
     */
    public FetchSharedAddressResponse fetchSharedAddress(FetchSharedAddressRequest request, TnContext tc) throws ThrottlingException
    {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("FetchSharedAddress");
        if (request != null && logger.isDebugEnabled())
        {
            logger.debug("FetchSharedAddressRequest======>" + request.toString());
        }
        boolean startAPICall = false;// the flag whether can start call API
        FetchSharedAddressResponse response = new FetchSharedAddressResponse();
        AddressSharingInfoResponseDTO qResponse = null;
        AddressSharingServiceStub stub = null;
        try
        {
            startAPICall = ThrottlingManager.startAPICall(SERVICE_ADDRESSSHARINGSERVER, tc);
            if (!startAPICall)
            {
                throw new ThrottlingException();
            }
            try
            {
                stub = createStub();
                PaginationRequestDTO gRequest = AddressSharingDataConvert.convertFetchSharedAddressRequestProxy2Request(request);
                cli.addData("Request", request.toString());
                qResponse = stub.fetchSharedAddress(gRequest);
                cli.addData("Response", "status " + qResponse.getResponseStatus().getStatusCode() + " "
                        + qResponse.getResponseStatus().getStatusMessage());
                response = AddressSharingDataConvert.convertFetchSharedAddressResponse2Proxy(qResponse);
                if (logger.isDebugEnabled())
                {
                    logger.debug("FetchSharedAddressResponse======>" + response.toString());
                }
            }
            catch (Exception ex)
            {
                logger.fatal("AddressSharingServiceProxy::fetchSharedAddress()", ex);
            }
        }
        finally
        {
            cli.complete();
            if (startAPICall)// throttling
            {
                ThrottlingManager.endAPICall(SERVICE_ADDRESSSHARINGSERVER, tc);
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
     * @throws RemoteException
     */
    public AddressSharingResponse shareMovie(LocationSharingRequest request, TnContext tc) throws ThrottlingException
    {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("SharingMovie");
        if (request != null && logger.isDebugEnabled())
        {
            logger.debug("LocationSharingRequest======>" + request.toString());
        }
        boolean startAPICall = false;// the flag whether can start call API
        AddressSharingResponse response = new AddressSharingResponse();
        ServiceMgmtResponseDTO qResponse = null;
        AddressSharingServiceStub stub = null;
        try
        {
            startAPICall = ThrottlingManager.startAPICall(SERVICE_ADDRESSSHARINGSERVER, tc);
            if (!startAPICall)
            {
                throw new ThrottlingException();
            }
            try
            {
                stub = createStub();
                LocationSharingRequestDTO gRequest = AddressSharingDataConvert.convertShareMovieRequestProxy2Request(request);
                cli.addData("Request", request.toString());
                qResponse = stub.shareLocation(gRequest);
                cli.addData("Response", "status " + qResponse.getResponseStatus().getStatusCode() + " "
                        + qResponse.getResponseStatus().getStatusMessage());
                response = AddressSharingDataConvert.convertShareAddressResponse2Proxy(qResponse);
                if (logger.isDebugEnabled())
                {
                    logger.debug("AddressSharingResponse======>" + response.toString());
                }
            }
            catch (Exception ex)
            {
                logger.fatal("AddressSharingServiceProxy::shareMovie()", ex);
            }
        }
        finally
        {
            cli.complete();
            if (startAPICall)// throttling
            {
                ThrottlingManager.endAPICall(SERVICE_ADDRESSSHARINGSERVER, tc);
            }
            WebServiceUtils.cleanupStub(stub);
        }
        return response;
    }
    
    private AddressSharingServiceStub createStub()
    {
    	AddressSharingServiceStub stub = null;
        try
        {
            WebServiceConfigInterface ws = WebServiceConfiguration.getInstance().getServiceConfig(WS_ADDRESSSHARING);
            stub = new AddressSharingServiceStub(serviceContext, ws.getServiceUrl());
            stub._getServiceClient().getOptions().setTimeOutInMilliSeconds(ws.getWebServiceItem().getWebServiceTimeout());
        }
        catch (AxisFault af)
        {
            logger.fatal("createStub()", af);
        }
        return stub;
    }


}
