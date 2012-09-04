package com.telenav.cserver.movie.html.executor;

import org.apache.log4j.Logger;
import com.telenav.cserver.framework.executor.AbstractExecutor;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.movie.html.util.HtmlConstants;

public class MovieListExecutor extends AbstractExecutor{
    
	private Logger logger = Logger.getLogger(MovieListExecutor.class);
    
    public void doExecute(ExecutorRequest req, ExecutorResponse resp,
            ExecutorContext context) throws ExecutorException {

        // Get the request and response
        MovieCommonRequest request = (MovieCommonRequest) req;
        MovieListResponse response = (MovieListResponse)resp;
        response.setPageType(request.getPageType());
        response.setAddress(request.getAddress());
        response.setDistanceUnit(request.getDistanceUnit());
        //search movie list with info of theater and schedule
        if(HtmlConstants.PAGE_TYPE_SIMPLE.equalsIgnoreCase(request.getPageType()))
        {
        	MovieServiceProxy.searchMovies(request, response);
        }
        else if(HtmlConstants.PAGE_TYPE_AJAXSIMPE.equalsIgnoreCase(request.getPageType()))
        {
        	MovieServiceProxy.searchMovies(request, response);
        }
        else if(HtmlConstants.PAGE_TYPE_SUB.equalsIgnoreCase(request.getPageType()))
        {
        	TheaterListResponse theaterListResponse = new TheaterListResponse();
        	MovieServiceProxy.lookupSubTheatreList(request, theaterListResponse);
			response.setTheaterList(theaterListResponse.getTheaterList());
        }
        else
        {
        	MovieServiceProxy.searchMoviesWithTheaterAndSchedule(request, response);
        }
        response.setStatus(ExecutorResponse.STATUS_OK);
    }
}
