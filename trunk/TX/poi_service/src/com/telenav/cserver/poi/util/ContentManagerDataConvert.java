/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.poi.util;

import com.telenav.cserver.poi.datatypes.Review;
import com.telenav.cserver.poi.datatypes.Stop;

/**
 * Data convert utility
 * @author zhjdou 2009-7-17
 */
public class ContentManagerDataConvert
{
    /**
     * convert the request to the c-server wrapped poi datatype
     * 
     * @param c_POI
     * @return
     */
    public static com.telenav.cserver.backend.datatypes.contents.EditablePoi convertClientPOI2CserverEditPOI(
            com.telenav.cserver.poi.datatypes.POI poiDataType,long userId)
    {
        com.telenav.cserver.backend.datatypes.contents.EditablePoi editablePoi = new com.telenav.cserver.backend.datatypes.contents.EditablePoi();
        editablePoi.setUserId(userId);
        editablePoi.setRating(poiDataType.avgRating);
        editablePoi.setPoiId(poiDataType.poiId);
        editablePoi.setBusinessHours(poiDataType.businessHourString);
        if (poiDataType.bizPoi != null)//bizpoi
        {
            editablePoi.setBrandName(poiDataType.bizPoi.brand);//
            editablePoi.setAddress(convertPOIStop_To_Address(poiDataType.bizPoi.address));
            editablePoi.setPhone(poiDataType.bizPoi.phoneNumber);
        }//bizpoi end
     
        if (poiDataType.getReviews().size() > 0)//review index 0
        {
            Review review = poiDataType.getReviews().get(0);
            editablePoi.setReview(review.reviewString);
            editablePoi.setRating(review.reviewStarRating);
        }//review
        if (poiDataType.priceRange != -1) {
            editablePoi.setPriceRange(poiDataType.priceRange);
        }
       
        return editablePoi;
    }

    /**
     * convert stop that store infor from client to Address(c-server defined)
     * 
     * @param stop
     * @return
     */
    public static com.telenav.cserver.backend.datatypes.contents.Stop convertPOIStop_To_Address(Stop stop)
    {
        if (stop == null)
        {
            return null;
        }
        com.telenav.cserver.backend.datatypes.contents.Stop address = new com.telenav.cserver.backend.datatypes.contents.Stop();
        address.setCityName(stop.city);
        address.setCountry(stop.country);
        address.setFirstLine(stop.firstLine);
        address.setLatitude(convertToDegree(stop.lat));
        address.setLongitude(convertToDegree(stop.lon));
        // firstline=door + street name or equal street at street2(cross street name)
        address.setCrossStreetName(stop.street2);//
        address.setPostalCode(stop.zip);
        address.setAddressId(stop.stopId);
        address.setCounty(stop.county);
        address.setLabel(stop.label);
        address.setState(stop.state);
        //TODO we need street name attribute from client like:address.setStreetName(stop.streetName);
        return address;

    }

    /**
     * Here is about protocol the unit of latitude and longitude between client and cserver and backend
     * 
     * @param dm5
     * @return
     */
    public static double convertToDegree(int dm5)
    {
        return dm5 / 1.e5;
    }
}
