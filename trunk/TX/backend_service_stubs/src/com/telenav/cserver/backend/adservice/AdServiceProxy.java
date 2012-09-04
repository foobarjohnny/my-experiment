/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.adservice;

import java.util.ArrayList;
import java.util.List;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.log4j.Logger;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.backend.config.WebServiceConfigInterface;
import com.telenav.cserver.backend.datatypes.adservice.BillBoardAds;
import com.telenav.cserver.backend.datatypes.adservice.GeoFence;
import com.telenav.cserver.backend.util.WebServiceConfiguration;
import com.telenav.cserver.backend.util.WebServiceUtils;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.cserver.framework.throttling.ThrottlingManager;
import com.telenav.datatypes.address.v30.GeoCode;
import com.telenav.datatypes.ads.v20.DisplayTime;
import com.telenav.datatypes.ads.v20.MobileDisplayAd;
import com.telenav.datatypes.services.v20.ResponseStatus;
import com.telenav.j2me.datatypes.GpsData;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.services.ads.v20.AdServiceStub;
import com.telenav.services.ads.v20.BillboardAdsRequestDTO;
import com.telenav.services.ads.v20.BillboardAdsResponseDTO;
import com.telenav.ws.datatypes.common.Distance;
import com.telenav.ws.datatypes.common.DistanceUnit;
import com.telenav.ws.datatypes.common.Speed;
import com.telenav.ws.datatypes.common.SpeedUnit;
import com.telenav.ws.datatypes.common.Time;
import com.telenav.ws.datatypes.common.TimeUnit;


/**
 * AdServiceProxy
 *
 * @author jzhu@telenav.cn
 * @version 1.0 2011-3-16
 */
public class AdServiceProxy
{
    public final static String SERVICE_ADVERTISE = "SERVICE_ADVERTISE";
    public final static String WS_ADVERTISE = "ADVERTISE";
    
    private static Logger logger = Logger.getLogger(AdServiceProxy.class);
    
    private static AdServiceProxy instance = new AdServiceProxy();
    private static ConfigurationContext configurationContext = WebServiceUtils.createConfigurationContext(WS_ADVERTISE); ;
    private AdServiceProxy() 
    {

    }
    
    /**
     * the only way to get a CommuteAlertServiceProxy instance
     * @return
     */
    public static AdServiceProxy getInstance() 
    {
        return instance;
    }
    
     /**
     * get BillBoardAds list
     * 
     * @param alertID
     * @return
     * @throws Exception
     */
    public List<BillBoardAds> getBillBoardAds(long routeId, GpsData gpsData, List<com.telenav.cserver.backend.datatypes.GeoCode> routePoints, TnContext tc) throws ThrottlingException 
    {
        List<BillBoardAds> list = new ArrayList<BillBoardAds>(); 
        
        CliTransaction cli = new CliTransaction(CliConstants.TYPE_MODULE);
        cli.setFunctionName("getBillBoardAds");
        boolean startAPICall = false;
        AdServiceStub stub = null;
        try
        {
            startAPICall = ThrottlingManager.startAPICall(SERVICE_ADVERTISE, tc);
            if(!startAPICall)
            {
                //can't call this API anymore, throws Exception
                logger.error("can't call this API anymore");
                throw new ThrottlingException();
            }
            
            BillboardAdsRequestDTO request = createRequest();
            request.setRouteId(String.valueOf(routeId));
            
            GeoCode geoCode = new GeoCode();
            geoCode.setLatitude(((double)gpsData.lat)/1.0e5);
            geoCode.setLongitude(((double)gpsData.lon)/1.0e5);
            request.setPosition(geoCode);
            
            request.setHeading(gpsData.heading);
            
            request.setSpeed(toSpeed(gpsData.speed));
            request.setRoutePoint(toGeoCode(routePoints));
            request.setContextString(tc==null?"":tc.toContextString());
            
            try
            {
                stub = createStub();
                cli.addData("getBillboardAds", "routeId=" + routeId + ",gpsData="+toString(gpsData)+", routePoint=" + toString(request.getRoutePoint()));
                cli.addData("tncontext", request.getContextString());
                BillboardAdsResponseDTO response = stub.getBillboardAds(request);
                
                if (response != null && response.getResponseStatus() != null)
                {
                    ResponseStatus status = response.getResponseStatus();
                    
                    cli.addData("getBillboardAds", "statusCode=" + status.getStatusCode() + "&message=" + status.getStatusMessage());
                    if ("OK".equalsIgnoreCase(status.getStatusCode()))
                    {
                        MobileDisplayAd[] mobileDisplayAds = response.getAdvertisement();
                        if (mobileDisplayAds != null)
                        {
                            for(MobileDisplayAd mobileDisplayAd : mobileDisplayAds)
                            {
                                list.add(toBillBoardAds(mobileDisplayAd));
                            }
                        }

                    }
                }
            } catch (Exception e)
            {
                cli.setStatus(e);
                logger.fatal("getBillBoardAds:", e);
            }
        }
        finally
        {
            cli.complete();
            if(startAPICall)
            {
                ThrottlingManager.endAPICall(SERVICE_ADVERTISE, tc);
            }
            
            WebServiceUtils.cleanupStub(stub);
        }
        
        return list;
    }
    
    private String toString(GeoCode point)
    {
        if (point == null)
            return "NULL";
        else
            return "lat:"+point.getLatitude() + ", lon:" + point.getLongitude();
    }
    
    /**
     * @param routePoint
     * @return
     */
    private String toString(GeoCode[] routePoint)
    {
        if (routePoint == null)
            return "NULL";
        else
        {
            StringBuffer sb = new StringBuffer();
            sb.append("size = " + routePoint.length).append(":");
            
            //print 5 points at most
            int dec = routePoint.length / 5 + 1;
            for(int i=0; i<routePoint.length; i=i+dec)
            {
                sb.append("["+i+"]:").append(toString(routePoint[i])).append(",");
            }
            return sb.toString();
            
        }
        
    }

    private static String toString(GpsData gpsData)
    {
        if (gpsData == null)
            return "NULL";
        else
            return "lat:"+gpsData.lat + ", lon:" + gpsData.lon + ", timestamp:"+gpsData.timeTag;
    }
    
    

    
    private GeoCode[] toGeoCode(List<com.telenav.cserver.backend.datatypes.GeoCode> routePoints)
    {
        if (routePoints == null)
            return new GeoCode[0];
        
        GeoCode[] geoCodes = new GeoCode[routePoints.size()];
        for(int i=0; i<geoCodes.length; i++)
        {
            com.telenav.cserver.backend.datatypes.GeoCode point = routePoints.get(i);
            geoCodes[i] = new GeoCode();
            geoCodes[i].setLatitude(point.getLatitude());
            geoCodes[i].setLongitude(point.getLongitude());
        }
        
        return geoCodes;
    }

    /**
     * create an BillboardAdsRequestDTO
     * 
     * @return
     */
    private BillboardAdsRequestDTO createRequest()
    {
        BillboardAdsRequestDTO request = new BillboardAdsRequestDTO();
        request.setClientName("c-server");
        request.setClientVersion("1.0");
        request.setTransactionId("unknown");
        
        return request;
    }
    
    /**
     * 
     * @return
     * @throws AxisFault 
     * @throws CommuteAlertException 
     */
    private AdServiceStub createStub() throws AxisFault
    {
        WebServiceConfigInterface config = WebServiceConfiguration.getInstance().getServiceConfig(WS_ADVERTISE);
        return new AdServiceStub(configurationContext, config.getServiceUrl());
    }
    
    private BillBoardAds toBillBoardAds(MobileDisplayAd mobileDisplayAd)
    {
        BillBoardAds billBoardAds = new BillBoardAds();
        
        long adsId = mobileDisplayAd.getAdIdentifier().getAdId();
        DisplayTime[] displayTimes = mobileDisplayAd.getDisplayRule().getDisplayTime();// Initial View and Detail View and POI View
        Time expirationTime = mobileDisplayAd.getDisplayRule().getExpirationTime();
        GeoCode geoFenceCenter = mobileDisplayAd.getDisplayRule().getGeoFenceCenter();
        Distance distance = mobileDisplayAd.getDisplayRule().getGeoFenceRadius();
        String adsSource = mobileDisplayAd.getAdIdentifier().getProviderCode();
        
        billBoardAds.setAdsId(adsId);
        if (displayTimes.length == 3)
        {
            billBoardAds.setInitialViewTime(toMilliSeconds(displayTimes[0].getDisplayTimeValue()));
            billBoardAds.setDetailViewTime(toMilliSeconds(displayTimes[1].getDisplayTimeValue()));
            billBoardAds.setPoiViewTime(toMilliSeconds(displayTimes[2].getDisplayTimeValue()));
        }
        
        billBoardAds.setExpirationTime(toMilliSeconds(expirationTime));
        billBoardAds.setGeoFence(toGeoFence(geoFenceCenter, distance));
        billBoardAds.setAdsSource(adsSource);

        
        return billBoardAds;
        
    }

    private double DM5 = 1.0e5; 
    private GeoFence toGeoFence(GeoCode geoFenceCenter, Distance distance)
    {
        GeoFence geoFence = new GeoFence();
        geoFence.setDistance(toMeter(distance));
        geoFence.setLat((int)(geoFenceCenter.getLatitude()*DM5));
        geoFence.setLon((int)(geoFenceCenter.getLongitude()*DM5));
        
        return geoFence;
    }
    
    private Speed toSpeed(double DM6)
    {
        Speed speed = new Speed();
        speed.setValue(DM6/6/3.6);//original one is (meters/second * 6)
        speed.setUnits(SpeedUnit.KPH);
        
        return speed;
    }
    
    private static final double MILE_TO_METER = 1609.344;
    private static final double FT_TO_METER = 0.189393939;
    private double toMeter(Distance distance)
    {
        if (distance == null)
            return 0;
        
        if (DistanceUnit.MI.equals(distance.getUnits()))
            return distance.getValue() * MILE_TO_METER;
        
        if (DistanceUnit.KM.equals(distance.getUnits()))
            return distance.getValue() * 1000;
        
        if (DistanceUnit.M.equals(distance.getUnits()))
            return distance.getValue();
        
        if (DistanceUnit.FT.equals(distance.getUnits()))
            return distance.getValue() * FT_TO_METER;
        
        else 
            return -1;
    }
    
    private long toMilliSeconds(Time time)
    {
        if (time == null)
            return 0;
        
        long value = time.getValue();
        if (TimeUnit.SECOND.equals(time.getUnits()))
            return value * 1000;
        
        if (TimeUnit.MILLISECOND.equals(time.getUnits()))
            return value;
        
        if (TimeUnit.MINUTE.equals(time.getUnits()))
            return value*60*1000;
        
        if (TimeUnit.HOUR.equals(time.getUnits()))
            return value*3600*1000;
        
        if (TimeUnit.DAY.equals(time.getUnits()))
            return value*24*3600*1000;
        
        if (TimeUnit.WEEK.equals(time.getUnits()))
            return value*7*24*3600*1000;
        
        else 
            return -1;//don't care month and year

    }
    
}
