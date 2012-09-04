package com.telenav.cserver.misc.protocol.protobuf;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.protocol.ProtocolRequestParser;
import com.telenav.cserver.misc.executor.SentAddressRequest;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoSentAddressReq;
import com.telenav.j2me.framework.util.ToStringUtils;

public class SentAddressProtoRequestParser implements ProtocolRequestParser {

	private static final Logger logger = Logger.getLogger(SentAddressProtoRequestParser.class);
	@Override
	public ExecutorRequest[] parse(Object object) throws ExecutorException 
	{
	    SentAddressRequest request = new SentAddressRequest();
		ProtocolBuffer protoBuffer = (ProtocolBuffer) object;
		ProtoSentAddressReq pSentAddressReq = null;
		try
		{
		    pSentAddressReq = ProtoSentAddressReq.parseFrom(protoBuffer.getBufferData());
		    if( logger.isDebugEnabled() )
		    {
		    	logger.debug(ToStringUtils.toString(pSentAddressReq));
		    }
		}
		catch(IOException ex)
		{
			throw new ExecutorException("Failed to parse Proto SentAddress Request");
		}
		if(pSentAddressReq == null)
		{
			throw new ExecutorException("SentAddressReq is null");
		}
		
		request.setAction(pSentAddressReq.getAction());
		request.setId(pSentAddressReq.getId());
		
		return new ExecutorRequest[]{request};
	}
	
	
}
