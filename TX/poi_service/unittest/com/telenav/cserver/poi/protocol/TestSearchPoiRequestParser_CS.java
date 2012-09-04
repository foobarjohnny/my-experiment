package com.telenav.cserver.poi.protocol;

import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.protocol.constants.NodeTypeDefinitions;

import junit.framework.TestCase;

public class TestSearchPoiRequestParser_CS extends TestCase{
	public void testParse() throws ExecutorException {
		TxNode node = new TxNode();
		node.addMsg("");
		node.addMsg("");
		node.addValue(1);
		node.addValue(1);
		node.addValue(3);
		node.addValue(1);
		node.addValue(1);
		node.addValue(1);
		node.addValue(1);
		node.addValue(1);
		node.addValue(1);
		
		TxNode childNode = new TxNode();
		childNode.addValue(NodeTypeDefinitions.TYPE_ONE_BOX_ROUTE_NODE);
		childNode.addValue(1);
		childNode.addValue(1);
		childNode.addValue(3);
		childNode.addValue(1);
		childNode.addValue(1);
		childNode.addValue(1);
		childNode.addValue(1);
		childNode.addValue(1);
		childNode.addValue(1);
		
		node.addChild(childNode);
		
		SearchPoiRequestParser_CS requestPaeser = new SearchPoiRequestParser_CS();
		requestPaeser.parse(node);
	}
}
