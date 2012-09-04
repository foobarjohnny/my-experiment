/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.ac.protocol.protobuf;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.telenav.cserver.ac.executor.ValidateAddressRequestACEWS;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.protocol.ProtocolRequestParser;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoValidateAddressReq;
import com.telenav.j2me.framework.util.ToStringUtils;

/**
 * ValidateAddressRequestParser.java
 *
 * jhjin@telenav.cn
 * @version 1.0 Feb 25, 2011
 *
 */
public class ValidateAddressRequestParser implements ProtocolRequestParser
{

	private static final Logger logger = Logger.getLogger(ValidateAddressRequestParser.class);
    @Override
    public ExecutorRequest[] parse(Object object) throws ExecutorException
    {
        ProtocolBuffer protoBuffer = (ProtocolBuffer) object;
        ValidateAddressRequestACEWS request = new ValidateAddressRequestACEWS();
        
        ProtoValidateAddressReq protoRequest = null;
        try
        {
            protoRequest = ProtoValidateAddressReq.parseFrom(protoBuffer.getBufferData());
            if(logger.isDebugEnabled())
            {
            	logger.debug(ToStringUtils.toString(protoRequest));
            }
        }
        catch(IOException ex)
        {
            throw new ExecutorException("Failed to parse Proto Check ProtoValidateAddress");
        }
        request.setCity(protoRequest.getCity());
        request.setCountry(protoRequest.getCountry());
        request.setFirstLine(protoRequest.getFirstLine());
        request.setLastLine(protoRequest.getLastLine());
        request.setState(protoRequest.getState());
        request.setStreet1(protoRequest.getStreet1());
        request.setStreet2(protoRequest.getStreet2());
        request.setZip(protoRequest.getZip());
        request.setTransactionId(protoRequest.getTransactionID());
        request.setAddressSearchId(protoRequest.getAddrSearchID());
        return new ExecutorRequest[]{request};
    }

}
