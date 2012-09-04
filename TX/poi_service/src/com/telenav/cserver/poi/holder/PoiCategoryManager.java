package com.telenav.cserver.poi.holder;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.telenav.cserver.backend.datatypes.poicategory.Category;
import com.telenav.cserver.backend.datatypes.poicategory.POICategory;
import com.telenav.cserver.backend.poicategory.PoiCategoryRequest;
import com.telenav.cserver.backend.poicategory.PoiCategoryResponse;
import com.telenav.cserver.backend.poicategory.PoiCategoryServiceProxy;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.cserver.poi.struts.util.PoiUtil;
import com.telenav.cserver.util.TnUtil;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.tnbrowser.util.DataHandler;

import edu.emory.mathcs.backport.java.util.Collections;

public class PoiCategoryManager {
	
	private Logger logger =  Logger.getLogger(PoiCategoryManager.class);
	
	private static PoiCategoryManager instance= new PoiCategoryManager();
	
    public static PoiCategoryManager getInstance()
    {
        return instance;
    }
    
    /**
     * 
     * @param categoryList
     * @param categories
     */
    public void convertCategory(List<PoiCategoryItem> categoryList,POICategory[] categories)
    {
    	PoiCategoryItem item;
    	String id = "";
    	for(int i=0;i<categories.length;i++)
    	{
    		item = new PoiCategoryItem();
    		
    		id = String.valueOf(categories[i].getId());

    		item.setId(id);
    		item.setName(categories[i].getLabel());
    		item.setImageId("poiCategory" + id);
    		item.setIndicator("0");
    		if(categories[i].getSubCategories() != null && categories[i].getSubCategories().length > 0 && isThereAnyOneNeed2Display(categories[i].getSubCategories()))	
    		{
    			item.setIndicator("1");
    		}
    		if(categories[i].isPopularSearch())
    		{
    			item.setMostPopular("1");
    		}
    		else
    		{
    			item.setMostPopular("0");
    		}
    		categoryList.add(item);
    	}    	
    }
    
	public List<PoiCategoryItem> getPoiHotList(TnContext tc,DataHandler handler)
	{
		List<PoiCategoryItem> categoryList= new ArrayList<PoiCategoryItem>();
		PoiCategoryServiceProxy service= PoiCategoryServiceProxy.getInstance();
		
		PoiCategoryRequest request=new PoiCategoryRequest();
		request.setCategoryId(0);
		request.setKeyWord(null);
		request.setPoiHierarchyId(2);
		request.setStrContext("");
		request.setVersion(TnUtil.getPoiFinderVersion(PoiUtil.getUserProfile(handler)));
		PoiCategoryResponse resp=null;
		try
		{
			resp=service.fetchPoiCategories(request, tc);
			POICategory[] categories =  resp.getCategories();
	        if(categories != null)
	        {
	        	PoiCategoryManager.getInstance().convertCategory(categoryList,categories);
	        }  
		}
	    catch (ThrottlingException e)
	    {
	    	logger.error("error occured when fetch poi categories form xnav!!!");
	        e.printStackTrace();
	    }
	    
	    Collections.sort(categoryList);
	    categoryList.add(PoiCategoryManager.getInstance().ceateMoreItem());
	    
	    return categoryList;
	}
	
    public static boolean isThereAnyOneNeed2Display(Category[] categoies)
    {
        for(Category ca:categoies)
        {
            if(ca instanceof POICategory && ((POICategory)ca).getDisplaySequence()>0)
            {
                return true;
            }
        }
        return false;
    }
	public PoiCategoryItem ceateAnyItem(long id)
	{
		PoiCategoryItem item = new PoiCategoryItem();
		item.setId(String.valueOf(id));
		item.setName("Any");//get this value from config files
		item.setImageId("");
		item.setIndicator("0");
		item.setMostPopular("0");
		
		return item;
	}
	
	public PoiCategoryItem ceateMostPopularItem(long id)
	{
		PoiCategoryItem item = new PoiCategoryItem();
		item.setId(String.valueOf(id));
		item.setName("Most Popular");//get this value from config files
		item.setImageId("");
		item.setIndicator("0");
		item.setMostPopular("1");
		
		return item;
	}
	
	public PoiCategoryItem ceateMoreItem()
	{
		PoiCategoryItem item = new PoiCategoryItem();
		item = new PoiCategoryItem();
		item.setId("0");
		item.setName("More");
		item.setImageId("poiCategoryMore");
		item.setIndicator("2");
		item.setMostPopular("0");
		
		return item;
	}
}
