package com.telenav.cserver.poidetail.protocol;

import org.json.me.JSONObject;

import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.protocol.ProtocolRequestParser;
import com.telenav.cserver.framework.util.JSONUtil;
import com.telenav.cserver.poidetail.executor.POIDetailsRequest;

public class POIDetailsRequestParser implements ProtocolRequestParser 
{

	@Override
	public ExecutorRequest[] parse(Object object) throws ExecutorException 
	{
		ExecutorRequest[] requests = new ExecutorRequest[1];
		POIDetailsRequest request = new POIDetailsRequest();
		requests[0] = request;
		String jsonString = ((JSONObject)object).toString();
		this.convert2POIDetailsReq(jsonString, request);	
		return requests;
	}
	
	//{\"PoiId\":\"xxxxxxx\"}
	private void convert2POIDetailsReq(String json, POIDetailsRequest request)
	{
		try
		{
			JSONObject object = new JSONObject(json);
			if(object != null && object instanceof JSONObject)
			{
				long poiId = Long.parseLong(JSONUtil.getJSONString(object, "PoiId"));//TODO : cannot hard coding here
				request.setPoiId(poiId);
			}
		}
		catch(Exception ex)
		{
		    ex.printStackTrace();	
		}
	}

}
