package com.telenav.cserver.ac.protocol.protobuf;

import java.io.IOException;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.ac.executor.ShareAddressRequest;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.util.helper.generictest.DataSource;
import com.telenav.cserver.util.helper.generictest.GenericTest;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.j2me.framework.protocol.ProtoBasePoi;
import com.telenav.j2me.framework.protocol.ProtoBizPoi;
import com.telenav.j2me.framework.protocol.ProtoContactInfo;
import com.telenav.j2me.framework.protocol.ProtoShareAddressReq;
import com.telenav.j2me.framework.protocol.ProtoStop;

public class ShareAddressRequestParserTest {

	private ShareAddressRequestParser instance = new ShareAddressRequestParser();
	private DataSource dataSource = new DataSource();
	private ProtocolBuffer protoBuf = new ProtocolBuffer();
	
	@Before
	public void setUp() throws Exception {
		
		ProtoShareAddressReq shareAddressReq = getProtoRequest();
		if(shareAddressReq.computeSize() > 0){
			protoBuf.setBufferData(shareAddressReq.toByteArray());
		}

		ExecutorRequest[] executorReq = new ExecutorRequest[2];
		dataSource.addData(Object.class.getName(), protoBuf);		
		dataSource.addData(ExecutorRequest[].class.getName(), executorReq);
		dataSource.addData(ProtocolBuffer.class.getName(), protoBuf);
		dataSource.addData(ShareAddressRequest.class.getName(), new ShareAddressRequest());
		dataSource.addData(ProtoBasePoi.class.getName(), getProtoBasePoi());
		dataSource.addData(com.televigation.db.poi.Poi.class.getName(), new com.televigation.db.poi.Poi());
		dataSource.addData(Stop.class.getName(), getProtoStop());
		
	}
	
	@After
	public void tearDown() throws Exception {
		dataSource.clear(); 		// clear the data after testing
	}

	@Test
	public void testSuite() throws IOException{
		GenericTest.getInstance().startTest(instance, dataSource);
	}
	
	public ProtoStop getProtoStop(){	
		ProtoStop stop = ProtoStop.newBuilder()
						.setCity("city")
						.setCountry("country")
						.setFirstLine("firstline")
						.setZip("zip")
						.setState("state")
						.setLabel("label")
						.setLat(10000)
						.setLon(10000)
						.build();	
		return stop;
	}
	
	public ProtoBasePoi getProtoBasePoi(){
		
		ProtoBizPoi bizPoi = ProtoBizPoi.newBuilder()
							.setAddress(getProtoStop())
							.build();
		
		ProtoBasePoi poi = ProtoBasePoi.newBuilder().setPoiId(1234)
							.setPopularity(1)
							.setIsRatingEnable(true)
							.setLocalAppInfos(new Vector())
							.setHasUserRatedThisPoi(true)
							.setAvgRating(1)
							.setBizPoi(bizPoi)
							.build();
		return poi;
	}
	
	public ProtoShareAddressReq getProtoRequest() throws IOException{
		
		Vector<ProtoContactInfo> vector = new Vector();
		ProtoContactInfo contact = ProtoContactInfo.newBuilder().setName("test@telenavsoftware.com").setPtn("5555218135").build();
		vector.add(contact);

		ProtoShareAddressReq req = ProtoShareAddressReq.newBuilder()
									.setSenderPTN("5555218135")
									.setLabel("label")
									.setContactList(vector)
									.setStop(getProtoStop())
									.setPoi(getProtoBasePoi())
									.build();
		return req;
	}
}
