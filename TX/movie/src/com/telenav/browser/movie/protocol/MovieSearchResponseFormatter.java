package com.telenav.browser.movie.protocol;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.me.JSONArray;
import org.json.me.JSONObject;

import com.telenav.browser.movie.Constant;
import com.telenav.browser.movie.Util;
import com.telenav.browser.movie.executor.MovieSearchResponse;
import com.telenav.cserver.browser.framework.protocol.BrowserProtocolResponseFormatter;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.datatypes.content.movie.v10.Movie;
import com.telenav.datatypes.content.movie.v10.MovieListingWithDetailTheaterInfo;
import com.telenav.datatypes.content.tnpoi.v10.TnPoi;


public class MovieSearchResponseFormatter extends BrowserProtocolResponseFormatter{
	private static Logger logger = Logger.getLogger(MovieSearchResponseFormatter.class);

    public void parseBrowserResponse(HttpServletRequest httpRequest,
            ExecutorResponse executorResponse) throws Exception {
    	MovieSearchResponse response = (MovieSearchResponse) executorResponse;

    	JSONObject jMovie = null;
        JSONArray joList = new JSONArray();
        if (response.movies != null){
        	Movie movie = null;
	        for (MovieListingWithDetailTheaterInfo movieListing : response.movies) {
	        	movie = movieListing.getMovie();
	        	jMovie = new JSONObject();
	        	jMovie.put(Constant.RRKey.M_ID, movie.getId());
	        	jMovie.put(Constant.RRKey.M_NAME, movie.getName());
	        	String grade = movie.getGrade();
				jMovie.put(Constant.RRKey.M_GRADE, grade!=null?grade:"NR");
				String runTime = Util.timeFormat(movie.getRuntime());
				jMovie.put(Constant.RRKey.M_RUNTIME, runTime);
				String rating = movie.getRating();
				int rI = rating!=null? (int)(Double.parseDouble(rating)*2): 0;
				jMovie.put(Constant.RRKey.M_RATING, rI);
				jMovie.put(Constant.RRKey.M_CAST, Util.makeOneString(movie.getCast(), ", "));
				TnPoi[] tArr = movieListing.getDetailTheaterInfo();
				String distance = " N/A ";
				String tInfoStr = "NA";
				if (tArr != null && tArr.length > 0){
					TnPoi t = tArr[0];
					if (t != null){
				    	tInfoStr = t.getBrandName() ;
						double t_lat = t.getAddress().getGeoCode().getLatitude();
						double t_lon = t.getAddress().getGeoCode().getLongitude();
						int meters = Util.calDistanceInMeter(t_lat, t_lon, response.lat, response.lon);
						if (response.dUnit == Constant.DUNIT_MILES){ //1 - use miles
							distance = Util.distanceInMiles(meters) + " mi";
						}else{
							distance = Util.distanceInKilometers(meters) + " km";
						}
					}
				}
				jMovie.put(Constant.RRKey.M_THEATER_ADDRESS, tInfoStr);
				jMovie.put(Constant.RRKey.M_THEATER_DISTANCE, distance);
				
				joList.put(jMovie);
				logger.debug("movie.........." + jMovie);
			}
	        // TODO remove before production
	        //joList = Util.generateMovieList(joList, response.batchNumber, response.batchSize);
        }

        TxNode node = new TxNode();
        String mString = joList.toString();
        node.addValue(1); // OK code
        node.addMsg(mString);

        httpRequest.setAttribute("node", node);
    }

}
