package com.telenav.cserver.ac.protocol;

import junit.framework.Assert;

import org.apache.struts.mock.MockHttpServletRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.ac.executor.ShareAddressResponse;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.j2me.datatypes.Stop;

public class ShareAddressResponseFormatterTest extends ShareAddressResponseFormatter{

	
	MockHttpServletRequest request = new MockHttpServletRequest();
	ShareAddressResponse response = new ShareAddressResponse();
	
	@Before
	public void setUp() throws Exception {

		response.setStatus(ExecutorResponse.STATUS_OK);
		response.setRGC(true);
		
		Stop address = new Stop();    
        address.firstLine = "Semiconductor Dr at Kifer Rd";
        address.city = "Santa Clara";
		address.state = "CA";
		address.country = "USA";
		address.lon = -12199916;
		address.lat = 3737456;
		address.zip = "95051";
		address.label = "";
		
		response.setAddress(address);
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testParseBrowserResponseHttpServletRequestExecutorResponse() {
		try
		{
			this.parseBrowserResponse(request, response);
		}catch(Throwable e)
		{
			Assert.fail();
		}
	}

}
