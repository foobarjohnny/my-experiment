package com.telenav.cserver.poi.protocol.protobuf;

import java.io.IOException;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.protocol.ProtocolResponseFormatter;
import com.telenav.cserver.poi.datatypes.ProtoDataConvert;
import com.telenav.cserver.poi.executor.POISearchResponse_WS;
import com.telenav.cserver.poi.protocol.POIResponseUtil;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoPoiSearchResp;
import com.telenav.j2me.framework.protocol.ProtoPromptItem;
import com.telenav.j2me.framework.util.ToStringUtils;

public class SearchPoiProtoResponseFormatter implements ProtocolResponseFormatter 
{

	private static Logger logger = Logger.getLogger(SearchPoiProtoResponseFormatter.class);
	@Override
	public void format(Object formatTarget, ExecutorResponse[] responses) throws ExecutorException 
	{
		CliTransaction cli = com.telenav.cserver.framework.cli.CliTransactionFactory.getInstance(CliConstants.TYPE_MODULE);
        cli.setFunctionName("format");
        ProtocolBuffer pBuffer = (ProtocolBuffer)formatTarget;
        POISearchResponse_WS response = (POISearchResponse_WS) responses[0];
        ProtoPoiSearchResp.Builder builder = ProtoPoiSearchResp.newBuilder();
        builder.setStatus(response.getStatus());
		builder.setErrorMessage(response.getErrorMessage());
		builder.setTotalCount(response.getTotalCount());
		ProtoPromptItem promptItem = POIResponseUtil.getPromptItem(response.getPromptItems());
		builder.setPromptItem(promptItem);
		Vector vPois = ProtoDataConvert.handleBasePoi(response.getBasePoiList());
		Vector vSponsorPois = ProtoDataConvert.handlePOI(response.getSponsorPoiList());
		builder.setPois(vPois);
		builder.setSponsorPois(vSponsorPois);
		builder.setPoiDetailUrl(response.getPoiDetailUrl());
		byte[] binData = null;
		try
		{
			ProtoPoiSearchResp protoResp = builder.build();
			if( logger.isDebugEnabled() )
			{
				logger.debug(ToStringUtils.toString(protoResp));
			}
			binData = protoResp.toByteArray();
		}
		catch(IOException ex)
		{
			logger.fatal("Failed to convert ProtoSearchPOIResponse", ex);
		}
		pBuffer.setBufferData(binData);
        cli.complete();
	}


}
