package com.telenav.cserver.dsr.framework;

import java.util.List;

import com.telenav.cserver.dsr.ds.ProcessedResult;
import com.telenav.cserver.dsr.ds.RecContext;

public interface ResponseFormatter
{
	//public byte[] formatResponse(List<ProcessedResult> results, Request request, long transactionId) ;
	
	public byte[] formatError(int statusCode, String message, Request request, long transactionId) ;

	/**
	 * @param processedResults
	 * @param request
	 * @param transactionId
	 * @return
	 */
	public byte[] formatResponse(ProcessObject procObj);
}
