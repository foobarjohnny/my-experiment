/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.onebox.protocol;

import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.protocol.ProtocolRequestParser;
import com.telenav.cserver.onebox.executor.OneBoxRequest;
import com.telenav.cserver.poi.datatypes.Stop;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.protocol.constants.NodeTypeDefinitions;

/**
 * OneBoxSearchRequestParser_CS
 * @author kwwang
 *
 */
public class OneBoxSearchRequestParser_CS implements ProtocolRequestParser
{

    @Override
    public ExecutorRequest[] parse(Object object) throws ExecutorException
    {
        TxNode node = (TxNode) object;
        OneBoxRequest request = new OneBoxRequest();

        request.setExecutorType(node.msgAt(0));
        request.setSearchString(node.msgAt(1));
        request.setPageNumber((int)(node.valueAt(0)));
        request.setMaxResults((int)node.valueAt(1));
        request.setDistanceUnit((int)node.valueAt(2));
        request.setSearchType((int)node.valueAt(3));
        if (node.getValuesCount()>4)
            request.setSponsorListingNumber((int)node.valueAt(4));
        else
            request.setSponsorListingNumber(1);

        TxNode child = null;
        for (int i = 0; i < node.childrenSize(); i++)
        {
            child = node.childAt(i);
            if (NodeTypeDefinitions.TYPE_ONE_BOX_ROUTE_NODE == child.valueAt(0))
            {
                request.setRouteID((int)child.valueAt(1));
                request.setSegmentId((int)child.valueAt(2));
                request.setEdgeId((int)child.valueAt(3));
                request.setShapePointId((int)child.valueAt(4));
                request.setRange((int)child.valueAt(5));
                request.setCurrentLat((int)child.valueAt(6));
                request.setCurrentLon((int)child.valueAt(7));
                request.setSearchAlongType((int)child.valueAt(8));
            }
            else if (NodeTypeDefinitions.TYPE_ONE_BOX_ADDRESS_SHARING_ORGIN_NODE == child.valueAt(0))
            {
                request.setStop(parseStop(child));
            }
            else if (NodeTypeDefinitions.TYPE_ONE_BOX_ADDRESS_SHARING_DEST_NODE == child.valueAt(0))
            {
                request.setStopDest(parseStop(child));
            }
        }

        return new ExecutorRequest[]
        { request };
    }

    private Stop parseStop(TxNode node)
    {
        Stop stop = new Stop();
        stop.lat = (int)node.valueAt(1);
        stop.lon = (int)node.valueAt(2);;
        stop.city = node.msgAt(0);
        stop.state = node.msgAt(1);
        stop.zip = node.msgAt(2);
        return stop;
    }

}
