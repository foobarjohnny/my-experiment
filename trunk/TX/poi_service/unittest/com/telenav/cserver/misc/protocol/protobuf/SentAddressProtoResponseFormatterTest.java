package com.telenav.cserver.misc.protocol.protobuf;

import java.util.ArrayList;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.misc.executor.SentAddressResponse;
import com.telenav.cserver.misc.struts.datatype.Address;
import com.telenav.j2me.datatypes.ProtocolBuffer;

public class SentAddressProtoResponseFormatterTest extends SentAddressProtoResponseFormatter{

	ProtocolBuffer formatTarget = new ProtocolBuffer();
	SentAddressResponse resp = new SentAddressResponse();
	ExecutorResponse[] responses = {resp};
	
	@Before
	public void setUp() throws Exception {
		resp.setStatus(ExecutorResponse.STATUS_OK);
		resp.setErrorMessage("errorMsg");
		resp.setPath("sentAddressPath");
		
		Address address = new Address();
		ArrayList<Address> addressList = new ArrayList<Address>();
		resp.setAddressList(addressList);
		addressList.add(address);
		
		address.setCity("Palo Alto");
		address.setCountry("USA");
		address.setDisplayCityText("Invite you to use TELENAV Navigation");
		address.setId(1);
		address.setLabel("welcome, my friend");
		address.setPostalCode("94303");
		address.setProvince("");
		address.setSentAt("setAt");
		address.setSentOn("sentOn");
		address.setStreet("street");
		address.setCreateTime(new Date(System.currentTimeMillis()));
		
		ArrayList<String> receiverList = new ArrayList<String>();
		receiverList.add(String.valueOf("5555218135"));
		receiverList.add(String.valueOf("5555215554"));
		address.setReceiverList(receiverList);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFormat() throws ExecutorException {
		this.format(formatTarget, responses);
	}
}
