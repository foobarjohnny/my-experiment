package com.telenav.cserver.movie.html.protocol;

import javax.servlet.http.HttpServletRequest;

import org.json.me.JSONObject;

import com.telenav.browser.movie.Constant;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.html.protocol.HtmlProtocolRequestParser;
import com.telenav.cserver.framework.html.util.HtmlCommonUtil;
import com.telenav.cserver.movie.html.executor.MovieListRequest;
import com.telenav.cserver.movie.html.util.HtmlConstants;
import com.telenav.cserver.movie.html.util.HtmlMovieUtil;
import com.telenav.cserver.movie.html.util.HtmlConstants.RRKey;

public class MovieListRequestParser extends HtmlProtocolRequestParser{

    public String getExecutorType() {
        return "movieList";
    }

    public ExecutorRequest parseBrowserRequest(HttpServletRequest httpRequest) throws Exception {
        
    	MovieListRequest request = new MovieListRequest();
    	// Get the JSON request.
    	String jsonStr = httpRequest.getParameter("jsonStr");
        JSONObject jo = new JSONObject(jsonStr);
        
        //Date
       	String searchDate = jo.getString(RRKey.MS_DATE_INDEX);
       	request.setSearchDate(searchDate);
       	//Address
        String addressStr = jo.getString(RRKey.MS_ADDRESS);
        JSONObject addressJ = new JSONObject(addressStr);
        request.setAddress(HtmlMovieUtil.convertJsonToStop(addressJ));
        //Distance Unit
        String distanceUnit = jo.getString(RRKey.MS_DISTANCE_UNIT);
    	int dUnit = Constant.DUNIT_MILES;
    	try
    	{
    		dUnit = Integer.parseInt(distanceUnit);
    	}
    	catch(NumberFormatException e)
    	{
    		
    	}
    	request.setDistanceUnit(dUnit);
        
    	
    	String pageType = HtmlCommonUtil.getString(httpRequest.getParameter("pageType"));
    	request.setPageType(pageType);
    	
    	if(!HtmlConstants.PAGE_TYPE_SUB.equals(pageType)){
    		//set page size
    		request.setBatchSize(HtmlConstants.MOVIE_SMALL_PAGE_SIZE);
    		
        	//pageNo
        	int batchNo = jo.getInt(RRKey.MS_BATCH_NUMBER);
        	request.setBatchNumber(batchNo);
        	
        	//startIndex
        	int startIndex = jo.getInt(RRKey.MS_START_INDEX);
        	request.setStartIndex(startIndex);
        	
    	}else{
    		
    	}

    	String movieId = HtmlCommonUtil.getString(httpRequest.getParameter("movieId"));
        request.setMovieId(movieId);

    	return request;
    }
}
