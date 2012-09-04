package com.telenav.cserver.dsr.ds;


public class CommandProcessedResult extends ProcessedResult
{
	private Command command;

	public CommandProcessedResult(String literal, double confidence)
	{
		super(literal, confidence) ;
	}
	
	public CommandProcessedResult(String literal, double confidence, Command command)
	{
		super(literal, confidence) ;
		this.command = command;
	}

	public Command getCommand() {
		return command;
	}

	public void setCommand(Command command) {
		this.command = command;
	}
	
	public ResultType getResultType()
	{
		return ResultType.TYPE_COMMAND;
	}
	
	public boolean equals(Object o)
	{
		CommandProcessedResult cpr = (CommandProcessedResult) o;
		return super.equals(o) && command == cpr.command;
	}
	
//	public String toString()
//	{
//		return super.toString()+","+command;
//	}
}
