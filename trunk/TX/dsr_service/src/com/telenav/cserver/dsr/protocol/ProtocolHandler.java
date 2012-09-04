/**
 * 
 */
package com.telenav.cserver.dsr.protocol;

import com.telenav.cserver.dsr.ds.RecContext;
import com.telenav.cserver.dsr.framework.ProcessObject;

/**
 * @author joses
 *
 * 
 */
public interface ProtocolHandler {

public static final byte NOT_DEFINED = -1 ;
	
	public RecContext parseRequest(byte[] metaData);
	
	//format response methods
	public byte[] formatResponse(ProcessObject procObj);

	public byte[] formatError(int statusCode, String message, int recType, long transactionId);
	
	public byte[] formatError(int statusCode, String message, long transactionId);
}
