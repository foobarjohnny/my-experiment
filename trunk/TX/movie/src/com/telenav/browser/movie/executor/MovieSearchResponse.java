package com.telenav.browser.movie.executor;

import java.util.Map;

import com.telenav.browser.movie.Constant;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.datatypes.content.movie.v10.MovieListing;
import com.telenav.datatypes.content.movie.v10.MovieListingWithDetailTheaterInfo;
import com.telenav.datatypes.content.tnpoi.v10.TnPoi;

public class MovieSearchResponse extends ExecutorResponse{
	public MovieListingWithDetailTheaterInfo[] movies;
	public double lat;
	public double lon;
	public int dUnit = Constant.DUNIT_MILES; // default value in miles
	
	public String toString() {
		int moviesSize = movies != null ? movies.length : 0;
		return "[moviesSize] = " + moviesSize + "; [lat] = " + lat
				+ "; [lon] = " + lon + "; [dUnit] = " + dUnit;
	}
}
