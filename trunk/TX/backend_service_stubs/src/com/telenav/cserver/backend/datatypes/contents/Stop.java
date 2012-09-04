/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.datatypes.contents;

import com.telenav.cserver.backend.datatypes.Address;

/**
 * We extends the based address object, it will add more elements if need.
 * This class modify freely
 * @author zhjdou 2009-07-14
 */
public class Stop extends Address
{
    private String label = "";
    
    private String addressId="0";
    
    /**
     * @return the label
     */
    public String getLabel()
    {
        return label;
    }

    /**
     * @return the addressId
     */
    public String getAddressId()
    {
        return addressId;
    }

    /**
     * @param addressId the addressId to set
     */
    public void setAddressId(String addressId)
    {
        this.addressId = addressId;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label)
    {
        this.label = label;
    }

    public String toString() {
        StringBuffer sb=new StringBuffer();
        sb.append("Stop[");
        sb.append(super.toString());
        sb.append(", label=");
        sb.append(this.label);
        sb.append(", addressId=");
        sb.append(this.addressId);
        return sb.toString();
    }
}
