package com.telenav.cserver.poi.protocol.protobuf;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.protocol.ProtocolRequestParser;
import com.telenav.cserver.poi.executor.BillBoardAdsRequest;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoBillBoardAdsReq;
import com.telenav.j2me.framework.util.ToStringUtils;

public class BillBoardAdsRequestParser implements ProtocolRequestParser 
{
    
    private static final Logger logger = Logger.getLogger(BillBoardAdsRequestParser.class);
	
	@Override
	public ExecutorRequest[] parse(Object object) throws ExecutorException 
	{
		BillBoardAdsRequest request = new BillBoardAdsRequest();
		ProtocolBuffer protoBuffer = (ProtocolBuffer) object;
		ProtoBillBoardAdsReq pBillBoardAdsReq = null;
		try
		{
		    pBillBoardAdsReq = ProtoBillBoardAdsReq.parseFrom(protoBuffer.getBufferData());
		    if( logger.isDebugEnabled() ){
		        logger.debug(ToStringUtils.toString(pBillBoardAdsReq));
		    }
		    
		}
		catch(IOException ex)
		{
			throw new ExecutorException("Failed to parse Proto PoiSearch Request");
		}
		if(pBillBoardAdsReq == null)
		{
			throw new ExecutorException("ProtoPoiSearchReq is null");
		}
		
		request.setRouteId(pBillBoardAdsReq.getRouteId());
		
		return new ExecutorRequest[]{request};
	}

    

}
