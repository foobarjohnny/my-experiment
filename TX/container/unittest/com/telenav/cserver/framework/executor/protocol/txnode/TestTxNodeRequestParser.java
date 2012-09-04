/**
 * (c) Copyright 2011 TeleNav.
 * All Rights Reserved.
 */
package com.telenav.cserver.framework.executor.protocol.txnode;

import static com.telenav.cserver.matcher.MatchBox.txNodeEquals;
import static org.easymock.EasyMock.expect;

import java.lang.reflect.Method;
import java.util.List;

import junit.framework.TestCase;

import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import com.telenav.cserver.framework.UserProfile;
import com.telenav.cserver.framework.executor.ExecutorRequest;
import com.telenav.cserver.framework.executor.protocol.ProtocolRequestParser;
import com.telenav.j2me.datatypes.GpsData;
import com.telenav.j2me.datatypes.TxNode;
/**
 * TestTxNodeRequestParser.java
 *
 * xljiang@telenav.cn
 * @version 1.0 2011-4-26
 */
@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor("com.telenav.cserver.framework.executor.protocol.txnode.ExecutorDataFactory")
@PrepareForTest(ExecutorDataFactory.class)
public class TestTxNodeRequestParser extends TestCase{
	
	private TxNodeRequestParser txNodeRequestParser = new TxNodeRequestParser();
	private ExecutorRequest[] executorRequests = new ExecutorRequest[1];
	
	private ProtocolRequestParser protocolRequestParser;
	private ExecutorDataFactory executorDataFactory  ;
	@Override
	protected void setUp() throws Exception {
		protocolRequestParser = PowerMock.createMock(ProtocolRequestParser.class);
		executorDataFactory  = PowerMock.createMock(ExecutorDataFactory.class);
		executorRequests[0] = new ExecutorRequest();
	}
	public void testParse() throws Exception{
		//mock object
		PowerMock.mockStatic(ExecutorDataFactory.class);
		expect(ExecutorDataFactory.getInstance()).andReturn(executorDataFactory);
		expect(executorDataFactory.createProtocolRequestParser("0")).andReturn(protocolRequestParser);
		TxNode root = createNewTxNode("0",0);
		expect(protocolRequestParser.parse(txNodeEquals(root.childAt(0)))).andReturn(executorRequests);
		
		PowerMock.replayAll();
		//call method
		ExecutorRequest[] results = txNodeRequestParser.parse(TxNode.toByteArray(root));
		
		PowerMock.verifyAll();
		//assert
		assertEquals(1,results.length);
		assertEquals("0",results[0].getExecutorType());
		assertNull(results[0].getUserProfile().getDevice());
		assertNull(results[0].getGpsData());
	}
	/**
	 * @param nodeType   nodeType equals node.msgAt(0)
	 * @param value
	 * @return
	 */
	private TxNode createNewTxNode(String nodeType,long value){
		TxNode root = new TxNode();
		TxNode child = new TxNode();
		if(nodeType!=null && !nodeType.equals("")){
			child.addMsg(nodeType);
		}else{
			child.addMsg("0");
		}
		child.addMsg("1");
		child.addMsg("2");
		child.addMsg("3");
		child.addMsg("4");
		child.addMsg("5");
		child.addMsg("6");
		child.addMsg("204");
		child.addMsg("8");
		child.addMsg("9");
		child.addValue(value);
		root.addChild(child);
		return root;
	}
	public void testCreateGpsData()throws Exception{
		Method method = TxNodeRequestParser.class.getDeclaredMethod("createGpsData", TxNode.class);
        method.setAccessible(true);
        
        List<GpsData> gpsData = (List<GpsData>)method.invoke(txNodeRequestParser, createNewTxNode("",0));
        assertNotNull(gpsData);
	}
	
	public void testCreateUserProfile_USER_INFO()throws Exception{
		UserProfile up = txNodeRequestParser.createUserProfile(createNewTxNode(null,0x56));
		assertEquals("0",up.getMin());
		assertEquals("1",up.getPassword());
		assertEquals("2",up.getUserId());
		assertEquals("3",up.getEqPin());
		assertEquals("4",up.getLocale());
		assertEquals("5",up.getRegion());
		assertEquals("6",up.getSsoToken());
		assertEquals("male",up.getGuideTone());
	}
	
	public void testCreateUserProfile_CLIENT_VERSION()throws Exception{
		UserProfile up = txNodeRequestParser.createUserProfile(createNewTxNode("",0x52));
		assertEquals("0",up.getCarrier());
		assertEquals("1",up.getPlatform());
		assertEquals("2",up.getVersion());
		assertEquals("3",up.getDevice());
		assertEquals("4",up.getBuildNumber());
		assertEquals("5",up.getGpsType());
		assertEquals("6",up.getProduct());
		assertEquals("204",up.getMapDpi());
		assertEquals("8",up.getOSVersion());
	}
	
	public void testCreateUserProfile_USER_PREFS()throws Exception{
		UserProfile up = txNodeRequestParser.createUserProfile(createNewTxNode("",0x53));
		assertEquals("0",up.getAudioFormat());
		assertEquals("1",up.getImageType());
		assertEquals("2",up.getAudioLevel());
		assertEquals("3",up.getDataProcessType());
		assertEquals("4",up.getScreenWidth());
		assertEquals("5",up.getScreenHeight());
	}
	
	public void testGetGuideToneName() throws Exception{
        Method method = TxNodeRequestParser.class.getDeclaredMethod("getGuideToneName", String.class);
        method.setAccessible(true);
        assertEquals("202",method.invoke(txNodeRequestParser, "202"));
        assertEquals(null, method.invoke(txNodeRequestParser, "203"));
        assertEquals("male",method.invoke(txNodeRequestParser, "204"));
        assertEquals("yes",method.invoke(txNodeRequestParser, "1,yes"));
        assertEquals("202",method.invoke(txNodeRequestParser, "202,"));
        assertEquals("abc,4",method.invoke(txNodeRequestParser, "11,abc,4"));
        assertEquals(null,method.invoke(txNodeRequestParser, "11,"));
        assertEquals(null,method.invoke(txNodeRequestParser, ","));
        assertEquals(null,method.invoke(txNodeRequestParser, ""));
        assertEquals(null,method.invoke(txNodeRequestParser, new Object[]{null}));
	}

}
