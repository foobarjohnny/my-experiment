package com.telenav.cserver.poidetail.protocol;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.framework.executor.protocol.ProtocolRequestParser;
import com.telenav.cserver.framework.executor.protocol.ProtocolResponseFormatter;
import com.telenav.cserver.poidetail.executor.POIDetailsResponse;

public class POIDetailsResponseFormatter implements ProtocolResponseFormatter
{

	@Override
	//{\"Status\":\"xxx\",\"Message\":\"xxxx\",\"PoiDetailsResp\":{\"PoiId\":\"xxx\",\"BussinessHour\":\"xxx\",,,}}
	public void format(Object formatTarget, ExecutorResponse[] responses) throws ExecutorException
	{
		POIDetailsResponse response = (POIDetailsResponse)responses[0];
		JSONObject resp = (JSONObject)formatTarget;
		try
		{
			//TODO : remove hard coding
			//JSONObject resp = new JSONObject();
			resp.put("Message", response.getErrorMessage());
			resp.put("Status", Integer.toString(response.getStatus()));
			if(response.getStatus() == ExecutorResponse.STATUS_OK)
			{
				resp.put("PoiDetailsResp", this.convert2JSONString(response));
			}
			//json.put("ServerResponse", resp);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private JSONObject convert2JSONString(POIDetailsResponse response)
	{
		JSONObject detailsResp = new JSONObject();
		try
		{
			//TODO : remove hard coding		
			detailsResp.put("PoiId", Long.toString(response.getPoiId()));
			detailsResp.put("BusinessHours", response.getBusinessHours());
			detailsResp.put("BusinessHoursNote", response.getBusinessHoursNote());
			detailsResp.put("Description", response.getDescription());
			detailsResp.put("PriceRange", response.getPriceRange());
			detailsResp.put("OlsonTimezone", response.getOlsonTimezone());
			detailsResp.put("LogoId", response.getLogoId());
			detailsResp.put("MediaServerKey", response.getMediaServerKey());
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}
		return detailsResp;
	}
	

}
