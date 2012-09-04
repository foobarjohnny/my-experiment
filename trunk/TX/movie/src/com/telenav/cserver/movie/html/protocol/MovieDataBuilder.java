package com.telenav.cserver.movie.html.protocol;

import org.apache.log4j.Logger;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.telenav.browser.movie.Util;
import com.telenav.cserver.movie.html.datatypes.MovieItem;
import com.telenav.cserver.movie.html.datatypes.ScheduleItem;
import com.telenav.cserver.movie.html.datatypes.TheaterItem;
import com.telenav.cserver.movie.html.util.HtmlConstants;
import com.telenav.cserver.movie.html.util.HtmlMovieUtil;
import com.telenav.cserver.movie.html.util.HtmlConstants.RRKey;

public class MovieDataBuilder {
	
	private static Logger logger = Logger.getLogger(MovieDataBuilder.class);

	private static MovieDataBuilder instance = new MovieDataBuilder();
	
	public static MovieDataBuilder getInstance()
	{
		return instance;
	}
	
	public JSONObject toJson(MovieItem item)
	{
		//get basic movie information
		JSONObject jMovie =  toJsonWithBasicInfo(item);
		try {
			
			//get theater List information with schedule
			JSONArray joList = new JSONArray();
			for(TheaterItem theaterItem: item.getTheaterList())
			{
				joList.put(toJsonWithBasicInfoAndSchedule(theaterItem));
			}
			jMovie.put(HtmlConstants.RRKey.M_THEATER_LIST, joList.toString());
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			logger.error("error occured during put theaterList into key "+ HtmlConstants.RRKey.M_THEATER_LIST);
			logger.error("cause:"+e.getCause()+",message:"+e.getMessage());
		}

		return jMovie;
	}
	
	
	public JSONObject toJsonWithBasicInfo(MovieItem item)
	{
		JSONObject jMovie = new JSONObject();
		
    	try {
			jMovie.put(HtmlConstants.RRKey.M_ID, item.getId());
	    	jMovie.put(HtmlConstants.RRKey.M_NAME, item.getName());
	    	String grade = item.getGrade();
			jMovie.put(HtmlConstants.RRKey.M_GRADE, grade!=null?grade:"NR");
			String runTime =item.getRuntime();
			jMovie.put(HtmlConstants.RRKey.M_RUNTIME, runTime);
			double rating = item.getRating();
			int rI = (int)(rating*2);
			jMovie.put(HtmlConstants.RRKey.M_RATING, rI);
			jMovie.put(HtmlConstants.RRKey.M_CAST, Util.makeOneString(item.getCast(), ", "));
			jMovie.put(HtmlConstants.RRKey.M_DIRECTOR, item.getDirector());
			jMovie.put(HtmlConstants.RRKey.M_DESCRIPTION, item.getDescription());
			jMovie.put(HtmlConstants.RRKey.M_VENDOR_ID, item.getVendorId());
			jMovie.put(HtmlConstants.RRKey.M_GENRES, item.getGenres());
			jMovie.put(HtmlConstants.RRKey.M_THEATER_ID, item.getTheaterId());
			
	
		} catch (JSONException e) {
			logger.error("cause:"+e.getCause()+",message:"+e.getMessage());
		}

		return jMovie;
	}
	
	public JSONObject toJsonWithBasicInfoAndSchedule(MovieItem item)
	{
		JSONObject jMovie = toJsonWithBasicInfo(item);
		try {
			if(null != item.getScheduleItem()){
				jMovie.put(RRKey.M_SCHEDULE_ITEM, toJson(item.getScheduleItem()));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			logger.error("error occured during put ScheduleItem into key "+ RRKey.M_SCHEDULE_ITEM);
			logger.error("cause:"+e.getCause()+",message:"+e.getMessage());
		}

		return jMovie;
	}
	
	public JSONObject toJsonWithBasicInfo(TheaterItem item)
	{
		JSONObject jo = new JSONObject();
		
    	try {
    		jo.put(RRKey.T_ID, item.getId());
        	jo.put(RRKey.T_NAME, item.getName());
    		jo.put(RRKey.T_ADDRESS, HtmlMovieUtil.convertStopToJson(item.getAddress()));
    		jo.put(RRKey.T_DISTANCE, item.getDistance());
    		jo.put(RRKey.T_DISTANCE_UNIT, item.getDistanceUnit());
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			logger.error("cause:"+e.getCause()+",message:"+e.getMessage());
		}

		return jo;
	}
	
	public JSONObject toJsonWithBasicInfoAndSchedule(TheaterItem item)
	{
		JSONObject jo = toJsonWithBasicInfo(item);
		try {
			jo.put(RRKey.T_SCHEDULE_ITEM, toJson(item.getScheduleItem()));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			logger.error("error occured during put ScheduleItem into key "+ RRKey.T_SCHEDULE_ITEM);
			logger.error("cause:"+e.getCause()+",message:"+e.getMessage());
		}

		return jo;
	}
	
	public JSONObject toJson(TheaterItem item)
	{
		JSONObject jo = toJsonWithBasicInfo(item);
		try {
			
			//get theater List information with schedule
			JSONArray joList = new JSONArray();
			for(MovieItem movieItem: item.getMovieList())
			{
				joList.put(toJsonWithBasicInfoAndSchedule(movieItem));
			}
			jo.put(HtmlConstants.RRKey.T_MOVIE_LIST, joList.toString());
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			logger.error("error occured during put MovieList into key "+ HtmlConstants.RRKey.T_MOVIE_LIST);
			logger.error("cause:"+e.getCause()+",message:"+e.getMessage());
		}
		return jo;
	}
	
	public JSONObject toJson(ScheduleItem item)
	{
		JSONObject jo = new JSONObject();
		
    	try {
    		jo.put(RRKey.S_ID, item.getId());
        	jo.put(RRKey.S_MOVIEID, item.getMovieId());
    		jo.put(RRKey.S_THEATERID, item.getTheaterId());
    		jo.put(RRKey.S_VENDORNAME, item.getVendorName());
    		jo.put(RRKey.S_TICKETURL, item.getTicketURI());
    		jo.put(RRKey.S_SHOWTIMES, item.formatShowTimes());
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			logger.error("cause:"+e.getCause()+",message:"+e.getMessage());
		}

		return jo;
	}
}
