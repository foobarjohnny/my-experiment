package com.telenav.cserver.onebox.protocol.protobuf;

import java.io.IOException;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.protocol.ProtocolRequestParser;
import com.telenav.cserver.onebox.executor.OneBoxRequest;
import com.telenav.cserver.poi.datatypes.Stop;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoOneBoxRoute;
import com.telenav.j2me.framework.protocol.ProtoOneBoxSearchReq;
import com.telenav.j2me.framework.protocol.ProtoSearchItem;
import com.telenav.j2me.framework.protocol.ProtoStop;
import com.telenav.j2me.framework.util.ToStringUtils;
import com.telenav.protocol.constants.NodeTypeDefinitions;

public class OneBoxSearchProtoRequestParser implements ProtocolRequestParser {

	private static final Logger logger = Logger.getLogger(OneBoxSearchProtoRequestParser.class);
	@Override
	public ExecutorRequest[] parse(Object object) throws ExecutorException 
	{
		OneBoxRequest request = new OneBoxRequest();
		ProtocolBuffer protoBuffer = (ProtocolBuffer) object;
		ProtoOneBoxSearchReq pOneBox = null;
		try
		{
			pOneBox = ProtoOneBoxSearchReq.parseFrom(protoBuffer.getBufferData());
			if(logger.isDebugEnabled())
			{
				logger.debug(ToStringUtils.toString(pOneBox));
			}
		}
		catch(IOException ex)
		{
			throw new ExecutorException("Failed to parse Proto OneBoxSearch Request");
		}
		if(pOneBox == null)
		{
			throw new ExecutorException("ProtoOneBoxSearchReq is null");
		}
		request.setSearchString(pOneBox.getSearchString());
		request.setPageNumber(pOneBox.getPageNumber());
		request.setMaxResults(pOneBox.getMaxResult());
		request.setDistanceUnit(pOneBox.getDistanceUnit());
		request.setSearchType(pOneBox.getSearchType());
		request.setSponsorListingNumber(pOneBox.getSponsorListingNumber());
		request.setTransactionId(pOneBox.getTransactionId());
		Vector<ProtoSearchItem> items = pOneBox.getSearchItems();
		for(ProtoSearchItem item : items)
		{
			if(item.getType() == NodeTypeDefinitions.TYPE_ONE_BOX_ROUTE_NODE)
			{
				ProtoOneBoxRoute oneBoxRoute = item.getOneBoxRoute();
				request.setRouteID(oneBoxRoute.getRouteId());
				request.setSegmentId(oneBoxRoute.getSegmentId());
				request.setEdgeId(oneBoxRoute.getEdgeId());
				request.setShapePointId(oneBoxRoute.getShapePointId());
				request.setRange(oneBoxRoute.getRange());
				request.setCurrentLat(oneBoxRoute.getCurrentLat());
				request.setCurrentLon(oneBoxRoute.getCurrentLon());
				request.setSearchAlongType(oneBoxRoute.getSearchAlongType());
			}
			else if(item.getType() == NodeTypeDefinitions.TYPE_ONE_BOX_ADDRESS_SHARING_ORGIN_NODE)
			{
				request.setStop(parseStop(item.getStop()));
			}
			else if(item.getType() == NodeTypeDefinitions.TYPE_ONE_BOX_ADDRESS_SHARING_DEST_NODE)
			{
				request.setStopDest(parseStop(item.getDest()));
			}
		}
		return new ExecutorRequest[]{request};
	}
	
	private Stop parseStop(ProtoStop pStop)
    {
        Stop stop = new Stop();
        stop.lat = pStop.getLat();
        stop.lon = pStop.getLon();
        stop.city = pStop.getCity();
        stop.state = pStop.getState();
        stop.zip = pStop.getZip();
        return stop;
    }

}
