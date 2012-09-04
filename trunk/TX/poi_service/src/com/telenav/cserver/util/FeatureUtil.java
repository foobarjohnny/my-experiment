/*
 * Copyright (c) 2009 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 */
package com.telenav.cserver.util;

import com.telenav.cserver.pref.struts.util.PreferenceConstants;
import com.telenav.tnbrowser.util.DataHandler;

/**
 * @author pzhang
 *
 * @version 1.0, 2009-8-4
 */
public class FeatureUtil {
    
    public static final String PRODUCT_TYPE_TN = "TN";
    /**
     * 
     * @param handler
     * @return
     */
    public static boolean isSupportWeather(DataHandler handler)
    {
        boolean support = false;
        String region = handler.getClientInfo(DataHandler.KEY_REGION);
        if(PreferenceConstants.VALUE_REGION_AMERICA.equals(region))
        {
            support = true;
        }
        return support;
    }
    
    /**
     * 
     * @param handler
     * @return
     */
    public static boolean isSupportDSR(DataHandler handler)
    {
        boolean support = false;
        String region = handler.getClientInfo(DataHandler.KEY_REGION);
        if(PreferenceConstants.VALUE_REGION_AMERICA.equals(region))
        {
            support = true;
        }
        return support;
    }
    
    /**
     * 
     * @param handler
     * @return
     */
    public static boolean isSupportFindOutMore(DataHandler handler)
    {
        boolean support = false;
        String productType = handler.getClientInfo(DataHandler.KEY_PRODUCTTYPE);
        if(PRODUCT_TYPE_TN.equalsIgnoreCase(productType))
        {
            support = true;
        }
            
        return support;
    }
    
    /**
     * 
     * @param handler
     * @return
     */
    public static boolean isSupportEmailSupport(DataHandler handler)
    {
        boolean support = false;
        String productType = handler.getClientInfo(DataHandler.KEY_PRODUCTTYPE);
        String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
        if(PRODUCT_TYPE_TN.equalsIgnoreCase(productType) && !"BellMob".equals(carrier) && !"VMC".equals(carrier) && !"VIVOGSM".equalsIgnoreCase(carrier))
        {
            support = true;
        }
            
        return support;
    }
    
    /**
     * 
     * @param handler
     * @return
     */
    public static boolean isSupportCallSupport(DataHandler handler)
    {
        boolean support = true;
        String carrier = handler.getClientInfo(DataHandler.KEY_CARRIER);
        if("BellMob".equals(carrier) || "VMC".equals(carrier))
        {
            support = false;
        }
            
        return support;
    }    
}
