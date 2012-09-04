/**
 * (c) Copyright 2011 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.ac.protocol.protobuf;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.telenav.cserver.ac.executor.ValidateAirportResponse;
import com.telenav.cserver.backend.datatypes.DataConvert;
import com.telenav.cserver.backend.datatypes.TnPoi;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.protocol.ProtocolResponseFormatter;
import com.telenav.cserver.framework.util.ProtoUtil;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.j2me.framework.protocol.ProtoAirportStop;
import com.telenav.j2me.framework.protocol.ProtoValidateAirportResp;
import com.telenav.j2me.framework.util.ToStringUtils;

/**
 * ValidateAirportResponseFormatter.java
 *
 * jhjin@telenav.cn
 * @version 1.0 Feb 25, 2011
 *
 */
public class ValidateAirportResponseFormatter implements ProtocolResponseFormatter
{

    private static final Logger logger = Logger.getLogger(ValidateAirportResponseFormatter.class);
    @Override
    public void format(Object formatTarget, ExecutorResponse[] responses) throws ExecutorException
    {
        ValidateAirportResponse response = (ValidateAirportResponse)responses[0];
        
        ProtocolBuffer protoBuffer = (ProtocolBuffer)formatTarget;
        ProtoValidateAirportResp.Builder validateAirportRespbuilder = ProtoValidateAirportResp.newBuilder();
        
        validateAirportRespbuilder.setStatus(response.getStatus())
                                  .setErrorMessage(response.getErrorMessage());
        
        List<TnPoi> airports = response.getAirportList();
        if( airports != null )
        {
            for(TnPoi airport : airports)
            {
                Stop stop = DataConvert.convertToStop(airport);
                if (stop == null)
                {
                    continue;
                }

                String airportMsg = airport.getFeatureName() + ": " + airport.getBrandName();
                
                ProtoAirportStop.Builder airportStop = ProtoAirportStop.newBuilder();
                airportStop.setMessage(airportMsg)
                           .setStop(ProtoUtil.convertStopToProtoBuf(stop));
                validateAirportRespbuilder.addElementAirportStops(airportStop.build());
            }
        }
        try
        {                      
        	ProtoValidateAirportResp protoResp = validateAirportRespbuilder.build();
        	if( logger.isDebugEnabled() )
        	{
        		logger.debug(ToStringUtils.toString(protoResp));
        	}
            protoBuffer.setBufferData(protoResp.toByteArray());
        }
        catch(IOException ex)
        {
            logger.fatal("can't format ValidateAirportResponse!", ex);
        }
    }

}
