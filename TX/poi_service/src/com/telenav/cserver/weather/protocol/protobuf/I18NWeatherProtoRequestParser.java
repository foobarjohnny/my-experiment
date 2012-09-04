package com.telenav.cserver.weather.protocol.protobuf;

import org.apache.log4j.Logger;

import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.protocol.ProtocolRequestParser;
import com.telenav.cserver.framework.util.ProtoUtil;
import com.telenav.cserver.weather.executor.I18NWeatherRequest;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoWeatherReq;
import com.telenav.j2me.framework.util.ToStringUtils;

public class I18NWeatherProtoRequestParser implements ProtocolRequestParser 
{
	private static final Logger logger = Logger.getLogger(I18NWeatherProtoRequestParser.class);
	@Override
	public ExecutorRequest[] parse(Object object) throws ExecutorException 
	{
		I18NWeatherRequest request = new I18NWeatherRequest();
		ProtocolBuffer protoBuffer = (ProtocolBuffer) object;
		ProtoWeatherReq pWeather = null;
		try
		{
		    pWeather = ProtoWeatherReq.parseFrom(protoBuffer.getBufferData());
		    if( logger.isDebugEnabled() )
		    {
		    	logger.debug(ToStringUtils.toString(pWeather));
		    }
		}
		catch(Exception e)
		{
			throw new ExecutorException("Failed to parse Proto Weather Request");
		}
		if(pWeather == null)
		{
			throw new ExecutorException("WeatherRequest is null");
		}
		request.setLocation(ProtoUtil.convertProtoBufToStop(pWeather.getLocation()));
		request.setCanadianCarrier(pWeather.getIsCanadianCarrier());
		request.setLocale(pWeather.getLocale());
		return new ExecutorRequest[]{request};
	}

}
