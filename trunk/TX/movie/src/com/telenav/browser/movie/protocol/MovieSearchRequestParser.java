package com.telenav.browser.movie.protocol;

import javax.servlet.http.HttpServletRequest;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.telenav.browser.movie.datatypes.Address;
import com.telenav.browser.movie.executor.MovieSearchRequest;
import com.telenav.cserver.browser.framework.BrowserFrameworkConstants;
import com.telenav.cserver.browser.framework.protocol.BrowserProtocolRequestParser;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;
import com.telenav.browser.movie.Constant.RRKey;

public class MovieSearchRequestParser extends BrowserProtocolRequestParser{

    public String getExecutorType() {
        return "movieSearch";
    }

    public ExecutorRequest parseBrowserRequest(HttpServletRequest httpRequest) throws Exception {
        
    	MovieSearchRequest request = new MovieSearchRequest();
    	// Get the JSON request.
        DataHandler handler = (DataHandler) httpRequest.getAttribute(BrowserFrameworkConstants.CLIENT_INFO);
        TxNode body = handler.getAJAXBody();
        String joString = body.msgAt(0);
        JSONObject jo = new JSONObject(joString);
        
        // SearchString
        String searchString = jo.getString(RRKey.MS_INPUT_STRING);
        request.setSearchString(searchString);
        
        // Date index from now, 0 - means Today
       	String searchDate = jo.getString(RRKey.MS_DATE_INDEX);
       	request.setSearchDate(searchDate);
        
    	int dUnit = jo.getInt(RRKey.MS_DISTANCE_UNIT);
    	request.setDistanceUnit(dUnit);
    	
        try{
        	int idx = jo.getInt(RRKey.MS_BATCH_NUMBER);
        	request.setBatchNumber(idx);
        	idx = jo.getInt(RRKey.MS_BATCH_SIZE);
        	request.setBatchSize(idx);
        	boolean sortByName = jo.getBoolean(RRKey.MS_SORT_BY_NAME);
        	request.setSortByName(sortByName);
        	String newSortBy = jo.optString(RRKey.MS_NEW_SORT_BY);
        	request.setNewSortBy(newSortBy);
        }catch(JSONException ex){// not suppose to throw exception... take default values
        }
        
        String addressStr = jo.getString(RRKey.MS_ADDRESS);
        JSONObject addressJ = new JSONObject(addressStr);
        Address address = new Address();
        address.makeFrom(addressJ);
        request.setAddress(address);
        
    	return request;
    }
}
