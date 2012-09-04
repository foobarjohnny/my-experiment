package com.telenav.cserver.movie.html.executor;

import com.telenav.browser.movie.Constant;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.movie.html.util.HtmlConstants;
import com.telenav.j2me.datatypes.Stop;

public class MovieCommonRequest extends ExecutorRequest{
	private Stop address;
	private String searchDate;
	private String pageType;
	private String movieId;
	private long theaterId;
	private int batchNumber = 1;
	private int batchSize = HtmlConstants.MOVIE_PAGESIZE;
	private int distanceUnit=Constant.DUNIT_MILES;

	private int startIndex = 0;
	
	public Stop getAddress() {
		return address;
	}
	public void setAddress(Stop address) {
		this.address = address;
	}
	public String getSearchDate() {
		return searchDate;
	}
	public void setSearchDate(String searchDate) {
		this.searchDate = searchDate;
	}
	public int getBatchNumber() {
		return batchNumber;
	}
	public void setBatchNumber(int batchNumber) {
		this.batchNumber = batchNumber;
	}
	public int getBatchSize() {
		return batchSize;
	}
	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
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
	public String getMovieId() {
		return movieId;
	}
	public void setMovieId(String movieId) {
		this.movieId = movieId;
	}
	public long getTheaterId() {
		return theaterId;
	}
	public void setTheaterId(long theaterId) {
		this.theaterId = theaterId;
	}
	public int getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
}
