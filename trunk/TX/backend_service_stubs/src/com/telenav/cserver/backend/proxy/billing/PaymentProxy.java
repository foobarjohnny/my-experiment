/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 *  @author Jianyu Zhou
 */
package com.telenav.cserver.backend.proxy.billing;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;

import com.telenav.billing2.payment.PaymentStub;
import com.telenav.billing2.ws.datatypes.payment.ConfirmPurchaseRequest;
import com.telenav.billing2.ws.datatypes.payment.DeclinePurchaseRequest;
import com.telenav.billing2.ws.datatypes.payment.PreparePurchaseRequest;
import com.telenav.billing2.ws.datatypes.payment.ProcessAMPMessageRequest;
import com.telenav.billing2.ws.datatypes.payment.ProcessAMPMessageResponse;
import com.telenav.billing2.ws.datatypes.payment.PurchaseResponse;
import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.backend.config.WebServiceConfigInterface;
import com.telenav.cserver.backend.proxy.AbstractStubProxy;
import com.telenav.cserver.backend.proxy.annotation.BackendProxy;
import com.telenav.cserver.backend.proxy.annotation.ProxyDebugLog;
import com.telenav.cserver.backend.proxy.annotation.Throttling;
import com.telenav.cserver.backend.proxy.annotation.ThrottlingConf;
import com.telenav.cserver.backend.util.WebServiceUtils;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.kernel.util.datatypes.TnContext;

@BackendProxy
@ThrottlingConf("PaymentProxy")
public class PaymentProxy extends AbstractStubProxy<PaymentStub>
{
    private Logger logger = Logger.getLogger(PaymentProxy.class);

    protected PaymentProxy()
    {
    }

    
    @ProxyDebugLog
    @Throttling
    public PurchaseResponse preparePurchase(PreparePurchaseRequest request, TnContext tc) throws ThrottlingException
    {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("preparePurchase");

        cli.addData("preparePurchaseRequest", ReflectionToStringBuilder.toString(request));

        PurchaseResponse response = null;
        PaymentStub stub=null;
        try
        {
            stub=createStub(getWebServiceConfigInterface());
            response = stub.preparePurchase(request);
            cli.addData("preparePurchaseResponse", ReflectionToStringBuilder.toString(response));
        }
        catch (Exception e)
        {
            logger.error("Error occurs while preparing purchase: " + e.getMessage());
        	cli.setStatus(e);
        }
        finally
        {
            cli.complete();
            WebServiceUtils.cleanupStub(stub);
        }

        return response;
    }
    
    @ProxyDebugLog
    @Throttling
    public PurchaseResponse confirmPurchase(ConfirmPurchaseRequest request, TnContext tc) throws ThrottlingException
    {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("confirmPurchase");

        cli.addData("confirmPurchaseRequest", ReflectionToStringBuilder.toString(request));

        PurchaseResponse response = null;
        PaymentStub stub=null;
        try
        {
            stub=createStub(getWebServiceConfigInterface());
            response = stub.confirmPurchase(request);
            cli.addData("confirmPurchaseResponse", ReflectionToStringBuilder.toString(response));
        }
        catch (Exception e)
        {
            logger.error("Error occurs while confirming purchase: " + e.getMessage());
        	cli.setStatus(e);
        }
        finally
        {
            cli.complete();
            WebServiceUtils.cleanupStub(stub);
        }

        return response;
    }

    @ProxyDebugLog
    @Throttling
    public PurchaseResponse declinePurchase(DeclinePurchaseRequest request, TnContext tc)
            throws ThrottlingException
    {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("declinePurchase");

        cli.addData("declinePurchaseReq", ReflectionToStringBuilder.toString(request));

        PurchaseResponse response = null;
        PaymentStub stub=null;
        try
        {
            stub=createStub(getWebServiceConfigInterface());
            response = stub.declinePurchase(request);
            cli.addData("declinePurchaseResp", ReflectionToStringBuilder.toString(response));
        }
        catch (Exception e)
        {
        	logger.error("Error occurs while declining purchase: " + e.getMessage());
        	cli.setStatus(e);
        }
        finally
        {
            cli.complete();
            WebServiceUtils.cleanupStub(stub);
        }

        return response;
    }
    
    @ProxyDebugLog
    @Throttling
    public ProcessAMPMessageResponse processAMPMessage(ProcessAMPMessageRequest request, TnContext tc)
            throws ThrottlingException
    {
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("processAMPMessage");

        cli.addData("processAMPMessageReq", ReflectionToStringBuilder.toString(request));

        ProcessAMPMessageResponse response = null;
        PaymentStub stub=null;
        try
        {
            stub=createStub(getWebServiceConfigInterface());
            response = stub.processAMPMessage(request);
            cli.addData("processAMPMessageResp", ReflectionToStringBuilder.toString(response));
        }
        catch (Exception e)
        {
        	logger.error("processAMPMessage failed,"+e.getMessage(),e);
        	cli.setStatus(e);
        }
        finally
        {
            cli.complete();
            WebServiceUtils.cleanupStub(stub);
        }

        return response;
    }

    @Override
    protected PaymentStub createStub(WebServiceConfigInterface ws) throws Exception
    {
    	PaymentStub stub = null;
        try
        {
            stub = new PaymentStub(createContext(ws), ws.getServiceUrl());
            stub._getServiceClient().getOptions().setTimeOutInMilliSeconds(ws.getWebServiceItem().getWebServiceTimeout());
        }
        catch (Exception e)
        {
            logger.fatal("create paymentStub stub failed", e);
        }

        return stub;
    }

    @Override
    public String getProxyConfType()
    {
        return "WS_SERVICE_PAYMENT";
    }
}
