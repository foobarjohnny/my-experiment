/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */

package com.telenav.cserver.common.resource;

import java.util.ArrayList;
import java.util.List;

import com.telenav.cserver.framework.UserProfile;
import com.telenav.kernel.util.datatypes.TnContext;


/**
 * manager the resemblance path for the resource
 * 
 * @author jzhu(jzhu@telenav.cn)
 * @version 1.0 2010-11-22 
 */
public class ResourceResemblance
{

    public static List<String> getResemblanceFullPath(String fullName, String configName, List<LoadOrder> orderList, UserProfile profile, TnContext tnContext)
    {
        List<String> resemblanceFullPath = new ArrayList<String>();
        for(LoadOrder order: orderList)
        {
            List<String> list = order.getResemblanceFullPath(fullName,configName, profile, tnContext);
            resemblanceFullPath.addAll(list);
        }
        return resemblanceFullPath;
    }

}
