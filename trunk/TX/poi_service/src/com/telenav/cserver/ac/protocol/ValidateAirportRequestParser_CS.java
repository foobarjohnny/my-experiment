 /**
     * (c) Copyright 2010 TeleNav.
     *  All Rights Reserved.
     */
package com.telenav.cserver.ac.protocol;

import com.telenav.cserver.ac.executor.ValidateAirportRequest;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.protocol.ProtocolRequestParser;
import com.telenav.j2me.datatypes.TxNode;

/**
 * ValidateAirport Request Parser
 * @author jzhu
 * 2010-12-01
 */
public class ValidateAirportRequestParser_CS implements ProtocolRequestParser
{    
    @Override
    public ExecutorRequest[] parse(Object object) throws ExecutorException
    {
        ExecutorRequest[] requests = new ExecutorRequest[1];
        ValidateAirportRequest request = new ValidateAirportRequest();
        requests[0] = request;
        
        TxNode node = (TxNode)object;
        String airportName = node.msgAt(1);
        String region = node.msgAt(2);
        
        request.setAirportName(airportName);
        request.setRegion(region);

        return requests;
    }
}
