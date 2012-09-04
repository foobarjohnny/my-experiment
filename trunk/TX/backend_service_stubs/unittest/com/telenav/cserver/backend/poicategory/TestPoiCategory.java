/**
 * (c) Copyright 2010 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.backend.poicategory;

import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.kernel.util.datatypes.TnContext;

import junit.framework.TestCase;

public class TestPoiCategory extends TestCase
{
    
   public void testPoiCategory() {
       PoiCategoryServiceProxy service=PoiCategoryServiceProxy.getInstance();
       // server initial success
       assertEquals("OK", PoiCategoryServiceProxy.response_status);
       
       TnContext tc = new TnContext();
       tc.addProperty(TnContext.PROP_POI_DATASET, "YPC");
       tc.addProperty("dataset", "Navteq");
       
       PoiCategoryRequest request=new PoiCategoryRequest();
       request.setCategoryId(0);
       request.setKeyWord(null);
       request.setPoiHierarchyId(1);
       request.setStrContext("");
       request.setVersion("YP50");
       System.err.println(request.toString());
       PoiCategoryResponse resp=null;
       try
       {
           resp=service.fetchPoiCategories(request, tc);
           System.err.println(resp.toString());
        }
        catch (ThrottlingException e)
        {
            e.printStackTrace();
        }
      
       assertEquals("OK", resp.getStatusCode());
   }
   
   public void testSubPoiCategory() {
       PoiCategoryServiceProxy service=PoiCategoryServiceProxy.getInstance();
       // server initial success
       assertEquals("OK", PoiCategoryServiceProxy.response_status);
       
       TnContext tc = new TnContext();
       tc.addProperty(TnContext.PROP_POI_DATASET, "YPC");
       tc.addProperty("dataset", "Navteq");
       
       PoiCategoryRequest request=new PoiCategoryRequest();
       request.setCategoryId(5800);
       request.setKeyWord(null);
       request.setPoiHierarchyId(1);
       request.setStrContext("");
       request.setVersion("YP50");
       System.err.println(request.toString());
       PoiCategoryResponse resp=null;
       try
       {
           resp=service.fetchSubPoiCategories(request, tc);
           System.err.println("sub category---"+resp.toString());
        }
        catch (ThrottlingException e)
        {
            e.printStackTrace();
        }
      
       assertEquals("OK", resp.getStatusCode());
   }
}
