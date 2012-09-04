/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.ac.protocol.v20;

import java.util.List;

import com.telenav.cserver.ac.executor.v20.ValidateAirportResponse;
import com.telenav.cserver.backend.datatypes.AddressDataConverter;
import com.telenav.cserver.backend.datatypes.TnPoi;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.protocol.ProtocolResponseFormatter;
import com.telenav.j2me.datatypes.DataConstants;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.protocol.constants.NodeTypeDefinitions;

/**
 * ValidateAirport  response formatter<br>
 * @author jzhu 2010-12-01
 * copy and update by xfliu at 2011/12/6
 */
public class ValidateAirportResponseFormatter_CS implements ProtocolResponseFormatter
{

    @Override
    public void format(Object formatTarget, ExecutorResponse[] responses) throws ExecutorException
    {
        TxNode node = (TxNode) formatTarget;
        ValidateAirportResponse response = (ValidateAirportResponse) responses[0];// response
        node.addValue(DataConstants.TYPE_SERVER_RESPONSE);
        node.addValue(response.getStatus());
        node.addMsg(response.getExecutorType());
        node.addMsg(response.getErrorMessage());

        List<TnPoi> airportList = response.getAirportList();
        for(TnPoi airport: airportList)
        {
            Stop stop = AddressDataConverter.TnPoi2Stop(airport);
            if (stop == null)
            {
                continue;
            }

            String airportMsg = airport.getFeatureName() + ": " + airport.getBrandName();
            
            TxNode airportNode = new TxNode();
            airportNode.addValue(NodeTypeDefinitions.TYPE_AIRPORT_WITH_SHOW_MSG);
            airportNode.addMsg(airportMsg);
            airportNode.addChild(stop.toTxNode());
            
            node.addChild(airportNode);
        }

    }

}
