package com.telenav.cserver.movie.html.protocol;

import javax.servlet.http.HttpServletRequest;

import com.telenav.browser.movie.Constant;
import com.telenav.browser.movie.Util;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.html.protocol.HtmlProtocolResponseFormatter;
import com.telenav.cserver.movie.html.datatypes.TheaterItem;
import com.telenav.cserver.movie.html.executor.TheaterListResponse;
import com.telenav.cserver.movie.html.util.HtmlConstants;
import com.telenav.cserver.movie.html.util.HtmlMovieUtil;


public class TheaterListResponseFormatter extends HtmlProtocolResponseFormatter{

    public void parseBrowserResponse(HttpServletRequest httpRequest,
            ExecutorResponse executorResponse) throws Exception {
    	TheaterListResponse response = (TheaterListResponse) executorResponse;
    	
    	if(HtmlConstants.PAGE_TYPE_SUB.equalsIgnoreCase(response.getPageType()))
    	{
    		httpRequest.setAttribute("movieList", response.getMoiveList()); 
    	}
    	else
    	{
	        for(TheaterItem theaterItem: response.getTheaterList())
	        {
	        	String distance = "";
				String distanceUnit = "";
				int meters = HtmlMovieUtil.calDistanceInMeter(theaterItem.getAddress(), response.getAddress());
				if (response.getDistanceUnit() == Constant.DUNIT_MILES){ //1 - use miles
					distance = Util.distanceInMiles(meters);
					distanceUnit = "mi";
				}else{
					distance = Util.distanceInKilometers(meters);
					distanceUnit = "km";
				}
				theaterItem.setDistance(distance);
				theaterItem.setDistanceUnit(distanceUnit);
				theaterItem.setAddressDisplay(HtmlMovieUtil.getAddressDisplay(theaterItem.getAddress()));
	        }
	        httpRequest.setAttribute("theaterList", response.getTheaterList());
        	httpRequest.setAttribute("startIndex",response.getStartIndex());
    	}
    }
    

}
