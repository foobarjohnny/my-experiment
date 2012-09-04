/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.ace;

import com.telenav.cserver.backend.datatypes.Address;


/**
 * GeoCodeRequest
 * @author kwwang
 *
 */
public class GeoCodeRequest
{
    private Address address;
    
    private String transactionId;

    public Address getAddress()
    {
        return address;
    }

    public void setAddress(Address address)
    {
        this.address = address;
    }

    public String getTransactionId()
    {
        return transactionId;
    }

    public void setTransactionId(String transactionId)
    {
        this.transactionId = transactionId;
    }
    
    public com.telenav.datatypes.address.v20.Address convertAddress2WsAddress()
    {
        return AceDataConverter.addressToWSAddress(address);
    }
    
    
}
