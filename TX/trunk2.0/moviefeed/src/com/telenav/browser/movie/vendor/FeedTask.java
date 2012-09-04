/******************************************************************************
 * Copyright (c) 2008 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on Nov 20, 2008
 * File name: LoadTimerTask.java
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

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.telenav.browser.movie.util.ReportMail;
import com.televigation.log.TVCategory;

/**
 * @author dysong(dysong@telenav.cn) 6:36:42 PM , Nov 20, 2008
 */
public class FeedTask {

    private static final Logger log = Logger.getLogger(FeedTask.class);

    private static final Logger misLogger = (Logger) TVCategory
            .getInstance("movie.feed.mis");

    private int retryLimitCount;

    private int retryInterval;

    public static void main(String[] args) {
        FeedTask task = new FeedTask();
        task.start();
    }

    public FeedTask() {

        // Get feed schedule config
        FeedScheduleConfig feedScheduleConfig = FeedScheduleConfig
                .loadScheduleConfig();

        this.retryInterval = feedScheduleConfig.getRetryInterval();
        this.retryLimitCount = feedScheduleConfig.getRetryLimitCount();

    }

    public void start() {

        StringBuffer mailContent = new StringBuffer();
        try {
            // Load count
            int count = 1;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            InetAddress address = InetAddress.getLocalHost();
            mailContent.append("Feed on server [" + address.getHostName()
                    + "]\n");

            while (true) {

                // Data feed
                mailContent.append("[" + sdf.format(new Date())
                        + "] Start feed\n");
                FeedReceiver feedReceiver = new FeedReceiver();
                boolean flag = feedReceiver.catchFeed();

                if (flag) {
                    mailContent.append("[" + sdf.format(new Date())
                            + "] Feed finished successfully\n");
                    report(mailContent.toString(), true);
                    return;
                } else {
                    mailContent.append("[" + sdf.format(new Date())
                            + "] Feed failed\n");
                    if (count == retryLimitCount) {
                        mailContent.append("Max retry count ["
                                + retryLimitCount + "] reached. \nAbort.");
                        report(mailContent.toString(), false);
                        return;
                    } else {
                        count++;
                        mailContent.append("Start retry in [" + retryInterval
                                + "] minutes\n");
                    }
                }
                try {
                    Thread.sleep(retryInterval * 60 * 1000);
                } catch (InterruptedException e) {
                    log.error("Unknown error", e);
                }
            }
        } catch (Exception e) {
            mailContent.append(e);
            report(mailContent.toString(), false);
            misLogger.fatal("excpetion in LoadTimerTask", e);
        }
    }

    /**
     * @param count
     * @param status 1. success 2. fail and retry 3. fail and stop
     */
    private void report(String message, boolean successful) {
        try {

            if (successful) {
                ReportMail.send(message,
                        "[INFO] Movies feed finished successully.");
            } else {
                ReportMail.send(message, "[ERROR] Movies feed FAILED.");
            }
        } catch (Exception e) {
            misLogger.fatal("email sent failed", e);
        }
    }

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
}
