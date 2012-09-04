/******************************************************************************
 * Copyright (c) 2006 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on Nov 28, 2008
 * File name: TMSConfig.java
 * Package name: com.telenav.j2me.browser.vendor.TMS
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: lwei(lwei@telenav.cn) 11:21:02 AM
 *  Update:
 *******************************************************************************/
package com.telenav.browser.movie.vendor.tms;

import java.util.PropertyResourceBundle;

import org.apache.log4j.Logger;

import com.televigation.log.TVCategory;

/**
 * The configuration of TMS vendor.
 * 
 * @author lwei (lwei@telenav.cn) 11:21:02 AM
 */
public class TmsConfig {
    private static final Logger misLogger = (Logger) TVCategory
            .getInstance("movie.feed.mis");

    /** The config information. */
    private String host;
    private String account;
    private String password;
    private String dataRemotePath;
    private String imageRemotePath;
    private String dataLocalPath;

    private String programFileInitial;
    private String scheduleFileInitial;
    private String theaterFileInitial;
    private int defaultTimeout;
    private int establishTimeout;

    /**
     * Load the configuration.
     * 
     * @return Succeeded or not.
     */
    public boolean loadConfig() {
        misLogger.info("Loading TMS configuration.");
        try {
            PropertyResourceBundle serverBundle = (PropertyResourceBundle) PropertyResourceBundle
                    .getBundle("config.tms_feed");

            // Get properties.
            String host = serverBundle.getString("host");
            String account = serverBundle.getString("account");
            String password = serverBundle.getString("password");
            String dataRemotePath = serverBundle.getString("data.remote.path");
            String dataLocalPath = serverBundle.getString("data.local.path");
            String imageRemotePath = serverBundle
                    .getString("image.remote.path");

            String programFileInitial = serverBundle
                    .getString("program.file.initial");
            String scheduleFileInitial = serverBundle
                    .getString("schedule.file.initial");
            String theaterFileInitial = serverBundle
                    .getString("theater.file.initial");
            String defaultTimeout = serverBundle.getString("default.timeout");
            String establishTimeout = serverBundle
                    .getString("establish.timeout");

            // Set values.
            if (checkStr(host) && checkStr(account) && checkStr(password)
                    && checkStr(dataRemotePath) && checkStr(dataLocalPath)
                    && checkStr(imageRemotePath)
                    && checkStr(programFileInitial)
                    && checkStr(scheduleFileInitial)
                    && checkStr(theaterFileInitial) && checkStr(defaultTimeout)
                    && checkStr(establishTimeout)) {
                this.host = host;
                this.account = account;
                this.password = password;
                this.dataRemotePath = dataRemotePath;
                this.imageRemotePath = imageRemotePath;
                this.dataLocalPath = dataLocalPath;

                this.programFileInitial = programFileInitial;
                this.scheduleFileInitial = scheduleFileInitial;
                this.theaterFileInitial = theaterFileInitial;
                this.defaultTimeout = Integer.parseInt(defaultTimeout) * 1000;
                this.establishTimeout = Integer.parseInt(establishTimeout) * 1000;
                misLogger
                        .info("Config file for TMS vendor is loaded successfully.");
                return true;
            }
        } catch (Exception e) {
            misLogger.error("Failed to parse the config file for TMS vendor.",
                    e);
        }
        return false;
    }

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return the account
     */
    public String getAccount() {
        return account;
    }

    /**
     * @param account the account to set
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the dataRemotePath
     */
    public String getDataRemotePath() {
        return dataRemotePath;
    }

    /**
     * @param dataRemotePath the dataRemotePath to set
     */
    public void setDataRemotePath(String dataRemotePath) {
        this.dataRemotePath = dataRemotePath;
    }

    /**
     * @return the imageRemotePath
     */
    public String getImageRemotePath() {
        return imageRemotePath;
    }

    /**
     * @param imageRemotePath the imageRemotePath to set
     */
    public void setImageRemotePath(String imageRemotePath) {
        this.imageRemotePath = imageRemotePath;
    }

    /**
     * @return the dataLocalPath
     */
    public String getDataLocalPath() {
        return dataLocalPath;
    }

    /**
     * @param dataLocalPath the dataLocalPath to set
     */
    public void setDataLocalPath(String dataLocalPath) {
        this.dataLocalPath = dataLocalPath;
    }

    /**
     * @return the programFileInitial
     */
    public String getProgramFileInitial() {
        return programFileInitial;
    }

    /**
     * @param programFileInitial the programFileInitial to set
     */
    public void setProgramFileInitial(String programFileInitial) {
        this.programFileInitial = programFileInitial;
    }

    /**
     * @return the scheduleFileInitial
     */
    public String getScheduleFileInitial() {
        return scheduleFileInitial;
    }

    /**
     * @param scheduleFileInitial the scheduleFileInitial to set
     */
    public void setScheduleFileInitial(String scheduleFileInitial) {
        this.scheduleFileInitial = scheduleFileInitial;
    }

    /**
     * @return the theaterFileInitial
     */
    public String getTheaterFileInitial() {
        return theaterFileInitial;
    }

    /**
     * @param theaterFileInitial the theaterFileInitial to set
     */
    public void setTheaterFileInitial(String theaterFileInitial) {
        this.theaterFileInitial = theaterFileInitial;
    }

    private boolean checkStr(String toCheck) {
        if (toCheck == null || toCheck.trim().length() == 0) {
            return false;
        }
        return true;
    }

    /**
     * @return the defaultTimeout
     */
    public int getDefaultTimeout() {
        return defaultTimeout;
    }

    /**
     * @param defaultTimeout the defaultTimeout to set
     */
    public void setDefaultTimeout(int defaultTimeout) {
        this.defaultTimeout = defaultTimeout;
    }

    /**
     * @return the establishTimeout
     */
    public int getEstablishTimeout() {
        return establishTimeout;
    }

    /**
     * @param establishTimeout the establishTimeout to set
     */
    public void setEstablishTimeout(int establishTimeout) {
        this.establishTimeout = establishTimeout;
    }
}
