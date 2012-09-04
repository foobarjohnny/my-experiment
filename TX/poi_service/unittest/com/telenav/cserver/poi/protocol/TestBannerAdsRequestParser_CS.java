package com.telenav.cserver.poi.protocol;

import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.j2me.datatypes.TxNode;

import junit.framework.TestCase;

public class TestBannerAdsRequestParser_CS extends TestCase {
	public void testParse() {
		BannerAdsRequestParser_CS requestParser = new BannerAdsRequestParser_CS();
		try {
			requestParser.parse(getObjectNode());
		} catch (ExecutorException e) {
			e.printStackTrace();
		}
	}
	
	private TxNode getObjectNode(){
		TxNode node = new TxNode();
		node.addMsg("");
		node.addMsg("");
		node.addMsg("");
		node.addMsg("");
		node.addValue(0);
		node.addValue(3737391);
		node.addValue(-12199926);
		node.addValue(6);
		node.addValue(0);
		node.addValue(480);
		node.addValue(800);
		return node;
	}
}
