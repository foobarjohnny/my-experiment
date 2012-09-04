package com.telenav.cserver.ac.holder;



import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import com.telenav.cserver.common.resource.ResourceFactory;
import com.telenav.cserver.common.resource.ResourceHolderManager;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.util.CommonUtil;
import com.telenav.cserver.util.TnUtil;

import junit.framework.TestCase;


public class CountryMappingHolderTest extends TestCase{

	 private  CountryMappingHolder countryMappingHolder;
	 
	 @Override
	 protected void setUp() throws Exception
	 {
	        ResourceFactory.init();
	        countryMappingHolder = ResourceHolderManager.getResourceHolder("countryMapping");
	        
	        /*
	         * [Refine the code]
	         * 
	         * couldn't assert like this
	         * 	assertNotNull(countryMappingHolder);
	         *  
	         */

	 }
	 
	 public void testGetCountryMapping()
	 {
		 UserProfile userProfile = new UserProfile();

	     CountryMessages cm = countryMappingHolder.getCountryMapping( userProfile , null);
	     Iterator<List<String>> it = cm.getRegionMap().values().iterator();
	     while( it.hasNext() )
	     {
	    	 System.out.println( it.next().toString() );
	     }

	     assertNotNull(cm);
	 }

}
