/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.ace;

import com.telenav.cserver.backend.datatypes.Address;
import com.telenav.cserver.backend.datatypes.ace.GeoCodedAddress;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.kernel.util.datatypes.TnContext;

import junit.framework.TestCase;

/**
 * GeocodingTestCase.java
 *
 * @author bhu@telenav.cn
 * @version 1.0 2009-7-6
 */
public class TestGeocoding extends TestCase 
{
	Address address = null;
	TnContext tc = null;
	
	protected void setUp() throws Exception 
	{
		address = new Address();
        address.setFirstLine("1130 kifer rd");
        address.setCityName("sunnyvale");
        address.setState("ca");
        address.setLastLine("sunnyvale,ca");
	    address.setCountry("US");
	    
	    tc =new TnContext();
        tc.addProperty(TnContext.PROP_MAP_DATASET, "NavTeq");
	}
	
//	public void testGeoCodeCN()
//	{
//        address = new Address();
//        address.setFirstLine("ningbo rd at source shanxi rd");
//        address.setCityName("SHANGHAI");
//        address.setState("");
//        address.setCountry("CN");
//        
//        GeoCodeResponse response;
//        try
//        {
//            response = GeoCodingProxy.getInstance(tc).geoCode(address);
//            
//            assertNotNull(response);
//            assertTrue(response.getStatus().isSuccessful());
//            
//            for (GeoCodedAddress match : response.getMatches())
//            {
//            	System.out.println(match.getStreetName());
//            	System.out.println(match.getFirstLine());
//            	System.out.println(match.getCityName());
//            	System.out.println(match.getState());
//            	System.out.println(match.getPostalCode());
//            	System.out.println(match.getLatitude());
//            	System.out.println(match.getLongitude());
//            }
//        }
//        catch (ThrottlingException e)
//        {
//        }
//	}
	public void testGeoCode()
	{
		address = new Address();
        address.setFirstLine("sa");
        address.setCityName("santa barbara");
        address.setState("ca");
        address.setLastLine("sunnyvale,ca");
	    address.setCountry("US");
        GeoCodeResponse response;
        try
        {
            response = GeoCodingProxy.getInstance(tc).geoCode(address);
            
            assertNotNull(response);
            assertTrue(response.getStatus().isSuccessful());
            System.out.println("result size >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + response.getMatches().size());
            for (GeoCodedAddress match : response.getMatches())
            {
            	System.out.println(match.getStreetName());
            	System.out.println(match.getFirstLine());
            	System.out.println(match.getCityName());
            	System.out.println(match.getState());
            	System.out.println(match.getPostalCode());
            	System.out.println(match.getLatitude());
            	System.out.println(match.getLongitude());
            }
        }
        catch (ThrottlingException e)
        {
        }
	}
}
