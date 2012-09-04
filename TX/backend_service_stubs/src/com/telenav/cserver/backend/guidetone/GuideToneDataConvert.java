/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.guidetone;

import com.telenav.billing.ws.datatypes.userprofile.GuideToneOrder;
import com.telenav.cserver.backend.datatypes.guidetone.BackendGuideToneOrder;

/**
 * GuideToneDataConvert.java
 * 
 * jhjin@telenav.cn
 * 
 * @version 1.0 2010-9-3
 * 
 */
public class GuideToneDataConvert
{

    public static BackendGuideToneOrder parse(GuideToneOrder order)
    {
        if (order != null)
        {
            BackendGuideToneOrder backendGuideToneOrder = new BackendGuideToneOrder();
            backendGuideToneOrder.setUserId(order.getUserId());
            backendGuideToneOrder.setName(order.getGuideToneName());
            backendGuideToneOrder.setId(order.getGuideToneId() == Long.MIN_VALUE ? 0 : order.getGuideToneId());
            backendGuideToneOrder.setDefaultGuideToneOrder(order.getDefaultTone() == 1 ? true : false);
            if (order.getCreateTime() != null)
            {
                backendGuideToneOrder.setCreateTime(order.getCreateTime().getTime());
            }
            if (order.getUpdateTime() != null)
            {
                backendGuideToneOrder.setUpdateTime(order.getUpdateTime().getTime());
            }
            return backendGuideToneOrder;
        }
        else
        {
            return null;
        }
    }
}
