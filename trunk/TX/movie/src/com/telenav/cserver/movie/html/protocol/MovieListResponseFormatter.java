package com.telenav.cserver.movie.html.protocol;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.telenav.browser.movie.Constant;
import com.telenav.browser.movie.Util;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.html.protocol.HtmlProtocolResponseFormatter;
import com.telenav.cserver.movie.html.datatypes.MovieItem;
import com.telenav.cserver.movie.html.datatypes.TheaterItem;
import com.telenav.cserver.movie.html.executor.MovieListResponse;
import com.telenav.cserver.movie.html.executor.MovieServiceProxy;
import com.telenav.cserver.movie.html.util.HtmlConstants;
import com.telenav.cserver.movie.html.util.HtmlMovieUtil;


public class MovieListResponseFormatter extends HtmlProtocolResponseFormatter{

    public void parseBrowserResponse(HttpServletRequest httpRequest,
            ExecutorResponse executorResponse) throws Exception {
    	MovieListResponse response = (MovieListResponse) executorResponse;

    	if(HtmlConstants.PAGE_TYPE_SIMPLE.equalsIgnoreCase(response.getPageType()))
    	{
    		httpRequest.setAttribute("movieList", response.getMoiveList()); 
    	}
    	else if(HtmlConstants.PAGE_TYPE_SUB.equalsIgnoreCase(response.getPageType()))
    	{
    		List<TheaterItem> theaterList = response.getTheaterList();
    		formatTheater(theaterList,response);
    		httpRequest.setAttribute("theaterList", theaterList); 
    	}
        else if(HtmlConstants.PAGE_TYPE_AJAXSIMPE.equalsIgnoreCase(response.getPageType()))
        {
        	httpRequest.setAttribute("movieList", response.getMoiveList()); 
        	httpRequest.setAttribute("startIndex",response.getStartIndex());
        }
    	else
    	{
	        for(MovieItem movieItem: response.getMoiveList()){
	        	//caculate theater's distance first
	        	formatTheater(movieItem.getTheaterList(),response);
	        }
	        httpRequest.setAttribute("movieList", response.getMoiveList()); 
    	}
    }
    
    private void formatTheater(List<TheaterItem> theaterList,MovieListResponse response)
    {
    	for(TheaterItem theaterItem:theaterList)
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
    }

}
