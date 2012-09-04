package com.telenav.cserver.poi.protocol;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.me.JSONArray;
import org.json.me.JSONObject;

import com.telenav.cserver.util.TestUtil;
import org.junit.Test;

public class PoiDetailsSearchTest 
{
	public static void searchPoiDetails() throws Exception
	{
		JSONObject request = new JSONObject();
		
		JSONArray array = new JSONArray();
//		array.put(TestUtil.getJSONMandatory());
		JSONObject sub = new JSONObject();
		
		JSONObject objPoiDetails = new JSONObject();
		objPoiDetails.put("PoiId", "3000530053");
		sub.put("PoiDetails", objPoiDetails);
		
		array.put(sub);
		
		request.put("JSONRequest", array);
		
		URL url = new URL(TestUtil.poiCServerUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		if(conn.getResponseCode() == 200){
			System.out.println(conn);
/*			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			OutputStream output = conn.getOutputStream();
			System.out.println(request.toString());
			output.write(request.toString().getBytes());
			output.flush();
			output.close();*/
	
			
			System.out.println("try to get input stream");
			BufferedInputStream is = new BufferedInputStream(conn
					.getInputStream());
	
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int ch = -1;
			while((ch = is.read()) != -1)
			{
				baos.write(ch);
			}
			byte[] resp = baos.toByteArray();
			String strResp = new String(resp);
			System.out.println("resp:" + strResp);
		}
	}
	
	@Test
	public void testSearchPoiDetails() throws Exception{
		PoiDetailsSearchTest.searchPoiDetails();
	}
	
/*	public static void main(String[] args) throws Exception
	{
	    PoiDetailsSearchTest.searchPoiDetails();
	}*/
}
