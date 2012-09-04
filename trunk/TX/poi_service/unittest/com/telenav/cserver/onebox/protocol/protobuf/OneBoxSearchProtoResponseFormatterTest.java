package com.telenav.cserver.onebox.protocol.protobuf;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.cserver.framework.executor.ExecutorResponse;
import com.telenav.cserver.onebox.executor.OneBoxResponse;
import com.telenav.cserver.poi.executor.POISearchResponse_WS;
import com.telenav.j2me.datatypes.ProtocolBuffer;
import com.telenav.services.search.onebox.v10.QuerySuggestion;
import com.telenav.ws.datatypes.address.Address;
import com.telenav.ws.datatypes.address.GeoCode;

public class OneBoxSearchProtoResponseFormatterTest extends OneBoxSearchProtoResponseFormatter{

	ProtocolBuffer formatTarget = new ProtocolBuffer();
	OneBoxResponse resp = new OneBoxResponse();
	ExecutorResponse[] responses = {resp};
	
	@Before
	public void setUp() throws Exception {
		resp.setStatus(ExecutorResponse.STATUS_OK);
		resp.setErrorMessage("errorMsg");
		resp.setExactMatchStatus("");
		
		POISearchResponse_WS poi = new POISearchResponse_WS();
		poi.setTotalCount(10);
		resp.setPoiResp(poi);
		
		QuerySuggestion suggestion = new QuerySuggestion();
		suggestion.setDisplayLabel("label");
		suggestion.setQuery("query");
		QuerySuggestion[] suggestions = {suggestion};
		resp.setSuggestions(suggestions);
		

		Address addr = new Address();
		ArrayList<Address> addressList = new ArrayList<Address>();
		addressList.add(addr);
		resp.setAddressList(addressList);
		
		addr.setCity("sunnyvale");
		addr.setState("CA");
		addr.setLabel("label");
		addr.setCountry("USA");
		addr.setPostalCode("94086");
		addr.setFirstLine("1130, kifer");
		GeoCode geoCode = new GeoCode();
		addr.setGeoCode(geoCode);
		geoCode.setLatitude(3737392);
		geoCode.setLongitude(-12199919);
		
		    	
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFormat() throws ExecutorException {
		this.format(formatTarget, responses);
	}

}
