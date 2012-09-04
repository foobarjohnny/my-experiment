package com.telenav.cserver.dsr.framework;

import com.telenav.cserver.framework.executor.ExecutorResponse;

public class DSRExecutorResponse extends ExecutorResponse
{
	private ProcessObject processObject;

	public ProcessObject getProcessObject()
	{
		return processObject;
	}

	public void setProcessObject(ProcessObject processObject)
	{
		this.processObject = processObject;
	}
	
}
