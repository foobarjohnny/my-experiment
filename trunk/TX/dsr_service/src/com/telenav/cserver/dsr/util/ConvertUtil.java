package com.telenav.cserver.dsr.util;

import java.util.logging.Logger;
import com.telenav.cserver.backend.datatypes.Address;
import com.telenav.cserver.backend.datatypes.TnPoi;
import com.telenav.cserver.backend.datatypes.ace.GeoCodedAddress;
import com.telenav.j2me.datatypes.Stop;

/**
 * User: llzhang
 * Date: 2010-1-5
 * Time: 10:48:15
 */
public class ConvertUtil {
    public static final double DEGREE_MULTIPLIER = 1.e5; // 1e-5 deg units

    private static Logger logger = Logger.getLogger(ConvertUtil.class.getName()) ;
    
    public static Stop convert2Stop(GeoCodedAddress address) {
        Stop stop = new Stop();
        stop.firstLine = StrUtil.firstLetterUppercase(address.getFirstLine() == null ? "" : address.getFirstLine());
        stop.city = StrUtil.firstLetterUppercase(address.getCityName() == null ? "" : address.getCityName());
        stop.state = address.getState();
        stop.zip = address.getPostalCode();
        stop.lat = convert2DM5(address.getLatitude());
        stop.lon = convert2DM5(address.getLongitude());
        stop.isGeocoded = true;
        stop.country = address.getCountry();
        return stop;
    }

    private static Stop convert2Stop(Address address) {
        Stop stop = new Stop();
        stop.firstLine = address.getFirstLine();
        stop.city = StrUtil.firstLetterUppercase(address.getCityName() == null ? "" : address.getCityName());
        stop.state = address.getState();
        stop.zip = address.getPostalCode();
        stop.lat = convert2DM5(address.getLatitude());
        stop.lon = convert2DM5(address.getLongitude());
        stop.isGeocoded = true;
        stop.country = address.getCountry();
        return stop;
    }

    public static Address convert2Address(Stop stop) {
        if (stop == null){
            return null;
        }
        Address address = new Address();
        address.setFirstLine(stop.firstLine);
        address.setLatitude(convert2Degree(stop.lat));
        address.setLongitude(convert2Degree(stop.lon));
        address.setCityName(stop.city);
        address.setState(stop.state);
        address.setCountry(stop.country);
        address.setPostalCode(stop.zip);
        return address;
    }

    public static Stop convert2Stop(TnPoi tnPoi) {
        if (tnPoi == null || tnPoi.getAddress() == null) {
            return null;
        }
        Stop stop = convert2Stop(tnPoi.getAddress());
        stop.firstLine = tnPoi.getBrandName();
        return stop;
    }

    public static int convert2DM5(double degree) {
        return (int) (degree * DEGREE_MULTIPLIER);
    }

    public static double convert2Degree(int dm5) {
        return dm5 / DEGREE_MULTIPLIER;
    }
    
}
