package com.telenav.cserver.onebox.protocol;

import com.telenav.cserver.browser.framework.protocol.BrowserProtocolResponseFormatter;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.onebox.executor.OneBoxResponse;
import com.telenav.cserver.poi.protocol.PoiBrowserProtocolResponseFormatter;
import com.telenav.cserver.poi.struts.Constant;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.services.search.onebox.v10.QuerySuggestion;
import com.telenav.ws.datatypes.address.Address;

import org.apache.log4j.Logger;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

public class OneBoxResponseFormatter extends BrowserProtocolResponseFormatter {

	private Logger logger = Logger.getLogger(OneBoxResponseFormatter.class);
	
	public void parseBrowserResponse(HttpServletRequest httpRequest,
			ExecutorResponse executorResponse) throws Exception {
		OneBoxResponse oneBoxRsp = (OneBoxResponse) executorResponse;
		logger.info("oneboxsearch resulttype:" + oneBoxRsp.getResultType());
		switch (oneBoxRsp.getResultType()) {
		case Constant.OneBox.ADDRESS_RESULT:
			logger.info("oneboxsearch resulttype is address result");
			formatAddress(httpRequest, oneBoxRsp);
			break;
		case Constant.OneBox.POI_RESULT:
			logger.info("oneboxsearch resulttype is poi result");
			PoiBrowserProtocolResponseFormatter formater = new PoiBrowserProtocolResponseFormatter();
			formater.parseBrowserResponse(httpRequest, oneBoxRsp.getPoiResp());
			break;
		case Constant.OneBox.DID_YOU_MEAN:
			logger.info("oneboxsearch resulttype is did you mean result");
			formatDidYouMean(httpRequest, oneBoxRsp);
			break;
		default:
			//return error result
			logger.info("oneboxsearch resulttype is unexpected, so throw exception");
			TxNode node = new TxNode();
			JSONArray joList = new JSONArray();
			node.addMsg(joList.toString());
			httpRequest.setAttribute("node", node);
		}

	}

	/**
	 * @param httpRequest
	 * @param oneBoxRsp
	 * @throws JSONException
	 */
	private void formatDidYouMean(HttpServletRequest httpRequest,
			OneBoxResponse oneBoxRsp) throws JSONException {
		TxNode node = new TxNode();
		QuerySuggestion[] suggestions = oneBoxRsp.getSuggestions();
		JSONArray sugArray = new JSONArray();
		for (QuerySuggestion entry : suggestions) {
			JSONObject obj = new JSONObject();
			obj.put("display", entry.getDisplayLabel());
			obj.put("search", entry.getQuery());
			sugArray.put(obj);
		}
		String nodeString = sugArray.toString();
		node.addMsg(nodeString);
		node.addValue(1);
		node.addValue(Constant.OneBox.DID_YOU_MEAN);
		httpRequest.setAttribute("node", node);
	}

	/**
	 * @param httpRequest
	 * @param oneBoxRsp
	 * @see com.telenav.cserver.ac.protocol.ValidateAddressResponseFormatterACEWS
	 */
	private void formatAddress(HttpServletRequest httpRequest,
			OneBoxResponse oneBoxRsp) {
		JSONArray addressJSONArray = formatAddress(oneBoxRsp.getAddressList());
		TxNode node = new TxNode();
		if (addressJSONArray.length() > 0) {
			node.addValue(1);
			node.addMsg(addressJSONArray.toString());
		} else {
			node.addValue(0);
		}
		node.addValue(Constant.OneBox.ADDRESS_RESULT);
		if(OneBoxResponse.STATUS_NOT_EXACT_MATCH.equalsIgnoreCase(oneBoxRsp.getExactMatchStatus())){
			node.addValue(1);
		}else{
			node.addValue(0);
		}
		httpRequest.setAttribute("node", node);
	}


	private JSONArray formatAddress(List<Address> addressList) {
		JSONArray array = new JSONArray();
		for (Address addr : addressList) {
			array.put(address2JSON(addr));
		}
		return array;
	}

	public static JSONObject address2JSON(Address address) {
		try {
			JSONObject jo = new JSONObject();
			jo.put("firstLine", convertNull(address.getFirstLine()));

			jo.put("label", convertNull(address.getFirstLine()));
			jo.put("city", convertNull(address.getCity()));
			jo.put("state", convertNull(address.getState()));
			jo.put("zip", convertNull(address.getPostalCode()));
			jo.put("lat", convertToDM5(address.getGeoCode().getLatitude()));
			jo.put("lon", convertToDM5(address.getGeoCode().getLongitude()));
			jo.put("country", convertNull(address.getCountry()));
			jo.put("isGeocoded", 1);
			// TODO
			jo.put("type", 1);
			return jo;
		} catch (JSONException je) {
			return null;
		}
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

	public static final double DEGREE_MULTIPLIER = 1.e5; // 1e-5 deg units

}
