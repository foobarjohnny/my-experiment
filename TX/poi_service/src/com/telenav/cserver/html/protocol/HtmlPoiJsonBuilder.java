/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.html.protocol;

import java.util.List;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.telenav.cserver.framework.html.util.HtmlCommonUtil;
import com.telenav.cserver.html.datatypes.AdsOffer;
import com.telenav.cserver.html.datatypes.MenuItem;

/**
 * @TODO	Convert the data to a JSON object
 * @author  
 * @version 1.0 
 */
public class HtmlPoiJsonBuilder {
	private static HtmlPoiJsonBuilder instance = new HtmlPoiJsonBuilder();
	
	public static HtmlPoiJsonBuilder getInstance()
	{
		return instance;
	}
	/**
	 * TODO		convert deals data
	 * @param offerList
	 * @return
	 * @throws JSONException
	 */
	public JSONObject buildDeals(List<AdsOffer> offerList) throws JSONException
	{
		JSONObject jsonOffer = new JSONObject();
		if(offerList != null)
		{
			jsonOffer.put("success", true);
			JSONArray jsArray = new JSONArray();
			for(AdsOffer offer:offerList)
			{
				JSONObject item = new JSONObject();
				item.put("name", HtmlCommonUtil.getString(offer.getName()));
				item.put("description", HtmlCommonUtil.getString(offer.getDescription()));
				item.put("dealImage", HtmlCommonUtil.getString(offer.getDealImage()));
				jsArray.put(item);
			}
			jsonOffer.put("deals", jsArray.toString());
		}
		else
		{
			jsonOffer.put("success", false);
		}
		
		return jsonOffer;
	}
	/**
	 * TODO		convert menu data
	 * @param offerList
	 * @return
	 * @throws JSONException
	 */
	public String buildMenu(MenuItem menu) throws JSONException
	{
		if(menu == null) return "";
		JSONObject jsonMenu = new JSONObject();
	    //menu
		String menuText = HtmlCommonUtil.getString(menu.getMenuText());
		String menuImage = HtmlCommonUtil.getString(menu.getMenuImage());
		
		if(!"".equals(menuText) || !"".equals(menuImage))
		{
			jsonMenu.put("success", true);
			jsonMenu.put("menu",menuText);
			jsonMenu.put("menuImage",menuImage);
			return jsonMenu.toString();
		}
		else
		{
			return "";
		}
	}
}
