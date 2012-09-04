package com.telenav.cserver.dsr.ds;

import com.telenav.j2me.datatypes.Stop;


public class PoiQueryProcessedResult extends PoiProcessedResult
{
	private Stop searchLocation;
	
	public PoiQueryProcessedResult(String literal, double confidence)
	{
		super(literal, confidence) ;
	}
	
	public PoiQueryProcessedResult(String literal, double confidence, Command command)
	{
		super(literal, confidence, command) ;
	}
	
	public ResultType getResultType()
	{
		return ResultType.TYPE_POI_QUERY;
	}

	public Stop getSearchLocation() {
		return searchLocation;
	}

	public void setSearchLocation(Stop searchLocation) {
		this.searchLocation = searchLocation;
	}
	
}
