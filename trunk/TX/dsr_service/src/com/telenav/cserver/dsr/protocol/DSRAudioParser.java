package com.telenav.cserver.dsr.protocol;

import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.protocol.ProtocolRequestParser;
import com.telenav.j2me.datatypes.ProtocolBuffer;

public class DSRAudioParser implements ProtocolRequestParser, ProtocolConstants
{
	
		@Override
	public ExecutorRequest[] parse(Object object) throws ExecutorException
	{
		ProtocolBuffer pb = (ProtocolBuffer) object;
		ExecutorRequest er = new ExecutorRequest();
		er.setAttribute(ProtocolConstants.AUDIO, pb.getBufferData());
		return new ExecutorRequest[]{er};
	}
}
