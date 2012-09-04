package com.telenav.cserver.weather.protocol.protobuf;

import org.apache.log4j.Logger;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.protocol.ProtocolResponseFormatter;
import com.telenav.cserver.framework.util.ProtoUtil;
import com.telenav.cserver.weather.executor.I18NWeatherResponse;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoWeatherResp;
import com.telenav.j2me.framework.util.ToStringUtils;

public class I18NWeatherProtoResponseFormatter implements ProtocolResponseFormatter 
{
	private static Logger logger = Logger.getLogger(I18NWeatherProtoResponseFormatter.class);
	@Override
	public void format(Object formatTarget, ExecutorResponse[] responses)
			throws ExecutorException 
	{
		CliTransaction cli = com.telenav.cserver.framework.cli.CliTransactionFactory.getInstance(CliConstants.TYPE_MODULE);
        cli.setFunctionName("format");
        ProtocolBuffer pBuffer = (ProtocolBuffer)formatTarget;
        I18NWeatherResponse response = (I18NWeatherResponse) responses[0];
        ProtoWeatherResp.Builder builder = ProtoWeatherResp.newBuilder();
        builder.setStatus(response.getStatus());
		builder.setErrorMessage(response.getErrorMessage());
		builder.setLocation(ProtoUtil.convertStopToProtoBuf(response.getLocation()));
		builder.setIsCanadianCarrier(response.isCanadianCarrier());
		builder.setLocale(response.getLocale());
		builder.setView(response.getWeatherView().toProtobuf());
		
		byte[] binData = null;
		try
		{
			ProtoWeatherResp protoResp = builder.build();
			if( logger.isDebugEnabled() )
			{
				logger.debug(ToStringUtils.toString(protoResp));
			}
			binData = protoResp.toByteArray();
		}
		catch(Exception ex)
		{
		    logger.fatal("Failed to convert I18NWeatherProtoResponseFormatter", ex);	
		}
		pBuffer.setBufferData(binData);
        cli.complete();
	}

}
