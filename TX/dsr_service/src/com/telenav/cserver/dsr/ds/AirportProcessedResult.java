package com.telenav.cserver.dsr.ds;

import com.telenav.cserver.backend.datatypes.TnPoi;


public class AirportProcessedResult extends CommandProcessedResult
{
	private String airportCode;
	private String airportName;
	private TnPoi airport;
	
	public AirportProcessedResult(String literal, double confidence)
	{
		super(literal, confidence) ;
		
	}
	
	public AirportProcessedResult(String literal, double confidence, Command command)
	{
		super(literal, confidence, command) ;
	}
	
	
	public ResultType getResultType()
	{
		return ResultType.TYPE_AIRPORT;
	}

	public String getAirportCode() {
		return airportCode;
	}

	public void setAirportCode(String airportCode) {
		this.airportCode = airportCode;
	}

	public String getAirportName() {
		return airportName;
	}

	public void setAirportName(String airportName) {
		this.airportName = airportName;
	}

	public TnPoi getAirport() {
		return airport;
	}

	public void setAirport(TnPoi airport) {
		this.airport = airport;
	}
	
	public boolean equals(Object o)
	{	
		AirportProcessedResult apr = (AirportProcessedResult) o;
		return super.equals(o)
			 && equals(this.airportCode, apr.airportCode)
			 && equals(this.airportName, apr.airportName);
	}
}
