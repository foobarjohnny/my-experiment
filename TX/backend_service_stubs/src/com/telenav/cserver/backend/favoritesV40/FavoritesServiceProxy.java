/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.favoritesV40;

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
import com.telenav.services.favorite.v20.AllRequestDTO;
import com.telenav.services.favorite.v20.AllResponseDTO;
import com.telenav.services.favorite.v20.BasicRequestDTO;
import com.telenav.services.favorite.v20.CountResponseDTO;
import com.telenav.services.favorite.v20.FavoriteMappingResponseDTO;
import com.telenav.services.favorite.v20.FavoriteServiceStub;
import com.telenav.services.favorite.v20.FavoritesAndCategoriesRequestDTO;
import com.telenav.services.favorite.v20.BasicResponseDTO;

/**
 * Favourite Proxy
 * 
 * @author zhjdou 2010-2-21
 */
public class FavoritesServiceProxy
{
    private final static Logger logger = Logger.getLogger(FavoritesServiceProxy.class);

    private final static String SERVICE_FAVORITESERVER = "FAVORITESERVER";

    private static FavoritesServiceProxy service = new FavoritesServiceProxy();

    public final static String clientName = "favorite";

    public final static String clientVersion = "1.0";

    public final static String transactionId = "";
    
    private final static String WS_FAVORITES = "FAVORITE_v20";

    private static ConfigurationContext favoritesServiceContext = WebServiceUtils.createConfigurationContext(WS_FAVORITES);

    // service stub, initial it first time.
    // private static FavoriteServiceStub stub = getStub();

    private FavoritesServiceProxy()
    {
    }

    /**
     * singleton
     * 
     * @return
     */
    public static FavoritesServiceProxy getInstance()
    {
        return service;
    }

    /**
     * Initial the stub once
     * 
     * @return
     */
    /*
     * private static FavoriteServiceStub getStub() { try { WebServiceConfigInterface ws =
     * WebServiceConfiguration.getInstance().getServiceConfig("FAVORITE"); stub = new
     * FavoriteServiceStub(WebServiceManager.createNewContext(ws.getWebServiceItem()), ws.getServiceUrl());
     * stub._getServiceClient().getOptions().setTimeOutInMilliSeconds(ws.getWebServiceItem().getWebServiceTimeout()); }
     * catch (AxisFault af) { logger.fatal(af, af); } return stub; }
     */

    /**
     * Fetch all favorites and category
     * 
     * @param request
     * @return
     * @throws ThrottlingException
     */
    public QueryFavoritesResponse getFavoriteCategory(QueryFavoritesRequest request, TnContext tc) throws ThrottlingException
    {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("Fetch_Favorites");
        if (request != null && logger.isDebugEnabled())
        {
            logger.debug("FetchfavsRequest===>" + request.toString());
        }

        boolean startAPICall = false;// the flag whether can start call API
        QueryFavoritesResponse response = null;
        FavoriteServiceStub stub = null;
        try
        {
            startAPICall = ThrottlingManager.startAPICall(SERVICE_FAVORITESERVER, tc);
            if (!startAPICall)
            {
                throw new ThrottlingException();
            }
            try
            {
                stub = createStub();
                // transform the request
                BasicRequestDTO bRequest = FavoritesDataConvert.convertProxy2FavoritesRequest(request);
                // cli log for stub request
                cli.addData("fetch_favs_req", request.toString());
                // sync response
                FavoriteMappingResponseDTO fResponse = stub.getAllNodesAndMapping(bRequest);
                response = FavoritesDataConvert.convertFavoritesResponse2Proxy(fResponse);
                // cli log for stub response
                cli.addData("fetch_favs_resq", response.toString());
                if (logger.isDebugEnabled())
                {
                    logger.debug("fetchFav_resp======>" + response.toString());
                }
            }
            catch (Exception ex)
            {
                logger.fatal("fav_excep", ex);
            }
        }
        finally
        {
            cli.complete();
            if (startAPICall)
            {
                ThrottlingManager.endAPICall(SERVICE_FAVORITESERVER, tc);
            }
            WebServiceUtils.cleanupStub(stub);
        }
        return response;
    }

    /**
     * Fetch all favorites and category
     * 
     * @param request
     * @return
     * @throws ThrottlingException
     */
    public CountReceivedAddressResponse countNewlyReceivedAddresses(CountReceivedAddressRequest request, TnContext tc)
            throws ThrottlingException
    {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("Count_ReceivedAddress");
        if (request != null && logger.isDebugEnabled())
        {
            logger.debug("Count_ReceivedAddress===>" + request.toString());
        }

        boolean startAPICall = false;// the flag whether can start call API
        CountReceivedAddressResponse response = null;
        FavoriteServiceStub stub = null;
        try
        {
            startAPICall = ThrottlingManager.startAPICall(SERVICE_FAVORITESERVER, tc);
            if (!startAPICall)
            {
                throw new ThrottlingException();
            }
            try
            {
                stub = createStub();
                // transform the request
                BasicRequestDTO bRequest = FavoritesDataConvert.convertProxy2CountFavoritesRequest(request);
                // cli log for stub request
                cli.addData("count_receive_req", request.toString());
                CountResponseDTO cResponse = stub.countNewlyReceivedAddresses(bRequest);
                // CountResponseDTO cResponse = new CountResponseDTO();
                response = FavoritesDataConvert.convertCountFavoritesResponse2Proxy(cResponse);
                // cli log for stub response
                cli.addData("count_receive_resq", response.toString());
                if (logger.isDebugEnabled())
                {
                    logger.debug("count_receive_resq======>" + response.toString());
                }
            }
            catch (Exception ex)
            {
                logger.fatal("fav_excep", ex);
            }
        }
        finally
        {
            cli.complete();
            if (startAPICall)
            {
                ThrottlingManager.endAPICall(SERVICE_FAVORITESERVER, tc);
            }
            WebServiceUtils.cleanupStub(stub);
        }
        return response;
    }

    /**
     * sync favorites and category
     * 
     * @param request
     * @return
     * @throws ThrottlingException
     */
    public SyncFavoritesResponse syncFavoriteCategory(SyncFavoritesRequest request, TnContext tc) throws ThrottlingException
    {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("Sync_Favorites");
        if (request != null && logger.isDebugEnabled())
        {
            logger.debug("SyncfavsRequest===>" + request.toString());
        }

        boolean startAPICall = false;
        SyncFavoritesResponse response = null;
        FavoriteServiceStub stub = null;
        try
        {
            startAPICall = ThrottlingManager.startAPICall(SERVICE_FAVORITESERVER, tc);
            if (!startAPICall)
            {
                throw new ThrottlingException();
            }
            try
            {
                stub = createStub();
                // transform the request
                AllRequestDTO sRequest = FavoritesDataConvert.convertProxy2SyncFavoritesRequest(request);
                // cli log for stub request
                cli.addData("sync_favs_req", request.toString());
                // sync response
                AllResponseDTO sResponse = stub.sync(sRequest);
                response = FavoritesDataConvert.convertSyncFavoritesResponse2Proxy(sResponse);
                // cli log for stub response
                cli.addData("sync_favs_resq", response.toString());
                if (logger.isDebugEnabled())
                {
                    logger.debug("syncFav_resp======>" + response.toString());
                }
            }
            catch (Exception ex)
            {
                logger.fatal("sync_fav_excep", ex);
            }
        }
        finally
        {
            cli.complete();
            if (startAPICall)
            {
                ThrottlingManager.endAPICall(SERVICE_FAVORITESERVER, tc);
            }
            WebServiceUtils.cleanupStub(stub);
        }
        return response;
    }

    /**
     * Confirm the sync response was received by client the protocol
     * 
     * @return
     */
    public FavoriteConfirmResponse confirmReceived(FavoriteConfirmRequest cRequest, TnContext tc) throws ThrottlingException
    {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("confirm_Favorites");
        if (cRequest != null && logger.isDebugEnabled())
        {// log query request
            logger.debug("ConfirmRequest======>" + cRequest.toString());
        }
        boolean startAPICall = false;// the flag whether can start call API
        FavoriteConfirmResponse cResponse = new FavoriteConfirmResponse();
        FavoriteServiceStub stub = null;
        try
        {
            startAPICall = ThrottlingManager.startAPICall(SERVICE_FAVORITESERVER, tc);
            if (!startAPICall)
            {
                throw new ThrottlingException();
            }
            try
            {
                stub = createStub();
                FavoritesAndCategoriesRequestDTO request = FavoritesDataConvert.convertConfirmReuqestProxy2ConfirmRequest(cRequest);
                cli.addData("sync_favs_req", cRequest.toString());
                BasicResponseDTO response = stub.confirmedReceived(request);
                cResponse = FavoritesDataConvert.convertConfirmResponse2ProxyResponse(response);
                cli.addData("sync_favs_resq", cResponse.toString());
                if (logger.isDebugEnabled())
                {// log query response
                    logger.debug("ConfirmReponse=====>" + cResponse.toString());
                }
            }
            catch (Exception ex)
            {
                logger.fatal("confirm_excep", ex);
            }
        }
        finally
        {
            if (startAPICall)// throttling
            {
                ThrottlingManager.endAPICall(SERVICE_FAVORITESERVER, tc);
            }
            WebServiceUtils.cleanupStub(stub);
        }
        return cResponse;
    }


    private FavoriteServiceStub createStub()
    {
        FavoriteServiceStub stub = null;
        try
        {
            WebServiceConfigInterface ws = WebServiceConfiguration.getInstance().getServiceConfig(WS_FAVORITES);
            stub = new FavoriteServiceStub(favoritesServiceContext, ws.getServiceUrl());
            stub._getServiceClient().getOptions().setTimeOutInMilliSeconds(ws.getWebServiceItem().getWebServiceTimeout());
        }
        catch (AxisFault af)
        {
            logger.fatal("createStub", af);
        }
        return stub;
    }

}
