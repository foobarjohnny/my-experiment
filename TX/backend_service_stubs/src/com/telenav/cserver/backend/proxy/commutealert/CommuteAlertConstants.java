/**
 * (c) Copyright 2007 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.proxy.commutealert;

/**
 * Constants definition for alert management
 * 
 * @author yqchen
 * @version 1.0 2007-11-22 10:07:42
 */
public interface CommuteAlertConstants
{
    //status code 
    public static final int STATUS_OK = 0;   
  
    
    public static final int STATUS_CANNOT_CONNECTTO_WS = 90;
    public static final int STATUS_WS_EXCEPTION = 91;
    public static final int STATUS_WS_NULL_TRAFFIC_SUMMARY = 92;
    public static final int STATUS_WS_NULL_ALERT = 93;
    
    public static final int STATUS_GENERAL_ERROR = 99;
}
