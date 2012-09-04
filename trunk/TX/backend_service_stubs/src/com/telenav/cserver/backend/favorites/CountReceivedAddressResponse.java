/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.favorites;

/**
 * 
 * @author zhjdou
 * 2010-4-1
 */
public class CountReceivedAddressResponse extends FavoriteBasicResponse
{
    private int count=0;
    
    /**
     * @return the count
     */
    public int getCount()
    {
        return count;
    }
    /**
     * @param count the count to set
     */
    public void setCount(int count)
    {
        this.count = count;
    }
    
    /**
     * The element to String
     */
    public String toString() {
        StringBuffer sf=new StringBuffer();
        sf.append("CountReceivedAddressResponse[");
        sf.append("statusCode="+ super.getStatus_code());
        sf.append(", statusMessage="+super.getStatus_message());
        sf.append(", count="+this.count);
        return sf.toString();
    }
}
