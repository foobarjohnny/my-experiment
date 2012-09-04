/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy.billing;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;

import com.telenav.billing2.account.AccountStub;
import com.telenav.billing2.ws.datatypes.account.BindDeviceRequest;
import com.telenav.billing2.ws.datatypes.account.BindDeviceResponse;
import com.telenav.billing2.ws.datatypes.account.UnBindDeviceRequest;
import com.telenav.billing2.ws.datatypes.account.UnBindDeviceResponse;
import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.backend.config.WebServiceConfigInterface;
import com.telenav.cserver.backend.proxy.AbstractStubProxy;
import com.telenav.cserver.backend.proxy.annotation.BackendProxy;
import com.telenav.cserver.backend.proxy.annotation.ProxyDebugLog;
import com.telenav.cserver.backend.proxy.annotation.Throttling;
import com.telenav.cserver.backend.proxy.annotation.ThrottlingConf;
import com.telenav.cserver.backend.util.WebServiceUtils;
import com.telenav.kernel.util.datatypes.TnContext;

@BackendProxy
@ThrottlingConf("BindDeviceProxy")
public class BindDeviceProxy extends AbstractStubProxy<AccountStub> {
	private Logger logger = Logger.getLogger(ServiceProvisioningProxy.class);

	protected BindDeviceProxy() {
	}

	protected AccountStub createStub(WebServiceConfigInterface ws)
			throws Exception {
		AccountStub stub = null;
		try {
			stub = new AccountStub(createContext(ws), ws
					.getServiceUrl());
			stub._getServiceClient().getOptions().setTimeOutInMilliSeconds(
					ws.getWebServiceItem().getWebServiceTimeout());
		} catch (Exception e) {
			logger.fatal("create ServiceProvisioningStub stub failed", e);
		}

		return stub;
	}
	
	@ProxyDebugLog
    @Throttling
    public UnBindDeviceResponse unbindDevice(UnBindDeviceRequest request,TnContext tc){
    	UnBindDeviceResponse response=null;
    	CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("bindDevice");
        cli.addData("UnBindDeviceRequest", ReflectionToStringBuilder.toString(request));
        
        AccountStub stub=null;
        try
        {
            stub=createStub(getWebServiceConfigInterface());
            response=stub.unBindDevice(request);
            cli.addData("UnBindDeviceResponse", ReflectionToStringBuilder.toString(response));
        }
        catch (Exception e)
        {
            cli.setStatus(e);
            logger.fatal("unbindDevice failed.",e);
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
	public BindDeviceResponse bindDevice(BindDeviceRequest request,TnContext tc){
		BindDeviceResponse response = null;
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("bindDevice");
        cli.addData("BindDeviceRequest", ReflectionToStringBuilder.toString(request));
        
        AccountStub stub=null;
        try
        {
            stub=createStub(getWebServiceConfigInterface());
            response=stub.bindDevice(request);
            cli.addData("BindDeviceResponse", ReflectionToStringBuilder.toString(response));
        }
        catch (Exception e)
        {
            cli.setStatus(e);
            logger.fatal("getPurchaseOption failed.",e);
        }
        finally
        {
            cli.complete();
            WebServiceUtils.cleanupStub(stub);
        }
        return response;
	}

	public String getProxyConfType() {
		return "WS_ACCOUNT_PROVISION";
	}
}
