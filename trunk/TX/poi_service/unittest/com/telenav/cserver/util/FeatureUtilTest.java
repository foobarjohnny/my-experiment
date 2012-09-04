package com.telenav.cserver.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.util.helper.log2txnode.TxNode2Request;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.tnbrowser.util.DataHandler;

public class FeatureUtilTest extends FeatureUtil {

	MockHttpServletRequest request = null;
	DataHandler handler = null;
	
	@Before
	public void setUp() throws Exception {
		
		TxNode txNode = TestUtil.getMandantoryNode();
		byte[] TxNodeBinary = TxNode.toByteArray(txNode);
		request = new MockHttpServletRequest(TxNodeBinary);
		TxNode2Request.getInstance(txNode).toMockHttpServletRequest(request);
		handler = new DataHandler(request, true);

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testIsSupportWeather() {
		FeatureUtil.isSupportWeather(handler);
	}

	@Test
	public void testIsSupportDSR() {
		FeatureUtil.isSupportDSR(handler);
	}

	@Test
	public void testIsSupportFindOutMore() {
		FeatureUtil.isSupportFindOutMore(handler);
	}

	@Test
	public void testIsSupportEmailSupport() {
		FeatureUtil.isSupportEmailSupport(handler);
	}

	@Test
	public void testIsSupportCallSupport() {
		FeatureUtil.isSupportCallSupport(handler);
	}

}
