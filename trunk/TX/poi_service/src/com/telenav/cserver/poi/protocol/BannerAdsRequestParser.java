/*
 * Copyright (c) 2009 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 */
package com.telenav.cserver.poi.protocol;

import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.browser.framework.protocol.BrowserProtocolRequestParser;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.poi.executor.BannerAdsRequest;
import com.telenav.datatypes.content.ads.v10.ImageSizeType;
import com.telenav.tnbrowser.util.DataHandler;
import com.telenav.ws.datatypes.address.GeoCode;
import com.telenav.ws.datatypes.address.Location;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import javax.servlet.http.HttpServletRequest;

/**
 * @author weiw
 * @date Mar 25, 2010
 *
 */

public class BannerAdsRequestParser extends BrowserProtocolRequestParser {

    /** Unit for GPS conversion. */
    private static final double DM5 = 100000;
    
    /** current location type*/
    private static final int CURRENT_LOCATION_TYPE=6;

    /**
     * executor type.
     *
     * @return executor type
     */
    @Override
    public final String getExecutorType() {
        return "bannerAds";
    }

    /**
     * parse browser request to banner ads request.
     *
     * @param object
     * @return bannerRequest
     * @throws Exception
     */
    @Override
    public final ExecutorRequest parseBrowserRequest(
            final HttpServletRequest object) throws Exception {
        final HttpServletRequest httpRequest = (HttpServletRequest) object;

        final JSONObject bannerJo = POIRequetUtil.getJo(httpRequest);

        final String pageId = bannerJo.getString("pageId");
        final int pageIndex = bannerJo.getInt("pageIndex");

        final String keyWord = bannerJo.getString("keyWord");
        final long catId = bannerJo.getLong("catId");
        final String searchId = bannerJo.getString("searchUID");
        BannerAdsRequest request = new BannerAdsRequest();
        final JSONObject location = bannerJo.optJSONObject("searchLocation");

        if(location!=null){
	        Location loc = getLocation(location);
	        request.setLoc(loc);
	        int type = location.optInt("type");
	        if(type==CURRENT_LOCATION_TYPE){
	        	request.setCurLoc(loc);
	        }
        }

        DataHandler handler = (DataHandler) httpRequest
                .getAttribute(BrowserFrameworkConstants.CLIENT_INFO);
        int width = Integer.valueOf(handler
                .getClientInfo(DataHandler.KEY_WIDTH));
        int height = Integer.valueOf(handler
                .getClientInfo(DataHandler.KEY_HEIGHT));

        // FIXME: what is the real configuration?
        final int preferredWidth = (int) (width * 0.95);
        int preferredHeight = (int) (height / 4);

        ImageSizeType maxSize = new ImageSizeType();
        maxSize.setHeight(preferredHeight);
        maxSize.setWidth(preferredWidth);
        request.setMaxSize(maxSize);

        ImageSizeType minSize = new ImageSizeType();
        minSize.setWidth(width / 2);
        minSize.setHeight((int) (height * 0.04));

        request.setMinSize(minSize);

        request.setPublicIP(getRemoteAddress(httpRequest));

        request.setCategory(catId);
        request.setKeyWord(keyWord);
        request.setSearchId(searchId);
        request.setPageId(pageId);
        request.setPageIndex(pageIndex);

        return request;
    }

    /**
     * @return Returns the remote address.
     */
    public String getRemoteAddress(HttpServletRequest httpRequest)
    {
      String ip = httpRequest.getHeader("x-forwarded-for");
      if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
      {
            ip = httpRequest.getRemoteAddr();
      }
      return ip;
    }

    /**
     * @param location
     * @return
     * @throws JSONException
     */
    private Location getLocation(final JSONObject location)
            throws JSONException {
        Location loc = new Location();
        GeoCode geoCode = new GeoCode();
        geoCode.setLatitude(location.getInt("lat") / DM5);
        geoCode.setLongitude(location.getInt("lon") / DM5);
        loc.setGeoCode(geoCode);
        return loc;
    }

}
