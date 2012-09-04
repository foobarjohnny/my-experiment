/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.adservice;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.telenav.cserver.backend.datatypes.GeoCode;
import com.telenav.cserver.backend.datatypes.adservice.BillBoardAds;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.j2me.datatypes.GpsData;
import com.telenav.kernel.util.datatypes.TnContext;



public class TestAdServiceService extends TestCase
{
    private TnContext tnContext;
	
    @Override
    protected void setUp() throws Exception
    {
        tnContext = new TnContext();
        tnContext.addProperty(TnContext.PROP_CARRIER, "ATT");
        tnContext.addProperty(TnContext.PROP_DEVICE, "9000");
        tnContext.addProperty(TnContext.PROP_PRODUCT, "RIM");
        tnContext.addProperty(TnContext.PROP_VERSION, "6.0.01");
        tnContext.addProperty("application", "ATT_NAV");
        tnContext.addProperty("login", "3817799999");
        tnContext.addProperty("userid", "3707312");
        
        
    }

    /**
     * 
     * @throws ThrottlingException
     */
    public void testGetBillBoardAds() throws ThrottlingException
    {
        GpsData gpsData = new GpsData();
        gpsData.lat = 3737501;
        gpsData.lon = -12199770;
        List<GeoCode> geoCodeList = new ArrayList<GeoCode>();
        GeoCode geoCode = new GeoCode();
        geoCode.setLatitude(gpsData.lat/1.0e5);
        geoCode.setLongitude(gpsData.lon/1.0e5);
        geoCodeList.add(geoCode);
        
        List<BillBoardAds>  billBoardAds = AdServiceProxy.getInstance().getBillBoardAds(123456, gpsData, geoCodeList, tnContext);
        
        
        assertTrue(billBoardAds.size()>0);
        
    }
    
    
}
