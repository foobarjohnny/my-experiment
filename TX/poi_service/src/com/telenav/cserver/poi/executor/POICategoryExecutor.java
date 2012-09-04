/******************************************************************************
 * Copyright (c) 2006 TeleNav, Inc
 * All rights reserved
 * This program is UNPUBLISHED PROPRIETARY property of TeleNav.
 * Only for internal distribution.
 *
 * Created on Apr 22, 2009
 * File name: POICategoryExecutor.java
 * Package name: com.telenav.cserver.poi.handler
 *
 * Purpose:
 *
 *
 * History:
 *  Intial: lwei(lwei@telenav.cn) 1:56:00 PM
 *  Update:
 *******************************************************************************/
package com.telenav.cserver.poi.executor;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.telenav.cserver.backend.datatypes.poicategory.POICategory;
import com.telenav.cserver.backend.poicategory.PoiCategoryRequest;
import com.telenav.cserver.backend.poicategory.PoiCategoryResponse;
import com.telenav.cserver.backend.poicategory.PoiCategoryServiceProxy;
import com.telenav.cserver.common.resource.ResourceHolderManager;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.executor.AbstractExecutor;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.throttling.ThrottlingException;
import com.telenav.cserver.poi.holder.PoiCategoryItem;
import com.telenav.cserver.poi.holder.PoiCategoryManager;
import com.telenav.cserver.poi.holder.PoiLocalParametersCollection;
import com.telenav.cserver.poi.holder.PoiLocalParametersHolder;
import com.telenav.kernel.util.datatypes.TnContext;

/**
 * @author panzhang (panzhang@telenav.cn) 2010/09/08
 */
public class POICategoryExecutor extends AbstractExecutor {
	
	private Logger logger =  Logger.getLogger(POICategoryExecutor.class);
	private final String FOOD_COFFEE = "Food/Coffee";
	private static final String WITH_SUB_CATEGORY = "1";
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.telenav.cserver.framework.executor.AbstractExecutor#doExecute(com
     * .telenav.cserver.framework.executor.ExecutorRequest,
     * com.telenav.cserver.framework.executor.ExecutorResponse,
     * com.telenav.cserver.framework.executor.ExecutorContext)
     */
    @Override
    public void doExecute(ExecutorRequest eReq, ExecutorResponse eRes,
            ExecutorContext context) throws ExecutorException {
        // Get request/response.
    	UserProfile userprofile = eReq.getUserProfile();
    	TnContext tc= context.getTnContext();
        POICategoryRequest req = (POICategoryRequest) eReq;
        POICategoryResponse res = (POICategoryResponse) eRes;
       //TODO didn't find sub category of "Transportation" and the sub category of "Gas" didn't meet requirement of bug TNSIXTWO-1175
       //Please push backend to provide YP62 poi category tree for project TN 62
        JSONObject jo = new JSONObject();
 		PoiLocalParametersHolder poiLocalParametersHolder = ResourceHolderManager.getResourceHolder(PoiLocalParametersHolder.CONTENT_KEY);
		PoiLocalParametersCollection plpc = poiLocalParametersHolder.getPoiLocalParametersCollection(userprofile);
		try {
			//If we can get configuration data for current carrier, get POI categories form the configuration file 
			if(null != plpc && 0 != plpc.getSubCategory().size())
			{
				List<PoiCategoryItem> categoryList = plpc.getSubCategory();
				for(int i = 0; i < categoryList.size(); i ++)
				{
					PoiCategoryItem category = categoryList.get(i);
					if(!WITH_SUB_CATEGORY.equals(category.getIndicator()))
					{
						continue;
					}
					List<PoiCategoryItem> subCategoryList = category.getSubCategory();
					if(null != subCategoryList)
					{
						if(FOOD_COFFEE.equals(category.getName()))
						{
							jo.put(category.getId(),convertToJson(getFoodSubCategory(Integer.parseInt(category.getId()),true,plpc.getPoiFindVersion(),tc)));
						}
						else
						{
							jo.put(category.getId(), convertToJson(subCategoryList));
						}
					}
				}
			}
			//if no configuration for current carrier, we use original logic
			else{
				jo.put("601", convertToJson(ceateFakeTransportationSubCategory()));
				jo.put("50500", convertToJson(ceateFakeGasSubCategory()));
				jo.put("2041", convertToJson(getFoodSubCategory(2041,true,req.getPoiFinderVersion(),tc)));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			logger.error("error occured when converting or putting category to jsonobjcet");
			e.printStackTrace();
		}
		
		res.setCategoryList(jo);
    }
    
    /**
     * 
     * @param id
     * @param isMostPopular
     * @param poiVersion
     * @param tc
     * @return
     */
    private List<PoiCategoryItem> getFoodSubCategory(long id,boolean isMostPopular,String poiVersion,TnContext tc) {
    	PoiCategoryRequest request=new PoiCategoryRequest();
        request.setCategoryId(id);
        request.setKeyWord(null);
        request.setPoiHierarchyId(1);
        request.setStrContext(tc.toContextString());
        request.setVersion(poiVersion);
  
        PoiCategoryResponse resp=null;
        PoiCategoryItem item;
        List<PoiCategoryItem> categoryList= new ArrayList<PoiCategoryItem>();
        try
        {
            resp= PoiCategoryServiceProxy.getInstance().fetchSubPoiCategories(request, tc);

            POICategory[] categories =  resp.getCategories();
            if(categories != null)
            {
            	//add Any
            	item = PoiCategoryManager.getInstance().ceateAnyItem(id);
        		categoryList.add(item);
 
        		if(isMostPopular)
        		{
	            	//add Most Popular
	            	item = PoiCategoryManager.getInstance().ceateMostPopularItem(id);
	        		categoryList.add(item);
        		}
        		
        		PoiCategoryManager.getInstance().convertCategory(categoryList,categories);
            }
         }
         catch (ThrottlingException e)
         {
        	 logger.error("error occured when fetch subpoicategories form xnav!!!");
             e.printStackTrace();
         }
         
         return categoryList;
    }

	// TODO temporary resolution to bug TNSIXTWO-1175 hard code
	private List<PoiCategoryItem> ceateFakeGasSubCategory() {
		List<PoiCategoryItem> categoryList = new ArrayList<PoiCategoryItem>();
		PoiCategoryItem item;

		item = new PoiCategoryItem();
		item.setId("50500");
		item.setName("Any");
		item.setImageId("");
		item.setIndicator("0");
		item.setMostPopular("0");
		categoryList.add(item);

		item = new PoiCategoryItem();
		item.setId("702");
		item.setName("By Price (Regular)");
		item.setImageId("");
		item.setIndicator("0");
		item.setMostPopular("0");
		categoryList.add(item);

		item = new PoiCategoryItem();
		item.setId("703");
		item.setName("By Price (Plus)");
		item.setImageId("");
		item.setIndicator("0");
		item.setMostPopular("0");
		categoryList.add(item);

		item = new PoiCategoryItem();
		item.setId("704");
		item.setName("By Price (Premium)");
		item.setImageId("");
		item.setIndicator("0");
		item.setMostPopular("0");
		categoryList.add(item);

		item = new PoiCategoryItem();
		item.setId("705");
		item.setName("By Price (Diesel)");
		item.setImageId("");
		item.setIndicator("0");
		item.setMostPopular("0");
		categoryList.add(item);

		return categoryList;
	}

	// TODO temporary resolution to bug TNSIXTWO-1175 hard code
	private List<PoiCategoryItem> ceateFakeTransportationSubCategory() {
		List<PoiCategoryItem> categoryList = new ArrayList<PoiCategoryItem>();
		PoiCategoryItem item;

		item = new PoiCategoryItem();
		item.setId("68");
		item.setName("Car Rental");
		item.setImageId("");
		item.setIndicator("0");
		item.setMostPopular("0");
		categoryList.add(item);

		item = new PoiCategoryItem();
		item.setId("601");
		item.setName("Taxi");
		item.setImageId("");
		item.setIndicator("0");
		item.setMostPopular("0");
		categoryList.add(item);

		item = new PoiCategoryItem();
		item.setId("598");
		item.setName("Limo/Shuttle");
		item.setImageId("");
		item.setIndicator("0");
		item.setMostPopular("0");
		categoryList.add(item);

		item = new PoiCategoryItem();
		item.setId("599");
		item.setName("Mass Transit");
		item.setImageId("");
		item.setIndicator("0");
		item.setMostPopular("0");
		categoryList.add(item);

		return categoryList;
	}
	
	/**
	 * Fake MMI sub category for Transportation
	 * @param 
	 * @return
	 */
	//This hard code function is now useless. we move these data to the file of device/PoiLocalPrameters.xml
	@Deprecated
	private List<PoiCategoryItem> ceateFakeTransportationSubCategoryForConfigurationFile() {
		List<PoiCategoryItem> categoryList = new ArrayList<PoiCategoryItem>();
		PoiCategoryItem item;

		item = new PoiCategoryItem();
		item.setId("100040");
		item.setName("Bus Terminals");
		item.setImageId("");
		item.setIndicator("0");
		item.setMostPopular("0");
		categoryList.add(item);

		item = new PoiCategoryItem();
		item.setId("100041");
		item.setName("Railway Stations");
		item.setImageId("");
		item.setIndicator("0");
		item.setMostPopular("0");
		categoryList.add(item);

		item = new PoiCategoryItem();
		item.setId("100039");
		item.setName("Seaports");
		item.setImageId("");
		item.setIndicator("0");
		item.setMostPopular("0");
		categoryList.add(item);

		item = new PoiCategoryItem();
		item.setId("100042");
		item.setName("Taxi Services& Parking");
		item.setImageId("");
		item.setIndicator("0");
		item.setMostPopular("0");
		categoryList.add(item);

		return categoryList;
	}
	
	/**
	 * 
	 * @param categoryList
	 * @return
	 */
	private JSONArray convertToJson(List<PoiCategoryItem> categoryList)
	{
		JSONArray joList = new JSONArray();
		JSONObject jo;
		String name;
		for(PoiCategoryItem item:categoryList)
		{
			jo = new JSONObject();
			try {
				jo.put("id", item.getId());
				//jo.put("name", item.getName());
				name = "poi.sub.category." + item.getId();
				if("1".equals(item.getMostPopular()) && ("2041".equals(item.getId()) || "226".equals(item.getId())))
				{
					name += "_" +item.getMostPopular();
				}
				jo.put("name", name);
				jo.put("mostPopular", item.getMostPopular());
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			joList.put(jo);
		}
		
		return joList;
	}

}