package com.telenav.cserver.dsr.protocol;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.telenav.cserver.dsr.ds.RecContext;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.protocol.ProtocolRequestParser;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoDSRReq;

public class DsrRequestParser implements ProtocolRequestParser, ProtocolConstants
{
	private static Logger logger = Logger.getLogger(DsrRequestParser.class.getName());

	@Override
	public ExecutorRequest[] parse(Object object) throws ExecutorException
	{
		ProtocolBuffer buffer = (ProtocolBuffer)object;
		
		try
		{
			ProtoDSRReq request = ProtoDSRReq.parseFrom(buffer.getBufferData());
			
			RecContext context = new RecContext((byte) request.getRecType(),
									(byte) request.getAudioFormat(),
									null,
									null, // this will be set from csvr container
									null, // this will be set from csvr container
									null);
			
			ExecutorRequest er = new ExecutorRequest();
			er.setAttribute(ProtocolConstants.REC_CONTEXT, context);
			er.setAttribute(SIGNAL, request.getSignal().getSignal());
			return new ExecutorRequest[]{er};
			
		}
		catch (IOException e)
		{
			logger.log(Level.WARNING,"Unable to parse protobuf request.",e);
			return null;
		}
	}
}
