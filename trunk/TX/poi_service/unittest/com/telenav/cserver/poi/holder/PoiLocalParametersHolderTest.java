package com.telenav.cserver.poi.holder;

import java.util.List;

import com.telenav.cserver.common.resource.ResourceHolderManager;
import com.telenav.cserver.framework.UserProfile;
import junit.framework.*;

public class PoiLocalParametersHolderTest extends TestCase{
	
	public void testGetPoiLocalParametersCollection()
	{	
		UserProfile userprofile = new UserProfile();
		
		userprofile.setCarrier("MMI");
		PoiLocalParametersHolder poiLocalParametersHolder = ResourceHolderManager.getResourceHolder("PoiLocalParameters");
		PoiLocalParametersCollection plpc = poiLocalParametersHolder.getPoiLocalParametersCollection(userprofile);
		
		assertNotNull(plpc);
		assertEquals("MMI", plpc.getCarrierName());
		assertEquals("TN_MMI_2", plpc.getPoiFindVersion());
		List<PoiCategoryItem> list  = plpc.getSubCategory();
		PoiCategoryItem pci = new PoiCategoryItem();
		for(int i = 0; i < list.size(); i ++)
		{
			pci = list.get(i);
			if(pci.getId().equals("100040"))
			{
				List<PoiCategoryItem> subList  = pci.getSubCategory();
				assertEquals(4, subList.size());
			}
		}
		assertEquals(12, ((List<PoiCategoryItem>)plpc.getSubCategory()).size());
		
		
		userprofile.setCarrier("ATT");
		poiLocalParametersHolder = ResourceHolderManager.getResourceHolder("PoiLocalParameters");
		plpc = poiLocalParametersHolder.getPoiLocalParametersCollection(userprofile);
		
		assertNull(plpc);
	}

}
