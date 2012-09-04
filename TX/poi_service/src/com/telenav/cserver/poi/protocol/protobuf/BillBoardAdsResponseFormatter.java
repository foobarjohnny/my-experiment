package com.telenav.cserver.poi.protocol.protobuf;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.backend.datatypes.adservice.BillBoardAds;
import com.telenav.cserver.backend.datatypes.adservice.GeoFence;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.protocol.ProtocolResponseFormatter;
import com.telenav.cserver.poi.executor.BillBoardAdsResponse;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoBillBoardAds;
import com.telenav.j2me.framework.protocol.ProtoBillBoardAdsResp;
import com.telenav.j2me.framework.protocol.ProtoGeoFence;
import com.telenav.j2me.framework.util.ToStringUtils;

public class BillBoardAdsResponseFormatter implements ProtocolResponseFormatter 
{

	private static Logger logger = Logger.getLogger(BillBoardAdsResponseFormatter.class);
	@Override
	public void format(Object formatTarget, ExecutorResponse[] responses) throws ExecutorException 
	{
		CliTransaction cli = com.telenav.cserver.framework.cli.CliTransactionFactory.getInstance(CliConstants.TYPE_MODULE);
        cli.setFunctionName("format");
        ProtocolBuffer pBuffer = (ProtocolBuffer)formatTarget;
        BillBoardAdsResponse response = (BillBoardAdsResponse) responses[0];
        ProtoBillBoardAdsResp.Builder builder = ProtoBillBoardAdsResp.newBuilder();
        
        builder.setStatus(response.getStatus());
		builder.setErrorMessage(response.getErrorMessage());
		
		List<BillBoardAds> billBoardAdsList = response.getBillBoardAdsList();
		for(BillBoardAds billBoardAds: billBoardAdsList)
		{
		    builder.addElementBillBoardAds(getBillBoardAds(billBoardAds));
		}
		
		byte[] binData = null;
		try
		{
		    ProtoBillBoardAdsResp protoResp = builder.build();
		    if( logger.isDebugEnabled() ){
		        logger.debug(ToStringUtils.toString(protoResp));;
		    }
			binData = protoResp.toByteArray();
		}
		catch(IOException ex)
		{
			logger.fatal("Failed to convert BillBoardAdsResponse", ex);
		}
		pBuffer.setBufferData(binData);
        cli.complete();
	}
	
	private ProtoBillBoardAds getBillBoardAds(BillBoardAds billBoardAds)
	{
	    ProtoBillBoardAds.Builder builder = ProtoBillBoardAds.newBuilder();
	    builder.setAdsId(billBoardAds.getAdsId()).setDetailViewTime(billBoardAds.getDetailViewTime())
	            .setExpirationTime(billBoardAds.getExpirationTime())
	            .setInitialViewTime(billBoardAds.getInitialViewTime())
	            .setPoiViewTime(billBoardAds.getPoiViewTime())
	            .setAdsUrl(billBoardAds.getAdsUrl());
	    
	    if (billBoardAds.getGeoFence() != null)
	    {
	        builder.setGeoFence(getGeoFence(billBoardAds.getGeoFence()));
	    }
	    return builder.build();
	}
	
	private ProtoGeoFence getGeoFence(GeoFence geoFence)
	{
	    ProtoGeoFence.Builder builder = ProtoGeoFence.newBuilder();
	    builder.setDistance(geoFence.getDistance())
	            .setLat(geoFence.getLat())
	            .setLon(geoFence.getLon());
	    
	    return builder.build();
	}

}
