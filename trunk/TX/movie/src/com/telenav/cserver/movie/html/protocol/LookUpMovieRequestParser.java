package com.telenav.cserver.movie.html.protocol;

import javax.servlet.http.HttpServletRequest;

import org.json.me.JSONObject;

import com.telenav.browser.movie.Constant;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.html.protocol.HtmlProtocolRequestParser;
import com.telenav.cserver.movie.html.executor.MovieCommonRequest;
import com.telenav.cserver.movie.html.util.HtmlConstants.RRKey;

public class LookUpMovieRequestParser extends HtmlProtocolRequestParser{

    public String getExecutorType() {
        return "lookUpMovie";
    }

    public ExecutorRequest parseBrowserRequest(HttpServletRequest httpRequest) throws Exception {
        
    	MovieCommonRequest request = new MovieCommonRequest();
    	// Get the JSON request.
    	String jsonStr = httpRequest.getParameter("jsonStr");
        JSONObject jo = new JSONObject(jsonStr);
        
        //Date
       	String searchDate = jo.getString(RRKey.MS_DATE_INDEX);
       	request.setSearchDate(searchDate);
       	//Theater Id
    	String sTheaterId = jo.getString(RRKey.MS_THEATER_ID);
    	long theaterId = Long.parseLong(sTheaterId);
       	request.setTheaterId(theaterId);
        //Distance Unit
    	int dUnit = Constant.DUNIT_MILES;
    	request.setDistanceUnit(dUnit);
        
    	return request;
    }
}
