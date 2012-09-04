package com.telenav.cserver.ac.protocol.v20;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.telenav.cserver.ac.executor.v20.ValidateAddressResponseACEWS;
import com.telenav.cserver.backend.datatypes.AddressDataConverter;
import com.telenav.cserver.backend.datatypes.ace.GeoCodedAddress;
import com.telenav.cserver.browser.framework.protocol.BrowserProtocolResponseFormatter;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.util.CommonUtil;
import com.telenav.j2me.datatypes.TxNode;

public class ValidateAddressResponseFormatterACEWS extends
        BrowserProtocolResponseFormatter {
	
	private Logger logger = Logger.getLogger(ValidateAddressResponseFormatterACEWS.class);
	
    public static final double DEGREE_MULTIPLIER = 1.e5; // 1e-5 deg units

    public void parseBrowserResponse(HttpServletRequest httpRequest,
            ExecutorResponse executorResponse) {
        ValidateAddressResponseACEWS response = (ValidateAddressResponseACEWS) executorResponse;

        List<GeoCodedAddress> geoCodedAddressList = response.getAddresses();
        JSONArray ja = new JSONArray();
		if (geoCodedAddressList != null) {
            for (GeoCodedAddress geoCodedAddress : geoCodedAddressList) {
                try
                {
                	ja.put(convertAddressToJSONObject(geoCodedAddress, response));
                } 
                catch (JSONException e) 
                {
                    // TODO Auto-generated catch block
                	logger.error("cause:"+e.getCause()+",message:"+e.getMessage());
					e.printStackTrace();
                    continue;
                }

            }
        }
        TxNode node = new TxNode();
        if (ja.length() > 0) {
            node.addValue(1);
            node.addMsg(ja.toString());
        } else {
            node.addValue(0);
        }
        node.addValue(response.getGeoCodeStatusCode());
        httpRequest.setAttribute("node", node);

        // TODO save audio

    }

	public JSONObject convertAddressToJSONObject(
			GeoCodedAddress geoCodedAddress,
			ValidateAddressResponseACEWS response) throws JSONException {
		Map<String, String> addressV40Map = geoCodedAddress.getLines();
		JSONObject jo = new JSONObject();

		jo.put("firstLine",
				CommonUtil.convertNull(addressV40Map
						.get(com.telenav.cserver.backend.datatypes.AddressFormatConstants.FIRST_LINE)));
		if (response.isMaitai()) {
			jo.put("label", response.getLabel());
		} else {
			jo.put("label",
					CommonUtil.convertNull(addressV40Map
							.get(com.telenav.cserver.backend.datatypes.AddressFormatConstants.FIRST_LINE)));
		}
		jo.put("lastLine",
				CommonUtil.convertNull(addressV40Map
						.get(com.telenav.cserver.backend.datatypes.AddressFormatConstants.LAST_LINE)));
		jo.put("city",
				AddressDataConverter.convertNull(geoCodedAddress.getCity()));
		jo.put("state",
				AddressDataConverter.convertNull(geoCodedAddress.getState()));
		jo.put("zip", AddressDataConverter.convertNull(geoCodedAddress
				.getPostalCode()));
		jo.put("street1", AddressDataConverter.convertNull(geoCodedAddress
				.getStreetName()));
		jo.put("street2", AddressDataConverter.convertNull(geoCodedAddress
				.getCrossStreetName()));
		jo.put("county",
				AddressDataConverter.convertNull(geoCodedAddress.getCounty()));

		jo.put("lat", AddressDataConverter.convertToDM5(geoCodedAddress.getLatitude()));
		jo.put("lon", AddressDataConverter.convertToDM5(geoCodedAddress.getLongitude()));
		jo.put("country",
				AddressDataConverter.convertNull(geoCodedAddress.getCountry()));

		jo.put("suite",
				AddressDataConverter.convertNull(geoCodedAddress.getSuite()));
		jo.put("sublocality", AddressDataConverter.convertNull(geoCodedAddress
				.getSublocality()));
		jo.put("locality",
				AddressDataConverter.convertNull(geoCodedAddress.getLocality()));
		jo.put("locale",
				AddressDataConverter.convertNull(geoCodedAddress.getLocale()));
		jo.put("subStreet", AddressDataConverter.convertNull(geoCodedAddress
				.getSubStreet()));
		jo.put("buildingName", AddressDataConverter.convertNull(geoCodedAddress
				.getBuildingName()));
		jo.put("addressId", AddressDataConverter.convertNull(geoCodedAddress
				.getAddressId()));

		jo.put("isGeocoded", 1);

		jo.put("type", 1);
		if (geoCodedAddress.getSubStatus() != null) {
			jo.put("isStreetChanged", geoCodedAddress.getSubStatus()
					.isSubstatus_STREET_CHANGED() ? 1 : 0);
			jo.put("isCityChanged", geoCodedAddress.getSubStatus()
					.isSubstatus_CITY_CHANGED() ? 1 : 0);
		}

		// ACE 4.0
		jo.put("streetName", AddressDataConverter.convertNull(geoCodedAddress.getStreetName()));
		jo.put("houseNumber", AddressDataConverter.convertNull(geoCodedAddress.getDoor()));
		return jo;
	}
}
