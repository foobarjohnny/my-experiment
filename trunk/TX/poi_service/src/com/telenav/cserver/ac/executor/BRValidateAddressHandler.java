/**
 * (c) Copyright 2011 TeleNav.
 * 
 * All Rights Reserved.
 */
package com.telenav.cserver.ac.executor;

import com.telenav.cserver.backend.proxy.BackendProxyManager;
import com.telenav.cserver.backend.proxy.ace.ValidateAddressProxy;
import com.telenav.cserver.backend.proxy.ace.ValidateAddressRequest;
import com.telenav.cserver.backend.proxy.ace.ValidateAddressResponse;
import com.telenav.cserver.framework.executor.ExecutorContext;

/**
 * BRValidateAddressHandler
 * 
 * @author kwwang
 * @date 2011-6-30
 */
public class BRValidateAddressHandler implements ValidateAddressHandler
{

    @Override
    public String doValidateAddress(ValidateAddressRequestACEWS req, ValidateAddressResponseACEWS resp, ExecutorContext context)
            throws Exception
    {
        ValidateAddressRequest vaReq = new ValidateAddressRequest();
        vaReq.setCity(req.getCity());
        vaReq.setState(req.getState());
        vaReq.setZipCode(req.getZip());
        vaReq.parseFirstLine(req.getFirstLine());
        ValidateAddressResponse vaResp = BackendProxyManager.getBackendProxyFactory().getBackendProxy(ValidateAddressProxy.class)
                .validateAddress(vaReq, context.getTnContext());
        resp.setAddressList(vaResp.getAddresses());
        return String.valueOf(vaResp.getStatusCode());
    }

}
