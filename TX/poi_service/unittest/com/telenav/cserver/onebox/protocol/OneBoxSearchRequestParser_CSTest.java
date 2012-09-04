package com.telenav.cserver.onebox.protocol;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telenav.cserver.framework.executor.ExecutorException;
import com.telenav.j2me.datatypes.TxNode;
import com.telenav.protocol.constants.NodeTypeDefinitions;

public class OneBoxSearchRequestParser_CSTest extends OneBoxSearchRequestParser_CS{

	TxNode oneBoxTxNode = null;
	@Before
	public void setUp() throws Exception {
		/*
		  	{
		  		"inputString":"fake tn 4 near sunnyvale,ca","transactionId":"1326204933850","sponsorListingNumber":2,"searchTypeStr":"5",
		  		"from":"type","currentPage":"0","distanceUnit":1,"addressString":"{\"zip\":\"\",\"lon\":-12199983,\"state\":\"\",
		  		\"firstLine\":\"\",\"label\":\"\",\"type\":6,\"lat\":3737453,\"country\":\"\",\"city\":\"\"}","maxResults":9
		 	}
		 */
		
		oneBoxTxNode = new TxNode();
		oneBoxTxNode.addMsg("oneExecutor");
		oneBoxTxNode.addMsg("5");
		
		oneBoxTxNode.addValue(0);
		oneBoxTxNode.addValue(9);
		oneBoxTxNode.addValue(1);
		oneBoxTxNode.addValue(1);
		
		TxNode child = new TxNode();
		oneBoxTxNode.addChild(child);
		child.addValue(NodeTypeDefinitions.TYPE_ONE_BOX_ROUTE_NODE);
		child.addValue(617792720);  // route id
		child.addValue(0); // segment id
		child.addValue(1); //edge id
		child.addValue(0); // shape point id
		child.addValue(35);  // range
		child.addValue(3737392);   // current latitude
		child.addValue(-12199919);  // current longitude
		child.addValue(0);  // search along type
		
		child = new TxNode();
		oneBoxTxNode.addChild(child);
        child.addValue(NodeTypeDefinitions.TYPE_ONE_BOX_ADDRESS_SHARING_ORGIN_NODE);
		child.addValue(3738587);
        child.addValue(-12210461);
        child.addMsg("sunnyvale");
        child.addMsg("CA");
        child.addMsg("94303");

		
		child = new TxNode();
		oneBoxTxNode.addChild(child);
		child.addValue(NodeTypeDefinitions.TYPE_ONE_BOX_ADDRESS_SHARING_DEST_NODE);
		child.addValue(3738587);
        child.addValue(-12210461);
        child.addMsg("sunnyvale");
        child.addMsg("CA");
        child.addMsg("94303");

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testParse() throws ExecutorException {

		this.parse(oneBoxTxNode);
	}

}
