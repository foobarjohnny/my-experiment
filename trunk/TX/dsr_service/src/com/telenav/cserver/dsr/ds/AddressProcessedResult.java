package com.telenav.cserver.dsr.ds;

import com.telenav.j2me.datatypes.Stop;



public class AddressProcessedResult extends CommandProcessedResult
{
	private String address;
	private String city;
	private String state;
	private Stop stop;
	
	public AddressProcessedResult(String literal, double confidence)
	{
		super(literal, confidence) ;
	}
	
	public AddressProcessedResult(String literal, double confidence, Command command)
	{
		super(literal, confidence, command) ;
	}
	
	public ResultType getResultType()
	{
		return ResultType.TYPE_ADDRESS;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public Stop getStop() {
		return stop;
	}

	public void setStop(Stop stop) {
		this.stop = stop;
	}
	
	public boolean equals(Object o)
	{
		
		AddressProcessedResult apr = (AddressProcessedResult) o;
		return super.equals(o)
			 && equals(this.address, apr.address)
			 && equals(this.city, apr.city)
			 && equals(this.state, apr.state);
	}
}
