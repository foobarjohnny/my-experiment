package com.telenav.cserver.dsr.ds;


public abstract class ProcessedResult {
	
	private String literal = "";
	private double confidence;
	
	public ProcessedResult(String literal, double confidence)
	{
		this.literal = literal;
		this.confidence = confidence;
	}

	public abstract ResultType getResultType() ;

	public String getLiteral() {
		return literal;
	}

	public double getConfidence() {
		return confidence;
	}
	
	public boolean equals(Object o)
	{
		ProcessedResult r = (ProcessedResult) o;
		
		return this.literal.equals(r.literal)
			&& this.getResultType() == r.getResultType();
	}
	
	static boolean equals(String s1, String s2)
	{
		if (s1 != null)
			return s1.equals(s2);
		else if (s2 != null)
			return s2.equals(s1);
		else
			return true; // they are both null
	}
	
	public String toString()
	{
		return literal;
	}
}

