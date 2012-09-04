package com.telenav.cserver.poi.executor;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.telenav.cserver.TestUtil;
import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.poi.holder.PoiCategoryItem;

import junit.framework.TestCase;

public class TestPOICategoryExecutor extends TestCase{
	private POICategoryResponse resp = new POICategoryResponse();
	
	public void testDoExecute() throws ExecutorException {
		POICategoryExecutor excutor = new POICategoryExecutor();
		excutor.doExecute(getPOICategoryRequest(""), resp, TestUtil
				.getExecutorContext());
	}
	
	public void testDoExecuteMMI() throws ExecutorException, JSONException
	{
		POICategoryExecutor excutor = new POICategoryExecutor();
		excutor.doExecute(getPOICategoryRequest("MMI"), resp, TestUtil
				.getExecutorContextForTN64("MMI"));
		JSONObject jo = resp.getCategoryList();
		JSONArray item = (JSONArray)jo.get("100040");
		assertEquals(4, item.length());
		item = (JSONArray)jo.get("226");
		assertEquals(8, item.length());
		
	}
	
	public void testDoExecuteVIVO() throws ExecutorException, JSONException
	{
		POICategoryExecutor excutor = new POICategoryExecutor();
		excutor.doExecute(getPOICategoryRequest("VIVOGSM"), resp, TestUtil
				.getExecutorContextForTN64("VIVOGSM"));
		JSONObject jo = resp.getCategoryList();
		JSONArray item = (JSONArray)jo.get("601");
		assertEquals(4, item.length());
		item = (JSONArray)jo.get("2041");
		assertEquals(21, item.length());
		
	}
	
	private POICategoryRequest getPOICategoryRequest(String carrier){
		POICategoryRequest request = new POICategoryRequest();
		request.setPoiFinderVersion("YP50");
		request.setMostPopular(false);
		request.setName("");
		if(!carrier.equals(""))
		{	
			request.setUserProfile(TestUtil.getUserProfileForTN64(carrier));
		}
		else
		{
			request.setUserProfile(TestUtil.getUserProfile());
		}
		
		return request;
	}

}
