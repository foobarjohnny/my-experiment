package com.telenav.cserver.onebox.protocol.protobuf;

import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.poi.protocol.protobuf.SearchPoiProtoRequestParser;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoOneBoxRoute;
import com.telenav.j2me.framework.protocol.ProtoOneBoxSearchReq;
import com.telenav.j2me.framework.protocol.ProtoSearchItem;
import com.telenav.j2me.framework.protocol.ProtoStop;
import com.telenav.protocol.constants.NodeTypeDefinitions;

public class OneBoxSearchProtoRequestParserTest extends OneBoxSearchProtoRequestParser
{
	ProtocolBuffer protoBuf = new ProtocolBuffer();
	@Before
	public void setUp() throws Exception {
	
		ProtoStop stop = ProtoStop.newBuilder().build();
		ProtoStop dest = ProtoStop.newBuilder().build();
		ProtoOneBoxRoute oneBoxRoute = ProtoOneBoxRoute.newBuilder().setCurrentLat(3737392)
										.setCurrentLon(-12199919)
										.setEdgeId(1)
										.setRange(0)
										.setRouteId(617792720)
										.setSearchAlongType(0)
										.setSegmentId(0)
										.setShapePointId(0)
										.build();

		ProtoSearchItem item0 = ProtoSearchItem.newBuilder().setDest(dest)
				.setOneBoxRoute(oneBoxRoute)
				.setStop(stop)
				.setType(NodeTypeDefinitions.TYPE_ONE_BOX_ROUTE_NODE)
				.build();
		
		ProtoSearchItem item1 = ProtoSearchItem.newBuilder().setDest(dest)
				.setOneBoxRoute(oneBoxRoute)
				.setStop(stop)
				.setType(NodeTypeDefinitions.TYPE_ONE_BOX_ADDRESS_SHARING_ORGIN_NODE)
				.build();
		
		ProtoSearchItem item2 = ProtoSearchItem.newBuilder().setDest(dest)
				.setOneBoxRoute(oneBoxRoute)
				.setStop(stop)
				.setType(NodeTypeDefinitions.TYPE_ONE_BOX_ADDRESS_SHARING_DEST_NODE)
				.build();
		
		Vector<ProtoSearchItem> items = new Vector<ProtoSearchItem>();
		items.add(item0);
		items.add(item1);
		items.add(item2);

		ProtoOneBoxSearchReq req = ProtoOneBoxSearchReq.newBuilder()
									.setDistanceUnit(1)
									.setInputType(1)
									.setMaxResult(9)
									.setPageNumber(0)
									.setSearchItems(items)
									.setSearchString("")
									.setSearchType(5)
									.setSponsorListingNumber(2)
									.setTransactionId("1321347362794")
									.build();		
		
		protoBuf.setBufferData(req.toByteArray());
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testParse() throws ExecutorException {
		this.parse(protoBuf);
	}

}
