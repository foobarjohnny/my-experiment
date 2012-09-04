package com.telenav.cserver;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.json.me.JSONException;
import org.json.me.JSONObject;
import com.telenav.j2me.datatypes.Stop;
import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.executor.ExecutorContext;
import com.telenav.cserver.framework.html.datatype.HtmlClientInfo;
import com.telenav.cserver.framework.html.util.HtmlClientInfoFactory;
import com.telenav.cserver.framework.html.util.HtmlCommonUtil;
import com.telenav.cserver.html.util.HtmlConstants;
import com.telenav.kernel.util.datatypes.TnContext;
import com.telenav.ws.datatypes.address.GeoCode;
import com.telenav.ws.datatypes.address.Location;

public class TestUtil {
	private static final double DM5 = 100000;

	public static ExecutorContext getExecutorContext() {
		ExecutorContext context = new ExecutorContext();
		TnContext tnContext = new TnContext();
		tnContext.addProperty(TnContext.PROP_CARRIER, "ATT");
		tnContext.addProperty(TnContext.PROP_DEVICE, "8900");
		tnContext.addProperty(TnContext.PROP_PRODUCT, "RIM");
		tnContext.addProperty(TnContext.PROP_VERSION, "6.0.01");
		tnContext.addProperty("application", "ATT_NAV");
		tnContext.addProperty("login", "4085057537");
		tnContext.addProperty("userid", "9826225");
		context.setTnContext(tnContext);

		return context;
	}
	
	public static ExecutorContext getExecutorContextForTN64(String carrier) {
		ExecutorContext context = new ExecutorContext();
		TnContext tnContext = new TnContext();
		tnContext.addProperty(TnContext.PROP_CARRIER, carrier);
		tnContext.addProperty(TnContext.PROP_DEVICE, "8530");
		tnContext.addProperty(TnContext.PROP_PRODUCT, "RIM");
		tnContext.addProperty(TnContext.PROP_VERSION, "6.4.01");
		tnContext.addProperty("application", "TN");
		tnContext.addProperty("login", "4085057537");
		tnContext.addProperty("userid", "9826225");
		context.setTnContext(tnContext);

		return context;
	}	
	
	public static ExecutorContext getExecutorContextFor7x() {
		ExecutorContext context = new ExecutorContext();
		TnContext tnContext = new TnContext();
		tnContext.addProperty(TnContext.PROP_CARRIER, "SprintPCS");
		tnContext.addProperty(TnContext.PROP_DEVICE, "8530");
		tnContext.addProperty(TnContext.PROP_PRODUCT, "ANDROID");
		tnContext.addProperty(TnContext.PROP_VERSION, "7.1.01");
		tnContext.addProperty("program_code", "SNNAVPROG");
		tnContext.addProperty("application", "SN_prem");
		tnContext.addProperty("login", "4085057537");
		tnContext.addProperty("userid", "9826225");
		context.setTnContext(tnContext);

		return context;
	}

	public static UserProfile getUserProfile() {
		UserProfile userProfile = new UserProfile();
		userProfile.setMin("4085057537");
		userProfile.setPassword("7537");

		userProfile.setVersion("6.0.01");
		userProfile.setPlatform("RIM");
		userProfile.setDevice("8900");
		userProfile.setLocale("en_US");
		userProfile.setCarrier("ATT");
		userProfile.setProduct("ATT");
		userProfile.setRegion("");
		userProfile.setScreenWidth("480");
		userProfile.setScreenHeight("360");
		userProfile.setProgramCode("ATT");
		userProfile.setAudioFormat("amr");
		userProfile.setUserId("9826225");

		return userProfile;
	}
	
	public static UserProfile getUserProfileForTN64(String carrier) {
		UserProfile userProfile = new UserProfile();
		userProfile.setMin("4085057537");
		userProfile.setPassword("7537");

		userProfile.setVersion("6.4.01");
		userProfile.setPlatform("RIM");
		userProfile.setDevice("8530");
		userProfile.setLocale("en_IN");
		userProfile.setCarrier(carrier);
		userProfile.setProduct("TN");
		userProfile.setRegion("");
		userProfile.setScreenWidth("320");
		userProfile.setScreenHeight("240");
		userProfile.setProgramCode("TN");
		userProfile.setAudioFormat("amr");
		userProfile.setUserId("9826225");

		return userProfile;
	}

	public static Location getLocation() {
		Location loc = new Location();
		GeoCode geoCode = new GeoCode();
		geoCode.setLatitude(3737469 / DM5);
		geoCode.setLongitude(-12199586 / DM5);
		loc.setGeoCode(geoCode);
		return loc;
	}

	public static JSONObject getLocationJSON() throws JSONException {
		JSONObject searchLocationJson = new JSONObject();
		searchLocationJson.put("label", "");
		searchLocationJson.put("firstLine", "3755 EL CAMINO REAL");
		searchLocationJson.put("zip", "95051");
		searchLocationJson.put("state", "CA");
		searchLocationJson.put("country", "US");
		searchLocationJson.put("city", "SANTA CLARA");
		searchLocationJson.put("lat", 3735237);
		searchLocationJson.put("lon", -12199984);

		return searchLocationJson;
	}
	
	public static JSONObject getLocationJSONObject(){
		JSONObject addressinfo = new JSONObject();
		try {
			addressinfo.put("city", "Sunnyvale");
			addressinfo.put("state", "Ca");
			addressinfo.put("country", "US");
			addressinfo.put("lat", 3737391);
			addressinfo.put("lon", -12199926);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return addressinfo;
	}
	
	public static Stop getStop(){
		Stop stop = new Stop();
		stop.city = "Sunnyvale";
		stop.firstLine = "1130 kifer rd";
		stop.state = "Ca";
		stop.zip = "84056";
		stop.country = "US";
		stop.lat = 3737391;
		stop.lon = -12199926;
		
		return stop;
	}
	
	public static HtmlClientInfo getHtmlClientInfo(){
		HtmlClientInfo htmlClientInfo = new HtmlClientInfo();
		String clientInfoString = HtmlCommonUtil
				.filterLastPara("{\"programCode\":\"SNNAVPROG\",\"deviceCarrier\":\"SprintPCS\",\"platform\":\"ANDROID\",\"version\":\"7.1.0\",\"productType\":\"SN_NAV\",\"device\":\"genericTest1\",\"locale\":\"en_US\",\"buildNumber\":\"1010\"}");
		try {
			clientInfoString = URLDecoder.decode(clientInfoString, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String clientWidth = "480";
		String clientHeight = "800";
		
		htmlClientInfo = HtmlClientInfoFactory.getInstance().build(clientInfoString,clientWidth,clientHeight,"");
		return htmlClientInfo;
	}
}
