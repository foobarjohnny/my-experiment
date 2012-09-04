/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.ace;

import java.util.List;

import com.telenav.cserver.backend.datatypes.ace.GeoCodedAddress;
import com.telenav.ws.datatypes.services.ResponseStatus;

/**
 * GeoCodeResponse.java
 *
 * @author bhu@telenav.cn
 * @version 1.0 2009-7-6
 */
public class GeoCodeResponse
{
	public final static GeoCodeResponse NULL_RESPONSE = new GeoCodeResponse(null,null);
    private List<GeoCodedAddress> addresses = null;
    private GeoCodeResponseStatus status = null;
    
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append("GeoCodeResponse==>[");
        sb.append("status=");
        sb.append(this.status.isSuccessful());
        sb.append(",addresses=");
        if(this.addresses!=null) {
            for(int index=0;index< this.addresses.size();index++) {
                sb.append("\n");
                sb.append(addresses.get(index).toString());               
            }
        }
        sb.append("]");
        return sb.toString();
    }
    
    public GeoCodeResponse(List<GeoCodedAddress> addresses, ResponseStatus respStatus)
    {
        if (null != addresses)
        {
            this.addresses = addresses;
        }
        this.status = new GeoCodeResponseStatus(respStatus);
    }

    public List<GeoCodedAddress> getMatches()
    {
        return addresses;
    }

    public GeoCodeResponseStatus getStatus()
    {
        return status;
    }

    /**
     * Return the number of match resulting from the geocode
     *
     * @return int number of match resulting from the geocode
     */
    public int getMatchCount()
    {
        if (addresses != null)
            return addresses.size();
        else
            return 0;
    }
}
