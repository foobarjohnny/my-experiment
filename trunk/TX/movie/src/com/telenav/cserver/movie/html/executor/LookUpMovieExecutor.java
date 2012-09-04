package com.telenav.cserver.movie.html.executor;


import org.apache.log4j.Logger;

import com.telenav.cserver.framework.executor.AbstractExecutor;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;


public class LookUpMovieExecutor extends AbstractExecutor{
    
	private Logger logger = Logger.getLogger(MovieListExecutor.class);
    
    public void doExecute(ExecutorRequest req, ExecutorResponse resp,
            ExecutorContext context) throws ExecutorException {

        // Get the request and response
    	MovieCommonRequest request = (MovieCommonRequest) req;
        MovieListResponse response = (MovieListResponse)resp;
        response.setAddress(request.getAddress());
        //search movie list with info of theater and schedule
        MovieServiceProxy.lookupSubMovieList(request, response);
        response.setStatus(ExecutorResponse.STATUS_OK);
    }
}
