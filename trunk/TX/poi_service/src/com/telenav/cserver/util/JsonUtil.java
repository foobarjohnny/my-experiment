/******************************************************************************
 * Copyright (c) 2008 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on May 18, 2009
 * File name: JsonUtil.java
 * Package name: com.telenav.cserver.util
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: dysong(dysong@telenav.cn) 1:10:46 PM
 *  Update:
 *******************************************************************************/
package com.telenav.cserver.util;

import org.json.me.JSONObject;

/**
 * @author dysong (dysong@telenav.cn) 1:10:46 PM, May 18, 2009
 */
public class JsonUtil {
    public static String getString(JSONObject jo, String key) {
        String s = "";
        try {
            s = jo.getString(key);
        } catch (Exception e) {
        }
        return s;
    }
    
    public static int getInt(JSONObject jo, String key) {
        int i = 0;
        try {
            i = jo.getInt(key);
        } catch (Exception e) {
        }
        return i;
    }
    
    public static boolean getBoolean(JSONObject jo, String key) {
        boolean b = false;
        try {
            b = jo.getBoolean(key);
        } catch (Exception e) {
        }
        return b;
    }
}
