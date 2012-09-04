/*
 * Created on 2009-6-26
 * 
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.datatypes.recentstop;

import com.telenav.cserver.backend.datatypes.Address;

/**
 * The construction of RecentAddressProx is similar with Address and the subclass recent address
 * @author zhjdou
 * 
 */
public class RecentAddress extends Address
{
    private int streetNumberHigh;//
    private int streetNumberLow;//
    private String landmark; //
    private String postalCodePlus4;//
    private String streetNumber; // we only have street
    private boolean isIntersection;//
    private String poBox;//
  
    /**
     * @return the streetNumber
     */
    public String getStreetNumber()
    {
        return streetNumber;
    }
    /**
     * @param streetNumber the streetNumber to set
     */
    public void setStreetNumber(String streetNumber)
    {
        this.streetNumber = streetNumber;
    }
   
    /**
     * @return the isIntersection
     */
    public boolean isIntersection()
    {
        return isIntersection;
    }
    /**
     * @param isIntersection the isIntersection to set
     */
    public void setIntersection(boolean isIntersection)
    {
        this.isIntersection = isIntersection;
    }
    
    /**
     * @return the poBox
     */
    public String getPoBox()
    {
        return poBox;
    }
    /**
     * @param poBox the poBox to set
     */
    public void setPoBox(String poBox)
    {
        this.poBox = poBox;
    }
    /**
     * @return the city
     */
      
    /**
     * @return the streetNumberHigh
     */
    public int getStreetNumberHigh()
    {
        return streetNumberHigh;
    }
    /**
     * @param streetNumberHigh the streetNumberHigh to set
     */
    public void setStreetNumberHigh(int streetNumberHigh)
    {
        this.streetNumberHigh = streetNumberHigh;
    }
    /**
     * @return the streetNumberLow
     */
    public int getStreetNumberLow()
    {
        return streetNumberLow;
    }
    /**
     * @param streetNumberLow the streetNumberLow to set
     */
    public void setStreetNumberLow(int streetNumberLow)
    {
        this.streetNumberLow = streetNumberLow;
    }
   
    /**
     * @return the landmark
     */
    public String getLandmark()
    {
        return landmark;
    }
    /**
     * @param landmark the landmark to set
     */
    public void setLandmark(String landmark)
    {
        this.landmark = landmark;
    }

    /**
     * @return the postalCodePlus4
     */
    public String getPostalCodePlus4()
    {
        return postalCodePlus4;
    }
    /**
     * @param postalCodePlus4 the postalCodePlus4 to set
     */
    public void setPostalCodePlus4(String postalCodePlus4)
    {
        this.postalCodePlus4 = postalCodePlus4;
    }
    
    /**
     * record basic information
     */
    public String toString() {
        StringBuffer bf=new StringBuffer();
        bf.append("recentAddress[");
        bf.append("addressId=");
        bf.append(this.getAddressId());
        bf.append(", city=");
        bf.append(this.getCityName());
        bf.append(", state=");
        bf.append(this.getState());
        bf.append(", country=");
        bf.append(this.getCountry());
        bf.append(", county=");
        bf.append(this.getCountry());
        bf.append(", postalCode=");
        bf.append(this.getPostalCode());
        bf.append(", label=");
        bf.append(this.getLabel());
        bf.append(", latitude=");
        bf.append(this.getLatitude());
        bf.append(", longitude");
        bf.append(this.getLongitude());
        bf.append(", firstLine=");
        bf.append(this.getFirstLine());
        bf.append(", streetNumberHigh=");
        bf.append(this.streetNumberHigh);
        bf.append(", streetNumberLow=");
        bf.append(this.streetNumberLow);
        bf.append(", lastline=");
        bf.append(this.getLastLine());
        bf.append(", landmark=");
        bf.append(this.landmark);
        bf.append(", postalCodePlus4=");
        bf.append(this.postalCodePlus4);
        bf.append(", streetName=");
        bf.append(this.getStreetName());
        bf.append(", streetNumber=");
        bf.append(this.getStreetNumber());
        bf.append(", isIntersection=");
        bf.append(this.isIntersection);
        bf.append(", suite=");
        bf.append(this.getSuite());
        bf.append(", poBox");
        bf.append(this.poBox);
        bf.append("]");
        return bf.toString();
    }
}