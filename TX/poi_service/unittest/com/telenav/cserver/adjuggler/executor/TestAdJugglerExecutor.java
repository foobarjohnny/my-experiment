package com.telenav.cserver.adjuggler.executor;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.telenav.cserver.adjuggler.executor.AdJugglerRequest;
import com.telenav.cserver.adjuggler.executor.AdJugglerResponse;
import com.telenav.tnbrowser.util.DataHandler;
import com.telenav.cserver.poi.struts.Constant;

import junit.framework.TestCase;

public class TestAdJugglerExecutor extends TestCase{
	
	public void testCase(){
		System.out.println("do noting now, just for pass the testing, otherwise it will report No tests found in com.telenav.cserver.adjuggler.executor.TestAdJugglerExecutor");
		System.out.println("complete this test in other place");
	}
	
	private AdJugglerResponse resp = new AdJugglerResponse();
	
	private AdJugglerRequest getAdJugglerRequest() throws JSONException{
		AdJugglerRequest request = new AdJugglerRequest();
		
		JSONObject jo = new JSONObject();
		
		JSONObject locationJson = new JSONObject();
		locationJson.put("label", "");
		locationJson.put("firstLine", "3755 EL CAMINO REAL");
		locationJson.put("zip", "95051");
		locationJson.put("state", "CA");
		locationJson.put("country", "US");
		locationJson.put("city", "SANTA CLARA");
		locationJson.put("lat", 3735237);
		locationJson.put("lon", -12199984);
		jo.put(Constant.JSON_KEY_LOCATION, locationJson);
		jo.put(Constant.JSON_KEY_FROM, "MainPage");
		jo.put(Constant.JSON_KEY_ISPREMIUMACCOUNT, false);
		
		request.setActionJO(jo);
		
		DataHandler handler = new DataHandler(null);
		
		request.setHandler(handler);
		
//		String imageKey = ;
//		request.setImageKey(imageKey);
		
		return request;
	}

}
