/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.recentstop;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.log4j.Logger;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.backend.config.WebServiceConfigInterface;
import com.telenav.cserver.backend.util.WebServiceConfiguration;
import com.telenav.cserver.backend.util.WebServiceUtils;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.cserver.framework.throttling.ThrottlingManager;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.services.recentstop.v20.AllRequestDTO;
import com.telenav.services.recentstop.v20.AllResponseDTO;
import com.telenav.services.recentstop.v20.DeleteRequestDTO;
import com.telenav.services.recentstop.v20.QueryRequestDTO;
import com.telenav.services.recentstop.v20.RecentStopsRequestDTO;
import com.telenav.services.recentstop.v20.RecentStopsResponseDTO;
import com.telenav.services.recentstop.v20.RecentstopServiceStub;
import com.telenav.ws.datatypes.services.mgmt.ServiceMgmtResponseDTO;

/**
 * @author zhjdou
 *         <p>
 *         Currently, client is calling the API of sync(...) in xnav client to sync the changes in client to server and
 *         get back the changes done by webiste. So in TN60, Cserver needs switch to call Recentstop webservice instead
 *         of xnav client to do sync.
 *         </P>
 * <br>
 *         <p>
 *         Set a proxy that can receive request from input and build up the response in given format between the client
 *         and backend services.
 *         </p>
 */
public class RecentStopsServiceProxy
{
    private final static Logger logger = Logger.getLogger(RecentStopsServiceProxy.class);

    private final static String SERVICE_RECENTSTOPSERVER = "RECENTSTOPSERVER";

    private static RecentStopsServiceProxy instance = new RecentStopsServiceProxy();

    public static final String _OK = "0"; // status success

    public final static String clientName = "recentstop";//

    public final static String clientVersion = "2.0";//

    public final static String transactionId = "";//
    
    public final static String WS_RECENTSTOP = "RECENTSTOP";

    private static ConfigurationContext recentStopsServiceContext = WebServiceUtils.createConfigurationContext(WS_RECENTSTOP);

    // service stub, inital it first time.
    // private static RecentstopServiceStub stub = getStub();

    private RecentStopsServiceProxy()
    {
    }

    /**
     * singleton
     * 
     * @return
     */
    public static RecentStopsServiceProxy getInstance()
    {
        return instance;
    }

    /**
     * inital the stub once
     * 
     * @return
     */
    /*
     * private static RecentstopServiceStub getStub() { try { WebServiceConfigInterface ws =
     * WebServiceConfiguration.getInstance().getServiceConfig("RECENTSTOP"); stub = new
     * RecentstopServiceStub(WebServiceManager.createNewContext(ws.getWebServiceItem()), ws.getServiceUrl());
     * stub._getServiceClient().getOptions().setTimeOutInMilliSeconds(ws.getWebServiceItem().getWebServiceTimeout()); }
     * catch (AxisFault af) { logger.fatal("RecentStopsServiceProxy::getStub()", af); }
     * 
     * return stub; }
     */

    /**
     * Record the cli log for sync recent stops request
     * 
     * @param cli
     * @param request
     */
    public void recoredRecentStopsRequestCli(CliTransaction cli, AllRequestDTO request)
    {
        if (request != null)
        {
            StringBuilder sb = new StringBuilder();
            sb.append("Sync req[");
            sb.append(", NeedPoi" + request.getNeedPoiInfo());
            sb.append(",Add id=");
            if (request.getAddedRecentStops() != null)
            {
                for (int i = 0; i < request.getAddedRecentStops().length; i++)
                {
                    sb.append(request.getAddedRecentStops()[i].getRecentStopId() + ",");
                }
            }
            sb.append(", Update id=");
            if (request.getUpdatedRecentStops() != null)
            {
                for (int i = 0; i < request.getUpdatedRecentStops().length; i++)
                {
                    sb.append(request.getUpdatedRecentStops()[i].getRecentStopId() + ",");
                }
            }
            sb.append(", Delete id=");
            if (request.getDeletedRecentStops() != null)
            {
                for (int i = 0; i < request.getDeletedRecentStops().length; i++)
                {
                    sb.append(request.getDeletedRecentStops()[i].getRecentStopId() + ",");
                }
            }
            cli.addData("request: ", sb.toString());
        }
    }

    /**
     * Record the cli log for sync recent stops response
     * 
     * @param cli
     * @param request
     */
    public void recoredRecentStopsReponseCli(CliTransaction cli, AllResponseDTO response)
    {
        if (response != null)
        {
            StringBuilder sb = new StringBuilder();
            sb.append("Sync Resp[");
            sb.append("status= " + response.getResponseStatus().getStatusCode());
            sb.append(", message= " + response.getResponseStatus().getStatusMessage());
            sb.append(", Insert id=");
            if (response.getInsertedRecentStops() != null)
            {
                for (int i = 0; i < response.getInsertedRecentStops().length; i++)
                {
                    sb.append(response.getInsertedRecentStops()[i].getRecentStopId() + ",");
                }
            }
            // delete stops array will set the first as null even no stop return.
            sb.append(", Delete id=");
            if (response.getDeletedRecentStops() != null && response.getDeletedRecentStops()[0] != null)
            {
                for (int i = 0; i < response.getDeletedRecentStops().length; i++)
                {
                    sb.append(response.getDeletedRecentStops()[i].getRecentStopId() + ",");
                }
            }

            cli.addData("response: ", sb.toString());
        }
    }

    /**
     * Record the cli log for fetch recent stops response
     * 
     * @param cli
     * @param rsResponse
     */
    public void recordFetchRecentStopsResponse(CliTransaction cli, RecentStopsResponseDTO response)
    {
        if (response != null)
        {
            StringBuilder sb = new StringBuilder();
            sb.append("Fetch Resp[");
            sb.append("status= " + response.getResponseStatus().getStatusCode());
            sb.append(", message= " + response.getResponseStatus().getStatusMessage());
            sb.append(",Stop id=");
            if (response.getRecentStops() != null)
            {
                for (int i = 0; i < response.getRecentStops().length; i++)
                {
                    sb.append(response.getRecentStops()[i].getRecentStopId() + ",");
                }
            }
            cli.addData("response: ", sb.toString());
        }
    }

    /**
     * The mobile phone Synchronizes recent stops with the server, to update the client's side recent stops, which will
     * store the newly added recent stops added from mobile client, and update those recent stops that have been
     * modified in the mobile client, and delete those recent stops that have been deleted in the mobile client. And
     * return the newest 100 recent stops including added from website, voice and mobile client. Convert the request to
     * original type, the service can handle it,tansmit the request to Service stub and get the response
     * 
     * @param userId
     * @param createdStops
     * @param updateStops
     * @param deletedStops
     * @param lastSyncTime
     * @param contextString
     * @throws AxisFault
     */
    public RecentStopsResponse syncRecentStops(RecentStopsRequest request, TnContext tc) throws ThrottlingException
    {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("Sync_Recent_Stops");

        if (request != null && logger.isDebugEnabled())
        {// log query request
            logger.debug("SyncRequest======>" + request.toString());
        }
        boolean startAPICall = false;// the flag whether can start call API
        RecentStopsResponse response = null;
        RecentstopServiceStub stub = null;
        try
        {
            startAPICall = ThrottlingManager.startAPICall(SERVICE_RECENTSTOPSERVER, tc);
            if (!startAPICall)
            {
                throw new ThrottlingException();
            }
            try
            {
                stub = createStub();
                // transform the request
                AllRequestDTO rsRequest = RecentStopsDataConvert.convertProxy2RecentStopRequest(request);
                // cli log for stub request
                recoredRecentStopsRequestCli(cli, rsRequest);
                // sync response
                AllResponseDTO rsResponse;
                rsResponse = stub.sync(rsRequest);
                // cli log for stub response
                recoredRecentStopsReponseCli(cli, rsResponse);
                // confirm after sync
                response = RecentStopsDataConvert.convertDTOResponse2Proxy(rsResponse);
                if (logger.isDebugEnabled())
                {// log query request
                    logger.debug("Syncresponse======>" + response.toString());
                }
            }
            catch (Exception ex)
            {
                logger.fatal("RecentStopsServiceProxy::syncRecentStops()", ex);
            }
        }
        finally
        {
            cli.complete();
            if (startAPICall)// throttling
            {
                ThrottlingManager.endAPICall(SERVICE_RECENTSTOPSERVER, tc);
            }
            WebServiceUtils.cleanupStub(stub);
        }
        return response;
    }

    /**
     * Comfirm the sync response was received by client the protocol
     * 
     * @return
     */
    public ConfirmResponse confirmReceived(ConfirmRequest cRequest, TnContext tc) throws ThrottlingException
    {
        if (cRequest != null && logger.isDebugEnabled())
        {// log query request
            logger.debug("ConfirmRequest======>" + cRequest.toString());
        }
        boolean startAPICall = false;// the flag whether can start call API
        ConfirmResponse cResponse = new ConfirmResponse();
        RecentstopServiceStub stub = null;
        try
        {
            startAPICall = ThrottlingManager.startAPICall(SERVICE_RECENTSTOPSERVER, tc);
            if (!startAPICall)
            {
                throw new ThrottlingException();
            }
            try
            {
                stub = createStub();
                RecentStopsRequestDTO request = RecentStopsDataConvert.convertConfirmReuqestProxy2ConfirmRequest(cRequest);
                ServiceMgmtResponseDTO response = stub.confirmReceived(request);
                cResponse = RecentStopsDataConvert.convertConfirmResponse2ProxyResponse(response);
                if (logger.isDebugEnabled())
                {// log query response
                    logger.debug("ConfirmReponse=====>" + cResponse.toString());
                }
            }
            catch (Exception ex)
            {
                logger.fatal("RecentStopsServiceProxy::confirmReceived()", ex);
            }
        }
        finally
        {
            if (startAPICall)// throttling
            {
                ThrottlingManager.endAPICall(SERVICE_RECENTSTOPSERVER, tc);
            }
            WebServiceUtils.cleanupStub(stub);
        }
        return cResponse;
    }

    /**
     * Comfirm the sync response was received by client the protocol
     * 
     * @return
     */
    public DeleteStopsResponse DeleteStops(DeleteStopsRequest request, TnContext tc) throws ThrottlingException
    {
        if (request != null && logger.isDebugEnabled())
        {// log delete request
            logger.debug("DeleteStops Request======>" + request.toString());
        }
        boolean startAPICall = false;// the flag whether can start call API
        DeleteStopsResponse dResponse = new DeleteStopsResponse();
        RecentstopServiceStub stub = null;
        try
        {
            startAPICall = ThrottlingManager.startAPICall(SERVICE_RECENTSTOPSERVER, tc);
            if (!startAPICall)
            {
                throw new ThrottlingException();
            }
            try
            {
                stub = createStub();
                DeleteRequestDTO req = RecentStopsDataConvert.convertDeleteReuqestProxy2DeleteRequest(request);
                ServiceMgmtResponseDTO response = stub.delete(req);
                dResponse = RecentStopsDataConvert.convertDeleteResponse2ProxyResponse(response);
                if (logger.isDebugEnabled())
                {// log query response
                    logger.debug("DeleteStops Reponse=====>" + dResponse.toString());
                }
            }
            catch (Exception ex)
            {
                logger.fatal("RecentStopsServiceProxy::DeleteStops()", ex);
            }
        }
        finally
        {
            if (startAPICall)// throttling
            {
                ThrottlingManager.endAPICall(SERVICE_RECENTSTOPSERVER, tc);
            }
            WebServiceUtils.cleanupStub(stub);
        }
        return dResponse;
    }

    /**
     * retrieve the recent stop by one userid
     * 
     * @param request
     * @return
     */
    public QueryResponse getRecentStops(QueryRequest request, TnContext tc) throws ThrottlingException
    {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("Fetch_Recent_Stops");
        boolean startAPICall = false;// the flag whether can start call API
        if (request != null && logger.isDebugEnabled())
        {// log query request
            logger.debug("QueryRequest======>" + request.toString());
        }
        QueryResponse response = null;
        RecentstopServiceStub stub = null;
        try
        {
            startAPICall = ThrottlingManager.startAPICall(SERVICE_RECENTSTOPSERVER, tc);
            if (!startAPICall)
            {
                throw new ThrottlingException();
            }
            try
            {
                stub = createStub();
                // transform the request
                QueryRequestDTO rsRequest = RecentStopsDataConvert.convertProxy2QueryRequest(request);
                // response
                RecentStopsResponseDTO rsResponse = stub.query(rsRequest);
                // record log for stub response
                recordFetchRecentStopsResponse(cli, rsResponse);
                // confirm after sync
                response = RecentStopsDataConvert.convertQueryResponse2Proxy(rsResponse);
                if (logger.isDebugEnabled())
                {// log query response
                    logger.debug("QueryReponse=====>" + response.toString());
                }
            }
            catch (Exception ex)
            {
                logger.fatal("RecentStopsServiceProxy::getRecentStops()", ex);
            }
        }
        finally
        {
            cli.complete();
            if (startAPICall)// throttling
            {
                ThrottlingManager.endAPICall(SERVICE_RECENTSTOPSERVER, tc);
            }
            WebServiceUtils.cleanupStub(stub);
        }
        return response;
    }

    private RecentstopServiceStub createStub()
    {
        RecentstopServiceStub stub = null;
        try
        {
            WebServiceConfigInterface ws = WebServiceConfiguration.getInstance().getServiceConfig(WS_RECENTSTOP);
            stub = new RecentstopServiceStub(recentStopsServiceContext, ws.getServiceUrl());
            stub._getServiceClient().getOptions().setTimeOutInMilliSeconds(ws.getWebServiceItem().getWebServiceTimeout());
        }
        catch (AxisFault af)
        {
            logger.fatal("createStub()", af);
        }
        return stub;
    }

   
}