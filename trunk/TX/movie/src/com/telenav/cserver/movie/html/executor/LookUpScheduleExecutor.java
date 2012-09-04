package com.telenav.cserver.movie.html.executor;

import org.apache.log4j.Logger;

import com.telenav.cserver.framework.executor.AbstractExecutor;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;


public class LookUpScheduleExecutor extends AbstractExecutor{
    
	private Logger logger = Logger.getLogger(LookUpScheduleExecutor.class);

    public void doExecute(ExecutorRequest req, ExecutorResponse resp,
            ExecutorContext context) throws ExecutorException {

        // Get the request and response
        LookUpScheduleRequest request = (LookUpScheduleRequest) req;
        LookUpScheduleResponse response = (LookUpScheduleResponse)resp;
        //search movie list which theater information
        MovieServiceProxy.lookupSchedules(request, response);
        //search schedule for this movie/theater pair
        response.setStatus(ExecutorResponse.STATUS_OK);
    }
    
}
