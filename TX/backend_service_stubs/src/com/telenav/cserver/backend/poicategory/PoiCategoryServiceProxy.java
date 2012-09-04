/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.poicategory;

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
import com.telenav.services.xnav.v10.PoiCategoriesResponseDTO;
import com.telenav.services.xnav.v10.PoiCategoryRequestDTO;
import com.telenav.services.xnav.v10.XnavServiceStub;


/**
 * Retrieve POI category from Xnav web service
 * <li>
 * the proxy encapsulate x-nav service in tn-kernal-stub jar that more than poi category, if need we can talk about divide
 * package by module or functional
 * </li>
 * <li>
 * The class major contain two functionals:<code>fetchPoiCategories</code> and <code>fetchSubPoiCategories</code>
 * </br>
 * <code>fetchPoiCategories</code> provide the <strong>category</strong> details depend on the category id in the request <code>PoiCategoryRequest</code>.
 * </br>
 * <code>fetchPoiCategories</code> provide the <strong>sub category</strong> details depend on the category id in the request <code>PoiCategoryRequest</code>.
 * </li>
 * @author zhjdou
 * 2010-4-27
 */
public class PoiCategoryServiceProxy
{   
    //logger for debug
    private final static Logger logger=Logger.getLogger(PoiCategoryServiceProxy.class);
    
    public final static String response_status="OK";
    
    private final static String SERVICE_POICATEGORY = "POICATEGORY";
    
    private static PoiCategoryServiceProxy instance=new PoiCategoryServiceProxy();
    
    protected final static String clientVersion = "1.0";

    protected final static String clientName = "category";

    protected final static String transactionId = "";
    
    private final static String WS_POICATEGORY = "POICATEGORY";
    
    private static ConfigurationContext poiCategoryServiceContext = WebServiceUtils.createConfigurationContext(WS_POICATEGORY); 
    
    
    //private constructor for singleton 
    private PoiCategoryServiceProxy() {
        
    }
    
    
    /**
     * return the singleton instance
     * @return PoiCategoryServiceProxy
     */
    public static PoiCategoryServiceProxy getInstance()
    {
        return instance;
    }
    
    /**
     * Fetch POI category, notice parameters in the request
     * @param request
     * @param tc for throttling module
     * @return
     * @throws ThrottlingException
     */
    public PoiCategoryResponse fetchPoiCategories(PoiCategoryRequest request,TnContext tc) throws ThrottlingException {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("PoiCategory");
        if (request != null && logger.isDebugEnabled())
        {
            logger.debug("fetchPoiCategoriesRequest======>" + request.toString());
        }
        boolean startAPICall = false;// the flag whether can start call API
        PoiCategoryResponse response=new PoiCategoryResponse();
        PoiCategoriesResponseDTO poiResponse = null;
        XnavServiceStub stub = null;
        try
        {
            startAPICall = ThrottlingManager.startAPICall(SERVICE_POICATEGORY, tc);
            if (!startAPICall)
            {
                throw new ThrottlingException();
            }
            try
            {
                 stub = createStub();
                 PoiCategoryRequestDTO poiRequest = PoiCategoryDataConvert.convertGetPoiCategoryRequestProxy2Request(request);
                 cli.addData("Request", request.toString());
                 poiResponse = stub.fetchPoiCategories(poiRequest);               
                 response = PoiCategoryDataConvert.convertGetPoiCategoryResponse2Proxy(poiResponse);
                 cli.addData("Response", response.getStatusCode()+"_"+response.getStatusMsg());
                if (logger.isDebugEnabled())
                {
                    logger.debug("fetchPoiCategoriesResponse======>" + response.toString());
                }
            }
            catch (Exception ex)
            {
                logger.fatal("PoiCategoryProxy::fetchPoiCategories()", ex);
            }
        }
        finally
        {
            cli.complete();
            if (startAPICall)// throttling
            {
                ThrottlingManager.endAPICall(SERVICE_POICATEGORY, tc);
            }
            WebServiceUtils.cleanupStub(stub);
        }
        return response;            
    }
    
    /**
     * Fetch POI category, notice parameters in the request
     * @param request
     * @param tc for throttling module
     * @return
     * @throws ThrottlingException         
     */
    public PoiCategoryResponse fetchSubPoiCategories(PoiCategoryRequest request,TnContext tc) throws ThrottlingException {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("SubPoiCategory");
        if (request != null && logger.isDebugEnabled())
        {
            logger.debug("fetchSubPoiCategoriesRequest======>" + request.toString());
        }
        boolean startAPICall = false;// the flag whether can start call API
        PoiCategoryResponse response=new PoiCategoryResponse();
        PoiCategoriesResponseDTO poiResponse = null;
        XnavServiceStub stub = null;
        try
        {
            startAPICall = ThrottlingManager.startAPICall(SERVICE_POICATEGORY, tc);
            if (!startAPICall)
            {
                throw new ThrottlingException();
            }
            try
            {
                 stub = createStub();
                 PoiCategoryRequestDTO poiRequest = PoiCategoryDataConvert.convertGetPoiCategoryRequestProxy2Request(request);
                 cli.addData("sub Request", request.toString());
                 poiResponse = stub.fetchSubPoiCategories(poiRequest);               
                 response = PoiCategoryDataConvert.convertGetPoiCategoryResponse2Proxy(poiResponse);
                 cli.addData("sub Response", response.getStatusCode()+"_"+response.getStatusMsg());
                if (logger.isDebugEnabled())
                {
                    logger.debug("fetchSubPoiCategoriesResponse======>" + response.toString());
                }
            }
            catch (Exception ex)
            {
                logger.fatal("Sub PoiCategoryProxy::fetchSubPoiCategories()", ex);
            }
        }
        finally
        {
            cli.complete();
            if (startAPICall)// throttling
            {
                ThrottlingManager.endAPICall(SERVICE_POICATEGORY, tc);
            }
            WebServiceUtils.cleanupStub(stub);
        }
        return response;            
    }
    
    private XnavServiceStub createStub()
    {
        XnavServiceStub stub = null;
        try
        {
            WebServiceConfigInterface ws = WebServiceConfiguration.getInstance().getServiceConfig(WS_POICATEGORY);
            stub = new XnavServiceStub(poiCategoryServiceContext, ws.getServiceUrl());
            stub._getServiceClient().getOptions().setTimeOutInMilliSeconds(ws.getWebServiceItem().getWebServiceTimeout());
        }
        catch (AxisFault af)
        {
            logger.fatal("createStub()", af);
        }
        return stub;
    }
}
