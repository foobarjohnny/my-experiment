/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.guidetone;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.log4j.Logger;

import com.telenav.billing.ws.datatypes.common.RequestSource;
import com.telenav.billing.ws.datatypes.userprofile.GetGuideToneOrderRequest;
import com.telenav.billing.ws.datatypes.userprofile.GetGuideToneOrderResponse;
import com.telenav.billing.ws.datatypes.userprofile.GuideToneOrder;
import com.telenav.billing.ws.userprofilemanagementservice.UserProfileManagementServiceStub;
import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.backend.StatusConstants;
import com.telenav.cserver.backend.config.WebServiceConfigInterface;
import com.telenav.cserver.backend.datatypes.guidetone.BackendGuideToneOrder;
import com.telenav.cserver.backend.util.WebServiceConfiguration;
import com.telenav.cserver.backend.util.WebServiceUtils;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.cserver.framework.throttling.ThrottlingManager;
import com.telenav.datatypes.identityprovisioning.v10.ProductDef;
import com.telenav.datatypes.identityprovisioning.v10.QueryCriteriaKey;
import com.telenav.datatypes.identityprovisioning.v10.QueryProductDefRequestDTO;
import com.telenav.datatypes.identityprovisioning.v10.QueryProductDefResponseDTO;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.kernel.ws.axis2.Axis2Helper;
import com.telenav.services.identityprovisioning.v10.IdentityProvisioningServiceStub;

/**
 * GuideToneProxy.java
 * 
 * jhjin@telenav.cn
 * 
 * @version 1.0 2010-9-3
 * 
 */
public class GuideToneServiceProxy
{
    private static final Logger logger = Logger.getLogger(GuideToneServiceProxy.class);

    private static String GUIDETONE_SERVICE = "GUIDETONE_SERVICE";

    private static GuideToneServiceProxy instance = new GuideToneServiceProxy();

    private GuideToneServiceProxy()
    {
    }

    private static final String GUIDETONE = "GUIDETONE";

    private static String WS_TELEPERSONALIZATION = "TELEPERSONALIZATION";

    private static String WS_IDENTITYPROVISION = "IDENTITYPROVISION";

    private static ConfigurationContext telepersonalizationServiceContext = WebServiceUtils.createConfigurationContext(WS_TELEPERSONALIZATION);

    private static ConfigurationContext identityProvisionServiceContext = WebServiceUtils.createConfigurationContext(WS_IDENTITYPROVISION);

    public static GuideToneServiceProxy getInstance()
    {
        return instance;
    }

    public GetUserGuideToneInfoResponse getUserGuideToneInfo(GetUserGuideToneInfoRequest request, TnContext tc) throws ThrottlingException
    {

        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("getUserGuideToneInfo");
        GetUserGuideToneInfoResponse response = new GetUserGuideToneInfoResponse();
        if( logger.isDebugEnabled() )
        {
            logger.debug("GetUserGuideToneInfoRequest======>"+ (request == null ? "null" : request.toString()));
        }
        if( request == null )
        {
            response.setStatusCode(StatusConstants.FAIL);
            response.setStatusMessage("request can't be null");
            return response;
        }
        long userId = request.getUserId();
        
        boolean startAPICall = false;
        try
        {
            startAPICall = ThrottlingManager.startAPICall(GUIDETONE_SERVICE, tc);
            if (!startAPICall)
            {
                throw new ThrottlingException();
            }

            try
            {
                Vector<BackendGuideToneOrder> purchasedOrders = fetchGuideTone(userId);
                Vector<BackendGuideToneOrder> validPurchasedOrders = new Vector<BackendGuideToneOrder>();

                Map<String, BackendGuideToneOrder> purchasedOrderMap = new HashMap<String, BackendGuideToneOrder>();
                Map<String, BackendGuideToneOrder> validPurchasedOrderMap = new HashMap<String, BackendGuideToneOrder>();
                if (purchasedOrders != null && !purchasedOrders.isEmpty())
                {
                    String[] guideToneNames = new String[purchasedOrders.size()];

                    for (int i = 0; i < purchasedOrders.size(); i++)
                    {
                        guideToneNames[i] = purchasedOrders.get(i).getName();
                        purchasedOrderMap.put(guideToneNames[i], purchasedOrders.get(i));
                    }

                    if (guideToneNames != null && guideToneNames.length > 0)
                    {
                        ProductDef[] productDefs = queryProductDef(QueryCriteriaKey.PRODUCT_DEF, guideToneNames);
                        if (Axis2Helper.isNonEmptyArray(productDefs))
                        {
                            for (int i = 0; i < productDefs.length; i++)
                            {
                                BackendGuideToneOrder order = purchasedOrderMap.get(productDefs[i].getProductDef());
                                if (order != null)
                                {
                                    order.setPrice(productDefs[i].getPrice() == Double.NaN ? 0 : productDefs[i].getPrice());
                                    order.setDesc(productDefs[i].getProductDesc());
                                    validPurchasedOrders.add(order);
                                    validPurchasedOrderMap.put(order.getName(), order);
                                }
                            }
                        }
                    }
                }

                Vector<BackendGuideToneOrder> unpurchasedOrders = new Vector<BackendGuideToneOrder>();
                ProductDef[] allProductDefs = queryProductDef(QueryCriteriaKey.PRODUCT_FAMILY, new String[]
                { GUIDETONE });
                if (Axis2Helper.isNonEmptyArray(allProductDefs))
                {
                    for (int i = 0; i < allProductDefs.length; i++)
                    {
                        if (allProductDefs[i].getIsValid() == 1 && validPurchasedOrderMap.get(allProductDefs[i].getProductDef()) == null)
                        {
                            BackendGuideToneOrder order = new BackendGuideToneOrder();
                            order.setPrice(allProductDefs[i].getPrice());
                            order.setName(allProductDefs[i].getProductDef());
                            order.setDesc(allProductDefs[i].getProductDesc());
                            unpurchasedOrders.add(order);
                        }
                    }
                }
                response.setPurchasedGuideToneOrders( validPurchasedOrders);
                response.setUnpurchasedGuideToneOrders( unpurchasedOrders);
                response.setStatusCode(StatusConstants.SUCCESS);
                response.setStatusMessage(StatusConstants.SUCCESS_MSG);
            }

            catch (Exception ex)
            {
                response.setStatusCode(StatusConstants.FAIL);
                response.setStatusMessage(StatusConstants.EXCEPTION_MSG);
                cli.setStatus(ex);
                logger.fatal("getUserGuideToneInfo", ex);
            }
            
        }

        finally
        {
            if (startAPICall)
            {
                ThrottlingManager.endAPICall(GUIDETONE_SERVICE, tc);
            }
            cli.complete();
        }
        
        if(logger.isDebugEnabled())
        {
            logger.debug("GetUserGuideToneInfoResponse======>"+response);
        }
        
        return response;
    }

    /**
     * 
     * @param userId
     * @return return null represents server has issue.
     * @throws ThrottlingException
     */
    private Vector<BackendGuideToneOrder> fetchGuideTone(long userId) throws RemoteException
    {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("fetchGuideTone");
        if (logger.isDebugEnabled())
        {
            logger.debug("userId = " + userId);
        }
        Vector<BackendGuideToneOrder> vector = null;
        UserProfileManagementServiceStub userProfileManagementServiceStub = null;
        try
        {
            userProfileManagementServiceStub = createUserProfileManagementServiceStub();
            GetGuideToneOrderRequest getGuideToneOrderRequest = new GetGuideToneOrderRequest();
            getGuideToneOrderRequest.setRequestSource(RequestSource.TelenavXnav);
            getGuideToneOrderRequest.setUserId(userId);
            GetGuideToneOrderResponse getGuideToneOrderResponse = userProfileManagementServiceStub
                    .getGuideToneOrder(getGuideToneOrderRequest);
            
            String responseLogMsg = getGuideToneOrderResponse == null ? 
                    "null" : "statusCode="+getGuideToneOrderResponse.getResponseCode()+",statusMsg="+getGuideToneOrderResponse.getResponseMessage();
            cli.addData("getGuideToneOrderResponse", responseLogMsg);
            if( logger.isDebugEnabled() )
            {
                logger.debug("GetGuideToneOrderResponse ======> "+responseLogMsg);
            }
            if (getGuideToneOrderResponse != null)
            {

                GuideToneOrder[] orders = getGuideToneOrderResponse.getGuideToneOrder();
                vector = new Vector<BackendGuideToneOrder>();
                if (Axis2Helper.isNonEmptyArray(orders))
                {
                    for (int i = 0; i < orders.length; i++)
                    {
                        BackendGuideToneOrder order = GuideToneDataConvert.parse(orders[i]);
                        vector.add(order);
                    }
                }
                
            }
        }
        finally
        {
            cli.complete();
            WebServiceUtils.cleanupStub(userProfileManagementServiceStub);
        }
        return vector;
    }

    /**
     * 
     * @param type
     * @param keywords, there is limit for size of 'keywords', max is 20
     * @return
     */
    private ProductDef[] queryProductDef(QueryCriteriaKey type, String[] keywords) throws RemoteException
    {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("queryProductDef");
        
        if (type == null || keywords == null || keywords.length == 0)
        {
            return new ProductDef[0];
        }
        IdentityProvisioningServiceStub identityProvisioningServiceStub = null;
        List<ProductDef> productDefList = new ArrayList<ProductDef>();
        try
        {

            identityProvisioningServiceStub = createIdentityProvisioningServiceStub();
            QueryProductDefRequestDTO request = new QueryProductDefRequestDTO();
            if (QueryCriteriaKey.PRODUCT_FAMILY.getValue().equals(type.getValue()))
            {
                request.setQueryCriteriaKey(QueryCriteriaKey.PRODUCT_FAMILY);
            }
            else if ((QueryCriteriaKey.PRODUCT_DEF.getValue().equals(type.getValue())))
            {
                request.setQueryCriteriaKey(QueryCriteriaKey.PRODUCT_DEF);
            }
            request.setRequestSource(RequestSource.TelenavXnav.getValue());
            int length = keywords.length;
            int frequency = 1;
            if (length > 20)
            {
                frequency = (length % 20 == 0 ? length / 20 : (length / 20 + 1));
            }
            List<String> keywordList = Arrays.asList(keywords);

            for (int i = 0; i < frequency; i++)
            {
                List<String> subList = null;
                if (i == frequency - 1)
                {
                    subList = keywordList.subList(i * 20, keywordList.size());
                }
                else
                {
                    subList = keywordList.subList(i * 20, i * 20 + 20);
                }
                request.setQueryCriteriaValue((String[]) (subList.toArray(new String[subList.size()])));
                cli.addData("QueryProductDefRequestDTO",  "QueryCriteriaKey="+(request.getQueryCriteriaKey() == null ?"null": request.getQueryCriteriaKey().toString()));
                QueryProductDefResponseDTO response = identityProvisioningServiceStub.queryProductDef(request);
                String responseLogMsg = response == null ? 
                        "null" : "statusCode="+response.getResponseCode()+",statusMsg="+response.getResponseMessage();
                cli.addData("QueryProductDefResponseDTO", responseLogMsg);
                if( logger.isDebugEnabled() )
                {
                    logger.debug("QueryProductDefResponseDTO ======>"+responseLogMsg);
                }
                if (Axis2Helper.isNonEmptyArray(response.getProductDef()))
                {
                    productDefList.addAll(Arrays.asList(response.getProductDef()));
                }
            }
        }
        finally
        {
            cli.complete();
            WebServiceUtils.cleanupStub(identityProvisioningServiceStub);
        }

        return (ProductDef[]) productDefList.toArray(new ProductDef[productDefList.size()]);
    }

    /**
     * crate UserProfileManagementServiceStub, if fail, return null.
     * 
     * @return
     */
    private UserProfileManagementServiceStub createUserProfileManagementServiceStub()
    {
        UserProfileManagementServiceStub stub = null;
        WebServiceConfigInterface webserviceConifgInterface = WebServiceConfiguration.getInstance().getServiceConfig(WS_TELEPERSONALIZATION);
        try
        {
            stub = new UserProfileManagementServiceStub(telepersonalizationServiceContext, webserviceConifgInterface.getServiceUrl());
            stub._getServiceClient().getOptions().setTimeOutInMilliSeconds(
                webserviceConifgInterface.getWebServiceItem().getWebServiceTimeout());
        }
        catch (AxisFault fault)
        {
            logger.error("GuideToneServiceProxy:createUserProfileManagementServiceStub()", fault);
        }
        return stub;
    }

    private IdentityProvisioningServiceStub createIdentityProvisioningServiceStub()
    {
        IdentityProvisioningServiceStub stub = null;
        WebServiceConfigInterface webserviceConifgInterface = WebServiceConfiguration.getInstance().getServiceConfig(WS_IDENTITYPROVISION);
        try
        {
            stub = new IdentityProvisioningServiceStub(identityProvisionServiceContext, webserviceConifgInterface.getServiceUrl());
            stub._getServiceClient().getOptions().setTimeOutInMilliSeconds(
                webserviceConifgInterface.getWebServiceItem().getWebServiceTimeout());
        }
        catch (AxisFault fault)
        {
            logger.error("GuideToneServiceProxy:createIdentityProvisioningServiceStub()", fault);
        }
        return stub;
    }
}
