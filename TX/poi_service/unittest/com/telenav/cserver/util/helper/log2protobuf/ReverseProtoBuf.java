package com.telenav.cserver.util.helper.log2protobuf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ReverseProtoBuf{
	
		Map<String, String> common = null;
		Map<String,ReverseProtoBuf> child = null;
		Map<String,List<ReverseProtoBuf>> array = null;


		public void addElement(String key, String value)
		{
			if(common == null)
			{
				common = new HashMap<String, String>();
			}
			common.put(key, value);
		}	
		
		public Map<String, String> getElement()
		{
			return this.common;
		}
		
		public void addArray(String key, ReverseProtoBuf element)
		{
			if(array == null)
			{
				array = new HashMap<String, List<ReverseProtoBuf>>();
			}
			
			Set<String> keySet = array.keySet();
			Iterator<String> itr = keySet.iterator();

			boolean exist = false;
			while(itr.hasNext())
			{
				String k = (String)itr.next();
				if(key.equals(k))
				{
					array.get(k).add(element);
					exist = true;
					break;
				}
			}
			
			if(!exist)
			{
				List<ReverseProtoBuf> lists = new ArrayList<ReverseProtoBuf>();
				lists.add(element);
				array.put(key, lists);
			}
		}
		
		public Map<String, List<ReverseProtoBuf>> getArray()
		{
			return array;
		}
		
		
		public void addChild(String key, ReverseProtoBuf child)
		{
			if(this.child == null)
			{
				this.child = new HashMap<String, ReverseProtoBuf>();
			}
			this.child.put(key, child);
		}
		
		public Map<String,ReverseProtoBuf> getChild()
		{
			return this.child;
		}
}

/*
ProtoLoginResp
{
    status: 0
    errorMessage: 
    userId: %E0NI%5E%2Fg%7E%16
    pin: 4900
    eqPin: 1311
    accountInfo: 
               {
                   accountStatus: 10
                   accountType: ATT_MAPS
                   featureList: [
                                  {
                                      key: FE_NAV_SPEED_LIMIT
                                      value: 1
                                  },
                                  {
                                      key: FE_TRAFFIC
                                      value: 1
                                  },
                                  {
                                      key: FE_DYNAMIC_ROUTE
                                      value: 1
                                  },
                                  {
                                      key: FE_ROUTE_PLANNING_MULTI_ROUTE
                                      value: 1
                                  },
                                  {
                                      key: FE_NAV_LANE_ASSIST
                                      value: 1
                                  },
                                  {
                                      key: FE_NAV_JUNCTION_VIEW
                                      value: 1
                                  },
                                  {
                                      key: FE_MAP_LAYER_SATELLITE
                                      value: 2
                                  },
                                  {
                                      key: FE_NAV_TRAFFIC_CAMERA
                                      value: 1
                                  },
                                  {
                                      key: FE_MAP_LAYER_CAMERA
                                      value: 1
                                  },
                                  {
                                      key: FE_MAP_LAYER_TRAFFIC
                                      value: 1
                                  },
                                  {
                                      key: FE_DSR
                                      value: 1
                                  },
                                  {
                                      key: FE_NAV_TRAFFIC
                                      value: 1
                                  }
                                 ]
                   needPurchase: true
               }
    ptn: %82c6%9D%5B%A9%86Y%22%AF%90%DC%21%EB%9F%21
    socCodeOfCurrentProduct: null
}
	 */
	
	// abstract the data
	
/*	public static final int common_type = 0;
	public static final int array_type = 1;
	public static final int child_type = 2;
*/	
