/**
 * (c) Copyright 2011 TeleNav.
 * 
 * All Rights Reserved.
 */
package com.telenav.cserver.ac.executor;

import com.telenav.cserver.backend.ace.GeoCodeRequest;
import com.telenav.cserver.backend.ace.GeoCodeResponse;
import com.telenav.cserver.backend.ace.GeoCodingProxy;
import com.telenav.cserver.backend.datatypes.Address;
import com.telenav.cserver.framework.executor.ExecutorContext;

/**
 * CommonValidateAddressHandler
 * 
 * @author kwwang
 * @date 2011-6-30
 */
public class CommonValidateAddressHandler implements ValidateAddressHandler
{

    @Override
    public String doValidateAddress(ValidateAddressRequestACEWS req, ValidateAddressResponseACEWS resp, ExecutorContext context)
            throws Exception
    {
        String status = "";
        GeoCodingProxy proxy = GeoCodingProxy.getInstance(context.getTnContext());
        Address address = new Address();

        address.setFirstLine(req.getFirstLine());
        address.setLastLine(req.getLastLine());
        address.setStreetName(req.getStreet1());
        address.setCrossStreetName(req.getStreet2());
        address.setCountry(req.getCountry());

        GeoCodeRequest geoCodeReq = new GeoCodeRequest();
        geoCodeReq.setTransactionId(req.getTransactionId());
        geoCodeReq.setAddress(address);

        GeoCodeResponse geoCodeResponse = proxy.geoCode(geoCodeReq);
        resp.setGeoCodeStatusCode(geoCodeResponse.getStatus().getStatusCode());
        status = String.valueOf(geoCodeResponse.getStatus().getStatusCode());

        if (geoCodeResponse != null && geoCodeResponse.getStatus().isSuccessful())
        {
            resp.setAddressList(geoCodeResponse.getMatches());
            resp.setTotalCount(geoCodeResponse.getMatchCount());
        }
        return status;
    }

}
