/**
 * (c) Copyright 2012 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.html.datatypes;

import java.util.HashMap;
import java.util.Map;

/**
 * @TODO	The amenity id that returned from web service is not really expected.
 * 			There is amenity group id which exists in our code. We have to convert the amenity id to amenity group id.
 * @author  xljiang@telenav.cn
 * @version 1.0 2011-8-18
 */

public class AmenityMapping {
	private static Map<Integer, String> amenityGroup = new HashMap<Integer, String>();
	private static Map<String, Integer> mappingMap = new HashMap<String, Integer>();
	
	static{
		amenityGroup.put(1, "Airport Shuttle");
		amenityGroup.put(2, "Social Hour");
		amenityGroup.put(3, "Fitness Center");
		amenityGroup.put(4, "Internet Access");
		amenityGroup.put(5, "Free Local Calls");
		amenityGroup.put(6, "Complimentary Breakfast");
		amenityGroup.put(7, "Pets Allowed");
		amenityGroup.put(8, "Pool");
		amenityGroup.put(9, "Restaurant on-site");
		amenityGroup.put(10, "Kitchen/Kitchenette");
		
		
		mappingMap.put("9000206", 1);
		mappingMap.put("9000255", 2);
		mappingMap.put("9000256", 2);
		mappingMap.put("132", 3);
		mappingMap.put("9000502", 3);
		mappingMap.put("9000508", 4);
		mappingMap.put("9000509", 4);
		mappingMap.put("9000510", 4);
		mappingMap.put("9000511", 4);
		mappingMap.put("9000512", 4);
		mappingMap.put("9000527", 4);
		mappingMap.put("9000528", 4);
		mappingMap.put("9000257", 5);
		mappingMap.put("9000505", 6);
		mappingMap.put("9000520", 6);
		mappingMap.put("9000521", 6);
		mappingMap.put("9000522", 6);
		mappingMap.put("142", 7);
		mappingMap.put("48", 8);
		mappingMap.put("61", 8);
		mappingMap.put("652", 8);
		mappingMap.put("654", 8);
		mappingMap.put("9000530", 8);
		mappingMap.put("9000531", 8);
		mappingMap.put("9000527", 9);
		mappingMap.put("9000132", 10);
		mappingMap.put("9000133", 10);
	}
	/**
	 * TODO	  get the description information of amenity group.
	 * 		  If the groupId not exists in amenityGroup, the method will return "Group does not exist."
	 * @param groupId
	 * @return
	 */
	public static String getAmenityGroupDesc(int groupId){
		String ret = amenityGroup.get(groupId);
		if(ret == null || ret.equals("")){
			ret = "Group does not exist.";
		}
		return ret;
	}
	/**
	 * TODO		Mapping the amenity group id from amenity id returned from web service 
	 * 			If the mappingKey not exists in mappingMap, the method will return -1
	 * @param amenityId
	 * @return
	 */
	public static int getAmenityGroupId(String amenityId){
		return mappingMap.get(amenityId) == null?-1:mappingMap.get(amenityId);
	}
}
