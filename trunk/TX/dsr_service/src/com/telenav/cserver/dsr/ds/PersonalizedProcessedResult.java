package com.telenav.cserver.dsr.ds;

import com.telenav.j2me.datatypes.Stop;


public class PersonalizedProcessedResult extends CommandProcessedResult
{
	private PersonalizedType type;
	private int id;
	private String label;
	private boolean useAddress;
	private Stop location;
	
	public PersonalizedProcessedResult(String literal, double confidence)
	{
		super(literal, confidence) ;
	}
	
	public PersonalizedProcessedResult(String literal, double confidence, Command command)
	{
		super(literal, confidence, command) ;
	}
	
	public ResultType getResultType()
	{
		return ResultType.TYPE_PERSONALIZED;
	}

	public PersonalizedType getType() {
		return type;
	}

	public void setType(PersonalizedType type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Stop getLocation() {
		return location;
	}

	public void setLocation(Stop location) {
		this.location = location;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean useAddress()
	{
		return useAddress;
	}

	public void setUseAddress(boolean useAddress)
	{
		this.useAddress = useAddress;
	}
	
	public boolean equals(Object o)
	{	
		PersonalizedProcessedResult ppr = (PersonalizedProcessedResult) o;
		return super.equals(o)
			 && this.type == ppr.type
			 && this.id == ppr.id
			 && this.useAddress == ppr.useAddress;
	}
}
