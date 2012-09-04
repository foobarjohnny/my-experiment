package com.telenav.cserver.dsr.framework;

import java.util.List;

import com.telenav.cserver.dsr.handler.IResultsHandler;

public class ProcessConfiguration
{
	private RecognizerProxy proxy ;
	private List<IResultsHandler> handlers;
	
	public ProcessConfiguration(RecognizerProxy proxy, List<IResultsHandler> handlers){
		this.proxy = proxy;
		this.handlers = handlers;
	}
	
	public RecognizerProxy getProxy()
	{
		return proxy;
	}
	
	public List<IResultsHandler> getHandlers()
	{
		return handlers;
	}
}
