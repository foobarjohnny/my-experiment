package com.telenav.cserver.poidetail.protocol.v20;

import org.json.me.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.framework.executor.ExecutorException;

public class POIDetailsRequestParserTest extends POIDetailsRequestParser{
	
	JSONObject json = new JSONObject();
	
	@Before
	public void setUp() throws Exception {
		json.put("PoiId", 1234);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testParse() throws ExecutorException {
		this.parse(json);
	}

}
