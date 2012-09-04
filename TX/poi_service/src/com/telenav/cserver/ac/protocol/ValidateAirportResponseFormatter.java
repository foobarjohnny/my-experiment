package com.telenav.cserver.ac.protocol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.telenav.cserver.ac.executor.ValidateAirportResponse;
import com.telenav.cserver.backend.datatypes.TnPoi;
import com.telenav.cserver.browser.framework.protocol.BrowserProtocolResponseFormatter;
import com.telenav.cserver.entity.AirportPoi;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.cserver.backend.datatypes.Address;

public class ValidateAirportResponseFormatter extends
        BrowserProtocolResponseFormatter {
	
	private Logger logger = Logger.getLogger(ValidateAddressResponseFormatter.class);
    public static final double DEGREE_MULTIPLIER = 1.e5; // 1e-5 deg units

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
                JSONObject jo = this.convertAddressToJSONObject(ap);
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

    public  JSONObject convertAddressToJSONObject(AirportPoi ap) {
        JSONObject jo = new JSONObject();
        try {
            Address addr = ap.getAddress();
            jo.put("firstLine", ap.getPoi().getBrandName());//convertNull(street.getFirstLine()));
            jo.put("label", ap.getPoi().getFeatureName());
            jo.put("city", convertNull(addr.getCityName()));
            jo.put("state", convertNull(addr.getState()));
            jo.put("zip", convertNull(addr.getPostalCode()));
            jo.put("lat", convertToDM5(addr.getLatitude()));
            jo.put("lon",  convertToDM5(addr.getLongitude()));
            //jo.put("label", addr.getLabel());
            jo.put("country", convertNull(addr.getCountry()));
            jo.put("isGeocoded",true);
            String airportMsg = ap.getPoi().getFeatureName() + ": " + ap.getPoi().getBrandName();
            jo.put("airportMsg", airportMsg);
        } catch (JSONException e) {
        	logger.error("cause:"+e.getCause()+",message:"+e.getMessage());
			e.printStackTrace();
        }
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
