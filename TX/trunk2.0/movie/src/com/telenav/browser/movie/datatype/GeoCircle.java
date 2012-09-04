/******************************************************************************
 * Copyright (c) 2006 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on Oct 23, 2008
 * File name: GeoCircle.java
 * Package name: com.telenav.j2me.browser.datatype
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: lwei(lwei@telenav.cn) 2:35:07 PM
 *  Update:
 *******************************************************************************/
package com.telenav.browser.movie.datatype;

import com.telenav.j2me.datatypes.TxNode;

/**
 * @author lwei (lwei@telenav.cn) 2:35:07 PM, Oct 23, 2008
 */
public class GeoCircle {

    public static final long MULTIPLIER = 10000;

    /** The fields. */
    private double lat;
    private double lon;
    private int radius;

    /**
     * @return the lat
     */
    public double getLat() {
        return lat;
    }

    /**
     * @param lat the lat to set
     */
    public void setLat(double lat) {
        this.lat = lat;
    }

    /**
     * @return the lon
     */
    public double getLon() {
        return lon;
    }

    /**
     * @param lon the lon to set
     */
    public void setLon(double lon) {
        this.lon = lon;
    }

    /**
     * @return the radius
     */
    public int getRadius() {
        return radius;
    }

    /**
     * @param radius the radius to set
     */
    public void setRadius(int radius) {
        this.radius = radius;
    }

    public TxNode toTxNode() {
        TxNode node = new TxNode();
        node.addValue((long) (lat * MULTIPLIER));
        node.addValue((long) (lon * MULTIPLIER));
        node.addValue(radius);
        return node;
    }

    public static GeoCircle fromTxNode(TxNode node) {
        GeoCircle circle = new GeoCircle();
        circle.lat = ((double) node.valueAt(0)) / MULTIPLIER;
        circle.lon = ((double) node.valueAt(1)) / MULTIPLIER;
        circle.radius = (int) node.valueAt(2);

        return circle;
    }
}
