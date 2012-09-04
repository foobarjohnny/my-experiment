/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.common.resource.orders;

import java.util.ArrayList;
import java.util.List;

import com.telenav.cserver.common.resource.LoadOrder;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * the order for loading
 * 
 * @author jzhu
 * @version 2.0 2010-11-24 09:46
 */
public class OrLoadOrder extends LoadOrder
{	
    private List<LoadOrder> loadOrderList = new ArrayList<LoadOrder>();
    
    public void setLoadOrderList(List<LoadOrder> loadOrderList)
    {
        this.loadOrderList = loadOrderList;
    }

    /**
     * get the first not NUll or "" attribute value from loadOrderList
     * @param profile
     * @return
     */
    public String getAttributeValue(UserProfile profile, TnContext tnContext)
    {
        for(LoadOrder order:loadOrderList)
        {
            String att = order.getAttributeValue(profile, tnContext);
            if (att!=null && !att.equals(""))
                return att;
        }
        return "";
    }
    
    
    public List<String> getAttributeValueList(UserProfile profile, TnContext tnContext)
    {
        List<String> list = new ArrayList<String>();
        
        for(LoadOrder order:loadOrderList)
        {
            list.addAll(order.getAttributeValueList(profile, tnContext));
        }
        return list;
    }
    
    public List<String> getResemblanceFullPath(String fullPath, String configFileName, UserProfile profile, TnContext tnContext)
    {
        List<String> list = new ArrayList<String>();
        
        for(LoadOrder order:loadOrderList)
        {
            list.addAll(order.getResemblanceFullPath(fullPath, configFileName, profile, tnContext));
        }
        return list;
    }
}
