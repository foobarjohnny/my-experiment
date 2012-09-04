/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.ac.protocol.protobuf;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.telenav.cserver.ac.executor.ValidateAirportRequest;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.protocol.ProtocolRequestParser;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoValidateAirportReq;
import com.telenav.j2me.framework.util.ToStringUtils;

/**
 * ValidateAirportRequestParser.java
 *
 * jhjin@telenav.cn
 * @version 1.0 Feb 25, 2011
 *
 */
public class ValidateAirportRequestParser implements ProtocolRequestParser
{

	private static final Logger logger = Logger.getLogger(ValidateAirportRequestParser.class);
    @Override
    public ExecutorRequest[] parse(Object object) throws ExecutorException
    {
        ProtocolBuffer protoBuff = (ProtocolBuffer)object;
        ValidateAirportRequest request = new ValidateAirportRequest();
        ProtoValidateAirportReq protoRequest = null;
        try{
            protoRequest = ProtoValidateAirportReq.parseFrom(protoBuff.getBufferData());
            if(logger.isDebugEnabled())
            {
            	logger.debug(ToStringUtils.toString(protoRequest));
            }
            request.setAirportName(protoRequest.getAirportName());
            request.setRegion(protoRequest.getRegion());
        }
        catch(IOException ex)
        {
            throw new ExecutorException("can't conver to ValidateAirportRequest!",ex);
        }
        return new ExecutorRequest[]{request};
    }

}
