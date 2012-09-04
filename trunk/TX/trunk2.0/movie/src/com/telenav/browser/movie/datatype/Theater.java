/******************************************************************************
 * Copyright (c) 2008 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on Oct 23, 2008
 * File name: Theater.java
 * Package name: com.telenav.j2me.browser.datatype
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: lwei (lwei@telenav.cn) 1:16:11 PM
 *  Update:
 *******************************************************************************/
package com.telenav.browser.movie.datatype;

/**
 * @author lwei (lwei@telenav.cn) 1:16:11 PM, Oct 23, 2008
 * 
 */
public class Theater {

    /** Fields. */
    private long id;

    private String vendorId;

    private String name;

    private String street;

    private String city;

    private String state;

    private String postalcode;

    private String country;

    private String telephone;

    private double lat;

    private double lon;

    private int active;
    
    private String timeZone;

    /**
     * The distance between the theater and the given position.
     * 
     * @see
     */
    private long distance = 0;

    /**
     * Set the reference point to get the distance.
     * 
     * @param lat The lat.
     * @param lon The lon.
     */
    public void setReferencePoint(double refLat, double refLon) {
        double dLon = (refLon - lon)
                * Math.cos((refLat + lat) / 2.0 / 180.0 * Math.PI);
        double dLat = refLat - lat;

        double ret = Math.sqrt(Math.pow(dLat, 2) + Math.pow(dLon, 2));

        ret *= 10000000.0 / 90.0;
        distance = (int) ret;
    }

    /**
     * Get the distance between the reference point and the theater. If the
     * reference point is not set, return 0.
     * 
     * @return
     */
    public long getDistance() {
        return distance;
    }

    /**
     * Judge whether the theater is in the certain geoCircle
     * 
     * @param geoCircle certain geoCircle
     * @return
     */
    public boolean inCircle(GeoCircle geoCircle) {
        if (geoCircle == null) {
            return false;
        }
        this.setReferencePoint(geoCircle.getLat(), geoCircle.getLon());
        if (this.getDistance() > geoCircle.getRadius() * 1609.344) {
            return false;
        }
        return true;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the vendorId
     */
    public String getVendorId() {
        return vendorId;
    }

    /**
     * @param vendorId the vendorId to set
     */

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Theater================START\n");

        buffer.append("id:" + this.getId() + "\n");
        buffer.append("vendorId:" + this.getVendorId() + "\n");
        buffer.append("name:" + this.getName() + "\n");
        buffer.append("street:" + this.getStreet() + "\n");
        buffer.append("city:" + this.getCity() + "\n");
        buffer.append("state:" + this.getState() + "\n");
        buffer.append("postalcode:" + this.getPostalcode() + "\n");
        buffer.append("country:" + this.getCountry() + "\n");
        buffer.append("telephone:" + this.getTelephone() + "\n");
        buffer.append("lon:" + this.getLon() + "\n");
        buffer.append("lat:" + this.getLat() + "\n");
        buffer.append("active:" + this.getActive() + "\n");
        buffer.append("distance:" + this.getDistance() + "\n");
        buffer.append("Theater================OVER\n");
        return buffer.toString();
    }

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

}
