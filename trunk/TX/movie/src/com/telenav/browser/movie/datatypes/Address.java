package com.telenav.browser.movie.datatypes;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.telenav.j2me.datatypes.Stop;

public class Address extends Stop {
	public static final String FIRST_LINE = "firstLine"; 
	public static final String STREET1 = "street1"; 
	public static final String STREET2 = "street2"; 
	public static final String CITY = "city"; 
	public static final String STATE = "state"; 
	public static final String ZIP = "zip"; 
	public static final String COUNTRY = "country";
	public static final String LAT = "lat";
	public static final String LON = "lon";
	public static final String TYPE = "type";
	
	public void makeFrom(JSONObject address) throws JSONException {
		
		if (address.has(STREET1)){
			firstLine = address.getString(STREET1);
			if (address.has(STREET2)) firstLine = firstLine + " " + address.getString(STREET2);
			if (address.has(FIRST_LINE)) label = address.getString(FIRST_LINE);
		}
		else if (address.has(FIRST_LINE)) firstLine = address.getString(FIRST_LINE);
		
		if (address.has(CITY)) city = address.getString(CITY);
		if (address.has(STATE)) state = address.getString(STATE);
		if (address.has(ZIP)) zip = address.getString(ZIP);
		if (address.has(COUNTRY)) country = address.getString(COUNTRY);
		
		try{
			// either both ok or just invalid pair
			if (address.has(LAT) && address.has(LON)) {
				lat = address.getInt(LAT);
				lon = address.getInt(LON);
				stopType = STOP_CURRENT_LOCATION;
			}
		}catch(JSONException ex){}
	}
	
}
