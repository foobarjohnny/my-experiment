/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.guidetone;

import java.util.Vector;

import com.telenav.cserver.backend.StatusConstants;
import com.telenav.cserver.backend.datatypes.guidetone.BackendGuideToneOrder;

/**
 * BackendGetGuideToneOrderResponse.java
 *
 * jhjin@telenav.cn
 * @version 1.0 2010-9-3
 *
 */
public class GetUserGuideToneInfoResponse
{
    
    private String statusCode = StatusConstants.SUCCESS;

    private String statusMessage;
    
    private Vector<BackendGuideToneOrder> purchasedGuideToneOrders;
    private Vector<BackendGuideToneOrder> unpurchasedGuideToneOrders;

    public String getStatusCode()
    {
        return statusCode;
    }

    public void setStatusCode(String statusCode)
    {
        this.statusCode = statusCode;
    }

    public String getStatusMessage()
    {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage)
    {
        this.statusMessage = statusMessage;
    }
 
    public Vector<BackendGuideToneOrder> getPurchasedGuideToneOrders()
    {
        return purchasedGuideToneOrders;
    }

    public void setPurchasedGuideToneOrders(Vector<BackendGuideToneOrder> purchasedGuideToneOrders)
    {
        this.purchasedGuideToneOrders = purchasedGuideToneOrders;
    }

    public Vector<BackendGuideToneOrder> getUnpurchasedGuideToneOrders()
    {
        return unpurchasedGuideToneOrders;
    }

    public void setUnpurchasedGuideToneOrders(Vector<BackendGuideToneOrder> unpurchasedGuideToneOrders)
    {
        this.unpurchasedGuideToneOrders = unpurchasedGuideToneOrders;
    }

    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("statusCode="+statusCode+",");
        sb.append("statusMessage="+statusMessage);
        sb.append(",");
        sb.append("purchasedGuideToneOrders=");
        if( purchasedGuideToneOrders != null && purchasedGuideToneOrders.size() > 0 )
        {
            for( int i=0; i<purchasedGuideToneOrders.size(); i++)
            {
                sb.append("[");
                sb.append(purchasedGuideToneOrders.get(i));
                sb.append("]");
            }
        }else{
            sb.append("nil");
        }
        sb.append(",");
        sb.append("unpurchasedGuideToneOrders=");
        if( unpurchasedGuideToneOrders != null && unpurchasedGuideToneOrders.size() > 0 )
        {
            for( int i=0; i<unpurchasedGuideToneOrders.size(); i++)
            {
                sb.append("[");
                sb.append(unpurchasedGuideToneOrders.get(i));
                sb.append("]");
            }
        }else{
            sb.append("nil");
        }
        return sb.toString();
    }
    
    
    

}
