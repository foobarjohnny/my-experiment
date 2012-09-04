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
package com.telenav.cserver.onebox.protocol;

import javax.servlet.http.HttpServletRequest;

import org.json.me.JSONObject;

import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.browser.framework.protocol.BrowserProtocolRequestParser;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.onebox.executor.OneBoxRequest;
import com.telenav.cserver.poi.datatypes.Stop;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;

public class OneBoxRequestParser extends BrowserProtocolRequestParser {

	@Override
	public String getExecutorType() {
		return "OneBox";
	}

	@Override
	public ExecutorRequest parseBrowserRequest(HttpServletRequest object)
			throws Exception {
		HttpServletRequest httpRequest = (HttpServletRequest) object;
		OneBoxRequest request = new OneBoxRequest();

		// Get the JSON request.
		DataHandler handler = (DataHandler) httpRequest
				.getAttribute(BrowserFrameworkConstants.CLIENT_INFO);
		TxNode body = handler.getAJAXBody();
		String joString = body.msgAt(0);
		JSONObject jo = new JSONObject(joString);

		// SearchString
		String searchString = jo.optString("inputString");
		request.setSearchString(searchString);

		// Start PageNumber.
		int currentPage = 0;
		String currentPageStr = jo.getString("currentPage");
		if (currentPageStr != null && !"".equals(currentPageStr)) {
			currentPage = Integer.parseInt(jo.getString("currentPage"));
		}
		request.setPageNumber(currentPage);

		// Max results.
		int maxResults = jo.optInt("maxResults", 9);
		request.setMaxResults(maxResults);

		int distanceUnit = jo.getInt("distanceUnit");
		request.setDistanceUnit(distanceUnit);
		
		//Number of sponsor poi
        int sponsorListingNumber = 1;
        if(jo.has("sponsorListingNumber")){
        	sponsorListingNumber = jo.getInt("sponsorListingNumber");
        }
        request.setSponsorListingNumber(sponsorListingNumber);
        
		// Search type.
        int searchType = 5; // search type 5 = SEARCH_ADDRESS
        String searchTypeStr = jo.getString("searchTypeStr");
        if(searchTypeStr!=null && !"".equals(searchTypeStr)){
            searchType = Integer.parseInt(searchTypeStr);
        }
        request.setSearchType(searchType);
        
        String transactionId = "";
        if(jo.has("transactionId")){
        	transactionId = jo.getString("transactionId");
        }
        request.setTransactionId(transactionId);
        
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
		// TxNode gpsNode = (TxNode) handler.getParameter("currentLocation");
		String addressString = jo.getString("addressString");
		// Stop TODO
		Stop stop = new Stop();

		if (addressString != null && !"".equals(addressString)) {
			JSONObject addressJO = new JSONObject(addressString);
			stop.lat = addressJO.getInt("lat");
			stop.lon = addressJO.getInt("lon");
			if (addressJO.has("city")) {
				stop.city = addressJO.getString("city");
			}
			if (addressJO.has("state")) {
				stop.state = addressJO.getString("state");
			}
			if (addressJO.has("zip")) {
				stop.zip = addressJO.getString("zip");
			}
		}

		request.setStop(stop);
		
		//get dest address and do the search for search along destination case
		Stop stopDest = new Stop();
		if(jo.has("addressStringDest"))
		{
			String addressStringDest = jo.getString("addressStringDest");
			if (addressStringDest != null && !"".equals(addressStringDest)) {
				JSONObject addressJODest = new JSONObject(addressStringDest);
				stopDest.lat = addressJODest.getInt("lat");
				stopDest.lon = addressJODest.getInt("lon");
				if (addressJODest.has("city")) {
					stopDest.city = addressJODest.getString("city");
				}
				if (addressJODest.has("state")) {
					stopDest.state = addressJODest.getString("state");
				}
				if (addressJODest.has("zip")) {
					stopDest.zip = addressJODest.getString("zip");
				}
			}
		}
		request.setStopDest(stopDest);
		return request;
	}
}
