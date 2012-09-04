package com.telenav.cserver.poi.protocol.protobuf;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.protocol.ProtocolRequestParser;
import com.telenav.cserver.poi.executor.BannerAdsRequest;
import com.telenav.datatypes.content.ads.v10.ImageSizeType;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoBannerAdsReq;
import com.telenav.j2me.framework.util.ToStringUtils;
import com.telenav.ws.datatypes.address.GeoCode;
import com.telenav.ws.datatypes.address.Location;

public class BannerAdsProtoRequestParser implements ProtocolRequestParser 

{
    private static final double DM5 = 100000;

    private static final int TYPE_CURRENT_LOCATION = 6;

    private static final Logger logger = Logger.getLogger(BannerAdsProtoRequestParser.class);
    @Override
    public ExecutorRequest[] parse(Object object) throws ExecutorException
    {
        BannerAdsRequest request = new BannerAdsRequest();
        ProtocolBuffer protoBuffer = (ProtocolBuffer) object;
        ProtoBannerAdsReq pBannerAds = null;
        try
        {
            pBannerAds = ProtoBannerAdsReq.parseFrom(protoBuffer
                    .getBufferData());
            if(logger.isDebugEnabled())
            {
            	logger.debug(ToStringUtils.toString(pBannerAds));
            }
        }
        catch (IOException ex)
        {
            throw new ExecutorException(
                    "Failed to parse Proto BannerAds Request");
        }
        if (pBannerAds == null)
        {
            throw new ExecutorException("ProtoBannerAdsReq is null");
        }

        request.setPageId(String.valueOf(pBannerAds.getPageId()));
        request.setKeyWord(pBannerAds.getKeyWord());
        request.setSearchId(pBannerAds.getSearchId());
        request.setPageIndex(pBannerAds.getPageIndex());

        Location location = getLocation(pBannerAds.getLat(),
                pBannerAds.getLon());
        request.setLoc(location);
        if (TYPE_CURRENT_LOCATION == pBannerAds.getAddressType())
        {
            request.setCurLoc(location);
        }

        double width = pBannerAds.getWidth();
        double height = pBannerAds.getHeight();
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

        return new ExecutorRequest[] { request };

    }

    private Location getLocation(long lat, long lon)
    {
        Location loc = new Location();
        GeoCode geoCode = new GeoCode();
        geoCode.setLatitude((double) lat / DM5);
        geoCode.setLongitude((double) lon / DM5);
        loc.setGeoCode(geoCode);
        return loc;
    }
}
