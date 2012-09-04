package com.telenav.cserver.ac.protocol.v20;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.telenav.cserver.ac.executor.v20.ValidateAirportResponse;
import com.telenav.cserver.backend.datatypes.Address;
import com.telenav.cserver.backend.datatypes.AddressDataConverter;
import com.telenav.cserver.backend.datatypes.TnPoi;
import com.telenav.cserver.browser.framework.protocol.BrowserProtocolResponseFormatter;
import com.telenav.cserver.entity.AirportPoi;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.util.CommonUtil;
import com.telenav.j2me.datatypes.TxNode;

public class ValidateAirportResponseFormatter extends
        BrowserProtocolResponseFormatter {
	
	private Logger logger =  Logger.getLogger(ValidateAddressResponseFormatter.class);
    public static final double DEGREE_MULTIPLIER = 1.e5; // 1e-5 deg units

    @SuppressWarnings("unchecked")
	public void parseBrowserResponse(HttpServletRequest httpRequest,
            ExecutorResponse executorResponse) {
        ValidateAirportResponse response = (ValidateAirportResponse) executorResponse;
                
        JSONArray ja = new JSONArray();
        List<TnPoi> pois = response.getAirportList();
        TxNode respNode = new TxNode();
        if ((pois != null) && (pois.size() > 0)) {
            List<AirportPoi> v = new ArrayList<AirportPoi>();
            for (int i = 0; i < pois.size(); i++) {
            	TnPoi airport = pois.get(i);
                AirportPoi ap = new AirportPoi(airport, 0D, 0D);
                v.add(ap);
            }
            Collections.sort(v);
            for (int i = 0; i < v.size(); i++) {
                AirportPoi ap = v.get(i);
                JSONObject jo = convertAddressToJSONObject(ap);
                ja.put(jo);
            }
            
            respNode.addValue(1);
            respNode.addMsg(ja.toString());
        }else{
            respNode.addValue(0);
        }
        
        httpRequest.setAttribute("node", respNode);

        // TODO save audio

    }
    
    public JSONObject convertAddressToJSONObject(AirportPoi ap) {
		JSONObject jo = new JSONObject();
		try {
			Address addr = ap.getAddress();
			jo.put("firstLine", ap.getPoi().getBrandName());// convertNull(street.getFirstLine()));
			jo.put("label", ap.getPoi().getFeatureName());
			HashMap<String, String> addressV40Map = addr.getLines();
			jo.put("lastLine",
					CommonUtil.convertNull(addressV40Map
							.get(com.telenav.cserver.backend.datatypes.AddressFormatConstants.LAST_LINE)));
			jo.put("city", AddressDataConverter.convertNull(addr.getCity()));
			jo.put("state", AddressDataConverter.convertNull(addr.getState()));
			jo.put("postalCode",
					AddressDataConverter.convertNull(addr.getPostalCode()));
			jo.put("street1",
					AddressDataConverter.convertNull(addr.getStreetName()));
			jo.put("street2",
					AddressDataConverter.convertNull(addr.getCrossStreetName()));
			jo.put("county", AddressDataConverter.convertNull(addr.getCounty()));

			jo.put("lat", AddressDataConverter.convertToDM5(addr.getLatitude()));
			jo.put("lon", AddressDataConverter.convertToDM5(addr.getLongitude()));
			jo.put("country",
					AddressDataConverter.convertNull(addr.getCountry()));

			jo.put("suite", AddressDataConverter.convertNull(addr.getSuite()));
			jo.put("sublocality",
					AddressDataConverter.convertNull(addr.getSublocality()));
			jo.put("locality",
					AddressDataConverter.convertNull(addr.getLocality()));
			jo.put("locale", AddressDataConverter.convertNull(addr.getLocale()));
			jo.put("subStreet",
					AddressDataConverter.convertNull(addr.getSubStreet()));
			jo.put("buildingName",
					AddressDataConverter.convertNull(addr.getBuildingName()));
			jo.put("addressId",
					AddressDataConverter.convertNull(addr.getAddressId()));

			jo.put("isGeocoded", true);
			String airportMsg = ap.getPoi().getFeatureName() + ": "
					+ ap.getPoi().getBrandName();
			jo.put("airportMsg", airportMsg);

			String streetName = addr.getStreetName();
			String houseNumber = addr.getDoor();
			if ((streetName == null || streetName.length() == 0) && (houseNumber == null || houseNumber.length() == 0))
			{
				// ACE 4.0, if the streetName and houseNumber is empty, put the brandName(firstLine) to streetName, make sure houseNumber is
				// null also, else the firstLine will missing after sync with XNAV
				jo.put("streetName", AddressDataConverter.convertNull(ap.getPoi().getBrandName()));
				jo.put("houseNumber", AddressDataConverter.convertNull("")); // make sure houseNumber is empty
			}
			else
			{
				jo.put("streetName", AddressDataConverter.convertNull(addr.getStreetName()));
				jo.put("houseNumber", AddressDataConverter.convertNull(addr.getDoor()));
			}
		} catch (JSONException e) {
			e.printStackTrace();
        	logger.error("cause:"+e.getCause()+",message:"+e.getMessage());
		}
		return jo;
	}
}
