package com.telenav.cserver.ugc.struts.action;


import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.backend.ace.CountryUtils;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.cserver.util.TestUtil;
import com.telenav.kernel.util.datatypes.TnContext;

public class PoiFinderManagerTest {

	TnContext tnContext = null;
	PoiFinderManager poiFinderManager = new PoiFinderManager();
	
	int count = 0;
	String requestCountry = "USA";
	
	@Before
	public void setUp() throws Exception {
		count = 50;
		requestCountry = CountryUtils.US.getIso3Name();
		tnContext = TestUtil.getTnContext();
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testPoiFinderManager(){
		
		try {
			List<String> brands = poiFinderManager.searchPOIBrandNames(requestCountry, count, tnContext);
			
			Assert.assertNotNull(brands);
			if(null != brands){
				Assert.assertEquals(count, brands.size());
			}
			
		} catch (ThrottlingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
