/******************************************************************************
 * Copyright (c) 2008 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on Nov 28, 2008
 * File name: BoundingBox.java
 * Package name: com.telenav.j2me.browser.datatype
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: dysong(dysong@telenav.cn) 5:01:21 PM, Nov 28, 2008
 *  Update:
 *******************************************************************************/
package com.telenav.browser.movie.datatype;

import com.telenav.browser.movie.Constants;

/**
 * The bounding box is a square with center point(refLat,refLon) and side length
 * is radius.
 * 
 * @author dysong (dysong@telenav.cn) 5:01:21 PM, Nov 28, 2008
 */
public class BoundingBox {

    private double latA;

    private double latB;

    private double lonA;

    private double lonB;

    private double maxLat;

    private double maxLon;

    private double minLat;

    private double minLon;

    /**
     * Get a bouding box with center point(refLat,refLon) and half of side
     * length (radius)
     * 
     * @param refLat
     * @param refLon
     * @param radius
     * @return
     */
    public static BoundingBox getBoundingBox(double refLat, double refLon,
            double radius) {

        BoundingBox boundingBox = new BoundingBox();

        double offset = radius * Constants.MILE_METER / 100000;

        boundingBox.setLonA(refLon + offset);
        boundingBox.setLonB(refLon - offset);

        boundingBox.setLatA(refLat + offset);
        boundingBox.setLatB(refLat - offset);

        boundingBox.setMaxMin();

        return boundingBox;

    }

    /**
     * Set the max(min) lat and lon of bounding box, considering border of lon.
     */
    private void setMaxMin() {
        setMaxLat(Math.max(getLatA(), getLatB()));
        setMinLat(Math.min(getLatA(), getLatB()));
        if (getLonA() * getLonB() < 0 && Math.abs(getLonA() - getLonB()) > 180) {
            setMaxLon(Math.min(getLonA(), getLonB()));
            setMinLon(Math.max(getLonA(), getLonB()));
        } else {
            setMaxLon(Math.max(getLonA(), getLonB()));
            setMinLon(Math.min(getLonA(), getLonB()));
        }
    }

    /**
     * Check whether the point(refLat,refLon) is in the certain bounding box
     * 
     * @param refLat
     * @param refLon
     * @param boundingBox
     * @return
     */
    public static boolean inBoundingBox(double refLat, double refLon,
            BoundingBox boundingBox) {

        if (refLat > boundingBox.getMaxLat()) {
            return false;
        }
        if (refLat < boundingBox.getMinLat()) {
            return false;
        }
        if (refLon > boundingBox.getMaxLon()) {
            return false;
        }

        if (refLon < boundingBox.getMinLon()) {
            return false;
        }
        return true;

    }

    public double getLatA() {
        return latA;
    }

    public void setLatA(double latA) {
        this.latA = latA;
    }

    public double getLatB() {
        return latB;
    }

    public void setLatB(double latB) {
        this.latB = latB;
    }

    public double getLonA() {
        return lonA;
    }

    public void setLonA(double lonA) {
        this.lonA = lonA;
    }

    public double getLonB() {
        return lonB;
    }

    public void setLonB(double lonB) {
        this.lonB = lonB;
    }

    public double getMinLon() {
        return minLon;
    }

    public void setMinLon(double minLon) {
        this.minLon = minLon;
    }

    public double getMaxLat() {
        return maxLat;
    }

    public void setMaxLat(double maxLat) {
        this.maxLat = maxLat;
    }

    public double getMaxLon() {
        return maxLon;
    }

    public void setMaxLon(double maxLon) {
        this.maxLon = maxLon;
    }

    public double getMinLat() {
        return minLat;
    }

    public void setMinLat(double minLat) {
        this.minLat = minLat;
    }
}
