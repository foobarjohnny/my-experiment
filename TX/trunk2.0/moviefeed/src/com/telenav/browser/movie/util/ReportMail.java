/******************************************************************************
 * Copyright (c) 2008 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on Nov 20, 2008
 * File name: ReportMail.java
 * Package name: com.telenav.j2me.browser.vendor
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: dysong(dysong@telenav.cn) 6:36:42 PM 
 *  Update:
 *******************************************************************************/
package com.telenav.browser.movie.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.PropertyResourceBundle;

import org.apache.log4j.Logger;

import com.televigation.log.TVCategory;
import com.televigation.util.mail.TvMail;

/**
 * @author dysong (dysong@telenav.cn) 6:36:42 PM, Nov 20, 2008
 */

public class ReportMail implements Runnable {
    private static PropertyResourceBundle config = null;
    private static final Logger misLogger = (Logger) TVCategory
            .getInstance("movie.feed.mis");
    private String strMsg;

    private String strSubject;

    static public void send(String strMsg, String strSubject) {
        ReportMail sender = new ReportMail(strMsg, strSubject);
        Thread t = new Thread(sender);
        t.start();
    }

    public ReportMail(String strMsg, String strSubject) {
        this.strMsg = strMsg;
        this.strSubject = strSubject;

        try {
            InputStream is = new FileInputStream(
                    "/home/tnuser/movies_config/mail.properties");
            config = new PropertyResourceBundle(is);
        } catch (Exception e) {
            misLogger.error("Fail to load mail properties", e);
        }
    }

    public void run() {
        try {
            if (config == null) {
                return;
            }
            String receipt = config.getString("report.mail.receipt");
            String strSystemSender = config.getString("report.mail.sender");
            String strMailServer = config.getString("report.mail.server");
            String[] receipts = receipt.split(",");
            logDebug("strSystemSender:" + strSystemSender);
            logDebug("strMailServer:" + strMailServer);
            logDebug("strMsg:" + strMsg);
            logDebug("strSubject:" + strSubject);
            for (int i = 0; i < receipts.length; i++) {
                logDebug("receipts[" + i + "]:" + receipts[i]);
                TvMail.sendMail(strSystemSender, receipts[i], this.strSubject,
                        this.strMsg, strMailServer);
                misLogger.info("Mail has been send to " + receipts[i]);
            }

        } catch (Exception e) {
            misLogger.error("Fail to send mail", e);
        }
    }

    private void logDebug(String s) {
        System.out.println(s);
    }
}