package com.telenav.cserver.movie.html.protocol;

import javax.servlet.http.HttpServletRequest;
import org.json.me.JSONArray;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.html.protocol.HtmlProtocolResponseFormatter;
import com.telenav.cserver.movie.html.datatypes.MovieItem;
import com.telenav.cserver.movie.html.executor.MovieListResponse;


public class LookUpMovieResponseFormatter extends HtmlProtocolResponseFormatter{

    public void parseBrowserResponse(HttpServletRequest httpRequest,
            ExecutorResponse executorResponse) throws Exception {
    	MovieListResponse response = (MovieListResponse) executorResponse;

        JSONArray joList = new JSONArray();
        for(MovieItem movieItem: response.getMoiveList()){
        	joList.put(MovieDataBuilder.getInstance().toJsonWithBasicInfoAndSchedule(movieItem));
        }
        
        httpRequest.setAttribute("ajaxResponse", joList.toString()); 
    }

}
