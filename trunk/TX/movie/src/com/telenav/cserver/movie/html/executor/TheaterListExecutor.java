package com.telenav.cserver.movie.html.executor;

import org.apache.log4j.Logger;

import com.telenav.cserver.framework.executor.AbstractExecutor;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.movie.html.util.HtmlConstants;

public class TheaterListExecutor extends AbstractExecutor{
    
	private Logger logger = Logger.getLogger(TheaterListExecutor.class);
	
    public void doExecute(ExecutorRequest req, ExecutorResponse resp,
            ExecutorContext context) throws ExecutorException {
        
        // Get the request and response
    	MovieCommonRequest request = (MovieCommonRequest) req;
        TheaterListResponse response = (TheaterListResponse)resp;
        response.setPageType(request.getPageType());
        //response.setAddress(request.getAddress());
        //response.setDistanceUnit(request.getDistanceUnit());
        //search movie list with info of theater and schedule
        if(HtmlConstants.PAGE_TYPE_SIMPLE.equalsIgnoreCase(request.getPageType()))
        {
        	MovieServiceProxy.lookupTheatresWithDetailInfo(request, response);
        }
        else if(HtmlConstants.PAGE_TYPE_AJAXSIMPE.equalsIgnoreCase(request.getPageType()))
        {
        	MovieServiceProxy.lookupTheatresWithDetailInfo(request, response);
        }
        else if(HtmlConstants.PAGE_TYPE_SUB.equalsIgnoreCase(request.getPageType()))
        {
        	MovieListResponse movieListResponse = new MovieListResponse();
        	MovieServiceProxy.lookupSubMovieList(request, movieListResponse);
			response.setMoiveList(movieListResponse.getMoiveList());
        }
        else
        {
        	MovieServiceProxy.searchTheatresWithMovieAndSchedule(request, response);
        }
        response.setStatus(ExecutorResponse.STATUS_OK);
    }
}
