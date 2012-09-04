/******************************************************************************
 * Copyright (c) 2006 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on Apr 17, 2009
 * File name: PoiBrowserProtocolRequestParser.java
 * Package name: com.telenav.cserver.poi.protocol
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: lwei(lwei@telenav.cn) 10:10:08 AM
 *  Update:
 *******************************************************************************/
package com.telenav.cserver.poi.protocol.v20;

import javax.servlet.http.HttpServletRequest;

import org.json.me.JSONObject;

import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.browser.framework.protocol.BrowserProtocolRequestParser;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.poi.datatypes.Stop;
import com.telenav.cserver.poi.executor.v20.POISearchRequest_WS;
import com.telenav.cserver.poi.struts.Constant;
import com.telenav.cserver.poi.struts.util.PoiUtil;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;

/**
 * @author lwei (lwei@telenav.cn) 10:10:08 AM
 * copy and update by xfliu at 2011/12/6
 */
public class PoiBrowserProtocolRequestParser extends
        BrowserProtocolRequestParser {
    @Override
    public String getExecutorType() {
        return "poi20";
    }

    @Override
    public ExecutorRequest parseBrowserRequest(HttpServletRequest object)
            throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest) object;
        POISearchRequest_WS request = new POISearchRequest_WS();

        // Get the JSON request.
        DataHandler handler = (DataHandler) httpRequest
                .getAttribute(BrowserFrameworkConstants.CLIENT_INFO);
        String productType = handler.getClientInfo(DataHandler.KEY_PRODUCTTYPE);
        request.setProductType(productType);
        TxNode body = handler.getAJAXBody();
        String joString = body.msgAt(0);
        JSONObject jo = new JSONObject(joString);
        
        String audioFormat = PoiUtil.getString(handler.getClientInfo(DataHandler.KEY_AUDIOFORMAT));
        System.out.println("audioFormat format is:" + audioFormat);
        if("".equals(audioFormat))
        {
            audioFormat = "amr";
        }
        
        request.setAudioFormat(audioFormat);
        // Parse the parameters.
        // Category Id
        // TODO Rain make sure client will send a valid category id in our JSP.
        long categoryId = Constant.DEFAULT_CATEGORY;
        categoryId = Long.parseLong(jo.getString("categoryId"));
        request.setCategoryId(categoryId);

        // SearchString
        String searchString = "";
        searchString = jo.getString("inputString");
        request.setSearchString(searchString);

        // Radius
        // TODO Rain why radius is not in JSON format?
        int radiusInMeter = 0;
//        try {
//            radius = Integer.parseInt(getStringParm(handler, "radius"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        if (radiusInMeter == 0) {
            // TODO Log.
            radiusInMeter = Constant.DEFAULT_RADIUS;
        }
        request.setRadiusInMeter(radiusInMeter);
        
        String from = jo.getString("from");
        boolean needAudio = true;
        if(Constant.StorageKey.POI_MODULE_FROM_TYPE.equals(from)){
            needAudio = false;           
        }
        request.setNeedAudio(needAudio);

        // Search type.
        int searchType = 5; // search type 5 = SEARCH_ADDRESS
        String searchTypeStr = jo.getString("searchTypeStr");
        if(searchTypeStr!=null && !"".equals(searchTypeStr)){
            searchType = Integer.parseInt(searchTypeStr);
        }
        request.setSearchType(searchType);
        
        if(searchType == 7){
            String routeIdStr = jo.getString("routeID");
            int routeId = 0;
            if(routeIdStr!=null && !"".equals(routeIdStr)){
                routeId = Integer.parseInt(routeIdStr);
            }
            request.setRouteID(routeId);
            
            String segmentIdStr = jo.getString("segmentId");
            int segmentId = 0;
            if(segmentIdStr!=null && !"".equals(segmentIdStr)){
                segmentId = Integer.parseInt(segmentIdStr);
            }
            request.setSegmentId(segmentId);
            
            String edgeIdStr = jo.getString("edgeId");
            int edgeId = 0;
            if(edgeIdStr!=null && !"".equals(edgeIdStr)){
                edgeId = Integer.parseInt(edgeIdStr);
            }
            request.setEdgeId(edgeId);
            
            String shapePointIdStr = jo.getString("shapePointId");
            int shapePointId = 0;
            if(shapePointIdStr!=null && !"".equals(shapePointIdStr)){
                shapePointId = Integer.parseInt(shapePointIdStr);
            }
            request.setShapePointId(shapePointId);
            
            String rangeStr = jo.getString("range");
            int range = 0;
            if(rangeStr!=null && !"".equals(rangeStr)){
                range = Integer.parseInt(rangeStr);
            }
            request.setRange(range);
            
            String currentLatStr = jo.getString("currentLat");
            int currentLat = 0;
            if(currentLatStr!=null && !"".equals(currentLatStr)){
                currentLat = Integer.parseInt(currentLatStr);
            }
            request.setCurrentLat(currentLat);
            
            String currentLonStr = jo.getString("currentLon");
            int currentLon = 0;
            if(currentLonStr!=null && !"".equals(currentLonStr)){
                currentLon = Integer.parseInt(currentLonStr);
            }
            request.setCurrentLon(currentLon);
            
            int searchAlongType = jo.getInt("searchAlongType");
            request.setSearchAlongType(searchAlongType);
        }
        
//      POI_TYPEIN = 1;POI_SPEAKIN = 2;POI_TYPEIN_ALONG = 3;POI_SPEAKIN_ALONG = 4;
        int searchFromType = 1;
        String searchFromTypeStr = jo.getString("searchFromType");
        if(searchFromTypeStr!=null && !"".equals(searchFromTypeStr)){
            searchFromType = Integer.parseInt(searchFromTypeStr);
        }
        request.setSearchFromType(searchFromType);

        // Sort type
        int sortType = Constant.SORT_BY_RELEVANCE;
        String sortTypeStr = jo.getString("sortType");
        if(sortTypeStr!=null && !"".equals(sortTypeStr)){
            sortType = Integer.parseInt(sortTypeStr);
        }
        request.setSortType(sortType);

        // Start PageNumber.
        int currentPage = 0;
        String currentPageStr = jo.getString("currentPage");
        if(currentPageStr!=null && !"".equals(currentPageStr)){
            currentPage = Integer.parseInt(jo.getString("currentPage"));
        }
        request.setPageNumber(currentPage);

        // Max results.
        int maxResults = 9;
        try {
            maxResults = Integer.parseInt(jo.getString("maxResults"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        request.setMaxResults(maxResults);

        // To request POI from COSE, Three parameters are required, page number, page size and maxResult
        // So we need add an individual parameter for page size
        // While it is impossible to add new parameter in client request since we have release many clients to market
        // After investigation, page size is equal to 9 for 6.x product now.
        // If need, we can add new parameter in client request to set page size later.
        request.setPageSize(Math.max(request.getMaxResults(), Constant.PAGE_SIZE));  
        
        // Most popular TODO
        boolean mostPopular = false;
        String isMostPopular = jo.getString("isMostPopular");
        if("1".equals(isMostPopular)){
            mostPopular = true;
        }
        request.setMostPopular(mostPopular);
        
        int distanceUnit = jo.getInt("distanceUnit");
        request.setDistanceUnit(distanceUnit);
        
        //Number of sponsor poi
        int sponsorListingNumber = 1;
        if(jo.has("sponsorListingNumber")){
        	sponsorListingNumber = jo.getInt("sponsorListingNumber");
        }
        request.setSponsorListingNumber(sponsorListingNumber);

//        TxNode gpsNode = (TxNode) handler.getParameter("currentLocation");
        String addressString = jo.getString("addressString");
        // Stop TODO
        Stop stop = new Stop();
        stop.lat = 3737391;
        stop.lon = -12199926;

        if (addressString != null && !"".equals(addressString)) {
            JSONObject addressJO =  new JSONObject(addressString);
            stop.lat = addressJO.getInt("lat");
            stop.lon = addressJO.getInt("lon");
            if(addressJO.has("city")){
               stop.city = addressJO.getString("city");
            }
            if(addressJO.has("state")){
               stop.state = addressJO.getString("state");
            }
            if(addressJO.has("zip")){
               stop.zip = addressJO.getString("zip");
            }
            
            if(addressJO.has("type")){
               stop.type = addressJO.getString("type");
            }
        }
        
        String transactionId = "";
        if(jo.has("transactionId")){
        	transactionId = jo.getString("transactionId");
        }
        request.setTransactionID(transactionId);

        request.setStop(stop);
        return request;
    }
}
