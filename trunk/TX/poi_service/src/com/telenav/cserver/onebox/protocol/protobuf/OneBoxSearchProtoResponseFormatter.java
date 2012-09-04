package com.telenav.cserver.onebox.protocol.protobuf;

import java.io.IOException;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.backend.commutealert.DataConverter;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.protocol.ProtocolResponseFormatter;
import com.telenav.cserver.onebox.executor.OneBoxResponse;
import com.telenav.cserver.poi.datatypes.ProtoDataConvert;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoAddressItem;
import com.telenav.j2me.framework.protocol.ProtoOneBoxSearchResp;
import com.telenav.j2me.framework.protocol.ProtoSuggestionItem;
import com.telenav.j2me.framework.util.ToStringUtils;
import com.telenav.services.search.onebox.v10.QuerySuggestion;
import com.telenav.ws.datatypes.address.Address;

public class OneBoxSearchProtoResponseFormatter implements ProtocolResponseFormatter 
{
	private static Logger logger = Logger.getLogger(OneBoxSearchProtoResponseFormatter.class);
	@Override
	public void format(Object formatTarget, ExecutorResponse[] responses) throws ExecutorException
	{
		CliTransaction cli = com.telenav.cserver.framework.cli.CliTransactionFactory.getInstance(CliConstants.TYPE_MODULE);
        cli.setFunctionName("format");
        ProtocolBuffer pBuffer = (ProtocolBuffer)formatTarget;
        OneBoxResponse resp = (OneBoxResponse) responses[0];
        ProtoOneBoxSearchResp.Builder builder = ProtoOneBoxSearchResp.newBuilder();
        builder.setStatus(resp.getStatus());
		builder.setErrorMessage(resp.getErrorMessage());
		builder.setExactMatchStatus(resp.getExactMatchStatus());
		if(resp.getPoiResp() != null)
		    builder.setTotalCount(String.valueOf(resp.getPoiResp().getTotalCount()));
		builder.setSuggestionItem(handleSuggestion(resp));
		builder.setAddressItem(handleAddressList(resp));
		if(resp.getPoiResp() != null)
		{
			builder.setSponsorPois(ProtoDataConvert.handlePOI(resp.getPoiResp().getSponsorPoiList()));
			builder.setPois(ProtoDataConvert.handleBasePoi(resp.getPoiResp().getBasePoiList()));
			builder.setPoiDetailUrl(resp.getPoiDetailUrl());
		}
		byte[] binData = null;
		try
		{
			ProtoOneBoxSearchResp protoResp = builder.build();
			if( logger.isDebugEnabled() )
			{
				logger.debug(ToStringUtils.toString(protoResp));
			}
			binData = protoResp.toByteArray();
		}
		catch(IOException ex)
		{
		    logger.fatal("Failed to convert ProtoOneBoxSearchPOIResponse", ex);	
		}
		pBuffer.setBufferData(binData);
        cli.complete();

	}
	
	private Vector handleSuggestion(OneBoxResponse resp)
	{
		Vector vc = null;
		if (resp.getSuggestions() != null && resp.getSuggestions().length > 0)
		{
			vc = new Vector(); 
			for (QuerySuggestion suggestion : resp.getSuggestions())
			{
				vc.add(ProtoSuggestionItem.newBuilder().
						setDisplayLabel(suggestion.getDisplayLabel()).
						setQueryStr(suggestion.getQuery()).build());
			}
		}
        return vc;
	}
	
	private Vector handleAddressList(OneBoxResponse resp)
	{
		Vector vc = null;
		if (resp.getAddressList() != null && resp.getAddressList().size() > 0)
		{
            vc = new Vector();
            for(Address addr : resp.getAddressList())
            {
            	ProtoAddressItem.Builder addressItem = ProtoAddressItem.newBuilder();
            	addressItem.setLatitude(DataConverter.convertToDM5(addr.getGeoCode().getLatitude()));
            	addressItem.setLotitude(DataConverter.convertToDM5(addr.getGeoCode().getLongitude()));
            	addressItem.setStopType(1);
            	addressItem.setIsGeocoded(true);
            	addressItem.setFirstLine(addr.getFirstLine());
            	addressItem.setLabel(addr.getFirstLine());
            	addressItem.setCity(addr.getCity());
            	addressItem.setState(addr.getState());
            	addressItem.setZipCode(addr.getPostalCode());
            	addressItem.setCountry(addr.getCountry());
            	vc.add(addressItem.build());
            }
		}
		return vc;
	}
	
	
}
