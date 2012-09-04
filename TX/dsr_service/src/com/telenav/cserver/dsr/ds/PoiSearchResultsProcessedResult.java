package com.telenav.cserver.dsr.ds;

import java.util.List;

import com.telenav.cserver.backend.datatypes.TnPoi;
import com.telenav.j2me.datatypes.Stop;


public class PoiSearchResultsProcessedResult extends PoiProcessedResult
{
	private boolean searchNearBy;
	private Stop anchorPoint;
	private List<TnPoi> searchResults;
	
	public PoiSearchResultsProcessedResult(String literal, double confidence)
	{
		super(literal, confidence) ;
	}
	
	public PoiSearchResultsProcessedResult(String literal, double confidence, Command command)
	{
		super(literal, confidence, command) ;
	}
	
	public ResultType getResultType()
	{
		return ResultType.TYPE_POI_SEARCH_RESULTS;
	}

	public List<TnPoi> getSearchResults() {
		return searchResults;
	}

	public void setSearchResults(List<TnPoi> searchResults) {
		this.searchResults = searchResults;
	}

	public boolean isSearchNearBy() {
		return searchNearBy;
	}

	public void setSearchNearBy(boolean searchNearBy) {
		this.searchNearBy = searchNearBy;
	}

	public Stop getAnchorPoint() {
		return anchorPoint;
	}

	public void setAnchorPoint(Stop anchorPoint) {
		this.anchorPoint = anchorPoint;
	}
		
}
