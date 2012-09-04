/******************************************************************************
 * Copyright (c) 2010 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *******************************************************************************/
package com.telenav.cserver.poi.protocol;

import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.protocol.ProtocolRequestParser;
import com.telenav.cserver.poi.executor.BannerAdsRequest;
import com.telenav.datatypes.content.ads.v10.ImageSizeType;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.ws.datatypes.address.GeoCode;
import com.telenav.ws.datatypes.address.Location;

/**
 * @author jzhu (jzhu@telenav.cn) 10:10:08 AM
 */
public class BannerAdsRequestParser_CS implements ProtocolRequestParser
{
    private static final double DM5 = 100000;
    
    private static final int TYPE_CURRENT_LOCATION = 6;

    
    @Override
    public ExecutorRequest[] parse(Object object) throws ExecutorException
    {
        TxNode node = (TxNode) object;
        BannerAdsRequest request = new BannerAdsRequest();

        request.setExecutorType(node.msgAt(0));
        request.setPageId(node.msgAt(1));
        request.setKeyWord(node.msgAt(2));
        request.setSearchId(node.msgAt(3));
        request.setPageIndex((int)node.valueAt(3));
        
        
        Location location =  getLocation(node.valueAt(1), node.valueAt(2));
        request.setLoc(location);
        if (TYPE_CURRENT_LOCATION == node.valueAt(3))
        {
            request.setCurLoc(location);
        }
        
        double width = node.valueAt(5);
        double height = node.valueAt(6);
        // FIXME: what is the real configuration?
        final int preferredWidth = (int) (width * 0.95);
        int preferredHeight = (int) (height / 4);

        ImageSizeType maxSize = new ImageSizeType();
        maxSize.setHeight(preferredHeight);
        maxSize.setWidth(preferredWidth);
        request.setMaxSize(maxSize);

        ImageSizeType minSize = new ImageSizeType();
        minSize.setWidth((int) (width / 2));
        minSize.setHeight((int) (height * 0.04));

        request.setMinSize(minSize);
        
        return new ExecutorRequest[]
        { request };
    }
    
    
    private Location getLocation(long lat, long lon)
    {
        Location loc = new Location();
        GeoCode geoCode = new GeoCode();
        geoCode.setLatitude((double)lat / DM5);
        geoCode.setLongitude((double)lon/ DM5);
        loc.setGeoCode(geoCode);
        return loc;
    }
}
