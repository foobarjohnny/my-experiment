/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.ac.protocol.protobuf;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.telenav.cserver.ac.executor.ShareAddressResponse;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.protocol.ProtocolResponseFormatter;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoShareAddressResp;
import com.telenav.j2me.framework.util.ToStringUtils;

/**
 * ShareAddressResponseFormatter
 * @author jhjin
 * 
 */
public class ShareAddressResponseFormatter implements ProtocolResponseFormatter
{

    private static Logger logger = Logger.getLogger(ShareAddressResponseFormatter.class);
    @Override
    public void format(Object formatTarget, ExecutorResponse[] responses) throws ExecutorException
    {
        ShareAddressResponse resp = (ShareAddressResponse) responses[0];
        ProtocolBuffer protoBuffer = (ProtocolBuffer)formatTarget;
        
        ProtoShareAddressResp.Builder builder = ProtoShareAddressResp.newBuilder();
        builder.setStatus(resp.getStatus())
               .setErrorMessage(resp.getErrorMessage());
        
        try
        {
        	ProtoShareAddressResp protoResp = builder.build();
        	if (logger.isDebugEnabled() )
        	{
        		logger.debug(ToStringUtils.toString(protoResp));
        	}
            protoBuffer.setBufferData(protoResp.toByteArray());
        }
        catch (IOException ex)
        {
            logger.fatal("Failed to convert ShareAddressResponseFormatter", ex);
        }
    }

}
