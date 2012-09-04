/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.poi.holder;

import java.util.ArrayList;
import java.util.List;

import com.telenav.cserver.common.resource.ResourceHolderManager;
import com.telenav.cserver.framework.UserProfile;




/**
 * PoiCategoryCollection
 * @author kwwang
 *
 */
public class PoiCategoryCollection {
	
	/**
	 * Use spring feature to do a magic converting here, but I don't think this is a 
	 * good idea to add the parsing strategy here.
	 * @param poiCategories
	 */
	
	public List<PoiCategoryItem> getPoiHotList(UserProfile userprofile)
	{
		
		PoiLocalParametersHolder poiLocalParametersHolder = ResourceHolderManager.getResourceHolder(PoiLocalParametersHolder.CONTENT_KEY);
		PoiLocalParametersCollection plpc = poiLocalParametersHolder.getPoiLocalParametersCollection(userprofile);
		
		//add new configuration file, get sub category from the file first
		if(null != plpc)
		{
			if(!plpc.getSubCategory().isEmpty())
			{
				return plpc.getSubCategory();
			}
		}
		//if can't get the sub category from the configuration file, return the original ones
		//#1
		List<PoiCategoryItem> hotList= new ArrayList<PoiCategoryItem>();
		PoiCategoryItem item = new PoiCategoryItem();
		item.setId("374");
		item.setName("ATM");
		item.setImageId("poiCategory374");
		item.setIndicator("0");
		item.setMostPopular("0");
		hotList.add(item);
		//#2
		item = new PoiCategoryItem();
		item.setId("2041");
		item.setName("Food/Coffee");
		item.setImageId("poiCategory2041");
		item.setIndicator("1");
		item.setMostPopular("1");
		hotList.add(item);
		//#3
		item = new PoiCategoryItem();
		item.setId("50500");
		item.setName("Gas");
		item.setImageId("poiCategory50500");
		item.setIndicator("1");
		item.setMostPopular("0");
		hotList.add(item);
		//#4
		item = new PoiCategoryItem();
		item.setId("221");
		item.setName("Grocery");
		item.setImageId("poiCategory221");
		item.setIndicator("0");
		item.setMostPopular("0");
		hotList.add(item);
		//#5
		item = new PoiCategoryItem();
		item.setId("595");
		item.setName("Lodging");
		item.setImageId("poiCategory595");
		item.setIndicator("0");
		item.setMostPopular("0");
		hotList.add(item);
		//#6
		item = new PoiCategoryItem();
		item.setId("181");
		item.setName("Theaters");
		item.setImageId("poiCategory181");
		item.setIndicator("0");
		item.setMostPopular("0");
		hotList.add(item);
		//#7
		item = new PoiCategoryItem();
		item.setId("163");
		item.setName("Nightlife");
		item.setImageId("poiCategory163");
		item.setIndicator("0");
		item.setMostPopular("0");
		hotList.add(item);	
		//#8
		item = new PoiCategoryItem();
		item.setId("600");
		item.setName("Parking");
		item.setImageId("poiCategory600");
		item.setIndicator("0");
		item.setMostPopular("0");
		hotList.add(item);
		//#9
		item = new PoiCategoryItem();
		item.setId("641");
		item.setName("Shopping");
		item.setImageId("poiCategory641");
		item.setIndicator("0");
		item.setMostPopular("0");
		hotList.add(item);
		//#10
		//TODO didn't find poi category id of "Transportation", please update ID and rename image id and update image name.
		item = new PoiCategoryItem();
		item.setId("601");
		item.setName("Transportation");
		item.setImageId("poiCategory601");
		item.setIndicator("1");
		item.setMostPopular("0");
		hotList.add(item);
		//#11
		item = new PoiCategoryItem();
		item.setId("730");
		item.setName("Wi-Fi");
		item.setImageId("poiCategory730");
		item.setIndicator("0");
		item.setMostPopular("0");
		hotList.add(item);
		//#12
		item = new PoiCategoryItem();
		item.setId("0");
		item.setName("More");
		item.setImageId("poiCategoryMore");
		item.setIndicator("2");
		item.setMostPopular("0");
		hotList.add(item);
		

		// //#2
		// item = new PoiCategoryItem();
		// item.setId("68");
		// item.setName("Car Rental");
		// item.setImageId("poiCategory68");
		// item.setIndicator("0");
		// item.setMostPopular("0");
		// hotList.add(item);
		// //#3
		// item = new PoiCategoryItem();
		// item.setId("446");
		// item.setName("Dept. Stores");
		// item.setImageId("poiCategory446");
		// item.setIndicator("0");
		// item.setMostPopular("0");
		// hotList.add(item);
		// //#6
		// item = new PoiCategoryItem();
		// item.setId("49");
		// item.setName("Gas Stations");
		// item.setImageId("poiCategory49");
		// item.setIndicator("0");
		// item.setMostPopular("0");
		// hotList.add(item);
		// //#10
		// item = new PoiCategoryItem();
		// item.setId("287");
		// item.setName("Medical");
		// item.setImageId("poiCategory287");
		// item.setIndicator("0");
		// item.setMostPopular("0");
		// hotList.add(item);
		
		
		
		
		return hotList;
	}
	
	//after refine, this function is useless
	@Deprecated
	public List<PoiCategoryItem> getPoiHotListForMMI()
	{
		List<PoiCategoryItem> hotList= new ArrayList<PoiCategoryItem>();
		//#1
		PoiCategoryItem item = new PoiCategoryItem();
		item.setId("1026");
		item.setName("ATM");
		item.setImageId("poiCategory1026");
		item.setIndicator("0");
		item.setMostPopular("0");
		hotList.add(item);
		//#2
		item = new PoiCategoryItem();
		item.setId("226");
		item.setName("Food/Coffee");
		item.setImageId("poiCategory226");
		item.setIndicator("1");
		item.setMostPopular("1");
		hotList.add(item);
		//#3
		item = new PoiCategoryItem();
		item.setId("811");
		item.setName("Petrol");
		item.setImageId("poiCategory811");
		item.setIndicator("0");
		item.setMostPopular("0");
		hotList.add(item);
		//#4
		item = new PoiCategoryItem();
		item.setId("100031");
		item.setName("Market");
		item.setImageId("poiCategory100031");
		item.setIndicator("0");
		item.setMostPopular("0");
		hotList.add(item);
		//#5
		item = new PoiCategoryItem();
		item.setId("5850");
		item.setName("Lodging");
		item.setImageId("poiCategory5850");
		item.setIndicator("0");
		item.setMostPopular("0");
		hotList.add(item);
		//#6
		item = new PoiCategoryItem();
		item.setId("181");
		item.setName("Movies");
		item.setImageId("poiCategory181");
		item.setIndicator("0");
		item.setMostPopular("0");
		hotList.add(item);
		//#7
		item = new PoiCategoryItem();
		item.setId("165");
		item.setName("Nightlife");
		item.setImageId("poiCategory165");
		item.setIndicator("0");
		item.setMostPopular("0");
		hotList.add(item);	
		//#8
		item = new PoiCategoryItem();
		item.setId("600");
		item.setName("Parking");
		item.setImageId("poiCategory600");
		item.setIndicator("0");
		item.setMostPopular("0");
		hotList.add(item);
		//#9
		item = new PoiCategoryItem();
		item.setId("4090");
		item.setName("Shopping");
		item.setImageId("poiCategory4090");
		item.setIndicator("0");
		item.setMostPopular("0");
		hotList.add(item);
		//#10
		item = new PoiCategoryItem();
		item.setId("1005");
		item.setName("Transport");
		item.setImageId("poiCategory1005");
		item.setIndicator("1");
		item.setMostPopular("0");
		hotList.add(item);
		//#11
		item = new PoiCategoryItem();
		item.setId("100019");
		item.setName("Religion");
		item.setImageId("poiCategory100019");
		item.setIndicator("0");
		item.setMostPopular("0");
		hotList.add(item);
		//#12
		item = new PoiCategoryItem();
		item.setId("0");
		item.setName("More");
		item.setImageId("poiCategoryMore");
		item.setIndicator("2");
		item.setMostPopular("0");
		hotList.add(item);
				
		return hotList;
	}
}
