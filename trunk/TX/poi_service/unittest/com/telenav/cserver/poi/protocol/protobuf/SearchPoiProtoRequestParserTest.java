package com.telenav.cserver.poi.protocol.protobuf;

import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoOneBoxRoute;
import com.telenav.j2me.framework.protocol.ProtoPoiSearchReq;
import com.telenav.j2me.framework.protocol.ProtoSearchItem;
import com.telenav.j2me.framework.protocol.ProtoStop;
import com.telenav.protocol.constants.NodeTypeDefinitions;

public class SearchPoiProtoRequestParserTest extends SearchPoiProtoRequestParser{

	ProtocolBuffer protoBuf = new ProtocolBuffer();
	@Before
	public void setUp() throws Exception {
	
	/*
		{	
			"inputString":"","isMostPopular":"0","transactionId":"1321347362794","searchFromType":"1","sponsorListingNumber":2,
			"searchTypeStr":"5","categoryId":"703","sortType":"4","from":"type","currentPage":"0","distanceUnit":1,
			"addressString":"{\"zip\":\"\",\"lon\":-12199983,\"state\":\"\",\"firstLine\":\"\",\"label\":\"\",\"type\":6,\"lat\":3737453,\"country\":\"\",\"city\":\"\"}","maxResults":9
		}
		
		{
			"range":"35","currentLon":"-12199919","segmentId":"0","sponsorListingNumber":2,"searchTypeStr":"7","categoryId":"374","searchAlongType":0,"shapePointId":"0","from":"type",
			"routeID":"617792720","maxResults":9,"inputString":"","currentLat":"3737392","isMostPopular":"0","edgeId":"1","searchFromType":"3","sortType":"3","currentPage":"0",
			"distanceUnit":1,"addressStringDest":"{\"lon\":-12239382,\"lat\":3761386}","addressString":"{\"lon\":-12199919,\"lat\":3737392}"
		}
		
	*/
		
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
								
		ProtoSearchItem item = ProtoSearchItem.newBuilder().setDest(dest)
									.setOneBoxRoute(oneBoxRoute)
									.setStop(stop)
									.setType(NodeTypeDefinitions.TYPE_ONE_BOX_ROUTE_NODE)
									.build();
					
		Vector<ProtoSearchItem> items = new Vector<ProtoSearchItem>();
		items.add(item);
		ProtoPoiSearchReq req = ProtoPoiSearchReq.newBuilder().setCategoryId(703)
									.setDistanceUnit(1)
									.setIsMostPopular(true)
									.setMaxResult(9)
									.setPageNumber(0)
									.setRadiusInMeter(10)
									.setSearchFromType(SearchPoiProtoRequestParser.TYPE_POI_SPEAKIN_ALONG)
									.setSearchItems(items)
									.setSearchString("")
									.setSearchType(5)
									.setSortType(4)
									.setSponsorListingNumber(2)
									.setTransactionID("1321347362794")
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
