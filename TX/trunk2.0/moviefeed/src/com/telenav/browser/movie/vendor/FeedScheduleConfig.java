/******************************************************************************
 * Copyright (c) 2008 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on Nov 20, 2008
 * File name: FeedScheduleConfig.java
 * Package name: com.telenav.j2me.browser.vendor
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: dysong(dysong@telenav.cn) 6:36:42 PM , Nov 20, 2008
 *  Update:
 *******************************************************************************/
package com.telenav.browser.movie.vendor;

import java.util.PropertyResourceBundle;

import org.apache.log4j.Logger;

/**
 * @author dysong(dysong@telenav.cn) 6:36:42 PM , Nov 20, 2008
 */
public class FeedScheduleConfig {

    private static final Logger log = Logger
            .getLogger(FeedScheduleConfig.class);

    // retry limit count
    private int retryLimitCount;

    // minute
    private int retryInterval;

    public int getRetryLimitCount() {
        return retryLimitCount;
    }

    public void setRetryLimitCount(int retryLimitCount) {
        this.retryLimitCount = retryLimitCount;
    }

    public int getRetryInterval() {
        return retryInterval;
    }

    public void setRetryInterval(int retryInterval) {
        this.retryInterval = retryInterval;
    }

    public static FeedScheduleConfig loadScheduleConfig() {
        // Properties properties = new Properties();
        FeedScheduleConfig config = new FeedScheduleConfig();
        try {

            PropertyResourceBundle serverBundle = (PropertyResourceBundle) PropertyResourceBundle
                    .getBundle("config.feed");

            // Get properties.
            String retryLimitCount = serverBundle
                    .getString("load.retry.limit.count");
            String retryInterval = serverBundle
                    .getString("load.retry.interval");

            // Set values.
            if (checkStr(retryLimitCount) && checkStr(retryInterval)) {

                config.setRetryInterval(Integer.parseInt(retryInterval));
                config.setRetryLimitCount(Integer.parseInt(retryLimitCount));

                return config;
            }
        } catch (Exception e) {
            log.error("Load config error", e);
        }
        return null;
    }

    private static boolean checkStr(String toCheck) {
        if (toCheck == null || toCheck.trim().length() == 0) {
            return false;
        }
        return true;
    }
}