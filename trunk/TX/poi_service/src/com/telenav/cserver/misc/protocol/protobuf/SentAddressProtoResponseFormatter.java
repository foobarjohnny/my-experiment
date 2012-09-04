package com.telenav.cserver.misc.protocol.protobuf;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.telenav.cli.client.CliConstants;
import com.telenav.cli.client.CliTransaction;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.protocol.ProtocolResponseFormatter;
import com.telenav.cserver.misc.executor.SentAddressResponse;
import com.telenav.cserver.misc.struts.datatype.Address;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.j2me.framework.protocol.ProtoSentAddress;
import com.telenav.j2me.framework.protocol.ProtoSentAddressResp;
import com.telenav.j2me.framework.util.ToStringUtils;

public class SentAddressProtoResponseFormatter implements ProtocolResponseFormatter 
{
	private static Logger logger = Logger.getLogger(SentAddressProtoResponseFormatter.class);
	@Override
	public void format(Object formatTarget, ExecutorResponse[] responses) throws ExecutorException
	{
		CliTransaction cli = com.telenav.cserver.framework.cli.CliTransactionFactory.getInstance(CliConstants.TYPE_MODULE);
        cli.setFunctionName("format");
        ProtocolBuffer pBuffer = (ProtocolBuffer)formatTarget;
        SentAddressResponse resp = (SentAddressResponse) responses[0];
        ProtoSentAddressResp.Builder builder = ProtoSentAddressResp.newBuilder();
        
        builder.setStatus(resp.getStatus());
		builder.setErrorMessage(resp.getErrorMessage());
		builder.setPath(resp.getPath());
		
		
		List<Address> addressList = resp.getAddressList();
		if (addressList != null)
		{
		    for(Address address:addressList)
		    {
		        ProtoSentAddress pSentAddress = convertAddress(address);
		        builder.addElementAddressList(pSentAddress);
		    }
		}
		
		byte[] binData = null;
		try
		{
			ProtoSentAddressResp protoResp = builder.build();
			if( logger.isDebugEnabled() )
			{
				logger.debug(ToStringUtils.toString(protoResp));
			}
			binData = protoResp.toByteArray();
		}
		catch(IOException ex)
		{
		    logger.fatal("Failed to convert SentAddressProtoResponse ", ex);	
		}
		pBuffer.setBufferData(binData);
        cli.complete();

	}
	
	
	private ProtoSentAddress convertAddress(Address address)
	{
		if (address == null)
		    return null;
		
		ProtoSentAddress.Builder builder = ProtoSentAddress.newBuilder();
		builder.setCity(address.getCity())
		        .setCountry(address.getCountry())

		        .setDisplayCityText(address.getDisplayCityText())
		        .setId(address.getId())
		        .setLabel(address.getLabel())
		        .setPostalCode(address.getPostalCode())
		        .setProvince(address.getProvince())
		        .setSentAt(address.getSentAt())
		        .setSentOn(address.getSentOn())
		        .setStreet(address.getStreet());
		
		if (address.getCreateTime() != null)
		    builder.setCreateTime(address.getCreateTime().getTime());
		
		if (address.getReceiverList() != null)
		{
		    for(String receiver:address.getReceiverList())
		    {
		        builder.addElementReceiverList(receiver);
		    }
		}
				
		
		return builder.build();
	} 
}
