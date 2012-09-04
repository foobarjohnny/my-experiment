package com.telenav.cserver.dsr.ds;


public class ListNumberProcessedResult extends ProcessedResult
{
	private String number;

	public ListNumberProcessedResult(String literal, double confidence)
	{
		super(literal, confidence) ;
	}
	
	public ResultType getResultType()
	{
		return ResultType.TYPE_LIST_NUMBER;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	
	public boolean equals(Object o)
	{	
		ListNumberProcessedResult apr = (ListNumberProcessedResult) o;
		return super.equals(o)
			 && equals(this.number, apr.number);
	}
}
