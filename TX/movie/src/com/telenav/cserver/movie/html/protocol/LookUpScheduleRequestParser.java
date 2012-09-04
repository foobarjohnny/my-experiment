package com.telenav.cserver.movie.html.protocol;

import javax.servlet.http.HttpServletRequest;

import org.json.me.JSONObject;

import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.html.protocol.HtmlProtocolRequestParser;
import com.telenav.cserver.movie.html.executor.LookUpScheduleRequest;
import com.telenav.cserver.movie.html.util.HtmlConstants.RRKey;

public class LookUpScheduleRequestParser extends HtmlProtocolRequestParser{

    public String getExecutorType() {
        return "lookUpSchedule";
    }

    public ExecutorRequest parseBrowserRequest(HttpServletRequest httpRequest) throws Exception {
        
    	LookUpScheduleRequest request = new LookUpScheduleRequest();
    	// Get the JSON request.
    	String jsonStr = httpRequest.getParameter("jsonStr");
        JSONObject jo = new JSONObject(jsonStr);
        
        //Date
       	String searchDate = jo.getString(RRKey.MS_DATE_INDEX);
       	request.setSearchDate(searchDate);
       	//Address
       	String movieId = jo.getString(RRKey.S_MOVIEID);
       	String theaterId = jo.getString(RRKey.S_THEATERID);
       	long lTheaterId = -1;
       	try
       	{
       		lTheaterId = Long.parseLong(theaterId);
       	}catch(NumberFormatException e)
       	{
       		
       	}
        
       	request.setMovieIds(new String[]{movieId});
       	request.setTheaterIds(new long[]{lTheaterId});
        
    	return request;
    }
}
