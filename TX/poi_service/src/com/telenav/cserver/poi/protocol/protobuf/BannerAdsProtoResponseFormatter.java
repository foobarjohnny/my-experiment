package com.telenav.cserver.poi.protocol.protobuf;

import java.io.IOException;

import net.jarlehansen.protobuf.javame.ByteString;

import org.apache.log4j.Logger;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.protocol.ProtocolResponseFormatter;
import com.telenav.cserver.poi.executor.BannerAdsReponse;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoBannerAdsResp;
import com.telenav.j2me.framework.util.ToStringUtils;

public class BannerAdsProtoResponseFormatter implements ProtocolResponseFormatter 
{

	private static Logger logger = Logger.getLogger(BannerAdsProtoResponseFormatter.class);
	@Override
	public void format(Object formatTarget, ExecutorResponse[] responses) throws ExecutorException 
	{
		CliTransaction cli = com.telenav.cserver.framework.cli.CliTransactionFactory.getInstance(CliConstants.TYPE_MODULE);
        cli.setFunctionName("format");
        ProtocolBuffer pBuffer = (ProtocolBuffer)formatTarget;
        BannerAdsReponse response = (BannerAdsReponse) responses[0];
        
        ProtoBannerAdsResp.Builder builder = ProtoBannerAdsResp.newBuilder();
        builder.setStatus(response.getStatus());
		builder.setErrorMessage(response.getErrorMessage());
		builder.setImageHeight(response.getImageHeight());
		builder.setImageWidth(response.getImageWidth());
		builder.setClickUrl(response.getClickUrl());
		builder.setImageUrl(response.getImageUrl());
		if (null != response.getImgData())
		{
		    builder.setImgData(ByteString.copyFrom(response.getImgData()));
		}
		
		
		byte[] binData = null;
		try
		{
			ProtoBannerAdsResp protoResp = builder.build();
			if( logger.isDebugEnabled() )
			{
				logger.debug(ToStringUtils.toString(protoResp));
			}
			binData = protoResp.toByteArray();
		}
		catch(IOException ex)
		{
			logger.fatal("Failed to convert BannerAdsProtoResponse", ex);
		}
		pBuffer.setBufferData(binData);
        cli.complete();
	}

}
