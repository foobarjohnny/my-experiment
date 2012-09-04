package com.telenav.cserver.movie.html.executor;

import java.util.ArrayList;
import java.util.List;

import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.movie.html.datatypes.MovieItem;
import com.telenav.cserver.movie.html.datatypes.TheaterItem;
import com.telenav.j2me.datatypes.Stop;

public class MovieCommonResponse extends ExecutorResponse{
	private Stop address;
	private int distanceUnit;
	private String pageType;
	private List<TheaterItem> theaterList = new ArrayList<TheaterItem>();
	private List<MovieItem> moiveList = new ArrayList<MovieItem>();

	private int startIndex = 0;
	
	public Stop getAddress() {
		return address;
	}

	public void setAddress(Stop address) {
		this.address = address;
	}

	public int getDistanceUnit() {
		return distanceUnit;
	}

	public void setDistanceUnit(int distanceUnit) {
		this.distanceUnit = distanceUnit;
	}

	public String getPageType() {
		return pageType;
	}

	public void setPageType(String pageType) {
		this.pageType = pageType;
	}

	public List<TheaterItem> getTheaterList() {
		return theaterList;
	}

	public void setTheaterList(List<TheaterItem> theaterList) {
		this.theaterList = theaterList;
	}

	public List<MovieItem> getMoiveList() {
		return moiveList;
	}

	public void setMoiveList(List<MovieItem> moiveList) {
		this.moiveList = moiveList;
	}
	
	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
}
