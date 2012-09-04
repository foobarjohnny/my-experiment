package com.telenav.cserver.dsr.ds;

import java.util.HashMap;
import java.util.Map;

import com.telenav.audio.recognition.ds.Stop;
import com.telenav.cserver.dsr.util.MatchingUtil;

/**
 * User: llzhang
 * Date: 2009-5-18
 * Time: 15:32:30
 */
public class RecResult {
    private int id;
    private String value;
    private double confidence;
    private Stop stop;
    private Map<String, String> slots = new HashMap<String, String>();


    public static String SLOT_CITY = "city";
    public static String SLOT_STATE = "state";
    public static String SLOT_STREET_NUM = "snum";
    public static String SLOT_STREET_NAME = "sname";
    public static String SLOT_COMMAND_ACTION = "action";
    public static String SLOT_COMMAND_NAME = "name";
    public static String SLOT_COMMAND_TARGET = "target";
    public static String SLOT_COMMAND_DAY = "day";
    public static String SLOT_ADDRESS = "address";
    public static String SLOT_AIRPORT = "airport";
    public static String SLOT_COMMAND_MYSTUFF = "mystuff";
    public static String SLOT_COMMAND_FAVORITE = "favoriteid";
    public static String SLOT_FAVORITE_USE_ADDRESS = "use_address";
    public static String SLOT_FAVORITE_USE_LABEL = "use_label";
    public static String SLOT_COMMAND_LAT = "lat";
    public static String SLOT_COMMAND_LON = "lon";
    public static String SLOT_STREET = "street";
    public static String SLOT_XSTREET = "xstreet";
    public static String SLOT_POI = "name";
    public static String SLOT_CURRENT_LOC = "use_current_location";
    public static String SLOT_TRANSACTION_ID = "transactionId";
    public static String SLOT_TYPE = "type" ;
    public static String SLOT_APPLICATION = "application" ;
    public static String SLOT_ACTION = "action" ;
    public static String SLOT_DIGIT_TEXT = "digitText" ;
    public static String SLOT_DIGITS = "digits" ;
    public static String SLOT_LANDMARK_CITY = "landmark_city" ;
    public static String SLOT_LANDMARK_STATE = "landmark_state" ;
    public static String SLOT_CURRENT_CITY = "current_city" ;
    public static String SLOT_CURRENT_STATE = "current_state" ;
    public static String SLOT_SPOKEN_TERM = "spoken_term" ;
    public static String SLOT_AIRPORT_CODE = "airport_code" ;
    public static String SLOT_SEARCH_RESULT = "search_result" ;
    public static String SLOT_INDEX = "index";
    
    
    public static String ADDRESS_TYPE = "address" ;
    public static String POI_TYPE = "poi" ;
    public static String STREETS_TYPE = "streets" ;
    public static String XSTREET_TYPE = "xstreet" ;
    
    // TODO change this back to maps once the client supports it
    public static String APP_MAPS = "poi" ;

//    public RecResult(int id, String value, int confidence, String property) {
//		super();
//		this.id = id;
//		this.value = value;
//		this.confidence = confidence;
//		this.property = property;
//	}

    public RecResult()
    {
    	this.slots = new HashMap<String, String>() ;
    }
    
    public RecResult(int id, String value, int confidence, Stop stop, Map<String, String> slots) {
        super();
        this.id = id;
        this.value = value;
        this.confidence = confidence;
        this.stop = stop;
        this.slots = slots;
    }
    @Override
    public boolean equals(Object other){
    	if(this == other){
    		return true;
    	}
    	if(!(other instanceof RecResult)){
    		return false;
    	}
    	RecResult o = (RecResult)other;
    	if(this.id != o.id){
    		return false;
    	}
    	if(!MatchingUtil.equalStr(this.value, o.value)){
    		return false;
    	}
    	if(!MatchingUtil.isDuplicateStop(this.stop, o.stop)){
    		return false;
    	}
    	if(!this.slots.equals(o.slots)){
    		return false;
    	}
    	return true;
    }
    
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(" [ id=").append(id).append(" value=\"").append(value).append("\"");
        for (String key : slots.keySet()) {
            buffer.append(" ").append(key).append("=\"").append(slots.get(key)).append("\"");
        }
        buffer.append(" confidence=").append(confidence).append(" ] ");
        return buffer.toString();
    }

//    public String formattedLiteral(int recType) {
//        switch (recType) {
//            case Constants.TYPE_CITY_STATE:
//                String formattedCity = Pattern.compile("_").matcher(slots.get(SLOT_CITY)).replaceAll(" ");
//                return formattedCity + "," + slots.get(SLOT_STATE).toUpperCase();
//            case Constants.TYPE_STREET_ADDRESS:
//                return slots.get(SLOT_STREET_NUM) + " " + slots.get(SLOT_STREET_NAME).toUpperCase();
//            case Constants.TYPE_STREET:
//                return slots.get(SLOT_STREET_NAME).toUpperCase();
//            case Constants.TYPE_XSTREET:
//                return slots.get(SLOT_STREET_NAME).toUpperCase();
//            case Constants.TYPE_COMMAND_CONTROL:
//            {
//            	String action = slots.get(SLOT_COMMAND_ACTION).toUpperCase();
//            	String value = slots.get(SLOT_COMMAND_NAME);
//            	if(value != null){
//            		return action + "" + value.toUpperCase();
//            	}else{
//            		return action;
//            	}
//            }
//            default:
//                return value;
//        }
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }
    
    /**
	 * @return the stop
	 */
	public Stop getStop() {
		return stop;
	}

	/**
	 * @param stop the stop to set
	 */
	public void setStop(Stop stop) {
		this.stop = stop;
	}

	public Map<String, String> getSlots() {
        return slots;
    }

    public void setSlots(Map<String, String> slots) {
        this.slots = slots;
    }
    
    public void setSlot(String key, String value) {
        this.slots.put(key, value);
    }

    public String getSlot(String key){
    	return getSlot(key, "");
    }
    
    public String removeSlot(String key){
    	return slots.remove(key);
    }
    
    public String getSlot(String key, String defaultValue){
        String value = slots.get(key);
        return value == null ? defaultValue : value.trim();
    }
}
