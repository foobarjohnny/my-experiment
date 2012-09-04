/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy.image;

import com.telenav.cserver.backend.proxy.HttpClientResponse;

/**
 * ImageResponse.java
 *
 * jhjin@telenav.cn
 * @version 1.0 Apr 12, 2011
 *
 */
public class ImageResponse extends HttpClientResponse
{

    private byte[] binData = null;
    
    private long expirationData = -1;
    
    public byte[] getBinData() 
    {
        return binData;
    }

    public void setBinData(byte[] binData)
    {
        this.binData = binData;
    }

    public long getExpirationData() 
    {
        return expirationData;
    }

    public void setExpirationData(long expirationData)
    {
        this.expirationData = expirationData;
    }
    
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Status Code=" + this.getStatusCode());
        buffer.append(", expiration Data=" + this.getExpirationData());
        buffer.append(", data size=" + ((this.getBinData() == null) ? 0 : this.getBinData().length));
        return buffer.toString();
    }
}
