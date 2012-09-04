package com.telenav.cserver.movie.html.protocol;

import javax.servlet.http.HttpServletRequest;

import org.json.me.JSONObject;

import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.html.protocol.HtmlProtocolResponseFormatter;
import com.telenav.cserver.movie.html.datatypes.ScheduleItem;
import com.telenav.cserver.movie.html.executor.LookUpScheduleResponse;


public class LookUpScheduleResponseFormatter extends HtmlProtocolResponseFormatter{

    public void parseBrowserResponse(HttpServletRequest httpRequest,
            ExecutorResponse executorResponse) throws Exception {
    	LookUpScheduleResponse response = (LookUpScheduleResponse) executorResponse;

    	ScheduleItem ScheduleItem;
    	if(response.getScheduleList() != null && response.getScheduleList().size() >0)
    	{
    		ScheduleItem = response.getScheduleList().get(0);
    	}
    	else
    	{
    		ScheduleItem = new ScheduleItem();
    	}

    	JSONObject jo = MovieDataBuilder.getInstance().toJson(ScheduleItem);
    	
        httpRequest.setAttribute("ajaxResponse", jo.toString()); 
    }

}
