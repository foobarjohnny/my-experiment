/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.datatypes.feedback;

/**
 * The content of navigation, that contain origin,destination,deviation and current location
 * 
 * @author zhjdou
 * 
 */
public class NavigationContext
{
    private GeoCode[] originFix;

    private GeoCode destination;

    private GeoCode[] deviationFix;

    private GeoCode[] currentLocation;

    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append("NavigationContext=[");
        sb.append("originFix=");
        if(this.originFix!=null) {
            for(GeoCode code:this.originFix) {
                if(code!=null) {
                    sb.append(code.toString());
                    sb.append("\n");
                 }
            }
        }
        sb.append(", destination=");
        if(this.destination!=null) {
           sb.append(this.destination.toString());
        }
        sb.append(", deviationFix=");
        if(this.deviationFix!=null) {
            for(GeoCode code:this.deviationFix) {
                if(code!=null) {
                    sb.append(code.toString());
                    sb.append("\n");
                 }
            }
        }
        sb.append(", currentLocation=");
        if(this.currentLocation!=null) {
            for(GeoCode code:this.currentLocation) {
                if(code!=null) {
                    sb.append(code.toString());
                    sb.append("\n");
                 }
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * @return the originFix
     */
    public GeoCode[] getOriginFix()
    {
        return originFix;
    }

    /**
     * @param originFix the originFix to set
     */
    public void setOriginFix(GeoCode[] originFix)
    {
        this.originFix = originFix;
    }

    /**
     * @return the destination
     */
    public GeoCode getDestination()
    {
        return destination;
    }

    /**
     * @param destination the destination to set
     */
    public void setDestination(GeoCode destination)
    {
        this.destination = destination;
    }

    /**
     * @return the deviationFix
     */
    public GeoCode[] getDeviationFix()
    {
        return deviationFix;
    }

    /**
     * @param deviationFix the deviationFix to set
     */
    public void setDeviationFix(GeoCode[] deviationFix)
    {
        this.deviationFix = deviationFix;
    }

    /**
     * @return the currentLocation
     */
    public GeoCode[] getCurrentLocation()
    {
        return currentLocation;
    }

    /**
     * @param currentLocation the currentLocation to set
     */
    public void setCurrentLocation(GeoCode[] currentLocation)
    {
        this.currentLocation = currentLocation;
    }

}
