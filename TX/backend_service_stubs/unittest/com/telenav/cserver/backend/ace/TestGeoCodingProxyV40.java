package com.telenav.cserver.backend.ace;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.backend.datatypes.Address;
import com.telenav.cserver.backend.datatypes.AddressDataConverter;
import com.telenav.cserver.backend.datatypes.AddressFormatConstants;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.datatypes.address.v40.Street;
import com.telenav.datatypes.locale.v10.Country;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.services.geocoding.v40.GeoCodedAddress;

public class TestGeoCodingProxyV40
{
	private Address Address;
	
	private GeoCodeRequestV40 csReq;
	
	private GeoCodingProxyV40 proxyV40;
	
	@Before
	public void init()
	{
//		"B1/B2,Dadabhai Cross Road,Khira Nagar", "Santacruz West,Mumbai,maharastra"
		//1. initialize Address
		Address = new Address();
//		Address.setDoor("B1/B2");
//		Address.setStreetName("Dadabhai Cross Road");
//		Address.setSublocality("Khira Nagar");
//		Address.setLocality("Santacruz West");
//		Address.setCityName("Mumbai");
//		Address.setCounty("maharastra");
//		Address.setState("");
//		Address.setPostalCode("");
		
		//2. set SearchArea related attributes;
		Address.setCountry("IN");
//		Address.setLatitude(0);
//		Address.setLontitude(0);
		
		//3. initialize lines
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(AddressFormatConstants.FIRST_LINE,"X 19, Block X");
		map.put(AddressFormatConstants.LAST_LINE, "Patel Nagar West, New Delhi, Delhi NCR");
		Address.setLines(map);
		
		//4. create ws request
		csReq = new GeoCodeRequestV40();
		csReq.setAddress(Address);
		csReq.setTransactionID(String.valueOf(System.currentTimeMillis()));
		
//		5. create proxy
		TnContext context =new TnContext("");
    	context.addProperty("dataset","MMI");
		proxyV40 = GeoCodingProxyV40.getInstance(context);
	}
	
	@Test
	public void testQueryWithAddressObj()
	{
//		6. get response
		GeoCodeResponseV40 csRes = null;
		try
		{
			csRes = proxyV40.geoCode(csReq);
//			csRes = proxyV40.geoCode(Address);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		Assert.assertNotSame(GeoCodeResponseV40.NULL_RESPONSE, csRes);
		Assert.assertTrue(csRes.getMatchCount() > 0);
		processResults(csRes);		
	}

	@Test
	public void testQueryWithRequestObj()
	{
//		7. get response
		GeoCodeResponseV40 csRes = null;
		try
		{
//			csRes = proxyV40.geoCode(csReq);
			csRes = proxyV40.geoCode(Address);
		} catch (ThrottlingException e)
		{
			e.printStackTrace();
		}
		Assert.assertNotSame(GeoCodeResponseV40.NULL_RESPONSE, csRes);
		Assert.assertTrue(csRes.getMatchCount() > 0);
		processResults(csRes);
	}
	
	private void processResults(GeoCodeResponseV40 csRes)
	{
		csRes.toString();
		if(csRes.getMatchCount() == 0)
		{
			return;
		}
		
		System.out.println("Total >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + csRes.getMatchCount());
		
		for(Address temp : csRes.getMatches())
		{
//			System.out.println("map >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//			System.out.println(temp.getLines().get(AddressFormatConstants.FIRST_LINE) + ", " + temp.getLines().get(AddressFormatConstants.LAST_LINE));
			System.out.println("address >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			System.out.println(">>>>>>>>>>>>>>>>>>"+temp.getDoor());
		}
	}
}
