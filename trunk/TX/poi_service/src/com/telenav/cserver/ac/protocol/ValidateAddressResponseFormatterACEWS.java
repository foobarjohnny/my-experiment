package com.telenav.cserver.ac.protocol;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.telenav.cserver.ac.executor.ValidateAddressResponseACEWS;
import com.telenav.cserver.backend.datatypes.ace.GeoCodedAddress;
import com.telenav.cserver.browser.framework.protocol.BrowserProtocolResponseFormatter;
import com.telenav.cserver.framework.executor.ExecutorResponse;
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
                try {
                    ja.put(convertAddressToJSONObject(geoCodedAddress, response));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                	logger.error("error occured when converting geoCodedAddress to JSONObject," +
                			"the geoCodedAddress is "+geoCodedAddress.toString());
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

    public static JSONObject convertAddressToJSONObject(
            GeoCodedAddress geoCodedAddress, ValidateAddressResponseACEWS response) throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("firstLine", convertNull(geoCodedAddress.getFirstLine()));
        if(response.isMaitai()) {
            jo.put("label", response.getLabel());
        } else {
            jo.put("label", convertNull(geoCodedAddress.getFirstLine()));
        }        
        jo.put("city", convertNull(geoCodedAddress.getCityName()));
        jo.put("state", convertNull(geoCodedAddress.getState()));
        jo.put("zip", convertNull(geoCodedAddress.getPostalCode()));
        jo.put("lat", convertToDM5(geoCodedAddress.getLatitude()));
        jo.put("lon", convertToDM5(geoCodedAddress.getLongitude()));
        jo.put("country", convertNull(geoCodedAddress.getCountry()));
        jo.put("isGeocoded", 1);
        // TODO
        jo.put("type", 1);
        jo.put("isStreetChanged",geoCodedAddress.getSubStatus().isSubstatus_STREET_CHANGED()? 1:0);
        jo.put("isCityChanged",geoCodedAddress.getSubStatus().isSubstatus_CITY_CHANGED()? 1:0);
        return jo;
    }

    private static String convertNull(String s) {
        if (null == s) {
            s = "";
        }
        return s;
    }

    public static int convertToDM5(double degree) {
        return (int) (degree * DEGREE_MULTIPLIER);
    }
}
