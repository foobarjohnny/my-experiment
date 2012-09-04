package com.telenav.cserver.dsr.ds;


public abstract class PoiProcessedResult extends CommandProcessedResult
{
	private String name;
	private String city;
	private String state;
	
	public PoiProcessedResult(String literal, double confidence)
	{
		super(literal, confidence) ;
	}
	
	public PoiProcessedResult(String literal, double confidence, Command command)
	{
		super(literal, confidence, command) ;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
//	public boolean equals(Object o)
//	{
//		PoiProcessedResult ppr = (PoiProcessedResult) o;
//		return super.equals(o)
//			 && equals(this.name, ppr.name)
//			 && equals(this.city, ppr.city)
//			 && equals(this.state, ppr.state);
//	}
	
	public String toString()
	{
		return super.toString()+","+name+","+city+","+state;
	}
}
